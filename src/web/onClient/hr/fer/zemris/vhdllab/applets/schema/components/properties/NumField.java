package hr.fer.zemris.vhdllab.applets.schema.components.properties;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import hr.fer.zemris.vhdllab.applets.schema.components.IUpdateable;
import hr.fer.zemris.vhdllab.applets.schema.components.Ptr;

public class NumField extends AbstractPropField {
	Ptr<Object> p;
	JTextField tfield;
	
	public NumField(Ptr<Object> pNum) {
		p = pNum;		
		tfield = new JTextField(((Integer)p.val).toString());
		tfield.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateProperty();
			}			
		});
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.properties.AbstractPropField#getComponent()
	 */
	@Override
	public Component getComponent() {
		return tfield;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.properties.AbstractPropField#updateProperty()
	 */
	@Override
	public void updateProperty() {
		String old = ((Integer) p.val).toString();
		Integer newval = null;
		try {
			newval = Integer.parseInt(tfield.getText());
		} catch (Exception e) {
			tfield.setText(old);
			return;
		}
		p.val = newval;
	}

	
}
