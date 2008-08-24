/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.client.core.log.MessageType;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Tests {@link InitializationGlassPane}, but just to see if its working. This
 * is not an actual JUnit test.
 * 
 * @author Miro Bezjak
 */
public class InitializationGlassPaneTest {

	/**
	 * Start of test. Not a JUnit test! Just testing to see if its working.
	 * 
	 * @param args
	 *            no effect
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setPreferredSize(new Dimension(900, 600));
		frame.setLayout(new BorderLayout());
		InitializationGlassPane glassPane = new InitializationGlassPane();
		frame.add(glassPane, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.pack();
		glassPane.setVisible(true);

		SystemLog.instance().addSystemMessage("System intialization started!",
				MessageType.INFORMATION);
	}

}
