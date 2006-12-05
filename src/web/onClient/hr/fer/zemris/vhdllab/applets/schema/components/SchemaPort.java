package hr.fer.zemris.vhdllab.applets.schema.components;


/**
 * Ovo je port za std_logic.
 * @author Axel
 *
 */
public class SchemaPort extends AbstractSchemaPort {

	@Override
	public String getTypeID() {
		return "SchemaPort";
	}

	@Override
	protected String serializeSpecific() {
		return "";
	}

	@Override
	protected void deserializeSpecific(String code) {
		// apsolutno nist :)
	}

	
	
}
