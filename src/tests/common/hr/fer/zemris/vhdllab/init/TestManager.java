package hr.fer.zemris.vhdllab.init;

import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.constants.UserFileConstants;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.dao.GlobalFileDAO;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.dao.impl.dummy.FileDAOMemoryImpl;
import hr.fer.zemris.vhdllab.dao.impl.dummy.GlobalFileDAOMemoryImpl;
import hr.fer.zemris.vhdllab.dao.impl.dummy.ProjectDAOMemoryImpl;
import hr.fer.zemris.vhdllab.dao.impl.dummy.UserFileDAOMemoryImpl;
import hr.fer.zemris.vhdllab.dao.impl.hibernate.FileDAOHibernateImpl;
import hr.fer.zemris.vhdllab.dao.impl.hibernate.GlobalFileDAOHibernateImpl;
import hr.fer.zemris.vhdllab.dao.impl.hibernate.ProjectDAOHibernateImpl;
import hr.fer.zemris.vhdllab.dao.impl.hibernate.UserFileDAOHibernateImpl;
import hr.fer.zemris.vhdllab.dao.impl.hibernate2.FileDAOHibernateImplWOSession;
import hr.fer.zemris.vhdllab.dao.impl.hibernate2.GlobalFileDAOHibernateImplWOSession;
import hr.fer.zemris.vhdllab.dao.impl.hibernate2.ProjectDAOHibernateImplWOSession;
import hr.fer.zemris.vhdllab.dao.impl.hibernate2.UserFileDAOHibernateImplWOSession;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.GlobalFile;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.model.UserFile;
import hr.fer.zemris.vhdllab.preferences.Preferences;
import hr.fer.zemris.vhdllab.preferences.SingleOption;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.impl.VHDLLabManagerImpl;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.manprovs.SampleManagerProvider;
import hr.fer.zemris.vhdllab.utilities.ModelUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;

/**
 * Helper class used when initializing each test.
 * @author Miro Bezjak
 */
public final class TestManager {
	
	/** VHDLLab Manager helping with initialization. */
	private final VHDLLabManager labman;
	/** A DAO for file model */
	private final FileDAO fileDAO;
	/** A DAO for project model */
	private final ProjectDAO projectDAO;
	/** A DAO for global file model */
	private final GlobalFileDAO globalFileDAO;
	/** A DAO for user file model */
	private final UserFileDAO userFileDAO;
	
	/**
	 * Constructor.
	 * @param fileDAO a DAO for file model
	 * @param projectDAO a DAO for project model
	 * @param globalFileDAO a DAO for global file model
	 * @param userFileDAO a DAO for user file model
	 * @throws NullPointerException if any parametar is <code>null</code>
	 */
	public TestManager(FileDAO fileDAO, ProjectDAO projectDAO,
			GlobalFileDAO globalFileDAO, UserFileDAO userFileDAO) {

		this(null, fileDAO, projectDAO, globalFileDAO, userFileDAO);
	}
	
	/**
	 * Constructor.
	 * @param a vhdllab manager helping with initialization.
	 * @param fileDAO a DAO for file model
	 * @param projectDAO a DAO for project model
	 * @param globalFileDAO a DAO for global file model
	 * @param userFileDAO a DAO for user file model
	 * @throws NullPointerException if any parametar is <code>null</code>
	 */
	public TestManager(VHDLLabManager labman, FileDAO fileDAO, ProjectDAO projectDAO,
			GlobalFileDAO globalFileDAO, UserFileDAO userFileDAO) {

		if(fileDAO == null) {
			throw new NullPointerException("File DAO can not be null.");
		}
		if(projectDAO == null) {
			throw new NullPointerException("Project DAO can not be null.");
		}
		if(globalFileDAO == null) {
			throw new NullPointerException("Global File DAO can not be null.");
		}
		if(userFileDAO == null) {
			throw new NullPointerException("User File DAO can not be null.");
		}

		this.fileDAO = fileDAO;
		this.projectDAO = projectDAO;
		this.globalFileDAO = globalFileDAO;
		this.userFileDAO = userFileDAO;
		if(labman == null) {
			this.labman = new VHDLLabManagerImpl();
		} else {
			this.labman = labman;
		}
		if(this.labman instanceof VHDLLabManagerImpl) {
			VHDLLabManagerImpl labmanImpl = (VHDLLabManagerImpl) this.labman;
			labmanImpl.setFileDAO(fileDAO);
			labmanImpl.setProjectDAO(projectDAO);
			labmanImpl.setGlobalFileDAO(globalFileDAO);
			labmanImpl.setUserFileDAO(userFileDAO);
		} else {
			throw new IllegalArgumentException("Unknown VHDLLab Manager. Can not set DAO interfaces to manager.");
		}
	}
	
	/**
	 * Returns all known DAO implementations.
	 * <p>
	 * Array of <code>Object</code> is as follows:
	 * <ul>
	 * <li>index <code>0</code> - {@link FileDAO}</li>
	 * <li>index <code>1</code> - {@link ProjectDAO}</li>
	 * <li>index <code>2</code> - {@link GlobalFileDAO}</li>
	 * <li>index <code>3</code> - {@link UserFileDAO}</li>
	 * </ul>
	 * @return all known DAO implementations
	 */
	public static List<Object[]> getDAOParametars() {
		List<Object[]> params = new ArrayList<Object[]>();
		params.addAll(TestManager.getDAOMemoryParametars());
		params.addAll(TestManager.getDAOHibernateParametars());
		params.addAll(TestManager.getDAOHibernateWOSessionParametars());
		return params;
	}
	
	/**
	 * Returns all DAO interfaces using memory implementation.
	 * @return all DAO interfaces using memory implementation
	 */
	public static List<Object[]> getDAOMemoryParametars() {
		List<Object[]> params = new ArrayList<Object[]>();
		FileDAO fileDAO = new FileDAOMemoryImpl();
		ProjectDAO projectDAO = new ProjectDAOMemoryImpl(fileDAO);
		GlobalFileDAO globalFileDAO = new GlobalFileDAOMemoryImpl();
		UserFileDAO userFileDAO = new UserFileDAOMemoryImpl();
		params.add(new Object[] {fileDAO, projectDAO, globalFileDAO, userFileDAO});
		return params;
	}

	/**
	 * Returns all DAO interfaces using hibernate (database) implementation.
	 * @return all DAO interfaces using hibernate (database) implementation
	 */
	public static List<Object[]> getDAOHibernateParametars() {
		List<Object[]> params = new ArrayList<Object[]>();
		FileDAO fileDAO = new FileDAOHibernateImpl();
		ProjectDAO projectDAO = new ProjectDAOHibernateImpl();
		GlobalFileDAO globalFileDAO = new GlobalFileDAOHibernateImpl();
		UserFileDAO userFileDAO = new UserFileDAOHibernateImpl();
		params.add(new Object[] {fileDAO, projectDAO, globalFileDAO, userFileDAO});
		return params;
	}
	
	/**
	 * Returns all DAO interfaces to use hibernate (database) implementation.
	 * This implementation also uses hibernate implementation but
	 * session factory is automaticly provided (usualy at tomcat
	 * initialization).
	 * @return all DAO interfaces using hibernate (database) implementation
	 * 		without session
	 */
	public static List<Object[]> getDAOHibernateWOSessionParametars() {
		List<Object[]> params = new ArrayList<Object[]>();
		FileDAOHibernateImplWOSession fileDAO = new FileDAOHibernateImplWOSession();
		ProjectDAOHibernateImplWOSession projectDAO = new ProjectDAOHibernateImplWOSession();
		GlobalFileDAOHibernateImplWOSession globalFileDAO = new GlobalFileDAOHibernateImplWOSession();
		UserFileDAOHibernateImplWOSession userFileDAO = new UserFileDAOHibernateImplWOSession();
		
		SessionFactory sessionFactory;
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch (Exception e) {
			e.printStackTrace();
			return null; // escape from setting this implementation and continue using old one
		}
		fileDAO.setSessionFactory(sessionFactory);
		projectDAO.setSessionFactory(sessionFactory);
		globalFileDAO.setSessionFactory(sessionFactory);
		userFileDAO.setSessionFactory(sessionFactory);
		params.add(new Object[] {fileDAO, projectDAO, globalFileDAO, userFileDAO});
		return params;
	}
	
	/**
	 * Returns all known service implementations.
	 * <p>
	 * Array of <code>Object</code> is as follows:
	 * <ul>
	 * <li>index <code>0</code> - {@link VHDLLabManager}</li>
	 * <li>index <code>1</code> - {@link FileDAO}</li>
	 * <li>index <code>2</code> - {@link ProjectDAO}</li>
	 * <li>index <code>3</code> - {@link GlobalFileDAO}</li>
	 * <li>index <code>4</code> - {@link UserFileDAO}</li>
	 * </ul>
	 * {@link VHDLLabManagerImpl} is used as {@link VHDLLabManager}.<br/>
	 * Memory implementation is used as DAO implementation.
	 * @return all known service implementations
	 */
	public static List<Object[]> getServiceParametars() {
		List<Object[]> params = new ArrayList<Object[]>();
		VHDLLabManagerImpl labman = new VHDLLabManagerImpl();
		FileDAO fileDAO = new FileDAOMemoryImpl();
		ProjectDAO projectDAO = new ProjectDAOMemoryImpl(fileDAO);
		GlobalFileDAO globalFileDAO = new GlobalFileDAOMemoryImpl();
		UserFileDAO userFileDAO = new UserFileDAOMemoryImpl();
		labman.setFileDAO(fileDAO);
		labman.setProjectDAO(projectDAO);
		labman.setGlobalFileDAO(globalFileDAO);
		labman.setUserFileDAO(userFileDAO);
		
		Object[] objects = new Object[] {labman, fileDAO, projectDAO, globalFileDAO, userFileDAO};
		params.add(objects);
		return params;
	}
	
	/**
	 * Returns all known manager provider implementations.
	 * <p>
	 * Array of <code>Object</code> is as follows:
	 * <ul>
	 * <li>index <code>0</code> - {@link ManagerProvider}</li>
	 * <li>index <code>1</code> - {@link VHDLLabManager}</li>
	 * <li>index <code>2</code> - {@link FileDAO}</li>
	 * <li>index <code>3</code> - {@link ProjectDAO}</li>
	 * <li>index <code>4</code> - {@link GlobalFileDAO}</li>
	 * <li>index <code>5</code> - {@link UserFileDAO}</li>
	 * </ul>
	 * {@link VHDLLabManagerImpl} is used as {@link VHDLLabManager}.<br/>
	 * Memory implementation is used as DAO implementation.
	 * @return all known manager provider implementations
	 */
	public static List<Object[]> getWebParametars() {
		List<Object[]> params = new ArrayList<Object[]>();
		SampleManagerProvider mprov = new SampleManagerProvider();
		VHDLLabManagerImpl labman = new VHDLLabManagerImpl();
		FileDAO fileDAO = new FileDAOMemoryImpl();
		ProjectDAO projectDAO = new ProjectDAOMemoryImpl(fileDAO);
		GlobalFileDAO globalFileDAO = new GlobalFileDAOMemoryImpl();
		UserFileDAO userFileDAO = new UserFileDAOMemoryImpl();
		labman.setFileDAO(fileDAO);
		labman.setProjectDAO(projectDAO);
		labman.setGlobalFileDAO(globalFileDAO);
		labman.setUserFileDAO(userFileDAO);
		mprov.setVHDLLabManager(labman);
		
		Object[] objects = new Object[] {mprov, labman, fileDAO, projectDAO, globalFileDAO, userFileDAO};
		params.add(objects);
		return params;
	}
	
	/**
	 * Initializes DAO layer. Stores some projects, global files and user files.
	 * This method is equivalent of separately calling methods:
	 * <ul>
	 * <li>{@link #initProjects(String)}</li>
	 * <li>{@link #initGlobalFiles()}</li>
	 * <li>{@link #initUserFiles(String)}</li>
	 * </ul>
	 * @param userId owner of projects
	 * @throws NullPointerException if <code>userId</code> is <code>null</code>
	 */
	public void initDAO(String userId) {
		if(userId == null) {
			throw new NullPointerException("User identifier can not be null.");
		}
		initProjects(userId);
		initGlobalFiles();
		initUserFiles(userId);
	}
	
	/**
	 * Initializes project.
	 * @param userId owner of projects
	 * @throws NullPointerException if <code>userId</code> is <code>null</code>
	 */
	public void initProjects(String userId) {
		if(userId == null) {
			throw new NullPointerException("User identifier can not be null.");
		}
		try {
			String projectName;
			String fileName;
			Project project;
			
			projectName = "Project1";
			if(!labman.existsProject(userId, projectName)) {
				project = labman.createNewProject(projectName, userId);
			} else {
				project = labman.findProjectByName(userId, projectName);
			}

			fileName = "mux41";
			if(!labman.existsFile(project.getId(), fileName)) {
				File file = labman.createNewFile(project, fileName, FileTypes.FT_VHDL_SOURCE);
				String content = readFile("mux41.vhdl");
				labman.saveFile(file.getId(), content);
			}

			fileName = "mux41_tb";
			if(!labman.existsFile(project.getId(), fileName)) {
				File file = labman.createNewFile(project, fileName, FileTypes.FT_VHDL_TB);
				String content = readFile("mux41_tb.txt");
				labman.saveFile(file.getId(), content);
			}

			fileName = "Automat1";
			if(!labman.existsFile(project.getId(), fileName)) {
				File file = labman.createNewFile(project, fileName, FileTypes.FT_VHDL_AUTOMAT);
				String content = readFile("automat1.xml");
				labman.saveFile(file.getId(), content);
			}
			
			
			projectName = "unused project";
			if(!labman.existsProject(userId, projectName)) {
				labman.createNewProject(projectName, userId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes global files.
	 */
	public void initGlobalFiles() {
		try {
			String fileName;
			
			fileName = "Common language settings";
			if(!labman.existsGlobalFile(fileName)) {
				Preferences preferences = new Preferences();
				List<String> values = new ArrayList<String>();
				values.add("en");
				SingleOption o = new SingleOption(UserFileConstants.COMMON_LANGUAGE, "Language", "String", values, "en", "en");
				preferences.setOption(o);
				
				GlobalFile file = labman.createNewGlobalFile(fileName, FileTypes.FT_COMMON);
				labman.saveGlobalFile(file.getId(), preferences.serialize());
			}
			
			fileName = "Applet Settings";
			if(!labman.existsGlobalFile(fileName)) {
				Preferences preferences = new Preferences();
				SingleOption o = new SingleOption(UserFileConstants.APPLET_PROJECT_EXPLORER_WIDTH, "PE width", "Double", null, "0.15", "0.15");
				preferences.setOption(o);
		
				o = new SingleOption(UserFileConstants.APPLET_SIDEBAR_WIDTH, "Sidebar width", "Double", null, "0.75", "0.75");
				preferences.setOption(o);
		
				o = new SingleOption(UserFileConstants.APPLET_VIEW_HEIGHT, "View height", "Double", null, "0.75", "0.75");
				preferences.setOption(o);
		
				List<String> values = new ArrayList<String>();
				values.add("true");
				values.add("false");
				o = new SingleOption(UserFileConstants.APPLET_SAVE_DIALOG_ALWAYS_SAVE_RESOURCES, "Always save resources", "Boolean", values, "false", "false");
				preferences.setOption(o);

				o = new SingleOption(UserFileConstants.APPLET_OPENED_EDITORS, "Opened editors", false, "String", null, null, null);
				preferences.setOption(o);
				
				o = new SingleOption(UserFileConstants.APPLET_OPENED_VIEWS, "Opened views", false, "String", null, null, null);
				preferences.setOption(o);

				GlobalFile file = labman.createNewGlobalFile(fileName, FileTypes.FT_APPLET);
				labman.saveGlobalFile(file.getId(), preferences.serialize());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes user files.
	 * @param userId owner of projects
	 * @throws NullPointerException if <code>userId</code> is <code>null</code>
	 */
	public void initUserFiles(String userId) {
		if(userId == null) {
			throw new NullPointerException("User identifier can not be null.");
		}
		try {
			String fileName;
			
			fileName = "Common language settings";
			if(!labman.existsUserFile(userId, fileName)) {
				Preferences preferences = new Preferences();
				List<String> values = new ArrayList<String>();
				values.add("en");
				SingleOption o = new SingleOption(UserFileConstants.COMMON_LANGUAGE, "Language", "String", values, "en", "en");
				preferences.setOption(o);
				
				UserFile file = labman.createNewUserFile(userId, fileName, FileTypes.FT_COMMON);
				labman.saveUserFile(file.getId(), preferences.serialize());
			}
			
			fileName = "Applet Settings";
			if(!labman.existsGlobalFile(fileName)) {
				Preferences preferences = new Preferences();
				SingleOption o = new SingleOption(UserFileConstants.APPLET_PROJECT_EXPLORER_WIDTH, "PE width", "Double", null, "0.15", "0.15");
				preferences.setOption(o);
		
				o = new SingleOption(UserFileConstants.APPLET_SIDEBAR_WIDTH, "Sidebar width", "Double", null, "0.75", "0.75");
				preferences.setOption(o);
		
				o = new SingleOption(UserFileConstants.APPLET_VIEW_HEIGHT, "View height", "Double", null, "0.75", "0.75");
				preferences.setOption(o);
		
				List<String> values = new ArrayList<String>();
				values.add("true");
				values.add("false");
				o = new SingleOption(UserFileConstants.APPLET_SAVE_DIALOG_ALWAYS_SAVE_RESOURCES, "Always save resources", "Boolean", values, "false", "false");
				preferences.setOption(o);

				StringBuilder sb = new StringBuilder(30);
				sb.append("Project1/mux41\n")
					.append("Project1/mux41_tb\n")
					.append("Project1/Automat1\n");
				o = new SingleOption(UserFileConstants.APPLET_OPENED_EDITORS, "Opened editors", false, "String", null, null, sb.toString());
				preferences.setOption(o);
				
				o = new SingleOption(UserFileConstants.APPLET_OPENED_VIEWS, "Opened views", false, "String", null, null, null);
				preferences.setOption(o);

				UserFile file = labman.createNewUserFile(userId, fileName, FileTypes.FT_APPLET);
				labman.saveUserFile(file.getId(), preferences.serialize());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Completely delete DAO layer. This method is equivalent of separately
	 * calling methods:
	 * <ul>
	 * <li>{@link #cleanProjects(String)}</li>
	 * <li>{@link #cleanGlobalFiles()}</li>
	 * <li>{@link #cleanUserFiles(String)}</li>
	 * </ul>
	 * @param userId owner of project to clean
	 * @throws NullPointerException if <code>userId</code> is <code>null</code>
	 */
	public void cleanDAO(String userId) {
		if(userId == null) {
			throw new NullPointerException("User identifier can not be null.");
		}
		cleanProjects(userId);
		cleanGlobalFiles();
		cleanUserFiles(userId);
	}

	/**
	 * Delete every project made by user with <code>userId</code> identifier.
	 * @param userId owner of project to clean
	 * @throws NullPointerException if <code>userId</code> is <code>null</code>
	 */
	public void cleanProjects(String userId) {
		if(userId == null) {
			throw new NullPointerException("User identifier can not be null.");
		}
		try {
			for(Project p : projectDAO.findByUser(userId)) {
				projectDAO.delete(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete every global file in DAO layer.
	 */
	public void cleanGlobalFiles() {
		try {
			for(String type : FileTypes.values()) {
				for(GlobalFile f : globalFileDAO.findByType(type)) {
					globalFileDAO.delete(f);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete every user file, in DAO layer, whose owner is user with
	 * <code>userId</code> identifier. 
	 * @param userId owner of user files
	 * @throws NullPointerException if <code>userId</code> is <code>null</code>
	 */
	public void cleanUserFiles(String userId) {
		if(userId == null) {
			throw new NullPointerException("User identifier can not be null.");
		}
		try {
			for(UserFile f : userFileDAO.findByUser(userId)) {
				userFileDAO.delete(f);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a random file from DAO layer from <code>project</code>.
	 * @param p project where file is located
	 * @return a random chosen file or <code>null</code> if <code>project</code>
	 * 		has no files
	 * @throws NullPointerException if <code>userId</code> is <code>null</code>
	 */
	public File pickRandomFile(Project project) {
		if(project == null) {
			throw new NullPointerException("Project can not be null.");
		}
		if(project.getFiles().isEmpty()) {
			return null;
		}
		Random rand = new Random();
		int index = rand.nextInt(project.getFiles().size());
		return (File) project.getFiles().toArray()[index];
	}
	
	/**
	 * Returns a random file from DAO layer from one of users projects.
	 * @param userId owner of project to pick from
	 * @return a random file from DAO layer from one of users projects
	 * @throws NullPointerException if <code>userId</code> is <code>null</code>.
	 */
	public File pickRandomFile(String userId) {
		if(userId == null) {
			throw new NullPointerException("User identifier can not be null.");
		}
		List<Project> projects;
		try {
			projects = labman.findProjectsByUser(userId);
		} catch (ServiceException e) {
			e.printStackTrace();
			return null;
		}
		while(!projects.isEmpty()) {
			Random rand = new Random();
			int index = rand.nextInt(projects.size());
			Project p = projects.get(index);
			if(!p.getFiles().isEmpty()) {
				return pickRandomFile(p);
			} else {
				projects.remove(p);
			}
		}
		return null;
	}
	
	/**
	 * Returns a random project from DAO layer from <code>userId</code>.
	 * @param userId owner of user files
	 * @return a random chosen project or <code>null</code> if there is no
	 * 		projects in DAO layer with <code>userId</code>
	 * @throws NullPointerException if <code>userId</code> is <code>null</code>
	 */
	public Project pickRandomProject(String userId) {
		if(userId == null) {
			throw new NullPointerException("User identifier can not be null.");
		}
		List<Project> projects;
		try {
			projects = labman.findProjectsByUser(userId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if(projects.isEmpty()) {
			return null;
		}
		Random rand = new Random();
		int index = rand.nextInt(projects.size());
		return projects.get(index);
	}
	
	/**
	 * Returns a random project with no files from DAO layer from <code>userId</code>.
	 * @param userId owner of user files
	 * @return a random chosen project with no files or <code>null</code> if
	 * 		there is no projects in DAO layer with <code>userId</code>
	 * @throws NullPointerException if <code>userId</code> is <code>null</code>
	 */
	public Project pickRandomEmptyProject(String userId) {
		if(userId == null) {
			throw new NullPointerException("User identifier can not be null.");
		}
		List<Project> projects;
		try {
			projects = labman.findProjectsByUser(userId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		while(!projects.isEmpty()) {
			Random rand = new Random();
			int index = rand.nextInt(projects.size());
			Project p = projects.get(index);
			if(p.getFiles().isEmpty()) {
				return p;
			} else {
				projects.remove(p);
			}
		}
		return null;
	}
	
	/**
	 * Returns a random global file from DAO layer.
	 * @param userId owner of user files
	 * @return a random chosen global file or <code>null</code> if there is no
	 * 		global files in DAO layer
	 * @throws NullPointerException if <code>userId</code> is <code>null</code>
	 */
	public GlobalFile pickRandomGlobalFile() {
		List<GlobalFile> files = new ArrayList<GlobalFile>();
		try {
			for(String type : FileTypes.values()) {
				files.addAll(labman.findGlobalFilesByType(type));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if(files.isEmpty()) {
			return null;
		}
		Random rand = new Random();
		int index = rand.nextInt(files.size());
		return files.get(index);
	}
	
	/**
	 * Returns a random user file from DAO layer from <code>userId</code>.
	 * @param userId owner of user files
	 * @return a random chosen user file or <code>null</code> if there is no
	 * 		user files in DAO layer with <code>userId</code>
	 * @throws NullPointerException if <code>userId</code> is <code>null</code>
	 */
	public UserFile pickRandomUserFile(String userId) {
		if(userId == null) {
			throw new NullPointerException("User identifier can not be null.");
		}
		List<UserFile> files;
		try {
			files = labman.findUserFilesByUser(userId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if(files.isEmpty()) {
			return null;
		}
		Random rand = new Random();
		int index = rand.nextInt(files.size());
		return files.get(index);
	}
	
	/**
	 * Generates a junk string (content not relevant) whose size equals
	 * <code>size</code>.
	 * @param size a size of a junk string
	 * @return a junk string with size <code>size</code>
	 * @throws IllegalArgumentException is <code>size</code> is negative
	 */
	private static String generateJunkContent(int size) {
		if(size < 0) throw new IllegalArgumentException("Size can not be negative.");
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < size; i++) {
			sb.append("a");
		}
		return sb.toString();
	}
	
	/**
	 * Generates a file name that is larger then maximum allowed file name.
	 * @return a file name that is larger then maximum allowed file name
	 */
	public static String generateOverMaxJunkFileName() {
		return generateJunkContent(255 + 10);
	}
	
	/**
	 * Generates a global file name that is larger then maximum allowed global
	 * file name.
	 * @return a global file name that is larger then maximum allowed global
	 * 		file name
	 */
	public static String generateOverMaxJunkGlobalFileName() {
		return generateJunkContent(255 + 10);
	}
	
	/**
	 * Generates a user file name that is larger then maximum allowed user
	 * file name.
	 * @return a user file name that is larger then maximum allowed user
	 * 		file name
	 */
	public static String generateOverMaxJunkUserFileName() {
		return generateJunkContent(255 + 10);
	}
	
	/**
	 * Generates a project name that is larger then maximum allowed project name.
	 * @return a project name that is larger then maximum allowed project name
	 */
	public static String generateOverMaxJunkProjectName() {
		return generateJunkContent(255 + 10);
	}
	
	/**
	 * Generates a file type that is larger then maximum allowed file type.
	 * @return a file type that is larger then maximum allowed file type
	 */
	public static String generateOverMaxJunkFileType() {
		return generateJunkContent(255 + 10);
	}
	
	/**
	 * Generates a global file type that is larger then maximum allowed global
	 * file type.
	 * @return a global file type that is larger then maximum allowed global
	 * 		file type
	 */
	public static String generateOverMaxJunkGlobalFileType() {
		return generateJunkContent(255 + 10);
	}
	
	/**
	 * Generates a user file type that is larger then maximum allowed user
	 * file type.
	 * @return a user file type that is larger then maximum allowed user
	 * 		file type
	 */
	public static String generateOverMaxJunkUserFileType() {
		return generateJunkContent(255 + 10);
	}
	
	/**
	 * Generates a file content that is larger then maximum allowed file content.
	 * @return a file content that is larger then maximum allowed file content
	 */
	public static String generateOverMaxJunkFileContent() {
		return generateJunkContent(65536 + 10);
	}
	
	/**
	 * Generates a global file content that is larger then maximum allowed global
	 * file content.
	 * @return a global file content that is larger then maximum allowed global
	 * 		file content
	 */
	public static String generateOverMaxJunkGlobalFileContent() {
		return generateJunkContent(65536 + 10);
	}
	
	/**
	 * Generates a user file content that is larger then maximum allowed user
	 * file content.
	 * @return a user file content that is larger then maximum allowed user
	 * 		file content
	 */
	public static String generateOverMaxJunkUserFileContent() {
		return generateJunkContent(65536 + 10);
	}
	
	/**
	 * Generates a owner identifier that is larger then maximum allowed owner identifier.
	 * @return a owner identifier that is larger then maximum allowed owner identifier
	 */
	public static String generateOverMaxJunkOwnerId() {
		return generateJunkContent(255 + 10);
	}
	
	/**
	 * Returns all project containing specified user identifier. If any error,
	 * in DAO layer occures then return value will be <code>null</code>.
	 * @param userId a user identifier
	 * @return all project containing specified user identifier
	 * @throws NullPointerException is <code>userId</code> is <code>null</code>
	 */
	public List<Project> getProjectsByUser(String userId) {
		if(userId == null) {
			throw new NullPointerException("User identifier can not be null.");
		}
		try {
			return projectDAO.findByUser(userId);
		} catch (DAOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns all global files containing specified <code>type</code>. If any
	 * error, in DAO layer occures then return value will be <code>null</code>.
	 * @param type a global file type
	 * @return all global files containing specified <code>type</code>
	 * @throws NullPointerException is <code>type</code> is <code>null</code>
	 */
	public List<GlobalFile> getGlobalFilesByType(String type) {
		if(type == null) {
			throw new NullPointerException("Global file type can not be null.");
		}
		try {
			return globalFileDAO.findByType(type);
		} catch (DAOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns all user files containing specified user identifier. If any
	 * error, in DAO layer occures then return value will be <code>null</code>.
	 * @param userId a user identifier
	 * @return all user files containing specified <code>userId</code>
	 * @throws NullPointerException is <code>userId</code> is <code>null</code>
	 */
	public List<UserFile> getUserFilesByUser(String userId) {
		if(userId == null) {
			throw new NullPointerException("User identifier can not be null.");
		}
		try {
			return userFileDAO.findByUser(userId);
		} catch (DAOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns a file identifier that is not in use. Meaning there is no file
	 * containing that particular identifier. If, after cirtain amount of
	 * iterations (more then 1000) this method can not find such file identifier
	 * then returned value will be <code>null</code>. 
	 * @return a file identifier that is not in use
	 */
	public Long getUnusedFileId() {
		Long id = Long.valueOf(1234567890123L);
		try {
			Random rand = new Random();
			for(int i = 1; i<2001; i++) {
				boolean exists = fileDAO.exists(id);
				if(!exists) return id;
				long newValue = id.longValue() + rand.nextInt(123456789 * i);
				id = Long.valueOf(newValue);
			}
		} catch (DAOException e) {}
		return null;
	}
	
	/**
	 * Returns a global file identifier that is not in use. Meaning there is no
	 * global file containing that particular identifier. If, after cirtain amount
	 * of iterations (more then 1000) this method can not find such global file
	 * identifier then returned value will be <code>null</code>. 
	 * @return a global file identifier that is not in use
	 */
	public Long getUnusedGlobalFileId() {
		Long id = Long.valueOf(1234567890123L);
		try {
			Random rand = new Random();
			for(int i = 1; i<2001; i++) {
				boolean exists = globalFileDAO.exists(id);
				if(!exists) return id;
				long newValue = id.longValue() + rand.nextInt(123456789 * i);
				id = Long.valueOf(newValue);
			}
		} catch (DAOException e) {}
		return null;
	}

	/**
	 * Returns a user file identifier that is not in use. Meaning there is no
	 * user file containing that particular identifier. If, after cirtain amount
	 * of iterations (more then 1000) this method can not find such user file
	 * identifier then returned value will be <code>null</code>. 
	 * @return a user file identifier that is not in use
	 */
	public Long getUnusedUserFileId() {
		Long id = Long.valueOf(1234567890123L);
		try {
			Random rand = new Random();
			for(int i = 1; i<2001; i++) {
				boolean exists = userFileDAO.exists(id);
				if(!exists) return id;
				long newValue = id.longValue() + rand.nextInt(123456789 * i);
				id = Long.valueOf(newValue);
			}
		} catch (DAOException e) {}
		return null;
	}
	
	/**
	 * Returns a project identifier that is not in use. Meaning there is no
	 * projects containing that particular identifier. If, after cirtain amount
	 * of iterations (more then 1000) this method can not find such project
	 * identifier then returned value will be <code>null</code>. 
	 * @return a project identifier that is not in use
	 */
	public Long getUnusedProjectId() {
		Long id = Long.valueOf(1234567890123L);
		try {
			Random rand = new Random();
			for(int i = 1; i<2001; i++) {
				boolean exists = projectDAO.exists(id);
				if(!exists) return id;
				long newValue = id.longValue() + rand.nextInt(123456789 * i);
				id = Long.valueOf(newValue);
			}
		} catch (DAOException e) {}
		return null;
	}
	
	/**
	 * Returns a user identifier that is not in use. Meaning there is no records
	 * of user's projects and user files containing that particular user id. If,
	 * after cirtain amount of iterations (more then 1000) this method can not
	 * find such user identifier then returned value will be <code>null</code>. 
	 * @return a user identifier that is not in use
	 */
	public String getUnusedUserId() {
		String userId = "uid:id-not-set";
		try {
			for(int i = 1; i<2001; i++) {
				List<Project> projects = labman.findProjectsByUser(userId);
				if(projects.isEmpty()) {
					return userId;
//					List<UserFile> files = labman.findUserFilesByUser(userId);
//					if(files.isEmpty()) return userId;
				}
				userId = userId + "::" + i;
			}
		} catch (ServiceException e) {}
		return null;
	}
	
	/**
	 * Returns a file name that is not in use. Meaning there is no file
	 * containing that particular name in <code>project</code>. If, after
	 * cirtain amount of iterations (more then 1000) this method can not
	 * find such file name then returned value will be <code>null</code>.
	 * @param project a project where to look for such file name 
	 * @return a file name that is not in use
	 * @throws NullPointerException if <code>project</code> is <code>null</code>
	 */
	public String getUnusedFileName(Project project) {
		if(project == null) {
			throw new NullPointerException("Project can not be null.");
		}
		String fileName = "a never used file name";
		for(int i = 1; i<2001; i++) {
			boolean equals = false;
			for(File f : project.getFiles()) {
				if(ModelUtil.fileNamesAreEqual(fileName, f.getFileName())) {
					equals = true;
					break;
				}
			}
			if(!equals) return fileName;
			fileName = fileName + "::" + i;
		}
		return null;
	}
	
	/**
	 * Returns a global file name that is not in use. Meaning there is no
	 * global file with that name in DAO layer. If, after cirtain amount
	 * of iterations (more then 1000) this method can not find such global
	 * file name then returned value will be <code>null</code>.
	 * @return a global file name that is not in use
	 */
	public String getUnusedGlobalFileName() {
		String name = "a never used global file name";
		try {
			for(int i = 1; i<2001; i++) {
				boolean exists = globalFileDAO.exists(name);
				if(!exists) return name;
				name = name + "::" + i;
			}
		} catch (DAOException e) {}
		return null;
	}
	
	/**
	 * Returns a user file name that is not in use. Meaning there is no
	 * user file with that name in DAO layer. If, after cirtain amount
	 * of iterations (more then 1000) this method can not find such user
	 * file name then returned value will be <code>null</code>.
	 * @param userId a user identifier for whom to user files
	 * @return a user file name that is not in use
	 * @throws NullPointerException is <code>userId</code> is <code>null</code>
	 */
	public String getUnusedUserFileName(String userId) {
		if(userId == null) {
			throw new NullPointerException("User identifier can not be null.");
		}
		String name = "a never used user file name";
		try {
			for(int i = 1; i<2001; i++) {
				boolean exists = userFileDAO.exists(userId, name);
				if(!exists) return name;
				name = name + "::" + i;
			}
		} catch (DAOException e) {}
		return null;
	}
	
	/**
	 * Returns a project name that is not in use. Meaning there is no projects
	 * by that name having user <code>userId</code>. If, after cirtain amount
	 * of iterations (more then 1000) this method can not find such project name
	 * then returned value will be <code>null</code>.
	 * @param userId a user identifier for whom to find projects 
	 * @return a project name that is not in use
	 * @throws NullPointerException is <code>userId</code> is <code>null</code>
	 */
	public String getUnusedProjectName(String userId) {
		if(userId == null) {
			throw new NullPointerException("User identifier can not be null.");
		}
		List<Project> projects;
		try {
			projects = labman.findProjectsByUser(userId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if(projects.isEmpty()) {
			return null;
		}
		String projectName = "a never used project name";
		for(int i = 1; i<2001; i++) {
			boolean equals = false;
			for(Project p : projects) {
				if(ModelUtil.projectNamesAreEqual(projectName, p.getProjectName())) {
					equals = true;
					break;
				}
			}
			if(!equals) return projectName;
			projectName = projectName + "::" + i;
		}
		return null;
	}
	
	/**
	 * Returns a global file type that is not in use. Meaning there is no
	 * global files with that type in DAO layer. If, after cirtain amount
	 * of iterations (more then 1000) this method can not find such global
	 * file type then returned value will be <code>null</code>.
	 * @return a global file type that is not in use
	 */
	public String getUnusedGlobalFileType() {
		String type = "a never used global file type";
		try {
			for(int i = 1; i<2001; i++) {
				List<GlobalFile> files = globalFileDAO.findByType(type);
				if(files.isEmpty()) return type;
				type = type + "::" + i;
			}
		} catch (DAOException e) {}
		return null;
	}
	
	/**
	 * Helper method. This method reads contents of given file
	 * and return it as a string.
	 * @param fileName name of file to read
	 * @return string which corresponds to content of file; <code>null</code> 
	 *         if file could not be read or other error occur.
	 * @throws NullPointerException if <code>fileName</code> is <code>null</code>
	 */
	private String readFile(String fileName) {
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		InputStream is = this.getClass().getResourceAsStream(fileName);
		StringBuilder sb = new StringBuilder(1024);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			char[] buf = new char[1024*16];
			while(true) {
				int read = br.read(buf);
				if(read<1) break;
				sb.append(buf,0,read);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if(br!=null) try {br.close();} catch(Exception ex) {}
		}
		return sb.toString();
	}
	
}
