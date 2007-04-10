package hr.fer.zemris.vhdllab.applets.editor.tb2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * JPanel that acts as a canvas for drawing signals and editing input.
 * @author ddelac
 *
 */
public class CanvasPanel extends JPanel {

	/**
	 *Generated serial ID 
	 */
	private static final long serialVersionUID = 6272897016635522967L;

	/**
	 * buffer u kojem se crta slika sklopa
	 */
	private BufferedImage img = null;
	
	/**
	 * Constructor
	 * TODO:change to accept input parametars
	 *
	 */
	public CanvasPanel() {
		super();
		createGUI();
	}

	/**
	 * metoda paintComponent klase JPanel overrideana
	 */
	protected void paintComponent(Graphics g) {
		if(img == null) {
			img=new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
			nacrtaj();
		} else {
			if (img.getHeight()!=this.getHeight()||img.getWidth()!=this.getWidth()){
				img=new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
				nacrtaj();
			}
			g.drawImage(img,0,0,img.getWidth(),img.getHeight(),null);
		}
	}
	
	/**
	 * Metoda je zaduzena za crtanje trenutnog stanja po platnu
	 *
	 */
	private void nacrtaj() {
		Graphics2D gr=(Graphics2D) img.getGraphics();
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		gr.setColor(Color.WHITE);;
		gr.fillRect(0,0,img.getWidth(),img.getHeight());
		
		//TODO: DRAW!!!!
		gr.setColor(Color.RED);
		gr.drawRect(10, 10, 100, 100);
		//****
		
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
		repaint();
	}

	private void createGUI() {
		this.setLayout(new BorderLayout());
		this.add(new JLabel("   CANVAS PANEL drawing area"),BorderLayout.CENTER);
		this.setOpaque(true);
	}
}
