package hr.fer.zemris.vhdllab.servlets;

import hr.fer.zemris.ajax.shared.JavaToAjaxRegisteredMethod;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.ajax.shared.XMLUtil;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sample servlet which responses to AJAX calls.
 * 
 * @author marcupic
 *
 */
public class AjaxServlet extends HttpServlet {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -8488801764777289854L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStream is = request.getInputStream();
		
		// Another way to do it (and log request as well)
		String text = XMLUtil.inputStreamAsText(is,"UTF-8");
		log("Recieved following[\n"+text+"]\n");
		Properties p = XMLUtil.deserializeProperties(text);
		
		// Perform method dispatching...
		String method = p.getProperty("method","");
		ManagerProvider mprov = (ManagerProvider)this.getServletContext().getAttribute("managerProvider");
		VHDLLabManager labman = (VHDLLabManager)mprov.get("vhdlLabManager");
		JavaToAjaxRegisteredMethod regMethod = (JavaToAjaxRegisteredMethod)((Map)mprov.get("registeredMethods")).get(method);
		if(regMethod==null) returnError("Invalid method called!", request, response);
		
		// Prepair response
		Properties resProp = regMethod.run(p, request, response, labman);
		returnXMLResponse(XMLUtil.serializeProperties(resProp), request, response);
	}
	
	/**
	 * This method is called if errors occur.
	 * @param errorMessage Error message to pass back to caller
	 * @param request http servlet request
	 * @param response http servlet response
	 * @throws IOException
	 */
	private void returnError(String errorMessage, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_ERROR);
		resProp.setProperty(MethodConstants.STATUS_CONTENT,errorMessage);
		returnXMLResponse(XMLUtil.serializeProperties(resProp), request, response);
	}
	
	/**
	 * This method is called to actually send the results back to the caller.
	 * @param message message to send - this is assumed to be a XML message
	 * @param request http servlet request
	 * @param response http servlet response
	 * @throws IOException
	 */
	private void returnXMLResponse(String message, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control","no-cache");
		response.getWriter().write(message);
		response.getWriter().flush();
	}

}
