package hr.fer.zemris.vhdllab.applets.editor.automat.entityTable;

import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.ci.Port;
import hr.fer.zemris.vhdllab.service.ci.PortDirection;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class EntityTable extends JPanel {

    private static final long serialVersionUID = 7533182574459245416L;

    JTable table;
    MyTableModel model;

    String imeSklop;
    ResourceBundle bundle;

    public EntityTable() {
        bundle = ResourceBundle
                .getBundle("Client_EntityTable_ApplicationResources");
        this.setLayout(new BorderLayout());

        Object[][] obj = { { "", "in", "std_logic", "0", "0" } };
        JComboBox inComboBox = new JComboBox(new String[] { "in", "out" });
        JComboBox tipComboBox = new JComboBox(new String[] { "std_logic",
                "std_logic_vector" });
        JTextField imeSignala = new JTextField("");

        NumberBox brojevi = new NumberBox("0");

        String[] data1 = { bundle.getString("table.column0"),
                bundle.getString("table.column1"),
                bundle.getString("table.column2"),
                bundle.getString("table.column3"),
                bundle.getString("table.column4") };

        model = new MyTableModel(obj, data1);
        model.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent arg0) {
                if (!model.getValueAt(model.getRowCount() - 1, 0).equals(""))
                    model.addRow();
            }
        });
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.getColumnModel().getColumn(0).setCellEditor(
                new DefaultCellEditor(imeSignala));
        table.getColumnModel().getColumn(1).setCellEditor(
                new DefaultCellEditor(inComboBox));
        table.getColumnModel().getColumn(2).setCellEditor(
                new DefaultCellEditor(tipComboBox));
        table.getColumnModel().getColumn(3).setCellEditor(
                new DefaultCellEditor(brojevi));
        table.getColumnModel().getColumn(4).setCellEditor(
                new DefaultCellEditor(brojevi));

        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(100, 100));
        this.add(pane, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(500, 300));
    }

    public ReturnData getData() {
        return model.getData();
    }

    // ***********MyTableModel************

    private class MyTableModel extends AbstractTableModel {
        private static final long serialVersionUID = -4781965036332460224L;
        private String[] columnNames = new String[5];
        private Object[][] data = null;

        public MyTableModel(Object[][] obj, String[] names) {
            super();
            data = obj;
            for (int i = 0; i < 5 && i < names.length; i++)
                columnNames[i] = names[i];
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * public Class getColumnClass(int c) { return getValueAt(0,
         * c).getClass(); }
         */

        @Override
        public boolean isCellEditable(int row, int col) {
            if (col > 2 && getValueAt(row, 2).equals("std_logic"))
                return false;
            return true;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }

        public void addRow() {
            int brojRedova = getRowCount();
            Object[][] obj = new Object[brojRedova + 1][getColumnCount()];
            for (int i = 0; i < getRowCount(); i++)
                for (int j = 0; j < 5; j++)
                    obj[i][j] = data[i][j];
            obj[getRowCount()][0] = "";
            obj[getRowCount()][1] = "in";
            obj[getRowCount()][2] = "std_logic";
            obj[getRowCount()][3] = "0";
            obj[getRowCount()][4] = "0";
            data = obj;
            fireTableDataChanged();
        }

        public ReturnData getData() {
            table.getCellEditor().cancelCellEditing();
            String[][] pom = new String[getRowCount()][getColumnCount()];
            for (int i = 0; i < getRowCount(); i++)
                for (int j = 0; j < getColumnCount(); j++)
                    pom[i][j] = (String) data[i][j];
            ReturnData dat = new ReturnData(imeSklop, pom);
            return dat;
        }

        public void setData(Object[][] obj) {
            data = obj;
            addRow();
        }
    }

    public void setData(CircuitInterface data) {
        List<Port> lista = data.getPorts();
        Object[][] obj = new Object[lista.size()][model.getColumnCount()];
        int i = 0;
        for (Port p : lista) {
            obj[i][0] = p.getName();
            if (p.getDirection().equals(PortDirection.IN))
                obj[i][1] = "in";
            else
                obj[i][1] = "out";
            obj[i][2] = p.getTypeName();
            if (p.isVector()) {
                obj[i][3] = p.getFrom();
                obj[i][4] = p.getTo();
            } else {
                obj[i][3] = 0;
                obj[i][4] = 0;
            }
            i++;
        }
        model.setData(obj);
    }

    public void setImeSklop(String imeSklop) {
        this.imeSklop = imeSklop;
    }

    public CircuitInterface getCircuitInterface() {
        List<Port> lista = new ArrayList<Port>();
        ReturnData data = model.getData();
        String[][] pomData = data.getData();
        for (int i = 0; i < pomData.length; i++) {
            if (pomData[i][0].equals(""))
                continue;
            PortDirection d = null;
            if (pomData[i][1].toLowerCase().equals("in"))
                d = PortDirection.IN;
            else
                d = PortDirection.OUT;
            Port p = new Port(pomData[i][0], d);
            if (!pomData[i][2].equalsIgnoreCase("Std_Logic")) {
                int[] inte = new int[] { Integer.parseInt(pomData[i][3]),
                        Integer.parseInt(pomData[i][4]) };
                p.setFrom(inte[0]);
                p.setTo(inte[1]);
            }
            lista.add(p);
        }
        CircuitInterface inter = new CircuitInterface(data.getName());
        inter.addAll(lista);
        return inter;
    }

    /**
     * Funkcija provjerava dali su uneseni podatci u tablici dobri.
     */
    public boolean isDataCorrect() {
        Set<String> test = new HashSet<String>();
        for (int i = 0; i < model.getRowCount() - 1; i++) {
            if (test.contains(model.getValueAt(i, 0)))
                return false;
            else
                test.add((String) model.getValueAt(i, 0));
        }
        return true;
    }

}