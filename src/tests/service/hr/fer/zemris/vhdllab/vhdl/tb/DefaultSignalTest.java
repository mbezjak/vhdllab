package hr.fer.zemris.vhdllab.vhdl.tb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.Ignore;
import org.junit.Test;

public class DefaultSignalTest {

	/**
	 * Name is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void constructorString() {
		new DefaultSignal(null);
	}
	
	/**
	 * Name is incorrect.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void constructorString2() {
		new DefaultSignal("%Signal_2");
	}
	
	/**
	 * Name is correct.
	 */
	@Test
	public void constructorString3() {
		new DefaultSignal("Signal_2");
	}

	/**
	 * Range has more then 2 elements.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void constructorStringIntArray() {
		new DefaultSignal("Signal_2", new int[] {2, 3, 4});
	}
	
	/**
	 * Range has one negative element.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void constructorStringIntArray2() {
		new DefaultSignal("Signal_2", new int[] {-2, 3});
	}
	
	/**
	 * Range has both negative elements.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void constructorStringIntArray3() {
		new DefaultSignal("Signal_2", new int[] {-2, -3});
	}
	
	/**
	 * Range is <code>null</code>.
	 */
	@Test
	public void constructorStringIntArray4() {
		int[] range = null;
		new DefaultSignal("Signal_2", range);
	}
	
	/**
	 * Range has equal bounds.
	 */
	@Test
	public void constructorStringIntArray5() {
		new DefaultSignal("Signal_2", new int[] {2, 2});
	}

	/**
	 * Exciter is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void constructorStringListOfImpulse() {
		List<Impulse> exciter = null;
		new DefaultSignal("Signal_0", exciter);
	}
	
	/**
	 * Exciter is correct.
	 */
	@Test
	public void constructorStringListOfImpulse2() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1"));
		exciter.add(new DefaultImpulse(100, "0"));
		exciter.add(new DefaultImpulse(200, "1"));
		exciter.add(new DefaultImpulse(300, "11"));
		exciter.add(new DefaultImpulse(500, "0"));
		exciter.add(new DefaultImpulse(30, "1"));
		exciter.add(new DefaultImpulse(500, "0"));
		Signal signal = new DefaultSignal("Signal_0", exciter);
		
		List<Impulse> exciter2 = new ArrayList<Impulse>();
		exciter2.add(new DefaultImpulse(0, "1"));
		exciter2.add(new DefaultImpulse(100, "0"));
		exciter2.add(new DefaultImpulse(200, "1"));
		exciter2.add(new DefaultImpulse(500, "0"));
		Signal signal2 = new DefaultSignal("Signal_0", exciter2);
		
		assertEquals(signal2, signal);
	}
	
	/**
	 * Exciter is correct.
	 */
	@Test
	public void constructorStringListOfImpulse3() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(300, "110"));
		exciter.add(new DefaultImpulse(500, "00010"));
		exciter.add(new DefaultImpulse(30, "100"));
		exciter.add(new DefaultImpulse(500, "010"));
		Signal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		List<Impulse> exciter2 = new ArrayList<Impulse>();
		exciter2.add(new DefaultImpulse(0, "1uu"));
		exciter2.add(new DefaultImpulse(100, "000"));
		exciter2.add(new DefaultImpulse(200, "110"));
		exciter2.add(new DefaultImpulse(500, "010"));
		
		assertEquals(new DefaultSignal("Signal_0", new int[] {2, 4}, exciter2), signal);
	}

	/**
	 * Impulse is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void constructorStringImpulse() {
		Impulse impulse = null;
		new DefaultSignal("Signal_0", impulse);
	}
	
	/**
	 * Impulse is correct.
	 */
	@Test
	public void constructorStringImpulse2() {
		Impulse impulse = new DefaultImpulse(0, "0");
		new DefaultSignal("Signal_0", impulse);
	}
	
	/**
	 * Impulse is correct.
	 */
	@Test
	public void constructorStringIntArrayImpulse() {
		Impulse impulse = new DefaultImpulse(0, "010");
		new DefaultSignal("Signal_0", new int[] {0, 2}, impulse);
	}
	
	/**
	 * Signals are equal and are scalars.
	 */
	@Test
	public void equalsAndHashCode() {
		Signal s1 = new DefaultSignal("Signal_0");
		Signal s2 = new DefaultSignal("signal_0");
		assertEquals(true, s1.equals(s2));
		assertEquals(s1.hashCode(), s2.hashCode());
	}
	
	/**
	 * Signals are equal and are scalars.
	 */
	@Test
	public void equalsAndHashCode2() {
		Impulse i1 = new DefaultImpulse(0, "1");
		Impulse i2 = new DefaultImpulse(0, "1");
		Signal s1 = new DefaultSignal("Signal_0", i1);
		Signal s2 = new DefaultSignal("signal_0", i2);
		assertEquals(true, s1.equals(s2));
		assertEquals(s1.hashCode(), s2.hashCode());
	}
	
	/**
	 * Signals are not equal and are scalars.
	 */
	@Test
	public void equalsAndHashCode3() {
		Signal s1 = new DefaultSignal("Signal_0");
		Signal s2 = new DefaultSignal("signal0");
		assertEquals(false, s1.equals(s2));
		assertNotSame(s1.hashCode(), s2.hashCode());
	}
	
	/**
	 * Signals are not equal and are scalars.
	 */
	@Test
	public void equalsAndHashCode4() {
		Impulse i1 = new DefaultImpulse(0, "1");
		Impulse i2 = new DefaultImpulse(0, "0");
		Signal s1 = new DefaultSignal("Signal_0", i1);
		Signal s2 = new DefaultSignal("signal_0", i2);
		assertEquals(false, s1.equals(s2));
		assertNotSame(s1.hashCode(), s2.hashCode());
	}
	
	/**
	 * Signals are equal and are vectors.
	 */
	@Test
	public void equalsAndHashCode5() {
		Signal s1 = new DefaultSignal("Signal_0", new int[] {2, 0});
		Signal s2 = new DefaultSignal("signal_0", new int[] {2, 0});
		assertEquals(true, s1.equals(s2));
		assertEquals(s1.hashCode(), s2.hashCode());
	}
	
	/**
	 * Signals are equal and are vectors.
	 */
	@Test
	public void equalsAndHashCode6() {
		Impulse i1 = new DefaultImpulse(0, "101");
		Impulse i2 = new DefaultImpulse(0, "101");
		Signal s1 = new DefaultSignal("Signal_0", new int[] {2, 0}, i1);
		Signal s2 = new DefaultSignal("signal_0", new int[] {2, 0}, i2);
		assertEquals(true, s1.equals(s2));
		assertEquals(s1.hashCode(), s2.hashCode());
	}
	
	/**
	 * Signals are not equal and are vectors.
	 */
	@Test
	public void equalsAndHashCode57() {
		Signal s1 = new DefaultSignal("Signal_0", new int[] {2, 0});
		Signal s2 = new DefaultSignal("signal0", new int[] {2, 0});
		assertEquals(false, s1.equals(s2));
		assertNotSame(s1.hashCode(), s2.hashCode());
	}
	
	/**
	 * Signals are not equal and are vectors.
	 */
	@Test
	public void equalsAndHashCode8() {
		Impulse i1 = new DefaultImpulse(0, "101");
		Impulse i2 = new DefaultImpulse(0, "111");
		Signal s1 = new DefaultSignal("Signal_0", new int[] {2, 0}, i1);
		Signal s2 = new DefaultSignal("signal_0", new int[] {2, 0}, i2);
		assertEquals(false, s1.equals(s2));
		assertNotSame(s1.hashCode(), s2.hashCode());
	}
	
	/**
	 * Object is <code>null</code>.
	 */
	@Test
	public void equalsObject() {
		Signal signal = new DefaultSignal("Signal_0");
		assertEquals(false, signal.equals(null));
	}
	
	/**
	 * Object is not instance of Type.
	 */
	@Test
	public void equalsObject2() {
		Signal signal = new DefaultSignal("Signal_0");
		assertEquals(false, signal.equals(new String("Signal_0")));
	}
	
	/**
	 * Signal is a scalar.
	 */
	@Test(expected=IllegalStateException.class)
	public void getRangeFrom() {
		Signal signal = new DefaultSignal("Signal_0");
		signal.getRangeFrom();
	}
	
	/**
	 * Signal is a vector.
	 */
	@Test
	public void getRangeFrom2() {
		Signal signal = new DefaultSignal("Signal_0", new int[] {2, 0});
		assertEquals(2, signal.getRangeFrom());
	}

	/**
	 * Signal is a scalar.
	 */
	@Test(expected=IllegalStateException.class)
	public void getRangeTo() {
		Signal signal = new DefaultSignal("Signal_0");
		signal.getRangeTo();
	}
	
	/**
	 * Signal is a vector.
	 */
	@Test
	public void getRangeTo2() {
		Signal signal = new DefaultSignal("Signal_0", new int[] {2, 0});
		assertEquals(0, signal.getRangeTo());
	}
	
	/**
	 * Signal is a scalar.
	 */
	@Test
	public void isScalar() {
		Signal signal = new DefaultSignal("Signal_0");
		assertEquals(true, signal.isScalar());
	}
	
	/**
	 * Signal is not a scalar.
	 */
	@Test
	public void isScalar2() {
		Signal signal = new DefaultSignal("Signal_0", new int[] {2, 0});
		assertEquals(false, signal.isScalar());
	}
	
	/**
	 * Signal is not a vector.
	 */
	@Test
	public void isVector() {
		Signal signal = new DefaultSignal("Signal_0");
		assertEquals(false, signal.isVector());
	}
	
	/**
	 * Signal is a vector.
	 */
	@Test
	public void isVector2() {
		Signal signal = new DefaultSignal("Signal_0", new int[] {2, 0});
		assertEquals(true, signal.isVector());
	}
	
	/**
	 * Try to add an impulse after method getExciter().
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void getExciter() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		Signal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);

		signal.getExciter().add(new DefaultImpulse(600, "101"));
	}
	
	/**
	 * Impulse is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void addImpulse(){
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		signal.addImpulse(null);
	}
	
	/**
	 * Impulse is correct.
	 */
	@Test
	public void addImpulse2(){
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		signal.addImpulse(new DefaultImpulse(600, "111"));
		
		List<Impulse> impulseList = new ArrayList<Impulse>();
		impulseList.add(new DefaultImpulse(0, "1uu"));
		impulseList.add(new DefaultImpulse(100, "000"));
		impulseList.add(new DefaultImpulse(200, "110"));
		impulseList.add(new DefaultImpulse(500, "010"));
		impulseList.add(new DefaultImpulse(600, "111"));
		DefaultSignal signal2 = new DefaultSignal("Signal_0", new int[] {2, 4}, impulseList);
		
		assertEquals(signal2, signal);
	}

	/**
	 * Exciter is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void addExciter(){
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		signal.addExciter(null);
	}
	
	/**
	 * Exciter is correct.
	 */
	@Test
	public void addExciter2(){
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

		signal.addExciter(exciterAdd);
		
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

	/**
	 * Time is too negative.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void getImpulseAfterTime() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		signal.getImpulseAfterTime(-2);
	}
	
	/**
	 * Time is <code>-1</code>.
	 */
	@Test
	public void getImpulseAfterTime2() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		Impulse impulseAtTime = signal.getImpulseAfterTime(-1);
		Impulse i = new DefaultImpulse(0, "1uu");
		assertEquals(i, impulseAtTime);
	}
	
	/**
	 * Time is zero.
	 */
	@Test
	public void getImpulseAfterTime3() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		Impulse impulseAtTime = signal.getImpulseAfterTime(0);
		Impulse i = new DefaultImpulse(100, "000");
		assertEquals(i, impulseAtTime);
	}
	
	/**
	 * Time is positive.
	 */
	@Test
	public void getImpulseAfterTime4() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		Impulse impulseAtTime = signal.getImpulseAfterTime(100);
		Impulse i = new DefaultImpulse(200, "110");
		assertEquals(i, impulseAtTime);
	}

	/**
	 * Time is negative.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void getImpulseAtTime() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		signal.getImpulseAtTime(-1);
	}
	
	/**
	 * Time is zero.
	 */
	@Test
	public void getImpulseAtTime2() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		Impulse impulseAtTime = signal.getImpulseAtTime(0);
		Impulse i = new DefaultImpulse(0, "1uu");	
		assertEquals(i, impulseAtTime);
	}

	/**
	 * Time is correct.
	 */
	@Test
	public void getImpulseAtTime3() {
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(new DefaultImpulse(0, "1uu"));
		exciter.add(new DefaultImpulse(100, "000"));
		exciter.add(new DefaultImpulse(200, "110"));
		exciter.add(new DefaultImpulse(500, "010"));
		DefaultSignal signal = new DefaultSignal("Signal_0", new int[] {2, 4}, exciter);
		
		Impulse impulseAtTime = signal.getImpulseAtTime(200);
		Impulse i = new DefaultImpulse(200, "110");
		assertEquals(i, impulseAtTime);
	}

	/**
	 * Test method toString(). No asserting necessary,
	 * just testing to see this method work.
	 */
	@Ignore("Writting on screen... Already tested!")
	@Test
	public void toStringTest() {
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
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(DefaultSignalTest.class);
	}
	
}
