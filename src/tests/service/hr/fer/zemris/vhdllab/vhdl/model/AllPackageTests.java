package hr.fer.zemris.vhdllab.vhdl.model;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	DirectionTest.class,
	DefaultTypeTest.class,
	DefaultPortTest.class,
	DefaultCircuitInterfaceTest.class,
	ExtractorTest.class,
	CompileTest.class,
	SimulateTest.class,
	GenerateVHDLTest.class
})
public class AllPackageTests extends TestCase {
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AllPackageTests.class);
	}
	
}
