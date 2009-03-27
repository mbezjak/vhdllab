package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.entity.OwnedEntity;

import javax.persistence.Entity;

/**
 * Used as an entity to test persistence of {@link OwnedEntity}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Entity
public class OwnedEntityTable extends OwnedEntity {

    private static final long serialVersionUID = 1L;

    public OwnedEntityTable() {
        super();
    }

    public OwnedEntityTable(String userId, String name) {
        super(userId, name);
    }

}
