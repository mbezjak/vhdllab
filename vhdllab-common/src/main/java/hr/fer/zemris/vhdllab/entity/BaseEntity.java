package hr.fer.zemris.vhdllab.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;

@MappedSuperclass
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 604912227799417822L;

    @Id
    @GeneratedValue
    @Column(insertable = false, updatable = false)
    private Integer id;
    @Version
    private Integer version;

    public BaseEntity() {
        super();
    }

    public BaseEntity(BaseEntity clone) {
        setId(clone.id);
        setVersion(clone.version);
    }

    public boolean isNew() {
        return id == null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .append("id", id)
                    .append("version", version)
                    .toString();
    }

}
