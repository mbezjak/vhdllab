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

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQuery;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQueryResult;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.QueryResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;





/**
 * Postavlja upit o tome koliko navedena zica ima odvojenih skupova
 * segmenata. Ako je zica povezana segmentima u jednu cjelinu,
 * vratit ce se lista s jednim skupom segmenata koji sadrzi sve segmente.
 * Ako zica nema segmenata, vratit ce se prazna lista segmenata.
 * 
 * Rezultat se vraca unutar QueryResult-a pod kljucem KEY_SEGMENT_SETS,
 * ako je pretraga uspjesna.
 * Vrijednost pohranjena pod tim kljucem je tipa DisjointSets (inner klasa
 * ovog Query-a). DisjointSets sadrzi listu skupova segmenata.
 * 
 * @author brijest
 *
 */
public class FindDisjointSegments implements IQuery {
	
	public static class DisjointSets {
		public List<Set<WireSegment>> segmentsets = new ArrayList<Set<WireSegment>>();
	}
	
	private static class SegmentHolder {
		public WireSegment seg;
		public boolean ishandled = false;
		public SegmentHolder(WireSegment ws) { seg = ws; }
		@Override
		public boolean equals(Object obj) {
			if (obj == null) return false;
			if (!(obj instanceof SegmentHolder)) return false;
			SegmentHolder holder = (SegmentHolder)obj;
			return (this.seg.equals(holder.seg));
		}
		@Override
		public int hashCode() {
			return seg.hashCode();
		}
	}

	
	/* static fields */
	public static final String QUERY_NAME = FindDisjointSegments.class.getSimpleName(); 
	public static final String KEY_SEGMENT_SETS = "segment_sets";
	private static final List<EPropertyChange> propdepend = new ArrayList<EPropertyChange>();
	private static final List<EPropertyChange> ro_pd = Collections.unmodifiableList(propdepend);
	
	{
		propdepend.add(EPropertyChange.CANVAS_CHANGE);
		propdepend.add(EPropertyChange.PROPERTY_CHANGE);
	}
	
	
	/* private fields */
	private Caseless wirename;
	
	
	
	/* ctors */

	public FindDisjointSegments(Caseless wireToAnalyze) {
		wirename = wireToAnalyze;
	}
	
	
	
	
	/* methods */
	
	public List<EPropertyChange> getPropertyDependency() {
		return ro_pd;
	}

	public boolean isCacheable() {
		return true;
	}

	public String getQueryName() {
		return QUERY_NAME;
	}

	public IQueryResult performQuery(ISchemaInfo info) {
		ISchemaWire wire = info.getWires().fetchWire(wirename);
		
		if (wire == null) {
			return new QueryResult(false);
		}
		
		if (wire.getSegments().size() == 0) {
			return new QueryResult(KEY_SEGMENT_SETS, new DisjointSets());
		}
		
		dissets = new DisjointSets();
		tohandlesegs = new HashSet<SegmentHolder>();
		segmentmap = new HashMap<XYLocation, List<SegmentHolder>>();
		
		// put all segments into a location based map and the tohandle list
		for (WireSegment segment : wire.getSegments()) {
			SegmentHolder seghold = new SegmentHolder(segment);
			
			List<SegmentHolder> segs = segmentmap.get(segment.getStart());
			if (segs == null) {
				segs = new ArrayList<SegmentHolder>();
				segmentmap.put(segment.getStart(), segs);
			}
			segs.add(seghold);
			
			segs = segmentmap.get(segment.getEnd());
			if (segs == null) {
				segs = new ArrayList<SegmentHolder>();
				segmentmap.put(segment.getEnd(), segs);
			}
			segs.add(seghold);
			
			tohandlesegs.add(seghold);
		}
		
		// pick unhandled segments and handle them recursively
		for (SegmentHolder holder : tohandlesegs) {
			if (holder.ishandled == false) {
				Set<WireSegment> wsset = new HashSet<WireSegment>();
				dissets.segmentsets.add(wsset);
				rec_handle_segment(holder, wsset);
			}
		}
		
		segmentmap = null;
		tohandlesegs = null;
		
		return new QueryResult(KEY_SEGMENT_SETS, dissets);
	}
	
	private DisjointSets dissets;
	private Map<XYLocation, List<SegmentHolder>> segmentmap;
	private Set<SegmentHolder> tohandlesegs;
	private void rec_handle_segment(SegmentHolder holder, Set<WireSegment> wsset) {
		// handle this holder
		holder.ishandled = true;
		wsset.add(holder.seg);
		
		// handle unhandled neighbours
		List<SegmentHolder> segholds = segmentmap.get(holder.seg.getStart());
		for (SegmentHolder sh : segholds) if (sh.ishandled == false) rec_handle_segment(sh, wsset);
		segholds = segmentmap.get(holder.seg.getEnd());
		for (SegmentHolder sh : segholds) if (sh.ishandled == false) rec_handle_segment(sh, wsset);
	}
	

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof FindDisjointSegments)) return false;
		FindDisjointSegments other = (FindDisjointSegments)obj;
		return other.wirename.equals(this.wirename);
	}

	@Override
	public int hashCode() {
		return wirename.hashCode();
	}

	@Override
	public String toString() {
		return super.toString();
	}
	
	

}













