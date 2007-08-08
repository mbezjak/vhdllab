package hr.fer.zemris.vhdllab.applets.main.interfaces;

import javax.swing.JComponent;

public interface IIDResolver {

	String resolve(String originalId, JComponent component);
	
}
