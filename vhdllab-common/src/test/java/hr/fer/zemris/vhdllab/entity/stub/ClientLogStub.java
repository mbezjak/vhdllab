package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.ClientLog;
import hr.fer.zemris.vhdllab.util.BeanUtils;

public class ClientLogStub extends ClientLog {

    private static final long serialVersionUID = 1L;

    public ClientLogStub() {
        BeanUtils.copyProperties(this, new FileInfoStub());
    }

}
