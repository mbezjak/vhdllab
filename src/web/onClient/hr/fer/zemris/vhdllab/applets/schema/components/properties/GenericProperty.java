package hr.fer.zemris.vhdllab.applets.schema.components.properties;

import hr.fer.zemris.vhdllab.applets.schema.components.Ptr;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

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
			setStartValue();
		}
		
		public void updateProperty() {
			onUpdate(tfield);
		}
		
		public void setStartValue() {
			onLoad(tfield);
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
	
	public abstract void onLoad(JTextField tf);
}
