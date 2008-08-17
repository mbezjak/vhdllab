package hr.fer.zemris.vhdllab.servlets.initialize.preferencesFiles;

import hr.fer.zemris.vhdllab.preferences.global.Preferences;
import hr.fer.zemris.vhdllab.preferences.global.PreferencesParser;
import hr.fer.zemris.vhdllab.preferences.global.Property;
import hr.fer.zemris.vhdllab.utilities.FileUtil;

import java.io.InputStream;
import java.util.Properties;

public class PreferencesInitializer {
	
	private static final String PREFERENCES_FILES = "preferencesFiles.properties";
	private static final String PREFERENCES_FILES_LIST = "files";
	private static final String PREFERENCES_FILES_SEPARATOR = ";";

	private static final PreferencesInitializer INSTANCE = new PreferencesInitializer();
	private Preferences preferences;
	private Properties sources;

	/**
	 * Private constructor
	 */
	private PreferencesInitializer() {
		super();
		sources = new Properties();
	}

	/**
	 * Returns an instance of preferences parser.
	 * 
	 * @return an instance of preferences parser
	 */
	public static PreferencesInitializer instance() {
		return INSTANCE;
	}

	public synchronized Preferences getPreferences() {
		if (preferences == null) {
			preferences = parsePreferences();
		}
		return preferences;
	}

	public synchronized String getSource(String id) {
		return sources.getProperty(id);
	}

	private Preferences parsePreferences() {
		Preferences pref = new Preferences();
		Class<?> clazz = this.getClass();
		InputStream is = clazz
				.getResourceAsStream(PREFERENCES_FILES);
		Properties p = FileUtil.getProperties(is);
		String fileList = p
				.getProperty(PREFERENCES_FILES_LIST);
		String[] files = fileList
				.split(PREFERENCES_FILES_SEPARATOR);
		for (String s : files) {
			s = s.trim();
			if (s.equals(""))
				continue;
			String data = FileUtil.readFile(clazz.getResourceAsStream(s));
			Property property = PreferencesParser.parseProperty(data);
			pref.addProperty(property);
			sources.setProperty(property.getId(), data);
		}
		return pref;
	}

}
