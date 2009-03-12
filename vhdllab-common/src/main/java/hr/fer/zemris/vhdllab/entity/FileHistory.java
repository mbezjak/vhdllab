package hr.fer.zemris.vhdllab.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "file_history", uniqueConstraints = { @UniqueConstraint(columnNames = {
        "name", "project_id", "insert_version", "update_version" }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FileHistory extends FileInfo {

    private static final long serialVersionUID = -5509272848576278063L;

    @NotNull
    @Column(name = "project_id", updatable = false)
    private Integer projectId;
    @NotNull
    @Embedded
    private History history;

    public FileHistory() {
        super();
    }

    public FileHistory(File file, History history) {
        this(file, file.getProject().getId(), history);
    }

    public FileHistory(FileInfo file, Integer projectId, History history) {
        super(file);
        setProjectId(projectId);
        setHistory(history);
    }

    public FileHistory(FileHistory file) {
        this(file, file.getProjectId(), file.getHistory());
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .appendSuper(super.hashCode())
                    .append(projectId)
                    .append(history)
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof FileHistory))
            return false;
        FileHistory other = (FileHistory) obj;
        return new EqualsBuilder()
                    .appendSuper(super.equals(obj))
                    .append(projectId, other.projectId)
                    .append(history, other.history)
                    .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .append("projectId", projectId)
                    .append("history", history)
                    .toString();
    }

}
