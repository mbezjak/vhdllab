package hr.fer.zemris.vhdllab.applets.schema.components.properties;

import hr.fer.zemris.vhdllab.applets.schema.components.Ptr;

public class TextProperty extends AbstractComponentProperty {
	private TextField textfield;
	
	public TextProperty(String name, Ptr<Object> p) {
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
