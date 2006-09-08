package hr.fer.zemris.ajax.shared;

import hr.fer.zemris.vhdllab.service.VHDLLabManager;

import java.util.Map;
import java.util.Properties;

public interface MethodDispatcher {
	
	/**
	 * @param p
	 * @param regMap
	 * @param labman
	 * @return
	 */
	public Properties preformMethodDispatching(Properties p,
						Map<String, JavaToAjaxRegisteredMethod> regMap,
						VHDLLabManager labman);
	
}
