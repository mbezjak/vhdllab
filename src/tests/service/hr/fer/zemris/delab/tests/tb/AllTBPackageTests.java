package hr.fer.zemris.delab.tests.tb;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTBPackageTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new TestSuite(StringUtilTest.class));
		suite.addTest(new TestSuite(DefaultImpulseTest.class));
		suite.addTest(new TestSuite(DefaultSignalTest.class));
		suite.addTest(new TestSuite(DefaultGeneratorTest.class));
		suite.addTest(new TestSuite(TestbenchTest.class));
		return suite;
	}
}
