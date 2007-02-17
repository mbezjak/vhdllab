package hr.fer.zemris.vhdllab.dao.impl.dummy;

import static hr.fer.zemris.vhdllab.model.ModelUtil.userFileNamesAreEqual;
import static hr.fer.zemris.vhdllab.model.ModelUtil.userIdAreEqual;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.model.ModelUtil;
import hr.fer.zemris.vhdllab.model.UserFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFileDAOMemoryImpl implements UserFileDAO {

	private long id = 0;

	private Map<Long, UserFile> files = new HashMap<Long, UserFile>();

	public synchronized UserFile load(Long id) throws DAOException {
		if(id == null) {
			throw new DAOException("User file identifier can not be null.");
		}
		UserFile file = files.get(id);
		if(file==null) throw new DAOException("Unable to load user file!");
		return new UserFile(file);
	}

	public synchronized void save(UserFile file) throws DAOException {
		if(file == null) {
			throw new DAOException("User file can not be null.");
		}
		if(file.getOwnerID() == null) {
			throw new DAOException("User identifier can not be null.");
		}
		if(file.getName() == null) {
			throw new DAOException("User file name can not be null.");
		}
		if(file.getType() == null) {
			throw new DAOException("User file type can not be null.");
		}
		if(file.getOwnerID().length() > ModelUtil.USER_ID_SIZE) {
			throw new DAOException("User identifier can not be null.");
		}
		if(file.getName().length() > ModelUtil.USER_FILE_NAME_SIZE) {
			throw new DAOException("User file name too long.");
		}
		if(file.getType().length() > ModelUtil.USER_FILE_TYPE_SIZE) {
			throw new DAOException("User file type too long.");
		}
		if(file.getContent() != null && file.getContent().length() > ModelUtil.USER_FILE_CONTENT_SIZE) {
			throw new DAOException("User file content too long.");
		}
		if(file.getId()==null) file.setId(Long.valueOf(id++));
		files.put(file.getId(), new UserFile(file));
	}

	public synchronized void delete(UserFile file) throws DAOException {
		if(file == null) {
			throw new DAOException("User file can not be null.");
		}
		UserFile f = files.remove(file.getId());
		if(f == null) {
			throw new DAOException("User file does not exist.");
		}
	}

	public synchronized List<UserFile> findByUser(String userID) throws DAOException {
		if(userID == null) {
			throw new DAOException("User identifier can not be null.");
		}
		List<UserFile> fileList = new ArrayList<UserFile>();
		for(UserFile f : files.values()) {
			if(userIdAreEqual(f.getOwnerID(), userID)) {
				fileList.add(new UserFile(f));
			}
		}
		return fileList;
	}
	
	public synchronized boolean exists(Long fileId) throws DAOException {
		if(fileId == null) {
			throw new DAOException("Global file identifier can not be null.");
		}
		return files.containsKey(fileId);
	}

	public synchronized boolean exists(String ownerId, String name) throws DAOException {
		if(ownerId == null) {
			throw new DAOException("Owner identifier can not be null.");
		}
		if(name == null) {
			throw new DAOException("User file name can not be null.");
		}
		for(UserFile f : files.values()) {
			if(userFileNamesAreEqual(f.getName(), name) &&
					userIdAreEqual(f.getOwnerID(), ownerId)) {
				return true;
			}
		}
		return false;
	}
}
