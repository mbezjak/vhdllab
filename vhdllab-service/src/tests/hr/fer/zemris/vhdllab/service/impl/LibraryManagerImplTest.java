package hr.fer.zemris.vhdllab.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.dao.impl.EntityManagerUtil;
import hr.fer.zemris.vhdllab.dao.impl.LibraryDAOImpl;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.service.LibraryManager;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Very simple test case for {@link LibraryManagerImpl}.
 * 
 * @author Miro Bezjak
 */
public class LibraryManagerImplTest {

	private static LibraryManager manager;

	@BeforeClass
	public static void initOnce() {
		manager = new LibraryManagerImpl(new LibraryDAOImpl());
		EntityManagerUtil.createEntityManagerFactory();
	}

	/**
	 * Test save and delete.
	 */
	@Test
	public void saveAndDelete() throws Exception {
		EntityManagerUtil.currentEntityManager();
		Library library = new Library("library.name");
		assertFalse("library already exists.", manager
				.exists(library.getName()));

		manager.save(library);
		assertTrue("library doesn't exist.", manager.exists(library.getId()));
		assertTrue("library doesn't exist.", manager.exists(library.getName()));

		manager.delete(library.getId());
		assertFalse("library not deleted.", manager.exists(library.getId()));
		assertFalse("library not deleted.", manager.exists(library.getName()));
		EntityManagerUtil.closeEntityManager();
	}

}
