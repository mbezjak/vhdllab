package hr.fer.zemris.vhdllab.applets.editor.preferences;

import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.client.core.prefs.UserPreferences;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


/**
 * Tests {@link PreferencesEditor} dialog, but just to see if its
 * working. This is not an actual JUnit test.
 * 
 * @author Miro Bezjak
 */
public class PreferencesEditorTest extends JFrame {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public PreferencesEditorTest() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(700, 400));
		setLocation(200, 100);
		setLayout(new BorderLayout());
		initGUI();
		pack();
	}
	
	private void initGUI() {
		PreferencesEditor editor = new PreferencesEditor();
		editor.init();
		Properties p = new Properties();
		p.setProperty("a", "b");
		UserPreferences.instance().init(p);
		FileContent content = new FileContent("about", "config", "");
		editor.setFileContent(content);
		
		add(editor, BorderLayout.CENTER);
	}

	/**
	 * Start of test. Not a JUnit test! Just testing to see if its working.
	 * 
	 * @param args
	 *            no effect
	 */
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					new PreferencesEditorTest().setVisible(true);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
