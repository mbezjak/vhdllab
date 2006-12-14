package hr.fer.zemris.vhdllab.vhdl.tb;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	StringFormatTest.class,
	DefaultImpulseTest.class,
	DefaultSignalTest.class,
	DefaultGeneratorTest.class,
	TestbenchTest.class
})
public class AllPackageTests extends TestCase {
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AllPackageTests.class);
	}
	
}
