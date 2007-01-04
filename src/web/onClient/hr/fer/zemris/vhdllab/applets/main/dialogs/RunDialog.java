package hr.fer.zemris.vhdllab.applets.main.dialogs;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * A run dialog that displays to a user list of files and he should select
 * one. To use this dialog just use one constructor, modify text to buttons
 * (optional) and just invoke method {@link #startDialog()}. After that
 * controls are locked and youre code will stop at that method. When user
 * clicks on a button (OK, CANCEL or CLOSE) controls will become unlocked
 * and youre code will resume just after {@link #startDialog()} method. To
 * know what button user clicked use {@link #getOption()} method. Finaly
 * to get what file user selected use {@link #getSelectedFile()} method.
 * This dialog uses project container to get files to display and dialog
 * type to chose which method to invoke. So far these are dialog types:
 * <ul>
 * <li>{@link #COMPILATION_TYPE} - uses {@link ProjectContainer#getAllCircuits(String)} method</li>
 * <li>{@link #SIMULATION_TYPE} - uses {@link ProjectContainer#getAllTestbenches(String)} method</li>
 * </ul>
 * <p>Here is an example of how to user this dialog:</p>
 * <code>
 * <pre>
 * RunDialog dialog = new RunDialog(this, true, this, RunDialog.COMPILATION_TYPE);
 * dialog.setCancelButtonText("Wait a minute, I forgot something");
 * dialog.setActiveProjectText("'Project1' is currently made active.");
 * dialog.setTitle("A Title");
 * dialog.startDialog();
 * String fileName = dialog.getSelectedFile();
 * ...
 * </pre>
 * </code>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 28.12.2006
 */
public class RunDialog extends JDialog {
	
	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = -2559718473779566728L;
	/** Size of a border */
	private static final int BORDER = 10;
	/** Width of this dialog */
	private static final int DIALOG_WIDTH = 300;
	/** Height of this dialog */
	private static final int DIALOG_HEIGHT = 400;
	/** Height of a currentProjectLabel */
	private static final int LABEL_HEIGHT = 50;
	/** Width of all buttons */
	private static final int BUTTON_WIDTH = 70;
	/** Height of all buttons */
	private static final int BUTTON_HEIGHT = 24;
	/** Height of one menu item */
	private static final int MENU_ITEM_HEIGHT = 24;
	/** Maximum width of a menu bar */
	private static final int MENU_BAR_MAX_WIDTH = DIALOG_HEIGHT / 2;
	/** Minimum width of a menu bar */
	private static final int MENU_BAR_MIN_WIDTH = DIALOG_HEIGHT / 4;
	
	/** Owner of this dialog (used to enable modal dialog) */
	private Component owner;
	/** Project Container to enable communication */
	private ProjectContainer container;
	/** Current displayed project */
	private String currentProject;
	
	/** 
	 * A Popup menu that will contain all projects and will allow
	 * to change current project
	 */
	private JPopupMenu popupMenu;
	/** Label to contain a name of current displayed project */
	private JLabel currentProjectLabel;
	/** Button to change current project */
	private JButton changeProjectButton;
	/** Container for currentProjectLabel and changeProjectButton */
	private JPanel currentProjectPanel;
	/** Border for currentProjectPanel */
	private TitledBorder currentProjectBorder;
	
	/** A model for fileList */
	private DefaultListModel listModel;
	/** A list for files */
	private JList fileList;
	/** Border for fileList */
	private TitledBorder listBorder;
	
	/** OK button to end this dialog with {@link #OK_OPTION} */
	private JButton ok;
	/** Cancel button to end this dialog with {@link #CANCEL_OPTION} */
	private JButton cancel;
	/**
	 * Variable to indicate an option that user chose. Option can
     * be:
     * <ul>
     * <li>{@link #OK_OPTION}</li>
     * <li>{@link #CANCEL_OPTION}</li>
     * <li>{@link #CLOSED_OPTION}</li>
     * </ul>
	 */
	private int option = -1;
	
	/**
	 * Return value from class method if user closes window without selecting
	 * anything, more than likely this should be treated as <code>CANCEL_OPTION</code>. 
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
	 * A type of a dialog (what is this dialog used for). Dialog type can be:
	 * <ul>
	 * <li>{@link #COMPILATION_TYPE}</li>
	 * <li>{@link #SIMULATION_TYPE}</li>
	 * </ul>
	 */
	private int dialogType;
	
	/**
	 * A compilation type dialog means that this dialog will be used as a 
	 * compilation wizard.
	 */
	public static final int COMPILATION_TYPE = 0;

	/**
	 * A simulation type dialog means that this dialog will be used as a 
	 * simulation wizard.
	 */
	public static final int SIMULATION_TYPE = 1;


    /**
     * Creates a modal or non-modal dialog without a title and
     * with the specified owner <code>Frame</code>.  If <code>owner</code>
     * is <code>null</code>, a shared, hidden frame will be set as the
     * owner of the dialog.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     * <p>
     * Dialog type can be:
	 * <ul>
	 * <li>{@link #COMPILATION_TYPE}</li>
	 * <li>{@link #SIMULATION_TYPE}</li>
	 * </ul>
     *
     * @param owner the <code>Frame</code> from which the dialog is displayed
     * @param modal  true for a modal dialog, false for one that allows
     *               others windows to be active at the same time
     * @param container project container to enable communication
     * @param dialogType a type of this dialog
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public RunDialog(Frame owner, boolean modal, ProjectContainer container, int dialogType) throws HeadlessException {
        super(owner, null, modal);
        runDialogImpl(owner, container, dialogType);
    }


    /**
     * Creates a modal or non-modal dialog without a title and
     * with the specified owner dialog.
     * <p>
     * This constructor sets the component's locale property to the value 
     * returned by <code>JComponent.getDefaultLocale</code>.
     * <p>
     * Dialog type can be:
	 * <ul>
	 * <li>{@link #COMPILATION_TYPE}</li>
	 * <li>{@link #SIMULATION_TYPE}</li>
	 * </ul>
     *
     * @param owner the non-null <code>Dialog</code> from which the dialog is displayed
     * @param modal  true for a modal dialog, false for one that allows
     *               other windows to be active at the same time
     * @param container project container to enable communication
     * @param dialogType a type of this dialog
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public RunDialog(Dialog owner, boolean modal, ProjectContainer container, int dialogType) throws HeadlessException {
        super(owner, null, modal);
        runDialogImpl(owner, container, dialogType);
    }

    /**
     * Creates a modal or non-modal dialog without a title and
     * with the specified owner <code>JApplet</code>.  If <code>owner</code>
     * is <code>null</code>, a shared, hidden frame will be set as the
     * owner of the dialog.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     * <p>
     * Dialog type can be:
	 * <ul>
	 * <li>{@link #COMPILATION_TYPE}</li>
	 * <li>{@link #SIMULATION_TYPE}</li>
	 * </ul>
	 * 
     * @param owner the <code>JApplet</code> from which the dialog is displayed
     * @param modal  true for a modal dialog, false for one that allows
     *               others windows to be active at the same time
     * @param container project container to enable communication
     * @param dialogType a type of this dialog
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public RunDialog(JApplet owner, boolean modal, ProjectContainer container, int dialogType) throws HeadlessException {
        super(getAppletFrame(owner), null, modal);
        runDialogImpl(owner, container, dialogType);
    }

    /**
     * Implemetation of a constructor of <code>RunDialog</code>.
     * @param owner owner for which the dialog is displayed
     * @param container project container to enable communication
     * @param dialogType a type of this dialog
     */
    private void runDialogImpl(Component owner, ProjectContainer container, int dialogType) {
    	this.owner = owner;
    	this.container = container;
    	this.dialogType = dialogType;
    	
    	int width = 0;
    	int height = 0;
    	
    	popupMenu = new JPopupMenu();
    	Font font = popupMenu.getFont();
    	FontMetrics metrics = popupMenu.getFontMetrics(font);
    	List<String> allProjects = container.getAllProjects();
    	for(final String project : allProjects) {
    		JMenuItem item = new JMenuItem(project);
    		item.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				changeActiveProject(project);
    			}
    		});
    		popupMenu.add(item);
    		int stringWidth = metrics.stringWidth(project);
    		if(stringWidth > width) {
    			width = stringWidth;
    		}
    	}
    	currentProject = container.getSelectedProject();
    	if(currentProject == null && !allProjects.isEmpty()) {
    		currentProject = allProjects.get(allProjects.size() - 1);
    	}
    	if(width > MENU_BAR_MAX_WIDTH) {
    		width = MENU_BAR_MAX_WIDTH;
    	}
    	if(width < MENU_BAR_MIN_WIDTH) {
    		width = MENU_BAR_MIN_WIDTH;
    	}
    	popupMenu.setPreferredSize(new Dimension(width, allProjects.size() * MENU_ITEM_HEIGHT));
    	
    	// setup current project label
    	currentProjectPanel = new JPanel(new BorderLayout());
    	currentProjectLabel = new JLabel();
    	width = DIALOG_WIDTH - 2 * BORDER;
    	height = LABEL_HEIGHT - 2 * BORDER;
    	currentProjectLabel.setPreferredSize(new Dimension(width, height));
    	changeProjectButton = new JButton("change");
    	changeProjectButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			JButton source = (JButton) e.getSource();
    			preformPopupMenuAction(source);
    		}
    	});
    	currentProjectPanel.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));
    	currentProjectPanel.add(currentProjectLabel, BorderLayout.CENTER);
    	currentProjectPanel.add(changeProjectButton, BorderLayout.EAST);
    	currentProjectBorder = BorderFactory.createTitledBorder("Displayed Project");
    	currentProjectPanel.setBorder(currentProjectBorder);
    	
    	// setup file listModel
    	listModel = new DefaultListModel();
    	fileList = new JList(listModel);
    	fileList.addMouseListener(new MouseListener() {
    		public void mouseClicked(MouseEvent e) {
    			if(e.getClickCount() == 2) {
    				preformListAction();
    			}
    		}
    		public void mouseEntered(MouseEvent e) {}
    		public void mouseExited(MouseEvent e) {}
    		public void mousePressed(MouseEvent e) {}
    		public void mouseReleased(MouseEvent e) {}
    	});
    	setupList();
    	
    	JPanel listPanel = new JPanel(new BorderLayout());
    	listBorder = BorderFactory.createTitledBorder("Select file to run");
    	Border outsideBorder = BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER);
    	listPanel.setBorder(BorderFactory.createCompoundBorder(outsideBorder, listBorder));
    	JScrollPane scroll = new JScrollPane(fileList);
    	listPanel.add(scroll, BorderLayout.CENTER);
    	
    	// setup ok and cancel buttons
		ok = new JButton("OK");
    	ok.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
    	ok.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			preformListAction();
    		}
    	});
    	
    	cancel = new JButton("Cancel");
    	cancel.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
    	cancel.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			option = RunDialog.CANCEL_OPTION;
    			close();
    		}
    	});
    	
    	Box actionBox = Box.createHorizontalBox();
    	actionBox.add(ok);
    	actionBox.add(Box.createRigidArea(new Dimension(BORDER, BUTTON_HEIGHT)));
    	actionBox.add(cancel);
    	actionBox.setBorder(BorderFactory.createEmptyBorder(BORDER, 0, BORDER, 0));
    	
    	JPanel actionPanel = new JPanel(new BorderLayout());
    	actionPanel.add(actionBox, BorderLayout.EAST);
    	
    	this.setLayout(new BorderLayout());
    	JPanel messagePanel = new JPanel(new BorderLayout());
    	messagePanel.add(currentProjectPanel, BorderLayout.NORTH);
    	messagePanel.add(listPanel, BorderLayout.CENTER);
    	messagePanel.add(actionPanel, BorderLayout.SOUTH);
    	this.getContentPane().add(messagePanel, BorderLayout.CENTER);
    	this.getRootPane().setDefaultButton(ok);
    	this.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
    	setChangeProjectButtonText(null);
    	if(getTitle() == null) {
    		setTitle("Run");
    	}
    }
    
    /**
     * Starts a dialog and locks the control.
     * @throws IllegalStateException if there are no items to run
     */
    public void startDialog() {
    	if(listModel.isEmpty()) {
    		throw new IllegalStateException("There are no items to run.");
    	}
    	this.pack();
    	this.setLocationRelativeTo(owner);
    	this.setVisible(true);
    }
    
    /**
     * Returns what button was pressed in this dialog as an option. Option can
     * be:
     * <ul>
     * <li>{@link #OK_OPTION}</li>
     * <li>{@link #CANCEL_OPTION}</li>
     * <li>{@link #CLOSED_OPTION}</li>
     * </ul>
     * @return an option
     */
    public int getOption() {
    	return option;
    }
    
    /**
     * Returns file name that were selected by user to run.
     * This method will return <code>null</code> if user did not click
     * on a OK button.
     * @return a name of a file that was selected
     */
    public String getSelectedFile() {
    	if(getOption() != RunDialog.OK_OPTION) {
    		return null;
    	}
    	return (String)fileList.getSelectedValue();
    }
    
    /**
     * Set text to be displayed as current project title just above a current
     * project label. If <code>text</code> is <code>null</code> then current
     * project title will be set to an empty string.
     * @param text text to be displayed
     */
    public void setCurrentProjectTitle(String text) {
    	if(text == null) text = "";
    	currentProjectBorder.setTitle(text);
    }
    
    /**
     * Set text to be displayed in a current project label just above a list of files.
     * If <code>text</code> is <code>null</code> then current project label will
     * be set to an empty string.
     * @param text text to be displayed
     */
    public void setCurrentProjectText(String text) {
    	if(text == null) text = "";
    	currentProjectLabel.setText(text);
    }
    
    /**
     * Set text to be displayed in a change project button, left to current project
     * label. If <code>text</code> is <code>null</code> then change project button
     * will be set to an empty string.
     * @param text text to be displayed
     */
    public void setChangeProjectButtonText(String text) {
    	if(text == null) text = "";
    	changeProjectButton.setText(text);
    }
    
    /**
     * Set text to be displayed as a title of file list. If <code>text</code> is
     * <code>null</code> then list title will be set to an empty string.
     * @param text text to be displayed
     */
    public void setListTitle(String text) {
    	if(text == null) text = "";
    	listBorder.setTitle(text);
	}
    
    /**
     * Set text to be displayed in an <code>OK</code> button. If
     * <code>text</code> is <code>null</code> then button will be
     * set to an empty string.
     * @param text text to be displayed
     */
    public void setOKButtonText(String text) {
    	if(text == null) text = "";
    	ok.setText(text);
    }
    
    /**
     * Set text to be displayed in an <code>cancel</code> button. If
     * <code>text</code> is <code>null</code> then button will be set
     * to an empty string.
     * @param text text to be displayed
     */
    public void setCancelButtonText(String text) {
    	if(text == null) text = "";
    	cancel.setText(text);
    }
    
    /**
     * Set title to be displayed in this dialog. If <code>title</code> is
     * <code>null</code> then title will be set to an empty string.
     * @param title text to be displayed
     */
    @Override
    public void setTitle(String title) {
    	if(title == null) title = "";
    	super.setTitle(title);
    }
    
    /**
     * Preforms action to be taken after user wants to exist this dialog
     * with {@link #OK_OPTION}.
     */
    private void preformListAction() {
    	if(fileList.getSelectedValue() != null) {
			option = RunDialog.OK_OPTION;
			close();
		}
    }
    
    /**
     * Shows popup menu relative to <code>invoker</code> location
     * @param invoker component that is invoking this popup
     */
    private void preformPopupMenuAction(Component invoker) {
		int x = 0;
    	int y = 0;
    	int difference = invoker.getSize().width - popupMenu.getPreferredSize().width;
    	if(difference < 0) {
    		x = difference;
    	}
    	y += invoker.getSize().height;
		popupMenu.show(invoker, x, y);
	}
    
    /**
     * Loads a list of files to be shown in fileList. Which file will be loaded
     * depends on dialog type.
     */
    private void setupList() {
    	listModel.clear();
    	List<String> files = null;
    	try {
			if(dialogType == RunDialog.COMPILATION_TYPE) {
				files = container.getAllCircuits(currentProject);
			} else if(dialogType == RunDialog.SIMULATION_TYPE) {
				files = container.getAllTestbenches(currentProject);
			}
		} catch (UniformAppletException e) {
			files = null;
		}
    	if(files == null) {
    		files = Collections.emptyList();
    	}
    	for(String file : files) {
    		listModel.addElement(file);
    	}
    }
    
    /**
     * Changes current project, resets currentProjectLabel and reloads fileList
     * to match a new current project. If <code>projectName</code> equals to
     * already current project no action will be taken.
     * @param projectName a project to make current
     */
    private void changeActiveProject(String projectName) {
    	// current project is null only when there is no project to display
    	if(currentProject == null) return;
    	if(!currentProject.equals(projectName)) {
    		String text = currentProjectLabel.getText().replace(currentProject, projectName);
    		currentProjectLabel.setText(text);
    		currentProject = projectName;
    		setupList();
    	}
	}
    
    /**
     * Closes this dialog and releases all screen resources user by this window.
     */
    private void close() {
    	this.setVisible(false);
    	this.dispose();
    }
    
    /**
     * Creates and positions an owner user by dialog to ensure that dialog
     * is modal and centered over applet.
     * 
     * @return  frame to wrap this dialog with
     */
    private static Frame getAppletFrame(JApplet owner) {
    	return JOptionPane.getFrameForComponent(owner);
	}
    
}