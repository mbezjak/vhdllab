package hr.fer.zemris.vhdllab.applets.schema.components.basics;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaPort;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentPropertyList;
import hr.fer.zemris.vhdllab.applets.schema.components.Ptr;
import hr.fer.zemris.vhdllab.applets.schema.components.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.GenericProperty;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;

import java.awt.Point;

import javax.swing.JTextField;

public class Sklop_MUX2nNA1 extends AbstractSchemaComponent {
	private final static int RAZMAK_IZMEDU_PORTOVA = 5;
	private final static int SIRINA_MUX2nNA1 = 12;
	private final static int ODMAK_OD_RUBA = 2;
	
	private Integer brojSelUlaza;
	
	static {
		ComponentFactory.registerComponent(new Sklop_MUX2nNA1("Sklop_MUX2nNA1"));
	}
	
	public Sklop_MUX2nNA1(String imeInstanceSklopa) {
		super(imeInstanceSklopa);
		setComponentName("Sklop_MUX2nNA1");
		SchemaPort izlaz = new SchemaPort();
		this.addPort(izlaz);
		brojSelUlaza = 0;
		setBrojSelUlaza(1);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent#addPropertiesToComponentPropertyList(hr.fer.zemris.vhdllab.applets.schema.components.ComponentPropertyList)
	 */
	@Override
	protected void addPropertiesToComponentPropertyList(ComponentPropertyList cplist) {
		super.addPropertiesToComponentPropertyList(cplist);
		GenericProperty prop1 = new GenericProperty("Broj selekcijskih ulaza", new Ptr<Object>(this)) {
			@Override
			public void onUpdate(JTextField tf) {
				Integer i = null;
				try {
					i = Integer.parseInt(tf.getText());
				}
				catch (Exception e) {
					i = 0;
				}
				if (i <= 0 || i > 10) {
					i = ((Sklop_MUX2nNA1)getSklopPtr().val).getBrojSelUlaza();
					tf.setText(i.toString());
					return;
				}
				((Sklop_MUX2nNA1)getSklopPtr().val).setBrojSelUlaza(i);				
			}

			@Override
			public void onLoad(JTextField tf) {
				tf.setText(((Sklop_MUX2nNA1)getSklopPtr().val).getBrojSelUlaza().toString());	
			}
		};
		cplist.add(prop1);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.ISchemaComponent#draw(hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter)
	 */
	public void drawSpecific(SchemaDrawingAdapter adapter) {
		for (AbstractSchemaPort port : portlist) {
			Point p = port.getCoordinate();
			if (port.getTipPorta() == "POD") {
				adapter.drawLine(p.x, p.y, p.x + ODMAK_OD_RUBA, p.y);
			} else if (port.getTipPorta() == "SEL") {
				adapter.drawLine(p.x, p.y, p.x, p.y - this.getComponentHeightSpecific() / 2);
			} else if (port.getTipPorta() == "IZL") {
				adapter.drawLine(p.x, p.y, p.x - ODMAK_OD_RUBA, p.y);
			}
		}
		adapter.draw4gon(
				ODMAK_OD_RUBA, ODMAK_OD_RUBA,
				this.getComponentWidthSpecific() - ODMAK_OD_RUBA, RAZMAK_IZMEDU_PORTOVA,
				this.getComponentWidthSpecific() - ODMAK_OD_RUBA, this.getComponentHeightSpecific() - RAZMAK_IZMEDU_PORTOVA,
				ODMAK_OD_RUBA, this.getComponentHeightSpecific() - ODMAK_OD_RUBA
				);
	}

	/**
	 * @return Returns the brojUlaza.
	 */
	public Integer getBrojSelUlaza() {
		return brojSelUlaza;
	}
	
	public Integer getBrojPodUlaza() {
		return portlist.size() - 1 - brojSelUlaza;
	}

	/**
	 * @param brojUlaza The brojUlaza to set.
	 */
	public void setBrojSelUlaza(Integer nbr) {
		System.out.println("Postavljam selekcijske na: " + nbr);
		if (nbr != brojSelUlaza) {
			// izracunaj 2^n
			int dvaNaN = 1;
			for (int i = 0; i < nbr; i++) {
				dvaNaN *= 2;
			}
			portlist.clear();
			SchemaPort port = null;
			port = new SchemaPort();
			port.setName("Izlaz");
			port.setDirection(AbstractSchemaPort.PortDirection.OUT);
			port.setTipPorta("IZL");
			portlist.add(port);
			for (int i = 0; i < nbr; i++) {
				port = new SchemaPort();
				port.setName("Selekcijski ulaz " + i);
				port.setDirection(AbstractSchemaPort.PortDirection.IN);
				port.setTipPorta("SEL");
				portlist.add(port);
			}
			for (int i = nbr; i < 10; i++) {
				port = new SchemaPort();
				port.setName("_boxing" + i);
				port.setDirection(AbstractSchemaPort.PortDirection.IN);
				port.setTipPorta("_boxing");
				portlist.add(port);
			}
			for (int i = 0; i < dvaNaN; i++) {
				port = new SchemaPort();
				port.setName("Podatkovni ulaz" + i);
				port.setDirection(AbstractSchemaPort.PortDirection.IN);
				port.setTipPorta("POD");
				portlist.add(port);
			}
			brojSelUlaza = nbr;
		}
		updatePortCoordinates();
	}
	
	/**
	 * Updatea koordinate portova.
	 */
	protected void updatePortCoordinates() {
		int pod = 0, sel = 0;
		for (AbstractSchemaPort port : portlist) {
			if (port.getDirection() == AbstractSchemaPort.PortDirection.OUT) {
				port.setCoordinate(new Point(SIRINA_MUX2nNA1, 
						getComponentHeightSpecific() / 2));
			} else {
				if (port.getTipPorta() == "SEL") {
					port.setCoordinate(new Point(
									(int) (ODMAK_OD_RUBA + 1.f * ((SIRINA_MUX2nNA1 - 2 * ODMAK_OD_RUBA) / (brojSelUlaza + 1) * (sel + 1))),
									RAZMAK_IZMEDU_PORTOVA * (portlist.size() - 10)));
					sel++;
				} else if (port.getTipPorta() == "POD") {
					port.setCoordinate(new Point(0, (pod + 1) * RAZMAK_IZMEDU_PORTOVA));
					pod++;
				}
			}
		}
	}

	public int getComponentWidthSpecific() {
		return SIRINA_MUX2nNA1;
	}

	public int getComponentHeightSpecific() {
		return (portlist.size() - 10) * RAZMAK_IZMEDU_PORTOVA;
	}

	@Override
	public AbstractSchemaComponent vCtr() {
		return new Sklop_MUX2nNA1((String) pComponentInstanceName.val);
	}

	@Override
	protected boolean deserializeComponentSpecific(String serial) {
		// ne koristiti xml ovdje!!
		String [] sfield = serial.split("#");
		
		try {
			setBrojSelUlaza(Integer.parseInt(sfield[1]));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	protected String serializeComponentSpecific() {
		return "#" + brojSelUlaza + "#";
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
		return "/tgeneric()/n/tport()";
	}
	
}
