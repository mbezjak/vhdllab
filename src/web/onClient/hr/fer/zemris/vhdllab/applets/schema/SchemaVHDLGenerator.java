package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.generator.IVHDLGenerator;

public class SchemaVHDLGenerator implements IVHDLGenerator {
	public String generateVHDL(File f, VHDLLabManager labman) throws ServiceException {
		// TODO Auto-generated method stub
		// SchemaSerializableInformation info = getSerializableInfo(f, labman); Rajakovic
		// return generateVHDLFromSerializableInfo(info);
		return null;
	}
	
	private SchemaSerializableInformation getSerializableInfo(File f, VHDLLabManager labman) {
		// TODO Ovo je Rajakovicev posel
		return null;
	}
	
	private String generateVHDLFromSerializableInfo(SchemaSerializableInformation info) {
		return null;
	}
}
