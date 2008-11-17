package hr.fer.zemris.vhdllab.entities.stub;


/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface ILibraryFileInfoStub extends IResourceStub {

    static final Integer LIBRARY_ID = Integer.valueOf(332211);
    static final Integer LIBRARY_ID_2 = Integer.valueOf(665544);
    
    void setLibraryId(Integer libraryId);

}
