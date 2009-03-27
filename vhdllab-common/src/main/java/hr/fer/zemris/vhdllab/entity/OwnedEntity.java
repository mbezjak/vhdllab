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
public class OwnedEntity extends NamedEntity {

    private static final long serialVersionUID = 1276623438439954593L;

    @NotEmpty
    @Length(max = 255)
    @Column(name = "user_id", updatable = false)
    private String userId;

    public OwnedEntity() {
        super();
    }

    public OwnedEntity(String name) {
        this(null, name);
    }

    public OwnedEntity(String userId, String name) {
        super(name);
        setUserId(userId);
    }

    public OwnedEntity(OwnedEntity clone) {
        super(clone);
        setUserId(clone.userId);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .appendSuper(super.hashCode())
                    .append(lowerCase(userId))
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof OwnedEntity))
            return false;
        OwnedEntity other = (OwnedEntity) obj;
        return new EqualsBuilder()
                    .appendSuper(super.equals(obj))
                    .append(lowerCase(userId), lowerCase(other.userId))
                    .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .append("userId", userId)
                    .toString();
    }

}
