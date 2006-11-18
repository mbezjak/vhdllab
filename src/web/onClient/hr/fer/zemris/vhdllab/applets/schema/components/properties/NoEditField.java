package hr.fer.zemris.vhdllab.applets.schema.components.properties;

import hr.fer.zemris.vhdllab.applets.schema.components.Ptr;

import java.awt.Component;

import javax.swing.JTextField;

public class NoEditField extends AbstractPropField {
	private JTextField tf;
	
	public NoEditField(Ptr<Object> pStr) {
		tf = new JTextField((String)pStr.val);
		tf.setEnabled(false);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.AbstractPropField#getComponent()
	 */
	@Override
	public Component getComponent() {
		return tf;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.AbstractPropField#updateProperty()
	 */
	@Override
	public void updateProperty() {
		// do nothing here
	}

}
