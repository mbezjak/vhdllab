package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;

import java.util.List;

public interface ICommunicator {

    List<Caseless> getAllProjects();

    List<Caseless> findFilesByProject(Caseless projectName)
            throws UniformAppletException;

    boolean existsFile(Caseless projectName, Caseless fileName)
            throws UniformAppletException;

    void deleteFile(Caseless projectName, Caseless fileName)
            throws UniformAppletException;

    void deleteProject(Caseless projectName) throws UniformAppletException;

    void createFile(Caseless projectName, Caseless fileName, FileType type,
            String data) throws UniformAppletException;

    FileType loadFileType(Caseless projectName, Caseless fileName);

    Hierarchy extractHierarchy(Caseless projectName)
            throws UniformAppletException;

    VHDLGenerationResult generateVHDL(Caseless projectName, Caseless fileName)
            throws UniformAppletException;

    CompilationResult compile(Caseless projectName, Caseless fileName)
            throws UniformAppletException;

    SimulationResult runSimulation(Caseless projectName, Caseless fileName)
            throws UniformAppletException;

    CircuitInterface getCircuitInterfaceFor(Caseless projectName,
            Caseless fileName) throws UniformAppletException;

    void saveErrorMessage(String content);

}