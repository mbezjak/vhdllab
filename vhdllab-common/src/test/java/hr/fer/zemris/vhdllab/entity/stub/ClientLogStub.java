package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.ClientLog;

import org.apache.commons.beanutils.BeanUtils;

public class ClientLogStub extends ClientLog {

    private static final long serialVersionUID = 1L;

    public ClientLogStub() throws Exception {
        BeanUtils.copyProperties(this, new FileInfoStub());
        setType(null);
    }

}
