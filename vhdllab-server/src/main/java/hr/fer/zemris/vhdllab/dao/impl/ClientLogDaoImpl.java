package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.ClientLogDao;
import hr.fer.zemris.vhdllab.entity.ClientLog;

public class ClientLogDaoImpl extends AbstractEntityDao<ClientLog> implements
        ClientLogDao {

    public ClientLogDaoImpl() {
        super(ClientLog.class);
    }

}
