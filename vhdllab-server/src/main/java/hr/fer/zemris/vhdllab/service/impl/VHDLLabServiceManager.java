package hr.fer.zemris.vhdllab.service.impl;

import static hr.fer.zemris.vhdllab.api.StatusCodes.INTERNAL_SERVER_ERROR;
import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.api.StatusCodes;
import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.hierarchy.HierarchyNode;
import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.server.conf.FileTypeMapping;
import hr.fer.zemris.vhdllab.server.conf.FunctionalityType;
import hr.fer.zemris.vhdllab.server.conf.ServerConf;
import hr.fer.zemris.vhdllab.server.conf.ServerConfParser;
import hr.fer.zemris.vhdllab.service.FileManager;
import hr.fer.zemris.vhdllab.service.Functionality;
import hr.fer.zemris.vhdllab.service.ServiceContainer;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.ServiceManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * A default implementation of {@link ServiceManager}.
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class VHDLLabServiceManager implements ServiceManager {

    /**
     * A log instance.
     */
    private static final Logger log = Logger
            .getLogger(VHDLLabServiceManager.class);

    /**
     * A default length of a various reports in characters.
     *
     * @see #reportFunctionality(FunctionalityType, String, Object, long)
     */
    private static final int REPORT_LENGTH = 1000;

    private final Map<String, Map<FunctionalityType, Functionality<?>>> functionalities;

    /**
     * Default constructor.
     *
     * @throws IllegalStateException
     *             if classes associated with file types can't be instantiated
     *             (e.g. specified generator class name could not be found)
     */
    public VHDLLabServiceManager() {
        functionalities = new HashMap<String, Map<FunctionalityType, Functionality<?>>>();
        ServerConf conf = ServerConfParser.getConfiguration();
        Properties properties = conf.getProperties();
        for (String type : conf.getFileTypes()) {
            FileTypeMapping m = conf.getFileTypeMapping(type);
            for (FunctionalityType t : FunctionalityType.values()) {
                String className = m.getFunctionality(t);
                if (className != null) {
                    Functionality<?> func = configureFunctionality(className,
                            properties);
                    Map<FunctionalityType, Functionality<?>> map = functionalities
                            .get(type);
                    if (map == null) {
                        map = new HashMap<FunctionalityType, Functionality<?>>();
                        functionalities.put(type, map);
                    }
                    map.put(t, func);
                    if (log.isDebugEnabled()) {
                        StringBuilder sb = new StringBuilder(40);
                        sb.append("Instantiated ").append(t);
                        sb.append(" functionality ").append(className);
                        sb.append(" for file type: ").append(type);
                        log.debug(sb.toString());
                    }
                } else {
                    if (log.isDebugEnabled()) {
                        StringBuilder sb = new StringBuilder(40);
                        sb.append(t);
                        sb.append(" functionality not defined for file type: ");
                        sb.append(type);
                        log.debug(sb.toString());
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see hr.fer.zemris.vhdllab.service.ServiceManager#generateVHDL(hr.fer.zemris.vhdllab.entities.File)
     */
    @Override
    public VHDLGenerationResult generateVHDL(File file) throws ServiceException {
        return executeFunctionality(file, FunctionalityType.GENERATOR);
    }

    /*
     * (non-Javadoc)
     *
     * @see hr.fer.zemris.vhdllab.service.ServiceManager#extractCircuitInterface(hr.fer.zemris.vhdllab.entities.File)
     */
    @Override
    public CircuitInterface extractCircuitInterface(File file)
            throws ServiceException {
        return executeFunctionality(file, FunctionalityType.EXTRACTOR);
    }

    /*
     * (non-Javadoc)
     *
     * @see hr.fer.zemris.vhdllab.service.ServiceManager#extractDependencies(hr.fer.zemris.vhdllab.entities.File,
     *      boolean)
     */
    @Override
    public Set<String> extractDependencies(File file, boolean includeTransitive)
            throws ServiceException {
        if (!includeTransitive) {
            return executeFunctionality(file, FunctionalityType.DEPENDENCY);
        }
        return extractAllDependencies(file);
    }

    /**
     * Retrieves all dependencies (including transitive) for given file.
     * Returned value can never be <code>null</code> although it can be empty
     * collection.
     *
     * @param file
     *            a file for whom to retrieve all dependencies
     * @return all dependencies (including transitive) for given file
     * @throws ServiceException
     *             if exceptional condition occurs
     */
    private Set<String> extractAllDependencies(File file)
            throws ServiceException {
        /*
         * This method extracts all dependencies by recursively invoking
         * #extract(File, boolean) method for every first level dependency
         * found.
         */
        FileManager fileMan = ServiceContainer.instance().getFileManager();
        Set<String> visitedFiles = new HashSet<String>();
        List<String> notYetAnalyzedFiles = new LinkedList<String>();
        notYetAnalyzedFiles.add(file.getName());
        visitedFiles.add(file.getName());
        while (!notYetAnalyzedFiles.isEmpty()) {
            String name = notYetAnalyzedFiles.remove(0);
            Long projectId = file.getProject().getId();
            Set<String> dependancies;
            if (fileMan.exists(projectId, name)) {
                File f = fileMan.findByName(projectId, name);
                dependancies = extractDependencies(f, false);
            } else {
                // else a predefined file and they don't have dependencies
                dependancies = Collections.emptySet();
            }
            for (String dependancy : dependancies) {
                if (visitedFiles.contains(dependancy)) {
                    continue;
                }
                notYetAnalyzedFiles.add(dependancy);
                visitedFiles.add(dependancy);
            }
        }
        if (log.isDebugEnabled()) {
            StringBuilder message = new StringBuilder(
                    20 + visitedFiles.size() * 50);
            message.append("Extracted dependencies for file:");
            message.append(file.toString()).append(" {\n");
            for (String n : visitedFiles) {
                message.append(n).append("\n");
            }
            message.append("}");
            log.debug(message.toString());
        }
        visitedFiles.remove(file.getName());
        return Collections.unmodifiableSet(visitedFiles);
    }

    /*
     * (non-Javadoc)
     *
     * @see hr.fer.zemris.vhdllab.service.ServiceManager#extractHierarchy(hr.fer.zemris.vhdllab.entities.Project)
     */
    @Override
    public Hierarchy extractHierarchy(Project project) throws ServiceException {
        Set<File> files = project.getFiles();
        Map<String, HierarchyNode> resolvedNodes = new HashMap<String, HierarchyNode>(
                files.size());
        for (File file : files) {
            String name = file.getName();
            if (resolvedNodes.containsKey(name.toLowerCase())) {
                continue;
            }
            HierarchyNode node = new HierarchyNode(name, file.getType(), null);
            resolvedNodes.put(name.toLowerCase(), node);
            resolveHierarchy(file, resolvedNodes);
        }
        Hierarchy hierarchy = new Hierarchy(project.getName(),
                new HashSet<HierarchyNode>(resolvedNodes.values()));
        return hierarchy;
    }

    /**
     * Resolves a hierarchy tree for specified file. Resolved hierarchy trees
     * gets added to <code>resolvedNodes</code> parameter.
     *
     * @param nodeFile
     *            a file for whom to resolve hierarchy tree
     * @param resolvedNodes
     *            contains hierarchy tree for all resolved files
     * @throws ServiceException
     *             if exceptional condition occurs
     */
    private void resolveHierarchy(File nodeFile,
            Map<String, HierarchyNode> resolvedNodes) throws ServiceException {
        /*
         * This method resolves a hierarchy by recursively invoking itself.
         */
        Set<String> dependencies;
        try {
            dependencies = extractDependencies(nodeFile, false);
        } catch (ServiceException e) {
            dependencies = Collections.emptySet();
        }
        FileManager fileMan = ServiceContainer.instance().getFileManager();
        Long projectId = nodeFile.getProject().getId();
        for (String name : dependencies) {
            HierarchyNode parent = resolvedNodes.get(nodeFile.getName()
                    .toLowerCase());
            if (resolvedNodes.containsKey(name)) {
                HierarchyNode depNode = resolvedNodes.get(name);
                parent.addDependency(depNode);
            } else {
                File dep = null;
                String type;
                if (fileMan.exists(projectId, name)) {
                    dep = fileMan.findByName(projectId, name);
                    type = dep.getType();
                } else {
                    // else its a predefined file
                    type = FileTypes.VHDL_PREDEFINED;
                }
                HierarchyNode depNode = new HierarchyNode(name, type, parent);
                resolvedNodes.put(name.toLowerCase(), depNode);
                if (dep != null) {
                    // predefined files don't have dependencies
                    resolveHierarchy(dep, resolvedNodes);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see hr.fer.zemris.vhdllab.service.ServiceManager#compile(hr.fer.zemris.vhdllab.entities.File)
     */
    @Override
    public CompilationResult compile(File file) throws ServiceException {
        return executeFunctionality(file, FunctionalityType.COMPILER);
    }

    /* (non-Javadoc)
     * @see hr.fer.zemris.vhdllab.service.ServiceManager#simulate(hr.fer.zemris.vhdllab.entities.File)
     */
    @Override
    public SimulationResult simulate(File file) throws ServiceException {
        return executeFunctionality(file, FunctionalityType.SIMULATOR);
    }
    
    /**
     * Executes functionality for specified <code>file</code> and returns a
     * result of execution.
     *
     * @param <T>
     *            result type of a functionality execution
     * @param file
     *            a file for whom to execute specified functionality
     * @param funcType
     *            a functionality to be executed
     * @return result of an execution
     * @throws NullPointerException
     *             if <code>file</code> is <code>null</code>
     * @throws ServiceException
     *             if functionality throws exception during execution or
     *             returned <code>null</code> result
     */
    private <T> T executeFunctionality(File file, FunctionalityType funcType)
            throws ServiceException {
        Functionality<T> functionality = getFunctionality(file.getType(),
                funcType);
        String className = functionality.getClass().getCanonicalName();
        T result;
        long start = System.currentTimeMillis();
        try {
            result = functionality.execute(file);
        } catch (RuntimeException e) {
            String message = className + " threw exception during execution!";
            log.error(message, e);
            throw new ServiceException(INTERNAL_SERVER_ERROR, message);
        }
        long end = System.currentTimeMillis();
        if (result == null) {
            String message = funcType + " functionality " + className
                    + " returned null result!";
            log.error(message);
            throw new ServiceException(INTERNAL_SERVER_ERROR, message);
        }
        reportFunctionality(funcType, className, result, end - start);
        return result;
    }

    /**
     * Returns an object implementing specified functionality for a file type.
     * Return value will never be <code>null</code>.
     *
     * @param <T>
     *            result type of a functionality execution
     * @param fileType
     *            a file type for whom to return object implementing specified
     *            functionality
     * @param funcType
     *            a functionality that returned object is implementing
     * @return an object implementing specified functionality for a file type
     * @throws ServiceException
     *             if such object doesn't exist
     */
    @SuppressWarnings("unchecked")
    private <T> Functionality<T> getFunctionality(String fileType,
            FunctionalityType funcType) throws ServiceException {
        Map<FunctionalityType, Functionality<?>> map = functionalities
                .get(fileType);
        if (map == null) {
            String message = "No functionalities (at all) for file type: "
                    + fileType;
            log.error(message);
            throw new ServiceException(StatusCodes.SERVER_ERROR, message);
        }
        Functionality<T> func = (Functionality<T>) map.get(funcType);
        if (func == null) {
            StringBuilder sb = new StringBuilder(40);
            sb.append("No functionality ").append(funcType);
            sb.append(" defined for file type: ").append(fileType);
            throw new ServiceException(INTERNAL_SERVER_ERROR, sb.toString());
        }
        return func;
    }

    /**
     * If debugging is enabled then this method will log an execution of
     * specified functionality.
     *
     * @param funcType
     *            a type of a functionality that was executed
     * @param className
     *            a functionality class name
     * @param result
     *            a result of an execution
     * @param length
     *            a time in milliseconds that execution took
     */
    private void reportFunctionality(FunctionalityType funcType,
            String className, Object result, long length) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder(REPORT_LENGTH);
            sb.append(funcType).append(" functionality ").append(className);
            sb.append(" finished execution in ");
            sb.append(length).append("ms:\n").append(result);
            sb.append("\n-----------------------------------");
            log.debug(sb.toString());
        }
    }

    /**
     * Instantiates and configured specified functionality by invoking default
     * constructor and {@link Functionality#configure(Properties)} method.
     * Return value will never be <code>null</code>.
     *
     * @param className
     *            a class name of a functionality
     * @param properties
     *            properties for functionality configuration
     * @return configured functionality
     * @throws IllegalStateException
     *             if exceptional condition occurs
     * @throws RuntimeException
     *             if functionality threw exception during configuration
     */
    private Functionality<?> configureFunctionality(String className,
            Properties properties) {
        Functionality<?> functionality = null;
        try {
            functionality = (Functionality<?>) Class.forName(className)
                    .newInstance();
        } catch (InstantiationException e) {
            String message = className + " couldn't be instantiated!";
            log.error(message, e);
            throw new IllegalStateException(message);
        } catch (IllegalAccessException e) {
            String message = className
                    + " doesn't have public default constructor!";
            log.error(message, e);
            throw new IllegalStateException(message);
        } catch (ClassNotFoundException e) {
            String message = "Class " + className + " doesn't exist!";
            log.error(message, e);
            throw new IllegalStateException(message);
        } catch (ClassCastException e) {
            String message = "Inappropriate class type!";
            log.error(message, e);
            throw new IllegalStateException(message);
        }
        functionality.configure(properties);
        return functionality;
    }

}
