package hr.fer.zemris.vhdllab.dao.impl;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * A servlet request listener for creating and destroying entity manager
 * associated with a thread that processes given request. Basically an
 * entity-manager-per-request idiom.
 * 
 * @author Miro Bezjak
 */
public class EntityManagerLifecycle implements ServletRequestListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequestListener#requestInitialized(javax.servlet.ServletRequestEvent)
	 */
	@Override
	public void requestInitialized(ServletRequestEvent event) {
		// create entity manager for this request
		EntityManagerUtil.currentEntityManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequestListener#requestDestroyed(javax.servlet.ServletRequestEvent)
	 */
	@Override
	public void requestDestroyed(ServletRequestEvent event) {
		// close entity manager associated with this request
		EntityManagerUtil.closeEntityManager();
	}

}
