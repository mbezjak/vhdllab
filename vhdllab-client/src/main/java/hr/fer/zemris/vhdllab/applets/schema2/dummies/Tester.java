package hr.fer.zemris.vhdllab.applets.schema2.dummies;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQuery;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQueryResult;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.SchemaInfo2VHDL;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.AddWireCommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.InstantiateComponentCommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.queries.FindConnectedPins;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.SchemaSerializer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.schema2.gui.DefaultWizard;
import hr.fer.zemris.vhdllab.applets.schema2.gui.SchemaMainPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

//import de.muntjak.tinylookandfeel.TinyLookAndFeel;






public class Tester {
	
//	{
//		try {
////			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
//			TinyLookAndFeel lnf = new TinyLookAndFeel();
//			UIManager.setLookAndFeel(lnf);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
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

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
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
		final SchemaMainPanel mpanel = new SchemaMainPanel();
		ISystemContainer dummypc = new DummySystemContainer();
		mpanel.setSystemContainer(dummypc);
		mpanel.init();
		
		// test setFileContent
		IWizard wiz = new DefaultWizard();
		wiz.setSystemContainer(new DummySystemContainer());
		FileContent fc = wiz.getInitialFileContent(null, new hr.fer.zemris.vhdllab.entities.Caseless("dummyProject"));
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
		InstantiateComponentCommand instantiate = new InstantiateComponentCommand(
				new Caseless("VL_OR"), 150, 50);
		mpanel.getController().send(instantiate);
//		ISchemaComponent cmp = mpanel.getController().getSchemaInfo().getComponents().fetchComponent(70, 50, 10);
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
		
//		InspectWalkability inspect = new InspectWalkability(false);
//		IQueryResult result = mpanel.getController().send(inspect);
		
//		SmartConnect connect = new SmartConnect(new XYLocation(10, 10), new XYLocation(250, 150),
//				(WalkabilityMap)(result.get(InspectWalkability.KEY_WALKABILITY)));
//		result = mpanel.getController().send(connect);
		
		final ISchemaInfo nfo = mpanel.getController().getSchemaInfo();
		mpanel.getController().addListener(EPropertyChange.ANY_CHANGE, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				SchemaInfo2VHDL si2vhdl = new SchemaInfo2VHDL();
				System.out.println(si2vhdl.generateVHDL(nfo).getVHDL());
				SchemaSerializer ss = new SchemaSerializer();
				StringWriter sw = new StringWriter();
				try {
					ss.serializeSchema(sw, nfo);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println(sw.toString());
				SchemaDeserializer sd = new SchemaDeserializer();
				ISchemaInfo info = sd.deserializeSchema(new StringReader(sw.toString()));
				
				IQuery q = new FindConnectedPins(new Caseless("zica1"));
				IQueryResult result = mpanel.getController().send(q);
				if (result.isSuccessful()) {
					System.out.println(result.get(FindConnectedPins.KEY_PIN_LOCATIONS));
				} else {
					System.out.println("Nema takve.");
				}
				
				System.out.println(nfo.getWires().distanceTo(new Caseless("zica1"), 120, 310));
			}
		});
		
		
		// FIXME COMMENTED BECAUSE OF COMPILATION ERRORS!
//		SchemaCircuitInterfaceExtractor sciextract = new SchemaCircuitInterfaceExtractor();
//		try {
//			File f = new File();
//			SchemaSerializer ss = new SchemaSerializer();
//			StringWriter writer = new StringWriter(1500);
//			ss.serializeSchema(writer, mpanel.getController().getSchemaInfo());
//			
//			f.setContent(writer.toString());
//			CircuitInterface ci = sciextract.extractCircuitInterface(f);
//
//			SchemaDeserializer ds = new SchemaDeserializer();
//			ds.deserializeSchema(new StringReader(writer.toString()));
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
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














