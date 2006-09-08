package hr.fer.zemris.vhdllab.servlets.dispatch;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllPackageTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new TestSuite(AdvancedMethodDispatcherTest.class));
		return suite;
	}
}
