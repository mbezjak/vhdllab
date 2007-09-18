package hr.fer.zemris.vhdllab.service.generator.schema;

import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.SchemaInfo2VHDL;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.generator.IVHDLGenerator;
import hr.fer.zemris.vhdllab.vhdl.VHDLGenerationResult;

import java.io.StringReader;



/**
 * Generira VHDL iz zadane datoteke.
 * 
 * @author Axel
 *
 */
public class SchemaVHDLGenerator implements IVHDLGenerator {

	public VHDLGenerationResult generateVHDL(File f, VHDLLabManager labman) throws ServiceException {
		SchemaDeserializer sd = new SchemaDeserializer();
		ISchemaInfo info = sd.deserializeSchema(new StringReader(f.getContent()));
		
		SchemaInfo2VHDL vhdlgen = new SchemaInfo2VHDL();
		String vhdl = vhdlgen.generateVHDL(info);
		
		if (vhdl == null) throw new ServiceException("Are there invalidated components or empty in ports?");
		
		return new VHDLGenerationResult(vhdl);
	}

}
