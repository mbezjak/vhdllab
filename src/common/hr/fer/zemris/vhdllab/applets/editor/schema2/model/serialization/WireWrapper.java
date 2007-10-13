package hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization;

import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ParameterWrapper;

import java.util.ArrayList;
import java.util.List;



// TODO
public class WireWrapper {
	/* static fields */

	
	/* private fields */
	public List<WireSegment> segs;
	public List<ParameterWrapper> params;
	public String drawername;

	
	
	/* ctors */

	public WireWrapper() {
		segs = new ArrayList<WireSegment>();
		params = new ArrayList<ParameterWrapper>();
	}
	
	
	
	/* methods */

	public void addWireSegment(WireSegment ws) {
		segs.add(ws);
	}
	
	public void setDrawer(String name) {
		drawername = name;
	}
	
	public void addParameterWrapper(ParameterWrapper pw) {
		params.add(pw);
	}
	
}



















