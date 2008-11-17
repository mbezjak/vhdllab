package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.entities.UserFileInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Cache {

	private final Map<FileIdentifier, Integer> identifiers;
	private final Map<Caseless, UserFileInfo> userFileIdentifiers;
	
	private final Map<Integer, FileInfo> files;
	private final Map<Integer, ProjectInfo> projects;

	
	public Cache() {
		identifiers = new HashMap<FileIdentifier, Integer>();
		userFileIdentifiers = new HashMap<Caseless, UserFileInfo>();
		files = new HashMap<Integer, FileInfo>();
		projects = new HashMap<Integer, ProjectInfo>();
	}

	public boolean containsIdentifierFor(Caseless projectName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		return getIdentifierFor(projectName) != null;
	}

	public boolean containsIdentifierFor(Caseless projectName, Caseless fileName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		return getIdentifierFor(projectName, fileName) != null;
	}
	
	public List<Caseless> findFilesForProject(Caseless projectName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		List<Caseless> fileNames = new ArrayList<Caseless>();
		for(Entry<FileIdentifier, Integer> entry : identifiers.entrySet()) {
			FileIdentifier identifier = entry.getKey();
			if(identifier.isFile() && identifier.getProjectName().equals(projectName)) {
				fileNames.add(identifier.getFileName());
			}
		}
		return fileNames;
	}
	
	public List<Caseless> findFilesForProject() {
		List<Caseless> projectNames = new ArrayList<Caseless>();
		for(Entry<FileIdentifier, Integer> entry : identifiers.entrySet()) {
			FileIdentifier identifier = entry.getKey();
			if(identifier.isProject()) {
				projectNames.add(identifier.getProjectName());
			}
		}
		return projectNames;
	}
	
	public UserFileInfo getUserFile(Caseless name) {
		return userFileIdentifiers.get(name);
	}
	
	public FileInfo getFile(Integer fileIdentifier) {
		return files.get(fileIdentifier);
	}

	public ProjectInfo getProject(Integer projectIdentifier) {
	    return projects.get(projectIdentifier);
	}
	
	public void cacheFile(FileInfo file) {
		files.put(file.getId(), file);
	}

	public void cacheProject(ProjectInfo project) {
	    projects.put(project.getId(), project);
	}
	
	public FileIdentifier getFileForIdentifier(FileInfo file) {
	    return getFileForIdentifier(file.getId());
	}
	
	public FileIdentifier getFileForIdentifier(Integer id) {
		if(id == null) {
			throw new NullPointerException("File identifier can not be null.");
		}
		for(Entry<FileIdentifier, Integer> entry : identifiers.entrySet()) {
			FileIdentifier identifier = entry.getKey();
			if(entry.getValue().equals(id) && identifier.isFile()) {
				return identifier;
			}
		}
		return null;
	}
	
	public Caseless getProjectForIdentifier(ProjectInfo project) {
	    return getProjectForIdentifier(project.getId());
	}
	
	public Caseless getProjectForIdentifier(Integer id) {
		if(id == null) {
			throw new NullPointerException("Project identifier can not be null.");
		}
		for(Entry<FileIdentifier, Integer> entry : identifiers.entrySet()) {
			FileIdentifier identifier = entry.getKey();
			if(entry.getValue().equals(id) && identifier.isProject()) {
				return identifier.getProjectName();
			}
		}
		return null;
	}
	
	public Integer getIdentifierFor(Caseless projectName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName);
		return identifiers.get(key);
	}

	public Integer getIdentifierFor(Caseless projectName, Caseless fileName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName, fileName);
		return identifiers.get(key);
	}
	
	public void cacheUserFileItem(Caseless name, UserFileInfo file) {
		if(name == null) {
			throw new NullPointerException("Name can not be null.");
		}
		if(file == null) {
			throw new NullPointerException("User file can not be null.");
		}
		userFileIdentifiers.put(name, file);
	}
	
	public void cacheItem(Caseless projectName, ProjectInfo project) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(project == null) {
			throw new NullPointerException("Project can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName);
		identifiers.put(key, project.getId());
		cacheProject(project);
	}

	public void cacheItem(Caseless projectName, Caseless fileName, FileInfo file) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		if(file == null) {
			throw new NullPointerException("File can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName, fileName);
		identifiers.put(key, file.getId());
		cacheFile(file);
	}

	public void removeUserFileItem(FileType type) {
		if(type == null) {
			throw new NullPointerException("Type can not be null.");
		}
		userFileIdentifiers.remove(type);
	}

	public void removeItem(Caseless projectName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName);
		identifiers.remove(key);
	}

	public void removeItem(Caseless projectName, Caseless fileName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName, fileName);
		identifiers.remove(key);
	}

	private FileIdentifier makeIdentifier(Caseless projectName) {
		FileIdentifier identifier = new FileIdentifier(projectName);
		return identifier;
	}

	private FileIdentifier makeIdentifier(Caseless projectName, Caseless fileName) {
		FileIdentifier identifier = new FileIdentifier(projectName, fileName);
		return identifier;
	}

}