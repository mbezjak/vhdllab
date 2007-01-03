package hr.fer.zemris.vhdllab.applets.texteditor;

 

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

 

 

public class TextEditor extends JPanel implements IEditor, IWizard {

	private static final long serialVersionUID = 5853551043423675268L;
	static JTextPane text;
	private boolean savable;
	private boolean readonly;
	private boolean modifiedFlag = true;
	
	AbstractDocument doc;
	
 
	
	 
 
	static HashMap<Object, Action> actions;
	public JPopupMenu popup;

	// undo helpers
	protected static UndoAction undoAction;
	protected static RedoAction redoAction;
	protected static UndoManager undo = new UndoManager();
	
	private ProjectContainer container;
	private FileContent content;
	
	 
	 
	
 
	
	public void init() {
		
		
		
		
		
		text = new JTextPane();
		text.setCaretPosition(0);
		text.setLocation(25, 50);
		text.setPreferredSize(new Dimension(300,300));
		
		
		
		//create pop up menu

		popup = new JPopupMenu();

		ActionListener menuListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				String command = event.getActionCommand();

				if (command.equals("Cut")) {
					text.cut();
				} else if (command.equals("Copy")) {
					text.copy();
				} else if (command.equals("Paste")) {
					text.paste();
				} else if (command.equals("Select all")) {
					text.selectAll();
				}

			}
		};
		
		
		JMenuItem item;
		TextEditor.undoAction = new UndoAction();
		popup.add(item = new JMenuItem(TextEditor.undoAction));
		 

		TextEditor.redoAction = new RedoAction();
		popup.add(item = new JMenuItem(TextEditor.redoAction));
	 
		
		popup.addSeparator();
		popup.add(item = new JMenuItem("Cut"));
		item.setHorizontalTextPosition(SwingConstants.RIGHT);
		item.addActionListener(menuListener);

		popup.add(item = new JMenuItem("Copy"));
		item.setHorizontalTextPosition(SwingConstants.RIGHT);
		item.addActionListener(menuListener);

		popup.add(item = new JMenuItem("Paste"));
		item.setHorizontalTextPosition(SwingConstants.RIGHT);
		item.addActionListener(menuListener);

		popup.addSeparator();

		popup.add(item = new JMenuItem("Select all"));
		item.setHorizontalTextPosition(SwingConstants.RIGHT);
		item.addActionListener(menuListener);

		popup.setBorder(new BevelBorder(BevelBorder.RAISED));

		text.addMouseListener(new MousePopupListener());

		text.add(popup);
		
		JScrollPane scroll = new JScrollPane(text);
		 
		 
		this.setLayout(new BorderLayout());
		this.add(scroll, BorderLayout.CENTER);
		this.setBackground(Color.RED);
		
		StyledDocument styledDoc =   text.getStyledDocument();
		if (styledDoc instanceof AbstractDocument) {
			doc = (AbstractDocument) styledDoc;
		 
		} else {
			System.err
					.println("Text pane's document isn't an AbstractDocument!");
			System.exit(-1);
		}
		
		
		text.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				if(modifiedFlag) return;
				modifiedFlag = true;
				container.resetEditorTitle(modifiedFlag, content.getProjectName(),content.getFileName());
			}
			public void removeUpdate(DocumentEvent e) {
				if(modifiedFlag) return;
				modifiedFlag = true;
				container.resetEditorTitle(modifiedFlag, content.getProjectName(),content.getFileName());
			}
			public void insertUpdate(DocumentEvent e) {
				if(modifiedFlag) return;
				modifiedFlag = true;
				container.resetEditorTitle(modifiedFlag, content.getProjectName(),content.getFileName());
			}
		});
		
//		
	     
 
		createActionTable(text);

		// Add some key bindings.
		addBindings();

		//initDocument();

		doc.addUndoableEditListener(new MyUndoableEditListener());
		
		
		
	 
	}

	public String getData() {
		modifiedFlag = false;
		return text.getText();
	}

	public IWizard getWizard() {
		return this;
	}

	public boolean isModified() {
		return modifiedFlag;
	}

	public static StyledDocument getDocument() {
		return text.getStyledDocument();
	}
	public static void setDocument(StyledDocument doc) {
		text.setStyledDocument(doc);
		 
	}
	
	public void setFileContent(FileContent fContent) {
		this.content = fContent;
		text.setText(content.getContent());
		modifiedFlag = false;
		container.resetEditorTitle(modifiedFlag, content.getProjectName(),content.getFileName());
	}

	public void setProjectContainer(ProjectContainer pContainer) {
		this.container = pContainer;
	}

	public FileContent getInitialFileContent(Component parent) {
		try {
			String projectName = container.getActiveProject();
			String fileName;
			do {
				fileName = JOptionPane.showInputDialog(parent, "Enter file name:");
			} while(container.existsFile(projectName, fileName));
			
			String data = "new file named '" + fileName + "' that belongs to '" +projectName +"' was created in: " + getCurrentDateAndTime();
			FileContent initialContent = new FileContent(projectName, fileName, data);
			return initialContent;
		} catch (UniformAppletException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getFileName() {
		return content.getFileName();
	}

	public String getProjectName() {
		return content.getProjectName();
	}
	
	private String getCurrentDateAndTime() {
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getDefault());
		return sdf.format(cal.getTime());
	}

	public void highlightLine(int line) {}

	public void setReadOnly(boolean flag) {
		this.readonly = flag;
	}

	public void setSaveable(boolean flag) {
		this.savable = flag;
	}

	public boolean isReadOnly() {
		return readonly;
	}

	public boolean isSavable() {
		return savable;
	}

	public void setSavable(boolean flag) {
		savable = flag;
	}
	
	class MousePopupListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			checkPopup(e);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			checkPopup(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			checkPopup(e);
		}

		private void checkPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(TextEditor.this, e.getX(), e.getY());
			}
		}
	}

	// This one listens for edits that can be undone.
	protected class MyUndoableEditListener implements UndoableEditListener {
		public void undoableEditHappened(UndoableEditEvent e) {
			// Remember the edit and update the menus.
			undo.addEdit(e.getEdit());
			undoAction.updateUndoState();
			redoAction.updateRedoState();
		}
	}

	// Add a couple of emacs key bindings for navigation.
	protected void addBindings() {
		InputMap inputMap = text.getInputMap();

		// Ctrl-b to go backward one character
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK);
		inputMap.put(key, DefaultEditorKit.backwardAction);

		// Ctrl-f to go forward one character
		key = KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK);
		inputMap.put(key, DefaultEditorKit.forwardAction);

		key = KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK);
		inputMap.put(key, DefaultEditorKit.upAction);

		key = KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK);
		inputMap.put(key, DefaultEditorKit.downAction);
		// Ctrl-c to copy to clipboard
		key = KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK);
		inputMap.put(key, DefaultEditorKit.copyAction);
		// Ctrl-c to copy to clipboard
		key = KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK);
		inputMap.put(key, DefaultEditorKit.cutAction);
		// Ctrl-v to copy to clipboard
		key = KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK);
		inputMap.put(key, DefaultEditorKit.pasteAction);
		// Ctrl-z to Undo Action
		key = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
		inputMap.put(key, undoAction);
		// Ctrl-y to Redo Action
		key = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);
		inputMap.put(key, redoAction);
		// TAB for TAB Action
		key = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0);
		inputMap.put(key, DefaultEditorKit.insertTabAction);
	}

	protected void initDocument() {

	}

	// The following two methods allow us to find an
	// action provided by the editor kit by its name.
	private static void createActionTable(JTextComponent textComponent) {
		actions = new HashMap<Object, Action>();
		Action[] actionsArray = textComponent.getActions();
		for (int i = 0; i < actionsArray.length; i++) {
			Action a = actionsArray[i];
			actions.put(a.getValue(Action.NAME), a);
		}
	}

	public static Action getActionByName(String name) {
		return actions.get(name);
	}

	static class UndoAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4320749325632195129L;

		public UndoAction() {
			super("Undo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undo.undo();
			} catch (CannotUndoException ex) {
				System.out.println("Unable to undo: " + ex);
				ex.printStackTrace();
			}
			updateUndoState();
			redoAction.updateRedoState();
		}

		protected void updateUndoState() {
			if (undo.canUndo()) {
				setEnabled(true);
				putValue(Action.NAME, undo.getUndoPresentationName().split(" ")[0]);
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Undo");
			}
		}
	}

	static class RedoAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7146009349531047274L;

		public RedoAction() {
			super("Redo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undo.redo();
			} catch (CannotRedoException ex) {
				System.out.println("Unable to redo: " + ex);
				ex.printStackTrace();
			}
			updateRedoState();
			undoAction.updateUndoState();
		}

		protected void updateRedoState() {
			if (undo.canRedo()) {
				setEnabled(true);
				putValue(Action.NAME, undo.getRedoPresentationName().split(" ")[0]);
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Redo");
			}
		}
	}

	 

	 
	}

 
		
 