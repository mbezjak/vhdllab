package hr.fer.zemris.vhdllab.api.hierarchy;

import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.util.Caseless;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a hierarchy (dependency tree) node for one file. Hierarchy node
 * contains only basic information about the file and names of all files that a
 * file depends on (uses). Once a node is serialized it can no longed be
 * modified and becomes immutable.
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class HierarchyNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * A name of a file that a node represents.
     *
     * @serial
     */
    private final Caseless fileName;
    /**
     * A type of a file that a node represents.
     *
     * @serial
     */
    private final FileType fileType;
    /**
     * A set of file names that a file (represented by a node) depends upon
     * (uses).
     *
     * @serial
     */
    private final Set<Caseless> dependencies;
    /**
     * A parent to a hierarchy node. It is used to disallow cyclic dependencies.
     */
    private transient HierarchyNode parent;
    /**
     * A flag indicating if a hierarchy node is mutable or not. Once a node is
     * deserialized it becomes immutable.
     */
    private final transient boolean mutable;

    /**
     * Copy constructor.
     */
    HierarchyNode(HierarchyNode node) {
        this.fileName = node.fileName;
        this.fileType = node.fileType;
        this.dependencies = new HashSet<Caseless>(node.dependencies);
        this.parent = null;
        this.mutable = false;
        if (fileName == null) {
            throw new NullPointerException("File name cant be null");
        }
        if (fileType == null) {
            throw new NullPointerException("File type cant be null");
        }
    }

    /**
     * Constructs a hierarchy node and adds itself as a dependency to
     * <code>parent</code> node provided that its not <code>null</code>.
     *
     * @param fileName
     *            a name of a file that a node represents
     * @param fileType
     *            a type of a file that a node represents
     * @param parent
     *            a parent hierarchy node or <code>null</code> for root node
     * @throws NullPointerException
     *             if <code>fileName</code> or <code>fileType</code> is
     *             <code>null</code>
     * @throws IllegalArgumentException
     *             if parent already contains specified <code>fileName</code>
     *             or a cyclic dependency was found
     * @throws IllegalStateException
     *             if node is no longed mutable
     */
    public HierarchyNode(String fileName, FileType fileType, HierarchyNode parent) {
        if (fileName == null) {
            throw new NullPointerException("File name cant be null");
        }
        if (fileType == null) {
            throw new NullPointerException("File type cant be null");
        }
        this.fileName = new Caseless(fileName);
        this.fileType = fileType;
        this.dependencies = new HashSet<Caseless>();
        this.parent = parent;
        this.mutable = true;
        if (parent != null) {
            parent.addDependency(this);
        }
    }

    /**
     * Adds a new dependency to this node. After a node is deserialized it
     * becomes immutable.
     *
     * @param node
     *            a dependency to add
     * @throws NullPointerException
     *             if <code>node</code> is <code>null</code>
     * @throws IllegalArgumentException
     *             if specified <code>node</code> is already added as a
     *             dependency or a cyclic dependency was found
     * @throws IllegalStateException
     *             if node is no longed mutable
     */
    public void addDependency(HierarchyNode node) {
        if (!mutable) {
            throw new IllegalStateException("Node is no longed mutable");
        }
        if (contains(node.getFileName())) {
            throw new IllegalArgumentException(
                    "Node already added as a dependency: " + node);
        }
        /*
         * disallow cyclic dependencies!
         */
        if (canFormCyclicDependency(this, node)) {
            throw new IllegalArgumentException(node + "already uses " + this);
        }
        dependencies.add(new Caseless(node.getFileName()));
        node.parent = this;
    }

    /**
     * Returns <code>true</code> if a node contains specified dependency of
     * <code>false</code> otherwise.
     *
     * @param name
     *            a name of a dependency
     * @return <code>true</code> if a node contains specified dependency of
     *         <code>false</code> otherwise
     */
    private boolean contains(String name) {
        for (Caseless dep : dependencies) {
            if (dep.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns <code>true</code> if specified <code>depNode</code> contains
     * direct or transitive dependency to <code>node</code> and thus forming a
     * cyclic dependency or <code>false</code> otherwise.
     * <p>
     * This method should be invoked before adding <code>depNode</code> as a
     * dependency to <code>node</code>.
     * </p>
     *
     * @param node
     *            a possible direct or transitive dependency to
     *            <code>depNode</code>
     * @param depNode
     *            a dependency node to test if it can form cyclic dependency
     * @return <code>true</code> if cyclic dependency can form or
     *         <code>false</code> otherwise
     */
    private static boolean canFormCyclicDependency(HierarchyNode node,
            HierarchyNode depNode) {
        if (node == null) {
            return false;
        }
        if (node.getFileName().equals(depNode.getFileName())) {
            return true;
        }
        return canFormCyclicDependency(node.parent, depNode);
    }

    /**
     * Returns a name of a file that a node represents. Return value can never
     * be <code>null</code>.
     *
     * @return a name of a file that a node represents
     */
    public String getFileName() {
        return fileName.toString();
    }

    /**
     * Returns a type of a file that a node represents. Return value can never
     * be <code>null</code>.
     *
     * @return a type of a file that a node represents
     */
    public FileType getFileType() {
        return fileType;
    }

    /**
     * Returns an unmodifiable collection of file names that a file (represented
     * by a node) depends upon (uses). Return value can never be
     * <code>null</code> although it can be empty collection.
     *
     * @return an unmodifiable collection of file names that a file (represented
     *         by a node) depends upon (uses)
     */
    public Set<Caseless> getDependencies() {
        return Collections.unmodifiableSet(dependencies);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + fileName.toLowerCase().hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof HierarchyNode))
            return false;
        final HierarchyNode other = (HierarchyNode) obj;
        return fileName.equals(other.fileName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(20 + dependencies.size() * 10);
        sb.append(fileName).append(".").append(fileType);
        sb.append(" ").append(dependencies);
        return sb.toString();
    }

    /**
     * Make a defensive copy.
     */
    private Object readResolve() {
        return new HierarchyNode(this);
    }

}
