package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.dao.GlobalFileDAO;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.GlobalFile;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.model.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.dependency.IDependency;
import hr.fer.zemris.vhdllab.vhdl.CompilationMessage;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationMessage;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.VHDLDependencyExtractor;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultType;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Extractor;
import hr.fer.zemris.vhdllab.vhdl.tb.Testbench;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;


public class VHDLLabManagerImpl0 implements VHDLLabManager {
	
	private FileDAO fileDAO;
	private ProjectDAO projectDAO;
	private GlobalFileDAO globalFileDAO;
	private UserFileDAO userFileDAO;
	
	public VHDLLabManagerImpl0() {}
	
	
	/**prije koristenja ove metode moraju biti postavljeni propertiji: "simulator" 
	 * npr C:\\Program Files\\Ghdl\\bin\\ghdl.exe i direktorij u kojem se nalaze 
	 * korisnicki dir-ovi imenivani prema OwnerID-ovima 
	 */
	public CompilationResult compile(Long fileId) throws ServiceException {
		
			InputStream is = null;
			Properties prop = new Properties();
			try {
				is = this.getClass().getClassLoader().getResourceAsStream("appProperties");
			//gdje smjestiti nesto da ga cl loader nade
				
				prop.load(is);
			} catch(Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			} finally {
				if(is!=null) {
					try { is.close(); } catch(Exception ex) {}
				}
			}
		
			System.out.println("dataDir "+prop.getProperty("dataDir"));
			System.out.println("simulator "+prop.getProperty("simulator"));
			File f=loadFile(fileId);
			String user= f.getProject().getOwnerId();
			
			String content = f.getContent();
			
			String CIime = null;
			CircuitInterface ci= Extractor.extractCircuitInterface(f.getContent());
			CIime= ci.getEntityName();
			
							        
			String t =f.getFileType();
			
			FileWriter writer;
			BufferedWriter bWriter;
			
			
			ArrayList list =new ArrayList<CompilationMessage>();
			
			if(t.equals(FileTypes.FT_VHDLSOURCE)){
				
				String ime = f.getFileName()+".vhdl";
				String of = prop.getProperty("dataDir")+user+"\\\\";
				System.out.println(of);
				java.io.File outputFile = new java.io.File(of+ime);
				
				
				try {
					writer = new FileWriter(outputFile);
					bWriter = new BufferedWriter(writer);
					
					
					BufferedReader sReader =new BufferedReader(new StringReader(content));
					//ako se dogode problemi korisriti read umjesto readline
					 String line;
					 while ((line = sReader.readLine()) != null){
					    System.out.println(line);
						 bWriter.write(line);
					}
					//int line;
					//while((line= sReader.read())!= -1){
					///	System.out.println(line+"jdjksdjhf");
					//	bWriter.write(line);
					//}
					 
					 bWriter.close();
				
				} catch (IOException e1) {
					throw new ServiceException();
				}
				
				
				
				try {		        
					list=Rn.cmp(ime, CIime, of, prop.getProperty("simulator"));
				} catch (IOException e) {
					throw new ServiceException();
				} catch (InterruptedException e) {
					throw new ServiceException();
				}
				
				
			}else if(t.equals(FileTypes.FT_VHDLTB)){
				
				String ime = f.getFileName()+".vhdl";
				String of = prop.getProperty("dataDir")+user+"\\\\";
				System.out.println(of);
				java.io.File outputFile = new java.io.File(of+ime);
				

				try {
					writer = new FileWriter(outputFile);
					bWriter = new BufferedWriter(writer);
					
					
					BufferedReader sReader =new BufferedReader(new StringReader(content));
					//ako se dogode problemi korisriti read umjesto readline
					 String line;
					 while ((line = sReader.readLine()) != null){
					    System.out.println(line);
						 bWriter.write(line);
					}
					//int line;
					//while((line= sReader.read())!= -1){
					///	System.out.println(line+"jdjksdjhf");
					//	bWriter.write(line);
					//}
					 
					 bWriter.close();
				
				} catch (IOException e1) {
					throw new ServiceException();
				}
				
				
				
				try {		         	
					list=Rn.cmp(ime,CIime, of , prop.getProperty("simulator"));
				} catch (IOException e) {
					throw new ServiceException();
				} catch (InterruptedException e) {
					throw new ServiceException();
				}

				
				
			}else if(t.equals(FileTypes.FT_STRUCT_SCHEMA)){
				
				//TODO 
				
				
				
			}
			
			
			boolean b= true;
			
			if (!list.isEmpty()){
				b=false;
			}
									 //TODO sto bi trebalo ici kao prvi argument
			return new CompilationResult(0, b,list);
	}

	
	
	
	
	
	public File createNewFile(Project project, String fileName, String fileType) throws ServiceException {
		File file = new File();
		file.setFileName(fileName);
		file.setFileType(fileType);
		file.setProject(project);
		try {
			fileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return file;
	}

	public List<Project> findProjectsByUser(String userId) throws ServiceException {
		List<Project> projects = null;
		try {
			projects = projectDAO.findByUser(userId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return projects;
	}

	public String generateVHDL(File file) throws ServiceException {
		//TODO nema nacina da utvrdim direction
		if(file.getFileType().equals("FT_VHDLTB")){
			String data=file.getContent();
			/*data dolazi u obliku:
			 * <measureUnit>ns<measureUnit>1000</duration>
					<signal name="A" type="scalar">(0,0)(100, 1)(150, 0)(300,1)</signal> 
					<signal name="b" type="scalar">(0,0)(200, 1)(300, z)(440, U)</signal>
					<signal name="c" type="scalar" rangeFrom="0" rangeTo="0">(0,0)(50,1)(300,0)(400,1)</signal>
					<signal name="d" type="vector" rangeFrom="0" rangeTo="0">(100,1)(200,0)(300,1)(400,z)</signal>
					<signal name="e" type="vector" rangeFrom="2" rangeTo="0">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>
					<signal name="f" type="vector" rangeFrom="1" rangeTo="4">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>
			
			 */
			//PROBLEM smijer , nemam nacina da utvrdim smijer!!!!!!!!!!!!!!!!!!!!
			/*
			 * 
					 name="A"type="scalar"
					 name="b"type="scalar"
					 name="c"type="scalar"rangeFrom="0"rangeTo="0"
					 name="d"type="vector"rangeFrom="0"rangeTo="0"
					 name="e"type="vector"rangeFrom="2"rangeTo="0"
					 f 1 4
					 ime 21 484
			 */
			DefaultCircuitInterface ci = new DefaultCircuitInterface(file.getFileName());
			
			
			data=data.split("</duration>")[1].replace("<signal","").replace(" ","");
			String[] p=data.split("</signal>");
			
			
			int l=p.length;
			for(int i=0;i<l;i++){
				if(p[i].contains("scalar")){
					
					p[i]=p[i].split(">")[0];
					String ime =p[i].replace("name=\"","").replace("\"type=\"scalar\"","");
					ci.addPort(new DefaultPort(ime, Direction.IN, new DefaultType("std_logic", null, null)));
					
				}else if(p[i].contains("vector")){
					
					p[i]=p[i].split(">")[0];
					String pom =p[i].replace("name=\"","").replace("\"type=\"vector\"rangeFrom=\""," ").replace("\"rangeTo=\""," ").replace("\"","");
					String[] pom2=pom.split(" ");
					String dir;
					if(Integer.parseInt(pom2[1])>Integer.parseInt(pom2[2])){
						dir =new String("DOWNTO");
					}else{
						dir=new String("TO");
						 
					}									//nema nacina da utvrdim direction TODO
					ci.addPort(new DefaultPort(pom2[0], Direction.IN, new DefaultType("std_logic", new int[] {Integer.parseInt(pom2[1]), Integer.parseInt(pom2[2])}, dir)));
					
				}
			}
			
			String VHDLtb = null;
			try {
				VHDLtb = Testbench.writeVHDL(ci,file.getContent());
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServiceException();
			}
			
			return VHDLtb;	
			
		}else{
			return "";
		}
		
		
		
	}

	public File loadFile(Long fileId) throws ServiceException {
		try {
			return fileDAO.load(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public Project loadProject(Long projectId) throws ServiceException {
		try {
			return projectDAO.load(projectId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public void renameFile(Long fileId, String newName) throws ServiceException {
		try {
			File file = fileDAO.load(fileId);
			file.setFileName(newName);
			fileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public void renameProject(Long projectId, String newName) throws ServiceException {
		try {
			Project project = projectDAO.load(projectId);
			project.setProjectName(newName);
			projectDAO.save(project);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public SimulationResult runSimulation(Long fileId) throws ServiceException  {
		InputStream is = null;
		Properties prop = new Properties();
		try {
			is = this.getClass().getClassLoader().getResourceAsStream("appProperties");
		//gdje smjestiti nesto da ga cl loader nade
			
			prop.load(is);
		} catch(Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		} finally {
			if(is!=null) {
				try { is.close(); } catch(Exception ex) {}
			}
		}
	
		System.out.println("dataDir "+prop.getProperty("dataDir"));
		System.out.println("simulator "+prop.getProperty("simulator")); new SimulationResult(0, true, new ArrayList<SimulationMessage>(), "");
		
		
		File f = null;
		try {
			f = loadFile(fileId);
		} catch (ServiceException e) {
			e.printStackTrace();
	
		}
		
		String user = f.getProject().getOwnerId();
		
		String CIime = null;
		CircuitInterface ci= Extractor.extractCircuitInterface(f.getContent());
		CIime= ci.getEntityName();
		
		ArrayList list=null;
		String dd = prop.getProperty("dataDir")+user;//dir u kojemu se radi je /neki dir/.../user/                prop sadrzi prvi dio /neko dir/.../
		try {					
			list = Rn.sim(CIime,dd,prop.getProperty("simulator"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		
			
			
			
		boolean b= true;
		//provjeri
		if (!list.isEmpty()){
			b=false;
		}
		//String wawe= new VcdParser()
		//sto u SimulationResult-u treba biti waveform
		//ako je rez treba biti parsirani vcd trebam cd parser koji mogu upotrijebiti na
		//string ili datoteku u dir-u koji ja odredim 
		
		BufferedReader reader=null;
		StringBuffer bu = new StringBuffer();
		
		try {
			reader= new BufferedReader(new FileReader(dd+"adder.vcd"/*CIime+".vcd"*/));
			String l;
			while((l=reader.readLine())!=null){
				bu.append(l);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		
		
		return new SimulationResult(0, b,list,new String(bu));
	}

	public void saveFile(Long fileId, String content) throws ServiceException {
		File file = loadFile(fileId);
		file.setContent(content);
		try {
			fileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public void saveProject(Project p) throws ServiceException {
		try {
			projectDAO.save(p);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public FileDAO getFileDAO() {
		return fileDAO;
	}
	public void setFileDAO(FileDAO fileDAO) {
		this.fileDAO = fileDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}
	
	public GlobalFileDAO getGlobalFileDAO() {
		return globalFileDAO;
	}
	public void setGlobalFileDAO(GlobalFileDAO globalFileDAO) {
		this.globalFileDAO = globalFileDAO;
	}

	public UserFileDAO getUserFileDAO() {
		return userFileDAO;
	}
	public void setUserFileDAO(UserFileDAO userFileDAO) {
		this.userFileDAO = userFileDAO;
	}

	public boolean existsFile(Long projectId, String fileName) throws ServiceException {
		try {
			return fileDAO.exists(projectId,fileName);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public boolean existsProject(Long projectId) throws ServiceException {
		try {
			return projectDAO.exists(projectId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public Project createNewProject(String projectName, String ownerId) throws ServiceException {
		Project project = new Project();
		project.setProjectName(projectName);
		project.setOwnerID(ownerId);
		try {
			projectDAO.save(project);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return project;
	}

	public GlobalFile createNewGlobalFile(String name, String type) throws ServiceException {
		GlobalFile file = new GlobalFile();
		file.setName(name);
		file.setType(type);
		
		try {
			globalFileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException("Can not save global file.");
		}
		
		return file;
	}

	public UserFile createNewUserFile(String ownerId, String type) throws ServiceException {
		UserFile file = new UserFile();
		file.setOwnerID(ownerId);
		file.setType(type);
		
		try {
			userFileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException("Can not save user file.");
		}
		
		return file;
	}

	public void deleteFile(Long fileId) throws ServiceException {
		try {
			fileDAO.delete(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException("Can not delete file.");
		}
	}

	public void deleteGlobalFile(Long fileId) throws ServiceException {
		try {
			globalFileDAO.delete(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException("Can not delete global file.");
		}
	}

	public void deleteProject(Long projectId) throws ServiceException {
		try {
			projectDAO.delete(projectId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException("Can not delete project.");
		}
	}

	public void deleteUserFile(Long fileId) throws ServiceException {
		try {
			userFileDAO.delete(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException("Can not delete user file.");
		}
	}

	public boolean existsGlobalFile(Long fileId) throws ServiceException {
		try {
			return globalFileDAO.exists(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public boolean existsUserFile(Long fileId) throws ServiceException {
		try {
			return userFileDAO.exists(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<GlobalFile> findGlobalFilesByType(String type) throws ServiceException {
		List<GlobalFile> files = null;
		try {
			files = globalFileDAO.findByType(type);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return files;
	}

	public List<UserFile> findUserFilesByUser(String userId) throws ServiceException {
		List<UserFile> files = null;
		try {
			files = userFileDAO.findByUser(userId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return files;
	}

	public GlobalFile loadGlobalFile(Long fileId) throws ServiceException {
		try {
			return globalFileDAO.load(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public UserFile loadUserFile(Long fileId) throws ServiceException {
		try {
			return userFileDAO.load(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public void renameGlobalFile(Long fileId, String newName) throws ServiceException {
		try {
			GlobalFile file = globalFileDAO.load(fileId);
			file.setName(newName);
			globalFileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public void saveGlobalFile(Long fileId, String content) throws ServiceException {
		GlobalFile file = loadGlobalFile(fileId);
		file.setContent(content);
		try {
			globalFileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public void saveUserFile(Long fileId, String content) throws ServiceException {
		UserFile file = loadUserFile(fileId);
		file.setContent(content);
		try {
			userFileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}

	}

	public File findByName(Long projectId, String name) throws ServiceException {
		try {
			return fileDAO.findByName(projectId,name);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}
	
	public CircuitInterface extractCircuitInterface(File file){
		return new DefaultCircuitInterface("cicinc");
	}

	public List<File> extractDependencies(File file) throws ServiceException {
		Set<File> visitedFiles = new HashSet<File>();
		List<File> notYetAnalyzedFiles = new LinkedList<File>();
		notYetAnalyzedFiles.add(file);
		visitedFiles.add(file);
		while(!notYetAnalyzedFiles.isEmpty()) {
			File f = notYetAnalyzedFiles.remove(0);
			List<File> dependancies = extractDependenciesDisp(f);
			for(File dependancy : dependancies) {
				if(visitedFiles.contains(dependancy)) continue;
				notYetAnalyzedFiles.add(dependancy);
				visitedFiles.add(dependancy);
			}
		}
		return new ArrayList<File>(visitedFiles);
	}

	private List<File> extractDependenciesDisp(File file) throws ServiceException {
		IDependency depExtractor = null;
		if(file.getFileType().equals("FT_VHDLSOURCE")) {
			depExtractor = new VHDLDependencyExtractor();
		} else {
			throw new ServiceException("FileType "+file.getFileType()+" has no registered dependency extractors!");
		}
		return depExtractor.extractDependencies(file, this);
	}






}