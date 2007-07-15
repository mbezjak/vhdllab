package hr.fer.zemris.vhdllab.service.extractor.schema;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.extractor.ICircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.io.StringReader;





public class SchemaCircuitInterfaceExtractor implements ICircuitInterfaceExtractor {

	/* static fields */
	

	/* private fields */
	
	

	/* ctors */
	
	
	

	/* methods */

	public CircuitInterface extractCircuitInterface(File f) throws ServiceException {
		SchemaDeserializer sd = new SchemaDeserializer();
		ISchemaInfo info = sd.deserializeSchema(new StringReader(f.getContent()));
		
		return info.getEntity().getCircuitInterface(info);
	}
	
	
	
}








