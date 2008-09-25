package hr.fer.zemris.vhdllab.service.generators;

import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.SchemaInfo2VHDL;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLGenerator;

import java.io.StringReader;
import java.util.Properties;



/**
 * Generira VHDL iz zadane datoteke.
 * 
 * @author Axel
 *
 */
public class SchemaVHDLGenerator implements VHDLGenerator {

    /*
     * (non-Javadoc)
     *
     * @see hr.fer.zemris.vhdllab.service.Functionality#configure(java.util.Properties)
     */
    @Override
    public void configure(Properties properties) {
    }

    /*
     * (non-Javadoc)
     *
     * @see hr.fer.zemris.vhdllab.service.VHDLGenerator#execute(hr.fer.zemris.vhdllab.entities.File)
     */
    @Override
    public VHDLGenerationResult execute(File file) throws ServiceException {
		SchemaDeserializer sd = new SchemaDeserializer();
		ISchemaInfo info = sd.deserializeSchema(new StringReader(file.getData()));
		
		SchemaInfo2VHDL vhdlgen = new SchemaInfo2VHDL();
		return vhdlgen.generateVHDL(info);
	}

}




