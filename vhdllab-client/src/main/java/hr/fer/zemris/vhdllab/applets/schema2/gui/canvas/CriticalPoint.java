package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class CriticalPoint {
	
	public static int MAX_RADIUS = 20;
	public static final int ON_COMPONENT_PLUG = 0;
	public static final int ON_WIRE_PLUG = 1;

	private int x;
	private int y;
	
	private int type = 0;
	
	private Caseless name = null;
	private Caseless portName = null;
	
	public CriticalPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public CriticalPoint(int x, int y, int type, Caseless name, Caseless portName) {
		this(x,y);
		this.name=name;
		this.type=type;
		this.portName=portName;
	}
	
	public CriticalPoint(ISchemaWire wire, int x2, int y2) {
		// TODO jeli algoritam dobar
		this.x = x2;
		this.y = y2;
		name=wire.getName();
		type = CriticalPoint.ON_WIRE_PLUG;
		
		findLocationOnPlug(wire, x2,y2);
	}



	public void draw(Graphics2D g,int radius){
		Color cl = g.getColor();
		g.setColor(type == ON_COMPONENT_PLUG?
				CanvasColorProvider.CRITICAL_PORT:
					CanvasColorProvider.CRITICAL_WIRE);
		g.drawOval(x-radius/2, y-radius/2, radius, radius);
		g.setColor(cl);
	}

	public Caseless getName() {
		return name;
	}

	public void setName(Caseless name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Caseless getPortName() {
		return portName;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	private void findLocationOnPlug(ISchemaWire wire, int x, int y) {
		List<WireSegment> segmentList = wire.getSegments();
		
		int distance = 10;
		WireSegment clSegment = null;
		
		for(WireSegment segment:segmentList){
			if(segment.calcDist(x, y)<distance) {
				distance = segment.calcDist(x, y);
				clSegment = segment;
			}
		}
		
		if(clSegment.isVertical()){
			this.x = clSegment.getStart().x;
		}else{
			this.y = clSegment.getStart().y;
		}
		
		if(calculateDistance(clSegment.getStart())<10){
			this.x = clSegment.getStart().getX();
			this.y = clSegment.getStart().getY();
		}
		
		if(calculateDistance(clSegment.getEnd())<10){
			this.x = clSegment.getEnd().getX();
			this.y = clSegment.getEnd().getY();
		}
		
		List<XYLocation> nodeLocations = wire.getNodes();
		for(XYLocation xy:nodeLocations){
			if(calculateDistance(xy)<10){
				this.x = xy.x;
				this.y = xy.y;
			}
		}
	}

	private double calculateDistance(XYLocation xy) {
		return Math.sqrt((x-xy.x)*(x-xy.x)+(y-xy.y)*(y-xy.y));
	}
}
