package hr.fer.zemris.vhdllab.vhdl.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class represents a set of pairs that together build hierarchical graph
 * that is used to display to user dependencies that exists between files in
 * certain project.
 * 
 * @author Miro Bezjak
 */
public class Hierarchy implements Iterable<Pair>, Serializable {

	private static final long serialVersionUID = 1L;

	/** A name of a project for whom this hierarchy is created */
	private String projectName;

	/** A set of pairs that represent this hierarchy */
	private Set<Pair> pairs;

	/** A map of pairs for quick access to hierarchy */
	private transient Map<String, Pair> pairsMap;

	/**
	 * Constructor.
	 * 
	 * @param projectName
	 *            a name of a project for whom this hierarchy is created
	 * @throws NullPointerException
	 *             if <code>projectName</code> is <code>null</code>
	 */
	public Hierarchy(String projectName) {
		if (projectName == null) {
			throw new NullPointerException(
					"Project name or pairs can not be null.");
		}
		this.pairs = new TreeSet<Pair>();
		this.pairsMap = new HashMap<String, Pair>();
		this.projectName = projectName;
	}

	/**
	 * Returns a set of files that are not used in any other file. This method
	 * will never return <code>null</code> and will never return empty set
	 * because there must be at least one file that is not used in any other
	 * file.
	 * 
	 * @return a set of files that are not used in any other file
	 */
	public Set<String> getRootNodes() {
		Set<String> rootNodes = new TreeSet<String>();
		for (Pair p : pairs) {
			if (p.getParents().isEmpty()) {
				rootNodes.add(p.getFileName());
			}
		}
		return rootNodes;
	}

	/**
	 * Returns a set of files that are used in <code>parent</code> file. This
	 * method will never return <code>null</code> although it may return empty
	 * set.
	 * 
	 * @param parent
	 *            a file for which children should be extracted
	 * @return a set of files that are used in <code>parent</code> file
	 */
	public Set<String> getChildrenForParent(String parent) {
		Set<String> children = new TreeSet<String>();
		for (Pair p : pairs) {
			for (String parentOfPair : p.getParents()) {
				if (parent.equalsIgnoreCase(parentOfPair)) {
					children.add(p.getFileName());
					break;
				}
			}
		}
		return children;
	}

	/**
	 * Returns all descendats for a specified parent. Return value will never be
	 * <code>null</code> although it can be empty set.
	 * 
	 * @param parent
	 *            a parent for whom to get descendants for
	 * @return all descendants
	 * @throws NullPointerException if <code>parent</code> is <code>null</code>
	 */
	public Set<String> getDescendantsForParent(String parent) {
		if (parent == null) {
			throw new NullPointerException("Parent cant be null");
		}
		Set<String> descendants = new HashSet<String>();
		getDescendantsForParentRecursive(descendants, parent);
		return descendants;
	}

	private void getDescendantsForParentRecursive(Set<String> descendants,
			String parent) {
		// TODO napravit da hierarija nema kruyne yavistnosti inace je ovo
		// beskonacna petlja!!!
		for (String d : getChildrenForParent(parent)) {
			descendants.add(d);
			getDescendantsForParentRecursive(descendants, d);
		}
	}

	/**
	 * Getter for project name
	 * 
	 * @return a name of a project
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * Returns a pair containing <code>fileName</code>. If this hierarchy
	 * does not contain such file name this method will return <code>null</code>.
	 * 
	 * @param fileName
	 *            a name of a file for which to return <code>Pair</code>
	 * @return a pair containing <code>fileName</code>
	 */
	public Pair getPair(String fileName) {
		if (fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		return pairsMap.get(fileName.toUpperCase());
	}

	/**
	 * Adds a pair to this hierarchy
	 * 
	 * @param pair
	 *            a pair to add
	 * @throws NullPointerException
	 *             if <code>pair</code> is <code>null</code>
	 */
	public void addPair(Pair pair) {
		if (pair == null) {
			throw new NullPointerException("Pair can not be null.");
		}
		pairs.add(pair);
		pairsMap.put(pair.getFileName().toUpperCase(), pair);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Pair> iterator() {
		return pairs.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Hierarchy))
			return false;
		Hierarchy other = (Hierarchy) obj;

		return this.projectName.equals(other.projectName)
				&& this.pairs.equals(other.pairs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return projectName.hashCode() ^ pairs.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(10 * pairs.size());
		sb.append("Hierarchy for project '").append(projectName).append(
				"' contains ").append(pairs.size()).append(" pairs:\n");
		for (Pair p : pairs) {
			sb.append(p.toString()).append("\n");
		}
		// remove last character (\n)
		sb.replace(sb.length() - 1, sb.length(), "");
		return sb.toString();
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();

		pairsMap = new HashMap<String, Pair>(pairs.size());
		for (Pair p : pairs) {
			pairsMap.put(p.getFileName().toUpperCase(), p);
		}
	}

}
