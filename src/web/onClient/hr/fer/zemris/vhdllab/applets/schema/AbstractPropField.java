package hr.fer.zemris.vhdllab.applets.schema;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public abstract class AbstractPropField implements
		IUpdateable {
	public AbstractPropField() {
	}
	
	public void updateProperty() {
	}
	
	public Component getComponent() {
		return null;
	}
}
