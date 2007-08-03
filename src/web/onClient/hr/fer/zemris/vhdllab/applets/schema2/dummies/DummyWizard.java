package hr.fer.zemris.vhdllab.applets.schema2.dummies;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;






public class DummyWizard implements IWizard {
	
	private ISystemContainer systemContainer;

	public FileContent getInitialFileContent(Component parent, String projectName) {
//		URL url = this.getClass().getResource("dummySchema.xml");
		InputStream in = this.getClass().getResourceAsStream("dummySchema.xml");
//		FileReader freader;
		BufferedReader buffreader;
		StringBuilder sb = new StringBuilder();

		try {
			String s;
//			freader = new FileReader(url.getFile());
			buffreader = new BufferedReader(new InputStreamReader(in));
			while ((s = buffreader.readLine()) != null) {
				sb.append(s);
				sb.append('\n');
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		FileContent fc = new FileContent(projectName, "dummyName", sb.toString());
		
		return fc;
	}

	public void setSystemContainer(ISystemContainer container) {
		systemContainer = container;
	}
	
}














