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

public class Sklop_AND extends AbstractSchemaComponent {
	private final static int RAZMAK_IZMEDU_PORTOVA = 5;
	private final static int SIRINA_AND_VRATA = 9;
	
	private Integer brojUlaza;
	
	static {
		setComponentName("AND sklop");
		ComponentFactory.registerComponent(new Sklop_AND("Sklop_AND"));
	}
	
	public Sklop_AND(String imeInstanceSklopa) {
		super(imeInstanceSklopa);
		SchemaPort izlaz = new SchemaPort();
		izlaz.setName("Izlaz");
		izlaz.setDirection(AbstractSchemaPort.PortDirection.OUT);
		this.addPort(izlaz);
		brojUlaza = 0;
		setBrojUlaza(2);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent#addPropertiesToComponentPropertyList(hr.fer.zemris.vhdllab.applets.schema.components.ComponentPropertyList)
	 */
	@Override
	protected void addPropertiesToComponentPropertyList(ComponentPropertyList cplist) {
		super.addPropertiesToComponentPropertyList(cplist);
		GenericProperty prop1 = new GenericProperty("Broj ulaznih portova", new Ptr<Object>(this)) {
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
					i = ((Sklop_AND)getSklopPtr().val).getBrojUlaza();
					tf.setText(i.toString());
					return;
				}
				((Sklop_AND)getSklopPtr().val).setBrojUlaza(i);				
			}
		};
		cplist.add(prop1);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.ISchemaComponent#draw(hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter)
	 */
	public void draw(SchemaDrawingAdapter adapter) {
		super.draw(adapter);
	}

	/**
	 * @return Returns the brojUlaza.
	 */
	public Integer getBrojUlaza() {
		return brojUlaza;
	}

	/**
	 * @param brojUlaza The brojUlaza to set.
	 */
	public void setBrojUlaza(Integer nbr) {
		if (nbr > brojUlaza) {
			while (nbr > brojUlaza) {
				SchemaPort port = new SchemaPort();
				port.setName((brojUlaza + 1) + ". ulaz");
				port.setDirection(AbstractSchemaPort.PortDirection.IN);
				port.setOrientation(this.reverseOrient(smjer));
				portlist.add(port);
				brojUlaza++;
			}
			updatePortCoordinates();
		}
		if (nbr < brojUlaza) {
			while (nbr < brojUlaza) {
				int i = 0;
				for (AbstractSchemaPort port : portlist) {
					if (port.getDirection() ==  AbstractSchemaPort.PortDirection.IN) break;
					i++;
				}
				portlist.remove(i);
				brojUlaza--;
			}
			updatePortCoordinates();
		}
	}
	
	/**
	 * Updatea koordinate portova.
	 */
	protected void updatePortCoordinates() {
		int pos = 0;
		for (AbstractSchemaPort port : portlist) {
			if (port.getDirection() == AbstractSchemaPort.PortDirection.IN) {
				port.setCoordinate(new Point(0, (pos + 1) * RAZMAK_IZMEDU_PORTOVA));
				pos++;
			} else {
				port.setCoordinate(new Point(SIRINA_AND_VRATA, portlist.size() * RAZMAK_IZMEDU_PORTOVA / 2));
			}
		}
	}

	public int getComponentWidth() {
		return SIRINA_AND_VRATA;
	}

	public int getComponentHeight() {
		return portlist.size() * RAZMAK_IZMEDU_PORTOVA;
	}

	@Override
	public AbstractSchemaComponent vCtr() {
		return new Sklop_AND((String) pComponentInstanceName.val);
	}

	@Override
	protected boolean deserializeComponentSpecific(String serial) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String serializeComponentSpecific() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
