package hr.fer.zemris.vhdllab.servlets.initialize;

import hr.fer.zemris.vhdllab.dao.impl.EntityManagerUtil;
import hr.fer.zemris.vhdllab.entities.Caseless;
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
	    if(!libman.exists(new Caseless("errors"))) {
	        libman.save(new Library(new Caseless("errors")));
	    }
	    Library lib = new Library(new Caseless("preferences"));
        if(!libman.exists(new Caseless("preferences"))) {
            libman.save(lib);
            PreferencesInitializer parser = PreferencesInitializer.instance();
            Preferences preferences = parser.getPreferences();
            for(Property p : preferences.getProperties()) {
                try {
                    if(!libfileman.exists(lib.getId(), new Caseless(p.getId()))) {
                        LibraryFile file = new LibraryFile(new Caseless(p.getId()), parser.getSource(p.getId()));
                        lib.addFile(file);
                        libfileman.save(file);
                    }
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
            }
        }
	    
		
		lib = new Library(new Caseless("predefined"));
		if(!libman.exists(new Caseless("predefined"))) {
		    libman.save(lib);
	        PredefinedInitializer predefined = PredefinedInitializer.instance();
	        for (FileAndContent fc : predefined.getSources()) {
	            try {
	                if(!libfileman.exists(lib.getId(), fc.getFileName())) {
	                    LibraryFile file = new LibraryFile(fc.getFileName(), fc.getContent());
	                    lib.addFile(file);
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
