package hr.fer.zemris.vhdllab.entity;

import static org.apache.commons.lang.StringUtils.lowerCase;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

@MappedSuperclass
public class NamedEntity extends BaseEntity {

    private static final long serialVersionUID = -2369439762588805135L;

    @NotEmpty
    @Length(max = 255)
    @Column(updatable = false)
    private String name;

    public NamedEntity() {
        super();
    }

    public NamedEntity(String name) {
        setName(name);
    }

    public NamedEntity(NamedEntity clone) {
        super(clone);
        setName(clone.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(lowerCase(name))
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof NamedEntity))
            return false;
        NamedEntity other = (NamedEntity) obj;
        return new EqualsBuilder()
                    .append(lowerCase(name), lowerCase(other.name))
                    .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .append("name", name)
                    .toString();
    }

}
