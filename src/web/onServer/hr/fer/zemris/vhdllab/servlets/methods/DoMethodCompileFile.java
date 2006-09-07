package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.JavaToAjaxRegisteredMethod;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
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
 */
public class DoMethodCompileFile implements JavaToAjaxRegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.JavaToAjaxRegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.service.VHDLLabManager)
	 */
	public Properties run(Properties p, VHDLLabManager labman) {
		String fileID = p.getProperty(MethodConstants.PROP_FILE_ID,null);
		if(fileID == null) return errorProperties(MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No file ID specified!");

		Long id = null;
		try {
			id = Long.parseLong(fileID);
		} catch (NumberFormatException e) {
			return errorProperties(MethodConstants.SE_PARSE_ERROR,"Unable to parse file ID!");
		}
		
		// Get compilation result
		CompilationResult result = null;
		try {
			result = labman.compile(id);
		} catch (ServiceException e) {
			result = null;
		}
		if(result==null) return errorProperties(MethodConstants.SE_CAN_NOT_GET_COMPILATION_RESULT,"Unable to get compilation result!");

		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,MethodConstants.MTD_COMPILE_FILE);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		resProp.setProperty(MethodConstants.PROP_RESULT_STATUS,String.valueOf(result.getStatus()));
		resProp.setProperty(MethodConstants.PROP_RESULT_IS_SUCCESSFUL,String.valueOf(result.isSuccessful()));
		int i = 1;
		for(Message msg : result.getMessages()) {
			if(msg instanceof CompilationMessage){
				resProp.setProperty(MethodConstants.PROP_RESULT_MESSAGE_TYPE+i, MethodConstants.PROP_MESSAGE_TYPE_COMPILATION);
			} else if(msg instanceof WarningCompilationMessage) {
				resProp.setProperty(MethodConstants.PROP_RESULT_MESSAGE_TYPE+i, MethodConstants.PROP_MESSAGE_TYPE_COMPILATION_WARNING);
			} else if(msg instanceof ErrorCompilationMessage) {
				resProp.setProperty(MethodConstants.PROP_RESULT_MESSAGE_TYPE+i, MethodConstants.PROP_MESSAGE_TYPE_COMPILATION_ERROR);
			} else {
				return errorProperties(MethodConstants.SE_TYPE_SAFETY, "Found non-compilation type message for compilation result!");
			}
			resProp.setProperty(MethodConstants.PROP_RESULT_MESSAGE_TEXT+i, msg.getMessageText());
			CompilationMessage cmsg = (CompilationMessage) msg;
			resProp.setProperty(MethodConstants.PROP_RESULT_MESSAGE_ROW+i, String.valueOf(cmsg.getRow()));
			resProp.setProperty(MethodConstants.PROP_RESULT_MESSAGE_COLUMN+i, String.valueOf(cmsg.getColumn()));
			i++;
		}
		return resProp;
	}
	
	/**
	 * This method is called if errors occur.
	 * @param errNo error message number
	 * @param errorMessage error message to pass back to caller
	 * @return a response Properties
	 */
	private Properties errorProperties(String errNo, String errorMessage) {
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_STATUS,errNo);
		resProp.setProperty(MethodConstants.STATUS_CONTENT,errorMessage);
		return resProp;
	}
}