package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.ajax.shared.AjaxMediator;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.ajax.shared.XMLUtil;
import hr.fer.zemris.vhdllab.applets.main.interfaces.MethodInvoker;
import hr.fer.zemris.vhdllab.vhdl.CompilationMessage;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.ErrorCompilationMessage;
import hr.fer.zemris.vhdllab.vhdl.SimulationMessage;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.WarningCompilationMessage;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultType;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DefaultMethodInvoker implements MethodInvoker {

	private AjaxMediator ajax;

	public DefaultMethodInvoker(AjaxMediator ajax) {
		if(ajax == null) throw new NullPointerException("Ajax Mediator can not be null");
		this.ajax = ajax;
	}
	
	public CompilationResult compileFile(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_COMPILE_FILE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String resultStatus = resProperties.getProperty(MethodConstants.PROP_RESULT_STATUS);
		Integer status = Integer.parseInt(resultStatus);
		String resultIsSuccessful = resProperties.getProperty(MethodConstants.PROP_RESULT_IS_SUCCESSFUL);
		boolean isSuccessful = resultIsSuccessful.equals("1") ? true : false;
		List<CompilationMessage> messages = new ArrayList<CompilationMessage>();
		int i = 1;
		while(true) {
			String messageType = resProperties.getProperty(MethodConstants.PROP_RESULT_MESSAGE_TYPE+"."+i);
			if(messageType == null) break;
			String messageText = resProperties.getProperty(MethodConstants.PROP_RESULT_MESSAGE_TEXT+"."+i);
			String messageColumn = resProperties.getProperty(MethodConstants.PROP_RESULT_MESSAGE_COLUMN+"."+i);
			String messageRow = resProperties.getProperty(MethodConstants.PROP_RESULT_MESSAGE_ROW+"."+i);
			int column = Integer.parseInt(messageColumn);
			int row = Integer.parseInt(messageRow);
			
			if(messageType.equals(MethodConstants.PROP_MESSAGE_TYPE_COMPILATION)) {
				messages.add(new CompilationMessage(messageText, row, column));
			} else if(messageType.equals(MethodConstants.PROP_MESSAGE_TYPE_COMPILATION_ERROR)) {
				messages.add(new ErrorCompilationMessage(messageText, row, column));
			} else {
				messages.add(new WarningCompilationMessage(messageText, row, column));
			}
			i++;
		}
		
		return new CompilationResult(status, isSuccessful, messages);
	}

	public Long createFile(Long projectId, String name, String type) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_CREATE_NEW_FILE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(projectId));
		reqProperties.setProperty(MethodConstants.PROP_FILE_NAME, name);
		reqProperties.setProperty(MethodConstants.PROP_FILE_TYPE, type);
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String fileId = resProperties.getProperty(MethodConstants.PROP_FILE_ID);
		Long id = Long.parseLong(fileId);
		return id;
	}

	public Long createGlobalFile(String name, String type) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_CREATE_NEW_GLOBAL_FILE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_NAME, name);
		reqProperties.setProperty(MethodConstants.PROP_FILE_TYPE, type);
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String fileId = resProperties.getProperty(MethodConstants.PROP_FILE_ID);
		Long id = Long.parseLong(fileId);
		return id;
	}

	public Long createProject(String name, Long ownerId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_CREATE_NEW_PROJECT;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_PROJECT_NAME, name);
		reqProperties.setProperty(MethodConstants.PROP_PROJECT_OWNER_ID, String.valueOf(ownerId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String projectId = resProperties.getProperty(MethodConstants.PROP_PROJECT_ID);
		Long id = Long.parseLong(projectId);
		return id;
	}

	public Long createUserFile(Long ownerId, String type) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_CREATE_NEW_USER_FILE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_OWNER_ID, String.valueOf(ownerId));
		reqProperties.setProperty(MethodConstants.PROP_FILE_TYPE, type);
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String fileId = resProperties.getProperty(MethodConstants.PROP_FILE_ID);
		Long id = Long.parseLong(fileId);
		return id;
	}

	public void deleteFile(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_DELETE_FILE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		initiateAjax(reqProperties);
	}

	public void deleteGlobalFile(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_DELETE_GLOBAL_FILE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		initiateAjax(reqProperties);
	}

	public void deleteProject(Long projectId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_DELETE_PROJECT;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(projectId));
		
		initiateAjax(reqProperties);
	}

	public void deleteUserFile(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_DELETE_USER_FILE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		initiateAjax(reqProperties);
	}

	public boolean existsFile(Long projectId, String name) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_EXISTS_FILE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(projectId));
		reqProperties.setProperty(MethodConstants.PROP_FILE_NAME, name);
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String exists = resProperties.getProperty(MethodConstants.PROP_FILE_EXISTS);
		return exists.equals("1");
	}

	public boolean existsGlobalFile(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_EXISTS_GLOBAL_FILE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String exists = resProperties.getProperty(MethodConstants.PROP_FILE_EXISTS);
		return exists.equals("1");
	}

	public boolean existsProject(Long projectId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_EXISTS_PROJECT;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(projectId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String exists = resProperties.getProperty(MethodConstants.PROP_PROJECT_EXISTS);
		return exists.equals("1");
	}

	public boolean existsUserFile(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_EXISTS_USER_FILE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String exists = resProperties.getProperty(MethodConstants.PROP_FILE_EXISTS);
		return exists.equals("1");
	}

	public CircuitInterface extractCircuitInterface(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_EXTRACT_CIRCUIT_INTERFACE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String ciEntityName = resProperties.getProperty(MethodConstants.PROP_CI_ENTITY_NAME);
		List<Port> ports = new ArrayList<Port>();
		int i = 1;
		while(true) {
			String portName = resProperties.getProperty(MethodConstants.PROP_CI_PORT_NAME+"."+i);
			if(portName == null) break;
			String portDirection = resProperties.getProperty(MethodConstants.PROP_CI_PORT_DIRECTION+"."+i);
			String typeName = resProperties.getProperty(MethodConstants.PROP_CI_PORT_TYPE_NAME+"."+i);
			String typeRangeFrom = resProperties.getProperty(MethodConstants.PROP_CI_PORT_TYPE_RANGE_FROM+"."+i);
			String typeRangeTo = resProperties.getProperty(MethodConstants.PROP_CI_PORT_TYPE_RANGE_TO+"."+i);
			String vectorDirection = resProperties.getProperty(MethodConstants.PROP_CI_PORT_TYPE_VECTOR_DIRECTION+"."+i);
			int rangeFrom = Integer.parseInt(typeRangeFrom);
			int rangeTo = Integer.parseInt(typeRangeTo);
			
			Direction direction;
			if(portDirection.equalsIgnoreCase("IN")) {
				direction = Direction.IN;
			} else if(portDirection.equalsIgnoreCase("OUT")) {
				direction = Direction.OUT;
			} else if(portDirection.equalsIgnoreCase("INOUT")) {
				direction = Direction.INOUT;
			} else {
				direction = Direction.BUFFER;
			}
			
			Type type = new DefaultType(typeName, new int[] {rangeFrom, rangeTo}, vectorDirection);
			ports.add(new DefaultPort(portName, direction, type));
			i++;
		}
		
		return new DefaultCircuitInterface(ciEntityName, ports);
	}

	public List<Long> extractDependencies(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_EXTRACT_DEPENDENCIES;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		List<Long> files = new ArrayList<Long>();
		int i = 1;
		while(true) {
			String id = resProperties.getProperty(MethodConstants.PROP_FILE_ID+"."+i);
			if(id == null) break;
			files.add(Long.parseLong(id));
		}
		return files;
	}

	public Long findFileByName(Long projectId, String name) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_FIND_FILE_BY_NAME;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(projectId));
		reqProperties.setProperty(MethodConstants.PROP_FILE_NAME, name);
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String fileId = resProperties.getProperty(MethodConstants.PROP_FILE_ID);
		Long id = Long.parseLong(fileId);
		return id;
	}

	public List<Long> findGlobalFilesByType(String type) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_FIND_GLOBAL_FILES_BY_TYPE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_TYPE, type);
		
		Properties resProperties = initiateAjax(reqProperties);
		
		List<Long> files = new ArrayList<Long>();
		int i = 1;
		while(true) {
			String id = resProperties.getProperty(MethodConstants.PROP_FILE_ID+"."+i);
			if(id == null) break;
			files.add(Long.parseLong(id));
		}
		return files;
	}

	public List<Long> findProjectsByUser(Long ownerId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_FIND_PROJECTS_BY_USER;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_PROJECT_OWNER_ID, String.valueOf(ownerId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		List<Long> projects = new ArrayList<Long>();
		int i = 1;
		while(true) {
			String id = resProperties.getProperty(MethodConstants.PROP_PROJECT_ID+"."+i);
			if(id == null) break;
			projects.add(Long.parseLong(id));
		}
		return projects;
	}

	public List<Long> findUserFilesByOwner(Long ownerId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_FIND_USER_FILES_BY_USER;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_OWNER_ID, String.valueOf(ownerId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		List<Long> files = new ArrayList<Long>();
		int i = 1;
		while(true) {
			String id = resProperties.getProperty(MethodConstants.PROP_FILE_ID+"."+i);
			if(id == null) break;
			files.add(Long.parseLong(id));
		}
		return files;
	}

	public String generateVHDL(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_GENERATE_VHDL;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String vhdl = resProperties.getProperty(MethodConstants.PROP_GENERATED_VHDL);
		return vhdl;
	}

	public String loadFileContent(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_CONTENT;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String content = resProperties.getProperty(MethodConstants.PROP_FILE_CONTENT);
		return content;
	}

	public String loadFileName(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_NAME;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String name = resProperties.getProperty(MethodConstants.PROP_FILE_NAME);
		return name;
	}

	public Long loadFileProjectId(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_BELONGS_TO_PROJECT_ID;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String projectId = resProperties.getProperty(MethodConstants.PROP_PROJECT_ID);
		Long id = Long.parseLong(projectId);
		return id;

	}

	public String loadFileType(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_TYPE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String type = resProperties.getProperty(MethodConstants.PROP_FILE_TYPE);
		return type;
	}

	public String loadGlobalFileContent(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_LOAD_GLOBAL_FILE_CONTENT;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String content = resProperties.getProperty(MethodConstants.PROP_FILE_CONTENT);
		return content;
	}

	public String loadGlobalFileName(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_LOAD_GLOBAL_FILE_NAME;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String name = resProperties.getProperty(MethodConstants.PROP_FILE_NAME);
		return name;
	}

	public String loadGlobalFileType(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_LOAD_GLOBAL_FILE_TYPE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String type = resProperties.getProperty(MethodConstants.PROP_FILE_TYPE);
		return type;
	}

	public List<Long> loadProjectFilesId(Long projectId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_LOAD_PROJECT_FILES_ID;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(projectId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		List<Long> files = new ArrayList<Long>();
		int i = 1;
		while(true) {
			String id = resProperties.getProperty(MethodConstants.PROP_FILE_ID+"."+i);
			if(id == null) break;
			files.add(Long.parseLong(id));
		}
		return files;
	}

	public String loadProjectName(Long projectId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_LOAD_PROJECT_NAME;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(projectId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String name = resProperties.getProperty(MethodConstants.PROP_PROJECT_NAME);
		return name;
	}

	public Long loadProjectNumberFiles(Long projectId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_LOAD_PROJECT_NMBR_FILES;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(projectId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String numberOfFiles = resProperties.getProperty(MethodConstants.PROP_PROJECT_NMBR_FILES);
		Long count = Long.parseLong(numberOfFiles);
		return count;
	}

	public Long loadProjectOwnerId(Long projectId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_LOAD_PROJECT_OWNER_ID;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(projectId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String ownerId = resProperties.getProperty(MethodConstants.PROP_PROJECT_OWNER_ID);
		Long id = Long.parseLong(ownerId);
		return id;
	}

	public String loadUserFileContent(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_LOAD_USER_FILE_CONTENT;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String content = resProperties.getProperty(MethodConstants.PROP_FILE_CONTENT);
		return content;
	}

	public Long loadUserFileOwnerId(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_LOAD_USER_FILE_OWNER_ID;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String ownerId = resProperties.getProperty(MethodConstants.PROP_FILE_OWNER_ID);
		Long id = Long.parseLong(ownerId);
		return id;
	}

	public String loadUserFileType(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_LOAD_USER_FILE_TYPE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String type = resProperties.getProperty(MethodConstants.PROP_FILE_TYPE);
		return type;
	}

	public void renameFile(Long fileId, String name) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_RENAME_FILE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		reqProperties.setProperty(MethodConstants.PROP_FILE_NAME, name);
		
		initiateAjax(reqProperties);
	}

	public void renameGlobalFile(Long fileId, String name) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_RENAME_GLOBAL_FILE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		reqProperties.setProperty(MethodConstants.PROP_FILE_NAME, name);
		
		initiateAjax(reqProperties);
	}

	public void renameProject(Long projectId, String name) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_RENAME_PROJECT;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(projectId));
		reqProperties.setProperty(MethodConstants.PROP_PROJECT_NAME, name);
		
		initiateAjax(reqProperties);
	}

	public SimulationResult runSimulation(Long fileId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_RUN_SIMULATION;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		
		Properties resProperties = initiateAjax(reqProperties);
		
		String resultStatus = resProperties.getProperty(MethodConstants.PROP_RESULT_STATUS);
		Integer status = Integer.parseInt(resultStatus);
		String resultIsSuccessful = resProperties.getProperty(MethodConstants.PROP_RESULT_IS_SUCCESSFUL);
		boolean isSuccessful = resultIsSuccessful.equals("1") ? true : false;
		String waveform = resProperties.getProperty(MethodConstants.PROP_RESULT_WAVEFORM);
		List<SimulationMessage> messages = new ArrayList<SimulationMessage>();
		int i = 1;
		while(true) {
			String messageText = resProperties.getProperty(MethodConstants.PROP_RESULT_MESSAGE_TEXT+"."+i);
			if(messageText == null) break;
			messages.add(new SimulationMessage(messageText));
			i++;
		}
		
		return new SimulationResult(status, isSuccessful, messages, waveform);
	}

	public void saveFile(Long fileId, String content) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_SAVE_FILE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		reqProperties.setProperty(MethodConstants.PROP_FILE_CONTENT, content);
		
		initiateAjax(reqProperties);
	}

	public void saveGlobalFile(Long fileId, String content) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_SAVE_GLOBAL_FILE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		reqProperties.setProperty(MethodConstants.PROP_FILE_CONTENT, content);
		
		initiateAjax(reqProperties);
	}

	public void saveProject(Long projectId, List<Long> filesId) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_SAVE_PROJECT;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(projectId));
		int i = 1;
		for(Long id : filesId) {
			reqProperties.setProperty(MethodConstants.PROP_FILE_ID+"."+i, String.valueOf(id));
			i++;
		}
		
		initiateAjax(reqProperties);
	}

	public void saveUserFile(Long fileId, String content) throws AjaxException {
		Properties reqProperties = new Properties();
		String method = MethodConstants.MTD_SAVE_USER_FILE;
		reqProperties.setProperty(MethodConstants.PROP_METHOD, method);
		reqProperties.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(fileId));
		reqProperties.setProperty(MethodConstants.PROP_FILE_CONTENT, content);
		
		initiateAjax(reqProperties);
	}

	private Properties initiateAjax(Properties p) throws AjaxException {
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		String response = ajax.initiateSynchronousCall(XMLUtil.serializeProperties(p));
		Properties resProperties = XMLUtil.deserializeProperties(response);
		
		if(!method.equalsIgnoreCase(resProperties.getProperty(MethodConstants.PROP_METHOD))) {
			throw new AjaxException("Wrong method returned.");
		}
		String status = resProperties.getProperty(MethodConstants.PROP_STATUS);
		if(!status.equals(MethodConstants.STATUS_OK)) {
			throw new AjaxException(resProperties.getProperty(MethodConstants.PROP_STATUS_CONTENT));
		}
		
		return resProperties;
	}
}
