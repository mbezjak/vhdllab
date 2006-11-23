package hr.fer.zemris.vhdllab.applets.schema.components.properties;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import hr.fer.zemris.vhdllab.applets.schema.components.Ptr;

public abstract class GenericComboProperty extends AbstractComponentProperty {
	private Ptr<Object> pSklop;
	private GenericPropField gPropField;

	public GenericComboProperty(String nm, Ptr<Object> p) {
		super(nm, p);
		pSklop = p;
		gPropField = new GenericPropField();
	}
	
	protected Ptr<Object> getSklopPtr() {
		return pSklop;
	}
	
	class GenericPropField extends AbstractPropField {
		JComboBox cbox;
		
		public GenericPropField() {
			cbox = new JComboBox();
			cbox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					updateProperty();
				}
			});
			updateProperty();
		}
		
		public void updateProperty() {
			onUpdate(cbox);
		}
		
		public Component getComponent() {
			return cbox;
		}
	}
	
	

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.properties.AbstractComponentProperty#getPropField()
	 */
	@Override
	public AbstractPropField getPropField() {
		return gPropField;
	}

	public abstract void onUpdate(JComboBox cbox);
}
