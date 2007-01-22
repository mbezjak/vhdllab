/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.schema.drawings;

import hr.fer.zemris.ajax.shared.XMLUtil;
import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactoryException;

import java.awt.Point;
import java.util.Properties;

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
	
	/**
	 * Standardni konstruktor. 
	 * @param component Komponenta koju ovaj envelope sadrzi.
	 * @param position Pozicija komponente na canvasu.
	 */
	public SchemaDrawingComponentEnvelope(AbstractSchemaComponent component, Point position) {
		this.component=component;
		this.position=position;
	}
	
	/**
	 * Konstruktor za deserijalizaciju
	 * @param xmlSource
	 */
	public SchemaDrawingComponentEnvelope(String xmlSource,SchemaDrawingCanvas canvas){
		deserialize(xmlSource);
		canvas.addEnvelope(this);
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
		Properties prop=new Properties();
		
		prop.setProperty("compPosX", String.valueOf(position.x));
		prop.setProperty("compPosY", String.valueOf(position.y));
		prop.setProperty("compName", component.getComponentName());
		prop.setProperty("compSource", component.serializeComponent());
		
		return XMLUtil.serializeProperties(prop);
	}
	
	/**
	 * Deserijalizira ovaj objekt
	 * @param src XML dio koji je stvoren sa serialice() metodom i opisuje ovaj objekt
	 * @throws ComponentFactoryException 
	 * 
	 */
	public void deserialize(String src){
		Properties prop=XMLUtil.deserializeProperties(src);
		position=new Point();		
		
		position.x=Integer.parseInt(prop.getProperty("compPosX"));
		position.y=Integer.parseInt(prop.getProperty("compPosY"));
		
		try {
			component=ComponentFactory.getSchemaComponent(prop.getProperty("compName"));
			component.deserializeComponent(prop.getProperty("compSource"));
		} catch (ComponentFactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
