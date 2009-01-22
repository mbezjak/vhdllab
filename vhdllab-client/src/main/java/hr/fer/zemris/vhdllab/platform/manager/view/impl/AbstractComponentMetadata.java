package hr.fer.zemris.vhdllab.platform.manager.view.impl;

import hr.fer.zemris.vhdllab.platform.manager.view.View;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewMetadata;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class AbstractComponentMetadata implements ViewMetadata {

    protected final String code;

    public AbstractComponentMetadata(Class<? extends View> clazz) {
        Validate.notNull(clazz, "View class can't be null");
        this.code = StringUtils.uncapitalize(clazz.getSimpleName());
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public boolean isCloseable() {
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
        if (!(obj instanceof AbstractViewMetadata))
            return false;
        AbstractViewMetadata other = (AbstractViewMetadata) obj;
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
