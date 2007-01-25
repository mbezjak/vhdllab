package hr.fer.zemris.vhdllab.dao.impl.dummy;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.model.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectDAOMemoryImpl implements ProjectDAO {

	private long id = 0;

	Map<Long, Project> projects = new HashMap<Long, Project>();

	public synchronized Project load(Long id) throws DAOException {
		Project project = projects.get(id);
		if(project==null) throw new DAOException("Unable to load project!");
		return project;
	}

	public synchronized void save(Project project) throws DAOException {
		if(project.getId()==null) project.setId(Long.valueOf(id++));
		projects.put(project.getId(), project);
	}

	public synchronized void delete(Project project) throws DAOException {
		projects.remove(project.getId());
	}

	public synchronized List<Project> findByUser(String userId) throws DAOException {
		List<Project> projectList = new ArrayList<Project>();
		for(Project p : projects.values()) {
			if(p.getOwnerId().equals(userId)) {
				projectList.add(p);
			}
		}
		return projectList;
	}
	
	public synchronized boolean exists(Long projectId) throws DAOException {
		return projects.containsKey(projectId);
	}
	
	public synchronized boolean exists(String ownerId, String projectName) throws DAOException {
		for(Project p : projects.values()) {
			if(p.getProjectName().equals(projectName)) {
				return true;
			}
		}
		return false;
	}

}