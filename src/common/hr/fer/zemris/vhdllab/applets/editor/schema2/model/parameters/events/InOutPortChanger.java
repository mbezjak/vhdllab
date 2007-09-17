package hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterEvent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.InOutSchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.generic.ParamPort;


/**
 * Analizira port i postavlja schema portove (pinove).
 * Moze pripadati iskljucivo parametru InOutComponent-a,
 * i to parametru koji je po tipu OBJECT i sadrzi vrijednost
 * ParamPort.
 * 
 * @author Axel
 *
 */
public class InOutPortChanger implements IParameterEvent {

	/* static fields */
	private static final List<ChangeTuple> changes = new ArrayList<ChangeTuple>();
	private static final List<ChangeTuple> ro_ch = Collections.unmodifiableList(changes);
	{
		changes.add(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}

	/* private fields */
	
	

	/* ctors */
	
	public InOutPortChanger() {
	}
	

	/* methods */

	public IParameterEvent copyCtor() {
		return new InOutPortChanger();
	}
	
	public List<ChangeTuple> getChanges() {
		return ro_ch;
	}
	
	public boolean isUndoable() {
		return false;
	}
	
	public boolean performChange(Object oldvalue, IParameter parameter,
			ISchemaInfo info, ISchemaWire wire, ISchemaComponent component)
	{
		if (component == null || !(component instanceof InOutSchemaComponent))
				throw new IllegalArgumentException("This parameter only works for InOutComponents.");
		
		if (!(oldvalue instanceof ParamPort))
			throw new IllegalArgumentException("Old value should be instance of ParamPort.");
		
		Object newvalue = parameter.getValue();
		
		if (!(newvalue instanceof ParamPort))
			throw new IllegalStateException("Parameter contains a value which is not a ParamPort.");
		
		InOutSchemaComponent inoutcmp = (InOutSchemaComponent)component;
		ParamPort oldport = (ParamPort)oldvalue, newport = (ParamPort)newvalue;
		
		if (oldport.getPort().equals(newport.getPort())) return true;
		
		inoutcmp.setPort(newport.getPort());
		inoutcmp.rebuildSchemaPorts();
		
		return true;
	}
	
}














