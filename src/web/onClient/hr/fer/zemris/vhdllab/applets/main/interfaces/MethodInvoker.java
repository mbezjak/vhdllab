package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;

import java.util.List;

public interface MethodInvoker {

	String loadFileName(Long fileId) throws UniformAppletException;
	String loadFileType(Long fileId) throws UniformAppletException;
	String loadFileContent(Long fileId) throws UniformAppletException;
	Long loadFileProjectId(Long fileId) throws UniformAppletException;
	void saveFile(Long fileId, String content) throws UniformAppletException;
	void renameFile(Long fileId, String name) throws UniformAppletException;
	Long createFile(Long projectId, String name, String type) throws UniformAppletException;
	boolean existsFile(Long projectId, String name) throws UniformAppletException;
	void deleteFile(Long fileId) throws UniformAppletException;
	Long findFileByName(Long projectId, String name) throws UniformAppletException;
	List<Long> findFilesByProject(Long projectId) throws UniformAppletException;
	Long getProjectIdentifier(String ownerId, String projectName) throws UniformAppletException;

	String loadGlobalFileName(Long fileId) throws UniformAppletException;
	String loadGlobalFileType(Long fileId) throws UniformAppletException;
	String loadGlobalFileContent(Long fileId) throws UniformAppletException;
	void saveGlobalFile(Long fileId, String content) throws UniformAppletException;
	void renameGlobalFile(Long fileId, String name) throws UniformAppletException;
	Long createGlobalFile(String name, String type) throws UniformAppletException;
	boolean existsGlobalFile(Long fileId) throws UniformAppletException;
	boolean existsGlobalFile(String name) throws UniformAppletException;
	void deleteGlobalFile(Long fileId) throws UniformAppletException;
	List<Long> findGlobalFilesByType(String type) throws UniformAppletException;

	String loadUserFileOwnerId(Long fileId) throws UniformAppletException;
	String loadUserFileName(Long fileId) throws UniformAppletException;
	String loadUserFileType(Long fileId) throws UniformAppletException;
	String loadUserFileContent(Long fileId) throws UniformAppletException;
	void saveUserFile(Long fileId, String content) throws UniformAppletException;
	Long createUserFile(String ownerId, String name, String type) throws UniformAppletException;
	boolean existsUserFile(Long fileId) throws UniformAppletException;
	boolean existsUserFile(String ownerId, String fileName) throws UniformAppletException;
	void deleteUserFile(Long fileId) throws UniformAppletException;
	List<Long> findUserFilesByOwner(String ownerId) throws UniformAppletException;

	String loadProjectName(Long projectId) throws UniformAppletException;
	String loadProjectOwnerId(Long projectId) throws UniformAppletException;
	Long loadProjectNumberFiles(Long projectId) throws UniformAppletException;
	List<Long> loadProjectFilesId(Long projectId) throws UniformAppletException;
	void saveProject(Long projectId, List<Long> filesId) throws UniformAppletException;
	void renameProject(Long projectId, String name) throws UniformAppletException;
	Long createProject(String name, String ownerId) throws UniformAppletException;
	boolean existsProject(Long projectId) throws UniformAppletException;
	boolean existsProject(String ownerId, String projectName) throws UniformAppletException;
	void deleteProject(Long projectId) throws UniformAppletException;
	List<Long> findProjectsByUser(String ownerId) throws UniformAppletException;

	CompilationResult compileFile(Long fileId) throws UniformAppletException;
	SimulationResult runSimulation(Long fileId) throws UniformAppletException;
	String generateVHDL(Long fileId) throws UniformAppletException;
	CircuitInterface extractCircuitInterface(Long fileId) throws UniformAppletException;
	List<Long> extractDependencies(Long fileId) throws UniformAppletException;
	Hierarchy extractHierarchy(Long projectId) throws UniformAppletException;
	String loadPredefinedFileContent(String fileName) throws UniformAppletException;

}
