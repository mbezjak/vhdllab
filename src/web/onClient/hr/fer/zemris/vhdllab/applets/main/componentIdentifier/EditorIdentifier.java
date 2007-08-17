package hr.fer.zemris.vhdllab.applets.main.componentIdentifier;

import hr.fer.zemris.vhdllab.applets.main.ComponentGroup;

/**
 * This is a default implementation an editor identifier, a good class to
 * subclass for creating new editor identifier.
 * 
 * @author Miro Bezjak
 * @param <T> a type of an instance modifier parameter
 */
public class EditorIdentifier<T> extends DefaultComponentIdentifier<T> {

	/**
	 * Constructs an editor identifier with a single instance modifier.
	 * 
	 * @param editorType
	 *            a string that uniquely identifies an editor
	 * @throws NullPointerException
	 *             if <code>editorType</code> is <code>null</code>
	 */
	public EditorIdentifier(String editorType) {
		this(editorType, null);
	}

	/**
	 * Constructs an editor identifier out of specified parameters.
	 * <code>instanceModifier</code> may be <code>null</code> and in that
	 * case it indicates that only one instance of specified editor may
	 * exist.
	 * 
	 * @param editorType
	 *            a string that uniquely identifies an editor
	 * @param instanceModifier
	 *            a modifier that allows (and identifies) multiple instance of
	 *            specified editor; may be <code>null</code>
	 * @throws NullPointerException
	 *             if <code>editorType</code> is <code>null</code>
	 */
	public EditorIdentifier(String editorType, T instanceModifier) {
		super(ComponentGroup.EDITOR, editorType, instanceModifier);
	}

}
