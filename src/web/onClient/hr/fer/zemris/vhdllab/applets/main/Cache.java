package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.MethodInvoker;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JOptionPane;

	public class Cache {

		private MethodInvoker invoker;

		private String ownerId;
		private String options;
		private List<String> filetypes;
		private Properties editors;
		
		private Map<String, Long> identifiers;
		
		public Cache(MethodInvoker invoker, String ownerId) {
			if(invoker == null) throw new NullPointerException("Method invoker can not be null");
			identifiers = new HashMap<String, Long>();
			filetypes = loadType("filetypes.properties");
			editors = loadEditors("editors.properties");
			this.invoker = invoker;
			this.ownerId = ownerId;
		}
		
		public List<String> findProjects() {
			List<Long> projects;
			try {
				projects = invoker.findProjectsByUser(ownerId);
			} catch (AjaxException e) {
				return null;
			}
			
			List<String> projectNames = new ArrayList<String>();
			for(Long id : projects) {
				String name;
				try {
					name = invoker.loadProjectName(id);
				} catch (AjaxException e) {
					return null;
				}
				cacheItem(name, id);
				projectNames.add(name);
			}
			return projectNames;
		}
		
		public List<String> findFilesByProject(String projectName) {
			Long projectId = getIdentifierFor(projectName);
			List<Long> files;
			try {
				files =  invoker.findFileByProject(projectId);
			} catch (AjaxException e) {
				return null;
			}
			
			List<String> fileNames = new ArrayList<String>();
			for(Long id : files) {
				String name;
				try {
					name = invoker.loadFileName(id);
				} catch (AjaxException e) {
					return null;
				}
				cacheItem(projectName, name, id);
				fileNames.add(name);
			}
			return fileNames;
		}
		
		public boolean existsFile(String projectName, String fileName) {
			Long projectId = getIdentifierFor(projectName);
			if(projectId == null) return false;
			boolean exists;
			try {
				exists = invoker.existsFile(projectId, fileName);
			} catch (AjaxException e) {
				return false;
			}
			return exists;
		}
		
		public boolean existsProject(String projectName) {
			Long projectId = getIdentifierFor(projectName);
			if(projectId == null) return false;
			boolean exists;
			try {
				exists = invoker.existsProject(projectId);
			} catch (AjaxException e) {
				return false;
			}
			return exists;
		}
		
		public void createProject(String projectName) {
			Long id;
			try {
				id = invoker.createProject(projectName, ownerId);
			} catch (AjaxException e) {
				return;
			}
			cacheItem(projectName, id);
		}

		public void createFile(String projectName, String fileName, String type) {
			Long projectId = getIdentifierFor(projectName);
			Long id;
			try {
				id = invoker.createFile(projectId, fileName, type);
			} catch (AjaxException e) {
				return;
			}
			cacheItem(projectName, fileName, id);
		}
		
		public void saveFile(String projectName, String fileName, String content) {
			Long id = getIdentifierFor(projectName, fileName);
			try {
				invoker.saveFile(id, content);
			} catch (AjaxException e) {
				return;
			}
		}
		
		public String loadFileContent(String projectName, String fileName) {
			Long id = getIdentifierFor(projectName, fileName);
			String content = null;
			try {
				content = invoker.loadFileContent(id);
			} catch (AjaxException e) {
				content = null;
			}
			return content;
		}
		
		public String loadFileType(String projectName, String fileName) {
			Long id = getIdentifierFor(projectName, fileName);
			String type = null;
			try {
				type = invoker.loadFileType(id);
			} catch (AjaxException e) {
				type = null;
			}
			return type;
		}
		
		public CircuitInterface getCircuitInterfaceFor(String projectName, String fileName) {
			Long id = getIdentifierFor(projectName, fileName);
			CircuitInterface ci = null;
			try {
				ci = invoker.extractCircuitInterface(id);
			} catch (AjaxException e) {
				ci = null;
			}
			return ci;
		}
		
		public IEditor getEditor(String type) {
			String editorName = editors.getProperty(type);
			IEditor editor = null;
			try {
				editor = (IEditor)Class.forName(editorName).newInstance();
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
			    PrintWriter pw = new PrintWriter(sw);
			    e.printStackTrace(pw);
				JOptionPane.showMessageDialog(null, sw.toString());
				editor = null;
			}
			return editor;
		}
		
		public String getOptions() {
			if(options != null) return options;
			
			List<Long> userFiles;
			try {
				userFiles = invoker.findUserFilesByOwner(ownerId);
			} catch (AjaxException e) {
				setServerDefaultOptionsAsLocal();
				try {
					userFiles = invoker.findUserFilesByOwner(ownerId);
				} catch (AjaxException e1) {
					return null;
				}
			}
			
			StringBuilder sb = new StringBuilder(200);
			for(Long id : userFiles) {
				String content;
				try {
					content = invoker.loadUserFileContent(id);
				} catch (AjaxException e) {
					return null;
				}
				sb.append(content).append("\n");
			}
			
			options = sb.toString();
			return options;
		}
		
		public void setServerDefaultOptionsAsLocal() {
			for(String type : filetypes) {
				List<Long> globalFiles;
				try {
					globalFiles = invoker.findGlobalFilesByType(type);
				} catch (AjaxException e1) {
					continue;
				}
				for(Long id : globalFiles) {
					try {
						String content = invoker.loadGlobalFileContent(id);
						String globalType = invoker.loadGlobalFileType(id);
						Long userFileId = invoker.createUserFile(ownerId, globalType);
						invoker.saveUserFile(userFileId, content);
					} catch (AjaxException e) {
						continue;
					}
				}
			}
		}
		
		private List<String> loadType(String file) {
			List<String> types = new ArrayList<String>();
			InputStream in = this.getClass().getResourceAsStream(file);
			if(in == null) throw new NullPointerException("Can not find resource " + file + ".");
			Properties p = new Properties();
			try {
				p.load(in);
				for(Object o : p.keySet()) {
					String key = (String) o;
					types.add(p.getProperty(key));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {in.close();} catch (Throwable ignore) {}
			}
			return types;
		}
		
		private Properties loadEditors(String file) {
			InputStream in = this.getClass().getResourceAsStream(file);
			if(in == null) throw new NullPointerException("Can not find resource " + file + ".");
			Properties p = new Properties();
			try {
				p.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {in.close();} catch (Throwable ignore) {}
			}
			return p;
		}
		
		private Long getIdentifierFor(String projectName) {
			String key = makeKey(projectName);
			return identifiers.get(key);
		}

		private Long getIdentifierFor(String projectName, String fileName) {
			String key = makeKey(projectName, fileName);
			return identifiers.get(key);
		}
		
		private String makeKey(String str) {
			return str;
		}

		private String makeKey(String str1, String str2) {
			return str1 + "|" + str2;
		}

		private void cacheItem(String projectName, Long projectIdentifier) {
			if(projectName == null | projectIdentifier == null) {
				throw new NullPointerException("Project name and identifier can not be null.");
			}
			String key = makeKey(projectName);
			identifiers.put(key, projectIdentifier);
		}
		
		private void cacheItem(String projectName, String fileName, Long fileIdentifier) {
			if(projectName == null | fileName == null | fileIdentifier == null) {
				throw new NullPointerException("Project name, file name and identifier can not be null.");
			}
			String key = makeKey(projectName, fileName);
			identifiers.put(key, fileIdentifier);
		}

	}