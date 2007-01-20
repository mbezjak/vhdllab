package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingComponentEnvelope;
import hr.fer.zemris.vhdllab.applets.schema.wires.AbstractSchemaWire;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.util.ArrayList;

public class SchemaSerializableInformation {

	//Ovo je sucelje sklopa koji se crta
	private CircuitInterface circuitInterface;
	
	//Ovdje su sve komponente i njihove pozicije.	
	private ArrayList<SchemaDrawingComponentEnvelope> envelopeList;
	
    //Ovdje idu sve zice.
	public ArrayList<AbstractSchemaWire> wireList;

	
	private long componentNameCounter;
	private int wireNameCounter;
	
	
	
	public void setCircuitInterface(CircuitInterface circuitInterface){
		this.circuitInterface=circuitInterface;
	}
	
	public void setEnvelopeList(ArrayList<SchemaDrawingComponentEnvelope> envelopeList){
		this.envelopeList=envelopeList;
	}
	
	public void setWireList(ArrayList<AbstractSchemaWire> wireList){
		this.wireList=wireList;
	}
	
	public void setComponentNameCounter(long componentNameCounter){
		this.componentNameCounter=componentNameCounter;
	}
	
	public void setWireNameCounter(int wireNameCounter){
		this.wireNameCounter=wireNameCounter;
	}
	
	/**
	 * Dohvaca sucelje sklopa koji se crta.
	 * @return
	 */
	public CircuitInterface getCircuitInterface(){
		return circuitInterface;
	}
	
	/**
	 * Dohvaca komponente i njihove pozicije.
	 * @return
	 */
	public ArrayList<SchemaDrawingComponentEnvelope> getEnvelopeList(){
		return envelopeList;
	}
	
	
	/**
	 * Dohvaca zice.
	 * @return
	 */
	public ArrayList<AbstractSchemaWire> getWireList(){
		return wireList;
	}
	
	public long getComponentNameCounter(){
		return this.componentNameCounter;
	}
	
	public int getWireNameCounter(){
		return wireNameCounter;
	}
	
}




