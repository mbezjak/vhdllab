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
package hr.fer.zemris.vhdllab.service.hierarchy;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.util.EntityUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Represents a hierarchy (dependency tree) node for one file. Hierarchy node
 * contains only basic information about the file (lightweight clone) and all
 * files that a file depends on (uses).
 *
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
    private final transient Set<String> missingDependencies;
    private transient HierarchyNode parent;

    public HierarchyNode(File file, HierarchyNode parent) {
        Validate.notNull(file, "File can't be null");
        this.file = EntityUtils.lightweightClone(file);
        this.dependencies = new LinkedHashSet<File>();
        this.missingDependencies = new LinkedHashSet<String>();
        this.parent = parent;
        if (this.parent != null) {
            this.parent.addDependency(this);
        }
    }

    public void addDependency(HierarchyNode node) {
        if (dependencies.contains(node.getFile())) {
            return; // duplicate dependencies are ignored
        }
        /*
         * disallow cyclic dependencies!
         */
        if (canFormCyclicDependency(this, node)) {
            return; // cyclic dependencies are ignored
        }
        dependencies.add(node.getFile());
        node.parent = this;
    }

    public void addMissingDependency(String name) {
        if (!StringUtils.isBlank(name)) {
            missingDependencies.add(name);
        }
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

    public Set<String> getMissingDependencies() {
        return Collections.unmodifiableSet(missingDependencies);
    }

    public boolean containsDependency(File dependency) {
        Validate.notNull(dependency, "Dependency can't be null");
        return dependencies.contains(new File(dependency));
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
