package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingComponentEnvelope;
import hr.fer.zemris.vhdllab.applets.schema.wires.AbstractSchemaWire;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.util.ArrayList;

public class SchemaSerializableInformation {
	/**
	 * Ovo je sucelje sklopa koji se crta
	 */
	public CircuitInterface circuitInterface;
	
	/**
	 * Ovdje su sve komponente (!OSIM! ulaznih i izlaznih portova), to se
	 * spremilo vec u circuit interfaceu.
	 */
	public ArrayList<SchemaDrawingComponentEnvelope> envelopeList;
	
	/**
	 * Ovdje idu sve zice.
	 */
	public ArrayList<AbstractSchemaWire> wireList;
	
	public int componentNameCounter;
	public int wireNameCounter;
}




