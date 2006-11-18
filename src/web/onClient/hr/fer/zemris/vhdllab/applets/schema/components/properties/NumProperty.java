package hr.fer.zemris.vhdllab.applets.schema.components.properties;

import hr.fer.zemris.vhdllab.applets.schema.components.Ptr;

public class NumProperty extends AbstractComponentProperty {
	private NumField field;

	public NumProperty(String nm, Ptr<Object> p) {
		super(nm, p);
		field = new NumField(p);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.properties.AbstractComponentProperty#getPropField()
	 */
	@Override
	public AbstractPropField getPropField() {
		return field;
	}

	
}
