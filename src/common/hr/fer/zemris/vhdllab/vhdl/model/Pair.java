package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.ajax.shared.XMLUtil;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class represents one <code>file</code> and its relation with other files
 * in the same project. Relation is represented as a set of files (that does not
 * contain <code>file</code>) that all use <code>file</code>.
 * @author Miro Bezjak
 */
public class Pair implements Comparable<Pair>{
	
	/** Serialization key for file name */
	private static final String SERIALIZATION_KEY_FILE_NAME = "file.name";
	/** Serialization key for file type */
	private static final String SERIALIZATION_KEY_FILE_TYPE = "file.type";
	/** Serialization key for a parent */
	private static final String SERIALIZATION_KEY_PARENT = "parent";

	/** A set of files in which <code>fileName</code> is used */
	private Set<String> parents;
	/** A name of a file that this pair represents */
	private String fileName;
	/** A file type of <code>fileName</code> */
	private String fileType;
	
	/**
	 * Constructor. <code>FileName</code> is case insensitive.
	 * @param fileName a name of a file that this pair represents
	 * @param fileType a type of a file that this pair represents
	 * @throws NullPointerException if <code>fileName</code>,
	 * 		<code>fileType</code> or <code>parents</code> is <code>null</code>
	 */
	public Pair(String fileName, String fileType) {
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		if(fileType == null) {
			throw new NullPointerException("File type can not be null.");
		}
		this.fileName = fileName;
		this.fileType = fileType;
		this.parents = new TreeSet<String>();
	}

	/**
	 * Getter for file name. Will never return <code>null</code>. File name is
	 * case insensitive.
	 * @return a name of a file
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Getter for file type.
	 * @return a type of a file
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * Getter for parents of file (or in which files is this file used for).
	 * Will never return <code>null</code>. This method will return unmodifiable
	 * set. Files in this set are case insensitive.
	 * @return parents of file
	 */
	public Set<String> getParents() {
		return Collections.unmodifiableSet(parents);
	}
	
	/**
	 * Add parent to this pair. <code>Parent</code> represents file name of another
	 * file and therfor is case insensitive.
	 * @param parent
	 * @throws NullPointerException if <code>parent</code> is <code>null</code>
	 */
	public void addParent(String parent) {
		if(parent == null)  {
			throw new NullPointerException("Parent can not be null.");
		}
		if(fileName.equalsIgnoreCase(parent)) return;
		for(String p : parents) {
			if(parent.equalsIgnoreCase(p)) return;
		}
		this.parents.add(parent);
	}
	
	/**
	 * Serializes this pair.
	 * @return a serialized string
	 */
	public String serialize() {
		Properties p = new Properties();
		p.setProperty(SERIALIZATION_KEY_FILE_NAME, fileName);
		p.setProperty(SERIALIZATION_KEY_FILE_TYPE, fileType);
		int i = 1;
		for(String parent : parents) {
			p.setProperty(SERIALIZATION_KEY_PARENT + "." + i, parent);
		}
		return XMLUtil.serializeProperties(p);
	}
	
	/**
	 * Constructs pair out of <code>data</code> that contains serialized string.
	 * @param data serialized string
	 * @return a pair that <code>data</code> represents
	 */
	public static Pair deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		Properties p = XMLUtil.deserializeProperties(data);
		if(p == null) throw new IllegalArgumentException("Unknown serialization format: data");
		String fileName = p.getProperty(SERIALIZATION_KEY_FILE_NAME);
		String fileType = p.getProperty(SERIALIZATION_KEY_FILE_TYPE);
		Pair pair = new Pair(fileName, fileType);
		for(int i = 1; true; i++) {
			String parent = p.getProperty(SERIALIZATION_KEY_PARENT + "." + i);
			if(parent ==  null) break;
			pair.addParent(parent);
		}
		return pair;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Pair)) return false;
		Pair other = (Pair) obj;
		
		if(!(this.fileName.equalsIgnoreCase(other.fileName) &&
				this.fileType.equals(other.fileType))) return false;
		if(this.parents.size() != other.parents.size()) return false;
		// Compare two sets by ignoring case
		boolean found = false;
		for(String p : parents) {
			found = false;
			for(String pOther : other.parents) {
				if(p.equalsIgnoreCase(pOther)) {
					found = true;
				}
			}
			if(!found) return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = fileName.toUpperCase().hashCode();
		hash ^= fileType.hashCode();
		for(String p : parents) {
			hash ^= p.toUpperCase().hashCode();
		}
		return hash;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Pair o) {
		if(this.equals(o)) return 0;
		int compare = this.fileName.compareToIgnoreCase(o.getFileName());
		if(compare != 0) return compare;

		compare = this.fileType.compareTo(o.fileType);
		if(compare != 0) return compare;

		compare = parents.size() - o.parents.size();
		if(compare != 0) return compare;
		
		int numberOfOccurences = 0;
		for(String p : parents) {
			numberOfOccurences = 0;
			for(String pOther : parents) {
				if(p.equalsIgnoreCase(pOther)) {
					numberOfOccurences++;
				}
			}
			if(numberOfOccurences < 1) return 1;
			else if (numberOfOccurences > 1) return -1;
		}
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(10 * parents.size());
		sb.append(fileName).append(" with type ").append(fileType)
			.append(" has ").append(parents.size()).append(" parents:\n");
		for(String p : parents) {
			sb.append(p).append("\n");
		}
		// remove last chacacter (\n)
		sb.replace(sb.length()-1, sb.length(), "");
		return sb.toString();
	}

}
