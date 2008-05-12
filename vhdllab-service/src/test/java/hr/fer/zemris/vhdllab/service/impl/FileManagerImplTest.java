package hr.fer.zemris.vhdllab.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.dao.impl.EntityManagerUtil;
import hr.fer.zemris.vhdllab.dao.impl.FileDAOImpl;
import hr.fer.zemris.vhdllab.dao.impl.ProjectDAOImpl;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.service.FileManager;
import hr.fer.zemris.vhdllab.service.ProjectManager;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Very simple test case for {@link FileManagerImpl}.
 *
 * @author Miro Bezjak
 */
public class FileManagerImplTest {

	private static FileManager manager;
	private static ProjectManager projectManager;

	@BeforeClass
	public static void initOnce() {
		manager = new FileManagerImpl(new FileDAOImpl());
		projectManager = new ProjectManagerImpl(new ProjectDAOImpl());
		EntityManagerUtil.createEntityManagerFactory();
	}

	/**
	 * Test save and delete.
	 */
	@Test
	public void saveAndDelete() throws Exception {
		EntityManagerUtil.currentEntityManager();
		Project project = new Project("user.id", "project_name");
		projectManager.save(project);

		File file = new File(project, "file_name", FileTypes.VHDL_SOURCE);
		manager.save(file);
		assertTrue("file doesn't exist.", manager.exists(file.getId()));
		assertTrue("file doesn't exist.", manager.exists(project.getId(), file
				.getName()));

		manager.delete(file.getId());
		assertFalse("file not deleted.", manager.exists(file.getId()));
		assertFalse("file not deleted.", manager.exists(project.getId(), file
				.getName()));

		projectManager.delete(project.getId());
		EntityManagerUtil.closeEntityManager();
	}

}
