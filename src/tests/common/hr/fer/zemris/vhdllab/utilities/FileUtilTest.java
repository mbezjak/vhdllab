package hr.fer.zemris.vhdllab.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FileUtilTest {

	@Test
	public void filterMaliciousPath() {
		String path = "/etc/passwd";
		String filtered = FileUtil.filterMaliciousPath(path);
		assertEquals("etc/passwd", filtered);
	}

	@Test
	public void filterMaliciousPath2() {
		String path = "../etc/passwd";
		String filtered = FileUtil.filterMaliciousPath(path);
		assertEquals("etc/passwd", filtered);
	}
	
	@Test
	public void filterMaliciousPath3() {
		String path = "./etc/passwd";
		String filtered = FileUtil.filterMaliciousPath(path);
		assertEquals("etc/passwd", filtered);
	}
	
	@Test
	public void filterMaliciousPath4() {
		String path = ".project";
		String filtered = FileUtil.filterMaliciousPath(path);
		assertEquals(".project", filtered);
	}

	@Test
	public void filterMaliciousPath5() {
		String path = "C:\\Windows\\notepad.exe";
		String filtered = FileUtil.filterMaliciousPath(path);
		assertEquals("C\\Windows\\notepad.exe", filtered);
	}

	@Test
	public void filterMaliciousPath6() {
		String path = "../.././etc/passwd";
		String filtered = FileUtil.filterMaliciousPath(path);
		assertEquals("etc/passwd", filtered);
	}
	
}
