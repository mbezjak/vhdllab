package hr.fer.zemris.vhdllab.vhdl.tb;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.utilities.StringFormat;

import org.junit.Test;

public class StringFormatTest {

	/** 
	 * String is empty.
	 */
	@Test
	public void isCorrectName() {
		assertEquals(false, StringFormat.isCorrectEntityName(""));
	}
	
	/** 
	 * String contains letters, an underscore and a number.
	 */
	@Test
	public void isCorrectName2() {
		assertEquals(true, StringFormat.isCorrectEntityName("sklop_2"));
	}
	
	/** 
	 * String starts with an undeline. 
	 */
	@Test
	public void isCorrectName3() {
		assertEquals(false, StringFormat.isCorrectEntityName("_sklop2"));
	}
	
	/** 
	 * String starts with a number. 
	 */
	@Test
	public void isCorrectName4() {
		assertEquals(false, StringFormat.isCorrectEntityName("6sklop"));
	}
	
	/** 
	 * String contains an underscore after an undeline.
	 */
	@Test
	public void isCorrectName5() {
		assertEquals(false, StringFormat.isCorrectEntityName("sklop__a"));
	}
	
	/** 
	 * String ends with an undeline. 
	 */
	@Test
	public void isCorrectName6() {
		assertEquals(false, StringFormat.isCorrectEntityName("sklop5_"));
	}
	
	/** 
	 * String contains capital letters.
	 */
	@Test
	public void isCorrectName7() {
		assertEquals(true, StringFormat.isCorrectEntityName("SKLOP_5_drugi"));
	}
	
	/** 
	 * String contains illegal character (?).
	 */
	@Test
	public void isCorrectName8() {
		assertEquals(false, StringFormat.isCorrectEntityName("sklop?drugi"));
	}

	/** 
	 * String is a reserved word (xor).
	 */
	@Test
	public void isCorrectName9() {
		assertEquals(false, StringFormat.isCorrectEntityName("xor"));
	}
	
	/** 
	 * String is a reserved word (entity).
	 */
	@Test
	public void isCorrectName10() {
		assertEquals(false, StringFormat.isCorrectEntityName("entity"));
	}
	
	/**
	 * String is a reserved word (std_logic).
	 */
	@Test
	public void isCorrectName11() {
		assertEquals(false, StringFormat.isCorrectEntityName("std_logic"));
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
