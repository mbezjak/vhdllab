package hr.fer.zemris.vhdllab.entities.stub;

import static hr.fer.zemris.vhdllab.entities.stub.TestsUtil.setField;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.EntityObject;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class EntityObjectStub extends EntityObject implements IEntityObjectStub {

    private static final long serialVersionUID = 1L;

    public EntityObjectStub() {
        super(NAME);
        setId(ID);
        setVersion(VERSION);
    }

    public EntityObjectStub(EntityObject entity) {
        super(entity, true);
    }

    @Override
    public void setId(Integer id) {
        setField(this, "id", id);
    }

    @Override
    public void setName(Caseless name) {
        setField(this, "name", name);
    }

    @Override
    public void setVersion(Integer version) {
        setField(this, "version", version);
    }

}
