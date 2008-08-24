package hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans;

import java.util.ArrayList;
import java.util.List;

public class PredefinedConf {

	private List<PredefinedComponent> components;
	
	public PredefinedConf() {
		components = new ArrayList<PredefinedComponent>();
	}

	public List<PredefinedComponent> getComponents() {
		return components;
	}

	public void setComponents(List<PredefinedComponent> components) {
		this.components = components;
	}
	
	public void addPredefinedComponent(PredefinedComponent c) {
		components.add(c);
	}
	
	@Override
	public String toString() {
		return "Predefined Configuration {\n" + 
			components.toString() + "\n}";
	}
	
}
