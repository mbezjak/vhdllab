package hr.fer.zemris.vhdllab.applets.editor.newtb.listeners;

/**
 * 
 * @author Davor Jurisic
 *
 */
public interface TestbenchListener {
	
	public void signalChanged(String signalName);
	
	public void signalAdded(String signalName);
	
	public void testBenchLengthChanged();
	
	public void simulationLengthChanged();
}
