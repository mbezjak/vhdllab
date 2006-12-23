package hr.fer.zemris.vhdllab.vhdl.tb;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.vhdl.model.StringFormat;

import org.junit.Test;

public class StringFormatTest {

	/** 
	 * String is empty.
	 */
	@Test
	public void isCorrectName() {
		assertEquals(false, StringFormat.isCorrectName(""));
	}
	
	/** 
	 * String contains letters, an underscore and a number.
	 */
	@Test
	public void isCorrectName2() {
		assertEquals(true, StringFormat.isCorrectName("sklop_2"));
	}
	
	/** 
	 * String starts with an undeline. 
	 */
	@Test
	public void isCorrectName3() {
		assertEquals(false, StringFormat.isCorrectName("_sklop2"));
	}
	
	/** 
	 * String starts with a number. 
	 */
	@Test
	public void isCorrectName4() {
		assertEquals(false, StringFormat.isCorrectName("6sklop"));
	}
	
	/** 
	 * String contains an underscore after an undeline.
	 */
	@Test
	public void isCorrectName5() {
		assertEquals(false, StringFormat.isCorrectName("sklop__a"));
	}
	
	/** 
	 * String ends with an undeline. 
	 */
	@Test
	public void isCorrectName6() {
		assertEquals(false, StringFormat.isCorrectName("sklop5_"));
	}
	
	/** 
	 * String contains capital letters.
	 */
	@Test
	public void isCorrectName7() {
		assertEquals(true, StringFormat.isCorrectName("SKLOP_5_drugi"));
	}
	
	/** 
	 * String contains illegal character (?).
	 */
	@Test
	public void isCorrectName8() {
		assertEquals(false, StringFormat.isCorrectName("sklop?drugi"));
	}

	/** 
	 * String is measure unit.
	 */
	@Test
	public void isMeasureUnit() {
		assertEquals(true, StringFormat.isMeasureUnit("fs"));
	}
	
	/** 
	 * String is not measure unit.
	 */
	@Test
	public void isMeasureUnit2() {
		assertEquals(false, StringFormat.isMeasureUnit("h"));
	}
	
}
