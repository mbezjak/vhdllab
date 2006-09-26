package hr.fer.zemris.vhdllab.dao.impl;

import junit.framework.JUnit4TestAdapter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	FileDAOMySQLImplTest.class,
	ProjectDAOMySQLImplTest.class
})
public class AllPackageTests {
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AllPackageTests.class);
	}
	
}