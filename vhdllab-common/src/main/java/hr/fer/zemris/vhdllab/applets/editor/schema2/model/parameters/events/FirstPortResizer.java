package hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.events;

import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.api.vhdl.Range;
import hr.fer.zemris.vhdllab.api.vhdl.Type;
import hr.fer.zemris.vhdllab.api.vhdl.TypeName;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterEvent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;

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
	static {
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
		if (tp.getRange().isScalar()) return false;
		int[] range = new int[2];
		range[0] = tp.getRange().getFrom() + (portnum - oldnum) * ((tp.getRange().getDirection().isTO()) ? (0) : (1));
		range[1] = tp.getRange().getTo() + (portnum - oldnum) * ((tp.getRange().getDirection().isTO()) ? (1) : (0));
		port = new Port(port.getName(), port.getDirection(),
				new Type(TypeName.STD_LOGIC_VECTOR, new Range(range[0], tp.getRange().getDirection(), range[1])));
		component.setPort(0, port);
		
		return true;
	}
}



























