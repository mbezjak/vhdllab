package hr.fer.zemris.vhdllab.applets.editor.schema2.misc;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;




//TODO: implement double hashing
public class IntHashSet implements IntCollection, IntSet {
	
	// TODO: improve iterator if possible
	private class IntSetIterator implements Iterator<Integer> {
		private int pos, last;
		private Integer entry;
		
		public IntSetIterator() {
			pos = 0;
			last = -1;
			while (pos < space.length && space[pos] == EMPTY_CELL) pos++;
		}
		
		public boolean hasNext() {
			return (pos < space.length);
		}

		public Integer next() {
			if (pos >= space.length) throw new NoSuchElementException();
			
			entry = new Integer(space[pos]);
			last = pos;
			
			pos++;
			while (pos < space.length && space[pos] == EMPTY_CELL) pos++;
			
			return entry;
		}

		public void remove() {
			if (last == -1) throw new IllegalStateException();
			IntHashSet.this.remove(last);
			last = -1;
		}
	}

	private final static float DEFAULT_LOAD_FACTOR = 0.65f;
	private final static float MINIMAL_LOAD_FACTOR = 0.06f;
	private final static float MAXIMAL_LOAD_FACTOR = 0.94f;
	private final static int DUPL_FACTOR = 2;
	private final static int EMPTY_CELL = Integer.MIN_VALUE;
	private final static int[] SIZES = {
		17, 37, 79, 163, 331, 673, 1361, 2729, 5471, 10949,
		21911, 43853, 87719, 175447, 350899, 701819, 1403641,
		2807303
	}; // TODO
	
	
	private int[] space;
	private float loadfactor, loadstate;
	private int size;
	
	
	public IntHashSet() {
		space = new int[SIZES[0]];
		loadfactor = DEFAULT_LOAD_FACTOR;
		
		init();
	}
	
	/**
	 * A copied IntSet does not have copies
	 * of the elements that are stored, but
	 * merely references to original objects.
	 * This IntSet's internal data structures
	 * are, on the other hand, constructed from
	 * scratch.
	 * 
	 * @param other
	 */
	public IntHashSet(IntHashSet other) {
		this.space = new int[other.space.length];
		this.loadfactor = other.loadfactor;
		this.loadstate = other.loadstate;
		this.size = other.size;
		
		for (int i = 0; i < other.space.length; i++) {
			this.space[i] = other.space[i];
		}
	}
	
	
	private final void init() {
		size = 0;
		loadstate = 0.f;
		Arrays.fill(space, EMPTY_CELL);
	}
	
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.rg.axel.collections.IntSet#add(int)
	 */
	public final void add(int key) {
		if (loadstate > loadfactor) resize(space.length * DUPL_FACTOR);
		
		private_put(key);
	}
	
	private final void private_put(int key) {
		int hval = hash(key);
		
		while (space[hval] != EMPTY_CELL && space[hval] != key) hval = (hval + 1) % space.length;
		if (space[hval] != key) size++;
		
		space[hval] = key;
		
		loadstate = 1.f * size / space.length;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.rg.axel.collections.IntSet#contains(int)
	 */
	public final boolean contains(int key) {
		int hval = hash(key);
		
		while (space[hval] != EMPTY_CELL && space[hval] != key) hval = (hval + 1) % space.length;
		
		if (space[hval] == EMPTY_CELL) return false;
		else return true;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.rg.axel.collections.IntSet#remove(int)
	 */
	public final void remove(int key) { // TODO: implement shrinking
		int hval = hash(key);
		
		while (space[hval] != EMPTY_CELL && space[hval] != key) hval = (hval + 1) % space.length;
		
		if (space[hval] != EMPTY_CELL) {
			space[hval] = EMPTY_CELL;
			size--;
			loadstate = 1.f * size / space.length;
		}
	}
	
	
	
	// TODO
	/* (non-Javadoc)
	 * @see hr.fer.zemris.rg.axel.collections.IntSet#setLoadFactor(float)
	 */
	public final void setLoadFactor(float hashLoadFactor) {
		if (hashLoadFactor <= MINIMAL_LOAD_FACTOR || hashLoadFactor >= MAXIMAL_LOAD_FACTOR) {
			throw new RuntimeException("NI");
		}
		throw new RuntimeException("NI");
	}
	
	public final int capacity() {
		return space.length;
	}

	public final void clear() {
		init();
	}

	public final void reset() {
		space = new int[SIZES[0]];
		
		init();
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.rg.axel.collections.IntSet#reset(int)
	 */
	public final void reset(int newsize) {
		// find correct size (binary search)
		int r = SIZES.length - 1, l = 0, x = 0;
		
		while (r >= l) {
			x = (l + r) / 2;
			if (newsize < SIZES[x]) r = x - 1; else l = x + 1;
		}
		if (newsize < SIZES[x]) newsize = SIZES[x]; else newsize = SIZES[x+1];
		
		space = new int[newsize];
	}

	public final boolean empty() {
		return (size == 0);
	}

	// TODO
	public final Iterator<Integer> iterator() {
		return new IntSetIterator();
	}

	public final int size() {
		return size;
	}
	
	private final int hash(int num) {
		return num % space.length;
	}
	
	
	private final void resize(int newsize) {
		// find correct size (binary search)
		int r = SIZES.length - 1, l = 0, x = 0;
		
		while (r >= l) {
			x = (l + r) / 2;
			if (newsize < SIZES[x]) r = x - 1; else l = x + 1;
		}
		if (newsize < SIZES[x]) newsize = SIZES[x]; else newsize = SIZES[x+1];
		
		// put values to new hash space
		int[] oldkeys = space;
		l = size;
		
		space = new int[newsize];
		init();
		
		for (r = 0; r < l; r++) {
			if (oldkeys[r] != EMPTY_CELL) private_put(oldkeys[r]);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof IntHashSet)) return false;
		IntHashSet other = (IntHashSet)obj;
		
		// lazy check
		if (other.size != this.size || other.loadfactor != this.loadfactor) return false;
		
		for (Integer entry : other) {
			if (!this.contains(entry)) return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int sum = 0, pos = 0;
		
		while (pos < space.length) {
			while (pos < space.length && space[pos] != EMPTY_CELL) pos++;
			sum += space[pos];
			pos++;
		}
		
		return sum;
	}

	@Override
	public String toString() {
		return super.toString() + "_IntHashSet";
	}
	
	
	
	
	
}

