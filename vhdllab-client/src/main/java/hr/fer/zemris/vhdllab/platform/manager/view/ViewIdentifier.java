package hr.fer.zemris.vhdllab.platform.manager.view;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ViewIdentifier {

    private final ViewMetadata metadata;

    public ViewIdentifier(ViewMetadata metadata) {
        Validate.notNull(metadata, "View metadata can't be null");
        this.metadata = metadata;
    }

    public ViewMetadata getMetadata() {
        return metadata;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(metadata)
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ViewIdentifier))
            return false;
        ViewIdentifier other = (ViewIdentifier) obj;
        return new EqualsBuilder()
                    .append(metadata, other.metadata)
                    .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .append("metadata", metadata)
                    .toString();
    }

}
