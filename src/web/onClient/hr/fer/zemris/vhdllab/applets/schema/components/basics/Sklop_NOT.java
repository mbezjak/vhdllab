package hr.fer.zemris.vhdllab.applets.schema.components.basics;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaPort;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentPropertyList;
import hr.fer.zemris.vhdllab.applets.schema.components.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;

import java.awt.Point;
import java.util.Map;

public class Sklop_NOT extends AbstractSchemaComponent {
	private final static int SIRINA_NOT_VRATA = 9;
	private final static int VISINA_NOT_VRATA = 6;
	private final static int ODMAK_HORIZ = 2;
	private final static int ODMAK_VERT = 1;
	
	static {
		ComponentFactory.registerComponent(new Sklop_NOT("Sklop_NOT"));
	}
	
	public Sklop_NOT(String imeInstanceSklopa) {
		super(imeInstanceSklopa);
		setComponentName("Sklop_NOT");
		SchemaPort izlaz = new SchemaPort();
		izlaz.setName("Izlaz");
		izlaz.setDirection(AbstractSchemaPort.PortDirection.OUT);
		this.addPort(izlaz);
		SchemaPort ulaz = new SchemaPort();
		ulaz.setName("Ulaz");
		ulaz.setDirection(AbstractSchemaPort.PortDirection.IN);
		this.addPort(ulaz);
		updatePortCoordinates();
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
	public void drawSpecific(SchemaDrawingAdapter adapter) {
		adapter.drawLine(0, VISINA_NOT_VRATA / 2, SIRINA_NOT_VRATA, VISINA_NOT_VRATA / 2);
		adapter.draw4gon(
				ODMAK_HORIZ, VISINA_NOT_VRATA / 2,
				ODMAK_HORIZ, ODMAK_VERT,
				getComponentWidthSpecific() - ODMAK_HORIZ, VISINA_NOT_VRATA / 2,
				ODMAK_HORIZ, getComponentHeightSpecific() - ODMAK_VERT);
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

	public int getComponentWidthSpecific() {
		return SIRINA_NOT_VRATA;
	}

	public int getComponentHeightSpecific() {
		return VISINA_NOT_VRATA;
	}

	@Override
	public AbstractSchemaComponent vCtr() {
		return new Sklop_NOT((String) pComponentInstanceName.val);
	}

	@Override
	protected boolean deserializeComponentSpecific(String serial) {
		return true;
	}

	@Override
	protected String serializeComponentSpecific() {
		return "";
	}

	@Override
	public boolean isToBeSerialized() {
		return true;
	}

	@Override
	public boolean isToBeMapped() {
		return true;
	}

	@Override
	public String getEntityBlock() {
		return "\tport\n\t(ulaz: IN STD_LOGIC;\n\tizlaz: OUT STD_LOGIC_VECTOR\n\t);";
	}

	@Override
	public boolean hasComponentBlock() {
		return true;
	}

	@Override
	public String getMapping(Map<Integer, String> signalList) {
		String s = this.getComponentInstanceName() + ": " + this.getComponentName();
		s += " port map(";
		if (signalList.containsKey(1)) s += signalList.get(1) + ", ";
		else s += "open, ";
		if (signalList.containsKey(0)) s += signalList.get(0) + ");";
		else s += "open);";
		return s;
	}

	@Override
	public String getAdditionalSignals() {
		return "";
	}
	
}
