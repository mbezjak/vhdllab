package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaMainPanel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;

public class SEntitySetupper extends JPanel {
	private class ChangeEntityNameListener implements ActionListener {
		private JTextField tf;
		public ChangeEntityNameListener(JTextField t) {
			tf = t;
		}
		public void actionPerformed(ActionEvent ae) {
			entity.setEntityName(tf.getText());
			tf.setText(entity.getEntityName());
		}
	}
	
	private class AddPortListener implements ActionListener {
		private JTextField tf;
		public AddPortListener(JTextField t) {
			tf = t;
		}
		public void actionPerformed(ActionEvent ae) {
			ArrayList<SchemaModelledComponentPort> plist = entity.getPortList();
			SchemaModelledComponentPort port = new SchemaModelledComponentPort();
			String name = tf.getText();
			if (name.compareTo("") == 0) return;
			port.setPortName(name);
			//port.setPortName((name.compareTo("") == 0) ? "port" : name); druga varijanta
			plist.add(port);
			correctPortNames();
			tf.setText("");
		}
	}
	
	private class ModifyPortListener implements ActionListener {
		private JList list;
		public ModifyPortListener(JList l) {
			list = l;
		}
		public void actionPerformed(ActionEvent ae) {
			int index = list.getSelectedIndex();
			if (index < 0) return;
			ArrayList<SchemaModelledComponentPort> plist = entity.getPortList();
			showPortSetupper(plist.get(index));
		}
	}
	
	private SchemaMainPanel parentPanel;
	private SchemaModelledComponentEntity entity;
	private JPanel upperPanel;
	private JPanel lowerPanel;
	private DefaultListModel listModel;
	private JScrollPane scrpan;
	private JList list;
	private int nameCounter;
	private JTextField portNameField;
	
	public SEntitySetupper(SchemaModelledComponentEntity ent, SchemaMainPanel panel) {
		this.entity = ent;
		this.parentPanel = panel;
		this.nameCounter = 0;
		
		initUI();
	}
	
	public void initUI() {
		this.removeAll();
		
		this.setLayout(new BorderLayout(2, 2));
		
		upperPanel = new JPanel();
		lowerPanel = new JPanel();
		
		upperPanel.setLayout(new GridLayout(2, 2, 5, 5));
		lowerPanel.setLayout(new BorderLayout(2, 2));
		
		JLabel lab = new JLabel(" Ime sklopa: ");
		upperPanel.add(lab);
		JTextField tf = new JTextField(entity.getEntityName());
		tf.addActionListener(new ChangeEntityNameListener(tf));
		upperPanel.add(tf);
		
		JPanel pan = new JPanel();
		upperPanel.add(pan);
		pan = new JPanel();
		upperPanel.add(pan);
		
		pan = new JPanel();
		pan.setLayout(new BorderLayout(2, 2));
		portNameField = new JTextField();
		pan.add(portNameField, BorderLayout.CENTER);
		JButton butt = new JButton("Dodaj port");
		butt.addActionListener(new AddPortListener(portNameField));
		lowerPanel.add(pan, BorderLayout.NORTH);
		pan.add(butt, BorderLayout.EAST);
		
		listModel = new DefaultListModel();
		
		refreshListModel();
		list = new JList(listModel);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		scrpan = new JScrollPane(list);
		scrpan.setPreferredSize(new Dimension(200, 125));
		lowerPanel.add(scrpan, BorderLayout.CENTER);
		
		pan = new JPanel();
		JPanel pan2 = new JPanel();
		pan2.setLayout(new BorderLayout(2, 2));
		pan2.setPreferredSize(new Dimension(100, 20));
		lowerPanel.add(pan2, BorderLayout.EAST);
		pan2.add(pan, BorderLayout.NORTH);
		pan.setLayout(new GridLayout(1, 1, 2, 2));
		//butt = new JButton("Podesi...");
		//butt.addActionListener(new ModifyPortListener(list));
		//pan.add(butt);
		butt = new JButton("Obrisi");
		butt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteSelectedPorts();
			}
		});
		pan.add(butt);
		pan = new JPanel();
		pan.setLayout(new GridLayout(1, 1, 2, 2));
		pan2.add(pan, BorderLayout.SOUTH);
		butt = new JButton("Ok");
		pan.add(butt);
		
		
		this.add(upperPanel, BorderLayout.NORTH);
		this.add(lowerPanel, BorderLayout.CENTER);
	}
	
	private void refreshListModel() {
		listModel.removeAllElements();
		ArrayList<SchemaModelledComponentPort> plist = entity.getPortList();
		for (SchemaModelledComponentPort port : plist) {
			listModel.insertElementAt(port.getPortName() + " (" + port.getPortDirection() + ", " + port.getPortType()
					+ ((port.getPortType() == SchemaModelledComponentPort.SMCTip.std_logic_vector) ? 
							("[" + port.getPortCardinality() + "])") : (")"))
					, listModel.size());
		}
	}
	
	private void correctPortNames() {
		HashSet<String> foundNames = new HashSet<String>();
		ArrayList<SchemaModelledComponentPort> plist = entity.getPortList();
		for (SchemaModelledComponentPort port : plist) {
			String s = port.getPortName();
			if (foundNames.contains(s)) {
				s = regenerateName(s);
				port.setPortName(s);
			}
			foundNames.add(s);
		}
		refreshListModel();
	}
	
	private String regenerateName(String s) {
		if (s.length() > 4) {
			s = s.substring(0, s.length() - 2) + entity.getNameCounter();
		} else {
			s = "port" + entity.getNameCounter();
		}
		return s;
	}
	
	private void deleteSelectedPorts() {
		int [] indices = list.getSelectedIndices();
		for (int j = indices.length - 1; j >= 0; j--) {			
			deletePort(indices[j]);
		}
	}
	
	private void deletePort(int index) {
		if (index < 0) return;
		ArrayList<SchemaModelledComponentPort> plist = entity.getPortList();
		plist.remove(index);
		refreshListModel();
	}
	
	private void showPortSetupper(SchemaModelledComponentPort p) {
		Frame frame = JOptionPane.getFrameForComponent(this);
		JDialog dialogPortSetup = new JDialog(frame, "Port Setup", true);
		dialogPortSetup.add(new SPortSetupper(p, parentPanel));
		dialogPortSetup.setBounds(0, 0, 320, 250);
		dialogPortSetup.setLocation(150, 150);
		dialogPortSetup.setVisible(true);
		refreshListModel();
	}
}







