package hr.fer.zemris.vhdllab.servlets.dispatch;

import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.communicaton.MethodConstants;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.MethodDispatcher;
import hr.fer.zemris.vhdllab.servlets.MethodFactory;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.io.Serializable;

/**
 * This class simply calls a registered method written in
 * Properties (argument of constructor).
 * @author Miro Bezjak
 */
public class SimpleMethodDispatcher implements MethodDispatcher {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.servlets.MethodDispatcher#preformMethodDispatching(hr.fer.zemris.vhdllab.communicaton.IMethod, hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	@Override
	public void preformMethodDispatching(IMethod<Serializable> method,
			ManagerProvider provider) {
		if(method==null) throw new NullPointerException("Properties can not be null!");
		if(provider==null) throw new NullPointerException("Manager provider can not be null!");
		RegisteredMethod registeredMethod = MethodFactory.getMethod(method.getMethod());
		if(registeredMethod == null) {
			method.setStatus(MethodConstants.SE_INVALID_METHOD_CALL);
			return;
		}
		registeredMethod.run(method, provider);
	}
	
}