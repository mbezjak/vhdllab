package hr.fer.zemris.vhdllab.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.dao.impl.EntityManagerUtil;
import hr.fer.zemris.vhdllab.dao.impl.LibraryDAOImpl;
import hr.fer.zemris.vhdllab.dao.impl.LibraryFileDAOImpl;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.service.LibraryFileManager;
import hr.fer.zemris.vhdllab.service.LibraryManager;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Very simple test case for {@link LibraryFileManagerImpl}.
 * 
 * @author Miro Bezjak
 */
public class LibraryFileManagerImplTest {

	private static LibraryFileManager manager;
	private static LibraryManager libraryManager;

	@BeforeClass
	public static void initOnce() {
		manager = new LibraryFileManagerImpl(new LibraryFileDAOImpl());
		libraryManager = new LibraryManagerImpl(new LibraryDAOImpl());
		EntityManagerUtil.createEntityManagerFactory();
	}

	/**
	 * Test save and delete.
	 */
	@Test
	public void saveAndDelete() throws Exception {
		EntityManagerUtil.currentEntityManager();
		Library library = new Library("library.name");
		libraryManager.save(library);

		LibraryFile file = new LibraryFile(library, "file.name",
				FileTypes.VHDL_SOURCE);
		manager.save(file);
		assertTrue("file doesn't exist.", manager.exists(file.getId()));
		assertTrue("file doesn't exist.", manager.exists(library.getId(), file
				.getName()));

		manager.delete(file.getId());
		assertFalse("file not deleted.", manager.exists(file.getId()));
		assertFalse("file not deleted.", manager.exists(library.getId(),
				file.getName()));

		libraryManager.delete(library.getId());
		EntityManagerUtil.closeEntityManager();
	}

}
