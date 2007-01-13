package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.GlobalFileDAO;
import hr.fer.zemris.vhdllab.model.GlobalFile;
import hr.fer.zemris.vhdllab.model.Project;

import java.util.ArrayList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GlobalFileDAOHibernateImplTest {

	private static GlobalFileDAO fileDAO;

	private static GlobalFile file;

	@BeforeClass
	public static void init() {
		fileDAO = new GlobalFileDAOHibernateImpl();
	}

	@Before
	public void initEachTest() throws DAOException {
		file = new GlobalFile();
		file.setContent("simple content of global file!");
		file.setName("SimpleGlobalFileName");
		file.setType(FileTypes.FT_THEME);
		fileDAO.save(file);
	}
	@Test
	public void saveAndLoad() throws DAOException {
		fileDAO.save(file);
		GlobalFile file2 = fileDAO.load(file.getId());
		assertEquals(file, file2);
	}
	
	@Test(expected=DAOException.class)
	public void load() throws DAOException {
		fileDAO.load(Long.valueOf(121));
	}
	
	@Test(expected=DAOException.class)
	public void delete() throws DAOException {
		fileDAO.delete(file);
		fileDAO.load(file.getId());
	}

	@Test
	public void findByType() throws DAOException {
		GlobalFile file2 = new GlobalFile();
		file2.setContent("simple content of global file2!");
		file2.setName("SimpleGlobalFileName2");
		file2.setType(FileTypes.FT_APPLET);

		GlobalFile file3 = new GlobalFile();
		file3.setContent("simple content of global file3!");
		file3.setName("SimpleGlobalFileName3");
		file3.setType(FileTypes.FT_APPLET);

		fileDAO.save(file2);
		fileDAO.save(file3);
		
		List<GlobalFile> fileList = new ArrayList<GlobalFile>();
		fileList.add(file2);
		fileList.add(file3);
		
		assertEquals(fileList, fileDAO.findByType(FileTypes.FT_APPLET));
	}
	
	@Test
	public void findByUser2() throws DAOException {
		List<GlobalFile> files = fileDAO.findByType("new_type");
		assertEquals(new ArrayList<Project>(), files);
	}
	
	@Test
	public void exists() throws DAOException {
		assertEquals(false, fileDAO.exists(Long.valueOf(121)));
	}
	
	@Test
	public void exists2() throws DAOException {
		assertEquals(true, fileDAO.exists(file.getId()));
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(GlobalFileDAOHibernateImplTest.class);
	}
}
