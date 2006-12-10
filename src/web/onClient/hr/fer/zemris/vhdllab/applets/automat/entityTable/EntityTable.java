package hr.fer.zemris.vhdllab.applets.automat.entityTable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class EntityTable extends JPanel {

	private JTable table=null;
	private MyTableModel model=null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7533182574459245416L;

	/**
	 * @param header 
	 * @param args
	 */
	public EntityTable(String label,String[] data){
		super();
		
		createGUI(label,data);
	}

	private void createGUI(String lab1,String[] data) {
		JLabel label=new JLabel(lab1);
		JPanel panel=new JPanel(new GridLayout(1,2));
		panel.add(label);
		this.setLayout(new BorderLayout());
		this.add(panel,BorderLayout.NORTH);
		
		Object[][] obj={{"","in","Std_Logic","0","0"}};
		JComboBox inComboBox=createInComboBox();
		JComboBox tipComboBox=createTipComboBox();
		
		NumberBox brojevi=new NumberBox("0");
		
		model=new MyTableModel(obj,data);
		model.addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent arg0) {
				if(!model.getValueAt(model.getRowCount()-1,0).equals(""))model.addRow();
			}
		});
		table=new JTable(model);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(inComboBox));
		table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(tipComboBox));
		table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(brojevi));
		table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(brojevi));
		
		JScrollPane pane=new JScrollPane(table);
		pane.setPreferredSize(new Dimension(100,100));
		this.add(pane,BorderLayout.CENTER);
	}
	
	
	
	private JComboBox createInComboBox() {
		JComboBox cb=new JComboBox();
		InputStream in=this.getClass().getResourceAsStream("EntityTable-Direction.properties");
		Properties p=new Properties();
		try {
			p.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(Object key:p.keySet())cb.addItem(p.get(key));
		return cb;
	}

	private JComboBox createTipComboBox() {
		JComboBox cb=new JComboBox();
		InputStream in=this.getClass().getResourceAsStream("EntityTable-Type.properties");
		Properties p=new Properties();
		try {
			p.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(Object key:p.keySet())cb.addItem(p.get(key));
		return cb;
	}
	
	public String[][] getData(){
		return model.getData();
	}


	//***********MyTableModel************
	
    private class MyTableModel extends AbstractTableModel {
        /**
		 * 
		 */
		private static final long serialVersionUID = -4781965036332460224L;
		/**
		 * 
		 */
		private String[] columnNames = new String[5];
        private Object[][] data = null;

        protected MyTableModel(Object[][] obj,String[] names){
        	super();
        	data=obj;
        	for(int i=0;i<5&&i<names.length;i++)columnNames[i]=names[i];
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
/*
        public Class<? extends Object> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
*/

        public boolean isCellEditable(int row, int col) {
        	if(col>2&&getValueAt(row,2).equals("Std_Logic"))return false;
            return true;
        }

        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
        public void addRow(){
        	int brojRedova=getRowCount();
        	Object[][] obj=new Object[brojRedova+1][getColumnCount()];
        	for(int i=0;i<getRowCount();i++)
        		for(int j=0;j<5;j++)obj[i][j]=data[i][j];
        	obj[getRowCount()][0]="";
        	obj[getRowCount()][1]="in";
        	obj[getRowCount()][2]="Std_Logic";
        	obj[getRowCount()][3]="0";
        	obj[getRowCount()][4]="0";
        	data=obj;
        	fireTableDataChanged();
        };
        public String[][] getData(){
        	String[][] pom=new String[getRowCount()][getColumnCount()];
        	for(int i=0;i<getRowCount();i++)
        		for(int j=0;j<getColumnCount();j++)pom[i][j]=(String) data[i][j];
        	return  pom;
        }
    }
}
