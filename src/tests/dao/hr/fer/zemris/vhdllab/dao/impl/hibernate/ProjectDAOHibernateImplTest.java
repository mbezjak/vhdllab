package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.dao.impl.hibernate.ProjectDAOHibernateImpl;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;

public class ProjectDAOHibernateImplTest {

	private static ProjectDAO projectDAO;

	@BeforeClass
	public static void init() {
		projectDAO = new ProjectDAOHibernateImpl();
	}

	@Test
	public void saveAndLoad() throws DAOException {
		File file = new File();
		file.setContent("simple content of a file!");
		file.setFileName("sample name");
		file.setFileType(File.FT_VHDLSOURCE);

		Project project = new Project();
		project.setOwnerID(Long.valueOf(200));
		project.setProjectName("simple project");
		project.setFiles(new TreeSet<File>());
		project.getFiles().add(file);

		file.setProject(project);

		projectDAO.save(project);
		Project project2 = projectDAO.load(project.getId());
		assertEquals(project, project2);
	}

	@Test(expected=DAOException.class)
	public void delete() throws DAOException {
		File file = new File();
		file.setContent("simple content of a file4!");
		file.setFileName("sample name4");
		file.setFileType(File.FT_VHDLSOURCE);

		Project project = new Project();
		project.setOwnerID(Long.valueOf(200));
		project.setProjectName("simple project2");
		project.setFiles(new TreeSet<File>());
		project.getFiles().add(file);

		file.setProject(project);

		projectDAO.save(project);
		projectDAO.delete(project.getId());
		projectDAO.load(project.getId());
	}

	@Test
	public void findByUser() throws DAOException {
		List<Project> projects = new ArrayList<Project>();
		File file = new File();
		file.setContent("simple file!");
		file.setFileName("sample name");
		file.setFileType(File.FT_VHDLSOURCE);

		Project project = new Project();
		project.setOwnerID(Long.valueOf(400));
		project.setProjectName("simple project");
		project.setFiles(new TreeSet<File>());
		project.getFiles().add(file);

		file.setProject(project);
		projectDAO.save(project);

		projects.add(project);

		File file2 = new File();
		file2.setContent("simple file2!");
		file2.setFileName("sample name2");
		file2.setFileType(File.FT_VHDLTB);

		Project project2 = new Project();
		project2.setOwnerID(Long.valueOf(400));
		project2.setProjectName("simple project2");
		project2.setFiles(new TreeSet<File>());
		project2.getFiles().add(file2);

		file2.setProject(project2);
		projectDAO.save(project2);

		projects.add(project2);

		List<Project> projects2 = projectDAO.findByUser(Long.valueOf(400));
		assertEquals(projects, projects2);
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ProjectDAOHibernateImplTest.class);
	}
}
