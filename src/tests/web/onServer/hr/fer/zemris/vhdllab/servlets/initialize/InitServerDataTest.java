package hr.fer.zemris.vhdllab.servlets.initialize;

import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.model.GlobalFile;
import hr.fer.zemris.vhdllab.model.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.manprovs.SampleManagerProvider;

import java.util.List;

/**
 * Tests {@link InitServerData}, but just to see if its
 * working. This is not an actual JUnit test.
 * 
 * @author Miro Bezjak
 */
public class InitServerDataTest {

	/**
	 * Start of test. Not a JUnit test! Just testing to see if its working.
	 * 
	 * @param args
	 *            no effect
	 */
	public static void main(String[] args) {
		ManagerProvider prov = new SampleManagerProvider();
		VHDLLabManager labman = (VHDLLabManager) prov.get(ManagerProvider.VHDL_LAB_MANAGER);
		InitServerData d = new InitServerData(labman);

		System.out.println("\n\n\n\n\n");
		
		d.initGlobalFiles();
		try {
			List<GlobalFile> files = labman.findGlobalFilesByType(FileTypes.FT_COMMON);
			System.out.println(files);

			files = labman.findGlobalFilesByType(FileTypes.FT_SYSTEM);
			System.out.println(files);

			files = labman.findGlobalFilesByType(FileTypes.FT_THEME);
			System.out.println(files);
			
			List<UserFile> userFiles = labman.findUserFilesByUser("uid:id-not-set");
			System.out.println(userFiles);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

}
