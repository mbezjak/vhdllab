package hr.fer.zemris.vhdllab.applets.schema.drawings;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class SchemaDrawingCanvasListeners implements MouseListener,
		MouseMotionListener, MouseWheelListener {

	private Point position = new Point();
	private SchemaDrawingCanvas canvas = null;
	
	
	public SchemaDrawingCanvasListeners(SchemaDrawingCanvas parent){
		this.canvas=parent;
		parent.addMouseListener(this);
		parent.addMouseMotionListener(this);
	}
	
	
	public int getX(){
		return position.x;
	}
	
	public int getY(){
		return position.y;
	}
	
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseMoved(MouseEvent e) {
		this.position=e.getPoint();
		canvas.repaint();
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}

}
