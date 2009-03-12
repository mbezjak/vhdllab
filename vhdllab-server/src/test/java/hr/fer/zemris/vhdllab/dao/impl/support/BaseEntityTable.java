package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.entity.BaseEntity;

import javax.persistence.Entity;

/**
 * Used as an entity to test persistence of {@link BaseEntity}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Entity
public class BaseEntityTable extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public BaseEntityTable() {
        super();
    }

}
