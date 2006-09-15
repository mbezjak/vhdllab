package hr.fer.zemris.vhdllab.vhdl.tb;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringUtilTest {

	/** 
	 * String is empty.
	 */
	@Test
	public void isCorrectName() {
		assertEquals(false, StringUtil.isCorrectName(""));
	}
	
	/** 
	 * String contains letters, an underline and a number.
	 */
	@Test
	public void isCorrectName2() {
		assertEquals(true, StringUtil.isCorrectName("sklop_2"));
	}
	
	/** 
	 * String starts with an undeline. 
	 */
	@Test
	public void isCorrectName3() {
		assertEquals(false, StringUtil.isCorrectName("_sklop2"));
	}
	
	/** 
	 * String starts with a number. 
	 */
	@Test
	public void isCorrectName4() {
		assertEquals(false, StringUtil.isCorrectName("6sklop"));
	}
	
	/** 
	 * String contains an underline after an undeline.
	 */
	@Test
	public void isCorrectName5() {
		assertEquals(false, StringUtil.isCorrectName("sklop__a"));
	}
	
	/** 
	 * String ends with an undeline. 
	 */
	@Test
	public void isCorrectName6() {
		assertEquals(false, StringUtil.isCorrectName("sklop5_"));
	}
	
	/** 
	 * String contains capital letters.
	 */
	@Test
	public void isCorrectName7() {
		assertEquals(true, StringUtil.isCorrectName("SKLOP_5_drugi"));
	}
	
	/** 
	 * String contains illegal character (?).
	 */
	@Test
	public void isCorrectName8() {
		assertEquals(false, StringUtil.isCorrectName("sklop?drugi"));
	}

	/** 
	 * String is measure unit.
	 */
	@Test
	public void isMeasureUnit() {
		assertEquals(true, StringUtil.isMeasureUnit("fs"));
	}
	
	/** 
	 * String is not measure unit.
	 */
	@Test
	public void isMeasureUnit2() {
		assertEquals(false, StringUtil.isMeasureUnit("h"));
	}
	
	/** 
	 * String is vector direction DOWNTO.
	 */
	@Test
	public void isVectorDirection() {
		assertEquals(true, StringUtil.isVectorDirection("DOWNTO"));
	}
	
	/** 
	 * String is vector direction TO.
	 */
	@Test
	public void isVectorDirection2() {
		assertEquals(true, StringUtil.isVectorDirection("TO"));
	}
	
	/** 
	 * String is not vector direction.
	 */
	@Test
	public void isVectorDirection3() {
		assertEquals(false, StringUtil.isVectorDirection("UPTO"));
	}
}
