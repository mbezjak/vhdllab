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
	Map<FileNameIndexKey, File> nameIndex = new HashMap<FileNameIndexKey, File>(); 

	public synchronized File load(Long id) throws DAOException {
		File file = files.get(id);
		if(file==null) throw new DAOException("Unable to load file!");
		return file;
	}

	public synchronized void save(File file) throws DAOException {
		if(file.getFileType()==null) throw new DAOException("FileType can not be null!");		
		FileNameIndexKey k = null;
		if(file.getId()==null) {
			k = new FileNameIndexKey(file.getFileName().toUpperCase(),file.getProject().getId());
			if(nameIndex.containsKey(k)) {
				throw new DAOException("File "+file.getFileName()+" already exists!");
			}
			file.setId(Long.valueOf(id++));
		}
		Project project = file.getProject();
		if(project!=null) {
			if(project.getFiles()==null) {
				project.setFiles(new TreeSet<File>());
			}
			if(!project.getFiles().contains(file)) {
				project.getFiles().add(file);
			}
		}
		file.getProject().addFile(file);
		files.put(file.getId(), file);
		if(k!=null) {
			nameIndex.put(k, file);
		}
	}

	public synchronized void delete(File file) throws DAOException {
		if(file==null) return;
		FileNameIndexKey k = new FileNameIndexKey(file.getFileName().toUpperCase(),file.getProject().getId());
		nameIndex.remove(k);
		files.remove(file.getId());
	}

	public synchronized boolean exists(Long fileId) throws DAOException {
		return files.get(fileId) != null;
	}

	public synchronized boolean exists(Long projectId, String name) throws DAOException {
		FileNameIndexKey k = new FileNameIndexKey(name.toUpperCase(),projectId);
		return nameIndex.containsKey(k);
	}

	public synchronized File findByName(Long projectId, String name) throws DAOException {
		FileNameIndexKey k = new FileNameIndexKey(name.toUpperCase(),projectId);
		return nameIndex.get(k);
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
