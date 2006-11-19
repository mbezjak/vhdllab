package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.MethodInvoker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

	public class Cache {

		private MethodInvoker invoker;

		private Long ownerId;
		private String options;
		private List<String> filetypes;
		private List<String> globalFiletypes;
		private List<String> userFiletypes;
		
		private Map<String, Long> identifiers;
		
		public Cache(MethodInvoker invoker) {
			if(invoker == null) throw new NullPointerException("Method invoker can not be null");
			identifiers = new HashMap<String, Long>();
			filetypes = loadType("/hr/fer/zemris/vhdllab/model/filetype.properties");
			globalFiletypes = loadType("/hr/fer/zemris/vhdllab/model/globalFiletype.properties");
			userFiletypes = loadType("/hr/fer/zemris/vhdllab/model/userFiletype.properties");
			this.invoker = invoker;
			this.ownerId = Long.valueOf(0);
		}
		
		public String getOptions() {
			if(options != null) return options;
			
			List<Long> userFiles;
			try {
				userFiles = invoker.findUserFilesByOwner(ownerId);
			} catch (AjaxException e) {
				for(String type : globalFiletypes) {
					List<Long> globalFiles;
					try {
						globalFiles = invoker.findGlobalFilesByType(type);
					} catch (AjaxException e1) {}
					
				}
			}
			
			
			return options;
		}
		
		private List<String> loadType(String file) {
			List<String> types = new ArrayList<String>();
			InputStream in = this.getClass().getResourceAsStream(file);
			if(in == null) throw new NullPointerException("Can not find resource.");
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

		private void cacheItem(String projectName, Long projectIdentifier) {
			if(projectName == null | projectIdentifier == null) {
				throw new NullPointerException("Project name and identifier can not be null.");
			}
			identifiers.put(projectName, projectIdentifier);
		}
		
		private void cacheItem(String projectName, String fileName, Long fileIdentifier) {
			if(projectName == null | fileName == null | fileIdentifier == null) {
				throw new NullPointerException("Project name, file name and identifier can not be null.");
			}
			String key = projectName + "|" + fileName;
			identifiers.put(key, fileIdentifier);
		}

	}