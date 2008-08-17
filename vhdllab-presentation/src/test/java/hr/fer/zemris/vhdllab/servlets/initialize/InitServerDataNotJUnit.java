package hr.fer.zemris.vhdllab.servlets.initialize;

import hr.fer.zemris.vhdllab.dao.impl.EntityManagerUtil;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceContainer;
import hr.fer.zemris.vhdllab.service.ServiceException;

import java.util.List;

/**
 * Tests {@link InitServerData}, but just to see if its
 * working. This is not an actual JUnit test.
 * 
 * @author Miro Bezjak
 */
public class InitServerDataNotJUnit {

	/**
	 * Start of test. Not a JUnit test! Just testing to see if its working.
	 * 
	 * @param args
	 *            no effect
	 */
	public static void main(String[] args) {
	    ServiceContainer container = ServiceContainer.instance();
		InitServerData d = new InitServerData(container);

		System.out.println("\n\n\n\n\n");
		
		EntityManagerUtil.createEntityManagerFactory();
		EntityManagerUtil.currentEntityManager();
		try {
		    d.initGlobalFiles();
		    Library lib = container.getLibraryManager().findByName("preferences");
			System.out.println(lib);
			
			List<UserFile> userFiles = container.getUserFileManager().findByUser("uid:id-not-set");
			for (UserFile uf : userFiles) {
			    System.out.println(uf);
            }
		} catch (ServiceException e) {
			e.printStackTrace();
		}
        EntityManagerUtil.closeEntityManager();
	}

}
