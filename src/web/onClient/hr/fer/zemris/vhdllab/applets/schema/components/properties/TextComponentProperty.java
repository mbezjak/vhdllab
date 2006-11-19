package hr.fer.zemris.vhdllab.applets.schema.components.properties;

import hr.fer.zemris.vhdllab.applets.schema.components.Ptr;

// OVO TREBA ZBRISAT!!!! Slobodno ovo zbrisi.
// Aleksandar


public class TextComponentProperty extends AbstractComponentProperty {
	private TextField textfield;
	
	public TextComponentProperty(String name, Ptr<Object> p) {
		super(name, p);
		textfield = new TextField(p);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.AbstractComponentProperty#getPropField()
	 */
	@Override
	public AbstractPropField getPropField() {
		return textfield;
	}
	
	
}
