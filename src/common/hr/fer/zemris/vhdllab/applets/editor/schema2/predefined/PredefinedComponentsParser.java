package hr.fer.zemris.vhdllab.applets.editor.schema2.predefined;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.Parameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.Port;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedConf;

import java.io.InputStream;

import org.apache.commons.digester.Digester;

public class PredefinedComponentsParser {
	
	private PredefinedConf conf = null;
	private String pathToConfigurationFile = null;
	
	public PredefinedComponentsParser(String pathToConfigurationFile) {
		this.pathToConfigurationFile = pathToConfigurationFile;
	}
	
	public PredefinedConf getConfiguration() {
		if(conf == null) {
			conf = parseConfiguration();
		}
		return conf;
	}
	
	private PredefinedConf parseConfiguration() {
		Digester digester = new Digester();
		digester.setValidating(false);
		digester.addObjectCreate("componentList", PredefinedConf.class);
		digester.addObjectCreate("componentList/component", PredefinedComponent.class);
		digester.addBeanPropertySetter("componentList/component/componentName");
		digester.addBeanPropertySetter("componentList/component/codeFileName");
		digester.addBeanPropertySetter("componentList/component/drawerName");
		digester.addBeanPropertySetter("componentList/component/categoryName");
		digester.addBeanPropertySetter("componentList/component/genericComponent");

		digester.addObjectCreate("componentList/component/parameterList/parameter", Parameter.class);
		digester.addBeanPropertySetter("componentList/component/parameterList/parameter/generic");
		digester.addBeanPropertySetter("componentList/component/parameterList/parameter/type");
		digester.addBeanPropertySetter("componentList/component/parameterList/parameter/name");
		digester.addBeanPropertySetter("componentList/component/parameterList/parameter/value");
		digester.addBeanPropertySetter("componentList/component/parameterList/parameter/allowedValueSet");
		digester.addSetNext("componentList/component/parameterList/parameter", "addParameter");
		
		digester.addObjectCreate("componentList/component/portList/port", Port.class);
		digester.addBeanPropertySetter("componentList/component/portList/port/name");
		digester.addBeanPropertySetter("componentList/component/portList/port/orientation");
		digester.addBeanPropertySetter("componentList/component/portList/port/direction");
		digester.addBeanPropertySetter("componentList/component/portList/port/type");
		digester.addBeanPropertySetter("componentList/component/portList/port/vectorAscension");
		digester.addBeanPropertySetter("componentList/component/portList/port/lowerBound");
		digester.addBeanPropertySetter("componentList/component/portList/port/upperBound");
		digester.addSetNext("componentList/component/portList/port", "addPort");
		
		digester.addSetNext("componentList/component", "addPredefinedComponent");
		
		InputStream is = this.getClass().getResourceAsStream(pathToConfigurationFile);
		try {
			return (PredefinedConf) digester.parse(is);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

}
