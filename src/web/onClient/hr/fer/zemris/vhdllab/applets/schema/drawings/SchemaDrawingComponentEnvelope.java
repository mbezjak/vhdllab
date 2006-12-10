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

	@Override
	public boolean equals(Object arg0) {
		SchemaDrawingComponentEnvelope rhs = null;
		try {
			rhs = (SchemaDrawingComponentEnvelope)arg0;
		} catch (Exception e) {
			return false;
		}
		if (rhs.getComponent().getComponentInstanceName() == component.getComponentInstanceName()) {
			return true;
		}
		return false;
	}
	
	
	
}
