package hr.fer.zemris.vhdllab.applets.automat.entityTable;

public class ReturnData {
	private String[][] data;
	private String name;
	
	public ReturnData(String name,String[][] data) {
	super();
	this.name=name;
	this.data=data;
	}

	public String[][] getData() {
		return data;
	}

	public String getName() {
		return name;
	}
}
