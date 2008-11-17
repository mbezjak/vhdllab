package hr.fer.zemris.vhdllab.entities.stub;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.UserFileInfo;

import org.apache.commons.lang.RandomStringUtils;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface IOwnableStub extends IResourceStub {

    static final Caseless USER_ID = new Caseless("user.identifier");
    static final Caseless USER_ID_OPPOSITE_CASE = USER_ID.toUpperCase();
    static final Caseless USER_ID_2 = new Caseless("new.user.identifier");
    static final Caseless USER_ID_TOO_LONG = new Caseless(RandomStringUtils
            .randomAlphabetic(UserFileInfo.USER_ID_LENGTH + 1));

    void setUserId(Caseless userId);

}
