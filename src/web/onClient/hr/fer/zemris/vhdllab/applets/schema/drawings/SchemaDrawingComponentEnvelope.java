/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.schema.drawings;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;

import java.awt.Point;

/**
 * Razred koji sadrzi sve potrebne informacije o komponenti koja se stavlja 
 * i prikazuje na ekran.
 * 
 * @author Tommy
 *
 */
public class SchemaDrawingComponentEnvelope {
	private AbstractSchemaComponent component = null;
	private Point position = null;
	
	public SchemaDrawingComponentEnvelope(AbstractSchemaComponent component, Point position) {
		this.component=component;
		this.position=position;
	}

	/**
	 * @return the component
	 */
	public AbstractSchemaComponent getComponent() {
		return component;
	}

	/**
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}
	
	
	
}
