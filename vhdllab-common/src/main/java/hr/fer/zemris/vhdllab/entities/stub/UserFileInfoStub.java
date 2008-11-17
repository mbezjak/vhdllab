package hr.fer.zemris.vhdllab.entities.stub;

import static hr.fer.zemris.vhdllab.entities.stub.TestsUtil.setField;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.UserFileInfo;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class UserFileInfoStub extends UserFileInfo implements IOwnableStub {

    private static final long serialVersionUID = 1L;

    public UserFileInfoStub() {
        super(USER_ID, NAME, DATA);
        setId(ID);
        setVersion(VERSION);
    }

    public UserFileInfoStub(UserFileInfo file) {
        super(file);
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

    @Override
    public void setUserId(Caseless userId) {
        setField(this, "userId", userId);
    }

}
