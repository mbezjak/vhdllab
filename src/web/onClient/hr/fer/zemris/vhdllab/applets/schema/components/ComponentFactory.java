package hr.fer.zemris.vhdllab.applets.schema.components;

import java.util.HashMap;


public class ComponentFactory {
	static private HashMap<String, AbstractSchemaComponent> factoryMap;
	
	static {
		factoryMap = new HashMap<String, AbstractSchemaComponent>();
	}
	
	public ComponentFactory() {	
	}
	
	/**
	 * Stvara komponentu.
	 * @param componentName
	 * @return
	 * @throws Exception
	 */
	public AbstractSchemaComponent getSchemaComponent(String componentName) throws ComponentFactoryException {
		AbstractSchemaComponent component = factoryMap.get(componentName).vCtr();
		if (component == null)
			throw new ComponentFactoryException("Component factory unable to generate requested object!");
		else
			return component;
	}
	
	/**
	 * Stvara komponentu i puni je sa svojstvima pozivom component.deserializeComponent().
	 * @param componentName
	 * @param serial
	 * @return
	 * @throws Exception 
	 */
	public AbstractSchemaComponent getSchemaComponent(String componentName, String serial) throws ComponentFactoryException {
		AbstractSchemaComponent component = this.getSchemaComponent(componentName);
		component.deserializeComponent(serial);
		return component;
	}
	
	public AbstractSchemaComponent getSchemaComponent(AbstractSchemaComponent comp) throws ComponentFactoryException {
		AbstractSchemaComponent component = factoryMap.get(comp.getComponentName()).vCtr();
		if (component == null)
			throw new ComponentFactoryException("Component factory unable to generate requested object!");
		else
			return component;
	}
	
	static public void registerComponent(AbstractSchemaComponent component) {
		factoryMap.put(component.getComponentName(), component.vCtr());
	}
}
