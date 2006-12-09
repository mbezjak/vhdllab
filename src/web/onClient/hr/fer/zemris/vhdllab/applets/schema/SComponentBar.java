package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_AND;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_MUX2nNA1;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_NOT;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_OR;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_XOR;

import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;

public class SComponentBar extends JToolBar {
	private boolean isDrawingIcons;
	private JPanel cpanel;
	private JScrollPane scrpan;

	public SComponentBar() {
		super("Component Bar");
		isDrawingIcons = true;
		
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
		ButtonGroup group = new ButtonGroup();
		for (String cmpName : list) {
			JToggleButton button = new JToggleButton(cmpName);
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
	
	
}
