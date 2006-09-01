package hr.fer.zemris.vhdllab.model;

import hr.fer.zemris.vhdllab.model.File;
import junit.framework.TestCase;

/**
 * This is a TestCase for {@linkplain hr.fer.zemris.vhdllab.vhdl.model.model.File} class.
 * 
 * @author Miro Bezjak
 */
public class FileTest extends TestCase {

	public FileTest(String name) {
		super(name);
	}

	/**
	 * Test method hashCode() and equals(Object) when equals return true
	 * and all file.id is not null.
	 */
	public void testHashCode() {
		File file = new File();
		file.setId((long) 0);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);

		assertEquals(true, file.equals(file2));
		assertEquals(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * Test method hashCode() and equals(Object) when equals return true
	 * and all file.id is null and filename is equal.
	 */
	public void testHashCode2() {
		File file = new File();
		file.setId(null);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId(null);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);

		assertEquals(true, file.equals(file2));
		assertEquals(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * Test method hashCode() and equals(Object) when equals return true
	 * and all file.id is null and filename is not equal.
	 */
	public void testHashCode3() {
		File file = new File();
		file.setId(null);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId(null);
		file2.setFileName("SimpleFile2");
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);

		assertEquals(false, file.equals(file2));
		assertNotSame(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * Test method hashCode() and equals(Object) when equals return true
	 * and one file.id is null and type is equal.
	 */
	public void testHashCode4() {
		File file = new File();
		file.setId(null);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 1);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);

		assertEquals(false, file.equals(file2));
		assertNotSame(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * Test method hashCode() and equals(Object) when equals return true
	 * and one file.id is null and type is not equal.
	 */
	public void testHashCode5() {
		File file = new File();
		file.setId(null);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 1);
		file2.setFileName("SimpleFile2");
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);

		assertEquals(false, file.equals(file2));
		assertNotSame(file.hashCode(), file2.hashCode());
	}

	/**
	 * Test method equals(Object) when object is null
	 */
	public void testEqualsObject() {
		File file = new File();
		file.setId(null);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		assertEquals(false, file.equals(null));
	}
	
	/**
	 * Test method equals(Object) when object is not instance of File
	 */
	public void testEqualsObject2() {
		File file = new File();
		file.setId(null);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		assertEquals(false, file.equals(new String("File")));
	}

	/**
	 * Test method compareTo(Object) when they are equal
	 */
	public void testCompareTo() {
		File file = new File();
		file.setId((long) 0);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(0, file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when ID are different
	 */
	public void testCompareTo2() {
		File file = new File();
		file.setId((long) 1);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(1, file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when file types are different
	 */
	public void testCompareTo3() {
		File file = new File();
		file.setId((long) 0);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLTB);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(File.FT_VHDLSOURCE.compareTo(File.FT_VHDLTB), file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when file names are different
	 */
	public void testCompareTo4() {
		File file = new File();
		file.setId((long) 0);
		file.setFileName("FilenameSimple");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals("FilenameSimple".compareTo("SimpleFile"), file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when file names and types are different
	 */
	public void testCompareTo5() {
		File file = new File();
		file.setId((long) 0);
		file.setFileName("FilenameSimple");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLTB);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals("FilenameSimple".compareTo("SimpleFile"), file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when file names and ID are different
	 */
	public void testCompareTo6() {
		File file = new File();
		file.setId((long) 0);
		file.setFileName("FilenameSimple");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 1);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals("FilenameSimple".compareTo("SimpleFile"), file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when file types and ID are different
	 */
	public void testCompareTo7() {
		File file = new File();
		file.setId((long) 1);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLTB);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(File.FT_VHDLSOURCE.compareTo(File.FT_VHDLTB), file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when other file name is null
	 */
	public void testCompareTo8() {
		File file = new File();
		file.setId((long) 0);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName(null);
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(1, file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when other file type is null
	 */
	public void testCompareTo9() {
		File file = new File();
		file.setId((long) 0);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName("SimpleFile");
		file2.setFileType(null);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(1, file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when other ID is null
	 */
	public void testCompareTo10() {
		File file = new File();
		file.setId((long) 0);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId(null);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(1, file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when this file name is null
	 */
	public void testCompareTo11() {
		File file = new File();
		file.setId((long) 0);
		file.setFileName(null);
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(-1, file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when this file type is null
	 */
	public void testCompareTo12() {
		File file = new File();
		file.setId((long) 0);
		file.setFileName("SimpleFile");
		file.setFileType(null);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(-1, file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when this ID is null
	 */
	public void testCompareTo13() {
		File file = new File();
		file.setId(null);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(-1, file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when both file names are null
	 */
	public void testCompareTo14() {
		File file = new File();
		file.setId((long) 0);
		file.setFileName(null);
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName(null);
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(0, file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when both file types are null
	 */
	public void testCompareTo15() {
		File file = new File();
		file.setId((long) 0);
		file.setFileName("SimpleFile");
		file.setFileType(null);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName("SimpleFile");
		file2.setFileType(null);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(0, file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when both ID are null
	 */
	public void testCompareTo16() {
		File file = new File();
		file.setId(null);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId(null);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(0, file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when both file names are null and file types are different
	 */
	public void testCompareTo17() {
		File file = new File();
		file.setId((long) 0);
		file.setFileName(null);
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName(null);
		file2.setFileType(File.FT_VHDLTB);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(File.FT_VHDLSOURCE.compareTo(File.FT_VHDLTB), file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when both file names are null and ID are different
	 */
	public void testCompareTo18() {
		File file = new File();
		file.setId((long) 1);
		file.setFileName(null);
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName(null);
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(1, file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when both file names are null and file types and ID are different
	 */
	public void testCompareTo19() {
		File file = new File();
		file.setId(null);
		file.setFileName(null);
		file.setFileType(null);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId(null);
		file2.setFileName(null);
		file2.setFileType(null);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(0, file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when both file types are null and file names are different
	 */
	public void testCompareTo20() {
		File file = new File();
		file.setId((long) 0);
		file.setFileName("FileSimple");
		file.setFileType(null);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName("SimpleFile");
		file2.setFileType(null);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals("FileSimple".compareTo("SimpleFile"), file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when both file types are null and ID are different
	 */
	public void testCompareTo21() {
		File file = new File();
		file.setId((long) 1);
		file.setFileName("SimpleFile");
		file.setFileType(null);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName("SimpleFile");
		file2.setFileType(null);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(1, file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when both file types are null and file names and ID are different
	 */
	public void testCompareTo22() {
		File file = new File();
		file.setId((long) 1);
		file.setFileName("FileSimple");
		file.setFileType(null);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId((long) 0);
		file2.setFileName("SimpleFile");
		file2.setFileType(null);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals("FileSimple".compareTo("SimpleFile"), file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when both ID are null and file names are different
	 */
	public void testCompareTo23() {
		File file = new File();
		file.setId(null);
		file.setFileName("FileSimple");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId(null);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLSOURCE);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals("FileSimple".compareTo("SimpleFile"), file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when both ID are null and file types are different
	 */
	public void testCompareTo24() {
		File file = new File();
		file.setId(null);
		file.setFileName("SimpleFile");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId(null);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLTB);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals(File.FT_VHDLSOURCE.compareTo(File.FT_VHDLTB), file.compareTo(file2));
	}
	
	/**
	 * Test method compareTo(Object) when both ID are null and file names and types are different
	 */
	public void testCompareTo25() {
		File file = new File();
		file.setId(null);
		file.setFileName("FileSimple");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("");
		file.setProject(null);
		
		File file2 = new File();
		file2.setId(null);
		file2.setFileName("SimpleFile");
		file2.setFileType(File.FT_VHDLTB);
		file2.setContent("");
		file2.setProject(null);
		
		assertEquals("FileSimple".compareTo("SimpleFile"), file.compareTo(file2));
	}
}
