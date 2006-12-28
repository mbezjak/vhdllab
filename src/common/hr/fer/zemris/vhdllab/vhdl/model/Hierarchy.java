package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.ajax.shared.XMLUtil;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class represents a set of pairs that together build hierarchical
 * grapht that is used to diplay to a user dependencies that exists between
 * files in cirtain project.
 * @author Miro Bezjak
 */
public class Hierarchy implements Iterable<Pair> {

	/** Serialization key for pair. */
	private static final String SERIALIZATION_KEY_PAIR = "pair";
	
	/** A set of pairs that represent this hierarchy */
	private Set<Pair> pairs;
	
	/**
	 * Constructor.
	 * @param pairs pairs that represents this hierarchy.
	 */
	public Hierarchy(Set<Pair> pairs) {
		this.pairs = new TreeSet<Pair>();
		this.pairs.addAll(pairs);
	}
	
	/**
	 * Returns a set of files that are not used in any other file. This method
	 * will never return <code>null</code> and will never return empty set
	 * because there must be at least one file that is not used in any other file.
	 * @return a set of files that are not used in any other file
	 */
	public Set<String> getRootNodes() {
		Set<String> rootNodes = new TreeSet<String>();
		for(Pair p : pairs) {
			if(p.getParents().isEmpty()) {
				rootNodes.add(p.getFileName());
			}
		}
		return rootNodes;
	}
	
    /**
     * Returns a set of files that are used in <code>parent</code> file. This
     * method will never return <code>null</code> although it may return empty
     * set.
     * @param parent a file for which children should be extracted
     * @return a set of files that are used in <code>parent</code> file
     */
    public Set<String> getChildrenForParent(String parent) {
    	Set<String> children = new TreeSet<String>();
    	for(Pair p : pairs) {
    		for(String parentOfPair : p.getParents()) {
    			if(parent.equalsIgnoreCase(parentOfPair)) {
    				children.add(p.getFileName());
    				break;
    			}
    		}
    	}
    	return children;
    }
   
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Pair> iterator() {
		return pairs.iterator();
	}
	
	/**
	 * Serializes this hierarchy.
	 * @return a serialized string
	 */
	public String serialize() {
		Properties p = new Properties();
		int i = 1;
		for(Pair pair : pairs) {
			p.setProperty(SERIALIZATION_KEY_PAIR + "." + i, pair.serialize());
		}
		return XMLUtil.serializeProperties(p);
	}
	
	/**
	 * Constructs hierarchy out of <code>data</code> that contains serialized
	 * string.
	 * @param data serialized string
	 * @return a hierarchy that <code>data</code> represents
	 */
	public static Hierarchy deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		Properties p = XMLUtil.deserializeProperties(data);
		if(p == null) throw new IllegalArgumentException("Unknown serialization format: data");
		Set<Pair> pairs = new TreeSet<Pair>();
		for(int i = 1; true; i++) {
			String pair = p.getProperty(SERIALIZATION_KEY_PAIR + "." + i);
			if(pair ==  null) break;
			pairs.add(Pair.deserialize(pair));
		}
		return new Hierarchy(pairs);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if (!(obj instanceof Hierarchy)) return false;
		Hierarchy other = (Hierarchy) obj;
		
		return this.pairs.equals(other.pairs);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return pairs.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(10 * pairs.size());
		sb.append("Hierarchy contains ").append(pairs.size()).append(" pairs:\n");
		for(Pair p : pairs) {
			sb.append(p.toString()).append("\n");
		}
		// remove last chacacter
		sb.replace(sb.length()-1, sb.length(), "");
		return sb.toString();
	}
}
