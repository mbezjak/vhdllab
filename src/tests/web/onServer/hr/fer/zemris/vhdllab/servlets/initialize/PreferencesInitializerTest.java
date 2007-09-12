package hr.fer.zemris.vhdllab.servlets.initialize;

import hr.fer.zemris.vhdllab.preferences.global.Preferences;
import hr.fer.zemris.vhdllab.servlets.initialize.preferencesFiles.PreferencesInitializer;

/**
 * Tests {@link PreferencesInitializer}, but just to see if its working. This
 * is not an actual JUnit test.
 * 
 * @author Miro Bezjak
 */
public class PreferencesInitializerTest {

	/**
	 * Start of test. Not a JUnit test! Just testing to see if its working.
	 * 
	 * @param args
	 *            no effect
	 */
	public static void main(String[] args) {
		PreferencesInitializer parser = PreferencesInitializer.instance();
		Preferences preferences = parser.getPreferences();
		System.out.println(preferences);
	}

}
