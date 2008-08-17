package hr.fer.zemris.vhdllab.preferences.global;


import java.io.StringReader;

import org.apache.commons.digester.Digester;

public class PreferencesParser {
	
	public static Property parseProperty(String data) {
		Digester digester = new Digester();
		digester.setValidating(false);

		digester.addObjectCreate("property", Property.class);
		digester.addSetProperties("property");
		digester.addBeanPropertySetter("property/description");
		digester.addBeanPropertySetter("property/tooltip");
		String[] params = new String[] { "byUser" };
		String[] props = new String[] { "editableByUser" };
		digester.addSetProperties("property/editable", params, props);

		digester.addObjectCreate("property/data", PropertyData.class);
		digester.addSetProperties("property/data");
		digester.addCallMethod("property/data/values/value", "addValue", 1);
		digester.addCallParam("property/data/values/value", 0);
		digester.addCallMethod("property/data/", "setDefaultValue", 1);
		digester.addCallParam("property/data/default", 0);

		digester.addSetNext("property/data", "setData");

		try {
			return (Property) digester.parse(new StringReader(data));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}

}
