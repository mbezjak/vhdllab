package hr.fer.zemris.vhdllab.applets.schema.components;

import java.util.HashMap;


public class ComponentFactory {
	private HashMap<String, AbstractSchemaComponent> factoryMap;
	
	public ComponentFactory() {
		factoryMap = new HashMap<String, AbstractSchemaComponent>();
	}
	
	public AbstractSchemaComponent getSchemaComponent(String componentName) throws Exception {
		AbstractSchemaComponent component = factoryMap.get(componentName).vCtr();
		if (component == null)
			throw new Exception("Component factory unable to generate requested object!");
		else
			return component;
	}
	
	public AbstractSchemaComponent getSchemaComponent(AbstractSchemaComponent comp) throws Exception {
		AbstractSchemaComponent component = factoryMap.get(comp.getComponentName()).vCtr();
		if (component == null)
			throw new Exception("Component factory unable to generate requested object!");
		else
			return component;
	}
	
	public void registerComponent(AbstractSchemaComponent component) {
		factoryMap.put(component.getComponentName(), component.vCtr());
	}
}
