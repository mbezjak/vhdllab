package hr.fer.zemris.vhdllab.applets.automat.entityTable;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class EntityTableTester extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7679816895293214009L;
	
	public EntityTableTester(){
		super();
		this.getContentPane().setLayout(new BorderLayout());
		String[] st={"Name","Direction","Type","From","To"};
		EntityTable table=new EntityTable("Entity declaration:",st,"Entity name: ");
		this.getContentPane().add(table,BorderLayout.CENTER);
		this.setSize(new Dimension(500,300));
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EntityTableTester tester=new EntityTableTester();
		tester.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		tester.setVisible(true);
	}

}
