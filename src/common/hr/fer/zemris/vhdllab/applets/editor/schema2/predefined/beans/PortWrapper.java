package hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans;

public class PortWrapper {

	private String name;
	private String orientation;
	private String direction;
	private String type;
	private String vectorAscension;
	private String lowerBound;
	private String upperBound;

	public PortWrapper() {
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
		return type;
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
	
}