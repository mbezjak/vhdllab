package hr.fer.zemris.vhdllab.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.dao.impl.EntityManagerUtil;
import hr.fer.zemris.vhdllab.dao.impl.UserFileDAOImpl;
import hr.fer.zemris.vhdllab.entities.UserFile;
import hr.fer.zemris.vhdllab.server.FileTypes;
import hr.fer.zemris.vhdllab.service.UserFileManager;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Very simple test case for {@link UserFileManagerImpl}.
 * 
 * @author Miro Bezjak
 */
public class UserFileManagerImplTest {

	@BeforeClass
	public static void initOnce() {
		manager = new UserFileManagerImpl(new UserFileDAOImpl());
		EntityManagerUtil.createEntityManagerFactory();
	}

	private static UserFileManager manager;

	/**
	 * Test save and delete.
	 */
	@Test
	public void saveAndDelete() throws Exception {
		EntityManagerUtil.currentEntityManager();
		UserFile file = new UserFile("user.id", "file.name",
				FileTypes.PREFERENCES_USER);
		assertFalse("file already exists.", manager.exists(file.getUserId(),
				file.getName()));

		manager.save(file);
		assertTrue("file doesn't exist.", manager.exists(file.getId()));
		assertTrue("file doesn't exist.", manager.exists(file.getUserId(), file
				.getName()));

		manager.delete(file.getId());
		assertFalse("file not deleted.", manager.exists(file.getId()));
		assertFalse("file not deleted.", manager.exists(file.getUserId(), file
				.getName()));
		EntityManagerUtil.closeEntityManager();
	}

}
