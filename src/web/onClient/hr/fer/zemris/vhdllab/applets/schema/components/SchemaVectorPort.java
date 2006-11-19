package hr.fer.zemris.vhdllab.applets.schema.components;


/**
 * Port za std_logic_vector.
 * @author Axel
 *
 */
public class SchemaVectorPort extends AbstractSchemaPort {
	private int portSize;

	public SchemaVectorPort() {
		portSize = 1;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaPort#getPortSize()
	 */
	@Override
	public int getPortSize() {
		return portSize;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaPort#setPortSize(int)
	 */
	@Override
	public void setPortSize(int ps) {
		portSize = ps;
	}
	
	
	
}
