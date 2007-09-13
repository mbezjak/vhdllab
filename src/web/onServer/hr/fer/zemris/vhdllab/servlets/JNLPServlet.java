/**
 * 
 */
package hr.fer.zemris.vhdllab.servlets;

import hr.fer.zemris.vhdllab.utilities.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miro Bezjak
 * 
 */
public class JNLPServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * A file containing all properties that will be substituted in actual jnlp
	 * file.
	 */
	private static final String PARAM_FILE = "/WEB-INF/parameters.properties";
	/**
	 * A property containing a name of a jnlp file.
	 */
	private static final String JNLP_FILE = "jnlp.file";

	/**
	 * A content of created jnlp file.
	 */
	private static String jnlp;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext context = config.getServletContext();
		InputStream is = context.getResourceAsStream(PARAM_FILE);
		Properties p = FileUtil.getProperties(is);
		String jnlpFile = p.getProperty(JNLP_FILE);
		is = context.getResourceAsStream("/WEB-INF/" + jnlpFile);
		jnlp = FileUtil.readFile(is);

		jnlp = jnlp.replaceAll("@@codebase@@", p.getProperty("codebase"));

		String archives = p.getProperty("archive");
		String[] array = archives.split(",");
		StringBuilder sb = new StringBuilder(100);
		boolean first = true;
		for (String a : array) {
			a = a.trim();
			sb.append("<jar href= \"").append(a).append("\" ");
			if (first) {
				first = false;
				sb.append("main=\"true\"");
			}
			sb.append("/>\n\t\t");
		}

		jnlp = jnlp.replace("@@archive@@", sb.toString());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/x-java-jnlp-file");
		response.setHeader("Cache-Control", "no-cache");
		response.setContentLength(jnlp.length());
		response.getWriter().print(jnlp);
		response.getWriter().flush();
	}

}
