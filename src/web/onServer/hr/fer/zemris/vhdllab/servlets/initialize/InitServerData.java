package hr.fer.zemris.vhdllab.servlets.initialize;

import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.constants.UserFileConstants;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.GlobalFile;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.model.UserFile;
import hr.fer.zemris.vhdllab.preferences.Preferences;
import hr.fer.zemris.vhdllab.preferences.SingleOption;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Initializes data. For example: writes all global files.
 * @author Miro Bezjak
 */
public class InitServerData {

	private static final String userId = "uid:id-not-set";

	private VHDLLabManager labman;

	/**
	 * Constructor.
	 * @param mprov a manager provider
	 */
	public InitServerData(ManagerProvider mprov) {
		if(mprov == null) {
			throw new NullPointerException("ManagerProvider can not be null.");
		}
		this.labman = (VHDLLabManager) mprov.get(ManagerProvider.VHDL_LAB_MANAGER);
	}

	/**
	 * Initializes all global files.
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
//				UserFile file = labman.createNewUserFile(userId, "Common language settings", FileTypes.FT_COMMON);
//				labman.saveUserFile(file.getId(), preferences.serialize());
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

				o = new SingleOption(UserFileConstants.APPLET_OPENED_EDITORS, "Opened editors", "String", null, null, null);
				preferences.setOption(o);
				
				o = new SingleOption(UserFileConstants.APPLET_OPENED_VIEWS, "Opened views", "String", null, null, null);
				preferences.setOption(o);

				GlobalFile file = labman.createNewGlobalFile(fileName, FileTypes.FT_APPLET);
				labman.saveGlobalFile(file.getId(), preferences.serialize());
//				UserFile file = labman.createNewUserFile(userId, "Applet Settings", FileTypes.FT_APPLET);
//				labman.saveUserFile(file.getId(), preferences.serialize());
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes user files. Should be used only when testing client
	 * application.
	 */
	public void initUserFiles() {
		try {
			String fileName;
			
			fileName = "Applet Settings";
			if(!labman.existsUserFile(userId, fileName)) {
				Preferences preferences = new Preferences();
				StringBuilder sb = new StringBuilder(30);
				sb.append("Project1/mux41\n")
					.append("Project1/mux41_tb\n")
					.append("Project1/Automat1\n");
				SingleOption o = new SingleOption(UserFileConstants.APPLET_OPENED_EDITORS, "Opened editors", "String", null, null, sb.toString());
				preferences.setOption(o);

				UserFile file = labman.createNewUserFile(userId, "Applet Settings", FileTypes.FT_APPLET);
				labman.saveUserFile(file.getId(), preferences.serialize());
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializes projects. Should be used only when testing client
	 * application.
	 */
	public void initDummyProjects() {
		try {
			String projectName = "Project1";
			String fileName;
			Project project;
			if(!labman.existsProject(userId, projectName)) {
				project = labman.createNewProject(projectName, userId);
			} else {
				project = labman.getProject(userId, projectName);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes all created dummy projects.
	 */
	public void cleanServerData() {
		try {
			List<Project> projects = labman.findProjectsByUser(userId);
			for(Project p : projects) {
				labman.deleteProject(p.getId());
			}

			projects = labman.findProjectsByUser(userId);
			System.out.println(projects);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper method. This method reads contents of given file
	 * and return it as a string.
	 * @param fileName name of file to read
	 * @return string which corresponds to content of file; <code>null</code> 
	 *         if file could not be read or other error occur.
	 */
	private String readFile(String fileName) {
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
//			ex.printStackTrace();
//			return null;
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			throw new NullPointerException(sw.toString());
		} finally {
			if(br!=null) try {br.close();} catch(Exception ex) {}
		}
		return sb.toString();
	}

}
