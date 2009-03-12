package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.NamedEntity;
import hr.fer.zemris.vhdllab.util.BeanUtils;

public class NamedEntityStub extends NamedEntity {

    private static final long serialVersionUID = 1L;

    public static final String NAME = "entity name";
    public static final String NAME_UPPERCASE = NAME.toUpperCase();
    public static final String NAME_DIFFERENT = "another entity name";

    public NamedEntityStub() {
        BeanUtils.copyProperties(this, new BaseEntityStub());
        setName(NAME);
    }

}
