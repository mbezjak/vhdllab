package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.i18n.CachedResourceBundles;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ResourceBundle;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
		
		SchemaAdapter adapter = new SchemaAdapter(2.d);
		Graphics g = this.getGraphics();
		adapter.setGraphics(g);
		adapter.drawLine(0, 0, 50, 25);
	}
	
	static public void main(String[] args) {
		class probna extends Component {
			public void paint(Graphics g) {
				SchemaAdapter adapter = new SchemaAdapter(2.d);
				adapter.setGraphics(g);
				adapter.drawLine(5, 50, 75, 90);
			}
		}
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(250, 150);
		frame.setVisible(true);
		probna p = new probna();
		//frame.add(p);
		
		GridLayout gridlay = new GridLayout(5, 3);
		frame.setLayout(gridlay);
		
		String s1 = new String("Zika");
		Ptr<Object> p1 = new Ptr<Object>(s1);
		Ptr<Object> p2 = p1;
		TextComponentProperty tcp = new TextComponentProperty("Ime", p1);
		frame.add(new JLabel("Ime"));
		frame.add(tcp.getPropField().getComponent());
		frame.validate();
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
			System.out.println(p1.val);
			System.out.println(p2.val);
		}
	}
	
}
