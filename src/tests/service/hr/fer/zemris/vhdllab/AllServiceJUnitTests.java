package hr.fer.zemris.vhdllab;

import junit.framework.JUnit4TestAdapter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	hr.fer.zemris.vhdllab.vhdl.model.AllPackageTests.class,
	hr.fer.zemris.vhdllab.vhdl.tb.AllPackageTests.class
})
public class AllServiceJUnitTests {
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AllServiceJUnitTests.class);
	}
	
}
