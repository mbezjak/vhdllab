package hr.fer.zemris.vhdllab.dao.impl.dummy;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class FileDAOMemoryImpl implements FileDAO {

	private long id = 0;

	Map<Long, File> files = new HashMap<Long, File>();

	public File load(Long id) throws DAOException {
		File file = files.get(id);
		if(file==null) throw new DAOException("Unable to load file!");
		return file;
	}

	public void save(File file) throws DAOException {
		if(file.getFileType()==null) throw new DAOException("FileType can not be null!");
		if(file.getId()==null) file.setId(Long.valueOf(id++));
		Project project = file.getProject();
		if(project!=null) {
			if(project.getFiles()==null) {
				project.setFiles(new TreeSet<File>());
			}
			if(!project.getFiles().contains(file)) {
				project.getFiles().add(file);
			}
		}
		files.put(file.getId(), file);
	}

	public void delete(Long fileID) throws DAOException {
		files.remove(fileID);
	}

	public boolean exists(Long fileId) throws DAOException {
		return files.get(fileId) != null;
	}

	public boolean exists(Long projectId, String name) throws DAOException {
		Collection<File> c = files.values();
		List<File> fileList = new ArrayList<File>(c);
		for(File f : fileList) {
			if(!f.getFileName().equals(name)) continue;
			Project p = f.getProject();
			if(p==null) {
				if(projectId==null) return true;
				continue;
			}
			if(p.getId().equals(projectId)) return true;
		}
		return false;
	}

	public File findByName(Long projectId, String name) throws DAOException {
		Collection<File> c = files.values();
		List<File> fileList = new ArrayList<File>(c);
		for(File f : fileList) {
			if(!f.getFileName().equals(name)) continue;
			Project p = f.getProject();
			if(p==null) {
				if(projectId==null) return f;
				continue;
			}
			if(p.getId().equals(projectId)) return f;
		}
		return null;
	}
}
