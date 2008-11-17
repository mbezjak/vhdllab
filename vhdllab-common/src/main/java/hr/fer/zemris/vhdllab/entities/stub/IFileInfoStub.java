package hr.fer.zemris.vhdllab.entities.stub;


/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface IFileInfoStub extends IFileResourceStub {

    static final Integer PROJECT_ID = Integer.valueOf(112233);
    static final Integer PROJECT_ID_2 = Integer.valueOf(445566);
    
    void setProjectId(Integer projectId);

}
