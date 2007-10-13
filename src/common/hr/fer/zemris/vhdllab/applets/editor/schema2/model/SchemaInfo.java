package hr.fer.zemris.vhdllab.applets.editor.schema2.model;

import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaEntity;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaPrototypeCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Rect2d;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.ParameterFactory;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.EntityWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.PortFactory;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.ShortcutTable;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.WireWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ComponentWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ParameterWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PortWrapper;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;



public class SchemaInfo implements ISchemaInfo {
	
	private ISchemaWireCollection wires;
	private ISchemaComponentCollection components;
	private ISchemaEntity entity;
	private ISchemaPrototypeCollection prototypes;
	private ShortcutTable shortcuts;
	
	

	public SchemaInfo() {
		wires = new SimpleSchemaWireCollection();
		components = new SimpleSchemaComponentCollection();
		entity = new SchemaEntity(new Caseless("schema"));
		prototypes = new ComponentPrototyper();
		shortcuts = new ShortcutTable();
	}
	
	

	public ISchemaComponentCollection getComponents() {
		return components;
	}

	public ISchemaEntity getEntity() {
		return entity;
	}

	public Map<Caseless, ISchemaComponent> getPrototypes() {
		return prototypes.getPrototypes();
	}

	public ISchemaWireCollection getWires() {
		return wires;
	}

	public void setComponents(ISchemaComponentCollection components) {
		this.components = components;
	}

	public void setWires(ISchemaWireCollection wires) {
		this.wires = wires;
	}
	
	public void setEntity(ISchemaEntity schemaEntity) {
		entity = schemaEntity;
	}

	public ISchemaPrototypeCollection getPrototyper() {
		return prototypes;
	}
	
	public void setShortcutTable(ShortcutTable table) {
		if (table == null) throw new NullPointerException("Shortcut table cannot be null.");
		shortcuts = table;
	}
	
	public void addWire(ISchemaWire wire) {
		try {
			wires.addWire(wire);
		} catch (DuplicateKeyException e) {
			throw new IllegalStateException();
		} catch (OverlapException e) {
			throw new IllegalStateException();
		}
	}
	
	/**
	 * Assumes a shortcut table has been set.
	 * Creates a wire, but replaces the drawer name
	 * from shortcut map before.
	 * Replaces the parameter and event name shortcuts with
	 * full names.
	 * @param wrapper
	 */
	public void addWireWrapper(WireWrapper wrapper) {
		// replace drawer name shortcut
		wrapper.drawername = shortcuts.get(wrapper.drawername);
		
		// replace parameter name shortcuts
		for (ParameterWrapper pw : wrapper.params) {
			pw.setParamClassName(shortcuts.get(pw.getParamClassName()));
			String eventname = pw.getEventName();
			if (eventname != null && !eventname.equals("")) {
				pw.setEventName(shortcuts.get(eventname));
			}
		}
		
		// create and add a wire
		SchemaWire wire = new SchemaWire();
		wire.setDrawer(wrapper.drawername);
		for (ParameterWrapper pw : wrapper.params) {
			wire.addParameter(pw);
		}
		for (WireSegment ws : wrapper.segs) {
			wire.insertSegment(ws);
		}
		
		try {
			wires.addWire(wire);
		} catch (DuplicateKeyException e) {
			throw new IllegalStateException();
		} catch (OverlapException e) {
			throw new IllegalStateException();
		}
	}
	
	/**
	 * Replaces the drawer shortcut with the value
	 * from the shortcut map, and then adds a component.
	 * Replaces the parameter, value and event name shortcuts with
	 * full names.
	 * Also replaces the class name.
	 * Assumes a shortcut table has been set.
	 */
	public void replaceDrawerAndAddComponent(ComponentWrapper compwrap) {
		// replace component class name
		compwrap.setComponentClassName(shortcuts.get(compwrap.getComponentClassName()));
		
		// replace drawer name shortcut
		compwrap.setDrawerName(shortcuts.get(compwrap.getDrawerName()));
		
		// replace parameter name shortcuts
		for (ParameterWrapper pw : compwrap.getParamWrappers()) {
			pw.setParamClassName(shortcuts.get(pw.getParamClassName()));
			String eventname = pw.getEventName();
			if (eventname != null && !eventname.equals("")) {
				pw.setEventName(shortcuts.get(eventname));
			}
			pw.setValueType(shortcuts.get(pw.getValueType()));
		}
		
		// add component
		try {
			Class cls = Class.forName(compwrap.getComponentClassName());
			Class[] partypes = new Class[1];
			partypes[0] = ComponentWrapper.class;
			Constructor<ISchemaComponent> ct = cls.getConstructor(partypes);
			Object[] params = new Object[1];
			params[0] = compwrap;
			ISchemaComponent cmp = ct.newInstance(params);
			components.addComponent(compwrap.getX(), compwrap.getY(), cmp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Could not create component.", e);
		}
	}
	
	public void addComponent(ComponentWrapper compwrap) {
		try {
			Class cls = Class.forName(compwrap.getComponentClassName());
			Class[] partypes = new Class[1];
			partypes[0] = ComponentWrapper.class;
			Constructor<ISchemaComponent> ct = cls.getConstructor(partypes);
			Object[] params = new Object[1];
			params[0] = compwrap;
			ISchemaComponent cmp = ct.newInstance(params);
			components.addComponent(compwrap.getX(), compwrap.getY(), cmp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Could not create component.", e);
		}
	}
	
	public void setEntityFromWrapper(EntityWrapper entwrap) {
		SchemaEntity ent = new SchemaEntity();
		
		// fill with parameters
		IParameterCollection params = ent.getParameters();
		params.clear();
		for (ParameterWrapper paramwrap : entwrap.getParameters()) {
			params.addParameter(ParameterFactory.createParameter(paramwrap));
		}
		
		// fill with ports
		List<Port> ports = ent.getPorts(this);
		for (PortWrapper portwrap : entwrap.getPortwrappers()) {
			ports.add(PortFactory.createPort(portwrap));
		}
		
		entity = ent;
	}

	public Caseless getFreeIdentifier() {
		throw new NotImplementedException();
	}

	public Caseless getFreeIdentifier(Caseless offered) {
		throw new NotImplementedException();
	}

	public boolean isFreeIdentifier(Caseless identifier) {
		throw new NotImplementedException();
	}

	public Rect2d boundingBox() {
		Rect2d bounds = new Rect2d();
		bounds.left = Integer.MAX_VALUE;
		bounds.top = Integer.MAX_VALUE;
		int maxx = Integer.MIN_VALUE, maxy = Integer.MIN_VALUE;
		
		for (PlacedComponent placed : components) {
			int tx = placed.pos.x + placed.comp.getWidth(), ty = placed.pos.y + placed.comp.getHeight();
			if (placed.pos.x < bounds.left) bounds.left = placed.pos.x;
			if (placed.pos.y < bounds.top) bounds.top = placed.pos.y;
			if (tx > maxx) maxx = tx;
			if (ty > maxy) maxy = ty;
		}
		
		for (ISchemaWire wire : wires) {
			Rect2d wrec = wire.getBounds();
			if (wrec.left < bounds.left) bounds.left = wrec.left;
			if (wrec.top < bounds.top) bounds.top = wrec.top;
			if ((wrec.left + wrec.width) > maxx) maxx = wrec.left + wrec.width;
			if ((wrec.top + wrec.height) > maxy) maxy = wrec.top + wrec.height;
		}
		
		if (bounds.left == Integer.MAX_VALUE) bounds.left = 0;
		if (bounds.top == Integer.MAX_VALUE) bounds.top = 0;
		if (maxx == Integer.MIN_VALUE) maxx = 0;
		if (maxy == Integer.MIN_VALUE) maxy = 0;
		bounds.width = maxx - bounds.left;
		bounds.height = maxy - bounds.top;
		
		return bounds;
	}
	
	
	

}






















