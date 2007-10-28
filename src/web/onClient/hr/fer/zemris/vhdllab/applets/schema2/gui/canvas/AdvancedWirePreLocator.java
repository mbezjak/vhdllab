package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQuery;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQueryResult;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.AddWireCommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.BindWireCommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.ExpandWireCommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.PlugWireCommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.queries.InspectWalkability;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.queries.SmartConnect;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.queries.misc.WalkabilityMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class AdvancedWirePreLocator implements IWirePreLocator{

	private List<WireSegment> segmentList = null;
	
	private int x1;
	private int y1;
	
	private int x2;
	private int y2;
	
	private boolean wireInstantiable = true;
	
	ISchemaController controller;
	
	public AdvancedWirePreLocator(List<WireSegment> segmentList, ISchemaController controller) {
		this.segmentList = segmentList;
		this.controller = controller;
	}
	
	
	@Override
	public void draw(Graphics2D g) {
		if(wireInstantiable){
			for(WireSegment s:segmentList){
				XYLocation start = s.getStart();
				XYLocation end = s.getEnd();
				g.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
			}
		}else{
			Color c = g.getColor();
			g.setColor(CanvasColorProvider.SIGNAL_LINE_ERROR);
			g.drawLine(x1, y1, x2, y2);
			g.setColor(c);
		}
		
	}

	@Override
	public int getOrientation() {
		return 0;
	}

	@Override
	public int getX1() {
		return x1;
	}

	@Override
	public int getX2() {
		return x2;
	}

	@Override
	public int getY1() {
		return y1;
	}

	@Override
	public int getY2() {
		return y2;
	}

	
	@Override
	public void instantiateWire(ISchemaController controller, CriticalPoint wireBeginning, CriticalPoint wireEnding) {
		if((wireBeginning == null || wireBeginning.getType()==CriticalPoint.ON_COMPONENT_PLUG) &&
				(wireEnding == null || wireEnding.getType() == CriticalPoint.ON_COMPONENT_PLUG)){
			addWire(controller, wireBeginning, wireEnding);
		}else{
			if(wireBeginning == null || wireBeginning.getType() == CriticalPoint.ON_COMPONENT_PLUG)
				expandWire(controller, wireEnding, wireBeginning);
			else
				expandWire(controller, wireBeginning, wireEnding);
		}
	}

	private void expandWire(ISchemaController controller, CriticalPoint wireBeginning, CriticalPoint wireEnding) {
		//Caseless wireName = wireBeginning.getName();
		Caseless wireName = createName(x1,y1,x2,y2);

		ICommand instantiate = new ExpandWireCommand(wireName,segmentList);
		ICommandResponse response = controller.send(instantiate);
		System.out.println ("canvas report| wire instantiate succesful: "+response.isSuccessful());

	
		plugToPoint(wireBeginning,controller,wireName);
		plugToPoint(wireEnding,controller,wireName);		
	}

	private void addWire(ISchemaController controller, CriticalPoint wireBeginning, CriticalPoint wireEnding) {
		Caseless wireName = null;

		wireName = createName(x1,y1,x2,y2); //TODOcreateNeme
		ICommand instantiate = new AddWireCommand(wireName,segmentList);
		ICommandResponse response = controller.send(instantiate);
		System.out.println ("canvas report| wire instantiate succesful: "+response.isSuccessful());


		plugToPoint(wireBeginning,controller,wireName);
		plugToPoint(wireEnding,controller,wireName);
	}
	
	private void plugToPoint(CriticalPoint point, ISchemaController controller, Caseless wireName) {
		if (point!=null){
			if(point.getType()==CriticalPoint.ON_COMPONENT_PLUG){
				Caseless componentName = point.getName();
				ICommand plug = new PlugWireCommand(componentName, wireName, point.getPortName());
				ICommandResponse response = controller.send(plug);
				String message = "";
				try{
					message = response.getError().getMessage(); 
				}catch (NullPointerException e) {}
				System.out.println ("canvas report| wire instantiate & plug succesful: "+response.isSuccessful()+" "+message);
			}else{
				if(point.getType() == CriticalPoint.ON_WIRE_PLUG && !point.getName().equals(wireName)){
					Caseless wireToBindName = point.getName();
					ICommand plug = new BindWireCommand(wireToBindName, wireName);
					ICommandResponse response = controller.send(plug);
					String message = "";
					try{
						message = response.getError().getMessage(); 
					}catch (NullPointerException e) {}
					System.out.println ("canvas report| wire instantiate & plug succesful: "+response.isSuccessful()+" "+message);
				}else{
					System.out.println("canvas report| wire instantiate & plug expand wire");
				}
			}
		}
	}

	private Caseless createName(int x1, int y1, int x2, int y2) {
		StringBuilder build = new StringBuilder("WIRE_");
		build.append(normalize(x1)).append("_").append(normalize(y1)).append("_")
			.append(normalize(x2)).append("_").append(normalize(y2));
		return new Caseless(build.toString());
	}

	private String normalize(int x) {
		return x<0?"M"+String.valueOf(Math.abs(x)):String.valueOf(x);
	}

	@Override
	public boolean isWireInstance() {
		return (Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))>10) && wireInstantiable;
	}

	@Override
	public boolean isWireInstantiable() {
		return wireInstantiable;
	}

	
	
	
	
	@Override
	public void setOrientation(int orientation) {}

	@Override
	public void setWireInstantiable(boolean wireInstantiable) {
		this.wireInstantiable = wireInstantiable;		
	}

	@Override
	public void setWireInstantiable(CriticalPoint wireBeginning,
			CriticalPoint wireEnding) {
		
		if(wireBeginning != null && wireEnding != null){
			if(wireBeginning.getType()==CriticalPoint.ON_WIRE_PLUG 
					&& wireEnding.getType()==CriticalPoint.ON_WIRE_PLUG){
				if(wireBeginning.getName().equals(wireEnding.getName())){
					wireInstantiable = false;
				}else{
					wireInstantiable = true;
				}
			}else{
				wireInstantiable = true;
			}
		}else{
			wireInstantiable = true;
		}
		
	}

	@Override
	public void revalidateWire() {
		IQuery inspect = new InspectWalkability(true); 
		IQueryResult result = controller.send(inspect); 
		WalkabilityMap walkability = 
		(WalkabilityMap)result.get(InspectWalkability.KEY_WALKABILITY); 

		IQuery smartconnect = new SmartConnect(new XYLocation(x1,y1), new XYLocation(x2,y2), 
		walkability); 
		result = controller.send(smartconnect); 
		if (result.isSuccessful()) { 
		 segmentList = (List<WireSegment>)result.get(SmartConnect.KEY_SEGMENTS); 
		} else { 
			wireInstantiable = false;
		} 

	}

	@Override
	public void setX1(int x1) {
		this.x1=x1;
	}

	@Override
	public void setX2(int x2) {
		this.x2=x2;
	}

	@Override
	public void setY1(int y1) {
		this.y1=y1;
	}

	@Override
	public void setY2(int y2) {
		this.y2=y2;
	}

}
