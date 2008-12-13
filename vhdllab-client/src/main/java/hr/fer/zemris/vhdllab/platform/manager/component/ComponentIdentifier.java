package hr.fer.zemris.vhdllab.platform.manager.component;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class ComponentIdentifier {

    private final String componentName;

    protected ComponentIdentifier(Class<? extends IComponent> componentClass) {
        this(StringUtils.uncapitalize(componentClass.getSimpleName()));
    }

    protected ComponentIdentifier(String componentName) {
        Validate.notNull(componentName, "Component name can't be null");
        this.componentName = componentName;
    }

    public String getComponentName() {
        return componentName;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(componentName)
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ComponentIdentifier))
            return false;
        ComponentIdentifier other = (ComponentIdentifier) obj;
        return new EqualsBuilder()
                    .append(componentName, other.componentName)
                    .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .append("componentName", componentName)
                    .toString();
    }

}
