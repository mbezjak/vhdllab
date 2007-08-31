package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ParameterWrapper;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.ParameterNotFoundException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IWireDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Rect2d;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SMath;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.schema2.model.drawers.DefaultWireDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.CaselessParameter;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.ParameterFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;




public class SchemaWire implements ISchemaWire {

	private List<XYLocation> nodes;
	private Map<XYLocation, Integer> nodeuses;
	private Set<XYLocation> edgepoints;
	private IParameterCollection parameters;
	private List<WireSegment> segments;
	private IWireDrawer wiredrawer;
	
	
	/**
	 * Za deserijalizaciju.
	 */
	public SchemaWire() {
		create();
	}
	
	/**
	 * Koristiti ovaj konstruktor.
	 */
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
		nodeuses = new HashMap<XYLocation, Integer>();
		edgepoints = new HashSet<XYLocation>();
		parameters = new SchemaParameterCollection();
		segments = new ArrayList<WireSegment>();
		wiredrawer = new DefaultWireDrawer(this);
	}
	
	
	
	
	public ISchemaWire copyCtor() {
		throw new NotImplementedException();
	}

	public Rect2d getBounds() {
		Rect2d r = new Rect2d();
		int xmax = 0, ymax = 0;
		
		if (segments.size() > 0) {
			r.left = SMath.min(segments.get(0).getStart().x, segments.get(0).getEnd().x);
			xmax = SMath.max(segments.get(0).getStart().x, segments.get(0).getEnd().x);
			r.top = SMath.min(segments.get(0).getStart().y, segments.get(0).getEnd().y);
			ymax = SMath.max(segments.get(0).getStart().y, segments.get(0).getEnd().y);
		}
		
		for (int i = 1; i < segments.size(); i++) {
			r.left = SMath.min(segments.get(i).getStart().x, r.left);
			r.left = SMath.min(segments.get(i).getEnd().x, r.left);
			xmax = SMath.max(segments.get(i).getStart().x, xmax);
			xmax = SMath.max(segments.get(i).getEnd().x, xmax);
			r.top = SMath.min(segments.get(i).getStart().y, r.top);
			r.top = SMath.min(segments.get(i).getEnd().y, r.top);
			ymax = SMath.max(segments.get(i).getStart().y, ymax);
			ymax = SMath.max(segments.get(i).getEnd().y, ymax);
		}
		
		r.width = xmax - r.left;
		r.height = ymax - r.top;
		
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
	 * Provjerava da li se segment preklapa s nekim
	 * od postojecih segmenata.
	 * @param segment
	 * Segment NIJE u listi segmenata.
	 * @return
	 * Vraca true ako postoji preklapanje.
	 */
	private boolean doesOverlap(WireSegment segment) {
		XYLocation start = segment.getStart(), end = segment.getEnd();
		if (segment.isVertical()) {
			/* segment vertical */
			for (WireSegment ws : segments) {
				if (!ws.isVertical()) continue;
				XYLocation wsst = ws.getStart(), wsnd = ws.getEnd();
				if ((wsst.x == start.x) && 
					(SMath.insideOrd(start.y, wsst.y, wsnd.y) || SMath.insideOrd(end.y, wsst.y, wsnd.y)))
					return true;
			}
		} else {
			/* segment horizontal */
			for (WireSegment ws : segments) {
				if (ws.isVertical()) continue;
				XYLocation wsst = ws.getStart(), wsnd = ws.getEnd();
				if ((wsst.y == start.y) && 
					(SMath.insideOrd(start.x, wsst.x, wsnd.x) || SMath.insideOrd(end.x, wsst.x, wsnd.x)))
					return true;
			}
		}
		
		return false;
	}
	
	public Set<WireSegment> segmentsAt(int x, int y) {
		Set<WireSegment> wsset = null;
		
		for (WireSegment seg : segments) {
			if (seg.hasPoint(x, y)) {
				if (wsset == null) wsset = new HashSet<WireSegment>();
				wsset.add(seg);
			}
		}
		
		return wsset;
	}
	
	public boolean insertSegment(WireSegment segment) {
		/* check overlap */
		if (doesOverlap(segment)) return false;
		
		/* find intersections and edgepoints for this segment */
		List<XYLocation> segnodes = findIntersections(segment);
		List<XYLocation> segedges = findEdgepoints(segment);
		
		/* add segment and nodes for intersections */
		segments.add(segment);
		for (XYLocation node : segnodes) {
			addAndMapNode(node);
		}
		
		/* handle edge points */
		for (XYLocation ep : segedges) {
			processEdgepointAddition(ep);
		}
		
		return true;
	}
	
	private void processEdgepointAddition(XYLocation ep) {
		if (edgepoints.contains(ep)) {
			/* edge point already exists - check for a node */
			if (nodeuses.containsKey(ep)) {
				/* node already exists too - increase counter */
				nodeuses.put(ep, nodeuses.get(ep) + 1);
			} else {
				/* node does not exist - add it */
				nodes.add(ep);
				nodeuses.put(ep, 1);
			}
		} else {
			/* edge point does not exist - add it */
			edgepoints.add(ep);
		}
	}

	/**
	 * Vraca popis dodira rubova vezanih uz segment.
	 * BITNO: Pritom se pretpostavlja da segment
	 * jos nije u listi segmenata.
	 * @param segment
	 * @return
	 */
	private List<XYLocation> findEdgepoints(WireSegment segment) {
		Set<XYLocation> edges = new HashSet<XYLocation>();
		
		/* find all edge points */
		for (WireSegment ws : segments) {
			XYLocation edgepoint = ws.edgepoint(segment);
			if (edgepoint != null) edges.add(edgepoint);
		}
		
		return new ArrayList<XYLocation>(edges);
	}

	/**
	 * Vraca popis presjecista vezanih uz segment.
	 * BITNO: Pretpostavlja da zadani segment NIJE
	 * u listi segmenata.
	 * @param segment
	 * Segment koji se zeli dodati, ili koji je upravo
	 * obrisan, a koji NIJE u listi segmenata.
	 * @return
	 * Praznu listu ako nema presjecista. Nece vratiti null.
	 */
	private List<XYLocation> findIntersections(WireSegment segment) {
		List<XYLocation> nodes = new ArrayList<XYLocation>();
		
		/* find all intersections */
		for (WireSegment ws : segments) {
			XYLocation intersectpoint = ws.intersection(segment);
			if (intersectpoint != null) nodes.add(intersectpoint);
		}
		
		return nodes;
	}
	
	public boolean removeSegment(WireSegment segment) {
		if (!segments.contains(segment)) return false;
		
		/* first remove segment */
		segments.remove(segment);
		
		/* search for intersections and edgepoints caused by this segment */
		List<XYLocation> segnodes = findIntersections(segment);
		List<XYLocation> segedges = findEdgepoints(segment);
		for (XYLocation node : segnodes) {
			removeAndDemapNode(node);
		}
		
		/* handle edge points */
		for (XYLocation ep : segedges) {
			processEdgepointRemoval(ep);
		}
		
		return true;
	}
	
	private void processEdgepointRemoval(XYLocation ep) {
		if (nodeuses.containsKey(ep)) {
			/* node exists at location */
			int uses = nodeuses.get(ep);
			
			if (uses > 1) {
				/* other segments share this node - decrease counter */
				nodeuses.put(ep, uses - 1);
			} else {
				/* this node is obsolete - delete it */
				nodeuses.remove(ep);
				nodes.remove(ep);
			}
		} else {
			/* no node exists at location - remove edge point */
			edgepoints.remove(ep);
		}
	}

	/**
	 * Dodaje cvor ako on vec ne postoji, te dodatno
	 * povecava brojac segmenata koji dijele taj cvor.
	 * @param node
	 */
	private void addAndMapNode(XYLocation node) {
		if (!nodeuses.containsKey(node)) {
			/* this node does not exist yet */
			nodes.add(node);
			nodeuses.put(node, 1);
		} else {
			/* node already exists */
			nodeuses.put(node, nodeuses.get(node) + 1);
		}
	}
	
	/**
	 * Mice cvor ako on postoji, te dodatno smanjuje brojac segmenata
	 * koji koriste taj cvor.
	 * Ako cvor ne postoji, ne cini nista.
	 * @param node
	 */
	private void removeAndDemapNode(XYLocation node) {
		if (nodeuses.containsKey(node)) { /* node exists */
			int uses = nodeuses.get(node);
			if (uses > 1) { /* other segments share this node */ 
				nodeuses.put(node, uses - 1);
			} else { /* no other segment uses this node */
				nodeuses.remove(node);
				nodes.remove(node);
			}
		}
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
	
	/**
	 * Dodaje segment zice u listu segmenata,
	 * ne dodajuci pritom cvorove na presjecista.
	 * 
	 * @param segment
	 */
	public void addWireSegment(WireSegment segment) {
		insertSegment(segment);
	}

	public void addParameter(ParameterWrapper pw) {
		IParameter param = ParameterFactory.createParameter(pw);
		
		parameters.addParameter(param);
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
	@Deprecated
	@SuppressWarnings("unused")
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


}























