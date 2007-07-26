package hr.fer.zemris.vhdllab.applets.editor.preferences;

import hr.fer.zemris.ajax.shared.XMLUtil;
import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.preferences.PropertyListener;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class PreferencesEditor extends JPanel implements IEditor,
		PropertyListener {

	private static final long serialVersionUID = -7139479707266773753L;

	private ProjectContainer container;
	private FileContent content;
	private Map<String, Integer> rows;

	private DefaultTableModel model;
	private JTable table;

	public PreferencesEditor() {
	}

	@Override
	public void init() {
		rows = new HashMap<String, Integer>();
		Object[] columns = new Object[] { "key", "value" };
		model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = -1193719457041467467L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column != 0 ? true : false;
			}
		};
		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);

		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				int firstRow = e.getFirstRow();
				int lastRow = e.getLastRow();
				for (int i = firstRow; i <= lastRow; i++) {
					TableModel source = (TableModel) e.getSource();
					String key = (String) source.getValueAt(i, 0);
					String value = (String) source.getValueAt(i, 1);
					try {
						container.saveProperty(key, value);
					} catch (UniformAppletException ex) {
						ex.printStackTrace();
						return;
					}
				}
			}
		});
	}

	@Override
	public void dispose() {
	}

	@Override
	public String getData() {
		return null;
	}

	@Override
	public String getFileName() {
		return content.getFileName();
	}

	@Override
	public String getProjectName() {
		return content.getProjectName();
	}

	@Override
	public IWizard getWizard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void highlightLine(int line) {
	}

	@Override
	public boolean isModified() {
		return false;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public boolean isSavable() {
		return false;
	}

	@Override
	public void setFileContent(FileContent content) {
		this.content = content;
		Properties p = XMLUtil.deserializeProperties(content.getContent());
		for (Object o : p.keySet()) {
			String key = (String) o;
			String value = p.getProperty(key);
			model.addRow(new Object[] { key, value });
			int rowCount = model.getRowCount();
			rows.put(key, Integer.valueOf(rowCount - 1));
			container.addPropertyListener(this, key);
		}
	}

	@Override
	public void propertyChanged(String name, String oldValue, String newValue) {
		int row = rows.get(name).intValue();
		model.setValueAt(newValue, row, 1);
	}

	@Override
	public void setProjectContainer(ProjectContainer container) {
		this.container = container;
	}

	@Override
	public void setReadOnly(boolean flag) {
	}

	@Override
	public void setSavable(boolean flag) {
	}

}
