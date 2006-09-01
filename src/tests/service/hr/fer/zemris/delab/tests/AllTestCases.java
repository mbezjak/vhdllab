package hr.fer.zemris.delab.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTestCases extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(AllPackageTests.suite());
		suite.addTest(hr.fer.zemris.delab.tests.tb.AllTBPackageTests.suite());
		return suite;
	}
}
