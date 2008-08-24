package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import java.beans.PropertyChangeListener;

import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWireCollection;

public interface ISchemaCanvas extends PropertyChangeListener{

	//###########################PUBLIC METHODS##########################
	public void setDummyStuff(ISchemaComponentCollection components,
			ISchemaWireCollection wires);

	public void registerSchemaController(ISchemaController controller);

	public void registerLocalController(ILocalGuiController cont);

}