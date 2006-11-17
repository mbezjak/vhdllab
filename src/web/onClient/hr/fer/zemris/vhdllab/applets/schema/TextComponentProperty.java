package hr.fer.zemris.vhdllab.applets.schema;

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
