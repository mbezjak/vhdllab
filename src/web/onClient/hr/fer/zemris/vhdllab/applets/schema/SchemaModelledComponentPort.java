package hr.fer.zemris.vhdllab.applets.schema;

import java.util.TreeMap;

public class SchemaModelledComponentPort {
	public enum SMCTip { std_logic, std_logic_vector }
	public enum SMCDirection { IN, OUT }
	
	private String portName;
	private SMCTip portType;
	private SMCDirection portDirection;
	private int portCardinality;
	private TreeMap<Integer, String> connectionList;
	
	public SchemaModelledComponentPort() {
		portName = new String("port");
		portType = SMCTip.std_logic;
		portDirection = SMCDirection.IN;
		portCardinality = 1;
		connectionList = new TreeMap<Integer, String>();
	}

	public SMCDirection getPortDirection() {
		return portDirection;
	}

	public void setPortDirection(SMCDirection portDirection) {
		this.portDirection = portDirection;
	}

	public SMCTip getPortType() {
		return portType;
	}

	public void setPortType(SMCTip portType) {
		if (portType == SMCTip.std_logic) setPortCardinality(1);
		this.portType = portType;
	}

	public int getPortCardinality() {
		return portCardinality;
	}

	public void setPortCardinality(int portCardinality) {
		if (portCardinality <= 0) return;
		this.portCardinality = portCardinality;
	}
	
	public void setConnectionAt(int i, String s) {
		if (i >= 0 && i < portCardinality) {
			connectionList.put(i, s);
		}
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		if (portName.equals("") || (portName.charAt(0) >= '0' && portName.charAt(0) <= '9')) portName = '_' + portName;
		portName = portName.replaceAll("[^a-zA-Z0-9_]", "_");
		this.portName = portName;
	}

	public TreeMap<Integer, String> getConnectionList() {
		return connectionList;
	}

	public void setConnectionList(TreeMap<Integer, String> connectionList) {
		this.connectionList = connectionList;
	}
	
	
}
