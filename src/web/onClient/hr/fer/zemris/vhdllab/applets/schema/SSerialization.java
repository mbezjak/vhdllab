package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.ajax.shared.XMLUtil;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingComponentEnvelope;
import hr.fer.zemris.vhdllab.applets.schema.wires.AbstractSchemaWire;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Serijalizacija 
 * 
 * @author Tommy
 *
 */
public class SSerialization {

	private SchemaSerializableInformation ssInfo; 
	private Properties globalProperties;
	
	public static final String SCHEMATIC_VERSION = "schematic.version";
	public static final String SCHEMATIC_ENTITY = "schematic.entity";	
	public static final String SCHEMATIC_COMPONENTS = "schematic.components";
	public static final String SCHEMATIC_WIRES = "schematic.wires";
	
	public static final String SCHEMATIC_COMPONENTS_COMPONENT = "component";
	public static final String SCHEMATIC_WIRES_WIRE = "wire";

	
	public SSerialization(SchemaSerializableInformation SSInfo){
		ssInfo=SSInfo;
		
		globalProperties=new Properties();		
		generateXmlCode();
	}
	
	/**
	 * Konstruktor za kratku serijalizaciju (potrebe getInitialFileContent-a)
	 * @param ci CircuitInterface
	 */
	public SSerialization(CircuitInterface ci){
		CircuitInterface circuitInterface=ci;
		globalProperties=new Properties();
		
		globalProperties.setProperty(SCHEMATIC_VERSION, "1.00");
		globalProperties.setProperty(SCHEMATIC_ENTITY, XMLUtil.serializeProperties(Entity(circuitInterface)));
		globalProperties.setProperty(SCHEMATIC_COMPONENTS, "");
		globalProperties.setProperty(SCHEMATIC_WIRES, "");
		
	}
	
	/**
	 * Dohvaæa XML file generiran na temelju trenutog sadržaja Schematica.
	 * @return Schematic XML
	 */
	public String getSerializedData(){		
		return XMLUtil.serializeProperties(globalProperties);
	}

	private void generateXmlCode() {		
		CircuitInterface circuitInterface= ssInfo.getCircuitInterface();
		ArrayList<SchemaDrawingComponentEnvelope> envelopeList=ssInfo.getEnvelopeList();
		ArrayList<AbstractSchemaWire> wireList = ssInfo.getWireList();
		
		SchematicInformation();
		globalProperties.setProperty(SCHEMATIC_ENTITY, XMLUtil.serializeProperties(Entity(circuitInterface)));
		globalProperties.setProperty(SCHEMATIC_COMPONENTS, XMLUtil.serializeProperties(Components(envelopeList)));
		globalProperties.setProperty(SCHEMATIC_WIRES, XMLUtil.serializeProperties(Wires(wireList)));
	}
	
	private Properties Entity(CircuitInterface ci){		
		//entity
		Properties buff=new Properties();
		
		buff.setProperty("name", ci.getEntityName());

		int counter=1;
		for(Port port:ci.getPorts()){
			buff.setProperty("portName"+counter, port.getName());
			buff.setProperty("portDirection"+counter, port.getDirection().toString());
			buff.setProperty("portType"+counter, port.getType().getTypeName());
			if(port.getType().isVector()){
				Type tp=port.getType();						
				buff.setProperty("portRangeFrom"+counter, String.valueOf(tp.getRangeFrom()));
				buff.setProperty("portRangeTo"+counter, String.valueOf(tp.getRangeTo()));
				buff.setProperty("portVectorDirection"+counter, tp.getVectorDirection());
			}else{
				buff.setProperty("portRangeFrom"+counter, "");
				buff.setProperty("portRangeTo"+counter, "");
				buff.setProperty("portVectorDirection"+counter, "");
			}
			counter++;
		}				
		
		return buff;
	}
	
	private void SchematicInformation(){
		globalProperties.setProperty(SCHEMATIC_VERSION, "1.00");
	}


	private Properties Components(ArrayList<SchemaDrawingComponentEnvelope> envelopes){
		//components
		Properties buff=new Properties();
		
		int counter=1;
		for(SchemaDrawingComponentEnvelope envelope:envelopes){		
			if (envelope.getComponent().isToBeSerialized()) { 
				buff.setProperty(SCHEMATIC_COMPONENTS_COMPONENT+counter, envelope.serialize());
				counter++;
			}
		}
		
		return buff;
	}
	
	private Properties Wires(ArrayList<AbstractSchemaWire> wires){
		Properties buff=new Properties();
		
		int counter=1;
		for(AbstractSchemaWire wire:wires){			
			buff.setProperty(SCHEMATIC_WIRES_WIRE+counter, wire.serialize());
			counter++;
		}
		
		return buff;
	}
	
}
