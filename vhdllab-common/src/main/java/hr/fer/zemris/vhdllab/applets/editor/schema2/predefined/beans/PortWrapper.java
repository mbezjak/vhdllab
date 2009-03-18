package hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans;

import hr.fer.zemris.vhdllab.service.ci.Port;

public class PortWrapper {
	
	public static final String STD_LOGIC = "std_logic";
	public static final String STD_LOGIC_VECTOR = "std_logic_vector";
	public static final String DIRECTION_IN = "IN";
	public static final String DIRECTION_OUT = "OUT";
	public static final String ORIENTATION_NORTH = "NORTH";
	public static final String ORIENTATION_SOUTH = "SOUTH";
	public static final String ORIENTATION_WEST = "WEST";
	public static final String ORIENTATION_EAST = "EAST";
	public static final String ASCEND = "ascend";
	public static final String DESCEND = "descend";
	

	private String name;
	private String orientation;
	private String direction;
	private String type;
	private String vectorAscension;
	private String lowerBound;
	private String upperBound;
	private String relations;

	public PortWrapper() {
	}
	
	public PortWrapper(Port port, String orient) {
		name = port.getName();
		direction = port.isIN() ? (DIRECTION_IN) : (DIRECTION_OUT);
		orientation = orient;
		
		if (port.isScalar()) {
			type = STD_LOGIC;
		} else {
			type = STD_LOGIC_VECTOR;
			lowerBound = String.valueOf(port.getFrom());
			upperBound = String.valueOf(port.getTo());
			vectorAscension = port.isTO() ? (ASCEND) : (DESCEND);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name.trim().equals("")) {
			name = null;
		}
		this.name = name;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		if (orientation.trim().equals("")) {
			orientation = null;
		}
		this.orientation = orientation;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		if (direction.trim().equals("")) {
			direction = null;
		}
		this.direction = direction;
	}

	public String getType() {
		return type.toLowerCase();
	}

	public void setType(String type) {
		if (type.trim().equals("")) {
			type = null;
		}
		this.type = type;
	}

	public String getVectorAscension() {
		return vectorAscension;
	}

	public void setVectorAscension(String vectorAscension) {
		if (vectorAscension.trim().equals("")) {
			vectorAscension = null;
		}
		this.vectorAscension = vectorAscension;
	}

	public String getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(String lowerBound) {
		if (lowerBound.trim().equals("")) {
			lowerBound = null;
		}
		this.lowerBound = lowerBound;
	}

	public String getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(String upperBound) {
		if (upperBound.trim().equals("")) {
			upperBound = null;
		}
		this.upperBound = upperBound;
	}

	@Override
	public String toString() {
		return "Port {" +
			name + ", " + 
			orientation + ", " + 
			direction + ", " + 
			type + ", " + 
			vectorAscension + ", " + 
			lowerBound + ", " + 
			upperBound + "}"; 
	}

	public void setRelations(String relations) {
		this.relations = relations;
	}

	public String getRelations() {
		return relations;
	}
	
}