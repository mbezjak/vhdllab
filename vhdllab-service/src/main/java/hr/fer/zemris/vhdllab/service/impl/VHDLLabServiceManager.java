package hr.fer.zemris.vhdllab.service.impl;

import static hr.fer.zemris.vhdllab.api.StatusCodes.INTERNAL_SERVER_ERROR;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.server.conf.FileTypeMapping;
import hr.fer.zemris.vhdllab.server.conf.FunctionalityType;
import hr.fer.zemris.vhdllab.server.conf.ServerConf;
import hr.fer.zemris.vhdllab.server.conf.ServerConfParser;
import hr.fer.zemris.vhdllab.service.Functionality;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.ServiceManager;

import java.util.HashMap;
import java.util.Map;

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
        for (String type : conf.getFileTypes()) {
            FileTypeMapping m = conf.getFileTypeMapping(type);
            for (FunctionalityType t : FunctionalityType.values()) {
                String className = m.getFunctionality(t);
                if (className != null) {
                    Functionality<?> func = instantiateClass(className);
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
     * Instantiates specified class by invoking default constructor. Return
     * value will never be <code>null</code>.
     *
     * @param className
     *            a name of a class to instantiate
     * @return an instantiated object of specified class
     * @throws IllegalStateException
     *             if exceptional condition occurs
     */
    @SuppressWarnings("unchecked")
    private <T> T instantiateClass(String className) {
        Object object = null;
        try {
            object = Class.forName(className).newInstance();
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
        }
        try {
            return (T) object;
        } catch (ClassCastException e) {
            String message = "Inappropriate class type!";
            log.error(message, e);
            throw new IllegalStateException(message);
        }
    }

}
