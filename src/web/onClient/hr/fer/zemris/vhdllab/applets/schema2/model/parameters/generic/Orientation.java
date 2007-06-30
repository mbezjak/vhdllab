package hr.fer.zemris.vhdllab.applets.schema2.model.parameters.generic;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IGenericValue;





public class Orientation implements IGenericValue {
	public EOrientation orientation = EOrientation.NORTH;
	
	public Orientation() { }
	public Orientation(EOrientation ori) { orientation = ori; }
	
	public void deserialize(String code) {
		orientation = EOrientation.parse(code);
	}
	public String serialize() {
		return orientation.serialize();
	}
	public IGenericValue copyCtor() {
		return new Orientation(this.orientation);
	}
}







