package hr.fer.zemris.vhdllab.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.dao.impl.EntityManagerUtil;
import hr.fer.zemris.vhdllab.dao.impl.ProjectDAOImpl;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.entities.StubFactory;
import hr.fer.zemris.vhdllab.service.ProjectManager;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Very simple test case for {@link ProjectManagerImpl}.
 *
 * @author Miro Bezjak
 */
public class ProjectManagerImplTest {

	private static ProjectManager manager;

	@BeforeClass
	public static void initOnce() {
		manager = new ProjectManagerImpl(new ProjectDAOImpl());
		EntityManagerUtil.createEntityManagerFactory();
	}

	/**
	 * Test save and delete.
	 */
	@Test
	public void saveAndDelete() throws Exception {
		EntityManagerUtil.currentEntityManager();
        Project project = StubFactory.create(Project.class, 400);
		assertFalse("project already exists.", manager.exists(project
				.getUserId(), project.getName()));

		manager.save(project);
		assertTrue("project doesn't exist.", manager.exists(project.getId()));
		assertTrue("project doesn't exist.", manager.exists(
				project.getUserId(), project.getName()));

		manager.delete(project.getId());
		assertFalse("project not deleted.", manager.exists(project.getId()));
		assertFalse("project not deleted.", manager.exists(project.getUserId(),
				project.getName()));
		EntityManagerUtil.closeEntityManager();
	}

}
