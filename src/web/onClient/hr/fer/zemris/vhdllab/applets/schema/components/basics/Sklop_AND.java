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
import java.util.Map;

import javax.swing.JTextField;

public class Sklop_AND extends AbstractSchemaComponent {
	private final static int RAZMAK_IZMEDU_PORTOVA = 4;
	private final static int SIRINA_AND_VRATA = 14;
	private final static int ODMAK_OD_RUBA = 2;
	
	private Integer brojUlaza;
	
	static {
		ComponentFactory.registerComponent(new Sklop_AND("Sklop_AND"));
	}
	
	public Sklop_AND(String imeInstanceSklopa) {
		super(imeInstanceSklopa);
		setComponentName("Sklop_AND");
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

			@Override
			public void onLoad(JTextField tf) {
				tf.setText(((Sklop_AND)getSklopPtr().val).getBrojUlaza().toString());	
			}
		};
		cplist.add(prop1);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.ISchemaComponent#draw(hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter)
	 */
	public void drawSpecific(SchemaDrawingAdapter adapter) {		
		// draw wires to ports
		int w = getComponentWidthSpecific();
		int h = getComponentHeightSpecific();
		adapter.drawLine(w / 2, h / 2, w, h / 2);
		for (int i = 0; i < getBrojUlaza(); i++) {
			adapter.drawLine(0, (i + 1) * RAZMAK_IZMEDU_PORTOVA, w / 2, (i + 1) * RAZMAK_IZMEDU_PORTOVA);
		}
		
		// draw component
		int diameterY = (getComponentHeightSpecific() - 2 * ODMAK_OD_RUBA);
		int diameterX = (getComponentWidthSpecific() - 2 * ODMAK_OD_RUBA);
		adapter.fillRect(ODMAK_OD_RUBA, ODMAK_OD_RUBA, getComponentWidthSpecific() / 2 - ODMAK_OD_RUBA + 1, getComponentHeightSpecific() - ODMAK_OD_RUBA * 2);
		adapter.fillOvalSegment((getComponentWidthSpecific() - diameterX) / 2, (getComponentHeightSpecific() - diameterY) / 2, diameterX, diameterY, -90, 180);
		adapter.drawThickLine(ODMAK_OD_RUBA, ODMAK_OD_RUBA, ODMAK_OD_RUBA, getComponentHeightSpecific() - ODMAK_OD_RUBA, 1.15f);
		adapter.drawThickLine(ODMAK_OD_RUBA, ODMAK_OD_RUBA, getComponentWidthSpecific() / 2 + 1, ODMAK_OD_RUBA, 1.15f);
		adapter.drawThickLine(ODMAK_OD_RUBA, getComponentHeightSpecific() - ODMAK_OD_RUBA, getComponentWidthSpecific() / 2 + 1, getComponentHeightSpecific() - ODMAK_OD_RUBA, 1.15f);
		adapter.drawOvalSegment((getComponentWidthSpecific() - diameterX) / 2, (getComponentHeightSpecific() - diameterY) / 2, diameterX, diameterY, -90, 180);
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

	public int getComponentWidthSpecific() {
		return SIRINA_AND_VRATA;
	}

	public int getComponentHeightSpecific() {
		return portlist.size() * RAZMAK_IZMEDU_PORTOVA;
	}

	@Override
	public AbstractSchemaComponent vCtr() {
		return new Sklop_AND((String) pComponentInstanceName.val);
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

	@Override
	public boolean isToBeMapped() {
		return true;
	}

	@Override
	public String getEntityBlock() {
		return "\tgeneric(\n\tdel : time;\n\tbrUlaza: natural);\n\tport(\n\tulazi: STD_LOGIC_VECTOR(brUlaza DOWNTO 1)\n\tizlaz: STD_LOGIC;\n\t);";
	}

	@Override
	public boolean hasComponentBlock() {
		return true;
	}

	@Override
	public String getMapping(Map<Integer, String> signalList) {
		String s = "";
		for (int i = brojUlaza; i >= 1; i--) {
			s += this.getComponentInstanceName() + "_vector(" + i + ") <= ";
			if (signalList.containsKey(i)) s += signalList.get(i) + ";\n";
			else s += "open;\n";
		}
		s += this.getComponentInstanceName() + ": " + this.getComponentName();
		s += " generic map (" + this.getComponentDelay() + ", " + getBrojUlaza() + ") ";
		s += " port map(" + this.getComponentInstanceName() + "_vector, ";
		if (signalList.containsKey(0)) s += signalList.get(0) + ");";
		else s += "open);";
		return s;
	}

	@Override
	public String getAdditionalSignals() {
		return "SIGNAL " + this.getComponentInstanceName() + "_vector\t: STD_LOGIC_VECTOR(" + this.getBrojUlaza() + " DOWNTO 1);";
	}
	
}
