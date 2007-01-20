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
	
	/**
	 * Serijalizira ovaj objekt
	 * @return XML dio koji opisuje ovaj objekt
	 */
	public String serialize(){
		StringBuffer xmlComponent=new StringBuffer();
		xmlComponent.append("<component>\n");
			xmlComponent.append("<position>\n");
				xmlComponent.append("<x>").append(position.x).append("</x>\n");
				xmlComponent.append("<y>").append(position.y).append("</y>\n");
			xmlComponent.append("</position>\n");
		
			xmlComponent.append("<componentName>").append(component.getComponentName()).append("</componentName>\n");
			xmlComponent.append("<componentSource>\n").append(component.serializeComponent()).append("\n</componentSource>\n");			
		xmlComponent.append("</component>\n");
		
		return xmlComponent.toString();
	}
	
	/**
	 * Deserijalizira ovaj objekt
	 * @param src XML dio koji je stvoren sa serialice() metodom i opisuje ovaj objekt
	 * 
	 */
	public void deserialize(String src){
		//TODO TOMMY: Moram napravit deserijalizaciju
	}
}
