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
package hr.fer.zemris.vhdllab.entity;

import hr.fer.zemris.vhdllab.validation.NameFormatConstraint;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.Size;

@Entity
@Table(name = "projects", uniqueConstraints = { @UniqueConstraint(columnNames = {
        "user_id", "name" }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Project extends OwnedEntity {

    private static final long serialVersionUID = 7535295608182412183L;

    @Size(max = 50)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    private Set<File> files;

    public Project() {
        this(null, null);
    }

    public Project(String name) {
        this(null, name);
    }

    public Project(String userId, String name) {
        super(userId, name);
        initFiles();
    }

    public Project(Project clone) {
        super(clone);
        initFiles();
    }

    @NameFormatConstraint
    @Override
    public String getName() {
        return super.getName();
    }

    public Set<File> getFiles() {
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }

    private void initFiles() {
        this.files = new HashSet<File>();
    }

    public void addFile(File file) {
        Validate.notNull(file, "File can't be null");
        if (file.getProject() != null) {
            if (file.getProject() == this) {
                // a file is already in this project
                return;
            }
            throw new IllegalArgumentException(file.toString()
                    + " already belongs to some project; this project is "
                    + this.toString());
        }
        /*
         * Must set a project to a file before adding it to a collection because
         * project is required for hash code calculation (in file).
         * 
         * @see File#hashCode()
         */
        file.setProject(this);
        getFiles().add(file);
    }

    public void removeFile(File file) {
        Validate.notNull(file, "File can't be null");
        if (file.getProject() != this) {
            throw new IllegalArgumentException(file.toString()
                    + " doesn't belong to this " + this.toString());
        }
        // referencing though getter because of lazy loading
        if (getFiles().remove(file)) {
            file.setProject(null);
        }
    }

    @Override
    public String toString() {
        // referencing though getter because of lazy loading
        return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .append("files", getFiles(), false)
                    .toString();
    }

}
