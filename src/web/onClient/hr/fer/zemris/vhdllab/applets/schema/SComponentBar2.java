package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;

import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

@Deprecated
public class SComponentBar2 extends JToolBar {
	private boolean isDrawingIcons;

	public SComponentBar2() {
		super("Component Bar 2");
		isDrawingIcons = true;
		remanufactureComponents();
	}
	
	public void remanufactureComponents() {
		this.removeAll();
		Set<String> list = ComponentFactory.getAvailableComponents();
		ButtonGroup group = new ButtonGroup();
		for (String cmpName : list) {
			JToggleButton button = new JToggleButton(cmpName);
			if (isDrawingIcons) {
				button.setIcon(new SComponentBarIcon(cmpName));
			}
			group.add(button);
			this.add(button);
		}
		for (String cmpName : list) {
			JToggleButton button = new JToggleButton(cmpName);
			if (isDrawingIcons) {
				button.setIcon(new SComponentBarIcon(cmpName));
			}
			group.add(button);
			this.add(button);
		}
		for (String cmpName : list) {
			JToggleButton button = new JToggleButton(cmpName);
			if (isDrawingIcons) {
				button.setIcon(new SComponentBarIcon(cmpName));
			}
			group.add(button);
			this.add(button);
		}
		this.validate();
	}

	public boolean isDrawingIcons() {
		return isDrawingIcons;
	}

	public void setDrawingIcons(boolean isDrawingIcons) {
		this.isDrawingIcons = isDrawingIcons;
	}
	
	
}
