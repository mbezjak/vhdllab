package hr.fer.zemris.vhdllab;

import junit.framework.JUnit4TestAdapter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	hr.fer.zemris.vhdllab.model.AllPackageTests.class,
	hr.fer.zemris.vhdllab.dao.impl.AllPackageTests.class
})
public class AllDAOJUnitTests {
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AllDAOJUnitTests.class);
	}
	
}
