/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.schema.drawings;

import hr.fer.zemris.vhdllab.applets.schema.SchemaColorProvider;
import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JComponent;

/**
 * @author Tommy
 *
 */
public class SchemaDrawingCanvas extends JComponent {

	private static final long serialVersionUID = 168392906688186429L;
	
	private SchemaDrawingGrid grid = null;
	private SchemaDrawingAdapter adapter = null;
	private ArrayList<AbstractSchemaComponent> components = null;	
	private SchemaColorProvider colors = null;	
	
	public SchemaDrawingCanvas(SchemaColorProvider colors) {
		components=new ArrayList<AbstractSchemaComponent>();
		this.colors=colors;
	}

	/**
	 * Dodavanje komponente na crtacu povrsinu.
	 * @param component Komponenta koja se dodaje.
	 */
	public void addComponent(AbstractSchemaComponent component){
		//TODO ovdje treba jos napravit puno toga...neki mehanizam zastite i slicno...		
		components.add(component);
	}
	
	public void addComponent(AbstractSchemaComponent component, Point position){
		
	}
	
	public ArrayList<AbstractSchemaComponent> getComponentList(){
		return this.components;
	}

	
	@Override
	public void paint(Graphics g) {
		
		//super.paint(g);
	}
	
	
	
	
}
