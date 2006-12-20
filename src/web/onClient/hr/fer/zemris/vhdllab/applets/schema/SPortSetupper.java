package hr.fer.zemris.vhdllab.applets.schema;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Za uredivanje postavki portova.
 * @author Axel
 *
 */
public class SPortSetupper extends JPanel {
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
	
	private SchemaModelledComponentPort port;
	private SOptionBar parentBar;
	
	public SPortSetupper(SchemaModelledComponentPort p, SOptionBar parent) {
		this.port = p;
		this.parentBar = parent;
		
		initUI();
	}
	
	public void initUI() {
		this.removeAll();
		this.setLayout(new BorderLayout());
		
		JPanel upperPanel = new JPanel();
		
		upperPanel.setLayout(new GridLayout(2, 2, 5, 5));
		JLabel lab = new JLabel(" Ime porta: ");
		upperPanel.add(lab);
		JTextField tf = new JTextField(port.getPortName());
		tf.addActionListener(new ChangePortNameListener(tf));
		upperPanel.add(tf);
		
		JPanel pan = new JPanel();
		upperPanel.add(pan);
		pan = new JPanel();
		upperPanel.add(pan);
		
		this.add(upperPanel, BorderLayout.NORTH);
	}
}
