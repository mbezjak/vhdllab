package hr.fer.zemris.vhdllab.applets.schema2.model.parameters;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterEvent;

import java.lang.reflect.Constructor;




public abstract class AbstractParameter implements IParameter {
	protected IParameterEvent paramevent;
	
	
	public AbstractParameter() {
		paramevent = null;
	}
	

	public IParameterEvent getParameterEvent() {
		return paramevent;
	}

	public void setParameterEvent(IParameterEvent event) {
		paramevent = event;
	}

	public void setParameterEvent(String eventName) {
		if (eventName == null || eventName.equals("")) return;
		
		IParameterEvent ne;
		
		try {
			Class cls = Class.forName(eventName);
			Constructor<IParameterEvent> ctor = cls.getConstructor();
			ne = ctor.newInstance();
		} catch (Exception e) {
			return;
		}
		
		paramevent = ne;
	}
	
}








