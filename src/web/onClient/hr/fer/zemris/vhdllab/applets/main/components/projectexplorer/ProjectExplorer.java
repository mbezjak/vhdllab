package hr.fer.zemris.vhdllab.applets.main.components.projectexplorer;


import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;



public class ProjectExplorer extends JPanel implements IProjectExplorer {

	private Container cp = this;

	/** Popup za prikaz menija */
	private JPopupMenu optionsPopup = new JPopupMenu();

	/** JTree root node */
	private DefaultMutableTreeNode top;

	/** Used in root node */
	private DefaultMutableTreeNode root;

	/** Model kojeg koristi JTree */
	private DefaultTreeModel treeModel;

	/** JTree */
	private JTree tree;
	private JTree usedInTree;
	private JScrollPane treeView;
	private JScrollPane usedInTreeView;

	private JMenu newSubMenu;
	private JMenuItem addProject;
	private JMenuItem addFile;
	private JMenuItem compile;
	private JMenuItem simulate;
	private JMenuItem setActive;
	private JMenuItem viewVHDL;
	private JMenuItem delete;

	/** Aktivni projekt */
	private String projectName;

	/** ProjectContainer */
	private ProjectContainer projectContainer;

	private static final long serialVersionUID = 4932799790563214089L;


	/**
	 * Default Constructor
	 */
	public ProjectExplorer() {
		newSubMenu = new JMenu("New");
		addProject = new JMenuItem("Add project");
		addFile = new JMenuItem("Add file");
		compile = new JMenuItem("Compile");
		simulate = new JMenuItem("Simulate");
		setActive = new JMenuItem("Set active project");
		viewVHDL = new JMenuItem("View VHDL");
		delete = new JMenuItem("Delete");

		/* popup meni */
		addProject.addActionListener(popupListener);
		addFile.addActionListener(popupListener);
		compile.addActionListener(popupListener);
		simulate.addActionListener(popupListener);
		setActive.addActionListener(popupListener);
		viewVHDL.addActionListener(popupListener);
		delete.addActionListener(popupListener);

		newSubMenu.add(addProject);
		newSubMenu.add(addFile);
		optionsPopup.add(newSubMenu);
		optionsPopup.addSeparator();
		optionsPopup.add(compile);
		optionsPopup.add(simulate);
		optionsPopup.addSeparator();
		optionsPopup.add(setActive);
		optionsPopup.add(viewVHDL);
		optionsPopup.addSeparator();
		optionsPopup.add(delete);
		/* kraj inicijalizacije popupa menija */

		/* inicijalizacija root cvora i modela */
		top = new DefaultMutableTreeNode("Vhdllab");
		treeModel = new DefaultTreeModel(top);

		/* inicijalizacija JTree komponente */
		tree = new JTree(treeModel);
		//tree.setPreferredSize(new Dimension(500, 600));
		tree.addMouseListener(mouseListener);
		tree.addMouseListener(treeMouse);
		tree.setEditable(true);
		tree.setRootVisible(false);
		tree.expandRow(0);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		treeView = new JScrollPane(tree);
		VHDLCellRenderer renderer = new VHDLCellRenderer();
		tree.setCellRenderer(renderer);
		ToolTipManager.sharedInstance().registerComponent(tree);
		/* kraj inicijalizacije tree komponente */

		/* inicijalizacija transformirane JTree komponente */
		usedInTree = new JTree(root);
		usedInTree.setPreferredSize(new Dimension(500, 600));
		usedInTree.setEditable(false);
		usedInTree.setRootVisible(false);
		usedInTree.expandRow(0);
		usedInTree.getSelectionModel();
		usedInTree.setCellRenderer(renderer);
		ToolTipManager.sharedInstance().registerComponent(usedInTree);
		usedInTreeView = new JScrollPane(usedInTree);

		this.setLayout(new BorderLayout());
		cp.add(treeView, BorderLayout.CENTER);
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


		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
					hasFocus);

			DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
			// provjeri kojeg je tipa
			try {
				type = projectContainer.getFileType(projectName, node.toString());
			} catch (UniformAppletException e) {
				e.printStackTrace();
			}
			if (FileTypes.FT_VHDL_SOURCE.equals("and")) {
				setIcon(Icons.VHDL.getIcon());
				setToolTipText("VHDL source file");
			} else if (FileTypes.FT_VHDL_TB.equals(type)) {
				setIcon(Icons.TB.getIcon());
				setToolTipText("Testbench");
			} else if (FileTypes.FT_VHDL_AUTOMAT.equals(type)) {
				setIcon(Icons.AUTOMAT.getIcon());
				setToolTipText("Automat");
			} else if (FileTypes.FT_VHDL_STRUCT_SCHEMA.equals(type)) {
				setIcon(Icons.SCHEMA.getIcon());
				setToolTipText("Schema");
			} else if (FileTypes.FT_VHDL_SIMULATION.equals(type)) {
				setIcon(Icons.SIMULATION.getIcon());
				setToolTipText("Simulation");
			}

			return this;
		}
	}

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
	 * Listener popup menija
	 */
	private ActionListener popupListener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {

			String fileName = null;

			if (event.getSource().equals(addProject)) {
				tree.clearSelection();
			} else if (event.getSource().equals(addFile)) {
				addObject("novi cvor kojeg treba provjeriti kojeg je tipa i jadajada");
			} else if (event.getSource().equals(compile)) {
				fileName = getFileName();
				if (fileName != null) {
					try {
						projectContainer.compile(getProjectName(), fileName);
					} catch (UniformAppletException ex) {
						;
					}
				}
			} else if (event.getSource().equals(simulate)) {
				fileName = getFileName();
				if (fileName != null) {
					try {
						projectContainer.simulate(getProjectName(), fileName);
					} catch (UniformAppletException ex) {
						;
					}
				}
			} else if (event.getSource().equals(setActive)) {
				// selektirani projekt postaje aktivni projekt
				getProjectName();
			} else if (event.getSource().equals(viewVHDL)) {
				fileName = getFileName();
				if (fileName != null) {
					try {
						projectContainer.viewVHDLCode(getProjectName(), fileName);
					} catch (UniformAppletException ex) {
						;
					}
				}
			} else if (event.getSource().equals(delete)) {
				removeNode();
			}
			tree.repaint();

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
			// uzmi ime projekta i ucini ga aktivnim
			projectName = ((DefaultMutableTreeNode)(objectPath[1])).toString();
		} else {
			projectName = null;
		}
		this.setActiveProject(projectName);
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
	 * Listener JTree
	 */
	private MouseListener treeMouse = new MouseAdapter() {
		public void mousePressed(MouseEvent event) {

			String fileName = null;
			TreePath selPath = tree.getPathForLocation(event.getX(), event.getY());
			if (selPath != null) {
				if (event.getClickCount() == 1) {
					// System.out.println("selPath " + selPath);
					for (Object o1 : selPath.getPath()) {
						// System.out.println(o1);
					}

				} else if (event.getClickCount() == 2) {
					fileName = getFileName();
					if (fileName != null) {
						try {
							projectContainer.openEditor(getProjectName(), fileName, true,
									false);
						} catch (UniformAppletException ex) {
							;
						}
					}
				}
			}
		}
	};


	/**
	 * Postavlja projectContainer
	 * 
	 * @param pContainer projectContainer
	 */
	public void setProjectContainer(ProjectContainer pContainer) {
		this.projectContainer = pContainer;
	}


	/**
	 * Dodaje novu datoteku
	 * 
	 * @param projectContainer ime projekta u koji ide datoteka
	 * @param fileName ime datoteke
	 */
	public void addFile(String projectName, String fileName) {
		// postavlja aktivni projekt, ovisno o trenutnoj selekciji
		addObject(fileName);

		// ovdje po potrebi moze bacati IllegalArgumentException
		// ako se odluci striktno pratiti aktivni projekti
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
	public String getActiveProject() {
		return projectName;
	}


	/**
	 * Metoda vraca popis svih projekata
	 * 
	 * @return listu projekata
	 */
	public List<String> getAllProjects() {
		List<String> projects = new ArrayList<String>();
		DefaultMutableTreeNode projectNode = null;

		for (int i = 0; i < top.getChildCount(); i++) {
			projectNode = (DefaultMutableTreeNode)(top.getChildAt(i));
			projects.add(projectNode.toString());
		}
		return projects;
	}


	/**
	 * Metoda vraca popis svih datoteka koje se nalaze u projektu iz argumenata.
	 * 
	 * @param projectName ime projekta
	 */
	public List<String> getFilesByProject(String projectName) {
		/* umjesto sejvanja samo aktivnog projekta, sejvaj sve? */
		// if(!this.projectName.equals(projectName)) {
		// return null;
		// }
		boolean contains = false;
		DefaultMutableTreeNode projectNode = null;
		DefaultMutableTreeNode fileNode = null;
		List<String> fileNames = new ArrayList<String>();

		for (int i = 0; i < top.getChildCount(); i++) {
			projectNode = (DefaultMutableTreeNode)(top.getChildAt(i));
			if (projectNode.toString().equals((String)projectName)) {
				contains = true;
				break;
			}
		}
		if (!contains) {
			return null;
		}
		addNodesToList(projectNode, fileNames);
		return fileNames;
	}


	private void addNodesToList(DefaultMutableTreeNode parent, List<String> files) {
		DefaultMutableTreeNode child = null;

		for (int i = 0; i < parent.getChildCount(); i++) {
			child = (DefaultMutableTreeNode)(parent.getChildAt(i));
			files.add(child.toString());
			if (!child.isLeaf()) {
				addNodesToList(child, files);
			}
		}
	}


	/**
	 * Metoda brise datoteku iz projekta, ako postoji takav projekt, i ako
	 * postoji takva datoteka.
	 */
	public void removeFile(String projectName, String fileName) {
		boolean contains = false;
		DefaultMutableTreeNode projectNode = null;
		DefaultMutableTreeNode fileNode = null;

		for (int i = 0; i < top.getChildCount(); i++) {
			projectNode = (DefaultMutableTreeNode)(top.getChildAt(i));
			if (projectNode.toString().equals((String)projectName)) {
				contains = true;
				break;
			}
		}
		if (!contains) {
			return;
		}
		contains = false;
		for (int i = 0; i < projectNode.getChildCount(); i++) {
			fileNode = (DefaultMutableTreeNode)(projectNode.getChildAt(i));
			if (fileNode.toString().equals((String)fileName)) {
				contains = true;
				break;
			}
		}
		if (!contains) {
			return;
		}
		treeModel.removeNodeFromParent((DefaultMutableTreeNode)fileNode);
	}


	/**
	 * Metoda brise projekt, ako takav projekt uopce postoji
	 */
	public void removeProject(String projectName) {
		boolean contains = false;
		DefaultMutableTreeNode projectNode = null;

		for (int i = 0; i < top.getChildCount(); i++) {
			projectNode = (DefaultMutableTreeNode)(top.getChildAt(i));
			if (projectNode.toString().equals((String)projectName)) {
				contains = true;
				break;
			}
		}
		if (!contains) {
			return;
		}
		treeModel.removeNodeFromParent((DefaultMutableTreeNode)projectNode);
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
			parent.add(addChildren(new DefaultMutableTreeNode(child), hierarchy));
		}
		return parent;
	}


	/**
	 * Metoda dodaje novi projekt u projectExplorer
	 * 
	 * @param projectName ime projekta
	 */
	public void addProject(String projectName) {
		DefaultMutableTreeNode projectNode = null;
		Hierarchy hierarchy = null;

		// dodaj novi cvor, tj. projekt u stablo
		if (!containsNode(top, projectName)) {
			projectNode = new DefaultMutableTreeNode(projectName);
			this.top.add(projectNode);
		} else {
			// mora se inicijalizirat projectNode!!
		}
		// dohvaca sve root cvorove tog projekta
		try {
			hierarchy = projectContainer.extractHierarchy(projectName);
		} catch (UniformAppletException e) {
			;
		}
		DefaultMutableTreeNode rootNode = null;
		for (String string : hierarchy.getRootNodes()) {
			if (!containsNode(projectNode, string)) {
				rootNode = new DefaultMutableTreeNode(string);
				projectNode.add(rootNode);
			}
			addChildren(rootNode, hierarchy);
		}
		tree.setRootVisible(true);
	}


	/**
	 * Metoda postavlja aktivni projekt
	 */
	public void setActiveProject(String projectName) {
		this.projectName = projectName;
	}


	/**
	 * Metoda dodaje novi cvor u stablo
	 * 
	 * @param novi objekt u stablu
	 */
	private DefaultMutableTreeNode addObject(Object child) {
		DefaultMutableTreeNode projectNode = null;
		Object[] nodes = null;

		TreePath treePath = tree.getSelectionPath();
		if (treePath == null) {
			// ako nema selekcije
			return null;
		} else {
			nodes = treePath.getPath();
			projectNode = (DefaultMutableTreeNode)nodes[1];
		}

		return addObject(projectNode, child, true);
	}


	private DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child,
			boolean shouldBeVisible) {

		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);

		if (containsNode(parent, child)) {
			return childNode;
		}

		treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
		if (shouldBeVisible) {
			tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		}
		return childNode;
	}


	private void removeNode() {
		Object node = null;

		TreePath treePath = tree.getSelectionPath();
		if (treePath == null) {
			// ako nema selekcije
			return;
		} else {
			node = treePath.getLastPathComponent();
			treeModel.removeNodeFromParent((DefaultMutableTreeNode)node);
		}
	}


	/**
	 * Metoda provjerava djeca ovog roditelja, u ovom slucaju String objekt
	 */
	public boolean containsNode(DefaultMutableTreeNode parent, Object child) {
		DefaultMutableTreeNode temp = null;

		if (parent == null) {
			return false;
		}

		// provjera postoji li vec taj cvor
		for (int i = 0; i < parent.getChildCount(); i++) {
			temp = (DefaultMutableTreeNode)(parent.getChildAt(i));
			if (temp.toString().equals((String)child)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Metoda stvara used in relation stablo
	 */
	private void createUsedInTree() {
		// create used in relation tree
		root = new DefaultMutableTreeNode("Projekti");
		for (int i = 0; i < top.getChildCount(); i++) {
			root.add(new DefaultMutableTreeNode(((DefaultMutableTreeNode)(top
					.getChildAt(i))).toString()));
		}
		for (int i = 0; i < root.getChildCount(); i++) {
			transformTree((DefaultMutableTreeNode)(top.getChildAt(i)),
					(DefaultMutableTreeNode)(root.getChildAt(i)));
		}
	}


	/**
	 * Metoda stvara novo stablo iz danog stabla, s tim da je poredak obrnut
	 * View by 'used in' relation. Prvi
	 * 
	 * @param parent korijenski cvor, tj. sam projekt
	 * @param usedInTree novo podstablo
	 * @return DefaultMutableTreeNode vraca podstablo
	 */
	public void transformTree(DefaultMutableTreeNode parent,
			DefaultMutableTreeNode usedInTree) {

		if (parent == null) {
			return;
		}
		DefaultMutableTreeNode child;
		DefaultMutableTreeNode tempNode;
		DefaultMutableTreeNode[] tempNodes;
		boolean isUsed = true;
		;
		for (int i = 0; i < parent.getChildCount(); i++) {
			child = (DefaultMutableTreeNode)parent.getChildAt(i);
			isUsed = true;
			if (child.isLeaf()) {
				// uzmi sve pretke, ukljucujuci s trenutnim cvorom
				TreeNode[] nodes = child.getPath();
				tempNodes = new DefaultMutableTreeNode[nodes.length];
				// ako postoje samo root, project i trenutni cvor
				if (nodes.length <= 3) {
					isUsed = false;
				} else {
					// stvaranje novih cvorova
					for (int j = tempNodes.length - 2; j >= 2; j--) {
						tempNodes[j] = new DefaultMutableTreeNode(nodes[j].toString());
					}
					// stvaranje podstabla
					for (int j = tempNodes.length - 2; j >= 3; j--) {
						tempNodes[j].add(tempNodes[j - 1]);
					}
				}
				// ako taj cvor postoji u 'used in' podstablu
				tempNode = searchTree(usedInTree, child);
				if (tempNode != null) {
					if (isUsed) {
						tempNode.add(tempNodes[tempNodes.length - 2]);
					}
				} else {
					DefaultMutableTreeNode temp = new DefaultMutableTreeNode(child
							.toString());
					if (isUsed) {
						temp.add(tempNodes[tempNodes.length - 2]);
					}
					usedInTree.add(temp);
				}
			} else {
				transformTree(child, usedInTree);
			}
		}
	}


	/**
	 * Metoda koje pretrazuje stablo.
	 * 
	 * @param parent stablo koje se pretrazuje
	 * @param node cvor koji se tazi
	 * @return DefaultMutableTreeNode cvor, ako je naden
	 */
	public DefaultMutableTreeNode searchTree(DefaultMutableTreeNode parent,
			DefaultMutableTreeNode node) {

		if (parent == null) {
			return null;
		}
		if (parent.toString().equals(node.toString())) {
			return parent;
		}

		DefaultMutableTreeNode tempNode;
		for (int i = 0; i < parent.getChildCount(); i++) {
			tempNode = searchTree((DefaultMutableTreeNode)(parent.getChildAt(i)), node);
			if (tempNode != null) {
				return tempNode;
			}
		}
		return null;
	}

	public void refreshProject(String projectName) {
		addProject(projectName);	
	}
}