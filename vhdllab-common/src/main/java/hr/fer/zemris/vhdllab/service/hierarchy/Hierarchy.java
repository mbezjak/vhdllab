package hr.fer.zemris.vhdllab.service.hierarchy;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.util.EntityUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public final class Hierarchy implements Serializable {

    private static final long serialVersionUID = 6608251150623300335L;

    private final Project project;
    private final Map<File, HierarchyNode> nodes;
    private transient Set<HierarchyNode> topLevelNodes;
    private transient Set<HierarchyNode> bottomLevelNodes;

    public Hierarchy(Project project, Collection<HierarchyNode> collection) {
        Validate.notNull(project, "Project can't be null");
        Validate.notNull(collection, "Nodes can't be null");
        this.project = EntityUtils.lightweightClone(project);
        this.nodes = new LinkedHashMap<File, HierarchyNode>(collection.size());
        for (HierarchyNode n : collection) {
            this.nodes.put(n.getFile(), n);
        }
        this.initNodes();
    }

    public Project getProject() {
        return project;
    }

    public HierarchyNode getNode(File file) {
        Validate.notNull(file);
        return nodes.get(new File(file));
    }

    public int getFileCount() {
        return nodes.size();
    }

    public boolean fileHasDependency(File file, File dependency) {
        Validate.notNull(file, "File can't be null");
        Validate.notNull(dependency, "Dependency can't be null");
        HierarchyNode fileNode = getNode(file);
        Validate.notNull(fileNode, "File isn't in hierarchy: " + file);
        return fileNode.containsDependency(dependency);
    }

    public Collection<HierarchyNode> getAllNodes() {
        return nodes.values();
    }

    public Set<HierarchyNode> getTopLevelNodes() {
        return topLevelNodes;
    }

    public Set<HierarchyNode> getBottomLevelNodes() {
        return bottomLevelNodes;
    }

    public Set<HierarchyNode> getDependenciesFor(HierarchyNode node) {
        Validate.notNull(node, "Hierarchy node can't be null");
        Set<File> nodeDependencies = node.getDependencies();
        Set<HierarchyNode> dependencies = new HashSet<HierarchyNode>(
                nodeDependencies.size());
        for (File f : nodeDependencies) {
            dependencies.add(nodes.get(f));
        }
        return dependencies;
    }

    public Set<HierarchyNode> getParentsFor(HierarchyNode node) {
        Validate.notNull(node, "Hierarchy node can't be null");
        Set<HierarchyNode> parents = new HashSet<HierarchyNode>();
        for (HierarchyNode n : nodes.values()) {
            if (n.getDependencies().contains(node.getFile())) {
                parents.add(n);
            }
        }
        return parents;
    }

    public Iterator<HierarchyNode> iteratorFlatHierarchy() {
        return nodes.values().iterator();
    }

    public Iterator<HierarchyNode> iteratorXUsesYHierarchy(HierarchyNode node) {
        if (node == null) {
            return topLevelNodes.iterator();
        }
        return getDependenciesFor(node).iterator();
    }

    public Iterator<HierarchyNode> iteratorXUsedByYHierarchy(HierarchyNode node) {
        if (node == null) {
            return bottomLevelNodes.iterator();
        }
        return getParentsFor(node).iterator();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(project).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Hierarchy))
            return false;
        Hierarchy other = (Hierarchy) obj;
        return new EqualsBuilder().append(project, other.project).isEquals();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(30 + nodes.size() * 30);
        sb.append("Hierarchy for user=").append(project.getUserId());
        sb.append(", project=").append(project.getName()).append(" {\n");
        for (HierarchyNode node : nodes.values()) {
            sb.append(node).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    private void initNodes() {
        Collection<HierarchyNode> allNodes = nodes.values();
        topLevelNodes = new HashSet<HierarchyNode>(allNodes);
        bottomLevelNodes = new HashSet<HierarchyNode>();
        for (HierarchyNode n : allNodes) {
            if (!n.hasDependencies()) {
                bottomLevelNodes.add(n);
            }
            for (File f : n.getDependencies()) {
                topLevelNodes.remove(nodes.get(f));
            }
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();

        this.initNodes();
    }

}
