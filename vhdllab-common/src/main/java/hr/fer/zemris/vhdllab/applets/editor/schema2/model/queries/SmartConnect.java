/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.schema2.model.queries;

import hr.fer.zemris.vhdllab.applets.editor.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQuery;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQueryResult;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.CostSortedHash;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.QueryResult;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.queries.misc.WalkabilityMap;

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
		@Override
		public String toString() {
			if (parent != null) return parentloc.toString() + parent.toString();

			return "";
		}
	}

	
	/* static fields */
	public static final String QUERY_NAME = SmartConnect.class.getSimpleName();
	public static final String KEY_SEGMENTS = "segments";
	private static final int STEP = Constants.GRID_SIZE;
	private static final int NORMAL_COST = STEP;
	private static final int DETOUR_COST = STEP * 2;
	private static final int SEARCH_LIMIT = 1000;
	private static final Integer ALL_UNWALKABLE = 0;
	private static final List<EPropertyChange> propdepend = new ArrayList<EPropertyChange>();
	private static final List<EPropertyChange> ro_pd = Collections.unmodifiableList(propdepend);
	{
		propdepend.add(EPropertyChange.CANVAS_CHANGE);
		propdepend.add(EPropertyChange.PROPERTY_CHANGE);
	}

	
	/* private fields */
	private XYLocation begin, end;
	private WalkabilityMap walkability;
	
	
	/* ctors */

	/**
	 * Konstruktor kojim se specificira pocetna i zavrsna tocka puta.
	 */
	public SmartConnect(XYLocation startLocation, XYLocation endLocation) {
		begin = startLocation;
		end = endLocation;
		if (begin == null || end == null)
			throw new IllegalArgumentException("Start and end location cannot be null.");
		walkability = null;
	}

	/**
	 * Postavlja pocetnu i zavrsnu lokaciju, te walkability map.
	 * Ako se SmartConnect query stvori pomocu ovog ctora, algoritam
	 * trazenja puta ce raditi puno brze.
	 * @param startLocation
	 * @param endLocation
	 * @param walkabilitymap
	 * Klasa koja predstavlja prolaznost na mapi, a dobiva se pomocu
	 * InspectWalkability query-a. Bitno je da WalkabilityMap bude vazeci
	 * (tj. da je query kojim je dobiven obavljen neposredno prije ovog).
	 */
	public SmartConnect(XYLocation startLocation, XYLocation endLocation, WalkabilityMap walkabilitymap) {
		begin = startLocation;
		end = endLocation;
		if (begin == null || end == null)
			throw new IllegalArgumentException("Start and end location cannot be null.");
		walkability = walkabilitymap;
		if (walkability == null)
			throw new IllegalArgumentException("Walkability map cannot be null.");
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
		return other.begin.equals(this.begin) && other.end.equals(this.end);
	}

	@Override
	public int hashCode() {
		return begin.hashCode() << 16 + end.hashCode();
	}

	public IQueryResult performQuery(ISchemaInfo info) {
		List<WireSegment> segs = findPath(begin, end, info);

		return (segs == null) ? (new QueryResult(false)) : (new QueryResult(KEY_SEGMENTS, segs));
	}
	
	

	/* path finding */

	private List<WireSegment> findPath(XYLocation start, XYLocation target, ISchemaInfo info) {
		/* prepare path start so it's divisible with STEP */
		XYLocation actualstart = new XYLocation();
		if (!prepareStartAndActualStart(start, target, actualstart, info)) return null;
		
		/* A* */
		CostSortedHash<XYLocation, ANode> openlist = new CostSortedHash<XYLocation, ANode>();
		CostSortedHash<XYLocation, ANode> closedlist = new CostSortedHash<XYLocation, ANode>();
		ANode goal = null, helper = new ANode();
		XYLocation finder = new XYLocation();
		int counter = 0;

		/* put start on openlist */
		ANode startnode = new ANode(null, null, 0, heuristic(start.x, start.y));
		openlist.add(start, startnode.costsofar + startnode.estimate, startnode);

		/* while there are reachable nodes */
		if (isWalkable(info, start, null)) while (!openlist.isEmpty()) {
			/* check search limit */
			if (counter++ > SEARCH_LIMIT) break;
			
			/* get cheapest node */
			XYLocation currxy = openlist.cheapest();
			ANode currnode = openlist.get(currxy);
			
			//System.out.println("Iteration " + counter + ": " + currxy);

			/* check if goal has been reached */
			if (currxy.chebyshev(target.x, target.y) < STEP) {
				goal = currnode;
				finder = currxy;
				break;
			}

			/* goal not reached - expand current node */
			for (int i = 0, j = 1, t = 2; !(i == 0 && j == 1 && t != 2); t = i, i = j, j = -t) {
//				System.out.println(i + ", " + j + "; t = " + t);
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
						openlist.updateCost(new XYLocation(finder), neighbour.costsofar + neighbour.estimate);
					}
				} else {
					/* neighbour is completely new - add new neighbour to open list */
					neighbour = new ANode(currnode, currxy, 0, heuristic(finder.x, finder.y));
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
		List<WireSegment> path = reconstructPath(goal, finder);
		
		/* finish cutoffs */
		WireSegment firstseg = null, lastseg = null;
		int sz = path.size();
		if (sz > 0) {
			firstseg = path.get(0);
			lastseg = path.get(sz - 1);
		}
		
		/* finish path start cutoff - lastseg is actually the start */
		if (!start.equals(actualstart)) {
			if (lastseg != null) {
				/* find the last (the one nearest to start) segment orientation */
				if (lastseg.isVertical()) {
					path.add(new WireSegment(start.x, start.y, start.x, actualstart.y));
					path.add(new WireSegment(start.x, actualstart.y, actualstart.x, actualstart.y));
				} else {
					path.add(new WireSegment(start.x, start.y, actualstart.x, start.y));
					path.add(new WireSegment(actualstart.x, actualstart.y, actualstart.x, actualstart.y));
				}
			} else {
				/* create any kind of segment */
				path.add(new WireSegment(start.x, start.y, start.x, actualstart.y));
				path.add(new WireSegment(start.x, actualstart.y, actualstart.x, actualstart.y));
			}
		}
		
		/* finish path end cutoff - firstseg is actually the end */
		if (!finder.equals(target)) {
			if (firstseg != null) {
				/* find the first (the one nearest to the target) segment orientation */
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
	
	private boolean prepareStartAndActualStart(XYLocation start, XYLocation target,
			XYLocation actualstart, ISchemaInfo info)
	{
		int xstart = start.x - start.x % STEP;
		int ystart = start.y - start.y % STEP;
		XYLocation closest = new XYLocation();
		if (xstart != start.x || ystart != start.y) {
			actualstart.x = start.x;
			actualstart.y = start.y;
			
			/* find walkable slot closest to target */
			int dist = Math.abs(target.x - xstart) + Math.abs(target.y - ystart), mindist = Integer.MAX_VALUE;
			start.x = xstart; start.y = ystart;
			if (isWalkable(info, start, null)) { 
				mindist = dist; closest.x = xstart; closest.y = ystart;
			}
			
			xstart = xstart + STEP;
			dist = Math.abs(target.x - xstart) + Math.abs(target.y - ystart);
			start.x = xstart;
			if (dist < mindist && isWalkable(info, start, null)) { 
				mindist = dist; closest.x = xstart; closest.y = ystart;
			}

			ystart = ystart + STEP;
			dist = Math.abs(target.x - xstart) + Math.abs(target.y - ystart);
			start.y = ystart;
			if (dist < mindist && isWalkable(info, start, null)) { 
				mindist = dist; closest.x = xstart; closest.y = ystart;
			}
			
			xstart = xstart - STEP;
			dist = Math.abs(target.x - xstart) + Math.abs(target.y - ystart);
			start.x = xstart;
			if (dist < mindist && isWalkable(info, start, null)) { 
				mindist = dist; closest.x = xstart; closest.y = ystart;
			}
			
			/* if there is no adjacent free slot, no path can be found */
			if (mindist == Integer.MAX_VALUE) return false;
			
			/* otherwise, start from closest */
			start.x = closest.x;
			start.y = closest.y;
			
			return true;
		}

		actualstart.x = start.x;
		actualstart.y = start.y;
		
		return true;
	}

	private List<WireSegment> reconstructPath(ANode goal, XYLocation modend) {
		List<WireSegment> segs = new ArrayList<WireSegment>();
		int x, y, xstart = modend.x, ystart = modend.y;
		boolean vertical = false;
		
		/* prepare */
//		System.out.println(goal.toString());
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
	private boolean isWalkable(ISchemaInfo info, XYLocation loc, XYLocation parentloc) {
		if (walkability != null) {
			/* check with walkability map - optimized */
			int direction = 0;
			if (parentloc != null) {
				/* parent location exists */
				switch (parentloc.x - loc.x + 10 * (parentloc.y - loc.y)) {
				case -10:
					direction = WalkabilityMap.FROM_WEST;
					break;
				case 10:
					direction = WalkabilityMap.FROM_EAST;
					break;
				case -100:
					direction = WalkabilityMap.FROM_NORTH;
					break;
				case 100:
					direction = WalkabilityMap.FROM_SOUTH;
					break;
				}
				Integer walkinfo = walkability.walkmap.get(loc);
				if (walkinfo != null) {
					if ((walkinfo & direction) == 0) return false;
				}
			} else {
				/* this is a first node */
				Integer walkinfo = walkability.walkmap.get(loc);
				if (walkinfo != null && walkinfo == ALL_UNWALKABLE) return false;
			}
			
			return true;
		}

		/* check by inspecting ISchemaInfo - not optimized */
		
		/* check components */
		if (info.getComponents().containsAt(loc.x, loc.y, 0)) return false;
		
		/* check wires */
		Set<ISchemaWire> wires = info.getWires().fetchAllWires(loc.x, loc.y);
		
		if (wires != null && parentloc != null) {
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
		/* manhattan plus detour cost */
		return Math.abs(end.x - x) + Math.abs(end.y - y)
		+ ((end.x != x && end.y != y) ? (DETOUR_COST) : (0));
	}

}


















