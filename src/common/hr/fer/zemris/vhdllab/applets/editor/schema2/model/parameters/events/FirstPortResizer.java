package hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.events;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterEvent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultType;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;






/**
 * Uzima prvi port komponente kojoj pripada
 * i mijenja mu velicinu na zadanu.
 * Ovaj event nije undoable.
 * 
 * @author brijest
 *
 */
public class FirstPortResizer implements IParameterEvent {
	
	/* static fields */
	private static final List<ChangeTuple> changes = new ArrayList<ChangeTuple>();
	private static final List<ChangeTuple> ro_ch = Collections.unmodifiableList(changes);
	{
		changes.add(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}
	

	/* private fields */
	
	

	/* ctors */
	
	public FirstPortResizer() {
	}
	
	
	
	

	/* methods */

	public IParameterEvent copyCtor() {
		return new FirstPortResizer();
	}

	public List<ChangeTuple> getChanges() {
		return ro_ch;
	}

	public boolean isUndoable() {
		return false;
	}
	
	public boolean performChange(Object oldvalue, IParameter parameter, ISchemaInfo info,
			ISchemaWire wire, ISchemaComponent component)
	{
		if (component == null) return false;
		
		int oldnum = (Integer)oldvalue, portnum;
		
		try {
			portnum = parameter.getValueAsInteger();
		} catch (ClassCastException e) {
			return false;
		}
		
		if (oldnum == portnum) return true;
		if (portnum <= 0) return false;
		
		Port port = null;
		try {
			port = component.getPort(0);
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		
		// create new port of same type but new size
		Type tp = port.getType();
		if (tp.isScalar()) return false;
		int[] range = new int[2];
		range[0] = tp.getRangeFrom() + (portnum - oldnum) * ((tp.hasVectorDirectionTO()) ? (0) : (1));
		range[1] = tp.getRangeTo() + (portnum - oldnum) * ((tp.hasVectorDirectionTO()) ? (1) : (0));
		port = new DefaultPort(port.getName(), port.getDirection(),
				new DefaultType("std_logic_vector", range, tp.getVectorDirection()));
		component.setPort(0, port);
		
		return true;
	}
}



























