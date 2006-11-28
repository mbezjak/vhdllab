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

	private long id = 0;

	Map<Long, Project> projects = new HashMap<Long, Project>();

	public Project load(Long id) throws DAOException {
		Project project = projects.get(id);
		if(project==null) throw new DAOException("Unable to load project!");
		return projects.get(id);

	}

	public void save(Project project) throws DAOException {
		if(project.getId()==null) project.setId(Long.valueOf(id++));
		projects.put(project.getId(), project);
	}

	public void delete(Long projectID) throws DAOException {
		projects.remove(projectID);
	}

	public List<Project> findByUser(String userId) throws DAOException {
		Collection<Project> c = projects.values();
		List<Project> projectList = new ArrayList<Project>();
		for(Project p : new ArrayList<Project>(c)) {
			if(p.getOwnerId().equals(userId)) projectList.add(p);
		}
		return projectList;
	}
	
	public boolean exists(Long projectId) throws DAOException {
		return projects.get(projectId) != null;
	}

}
