package hr.fer.zemris.vhdllab.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

public class FileTest {
	
	private File file;
	private File file2;
	
	@Before
	public void init() {
		file = new File();
		file.setId(Long.valueOf(0));
		file.setFileName("FileNameSample");
		file.setFileType(FileTypes.FT_VHDL_SOURCE);
		file.setContent("Content sample.");
		file.setProject(null);
		
		file2 = new File();
		file2.setId(Long.valueOf(0));
		file2.setFileName("FileNameSample");
		file2.setFileType(FileTypes.FT_VHDL_SOURCE);
		file2.setContent("Content sample.");
		file2.setProject(null);
	}
	
	/**
	 * Files are equal.
	 */
	@Test
	public void equalsAndHashCode() {
		assertEquals(true, file.equals(file2));
		assertEquals(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * Both file IDs are <code>null</code>.
	 */
	@Test
	public void equalsAndHashCode2() {
		file.setId(null);
		file2.setId(null);
		assertEquals(true, file.equals(file2));
		assertEquals(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * Both file IDs are <code>null</code> and filenames are
	 * not equal.
	 */
	@Test
	public void equalsAndHashCode3() {
		file.setId(null);
		file.setFileName("FileNameSample1");
		file2.setId(null);
		file2.setFileName("FileNameSample2");
		assertEquals(false, file.equals(file2));
		assertNotSame(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * One File IDs is <code>null</code> while other is not.
	 */
	@Test
	public void equalsAndHashCode4() {
		file.setId(null);
		file2.setId(Long.valueOf(1));
		assertEquals(false, file.equals(file2));
		assertNotSame(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * One File IDs is <code>null</code> while other is not
	 * and file types are not equal.
	 */
	@Test
	public void equalsAndHashCode5() {
		file.setId(null);
		file.setFileType(FileTypes.FT_VHDL_SOURCE);
		file2.setId(Long.valueOf(1));
		file2.setFileType(FileTypes.FT_VHDL_TB);
		assertEquals(false, file.equals(file2));
		assertNotSame(file.hashCode(), file2.hashCode());
	}

	/**
	 * Object is <code>null</code>.
	 */
	@Test
	public void equalsObject() {
		assertEquals(false, file.equals(null));
	}
	
	/**
	 * Object is not instance of <code>File</code>.
	 */
	@Test
	public void equalsObject2() {
		assertEquals(false, file.equals(new String("File")));
	}

	/**
	 * Files are equal.
	 */
	@Test
	public void compareTo() {
		assertEquals(0, file.compareTo(file2));
	}
	
	/**
	 * File IDs are not equal.
	 */
	@Test
	public void compareTo2() {
		file.setId(Long.valueOf(1));
		file2.setId(Long.valueOf(0));
		assertEquals(1, file.compareTo(file2));
	}
	
	/**
	 * File types are not equal.
	 */
	@Test
	public void compareTo3() {
		file.setFileType(FileTypes.FT_VHDL_SOURCE);
		file2.setFileType(FileTypes.FT_VHDL_TB);
		assertEquals(FileTypes.FT_VHDL_SOURCE.compareTo(FileTypes.FT_VHDL_TB), file.compareTo(file2));
	}
	
	/**
	 * File names are not equal.
	 */
	@Test
	public void compareTo4() {
		file.setFileName("FilenameSimple");
		file2.setFileName("SimpleFile");
		assertEquals("FilenameSimple".compareTo("SimpleFile"), file.compareTo(file2));
	}
	
	/**
	 * File names and types are not equal.
	 */
	@Test
	public void compareTo5() {
		file.setFileName("FilenameSimple");
		file.setFileType(FileTypes.FT_VHDL_SOURCE);
		file2.setFileName("SimpleFile");
		file2.setFileType(FileTypes.FT_VHDL_TB);
		assertEquals("FilenameSimple".compareTo("SimpleFile"), file.compareTo(file2));
	}
	
	/**
	 * File names and IDs are not equal.
	 */
	@Test
	public void compareTo6() {
		file.setId(Long.valueOf(0));
		file.setFileName("FilenameSimple");
		file2.setId(Long.valueOf(1));
		file2.setFileName("SimpleFile");
		assertEquals("FilenameSimple".compareTo("SimpleFile"), file.compareTo(file2));
	}
	
	/**
	 * File types and IDs are not equal.
	 */
	@Test
	public void compareTo7() {
		file.setId(Long.valueOf(1));
		file.setFileType(FileTypes.FT_VHDL_SOURCE);
		file2.setId(Long.valueOf(0));
		file2.setFileType(FileTypes.FT_VHDL_TB);
		assertEquals(FileTypes.FT_VHDL_SOURCE.compareTo(FileTypes.FT_VHDL_TB), file.compareTo(file2));
	}
	
	/**
	 * One file name is <code>null</code> while other is not.
	 */
	@Test
	public void compareTo8() {
		file.setFileName("SimpleFile");
		file2.setFileName(null);
		assertEquals(1, file.compareTo(file2));
	}
	
	/**
	 * One file type is <code>null</code> while other is not.
	 */
	@Test
	public void compareTo9() {
		file.setFileType(FileTypes.FT_VHDL_SOURCE);
		file2.setFileType(null);
		assertEquals(1, file.compareTo(file2));
	}
	
	/**
	 * One file IDs is <code>null</code> while other is not.
	 */
	@Test
	public void compareTo10() {
		file.setId(Long.valueOf(0));
		file2.setId(null);
		assertEquals(1, file.compareTo(file2));
	}
	
	/**
	 * One file name is <code>null</code> while other is not.
	 */
	@Test
	public void compareTo11() {
		file.setFileName(null);
		file2.setFileName("SimpleFile");
		assertEquals(-1, file.compareTo(file2));
	}
	
	/**
	 * One file type is <code>null</code> while other is not.
	 */
	@Test
	public void compareTo12() {
		file.setFileType(null);
		file2.setFileType(FileTypes.FT_VHDL_SOURCE);
		assertEquals(-1, file.compareTo(file2));
	}
	
	/**
	 * One file IDs is <code>null</code> while other is not.
	 */
	@Test
	public void compareTo13() {
		file.setId(null);
		file2.setId(Long.valueOf(0));
		assertEquals(-1, file.compareTo(file2));
	}
	
	/**
	 * Both file names are <code>null</code>.
	 */
	@Test
	public void compareTo14() {
		file.setFileName(null);
		file2.setFileName(null);
		assertEquals(0, file.compareTo(file2));
	}
	
	/**
	 * Both file types are <code>null</code>.
	 */
	@Test
	public void compareTo15() {
		file.setFileType(null);
		file2.setFileType(null);
		assertEquals(0, file.compareTo(file2));
	}
	
	/**
	 * Both file IDs are <code>null</code>.
	 */
	@Test
	public void compareTo16() {
		file.setId(null);
		file2.setId(null);
		assertEquals(0, file.compareTo(file2));
	}
	
	/**
	 * Both file names are <code>null</code> and file types
	 * are not equal.
	 */
	@Test
	public void compareTo17() {
		file.setFileName(null);
		file.setFileType(FileTypes.FT_VHDL_SOURCE);
		file2.setFileName(null);
		file2.setFileType(FileTypes.FT_VHDL_TB);
		assertEquals(FileTypes.FT_VHDL_SOURCE.compareTo(FileTypes.FT_VHDL_TB), file.compareTo(file2));
	}
	
	/**
	 * Both file names are <code>null</code> and file IDs
	 * are not equal.
	 */
	@Test
	public void compareTo18() {
		file.setId(Long.valueOf(1));
		file.setFileName(null);
		file2.setId(Long.valueOf(0));
		file2.setFileName(null);
		assertEquals(1, file.compareTo(file2));
	}
	
	/**
	 * Both file names, types and IDs are <code>null</code>.
	 */
	@Test
	public void compareTo19() {
		file.setId(null);
		file.setFileName(null);
		file.setFileType(null);
		file2.setId(null);
		file2.setFileName(null);
		file2.setFileType(null);
		assertEquals(0, file.compareTo(file2));
	}
	
	/**
	 * Both file types are <code>null</code> and file names
	 * are not equal.
	 */
	@Test
	public void compareTo20() {
		file.setFileName("FileSimple");
		file.setFileType(null);
		file2.setFileName("SimpleFile");
		file2.setFileType(null);
		assertEquals("FileSimple".compareTo("SimpleFile"), file.compareTo(file2));
	}
	
	/**
	 * Both file types are <code>null</code> and file IDs
	 * are not equal.
	 */
	@Test
	public void compareTo21() {
		file.setId(Long.valueOf(1));
		file.setFileType(null);
		file2.setId(Long.valueOf(0));
		file2.setFileType(null);
		assertEquals(1, file.compareTo(file2));
	}
	
	/**
	 * Both file types are <code>null</code> and file names
	 * and IDs are not equal.
	 */
	@Test
	public void compareTo22() {
		file.setId(Long.valueOf(1));
		file.setFileName("FileSimple");
		file.setFileType(null);
		file2.setId(Long.valueOf(0));
		file2.setFileName("SimpleFile");
		file2.setFileType(null);
		assertEquals("FileSimple".compareTo("SimpleFile"), file.compareTo(file2));
	}
	
	/**
	 * Both file IDs are <code>null</code> and file names
	 * are not equal.
	 */
	@Test
	public void compareTo23() {
		file.setId(null);
		file.setFileName("FileSimple");
		file2.setId(null);
		file2.setFileName("SimpleFile");
		assertEquals("FileSimple".compareTo("SimpleFile"), file.compareTo(file2));
	}
	
	/**
	 * Both file IDs are <code>null</code> and file types
	 * are not equal.
	 */
	@Test
	public void compareTo24() {
		file.setId(null);
		file.setFileType(FileTypes.FT_VHDL_SOURCE);
		file2.setId(null);
		file2.setFileType(FileTypes.FT_VHDL_TB);
		assertEquals(FileTypes.FT_VHDL_SOURCE.compareTo(FileTypes.FT_VHDL_TB), file.compareTo(file2));
	}
	
	/**
	 * Both file IDs are <code>null</code> and file names
	 * and types are not equal.
	 */
	@Test
	public void compareTo25() {
		file.setId(null);
		file.setFileName("FileSimple");
		file.setFileType(FileTypes.FT_VHDL_SOURCE);
		file2.setId(null);
		file2.setFileName("SimpleFile");
		file2.setFileType(FileTypes.FT_VHDL_TB);
		assertEquals("FileSimple".compareTo("SimpleFile"), file.compareTo(file2));
	}
	
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(FileTest.class);
	}
}