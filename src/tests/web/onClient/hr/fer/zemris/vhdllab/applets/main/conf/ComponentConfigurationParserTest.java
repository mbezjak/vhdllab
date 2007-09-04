package hr.fer.zemris.vhdllab.applets.main.conf;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;

/**
 * Tests {@link ComponentConfigurationParser} dialog, but just to see if its
 * working. This is not an actual JUnit test.
 * 
 * @author Miro Bezjak
 */
public class ComponentConfigurationParserTest {

	/**
	 * Start of test. Not a JUnit test! Just testing to see if its working.
	 * 
	 * @param args
	 *            no effect
	 * @throws UniformAppletException
	 *             there is nothing to do with exception
	 */
	public static void main(String[] args) throws UniformAppletException {
		ComponentConfiguration conf = ComponentConfigurationParser
				.getConfiguration();
		System.out.println(conf);
	}
}
