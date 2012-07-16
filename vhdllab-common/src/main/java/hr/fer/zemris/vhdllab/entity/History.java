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

import hr.fer.zemris.vhdllab.validation.DeletedOnGreaterThenCreatedOnConstraint;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;

/**
 * All entity classes that implement this feature also have a separate table
 * that contains all changes throughout a history for every unique entity in
 * original table. In other words every change in an entity, be that change of a
 * name, data or simple deletion of an entity, is recorded in separate history
 * table for archival and statistical purposes.
 * <p>
 * Names of history entities are composed of original entity name plus
 * <i>History</i> suffix.
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@DeletedOnGreaterThenCreatedOnConstraint
@Embeddable
public class History implements Serializable {

    private static final long serialVersionUID = 6192687679834053487L;

    @NotNull
    @Min(value = 0)
    @Column(name = "insert_version", updatable = false)
    private Integer insertVersion;
    @NotNull
    @Min(value = 0)
    @Column(name = "update_version", updatable = false)
    private Integer updateVersion;
    @NotNull
    @Column(name = "created_on", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "deleted_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedOn;

    public History() {
        this(0, 0);
    }

    public History(Integer insertVersion, Integer updateVersion) {
        super();
        setInsertVersion(insertVersion);
        setUpdateVersion(updateVersion);
        setCreatedOn(new Date());
    }

    public History(History clone) {
        setInsertVersion(clone.insertVersion);
        setUpdateVersion(clone.updateVersion);
        setCreatedOn(clone.createdOn);
        setDeletedOn(clone.deletedOn);
    }

    /**
     * Insert version is essentially a number of times that one particular
     * unique entity was created. For example, an entity is considered unique if
     * it has unique properties A and B. If such entity is created, then deleted
     * and created again insert version would be 2 and history table would show
     * all such changes. Insert versions start at 0.
     * 
     * @return an insert version
     */
    public Integer getInsertVersion() {
        return insertVersion;
    }

    public void setInsertVersion(Integer insertVersion) {
        this.insertVersion = insertVersion;
    }

    /**
     * Update version is a number of times that an entity was updated (changed).
     * For example, if a name of an entity was changed then update version would
     * be incremented. Update versions starts at 0.
     * <p>
     * Note: update version is similar to version property that every entity
     * already has. But unlike version property, update version is manually set
     * and is not used for optimistic locking.
     * </p>
     * 
     * @return an update version
     */
    public Integer getUpdateVersion() {
        return updateVersion;
    }

    public void setUpdateVersion(Integer updateVersion) {
        this.updateVersion = updateVersion;
    }

    /**
     * Returns a timestamp when an history was created.
     * <p>
     * Note that this timestamp can be used to determine when an entity was
     * created. An adequate insert version and a version
     * (EntityObject#getVersion()) of <code>0</code> is the first history of an
     * entity an thus represents it's creation timestamp.
     * </p>
     * 
     * @return a timestamp when an history was created
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * Returns a timestamp when an entity was deleted. Return value can be
     * <code>null</code> if entity still exists.
     * 
     * @return a timestamp when an entity was deleted
     */
    public Date getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(Date deletedOn) {
        this.deletedOn = deletedOn;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(insertVersion)
                    .append(updateVersion)
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof History))
            return false;
        History other = (History) obj;
        return new EqualsBuilder()
                    .append(insertVersion, other.insertVersion)
                    .append(updateVersion, other.updateVersion)
                    .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .append("insertVersion", insertVersion)
                    .append("updateVersion", updateVersion)
                    .append("createdOn", createdOn)
                    .append("deletedOn", deletedOn)
                    .toString();
    }

}
