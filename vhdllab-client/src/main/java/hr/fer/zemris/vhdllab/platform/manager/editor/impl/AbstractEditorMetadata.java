package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorMetadata;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class AbstractEditorMetadata implements EditorMetadata {

    private final Class<? extends Editor> editorClass;
    protected final String code;

    public AbstractEditorMetadata(Class<? extends Editor> editorClass) {
        Validate.notNull(editorClass, "Editor class can't be null");
        this.code = StringUtils.uncapitalize(editorClass.getSimpleName());
        this.editorClass = editorClass;
    }

    @Override
    public Class<? extends Editor> getEditorClass() {
        return editorClass;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(code)
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof AbstractEditorMetadata))
            return false;
        AbstractEditorMetadata other = (AbstractEditorMetadata) obj;
        return new EqualsBuilder()
                    .append(code, other.code)
                    .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .append("viewName", code)
                    .toString();
    }

}
