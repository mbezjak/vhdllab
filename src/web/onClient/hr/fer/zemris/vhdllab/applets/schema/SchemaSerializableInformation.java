package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingComponentEnvelope;
import hr.fer.zemris.vhdllab.applets.schema.wires.AbstractSchemaWire;

import java.util.ArrayList;

public class SchemaSerializableInformation {
	// prije svega nekakav circuit interface - tu treba proucit Delacevo rjesenje
	// TODO rijesiti
	// ...
	
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




