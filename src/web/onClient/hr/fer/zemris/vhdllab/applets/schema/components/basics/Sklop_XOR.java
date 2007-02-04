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
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import javax.swing.JTextField;

public class Sklop_XOR extends AbstractSchemaComponent {
	private final static int RAZMAK_IZMEDU_PORTOVA = 4;
	private final static int SIRINA_XOR_VRATA = 14;
	private static final int ODMAK_OD_RUBA = 2;
	
	private Integer brojUlaza;
	
	static {
		ComponentFactory.registerComponent(new Sklop_XOR("Sklop_XOR"));
	}
	
	public Sklop_XOR(String imeInstanceSklopa) {
		super(imeInstanceSklopa);
		setComponentName("Sklop_XOR");
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
					i = ((Sklop_XOR)getSklopPtr().val).getBrojUlaza();
					tf.setText(i.toString());
					return;
				}
				((Sklop_XOR)getSklopPtr().val).setBrojUlaza(i);				
			}

			@Override
			public void onLoad(JTextField tf) {
				tf.setText(((Sklop_XOR)getSklopPtr().val).getBrojUlaza().toString());	
			}
		};
		cplist.add(prop1);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.ISchemaComponent#draw(hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter)
	 */
	public void drawSpecific(SchemaDrawingAdapter adapter) {
//		 draw wires to ports
		int w = getComponentWidthSpecific();
		int h = getComponentHeightSpecific();
		adapter.drawLine(w / 2, h / 2, w, h / 2);
		float koef = (1.5f * ODMAK_OD_RUBA - 0.5f * ODMAK_OD_RUBA) / (h * 0.5f - ODMAK_OD_RUBA);
		for (int i = 0; i < getBrojUlaza(); i++) {
			adapter.drawLine(0, (i + 1) * RAZMAK_IZMEDU_PORTOVA, 
					(i < getBrojUlaza() / 2) 
							? ((i + 1) * RAZMAK_IZMEDU_PORTOVA - ODMAK_OD_RUBA) * koef + ODMAK_OD_RUBA * 0.5f
							: ((i + 1) * RAZMAK_IZMEDU_PORTOVA - h + ODMAK_OD_RUBA) * (-koef) + ODMAK_OD_RUBA * 0.5f,
							(i + 1) * RAZMAK_IZMEDU_PORTOVA);
		}
		
		GeneralPath path = new GeneralPath();
		
		// draw component
		path.append(adapter.drawCubic(
				ODMAK_OD_RUBA, ODMAK_OD_RUBA,
				w / 2, h / 4,
				w * 2 / 3, h / 3,
				w - ODMAK_OD_RUBA, h / 2), true);
		path.append(adapter.drawCubic(
				w - ODMAK_OD_RUBA, h / 2,
				w * 2 / 3, h - h / 3,
				w / 2, h - h / 4,
				ODMAK_OD_RUBA, h - ODMAK_OD_RUBA), true);
		path.append(adapter.drawCubic(
				ODMAK_OD_RUBA, h - ODMAK_OD_RUBA,
				ODMAK_OD_RUBA * 1.5f, h - h / 4,
				ODMAK_OD_RUBA * 1.8f, h - h / 3,
				ODMAK_OD_RUBA * 2, h / 2), true);
		path.append(adapter.drawCubic(
				ODMAK_OD_RUBA * 2, h / 2,
				ODMAK_OD_RUBA * 1.8f, h / 3,
				ODMAK_OD_RUBA * 1.5f, h / 4,
				ODMAK_OD_RUBA, ODMAK_OD_RUBA
				), true);
		adapter.fill(path.createTransformedShape(new AffineTransform()));
		adapter.draw(path.createTransformedShape(new AffineTransform()));
		adapter.drawCubic(
				ODMAK_OD_RUBA / 2, h - ODMAK_OD_RUBA,
				ODMAK_OD_RUBA * 1.f, h - h / 4,
				ODMAK_OD_RUBA * 1.3f, h - h / 3,
				ODMAK_OD_RUBA * 3 / 2, h / 2
				);
		adapter.drawCubic(
				ODMAK_OD_RUBA * 3 / 2, h / 2,
				ODMAK_OD_RUBA * 1.3f, h / 3,
				ODMAK_OD_RUBA * 1.0f, h / 4,
				ODMAK_OD_RUBA / 2, ODMAK_OD_RUBA
				);
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
				port.setCoordinate(new Point(SIRINA_XOR_VRATA, portlist.size() * RAZMAK_IZMEDU_PORTOVA / 2));
			}
		}
	}

	public int getComponentWidthSpecific() {
		return SIRINA_XOR_VRATA;
	}

	public int getComponentHeightSpecific() {
		return portlist.size() * RAZMAK_IZMEDU_PORTOVA;
	}

	@Override
	public AbstractSchemaComponent vCtr() {
		return new Sklop_XOR((String) pComponentInstanceName.val);
	}

	@Override
	protected boolean deserializeComponentSpecific(String serial) {
		String [] sfield = serial.split("#");
		
		try {
			setBrojUlaza(Integer.parseInt(sfield[1]));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	protected String serializeComponentSpecific() {
		return "#" + brojUlaza + "#";
	}

	@Override
	public boolean isToBeSerialized() {
		return true;
	}
	
}
