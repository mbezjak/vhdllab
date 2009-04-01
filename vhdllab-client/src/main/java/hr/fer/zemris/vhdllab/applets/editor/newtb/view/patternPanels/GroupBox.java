package hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class GroupBox extends JPanel {

	private static final long serialVersionUID = 1L;

	protected String title;
	
	private Component innerPane;
	
	/**
	 * @param title The title of the GroupBox
	 * @param comp The component that the GroupBox surrounds.
	 * 
	 * Creates a GroupBox object that can be used in bot awt and swing 
	 * 
	 */
	public GroupBox(String title,Component comp){
		// instantiating
		super();
		this.title=title;
		innerPane=comp;
		// designing the layout
		setLayout(new BorderLayout());
		add("Center",innerPane);
		
		add("West",new sidePane());
		add("East",new sidePane());
		add("North",new upperPane());
		add("South",new lowerPane());
	}
	
	private class sidePane extends JPanel {

		private static final long serialVersionUID = 1L;

		public sidePane(){
			super();
		}
		
		@Override
        public void paint(Graphics g){
			g.setColor(Color.BLACK);
			g.drawLine(5,0,5,getSize().height);
		}
	}
	
	private class upperPane extends JPanel {
		
		private static final long serialVersionUID = 1L;
	
		Font font=new Font(Font.SANS_SERIF,Font.BOLD,10);
		
		public upperPane(){
			super();
		}
		
		@Override
        public void setFont(Font font){
			this.font=font;
		}
	
		@Override
        public void paint(Graphics graph){
			//cast so that we can use getFontRenderContext()
			Graphics2D g=(Graphics2D)graph;
			
			g.setColor(Color.BLACK);
			g.drawLine(5,5,10,5);
			g.setFont(font);
			g.drawString(title,12,9);
			FontRenderContext frc = g.getFontRenderContext();
			Rectangle2D stringBounds = font.getStringBounds(title, 0, title.length(), frc);
			g.drawLine((int)stringBounds.getWidth()+14,5,getSize().width-5,5);
			g.drawLine(5,5,5,getSize().height);
			g.drawLine(getSize().width-5,5,getSize().width-5,getSize().height);
		}
	}
	
	private class lowerPane extends JPanel {

		private static final long serialVersionUID = 1L;

		public lowerPane(){
			super();
		}
	
		@Override
        public void paint(Graphics g){
			g.setColor(Color.BLACK);
			g.drawLine(5,5,getSize().width-5,5);
			g.drawLine(5,5,5,0);
			g.drawLine(getSize().width-5,5,getSize().width-5,0);
		}
	}	
}
