package hr.fer.zemris.vhdllab.applets.schema2.model.queries;

import hr.fer.zemris.vhdllab.applets.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IQuery;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IQueryResult;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.misc.CostSortedHash;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.schema2.model.QueryResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Nalazi najkraci put sa sto manje skretanja izmedu predane pocetne i zavrsne
 * lokacije. Pritom je put pohranjen u obliku liste segmenata u QueryResult-u
 * pod kljucem KEY_SEGMENTS, koji je tipa List<WireSegments>.
 * 
 * U slucaju da put nije naden, pod navedenim kljucem nece biti pohranjeno
 * nista, odnosno vratit ce se null, a pripadni IQueryResult nece biti uspjesan.
 * 
 * @author Axel
 * 
 */
public class SmartConnect implements IQuery {

	private static class ANode {
		public XYLocation parentloc;
		public ANode parent;
		public int costsofar, estimate;
		
		public ANode() {
		}
		public ANode(ANode parentnode, XYLocation parentlocation, int tohere, int totarget) {
			parentloc = parentlocation;
			parent = parentnode;
			costsofar = tohere;
			estimate = totarget;
		}
	}

	
	/* static fields */
	public static final String QUERY_NAME = SmartConnect.class.getSimpleName();
	public static final String KEY_SEGMENTS = "segments";
	private static final int STEP = Constants.GRID_SIZE;
	private static final int NORMAL_COST = STEP;
	private static final int DETOUR_COST = STEP * 2;
	private static final List<EPropertyChange> propdepend = new ArrayList<EPropertyChange>();
	private static final List<EPropertyChange> ro_pd = Collections.unmodifiableList(propdepend);
	{
		propdepend.add(EPropertyChange.CANVAS_CHANGE);
		propdepend.add(EPropertyChange.PROPERTY_CHANGE);
	}

	
	/* private fields */
	private XYLocation start, end;
	private Set<WireSegment> additional;

	
	
	/* ctors */

	/**
	 * Konstruktor kojim se specificira pocetna i zavrsna tocka puta.
	 */
	public SmartConnect(XYLocation startLocation, XYLocation endLocation) {
		start = startLocation;
		end = endLocation;
		if (start == null || end == null)
			throw new IllegalArgumentException("Start and end location cannot be null.");
		additional = null;
	}

	
	
	/* methods */

	public List<EPropertyChange> getPropertyDependency() {
		return ro_pd;
	}

	public String getQueryName() {
		return QUERY_NAME;
	}

	public boolean isCacheable() {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof SmartConnect))
			return false;
		SmartConnect other = (SmartConnect) obj;
		return other.start.equals(this.start) && other.end.equals(this.end);
	}

	@Override
	public int hashCode() {
		return start.hashCode() << 16 + end.hashCode();
	}

	public IQueryResult performQuery(ISchemaInfo info) {
		List<WireSegment> segs = findPath(start, end, info);

		return (segs == null) ? (new QueryResult(false)) : (new QueryResult(
				KEY_SEGMENTS, segs));
	}
	
	

	/* path finding */

	private List<WireSegment> findPath(XYLocation start, XYLocation target, ISchemaInfo info) {
		/* A* */
		CostSortedHash<XYLocation, ANode> openlist = new CostSortedHash<XYLocation, ANode>();
		CostSortedHash<XYLocation, ANode> closedlist = new CostSortedHash<XYLocation, ANode>();
		ANode goal = null, helper = new ANode();
		XYLocation finder = new XYLocation();

		/* put start on openlist */
		ANode startnode = new ANode(null, null, 0, heuristic(start.x, start.y));
		openlist.add(start, startnode.costsofar + startnode.estimate, startnode);

		/* while there are reachable nodes */
		while (!openlist.isEmpty()) {
			/* get cheapest node */
			XYLocation currxy = openlist.cheapest();
			ANode currnode = openlist.get(currxy);

			/* check if goal has been reached */
			if (currxy.chebyshev(target.x, target.y) < STEP) {
				goal = currnode;
				finder = currxy;
				break;
			}

			/* goal not reached - expand current node */
			for (int i = 0, j = 1, t = 2; i != 0 && j != 1 && t != 2; t = i, i = j, j = -t) {
				finder.x = currxy.x + i * STEP;
				finder.y = currxy.y + j * STEP;
				ANode neighbour = null;
				if (!isWalkable(info, finder, currxy)) continue;
				else if (closedlist.contains(finder)) continue;
				else if (openlist.contains(finder)) {
					/* neighbour already on openlist - update cost if necessary */
					helper.parent = currnode;
					helper.parentloc = currxy;
					appendCost(helper, finder);
					neighbour = openlist.get(finder);
					int oldcost = neighbour.costsofar;
					int newcost = helper.costsofar;
					
					if (newcost < oldcost) {
						/* lower cost found - update cost */
						neighbour.costsofar = newcost;
						neighbour.parent = currnode;
						neighbour.parentloc = currxy;
						openlist.updateCost(finder, neighbour.costsofar + neighbour.estimate);
					}
				} else {
					/* neighbour is completely new - add new neighbour to open list */
					neighbour = new ANode(currnode, currxy, 0, heuristic(currxy.x, currxy.y));
					appendCost(neighbour, finder);
					openlist.add(new XYLocation(finder), neighbour.costsofar + neighbour.estimate, neighbour);
				}
			}
			
			/* remove currentnode from open list and put it on closedlist */
			openlist.remove(currxy);
			closedlist.add(currxy, currnode.costsofar + currnode.estimate, currnode);
		}
		
		/* if goal is null, a path has not been found */
		if (goal == null) return null;
		
		/* otherwise, path must be reconstructed */
		List<WireSegment> path = reconstructPath(goal);
		
		/* finish path end cutoff */
		if (!finder.equals(target)) {
			if (path.size() > 0) {
				/* find the first (the one nearest to the target) segment orientation */
				WireSegment firstseg = path.get(0);
				if (firstseg.isVertical()) {
					path.add(new WireSegment(finder.x, finder.y, finder.x, target.y));
					path.add(new WireSegment(finder.x, target.y, target.x, target.y));
				} else {
					path.add(new WireSegment(finder.x, finder.y, target.x, finder.y));
					path.add(new WireSegment(target.x, finder.y, target.x, target.y));
				}
			} else {
				/* create any kind of a segment */
				path.add(new WireSegment(finder.x, finder.y, finder.x, target.y));
				path.add(new WireSegment(finder.x, target.y, target.x, target.y));
			}
		}
		
		return path;
	}
	
	private List<WireSegment> reconstructPath(ANode goal) {
		List<WireSegment> segs = new ArrayList<WireSegment>();
		int x, y, xstart = end.x, ystart = end.y;
		boolean vertical = false;
		
		/* prepare */
		if (goal.parent == null) return segs;
		x = goal.parentloc.x;
		y = goal.parentloc.y;
		if (x == xstart) vertical = true;
		goal = goal.parent;
		if (goal.parent == null) {
			segs.add(new WireSegment(xstart, ystart, x, y));
			return segs;
		}
		
		/* iterate */
		do {
			if (vertical) {
				if (xstart != goal.parentloc.x) {
					vertical = false;
					segs.add(new WireSegment(xstart, ystart, x, y));
					xstart = x;
					ystart = y;
					x = goal.parentloc.x;
					y = goal.parentloc.y;
				} else {
					y = goal.parentloc.y;
				}
			} else {
				if (ystart != goal.parentloc.y) {
					vertical = true;
					segs.add(new WireSegment(xstart, ystart, x, y));
					xstart = x;
					ystart = y;
					x = goal.parentloc.x;
					y = goal.parentloc.y;
				} else {
					x = goal.parentloc.x;
				}
			}
			goal = goal.parent;
		} while (goal.parent != null);
		
		/* finish */
		segs.add(new WireSegment(xstart, ystart, x, y));
		
		return segs;
	}

	/**
	 * Calculates and appends the cost to reach this node. Assumes the parent of
	 * the node has been set, and that his cost to reach has already been set.
	 * 
	 * @param node
	 */
	private static void appendCost(ANode node, XYLocation location) {
		if (node.parent.parent == null) {
			node.costsofar = NORMAL_COST;
		} else {
			if (node.parent.parentloc.x == location.x || node.parent.parentloc.y == location.y) {
				/* both nodes on same line */
				node.costsofar = node.parent.costsofar + NORMAL_COST;
			}
			else {
				/* detour between */
				node.costsofar = node.parent.costsofar + DETOUR_COST;
			}
		}
	}
	
	/**
	 * Walkable slots are not:
	 * - slots within the component bounding rectangle
	 * - slots that are literally ON a wire, and form the same orientation as the wire
	 *   with their parent slot
	 * @param info
	 * @param loc
	 * @return
	 */
	private static boolean isWalkable(ISchemaInfo info, XYLocation loc, XYLocation parentloc) {
		/* check components */
		if (info.getComponents().containsAt(loc.x, loc.y, 0)) return false;
		
		/* check wires */
		Set<ISchemaWire> wires = info.getWires().fetchAllWires(loc.x, loc.y);
		
		if (wires != null) {
			boolean vertical = (loc.x == parentloc.x);
			for (ISchemaWire sw : wires) {
				Set<WireSegment> segments = sw.segmentsAt(loc.x, loc.y);
				if (segments != null) for (WireSegment ws : segments) {
					if (ws.isVertical() == vertical) return false;
				}
			}
		}
		
		return true;
	}

	private int heuristic(int x, int y) {
		/* manhattan */
		return Math.abs(end.x - x) + Math.abs(end.y - y);
	}

}


















