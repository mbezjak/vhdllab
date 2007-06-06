package hr.fer.zemris.vhdllab.applets.schema2.model;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.xerces.internal.util.DraconianErrorHandler;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IWireDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SMath;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.schema2.model.drawers.DefaultWireDrawer;
import hr.fer.zemris.vhdllab.vhdl.model.Type;




public class SchemaWire implements ISchemaWire {

	private Caseless name;
	private List<XYLocation> nodes;
	private IParameterCollection parameters;
	private List<WireSegment> segments;
	private DefaultWireDrawer wiredrawer;
	
	
	
	
	public SchemaWire(Caseless wireName) {
		name = wireName;
		
		create();
	}
	
	
	private void create() {
		nodes = new ArrayList<XYLocation>();
		parameters = new SchemaParameterCollection();
		segments = new ArrayList<WireSegment>();
		wiredrawer = new DefaultWireDrawer(this);
	}
	
	
	
	
	public ISchemaWire copyCtor() {
		throw new NotImplementedException();
	}

	public Rectangle getBounds() {
		Rectangle r = new Rectangle();
		int xmax = 0, ymax = 0;
		
		if (segments.size() > 0) {
			r.x = SMath.min(segments.get(0).loc1.x, segments.get(0).loc2.x);
			xmax = SMath.max(segments.get(0).loc1.x, segments.get(0).loc2.x);
			r.y = SMath.min(segments.get(0).loc1.y, segments.get(0).loc2.y);
			ymax = SMath.max(segments.get(0).loc1.y, segments.get(0).loc2.y);
		}
		
		for (int i = 1; i < segments.size(); i++) {
			r.x = SMath.min(segments.get(i).loc1.x, r.x);
			r.x = SMath.min(segments.get(i).loc2.x, r.x);
			xmax = SMath.max(segments.get(i).loc1.x, xmax);
			xmax = SMath.max(segments.get(i).loc2.x, xmax);
			r.y = SMath.min(segments.get(i).loc1.y, r.y);
			r.y = SMath.min(segments.get(i).loc2.y, r.y);
			ymax = SMath.max(segments.get(i).loc1.y, ymax);
			ymax = SMath.max(segments.get(i).loc2.y, ymax);
		}
		
		r.width = xmax - r.x;
		r.height = ymax - r.y;
		
		return r;
	}

	public IWireDrawer getDrawer() {
		return wiredrawer;
	}

	public Caseless getName() {
		return name;
	}

	public List<XYLocation> getNodes() {
		return nodes;
	}

	public IParameterCollection getParameters() {
		return parameters;
	}

	public List<WireSegment> getSegments() {
		return segments;
	}

	public void deserialize(String code) {
		throw new NotImplementedException();
	}

	public String serialize() {
		throw new NotImplementedException();
	}

	

}























