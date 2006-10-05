package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.model.UserFile;

import java.util.List;

public interface UserFileDAO {
	
	UserFile load(Long ownerID) throws DAOException;
	void save(UserFile file) throws DAOException;
	List<UserFile> findByType(String type) throws DAOException;
	
}
