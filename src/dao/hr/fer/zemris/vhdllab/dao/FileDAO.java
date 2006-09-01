package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.model.File;

public interface FileDAO {

	File load(Long id) throws DAOException;
	void save(File file) throws DAOException;
	
}
