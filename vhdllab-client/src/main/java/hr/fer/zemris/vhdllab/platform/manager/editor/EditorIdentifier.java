package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentIdentifier;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class EditorIdentifier extends ComponentIdentifier {

    private final FileInfo instanceModifier;

    public EditorIdentifier(Class<? extends Editor> editorClass) {
        this(editorClass, null);
    }

    public EditorIdentifier(Class<? extends Editor> editorClass,
            FileInfo instanceModifier) {
        super(editorClass);
        this.instanceModifier = instanceModifier;
    }

    public EditorIdentifier(String componentName, FileInfo instanceModifier) {
        super(componentName);
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
