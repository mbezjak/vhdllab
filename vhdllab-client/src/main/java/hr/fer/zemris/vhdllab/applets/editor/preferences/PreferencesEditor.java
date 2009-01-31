package hr.fer.zemris.vhdllab.applets.editor.preferences;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditor;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class PreferencesEditor extends AbstractEditor implements
        PreferenceChangeListener {

    private static final long serialVersionUID = 1L;

    private Map<String, Integer> rows;

    private DefaultTableModel model;
    private JTable table;

    @Override
    public void doInitWithoutData() {
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

                    int index = key.indexOf('#');
                    Preferences pref = Preferences.userRoot().node(
                            key.substring(1, index));
                    pref.put(key.substring(index + 1), value);
                }
            }
        });
    }

    @Override
    public void doDispose() {
        try {
            removeListener(Preferences.userRoot());
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getData() {
        return null;
    }

    @Override
    public IWizard getWizard() {
        return null;
    }

    @Override
    protected void doInitWithData(FileInfo f) {
        try {
            setPreferences(Preferences.userRoot());
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    private void setPreferences(Preferences node) throws BackingStoreException {
        for (String n : node.childrenNames()) {
            Preferences pref = node.node(n);
            for (String key : pref.keys()) {
                String value = pref.get(key, null);
                if (value == null) {
                    continue;
                }
                String name = getPropertyName(pref, key);
                model.addRow(new Object[] { name, value });
                int rowCount = model.getRowCount();
                rows.put(name, Integer.valueOf(rowCount - 1));
            }
            pref.addPreferenceChangeListener(this);
            setPreferences(pref);
        }
    }

    private void removeListener(Preferences node) throws BackingStoreException {
        for (String n : node.childrenNames()) {
            Preferences pref = node.node(n);
            pref.removePreferenceChangeListener(this);
            removeListener(pref);
        }
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent event) {
        String name = getPropertyName(event.getNode(), event.getKey());
        Integer integer = rows.get(name);
        if (integer != null) {
            int row = integer.intValue();
            model.setValueAt(event.getNewValue(), row, 1);
        }
    }

    private String getPropertyName(Preferences node, String key) {
        return node.absolutePath() + "#" + key;
    }

}
