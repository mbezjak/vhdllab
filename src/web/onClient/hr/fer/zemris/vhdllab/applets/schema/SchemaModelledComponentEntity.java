package hr.fer.zemris.vhdllab.applets.schema;

import java.util.ArrayList;


/**
 * U ovom razredu pohranjeni su svi podaci vezani uz sucelje sklopa.
 * 
 * Ti podaci su:
 * 
 * - lista ulaza i pripadni im tipovi (std_logic, std_logic_vector)
 * - lista izlaza i pripadni im tipovi
 * 
 * 
 * @author Axel
 *
 */
public class SchemaModelledComponentEntity {
	private ArrayList<SchemaModelledComponentPort> portList;
	private String entityName;
	private int nameCounter;
	
	public SchemaModelledComponentEntity() {
		entityName = new String("sklop01");
		portList = new ArrayList<SchemaModelledComponentPort>();
		nameCounter = -1;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		if (entityName.equals("") || (entityName.charAt(0) >= '0' && entityName.charAt(0) <= '9')) entityName = '_' + entityName;
		entityName = entityName.replaceAll("[^a-zA-Z0-9_]", "_");
		this.entityName = entityName;
	}

	public ArrayList<SchemaModelledComponentPort> getPortList() {
		return portList;
	}

	public void setPortList(ArrayList<SchemaModelledComponentPort> portList) {
		this.portList = portList;
	}

	public int getNameCounter() {
		nameCounter++;
		return nameCounter;
	}
	
	
}
