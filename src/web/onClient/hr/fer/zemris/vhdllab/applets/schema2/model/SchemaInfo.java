package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaEntity;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaPrototypeCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.ComponentWrapper;

import java.lang.reflect.Constructor;
import java.util.Map;



public class SchemaInfo implements ISchemaInfo {
	
	private ISchemaWireCollection wires;
	private ISchemaComponentCollection components;
	private ISchemaEntity entity;
	private ISchemaPrototypeCollection prototypes;
	
	

	public SchemaInfo() {
		wires = new SimpleSchemaWireCollection();
		components = new SimpleSchemaComponentCollection();
		entity = new SchemaEntity(new Caseless(""));
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

	public ISchemaPrototypeCollection getPrototyper() {
		return prototypes;
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
	
	public void addComponent(ComponentWrapper compwrap) {
		try {
			Class cls = Class.forName(compwrap.getComponentClassName());
			Class[] partypes = new Class[0];
			Constructor<ISchemaComponent> ct = cls.getConstructor(partypes);
			ISchemaComponent cmp = ct.newInstance();
			cmp.deserialize(compwrap);
			components.addComponent(compwrap.getX(), compwrap.getY(), cmp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Could not create component.", e);
		}
	}

}






















