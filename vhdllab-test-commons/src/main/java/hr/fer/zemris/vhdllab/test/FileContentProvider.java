package hr.fer.zemris.vhdllab.test;

import hr.fer.zemris.vhdllab.api.FileTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple utility that will retrieve a realistic file contents (i.e. files
 * that are actual VHDL components). This is useful when writing tests.
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class FileContentProvider {

	/**
	 * A map of file type -> file content
	 */
	private static final Map<String, List<NameAndContent>> contents;

	static {
		contents = new HashMap<String, List<NameAndContent>>();
		initContents();
	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private FileContentProvider() {
	}

	/**
	 * Initializes a contents map with provided files.
	 */
	private static void initContents() {
		contents.put(FileTypes.VHDL_SOURCE, new ArrayList<NameAndContent>());
		contents.put(FileTypes.VHDL_PREDEFINED, new ArrayList<NameAndContent>());
		contents.put(FileTypes.VHDL_SCHEMA, new ArrayList<NameAndContent>());
		contents.put(FileTypes.VHDL_AUTOMATON, new ArrayList<NameAndContent>());
		contents.put(FileTypes.VHDL_TESTBENCH, new ArrayList<NameAndContent>());

		List<NameAndContent> list = contents.get(FileTypes.VHDL_SOURCE);
		list.add(readFile("comp_and"));
		list.add(readFile("complex_source"));
		list.add(readFile("ultra_complex_source"));

		list = contents.get(FileTypes.VHDL_PREDEFINED);
        list.add(readFile("VL_OR"));

		list = contents.get(FileTypes.VHDL_SCHEMA);
		list.add(readFile("comp_or"));

		list = contents.get(FileTypes.VHDL_AUTOMATON);
		list.add(readFile("automaton"));
	}

	/**
	 * Reads and returns a file content for a file with specified
	 * <code>name</code>.
	 *
	 * @param name
	 *            a name of a file to read
	 * @return a file content and name
	 */
	private static NameAndContent readFile(String name) {
		ClassLoader cl = FileContentProvider.class.getClassLoader();
		InputStream is = cl.getResourceAsStream(name);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder content = new StringBuilder(5000);
		while (true) {
			char[] buffer = new char[500];
			int len;
			try {
				len = reader.read(buffer);
			} catch (IOException e) {
				try {
					reader.close();
				} catch (IOException ex) {
				}
				throw new ExceptionInInitializerError("Cant read file: " + name);
			}
			if (len < 0) {
				break;
			}
			content.append(buffer, 0, len);
		}
		try {
			reader.close();
		} catch (IOException e) {
		}
		return new NameAndContent(name, content.toString());
	}

	/**
	 * Retrieves a realistic file contents for specified file <code>type</code>.
	 *
	 * @param type
	 *            a file type for whom to retrieve content
	 * @return a list file name and content
	 */
	public static List<NameAndContent> getContent(String type) {
		return contents.get(type);
	}

}
