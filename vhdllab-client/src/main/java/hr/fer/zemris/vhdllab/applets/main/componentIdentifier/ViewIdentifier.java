package hr.fer.zemris.vhdllab.applets.main.componentIdentifier;

import hr.fer.zemris.vhdllab.applets.main.ComponentGroup;

/**
 * This is a default implementation a view identifier, a good class to subclass
 * for creating new view identifier.
 * 
 * @author Miro Bezjak
 * @param <T> a type of an instance modifier parameter
 */
public class ViewIdentifier<T> extends DefaultComponentIdentifier<T> {

	/**
	 * Constructs a view identifier with a single instance modifier.
	 * 
	 * @param viewType
	 *            a string that uniquely identifies a view
	 * @throws NullPointerException
	 *             if <code>viewType</code> is <code>null</code>
	 */
	public ViewIdentifier(String viewType) {
		this(viewType, null);
	}

	/**
	 * Constructs a view identifier out of specified parameters.
	 * <code>instanceModifier</code> may be <code>null</code> and in that
	 * case it indicates that only one instance of specified view may
	 * exist.
	 * 
	 * @param viewType
	 *            a string that uniquely identifies a view
	 * @param instanceModifier
	 *            a modifier that allows (and identifies) multiple instance of
	 *            specified view; may be <code>null</code>
	 * @throws NullPointerException
	 *             if <code>viewType</code> is <code>null</code>
	 */
	public ViewIdentifier(String viewType, T instanceModifier) {
		super(ComponentGroup.VIEW, viewType, instanceModifier);
	}

}
