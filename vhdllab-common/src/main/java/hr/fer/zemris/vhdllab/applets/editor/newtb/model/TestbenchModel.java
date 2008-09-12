package hr.fer.zemris.vhdllab.applets.editor.newtb.model;

import hr.fer.zemris.vhdllab.applets.editor.newtb.listeners.TestbenchListener;

/**
 * 
 * @author Davor Jurisic
 *
 */
public interface TestbenchModel {
	
	public void addTestbenchListener(TestbenchListener l);
		
	public Testbench getTestbench();

	public void removeTestbenchListener(TestbenchListener l);
}
