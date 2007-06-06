package hr.fer.zemris.vhdllab.applets.schema2.model.drawers;

import java.awt.Graphics2D;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IWireDrawer;





public class DefaultWireDrawer implements IWireDrawer {
	
	private ISchemaWire wire_to_draw; 
	
	
	public DefaultWireDrawer(ISchemaWire wireToDraw) {
		wire_to_draw = wireToDraw;
	}
	
	

	public void draw(Graphics2D graphics) {
		throw new NotImplementedException();
	}
	
}
