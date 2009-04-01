package hr.fer.zemris.vhdllab.applets.editor.newtb.help;

import hr.fer.zemris.vhdllab.applets.editor.newtb.TestbenchEditor;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.InitTimingDialog;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.AlternatePanel;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.CountPanel;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.LShiftPatternPanel;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.PulsePanel;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.RShiftPatternPanel;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.RandomPanel;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.RandomVectorPanel;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.TogglePanel;

import java.awt.Dialog.ModalExclusionType;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * @author Davor Jurisic
 *
 */
public class HelpManager {
	
	private static JFrame helpFrame = null;
	private static JHelpManager jHelp = null;
	private static Map<Class<?>, String> helpCodeMap = null;
	
	public static String getHelpCode(Class<?> c)
	{
		String r = getHelpCodeMap().get(c);
		if(r == null) {
			return "";
		}
		return r;
	}

	public static void openHelpDialog(String helpCode)
	{
		getJHelp().openHelpPage(helpCode);
		getHelpFrame().setVisible(true);
	}
	
	public static void openHelpDialog()
	{
		getJHelp().openInitHelpPage();
		getHelpFrame().setVisible(true);
	}
	
	private static Map<Class<?>, String> getHelpCodeMap() {
		if(helpCodeMap == null) {
			helpCodeMap = new HashMap<Class<?>, String>();
			/*
			 * Patterns
			 */
			helpCodeMap.put(AlternatePanel.class, "TB-PAT-VE-ALT");
			helpCodeMap.put(CountPanel.class, "TB-PAT-VE-CNT");
			helpCodeMap.put(RShiftPatternPanel.class, "TB-PAT-VE-SHR");
			helpCodeMap.put(LShiftPatternPanel.class, "TB-PAT-VE-SHL");
			helpCodeMap.put(RandomVectorPanel.class, "TB-PAT-VE-RND");
			helpCodeMap.put(PulsePanel.class, "TB-PAT-SC-PUL");
			helpCodeMap.put(RandomPanel.class, "TB-PAT-SC-RND");
			helpCodeMap.put(TogglePanel.class, "TB-PAT-SC-TOG");
			/*
			 * Init timing dialog, waveform
			 */
			helpCodeMap.put(TestbenchEditor.class, "TB-WAVEEDIT");
			helpCodeMap.put(InitTimingDialog.class, "TB-INITTIM");
		}
		return helpCodeMap;
	}
	
	private static JHelpManager getJHelp() {
		if(jHelp == null) {
			jHelp = new JHelpManager(getResourceIndexDocument());
		}
		return jHelp;
	}
	
	private static JFrame getHelpFrame() {
		if(helpFrame == null) {
	        helpFrame = new JFrame("VHDL Lab Help Manager");
	        helpFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	        helpFrame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
	        helpFrame.add(getJHelp());
	        helpFrame.pack();
		}
		return helpFrame;
	}
	
	private static Document getResourceIndexDocument() {
		try {
			InputStream is = HelpManager.class.getResourceAsStream("resources/resourceIndex.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = factory.newDocumentBuilder();
			InputSource in = new InputSource(is);
			return db.parse(in);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
