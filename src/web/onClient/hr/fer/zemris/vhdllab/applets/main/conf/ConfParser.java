package hr.fer.zemris.vhdllab.applets.main.conf;

import java.io.IOException;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

public class ConfParser {

	public static void main(String[] args) {
		String[] params;
		String[] methods;
		Digester digester = new Digester();
		digester.setValidating(false);
		digester.addObjectCreate("configuration", AppletConf.class);
		digester.addObjectCreate("configuration/editors/editor", EditorProperties.class);
		params = new String[] {"name", "fileType", "class", "savable", "readonly"};
		methods = new String[] {"name", "fileType", "clazz", "savable", "readonly"};
		digester.addSetProperties("configuration/editors/editor", params, methods);
		params = new String[] {"enabled", "class"};
		methods = new String[] {"explicitSave", "explicitSaveClass"};
		digester.addSetProperties("configuration/editors/editor/explicitSave", params, methods);
		digester.addSetNext("configuration/editors/editor", "addEditor");
		
		digester.addObjectCreate("configuration/views/view", ViewProperties.class);
		params = new String[] {"name", "viewType", "class", "singleton"};
		methods = new String[] {"name", "viewType", "clazz", "singleton"};
		digester.addSetProperties("configuration/views/view", params, methods);
		digester.addSetNext("configuration/views/view", "addView");

		ConfParser c = new ConfParser();
		try {
			AppletConf conf = (AppletConf) digester.parse(c.getClass().getResourceAsStream("configuration.xml"));
			System.out.print(conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
