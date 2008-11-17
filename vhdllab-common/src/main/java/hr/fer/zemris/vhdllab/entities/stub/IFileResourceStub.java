package hr.fer.zemris.vhdllab.entities.stub;

import hr.fer.zemris.vhdllab.entities.FileType;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface IFileResourceStub extends IResourceStub {

    static final FileType TYPE = FileType.SOURCE;
    static final FileType TYPE_2 = FileType.AUTOMATON;
    
    void setType(FileType type);

}
