package hr.fer.zemris.vhdllab.applets.editor.preferences;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.constants.FileTypes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PreferencesWizard extends JPanel implements IWizard {

	private static final long serialVersionUID = -7139479707266773753L;

	public PreferencesWizard() {
	}

	@Override
	public void setProjectContainer(ProjectContainer container) {
	}

	@Override
	public FileContent getInitialFileContent(Component parent,
			String projectName) {
		String tooltip;
		final JPanel grid = new JPanel(new GridLayout(6, 2));

		tooltip = "a type of a property is defined in src/common/hr.fer.zemris.vhdllab.constants.FileTypes class";
		final JLabel typeLabel = new JLabel("type:");
		final JTextField typeText = new JTextField();
		typeLabel.setToolTipText(tooltip);
		typeText.setToolTipText(tooltip);

		tooltip = "id of a property is string composed of type and unique property name with dot(.) as a separator";
		final JLabel idLabel = new JLabel("id:");
		final JTextField idText = new JTextField();
		idLabel.setToolTipText(tooltip);
		idText.setToolTipText(tooltip);

		tooltip = "a text that will appear to user as a key in preferences editor";
		final JLabel descriptionLabel = new JLabel("description:");
		final JTextField descriptionText = new JTextField();
		descriptionLabel.setToolTipText(tooltip);
		descriptionText.setToolTipText(tooltip);

		tooltip = "a text that will appear to user when he hovers with a mouse over a property (should contain more information then a description)";
		final JLabel tooltipLabel = new JLabel("tooltip:");
		final JTextField tooltipText = new JTextField();
		tooltipLabel.setToolTipText(tooltip);
		tooltipText.setToolTipText(tooltip);

		tooltip = "a data type in values or default tag";
		String[] comboData = new String[] { "string", "double", "boolean",
				"integer", "color" };
		final JLabel dataTypeLabel = new JLabel("data type:");
		final JComboBox dataTypeComboBox = new JComboBox(comboData);
		dataTypeLabel.setToolTipText(tooltip);
		dataTypeComboBox.setToolTipText(tooltip);

		tooltip = "an editor type that will show data in values column of preferences editor.";
		comboData = new String[] { "custom", "list", "boolean", "color", "none" };
		final JLabel dataEditorLabel = new JLabel("data editor:");
		final JComboBox dataEditorComboBox = new JComboBox(comboData);
		dataEditorLabel.setToolTipText(tooltip);
		dataEditorComboBox.setToolTipText(tooltip);

		typeText.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				changeIdText();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				changeIdText();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				changeIdText();
			}

			private void changeIdText() {
				idText.setText(typeText.getText() + ".");
			}
		});

		grid.add(typeLabel);
		grid.add(typeText);
		grid.add(idLabel);
		grid.add(idText);
		grid.add(descriptionLabel);
		grid.add(descriptionText);
		grid.add(tooltipLabel);
		grid.add(tooltipText);
		grid.add(dataTypeLabel);
		grid.add(dataTypeComboBox);
		grid.add(dataEditorLabel);
		grid.add(dataEditorComboBox);

		JButton addValue = new JButton("add");
		JButton removeValue = new JButton("remove");
		JPanel buttonsGrid = new JPanel(new GridLayout(2, 1));
		buttonsGrid.add(addValue);
		buttonsGrid.add(removeValue);
		final JCheckBox editableByUserCheckBox = new JCheckBox("editable by user",
				true);
		final DefaultListModel model = new DefaultListModel();
		final JList values = new JList(model);
		tooltip = "a list of acceptable values that will be rendered in a combo box (in editor type is list), so user can not enter unacceptable value";
		values.setToolTipText(tooltip);
		tooltip = "a default value that is given to a user";
		final JLabel defaultLabel = new JLabel("default value:");
		final JTextField defaultText = new JTextField();
		defaultLabel.setToolTipText(tooltip);
		defaultText.setToolTipText(tooltip);
		JPanel defaultGrid = new JPanel(new GridLayout(1, 2));
		defaultGrid.add(defaultLabel);
		defaultGrid.add(defaultText);
		JPanel valuesPanel = new JPanel(new BorderLayout());
		valuesPanel.setPreferredSize(new Dimension(0, 100));
		valuesPanel.add(new JScrollPane(values), BorderLayout.CENTER);
		valuesPanel.add(buttonsGrid, BorderLayout.EAST);
		valuesPanel.add(editableByUserCheckBox, BorderLayout.NORTH);
		valuesPanel.add(defaultGrid, BorderLayout.SOUTH);

		addValue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = JOptionPane.showInputDialog("Enter value:");
				if (s == null || s.equals("")) {
					return;
				}
				if (!model.contains(s)) {
					model.addElement(s);
				}
			}
		});
		removeValue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = values.getSelectedIndex();
				if (index == -1) {
					return;
				}
				model.remove(index);
			}
		});

		final JButton composeButton = new JButton("Compose property");
		final JTextArea composedArea = new JTextArea();
		composedArea.setTabSize(4);
		final JPanel composePanel = new JPanel(new BorderLayout());
		composePanel.add(composeButton, BorderLayout.NORTH);
		composePanel.add(new JScrollPane(composedArea), BorderLayout.CENTER);

		final JPanel composeAndValuesPanel = new JPanel(new BorderLayout());
		composeAndValuesPanel.add(valuesPanel, BorderLayout.NORTH);
		composeAndValuesPanel.add(composePanel, BorderLayout.CENTER);

		composeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(typeText.getText().equals("")) {
					composedArea.setText("type not set!");
					return;
				}
				if(idText.getText().equals("")) {
					composedArea.setText("id not set!");
					return;
				}
				if(idText.getText().equalsIgnoreCase(typeText.getText()+".")) {
					composedArea.setText("id and type must differ!");
					return;
				}
				if(defaultText.getText().equals("")) {
					composedArea.setText("default value not set!");
					return;
				}
				StringBuilder sb = new StringBuilder(1000);
				sb.append("Informations on how to install a property\n\n");
				if(!FileTypes.values().contains(typeText.getText())) {
					sb.append(" - file type ").append(typeText.getText())
						.append(" does not exists so you need to add it.\n\t")
						.append("* open src/common/hr.fer.zemris.vhdllab.constants.FileTypes class\n\t")
						.append("* add the following line in \"user file types\" section:\n\t\t")
						.append("public static final String FT_").append(typeText.getText().toUpperCase().replaceAll("\\.", "_"))
						.append(" = \"").append(typeText.getText()).append("\";\n\t")
						.append("* also add the following line at the bottom of values method:\n\t\t")
						.append("values.add(FileTypes.FT_").append(typeText.getText().toUpperCase())
						.append(");\n\n");
				}
				
				sb.append(" - a property id most likely does not exists so you need to add it.\n\t")
					.append("* open src/common/hr.fer.zemris.vhdllab.constants.UserFileConstants class\n\t")
					.append("* add the following line at the end:\n\t\t")
					.append("public static final String ").append(idText.getText().toUpperCase().replaceAll("\\.", "_"))
					.append(" = \"").append(typeText.getText()).append("\";\n\n");
				
				sb.append(" - now you need to add xml file to specified package\n\t")
					.append("* now open src/web/onServer/hr.fer.zemris.vhdllab.servlets.initialize.preferencesFiles package\n\t")
					.append("* create a new file called \"").append(idText.getText()).append(".xml\"\n\t")
					.append("* copy following lines in that file\n//START OF LINES TO COPY\n");
				
				sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<property id=\"")
					.append(idText.getText()).append("\" type=\"").append(typeText.getText())
					.append("\" >\n\t<description>").append(descriptionText.getText())
					.append("</description>\n\t<tooltip>").append(tooltipText.getText())
					.append("</tooltip>\n\t<editable byUser=\"")
					.append(String.valueOf(editableByUserCheckBox.isSelected()))
					.append("\" />\n\t<data type=\"").append(dataTypeComboBox.getSelectedItem())
					.append("\" editor=\"").append(dataEditorComboBox.getSelectedItem())
					.append("\">\n\t\t");
				
				if(model.getSize() != 0) {
					sb.append("<values>\n");
					for(int i = 0; i<model.getSize(); i++) {
						sb.append("\t\t\t<value>").append(model.get(i)).append("</value>\n");
					}
					sb.append("\t\t</values>\n\t\t");
				}
				sb.append("<default>").append(defaultText.getText()).append("</default>\n\t")
					.append("</data>\n</property>\n");
				
				sb.append("// END OF LINES TO COPY\n\n");
				
				sb.append(" - now you need to add that file to configuration file\n\t")
					.append("* open file preferencesFiles.properties in the same package\n\t")
					.append("* append the following line to the end of the file:\n\t\t")
					.append(idText.getText()).append(".xml; \\\n\n");
				
				sb.append(" - over and out. now try using that property. for example:\n\t")
					.append("...\n\t")
					.append("String data = projectContainer.getProperty(UserFileConstants.SYSTEM_PROJECT_EXPLORER_WIDTH);\n\t")
					.append("double width = Double.parseDouble(data);\n\t")
					.append("...\n\n");
				
				sb.append("NOTE: if data type or data editor does not contain data you need, report it...");
				
				
				composedArea.setText(sb.toString());
			}
		});

		final JPanel dialogPanel = new JPanel(new BorderLayout());
		dialogPanel.setPreferredSize(new Dimension(700, 500));
		dialogPanel.add(grid, BorderLayout.NORTH);
		dialogPanel.add(composeAndValuesPanel, BorderLayout.CENTER);

		JOptionPane.showOptionDialog(parent, dialogPanel,
				"Create new Property", JOptionPane.OK_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] { "close" }, 0);
		return null;
	}

}
