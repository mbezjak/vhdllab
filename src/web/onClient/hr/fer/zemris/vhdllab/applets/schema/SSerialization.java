package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingComponentEnvelope;
import hr.fer.zemris.vhdllab.applets.schema.wires.AbstractSchemaWire;

import java.util.ArrayList;

/**
 * Serijalizacija
 * 
 * @author Tommy
 *
 */
public class SSerialization {

	private SchemaSerializableInformation ssInfo; 
	private StringBuffer xmlCode;
	
	public SSerialization(SchemaSerializableInformation SSInfo){
		ssInfo=SSInfo;
		xmlCode=new StringBuffer(512);
		generateXmlCode();
	}
	
	public String getSerializedData(){		
		return xmlCode.toString();
	}

	private void generateXmlCode() {
		StringBuffer innerBuffer=new StringBuffer();
		//CircuitInterface circuitInterface= ssInfo.getCircuitInterface();
		ArrayList<SchemaDrawingComponentEnvelope> envelopeList=ssInfo.getEnvelopeList();
		ArrayList<AbstractSchemaWire> wireList = ssInfo.getWireList();
		
		xmlCode.append(xmlHeader());		
		innerBuffer.append(xmlComponent(envelopeList));
		innerBuffer.append(xmlWire(wireList));
		xmlCode.append(xmlSchema(innerBuffer.toString()));
	}
	
	private String xmlSchema(String xml){
		return "<schema version=\"1.0\"\n"+xml+"</schema>";
	}
	
	private String xmlHeader(){
		return "<?xml version=\"1.0\"?>";
	}

	private String xmlComponent(ArrayList<SchemaDrawingComponentEnvelope> envelopes){
		StringBuffer buff=new StringBuffer();
		
		for(SchemaDrawingComponentEnvelope envelope:envelopes){
			buff.append(envelope.serialize());
			buff.append("\n");
		}
		
		return buff.toString();
	}
	
	private String xmlWire(ArrayList<AbstractSchemaWire> wires){
		StringBuffer buff=new StringBuffer();
		
		for(AbstractSchemaWire wire:wires){
			buff.append("<wire>").append(wire.serialize()).append("</wire>");
			buff.append("\n");
		}
		
		return buff.toString();
	}
}
