package hr.fer.zemris.vhdllab.applets.schema2.temporary;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaCore;



public class Tester {

	/**
	 * Privremeno radi testiranja.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ISchemaCore core = new SchemaCore();
		
		core.addListener(EPropertyChange.ANY_CHANGE, new TestListener());
	}

}
