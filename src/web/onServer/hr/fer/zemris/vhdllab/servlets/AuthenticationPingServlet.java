package hr.fer.zemris.vhdllab.servlets;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("auth servlet !!!!!!!!!!!!!!!111111");
		Cookie[] cookies = request.getCookies();
		if(cookies == null) {
			System.out.println("auth servlet = == = nista od cookie-a");
		} else {
			for(Cookie c : cookies) {
				System.out.println("name=" + c.getName() + "/value=" + c.getValue() + "%%" +c.getMaxAge());
			}
		}
		Enumeration en = request.getHeaderNames();
		while(en.hasMoreElements()) {
			String header = (String) en.nextElement();
			System.out.println(header + "=" + request.getHeader(header));
		}
		System.out.println(request.getSession().getId());
		System.out.println("auth servlet !!!!!!!!!!!!!!!111111");
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}