package hr.fer.zemris.delab.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllPackageTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new TestSuite(ExtractorTest.class));
		suite.addTest(new TestSuite(DirectionTest.class));
		suite.addTest(new TestSuite(DefaultTypeTest.class));
		suite.addTest(new TestSuite(DefaultPortTest.class));
		suite.addTest(new TestSuite(DefaultCircuitInterfaceTest.class));
		return suite;
	}
}
