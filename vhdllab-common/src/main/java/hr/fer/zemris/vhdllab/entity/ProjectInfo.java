package hr.fer.zemris.vhdllab.entity;

import static org.apache.commons.lang.StringUtils.lowerCase;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@MappedSuperclass
public class ProjectInfo extends NamedEntity {

    private static final long serialVersionUID = -3795038948065257739L;

    @NotNull
    @Length(max = 255)
    @Column(name = "user_id", updatable = false)
    private String userId;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(updatable = false)
    private ProjectType type;

    public ProjectInfo() {
        this(null, null);
    }

    public ProjectInfo(String userId) {
        this(userId, null);
    }

    public ProjectInfo(String userId, String name) {
        super(name);
        setUserId(userId);
        setType(ProjectType.USER);
    }

    public ProjectInfo(ProjectInfo clone) {
        super(clone);
        setUserId(clone.userId);
        setType(clone.type);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ProjectType getType() {
        return type;
    }

    public void setType(ProjectType type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(lowerCase(userId))
                    .append(type)
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ProjectInfo))
            return false;
        ProjectInfo other = (ProjectInfo) obj;
        return new EqualsBuilder()
                    .appendSuper(super.equals(obj))
                    .append(lowerCase(userId), lowerCase(other.userId))
                    .append(type, other.type)
                    .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .append("userId", userId)
                    .append("type", type)
                    .toString();
    }

}
