package hr.fer.zemris.vhdllab.applets.main.dialogs;

import hr.fer.zemris.vhdllab.applets.main.interfaces.FileIdentifier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A save dialog that displays to a user files that should be save.
 * He may chose to save some of displayed files to save. To use this
 * dialog just use one constructor, modify text to buttons (optional),
 * add some files to be saved and just invoke method {@link #startDialog()}.
 * After that controls are locked and youre code will stop at that method.
 * When user clicks on a button (OK, CANCEL or CLOSE) controls will become
 * unlocked and youre code will resume just after {@link #startDialog()}
 * method. To know what button user clicked use {@link #getOption()}
 * method. It is also a good idea to check if user selected a <code>always
 * save resources</code> checkbox. And finaly to get what files user selected
 * use {@link #getSelectedResources()} method.
 * <p>Here is an example of how to user this dialog:</p>
 * <code>
 * <pre>
 * SaveDialog dialog = new SaveDialog(this, "A title", true);
 * dialog.setCancelButtonText("Wait a minute, I forgot something");
 * dialog.setText("Select Resources to save:");
 * dialog.addItem(true, "A Project", "A file");
 * dialog.addItem(true, "A Project", "Another file");
 * dialog.addItem(true, "Another Project", "A third file");
 * dialog.startDialog();
 * if(dialog.getOption() == SaveDialog.OK_OPTION) {
 * 	boolean always = dialog.shouldAlwaysSaveResources();
 * 	if(always) {
 * 		...
 * 	}
 * 	List<FileIdentifier> identifiers = dialog.getSelectedResources();
 * 	...
 * }
 * ...
 * </pre>
 * </code>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 23.12.2006
 */
public class SaveDialog extends JDialog {
	
	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = -6506589125240104983L;
	/** Size of a border */
	private static final int BORDER = 10;
	/** Width of this dialog */
	private static final int DIALOG_WIDTH = 350;
	/** Height of this dialog */
	private static final int DIALOG_HEIGHT = 450;
	/** Height of a label */
	private static final int LABEL_HEIGHT = 50;
	/** Width of all buttons */
	private static final int BUTTON_WIDTH = 100;
	/** Height of all buttons */
	private static final int BUTTON_HEIGHT = 24;
	
	/** Label to be displayed above a list of checkboxes */
	private JLabel label;
	/** A list of checkboxes displayed to user */
	private CheckBoxList list;
	/** Select All button used to select all checkboxes at once */
	private JButton selectAll;
	/** Deselect All button used to deselect all checkboxes at once */
	private JButton deselectAll;
	/** OK button to end this dialog with {@link #OK_OPTION} */
	private JButton ok;
	/** Cancel button to end this dialog with {@link #CANCEL_OPTION} */
	private JButton cancel;
	/**
	 * Checkbox to check if user does not want this dialog to appear anymore
	 * (auto save with no user interaction)
	 */
	private JCheckBox alwaysSave;
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
     * Creates a non-modal dialog without a title with the
     * specified <code>Frame</code> as its owner.  If <code>owner</code>
     * is <code>null</code>, a shared, hidden frame will be set as the
     * owner of the dialog.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param owner the <code>Frame</code> from which the dialog is displayed
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public SaveDialog(Frame owner) throws HeadlessException {
        this(owner, false);
        saveDialogImpl(owner);
    }

    /**
     * Creates a modal or non-modal dialog without a title and
     * with the specified owner <code>Frame</code>.  If <code>owner</code>
     * is <code>null</code>, a shared, hidden frame will be set as the
     * owner of the dialog.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     *
     * @param owner the <code>Frame</code> from which the dialog is displayed
     * @param modal  true for a modal dialog, false for one that allows
     *               others windows to be active at the same time
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public SaveDialog(Frame owner, boolean modal) throws HeadlessException {
        this(owner, null, modal);
        saveDialogImpl(owner);
    }

    /**
     * Creates a non-modal dialog with the specified title and
     * with the specified owner frame.  If <code>owner</code>
     * is <code>null</code>, a shared, hidden frame will be set as the
     * owner of the dialog.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     *
     * @param owner the <code>Frame</code> from which the dialog is displayed
     * @param title  the <code>String</code> to display in the dialog's
     *			title bar
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public SaveDialog(Frame owner, String title) throws HeadlessException {
        this(owner, title, false);
        saveDialogImpl(owner);
    }

    /**
     * Creates a modal or non-modal dialog with the specified title 
     * and the specified owner <code>Frame</code>.  If <code>owner</code>
     * is <code>null</code>, a shared, hidden frame will be set as the
     * owner of this dialog.  All constructors defer to this one.
     * <p>
     * NOTE: Any popup components (<code>JComboBox</code>,
     * <code>JPopupMenu</code>, <code>JMenuBar</code>)
     * created within a modal dialog will be forced to be lightweight.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     *
     * @param owner the <code>Frame</code> from which the dialog is displayed
     * @param title  the <code>String</code> to display in the dialog's
     *			title bar
     * @param modal  true for a modal dialog, false for one that allows
     *               other windows to be active at the same time
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public SaveDialog(Frame owner, String title, boolean modal)
        throws HeadlessException {
        super(owner, title, modal);
        saveDialogImpl(owner);
    }

     /**
     * Creates a non-modal dialog without a title with the
     * specified <code>Dialog</code> as its owner.
     * <p>
     * This constructor sets the component's locale property to the value 
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param owner the non-null <code>Dialog</code> from which the dialog is displayed
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public SaveDialog(Dialog owner) throws HeadlessException {
        this(owner, false);
        saveDialogImpl(owner);
    }

    /**
     * Creates a modal or non-modal dialog without a title and
     * with the specified owner dialog.
     * <p>
     * This constructor sets the component's locale property to the value 
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param owner the non-null <code>Dialog</code> from which the dialog is displayed
     * @param modal  true for a modal dialog, false for one that allows
     *               other windows to be active at the same time
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public SaveDialog(Dialog owner, boolean modal) throws HeadlessException {
        this(owner, null, modal);
        saveDialogImpl(owner);
    }

    /**
     * Creates a non-modal dialog with the specified title and
     * with the specified owner dialog.
     * <p>
     * This constructor sets the component's locale property to the value 
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param owner the non-null <code>Dialog</code> from which the dialog is displayed
     * @param title  the <code>String</code> to display in the dialog's
     *			title bar
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public SaveDialog(Dialog owner, String title) throws HeadlessException {
        this(owner, title, false);
        saveDialogImpl(owner);
    }

    /**
     * Creates a modal or non-modal dialog with the specified title 
     * and the specified owner frame. 
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     *
     * @param owner the non-null <code>Dialog</code> from which the dialog is displayed
     * @param title  the <code>String</code> to display in the dialog's
     *			title bar
     * @param modal  true for a modal dialog, false for one that allows
     *               other windows to be active at the same time
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public SaveDialog(Dialog owner, String title, boolean modal)
        throws HeadlessException {
        super(owner, title, modal);
        saveDialogImpl(owner);
    }
	
    /**
     * Implemetation of a constructor of <code>SaveDialog</code>.
     */
    private void saveDialogImpl(Component owner) {
    	// setup label
    	label = new JLabel();
    	JPanel labelPanel = new JPanel(new BorderLayout());
    	labelPanel.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, 0, BORDER));
    	labelPanel.add(label, BorderLayout.CENTER);
    	int width = DIALOG_WIDTH - 2 * BORDER;
    	int height = LABEL_HEIGHT - 2 * BORDER;
    	label.setPreferredSize(new Dimension(width, height));
    	
    	// setup check box list
    	list = new CheckBoxList();
    	JPanel tablePanel = new JPanel(new BorderLayout());
    	tablePanel.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));
    	tablePanel.add(list, BorderLayout.CENTER);
    	width = DIALOG_WIDTH - 2 * BORDER;
    	height = 0; // because list is a center component and it doesnt need height
    	list.setPreferredSize(new Dimension(width, height));
    	
    	// setup select all and deselect all buttons
    	selectAll = new JButton("Select All");
    	selectAll.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
    	selectAll.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			list.setSelectionToAll(true);
    		}
    	});
    	
    	deselectAll = new JButton("Deselect All");
    	deselectAll.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
    	deselectAll.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			list.setSelectionToAll(false);
    		}
    	});
    	
    	Box selectBox = Box.createHorizontalBox();
    	selectBox.add(selectAll);
    	selectBox.add(Box.createRigidArea(new Dimension(BORDER, BUTTON_HEIGHT)));
    	selectBox.add(deselectAll);
    	selectBox.setBorder(BorderFactory.createEmptyBorder(0, 0, BORDER, 0));
    	
    	// setup ok and cancel buttons
		ok = new JButton("OK");
    	ok.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
    	ok.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			option = SaveDialog.OK_OPTION;
    			close();
    		}
    	});
    	
    	cancel = new JButton("Cancel");
    	cancel.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
    	cancel.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			option = SaveDialog.CANCEL_OPTION;
    			close();
    		}
    	});
    	
    	Box actionBox = Box.createHorizontalBox();
    	actionBox.add(ok);
    	actionBox.add(Box.createRigidArea(new Dimension(BORDER, BUTTON_HEIGHT)));
    	actionBox.add(cancel);
    	actionBox.setBorder(BorderFactory.createEmptyBorder(BORDER, 0, BORDER, 0));
    	
    	JPanel selectPanel = new JPanel(new BorderLayout());
    	selectPanel.add(selectBox, BorderLayout.EAST);
    	JPanel actionPanel = new JPanel(new BorderLayout());
    	actionPanel.add(actionBox, BorderLayout.EAST);
    	
    	alwaysSave = new JCheckBox("Always save resources");
    	alwaysSave.setSelected(false);
    	JPanel alwaysSavePanel = new JPanel(new BorderLayout());
    	alwaysSavePanel.add(alwaysSave, BorderLayout.WEST);
    	
    	
    	JPanel lowerPanel = new JPanel(new BorderLayout());
    	lowerPanel.add(selectPanel, BorderLayout.NORTH);
    	lowerPanel.add(alwaysSavePanel, BorderLayout.CENTER);
    	lowerPanel.add(actionPanel, BorderLayout.SOUTH);
    	
    	
    	this.setLayout(new BorderLayout());
    	JPanel messagePanel = new JPanel(new BorderLayout());
    	messagePanel.add(labelPanel, BorderLayout.NORTH);
    	messagePanel.add(tablePanel, BorderLayout.CENTER);
    	messagePanel.add(lowerPanel, BorderLayout.SOUTH);
    	this.getContentPane().add(messagePanel, BorderLayout.CENTER);
    	this.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
    	setText("Select Resources to save:");
    	pack();
    	this.setLocationRelativeTo(owner);
    }
    
    /**
     * Starts a dialog and locks the control.
     */
    public void startDialog() {
    	if(list.isEmpty()) {
    		throw new IllegalStateException("There are no items to save.");
    	}
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
     * Returns FileIdentifiers that were selected by user to be saved.
     * This method will return <code>null</code> if user did not click
     * on a OK button.
     * @return FileIdentifiers to save
     */
    public List<FileIdentifier> getSelectedResources() {
    	if(getOption() != SaveDialog.OK_OPTION) {
    		return null;
    	}
    	
    	List<FileIdentifier> identifiers = new ArrayList<FileIdentifier>();
    	for(SaveItem item : list.getItems()) {
    		if(item.isSelected()) {
	    		String projectName = item.getProjectName();
	    		String fileName = item.getFileName();
	    		FileIdentifier file = new FileIdentifier(projectName, fileName);
	    		identifiers.add(file);
    		}
    	}
    	return identifiers;
    }
    
    /**
     * Return <code>true</code> if user clicked on a checkbox to always save
     * resources.
     * @return <code>true</code> if user clicked on a checkbox to always save
     * 		resources; <code>false</code> otherwise
     */
    public boolean shouldAlwaysSaveResources() {
    	return alwaysSave.isSelected();
    }
    
    /**
     * Add item to list so that it can be selected in a checkbox.
     * @param seleted indicating if checkbox should be selected or not
     * @param projectName a name of a project
     * @param fileName a name of a file
     * @throws NullPointerException if <code>fileName</code> is <code>null</code>
     */
    public void addItem(boolean seleted, String projectName, String fileName) {
    	if(projectName == null || fileName == null) {
    		throw new NullPointerException("Project name or File name can not be null.");
    	}
    	SaveItem item = new SaveItem(seleted, projectName, fileName);
    	list.addItem(item);
    }
    
    /**
     * Set text to be displayed in a label just above a list.
     * If <code>text</code> is <code>null</code> then label will
     * be set to an empty string.
     * @param text text to be displayed
     */
    public void setText(String text) {
    	if(text == null) text = "";
    	label.setText(text);
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
     * Set text to be displayed in an <code>selectAll</code> button.
     * If <code>text</code> is <code>null</code> then button will be set
     * to an empty string.
     * @param text text to be displayed
     */
    public void setSelectAllButtonText(String text) {
    	if(text == null) text = "";
    	selectAll.setText(text);
    }
    
    /**
     * Set text to be displayed in an <code>deselectAll</code> button.
     * If <code>text</code> is <code>null</code> then button will be set
     * to an empty string.
     * @param text text to be displayed
     */
    public void setDeselectAllButtonText(String text) {
    	if(text == null) text = "";
    	deselectAll.setText(text);
    }
    
    /**
     * Set text to be displayed in a <code>always save resources</code> check
     * box. If <code>text</code> is <code>null</code> then check box will be
     * set to an empty string.
     * @param text text to be displayed
     */
    public void setAlwaysSaveCheckBoxText(String text) {
    	if(text == null) text = "";
    	alwaysSave.setText(text);
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
     * Closes this dialog and releases all screen resources user by this window.
     */
    private void close() {
    	this.setVisible(false);
    	this.dispose();
    }
    
    /**
     * Represents an item that should be displayed in SaveDialog. 
     * 
     * @author Miro Bezjak
     * @see SaveDialog
     */
    private class SaveItem {
    	
    	/** Name of a project displayed next to file name in checkbox */
    	private String projectName;
    	/** Name of a file displayed in checkbox */
    	private String fileName;
    	/** Indicating if checkbox is selected */
    	private boolean selected;
    	
    	/**
    	 * Constructor.
    	 * @param selected whether checkbox should be selected or not
    	 * @param projectName a name of a project
    	 * @param fileName a name of a file
    	 */
    	public SaveItem(boolean selected, String projectName, String fileName) {
    		this.selected = selected;
    		this.projectName = projectName;
    		this.fileName = fileName;
		}

		/**
		 * Getter for selected;
		 * @return value of selected
		 */
    	public boolean isSelected() {
			return selected;
		}
		/**
		 * Setter for selected.
		 * @param selected value to be set
		 */
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		
		/**
		 * Getter for file name.
		 * @return file name
		 */
		public String getFileName() {
			return fileName;
		}

		/**
		 * Getter for project name.
		 * @return project name
		 */
		public String getProjectName() {
			return projectName;
		}

		/**
		 * Creates text out of project name and file name.
		 * @return created text
		 */
		public String getText() {
			return fileName + " [" + projectName + "]";
		}
    	
    }

    /**
     * Class that displays a list of checkboxes.
     * 
     * @author Miro Bezjak
     * @see SaveDialog
     */
    private class CheckBoxList extends JPanel {
    	
    	/**
		 * Serial Version UID.
		 */
		private static final long serialVersionUID = 4499815884621898659L;
		/** Items that are displayed to user. */
		private Map<JCheckBox, SaveItem> items;
		/** Container of checkboxes. */
    	private Box box;
    	
    	/**
    	 * Constructor.
    	 */
    	public CheckBoxList() {
    		items = new LinkedHashMap<JCheckBox, SaveItem>();
    		box = Box.createVerticalBox();
    		JScrollPane scroll = new JScrollPane(box);
    		scroll.getViewport().setBackground(Color.WHITE);
			this.setLayout(new BorderLayout());
			this.add(scroll, BorderLayout.CENTER);
		}
    	
    	/**
    	 * Add item to be displayed.
    	 * @param item item to display
    	 */
    	public void addItem(SaveItem item) {
    		JCheckBox checkBox = new JCheckBox(item.getText(), item.isSelected());
    		checkBox.setBackground(Color.WHITE);
    		box.add(checkBox);
    		items.put(checkBox, item);
    	}
    	
    	/**
    	 * Returns <code>true</code> if there are no displayed items.
    	 * @return <code>true</code> if there are no displayed items;
    	 * 		<code>false</code> otherwise.
    	 */
    	public boolean isEmpty() {
    		return items.isEmpty();
    	}
    	
    	/**
    	 * Returns all displayed items.
    	 * @return all displayed items
    	 */
    	public List<SaveItem> getItems() {
    		List<SaveItem> list = new ArrayList<SaveItem>();
    		for(Entry<JCheckBox, SaveItem> entry : items.entrySet()) {
    			JCheckBox checkbox = entry.getKey();
    			SaveItem item = entry.getValue();
    			item.setSelected(checkbox.isSelected());
    			list.add(item);
    		}
    		return list;
    	}
    	
    	/**
    	 * Sets a selection to all checkboxes to a value of <code>selected</code>.
    	 * @param selected <code>true</code> if checkboxes should be selected;
    	 * 			<code>false</code> otherwise
    	 */
    	public void setSelectionToAll(boolean selected) {
    		for(JCheckBox c : items.keySet()) {
    			c.setSelected(selected);
    		}
    	}
    	
    }
}