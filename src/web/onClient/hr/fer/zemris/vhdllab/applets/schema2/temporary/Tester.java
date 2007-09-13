package hr.fer.zemris.vhdllab.applets.schema2.temporary;

import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.schema2.dummies.DummySystemContainer;
import hr.fer.zemris.vhdllab.applets.schema2.gui.DefaultWizard;
import hr.fer.zemris.vhdllab.applets.schema2.gui.SchemaMainPanel;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IQueryResult;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaInfo2VHDL;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.AddWireCommand;
import hr.fer.zemris.vhdllab.applets.schema2.model.queries.InspectWalkability;
import hr.fer.zemris.vhdllab.applets.schema2.model.queries.SmartConnect;
import hr.fer.zemris.vhdllab.applets.schema2.model.queries.misc.WalkabilityMap;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaSerializer;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.extractor.schema.SchemaCircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;






public class Tester {
	
	{
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
//			SyntheticaLookAndFeel slaf = new SyntheticaStandardLookAndFeel();
//			UIManager.setLookAndFeel(slaf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static class TestComp implements Comparable {
		private int num;
		
		public TestComp(int n) {
			num = n;
		}
		
		public int getNum() {
			return num;
		}
		
		public int compareTo(Object o) {
			TestComp other = (TestComp)o;
			if (this.num < other.num) return -1;
			if (this.num > other.num) return 1;
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof TestComp)) return false;
			TestComp other = (TestComp)obj;
			return this.num == other.num;
		}

		@Override
		public int hashCode() {
			return num;
		}

		@Override
		public String toString() {
			return "TestComp(num=" + num + ")";
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
		ISystemContainer dummypc = new DummySystemContainer();
		mpanel.setSystemContainer(dummypc);
		mpanel.init();
		
		// test setFileContent
		IWizard wiz = new DefaultWizard();
		wiz.setSystemContainer(new DummySystemContainer());
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
		
		AddWireCommand addwire = new AddWireCommand(new Caseless("zica1"), 100, 300, 350, 300);
		mpanel.getController().send(addwire);
//		InstantiateComponentCommand instantiate = new InstantiateComponentCommand(
//				new Caseless("VL_OR"), 50, 0);
//		mpanel.getController().send(instantiate);
//		ExpandWireCommand expand = new ExpandWireCommand(new Caseless("zica1"), 150, 50, 150, 100);
//		mpanel.getController().send(expand);
//		expand = new ExpandWireCommand(new Caseless("zica1"), 150, 100, 300, 100);
//		mpanel.getController().send(expand);
//		expand = new ExpandWireCommand(new Caseless("zica1"), 50, 50, 50, 250);
//		mpanel.getController().send(expand);
//		
//		DeleteSegmentAndDivideCommand delndiv = new DeleteSegmentAndDivideCommand(new Caseless("zica1"),
//				new WireSegment(150, 50, 150, 100));
//		mpanel.getController().send(delndiv);
		
		SchemaInfo2VHDL si2vhdl = new SchemaInfo2VHDL();
		System.out.println(si2vhdl.generateVHDL(mpanel.getController().getSchemaInfo()));
		
		InspectWalkability inspect = new InspectWalkability(false);
		IQueryResult result = mpanel.getController().send(inspect);
		
		SmartConnect connect = new SmartConnect(new XYLocation(10, 10), new XYLocation(250, 150),
				(WalkabilityMap)(result.get(InspectWalkability.KEY_WALKABILITY)));
		result = mpanel.getController().send(connect);
		
		SchemaCircuitInterfaceExtractor sciextract = new SchemaCircuitInterfaceExtractor();
		try {
			File f = new File();
			SchemaSerializer ss = new SchemaSerializer();
			StringWriter writer = new StringWriter(1500);
			ss.serializeSchema(writer, mpanel.getController().getSchemaInfo());
			
			f.setContent(writer.toString());
			CircuitInterface ci = sciextract.extractCircuitInterface(f);

			SchemaDeserializer ds = new SchemaDeserializer();
			ds.deserializeSchema(new StringReader(writer.toString()));
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














