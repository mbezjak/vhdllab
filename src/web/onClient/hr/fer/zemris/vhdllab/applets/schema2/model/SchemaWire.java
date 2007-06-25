package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.ParameterNotFoundException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IWireDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SMath;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.schema2.model.drawers.DefaultWireDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.CaselessParameter;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.ParameterFactory;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.ParameterWrapper;

import java.awt.Rectangle;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;




public class SchemaWire implements ISchemaWire {

	private List<XYLocation> nodes;
	private IParameterCollection parameters;
	private List<WireSegment> segments;
	private IWireDrawer wiredrawer;
	private ParameterFactory paramFactory;
	
	
	public SchemaWire() {
		create();
	}
	
	public SchemaWire(Caseless wireName) {
		create();
		initDefaultParameters(wireName);
	}
	
	
	
	
	private void initDefaultParameters(Caseless wireName) {
		CaselessParameter caspar = new CaselessParameter(ISchemaWire.KEY_NAME, false, wireName);
		parameters.addParameter(caspar);
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
			r.x = SMath.min(segments.get(0).getStart().x, segments.get(0).getEnd().x);
			xmax = SMath.max(segments.get(0).getStart().x, segments.get(0).getEnd().x);
			r.y = SMath.min(segments.get(0).getStart().y, segments.get(0).getEnd().y);
			ymax = SMath.max(segments.get(0).getStart().y, segments.get(0).getEnd().y);
		}
		
		for (int i = 1; i < segments.size(); i++) {
			r.x = SMath.min(segments.get(i).getStart().x, r.x);
			r.x = SMath.min(segments.get(i).getEnd().x, r.x);
			xmax = SMath.max(segments.get(i).getStart().x, xmax);
			xmax = SMath.max(segments.get(i).getEnd().x, xmax);
			r.y = SMath.min(segments.get(i).getStart().y, r.y);
			r.y = SMath.min(segments.get(i).getEnd().y, r.y);
			ymax = SMath.max(segments.get(i).getStart().y, ymax);
			ymax = SMath.max(segments.get(i).getEnd().y, ymax);
		}
		
		r.width = xmax - r.x;
		r.height = ymax - r.y;
		
		return r;
	}

	public IWireDrawer getDrawer() {
		return wiredrawer;
	}

	public Caseless getName() {
		try {
			return (Caseless)(parameters.getValue(ISchemaWire.KEY_NAME));
		} catch (ParameterNotFoundException e) {
			throw new RuntimeException("Name parameter not found.");
		}
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
	
	/**
	 * Dodaje cvor u listu cvorova.
	 * 
	 * @param node
	 */
	public void addNode(XYLocation node) {
		nodes.add(node);
	}
	
	public void insertSegment(WireSegment segment) {
		List<XYLocation> segnodes = findNodesForSegment(segment, false, true);
		
		segments.add(segment);
		for (XYLocation node : segnodes) {
			nodes.add(node);
		}
	}
	
	public boolean removeSegment(WireSegment segment) {
		if (!segments.contains(segment)) return false;
		
		List<XYLocation> segnodes = findNodesForSegment(segment, true, true);
		
		segments.remove(segment);
		for (XYLocation node : segnodes) {
			nodes.remove(node);
		}
		
		return true;
	}
	
	/**
	 * Trazi cvorove koji su povezani s ovim segmentom.
	 * 
	 * @param segadded
	 * Ako je segment vec u listi segmenata, ovaj parametar
	 * mora biti true. U tom slucaju ce se vratiti samo oni
	 * cvorovi koji postoje radi ovog segmenta, i ujedno su
	 * u listi cvorova. U protivnom se nadeni cvorovi nece
	 * usporedivati s listom postojecih cvorova.
	 * 
	 * @param exclusive
	 * Ako je naveden true, funkcija ce vratiti samo one cvorove
	 * koji su vezani iskljucivo uz navedeni segment.
	 */
	private List<XYLocation> findNodesForSegment(WireSegment segment, boolean segadded, boolean exclusive) {
		List<XYLocation> segnodes = new ArrayList<XYLocation>();
		
		for (WireSegment otherseg : segments) {
			if (segadded && segment.equals(otherseg)) continue;
			
			// find a crosspoint, see if it is exclusive
			XYLocation crosspoint = segment.intersection(otherseg);
			if (crosspoint != null) {
				if (segadded && !nodes.contains(crosspoint)) continue;
				
				if (exclusive) {
					// see if other segments cause this intersection
					boolean found = false;
					for (WireSegment anyother : segments) {
						if (anyother.equals(segment) || anyother.equals(otherseg)) continue;
						if (anyother.intersection(otherseg) != null) {
							found = true;
							break;
						}
					}
					if (found) continue;
				}
				segnodes.add(crosspoint);
				continue;
			}
			
			// find an edgepoint
			crosspoint = segment.edgepoint(otherseg);
			if (crosspoint != null) {
				if (segadded && !nodes.contains(crosspoint)) continue;
				
				// now see whether this edgepoint is shared, and by how many segments
				int count = 0;
				for (WireSegment anyother : segments) {
					if (anyother.equals(segment) || anyother.equals(otherseg)) continue;
					if (anyother.edgepoint(otherseg) != null) count++;
				}
				if (count == 0) continue;
				if (exclusive && count > 1) continue;
				segnodes.add(crosspoint);
			}
		}
		
		return segnodes;
	}
	

	/**
	 * Dodaje segment zice u listu segmenata,
	 * ne dodajuci pritom cvorove na presjecista.
	 * 
	 * @param segment
	 */
	public void addWireSegment(WireSegment segment) {
		segments.add(segment);
	}

	public void setName(String name) {
		try {
			parameters.setValue(ISchemaWire.KEY_NAME, new Caseless(name));
		} catch (ParameterNotFoundException e) {
			throw new RuntimeException("Parameter name could not be set.");
		}
	}
	
	public void setDrawer(String drawerName) {
		try {
			Class cls = Class.forName(drawerName);
			Class[] partypes = new Class[1];
			partypes[0] = ISchemaWire.class;
			Constructor<IWireDrawer> ct = cls.getConstructor(partypes);
			wiredrawer = ct.newInstance();
		} catch (Exception e) {
			wiredrawer = new DefaultWireDrawer(this);
		}
	}
	
	public void addParameter(ParameterWrapper pw) {
		if (paramFactory == null) paramFactory = new ParameterFactory();
		
		IParameter param = paramFactory.createParameter(pw);
		
		parameters.addParameter(param);
	}

	

}























