package hr.fer.zemris.vhdllab.applets.editor.preferences;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.client.core.prefs.PreferencesEvent;
import hr.fer.zemris.vhdllab.client.core.prefs.PreferencesListener;
import hr.fer.zemris.vhdllab.client.core.prefs.UserPreferences;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class PreferencesEditor extends JPanel implements IEditor,
		PreferencesListener {

	private static final long serialVersionUID = -7139479707266773753L;

	private ISystemContainer container;
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
					UserPreferences.instance().set(key, value);
				}
			}
		});
	}

	@Override
	public void dispose() {
		UserPreferences.instance().removePreferencesListener(this);
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
		return new PreferencesWizard();
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
		UserPreferences pref = UserPreferences.instance();
		for(String key : pref.keys()) {
			String value = pref.get(key, null);
			if(value == null) {
				continue;
			}
			model.addRow(new Object[] { key, value });
			int rowCount = model.getRowCount();
			rows.put(key, Integer.valueOf(rowCount - 1));
			pref.addPreferencesListener(this, key);
		}
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.client.core.prefs.PreferencesListener#propertyChanged(hr.fer.zemris.vhdllab.client.core.prefs.PreferencesEvent)
	 */
	@Override
	public void propertyChanged(PreferencesEvent event) {
		int row = rows.get(event.getName()).intValue();
		model.setValueAt(event.getNewValue(), row, 1);
	}

	@Override
	public void setSystemContainer(ISystemContainer container) {
		this.container = container;
	}

	@Override
	public void setReadOnly(boolean flag) {
	}

	@Override
	public void setSavable(boolean flag) {
	}

}
