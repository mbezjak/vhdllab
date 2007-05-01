package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaEntity;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaPrototypeCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;

import java.util.Map;



public class SchemaInfo implements ISchemaInfo {
	private ISchemaWireCollection wires;
	private ISchemaComponentCollection components;
	private ISchemaEntity entity;
	private ISchemaPrototypeCollection prototypes;
	
	
	public SchemaInfo() {
		wires = new SimpleSchemaWireCollection();
		components = new SimpleSchemaComponentCollection();
		entity = new SchemaEntity();
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

	public void deserialize(String code) {
		throw new NotImplementedException();
	}

	public String serialize() {
		throw new NotImplementedException();
	}
	

}
