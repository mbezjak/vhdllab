package hr.fer.zemris.vhdllab.dao.impl.dummy;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class FileDAOMemoryImpl implements FileDAO {

	private long id = 0;

	Map<Long, File> files = new HashMap<Long, File>();

	public File load(Long id) throws DAOException {
		File file = files.get(id);
		if(file==null) throw new DAOException("Unable to load file!");
		return files.get(id);
	}

	public void save(File file) throws DAOException {
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
}
