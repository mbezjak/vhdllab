package hr.fer.zemris.vhdllab.applets.schema;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

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
			port.setPortName((name.compareTo("") == 0) ? "port" : name);
			plist.add(port);
			correctPortNames();
		}
	}
	
	private SchemaModelledComponentEntity entity;
	private SOptionBar parentBar;
	private JPanel upperPanel;
	private JPanel lowerPanel;
	private DefaultListModel listModel;
	private JScrollPane scrpan;
	private JList list;
	private int nameCounter;
	private JTextField portNameField;
	
	public SEntitySetupper(SchemaModelledComponentEntity ent, SOptionBar parent) {
		this.entity = ent;
		this.parentBar = parent;
		nameCounter = 0;
		
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
		lowerPanel.add(pan2, BorderLayout.EAST);
		pan2.add(pan, BorderLayout.SOUTH);
		pan.setLayout(new GridLayout(2, 1, 2, 2));
		butt = new JButton("Podesi...");
		pan.add(butt);
		butt = new JButton("Obrisi");
		butt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteSelectedPorts();
			}
		});
		pan.add(butt);
		
		this.add(upperPanel, BorderLayout.NORTH);
		this.add(lowerPanel, BorderLayout.CENTER);
	}
	
	private void refreshListModel() {
		listModel.removeAllElements();
		ArrayList<SchemaModelledComponentPort> plist = entity.getPortList();
		for (SchemaModelledComponentPort port : plist) {
			listModel.insertElementAt(port.getPortName(), listModel.size());
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
			deletePort(j);
		}
	}
	
	private void deletePort(int index) {
		if (index < 0) return;
		ArrayList<SchemaModelledComponentPort> plist = entity.getPortList();
		plist.remove(index);
		refreshListModel();
	}
}







