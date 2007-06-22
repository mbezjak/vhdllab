package hr.fer.zemris.vhdllab.applets.schema2.model.drawers;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IWireDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;

import java.awt.Graphics2D;






public class DefaultWireDrawer implements IWireDrawer {
	
	/* static fields */
	public static final int NODE_SIZE = 5;
	
	
	/* private fields */
	private ISchemaWire wire_to_draw; 
	
	
	public DefaultWireDrawer(ISchemaWire wireToDraw) {
		wire_to_draw = wireToDraw;
	}
	

	public void draw(Graphics2D graphics) {
		for (WireSegment segment : wire_to_draw.getSegments()) {
			graphics.drawLine(segment.loc1.x, segment.loc1.y,
					segment.loc2.x, segment.loc2.y);
		}
		for (XYLocation node : wire_to_draw.getNodes()) {
			graphics.fillOval(node.x - NODE_SIZE / 2, node.y - NODE_SIZE / 2, NODE_SIZE, NODE_SIZE);
		}
	}
	
}















