package hr.fer.zemris.vhdllab.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import hr.fer.zemris.vhdllab.api.FileTypes;

import java.util.List;

import org.junit.Test;

/**
 * A test case for {@link FileContentProvider}.
 *
 * @author Miro Bezjak
 */
public class FileContentProviderTest {

	/**
	 * Test if content is read.
	 */
	@Test
	public void readContent() throws Exception {
		List<NameAndContent> list = FileContentProvider
				.getContent(FileTypes.VHDL_SOURCE);
		assertNotNull("file not read.", list);
		assertFalse("empty list.", list.size() == 0);
	}

}
