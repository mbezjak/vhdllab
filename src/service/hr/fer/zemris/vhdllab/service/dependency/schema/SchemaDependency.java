package hr.fer.zemris.vhdllab.service.dependency.schema;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EComponentType;
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
			File dependency;
			if (placed.comp.getComponentType().equals(EComponentType.PREDEFINED)) {
				dependency = labman.getPredefinedFile(filename, false);
			} else {
				dependency = labman.findFileByName(f.getProject().getId(), filename);
			}
			if (dependency != null) retlist.add(dependency);
		}
		
		return retlist;
	}
	
}














