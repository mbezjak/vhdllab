package hr.fer.zemris.vhdllab.applets.schema2.misc.xml;




public final class XMLTree {
	
	/* static fields */

	/* private fields */
	// TODO xml metadata
	private XMLNode root;
	
	
	
	/* ctors */
	
	public XMLTree() {
		root = null;
	}
	
	public XMLTree(XMLNode rootNode) {
		root = rootNode;
	}
	

	
	/* methods */
	
	public final void setRoot(XMLNode root) {
		this.root = root;
	}

	public final XMLNode getRoot() {
		return root;
	}

}

















