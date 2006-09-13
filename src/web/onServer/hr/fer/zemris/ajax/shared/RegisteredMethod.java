package hr.fer.zemris.ajax.shared;

import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.manprovs.SampleManagerProvider;

import java.util.Properties;

/**
 * This interface represents one registered method for a given
 * request. All registered methods are registered in
 * {@linkplain hr.fer.zemris.vhdllab.servlets.manprovs.SampleManagerProvider}
 * 
 * @author Miro Bezjak
 * @see SampleManagerProvider#SampleManagerProvider()
 */
public interface RegisteredMethod {
	
	/**
	 * This method is called when cirtain request is received.
	 * 
	 * @param p Properties representing accepted request
	 * @param labman a lab manager defining communication between web-tier and service-tier
	 * @return a response Properties
	 */
	public Properties run(Properties p,	VHDLLabManager labman);
	
}