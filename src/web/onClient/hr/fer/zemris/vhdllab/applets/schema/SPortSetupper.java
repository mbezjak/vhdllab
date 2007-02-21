package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaMainPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Za uredivanje postavki portova.
 * @author Axel
 *
 */
public class SPortSetupper extends JPanel {
	private static final long serialVersionUID = -5760660013322232951L;

	private class ChangePortNameListener implements ActionListener {
		private JTextField tf;
		public ChangePortNameListener(JTextField t) {
			tf = t;
		}
		public void actionPerformed(ActionEvent ae) {
			port.setPortName(tf.getText());
			tf.setText(port.getPortName());
		}
	}
	
	private class ChangePortDirectionListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			JComboBox combo = (JComboBox) ae.getSource();
			int index = combo.getSelectedIndex();
			if (index == 0) {
				//System.out.println("Podesavam na nutra!");
				port.setPortDirection(SchemaModelledComponentPort.SMCDirection.IN);
			}
			if (index == 1) {
				//System.out.println("Podesavam na van!");
				port.setPortDirection(SchemaModelledComponentPort.SMCDirection.OUT);
			}
			chooseRightDirectionOnCombo();
		}
	}
	
	private class PortCardinalityChangeListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			NumField nf = (NumField) ae.getSource();
			Integer i = nf.getNumberFromField();
			try {
				i = Integer.parseInt(nf.getText());
			} catch (Exception e) {
			}
			if (i <= 0) {
				nf.setText(nf.getNumberFromField().toString());
				return;
			}
			nf.setNumberFromField(i);
			port.setPortCardinality(nf.getNumberFromField());
			//System.out.println("Sad cu da ga podesim. " + port.getPortCardinality());
			updateVectorList();
		}
	}
	
	private class NumField extends JTextField {
		private static final long serialVersionUID = 8609209839058151387L;
		private Integer valueBefore;
		public NumField(String startVal) {
			valueBefore = 0;
		}
		public Integer getNumberFromField() {
			return valueBefore;
		}
		public void setNumberFromField(Integer i) {
			this.setText(i.toString());
			valueBefore = i;
		}
	}
	
	private class ChangePortTypeListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			JComboBox combo = (JComboBox) ae.getSource();
			int index = combo.getSelectedIndex();
			if (index == 0) {
				port.setPortType(SchemaModelledComponentPort.SMCTip.std_logic);
			}
			if (index == 1) {
				//System.out.println("Vektore moj jedini...");
				port.setPortType(SchemaModelledComponentPort.SMCTip.std_logic_vector);
			}
			chooseRightPortTypeOnCombo();
			updatePortCardinalityField();
		}
	}
	
	@SuppressWarnings("unused")
	private class ConnectionListListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			int index = vectorList.getSelectedIndex();
			if (index < 0) return;
		}
	}
	
	private SchemaModelledComponentPort port;
	private SOptionBar parentBar;
	private JComboBox directionCombo;
	private JComboBox typeCombo;
	private NumField portcardfield;
	private DefaultListModel listModel;
	private SchemaMainPanel mainPanel;
	private JList vectorList;
	private JComboBox connectionCombo;
	
	public SPortSetupper(SchemaModelledComponentPort p, SchemaMainPanel panel) {
		this.port = p;
		this.listModel = new DefaultListModel();
		this.mainPanel = panel;
		
		initUI();
	}
	
	public void initUI() {
		this.removeAll();
		this.setLayout(new BorderLayout(2, 2));
		
		JPanel upperPanel = new JPanel();
		JPanel lowerPanel = new JPanel();
		
		upperPanel.setLayout(new GridLayout(4, 2, 5, 5));
		JLabel lab = new JLabel(" Ime porta: ");
		upperPanel.add(lab);
		JTextField tf = new JTextField(port.getPortName());
		tf.addActionListener(new ChangePortNameListener(tf));
		upperPanel.add(tf);
		
		lab = new JLabel(" Smjer porta: ");
		upperPanel.add(lab);
		directionCombo = new JComboBox();
		directionCombo.addItem("IN");
		directionCombo.addItem("OUT");
		directionCombo.addActionListener(new ChangePortDirectionListener());
		upperPanel.add(directionCombo);
		lab = new JLabel(" Tip porta: ");
		upperPanel.add(lab);
		typeCombo = new JComboBox();
		typeCombo.addItem("std_logic");
		typeCombo.addItem("std_logic_vector");
		typeCombo.addActionListener(new ChangePortTypeListener());
		upperPanel.add(typeCombo);
		lab = new JLabel(" Kardinalnost: ");
		upperPanel.add(lab);
		portcardfield = new NumField("");
		portcardfield.addActionListener(new PortCardinalityChangeListener());
		upperPanel.add(portcardfield);
		
		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout(2, 2));
		lowerPanel.setLayout(new BorderLayout(2, 2));
		lowerPanel.add(pan, BorderLayout.EAST);
		JButton butt = new JButton("Ok");
		connectionCombo = new JComboBox();
		connectionCombo.setPreferredSize(new Dimension(100, 20));
		pan.add(connectionCombo, BorderLayout.NORTH);
		pan.add(butt, BorderLayout.SOUTH);
		
		vectorList = new JList(listModel);
		JScrollPane scrpan = new JScrollPane(vectorList);
		pan = new JPanel();
		pan.setLayout(new BorderLayout(2, 2));
		pan.add(scrpan, BorderLayout.CENTER);
		lowerPanel.add(pan, BorderLayout.CENTER);
		
		this.add(upperPanel, BorderLayout.NORTH);
		this.add(lowerPanel, BorderLayout.CENTER);
		
		chooseRightDirectionOnCombo();
		chooseRightPortTypeOnCombo();
		updatePortCardinalityField();
		updateVectorList();
	}
	
	private void chooseRightDirectionOnCombo() {
		if (port.getPortDirection() == SchemaModelledComponentPort.SMCDirection.IN) directionCombo.setSelectedIndex(0);
		else directionCombo.setSelectedIndex(1);
	}
	
	private void chooseRightPortTypeOnCombo() {
		if (port.getPortType() == SchemaModelledComponentPort.SMCTip.std_logic) typeCombo.setSelectedIndex(0);
		else typeCombo.setSelectedIndex(1);
	}
	
	private void updatePortCardinalityField() {
		portcardfield.setNumberFromField(port.getPortCardinality());
		if (port.getPortType() == SchemaModelledComponentPort.SMCTip.std_logic) portcardfield.setEnabled(false);
		else portcardfield.setEnabled(true);
	}
	
	private void updateVectorList() {
		TreeMap<Integer, String> connlist = port.getConnectionList();
		Integer portCard = port.getPortCardinality();
		Integer oldListSize = connlist.size();
		
		if (portCard == oldListSize && (!listModel.isEmpty())) return;
		
		listModel.removeAllElements();
		
		TreeMap<Integer, String> updlist = new TreeMap<Integer, String>();
		
		
		for (Iterator iter = connlist.keySet().iterator(); iter.hasNext();) {
			Integer element = (Integer) iter.next();
            String value = (String)connlist.get(element);
            
            if (element < portCard && element >= 0) {
            	updlist.put(element, value);
            }
        }
		
		for (Integer i = oldListSize; i < portCard; i++) {
			updlist.put(i, null);
		}
		
		port.setConnectionList(updlist);
		
		//System.out.println("Stavljam: " + updlist);
		for (Iterator iter = updlist.keySet().iterator(); iter.hasNext();) {
			Integer element = (Integer) iter.next();
            String value = (String)updlist.get(element);
            
            listModel.addElement("Ulaz " + element + " : " + value);
        }
	}
}











