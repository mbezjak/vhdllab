package hr.fer.zemris.vhdllab.applets.main.conf;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;

import java.io.InputStream;

import org.apache.commons.digester.Digester;

public class ComponentConfigurationParser {

	public static ComponentConfiguration conf = null;

	public static ComponentConfiguration getConfiguration()
			throws UniformAppletException {
		if (conf == null) {
			conf = parseConfiguration();
		}
		return conf;
	}

	private static ComponentConfiguration parseConfiguration()
			throws UniformAppletException {
		Digester digester = new Digester();
		digester.setValidating(false);
		digester.addObjectCreate("configuration", ComponentConfiguration.class);
		digester.addObjectCreate("configuration/editors/editor",
				EditorProperties.class);
		digester.addSetProperties("configuration/editors/editor");
		String[] elements = new String[] {"class", "explicitSave"};
		String[] properties = new String[] {"clazz", null};
		digester.addSetNestedProperties("configuration/editors/editor", elements, properties);
		digester.addSetProperties("configuration/editors/editor/explicitSave",
				"value", "explicitSaveValue");
		digester.addSetNestedProperties("configuration/editors/editor/explicitSave",
				"class", "explicitSaveClass");
		digester.addSetNext("configuration/editors/editor", "addEditor");

		digester.addObjectCreate("configuration/views/view",
				ViewProperties.class);
		digester.addSetProperties("configuration/views/view");
		digester.addSetNestedProperties("configuration/views/view", "class",
				"clazz");
		digester.addSetNext("configuration/views/view", "addView");

		ComponentConfigurationParser c = new ComponentConfigurationParser();
		InputStream is = c.getClass().getResourceAsStream("configuration.xml");
		try {
			return (ComponentConfiguration) digester.parse(is);
		} catch (Exception e) {
			throw new UniformAppletException(e);
		}
	}

}
