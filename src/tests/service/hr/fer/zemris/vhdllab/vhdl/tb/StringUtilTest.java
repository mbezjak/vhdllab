package hr.fer.zemris.vhdllab.vhdl.tb;

import hr.fer.zemris.vhdllab.vhdl.tb.StringUtil;
import junit.framework.TestCase;

/**
 * This is a TestCase for {@linkplain hr.fer.zemris.vhdllab.vhdl.model.tb.StringUtil} class.
 * 
 * @author Miro Bezjak
 */
public class StringUtilTest extends TestCase {

	/** 
	 * Test method isCorrectName(String) when string is empty.
	 */
	public void testIsCorrectName() {
		boolean flag = StringUtil.isCorrectName("");
		assertEquals(false, flag);
	}
	
	/** 
	 * Test method isCorrectName(String) when string contains
	 * letters, an underline and a number.
	 */
	public void testIsCorrectName2() {
		boolean flag = StringUtil.isCorrectName("sklop_2");
		assertEquals(true, flag);
	}
	
	/** 
	 * Test method isCorrectName(String) when string starts with
	 * an undeline. 
	 */
	public void testIsCorrectName3() {
		boolean flag = StringUtil.isCorrectName("_sklop2");
		assertEquals(false, flag);
	}
	
	/** 
	 * Test method isCorrectName(String) when string starts with
	 * a number. 
	 */
	public void testIsCorrectName4() {
		boolean flag = StringUtil.isCorrectName("6sklop");
		assertEquals(false, flag);
	}
	
	/** 
	 * Test method isCorrectName(String) when string contains
	 * an underline after an undeline.
	 */
	public void testIsCorrectName5() {
		boolean flag = StringUtil.isCorrectName("sklop__a");
		assertEquals(false, flag);
	}
	
	/** 
	 * Test method isCorrectName(String) when string endss with
	 * an undeline. 
	 */
	public void testIsCorrectName6() {
		boolean flag = StringUtil.isCorrectName("sklop5_");
		assertEquals(false, flag);
	}
	
	/** 
	 * Test method isCorrectName(String) when string contains
	 * capital letters.
	 */
	public void testIsCorrectName7() {
		boolean flag = StringUtil.isCorrectName("SKLOP_5_drugi");
		assertEquals(true, flag);
	}
	
	/** 
	 * Test method isCorrectName(String) when string contains
	 * illegal character (?).
	 */
	public void testIsCorrectName8() {
		boolean flag = StringUtil.isCorrectName("sklop?drugi");
		assertEquals(false, flag);
	}

	/** 
	 * Test method isMeasureUnit(String) when string is measure unit.
	 */
	public void testIsMeasureUnit() {
		boolean flag = StringUtil.isMeasureUnit("fs");
		assertEquals(true, flag);
	}
	
	/** 
	 * Test method isMeasureUnit(String) when string is not measure unit.
	 */
	public void testIsMeasureUnit2() {
		boolean flag = StringUtil.isMeasureUnit("h");
		assertEquals(false, flag);
	}
	
	/** 
	 * Test method isVectorDirection(String) when string is
	 * vector direction DOWNTO.
	 */
	public void testIsVectorDirection() {
		boolean flag = StringUtil.isVectorDirection("DOWNTO");
		assertEquals(true, flag);
	}
	
	/** 
	 * Test method isVectorDirection(String) when string is
	 * vector direction TO.
	 */
	public void testIsVectorDirection2() {
		boolean flag = StringUtil.isVectorDirection("TO");
		assertEquals(true, flag);
	}
	
	/** 
	 * Test method isVectorDirection(String) when string is
	 * not vector direction.
	 */
	public void testIsVectorDirection3() {
		boolean flag = StringUtil.isVectorDirection("UPTO");
		assertEquals(false, flag);
	}
}
