package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.manager.file.FileManager;
import hr.fer.zemris.vhdllab.platform.manager.project.ProjectManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.ProjectIdentifier;
import hr.fer.zemris.vhdllab.service.Compiler;
import hr.fer.zemris.vhdllab.service.LibraryFileService;
import hr.fer.zemris.vhdllab.service.Simulator;
import hr.fer.zemris.vhdllab.service.filetype.CircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.filetype.VhdlGenerator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Communicator implements ICommunicator {

    @Autowired
    private LibraryFileService libraryFileService;
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
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private FileManager fileManager;

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
        List<FileInfo> files = workspaceManager.getFilesForProject(mapper
                .getProject(asIdentifier(projectName)));
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
        return mapper.getFile(asIdentifier(projectName, fileName)) != null;
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
        fileManager.delete(file);
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
        projectManager.delete(project);
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
        Integer projectIdentifier = mapper
                .getProject(asIdentifier(projectName)).getId();
        FileInfo file = new FileInfo(type, fileName, data, projectIdentifier);
        fileManager.create(file);
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
        if (file == null) {
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
        return workspaceManager.getHierarchy(asIdentifier(projectName));
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
        libraryFileService.saveClientLog(content);
    }

}