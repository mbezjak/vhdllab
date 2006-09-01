package hr.fer.zemris.vhdllab.dao.impl.dummy;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.model.File;

import java.util.HashMap;
import java.util.Map;

public class FileDAOMemoryImpl implements FileDAO {

	Map<Long, File> files = new HashMap<Long, File>();
	
	public File load(Long id) throws DAOException {
		return files.get(id);
	}

	public void save(File file) throws DAOException {
		files.put(file.getId(), file);
	}
}
