package hr.fer.zemris.vhdllab.Schema;

import hr.fer.zemris.vhdllab.i18n.CachedResourceBundles;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ResourceBundle;

import javax.swing.JApplet;

public class SchemaApplet extends JApplet {

	
	private static final long serialVersionUID = 302949341345123916L;

	//Language things
	private ResourceBundle rb = null;
	private String Language;
	
	//GUI things
	private SchemaMenu menu = null;
	private SchemaColorProvider colors = null;
	@SuppressWarnings("unused")
	private Container container = null;
	
	
	//Methods
	
	private void initGUI() {
		//default
		container=getContentPane();
		
		
		//default Layout
		this.setLayout(new BorderLayout());
		//default pane

		//menu
		menu=new SchemaMenu(rb,colors);
		this.setJMenuBar(menu.getMenu());
		
		this.addKeyListener(new KeyListener() {
		
			public void keyTyped(KeyEvent e) {
				System.err.print(e.getKeyChar());
		
			}
		
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
		
			}
		
			public void keyPressed(KeyEvent e) {
				
			}
		
		});
		
		
		
		
	}

	private String getLanguage() {
		return "en";
	}
	
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	private void initColors() {		
		colors=new SchemaColorProvider();
	}

	private void initLanguage() {
		
		Language = getLanguage();
		rb = CachedResourceBundles.getBundle("Client_Schema_ApplicationResources", Language);
		
	}

	@Override
	public void init() {
		initLanguage();
		initColors();		
		initGUI();
	}
	
}
