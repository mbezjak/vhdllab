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
 * Servlet which responses to AJAX calls.
 * 
 * @author Miro Bezjak
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
		String method = p.getProperty(MethodConstants.PROP_METHOD,"");
		ManagerProvider mprov = (ManagerProvider)this.getServletContext().getAttribute("managerProvider");
		VHDLLabManager labman = (VHDLLabManager)mprov.get("vhdlLabManager");
		JavaToAjaxRegisteredMethod regMethod = (JavaToAjaxRegisteredMethod)((Map)mprov.get("registeredMethods")).get(method);
		
		// Prepair response
		Properties resProp = null;
		if(regMethod==null) resProp = errorProperties(MethodConstants.SE_INVALID_METHOD_CALL, "Invalid method called!");
		else resProp = regMethod.run(p, labman);

		returnXMLResponse(XMLUtil.serializeProperties(resProp), request, response);
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
	
	/**
	 * This method is called to actually send the results back to the caller.
	 * @param message message to send - this is assumed to be a XML message
	 * @param request http servlet request
	 * @param response http servlet response
	 * @throws IOException if method can not send results
	 */
	private void returnXMLResponse(String message, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control","no-cache");
		response.getWriter().write(message);
		response.getWriter().flush();
	}
}