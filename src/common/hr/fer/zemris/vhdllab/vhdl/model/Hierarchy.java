package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.ajax.shared.XMLUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
	/** Serialization key for project name. */
	private static final String SERIALIZATION_KEY_PROJECT_NAME = "project.name";
	
	/** A name of a project for whom this hierachy is created */
	private String projectName;
	/** A set of pairs that represent this hierarchy */
	private Set<Pair> pairs;
	/** A map of pairs for quick access to hierarchy */
	private Map<String, Pair> pairsMap;
	
	/**
	 * Constructor.
	 * @param projectName a name of a project for whom this hierachy is created
	 * @throws NullPointerException if <code>projectName</code> is <code>null</code>
	 */
	public Hierarchy(String projectName) {
		if(projectName == null) {
			throw new NullPointerException("Project name or pairs can not be null.");
		}
		this.pairs = new TreeSet<Pair>();
		this.pairsMap = new HashMap<String, Pair>();
		this.projectName = projectName;
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
    
    /**
     * Getter for project name
     * @return a name of a project
     */
    public String getProjectName() {
		return projectName;
	}
    
    /**
     * Returns a pair containing <code>fileName</code>. If this hierarchy does
     * not contain such file name this method will return <code>null</code>.
     * @param fileName a name of a file for which to return <code>Pair</code>
     * @return a pair containing <code>fileName</code>
     */
    public Pair getPair(String fileName) {
    	if(fileName == null) {
    		throw new NullPointerException("File name can not be null.");
    	}
		return pairsMap.get(fileName.toUpperCase());
	}
    
    /**
     * Adds a pair to this hierarchy
     * @param pair a pair to add
     * @throws NullPointerException if <code>pair</code> is <code>null</code>
     */
    public void addPair(Pair pair) {
		if(pair == null) {
			throw new NullPointerException("Pair can not be null.");
		}
		pairs.add(pair);
		pairsMap.put(pair.getFileName().toUpperCase(), pair);
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
		p.setProperty(SERIALIZATION_KEY_PROJECT_NAME, projectName);
		int i = 1;
		for(Pair pair : pairs) {
			p.setProperty(SERIALIZATION_KEY_PAIR + "." + i, pair.serialize());
			i++;
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
		String projectName = p.getProperty(SERIALIZATION_KEY_PROJECT_NAME);
		Hierarchy h = new Hierarchy(projectName);
		for(int i = 1; true; i++) {
			String serializedPair = p.getProperty(SERIALIZATION_KEY_PAIR + "." + i);
			if(serializedPair ==  null) break;
			Pair pair = Pair.deserialize(serializedPair);
			h.addPair(pair);
		}
		return h;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if (!(obj instanceof Hierarchy)) return false;
		Hierarchy other = (Hierarchy) obj;
		
		return this.projectName.equals(other.projectName) && 
			this.pairs.equals(other.pairs);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return projectName.hashCode() ^ pairs.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(10 * pairs.size());
		sb.append("Hierarchy for project '").append(projectName)
			.append("' contains ").append(pairs.size()).append(" pairs:\n");
		for(Pair p : pairs) {
			sb.append(p.toString()).append("\n");
		}
		// remove last chacacter (\n)
		sb.replace(sb.length()-1, sb.length(), "");
		return sb.toString();
	}
}
