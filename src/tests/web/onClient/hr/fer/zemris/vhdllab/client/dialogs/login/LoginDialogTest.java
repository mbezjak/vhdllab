/**
 * 
 */
package hr.fer.zemris.vhdllab.client.dialogs.login;

import hr.fer.zemris.vhdllab.i18n.CachedResourceBundles;

import java.awt.Frame;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;

/**
 * Tests {@link LoginDialog}, but just GUI. This is not an actual JUnit test.
 * 
 * @author Miro Bezjak
 */
public class LoginDialogTest {

	/**
	 * Start of test. Not a JUnit test! Just testing GUI.
	 * 
	 * @param args
	 *            no effect
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				initGUI();
			}
		});
	}

	private static void initGUI() {
		Frame f = new Frame();
		ResourceBundle bundle = CachedResourceBundles.getBundle("LoginDialog",
				"en");
		LoginDialog dialog = new LoginDialog(f, bundle);
		dialog.setVisible(true);
		System.out.println(dialog.getOption());
		System.out.println(dialog.getCredentials());
		f.dispose();
	}

}
