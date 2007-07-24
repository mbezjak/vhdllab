package hr.fer.zemris.vhdllab.servlets.initialize;

import hr.fer.zemris.vhdllab.model.GlobalFile;
import hr.fer.zemris.vhdllab.preferences.global.Preferences;
import hr.fer.zemris.vhdllab.preferences.global.Property;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.initialize.preferencesFiles.PreferencesInitializer;

/**
 * Initializes data. For example: writes all global files.
 * @author Miro Bezjak
 */
public class InitServerData {

	private VHDLLabManager labman;

	/**
	 * Constructor.
	 * @param mprov a manager provider
	 */
	public InitServerData(VHDLLabManager labman) {
		if(labman == null) {
			throw new NullPointerException("VHDLLabManager can not be null.");
		}
		this.labman = labman;
	}

	/**
	 * Initializes all global files.
	 */
	public void initGlobalFiles() {
		PreferencesInitializer parser = PreferencesInitializer.instance();
		Preferences preferences = parser.getPreferences();
		for(Property p : preferences.getProperties()) {
			try {
				if(!labman.existsGlobalFile(p.getId())) {
					GlobalFile file = labman.createNewGlobalFile(p.getId(), p.getType());
					labman.saveGlobalFile(file.getId(), parser.getSource(p.getId()));
				}
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
	}
	
}
