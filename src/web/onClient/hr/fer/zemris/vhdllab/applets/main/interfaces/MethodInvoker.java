package hr.fer.zemris.vhdllab.applets.main.interfaces;


import hr.fer.zemris.vhdllab.applets.main.AjaxException;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.util.List;

public interface MethodInvoker {

	String loadFileName(Long fileId) throws AjaxException;
	String loadFileType(Long fileId) throws AjaxException;
	String loadFileContent(Long fileId) throws AjaxException;
	Long loadFileProjectId(Long fileId) throws AjaxException;
	void saveFile(Long fileId, String content) throws AjaxException;
	void renameFile(Long fileId, String name) throws AjaxException;
	Long createFile(Long projectId, String name, String type) throws AjaxException;
	boolean existsFile(Long projectId, String name) throws AjaxException;
	void deleteFile(Long fileId) throws AjaxException;
	Long findFileByName(Long projectId, String name) throws AjaxException;
	List<Long> findFileByProject(Long projectId) throws AjaxException;

	String loadGlobalFileName(Long fileId) throws AjaxException;
	String loadGlobalFileType(Long fileId) throws AjaxException;
	String loadGlobalFileContent(Long fileId) throws AjaxException;
	void saveGlobalFile(Long fileId, String content) throws AjaxException;
	void renameGlobalFile(Long fileId, String name) throws AjaxException;
	Long createGlobalFile(String name, String type) throws AjaxException;
	boolean existsGlobalFile(Long fileId) throws AjaxException;
	void deleteGlobalFile(Long fileId) throws AjaxException;
	List<Long> findGlobalFilesByType(String type) throws AjaxException;

	String loadUserFileOwnerId(Long fileId) throws AjaxException;
	String loadUserFileType(Long fileId) throws AjaxException;
	String loadUserFileContent(Long fileId) throws AjaxException;
	void saveUserFile(Long fileId, String content) throws AjaxException;
	Long createUserFile(String ownerId, String type) throws AjaxException;
	boolean existsUserFile(Long fileId) throws AjaxException;
	void deleteUserFile(Long fileId) throws AjaxException;
	List<Long> findUserFilesByOwner(String ownerId) throws AjaxException;

	String loadProjectName(Long projectId) throws AjaxException;
	String loadProjectOwnerId(Long projectId) throws AjaxException;
	Long loadProjectNumberFiles(Long projectId) throws AjaxException;
	List<Long> loadProjectFilesId(Long projectId) throws AjaxException;
	void saveProject(Long projectId, List<Long> filesId) throws AjaxException;
	void renameProject(Long projectId, String name) throws AjaxException;
	Long createProject(String name, String ownerId) throws AjaxException;
	boolean existsProject(Long projectId) throws AjaxException;
	void deleteProject(Long projectId) throws AjaxException;
	List<Long> findProjectsByUser(String ownerId) throws AjaxException;

	CompilationResult compileFile(Long fileId) throws AjaxException;
	SimulationResult runSimulation(Long fileId) throws AjaxException;
	String generateVHDL(Long fileId) throws AjaxException;
	CircuitInterface extractCircuitInterface(Long fileId) throws AjaxException;
	List<Long> extractDependencies(Long fileId) throws AjaxException;
}
