package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.ComponentPropertyList;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.AbstractComponentProperty;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PropertyPanel extends JPanel {
	public PropertyPanel(ComponentPropertyList cplist) {
		if (cplist != null) {
			int i = cplist.size();
			if (i <= 0) i = 1;
			GridLayout grlay = new GridLayout(i, 2);
			this.setLayout(grlay);
			JLabel lab = null;
			for (AbstractComponentProperty cprop : cplist) {
				lab = new JLabel(cprop.getPropertyName());
				this.add(lab);
				this.add(cprop.getPropField().getComponent());
			}
		}
	}
}
