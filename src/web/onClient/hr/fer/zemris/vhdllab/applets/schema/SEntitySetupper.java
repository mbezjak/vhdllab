package hr.fer.zemris.vhdllab.applets.schema;

import java.util.ArrayList;
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
	
	private SchemaModelledComponentEntity entity;
	private SOptionBar parentBar;
	private JPanel upperPanel;
	private JPanel lowerPanel;
	private DefaultListModel listModel;
	
	public SEntitySetupper(SchemaModelledComponentEntity ent, SOptionBar parent) {
		this.entity = ent;
		this.parentBar = parent;
		
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
		JButton butt = new JButton("Dodaj port");
		butt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ArrayList<SchemaModelledComponentPort> plist = entity.getPortList();
				plist.add(new SchemaModelledComponentPort());
			}
		});
		lowerPanel.add(pan, BorderLayout.NORTH);
		pan.add(butt, BorderLayout.EAST);
		tf = new JTextField();
		pan.add(tf, BorderLayout.CENTER);
		
		listModel = new DefaultListModel();
//		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		ArrayList<SchemaModelledComponentPort> plist = entity.getPortList();
		for (SchemaModelledComponentPort port : plist) {
			listModel.insertElementAt(port.getPortName(), listModel.size());
		}
		JList list = new JList();
		JScrollPane scrpan = new JScrollPane(list);
		lowerPanel.add(scrpan, BorderLayout.CENTER);
		
		pan = new JPanel();
		JPanel pan2 = new JPanel();
		lowerPanel.add(pan2, BorderLayout.EAST);
		pan2.add(pan, BorderLayout.SOUTH);
		pan.setLayout(new GridLayout(2, 1, 2, 2));
		butt = new JButton("Podesi...");
		pan.add(butt);
		butt = new JButton("Obrisi");
		pan.add(butt);
		
		this.add(upperPanel, BorderLayout.NORTH);
		this.add(lowerPanel, BorderLayout.CENTER);
	}
	
	private void checkPortNames() {
	}
}







