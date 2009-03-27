package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.OwnedEntity;

import org.apache.commons.beanutils.BeanUtils;

public class OwnedEntityStub extends OwnedEntity {

    private static final long serialVersionUID = 1L;

    public static final String USER_ID = "user identifier";
    public static final String USER_ID_UPPERCASE = USER_ID.toUpperCase();
    public static final String USER_ID_2 = "another user identifier";

    public OwnedEntityStub() throws Exception {
        BeanUtils.copyProperties(this, new NamedEntityStub());
        setUserId(USER_ID);
    }

}
