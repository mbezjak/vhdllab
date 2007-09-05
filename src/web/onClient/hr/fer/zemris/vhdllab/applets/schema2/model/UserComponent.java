package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ComponentWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PortWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedComponent;
import hr.fer.zemris.vhdllab.applets.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EComponentType;
import hr.fer.zemris.vhdllab.applets.schema2.model.drawers.DefaultComponentDrawer;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.util.List;




/**
 * Predstavlja user defined komponentu na shemi.
 * 
 * @author Axel
 *
 */
public class UserComponent extends DefaultSchemaComponent {

	/* static fields */
	

	/* private fields */
	
	
	

	/* ctors */
	
	public UserComponent(CircuitInterface cint) {
		super(preparePredefinedWrapper(cint));
	}
	
	/**
	 * Deserijalizacija.
	 * 
	 * @param wrapper
	 */
	public UserComponent(ComponentWrapper wrapper) {
		super(wrapper);
	}
	
	
	
	
	

	/* methods */	

	
	@Override
	public EComponentType getComponentType() {
		return EComponentType.USER_DEFINED;
	}
	
	private static PredefinedComponent preparePredefinedWrapper(CircuitInterface cint) {
		PredefinedComponent predef = new PredefinedComponent();
		String name = cint.getEntityName();
		
		// init basic fields
		predef.setComponentName(name);
		predef.setCodeFileName(name);
		predef.setCategoryName(Constants.USER_CATEGORY_NAME);
		predef.setPreferredName(name + Constants.PREFERRED_NAME_SUFIX);
		predef.setDrawerName(DefaultComponentDrawer.class.getName());
		predef.setGenericComponent(false);
		
		// init parameters
		
		// init ports - put IN on WEST, and OUT ports on EAST
		List<Port> ports = cint.getPorts();
		for (Port p : ports) {
			PortWrapper wrapper = new PortWrapper(p, null);
			if (p.getDirection().isIN()) {
				wrapper.setOrientation(PortWrapper.ORIENTATION_WEST);
			} else if (p.getDirection().isOUT()) {
				wrapper.setOrientation(PortWrapper.ORIENTATION_EAST);
			} else {
				throw new IllegalArgumentException("Direction '" + p.getDirection() + "' not implemented.");
			}
			predef.addPort(wrapper);
		}
		
		return predef;
	}

}


















