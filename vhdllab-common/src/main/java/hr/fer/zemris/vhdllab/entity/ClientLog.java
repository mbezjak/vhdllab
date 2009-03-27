package hr.fer.zemris.vhdllab.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "client_logs", uniqueConstraints = { @UniqueConstraint(columnNames = {
        "user_id", "created_on" }) })
public class ClientLog extends OwnedEntity {

    private static final long serialVersionUID = 2460564318284652078L;

    @NotNull
    @Length(max = 16000000) // ~ 16MB
    private String data;
    @NotNull
    @Column(name = "created_on", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    public ClientLog() {
        super();
    }

    public ClientLog(String data) {
        this(null, data);
    }

    public ClientLog(String userId, String data) {
        super(userId, null);
        setData(data);
        Date timestamp = new Date();
        setCreatedOn(timestamp);
        setName(timestamp.toString());
    }

    public ClientLog(ClientLog clone) {
        super(clone);
        setData(clone.data);
        setCreatedOn(clone.createdOn);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .append("createdOn", createdOn)
                    .append("dataLength", StringUtils.length(data))
                    .toString();
    }

}
