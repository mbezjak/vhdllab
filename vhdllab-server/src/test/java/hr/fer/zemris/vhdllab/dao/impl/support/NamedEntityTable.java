package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.entity.NamedEntity;

import javax.persistence.Entity;

/**
 * Used as an entity to test persistence of {@link NamedEntity}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Entity
public class NamedEntityTable extends NamedEntity {

    private static final long serialVersionUID = 1L;

    public NamedEntityTable() {
        super();
    }

}
