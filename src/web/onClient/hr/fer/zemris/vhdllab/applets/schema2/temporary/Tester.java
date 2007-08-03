package hr.fer.zemris.vhdllab.applets.schema2.temporary;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.schema2.dummies.DummyProjectContainer;
import hr.fer.zemris.vhdllab.applets.schema2.gui.DefaultWizard;
import hr.fer.zemris.vhdllab.applets.schema2.gui.SchemaMainPanel;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaInfo2VHDL;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.AddWireCommand;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.DeleteSegmentAndDivideCommand;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.ExpandWireCommand;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaSerializer;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.extractor.schema.SchemaCircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
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
		ISystemContainer dummypc = new DummyProjectContainer();
		mpanel.setSystemContainer(dummypc);
		mpanel.init();
		
		// test setFileContent
		IWizard wiz = new DefaultWizard();
		wiz.setSystemContainer(new DummyProjectContainer());
		FileContent fc = wiz.getInitialFileContent(null, "dummyProject");
		mpanel.setFileContent(fc);
		
		// create frame
		JFrame frame = new JFrame("Schema test");
		
		frame.setLayout(new BorderLayout());
		frame.add(mpanel, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setPreferredSize(new Dimension(750, 500));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		
		AddWireCommand addwire = new AddWireCommand(new Caseless("zica1"), 50, 50, 150, 50);
		mpanel.getController().send(addwire);
		ExpandWireCommand expand = new ExpandWireCommand(new Caseless("zica1"), 150, 50, 150, 100);
		mpanel.getController().send(expand);
		expand = new ExpandWireCommand(new Caseless("zica1"), 150, 100, 300, 100);
		mpanel.getController().send(expand);
		expand = new ExpandWireCommand(new Caseless("zica1"), 50, 50, 50, 250);
		mpanel.getController().send(expand);
		
		DeleteSegmentAndDivideCommand delndiv = new DeleteSegmentAndDivideCommand(new Caseless("zica1"),
				new WireSegment(150, 50, 150, 100));
		mpanel.getController().send(delndiv);
		
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	private static void testDummyWiz() {
//		DummyWizard wiz = new DummyWizard();
//		
//		FileContent fc = wiz.getInitialFileContent(null, "dummyProject");
//		
//		System.out.println(fc.getContent());
//	}
//	
//	private static void testPredef() {
//		PredefinedComponentsParser pcp = new PredefinedComponentsParser("configuration.xml");
//		
//		pcp.getConfiguration();
//	}
//	
//	private static void testSD() {
//		SchemaDeserializer sd = new SchemaDeserializer();
//		FileReader fr = null;
//		ISchemaInfo info = null;
//		
//		try {
//			fr = new FileReader("d:/sample.xml");
//		} catch (FileNotFoundException e) {
//			System.out.println("File not found");
//			System.exit(1);
//		}
//		
//		try {
//			info = sd.deserializeSchema(fr);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//		
//		System.out.println("Ok, parsed.");
//		
//		SchemaSerializer ss = new SchemaSerializer();
//		PrintWriter pw = new PrintWriter(System.out);
//		try {
//			ss.serializeSchema(pw, info);
//			pw.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}














