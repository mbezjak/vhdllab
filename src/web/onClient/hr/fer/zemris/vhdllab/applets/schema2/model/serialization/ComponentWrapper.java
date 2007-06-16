package hr.fer.zemris.vhdllab.applets.schema2.model.serialization;


import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;

import java.util.ArrayList;
import java.util.List;




public class ComponentWrapper {
	
	private String componentClassName;
	private Integer x, y;
	private String name;
	private String codeFileName;
	private String drawerName;
	private Boolean genericComponent;
	private List<ParameterWrapper> paramWrappers;
	private List<PortWrapper> portWrappers;
	private List<SchemaPort> schemaPorts;
	private String orientation;
	
	
	public ComponentWrapper() {
		paramWrappers = new ArrayList<ParameterWrapper>();
		portWrappers = new ArrayList<PortWrapper>();
		schemaPorts = new ArrayList<SchemaPort>();
	}


	public void setComponentClassName(String componentClassName) {
		this.componentClassName = componentClassName;
	}
	
	
	public String getComponentClassName() {
		return componentClassName;
	}


	public void setX(Integer x) {
		this.x = x;
	}


	public Integer getX() {
		return x;
	}


	public void setY(Integer y) {
		this.y = y;
	}


	public Integer getY() {
		return y;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}


	public void setCodeFileName(String codeFileName) {
		this.codeFileName = codeFileName;
	}


	public String getCodeFileName() {
		return codeFileName;
	}


	public void setDrawerName(String drawerName) {
		this.drawerName = drawerName;
	}


	public String getDrawerName() {
		return drawerName;
	}


	public void setGenericComponent(Boolean genericComponent) {
		this.genericComponent = genericComponent;
	}


	public Boolean getGenericComponent() {
		return genericComponent;
	}


	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}


	public String getOrientation() {
		return orientation;
	}
	
	
	public void addPortWrapper(PortWrapper pw) {
		portWrappers.add(pw);
	}
	
	
	public void addParameterWrapper(ParameterWrapper pw) {
		paramWrappers.add(pw);
	}
	
	
	public void addSchemaPort(SchemaPort sp) {
		schemaPorts.add(sp);
	}
	
}

















