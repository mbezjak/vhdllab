package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;
import hr.fer.zemris.vhdllab.vhdl.CompilationMessage;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.ErrorCompilationMessage;
import hr.fer.zemris.vhdllab.vhdl.Message;
import hr.fer.zemris.vhdllab.vhdl.WarningCompilationMessage;

import java.util.Properties;

/**
 * This class represents a registered method for "compile file" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_COMPILE_FILE
 */
public class DoMethodCompileFile implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	public Properties run(Properties p, ManagerProvider mprov) {
		VHDLLabManager labman = (VHDLLabManager)mprov.get("vhdlLabManager");
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		String fileID = p.getProperty(MethodConstants.PROP_FILE_ID,null);
		if(fileID==null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No file ID specified!");
		
		// Get compilation result
		CompilationResult result = null;
		try {
			Long id = Long.parseLong(fileID);
			result = labman.compile(id);
		} catch (NumberFormatException e) {
			return errorProperties(method,MethodConstants.SE_PARSE_ERROR,"Unable to parse file ID = '"+fileID+"'!");
		} catch (ServiceException e) {
			result = null;
		}
		if(result==null) return errorProperties(method,MethodConstants.SE_CAN_NOT_GET_COMPILATION_RESULT,"Unable to get compilation result for file ("+fileID+")!");
		
		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,method);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		resProp.setProperty(MethodConstants.PROP_RESULT_STATUS,String.valueOf(result.getStatus()));
		resProp.setProperty(MethodConstants.PROP_RESULT_IS_SUCCESSFUL,String.valueOf(result.isSuccessful() ? 1 : 0));
		int i = 1;
		for(Message msg : result.getMessages()) {
			if(msg instanceof CompilationMessage){
				resProp.setProperty(MethodConstants.PROP_RESULT_MESSAGE_TYPE+"."+i, MethodConstants.PROP_MESSAGE_TYPE_COMPILATION);
			} else if(msg instanceof WarningCompilationMessage) {
				resProp.setProperty(MethodConstants.PROP_RESULT_MESSAGE_TYPE+"."+i, MethodConstants.PROP_MESSAGE_TYPE_COMPILATION_WARNING);
			} else if(msg instanceof ErrorCompilationMessage) {
				resProp.setProperty(MethodConstants.PROP_RESULT_MESSAGE_TYPE+"."+i, MethodConstants.PROP_MESSAGE_TYPE_COMPILATION_ERROR);
			} else {
				return errorProperties(method,MethodConstants.SE_TYPE_SAFETY, "Found non-compilation type message for compilation result!");
			}
			resProp.setProperty(MethodConstants.PROP_RESULT_MESSAGE_TEXT+"."+i, msg.getMessageText());
			CompilationMessage cmsg = (CompilationMessage) msg;
			resProp.setProperty(MethodConstants.PROP_RESULT_MESSAGE_ROW+"."+i, String.valueOf(cmsg.getRow()));
			resProp.setProperty(MethodConstants.PROP_RESULT_MESSAGE_COLUMN+"."+i, String.valueOf(cmsg.getColumn()));
			i++;
		}
		return resProp;
	}
	
	/**
	 * This method is called if error occurs.
	 * @param method a method that caused this error
	 * @param errNo error message number
	 * @param errorMessage error message to pass back to caller
	 * @return a response Properties
	 */
	private Properties errorProperties(String method, String errNo, String errorMessage) {
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,method);
		resProp.setProperty(MethodConstants.PROP_STATUS,errNo);
		resProp.setProperty(MethodConstants.PROP_STATUS_CONTENT,errorMessage);
		return resProp;
	}
}