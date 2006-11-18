package hr.fer.zemris.vhdllab.applets.schema.components;

import hr.fer.zemris.vhdllab.applets.schema.SchemaDrawingAdapter;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.NumProperty;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.TextProperty;

import java.awt.Point;

public class PrivremeniProbniSklop extends AbstractSchemaComponent {
	private String maliTekst;
	private Integer mojInt;
	
	public PrivremeniProbniSklop() {
		mojInt = 15;
		maliTekst = "Tek tolko da nest pise.";
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent#addPropertiesToComponentPropertyList(hr.fer.zemris.vhdllab.applets.schema.components.ComponentPropertyList)
	 */
	@Override
	protected void addPropertiesToComponentPropertyList(ComponentPropertyList cplist) {
		super.addPropertiesToComponentPropertyList(cplist);
		cplist.add(new TextProperty("Tekstic", new Ptr<Object>(maliTekst)));
		cplist.add(new NumProperty("Brojcek", new Ptr<Object>(mojInt)));
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent#draw(hr.fer.zemris.vhdllab.applets.schema.SchemaAdapter)
	 */
	@Override
	public void draw(SchemaDrawingAdapter adapter) {
		adapter.drawRect(0, 0, 100, 100);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent#getInPortCoordinate(int)
	 */
	@Override
	public Point getInPortCoordinate(int portNum) {
		// TODO Auto-generated method stub
		return super.getInPortCoordinate(portNum);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent#getNumberOfInPorts()
	 */
	@Override
	public int getNumberOfInPorts() {
		// TODO Auto-generated method stub
		return super.getNumberOfInPorts();
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent#getNumberOfOutPorts()
	 */
	@Override
	public int getNumberOfOutPorts() {
		// TODO Auto-generated method stub
		return super.getNumberOfOutPorts();
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent#getOutPortCoordinate(int)
	 */
	@Override
	public Point getOutPortCoordinate(int portNum) {
		// TODO Auto-generated method stub
		return super.getOutPortCoordinate(portNum);
	}
	
}
