package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars;

import hr.fer.zemris.vhdllab.applets.schema2.dummies.DummyOR;

import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class SComponentPropertiesToolbarTest {

	/**
	 * @param args
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException,
			InvocationTargetException {
		SwingUtilities.invokeAndWait(new Runnable() {

			public void run() {
				createAndShow();
			}

		});
	}

	private static void createAndShow() {
		JFrame frame = new JFrame("Proba propertya");
		SComponentPropertiesToolbar propertyToolbar = new SComponentPropertiesToolbar(
				new DummyOR("blabla"));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(new JLabel("Property"), BorderLayout.NORTH);
		frame.getContentPane().add(propertyToolbar, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}

}
