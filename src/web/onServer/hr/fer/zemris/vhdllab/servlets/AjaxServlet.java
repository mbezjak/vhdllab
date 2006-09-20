package hr.fer.zemris.vhdllab.servlets;

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
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStream is = request.getInputStream();
		
		// Another way to do it (and log request as well)
		String text = XMLUtil.inputStreamAsText(is,"UTF-8");
		Properties p = XMLUtil.deserializeProperties(text);
		
		ManagerProvider mprov = (ManagerProvider)this.getServletContext().getAttribute("managerProvider");
		VHDLLabManager labman = (VHDLLabManager)mprov.get("vhdlLabManager");
		Map<String, RegisteredMethod> regMap = (Map<String, RegisteredMethod>)mprov.get("registeredMethods");
		MethodDispatcher disp = (MethodDispatcher)mprov.get("methodDispatcher");
		Properties resProp = disp.preformMethodDispatching(p, regMap, labman);
		returnXMLResponse(XMLUtil.serializeProperties(resProp), request, response);
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