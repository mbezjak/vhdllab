package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.i18n.CachedResourceBundles;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ResourceBundle;

import javax.swing.JFrame;

public class TommyTestDisplay extends JFrame {
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
	
	public TommyTestDisplay(){
		init();
	}
	
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
	
	public void init() {
		initLanguage();
		initColors();		
		initGUI();
		
		//SchemaDrawingAdapter adapter = new SchemaDrawingAdapter(colors,2.d);
		Graphics g = this.getGraphics();
		//adapter.setGraphics(g);
		//adapter.drawLine(0, 0, 50, 25);
	}
	
	private void initColors() {		
		colors=new SchemaColorProvider();
	}

	private void initLanguage() {
		
		Language = getLanguage();
		rb = CachedResourceBundles.getBundle("Client_Schema_ApplicationResources", Language);
		
	}
	
	private String getLanguage() {
		return "en";
	}
	
	public static void main(String[] args) {
		JFrame frame=new TommyTestDisplay();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1024,768));
		frame.setVisible(true);
	}
}
