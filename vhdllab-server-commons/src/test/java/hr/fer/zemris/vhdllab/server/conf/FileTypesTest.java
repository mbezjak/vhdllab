package hr.fer.zemris.vhdllab.server.conf;

import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.api.FileTypes;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

/**
 * A mutual test case for {@link FileTypes} class and server configuration.
 * 
 * @author Miro Bezjak
 */
public class FileTypesTest {

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
