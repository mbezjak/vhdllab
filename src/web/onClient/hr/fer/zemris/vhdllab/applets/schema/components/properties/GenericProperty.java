package hr.fer.zemris.vhdllab.applets.schema.components.properties;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import hr.fer.zemris.vhdllab.applets.schema.components.Ptr;

public abstract class GenericProperty extends AbstractComponentProperty {
	private Ptr<Object> pSklop;
	private GenericPropField gPropField;

	public GenericProperty(String nm, Ptr<Object> p) {
		super(nm, p);
		pSklop = p;
		gPropField = new GenericPropField();
	}
	
	protected Ptr<Object> getSklopPtr() {
		return pSklop;
	}
	
	class GenericPropField extends AbstractPropField {
		JTextField tfield;
		
		public GenericPropField() {
			tfield = new JTextField();
			tfield.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					updateProperty();
				}
			});
			updateProperty();
		}
		
		public void updateProperty() {
			onUpdate(tfield);
		}
		
		public Component getComponent() {
			return tfield;
		}
	}
	
	

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.properties.AbstractComponentProperty#getPropField()
	 */
	@Override
	public AbstractPropField getPropField() {
		return gPropField;
	}

	public abstract void onUpdate(JTextField tf);
}
