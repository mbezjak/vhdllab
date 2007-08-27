package hr.fer.zemris.vhdllab.applets.schema2.misc;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;





/**
 * 
 * @author Axel
 * 
 * @param <Tp>
 *            Must have implemented equals and hashCode, and must be Comparable.
 */
public class CostSortedHash<Key extends Comparable, Tp> {
	
	private static class CostWrapper implements Comparable {
		public int cost;
		public Comparable key;
		public CostWrapper(int c, Comparable k) { cost = c; key = k; }
		public int compareTo(Object o) {
			CostWrapper other = (CostWrapper)o;
			if (this.cost < other.cost) return -1;
			if (this.cost > other.cost) return 1;
			return this.key.compareTo(other.key);
		}
		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof CostWrapper)) return false;
			CostWrapper other = (CostWrapper)obj;
			return this.cost == other.cost && this.key.equals(other.key);
		}
		@Override
		public int hashCode() {
			return cost << 16 + key.hashCode();
		}
	}
	
	private static class TpWrapper {
		public Object value;
		public int cost;
		public TpWrapper(int c, Object val) { cost = c; value = val; }
	}

	/* private fields */
	private CostWrapper finder;
	private HashMap<Key, TpWrapper> hash;
	private TreeSet<CostWrapper> tree;

	
	/* ctors */
	
	public CostSortedHash() {
		finder = new CostWrapper(0, null);
		hash = new HashMap<Key, TpWrapper>();
		tree = new TreeSet<CostWrapper>();
	}
	
	
	

	/* methods */
	
	public boolean add(Key key, int cost, Tp elem) {
		if (hash.containsKey(key)) return false;
		
		/* add to hash */
		hash.put(key, new TpWrapper(cost, elem));
		
		/* add to tree */
		tree.add(new CostWrapper(cost, key));
		
		return true;
	}
	
	public Tp get(Key key) {
		return (Tp) hash.get(key);
	}

	public Key cheapest() {
		CostWrapper cw = tree.first();
		return (Key) ((cw == null) ? (null) : (cw.key));
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
		TpWrapper tpw = hash.get(key);
		
		if (tpw == null) throw new IllegalArgumentException("Not in collection.");
		return tpw.cost;
	}
	
	public boolean updateCost(Key key, int cost) {
		if (!hash.containsKey(key)) return false;
		
		/* update tree */
		TpWrapper tpw = hash.get(key);
		finder.cost = tpw.cost;
		finder.key = key;
		tree.remove(finder);
		tree.add(new CostWrapper(cost, key));
		
		/* update hash */
		tpw.cost = cost;
		
		return true;
	}

	

}














