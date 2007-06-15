package hr.fer.zemris.vhdllab.applets.schema2.misc.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;


// TODO: add remove element
public final class XMLNode implements Iterable<XMLNode> {

	private class ChildrenIterator implements Iterator<XMLNode> {
		private int i = 0;
		public boolean hasNext() {
			return (i < size);
		}
		public XMLNode next() {
			if (i >= size) throw new NoSuchElementException();
			
			return children[i++];
		}
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	
	/* static fields */
	private static final int INITIAL_SIZE = 2;

	/* private fields */
	private String name;
	private String innerText;
	private HashMap<String, String> attributes;
	private XMLNode[] children;	
	private int size;
	private XMLNode parent;
	

	/* ctors */
	
	public XMLNode(String nodeName) {
		name = nodeName;
		innerText = "";
		children = new XMLNode[INITIAL_SIZE];
		attributes = new HashMap<String, String>();
		size = 0;
		parent = null;
	}
	
	public XMLNode(String nodeName, String xmlInnerText) {
		name = nodeName;
		innerText = (xmlInnerText != null) ? (xmlInnerText) : ("");
		children = new XMLNode[INITIAL_SIZE];
		attributes = new HashMap<String, String>();
		size = 0;
		parent = null;
	}
	
	
	

	/* methods */
	
	public final String name() {
		return name;
	}
	
	public final String getInnerText() {
		return innerText;
	}
	
	public final void setInnerText(String xmlInnerText) {
		innerText = (xmlInnerText != null) ? (xmlInnerText) : ("");
	}

	public final XMLNode[] getChildren() {
		return children;
	}
	
	public final void addXMLNode(XMLNode node) {
		if (size >= children.length) resize(2 * children.length);
		
		children[size++] = node;
	}
	
	public final String getAttribute(String key) {
		return attributes.get(key);
	}
	
	public final boolean hasAttribute(String key) {
		return attributes.containsKey(key);
	}
	
	public final void addAttribute(String key, String value) {
		attributes.put(key, value);
	}
	
	public final XMLNode getParent() {
		return parent;
	}
	
	public final void setParent(XMLNode nParent) {
		parent = nParent;
	}

	public Iterator<XMLNode> iterator() {
		return new ChildrenIterator();
	}
	
	
	/**
	 * Will lose elements if the new size is smaller
	 * than original.
	 * @param nsize
	 */
	private final void resize(int nsize) {
		XMLNode[] field = new XMLNode[nsize];
		
		for (int i = 0; i < nsize && i < size; i++) {
			field[i] = children[i];
		}
		
		children = field;
	}

}













