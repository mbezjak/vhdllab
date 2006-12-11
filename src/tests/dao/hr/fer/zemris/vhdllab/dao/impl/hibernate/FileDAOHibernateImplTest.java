package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
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
		project.setOwnerID("user100");
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
		fileDAO.load(Long.valueOf(121));
	}
	
	@Test
	public void save() throws DAOException {
		file.setProject(null);

		fileDAO.save(file);
		File file2 = fileDAO.load(file.getId());
		assertEquals(file, file2);
	}
	
	@Test(expected=DAOException.class)
	public void delete() throws DAOException {
		fileDAO.delete(file.getId());
		fileDAO.load(file.getId());
	}

	@Test
	public void exists() throws DAOException {
		assertEquals(false, fileDAO.exists(Long.valueOf(121)));
	}
	
	@Test
	public void exists2() throws DAOException {
		assertEquals(true, fileDAO.exists(Long.valueOf(file.getId())));
	}
	
	@Test
	public void exists3() throws DAOException {
		assertEquals(false, fileDAO.exists(Long.valueOf(121), file.getFileName()));
	}
	
	@Test
	public void exists4() throws DAOException {
		assertEquals(true, fileDAO.exists(project.getId(), file.getFileName()));
	}
	
	@Test
	public void findByName() throws DAOException {
		assertEquals(file, fileDAO.findByName(project.getId(), file.getFileName()));
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(FileDAOHibernateImplTest.class);
	}
}
