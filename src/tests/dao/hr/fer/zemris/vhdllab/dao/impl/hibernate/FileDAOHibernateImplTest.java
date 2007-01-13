package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;

import java.util.TreeSet;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileDAOHibernateImplTest {

	private static FileDAO fileDAO;

	private static File file;
	private static Project project;

	@BeforeClass
	public static void init() {
		fileDAO = new FileDAOHibernateImpl();
	}

	@Before
	public void initEachTest() throws DAOException {
		file = new File();
		project = new Project();
		project.setOwnerId("user100");
		project.setProjectName("simple name of a project");
		project.setFiles(new TreeSet<File>());
		project.getFiles().add(file);
		file.setContent("simple content of a file!");
		file.setFileName("sample name");
		file.setFileType(FileTypes.FT_VHDL_SOURCE);
		file.setProject(project);
		fileDAO.save(file);
	}

	@Test
	public void saveAndLoad() throws DAOException {
		fileDAO.save(file);
		File file2 = fileDAO.load(file.getId());
		assertEquals(file, file2);
	}

	@Test(expected=DAOException.class)
	public void load() throws DAOException {
		fileDAO.load(Long.valueOf(121000110001101L));
	}
	
	@Test(expected=DAOException.class)
	public void save() throws DAOException {
		file.setProject(null);
		fileDAO.save(file);
	}
	
	@Test//(expected=DAOException.class)
	public void delete() throws DAOException {
		File file2 = new File();
		file2.setContent("dsadsa");
		file2.setFileName("new file name");
		file2.setFileType(FileTypes.FT_VHDL_AUTOMAT);
		file2.setProject(project);
		fileDAO.save(file2);
		System.out.println(project);
		
		ProjectDAO projectDAO = new ProjectDAOHibernateImpl();
		project.getFiles().remove(file);
		projectDAO.save(project);
		//fileDAO.delete(file.getId());
		//fileDAO.load(file.getId());
		System.out.println(project.getId());
		System.out.println(file.getId());
		Project project2 = projectDAO.load(project.getId());
		System.out.println(project2);
		System.out.println(project);
		System.out.println(project == project2);
		System.out.println(project.hashCode() == project2.hashCode());
		System.out.println(project.equals(project2));
	}

	@Test
	public void exists() throws DAOException {
		assertEquals(false, fileDAO.exists(Long.valueOf(121000101001110L)));
	}
	
	@Test
	public void exists2() throws DAOException {
		assertEquals(true, fileDAO.exists(Long.valueOf(file.getId())));
	}
	
	@Test
	public void exists3() throws DAOException {
		assertEquals(false, fileDAO.exists(Long.valueOf(121101010100101L), file.getFileName()));
	}
	
	@Test
	public void exists4() throws DAOException {
		assertEquals(true, fileDAO.exists(project.getId(), file.getFileName()));
	}
	
	@Test
	public void exists5() throws DAOException {
		assertEquals(true, fileDAO.exists(project.getId(), file.getFileName().toUpperCase()));
	}
	
	@Test
	public void findByName() throws DAOException {
		assertEquals(file, fileDAO.findByName(project.getId(), file.getFileName()));
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(FileDAOHibernateImplTest.class);
	}
}
