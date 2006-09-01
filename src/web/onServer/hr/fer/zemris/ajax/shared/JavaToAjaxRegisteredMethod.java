package hr.fer.zemris.ajax.shared;

import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.manprovs.SampleManagerProvider;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This interface represents one registered method for a given
 * request. All registered methods are registered in
 * {@linkplain hr.fer.zemris.vhdllab.vhdl.model.servlets.manprovs.SampleManagerProvider}
 * 
 * @author Miro Bezjak
 * @see SampleManagerProvider#SampleManagerProvider()
 */
public interface JavaToAjaxRegisteredMethod {
	
	/**
	 * This method is called when cirtain request is received.
	 * 
	 * @param p Properties representing accepted request
	 * @param request http servlet request
	 * @param response http servlet response
	 * @param labman a lab manager from where to draw data
	 * @return a response Properties
	 * @throws IOException
	 */
	public Properties run(Properties p,
			HttpServletRequest request,
			HttpServletResponse response,
			VHDLLabManager labman)
			throws IOException;
	
}