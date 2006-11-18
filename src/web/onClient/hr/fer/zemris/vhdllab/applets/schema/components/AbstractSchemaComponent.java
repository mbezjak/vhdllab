package hr.fer.zemris.vhdllab.applets.schema.components;

import hr.fer.zemris.vhdllab.applets.schema.SchemaDrawingAdapter;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.NoEditProperty;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.TextProperty;

import java.awt.Point;

import sun.java2d.pipe.TextPipe;

/**
 * Smisao ove klase jest ponuditi funkcionalnost 
 * zajednicku svim sklopovima.
 * Primjerice, svaki sklop ima svojstvo Name. Ova komponenta to
 * svojstvo stavlja na listu svojstava. Svaka 
 * naslijedena komponenta potom zove: 
 * super.addToPropertyList()
 * prije dodavanja vlastitih svojstava kao sto su broj
 * ulaza, izlaza, itd.
 * @author Axel
 *
 */
public abstract class AbstractSchemaComponent implements ISchemaComponent {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.ISchemaComponent#draw(hr.fer.zemris.vhdllab.applets.schema.SchemaAdapter)
	 */
	public void draw(SchemaDrawingAdapter adapter) {
		// not applicable here
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.ISchemaComponent#getInPortCoordinate(int)
	 */
	public Point getInPortCoordinate(int portNum) {
		return null;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.ISchemaComponent#getNumberOfInPorts()
	 */
	public int getNumberOfInPorts() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.ISchemaComponent#getNumberOfOutPorts()
	 */
	public int getNumberOfOutPorts() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.ISchemaComponent#getOutPortCoordinate(int)
	 */
	public Point getOutPortCoordinate(int portNum) {
		return null;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.ISchemaComponent#getPropertyList()
	 */
	public ComponentPropertyList getPropertyList() {
		ComponentPropertyList cplist = new ComponentPropertyList();
		addPropertiesToComponentPropertyList(cplist);
		return cplist;
	}
	
	/**
	 * Konstruktor.
	 *
	 */
	public AbstractSchemaComponent() {
		pComponentName = new Ptr<Object>();
		pComponentName.val = new String();
		pComponentInstanceName = new Ptr<Object>();
		pComponentInstanceName.val = new String("Mihelj sam ja, onaj, Herpes!");
	}
	
	private Ptr<Object> pComponentName;
	public String getComponentName() {
		return (String)pComponentName.val;
	}
	public void setComponentName(String name) {
		this.pComponentName.val = name;
	}
	
	private Ptr<Object> pComponentInstanceName;
	public String getComponentInstanceName() {
		return (String)pComponentInstanceName.val;
	}
	public void setComponentInstanceName(String name) {
		this.pComponentInstanceName.val = name;
	}

	/**
	 * Adds properties to the component property list.
	 * Inherited classes override this method, but call this
	 * method from their ancestors to get a complete property
	 * list.
	 * @param cplist Property list.
	 */
	protected void addPropertiesToComponentPropertyList(ComponentPropertyList cplist) {
//		Ptr<Object> pcn = new Ptr<Object>();
//		pcn.val = componentName;
//		Ptr<Object> pcin = new Ptr<Object>();
//		pcin.val = componentInstanceName;
		cplist.add(new NoEditProperty("Ime sklopa", pComponentName));
		cplist.add(new TextProperty("Ime instance sklopa", pComponentInstanceName));
	}
}




