package hr.fer.zemris.vhdllab.platform.manager.workspace.model;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public final class FileIdentifier extends ProjectIdentifier {

    private final String fileName;

    public FileIdentifier(String projectName, String fileName) {
        super(projectName);
        Validate.notNull(fileName, "File name can't be null");
        this.fileName = fileName;
    }

    public Caseless getFileName() {
        return fileName;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .appendSuper(super.hashCode())
                    .append(fileName.toLowerCase())
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof FileIdentifier))
            return false;
        FileIdentifier other = (FileIdentifier) obj;
        return new EqualsBuilder()
                    .appendSuper(super.equals(obj))
                    .append(fileName.toLowerCase(), other.fileName.toLowerCase())
                    .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .append("fileName", fileName)
                    .toString();
    }

}
