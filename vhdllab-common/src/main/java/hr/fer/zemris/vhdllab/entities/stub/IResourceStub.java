package hr.fer.zemris.vhdllab.entities.stub;

import hr.fer.zemris.vhdllab.entities.Resource;

import org.apache.commons.lang.RandomStringUtils;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface IResourceStub extends IEntityObjectStub {

    static final String DATA = "a resource data...";
    static final String DATA_2 = "...another resource data...";
    static final String DATA_TOO_LONG = RandomStringUtils
            .randomAlphabetic(Resource.DATA_LENGTH + 1);

}
