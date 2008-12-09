package hr.fer.zemris.vhdllab.platform.manager.workspace.model;

import hr.fer.zemris.vhdllab.entities.Caseless;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ProjectIdentifier {

    private final Caseless projectName;

    public ProjectIdentifier(Caseless projectName) {
        Validate.notNull(projectName, "ProjectName can't be null");
        this.projectName = projectName;
    }

    public Caseless getProjectName() {
        return projectName;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(projectName)
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ProjectIdentifier))
            return false;
        ProjectIdentifier other = (ProjectIdentifier) obj;
        return new EqualsBuilder()
                    .append(projectName, other.projectName)
                    .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(getClass())
                    .append(projectName)
                    .toString();
    }

}
