package hr.fer.zemris.vhdllab.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Miro Bezjak
 */
public class AuthenticationPingServlet extends HttpServlet {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		Enumeration e = request.getHeaderNames();
//		while(e.hasMoreElements()) {
//			String h = (String) e.nextElement();
//			System.out.println("header " + h + "=" +request.getHeader(h));
//		}
//		System.out.println(request.getRequestedSessionId());
//		System.out.println(request.getSession().getId());
//		System.out.println(request.getRequestedSessionId());
//		String user = request.getRemoteUser();
//		System.out.println(user);
//		System.out.println("\n\n");
//		returnResponse(user, request, response);
		request.getSession(); // create a session
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	/**
	 * This method is called to actually send the results back to the caller.
	 * @param method a method to send
	 * @param request http servlet request
	 * @param response http servlet response
	 * @throws IOException if method can not send results
	 */
	private void returnResponse(String user, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=UTF-8");
		response.setHeader("Cache-Control","no-cache");
		response.getWriter().print(user);
		response.getWriter().flush();
	}
}