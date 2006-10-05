package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.model.UserFile;
import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;

public class UserFileDAOHibernateImplTest {

	private static UserFileDAO fileDAO;
	
	@BeforeClass
	public static void init() {
		fileDAO = new UserFileDAOHibernateImpl();
	}
	
	@Test
	public void load() throws DAOException {
		UserFile file = new UserFile();
		file.setContent("simple content of global file!");
		file.setOwnerID(Long.valueOf(150));
		file.setType(UserFile.UFT_THEME);
		
		fileDAO.save(file);
		UserFile file2 = fileDAO.load(file.getId());
		assertEquals(file, file2);
	}

	@Test
	public void save() throws DAOException {
		UserFile file = new UserFile();
		file.setContent("simple content of global file!");
		file.setOwnerID(Long.valueOf(150));
		file.setType(UserFile.UFT_THEME);
		
		fileDAO.save(file);
		UserFile file2 = fileDAO.load(file.getId());
		assertEquals(file, file2);
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(UserFileDAOHibernateImplTest.class);
	}
}
