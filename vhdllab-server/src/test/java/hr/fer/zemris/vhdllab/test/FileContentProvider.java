package hr.fer.zemris.vhdllab.test;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;

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
	private static final Map<FileType, List<NameAndContent>> contents;

	static {
		contents = new HashMap<FileType, List<NameAndContent>>();
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
		contents.put(FileType.SOURCE, new ArrayList<NameAndContent>());
		contents.put(FileType.PREDEFINED, new ArrayList<NameAndContent>());
		contents.put(FileType.SCHEMA, new ArrayList<NameAndContent>());
		contents.put(FileType.AUTOMATON, new ArrayList<NameAndContent>());
		contents.put(FileType.TESTBENCH, new ArrayList<NameAndContent>());

		List<NameAndContent> list = contents.get(FileType.SOURCE);
		list.add(readFile("comp_and"));
		list.add(readFile("comp_or"));
		list.add(readFile("comp_or2"));
		list.add(readFile("comp_oror"));
		list.add(readFile("complex_source"));
		list.add(readFile("ultra_complex_source"));

		list = contents.get(FileType.TESTBENCH);
		list.add(readFile("comp_and_tb"));
		list.add(readFile("complex_source_tb"));
		list.add(readFile("ultra_complex_source_tb"));
		list.add(readFile("comp_oror_tb"));
		
		list = contents.get(FileType.PREDEFINED);
        list.add(readFile("VL_OR"));

        list = contents.get(FileType.AUTOMATON);
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
		return new NameAndContent(new Caseless(name), content.toString());
	}

	/**
	 * Retrieves a realistic file contents for specified file <code>type</code>.
	 *
	 * @param type
	 *            a file type for whom to retrieve content
	 * @return a list file name and content
	 */
	public static List<NameAndContent> getContent(FileType type) {
		return contents.get(type);
	}

}
