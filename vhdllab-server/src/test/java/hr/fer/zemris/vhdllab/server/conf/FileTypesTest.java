package hr.fer.zemris.vhdllab.server.conf;

import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.entities.FileType;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

/**
 * A mutual test case for {@link FileType} class and server configuration.
 * 
 * @author Miro Bezjak
 */
public class FileTypesTest {

	/**
	 * Check that every value defined in {@link FileType} is defined in main
	 * server configuration file and vice versa.
	 */
	@Test
	public void equalToConfValues() throws Exception {
		ServerConf conf = ServerConfParser.getConfiguration();
		Set<FileType> definedFileTypes = conf.getFileTypes();
		for (FileType v : FileType.values()) {
			boolean found = false;
			for (Iterator<FileType> it = definedFileTypes.iterator(); it
					.hasNext();) {
			    FileType fileType = it.next();
				if (fileType.equals(v)) {
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
