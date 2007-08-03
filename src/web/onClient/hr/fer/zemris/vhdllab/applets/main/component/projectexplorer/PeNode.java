package hr.fer.zemris.vhdllab.applets.main.component.projectexplorer;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Klasa PeNode nasljeduje {@link DefaultMutableTreeNode} i overridea metode
 * equals i hashcode iz {@link DefaultMutableTreeNode} klase jer je nuzno i 
 * dovoljnjo da cvorovi imaju iste objekte, u slusaju {@link DefaultProjectExplorer} 
 * klase to je samo String.
 * 
 * @author Boris Ozegovic
 *
 */
public class PeNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2783311398917179649L;
	
	public PeNode(String string) {
		super(string);
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PeNode)) {
			return false;
		}
		if (this.getUserObject().equals(((PeNode)(obj)).getUserObject())) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return super.getUserObject().hashCode();
	}
}
