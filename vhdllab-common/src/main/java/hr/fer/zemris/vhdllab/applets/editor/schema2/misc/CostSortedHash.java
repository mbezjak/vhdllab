/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.schema2.misc;

import java.util.HashMap;
import java.util.TreeSet;

/**
 * 
 * @author Axel
 * 
 * @param <Tp>
 *            Must have implemented equals and hashCode, and must be Comparable.
 */
public class CostSortedHash<Key extends Comparable<Key>, Tp> {

	private static class CostWrapper<K extends Comparable<K>> implements Comparable<CostWrapper<K>> {
		public int cost;
		public K key;

		public CostWrapper(int c, K k) {
			cost = c;
			key = k;
		}

		public int compareTo(CostWrapper<K> other) {
			if (this.cost < other.cost)
				return -1;
			if (this.cost > other.cost)
				return 1;
			return this.key.compareTo(other.key);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof CostWrapper))
				return false;
			CostWrapper other = (CostWrapper) obj;
			return this.cost == other.cost && this.key.equals(other.key);
		}

		@Override
		public int hashCode() {
			return cost << 16 + key.hashCode();
		}
	}

	private static class TpWrapper<T> {
		public T value;
		public int cost;

		public TpWrapper(int c, T val) {
			cost = c;
			value = val;
		}
	}

	
	/* private fields */
	private CostWrapper<Key> finder;
	private HashMap<Key, TpWrapper<Tp>> hash;
	private TreeSet<CostWrapper<Key>> tree;

	
	
	/* ctors */

	public CostSortedHash() {
		finder = new CostWrapper<Key>(0, null);
		hash = new HashMap<Key, TpWrapper<Tp>>();
		tree = new TreeSet<CostWrapper<Key>>();
	}

	
	
	
	/* methods */

	public boolean add(Key key, int cost, Tp elem) {
		if (hash.containsKey(key)) return false;

		/* add to hash */
		hash.put(key, new TpWrapper<Tp>(cost, elem));

		/* add to tree */
		tree.add(new CostWrapper<Key>(cost, key));

		return true;
	}

	public Tp get(Key key) {
		TpWrapper<Tp> t = hash.get(key);
		return (t != null) ? (t.value) : (null);
	}

	public Key cheapest() {
		CostWrapper<Key> cw = tree.first();
		return (cw == null) ? (null) : (cw.key);
	}

	public boolean isEmpty() {
		return hash.isEmpty();
	}

	public int size() {
		return hash.size();
	}

	public boolean contains(Key key) {
		return hash.containsKey(key);
	}

	public boolean remove(Key key) {
		if (!hash.containsKey(key)) return false;
		
		/* remove from tree */
		finder.cost = hash.get(key).cost;
		finder.key = key;
		tree.remove(finder);
		
		/* remove from hash */
		hash.remove(key);
		
		return true;
	}

	/**
	 * @throws IllegalArgumentException
	 *             U slucaju da key nije u kolekciji.
	 */
	public int getCost(Key key) {
		TpWrapper<Tp> tpw = hash.get(key);

		if (tpw == null)
			throw new IllegalArgumentException("Not in collection.");
		return tpw.cost;
	}

	public boolean updateCost(Key key, int cost) {
		if (!hash.containsKey(key)) return false;

		/* update tree */
		TpWrapper<Tp> tpw = hash.get(key);
		finder.cost = tpw.cost;
		finder.key = key;
		tree.remove(finder);
		tree.add(new CostWrapper<Key>(cost, key));

		/* update hash */
		tpw.cost = cost;

		return true;
	}

}















