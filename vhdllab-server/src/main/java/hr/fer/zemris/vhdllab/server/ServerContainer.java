package hr.fer.zemris.vhdllab.server;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.behaviors.Caching;

/**
 * Contains and instantiates various implementations of interfaces throughout
 * server tiers.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ServerContainer {
	/*
	 * This class in a thin wrapper around Pico Container.
	 */

	/**
	 * A pico container instance used by this wrapper class.
	 */
	private final MutablePicoContainer container;

	/**
	 * Constructs an empty server container.
	 */
	protected ServerContainer() {
		container = new DefaultPicoContainer(new Caching());
	}

	/**
	 * Constructs a child container of specified <code>parent</code>.
	 * 
	 * @param parent
	 *            a parent container
	 * @throws NullPointerException
	 *             if <code>parent</code> is <code>null</code>
	 */
	protected ServerContainer(ServerContainer parent) {
		container = parent.container.makeChildContainer();
	}

	/**
	 * Returns a mutable pico container. Return value will never be
	 * <code>null</code>.
	 * 
	 * @return a mutable pico container
	 */
	protected MutablePicoContainer getContainer() {
		return container;
	}

}
