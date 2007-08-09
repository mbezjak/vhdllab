package hr.fer.zemris.vhdllab.applets.main.interfaces;

import javax.swing.JComponent;

public interface IIDResolver {

	String resolveFromComponent(String originalId, JComponent component);
	
	String resolveFromData(String originalId, Object... data);
	
}
