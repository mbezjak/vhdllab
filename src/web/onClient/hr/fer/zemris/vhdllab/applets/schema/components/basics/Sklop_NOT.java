package hr.fer.zemris.vhdllab.applets.schema.components.basics;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaPort;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentPropertyList;
import hr.fer.zemris.vhdllab.applets.schema.components.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;

import java.awt.Point;

public class Sklop_NOT extends AbstractSchemaComponent {
	private final static int SIRINA_NOT_VRATA = 45;
	private final static int VISINA_NOT_VRATA = 26;
	
	public Sklop_NOT(String imeInstanceSklopa) {
		super(imeInstanceSklopa);
		pComponentName.val = "NOT sklop";
		SchemaPort izlaz = new SchemaPort();
		izlaz.setName("Izlaz");
		izlaz.setDirection(AbstractSchemaPort.PortDirection.OUT);
		this.addPort(izlaz);
		SchemaPort ulaz = new SchemaPort();
		ulaz.setName("Ulaz");
		ulaz.setDirection(AbstractSchemaPort.PortDirection.IN);
		this.addPort(ulaz);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent#addPropertiesToComponentPropertyList(hr.fer.zemris.vhdllab.applets.schema.components.ComponentPropertyList)
	 */
	@Override
	protected void addPropertiesToComponentPropertyList(ComponentPropertyList cplist) {
		super.addPropertiesToComponentPropertyList(cplist);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.ISchemaComponent#draw(hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter)
	 */
	public void draw(SchemaDrawingAdapter adapter) {
		super.draw(adapter);
	}
	
	/**
	 * Updatea koordinate portova.
	 */
	protected void updatePortCoordinates() {
		for (AbstractSchemaPort port : portlist) {
			if (port.getDirection() == AbstractSchemaPort.PortDirection.IN) {
				port.setCoordinate(new Point(0, VISINA_NOT_VRATA / 2));
			} else {
				port.setCoordinate(new Point(SIRINA_NOT_VRATA, VISINA_NOT_VRATA / 2));
			}
		}
	}

	public int getComponentWidth() {
		return SIRINA_NOT_VRATA;
	}

	public int getComponentHeight() {
		return VISINA_NOT_VRATA;
	}

	@Override
	public AbstractSchemaComponent vCtr() {
		return new Sklop_NOT((String) pComponentInstanceName.val);
	}
	
}
