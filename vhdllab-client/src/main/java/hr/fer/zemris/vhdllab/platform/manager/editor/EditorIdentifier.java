package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewIdentifier;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class EditorIdentifier extends ViewIdentifier {

    private final FileInfo instanceModifier;

    public EditorIdentifier(EditorMetadata metadata) {
        this(metadata, null);
    }

    public EditorIdentifier(EditorMetadata metadata, FileInfo instanceModifier) {
        super(metadata);
        this.instanceModifier = instanceModifier;
    }

    public FileInfo getInstanceModifier() {
        return instanceModifier;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .appendSuper(super.hashCode())
                    .append(instanceModifier)
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof EditorIdentifier))
            return false;
        EditorIdentifier other = (EditorIdentifier) obj;
        return new EqualsBuilder()
                    .appendSuper(super.equals(obj))
                    .append(instanceModifier, other.instanceModifier)
                    .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .append("instanceModifier", instanceModifier)
                    .toString();
    }

}
