package hr.fer.zemris.vhdllab.entity;

import hr.fer.zemris.vhdllab.validation.FileTypeNullabilityConstraint;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@MappedSuperclass
@FileTypeNullabilityConstraint
public class FileInfo extends NamedEntity {

    private static final long serialVersionUID = -7202306767258225281L;

    @Enumerated(EnumType.STRING)
    @Column(updatable = false)
    private FileType type;
    @NotNull
    @Length(max = 16000000) // ~ 16MB
    private String data;

    public FileInfo() {
        super();
    }

    public FileInfo(String name, FileType type, String data) {
        super(name);
        setType(type);
        setData(data);
    }

    public FileInfo(FileInfo clone) {
        super(clone);
        setType(clone.type);
        setData(clone.data);
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .append("type", type)
                    .append("dataLength", StringUtils.length(data))
                    .toString();
    }

}
