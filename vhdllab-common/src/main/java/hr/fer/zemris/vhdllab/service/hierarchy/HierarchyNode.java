package hr.fer.zemris.vhdllab.service.hierarchy;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.util.EntityUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Represents a hierarchy (dependency tree) node for one file. Hierarchy node
 * contains only basic information about the file (lightweight clone) and all
 * files that a file depends on (uses).
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 * @see EntityUtils#lightweightClone(File)
 */
public final class HierarchyNode implements Serializable {

    /*
     * Please note that although this class implements java.io.Serializable it
     * does not implement readObject method as specified by Joshua Bloch,
     * "Effective Java: Programming Language Guide",
     * "Item 56: Write readObject methods defensively", page 166.
     * 
     * The reason for this is that this class is used to transfer data from
     * server to client (reverse is not true). So by altering byte stream
     * attacker can only hurt himself!
     */
    private static final long serialVersionUID = -2215053203067361627L;

    private final File file;
    private final Set<File> dependencies;
    private transient HierarchyNode parent;

    public HierarchyNode(File file, HierarchyNode parent) {
        Validate.notNull(file, "File can't be null");
        this.file = EntityUtils.lightweightClone(file);
        this.dependencies = new HashSet<File>();
        this.parent = parent;
        if (this.parent != null) {
            this.parent.addDependency(this);
        }
    }

    public void addDependency(HierarchyNode node) {
        if (dependencies.contains(node.getFile())) {
            throw new IllegalArgumentException(
                    "Node already added as a dependency: " + node);
        }
        /*
         * disallow cyclic dependencies!
         */
        if (canFormCyclicDependency(this, node)) {
            throw new IllegalArgumentException(node + "already uses " + this);
        }
        dependencies.add(node.getFile());
        node.parent = this;
    }

    private static boolean canFormCyclicDependency(HierarchyNode node,
            HierarchyNode depNode) {
        if (node == null) {
            return false;
        }
        if (node.equals(depNode)) {
            return true;
        }
        return canFormCyclicDependency(node.parent, depNode);
    }

    public boolean hasDependencies() {
        return !dependencies.isEmpty();
    }

    public File getFile() {
        return file;
    }

    public Set<File> getDependencies() {
        return Collections.unmodifiableSet(dependencies);
    }

    HierarchyNode getParent() {
        return parent;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(file)
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof HierarchyNode))
            return false;
        HierarchyNode other = (HierarchyNode) obj;
        return new EqualsBuilder()
                    .append(file, other.file)
                    .isEquals();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder((dependencies.size() + 1) * 15);
        sb.append(file.getName()).append(" [");
        for (File dep : dependencies) {
            sb.append(dep.getName()).append(",");
        }
        if (!dependencies.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }

}
