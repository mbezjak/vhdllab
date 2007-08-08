package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.preferences.IUserPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Cache {

	private Map<FileIdentifier, Long> identifiers;
	private Map<String, Long> userFileIdentifiers;
	
	private Map<Long, String> fileTypes;

	private IUserPreferences preferences;


	public Cache() {
		identifiers = new HashMap<FileIdentifier, Long>();
		userFileIdentifiers = new HashMap<String, Long>();
		fileTypes = new HashMap<Long, String>();
	}

	public boolean containsIdentifierFor(String projectName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		return getIdentifierFor(projectName) != null;
	}

	public boolean containsIdentifierFor(String projectName, String fileName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		return getIdentifierFor(projectName, fileName) != null;
	}
	
	public List<String> findFilesForProject(String projectName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		List<String> files = new ArrayList<String>();
		for(Entry<FileIdentifier, Long> entry : identifiers.entrySet()) {
			FileIdentifier identifier = entry.getKey();
			if(identifier.isFile() && identifier.getProjectName().equals(projectName)) {
				files.add(identifier.getFileName());
			}
		}
		return files;
	}
	
	public List<String> findFilesForProject() {
		List<String> projects = new ArrayList<String>();
		for(Entry<FileIdentifier, Long> entry : identifiers.entrySet()) {
			FileIdentifier identifier = entry.getKey();
			if(identifier.isProject()) {
				projects.add(identifier.getProjectName());
			}
		}
		return projects;
	}
	
	public IUserPreferences getUserPreferences() {
		return preferences;
	}
	
	public Long getIdentifierForProperty(String name) {
		return userFileIdentifiers.get(name);
	}
	
	public String getFileType(Long fileIdentifier) {
		return fileTypes.get(fileIdentifier);
	}

	public void cacheFileType(Long fileIdentifier, String fileType) {
		fileTypes.put(fileIdentifier, fileType);
	}

	public FileIdentifier getFileForIdentifier(Long id) {
		if(id == null) {
			throw new NullPointerException("File identifier can not be null.");
		}
		for(Entry<FileIdentifier, Long> entry : identifiers.entrySet()) {
			FileIdentifier identifier = entry.getKey();
			if(entry.getValue().equals(id) && identifier.isFile()) {
				return identifier;
			}
		}
		return null;
	}
	
	public String getProjectForIdentifier(Long id) {
		if(id == null) {
			throw new NullPointerException("Project identifier can not be null.");
		}
		for(Entry<FileIdentifier, Long> entry : identifiers.entrySet()) {
			FileIdentifier identifier = entry.getKey();
			if(entry.getValue().equals(id) && identifier.isProject()) {
				return identifier.getProjectName();
			}
		}
		return null;
	}
	
	public Long getIdentifierFor(String projectName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName);
		return identifiers.get(key);
	}

	public Long getIdentifierFor(String projectName, String fileName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName, fileName);
		return identifiers.get(key);
	}
	
	public void cacheUserPreferences(IUserPreferences preferences) {
		this.preferences = preferences;
	}

	public void cacheUserFileItem(String name, Long userFileIdentifier) {
		if(name == null) {
			throw new NullPointerException("Name can not be null.");
		}
		if(userFileIdentifier == null) {
			throw new NullPointerException("User file identifier can not be null.");
		}
		userFileIdentifiers.put(name, userFileIdentifier);
	}
	
	public void cacheItem(String projectName, Long projectIdentifier) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(projectIdentifier == null) {
			throw new NullPointerException("Project identifier can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName);
		identifiers.put(key, projectIdentifier);
	}

	public void cacheItem(String projectName, String fileName, Long fileIdentifier) {
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

	public void removeUserFileItem(String type) {
		if(type == null) {
			throw new NullPointerException("Type can not be null.");
		}
		userFileIdentifiers.remove(type);
	}

	public void removeItem(String projectName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName);
		identifiers.remove(key);
	}

	public void removeItem(String projectName, String fileName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName, fileName);
		identifiers.remove(key);
	}

	private FileIdentifier makeIdentifier(String projectName) {
		FileIdentifier identifier = new FileIdentifier(projectName);
		return identifier;
	}

	private FileIdentifier makeIdentifier(String projectName, String fileName) {
		FileIdentifier identifier = new FileIdentifier(projectName, fileName);
		return identifier;
	}

}