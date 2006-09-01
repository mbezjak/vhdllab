package hr.fer.zemris.vhdllab.dao.impl.dummy;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.model.Project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectDAOMemoryImpl implements ProjectDAO {

	Map<Long, Project> projects = new HashMap<Long, Project>();
	
	public List<Project> findByUser(Long userId) throws DAOException {
		Collection<Project> c = projects.values();
		List<Project> p = new ArrayList<Project>(c);
		return p;
	}

	public Project load(Long id) throws DAOException {
		return projects.get(id);
	}

	public void save(Project project) throws DAOException {
		projects.put(project.getId(), project);
	}

}
