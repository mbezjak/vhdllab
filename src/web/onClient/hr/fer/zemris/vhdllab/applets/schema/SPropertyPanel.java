package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentPropertyList;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.AbstractComponentProperty;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicBorders;

public class SPropertyPanel extends JPanel implements KeyListener {
	private static final long serialVersionUID = -8523367122520795207L;
	//Ptr<Object> pSklop;
	private AbstractSchemaComponent selectedCmp;
	private String selectedCmpName;
	private SPropertyBar parentBar;
	
	public SPropertyPanel(AbstractSchemaComponent sklop, SPropertyBar parent) {
		this.setPreferredSize(new Dimension(200, 150));
		this.setBorder(BasicBorders.getInternalFrameBorder());
		this.selectedCmp = sklop;
		if (sklop != null) generatePanel(sklop.getPropertyList());
		parentBar = parent;
	}
	
	public void setLinkToComponent(AbstractSchemaComponent sklop) {
		this.selectedCmp = sklop;
		this.removeAll();
		if (sklop != null) {
			this.selectedCmpName = sklop.getComponentInstanceName();
			generatePanel(sklop.getPropertyList());
		}
		else System.out.println("Nisi kliknuo ni na kakvu komponentu.");
	}
	
	private void generatePanel(ComponentPropertyList cplist) {
		this.removeAll();
		if (cplist != null) {
			int i = cplist.size();
			if (i <= 0) i = 1;
			GridLayout grlay = new GridLayout(i, 2);
			this.setLayout(grlay);
			//this.setPreferredSize(new Dimension(250, grlay.getRows() * 45));
			//this.setMinimumSize(new Dimension(250, grlay.getRows() * 45));
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
		this.repaint();
	}

	public void keyTyped(KeyEvent arg0) {		
		if (arg0.getKeyChar() == KeyEvent.VK_ENTER) {
			generatePanel(selectedCmp.getPropertyList());
			parentBar.handlePropertyUpdate();
		}
		// ako se ime promijenilo
		if (selectedCmpName.compareTo(selectedCmp.getComponentInstanceName()) != 0) {
			parentBar.handleNameUpdate(selectedCmpName, selectedCmp.getComponentInstanceName());
			selectedCmpName = selectedCmp.getComponentInstanceName();
		}
	}

	public void keyPressed(KeyEvent arg0) {
	}

	public void keyReleased(KeyEvent arg0) {
	}
}
