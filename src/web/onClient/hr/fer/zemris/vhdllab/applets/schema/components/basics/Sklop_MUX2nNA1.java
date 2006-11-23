package hr.fer.zemris.vhdllab.applets.schema.components.basics;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaPort;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentPropertyList;
import hr.fer.zemris.vhdllab.applets.schema.components.Ptr;
import hr.fer.zemris.vhdllab.applets.schema.components.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.GenericProperty;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;

public class Sklop_MUX2nNA1 extends AbstractSchemaComponent {
	private final static int RAZMAK_IZMEDU_PORTOVA = 20;
	private final static int SIRINA_MUX2nNA1 = 75;
	
	private Integer brojSelUlaza;
	
	public Sklop_MUX2nNA1(String imeInstanceSklopa) {
		super(imeInstanceSklopa);
		pComponentName.val = "Multipleksor 2^n na 1";
		SchemaPort izlaz = new SchemaPort();
		izlaz.setName("Izlaz");
		izlaz.setDirection(AbstractSchemaPort.PortDirection.OUT);
		this.addPort(izlaz);
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
				if (i <= 0) {
					i = ((Sklop_MUX2nNA1)getSklopPtr().val).getBrojSelUlaza();
					tf.setText(i.toString());
					return;
				}
				((Sklop_MUX2nNA1)getSklopPtr().val).setBrojSelUlaza(i);				
			}
		};
		cplist.add(prop1);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.ISchemaComponent#draw(hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter)
	 */
	public void draw(SchemaDrawingAdapter adapter) {
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
		if (nbr != brojSelUlaza) {
			// izracunaj 2^n
			int dvaNaN = 1;
			for (int i = 0; i < nbr; i++) {
				dvaNaN *= 2;
			}
			portlist.clear();
			SchemaPort port = null;
			for (int i = 0; i < nbr; i++) {
				port = new SchemaPort();
				port.setName("Selekcijski ulaz " + i);
				port.setCoordinate(new Point(SIRINA_MUX2nNA1 + i * RAZMAK_IZMEDU_PORTOVA,
						RAZMAK_IZMEDU_PORTOVA * (1 + dvaNaN)));
				port.setDirection(AbstractSchemaPort.PortDirection.IN);
				port.setTipPorta("SEL");
				portlist.add(port);
			}
			for (int i = 0; i < dvaNaN; i++) {
				port = new SchemaPort();
				port.setName("Podatkovni ulaz" + i);
				port.setCoordinate(new Point(0, i * RAZMAK_IZMEDU_PORTOVA));
				port.setDirection(AbstractSchemaPort.PortDirection.IN);
				port.setTipPorta("POD");
				portlist.add(port);
			}
			port = new SchemaPort();
			port.setName("Izlaz");
			port.setCoordinate(new Point(SIRINA_MUX2nNA1, dvaNaN * RAZMAK_IZMEDU_PORTOVA / 2));
			port.setDirection(AbstractSchemaPort.PortDirection.OUT);
			portlist.add(port);
			brojSelUlaza = nbr;
		}
	}
	
	/**
	 * Updatea koordinate portova.
	 */
	protected void updatePortCoordinates() {
		for (AbstractSchemaPort port : portlist) {
		}
	}

	public int getComponentWidth() {
		return SIRINA_MUX2nNA1;
	}

	public int getComponentHeight() {
		return portlist.size() * RAZMAK_IZMEDU_PORTOVA;
	}
	
}
