package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentPropertyList;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.AbstractComponentProperty;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicBorders;

public class SPropertyPanel extends JPanel implements KeyListener {
	//Ptr<Object> pSklop;
	AbstractSchemaComponent sklop;
	
	public SPropertyPanel(AbstractSchemaComponent sklop) {
		this.setMinimumSize(new Dimension(200, 150));
		this.setPreferredSize(new Dimension(200, 150));
		this.setBorder(BasicBorders.getInternalFrameBorder());
		this.sklop = sklop;
		if (sklop != null) generatePanel(sklop.getPropertyList());
	}
	
	public void setLinkToComponent(AbstractSchemaComponent sklop) {
		this.sklop = sklop;
		this.removeAll();
		if (sklop != null) generatePanel(sklop.getPropertyList());
		else System.out.println("Nisi kliknuo ni na kakvu komponentu.");
	}
	
	private void generatePanel(ComponentPropertyList cplist) {
		this.removeAll();
		if (cplist != null) {
			int i = cplist.size();
			if (i <= 0) i = 1;
			GridLayout grlay = new GridLayout(i, 2);
			this.setLayout(grlay);
			JLabel lab = null;
			Component comp = null;
			for (AbstractComponentProperty cprop : cplist) {
				lab = new JLabel(cprop.getPropertyName());
				this.add(lab);
				comp = cprop.getPropField().getComponent();
				comp.addKeyListener(this);
				this.add(comp);
			}
		}
		this.validate();
	}

	public void keyTyped(KeyEvent arg0) {
		if (arg0.getKeyChar() == arg0.VK_ENTER)
			generatePanel(sklop.getPropertyList());
	}

	public void keyPressed(KeyEvent arg0) {
	}

	public void keyReleased(KeyEvent arg0) {
	}
}
