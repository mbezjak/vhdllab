package hr.fer.zemris.vhdllab.applets.schema.components.misc;

import java.awt.Point;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaPort;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentPropertyList;
import hr.fer.zemris.vhdllab.applets.schema.components.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;

public class Sklop_PORT extends AbstractSchemaComponent {
	private static final int FROM_EDGE = 2;
	private static final int COMPONENT_WIDTH = 10;
	private static final int RECT_WIDTH = 7;
	private static final int TRIANGLE_WIDTH = 1;
	private static final int TRIANGLE_HEIGHT = 2;
	private static final int PER_PORT_HEIGHT = 2;
	
	private Port port;
	private CircuitInterface circint;

	public Sklop_PORT(Port port, CircuitInterface ci) {
		super(port.getName());
		setComponentName("PORT sklop");
		setPort(port);
		circint = ci;
	}

	@Override
	protected void updatePortCoordinates() {
		Direction dir = port.getDirection();
		if (dir == Direction.IN) {
			updatePortsIfIN();
		} else if (dir == Direction.OUT) {
			updatePortsIfOUT();
		}
	}
	
	private void updatePortsIfOUT() {
		int pos = FROM_EDGE + TRIANGLE_HEIGHT / 2;
		for (AbstractSchemaPort port : portlist) {
			port.setCoordinate(new Point(0, pos));
			pos += PER_PORT_HEIGHT;
		}
	}

	private void updatePortsIfIN() {
		int pos = FROM_EDGE + TRIANGLE_HEIGHT / 2;
		for (AbstractSchemaPort port : portlist) {
			port.setCoordinate(new Point(COMPONENT_WIDTH, pos));
			pos += PER_PORT_HEIGHT;
		}
	}

	@Override
	public AbstractSchemaComponent vCtr() {
		return new Sklop_PORT(port, circint);
	}
	
	

	@Override
	protected void addPropertiesToComponentPropertyList(ComponentPropertyList cplist) {
		// super.addPropertiesToComponentPropertyList(cplist);
		// zasad se sva svojstva mijenjaju iskljucivo iz postavki sucelja sklopa,
		// pa ovdje nista nije potrebno ni dodavati, cak niti zvati super
	}

	@Override
	public void drawSpecific(SchemaDrawingAdapter adapter) {
		String typename = port.getType().getTypeName();
		if (typename.compareTo("std_logic") == 0) {
			drawWhen_std_logic(adapter);
		} else if (typename.compareTo("std_logic_vector") == 0) {
			drawWhen_std_logic_vector(adapter);
		}
	}

	private void drawWhen_std_logic_vector(SchemaDrawingAdapter adapter) {
		int w = getComponentWidthSpecific();
		int h = getComponentHeightSpecific();
		
		if (port.getDirection() == Direction.IN) {
			for (AbstractSchemaPort sp : portlist) {
				Point p = sp.getCoordinate();
				adapter.drawLine(p.x, p.y, p.x - COMPONENT_WIDTH / 2, p.y);
				adapter.fillRightBlackTriangle(p.x - TRIANGLE_WIDTH - FROM_EDGE, p.y - TRIANGLE_HEIGHT / 2, TRIANGLE_WIDTH, TRIANGLE_HEIGHT);
			}
			adapter.fillRect(FROM_EDGE, FROM_EDGE,
					w - 2 * FROM_EDGE - TRIANGLE_WIDTH, h - 2 * FROM_EDGE);
			adapter.drawRect(FROM_EDGE, FROM_EDGE,
					w - 2 * FROM_EDGE - TRIANGLE_WIDTH, h - 2 * FROM_EDGE);
		} else if (port.getDirection() == Direction.OUT) {
			for (AbstractSchemaPort sp : portlist) {
				Point p = sp.getCoordinate();
				adapter.drawLine(p.x, p.y, p.x + COMPONENT_WIDTH / 2, p.y);
				adapter.fillRightBlackTriangle(w - FROM_EDGE - TRIANGLE_WIDTH, p.y - TRIANGLE_HEIGHT / 2, TRIANGLE_WIDTH, TRIANGLE_HEIGHT);
			}
			adapter.fillRect(FROM_EDGE, FROM_EDGE,
					w - 2 * FROM_EDGE - TRIANGLE_WIDTH, h - 2 * FROM_EDGE);
			adapter.drawRect(FROM_EDGE, FROM_EDGE,
					w - 2 * FROM_EDGE - TRIANGLE_WIDTH, h - 2 * FROM_EDGE);
		}
	}

	private void drawWhen_std_logic(SchemaDrawingAdapter adapter) {
		drawWhen_std_logic_vector(adapter);
	}

	@Override
	protected boolean deserializeComponentSpecific(String serial) {
		// TODO Auto-generated method stub - iako vjerojatno nece biti potrebno
		return false;
	}

	@Override
	protected String serializeComponentSpecific() {
		// TODO Auto-generated method stub - iako vjerojatno nece ni biti nuzno
		return null;
	}

	public int getComponentWidthSpecific() {
		return COMPONENT_WIDTH;
	}

	public int getComponentHeightSpecific() {
		String typename = port.getType().getTypeName();
		if (typename.compareTo("std_logic") == 0) {
			return height_std_logic();
		} else if (typename.compareTo("std_logic_vector") == 0) {
			return height_std_logic_vector();
		}
		return 0;
	}

	private int height_std_logic_vector() {
		int numports = calculateNumberOfPorts();
		return numports * PER_PORT_HEIGHT + FROM_EDGE * 2;
	}

	private int height_std_logic() {
		return PER_PORT_HEIGHT + FROM_EDGE * 2;
	}

	public Port getPort() {
		return port;
	}

	public void setPort(Port port) {
		this.port = port;
		
		updatePorts();
		updatePortCoordinates();
	}

	private void updatePorts() {
		portlist.clear();
		int portnum = calculateNumberOfPorts();
		AbstractSchemaPort.PortDirection pdir = AbstractSchemaPort.PortDirection.IN;
		if (port.getDirection() == Direction.IN) pdir = AbstractSchemaPort.PortDirection.OUT;
		for (int i = 0; i < portnum; i++) {
			SchemaPort p = new SchemaPort();
			p.setName("_" + i);
			p.setDirection(pdir);
			p.setOrientation((pdir == AbstractSchemaPort.PortDirection.OUT) ? (smjer) : (this.reverseOrient(smjer)));
			portlist.add(p);
		}
	}
	
	private int calculateNumberOfPorts() {
		Type ptip = port.getType();
		Integer portnum = 0;
		if (ptip.hasVectorDirectionTO()) {
			portnum = ptip.getRangeTo() - ptip.getRangeFrom() + 1;
		} else if (ptip.hasVectorDirectionDOWNTO()) {
			portnum = ptip.getRangeFrom() - ptip.getRangeTo() + 1;
		}
		return portnum;
	}
}
