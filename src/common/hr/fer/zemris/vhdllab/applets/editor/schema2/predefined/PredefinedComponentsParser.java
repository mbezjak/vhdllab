package hr.fer.zemris.vhdllab.applets.editor.schema2.predefined;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.Parameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.Port;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedConf;

import java.io.InputStream;

import org.apache.commons.digester.Digester;

public class PredefinedComponentsParser {
	
	public static PredefinedConf conf = null;
	
	public static PredefinedConf getConfiguration() {
		if(conf == null) {
			conf = parseConfiguration();
		}
		return conf;
	}
	
	private static PredefinedConf parseConfiguration() {
		Digester digester = new Digester();
		digester.setValidating(false);
		digester.addObjectCreate("componentList", PredefinedConf.class);
		digester.addObjectCreate("componentList/component", PredefinedComponent.class);
		digester.addBeanPropertySetter("componentList/component/parameterName");
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
		
		PredefinedComponentsParser c = new PredefinedComponentsParser();
		InputStream is = c.getClass().getResourceAsStream("configuration.xml");
		try {
			return (PredefinedConf) digester.parse(is);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

}
