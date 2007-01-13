package hr.fer.zemris.vhdllab.dao.impl.dummy;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.model.UserFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFileDAOMemoryImpl implements UserFileDAO {

	private long id = 0;

	Map<Long, UserFile> files = new HashMap<Long, UserFile>();

	public UserFile load(Long id) throws DAOException {
		UserFile file = files.get(id);
		if(file==null) throw new DAOException("Unable to load user file!");
		return files.get(id);

	}

	public void save(UserFile file) throws DAOException {
		if(file.getId()==null) file.setId(Long.valueOf(id++));
		files.put(file.getId(), file);
	}

	public void delete(UserFile file) throws DAOException {
		files.remove(file.getId());
	}

	public List<UserFile> findByUser(String userID) throws DAOException {
		Collection<UserFile> c = files.values();
		List<UserFile> fileList = new ArrayList<UserFile>();
		for(UserFile f : new ArrayList<UserFile>(c)) {
			if(f.getOwnerID().equals(userID)) fileList.add(f);
		}
		return fileList;
	}
	
	public boolean exists(Long fileId) throws DAOException {
		return files.get(fileId) != null;
	}
}
