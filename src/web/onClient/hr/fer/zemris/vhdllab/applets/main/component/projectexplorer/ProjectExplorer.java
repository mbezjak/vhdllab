package hr.fer.zemris.vhdllab.applets.main.component.projectexplorer;


import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;



/**
 * Klasa predstavlja 'project explorer'. Project explorer sadrzi popis
 * projekata, te hijerarhiju datoteka koje se koriste u projektu.
 * 
 * @author Boris Ozegovic
 */
public class ProjectExplorer extends JPanel implements IProjectExplorer {

	private Container cp = this;
	private static final String X_USES_Y = "xusesY";
	private static final String X_USED_IN_Y = "xusedInY";
	private static final String FLAT = "flat";

	/** Popup za prikaz menija */
	private JPopupMenu optionsPopup = new JPopupMenu();

	/** x uses y root node */
	private DefaultMutableTreeNode top;

	/** x used in y root node */
	private DefaultMutableTreeNode topInverse;

	/** flat root node */
	private DefaultMutableTreeNode topFlat;

	/** Model kojeg koristi x uses y JTree */
	private DefaultTreeModel treeModel;

	/** Model kojeg koristi x uses y JTree */
	private DefaultTreeModel treeModelNormal;

	/** Model kojeg koristi x used in y JTree */
	private DefaultTreeModel treeModelInverse;

	/** Model kojeg koristi flat JTree */
	private DefaultTreeModel treeModelFlat;

	/** JTree */
	private JTree tree;
	private JTree treeNormal;
	private JTree treeInverse;
	private JTree treeFlat;
	private JScrollPane treeView;
	private JScrollPane treeViewInverse;
	private JScrollPane treeViewFlat;

	private JMenu newSubMenu;
	private JMenuItem addProject;
	private JMenuItem addVHDL;
	private JMenuItem addTb;
	private JMenuItem addSchema;
	private JMenuItem addAutomat;
	private JMenuItem compile;
	private JMenuItem simulate;
	private JMenuItem setActive;
	private JMenuItem viewVHDL;
	private JMenuItem deleteFile;
	private JMenuItem deleteProject;
	private JMenuItem xusesY;
	private JMenuItem xusedInY;
	private JMenuItem flat;
	private JMenu hierarchySubMenu;
	private String currentHierarchy = X_USES_Y;
	private JToolBar toolbar;
	private JPanel toolbarPanel;

	/** Aktivni projekt */
	private String projectName;

	/** Aktivna hijerarhija */
	private Hierarchy hierarchy;

	/** ProjectContainer */
	private ProjectContainer projectContainer;
	private VHDLCellRenderer renderer;

	private TreePath workingTreePath;
	private boolean isNowExpanded;
	private boolean isNowCollapse;

	/** This collection contains paths to collapse nodes */
	private Collection<TreePath> expandedNodes;

	private static final long serialVersionUID = 4932799790563214089L;

	// pitaj miru zakaj se projectContainer.getAllProject blesira i o cemu ona
	// ovisi?!
	// do tada interna kolekcija projekata
	private List<String> allProjects;

	/* mapa datoteka po projektu. */
	private Map<String, List<String>> filesByProjects;

	private Icon normal = new ImageIcon(getClass().getResource("normal.png"));
	private Icon inverse = new ImageIcon(getClass().getResource("inverse.png"));
	private Icon flatIcon = new ImageIcon(getClass().getResource("flat.png"));
	private JButton normalButton = new JButton(normal);
	private JButton inverseButton = new JButton(inverse);
	private JButton flatButton = new JButton(flatIcon);


	/**
	 * Default Constructor
	 */
	public ProjectExplorer() {
		// create set of expanded treepaths
		expandedNodes = new HashSet<TreePath>();
		allProjects = new ArrayList<String>();
		filesByProjects = new HashMap<String, List<String>>();

		newSubMenu = new JMenu("New");
		addProject = new JMenuItem("Add project");
		addVHDL = new JMenuItem("VHDL source");
		addTb = new JMenuItem("Testbench");
		addSchema = new JMenuItem("Schema");
		addAutomat = new JMenuItem("Automat");
		compile = new JMenuItem("Compile");
		simulate = new JMenuItem("Simulate");
		// setActive = new JMenuItem("Set active project");
		viewVHDL = new JMenuItem("View VHDL");
		deleteFile = new JMenuItem("Delete file");
		deleteProject = new JMenuItem("Delete project");
		hierarchySubMenu = new JMenu("Hierarchies");
		xusesY = new JMenuItem("X uses Y");
		xusedInY = new JMenuItem("X is-used-by Y");
		flat = new JMenuItem("Flat");

		/* popup meni */
		addProject.addActionListener(popupListener);
		addVHDL.addActionListener(popupListener);
		addTb.addActionListener(popupListener);
		addSchema.addActionListener(popupListener);
		addAutomat.addActionListener(popupListener);
		compile.addActionListener(popupListener);
		simulate.addActionListener(popupListener);
		// setActive.addActionListener(popupListener);
		viewVHDL.addActionListener(popupListener);
		deleteFile.addActionListener(popupListener);
		deleteProject.addActionListener(popupListener);
		xusesY.addActionListener(popupListener);
		xusedInY.addActionListener(popupListener);
		flat.addActionListener(popupListener);

		newSubMenu.add(addProject);
		newSubMenu.addSeparator();
		newSubMenu.add(addVHDL);
		newSubMenu.add(addTb);
		newSubMenu.add(addSchema);
		newSubMenu.add(addAutomat);
		optionsPopup.add(newSubMenu);
		optionsPopup.addSeparator();
		optionsPopup.add(compile);
		optionsPopup.add(simulate);
		optionsPopup.addSeparator();
		// optionsPopup.add(setActive);
		optionsPopup.add(viewVHDL);
		optionsPopup.addSeparator();
		optionsPopup.add(deleteFile);
		optionsPopup.add(deleteProject);
		optionsPopup.addSeparator();
		optionsPopup.add(hierarchySubMenu);
		hierarchySubMenu.add(xusesY);
		hierarchySubMenu.add(xusedInY);
		hierarchySubMenu.add(flat);
		/* kraj inicijalizacije popupa menija */

		/* filanje stabla */
		top = new DefaultMutableTreeNode("Vhdllab");
		treeModelNormal = new DefaultTreeModel(top);
		/* inicijalizacija JTree komponente */
		treeNormal = new JTree(treeModelNormal);
		// tree.setPreferredSize(new Dimension(500, 600));
		treeNormal.addMouseListener(mouseListener);
		treeNormal.addMouseListener(treeMouse);
		treeNormal.addTreeExpansionListener(treeExpansionListener);
		treeNormal.setEditable(false);
		treeNormal.setRootVisible(false);
		treeNormal.expandRow(0);
		treeNormal.setToggleClickCount(8);
		treeNormal.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		treeNormal.setShowsRootHandles(true);
		treeView = new JScrollPane(treeNormal);
		renderer = new VHDLCellRenderer();
		treeNormal.setCellRenderer(renderer);
		ToolTipManager.sharedInstance().registerComponent(treeNormal);

		// u startu je prvo stablo normalno stablo
		tree = treeNormal;
		/* kraj inicijalizacije tree komponente */

		/* inicijalizacija inverznog stabla JTree komponente */
		topInverse = new DefaultMutableTreeNode("Vhdllab");
		treeModelInverse = new DefaultTreeModel(topInverse);
		treeInverse = new JTree(treeModelInverse);
		treeInverse.addMouseListener(mouseListener);
		treeInverse.addMouseListener(treeMouse);
		treeInverse.addTreeExpansionListener(treeExpansionListener);
		treeInverse.setEditable(false);
		treeInverse.setRootVisible(false);
		treeInverse.expandRow(0);
		treeInverse.setToggleClickCount(8);
		treeInverse.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		treeInverse.setShowsRootHandles(true);
		treeViewInverse = new JScrollPane(treeInverse);
		renderer = new VHDLCellRenderer();
		treeInverse.setCellRenderer(renderer);
		ToolTipManager.sharedInstance().registerComponent(treeInverse);

		/* inicijalizacija flat stabla */
		topFlat = new DefaultMutableTreeNode("Vhdllab");
		treeModelFlat = new DefaultTreeModel(topFlat);
		treeFlat = new JTree(treeModelFlat);
		treeFlat.addMouseListener(mouseListener);
		treeFlat.addMouseListener(treeMouse);
		treeFlat.addTreeExpansionListener(treeExpansionListener);
		treeFlat.setEditable(false);
		treeFlat.setRootVisible(false);
		treeFlat.expandRow(0);
		treeFlat.setToggleClickCount(8);
		treeFlat.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		treeFlat.setShowsRootHandles(true);
		treeViewFlat = new JScrollPane(treeFlat);
		renderer = new VHDLCellRenderer();
		treeFlat.setCellRenderer(renderer);
		ToolTipManager.sharedInstance().registerComponent(treeFlat);

		cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
		// cp.setLayout(new FlowLayout());
		toolbarPanel = new JPanel();
		toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.LINE_AXIS));
		toolbarPanel.setPreferredSize(new Dimension(100, 35));
		toolbarPanel.setMaximumSize(new Dimension(400, 60));
		toolbar = new JToolBar();
		toolbar.setPreferredSize(new Dimension(100, 30));
		toolbar.setFloatable(false);
		toolbar.add(normalButton);
		toolbar.add(inverseButton);
		toolbar.add(flatButton);
		toolbarPanel.add(toolbar);
		normalButton.addActionListener(buttonListener);
		normalButton.setToolTipText("X uses Y hierarchy");
		inverseButton.addActionListener(buttonListener);
		inverseButton.setToolTipText("X used in Y hierarchy");
		flatButton.addActionListener(buttonListener);
		flatButton.setToolTipText("Flat hierarchy");
		cp.add(toolbarPanel);
		cp.add(treeView);
		treeModel = treeModelNormal;
	}


	/**
	 * Inner class
	 * 
	 * Custom TreeCellRenderer koji se koristi pri renderiranju cvorova JTree
	 * komponente u project exploreru
	 * 
	 * @author Boris Ozegovic
	 */
	class VHDLCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 3562380292516384882L;
		private String type;
		private Icon vhdl = new ImageIcon(getClass().getResource("vhdl.png"));
		private Icon tb = new ImageIcon(getClass().getResource("tb.png"));
		private Icon automat = new ImageIcon(getClass().getResource("automat.png"));
		private Icon schema = new ImageIcon(getClass().getResource("schema.png"));
		private Icon simulation = new ImageIcon(getClass().getResource("simulation.png"));


		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
					hasFocus);

			DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
			// String name = null;
			// provjeri je li to ime projekta, tj. je li cvor projekt
			DefaultMutableTreeNode parent = null;

			if (node.isRoot()) {
				return this;
			}
			parent = (DefaultMutableTreeNode)(node.getParent());
			if (parent.isRoot()) {
				return this;
			}
			TreeNode[] treeNode = node.getPath();
			String nodeProjectName = (String)((DefaultMutableTreeNode)(treeNode[1]))
					.getUserObject();

			// provjeri kojeg je tipa
			try {
				type = projectContainer.getFileType(nodeProjectName, node.toString());
			} catch (UniformAppletException e) {
				e.printStackTrace();
			}

			if (FileTypes.FT_VHDL_SOURCE.equals(type)) {
				setIcon(vhdl);
				setToolTipText("VHDL source file");
			} else if (FileTypes.FT_VHDL_TB.equals(type)) {
				setIcon(tb);
				setToolTipText("Testbench");
			} else if (FileTypes.FT_VHDL_AUTOMAT.equals(type)) {
				setIcon(automat);
				setToolTipText("Automat");
			} else if (FileTypes.FT_VHDL_STRUCT_SCHEMA.equals(type)) {
				setIcon(schema);
				setToolTipText("Schema");
			} else if (FileTypes.FT_VHDL_SIMULATION.equals(type)) {
				setIcon(simulation);
				setToolTipText("Simulation");
			}
			return this;
		}
	}



	private TreeExpansionListener treeExpansionListener = new TreeExpansionListener() {
		public void treeExpanded(TreeExpansionEvent event) {
			isNowExpanded = true;
		}


		public void treeCollapsed(TreeExpansionEvent e) {
			isNowCollapse = true;
		}
	};

	/**
	 * Listener popup menija
	 */
	private ActionListener buttonListener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			JButton source = (JButton)(event.getSource());
			if (source.equals(normalButton)) {
				if (currentHierarchy.equals(X_USES_Y)) {
					return;
				}
				// List<String> projects = projectContainer.getAllProjects();

				List<String> projects = allProjects;
				expandedNodes.clear();

				currentHierarchy = X_USES_Y;
				tree = treeNormal;
				treeModel = treeModelNormal;

				// obrisi sve prijasnje projekte iz stabla i napravi nove
				top.removeAllChildren();
				treeModel.reload();
				// nakon toga dohvati sve projekte is project containera i
				// kreiraj podstablo za taj projekt
				for (String project : projects) {
					DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode(project);
					treeModelNormal.insertNodeInto(tempNode, top, top.getChildCount());
					// topInverse.add(tempNode);
					buildXusesYForOneProject(project, tempNode);
				}

				treeModel.reload();
				cp.removeAll();
				cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
				cp.add(toolbarPanel);
				cp.add(treeView);
				cp.repaint();
				cp.validate();

			} else if (source.equals(inverseButton)) {
				if (currentHierarchy.equals(X_USED_IN_Y)) {
					return;
				}
				// List<String> projects = projectContainer.getAllProjects();
				List<String> projects = allProjects;
				expandedNodes.clear();

				currentHierarchy = X_USED_IN_Y;
				tree = treeInverse;
				treeModel = treeModelInverse;

				// obrisi sve prijasnje projekte iz stabla i napravi nove
				topInverse.removeAllChildren();
				treeModel.reload();
				// nakon toga dohvati sve projekte is project containera i
				// kreiraj
				// podstablo za taj projekt
				for (String project : projects) {
					DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode(project);
					treeModelInverse.insertNodeInto(tempNode, topInverse, topInverse
							.getChildCount());
					// topInverse.add(tempNode);
					buildXusedInYForOneProject(project, tempNode);
				}

				treeModel.reload();
				// izbrisi prethodno stablo iz panela i dodaj novo
				cp.removeAll();
				cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
				cp.add(toolbarPanel);
				cp.add(treeViewInverse);
				cp.repaint();
				cp.validate();

			} else {
				if (currentHierarchy.equals(FLAT)) {
					return;
				}
				// List<String> projects = projectContainer.getAllProjects();
				List<String> projects = allProjects;
				expandedNodes.clear();

				currentHierarchy = FLAT;
				tree = treeFlat;
				treeModel = treeModelFlat;

				// obrisi sve prijasnje projekte iz stabla i napravi nove
				topFlat.removeAllChildren();
				treeModel.reload();
				// nakon toga dohvati sve projekte is project containera i
				// kreiraj
				// podstablo za taj projekt
				for (String project : projects) {
					DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode(project);
					treeModelFlat.insertNodeInto(tempNode, topFlat, topFlat
							.getChildCount());
					// topInverse.add(tempNode);
					buildFlatForOneProject(project, tempNode);
				}

				treeModel.reload();
				// izbrisi prethodno stablo iz panela i dodaj novo
				cp.removeAll();
				cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
				cp.add(toolbarPanel);
				cp.add(treeViewFlat);
				cp.repaint();
				cp.validate();
			}
		}
	};

	/**
	 * Listener popup menija
	 */
	private ActionListener popupListener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {

			String fileName = null;
			String name = null;

			if (event.getSource().equals(addProject)) {
				try {
					projectContainer.createNewProjectInstance();
				} catch (UniformAppletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (event.getSource().equals(addVHDL)) {
				try {
					projectContainer.createNewFileInstance(FileTypes.FT_VHDL_SOURCE);
				} catch (UniformAppletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (event.getSource().equals(addTb)) {
				try {
					projectContainer.createNewFileInstance(FileTypes.FT_VHDL_TB);
				} catch (UniformAppletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (event.getSource().equals(addSchema)) {
				try {
					projectContainer
							.createNewFileInstance(FileTypes.FT_VHDL_STRUCT_SCHEMA);
				} catch (UniformAppletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (event.getSource().equals(addAutomat)) {
				try {
					projectContainer.createNewFileInstance(FileTypes.FT_VHDL_AUTOMAT);
				} catch (UniformAppletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (event.getSource().equals(compile)) {
				fileName = getFileName();
				if (fileName != null) {
					try {
						name = getProjectName();
						if (name != null) {
							projectContainer.compile(name, fileName);
						}
					} catch (UniformAppletException ex) {
						;
					}
				}
			} else if (event.getSource().equals(simulate)) {
				fileName = getFileName();
				if (fileName != null) {
					try {
						name = getProjectName();
						if (name != null) {
							projectContainer.simulate(name, fileName);
						}
					} catch (UniformAppletException ex) {
						;
					}
				}
			} else if (event.getSource().equals(setActive)) {
				// selektirani projekt postaje aktivni projekt
				name = getProjectName();
				if (name != null) {
					projectName = name;
				}
			} else if (event.getSource().equals(viewVHDL)) {
				fileName = getFileName();
				if (fileName != null) {
					try {
						name = getProjectName();
						if (name != null) {
							projectContainer.viewVHDLCode(name, fileName);
						}
					} catch (UniformAppletException ex) {
						;
					}
				}
			} else if (event.getSource().equals(deleteFile)) {
				deleteFile();
			} else if (event.getSource().equals(deleteProject)) {
				deleteProject();
			} else if (event.getSource().equals(xusesY)) {
				if (currentHierarchy.equals(X_USES_Y)) {
					return;
				}
				List<String> projects = allProjects;
				expandedNodes.clear();
				currentHierarchy = X_USES_Y;
				tree = treeNormal;
				treeModel = treeModelNormal;

				// obrisi sve prijasnje projekte iz stabla i napravi nove
				top.removeAllChildren();
				treeModel.reload();
				// nakon toga dohvati sve projekte is project containera i
				// kreiraj
				// podstablo za taj projekt
				for (String project : projects) {
					DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode(project);
					treeModelNormal.insertNodeInto(tempNode, top, top.getChildCount());
					// topInverse.add(tempNode);
					buildXusesYForOneProject(project, tempNode);
				}

				treeModel.reload();
				cp.removeAll();
				cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
				cp.add(toolbarPanel);
				cp.add(treeView);
				cp.repaint();
				cp.validate();
			} else if (event.getSource().equals(xusedInY)) {
				if (currentHierarchy.equals(X_USED_IN_Y)) {
					return;
				}
				List<String> projects = allProjects;
				expandedNodes.clear();
				currentHierarchy = X_USED_IN_Y;
				tree = treeInverse;
				treeModel = treeModelInverse;

				// obrisi sve prijasnje projekte iz stabla i napravi nove
				topInverse.removeAllChildren();
				treeModel.reload();
				// nakon toga dohvati sve projekte is project containera i
				// kreiraj
				// podstablo za taj projekt
				for (String project : projects) {
					DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode(project);
					treeModelInverse.insertNodeInto(tempNode, topInverse, topInverse
							.getChildCount());
					// topInverse.add(tempNode);
					buildXusedInYForOneProject(project, tempNode);
				}

				treeModel.reload();
				// izbrisi prethodno stablo iz panela i dodaj novo
				cp.removeAll();
				cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
				cp.add(toolbarPanel);
				cp.add(treeViewInverse);
				cp.repaint();
				cp.validate();
			} else if (event.getSource().equals(flat)) {
				if (currentHierarchy.equals(FLAT)) {
					return;
				}
				List<String> projects = allProjects;
				expandedNodes.clear();
				currentHierarchy = FLAT;
				tree = treeFlat;
				treeModel = treeModelFlat;

				// obrisi sve prijasnje projekte iz stabla i napravi nove
				topFlat.removeAllChildren();
				treeModel.reload();
				// nakon toga dohvati sve projekte is project containera i
				// kreiraj
				// podstablo za taj projekt
				for (String project : projects) {
					DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode(project);
					treeModelFlat.insertNodeInto(tempNode, topFlat, topFlat
							.getChildCount());
					// topInverse.add(tempNode);
					buildFlatForOneProject(project, tempNode);
				}

				treeModel.reload();
				// izbrisi prethodno stablo iz panela i dodaj novo
				cp.removeAll();
				cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
				cp.add(toolbarPanel);
				cp.add(treeViewFlat);
				cp.repaint();
				cp.validate();
			}
			TreePath treePath = tree.getSelectionPath();
			// tree.setExpandedState(treePath, true);
			// tree.repaint();
			// treeModel.reload();
			tree.scrollPathToVisible(treePath);
		}
	};

	/**
	 * Listener JTree
	 */
	private MouseListener treeMouse = new MouseAdapter() {
		public void mousePressed(MouseEvent event) {

			String fileName = null;
			String name = null;
			TreePath selPath = tree.getPathForLocation(event.getX(), event.getY());

			workingTreePath = tree.getClosestPathForLocation(event.getX(), event.getY());
			if (isNowExpanded) {
				isNowExpanded = false;
				expandedNodes.add(workingTreePath);
			}
			if (isNowCollapse) {
				isNowCollapse = false;
				expandedNodes.remove(workingTreePath);
			}

			if (selPath != null) {
				if (event.getClickCount() == 1) {
					name = getProjectName();
					if (name != null) {
						projectName = name;
					}

				} else if (event.getClickCount() == 2) {
					fileName = getFileName();
					if (fileName != null) {
						try {
							name = getProjectName();
							if (name != null) {
								projectContainer.openEditor(name, fileName, true, false);
							}
						} catch (UniformAppletException ex) {
							ex.printStackTrace();
						}
					}
				}
			}

		}
	};


		/**
	 * Mouse listener koji prikazuje popup meni
	 */
	private MouseListener mouseListener = new MouseListener() {
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
			if (event.getButton() == 3 || event.getButton() == 2) {
				TreePath selPath = tree.getPathForLocation(event.getX(), event.getY());
				tree.setSelectionPath(selPath);
				workingTreePath = selPath;
				String name = getProjectName();
				if (name != null) {
					projectName = name;
				}

				int borderPanel = 600 / 2;
				if (event.getY() >= borderPanel) {
					optionsPopup.show(tree, event.getX(), event.getY()
							- optionsPopup.getHeight());
				} else {
					optionsPopup.show(tree, event.getX(), event.getY());
				}
			}

			/* vraca fokus na kontejner */
			cp.requestFocusInWindow();
		}
	};


	/**
	 * Metoda provjerava koji projekt je trenutno selektiran i cini ga aktivnim
	 * 
	 * @return String ime selektiranog projekta, null nema selekcije
	 */
	private String getProjectName() {
		TreePath treePath = null;
		Object[] objectPath = null;

		treePath = tree.getSelectionPath();
		// samo ako je uopce bio selektiran
		if (treePath != null) {
			objectPath = treePath.getPath();
			// ako je selektiran root cvor
			if (objectPath.length == 1) {
				return projectName;
			}
			// uzmi ime projekta i ucini ga aktivnim
			projectName = ((DefaultMutableTreeNode)(objectPath[1])).toString();
		}
		
		return projectName;
	}


	/**
	 * Metoda provjerava koja je datoteka trenutno selektirana
	 * 
	 * @return String ime selektirane datoteke
	 */
	private String getFileName() {
		String fileName = null;
		DefaultMutableTreeNode selectedNode = null;

		selectedNode = ((DefaultMutableTreeNode)tree.getLastSelectedPathComponent());
		if (selectedNode != null) {
			fileName = selectedNode.toString();
		}
		return fileName;
	}


	/**
	 * Postavlja projectContainer
	 * 
	 * @param pContainer projectContainer
	 */
	public void setProjectContainer(ProjectContainer pContainer) {
		this.projectContainer = pContainer;
	}


	/**
	 * Dodaje novu datoteku u neki projekt.
	 * 
	 * @param projectContainer ime projekta u koji ide datoteka
	 * @param fileName ime datoteke
	 */
	public void addFile(String projectName, String fileName) {
		/* dodaje novu datoteku u mapu<projekt, kolekcija datoteka> */
		this.filesByProjects.get(projectName).add(fileName);
		this.refreshProject(projectName);
	}


	/**
	 * Metoda dodaje novi projekt u projectExplorer. 
	 * 
	 * @param projectName ime projekta
	 */
	public void addProject(String projectName) {
		DefaultMutableTreeNode projectNode = null;
		DefaultMutableTreeNode topNode = null;

		/* dodaje novi projekt u kolekciju svih projekata koje prikazuje PE */
		this.allProjects.add(projectName);

		/* dodaje novi projekt i stvara listu datoteka u mapi<projekt, datoteke> */
		this.filesByProjects.put(projectName, new ArrayList<String>());


		// nadi prikaz hijerarhije i stavljaj na njen root cvor
		if (currentHierarchy.equals(X_USES_Y)) {
			topNode = top;
		} else if (currentHierarchy.equals(X_USED_IN_Y)) {
			topNode = topInverse;
		} else {
			topNode = topFlat;
		}

		// dodaj novi cvor, tj. projekt u stablo
		if (getNode(topNode, projectName) == null) {
			projectNode = new DefaultMutableTreeNode(projectName);
			treeModel.insertNodeInto(projectNode, topNode, topNode.getChildCount());
			// this.topNode.add(projectNode); moze i tako, ili pomocu inserInto
		} else {
			projectNode = getNode(topNode, projectName);
		}

		// izgradi podstablo u ovisnosti o hijerarhiji
		if (currentHierarchy.equals(X_USES_Y)) {
			this.buildXusesYForOneProject(projectName, projectNode);
		} else if (currentHierarchy.equals(X_USED_IN_Y)) {
			this.buildXusedInYForOneProject(projectName, projectNode);
		} else {
			this.buildFlatForOneProject(projectName, projectNode);
		}
		treeModel.reload();
		for (TreePath treePath : expandedNodes) {
			tree.expandPath(treePath);
		}
	}


	/**
	 * Metoda refresha stablo.  Interakcijom s korinsikom u stablo se mogu
	 * dodavati, brisati elementi.  Ti elementi, s druge strane, imaju
	 * razlicitu hijerarhiju prikaza (source iz kojeg je napravljen testbench
	 * cini dijete testbencha i slicno) pa je potrebno svaki put pozivati
	 * novu hijerarhiju.
	 *
	 * @param projectName ime projekta koji se refresha
	 */
	public void refreshProject(String projectName) {
		// delete all previuos nodes from this projectName
		DefaultMutableTreeNode projectNode = null;
		DefaultMutableTreeNode topNode = null;

		// nadi prikaz hijerarhije i stavljaj na njen root cvor
		if (currentHierarchy.equals(X_USES_Y)) {
			topNode = top;
		} else if (currentHierarchy.equals(X_USED_IN_Y)) {
			topNode = topInverse;
		} else {
			topNode = topFlat;
		}

		for (int i = 0; i < topNode.getChildCount(); i++) {
			projectNode = (DefaultMutableTreeNode)(topNode.getChildAt(i));
			if (projectNode.getUserObject().equals(projectName)) {
				projectNode.removeAllChildren();
				// dohvaca sve root cvorove tog projekta
				// izgradi podstablo u ovisnosti o hijerarhiji
				if (currentHierarchy.equals(X_USES_Y)) {
					this.buildXusesYForOneProject(projectName, projectNode);
				} else if (currentHierarchy.equals(X_USED_IN_Y)) {
					this.buildXusedInYForOneProject(projectName, projectNode);
				} else {
					this.buildFlatForOneProject(projectName, projectNode);
				}
			}
		}
		// reloada model stabla i vraca stablo u prijasnje stanje, preciznije:
		// svi cvorovi koji su bili otvoreni, otvaraju se nanovo jer reload modela
		// skuplja cijelo stablo
		treeModel.reload();
		for (TreePath treePath : expandedNodes) {
			tree.expandPath(treePath);
		}
	}


	/**
	 * pomocna funkcija koja gradi stablo x uses y za jedan projekt
	 * 
	 * @param projectName ime projekta za kojeg se radi hijerarhija stabla
	 * @param projectNode
	 */
	private void buildXusesYForOneProject(String projectName,
			DefaultMutableTreeNode projectNode) {

		// dohvaca sve root cvorove tog projekta
		try {
			hierarchy = projectContainer.extractHierarchy(projectName);
		} catch (UniformAppletException e) {
			 StringWriter sw = new StringWriter();
			 PrintWriter pw = new PrintWriter(sw);
			 e.printStackTrace(pw);
			 throw new NullPointerException(sw.toString());
		}
		DefaultMutableTreeNode rootNode = null;
		for (String string : hierarchy.getRootNodes()) {
			if (getNode(projectNode, string) == null) {
				rootNode = new DefaultMutableTreeNode(string);
				// projectNode.add(rootNode);
				treeModel.insertNodeInto(rootNode, projectNode, projectNode
						.getChildCount());

			} else {
				rootNode = getNode(projectNode, string);
			}
			addChildren(rootNode, hierarchy);
		}
	}


	/**
	 * pomocna funkcija koja gradi stablo x used in y za jedan projekt. Prvo 
	 * se dodaju svi root cvorovi ako oni nemaju djece, a ako imaju djece 
	 * stavljaju se u listu koja cini trenutnu stazu do krajnjeg cvora.  
	 * Kada se nade krajnji cvor, on se _mozebitno_ dodaje u listu (ako vec
	 * nije bio dodan) i na njega se stavljaju redom cvorovi koji su cinili
	 * stazu do njega
	 * 
	 * @param projectName ime projekta za kojeg se radi hijerarhija stabla
	 * @param projectNode cvor projekta
	 */
	private void buildXusedInYForOneProject(String projectName,
			DefaultMutableTreeNode projectNode) {

		// kolekcija sadrzi sve leafove koji se vec nalaze u podstablu
		Map<String, DefaultMutableTreeNode> leafsInTree = new HashMap<String, DefaultMutableTreeNode>();
		// lista sadrzi cvorove koji cine stazu do leafa
		LinkedList<String> nodesUsesLeaf = new LinkedList<String>();

		DefaultMutableTreeNode rootNode = null;

		try {
			hierarchy = projectContainer.extractHierarchy(projectName);
		} catch (UniformAppletException e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			throw new NullPointerException(sw.toString());
		}

		// dohvaca sve root cvorove tog projekta i nakon sto dohvati rootove
		// one rootove koji su leafovi stavlja odmah u stablo, a ostalima
		// rekurzivno
		// trazi kraj
		for (String string : hierarchy.getRootNodes()) {
			nodesUsesLeaf.clear();
			Set<String> children = hierarchy.getChildrenForParent(string);
			if (children.isEmpty()) {
				rootNode = new DefaultMutableTreeNode(string);
				treeModel.insertNodeInto(rootNode, projectNode, projectNode
						.getChildCount());
			} else {
				nodesUsesLeaf.add(string);
				traverseNonLeaf(string, children, nodesUsesLeaf, leafsInTree, hierarchy,
						projectNode);
			}
		}
	}


	/**
	 * Metoda uzima cvor i pretrazuje stablo dok ne nade sve krajnje cvorove.
	 * Kada nade pojedini krajnji cvor, provjerava je li taj cvor vec dodan u
	 * inverse stablo ako je, onda dodaje samo cvorove kojima se doslo do tog
	 * cvora, inace dodaje krajnji i sve ostale.
	 * 
	 * @param parent cvor cije se podstablo pretrazuje
	 * @param children djeca tog roditelja
	 * @param nodesUsesLeaf lista cvorova kojima se doslo do krajnjeg
	 * @param leafsInTree mapa krajnjih koji se vec nalaze u inverznom stablu
	 * @param hierarchy hijerarhija ovog projekta
	 * @param projectNode cvor projekta
	 */
	private void traverseNonLeaf(String parent, Set<String> children,
			LinkedList<String> nodesUsesLeaf,
			Map<String, DefaultMutableTreeNode> leafsInTree, Hierarchy hierarchy,
			DefaultMutableTreeNode projectNode) {

		DefaultMutableTreeNode currentTop = null;
		DefaultMutableTreeNode node = null;

		for (String child : children) {
			Set<String> childChildren = hierarchy.getChildrenForParent(child);
			// ako dijete nema djece
			if (childChildren.isEmpty()) {
				// ako kraj postoji vec u stablu
				if (leafsInTree.containsKey(child)) {
					// uzmi taj cvor iz mape
					node = leafsInTree.get(child);

				} else {
					node = new DefaultMutableTreeNode(child);
					leafsInTree.put(child, node);
					this.treeModel.insertNodeInto(node, projectNode, projectNode
							.getChildCount());
				}
				currentTop = node;
				for (int i = 0; i < nodesUsesLeaf.size(); i++) {
					DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode(
							nodesUsesLeaf.getLast());
					nodesUsesLeaf.removeLast();
					this.treeModel.insertNodeInto(tempNode, currentTop, currentTop
							.getChildCount());
					currentTop = tempNode;
				}
			} else {
				nodesUsesLeaf.add(child);
				this.traverseNonLeaf(child, childChildren, nodesUsesLeaf, leafsInTree,
						hierarchy, projectNode);
			}
		}
	}


	/**
	 * pomocna funkcija koja gradi stablo flat hijerarhije za jedan projekt
	 * Obican algoritam kroz stablo.
	 * 
	 * @param projectName ime projekta za kojeg se radi hijerarhija stabla
	 */
	private void buildFlatForOneProject(String projectName,
			DefaultMutableTreeNode projectNode) {

		Set<String> nodesInTree = new HashSet<String>();
		DefaultMutableTreeNode rootNode = null;

		try {
			hierarchy = projectContainer.extractHierarchy(projectName);
		} catch (UniformAppletException e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			throw new NullPointerException(sw.toString());
		}

		for (String string : hierarchy.getRootNodes()) {
			rootNode = new DefaultMutableTreeNode(string);
			this.treeModel.insertNodeInto(rootNode, projectNode, projectNode.getChildCount());
			this.traverseTree(string, nodesInTree, hierarchy, projectNode);
		}
	}


	/**
	 * Metoda prolazi kroz sve cvorove stabla i stavlja ih u projectNode
	 * Za flat hijerarhiju
	 * 
	 * @param parent roditelj
	 * @param nodesInTree cvorovi koji su vec stavljeni u stablo
	 * @param projectNode cvor projekta
	 */
	private void traverseTree(String parent, Set<String> nodesInTree,
			Hierarchy hierarchy, DefaultMutableTreeNode projectNode) {

		Set<String> children = hierarchy.getChildrenForParent(parent);
		for (String child : children) {
			if (!nodesInTree.contains(child)) {
				DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode(child);
				this.treeModel.insertNodeInto(tempNode, projectNode, projectNode
						.getChildCount());
				nodesInTree.add(child);
			}
			this.traverseTree(child, nodesInTree, hierarchy, projectNode);
		}
	}


	/**
	 * Metoda koja puni stablo
	 * 
	 * @param parent cvor koji predstavlja roditelja
	 */
	private DefaultMutableTreeNode addChildren(DefaultMutableTreeNode parent,
			Hierarchy hierarchy) {

		Set<String> children = hierarchy.getChildrenForParent(parent.toString());
		for (String child : children) {
			if (getNode(parent, child) == null) {
				this.treeModel.insertNodeInto(addChildren(new DefaultMutableTreeNode(child),
						hierarchy), parent, parent.getChildCount());
				// parent.add(addChildren(new DefaultMutableTreeNode(child),
				// hierarchy));
			} else {
				// parent.add(addChildren(getNode(parent, child), hierarchy));
				this.treeModel.insertNodeInto(addChildren(getNode(parent, child), hierarchy),
						parent, parent.getChildCount());

			}
		}
		return parent;
	}


	/**
	 * Metoda provjerava djeca ovog roditelja, u ovom slucaju String objekt
	 * 
	 * @return cvor, ako postoji, inace null
	 */
	private DefaultMutableTreeNode getNode(DefaultMutableTreeNode parent, Object child) {
		DefaultMutableTreeNode temp = null;

		if (parent == null) {
			return null;
		}

		// provjera postoji li vec taj cvor
		for (int i = 0; i < parent.getChildCount(); i++) {
			temp = (DefaultMutableTreeNode)(parent.getChildAt(i));
			if (temp.toString().equals((String)child)) {
				return temp;
			}
		}
		return null;
	}


	/**
	 * Metoda zatvara projekt
	 */
	public void closeProject(String projectName) {
		;
	}


	/**
	 * Metoda vraca aktivni projekt
	 * 
	 * @return projectName aktivni projekt
	 */
	public String getSelectedProject() {
		return this.projectName;
	}


	/**
	 * Metoda vraca popis svih projekata koje sadrzi PE
	 * 
	 * @return listu projekata
	 */
	public List<String> getAllProjects() {
		return this.allProjects;
	}


	/**
	 * Metoda vraca popis svih datoteka koje se nalaze u projektu iz argumenata.
	 * 
	 * @param projectName ime projekta
	 * @return lista datoteka koju posjeduje taj projekt
	 */
	public List<String> getFilesByProject(String projectName) {
		return this.filesByProjects.get(projectName);
	}


	/**
	 * Metoda brise datoteku iz projekta, ako postoji takav projekt, i ako
	 * postoji takva datoteka.
	 *
	 * @param projectName ime projekta iz kojeg se zeli ukloniti datoteka
	 * @param fileName ime datoteke koja se uklanja
	 */
	public void removeFile(String projectName, String fileName) {
		/* mice tu datoteku iz mape<projekt, lista datoteka> */
		this.filesByProjects.get(projectName).remove(fileName);
		this.refreshProject(projectName);
	}


	/**
	 * Metoda brise projekt, ako takav projekt uopce postoji
	 *
	 * @param projectName ime projekta
	 */
	public void removeProject(String projectName) {
		this.allProjects.remove(projectName);
		this.refreshProject(projectName);
	}



	/**
	 * Metoda brise selektirani cvor.  Prvo se provjerava je li stablo uopce
	 * selektirano.  Nakon toga slijede provjere da nije selektiran cvor koji
	 * predstavlja sam projekt, i ako jest, ne radi nista.  Ako je pak selektiran
	 * obican file, taj se file brise iz PE-a i refresha se projekt nanovo jer
	 * se hijerarhija mogla promijeniti
	 */
	private void deleteFile() {
		Object obj = null;
		DefaultMutableTreeNode node = null;

		TreePath treePath = tree.getSelectionPath();
		String name = getProjectName();
		if (treePath == null) {
			// ako nema selekcije
			return;
		} else {
			obj = treePath.getLastPathComponent();
			node = (DefaultMutableTreeNode)obj;
			// ako je to projekt ili root
			if (node.isRoot() || ((DefaultMutableTreeNode)(node.getParent())).isRoot()) {
				return;
			}
			this.treeModel.removeNodeFromParent(node);
			try {
				this.projectContainer.deleteFile(name, node.toString());
			} catch (UniformAppletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		/* mice tu datoteku iz mape<projekt, lista datoteka> */
		this.filesByProjects.get(name).remove(node.toString());
		this.refreshProject(projectName);

		/* reloada model i nakon toga iznova otvara sve cvorove koji su bili otvoreni */
		this.treeModel.reload();
		for (TreePath treePath1 : expandedNodes) {
			tree.expandPath(treePath1);
		}
	}


	/**
	 * Metoda brise projekt iz PE-a.  Prvo se provjerava je li stablo uopce 
	 * selektirano i ako jest, pronalazi se ime projekta kojeg se zeli obrisati
	 * Vazno je napomenuti da moze biti selektiran i neki file unutar tog 
	 * projekta te je zbog toga potrebno napraviti proracun ciji je selektirani
	 * cvor
	 */
	private void deleteProject() {
		Object obj = null;
		DefaultMutableTreeNode node = null;

		TreePath treePath = tree.getSelectionPath();
		if (treePath == null) {
			return;
		} else {
			obj = treePath.getLastPathComponent();
			node = (DefaultMutableTreeNode)obj;
			if (node.isRoot() || !((DefaultMutableTreeNode)(node.getParent())).isRoot()) {
				return;
			}
			this.treeModel.removeNodeFromParent((DefaultMutableTreeNode)node);
			try {
				this.projectContainer.deleteProject(node.toString());
				this.allProjects.remove(node.toString());
			} catch (UniformAppletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/* brise projekt iz liste projekata u tom PE-u */
		this.allProjects.remove(node.toString());
		this.refreshProject(projectName);
	}


	public FileIdentifier getSelectedFile() {
		// TODO Auto-generated method stub
		return null;
	}
}
