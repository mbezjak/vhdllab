package hr.fer.zemris.vhdllab.applets.schema;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.Set;

import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_AND;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_MUX2nNA1;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_NOT;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_OR;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_XOR;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;

public class SComponentBar extends JToolBar {
	class SCBToggleButton extends JToggleButton implements ActionListener {		
		private String cmpName;
		private SComponentBar parent;

		public SCBToggleButton(String arg0, SComponentBar cbar) {
			super(arg0);
			cmpName = arg0;
			parent = cbar;
			this.addActionListener(this);
		}

		@Override
		public String toString() {
			return cmpName;
		}

		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Mama, stisnuo me!");
			parent.setSelectedComponentName(this.getText());
		}
	}	
	
	private boolean isDrawingIcons;
	private JPanel cpanel;
	private JScrollPane scrpan;
	private ButtonGroup group;
	private String selCompName;

	public SComponentBar() {
		super("Component Bar");
		isDrawingIcons = true;
		selCompName = null;
		
		cpanel = new JPanel();
		cpanel.setLayout(new BoxLayout(cpanel, BoxLayout.X_AXIS));
		
		initComponents();
		remanufactureComponents();
		
		scrpan = new JScrollPane(cpanel);
		scrpan.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		
		//this.setPreferredSize(new Dimension(500, 78));
		this.add(scrpan);
	}
	
	public void initComponents() {
		new Sklop_OR("ORsklop");
		new Sklop_AND("ANDsklop");
		new Sklop_XOR("XORsklop");
		new Sklop_NOT("NOTsklop");
		new Sklop_MUX2nNA1("Mux2n_na_1");
	}
	
	public void remanufactureComponents() {
		cpanel.removeAll();
		Set<String> list = ComponentFactory.getAvailableComponents();
		group = new ButtonGroup();
		for (String cmpName : list) {
			SCBToggleButton button = new SCBToggleButton(cmpName, this);
			if (isDrawingIcons) {
				button.setIcon(new SComponentBarIcon(cmpName));
			}
			group.add(button);
			cpanel.add(button);
		}
		this.validate();
	}

	public boolean isDrawingIcons() {
		return isDrawingIcons;
	}

	public void setDrawingIcons(boolean isDrawingIcons) {
		this.isDrawingIcons = isDrawingIcons;
	}
	
	public String getSelectedComponentName() {
		return selCompName;
	}
	
	public void selectNone() {
		// ne znam zasto ali ovo ne radi!
		ButtonModel butt = group.getSelection();
		if (butt != null) {
			butt.setSelected(false);
		}
	}
	
	public void setSelectedComponentName(String cmpName) {
		System.out.println("Odabran(a) je " + cmpName);
		selCompName = cmpName;
	}
	
	
}
