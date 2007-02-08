package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.model.UserFile;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserFileDAOHibernateImplTest {

	private static UserFileDAO fileDAO;

	private static UserFile file;

	@BeforeClass
	public static void init() {
		fileDAO = new UserFileDAOHibernateImpl();
	}

	@Before
	public void initEachTest() throws DAOException {
		file = new UserFile();
		file.setContent("simple content of global file!");
		file.setOwnerID("user100");
		file.setType(FileTypes.FT_THEME);
		fileDAO.save(file);
	}
	
	@Test
	public void saveAndLoad() throws DAOException {
		fileDAO.save(file);
		UserFile file2 = fileDAO.load(file.getId());
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
	public void findByUser() throws DAOException {
		UserFile file2 = new UserFile();
		file2.setContent("simple content of global file2!");
		file2.setOwnerID("user200");
		file2.setType(FileTypes.FT_APPLET);

		UserFile file3 = new UserFile();
		file3.setContent("simple content of global file3!");
		file3.setOwnerID("user200");
		file3.setType(FileTypes.FT_THEME);

		fileDAO.save(file2);
		fileDAO.save(file3);

		List<UserFile> fileList = new ArrayList<UserFile>();
		fileList.add(file2);
		fileList.add(file3);

		assertEquals(fileList, fileDAO.findByUser("user200"));
	}
	
	@Test
	public void findByUser2() throws DAOException {
		List<UserFile> files = fileDAO.findByUser("user111");
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

}
