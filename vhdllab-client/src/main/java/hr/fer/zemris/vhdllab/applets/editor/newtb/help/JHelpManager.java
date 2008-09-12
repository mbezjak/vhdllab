package hr.fer.zemris.vhdllab.applets.editor.newtb.help;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author Davor Jurisic
 *
 */
public class JHelpManager extends JPanel implements TreeSelectionListener {
	private static final long serialVersionUID = 1L;
	private JEditorPane htmlPane;
    private JTree tree;
    
    private Map<String, TreePath> codeReferences = null;
    
    public JHelpManager(Document helpIndex) {
    	super(new BorderLayout());
    	
    	codeReferences = new HashMap<String, TreePath>();

        tree = new JTree(createHelpNodes(helpIndex));
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        tree.addTreeSelectionListener(this); 
        JScrollPane treeView = new JScrollPane(tree);

        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
        JScrollPane htmlView = new JScrollPane(htmlPane);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(htmlView);

        splitPane.setDividerLocation(220); 
        splitPane.setPreferredSize(new Dimension(700, 400));

        add(splitPane);
    }

    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if (node == null) return;
        displayURL(((HelpItem)node.getUserObject()).helpURL);
    }

    private class HelpItem {
        public String topicName = null;
        public URL helpURL = null;

        public HelpItem(String topicName, String helpURL) {
            this.topicName = topicName;
            if(helpURL != null) {
            	this.helpURL = getClass().getResource(helpURL);
            }
        }

        public String toString() {
            return topicName;
        }

		public String getTopicName() {
			return topicName;
		}

		public URL getHelpURL() {
			return helpURL;
		}
    }

    private void displayURL(URL url) {
        try {
            if (url != null) {
                htmlPane.setPage(url);
            } else {
            	htmlPane.setText("File Not Found");
            }
        } catch (IOException e) {
        	htmlPane.setText("File Not Found");
        }
    }
    
    private DefaultMutableTreeNode getGropuNodes(Node node, DefaultMutableTreeNode parent) {
    	String groupName = null;
    	String groupPage = null;
    	String helpCode = null;
    	
    	if(node.getAttributes().getNamedItem("groupName") != null) {
    		groupName = node.getAttributes().getNamedItem("groupName").getNodeValue();
    	}
    	if(node.getAttributes().getNamedItem("groupPage") != null) {
    		groupPage = node.getAttributes().getNamedItem("groupPage").getNodeValue();
    	}
    	if(node.getAttributes().getNamedItem("helpCode").getNodeValue() != null) {
    		helpCode = node.getAttributes().getNamedItem("helpCode").getNodeValue();
    	}
    	
    	if(groupName == null || groupName.length() == 0) {
    		groupName = "No name";
    	}
    	if(groupPage == null || groupPage.length() == 0) {
    		groupPage = null;
    	}
    	if(helpCode == null || helpCode.length() == 0) {
    		helpCode = null;
    	}
    	
    	DefaultMutableTreeNode returnTreeNode = new DefaultMutableTreeNode(new HelpItem(groupName, "resources/" + groupPage));
    	
    	if(parent != null) {
    		parent.add(returnTreeNode);
    	}
    	
    	if(helpCode != null) {
    		codeReferences.put(helpCode, new TreePath(returnTreeNode.getPath()));
    	}
    	
    	String topicName = null;
    	String helpPage = null;
    	DefaultMutableTreeNode helpTreeNode = null;
    	
    	for(int i = 0; i < node.getChildNodes().getLength(); i++) {
        	if(node.getChildNodes().item(i).getNodeName().equals("HelpGroup")) {
        		getGropuNodes(node.getChildNodes().item(i), returnTreeNode);
        	}
        	else if(node.getChildNodes().item(i).getNodeName().equals("HelpItem")) {
        		try
        		{
	        		topicName = node.getChildNodes().item(i).getAttributes().getNamedItem("topicName").getNodeValue();
	            	helpPage = node.getChildNodes().item(i).getAttributes().getNamedItem("helpPage").getNodeValue();
	            	helpCode = node.getChildNodes().item(i).getAttributes().getNamedItem("helpCode").getNodeValue();
        		} catch (Exception e) {
        			e.printStackTrace();
					continue;
				}
            	
            	if(topicName == null || topicName.length() == 0) {
            		continue;
            	}
            	if(helpPage == null || helpPage.length() == 0) {
            		continue;
            	}
            	if(helpCode == null || helpCode.length() == 0) {
            		helpCode = null;
            	}
            	
            	helpTreeNode = new DefaultMutableTreeNode(new HelpItem(topicName, "resources/" + helpPage));
            	returnTreeNode.add(helpTreeNode);
            	
            	if(helpCode != null) {
            		codeReferences.put(helpCode, new TreePath(helpTreeNode.getPath()));
            	}
        	}
        }
    	
    	return returnTreeNode;
    }

    private DefaultMutableTreeNode createHelpNodes(Document helpIndex) {
        DefaultMutableTreeNode noHelpItems = new DefaultMutableTreeNode(new HelpItem("No help items", null));
        
        Element rootElement = helpIndex.getDocumentElement();
        
        if(rootElement.getChildNodes().getLength() == 0) {
        	return noHelpItems;
        }
        
        for(int i = 0; i < rootElement.getChildNodes().getLength(); i++) {
        	if(rootElement.getChildNodes().item(i).getNodeName().equals("HelpGroup")) {
        		return getGropuNodes(rootElement.getChildNodes().item(i), null);
        	}
        }
        
        return noHelpItems;
    }
    
    public void openInitHelpPage() {
    	openHelpPage("ROOT");
    }
    
    public void openHelpPage(String helpCode) {
    	if(codeReferences.containsKey(helpCode)) {
    		tree.getSelectionModel().setSelectionPath(codeReferences.get(helpCode));
    	}
    	else {
    		displayURL(null);
    	}
    }
}
