package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.entity.History;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Used as an entity to test persistence of {@link History}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Entity
public class HistoryTable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Integer id;
    @Embedded
    private History history;

    public HistoryTable() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
