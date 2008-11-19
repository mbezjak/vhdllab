package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.client.core.SystemContext;
import hr.fer.zemris.vhdllab.client.core.prefs.UserPreferences;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.entities.UserFileInfo;
import hr.fer.zemris.vhdllab.service.Compiler;
import hr.fer.zemris.vhdllab.service.FileService;
import hr.fer.zemris.vhdllab.service.HierarchyExtractor;
import hr.fer.zemris.vhdllab.service.LibraryFileService;
import hr.fer.zemris.vhdllab.service.ProjectService;
import hr.fer.zemris.vhdllab.service.Simulator;
import hr.fer.zemris.vhdllab.service.UserFileService;
import hr.fer.zemris.vhdllab.service.filetype.CircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.filetype.VhdlGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

public class Communicator implements ICommunicator {

    @Resource(name = "fileService")
    private FileService fileService;
    @Resource(name = "projectService")
    private ProjectService projectService;
    @Resource(name = "libraryFileService")
    private LibraryFileService libraryFileService;
    @Resource(name = "userFileService")
    private UserFileService userFileService;
    @Resource(name = "hierarchyExtractor")
    private HierarchyExtractor hierarchyExtractor;
    @Resource(name = "circuitInterfaceExtractor")
    private CircuitInterfaceExtractor circuitInterfaceExtractor;
    @Resource(name = "vhdlGenerator")
    private VhdlGenerator vhdlGenerator;
    @Resource(name = "compiler")
    private Compiler compiler;
    @Resource(name = "simulator")
    private Simulator simulator;

    private Cache cache;
    private Caseless userId;

    public Communicator() {
        cache = new Cache();
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.applets.main.ICommunicator#init()
     */
    public void init() throws UniformAppletException {
        userId = new Caseless(SystemContext.instance().getUserId());
        loadUserPreferences();
        // FIXME TMP SOLUTION
        loadFileIds();
    }

    private void loadFileIds() throws UniformAppletException {
        /*
         * this is a fix for loadFileContent method. it breaks when system
         * container tries to open editors (stored in a session). because
         * critical methods: getAllProjects and findFilesByProject was still not
         * called nothing is cached so loadFileContent method assumes that such
         * file does not exist.
         */
        for (Caseless projectName : getAllProjects()) {
            for (Caseless fileName : findFilesByProject(projectName)) {
                // this is just extra it has nothing to do with the actual bug
                loadFileType(projectName, fileName);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.applets.main.ICommunicator#dispose()
     */
    public void dispose() {
        UserPreferences preferences = UserPreferences.instance();
        List<UserFileInfo> files = new ArrayList<UserFileInfo>();
        for (String key : preferences.keys()) {
            String property = preferences.get(key, null);
            if (property == null) {
                continue;
            }
            UserFileInfo file = cache.getUserFile(new Caseless(key));
            files.add(file);
        }
        userFileService.save(files);
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.applets.main.ICommunicator#getAllProjects()
     */
    public List<Caseless> getAllProjects() {
        List<ProjectInfo> projects = projectService.findByUser(userId);

        List<Caseless> projectNames = new ArrayList<Caseless>();
        for (ProjectInfo p : projects) {
            Caseless projectName = cache.getProjectForIdentifier(p);
            if (projectName == null) {
                Caseless name = p.getName();
                cache.cacheItem(name, p);
                projectName = cache.getProjectForIdentifier(p);
            }
            projectNames.add(projectName);
        }
        return projectNames;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#getAllProjects(hr.fer
     * .zemris.vhdllab.entities.Caseless)
     */
    public List<Caseless> getAllProjects(Caseless userId) {
        List<ProjectInfo> projects = projectService.findByUser(userId);

        List<Caseless> projectNames = new ArrayList<Caseless>();
        for (ProjectInfo p : projects) {
            Caseless projectName = cache.getProjectForIdentifier(p);
            if (projectName == null) {
                Caseless name = p.getName();
                cache.cacheItem(name, p);
                projectName = cache.getProjectForIdentifier(p);
            }
            projectNames.add(projectName);
        }
        return projectNames;
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.applets.main.ICommunicator#isSuperuser()
     */
    public boolean isSuperuser() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#findFilesByProject(hr
     * .fer.zemris.vhdllab.entities.Caseless)
     */
    public List<Caseless> findFilesByProject(Caseless projectName)
            throws UniformAppletException {
        if (projectName == null) {
            throw new NullPointerException("Project name can not be null.");
        }
        Integer projectIdentifier = cache.getIdentifierFor(projectName);
        if (projectIdentifier == null) {
            throw new UniformAppletException("Project does not exists!");
        }
        List<FileInfo> files = fileService.findByProject(projectIdentifier);

        List<Caseless> fileNames = new ArrayList<Caseless>();
        for (FileInfo f : files) {
            FileIdentifier identifier = cache.getFileForIdentifier(f);
            if (identifier == null) {
                Caseless name = f.getName();
                cache.cacheItem(projectName, name, f);
                identifier = cache.getFileForIdentifier(f);
            }
            fileNames.add(identifier.getFileName());
        }
        return fileNames;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#existsFile(hr.fer.zemris
     * .vhdllab.entities.Caseless, hr.fer.zemris.vhdllab.entities.Caseless)
     */
    public boolean existsFile(Caseless projectName, Caseless fileName)
            throws UniformAppletException {
        if (projectName == null)
            throw new NullPointerException("Project name can not be null.");
        if (fileName == null)
            throw new NullPointerException("File name can not be null.");
        Integer projectIdentifier = cache.getIdentifierFor(projectName);
        if (projectIdentifier == null)
            throw new UniformAppletException("Project does not exists!");
        /* return invoker.existsFile(projectIdentifier, fileName); */
        return cache.containsIdentifierFor(projectName, fileName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#existsProject(hr.fer
     * .zemris.vhdllab.entities.Caseless)
     */
    public boolean existsProject(Caseless projectName) {
        if (projectName == null)
            throw new NullPointerException("Project name can not be null.");
        /*
         * Integer projectIdentifier = cache.getIdentifierFor(projectName);
         * if(projectIdentifier == null) return false; return
         * invoker.existsProject(projectIdentifier);
         */
        return cache.containsIdentifierFor(projectName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#deleteFile(hr.fer.zemris
     * .vhdllab.entities.Caseless, hr.fer.zemris.vhdllab.entities.Caseless)
     */
    public void deleteFile(Caseless projectName, Caseless fileName)
            throws UniformAppletException {
        if (projectName == null)
            throw new NullPointerException("Project name can not be null.");
        if (fileName == null)
            throw new NullPointerException("File name can not be null.");
        Integer fileIdentifier = cache.getIdentifierFor(projectName, fileName);
        if (fileIdentifier == null)
            throw new UniformAppletException("File does not exists!");
        fileService.delete(cache.getFile(fileIdentifier));
        cache.removeItem(projectName, fileName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#deleteProject(hr.fer
     * .zemris.vhdllab.entities.Caseless)
     */
    public void deleteProject(Caseless projectName)
            throws UniformAppletException {
        if (projectName == null)
            throw new NullPointerException("Project name can not be null.");
        Integer projectIdentifier = cache.getIdentifierFor(projectName);
        if (projectIdentifier == null)
            throw new UniformAppletException("File does not exists!");
        projectService.delete(cache.getProject(projectIdentifier));
        cache.removeItem(projectName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#createProject(hr.fer
     * .zemris.vhdllab.entities.Caseless)
     */
    public void createProject(Caseless projectName) {
        if (projectName == null)
            throw new NullPointerException("Project name can not be null.");
        ProjectInfo savedProject = projectService.save(new ProjectInfo(userId,
                projectName));
        cache.cacheItem(projectName, savedProject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#createFile(hr.fer.zemris
     * .vhdllab.entities.Caseless, hr.fer.zemris.vhdllab.entities.Caseless,
     * hr.fer.zemris.vhdllab.entities.FileType, java.lang.String)
     */
    public void createFile(Caseless projectName, Caseless fileName,
            FileType type, String data) throws UniformAppletException {
        if (projectName == null)
            throw new NullPointerException("Project name can not be null.");
        if (fileName == null)
            throw new NullPointerException("File name can not be null.");
        if (type == null)
            throw new NullPointerException("File type can not be null.");
        Integer projectIdentifier = cache.getIdentifierFor(projectName);
        if (projectIdentifier == null)
            throw new UniformAppletException("Project does not exists!");
        FileInfo savedFile = fileService.save(new FileInfo(type, fileName,
                data, projectIdentifier));
        cache.cacheItem(projectName, fileName, savedFile);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#saveFile(hr.fer.zemris
     * .vhdllab.entities.Caseless, hr.fer.zemris.vhdllab.entities.Caseless,
     * java.lang.String)
     */
    public void saveFile(Caseless projectName, Caseless fileName, String content)
            throws UniformAppletException {
        if (projectName == null)
            throw new NullPointerException("Project name can not be null.");
        if (fileName == null)
            throw new NullPointerException("File name can not be null.");
        if (content == null)
            throw new NullPointerException("File content can not be null.");
        Integer fileIdentifier = cache.getIdentifierFor(projectName, fileName);
        if (fileIdentifier == null)
            throw new UniformAppletException("File does not exists!");
        FileInfo file = cache.getFile(fileIdentifier);
        file.setData(content);
        FileInfo savedFile = fileService.save(file);
        cache.cacheFile(savedFile);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#loadFileContent(hr.fer
     * .zemris.vhdllab.entities.Caseless,
     * hr.fer.zemris.vhdllab.entities.Caseless)
     */
    public String loadFileContent(Caseless projectName, Caseless fileName) {
        if (projectName == null)
            throw new NullPointerException("Project name can not be null.");
        if (fileName == null)
            throw new NullPointerException("File name can not be null.");
        Integer fileIdentifier = cache.getIdentifierFor(projectName, fileName);
        if (fileIdentifier == null) {
            // throw new UniformAppletException("File (" + fileName + "/"
            // + projectName + ") does not exists!");
            return loadPredefinedFileContent(fileName);
        }
        return cache.getFile(fileIdentifier).getData();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#loadPredefinedFileContent
     * (hr.fer.zemris.vhdllab.entities.Caseless)
     */
    public String loadPredefinedFileContent(Caseless fileName) {
        if (fileName == null)
            throw new NullPointerException("File name can not be null.");
        return libraryFileService.findPredefinedByName(fileName).getData();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#loadFileType(hr.fer.
     * zemris.vhdllab.entities.Caseless,
     * hr.fer.zemris.vhdllab.entities.Caseless)
     */
    public FileType loadFileType(Caseless projectName, Caseless fileName) {
        if (projectName == null)
            throw new NullPointerException("Project name can not be null.");
        if (fileName == null)
            throw new NullPointerException("File name can not be null.");
        Integer fileIdentifier = cache.getIdentifierFor(projectName, fileName);
        if (fileIdentifier == null) {
            return FileType.PREDEFINED;
            // throw new UniformAppletException("File does not exists!");
        }
        return cache.getFile(fileIdentifier).getType();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#extractHierarchy(hr.
     * fer.zemris.vhdllab.entities.Caseless)
     */
    public Hierarchy extractHierarchy(Caseless projectName)
            throws UniformAppletException {
        if (projectName == null)
            throw new NullPointerException("Project name can not be null.");
        Integer projectIdentifier = cache.getIdentifierFor(projectName);
        if (projectIdentifier == null)
            throw new UniformAppletException("Project does not exists!");

        Hierarchy h = hierarchyExtractor.extract(cache
                .getProject(projectIdentifier));
        for (Caseless fileName : getAllForHierarchy(h)) {
            Integer id = cache.getIdentifierFor(projectName, fileName);
            if (id == null) {
                FileInfo file = fileService.findByName(projectIdentifier,
                        fileName);
                if (file != null) {
                    cache.cacheItem(projectName, fileName, file);
                }
            }
        }
        return h;
    }

    private Set<Caseless> getAllForHierarchy(Hierarchy h) {
        Set<Caseless> names = new HashSet<Caseless>(h.fileCount());
        for (Caseless f : h.getTopLevelFiles()) {
            names.add(f);
            names.addAll(h.getAllDependenciesForFile(f));
        }
        return names;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#generateVHDL(hr.fer.
     * zemris.vhdllab.entities.Caseless,
     * hr.fer.zemris.vhdllab.entities.Caseless)
     */
    public VHDLGenerationResult generateVHDL(Caseless projectName,
            Caseless fileName) throws UniformAppletException {
        if (projectName == null)
            throw new NullPointerException("Project name can not be null.");
        if (fileName == null)
            throw new NullPointerException("File name can not be null.");
        Integer fileIdentifier = cache.getIdentifierFor(projectName, fileName);
        if (fileIdentifier == null)
            throw new UniformAppletException("File does not exists!");
        return vhdlGenerator.generate(cache.getFile(fileIdentifier));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#compile(hr.fer.zemris
     * .vhdllab.entities.Caseless, hr.fer.zemris.vhdllab.entities.Caseless)
     */
    public CompilationResult compile(Caseless projectName, Caseless fileName)
            throws UniformAppletException {
        if (projectName == null)
            throw new NullPointerException("Project name can not be null.");
        if (fileName == null)
            throw new NullPointerException("File name can not be null.");
        Integer fileIdentifier = cache.getIdentifierFor(projectName, fileName);
        if (fileIdentifier == null)
            throw new UniformAppletException("File does not exists!");
        return compiler.compile(cache.getFile(fileIdentifier));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#runSimulation(hr.fer
     * .zemris.vhdllab.entities.Caseless,
     * hr.fer.zemris.vhdllab.entities.Caseless)
     */
    public SimulationResult runSimulation(Caseless projectName,
            Caseless fileName) throws UniformAppletException {
        if (projectName == null)
            throw new NullPointerException("Project name can not be null.");
        if (fileName == null)
            throw new NullPointerException("File name can not be null.");
        Integer fileIdentifier = cache.getIdentifierFor(projectName, fileName);
        if (fileIdentifier == null)
            throw new UniformAppletException("File does not exists!");
        return simulator.simulate(cache.getFile(fileIdentifier));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#getCircuitInterfaceFor
     * (hr.fer.zemris.vhdllab.entities.Caseless,
     * hr.fer.zemris.vhdllab.entities.Caseless)
     */
    public CircuitInterface getCircuitInterfaceFor(Caseless projectName,
            Caseless fileName) throws UniformAppletException {
        if (projectName == null)
            throw new NullPointerException("Project name can not be null.");
        if (fileName == null)
            throw new NullPointerException("File name can not be null.");
        Integer fileIdentifier = cache.getIdentifierFor(projectName, fileName);
        if (fileIdentifier == null)
            throw new UniformAppletException("File does not exists!");
        return circuitInterfaceExtractor.extract(cache.getFile(fileIdentifier));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#saveErrorMessage(java
     * .lang.String)
     */
    public void saveErrorMessage(String content) {
        if (content == null)
            throw new NullPointerException("Content can not be null.");
        libraryFileService.saveClientLog(content);
    }

    private void loadUserPreferences() {
        Properties properties = new Properties();
        List<UserFileInfo> files = userFileService.findByUser(userId);
        for (UserFileInfo f : files) {
            Caseless name = f.getName();
            cache.cacheUserFileItem(name, f);
            String data = f.getData();
            properties.setProperty(name.toString(), data);
        }
        UserPreferences.instance().init(properties);
    }

}