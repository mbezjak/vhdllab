package hr.fer.zemris.vhdllab.applets.texteditor;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.api.vhdl.Type;
import hr.fer.zemris.vhdllab.applets.editor.automat.entityTable.EntityTable;
import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.client.core.log.MessageType;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class TextEditor extends AbstractEditor implements IWizard, Runnable {

	private static final long serialVersionUID = 1L;
	JTextPane text;
	private Object highlighted;

	AbstractDocument doc;

	static HashMap<Object, Action> actions;
	public JPopupMenu popup;

	// undo helpers
	protected static UndoAction undoAction;
	protected static RedoAction redoAction;
	protected static UndoManager undo = new UndoManager();

	public void initGUI() {

		text = new JTextPane();
		text.setCaretPosition(0);
		text.setLocation(25, 50);
		// text.setPreferredSize(new Dimension(300,300));

		text.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				if (highlighted != null) {
					text.getHighlighter().removeHighlight(highlighted);
					highlighted = null;
				}
			}
		});

		// create pop up menu

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

		StyledDocument styledDoc = text.getStyledDocument();
		if (styledDoc instanceof AbstractDocument) {
			doc = (AbstractDocument) styledDoc;

		} else {
			System.err
					.println("Text pane's document isn't an AbstractDocument!");
			System.exit(-1);
		}

		text.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				setModified(true);
			}

			public void removeUpdate(DocumentEvent e) {
				setModified(true);
			}

			public void insertUpdate(DocumentEvent e) {
				setModified(true);
			}
		});

		//		

		createActionTable(text);

		// Add some key bindings.
		addBindings();

		// initDocument();

		doc.addUndoableEditListener(new MyUndoableEditListener());
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				text.requestFocusInWindow();
			}
		});
	}

	@Override
    public String getData() {
		return text.getText();
	}

	public IWizard getWizard() {
		return this;
	}

	public StyledDocument getDocument() {
		return text.getStyledDocument();
	}

	public void setDocument(StyledDocument doc) {
		text.setStyledDocument(doc);

	}
	
	@Override
	protected void doInitWithoutData() {
	}

	@Override
	protected void doDispose() {
	}
	
	@Override
	protected void doInitWithData(FileInfo f) {
	    text.setText(f.getData());
	}

	@Override
	public void undo() {
		super.undo();
		if (undo.canUndo()) {
			try {
				undo.undo();
			} catch (CannotUndoException ex) {
				System.out.println("Unable to undo: " + ex);
				ex.printStackTrace();
			}
			undoAction.updateUndoState();
			redoAction.updateRedoState();
		}
	}
	
	@Override
	public void redo() {
		super.redo();
		if (undo.canRedo()) {
			try {
				undo.redo();
			} catch (CannotUndoException ex) {
				System.out.println("Unable to redo: " + ex);
				ex.printStackTrace();
			}
			undoAction.updateUndoState();
			redoAction.updateRedoState();
		}
	}

	public FileContent getInitialFileContent(Component parent,
	        Caseless projectName) {
		String[] options = new String[] { "OK", "Cancel" };
		int optionType = JOptionPane.OK_CANCEL_OPTION;
		int messageType = JOptionPane.PLAIN_MESSAGE;
		EntityTable table = new EntityTable();
		table.setProjectContainer(container.getSystemContainer());
		table.init();
		int option = JOptionPane.showOptionDialog(parent, table,
				"New VHDL source", optionType, messageType, null, options,
				options[0]);
		if (option == JOptionPane.OK_OPTION) {
			if (projectName == null)
				return null;
			CircuitInterface ci = table.getCircuitInterface();
			try {
				if (container.getSystemContainer().getResourceManager().existsFile(projectName,
						new Caseless(ci.getName()))) {
					SystemLog.instance().addSystemMessage(
							ci.getName() + " already exists!",
							MessageType.INFORMATION);
				}
			} catch (UniformAppletException e) {
				e.printStackTrace();
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				JOptionPane.showMessageDialog(parent, sw.toString());
				return null;
			}
			StringBuilder sb = new StringBuilder(
					100 + ci.getPorts().size() * 20);
			sb.append("library IEEE;\nuse IEEE.STD_LOGIC_1164.ALL;\n\n");
			sb.append("-- note: entity name and resource name must match\n");
			sb.append("ENTITY ").append(ci.getName()).append(
					" IS PORT (\n");
			for (Port p : ci.getPorts()) {
				Type type = p.getType();
				sb.append("\t").append(p.getName()).append(" : ").append(
						p.getDirection().toString()).append(" ").append(
						type.getTypeName());
				if (type.getRange().isVector()) {
					sb.append(" (").append(type.getRange().getFrom()).append(" ")
							.append(type.getRange().getDirection()).append(" ")
							.append(type.getRange().getTo()).append(")");
				}
				sb.append(";\n");
			}
			if (ci.getPorts().size() == 0) {
				sb.replace(sb.length() - 8, sb.length(), "\n");
			} else {
				sb.replace(sb.length() - 2, sb.length() - 1, ");");
			}
			sb.append("end ").append(ci.getName()).append(";\n\n");
			sb.append("ARCHITECTURE arch OF ").append(ci.getName())
					.append(" IS \n\nBEGIN\n\nEND arch;");

			return new FileContent(projectName, new Caseless(ci.getName()), sb
					.toString());
		} else
			return null;
	}

	@Override
	public void highlightLine(int line) {
		int caret = text.getCaretPosition();
		Highlighter h = text.getHighlighter();
		h.removeAllHighlights();
		String content = text.getText();
		// content = content.replaceAll("\r+", "");
		// text.setText(content);
		text.setCaretPosition(caret);
		int pos = 0;
		line--;
		while (line != 0) {
			pos = content.indexOf('\n', pos) + 1;
			line--;
		}
		int last = content.indexOf('\n', pos) + 1;
		if (last == 0) {
			last = content.length();
		}
		try {
			highlighted = h.addHighlight(pos, last,
					new DefaultHighlighter.DefaultHighlightPainter(new Color(
							180, 210, 238)));
		} catch (BadLocationException e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			JOptionPane.showMessageDialog(this, sw.toString());
		}
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
		key = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
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
				putValue(Action.NAME,
						undo.getUndoPresentationName().split(" ")[0]);
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
				putValue(Action.NAME,
						undo.getRedoPresentationName().split(" ")[0]);
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Redo");
			}
		}
	}

	InitScanner scanner = new InitScanner();

	public void run() {
		initGUI();

		Thread thread = new Thread(scanner);
		thread.start();
	}

	@Override
	public void dispose() {
		scanner.stopScanner();
		super.dispose();
	}

	@Override
	public void init() {
		super.init();
		this.run();
	}

}

class InitScanner implements Runnable {
	EditorScanner scanner = new EditorScanner();

	public void stopScanner() {
		scanner.stopScanner();
	}

	public void run() {

		// scanner.run();
	}

}
