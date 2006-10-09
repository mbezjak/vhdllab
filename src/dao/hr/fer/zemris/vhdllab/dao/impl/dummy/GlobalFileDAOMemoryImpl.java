package hr.fer.zemris.vhdllab.dao.impl.dummy;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.GlobalFileDAO;
import hr.fer.zemris.vhdllab.model.GlobalFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalFileDAOMemoryImpl implements GlobalFileDAO {

	private long id = 0;

	Map<Long, GlobalFile> files = new HashMap<Long, GlobalFile>();

	public GlobalFile load(Long id) throws DAOException {
		GlobalFile file = files.get(id);
		if(file==null) throw new DAOException("Unable to load global file!");
		return files.get(id);

	}

	public void save(GlobalFile file) throws DAOException {
		if(file.getId()==null) file.setId(Long.valueOf(id++));
		files.put(file.getId(), file);
	}

	public void delete(Long fileID) throws DAOException {
		files.remove(fileID);
	}

	public List<GlobalFile> findByType(String type) throws DAOException {
		Collection<GlobalFile> c = files.values();
		List<GlobalFile> fileList = new ArrayList<GlobalFile>();
		for(GlobalFile f : new ArrayList<GlobalFile>(c)) {
			if(f.getType().equals(type)) fileList.add(f);
		}
		return fileList;
	}
	
	public boolean exists(Long fileId) throws DAOException {
		return files.get(fileId) != null;
	}
}
