package hr.fer.zemris.vhdllab.entities.stub;

import static hr.fer.zemris.vhdllab.entities.stub.TestsUtil.setField;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Resource;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ResourceStub extends Resource implements IResourceStub {
    
    private static final long serialVersionUID = 1L;

    public ResourceStub() {
        super(NAME, DATA);
        setId(ID);
        setVersion(VERSION);
    }
    
    public ResourceStub(Resource resource) {
        super(resource);
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
