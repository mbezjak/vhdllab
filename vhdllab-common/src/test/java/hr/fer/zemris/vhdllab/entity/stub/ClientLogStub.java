package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.ClientLog;
import hr.fer.zemris.vhdllab.util.BeanUtil;

public class ClientLogStub extends ClientLog {

    private static final long serialVersionUID = 1L;

    public ClientLogStub() {
        BeanUtil.copyProperties(this, new FileInfoStub());
        setType(null);
    }

}
