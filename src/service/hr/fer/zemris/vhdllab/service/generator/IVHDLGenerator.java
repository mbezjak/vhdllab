package hr.fer.zemris.vhdllab.service.generator;

import hr.fer.zemris.vhdllab.service.VHDLLabManager;

public interface IVHDLGenerator {

	String generateVHDL(File f, VHDLLabManager labman);
}
