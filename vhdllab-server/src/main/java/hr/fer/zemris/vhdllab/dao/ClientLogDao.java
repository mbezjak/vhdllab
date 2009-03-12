package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.entity.ClientLog;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ClientLogDao extends EntityDao<ClientLog> {
}
