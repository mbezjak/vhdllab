package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.ajax.shared.XMLUtil;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactoryException;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingCanvas;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingComponentEnvelope;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaMainPanel;
import hr.fer.zemris.vhdllab.applets.schema.wires.AbstractSchemaWire;
import hr.fer.zemris.vhdllab.applets.schema.wires.SimpleSchemaWire;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Deserijalizator za Schematic
 * @author Tommy
 *
 */
public class SDeserialization {
	private Properties globalPropery=null;
	private SchemaMainPanel mainPanel=null;
	
	public SDeserialization(String serializedXml, SchemaMainPanel mainPanel) throws Exception{
		if(mainPanel!=null && serializedXml.length()!=0){
			globalPropery=XMLUtil.deserializeProperties(serializedXml);
			this.mainPanel=mainPanel;
			deserialize();
		}else
			throw new IllegalArgumentException("Krivi parametri za deserializer");
	}
	
	private void deserialize() throws Exception {
		mainPanel.getSchemaDrawingCanvas().ResetCanvas();
		entity(XMLUtil.deserializeProperties(globalPropery.getProperty(SSerialization.SCHEMATIC_ENTITY)));
		components();
		wire();
	}

	private void entity(Properties entity) throws Exception {
		String entityName = entity.getProperty("name");
		
		String portName = null;
		Direction portDir = null;
		Type portType = null;
		List<Port> portlist = new ArrayList<Port>();
		
		for(int i=1;true;i++){
			portName=entity.getProperty("portName"+i,"");
			if (portName.equals(""))break;
			
			String pds = entity.getProperty("portDirection" + i);
			portDir = getDirection(pds);
			
			
			
			/*System.out.println("Deserialization: PortName:"+portName);
			System.out.println("Deserialization: PortDirection"+entity.getProperty("portDirection"+i));
			System.out.println("Deserialization: PortType:"+entity.getProperty("portType"+i));
			System.out.println("Deserialization: PortRangeFrom:"+entity.getProperty("portRangeFrom"+i,""));
			System.out.println("Deserialization: PortRangeTo:"+entity.getProperty("portRangeTo"+i,""));
			System.out.println("Deserialization: PortVectorDirection:"+entity.getProperty("portVectorDirection"+i,""));*/
		}
		
		CircuitInterface interf = new DefaultCircuitInterface(entityName);
		mainPanel.setCircuitInterface(interf);
	}
	
	private Direction getDirection(String pds) throws Exception {
		if (pds.equals("IN")) return Direction.IN;
		if (pds.equals("OUT")) return Direction.OUT;
		if (pds.equals("INOUT")) return Direction.INOUT;
		if (pds.equals("BUFFER")) return Direction.BUFFER;
		throw new Exception("Unsupported direction string: " + pds);
	}
	
	
	private void components(){
		
		Properties c=XMLUtil.deserializeProperties(globalPropery.getProperty(SSerialization.SCHEMATIC_COMPONENTS));
		String serData;
		@SuppressWarnings("unused")
		SchemaDrawingComponentEnvelope env;
		
		for(int i=1;true;i++){
			serData=c.getProperty(SSerialization.SCHEMATIC_COMPONENTS_COMPONENT+i);
			if(serData==null)break;
			System.out.println("Deserijalizacija: component source:"+serData);
			env=new SchemaDrawingComponentEnvelope();
			
			try {
				env.deserialize(serData);
				mainPanel.getSchemaDrawingCanvas().addEnvelope(env);
			} catch (ComponentFactoryException e) {
				System.err.println("Greska prilikom stvaranje envelope-a");
				e.printStackTrace();
			}
		}		
	}
	
	private void wire(){
		Properties c=XMLUtil.deserializeProperties(globalPropery.getProperty(SSerialization.SCHEMATIC_WIRES));
		String serData;
		AbstractSchemaWire wire=null;
		SchemaDrawingCanvas canvas=mainPanel.getSchemaDrawingCanvas();
		
		for(int i=1;true;i++){
			serData=c.getProperty(SSerialization.SCHEMATIC_WIRES_WIRE+i);
			if(serData==null)break;
			System.out.println("Deserijalizacija: wire source:"+serData);
			wire=new SimpleSchemaWire("w");//prilicno blesavo, ja neznam unaprijed ime zice
			wire.deserialize(serData);
			canvas.addWire(wire);
		}	
	}
}
