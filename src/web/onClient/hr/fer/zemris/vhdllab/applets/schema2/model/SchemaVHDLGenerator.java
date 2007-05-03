package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.generator.IVHDLGenerator;



/**
 * Generira VHDL iz zadane datoteke.
 * 
 * @author Axel
 *
 */
public class SchemaVHDLGenerator implements IVHDLGenerator {

	public String generateVHDL(File f, VHDLLabManager labman) throws ServiceException {
		// TODO: otvoriti datoteku
		// TODO: sadrzajem napuniti ISchemaInfo objekt
		ISchemaInfo info = null;
		
		SchemaInfo2VHDL vhdlgen = new SchemaInfo2VHDL();
		
		return vhdlgen.generateVHDL(info);
	}

}
