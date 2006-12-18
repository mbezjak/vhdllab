package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProjectDAOHibernateImplTest {

	private static ProjectDAO projectDAO;

	private static File file;
	private static Project project;

	@BeforeClass
	public static void init() {
		projectDAO = new ProjectDAOHibernateImpl();
	}

	@Before
	public void initEachTest() throws DAOException {
		project = new Project();
		file = new File();
		file.setContent("simple content of a file!");
		file.setFileName("sample name");
		file.setFileType(FileTypes.FT_VHDL_SOURCE);
		file.setProject(project);
		
		project.setOwnerId("user100");
		project.setProjectName("simple name of a project");
		project.setFiles(new TreeSet<File>());
		project.getFiles().add(file);
		projectDAO.save(project);
	}

	@Test
	public void saveAndLoad() throws DAOException {
		projectDAO.save(project);
		Project project2 = projectDAO.load(project.getId());
		assertEquals(project, project2);
	}
	
	@Test(expected=DAOException.class)
	public void load() throws DAOException {
		projectDAO.load(Long.valueOf(121));
	}

	@Test(expected=DAOException.class)
	public void delete() throws DAOException {
		projectDAO.delete(project.getId());
		projectDAO.load(project.getId());
	}

	@Test
	public void findByUser() throws DAOException {
		List<Project> projects = new ArrayList<Project>();

		File file2 = new File();
		file2.setContent("simple file2!");
		file2.setFileName("sample name2");
		file2.setFileType(FileTypes.FT_VHDL_TB);
		Project project2 = new Project();
		project2.setOwnerId("user101");
		project2.setProjectName("simple project2");
		project2.setFiles(new TreeSet<File>());
		project2.getFiles().add(file2);
		file2.setProject(project2);
		projectDAO.save(project2);
		projects.add(project2);
		
		Project project3 = new Project();
		project3.setOwnerId("user101");
		project3.setProjectName("name of third project");
		project3.setFiles(null);
		projectDAO.save(project3);
		projects.add(project3);

		List<Project> projects2 = projectDAO.findByUser(project2.getOwnerId());
		assertEquals(projects, projects2);
	}

	@Test
	public void findByUser2() throws DAOException {
		List<Project> projects = projectDAO.findByUser("user111");
		assertEquals(new ArrayList<Project>(), projects);
	}
	
	@Test
	public void exists() throws DAOException {
		assertEquals(false, projectDAO.exists(Long.valueOf(121)));
	}
	
	@Test
	public void exists2() throws DAOException {
		assertEquals(true, projectDAO.exists(project.getId()));
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ProjectDAOHibernateImplTest.class);
	}
}
