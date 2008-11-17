package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Resource;

/**
 * Used as an entity to test persistence of {@link Resource}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ResourceTable extends Resource {

    private static final long serialVersionUID = 1L;

    public ResourceTable() {
        super();
    }

    public ResourceTable(Caseless name, String data) {
        super(name, data);
    }

}
