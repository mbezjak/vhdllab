package hr.fer.zemris.vhdllab.dao.impl.dummy;

import static hr.fer.zemris.vhdllab.utilities.ModelUtil.globalFileNamesAreEqual;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.GlobalFileDAO;
import hr.fer.zemris.vhdllab.model.GlobalFile;
import hr.fer.zemris.vhdllab.utilities.ModelUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalFileDAOMemoryImpl implements GlobalFileDAO {

	private long id = 0;

	private Map<Long, GlobalFile> files = new HashMap<Long, GlobalFile>();

	public synchronized GlobalFile load(Long id) throws DAOException {
		if(id == null) {
			throw new DAOException("Global file identifier can not be null.");
		}
		GlobalFile file = files.get(id);
		if(file==null) throw new DAOException("Unable to load global file!");
		return new GlobalFile(file);
	}

	public synchronized void save(GlobalFile file) throws DAOException {
		if(file == null) {
			throw new DAOException("Global file can not be null.");
		}
		if(file.getName() == null) {
			throw new DAOException("Global file name can not be null.");
		}
		if(file.getType() == null) {
			throw new DAOException("Global file type can not be null.");
		}
		if(file.getName().length() > ModelUtil.GLOBAL_FILE_NAME_SIZE) {
			throw new DAOException("Global file name too long.");
		}
		if(file.getType().length() > ModelUtil.GLOBAL_FILE_TYPE_SIZE) {
			throw new DAOException("Global file type too long.");
		}
		if(file.getContent() != null && file.getContent().length() > ModelUtil.GLOBAL_FILE_CONTENT_SIZE) {
			throw new DAOException("Global file content too long.");
		}
		for(GlobalFile f : files.values()) {
			if(globalFileNamesAreEqual(f.getName(), file.getName()) &&
					!f.getId().equals(file.getId())) {
				throw new DAOException("Global file name must be unique.");
			}
		}
		if(file.getId()==null) file.setId(Long.valueOf(id++));
		files.put(file.getId(), new GlobalFile(file));
	}

	public synchronized void delete(GlobalFile file) throws DAOException {
		if(file == null) {
			throw new DAOException("Global file can not be null.");
		}
		GlobalFile f = files.remove(file.getId());
		if(f == null) {
			throw new DAOException("Global fiel does not exist.");
		}
	}

	public synchronized List<GlobalFile> findByType(String type) throws DAOException {
		if(type == null) {
			throw new DAOException("Global file type can not be null.");
		}
		List<GlobalFile> fileList = new ArrayList<GlobalFile>();
		for(GlobalFile f : files.values()) {
			if(f.getType().equals(type)) {
				fileList.add(new GlobalFile(f));
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
	
	public synchronized boolean exists(String name) throws DAOException {
		if(name == null) {
			throw new DAOException("Global file name can not be null.");
		}
		for(GlobalFile f : files.values()) {
			if(globalFileNamesAreEqual(f.getName(), name)) {
				return true;
			}
		}
		return false;
	}
}
