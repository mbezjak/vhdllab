package hr.fer.zemris.vhdllab.applets.schema;

import java.awt.Point;

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
	public void draw(SchemaAdapter adapter) {
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
	 * Adds properties to the component property list.
	 * Inherited classes override this method, but call this
	 * method from their ancestors to get a complete property
	 * list.
	 * @param cplist Property list.
	 */
	protected void addPropertiesToComponentPropertyList(ComponentPropertyList cplist) {
	}
	
}
