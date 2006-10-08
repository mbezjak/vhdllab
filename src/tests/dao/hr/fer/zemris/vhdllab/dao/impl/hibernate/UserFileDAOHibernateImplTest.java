package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.model.UserFile;

import java.util.ArrayList;
import java.util.List;

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
	public void saveAndLoad() throws DAOException {
		UserFile file = new UserFile();
		file.setContent("simple content of global file!");
		file.setOwnerID(Long.valueOf(150));
		file.setType(UserFile.UFT_THEME);

		fileDAO.save(file);
		UserFile file2 = fileDAO.load(file.getId());
		assertEquals(file, file2);
	}

	@Test(expected=DAOException.class)
	public void delete() throws DAOException {
		UserFile file = new UserFile();
		file.setContent("simple content of global file5!");
		file.setOwnerID(Long.valueOf(150));
		file.setType(UserFile.UFT_THEME);

		fileDAO.save(file);
		fileDAO.delete(file.getId());
		fileDAO.load(file.getId());
	}

	@Test
	public void findByUser() throws DAOException {
		UserFile file = new UserFile();
		file.setContent("simple content of global file2!");
		file.setOwnerID(Long.valueOf(210));
		file.setType(UserFile.UFT_THEME);

		UserFile file2 = new UserFile();
		file2.setContent("simple content of global file3!");
		file2.setOwnerID(Long.valueOf(200));
		file2.setType(UserFile.UFT_APPLET);

		UserFile file3 = new UserFile();
		file3.setContent("simple content of global file4!");
		file3.setOwnerID(Long.valueOf(200));
		file3.setType(UserFile.UFT_APPLET);

		fileDAO.save(file);
		fileDAO.save(file2);
		fileDAO.save(file3);

		List<UserFile> fileList = new ArrayList<UserFile>();
		fileList.add(file2);
		fileList.add(file3);

		assertEquals(fileList, fileDAO.findByUser(Long.valueOf(200)));
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(UserFileDAOHibernateImplTest.class);
	}
}