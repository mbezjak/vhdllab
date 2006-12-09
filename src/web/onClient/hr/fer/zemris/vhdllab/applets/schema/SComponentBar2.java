package hr.fer.zemris.vhdllab.applets.schema;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Set;

import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

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
