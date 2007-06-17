package hr.fer.zemris.vhdllab.applets.schema2.dummies;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.CharBuffer;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;






public class DummyWizard implements IWizard {
	
	private ProjectContainer projectContainer;

	public FileContent getInitialFileContent(Component parent, String projectName) {
		URL url = this.getClass().getResource("dummySchema.xml");
		FileReader freader;
		BufferedReader buffreader;
		StringBuilder sb = new StringBuilder();

		try {
			String s;
			freader = new FileReader(url.getFile());
			buffreader = new BufferedReader(freader);
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

	public void setProjectContainer(ProjectContainer container) {
		projectContainer = container;
	}
	
}














