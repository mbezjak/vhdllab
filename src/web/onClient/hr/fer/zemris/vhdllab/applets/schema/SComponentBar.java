package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactoryException;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_AND;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_MUX2nNA1;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_NOT;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_OR;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_XOR;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaMainPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class SComponentBar extends JToolBar {

	class SCBToggleButton extends JRadioButton implements ActionListener {		
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
			//System.out.println("Mama, stisnuo me!");
			if (this.getText().compareTo("(none)") == 0) {
				//System.out.println("Mama, iskljucio me!");
				parent.getParentToolbar().changeCursor(SchemaMainPanel.DEFAULT_CURSOR_TYPE);
				setSelectedComponentName(null);
				parent.getParentFrame().handleComponentSelected();
			}
			else {
				parent.setSelectedComponentName(this.getText());
				parent.getParentToolbar().changeCursor(SchemaMainPanel.CROSSHAIR_CURSOR_TYPE);
				parent.getParentFrame().handleComponentSelected();
			}
		}
	}
	
	private boolean isDrawingIcons;
	private JPanel cpanel;
	private JScrollPane scrpan;
	private ButtonGroup group;
	private String selCompName;
	private AbstractSchemaComponent selComp;
	private SCBToggleButton noneButt;
	private SchemaMainPanel parent;

	public SComponentBar(SchemaMainPanel mframe) {
		super("Component Bar");
		isDrawingIcons = true;
		selCompName = null;
		selComp = null;
		parent = mframe;
		
		initComponents();
		remanufactureAllComponents();
	}
	
	public void initComponents() {
		new Sklop_OR("ORsklop");
		new Sklop_AND("ANDsklop");
		new Sklop_XOR("XORsklop");
		new Sklop_NOT("NOTsklop");
		new Sklop_MUX2nNA1("Mux2n_na_1");
	}
	
	public void remanufactureComponents(ArrayList<String> cmplist) {
		this.removeAll();
		
		cpanel = new JPanel();
		cpanel.setLayout(new BoxLayout(cpanel, BoxLayout.X_AXIS));
		
		noneButt = new SCBToggleButton("(none)", this);
		noneButt.setIcon(new SComponentBarIcon("(none)"));

		group = new ButtonGroup();
		group.add(noneButt);
		cpanel.add(noneButt);
		
		for (String cmpName : cmplist) {
			SCBToggleButton button = new SCBToggleButton(cmpName, this);
			if (isDrawingIcons) {
				button.setIcon(new SComponentBarIcon(cmpName));
			}
			group.add(button);
			cpanel.add(button);
		}
		noneButt.setSelected(true);
		
		scrpan = new JScrollPane(cpanel);
		scrpan.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		
		this.add(scrpan);
		this.validate();
	}
	
	public void remanufactureAllComponents() {
		this.removeAll();
		
		cpanel = new JPanel();
		cpanel.setLayout(new BoxLayout(cpanel, BoxLayout.X_AXIS));
		
		noneButt = new SCBToggleButton("(none)", this);
		noneButt.setIcon(new SComponentBarIcon("(none)"));

		group = new ButtonGroup();
		group.add(noneButt);
		cpanel.add(noneButt);
		
		Set<String> list = ComponentFactory.getAvailableComponents();
		for (String cmpName : list) {
			SCBToggleButton button = new SCBToggleButton(cmpName, this);
			if (isDrawingIcons) {
				button.setIcon(new SComponentBarIcon(cmpName));
			}
			group.add(button);
			cpanel.add(button);
		}
		noneButt.setSelected(true);
		
		scrpan = new JScrollPane(cpanel);
		scrpan.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		
		this.add(scrpan);
		this.validate();
	}

	public boolean isDrawingIcons() {
		return isDrawingIcons;
	}

	public void setDrawingIcons(boolean isDrawingIcons) {
		this.isDrawingIcons = isDrawingIcons;
	}
	
	public SchemaMainPanel getParentToolbar() {
		return parent;
	}

	public String getSelectedComponentName() {
		return selCompName;
	}
	
	public AbstractSchemaComponent getSelectedComponent() {
		return selComp;
	}
	
	public void selectNone() {
		noneButt.setSelected(true);
		selCompName = null;
		parent.requestFocus();
	}
	
	public void setSelectedComponentName(String cmpName) {
		System.out.println("Odabran(a) je " + cmpName);
		selCompName = cmpName;
		if (cmpName == null) return;
		try {
			selComp = ComponentFactory.getSchemaComponent(cmpName);
		} catch (ComponentFactoryException e) {
			e.printStackTrace();
		}
	}
	
	public SchemaMainPanel getParentFrame() {
		return parent;
	}
}
