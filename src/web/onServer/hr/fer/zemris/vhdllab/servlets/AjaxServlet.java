package hr.fer.zemris.vhdllab.servlets;

import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.communicaton.MethodConstants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		request.getSession().setMaxInactiveInterval(15);
		System.out.println(request.getRequestedSessionId());
		System.out.println(request.getSession().getId());
		System.out.println("host=" + request.getRemoteHost());
		System.out.println("user=" + request.getRemoteUser());
		System.out.println("principal=" + request.getUserPrincipal());
		Cookie[] cookies = request.getCookies();
		if(cookies == null) {
			System.out.println("nista od cookie-a");
		} else {
			for(Cookie c : cookies) {
				System.out.println("name=" + c.getName() + "/value=" + c.getValue() + "%%" +c.getMaxAge());
			}
		}
//		Enumeration en = request.getHeaderNames();
//		while(en.hasMoreElements()) {
//			String header = (String) en.nextElement();
//			System.out.println(header + "=" + request.getHeader(header));
//		}
		ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
		IMethod<Serializable> method;
		try {
			method = (IMethod<Serializable>) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		} finally {
			ois.close();
		}
		ManagerProvider provider = (ManagerProvider)this.getServletContext().getAttribute("managerProvider");
		
		RegisteredMethod regMethod = MethodFactory.getMethod(method.getMethod());
		if(regMethod == null) {
			method.setStatus(MethodConstants.SE_INVALID_METHOD_CALL);
		} else {
			try {
				regMethod.run(method, provider);
			} catch (SecurityException e) {
				e.printStackTrace();
				method.setStatus(MethodConstants.SE_NO_PERMISSION);
			} catch (Throwable e) {
				e.printStackTrace();
				method.setStatus(MethodConstants.SE_INTERNAL_SERVER_ERROR);
			}
		}
		returnResponse(method, request, response);
	}
	
	/**
	 * This method is called to actually send the results back to the caller.
	 * @param method a method to send
	 * @param request http servlet request
	 * @param response http servlet response
	 * @throws IOException if method can not send results
	 */
	private void returnResponse(IMethod<Serializable> method, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		response.setHeader("Cache-Control","no-cache");
		ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream());
		oos.writeObject(method);
		oos.flush();
	}
}