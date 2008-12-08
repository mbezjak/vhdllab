package hr.fer.zemris.vhdllab.platform.gui.dialog.login;

import hr.fer.zemris.vhdllab.client.core.bundle.ResourceBundleProvider;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class LoginDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	/* BUNDLE CONSTANTS */
	public static final String BUNDLE_NAME = "LoginDialog";
	public static final String DEFAULT_MESSAGE = "message.default";
	public static final String RETRY_MESSAGE = "message.retry";
	private static final String TITLE = "title";
	private static final String BORDER_TITLE = "border.title";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String OK = "ok";
	private static final String CANCEL = "cancel";

	private static final int BORDER = 10;
	private static final int BUTTON_WIDTH = 70;
	private static final int BUTTON_HEIGHT = 24;

	/**
	 * Return value from class method if user closes window without selecting
	 * anything, more than likely this should be treated as
	 * <code>CANCEL_OPTION</code>.
	 */
	public static final int CLOSED_OPTION = -1;

	/**
	 * Return value from class method if CANCEL is chosen.
	 */
	public static final int CANCEL_OPTION = 2;

	/**
	 * Return value from class method if OK is chosen.
	 */
	public static final int OK_OPTION = 0;

	/**
	 * Variable to indicate an option that user chose. Option can be:
	 * <ul>
	 * <li>{@link #OK_OPTION}</li>
	 * <li>{@link #CANCEL_OPTION}</li>
	 * <li>{@link #CLOSED_OPTION}</li>
	 * </ul>
	 */
	private int option = CLOSED_OPTION;

	UserCredential credentials;

	public LoginDialog(Frame owner) {
		this(owner, null);
	}

	public LoginDialog(Frame owner, String message) {
		this(owner, null, message);
	}
	
	public LoginDialog(Frame owner, ResourceBundle b, String m) {
		super(owner, true);
		ResourceBundle bundle = b;
		if (bundle == null) {
			bundle = ResourceBundleProvider.getBundle(BUNDLE_NAME);
		}
		String text;
		text = bundle.getString(USERNAME);
		JLabel usernameLabel = new JLabel(text);
		text = bundle.getString(PASSWORD);
		JLabel passwordLabel = new JLabel(text);
		final JTextField usernameField = new JTextField();
		final JPasswordField passwordField = new JPasswordField();

		JPanel grid = new JPanel(new GridLayout(2, 2));
		grid.add(usernameLabel);
		grid.add(usernameField);
		grid.add(passwordLabel);
		grid.add(passwordField);
		text = bundle.getString(BORDER_TITLE);
		Border outsideBorder = BorderFactory.createEmptyBorder(BORDER, BORDER,
				BORDER, BORDER);
		Border insideBorder = BorderFactory.createTitledBorder(text);
		grid.setBorder(BorderFactory.createCompoundBorder(outsideBorder,
				insideBorder));

		text = bundle.getString(OK);
		JButton ok = new JButton(text);
		ok.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				if (!username.equals("")) {
					String password = new String(passwordField.getPassword());
					credentials = new UserCredential(username, password);
					close(OK_OPTION);
				} else {
					usernameField.requestFocusInWindow();
				}
			}
		});

		text = bundle.getString(CANCEL);
		JButton cancel = new JButton(text);
		cancel.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close(CANCEL_OPTION);
			}
		});

		Box actionBox = Box.createHorizontalBox();
		actionBox.add(ok);
		actionBox
				.add(Box.createRigidArea(new Dimension(BORDER, BUTTON_HEIGHT)));
		actionBox.add(cancel);
		actionBox.setBorder(BorderFactory.createEmptyBorder(BORDER, 0, BORDER,
				0));

		JPanel actionPanel = new JPanel(new BorderLayout());
		actionPanel.add(actionBox, BorderLayout.EAST);

		String message = m;
		if(message == null) {
			message = bundle.getString(DEFAULT_MESSAGE);
		}
		JLabel messageLabel = new JLabel(message);

		setLayout(new BorderLayout());
		add(messageLabel, BorderLayout.NORTH);
		add(grid, BorderLayout.CENTER);
		add(actionPanel, BorderLayout.SOUTH);
		getRootPane().setDefaultButton(ok);
		text = bundle.getString(TITLE);
		setTitle(text);
		setSize(new Dimension(300, 180));
		setMinimumSize(new Dimension(300, 190));
		setLocationRelativeTo(owner);
		pack();
	}

	/**
	 * @return the option
	 */
	public int getOption() {
		return option;
	}

	/**
	 * @return the credentials
	 */
	public UserCredential getCredentials() {
		return credentials;
	}

	void close(int o) {
		this.option = o;
		setVisible(false);
		dispose();
	}

}
