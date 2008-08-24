package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.ComponentGroup;
import hr.fer.zemris.vhdllab.applets.main.ComponentPlacement;

import javax.swing.JComponent;

/**
 * @author Miro Bezjak
 * @see IEditorStorage
 * @see IViewStorage
 * @see IProjectExplorerStorage
 * @see IComponentStorage
 */
public interface IComponentContainer {

	void addComponent(String title, JComponent component, ComponentGroup group,
			ComponentPlacement placement);

	void removeComponent(JComponent component);
	
	boolean containsComponent(JComponent component);
	
	ComponentPlacement getComponentPlacement(JComponent component);

	ComponentGroup getComponentGroup(JComponent component);
	
	void setComponentTitle(JComponent component, String title);

	String getComponentTitle(JComponent component);

	void setComponentToolTipText(JComponent component, String tooltip);

	void setSelectedComponent(JComponent component);

	JComponent getSelectedComponent();

	JComponent getSelectedComponent(ComponentGroup group);

}