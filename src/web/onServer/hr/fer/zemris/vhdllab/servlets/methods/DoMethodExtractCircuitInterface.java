package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;

import java.util.Properties;

/**
 * This class represents a registered method for "extract circuit interface" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_EXTRACT_CIRCUIT_INTERFACE
 */
public class DoMethodExtractCircuitInterface implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	public Properties run(Properties p, ManagerProvider mprov) {
		VHDLLabManager labman = (VHDLLabManager)mprov.get("vhdlLabManager");
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		String fileID = p.getProperty(MethodConstants.PROP_FILE_ID,null);
		if(fileID==null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No file ID specified!");
		
		// Extract circuit interface
		CircuitInterface ci;
		try {
			Long id = Long.parseLong(fileID);
			File file = labman.loadFile(id);
			ci = labman.extractCircuitInterface(file);
		} catch (NumberFormatException e) {
			return errorProperties(method,MethodConstants.SE_PARSE_ERROR,"Unable to parse file ID = '"+fileID+"'!");
		} catch (Exception e) {
			ci = null;
		}
		if(ci == null) return errorProperties(method,MethodConstants.SE_CAN_NOT_EXTRACT_CIRCUIT_INTERFACE, "Can not extract circuit interface for file id = '"+fileID+"'!");
		
		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,method);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		resProp.setProperty(MethodConstants.PROP_CI_ENTITY_NAME, ci.getEntityName());
		int i = 1;
		for(Port port : ci.getPorts()) {
			Type type = port.getType();
			resProp.setProperty(MethodConstants.PROP_CI_PORT_NAME+"."+i, port.getName());
			resProp.setProperty(MethodConstants.PROP_CI_PORT_DIRECTION+"."+i, port.getDirection().toString());
			resProp.setProperty(MethodConstants.PROP_CI_PORT_TYPE_NAME+"."+i, type.getTypeName());
			if(type.isVector()) {
				resProp.setProperty(MethodConstants.PROP_CI_PORT_TYPE_RANGE_FROM+"."+i, String.valueOf(type.getRangeFrom()));
				resProp.setProperty(MethodConstants.PROP_CI_PORT_TYPE_RANGE_TO+"."+i, String.valueOf(type.getRangeTo()));
				resProp.setProperty(MethodConstants.PROP_CI_PORT_TYPE_VECTOR_DIRECTION+"."+i, type.getVectorDirection());
			}
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