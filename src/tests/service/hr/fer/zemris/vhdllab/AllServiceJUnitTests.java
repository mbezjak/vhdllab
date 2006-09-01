package hr.fer.zemris.vhdllab;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllServiceJUnitTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(hr.fer.zemris.vhdllab.vhdl.model.AllPackageTests.suite());
		suite.addTest(hr.fer.zemris.vhdllab.vhdl.tb.AllPackageTests.suite());
		return suite;
	}
}
