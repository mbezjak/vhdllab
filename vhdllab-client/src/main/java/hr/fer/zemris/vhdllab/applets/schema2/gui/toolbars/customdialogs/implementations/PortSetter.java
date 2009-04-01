package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.customdialogs.implementations;

import hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.generic.ParamPort;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.customdialogs.ParameterSetterPanel;
import hr.fer.zemris.vhdllab.service.ci.Port;
import hr.fer.zemris.vhdllab.service.ci.PortDirection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;




public class PortSetter extends ParameterSetterPanel<ParamPort> {
	
	/* static fields */
	private static final long serialVersionUID = 1L;

	
	/* private fields */
	private JRadioButton std_logic_vectorRadio;
	private JRadioButton std_logicRadio;
	private JRadioButton outRadio;
	private JRadioButton inRadio;
	private JTextField rangeToField;
	private JComboBox vectorDirectionCombo;
	private JTextField rangeFromField;
	private ButtonGroup direction_group = new ButtonGroup();
	private JTextField nameField;
	private ButtonGroup type_group = new ButtonGroup();

	
	
	/* ctors */

	public PortSetter() {
		super();
		initGUI();
		selectStdLogic();
	}
	
	
	
	/* methods */
	
	@Override
	public void setToValue(ParamPort value) {
		Port p = value.getPort();
		nameField.setText(p.getName());
		if (p.isIN()) inRadio.setSelected(true);
		else outRadio.setSelected(true);
		if (p.isScalar()) {
			std_logicRadio.setSelected(true);
			selectStdLogic();
		} else {
			std_logic_vectorRadio.setSelected(true);
			selectStdLogicVector();
			if (p.isTO()) vectorDirectionCombo.setSelectedIndex(1);
			else vectorDirectionCombo.setSelectedIndex(0);
			rangeFromField.setText(String.valueOf(p.getFrom()));
			rangeToField.setText(String.valueOf(p.getTo()));
		}
	}

	@Override
	public ParamPort getNewValue() {
	    PortDirection dir = (inRadio.isSelected()) ? (PortDirection.IN) : (PortDirection.OUT);
        Port p = new Port(nameField.getText(), dir);
		if (std_logic_vectorRadio.isSelected()) {
			int[] range = new int[] {Integer.parseInt(rangeFromField.getText()),
					Integer.parseInt(rangeToField.getText())};
            p.setFrom(range[0]);
            p.setTo(range[1]);
		}
		return new ParamPort(p);
	}
	
	@Override
	public Class<ParamPort> isSettingValueFor() {
		return ParamPort.class;
	}
	
	protected void selectStdLogic() {
		rangeFromField.setEnabled(false);
		rangeToField.setEnabled(false);
		vectorDirectionCombo.setEnabled(false);
	}
	
	protected void selectStdLogicVector() {
		rangeFromField.setEnabled(true);
		rangeToField.setEnabled(true);
		vectorDirectionCombo.setEnabled(true);
	}
	
	private void initGUI() {
		final BorderLayout borderLayout = new BorderLayout();
		borderLayout.setVgap(4);
		borderLayout.setHgap(4);
		setLayout(borderLayout);

		final JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 0));
		add(panel, BorderLayout.WEST);

		std_logicRadio = new JRadioButton();
		std_logicRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectStdLogic();
			}
		});
		std_logicRadio.setSelected(true);
		type_group.add(std_logicRadio);
		std_logicRadio.setText("std_logic");
		panel.add(std_logicRadio);

		std_logic_vectorRadio = new JRadioButton();
		std_logic_vectorRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectStdLogicVector();
			}
		});
		type_group.add(std_logic_vectorRadio);
		std_logic_vectorRadio.setText("std_logic_vector");
		panel.add(std_logic_vectorRadio);

		final JPanel panel_1 = new JPanel();
		panel_1.setLayout(new GridLayout(2, 0));
		add(panel_1, BorderLayout.CENTER);

		final JPanel panel_2 = new JPanel();
		panel_2.setLayout(new BorderLayout());
		panel_1.add(panel_2);

		final JPanel panel_3 = new JPanel();
		panel_3.setLayout(new BorderLayout());
		panel_1.add(panel_3);

		final JPanel panel_4 = new JPanel();
		panel_4.setLayout(new GridBagLayout());
		panel_3.add(panel_4, BorderLayout.WEST);

		rangeFromField = new JTextField();
		rangeFromField.setText("0");
		rangeFromField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rto = Integer.parseInt(rangeToField.getText());
				int rfrom;
				try {
					rfrom = Integer.parseInt(rangeFromField.getText());
				} catch (NumberFormatException nfe) {
					rangeFromField.setText(String.valueOf(rto));
					return;
				}
				int isto = vectorDirectionCombo.getSelectedIndex();
				
				if (isto == 1) {
					if (rfrom > rto) rangeFromField.setText(String.valueOf(rto));
				} else {
					if (rfrom < rto) rangeFromField.setText(String.valueOf(rto));
				}
			}
		});
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(0, 0, 0, 5);
		gridBagConstraints.ipadx = 15;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		panel_4.add(rangeFromField, gridBagConstraints);

		vectorDirectionCombo = new JComboBox();
		vectorDirectionCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rfrom = Integer.parseInt(rangeFromField.getText());
				int rto = Integer.parseInt(rangeToField.getText());
				int isto = vectorDirectionCombo.getSelectedIndex();
				
				if (isto == 1) {
					if (rfrom > rto) rangeFromField.setText(String.valueOf(rto));
				} else {
					if (rfrom < rto) rangeFromField.setText(String.valueOf(rto));
				}
			}
		});
		vectorDirectionCombo.setModel(new DefaultComboBoxModel(new String[] {"downto", "to"}));
		final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
		gridBagConstraints_1.insets = new Insets(0, 0, 0, 5);
		gridBagConstraints_1.gridy = 0;
		gridBagConstraints_1.gridx = 1;
		panel_4.add(vectorDirectionCombo, gridBagConstraints_1);

		rangeToField = new JTextField();
		rangeToField.setText("0");
		rangeToField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rfrom = Integer.parseInt(rangeFromField.getText());
				int rto;
				try {
					rto = Integer.parseInt(rangeToField.getText());
				} catch (NumberFormatException nfe) {
					rangeToField.setText(String.valueOf(rfrom));
					return;
				}
				int isto = vectorDirectionCombo.getSelectedIndex();
				
				if (isto == 1) {
					if (rfrom > rto) rangeToField.setText(String.valueOf(rfrom));
				} else {
					if (rfrom < rto) rangeToField.setText(String.valueOf(rfrom));
				}
			}
		});
		final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
		gridBagConstraints_2.ipadx = 15;
		gridBagConstraints_2.gridy = 0;
		gridBagConstraints_2.gridx = 2;
		panel_4.add(rangeToField, gridBagConstraints_2);

		final JPanel north_panel = new JPanel();
		north_panel.setLayout(new BorderLayout());
		add(north_panel, BorderLayout.NORTH);

		final JPanel panel_8 = new JPanel();
		north_panel.add(panel_8, BorderLayout.WEST);
		panel_8.setLayout(new GridLayout(2, 0));

		final JLabel portNameLabel = new JLabel();
		panel_8.add(portNameLabel);
		portNameLabel.setText(" Port name ");

		final JLabel directionLabel = new JLabel();
		panel_8.add(directionLabel);
		directionLabel.setText(" Direction ");

		final JPanel panel_9 = new JPanel();
		north_panel.add(panel_9);
		final GridLayout gridLayout = new GridLayout(2, 0);
		gridLayout.setVgap(1);
		gridLayout.setHgap(1);
		panel_9.setLayout(gridLayout);

		nameField = new JTextField();
		panel_9.add(nameField);

		final JPanel panel_10 = new JPanel();
		final FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_10.setLayout(flowLayout);
		panel_9.add(panel_10);

		inRadio = new JRadioButton();
		inRadio.setSelected(true);
		direction_group.add(inRadio);
		panel_10.add(inRadio);
		inRadio.setText("IN");

		outRadio = new JRadioButton();
		direction_group.add(outRadio);
		panel_10.add(outRadio);
		outRadio.setText("OUT");
	}

}














