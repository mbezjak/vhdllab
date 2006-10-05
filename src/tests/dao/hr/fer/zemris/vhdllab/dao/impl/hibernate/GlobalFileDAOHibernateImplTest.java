package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import static org.junit.Assert.assertEquals;
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
	public void load() throws DAOException {
		GlobalFile file = new GlobalFile();
		file.setContent("simple content of global file!");
		file.setName("SimpleGlobalFileName");
		file.setType(GlobalFile.GFT_THEME);
		
		fileDAO.save(file);
		GlobalFile file2 = fileDAO.load(file.getId());
		assertEquals(file, file2);
	}

	@Test
	public void save() throws DAOException {
		GlobalFile file = new GlobalFile();
		file.setContent("simple content of global file!");
		file.setName("SimpleGlobalFileName");
		file.setType(GlobalFile.GFT_THEME);
		
		fileDAO.save(file);
		GlobalFile file2 = fileDAO.load(file.getId());
		assertEquals(file, file2);

	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(GlobalFileDAOHibernateImplTest.class);
	}
}
