package hr.fer.zemris.vhdllab.applets.schema2.temporary;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.PredefinedComponentsParser;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.schema2.dummies.DummyProjectContainer;
import hr.fer.zemris.vhdllab.applets.schema2.dummies.DummyWizard;
import hr.fer.zemris.vhdllab.applets.schema2.gui.DefaultWizard;
import hr.fer.zemris.vhdllab.applets.schema2.gui.SchemaMainPanel;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaInfo2VHDL;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaSerializer;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.extractor.schema.SchemaCircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;






public class Tester {
	
	{
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
	}
}

	/**
	 * Privremeno radi testiranja.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Tester();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				testSchema();
			}
		});
	}
	
	private static void testSchema() {
		// test init
		SchemaMainPanel mpanel = new SchemaMainPanel();
		ProjectContainer dummypc = new DummyProjectContainer();
		mpanel.setProjectContainer(dummypc);
		mpanel.init();
		
		// test setFileContent
		IWizard wiz = new DefaultWizard();
		wiz.setProjectContainer(new DummyProjectContainer());
		FileContent fc = wiz.getInitialFileContent(null, "dummyProject");
		mpanel.setFileContent(fc);
		
		// create frame
		JFrame frame = new JFrame();
		
		frame.setLayout(new BorderLayout());
		frame.add(mpanel, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setPreferredSize(new Dimension(750, 500));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		
//		InstantiateComponentCommand instantiate = new InstantiateComponentCommand(new Caseless("Input_scalar"), 40, 40);
//		mpanel.getController().send(instantiate);
//		instantiate = new InstantiateComponentCommand(new Caseless("Input_scalar"), 40, 120);
//		mpanel.getController().send(instantiate);
//		instantiate = new InstantiateComponentCommand(new Caseless("Output_scalar"), 520, 40);
//		mpanel.getController().send(instantiate);
//		instantiate = new InstantiateComponentCommand(new Caseless("VL_XOR"), 320, 40);
//		mpanel.getController().send(instantiate);
		
		SchemaInfo2VHDL si2vhdl = new SchemaInfo2VHDL();
		System.out.println(si2vhdl.generateVHDL(mpanel.getController().getSchemaInfo()));
		
		SchemaCircuitInterfaceExtractor sciextract = new SchemaCircuitInterfaceExtractor();
		try {
			File f = new File();
			SchemaSerializer ss = new SchemaSerializer();
			StringWriter writer = new StringWriter(1500);
			ss.serializeSchema(writer, mpanel.getController().getSchemaInfo());
			
			f.setContent(writer.toString());
			CircuitInterface ci = sciextract.extractCircuitInterface(f);
			ci.equals(null);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void testDummyWiz() {
		DummyWizard wiz = new DummyWizard();
		
		FileContent fc = wiz.getInitialFileContent(null, "dummyProject");
		
		System.out.println(fc.getContent());
	}
	
	private static void testPredef() {
		PredefinedComponentsParser pcp = new PredefinedComponentsParser("configuration.xml");
		
		pcp.getConfiguration();
	}
	
	private static void testSD() {
		SchemaDeserializer sd = new SchemaDeserializer();
		FileReader fr = null;
		ISchemaInfo info = null;
		
		try {
			fr = new FileReader("d:/sample.xml");
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(1);
		}
		
		try {
			info = sd.deserializeSchema(fr);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Ok, parsed.");
		
		SchemaSerializer ss = new SchemaSerializer();
		PrintWriter pw = new PrintWriter(System.out);
		try {
			ss.serializeSchema(pw, info);
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}














