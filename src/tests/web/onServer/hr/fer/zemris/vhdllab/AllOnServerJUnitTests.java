package hr.fer.zemris.vhdllab;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This TestCase tests each Test in src/tests/web/onServer.
 */
public class AllOnServerJUnitTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(hr.fer.zemris.vhdllab.servlets.dispatch.AllPackageTests.suite());
		return suite;
	}
}
