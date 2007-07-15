package hr.fer.zemris.vhdllab.service.dependency.schema;

import hr.fer.zemris.vhdllab.applets.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.dependency.IDependency;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;











public class SchemaDependency implements IDependency {
	
	/* static fields */
	
	
	/* private fields */
	
	

	/* ctors */	
	

	
	/* methods */

	public List<File> extractDependencies(File f, VHDLLabManager labman) throws ServiceException {
		List<File> retlist = new ArrayList<File>();
		SchemaDeserializer sd = new SchemaDeserializer();
		ISchemaInfo info = sd.deserializeSchema(new StringReader(f.getContent()));
		ISchemaComponentCollection components = info.getComponents();
		
		for (PlacedComponent placed : components) {
			String filename = placed.comp.getCodeFileName();
			if (filename == null || filename.trim().equals("")) continue;
			File dependency = labman.findFileByName(f.getProject().getId(), filename);
			retlist.add(dependency);
		}
		
		retlist.add(labman.getPredefinedFile(Constants.PREDEFINED_FILENAME, true)); // TODO: pitaj miru da l ovdje treba ic true
		
		return retlist;
	}
	
}














