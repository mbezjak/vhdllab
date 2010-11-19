package hr.fer.zemris.vhdllab.applets.texteditor.misc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;

/**
 * Razred koji predstavlja komponentu koja se moze upariti s nekim
 * {@link JEditorPane}-om i koja ce za njega prikazivati brojeve redaka
 * uz posebno istaknut redak u kojem je kursor.
 * 
 * @author marcupic
 *
 */
public class LineNumbers extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JEditorPane textPane;
	private int prefWidth;
	private JViewport viewport;

	public static LineNumbers createInstance(JEditorPane textPane, JScrollPane scrollPane, int prefWidth) {
		JViewport vp = new JViewport();
		LineNumbers ln = new LineNumbers(textPane, vp, prefWidth);
		vp.setView(ln);
		scrollPane.setRowHeader(vp);
		return ln;
	}
	
	public LineNumbers(JEditorPane textPane, JViewport viewport, int prefWidth) {
		this.textPane = textPane;
		this.prefWidth = prefWidth;
		this.viewport = viewport;
		
		setPreferredSize(new Dimension(prefWidth,30));
		
		textPane.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				LineNumbers.this.repaint();
			}
		});
		
		textPane.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				int h = e.getComponent().getHeight();
				if(h<1) return;
				setSize(LineNumbers.this.prefWidth, h);
				setPreferredSize(new Dimension(LineNumbers.this.prefWidth, h));
				revalidate();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Rectangle drawHere = viewport.getViewRect();

		Font font = textPane.getFont();
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		
		int fh = fm.getHeight();
		
        g.setColor(new Color(230, 163, 4));
        g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);
        g.setColor(new Color(100, 100, 100));
        g.drawLine(drawHere.x+drawHere.width-1, drawHere.y, drawHere.x+drawHere.width-1, drawHere.y+drawHere.height);
        
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g.setColor(Color.black);
        fm = g.getFontMetrics();
        
        int caretRow = getCaretRow(textPane, textPane.getCaret().getDot());
        int prviVidljivi = drawHere.y / fh + 1;
		g.setColor(Color.BLACK);
        for(int i = prviVidljivi; ; i++) {
        	int y = (i)*fh;
        	if(y>drawHere.height+drawHere.y) break;
        	if(i==caretRow) {
        		g.setColor(new Color(255,235,235));
        	}
        	String s = Integer.toString(i);
        	g.drawString(s, getWidth()-3-fm.stringWidth(s), y);
        	if(i==caretRow) {
        		g.setColor(Color.BLACK);
        	}
        }
	}

	public int getCaretRow(JTextComponent editor, int pos) {
        int rn = (pos==0) ? 1 : 0;
        try {
            int offs=pos;
            while( offs>0) {
                offs=Utilities.getRowStart(editor, offs)-1;
                rn++;
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return rn;
    }
}
