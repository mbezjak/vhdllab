package hr.fer.zemris.vhdllab.applets.editor.schema2.misc;




public interface IntSet extends IntCollection, Iterable<Integer> {

	
	/**
	 * Puts the specified value into this
	 * hash under the specified key.
	 * 
	 * @param key
	 * Integer key. If the key already exists
	 * in dictionary, then the old value will be
	 * overwritten.
	 * @param value
	 * Value to put under the specified key. It can
	 * also be null.
	 */
	void add(int key);

	/**
	 * Checks for existance of the specified key.
	 * 
	 * @param key
	 * @return
	 * True if the key is in the hashmap, false otherwise.
	 */
	boolean contains(int key);

	/**
	 * Removes the element under the specified key.
	 * 
	 * @param key
	 * The key that specified the element to be removed.
	 * If the key does not exist, nothing will be removed.
	 */
	void remove(int key);

	void setLoadFactor(float hashLoadFactor);

	void reset(int newsize);

}