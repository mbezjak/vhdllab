package hr.fer.zemris.vhdllab.applets.editor.schema2.misc;

import java.util.Iterator;






/**
 * 
 *  
 * @author Axel
 *
 */
public final class IntList implements IntCollection {
	
	private final static int DUPL_FACTOR = 2;
	private final static int DEFAULT_INITIAL_SIZE = 8;
	public final static int NOT_FOUND = -1;
	
	private int [] field;
	private int size;
	
	public IntList(int initialsize) {
		field = new int[initialsize];
		size = 0;
	}
	
	public IntList() {
		field = new int[DEFAULT_INITIAL_SIZE];
		size = 0;
	}
	
	/**
	 * Deep copy ctor.
	 * 
	 * @param fieldToCopy
	 */
	public IntList(int[] fieldToCopy) {
		this.field = new int[fieldToCopy.length + 4];
		this.size = fieldToCopy.length;
		
		for (int i = 0; i < this.size; i++) {
			this.field[i] = fieldToCopy[i];
		}
	}
	
	public IntList(IntList other) {
		this.field = new int[other.field.length];
		this.size = other.size;
		
		for (int i = 0; i < this.size; i++) {
			this.field[i] = other.field[i];
		}
	}
	
	
	
	
	public final int get(int index) {
		if (index >= size || index < 0) throw new ArrayIndexOutOfBoundsException();
		return field[index];
	}
	
	public final void inc(int index) {
		if (index >= size || index < 0) throw new ArrayIndexOutOfBoundsException();
		field[index]++;
	}
	
	public final void dec(int index) {
		if (index >= size || index < 0) throw new ArrayIndexOutOfBoundsException();
		field[index]--;
	}
	
	public final void set(int index, int value) {
		if (index >= field.length || index < 0) throw new ArrayIndexOutOfBoundsException();
		if (index >= size) size = index + 1;
		field[index] = value;
	}
	
	public final void setExtend(int index, int value) {
		if (index < 0) throw new ArrayIndexOutOfBoundsException();
		if (index >= field.length) {
			resize(index * DUPL_FACTOR);
		}
		if (index >= size) size = index + 1;
		field[index] = value;
	}
	
	// TODO: dovrsi
	public final void insert(int index, int value) {
		throw new RuntimeException("Not implemented yet.");
	}
	
	// TODO: 
	public final void insertAll(int index, IntCollection all) {
		throw new RuntimeException("Not implemented yet.");
	}
	
	// TODO:
	public final void insertList(int index, IntList list) {
		throw new RuntimeException("Not implemented yet.");
	}
	
	public final void add(int value) {
		if (field.length == size) {
			resize(size * DUPL_FACTOR);
		}
		field[size++] = value;
	}
	
	// TODO:
	public final void addAll(IntCollection c) {
		throw new RuntimeException("NI");
	}
	
	public final void addList(IntList list) {
		for (int i = 0; i < list.size; i++) {
			add(list.field[i]);
		}
	}
	
	public final void remove(int index) {
		if (index < 0 || index >= size) throw new ArrayIndexOutOfBoundsException();
		for (int i = index + 1; i < size; i++) {
			field[i - 1] = field[i];
		}
		size--;
	}
	
	public final int pop() {
		return field[size--];
	}
	
	public final int top() {
		return field[size];
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.rg.axel.bsp.misc.IntCollection#clear()
	 */
	public final void clear() {
		size = 0;
	}
	
	public final void clear(int value) {
		for (int i = 0; i < field.length; i++) {
			field[i] = value;
		}
		size = 0;
	}
	
	public final void reset() {
		field = new int[DEFAULT_INITIAL_SIZE];
		size = 0;
	}
	
	public final int firstIndexOf(int val) {
		for (int i = 0; i < size; i++) {
			if (val == field[i]) return i;
		}
		return NOT_FOUND;
	}
	
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.rg.axel.bsp.misc.IntCollection#size()
	 */
	public final int size() {
		return size;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.rg.axel.bsp.misc.IntCollection#capacity()
	 */
	public final int capacity() {
		return field.length;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.rg.axel.bsp.misc.IntCollection#empty()
	 */
	public final boolean empty() {
		return (size == 0);
	}
	
	
	
	
	private final void resize(int newsize) {
		int [] newfield = new int[newsize];
		System.arraycopy(field, 0, newfield, 0, size);
		field = newfield;
	}
	
	// TODO
	public final Iterator iterator() {
		throw new RuntimeException("Not implemented");
	}
	
	
	
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof IntList)) return false;
		
		IntList other = (IntList)obj;
		
		if (this.size != other.size) return false;
		for (int i = 0; i < this.size; i++) {
			if (this.field[i] != other.field[i]) return false;
		}
		
		return true;
	}
	
	

	@Override
	public int hashCode() {
		int sum = 0;
		for (int i = 0; i < size; i++) {
			sum += field[i];
		}
		return sum;
	}

	@Override
	public String toString() {
		return super.toString() + "_IntList";
	}

	
	
	
	
	
	
	
}

























