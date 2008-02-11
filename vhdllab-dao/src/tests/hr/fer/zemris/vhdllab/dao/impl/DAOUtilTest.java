package hr.fer.zemris.vhdllab.dao.impl;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.UserFile;

import org.junit.Test;

/**
 * Test case for {@link DAOUtil} class.
 * 
 * @author Miro Bezjak
 */
public class DAOUtilTest {

	/**
	 * Class is null
	 */
	@Test(expected = NullPointerException.class)
	public void columnLengthFor() throws Exception {
		DAOUtil.columnLengthFor(null, "name");
	}

	/**
	 * Property is null
	 */
	@Test(expected = NullPointerException.class)
	public void columnLengthFor2() throws Exception {
		DAOUtil.columnLengthFor(UserFile.class, null);
	}

	/**
	 * Property is empty string
	 */
	@Test(expected = IllegalArgumentException.class)
	public void columnLengthFor3() throws Exception {
		DAOUtil.columnLengthFor(UserFile.class, "");
	}

	/**
	 * non-existing property
	 */
	@Test(expected = DAOException.class)
	public void columnLengthFor4() throws Exception {
		DAOUtil.columnLengthFor(UserFile.class, "nonExistingProperty");
	}

	/**
	 * Length information is not available
	 */
	@Test(expected = IllegalArgumentException.class)
	public void columnLengthFor5() throws Exception {
		DAOUtil.columnLengthFor(File.class, "project");
	}

	/**
	 * Length information is located in Column annotation of a getter.
	 */
	@Test
	public void columnLengthFor6() throws Exception {
		int length = DAOUtil.columnLengthFor(UserFile.class, "name");
		assertEquals("Wrong column length", 255, length);
	}

	/**
	 * Length information is located in AttributeOverride annotation of a class.
	 */
	@Test
	public void columnLengthFor7() throws Exception {
		int length = DAOUtil.columnLengthFor(UserFile.class, "content");
		assertEquals("Wrong column length", 65535, length);
	}

}
