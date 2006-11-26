package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentPropertyList;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.AbstractComponentProperty;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PropertyPanel extends JPanel implements KeyListener {
	//Ptr<Object> pSklop;
	AbstractSchemaComponent sklop;
	
	public PropertyPanel(AbstractSchemaComponent sklop) {
		this.sklop = sklop;
		generatePanel(sklop.getPropertyList());
	}
	
	public void generatePanel(ComponentPropertyList cplist) {
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
