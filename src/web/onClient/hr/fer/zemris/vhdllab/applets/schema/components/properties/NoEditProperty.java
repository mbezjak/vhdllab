package hr.fer.zemris.vhdllab.applets.schema.components.properties;

import hr.fer.zemris.vhdllab.applets.schema.components.Ptr;



public class NoEditProperty extends AbstractComponentProperty {
	private NoEditField nefield;
	
	public NoEditProperty(String nm, Ptr<Object> p) {
		super(nm, p);
		nefield = new NoEditField(p);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.AbstractComponentProperty#getPropField()
	 */
	@Override
	public AbstractPropField getPropField() {
		return nefield;
	}
}
