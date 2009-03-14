package hr.fer.zemris.vhdllab.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "client_logs", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
public class ClientLog extends FileInfo {

    private static final long serialVersionUID = 2460564318284652078L;

    public ClientLog() {
        super();
    }

    public ClientLog(String userId) {
        setName(constructName(userId));
        setType(null);
    }

    private String constructName(String userId) {
        return "by user '" + userId + "' at " + new Date().toString();
    }

}
