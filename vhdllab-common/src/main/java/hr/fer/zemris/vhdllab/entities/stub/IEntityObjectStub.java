package hr.fer.zemris.vhdllab.entities.stub;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.EntityObject;

import org.apache.commons.lang.RandomStringUtils;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface IEntityObjectStub {

    static final Integer ID = Integer.valueOf(123456);
    static final Integer ID_2 = Integer.valueOf(654321);
    
    static final Integer VERSION = Integer.valueOf(123654);
    static final Integer VERSION_2 = Integer.valueOf(654123);
    
    static final Caseless NAME = new Caseless("entity_name");
    static final Caseless NAME_OPPOSITE_CASE = NAME.toUpperCase();
    static final Caseless NAME_2 = new Caseless("new_entity_name");
    static final Caseless NAME_TOO_LONG = new Caseless(RandomStringUtils
            .randomAlphabetic(EntityObject.NAME_LENGTH + 1));
    static final Caseless NAME_NOT_CORRECTLY_FORMATTED = new Caseless(
            "_wrong format.for?entity-name");

    void setId(Integer id);

    void setVersion(Integer version);

    void setName(Caseless name);

}
