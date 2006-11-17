package hr.fer.zemris.vhdllab.applets.schema;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
/**
 * Razred koji bi se trabao brinuti o svemu sto je vezano za
 * izradu glavnog menija.
 * @author Tommy
 *
 */
public class SchemaMenu {
	
	private SchemaMenuText menuText = null;
	private JMenuBar menuBar;
	private JMenu file,edit,option,help;
	private SchemaColorProvider colors;
	
	
	public SchemaMenu( ResourceBundle rb, SchemaColorProvider colors ){
		menuText = new SchemaMenuText(rb);
		this.colors=colors;
		initMenu();
	}
	
	private void initMenu(){
		JMenuItem item;
		menuBar=new JMenuBar();
		
		file=new JMenu(menuText.FILE);
		file.getAccessibleContext().setAccessibleDescription(menuText.FILE_DESC);
		
		//File->New
		item=new JMenuItem(menuText.FILE_NEW);
		item.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {
				preformNew();
			}		
		});
		item.setForeground(colors.MENU_TEXT);
		item.setBackground(colors.MENU_BG);
		file.add(item);
		
		//File->Save
		item=new JMenuItem(menuText.FILE_SAVE);
		item.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				preformSave();
			}		
		});
		item.setForeground(colors.MENU_TEXT);
		file.add(item);
		
		//Separator Save<->Exit
		file.addSeparator();
		
		//File->Save
		item=new JMenuItem(menuText.FILE_EXIT);
		item.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				preformExit();
			}		
		});
		file.add(item);
		
		
		menuBar.add(file);
		
		edit=new JMenu(menuText.EDIT);
		edit.add(new JMenuItem(menuText.EDIT_CUT));
		edit.add(new JMenuItem(menuText.EDIT_COPY));
		edit.add(new JMenuItem(menuText.EDIT_PASTE));
		menuBar.add(edit);
		
		option=new JMenu(menuText.OPTION);
		menuBar.add(option);
		
		help=new JMenu(menuText.HELP);
		menuBar.add(help);
		
		menuBar.setBackground(colors.MENU_BG);
		menuBar.setForeground(colors.MENU_TEXT);
	}
	
	public JMenuBar getMenu(){
		return this.menuBar;
	}
	
	private void preformNew() {
		// TODO Auto-generated method stub
		
	}
	private void preformSave() {
		// TODO Auto-generated method stub
		
	}
	private void preformExit(){
		
	}
}
