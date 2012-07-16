/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans;


import java.util.ArrayList;
import java.util.List;




public class ComponentWrapper {
	
	private String componentClassName;
	private Integer x, y, width, height;
	private String componentName;
	private String codeFileName;
	private String categoryName;
	private String drawerName;
	private Boolean genericComponent;
	private List<ParameterWrapper> paramWrappers;
	private List<PortWrapper> portWrappers;
	private List<SchemaPortWrapper> schemaPorts;
	
	
	public ComponentWrapper() {
		paramWrappers = new ArrayList<ParameterWrapper>();
		portWrappers = new ArrayList<PortWrapper>();
		schemaPorts = new ArrayList<SchemaPortWrapper>();
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


	public void setComponentName(String name) {
		this.componentName = name;
	}


	public String getComponentName() {
		return componentName;
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
	
	
	public void addPortWrapper(PortWrapper pw) {
		portWrappers.add(pw);
	}
	
	
	public void addParameterWrapper(ParameterWrapper pw) {
		paramWrappers.add(pw);
	}
	
	
	public void addSchemaPort(SchemaPortWrapper sp) {
		schemaPorts.add(sp);
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public String getCategoryName() {
		return categoryName;
	}


	public void setParamWrappers(List<ParameterWrapper> paramWrappers) {
		this.paramWrappers = paramWrappers;
	}


	public List<ParameterWrapper> getParamWrappers() {
		return paramWrappers;
	}


	public void setPortWrappers(List<PortWrapper> portWrappers) {
		this.portWrappers = portWrappers;
	}


	public List<PortWrapper> getPortWrappers() {
		return portWrappers;
	}


	public void setSchemaPorts(List<SchemaPortWrapper> schemaPorts) {
		this.schemaPorts = schemaPorts;
	}


	public List<SchemaPortWrapper> getSchemaPorts() {
		return schemaPorts;
	}


	public void setWidth(Integer width) {
		this.width = width;
	}


	public Integer getWidth() {
		return width;
	}


	public void setHeight(Integer height) {
		this.height = height;
	}


	public Integer getHeight() {
		return height;
	}
	
}

















