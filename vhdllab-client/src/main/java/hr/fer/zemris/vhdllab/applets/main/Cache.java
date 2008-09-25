package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Cache {

	private Map<FileIdentifier, Integer> identifiers;
	private Map<Caseless, Integer> userFileIdentifiers;
	
	private Map<Integer, FileType> fileTypes;

	
	public Cache() {
		identifiers = new HashMap<FileIdentifier, Integer>();
		userFileIdentifiers = new HashMap<Caseless, Integer>();
		fileTypes = new HashMap<Integer, FileType>();
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
		List<Caseless> files = new ArrayList<Caseless>();
		for(Entry<FileIdentifier, Integer> entry : identifiers.entrySet()) {
			FileIdentifier identifier = entry.getKey();
			if(identifier.isFile() && identifier.getProjectName().equals(projectName)) {
				files.add(identifier.getFileName());
			}
		}
		return files;
	}
	
	public List<Caseless> findFilesForProject() {
		List<Caseless> projects = new ArrayList<Caseless>();
		for(Entry<FileIdentifier, Integer> entry : identifiers.entrySet()) {
			FileIdentifier identifier = entry.getKey();
			if(identifier.isProject()) {
				projects.add(identifier.getProjectName());
			}
		}
		return projects;
	}
	
	public Integer getIdentifierForProperty(Caseless name) {
		return userFileIdentifiers.get(name);
	}
	
	public FileType getFileType(Integer fileIdentifier) {
		return fileTypes.get(fileIdentifier);
	}

	public void cacheFileType(Integer fileIdentifier, FileType fileType) {
		fileTypes.put(fileIdentifier, fileType);
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
	
	public void cacheUserFileItem(Caseless name, Integer userFileIdentifier) {
		if(name == null) {
			throw new NullPointerException("Name can not be null.");
		}
		if(userFileIdentifier == null) {
			throw new NullPointerException("User file identifier can not be null.");
		}
		userFileIdentifiers.put(name, userFileIdentifier);
	}
	
	public void cacheItem(Caseless projectName, Integer projectIdentifier) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(projectIdentifier == null) {
			throw new NullPointerException("Project identifier can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName);
		identifiers.put(key, projectIdentifier);
	}

	public void cacheItem(Caseless projectName, Caseless fileName, Integer fileIdentifier) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		if(fileIdentifier == null) {
			throw new NullPointerException("File identifier can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName, fileName);
		identifiers.put(key, fileIdentifier);
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