package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import java.awt.Graphics2D;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.AddWireCommand;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.ExpandWireCommand;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.PlugWireCommand;

public class WirePreLocator {

	public static int HORIZ_FIRST = 0;
	public static int VERT_FIRST = 1;
	
	private int x1;
	private int y1;
	
	private int x2;
	private int y2;
	
	private int orientation;
	
	private int odmak1;
	private int odmak2;
	
	public WirePreLocator(int x1, int y1, int x2, int y2, int orientation, int odm1, int odm2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		
		this.odmak1 = odm1;
		this.odmak2 = odm2;
		
		this.orientation = orientation;
	}
	
	public WirePreLocator() {
		this(0,0,0,0,0,0,0);
	}
	
	public WirePreLocator(int x1, int y1, int x2, int y2){
		this(x1,y1,x2,y2,0,0,0);
	}
	
	public int getOdmak1() {
		return odmak1;
	}
	
	public void setOdmak1(int odmak1) {
		this.odmak1 = odmak1;
	}
	
	public int getOdmak2() {
		return odmak2;
	}
	
	public void setOdmak2(int odmak2) {
		this.odmak2 = odmak2;
	}
	
	public int getOrientation() {
		return orientation;
	}
	
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	
	public int getX1() {
		return x1;
	}
	
	public void setX1(int x1) {
		this.x1 = x1;
	}
	
	public int getX2() {
		return x2;
	}
	
	public void setX2(int x2) {
		this.x2 = x2;
	}
	
	public int getY1() {
		return y1;
	}
	
	public void setY1(int y1) {
		this.y1 = y1;
	}
	
	public int getY2() {
		return y2;
	}
	
	public void setY2(int y2) {
		this.y2 = y2;
	}
	
	public void instantiateWire(ISchemaController controller, CriticalPoint wireBeginning, CriticalPoint wireEnding) {
		//TODO srediti za razlicit orientation i odmak ako ce to uopce postojati
		Caseless wireName = null;
		if(orientation == VERT_FIRST){
			if (x1 != x2 && y1 != y2) {
				wireName = createName(x1,y1,x1,y2);
				ICommand instantiate = new AddWireCommand(wireName,x1,y1,x1,y2);
				ICommandResponse response = controller.send(instantiate);
				System.out.println ("canvas report| wire instantiate succesful: "+response.isSuccessful());

				instantiate = new ExpandWireCommand(wireName,x1,y2,x2,y2);
				response = controller.send(instantiate);
				System.out.println ("canvas report| wire instantiate succesful: "+response.isSuccessful());
			}
			else{
				ICommand instantiate = new AddWireCommand(createName(x1, y1, x2, y2),x1,y1,x2,y2);
				ICommandResponse response = controller.send(instantiate);
				System.out.println ("canvas report| wire instantiate succesful: "+response.isSuccessful());
			}
		}else{
			if (x1 != x2 && y1 != y2) {
				wireName = createName(x1,y1,x1,y2);
				ICommand instantiate = new AddWireCommand(wireName,x1,y1,x2,y1);
				ICommandResponse response = controller.send(instantiate);
				System.out.println ("canvas report| wire instantiate succesful: "+response.isSuccessful());

				instantiate = new ExpandWireCommand(wireName,x2,y1,x2,y2);
				response = controller.send(instantiate);
				System.out.println ("canvas report| wire instantiate succesful: "+response.isSuccessful());
			}
			else{
				wireName = createName(x1, y1, x2, y2);
				ICommand instantiate = new AddWireCommand(wireName,x1,y1,x2,y2);
				ICommandResponse response = controller.send(instantiate);
				System.out.println ("canvas report| wire instantiate succesful: "+response.isSuccessful());
			}
		}
		
		plugToPoint(wireBeginning,controller,wireName);
		plugToPoint(wireEnding,controller,wireName);
	}
	
	private void plugToPoint(CriticalPoint point, ISchemaController controller, Caseless wireName) {
		if (point!=null){
			if(point.getType()==CriticalPoint.ON_COMPONENT_PLUG){
				Caseless componentName = point.getName();
				ICommand plug = new PlugWireCommand(componentName, wireName, point.getPortName());
				ICommandResponse response = controller.send(plug);
				System.out.println ("canvas report| wire instantiate & plug succesful: "+response.isSuccessful()+" "+response.getError());
			}else{
				System.out.println("canvas report| wire instantiate & plug succesful: false | not implemented on wire plug");
			}
		}
	}

	private Caseless createName(int x1, int y1, int x2, int y2) {
		StringBuilder build = new StringBuilder("WIRE");
		build.append(x1).append("-").append(y1).append("-")
			.append(x2).append("-").append("y2");
		return new Caseless(build.toString());
	}

	public void draw(Graphics2D g) {
		if(orientation == VERT_FIRST){
			g.drawLine(x1, y1, x1, y2);
			g.drawLine(x1, y2, x2, y2);
		}else{
			g.drawLine(x1, y1, x2, y1);
			g.drawLine(x2, y1, x2, y2);
		}
		
	}

	public boolean isWireInstance() {
		return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))>10;
	}
}
