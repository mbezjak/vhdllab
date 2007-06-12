package hr.fer.zemris.vhdllab.applets.schema2.temporary;

import hr.fer.zemris.vhdllab.applets.schema2.dummies.DummyOR;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.model.LocalController;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaInfo2VHDL;



public class Tester {

	/**
	 * Privremeno radi testiranja.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ISchemaCore core = new SchemaCore();
		ISchemaController controller = new LocalController();
		
		controller.registerCore(core);
		
		controller.addListener(EPropertyChange.ANY_CHANGE, new TestListener());
		
		try {
			core.getSchemaInfo().getComponents().addComponent(0, 0, new DummyOR("OR1"));
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
		} catch (OverlapException e) {
			e.printStackTrace();
		}
		
		SchemaInfo2VHDL i2v = new SchemaInfo2VHDL();
		
		String vhdlcode = i2v.generateVHDL(core.getSchemaInfo());
		
		System.out.println(vhdlcode);
	}

}














