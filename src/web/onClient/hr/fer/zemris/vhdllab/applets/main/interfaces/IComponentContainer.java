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

	void addComponent(String title, JComponent component, ComponentGroup group, ComponentPlacement placement);

	void removeComponent(JComponent component, ComponentPlacement placement);

	void setComponentTitle(JComponent component, ComponentPlacement placement,
			String title);

	String getComponentTitle(JComponent component, ComponentPlacement placement);

	void setComponentToolTipText(JComponent component, ComponentPlacement placement,
			String tooltip);

	void setSelectedComponent(JComponent component, ComponentPlacement placement);

	JComponent getSelectedComponent();

	JComponent getSelectedComponent(ComponentGroup group);

}