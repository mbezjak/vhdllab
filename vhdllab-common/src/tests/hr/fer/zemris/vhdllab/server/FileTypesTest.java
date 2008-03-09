package hr.fer.zemris.vhdllab.server;

import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.server.conf.ServerConf;
import hr.fer.zemris.vhdllab.server.conf.ServerConfParser;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

/**
 * A test case for {@link FileTypes} class.
 * 
 * @author Miro Bezjak
 */
public class FileTypesTest {

	/**
	 * Check that every field in {@link FileTypes} is 'public static final
	 * java.lang.String' and that no two fields contains the same value.
	 */
	@Test
	public void valuesAndTypes() throws Exception {
		Class<FileTypes> clazz = FileTypes.class;
		Set<String> values = new HashSet<String>();
		for (Field f : clazz.getDeclaredFields()) {
			int modifiers = f.getModifiers();
			if (!Modifier.isPublic(modifiers)) {
				fail("Field " + f.getName() + " is not public");
			}
			if (!Modifier.isStatic(modifiers)) {
				fail("Field " + f.getName() + " is not static");
			}
			if (!Modifier.isFinal(modifiers)) {
				fail("Field " + f.getName() + " is not final");
			}
			if (!f.getType().getCanonicalName().equals("java.lang.String")) {
				fail("Field " + f.getName()
						+ " is not of type java.lang.String");
			}
			String value = (String) f.get(null);
			if (values.contains(value)) {
				fail("Duplicate value " + value + " for field " + f.getName());
			}
			values.add(value);
		}
	}

	/**
	 * Check that every value defined in {@link FileTypes} is defined in main
	 * server configuration file and vice versa.
	 */
	@Test
	public void equalToConfValues() throws Exception {
		Class<FileTypes> clazz = FileTypes.class;
		Set<String> values = new HashSet<String>();
		for (Field f : clazz.getDeclaredFields()) {
			String value = (String) f.get(null);
			values.add(value);
		}

		ServerConf conf = ServerConfParser.getConfiguration();
		Set<String> definedFileTypes = conf.getFileTypes();
		for (String v : values) {
			boolean found = false;
			for (Iterator<String> it = definedFileTypes.iterator(); it
					.hasNext();) {
				String fileType = it.next();
				if (fileType.equalsIgnoreCase(v)) {
					it.remove();
					found = true;
				}
			}
			if (!found) {
				fail("File type " + v + " not defined in server configuration!");
			}
		}
		if (!definedFileTypes.isEmpty()) {
			fail("File types "
					+ definedFileTypes
					+ " defined in server configuration not defined in FileTypes!");
		}
	}

}
