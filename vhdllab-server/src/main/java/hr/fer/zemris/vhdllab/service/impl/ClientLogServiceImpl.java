package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.ClientLogDao;
import hr.fer.zemris.vhdllab.entity.ClientLog;
import hr.fer.zemris.vhdllab.service.ClientLogService;
import hr.fer.zemris.vhdllab.service.util.SecurityUtils;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;

public class ClientLogServiceImpl implements ClientLogService {

    @Autowired
    private ClientLogDao dao;

    @Override
    public void save(String data) {
        Validate.notNull(data, "Data can't be null");
        ClientLog log = new ClientLog(SecurityUtils.getUser(), data);
        dao.persist(log);
    }

}
