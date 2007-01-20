package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingComponentEnvelope;
import hr.fer.zemris.vhdllab.applets.schema.wires.AbstractSchemaWire;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;

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
		CircuitInterface circuitInterface= ssInfo.getCircuitInterface();
		ArrayList<SchemaDrawingComponentEnvelope> envelopeList=ssInfo.getEnvelopeList();
		ArrayList<AbstractSchemaWire> wireList = ssInfo.getWireList();
		
		xmlCode.append(xmlHeader());		
		innerBuffer.append(xmlEntity(circuitInterface));
		innerBuffer.append(xmlComponent(envelopeList));
		innerBuffer.append(xmlWire(wireList));		
		xmlCode.append(xmlSchema(innerBuffer.toString()));
		
	}
	
	private String xmlEntity(CircuitInterface ci){
		StringBuffer buff=new StringBuffer();
		
		buff.append("<entity>\n");
		
			buff.append("<name>").append(ci.getEntityName()).append("</name>\n\n");
			buff.append("<portList>\n");
			
				for(Port port:ci.getPorts()){
					buff.append("<port>\n");
						buff.append("<name>").append(port.getName()).append("</name>\n");
						buff.append("<direction>").append(port.getDirection()).append("</direction>\n");
						buff.append("<type>").append(port.getType().getTypeName()).append("</type>\n");
						if(port.getType().isVector()){
							Type tp=port.getType();
							buff.append("<rangeFrom>").append(tp.getRangeFrom()).append("</rangeFrom>\n");
							buff.append("<rangeTo>").append(tp.getRangeTo()).append("</rangeTo>\n");
							buff.append("<vectorDirection>").append(tp.getVectorDirection()).append("</vectorDirection>\n");							
						}
					buff.append("</port>\n\n");
				}
				
			buff.append("</portList>\n");
			
		buff.append("</entity>\n\n");		
		return buff.toString();
	}
	
	private String xmlSchema(String xml){
		return "<schema>\n\n"+xml+"</schema>";
	}
	
	private String xmlHeader(){
		return "<?xml version=\"1.0\"?>\n";
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
			buff.append("<wireSource>").append(wire.serialize()).append("</wireSource>\n");
		}
		if(!wires.isEmpty()) buff.append("\n");
		return buff.toString();
	}
	
}
