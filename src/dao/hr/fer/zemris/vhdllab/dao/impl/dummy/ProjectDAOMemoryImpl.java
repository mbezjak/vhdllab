package hr.fer.zemris.vhdllab.dao.impl.dummy;

import static hr.fer.zemris.vhdllab.model.ModelUtil.projectNamesAreEqual;
import static hr.fer.zemris.vhdllab.model.ModelUtil.userIdAreEqual;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.ModelUtil;
import hr.fer.zemris.vhdllab.model.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ProjectDAOMemoryImpl implements ProjectDAO {

	private long id = 0;
	private Map<Long, Project> projects = new HashMap<Long, Project>();
	
	private FileDAO fileDAO;
	
	public ProjectDAOMemoryImpl(FileDAO fileDAO) {
		this.fileDAO = fileDAO;
	}

	public synchronized Project load(Long id) throws DAOException {
		if(id == null) {
			throw new DAOException("Project identifier can not be null.");
		}
		Project project = projects.get(id);
		if(project==null) throw new DAOException("Unable to load project!");
		return new Project(project);
	}

	public synchronized void save(Project project) throws DAOException {
		if(project == null) {
			throw new DAOException("Project can not be null.");
		}
		if(project.getOwnerId() == null) {
			throw new DAOException("Project must belong to a user.");
		}
		if(project.getOwnerId().length() > ModelUtil.USER_ID_SIZE) {
			throw new DAOException("User identifier too long.");
		}
		if(project.getProjectName() == null) {
			throw new DAOException("Project must contain a project name.");
		}
		if(project.getProjectName().length() > ModelUtil.PROJECT_NAME_SIZE) {
			throw new DAOException("Project name too long.");
		}
		if(project.getId()==null) {
			project.setId(Long.valueOf(id++));
		}
		if(project.getFiles() == null) {
			project.setFiles(new TreeSet<File>());
		}
		projects.put(project.getId(), new Project(project));
	}

	public synchronized void delete(Project project) throws DAOException {
		if(project == null) {
			throw new DAOException("Project can not be null.");
		}
		Set<File> files = project.getFiles();
		Iterator<File> it = files.iterator();
		while(it.hasNext()) {
			File f = it.next();
			it.remove();
			fileDAO.delete(f);
		}
		Project p = projects.remove(project.getId());
		if(p == null) {
			throw new DAOException("Project doesnt exist!");
		}
	}

	public synchronized List<Project> findByUser(String userId) throws DAOException {
		if(userId == null) {
			throw new DAOException("User identifier can not be null.");
		}
		List<Project> projectList = new ArrayList<Project>();
		for(Project p : projects.values()) {
			if(ModelUtil.userIdAreEqual(p.getOwnerId(), userId)) {
				projectList.add(new Project(p));
			}
		}
		return projectList;
	}
	
	public synchronized boolean exists(Long projectId) throws DAOException {
		if(projectId == null) {
			throw new DAOException("Project identifier can not be null.");
		}
		return projects.containsKey(projectId);
	}
	
	public synchronized boolean exists(String ownerId, String projectName) throws DAOException {
		if(ownerId == null) {
			throw new DAOException("Owner identifier can not be null.");
		}
		if(projectName == null) {
			throw new DAOException("Project name can not be null.");
		}
		for(Project p : projects.values()) {
			if(projectNamesAreEqual(p.getProjectName(), projectName) &&
					userIdAreEqual(p.getOwnerId(), ownerId)) {
				return true;
			}
		}
		return false;
	}

}