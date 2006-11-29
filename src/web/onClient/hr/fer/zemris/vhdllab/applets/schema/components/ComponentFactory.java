package hr.fer.zemris.vhdllab.applets.schema.components;

import java.util.HashMap;


public class ComponentFactory {
	private HashMap<String, AbstractSchemaComponent> factoryMap;
	
	AbstractSchemaComponent getSchemaComponent(String componentName) throws Exception {
		AbstractSchemaComponent component = factoryMap.get(componentName).vCtr();
		if (component == null)
			throw new Exception("Component factory unable to generate requested object!");
		else
			return component;
	}
	
	void registerComponent(AbstractSchemaComponent component) {
		factoryMap.put(component.getComponentName(), component);
	}
}
