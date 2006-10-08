package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.GlobalFileDAO;
import hr.fer.zemris.vhdllab.model.GlobalFile;
import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;

public class GlobalFileDAOHibernateImplTest {

	private static GlobalFileDAO fileDAO;

	@BeforeClass
	public static void init() {
		fileDAO = new GlobalFileDAOHibernateImpl();
	}

	@Test
	public void saveAndLoad() throws DAOException {
		GlobalFile file = new GlobalFile();
		file.setContent("simple content of global file!");
		file.setName("SimpleGlobalFileName");
		file.setType(GlobalFile.GFT_THEME);

		fileDAO.save(file);
		GlobalFile file2 = fileDAO.load(file.getId());
		assertEquals(file, file2);
	}

	@Test(expected=DAOException.class)
	public void delete() throws DAOException {
		GlobalFile file = new GlobalFile();
		file.setContent("simple content of global file4!");
		file.setName("SimpleGlobalFileName4");
		file.setType(GlobalFile.GFT_THEME);

		fileDAO.save(file);
		fileDAO.delete(file.getId());
		fileDAO.load(file.getId());
	}

	@Test
	public void findByType() throws DAOException {
		GlobalFile file = new GlobalFile();
		file.setContent("simple content of global file!");
		file.setName("SimpleGlobalFileName");
		file.setType(GlobalFile.GFT_THEME);

		GlobalFile file2 = new GlobalFile();
		file2.setContent("simple content of global file2!");
		file2.setName("SimpleGlobalFileName2");
		file2.setType(GlobalFile.GFT_APPLET);

		GlobalFile file3 = new GlobalFile();
		file3.setContent("simple content of global file3!");
		file3.setName("SimpleGlobalFileName3");
		file3.setType(GlobalFile.GFT_APPLET);

		fileDAO.save(file);
		fileDAO.save(file2);
		fileDAO.save(file3);
		
		List<GlobalFile> fileList = new ArrayList<GlobalFile>();
		fileList.add(file2);
		fileList.add(file3);
		
		assertEquals(fileList, fileDAO.findByType(GlobalFile.GFT_APPLET));
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(GlobalFileDAOHibernateImplTest.class);
	}
}
