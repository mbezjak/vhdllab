package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.model.GlobalFile;

import java.util.List;

public interface GlobalFileDAO {
	
	GlobalFile load(Long id) throws DAOException;
	void save(GlobalFile file) throws DAOException;
	List<GlobalFile> findByType(String type) throws DAOException;
	
}
