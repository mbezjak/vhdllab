package hr.fer.zemris.vhdllab;

import junit.framework.JUnit4TestAdapter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	hr.fer.zemris.vhdllab.servlets.AllPackageTests.class,
	hr.fer.zemris.vhdllab.servlets.dispatch.AllPackageTests.class,
	hr.fer.zemris.vhdllab.servlets.methods.AllPackageTests.class
})
public class AllOnServerJUnitTests {
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AllOnServerJUnitTests.class);
	}
	
}
