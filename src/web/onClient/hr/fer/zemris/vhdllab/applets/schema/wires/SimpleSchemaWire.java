package hr.fer.zemris.vhdllab.applets.schema.wires;

public class SimpleSchemaWire extends AbstractSchemaWire {
	

	public SimpleSchemaWire(String wireName) {
		super(wireName);
	}	

	@Override
	protected String serializeSpecific() {
		return "";
	}

	@Override
	protected boolean deserializeSpecific(String serial) {
		return true;
	}

}
