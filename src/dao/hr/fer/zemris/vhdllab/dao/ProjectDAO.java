package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.model.Project;

import java.util.List;

public interface ProjectDAO {
	
	Project load(Long id) throws DAOException;
	void save(Project project) throws DAOException;
	List<Project> findByUser(Long userId) throws DAOException;
	
}
