package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.EntityObject;

/**
 * Used as an entity to test persistence of {@link EntityObject}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class EntityObjectTable extends EntityObject {

    private static final long serialVersionUID = 1L;

    public EntityObjectTable() {
        super();
    }

    public EntityObjectTable(Caseless name) {
        super(name);
    }

}
