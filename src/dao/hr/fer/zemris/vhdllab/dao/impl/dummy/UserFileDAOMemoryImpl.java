package hr.fer.zemris.vhdllab.dao.impl.dummy;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.model.UserFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFileDAOMemoryImpl implements UserFileDAO {

	private long id = 0;

	Map<Long, UserFile> files = new HashMap<Long, UserFile>();

	public synchronized UserFile load(Long id) throws DAOException {
		UserFile file = files.get(id);
		if(file==null) throw new DAOException("Unable to load user file!");
		return file;
	}

	public synchronized void save(UserFile file) throws DAOException {
		if(file.getId()==null) file.setId(Long.valueOf(id++));
		files.put(file.getId(), file);
	}

	public synchronized void delete(UserFile file) throws DAOException {
		files.remove(file.getId());
	}

	public synchronized List<UserFile> findByUser(String userID) throws DAOException {
		List<UserFile> fileList = new ArrayList<UserFile>();
		for(UserFile f : files.values()) {
			if(f.getOwnerID().equals(userID)) {
				fileList.add(f);
			}
		}
		return fileList;
	}
	
	public synchronized boolean exists(Long fileId) throws DAOException {
		return files.containsKey(fileId);
	}

	public synchronized boolean exists(String ownerId, String name) throws DAOException {
		for(UserFile f : files.values()) {
			if(f.getName().equalsIgnoreCase(name) &&
					f.getOwnerID().equals(ownerId)) {
				return true;
			}
		}
		return false;
	}
}
