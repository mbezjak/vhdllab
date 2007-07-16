package hr.fer.zemris.vhdllab.applets.simulations;


import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



public class WaveApplet extends JPanel implements IEditor, IWizard {

	/** Ovaj container */
	private JPanel cp = this;

	/** Panel koji sadrzi imena signala */
	private SignalNamesPanel signalNames;

	/** Panel koji sadrzi trenutne vrijednosti u ovisnosti o polozaju kursora */
	private SignalValuesPanel signalValues;

	/** Panel na kojem se crtaju valni oblici */
	private WaveDrawBoard waves;

	/** Panel koji sadrzi skalu */
	private Scale scale;

	/**
	 * Vertikalni scrollbar koji pomice panel s imenima signala i panel s valnim
	 * oblicima
	 */
	private JScrollBar verticalScrollbar;

	/** Horizontalni scrollbar pomice panel s valnim oblicima i skalu */
	private JScrollBar horizontalScrollbar;

	/** Scrollbar koji pomice panel s imenima signala */
	private JScrollBar signalNamesScrollbar;

	/** Scrollbar koji pomice panel s trenutnim vrijednostima ovisno o kursoru */
	private JScrollBar signalValuesScrollbar;

	// /** Textfield koji sadrzi tocnu vrijednost na kojoj se nalazi kursor misa
	// */
	// private JTextField textField = new RoundField(10);

	/** Search signal */
	private JTextField search = new RoundField(10);

	/** Vremenska razlika izmedu kursora i mouse kursora */
	private JTextField interval = new RoundField(10);

	/** Panel po kojem se pomice znacka kursora */
	private CursorPanel cursorPanel;

	/** Popup meni koji sadrzi ikone za pozicioniranje na bridove signala */
	private JPopupMenu popup = new JPopupMenu();

	/** Help popup */
	private JPopupMenu popupHelp = new JPopupMenu();

	/** Trenutna vrijednost na dvoklik misa */
	private JPopupMenu showValue = new JPopupMenu();

	/** Trenutna vrijednost ide u ovaj textField */
	/*
	 * Bitno je ovdje ne staviti fiksnu duljinu jer ce paneli gledati tu
	 * duljinu, a ne broj znakova u textFieldu
	 */
	private JTextField currentValue = new JTextField();

	/** Sadrzi rezultate simulacije. */
	private GhdlResults results;

	/** Help panel */
	private HelpPanel helpPanel;

	/** Options popup */
	private JPopupMenu optionsPopup = new JPopupMenu();

	/** Divider koji razdvaja panel s imenima signala i trenutnim vrijednostima */
	private JPanel divider1 = new JPanel();

	/** Divider koji razdvaja panel s trenutnim vrijednostima i valne oblike */
	private JPanel divider2 = new JPanel();

	/** Sve boje koje se koriste */
	private ThemeColor themeColor = new ThemeColor();

	/* ikone */
	private Icon navigate = new ImageIcon(getClass().getResource("navigate.png"));
	private Icon rightUpIcon = new ImageIcon(getClass().getResource("rightUp.PNG"));
	private Icon rightDownIcon = new ImageIcon(getClass().getResource("rightDown.PNG"));
	private Icon leftUpIcon = new ImageIcon(getClass().getResource("leftUp.PNG"));
	private Icon leftDownIcon = new ImageIcon(getClass().getResource("leftDown.PNG"));
	private Icon zoomInTwoIcon = new ImageIcon(getClass().getResource("+2.png"));
	private Icon zoomOutTwoIcon = new ImageIcon(getClass().getResource("-2.png"));
	private Icon zoomInTenIcon = new ImageIcon(getClass().getResource("+10.png"));
	private Icon zoomOutTenIcon = new ImageIcon(getClass().getResource("-10.png"));
	private Icon defaultIcon = new ImageIcon(getClass().getResource("default.png"));
	private Icon upIcon = new ImageIcon(getClass().getResource("up.png"));
	private Icon downIcon = new ImageIcon(getClass().getResource("down.png"));
	private Icon helpIcon = new ImageIcon(getClass().getResource("help.png"));
	private Icon optionsIcon = new ImageIcon(getClass().getResource("options.png"));
	private Icon gotoIcon = new ImageIcon(getClass().getResource("goto.png"));
	private Icon gotoPasiveIcon = new ImageIcon(getClass().getResource("gotoPasive.PNG"));

	/* buttons */
	/** Pokazuje popup za trazenje bridova signala */
	private JButton navigateSignals = new JButton(navigate);

	/** Sljedeci rastuci brid */
	private JButton rightUp = new JButton(rightUpIcon);

	/** Sljedeci padajuci brid */
	private JButton rightDown = new JButton(rightDownIcon);

	/** Prethodni rastuci brid */
	private JButton leftUp = new JButton(leftUpIcon);

	/** Prethodni padajuci brid */
	private JButton leftDown = new JButton(leftDownIcon);

	/* Go to aktivni kursor */
	private JButton gotoButton = new JButton(gotoIcon);

	/* Go to pasivni kursor */
	private JButton gotoPasiveButton = new JButton(gotoPasiveIcon);

	private JButton zoomInTwoButton = new JButton(zoomInTwoIcon);

	private JButton zoomOutTwoButton = new JButton(zoomOutTwoIcon);

	private JButton zoomInTenButton = new JButton(zoomInTenIcon);

	private JButton zoomOutTenButton = new JButton(zoomOutTenIcon);

	/** Vraca defaultni poredak signala */
	private JButton defaultButton = new JButton(defaultIcon);

	private JButton upButton = new JButton(upIcon);

	private JButton downButton = new JButton(downIcon);

	private JButton helpButton = new JButton(helpIcon);

	private JButton optionsButton = new JButton(optionsIcon);

	private JButton okButton = new JButton("Ok");

	private JButton defaultTheme = new JButton("DefaultTheme");

	private JButton secondTheme = new JButton("SecondTheme");

	/* liste */
	private DefaultListModel shapes = new DefaultListModel();

	private JList listShapes = new JList(shapes);

	private DefaultListModel components = new DefaultListModel();

	private JList listComponents = new JList(components);

	/* color chooser */
	private JColorChooser colorChooser = new JColorChooser();

	/* prethodno pritisnuta tipka. Potrebna za kombinaciju dviju tipki */
	private char previousKey = 'A';

	/** SerialVersionUID */
	private static final long serialVersionUID = 2;

	/** FileContent */
	private FileContent fileContent;

	/** flags */
	private boolean readOnly;
	private boolean savableFlag;


	/** ProjectContainer */
	// private ProjectContainer projectContainer;
	public WaveApplet() {
	}


	public void init() {
		// textField.setEditable(false);
		// textField.setToolTipText("Value");
		search.setText("search signal");
		search.addMouseListener(searchClickListener);
		search.addActionListener(searchListener);
		interval.setText("Interval");
		interval.setEditable(false);
		interval.setToolTipText("Time-interval between cursor and mouse cursor");

		/* rezultati prikazni stringom prenose se GhdlResults klasi */
		results = new GhdlResults();

		/* scrollbars */
		signalNamesScrollbar = new JScrollBar(SwingConstants.HORIZONTAL, 0, 0, 0, 0);
		signalNamesScrollbar.addAdjustmentListener(signalNamesScrollListener);
		signalValuesScrollbar = new JScrollBar(SwingConstants.HORIZONTAL, 0, 0, 0, 0);
		signalValuesScrollbar.addAdjustmentListener(signalValuesScrollListener);
		horizontalScrollbar = new JScrollBar(SwingConstants.HORIZONTAL, 0, 0, 0, 0);
		horizontalScrollbar.addAdjustmentListener(horizontalScrollListener);
		/* stvara se skala */
		scale = new Scale(horizontalScrollbar, themeColor);

		/* panel s imenima signala */
		signalNames = new SignalNamesPanel(themeColor, signalNamesScrollbar);
		signalNames.addMouseListener(mouseClickListener);
		signalNames.addMouseWheelListener(wheelListener);

		/* vertikalni scrollbar */
		verticalScrollbar = new JScrollBar(SwingConstants.VERTICAL, 0, 0, 0, 0);
		verticalScrollbar.addAdjustmentListener(verticalScrollListener);

		/* panel s valnim oblicima */
		waves = new WaveDrawBoard(scale, signalNames.getSignalNameSpringHeight(),
				verticalScrollbar, themeColor);
		waves.addMouseMotionListener(mouseListener);
		waves.addMouseListener(mouseWaveListener);
		waves.addMouseWheelListener(wheelListener);

		/* panel s trenutnim vrijednostima ovisno o kursoru */
		signalValues = new SignalValuesPanel(themeColor, signalValuesScrollbar);
		signalValues.addMouseListener(mouseClickListener);
		signalValues.addMouseWheelListener(wheelListener);

		/* panel u kojem klizi kursor */
		cursorPanel = new CursorPanel(scale, waves, themeColor);
		cursorPanel.addMouseMotionListener(mouseListener);
		cursorPanel.addMouseListener(mouseCursorListener);

		/* help panel */
		helpPanel = new HelpPanel();

		/* panel koji razdvaja imena signala i trenutne vrijednosti */
		divider1.setPreferredSize(new Dimension(4, 50));
		divider1.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
		divider1.setBackground(themeColor.getDivider());
		divider1.addMouseMotionListener(firstDividerListener);

		/* panel koji razdvaja trenutne vrijednosti i valne oblike */
		divider2.setPreferredSize(new Dimension(4, 50));
		divider2.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
		divider2.setBackground(themeColor.getDivider());
		divider2.addMouseMotionListener(secondDividerListener);

		/*
		 * Popup prozor koji ce izletjeti na trazenje sljedeceg/prethodnog
		 * padajuceg/rastuceg brid signala.
		 */
		rightUp.addActionListener(navigateListener);
		leftUp.addActionListener(navigateListener);
		rightDown.addActionListener(navigateListener);
		leftDown.addActionListener(navigateListener);
		gotoButton.addActionListener(gotoListener);
		gotoPasiveButton.addActionListener(gotoListener);

		rightUp.setToolTipText("Move to next right positive edge");
		leftUp.setToolTipText("Move to next left positive edge");
		rightDown.setToolTipText("Move to next right negative edge");
		leftDown.setToolTipText("Move to next left negative edge");

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(141, 176, 221));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(rightUp);
		buttonPanel.add(leftUp);
		buttonPanel.add(rightDown);
		buttonPanel.add(leftDown);
		buttonPanel.add(gotoButton);
		buttonPanel.add(gotoPasiveButton);
		popup.add(buttonPanel);
		/* kraj popup prozora */

		/* Popup koji ce izletjeti na dvoklik u panelu s valnim oblicima */
		showValue.setPreferredSize(new Dimension(300, 50));
		currentValue.setEditable(false);
		currentValue.setBackground(themeColor.getSignalNames());
		JPanel valuePanel = new JPanel();
		valuePanel.setLayout(new BorderLayout());
		valuePanel.setBackground(themeColor.getSignalNames());
		valuePanel.add(currentValue, BorderLayout.WEST);
		JScrollPane scrollPane = new JScrollPane(valuePanel);
		showValue.add(scrollPane);
		/* kraj popup prozora */

		/* toolbar */
		JPanel toolbar = new JPanel();
		toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.LINE_AXIS));
		toolbar.setBackground(new Color(141, 176, 221));

		zoomInTwoButton.addActionListener(zoomInTwoListener);
		zoomOutTwoButton.addActionListener(zoomOutTwoListener);
		zoomInTenButton.addActionListener(zoomInTenListener);
		zoomOutTenButton.addActionListener(zoomOutTenListener);
		defaultButton.addActionListener(defaultOrderListener);
		upButton.addActionListener(upListener);
		downButton.addActionListener(downListener);
		navigateSignals.addActionListener(showNavigation);
		helpButton.addActionListener(showHelp);
		optionsButton.addActionListener(showOptions);

		zoomInTwoButton.setToolTipText("Zoom in by two");
		zoomOutTwoButton.setToolTipText("Zoom out by two");
		zoomInTenButton.setToolTipText("Zoom in by ten");
		zoomOutTenButton.setToolTipText("Zoom out by ten");
		defaultButton.setToolTipText("Change to default order");
		upButton.setToolTipText("Move signal up");
		downButton.setToolTipText("Move signal down");
		navigateSignals.setToolTipText("Move to next/previous right/left edge");
		optionsButton.setToolTipText("Change current theme/define custom colors");
		helpButton.setToolTipText("Help");

		toolbar.add(zoomInTwoButton);
		toolbar.add(zoomOutTwoButton);
		// toolbar.add(zoomInTenButton);
		// toolbar.add(zoomOutTenButton);
		toolbar.add(upButton);
		toolbar.add(downButton);
		toolbar.add(defaultButton);
		toolbar.add(navigateSignals);
		toolbar.add(optionsButton);
		toolbar.add(helpButton);
		/* kraj toolbara */

		/* popup help */
		popupHelp.setPreferredSize(new Dimension(500, 600));
		popupHelp.add(helpPanel);

		/* popup options */
		optionsPopup.setPreferredSize(new Dimension(450, 550));
		optionsPopup.setBackground(new Color(238, 238, 238));

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.LINE_AXIS));
		JLabel titleComponents = new JLabel("Change applet components color:");
		JLabel titleShapes = new JLabel("Change Shapes color:");
		labelPanel.add(titleComponents);
		labelPanel.add(Box.createRigidArea(new Dimension(25, 0)));
		labelPanel.add(titleShapes);
		labelPanel.add(Box.createRigidArea(new Dimension(358, 0)));

		JPanel listPanel = new JPanel();
		listPanel.setBackground(new Color(238, 238, 238));
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.LINE_AXIS));

		components.addElement("Signal names background");
		components.addElement("Waveforms background");
		components.addElement("Scale background");
		components.addElement("Cursor background");
		components.addElement("Active cursor");
		components.addElement("Pasive cursor");
		components.addElement("Letters color");
		shapes.addElement("One");
		shapes.addElement("Zero");
		shapes.addElement("Unknown");
		shapes.addElement("High impedance");
		shapes.addElement("U, L, W and H");
		shapes.addElement("Bit-vector");

		listComponents.addListSelectionListener(listListener);
		listShapes.addListSelectionListener(listListener);

		listPanel.add(Box.createRigidArea(new Dimension(7, 0)));
		listPanel.add(listComponents);
		listPanel.add(Box.createRigidArea(new Dimension(60, 0)));
		listPanel.add(listShapes);
		listPanel.add(Box.createRigidArea(new Dimension(440, 0)));

		JPanel okPanel = new JPanel();
		okPanel.setBackground(new Color(238, 238, 238));
		okPanel.setLayout(new BoxLayout(okPanel, BoxLayout.LINE_AXIS));
		okButton.addActionListener(okListener);
		okPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		okPanel.add(okButton);
		okPanel.add(Box.createRigidArea(new Dimension(120, 0)));
		okPanel.add(defaultTheme);
		defaultTheme.addActionListener(themeListener);
		okPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		okPanel.add(secondTheme);
		secondTheme.addActionListener(themeListener);
		okPanel.add(Box.createRigidArea(new Dimension(418, 0)));

		JPanel optionsPanel = new JPanel();
		optionsPanel.setBackground(new Color(238, 238, 238));
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));
		optionsPanel.add(colorChooser);
		optionsPanel.add(labelPanel);
		optionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		optionsPanel.add(listPanel);
		optionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		optionsPanel.add(okPanel);
		optionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		optionsPopup.add(optionsPanel);
		/* kraj popup optionsa */

		/* postavljanje komponenti na applet */

		cp.setFocusable(true);
		cp.addKeyListener(keyListener);
		cp.setLayout(new WaveLayoutManager());
		cp.setBackground(themeColor.getSignalNames());
		cp.add(toolbar, "toolbar");
		// cp.add(textField, "textField");
		cp.add(cursorPanel, "cursorPanel");
		cp.add(search, "search");
		cp.add(interval, "interval");
		cp.add(signalNames, "signalNames");
		cp.add(divider1, "divider1");
		cp.add(divider2, "divider2");
		cp.add(signalValues, "signalValues");
		cp.add(waves, "waves");
		cp.add(scale, "scale");
		cp.add(verticalScrollbar, "verticalScrollbar");
		cp.add(horizontalScrollbar, "horizontalScrollbar");
		cp.add(signalNamesScrollbar, "signalNamesScrollbar");
		cp.add(signalValuesScrollbar, "valuesScrollbar");
	}


	/**
	 * Metoda postavlja sadrzaj
	 */
	public void setFileContent(FileContent content) {
		this.fileContent = content;
		/* uzima String preko HTTP-a i predaje ga GHDL parseru */
		results.parseString(content.getContent());
		scale.setContent(results);
		signalNames.setContent(results);
		waves.setContent(results);
		signalValues.setContent(results);
		cursorPanel.setContent();
		helpPanel.setContent(waves.getShapes());
	}

	/**
	 * Listener za vertikalni scrollbar koji pomice panel s valnim oblicima i
	 * panel s imenima signala
	 */
	private AdjustmentListener verticalScrollListener = new AdjustmentListener() {
		/*
		 * Postavlja odgovarajuci offset na temelju trenutne vrijednosti
		 * scrollbara i ponovno crta obje komponente
		 */
		public void adjustmentValueChanged(AdjustmentEvent event) {
			waves.setVerticalOffset(verticalScrollbar.getValue());
			signalNames.setVerticalOffset(verticalScrollbar.getValue());
			signalValues.setVerticalOffset(verticalScrollbar.getValue());
			waves.repaint();
			signalNames.repaint();
			signalValues.repaint();
		}
	};

	/**
	 * Slusa kotacic misa i pomice vertikalni scrollbar
	 */
	private MouseWheelListener wheelListener = new MouseWheelListener() {
		public void mouseWheelMoved(MouseWheelEvent event) {
			int value = event.getWheelRotation() * 24;
			verticalScrollbar.setValue(verticalScrollbar.getValue() + value);
		}
	};

	/**
	 * Listener horizontalnog scrollbara koji scrolla panel s valnim oblicima i
	 * panel sa skalom
	 */
	private AdjustmentListener horizontalScrollListener = new AdjustmentListener() {
		/*
		 * Postavlja odgovarajuci offset na temelju trenutne vrijednosti
		 * scrollbara i ponovno crta obje komponente
		 */
		public void adjustmentValueChanged(AdjustmentEvent event) {
			waves.setHorizontalOffset(horizontalScrollbar.getValue());
			scale.setHorizontalOffset(horizontalScrollbar.getValue());
			cursorPanel.setOffset(horizontalScrollbar.getValue());
			waves.repaint();
			scale.repaint();
			cursorPanel.repaint();
		}
	};

	/**
	 * Listener horizontalnog scrollbara koji scrolla panel s valnim oblicima i
	 * panel sa skalom
	 */
	private AdjustmentListener signalNamesScrollListener = new AdjustmentListener() {
		/**
		 * Postavlja odgovarajuci offset na temelju trenutne vrijednosti
		 * scrollbara i ponovno crta obje komponente
		 */
		public void adjustmentValueChanged(AdjustmentEvent event) {
			signalNames.setHorizontalOffset(signalNamesScrollbar.getValue());
			signalNames.repaint();
		}
	};

	/**
	 * Listener horizontalnog scrollbara koji scrolla panel s trenutnim
	 * vrijednostim ovisno o polozaju kursora
	 */
	private AdjustmentListener signalValuesScrollListener = new AdjustmentListener() {
		/**
		 * Postavlja odgovarajuci offset na temelju trenutne vrijednosti
		 * scrollbara i ponovno crta obje komponente
		 */
		public void adjustmentValueChanged(AdjustmentEvent event) {
			signalValues.setHorizontalOffset(signalValuesScrollbar.getValue());
			signalValues.repaint();
		}
	};

	/**
	 * Listener buttona koji pokrece popup prozor
	 */
	private ActionListener showNavigation = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			popup.show(cp, 350, 55);

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Otvara help popup
	 */
	private ActionListener showHelp = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			popupHelp.show(cp, 200, 55);

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Otvara options popup
	 */
	private ActionListener showOptions = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			optionsPopup.show(cp, 420, 55);

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Lista u option panelu
	 */
	private ListSelectionListener listListener = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent event) {
			if (event.getSource().equals(listComponents)) {
				listShapes.clearSelection();
			} else {
				listComponents.clearSelection();
			}
			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Promjena boje u options panelu
	 */
	private ActionListener okListener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			if (listComponents.isSelectionEmpty() && listShapes.isSelectionEmpty()) {
				return;
			}

			/* postavi index na custom boje */
			themeColor.setThemeIndex(0);
			int index;

			/* ako je selektirana lista s komponentama */
			if (!listComponents.isSelectionEmpty()) {
				index = listComponents.getSelectedIndex();
				switch (index) {
				case 0 :
					themeColor.setSignalNames(colorChooser.getColor());
					signalNames.repaint();
					break;
				case 1 :
					themeColor.setWaves(colorChooser.getColor());
					waves.repaint();
					break;
				case 2 :
					themeColor.setScale(colorChooser.getColor());
					scale.repaint();
					break;
				case 3 :
					themeColor.setCursorPanel(colorChooser.getColor());
					cursorPanel.repaint();
					break;
				case 4 :
					themeColor.setActiveCursor(colorChooser.getColor());
					waves.repaint();
					cursorPanel.repaint();
					break;
				case 5 :
					themeColor.setPasiveCursor(colorChooser.getColor());
					waves.repaint();
					cursorPanel.repaint();
					break;
				case 6 :
					themeColor.setLetters(colorChooser.getColor());
					signalNames.repaint();
					waves.repaint();
					scale.repaint();
					cursorPanel.repaint();
					break;
				}
			} else {
				index = listShapes.getSelectedIndex();
				switch (index) {
				case 0 :
					waves.getShapes()[3].setColor(colorChooser.getColor());
					waves.getShapes()[4].setColor(colorChooser.getColor());
					waves.getShapes()[5].setColor(colorChooser.getColor());
					break;
				case 1 :
					waves.getShapes()[0].setColor(colorChooser.getColor());
					waves.getShapes()[1].setColor(colorChooser.getColor());
					waves.getShapes()[2].setColor(colorChooser.getColor());
					break;
				case 2 :
					waves.getShapes()[12].setColor(colorChooser.getColor());
					break;
				case 3 :
					waves.getShapes()[9].setColor(colorChooser.getColor());
					waves.getShapes()[10].setColor(colorChooser.getColor());
					waves.getShapes()[11].setColor(colorChooser.getColor());
					break;
				case 4 :
					waves.getShapes()[13].setColor(colorChooser.getColor());
					waves.getShapes()[14].setColor(colorChooser.getColor());
					waves.getShapes()[15].setColor(colorChooser.getColor());
					break;
				case 5 :
					waves.getShapes()[6].setColor(colorChooser.getColor());
					waves.getShapes()[7].setColor(colorChooser.getColor());
					waves.getShapes()[8].setColor(colorChooser.getColor());
					break;
				}
				waves.repaint();
			}

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Theme button listener
	 */
	private ActionListener themeListener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource().equals(defaultTheme)) {
				themeColor.setThemeIndex(1);
			} else {
				themeColor.setThemeIndex(2);
			}
			signalNames.repaint();
			waves.repaint();
			scale.repaint();
			signalValues.repaint();
			cursorPanel.repaint();
			cp.setBackground(themeColor.getSignalNames());

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Povecava skalu i vrijednost valnih oblika za 10
	 */
	ActionListener zoomInTenListener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			/* ako je veci od 214748364 prekoracio bi max int */
			if (scale.getScaleEndPointInPixels() >= 214748364) {
				return;
			}
			/* postavlja nove vrijednosti i automatski podesava sve parametre */
			scale.setDurationsInPixelsAfterZoom(10d);
			int offset = horizontalScrollbar.getValue();

			/* scrollbar ostaje na istom mjestu */
			horizontalScrollbar.setValue(offset * 10);
			cursorPanel
					.setFirstCursorStartPoint(cursorPanel.getFirstCursorStartPoint() * 10);
			cursorPanel
					.setSecondCursorStartPoint(cursorPanel.getSecondCursorStartPoint() * 10);
			waves.setFirstCursorStartPoint(waves.getFirstCursorStartPoint() * 10);
			waves.setSecondCursorStartPoint(waves.getSecondCursorStartPoint() * 10);
			cursorPanel.repaint();
			scale.repaint();
			waves.repaint();

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Povecava skalu i vrijednost valnih oblika za 2
	 */
	ActionListener zoomInTwoListener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			/** Ako je veci od 1073741824, prekoracio bi max int */
			if (scale.getScaleEndPointInPixels() >= 1073741824) {
				return;
			}
			/* postavlja nove vrijednosti i automatski podesava sve parametre */
			scale.setDurationsInPixelsAfterZoom(2d);
			int offset = horizontalScrollbar.getValue();

			/* scrollbar ostaje na istom mjestu */
			horizontalScrollbar.setValue(offset * 2);
			cursorPanel
					.setFirstCursorStartPoint(cursorPanel.getFirstCursorStartPoint() * 2);
			cursorPanel
					.setSecondCursorStartPoint(cursorPanel.getSecondCursorStartPoint() * 2);
			waves.setFirstCursorStartPoint(waves.getFirstCursorStartPoint() * 2);
			waves.setSecondCursorStartPoint(waves.getSecondCursorStartPoint() * 2);
			cursorPanel.repaint();
			scale.repaint();
			waves.repaint();

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Smanjuje skalu i vrijednost valnih oblika za 10
	 */
	ActionListener zoomOutTenListener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			/* postavlja nove vrijednosti i automatski podesava sve parametre */
			scale.setDurationsInPixelsAfterZoom(0.1d);
			int offset = horizontalScrollbar.getValue();

			/* scrollbar ostaje na istom mjestu */
			horizontalScrollbar.setValue(offset / 10);
			cursorPanel
					.setFirstCursorStartPoint(cursorPanel.getFirstCursorStartPoint() / 10);
			cursorPanel
					.setSecondCursorStartPoint(cursorPanel.getSecondCursorStartPoint() / 10);
			waves.setFirstCursorStartPoint(waves.getFirstCursorStartPoint() / 10);
			waves.setSecondCursorStartPoint(waves.getSecondCursorStartPoint() / 10);
			cursorPanel.repaint();
			scale.repaint();
			waves.repaint();

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Smanjuje skalu i vrijednost valnih oblika za 2
	 */
	ActionListener zoomOutTwoListener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			/* postavlja nove vrijednosti i automatski podesava sve parametre */
			scale.setDurationsInPixelsAfterZoom(0.5d);
			int offset = horizontalScrollbar.getValue();

			/* scrollbar ostaje na istom mjestu */
			horizontalScrollbar.setValue(offset / 2);
			cursorPanel
					.setFirstCursorStartPoint(cursorPanel.getFirstCursorStartPoint() / 2);
			cursorPanel
					.setSecondCursorStartPoint(cursorPanel.getSecondCursorStartPoint() / 2);
			waves.setFirstCursorStartPoint(waves.getFirstCursorStartPoint() / 2);
			waves.setSecondCursorStartPoint(waves.getSecondCursorStartPoint() / 2);
			cursorPanel.repaint();
			scale.repaint();
			waves.repaint();

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Osluskuje 'up' button
	 */
	private ActionListener upListener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			int index = signalNames.getIndex();
			boolean isClicked = signalNames.getIsClicked();

			/* ako niti jedan signal nije selektiran */
			if (!isClicked) {
				return;
			}

			/* promijeni poredak signala prema gore */
			index = results.changeSignalOrderUp(index);

			signalNames.setIndex(index);
			waves.setIndex(index);
			signalValues.setIndex(index);

			/* repainta panel s imenima signala i panel s valnim oblicima */
			signalNames.repaint();
			waves.repaint();
			signalValues.repaint();

			if (index * 45 <= signalNames.getVerticalOffset()) {
				verticalScrollbar.setValue(verticalScrollbar.getValue() - 200);
			}

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Slusa 'down' button
	 */
	private ActionListener downListener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			int index = signalNames.getIndex();
			boolean isClicked = signalNames.getIsClicked();

			/* ako niti jedan signal nije selektiran */
			if (!isClicked) {
				return;
			}

			/* promijeni poredak signala prema dolje */
			index = results.changeSignalOrderDown(index);

			signalNames.setIndex(index);
			waves.setIndex(index);
			signalValues.setIndex(index);

			/* repainta panel s imenima signala i panel s valnim oblicima */
			signalNames.repaint();
			waves.repaint();
			signalValues.repaint();

			if ((index + 1) * 45 + 50 >= signalNames.getHeight()
					+ signalNames.getVerticalOffset()) {
				verticalScrollbar.setValue(verticalScrollbar.getValue() + 200);
			}

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * slusa 'default' button u popup meniju
	 */
	private ActionListener defaultOrderListener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			/* promijeni natrag na defaultni poredak */
			results.setDefaultOrder();
			results.setDefaultExpandedSignalNames();

			/* postavlja novi objekt imena signala i njihovih vrijednosti */
			signalNames.setSignalNames(results.getSignalNames());
			waves.setSignalValues(results.getSignalValues());

			signalNames.setIndex(0);
			waves.setIndex(0);
			signalValues.setIndex(0);

			/* repainta panel s imenima signala i panel s valnim oblicima */
			signalNames.repaint();
			waves.repaint();
			signalValues.repaint();

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Mouse listener koji osluskuje pokrete misa i svaki pokret registrira te
	 * na temelju vrijednosti po X-osi i na temelju trenutnog stanja skale vraca
	 * preciznu vrijednost
	 */
	private MouseMotionListener mouseListener = new MouseMotionListener() {
		/**
		 * Metoda koja upravlja eventom
		 */
		public void mouseMoved(MouseEvent event) {
			// /* trenutni offset + X-vrijednost kurosra misa */
			// int xValue = event.getX() + horizontalScrollbar.getValue();
			// /* podijeljeno s 100 jer je scaleStep za 100 piksela */
			// double value = xValue * scale.getScaleStepInTime() / 100;
			// // textField.setText((Math.round(value * 100000d) / 100000d) +
			// scale.getMeasureUnitName());
			//
			// /* racuna interval izmedu kursora i trenutne pozicije misa */
			// double measuredTime = Math.abs(cursorPanel.getSecondValue() -
			// cursorPanel.getFirstValue());
			// interval.setText((Math.round(measuredTime * 100000d) / 100000d) +
			// scale.getMeasureUnitName());
			//
			// signalNames.repaint();
		}


		/**
		 * Metoda koja upravlja kursorom
		 */
		public void mouseDragged(MouseEvent event) {
			if (cursorPanel.getActiveCursor() == 1) {
				waves
						.setFirstCursorStartPoint(event.getX()
								+ waves.getHorizontalOffset());
				cursorPanel.setFirstCursorStartPoint(event.getX()
						+ waves.getHorizontalOffset());
			} else {
				waves.setSecondCursorStartPoint(event.getX()
						+ waves.getHorizontalOffset());
				cursorPanel.setSecondCursorStartPoint(event.getX()
						+ waves.getHorizontalOffset());
			}

			int rightBorder = horizontalScrollbar.getValue() + waves.getPanelWidth();
			int leftBorder = horizontalScrollbar.getValue();
			if (event.getX() + waves.getHorizontalOffset() + 20 >= rightBorder) {
				horizontalScrollbar.setValue(horizontalScrollbar.getValue() + 20);
				if (cursorPanel.getActiveCursor() == 1) {
					cursorPanel.setFirstCursorStartPoint(rightBorder - 20);
					waves.setFirstCursorStartPoint(rightBorder - 20);
				} else {
					cursorPanel.setSecondCursorStartPoint(rightBorder - 20);
					waves.setSecondCursorStartPoint(rightBorder - 20);
				}
			} else if (event.getX() + waves.getHorizontalOffset() < leftBorder
					&& waves.getHorizontalOffset() != 0) {
				horizontalScrollbar.setValue(horizontalScrollbar.getValue() - 20);
				if (cursorPanel.getActiveCursor() == 1) {
					cursorPanel.setFirstCursorStartPoint(leftBorder + 20);
					waves.setFirstCursorStartPoint(leftBorder + 20);
				} else {
					cursorPanel.setSecondCursorStartPoint(leftBorder + 20);
					waves.setSecondCursorStartPoint(leftBorder + 20);
				}
			}

			/* trenutni offset + X-vrijednost kurosra misa */
			int xValue;
			if (cursorPanel.getActiveCursor() == 1) {
				xValue = cursorPanel.getFirstCursorStartPoint();
			} else {
				xValue = cursorPanel.getSecondCursorStartPoint();
			}
			/* podijeljeno s 100 jer je scaleStep za 100 piksela */
			double value = xValue * scale.getScaleStepInTime() / 100;
			// textField.setText((Math.round(value * 100000d) / 100000d) +
			// scale.getMeasureUnitName());

			if (value <= 0) {
				value = 0;
			}
			if (cursorPanel.getActiveCursor() == 1) {
				cursorPanel.setFirstString((Math.round(value * 100000d) / 100000d)
						+ scale.getMeasureUnitName());
				cursorPanel.setFirstValue((Math.round(value * 100000d) / 100000d));
			} else {
				cursorPanel.setSecondString((Math.round(value * 100000d) / 100000d)
						+ scale.getMeasureUnitName());
				cursorPanel.setSecondValue((Math.round(value * 100000d) / 100000d));
			}
			double measuredTime = Math.abs(cursorPanel.getSecondValue()
					- cursorPanel.getFirstValue());
			interval.setText((Math.round(measuredTime * 100000d) / 100000d)
					+ scale.getMeasureUnitName());

			/* trazi trenutni polozaj po tockama promjene */
			int index = 0;
			int transitionPoint = scale.getDurationInPixels()[0];
			if (cursorPanel.getActiveCursor() == 1) {
				while (cursorPanel.getFirstCursorStartPoint() >= transitionPoint) {
					if (index == results.getSignalValues().get(0).length - 1) {
						break;
					}
					index++;
					transitionPoint += scale.getDurationInPixels()[index];
				}
			} else {
				while (cursorPanel.getSecondCursorStartPoint() >= transitionPoint) {
					index++;
					transitionPoint += scale.getDurationInPixels()[index];
				}
			}

			signalValues.setValueIndex(index);

			waves.repaint();
			cursorPanel.repaint();
			signalValues.repaint();
		}
	};

	/**
	 * Mouse listener koji pomice kursor na sljedeci/prethodni padajuci/rastuci
	 * brid
	 */
	private ActionListener navigateListener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			if (signalNames.getIsClicked()) {
				String presentValue;
				String previousValue;
				String nextValue;
				int index = 0;
				int valueIndex = 0;
				int transitionPoint = scale.getDurationInPixels()[0];

				boolean isFound = false;
				if (cursorPanel.getActiveCursor() == 1) {
					if (cursorPanel.getFirstCursorStartPoint() < transitionPoint) {
						index = 0;
					} else {
						while (cursorPanel.getFirstCursorStartPoint() >= transitionPoint) {
							index++;
							if (index >= scale.getDurationInPixels().length) {
								return;
							}
							transitionPoint += scale.getDurationInPixels()[index];
						}
					}
				} else {
					if (cursorPanel.getSecondCursorStartPoint() < transitionPoint) {
						index = 0;
						signalValues.setValueIndex(index);
					} else {
						while (cursorPanel.getSecondCursorStartPoint() >= transitionPoint) {
							index++;
							transitionPoint += scale.getDurationInPixels()[index];
							signalValues.setValueIndex(index);
						}
					}
				}

				/* ako se trazi prethodni rastuci */
				if (event.getSource().equals(leftUp)) {

					for (; index >= 1;) {
						presentValue = results.getSignalValues().get(
								signalNames.getIndex())[index];
						previousValue = results.getSignalValues().get(
								signalNames.getIndex())[--index];
						if (presentValue.equals("1") && previousValue.equals("0")) {
							isFound = true;
							valueIndex = index;
							break;
						}
					}
				}
				/* ako se trazi prethodni padajuci */
				else if (event.getSource().equals(leftDown)) {
					for (; index >= 1;) {
						presentValue = results.getSignalValues().get(
								signalNames.getIndex())[index];
						previousValue = results.getSignalValues().get(
								signalNames.getIndex())[--index];
						if (presentValue.equals("0") && previousValue.equals("1")) {
							isFound = true;
							valueIndex = index;
							break;
						}
					}
				}
				/* ako se trazi sljedeci rastuci */
				else if (event.getSource().equals(rightUp)) {
					for (; index < results.getSignalValues().get(0).length - 1; index++) {
						presentValue = results.getSignalValues().get(
								signalNames.getIndex())[index];
						nextValue = results.getSignalValues().get(signalNames.getIndex())[index + 1];
						if (presentValue.equals("0") && nextValue.equals("1")) {
							isFound = true;
							valueIndex = index;
							signalValues.setValueIndex(index);
							break;
						}
					}
				}
				/* ako se trazi sljedeci padajuci */
				else if (event.getSource().equals(rightDown)) {
					for (; index < results.getSignalValues().get(0).length - 1; index++) {
						presentValue = results.getSignalValues().get(
								signalNames.getIndex())[index];
						nextValue = results.getSignalValues().get(signalNames.getIndex())[index + 1];
						if (presentValue.equals("1") && nextValue.equals("0")) {
							isFound = true;
							valueIndex = index;
							break;
						}
					}
				}
				if (isFound) {
					double value;
					transitionPoint = 0;
					for (; index >= 0; index--) {
						transitionPoint += scale.getDurationInPixels()[index];
					}
					if (cursorPanel.getActiveCursor() == 1) {
						cursorPanel.setFirstCursorIndex(valueIndex);
						cursorPanel.setFirstCursorStartPoint(transitionPoint);
						waves.setFirstCursorStartPoint(transitionPoint);
						value = transitionPoint * scale.getScaleStepInTime() / 100;
						cursorPanel
								.setFirstString((Math.round(value * 100000d) / 100000d)
										+ scale.getMeasureUnitName());
						cursorPanel
								.setFirstValue((Math.round(value * 100000d) / 100000d));
					} else {
						cursorPanel.setSecondCursorIndex(valueIndex);
						cursorPanel.setSecondCursorStartPoint(transitionPoint);
						waves.setSecondCursorStartPoint(transitionPoint);
						value = transitionPoint * scale.getScaleStepInTime() / 100;
						cursorPanel
								.setSecondString((Math.round(value * 100000d) / 100000d)
										+ scale.getMeasureUnitName());
						cursorPanel
								.setSecondValue((Math.round(value * 100000d) / 100000d));
					}
				}
			}
			cursorPanel.repaint();
			waves.repaint();
			signalValues.repaint();

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Mouse listener koji scrolla na aktivni/pasivni kursor
	 */
	private ActionListener gotoListener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			// idi na aktivan
			if (event.getSource().equals(gotoButton)) {
				if (cursorPanel.getActiveCursor() == 1) {
					horizontalScrollbar
							.setValue(cursorPanel.getFirstCursorStartPoint() - 100);
					signalValues.setValueIndex(cursorPanel.getFirstCursorIndex());
				} else {
					horizontalScrollbar
							.setValue(cursorPanel.getSecondCursorStartPoint() - 100);
					signalValues.setValueIndex(cursorPanel.getSecondCursorIndex());
				}
			} else {
				if (cursorPanel.getActiveCursor() == 1) {
					horizontalScrollbar
							.setValue(cursorPanel.getSecondCursorStartPoint() - 100);
					signalValues.setValueIndex(cursorPanel.getSecondCursorIndex());
				} else {
					horizontalScrollbar
							.setValue(cursorPanel.getFirstCursorStartPoint() - 100);
					signalValues.setValueIndex(cursorPanel.getFirstCursorIndex());
				}
			}
			cursorPanel.repaint();
			waves.repaint();
			signalValues.repaint();

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Mouse listener koji osluskuje klik misa iznad panela s kursorima i na
	 * temelju podrucja klika mijenja aktivni kursor
	 */
	private MouseListener mouseCursorListener = new MouseListener() {

		public void mousePressed(MouseEvent event) {
			;
		}


		public void mouseReleased(MouseEvent event) {
			;
		}


		public void mouseEntered(MouseEvent event) {
			;
		}


		public void mouseExited(MouseEvent event) {
			;
		}


		public void mouseClicked(MouseEvent event) {
			double value = event.getX() + horizontalScrollbar.getValue();
			/* provjerava je li kliknut cursor */
			if (value >= cursorPanel.getFirstCursorStartPoint() - 5
					&& value <= cursorPanel.getFirstCursorStartPoint() + 5) {
				/* postavi prvi kursor aktivnim */
				cursorPanel.setActiveCursor((byte)1);
				waves.setActiveCursor((byte)1);
			} else if (value >= cursorPanel.getSecondCursorStartPoint() - 5
					&& value <= cursorPanel.getSecondCursorStartPoint() + 5) {
				/* postavi drugi kursor aktivnim */
				cursorPanel.setActiveCursor((byte)2);
				waves.setActiveCursor((byte)2);
			}

			cursorPanel.repaint();
			waves.repaint();

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Mouse listener koji pomice divider1 i mijenja sirinu panela s imenima
	 * signala
	 */
	private MouseMotionListener firstDividerListener = new MouseMotionListener() {
		/**
		 * Metoda koja upravlja eventom
		 */
		public void mouseMoved(MouseEvent event) {
			;
		}


		/**
		 * Mijenja sirinu panela s imenima signala
		 */
		public void mouseDragged(MouseEvent event) {
			if (signalNames.getPanelWidth() <= 5 && event.getX() < 0) {
				return;
			}

			if (signalNames.getPanelWidth() + event.getX() <= 2
					|| signalNames.getPanelWidth() + event.getX() >= 650) {
				return;
			}
			signalNames.setPanelWidth(event.getX() + signalNames.getPanelWidth());
			signalNames.repaint();
			cp.doLayout();
		}
	};

	/**
	 * Mouse listener koji pomice divider2 i mijenja sirinu panela s trenutnim
	 * vrijednostima
	 */
	private MouseMotionListener secondDividerListener = new MouseMotionListener() {
		/**
		 * Metoda koja upravlja eventom
		 */
		public void mouseMoved(MouseEvent event) {
			;
		}


		/**
		 * Mijenja sirinu panela s imenima signala
		 */
		public void mouseDragged(MouseEvent event) {
			if (signalValues.getPanelWidth() <= 5 && event.getX() < 0) {
				return;
			}

			if (signalValues.getPanelWidth() + event.getX() <= 2
					|| signalValues.getPanelWidth() + event.getX() >= 650) {
				return;
			}
			signalValues.setPanelWidth(event.getX() + signalValues.getPanelWidth());
			signalValues.repaint();
			cp.doLayout();
		}
	};

	/**
	 * Mouse listener koji osluskuje klik misa iznad panela s imenima signala i
	 * panela s trenutnim vrijednostima te na temelju trenutne vrijednosti po
	 * X-osi mijenja background iznad trenutno oznacenog signala
	 */
	private MouseListener mouseClickListener = new MouseListener() {

		public void mousePressed(MouseEvent event) {
			;
		}


		public void mouseReleased(MouseEvent event) {
			;
		}


		public void mouseEntered(MouseEvent event) {
			;
		}


		public void mouseExited(MouseEvent event) {
			;
		}


		public void mouseClicked(MouseEvent event) {
			int mouseButton = event.getButton();
			int value = event.getY() + verticalScrollbar.getValue();
			int index = 0;
			if (mouseButton == 1) {
				/* pronalazi se index signala kojeg treba oznaciti */
				while (value % 45 == 0) {
					value -= 1;
				}
				index = value / 45;

				/* postavlja se vrijednost suprotna od one koja je do sada bila */
				if (waves.getIndex() == index && waves.getIsClicked() == true) {
					signalNames.setIsClicked(false);
					waves.setIsClicked(false);
					signalValues.setIsClicked(false);
				} else {
					signalNames.setIsClicked(true);
					signalNames.setIndex(index);
					waves.setIsClicked(true);
					waves.setIndex(index);
					signalValues.setIsClicked(true);
					signalValues.setIndex(index);
				}

				Integer defaultVectorIndex = results.getCurrentVectorIndex().get(index);
				/* provjerava je li kliknut plusic na bit-vektoru */
				if (defaultVectorIndex != -1
						&& results.getCurrentVectorIndex().indexOf(defaultVectorIndex) == index
						&& (event.getX() >= 0 && event.getX() <= 15)) {
					if (!results.getExpandedSignalNames().get(defaultVectorIndex)) {
						results.getExpandedSignalNames().set(defaultVectorIndex, true);
						signalNames.expand(index);
						waves.expand(index);
					} else {
						results.getExpandedSignalNames().set(defaultVectorIndex, false);
						signalNames.collapse(index);
						waves.collapse(index);
					}
				}
			}

			signalNames.repaint();
			waves.repaint();
			cursorPanel.repaint();
			signalValues.repaint();

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Mouse listener koji osluskuje klik misa iznad panela s valnim oblicima te
	 * na temelju trenutne vrijednosti po X-osi mijenja background iznad
	 * trenutno oznacenog signala
	 */
	private MouseListener mouseWaveListener = new MouseListener() {

		public void mousePressed(MouseEvent event) {
			;
		}


		public void mouseReleased(MouseEvent event) {
			;
		}


		public void mouseEntered(MouseEvent event) {
			;
		}


		public void mouseExited(MouseEvent event) {
			;
		}


		public void mouseClicked(MouseEvent event) {
			int mouseButton = event.getButton();

			/*
			 * Ako je kliknuta desna tipka misa, ili srednja tipka misa,
			 * trenutni pasivni kursor ce se pomaknuti tocno na mjesto na kojem
			 * smo kliknuli misem i ostat ce pasivan
			 */
			if ((mouseButton == 2 || mouseButton == 3) && event.getClickCount() == 1) {
				int xValue = event.getX() + horizontalScrollbar.getValue();
				double timeValue = xValue * scale.getScaleStepInTime() / 100;
				if (cursorPanel.getActiveCursor() == 1) {
					cursorPanel.setSecondCursorStartPoint(xValue);
					waves.setSecondCursorStartPoint(xValue);
					cursorPanel
							.setSecondString((Math.round(timeValue * 100000d) / 100000d)
									+ scale.getMeasureUnitName());
					cursorPanel
							.setSecondValue((Math.round(timeValue * 100000d) / 100000d));
				} else {
					cursorPanel.setFirstCursorStartPoint(xValue);
					waves.setFirstCursorStartPoint(xValue);
					cursorPanel
							.setFirstString((Math.round(timeValue * 100000d) / 100000d)
									+ scale.getMeasureUnitName());
					cursorPanel
							.setFirstValue((Math.round(timeValue * 100000d) / 100000d));
				}
				double measuredTime = Math.abs(cursorPanel.getSecondValue()
						- cursorPanel.getFirstValue());
				interval.setText((Math.round(measuredTime * 100000d) / 100000d)
						+ scale.getMeasureUnitName());
			} else if (event.getClickCount() == 2) {
				int value = event.getY() + verticalScrollbar.getValue();
				int index = 0;
				/* pronalazi se index signala kojeg treba oznaciti */
				while (value % 45 == 0) {
					value -= 1;
				}
				index = value / 45;
				if (index >= results.getSignalValues().size()) {
					return;
				}

				int xValue = event.getX() + horizontalScrollbar.getValue();
				int valueIndex = 0;
				int transitionPoint = scale.getDurationInPixels()[0];
				while (xValue >= transitionPoint) {
					if (valueIndex == results.getSignalValues().get(0).length - 1) {
						break;
					}
					valueIndex++;
					transitionPoint += scale.getDurationInPixels()[valueIndex];
				}
				showValue.show(cp, 610, 47);
				currentValue.setText(results.getSignalValues().get(index)[valueIndex]);
			}

			signalNames.repaint();
			waves.repaint();
			cursorPanel.repaint();
			signalValues.repaint();

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Listener koji provjerava je li sto upisano u search
	 */
	private ActionListener searchListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String input = search.getText();
			boolean isFound = false;
			int index = 0;

			/* pretrazivanje imena signala */
			for (int i = 0; i < results.getSignalNames().size(); i++) {
				if (results.getSignalNames().get(i).toLowerCase().equals(
						input.toLowerCase())) {
					isFound = true;
					index = i;
					break;
				}
			}

			/* ako je nasao */
			if (isFound) {
				signalNames.setIsClicked(true);
				signalNames.setIndex(index);
				waves.setIsClicked(true);
				waves.setIndex(index);
				verticalScrollbar.setValue(index * 45);
			} else {
				search.setText("Not found");
			}
			signalNames.repaint();
			waves.repaint();

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};

	/**
	 * Listener koji resetira search polje kada se klikne na njega
	 */
	private MouseListener searchClickListener = new MouseListener() {
		public void mousePressed(MouseEvent event) {
			;
		}


		public void mouseReleased(MouseEvent event) {
			;
		}


		public void mouseEntered(MouseEvent event) {
			;
		}


		public void mouseExited(MouseEvent event) {
			;
		}


		public void mouseClicked(MouseEvent event) {
			search.setText("");
		}
	};

	/**
	 * Key listener
	 */
	private KeyListener keyListener = new KeyListener() {
		public void keyTyped(KeyEvent event) {
			char key = event.getKeyChar();
			switch (key) {
			case '+' :
				zoomInTwoButton.doClick();
				break;
			case '-' :
				zoomOutTwoButton.doClick();
				break;
			case 's' :
				verticalScrollbar.setValue(verticalScrollbar.getValue() + 5);
				break;
			case 'w' :
				verticalScrollbar.setValue(verticalScrollbar.getValue() - 5);
				break;
			case 'd' :
				horizontalScrollbar.setValue(horizontalScrollbar.getValue() + 5);
				break;
			case 'a' :
				horizontalScrollbar.setValue(horizontalScrollbar.getValue() - 5);
				break;
			case 'b' :
				defaultButton.doClick();
				break;
			case 'k' :
				if (previousKey == 'l') {
					rightUp.doClick();
				} else if (previousKey == 'h') {
					leftUp.doClick();
				}
				break;
			case 'j' :
				if (previousKey == 'l') {
					rightDown.doClick();
				} else if (previousKey == 'h') {
					leftDown.doClick();
				}
				break;
			case '(' :
				zoomInTenButton.doClick();
				break;
			case ')' :
				zoomOutTenButton.doClick();
				break;
            case 'f' :
                scale.fitToWindow();
                scale.repaint();
                waves.repaint();
                cursorPanel.repaint();
                break;
                case 'u' :
                scale.unfitToWindow();
                scale.repaint();
                waves.repaint();
                cursorPanel.repaint();
                break;
			}
			previousKey = key;
		}


		public void keyPressed(KeyEvent event) {
			int key = event.getKeyCode();
			switch (key) {
			case KeyEvent.VK_UP :
				verticalScrollbar.setValue(verticalScrollbar.getValue() - 5);
				break;
			case KeyEvent.VK_DOWN :
				verticalScrollbar.setValue(verticalScrollbar.getValue() + 5);
				break;
			case KeyEvent.VK_RIGHT :
				horizontalScrollbar.setValue(horizontalScrollbar.getValue() + 5);
				break;
			case KeyEvent.VK_LEFT :
				horizontalScrollbar.setValue(horizontalScrollbar.getValue() - 5);
				break;
			case KeyEvent.VK_HOME :
				verticalScrollbar.setValue(0);
				break;
			case KeyEvent.VK_END :
				verticalScrollbar.setValue(waves.getPreferredSize().height);
				break;
			case KeyEvent.VK_PAGE_UP :
				verticalScrollbar.setValue(verticalScrollbar.getValue()
						- signalNames.getPanelHeight());
				break;
			case KeyEvent.VK_PAGE_DOWN :
				verticalScrollbar.setValue(verticalScrollbar.getValue()
						+ signalNames.getPanelHeight());
				break;
			}
		}


		public void keyReleased(KeyEvent event) {
			;
		}
	};


	public String getData() {
		return null;
	}


	public String getFileName() {
		return fileContent.getFileName();
	}


	public String getProjectName() {
		return fileContent.getProjectName();
	}


	public IWizard getWizard() {
		return null;
	}


	public void highlightLine(int line) {
		;
	}


	public boolean isModified() {
		return false;
	}


	public boolean isReadOnly() {
		return this.readOnly;
	}


	public boolean isSavable() {
		return this.savableFlag;
	}


	public void setProjectContainer(ProjectContainer container) {
		// this.projectContainer = container;
	}


	public void setReadOnly(boolean flag) {
		this.readOnly = flag;
	}


	public void setSavable(boolean flag) {
		this.savableFlag = flag;
	}


	public FileContent getInitialFileContent(Component parent, String projectName) {
		return null;
	}


	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
