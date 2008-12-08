package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.client.core.prefs.UserPreferences;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.entities.UserFileInfo;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;
import hr.fer.zemris.vhdllab.platform.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.workspace.model.FileIdentifier;
import hr.fer.zemris.vhdllab.platform.workspace.model.ProjectIdentifier;
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
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Communicator extends AbstractEventPublisher<CommunicatorResourceListener> implements ICommunicator {

    @Autowired
    private FileService fileService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private LibraryFileService libraryFileService;
    @Autowired
    private UserFileService userFileService;
    @Autowired
    private HierarchyExtractor hierarchyExtractor;
    @Autowired
    private CircuitInterfaceExtractor circuitInterfaceExtractor;
    @Autowired
    private VhdlGenerator vhdlGenerator;
    @Autowired
    private Compiler compiler;
    @Autowired
    private Simulator simulator;
    @Autowired
    private WorkspaceManager workspaceManager;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;

    private Cache cache;

    public Communicator() {
        super(CommunicatorResourceListener.class);
        cache = new Cache();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.applets.main.ICommunicator#init()
     */
    public void init() throws UniformAppletException {
        loadUserPreferences();
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
    
    private ProjectIdentifier asIdentifier(Caseless projectName) {
        return new ProjectIdentifier(projectName);
    }
    
    private FileIdentifier asIdentifier(Caseless projectName, Caseless fileName) {
        return new FileIdentifier(projectName, fileName);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.applets.main.ICommunicator#getAllProjects()
     */
    public List<Caseless> getAllProjects() {
        List<ProjectInfo> projects = workspaceManager.getProjects();
        List<Caseless> names = new ArrayList<Caseless>(projects.size());
        for (ProjectInfo p : projects) {
            names.add(p.getName());
        }
        return names;
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
        List<FileInfo> files = workspaceManager.getFilesForProject(mapper.getProject(asIdentifier(projectName)));
        List<Caseless> names = new ArrayList<Caseless>(files.size());
        for (FileInfo f : files) {
            names.add(f.getName());
        }
        return names;
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
        List<Caseless> files = findFilesByProject(projectName);
        boolean found = false;
        for (Caseless name : files) {
            if(name.equals(fileName)) {
                found = true;
                break;
            }
        }
        return found;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.ICommunicator#existsProject(hr.fer
     * .zemris.vhdllab.entities.Caseless)
     */
    public boolean existsProject(Caseless projectName) {
        List<Caseless> projects = getAllProjects();
        boolean found = false;
        for (Caseless name : projects) {
            if(name.equals(projectName)) {
                found = true;
                break;
            }
        }
        return found;
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
        FileInfo file = mapper.getFile(asIdentifier(projectName, fileName));
        fileService.delete(file);
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
        ProjectInfo project = mapper.getProject(asIdentifier(projectName));
        projectService.delete(project);
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
        Caseless userId = ApplicationContextHolder.getContext().getUserId();
        ProjectInfo savedProject = projectService.save(new ProjectInfo(userId,
                projectName));
        fireProjectCreated(savedProject);
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
        Integer projectIdentifier = mapper.getProject(asIdentifier(projectName)).getId();
        if (projectIdentifier == null)
            throw new UniformAppletException("Project does not exists!");
        FileInfo savedFile = fileService.save(new FileInfo(type, fileName,
                data, projectIdentifier));
        ProjectInfo project = mapper.getProject(asIdentifier(projectName));
        Hierarchy hierarchy = hierarchyExtractor.extract(project);
        fireFileCreated(project, savedFile, hierarchy);
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
        FileInfo file = mapper.getFile(asIdentifier(projectName, fileName));
        file.setData(content);
        fileService.save(file);
        ProjectInfo project = mapper.getProject(asIdentifier(projectName));
        Hierarchy hierarchy = hierarchyExtractor.extract(project);
        fireFileSaved(project, file, hierarchy);
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
        FileInfo file = mapper.getFile(asIdentifier(projectName, fileName));
        if(file == null) {
            return loadPredefinedFileContent(fileName);
        }
        return file.getData();
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
        FileInfo file = mapper.getFile(asIdentifier(projectName, fileName));
        if(file == null) {
            return FileType.PREDEFINED;
        }
        return file.getType();
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
        return  workspaceManager.getHierarchy(asIdentifier(projectName));
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
        FileInfo file = mapper.getFile(asIdentifier(projectName, fileName));
        return vhdlGenerator.generate(file);
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
        FileInfo file = mapper.getFile(asIdentifier(projectName, fileName));
        return compiler.compile(file);
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
        FileInfo file = mapper.getFile(asIdentifier(projectName, fileName));
        return simulator.simulate(file);
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
        FileInfo file = mapper.getFile(asIdentifier(projectName, fileName));
        return circuitInterfaceExtractor.extract(file);
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
        List<UserFileInfo> files = userFileService.findByUser();
        for (UserFileInfo f : files) {
            Caseless name = f.getName();
            cache.cacheUserFileItem(name, f);
            String data = f.getData();
            properties.setProperty(name.toString(), data);
        }
        UserPreferences.instance().init(properties);
    }
    
    private void fireProjectCreated(ProjectInfo project) {
        for (CommunicatorResourceListener l : getListeners()) {
            l.projectCreated(project);
        }
    }

    private void fireFileCreated(ProjectInfo project, FileInfo file, Hierarchy hierarchy) {
        for (CommunicatorResourceListener l : getListeners()) {
            l.fileCreated(project, file, hierarchy);
        }
    }

    private void fireFileSaved(ProjectInfo project, FileInfo file, Hierarchy hierarchy) {
        for (CommunicatorResourceListener l : getListeners()) {
            l.fileSaved(project, file, hierarchy);
        }
    }
    
}