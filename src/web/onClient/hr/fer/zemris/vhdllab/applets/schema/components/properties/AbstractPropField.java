package hr.fer.zemris.vhdllab.applets.schema.components.properties;

import hr.fer.zemris.vhdllab.applets.schema.components.IUpdateable;

import java.awt.Component;

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
