package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.generator.IVHDLGenerator;

/**
 * Zasad na krivom mjestu. Samo zasad.
 * @author Axel
 *
 */
public class SchemaVHDLGenerator implements IVHDLGenerator {
	public String generateVHDL(File f, VHDLLabManager labman) throws ServiceException {
		SchemaSerializableInformation info = getSerializableInfo(f, labman);
		return generateVHDLFromSerializableInfo(info);
	}
	
	// iz internog formata gradi SchemaSerializableInformation
	private SchemaSerializableInformation getSerializableInfo(File f, VHDLLabManager labman) {
		// ne kuzim kak da iz tog labmana izvadim svoju shemu. Al recimo da je imam - na
		// prvo mjesto umjesto null ide njezin string.
		try {
			SDeserialization deser = new SDeserialization(null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// ovo je samo privremeno public (radi testiranja)!
	// kasnije cemo promijeniti u private
	public String generateVHDLFromSerializableInfo(SchemaSerializableInformation info) {
		SSInfo2VHDL converter = new SSInfo2VHDL();
		return converter.generateVHDLFromSerializableInfo(info);
	}
}
