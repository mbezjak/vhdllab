package hr.fer.zemris.vhdllab;

import junit.framework.JUnit4TestAdapter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	
})
public class AllOnClientJUnitTests {

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AllOnClientJUnitTests.class);
	}
	
}
