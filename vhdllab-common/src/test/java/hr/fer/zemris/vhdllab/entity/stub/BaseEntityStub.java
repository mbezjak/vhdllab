package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.BaseEntity;

public class BaseEntityStub extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public static final Integer ID = 123456;
    public static final Integer VERSION = 654321;

    public BaseEntityStub() {
        setId(ID);
        setVersion(VERSION);
    }

}
