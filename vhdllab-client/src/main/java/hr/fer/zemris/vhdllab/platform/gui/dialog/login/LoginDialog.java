package hr.fer.zemris.vhdllab.platform.gui.dialog.login;

import hr.fer.zemris.vhdllab.platform.gui.dialog.AbstractDialog;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSource;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class LoginDialog extends AbstractDialog<UserCredential> {

    private static final long serialVersionUID = 1L;

    private static final String TITLE = "dialog.login.title";
    private static final String BORDER_TITLE = "dialog.login.border.title";
    private static final String DEFAULT_MESSAGE = "dialog.login.message.default";
    private static final String RETRY_MESSAGE = "dialog.login.message.retry";
    private static final String USERNAME = "dialog.login.username";
    private static final String PASSWORD = "dialog.login.password";
    private static final String OK = "dialog.login.ok";
    private static final String CANCEL = "dialog.login.cancel";

    private static final int BORDER_SIZE = 10;
    private static final int BUTTON_WIDTH = 70;
    private static final int BUTTON_HEIGHT = 24;

    public LoginDialog(LocalizationSource source, boolean showRetryMessage) {
        super(source);
        JLabel usernameLabel = new JLabel(source.getMessage(USERNAME));
        JLabel passwordLabel = new JLabel(source.getMessage(PASSWORD));
        final JTextField usernameField = new JTextField();
        final JPasswordField passwordField = new JPasswordField();

        JPanel grid = new JPanel(new GridLayout(2, 2));
        grid.add(usernameLabel);
        grid.add(usernameField);
        grid.add(passwordLabel);
        grid.add(passwordField);
        Border outsideBorder = BorderFactory.createEmptyBorder(BORDER_SIZE,
                BORDER_SIZE, BORDER_SIZE, BORDER_SIZE);
        Border insideBorder = BorderFactory.createTitledBorder(source
                .getMessage(BORDER_TITLE));
        grid.setBorder(BorderFactory.createCompoundBorder(outsideBorder,
                insideBorder));

        JButton ok = new JButton(source.getMessage(OK));
        ok.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                if (!username.equals("")) {
                    String password = new String(passwordField.getPassword());
                    closeDialog(new UserCredential(username, password));
                } else {
                    usernameField.requestFocusInWindow();
                }
            }
        });

        JButton cancel = new JButton(source.getMessage(CANCEL));
        cancel.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog(null);
            }
        });

        Box actionBox = Box.createHorizontalBox();
        actionBox.add(ok);
        actionBox.add(Box.createRigidArea(new Dimension(BORDER_SIZE,
                BUTTON_HEIGHT)));
        actionBox.add(cancel);
        actionBox.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, 0,
                BORDER_SIZE, 0));
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.add(actionBox, BorderLayout.EAST);

        String message;
        if (showRetryMessage) {
            message = source.getMessage(RETRY_MESSAGE);
        } else {
            message = source.getMessage(DEFAULT_MESSAGE);
        }
        JLabel messageLabel = new JLabel(message);

        setLayout(new BorderLayout());
        add(messageLabel, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(ok);
        setTitle(source.getMessage(TITLE));
        setSize(new Dimension(300, 180));
        setMinimumSize(new Dimension(300, 190));
        startDialog();
    }

}
