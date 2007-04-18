package hr.fer.zemris.vhdllab.dao.impl.dummy;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.utilities.ModelUtil;

import java.util.HashMap;
import java.util.Map;

public class FileDAOMemoryImpl implements FileDAO {

	private long id = 0;

	private Map<Long, File> files = new HashMap<Long, File>();
	private Map<FileNameIndexKey, File> nameIndex = new HashMap<FileNameIndexKey, File>(); 

	public synchronized File load(Long id) throws DAOException {
		if(id == null) throw new DAOException("File identifier can not be null.");
		File file = files.get(id);
		if(file==null) throw new DAOException("Unable to load file!");
		return new File(file);
	}

	public synchronized void save(File file) throws DAOException {
		if(file == null) throw new DAOException("File can not be null.");
		if(file.getFileType()==null) throw new DAOException("File type can not be null!");
		if(file.getFileName()==null) throw new DAOException("File name can not be null!");
		if(file.getProject() == null) throw new DAOException("File must have project as a parent.");
		if(file.getContent()!=null && file.getContent().length() > ModelUtil.FILE_CONTENT_SIZE) throw new DAOException("File content too long");
		if(file.getFileName().length() > ModelUtil.FILE_NAME_SIZE) throw new DAOException("File name too long");
		if(file.getFileType().length() > ModelUtil.FILE_TYPE_SIZE) throw new DAOException("File type too long");
		FileNameIndexKey k = null;
		if(file.getId()==null) {
			k = new FileNameIndexKey(file.getFileName().toUpperCase(),file.getProject().getId());
			if(nameIndex.containsKey(k)) {
				throw new DAOException("File "+file.getFileName()+" already exists!");
			}
			file.setId(Long.valueOf(id++));
		}
		file.getProject().addFile(file);
		File f = new File(file);
		files.put(file.getId(), f);
		if(k!=null) {
			nameIndex.put(k, f);
		}
	}

	public synchronized void delete(File file) throws DAOException {
		if(file==null) throw new DAOException("File can not be null.");
		if(!files.containsKey(file.getId())) throw new DAOException("Can not delete unexisting file.");
		FileNameIndexKey k = new FileNameIndexKey(file.getFileName().toUpperCase(),file.getProject().getId());
		nameIndex.remove(k);
		file.getProject().removeFile(file);
		files.remove(file.getId());
	}

	public synchronized boolean exists(Long fileId) throws DAOException {
		if(fileId == null) throw new DAOException("File identifier can not be null.");
		return files.containsKey(fileId);
	}

	public synchronized boolean exists(Long projectId, String name) throws DAOException {
		if(projectId == null) throw new DAOException("Project identifier can not be null."); 
		if(name == null) throw new DAOException("File name can not be null."); 
		FileNameIndexKey k = new FileNameIndexKey(name.toUpperCase(),projectId);
		return nameIndex.containsKey(k);
	}

	public synchronized File findByName(Long projectId, String name) throws DAOException {
		if(projectId == null) throw new DAOException("Project identifier can not be null."); 
		if(name == null) throw new DAOException("File name can not be null."); 
		FileNameIndexKey k = new FileNameIndexKey(name.toUpperCase(),projectId);
		File f = nameIndex.get(k);
		if(f == null) return null;
		else return new File(nameIndex.get(k));
	}
	
	private static class FileNameIndexKey {
		private String fileName;
		private Long projectId;
		
		public FileNameIndexKey(String name, Long id) {
			super();
			fileName = name;
			projectId = id;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj == null) return false;
			if(!(obj instanceof FileNameIndexKey)) return false;
			FileNameIndexKey other = (FileNameIndexKey)obj;
			return this.fileName.equals(other.fileName) && this.projectId.equals(other.projectId);
		}
		
		@Override
		public int hashCode() {
			return fileName.hashCode() ^ projectId.hashCode();
		}
	}
	
}
