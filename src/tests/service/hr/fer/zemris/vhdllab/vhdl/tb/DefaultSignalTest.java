package hr.fer.zemris.vhdllab.vhdl.tb;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * This is a TestCase for {@linkplain hr.fer.zemris.vhdllab.vhdl.tb.DefaultSignal} class.
 * 
 * @author Miro Bezjak
 */
public class DefaultSignalTest extends TestCase {

	public DefaultSignalTest(String name) {
		super(name);
	}

	/** Test constructor when name is null */
	public void testDefaultSignalString() {
		try {
			new DefaultSignal(null);
			fail("No exception when name is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when name is incorrect */
	public void testDefaultSignalString2() {
		try {
			new DefaultSignal("%Signal_2");
			fail("No exception when name is incorrect");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when name is correct */
	public void testDefaultSignalString3() {
		try {
			new DefaultSignal("Signal_2");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}

	/** Test constructor when range has more then 2 elements */
	public void testDefaultSignalStringIntArray() {
		try {
			new DefaultSignal("Signal_2", new int[] {2, 3, 4});
			fail("No exception range has more then 2 elements");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when range has one element negative */
	public void testDefaultSignalStringIntArray2() {
		try {
			new DefaultSignal("Signal_2", new int[] {-2, 3});
			fail("No exception range has one element negative");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when range has both elements negative */
	public void testDefaultSignalStringIntArray3() {
		try {
			new DefaultSignal("Signal_2", new int[] {-2, -3});
			fail("No exception range has both elements negative");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when range is null */
	public void testDefaultSignalStringIntArray4() {
		int[] range = null;
		try {
			new DefaultSignal("Signal_2", range);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when range has equal bounds */
	public void testDefaultSignalStringIntArray5() {
		try {
			new DefaultSignal("Signal_2", new int[] {2, 2});
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}

	/** Test constructor when exciter is null */
	public void testDefaultSignalStringListOfImpulse() {
		List<Impulse> exciter = null;
		try {
			new DefaultSignal("Signal_0", exciter);
			fail("No exception when exciter is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when exciter is correct */
	public void testDefaultSignalStringListOfImpulse2() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1"));
		exciter.add(new DefaultImpulse(100, "0"));
		exciter.add(new DefaultImpulse(200, "1"));
		exciter.add(new DefaultImpulse(300, "11"));
		exciter.add(new DefaultImpulse(500, "0"));
		exciter.add(new DefaultImpulse(30, "1"));
		exciter.add(new DefaultImpulse(500, "0"));
		
		Signal signal = null;
		try {
			signal = new DefaultSignal("Signal_0", exciter);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
		
		List<Impulse> exciter2 = new ArrayList<Impulse>();
		exciter2.add(new DefaultImpulse(0, "1"));
		exciter2.add(new DefaultImpulse(100, "0"));
		exciter2.add(new DefaultImpulse(200, "1"));
		exciter2.add(new DefaultImpulse(500, "0"));
		Signal signal2 = new DefaultSignal("Signal_0", exciter2);
		
		assertEquals(signal2, signal);
	}
	
	/** Test constructor when exciter is correct */
	public void testDefaultSignalStringIntArrayListOfImpulse() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(300, "110"));
		exciter.add(new DefaultImpulse(500, "00010"));
		exciter.add(new DefaultImpulse(30, "100"));
		exciter.add(new DefaultImpulse(500, "010"));
		
		Signal signal = null;
		try {
			signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
		
		List<Impulse> exciter2 = new ArrayList<Impulse>();
		exciter2.add(new DefaultImpulse(0, "1uu"));
		exciter2.add(new DefaultImpulse(100, "000"));
		exciter2.add(new DefaultImpulse(200, "110"));
		exciter2.add(new DefaultImpulse(500, "010"));
		
		assertEquals(new DefaultSignal("Signal_0", new int[] {2, 4}, exciter2), signal);
	}

	/** Test constructor when impulse is null */
	public void testDefaultSignalStringImpulse() {
		Impulse impulse = null;
		try {
			new DefaultSignal("Signal_0", impulse);
			fail("No exception when impulse is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when impulse is correct */
	public void testDefaultSignalStringImpulse2() {
		Impulse impulse = new DefaultImpulse(0, "0");
		try {
			new DefaultSignal("Signal_0", impulse);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when impulse is correct */
	public void testDefaultSignalStringIntArrayImpulse() {
		Impulse impulse = new DefaultImpulse(0, "010");
		try {
			new DefaultSignal("Signal_0", new int[] {0, 2}, impulse);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}
	
	/** Test method equals(Object) and hashCode() when equals return true and signal is a scalar */
	public void testEqualsAndHashCode() {
		Signal s1 = new DefaultSignal("Signal_0");
		Signal s2 = new DefaultSignal("signal_0");
		assertEquals(true, s1.equals(s2));
		assertEquals(s1.hashCode(), s2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return true and signal is a scalar */
	public void testEqualsAndHashCode2() {
		Impulse i1 = new DefaultImpulse(0, "1");
		Impulse i2 = new DefaultImpulse(0, "1");
		Signal s1 = new DefaultSignal("Signal_0", i1);
		Signal s2 = new DefaultSignal("signal_0", i2);
		assertEquals(true, s1.equals(s2));
		assertEquals(s1.hashCode(), s2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return false and signal is a scalar */
	public void testEqualsAndHashCode3() {
		Signal s1 = new DefaultSignal("Signal_0");
		Signal s2 = new DefaultSignal("signal0");
		assertEquals(false, s1.equals(s2));
		assertNotSame(s1.hashCode(), s2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return false and signal is a scalar */
	public void testEqualsAndHashCode4() {
		Impulse i1 = new DefaultImpulse(0, "1");
		Impulse i2 = new DefaultImpulse(0, "0");
		Signal s1 = new DefaultSignal("Signal_0", i1);
		Signal s2 = new DefaultSignal("signal_0", i2);
		assertEquals(false, s1.equals(s2));
		assertNotSame(s1.hashCode(), s2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return true and signal is a vector */
	public void testEqualsAndHashCode5() {
		Signal s1 = new DefaultSignal("Signal_0", new int[] {2, 0});
		Signal s2 = new DefaultSignal("signal_0", new int[] {2, 0});
		assertEquals(true, s1.equals(s2));
		assertEquals(s1.hashCode(), s2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return true and signal is a vector */
	public void testEqualsAndHashCode6() {
		Impulse i1 = new DefaultImpulse(0, "101");
		Impulse i2 = new DefaultImpulse(0, "101");
		Signal s1 = new DefaultSignal("Signal_0", new int[] {2, 0}, i1);
		Signal s2 = new DefaultSignal("signal_0", new int[] {2, 0}, i2);
		assertEquals(true, s1.equals(s2));
		assertEquals(s1.hashCode(), s2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return false and signal is a vector */
	public void testEqualsAndHashCode57() {
		Signal s1 = new DefaultSignal("Signal_0", new int[] {2, 0});
		Signal s2 = new DefaultSignal("signal0", new int[] {2, 0});
		assertEquals(false, s1.equals(s2));
		assertNotSame(s1.hashCode(), s2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return false and signal is a vector */
	public void testEqualsAndHashCode8() {
		Impulse i1 = new DefaultImpulse(0, "101");
		Impulse i2 = new DefaultImpulse(0, "111");
		Signal s1 = new DefaultSignal("Signal_0", new int[] {2, 0}, i1);
		Signal s2 = new DefaultSignal("signal_0", new int[] {2, 0}, i2);
		assertEquals(false, s1.equals(s2));
		assertNotSame(s1.hashCode(), s2.hashCode());
	}
	
	/** Test method equals(Object) if argument is null */
	public void testEqualsObject() {
		DefaultSignal signal = new DefaultSignal("Signal_0");
		boolean val = signal.equals(null);
		assertEquals(false, val);
	}
	
	/** Test method equals(Object) if argument is not instanceof DefaultType */
	public void testEqualsObject2() {
		DefaultSignal signal = new DefaultSignal("Signal_0");
		boolean val = signal.equals(new String("Signal_0"));
		assertEquals(false, val);
	}
	
	/** Test method getRangeFrom() when signal is a scalar */
	public void testGetRangeFrom() {
		Signal signal = new DefaultSignal("Signal_0");
		try {
			signal.getRangeFrom();
			fail("No exception on method getRangeFrom when signal is a scalar");
		} catch (IllegalStateException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test method getRangeFrom() when signal is a vector */
	public void testGetRangeFrom2() {
		Signal signal = new DefaultSignal("Signal_0", new int[] {2, 0});
		int ret = -1;
		try {
			ret = signal.getRangeFrom();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
		assertEquals(2, ret);
	}

	/** Test method getRangeTo() when signal is a scalar */
	public void testGetRangeTo() {
		Signal signal = new DefaultSignal("Signal_0");
		try {
			signal.getRangeTo();
			fail("No exception on method getRangeFrom when signal is a scalar");
		} catch (IllegalStateException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test method getRangeTo() when signal is a vector */
	public void testGetRangeTo2() {
		Signal signal = new DefaultSignal("Signal_0", new int[] {2, 0});
		int ret = -1;
		try {
			ret = signal.getRangeTo();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
		assertEquals(0, ret);
	}
	
	/** Test method isScalar() when signal is a scalar */
	public void testIsScalar() {
		Signal signal = new DefaultSignal("Signal_0");
		assertEquals(true, signal.isScalar());
	}
	
	/** Test method isScalar() when signal is not a scalar */
	public void testIsScalar2() {
		Signal signal = new DefaultSignal("Signal_0", new int[] {2, 0});
		assertEquals(false, signal.isScalar());
	}
	
	/** Test method isVector() when signal is a scalar */
	public void testIsVector() {
		Signal signal = new DefaultSignal("Signal_0");
		assertEquals(false, signal.isVector());
	}
	
	/** Test method isVector() when signal is not a scalar */
	public void testIsVector2() {
		Signal signal = new DefaultSignal("Signal_0", new int[] {2, 0});
		assertEquals(true, signal.isVector());
	}
	
	/** Test method getExciter(). Try to add an impulse from the outside */
	public void testGetExciter() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		Signal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		try {
			signal.getExciter().add(new DefaultImpulse(600, "101"));
			fail("Outside modification possible");
		} catch (UnsupportedOperationException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test method addImpulse(Impulse) when impulse is null */
	public void testAddImpulse(){
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		try {
			signal.addImpulse(null);
			fail("No exception on when impulse is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test method addImpulse(Impulse) when impulse is correct */
	public void testAddImpulse2(){
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		try {
			signal.addImpulse(new DefaultImpulse(600, "111"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
		
		List<Impulse> impulseList = new ArrayList<Impulse>();
		impulseList.add(new DefaultImpulse(0, "1uu"));
		impulseList.add(new DefaultImpulse(100, "000"));
		impulseList.add(new DefaultImpulse(200, "110"));
		impulseList.add(new DefaultImpulse(500, "010"));
		impulseList.add(new DefaultImpulse(600, "111"));
		DefaultSignal signal2 = new DefaultSignal("Signal_0", new int[] {2, 4}, impulseList);
		
		assertEquals(signal2, signal);
	}

	/** Test method addExciter(Exciter) when exciter is null */
	public void testAddExciter(){
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		try {
			signal.addExciter(null);
			fail("No exception on when exciter is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test method addExciter(Exciter) when exciter is correct */
	public void testAddExciter2(){
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		
		List<Impulse> exciterAdd = new ArrayList<Impulse>();
		exciterAdd.add(new DefaultImpulse(600, "110"));
		exciterAdd.add(new DefaultImpulse(600, "110"));
		exciterAdd.add(new DefaultImpulse(500, "010"));
		exciterAdd.add(new DefaultImpulse(800, "uUu"));
		exciterAdd.add(new DefaultImpulse(900, "1001"));
		exciterAdd.add(new DefaultImpulse(1000, "zUZ"));
		
		
		try {
			signal.addExciter(exciterAdd);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
		
		List<Impulse> impulseList = new ArrayList<Impulse>();
		impulseList.add(new DefaultImpulse(0, "1uu"));
		impulseList.add(new DefaultImpulse(100, "000"));
		impulseList.add(new DefaultImpulse(200, "110"));
		impulseList.add(new DefaultImpulse(500, "010"));
		impulseList.add(new DefaultImpulse(600, "110"));
		impulseList.add(new DefaultImpulse(800, "uuu"));
		impulseList.add(new DefaultImpulse(1000, "zuz"));
		DefaultSignal signal2 = new DefaultSignal("Signal_0", new int[] {2, 4}, impulseList);
		
		assertEquals(signal2, signal);
	}

	/** Test method getImpulseAfterTime when time is too negative */
	public void testGetImpulseAfterTime() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		try {
			signal.getImpulseAfterTime(-2);
			fail("No exception when time is too negative");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test method getImpulseAfterTime when time is -1 */
	public void testGetImpulseAfterTime2() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		Impulse impulseAtTime = null;
		try {
			impulseAtTime = signal.getImpulseAfterTime(-1);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Generated exception: "+e.getMessage());
		}
		
		Impulse i = new DefaultImpulse(0, "1uu");
		assertEquals(i, impulseAtTime);
	}
	
	/** Test method getImpulseAfterTime when time is 0 */
	public void testGetImpulseAfterTime3() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		Impulse impulseAtTime = null;
		try {
			impulseAtTime = signal.getImpulseAfterTime(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Generated exception: "+e.getMessage());
		}
		
		Impulse i = new DefaultImpulse(100, "000");
		assertEquals(i, impulseAtTime);
	}
	
	/** Test method getImpulseAfterTime when time is correct */
	public void testGetImpulseAfterTime4() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		Impulse impulseAtTime = null;
		try {
			impulseAtTime = signal.getImpulseAfterTime(100);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Generated exception: "+e.getMessage());
		}
		
		Impulse i = new DefaultImpulse(200, "110");
		assertEquals(i, impulseAtTime);
	}

	/** Test method getImpulseAtTime when time is negative */
	public void testGetImpulseAtTime() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		try {
			signal.getImpulseAtTime(-1);
			fail("No exception when time is negative");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test method getImpulseAtTime when time is zero */
	public void testGetImpulseAtTime2() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		Impulse impulseAtTime = null;
		try {
			impulseAtTime = signal.getImpulseAtTime(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Generated exception: "+e.getMessage());
		}
		
		Impulse i = new DefaultImpulse(0, "1uu");	
		assertEquals(i, impulseAtTime);
	}

	/** Test method getImpulseAtTime when time is correct */
	public void testGetImpulseAtTime3() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		Impulse impulseAtTime = null;
		try {
			impulseAtTime = signal.getImpulseAtTime(200);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Generated exception: "+e.getMessage());
		}
		
		Impulse i = new DefaultImpulse(200, "110");
		assertEquals(i, impulseAtTime);
	}

	/**
	 * Test method toString(). No asserting necessary,
	 * just testing to see this method work.
	 *//*
	public void testToString() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		Signal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		System.out.println("********************");
		System.out.println("DefaultSignal testing...");
		System.out.println("Testing method toString():");
		System.out.println(signal);
		System.out.println("********************");
	}*/
}
