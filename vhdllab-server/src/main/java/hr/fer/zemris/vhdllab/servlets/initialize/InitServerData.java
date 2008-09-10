package hr.fer.zemris.vhdllab.servlets.initialize;

import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.dao.impl.EntityManagerUtil;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.preferences.global.Preferences;
import hr.fer.zemris.vhdllab.preferences.global.Property;
import hr.fer.zemris.vhdllab.service.LibraryFileManager;
import hr.fer.zemris.vhdllab.service.LibraryManager;
import hr.fer.zemris.vhdllab.service.ServiceContainer;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.initialize.predefinedFiles.PredefinedInitializer;
import hr.fer.zemris.vhdllab.servlets.initialize.predefinedFiles.PredefinedInitializer.FileAndContent;
import hr.fer.zemris.vhdllab.servlets.initialize.preferencesFiles.PreferencesInitializer;

/**
 * Initializes data. For example: writes all global files.
 * @author Miro Bezjak
 */
public class InitServerData {

	private ServiceContainer container;

	/**
	 * Constructor.
	 * @param mprov a manager provider
	 */
	public InitServerData(ServiceContainer container) {
		if(container == null) {
			throw new NullPointerException("VHDLLabManager can not be null.");
		}
		this.container = container;
	}

	/**
	 * Initializes all global files and predefined files.
	 * @throws ServiceException 
	 */
	public void initFiles() throws ServiceException {
	    LibraryManager libman = container.getLibraryManager();
	    LibraryFileManager libfileman = container.getLibraryFileManager();
	    
	    EntityManagerUtil.currentEntityManager();
	    if(!libman.exists("errors")) {
	        libman.save(new Library("errors"));
	    }
	    Library lib = new Library("preferences");
        if(!libman.exists("preferences")) {
            libman.save(lib);
            PreferencesInitializer parser = PreferencesInitializer.instance();
            Preferences preferences = parser.getPreferences();
            for(Property p : preferences.getProperties()) {
                try {
                    if(!libfileman.exists(lib.getId(), p.getId())) {
                        LibraryFile file = new LibraryFile(lib, p.getId(), p.getType(), parser.getSource(p.getId()));
                        libfileman.save(file);
                    }
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
            }
        }
	    
		
		lib = new Library("predefined");
		if(!libman.exists("predefined")) {
		    libman.save(lib);
	        PredefinedInitializer predefined = PredefinedInitializer.instance();
	        for (FileAndContent fc : predefined.getSources()) {
	            try {
	                if(!libfileman.exists(lib.getId(), fc.getFileName())) {
	                    LibraryFile file = new LibraryFile(lib, fc.getFileName(), FileTypes.VHDL_PREDEFINED, fc.getContent());
	                    libfileman.save(file);
	                }
	            } catch (ServiceException e) {
	                e.printStackTrace();
	            }
	        }
		}
        
        EntityManagerUtil.closeEntityManager();
	}
	
}
