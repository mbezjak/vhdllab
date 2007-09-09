package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.customdialogs;

import javax.swing.JPanel;


/**
 * Abstract class for the parameter panel.
 * Defines a method that returns a new value set by the panel that
 * should be opened inside a dialog.
 * This is meant to be used within the properties toolbar.
 * Any class inheriting this abstract class must provide
 * a default constructor.
 * 
 * @author Axel
 *
 * @param <T>
 */
public abstract class ParameterSetterPanel<T> extends JPanel {
	
	/* static fields */

	
	/* private fields */

	
	
	/* ctors */

	
	
	
	/* methods */

	public abstract T getNewValue();
	
	public abstract Class<T> isSettingValueFor();
	
}














