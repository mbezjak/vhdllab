package hr.fer.zemris.vhdllab.applets.schema2.model.drawers;

import java.awt.Graphics2D;


import hr.fer.zemris.vhdllab.applets.schema.components.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;







public class DefaultComponentDrawer implements IComponentDrawer {
	
	private ISchemaComponent comp_to_draw;

	
	
	
	public DefaultComponentDrawer(ISchemaComponent componentToDraw) {
		comp_to_draw = componentToDraw;
	}
	
	
	public void draw(Graphics2D graphics) {
		throw new NotImplementedException();	
	}
	
}



















