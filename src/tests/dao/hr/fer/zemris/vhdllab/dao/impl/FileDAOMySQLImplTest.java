package hr.fer.zemris.vhdllab.dao.impl;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;

import java.util.TreeSet;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;

public class FileDAOMySQLImplTest {

	private static FileDAO fileDAO;
	
	@BeforeClass
	public static void init() {
		fileDAO = new FileDAOMySQLImpl();
	}
	
	@Test
	public void load() throws DAOException {
		File file = new File();
		file.setContent("simple content of a file!");
		file.setFileName("sample name");
		file.setFileType(File.FT_VHDLSOURCE);
		
		Project project = new Project();
		project.setOwnerID(Long.valueOf(100));
		project.setProjectName("simple name of a project");
		project.setFiles(new TreeSet<File>());
		project.getFiles().add(file);
		
		file.setProject(project);
		
		fileDAO.save(file);
		File file2 = fileDAO.load(file.getId());
		assertEquals(file, file2);
	}

	@Test
	public void save() throws DAOException {
		File file = new File();
		file.setContent("simple content of a file2!");
		file.setFileName("sample name2");
		file.setFileType(File.FT_VHDLSOURCE);
		
		Project project = new Project();
		project.setOwnerID(Long.valueOf(200));
		project.setProjectName("simple name of a project2");
		project.setFiles(new TreeSet<File>());
		project.getFiles().add(file);
		
		file.setProject(project);
		
		fileDAO.save(file);
		File file2 = fileDAO.load(file.getId());
		assertEquals(file, file2);
	}
	
	@Test
	public void save2() throws DAOException {
		File file = new File();
		file.setContent("simple content of a file2!");
		file.setFileName("sample name2");
		file.setFileType(File.FT_VHDLSOURCE);

		fileDAO.save(file);
		File file2 = fileDAO.load(file.getId());
		assertEquals(file, file2);
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(FileDAOMySQLImplTest.class);
	}
}
