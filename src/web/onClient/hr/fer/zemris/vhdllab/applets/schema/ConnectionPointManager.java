/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.schema;

import java.util.LinkedList;

/**
 * Ovo je razred koji ce se bavit svim "connection" tockama, tj. 
 * svaka fizicka komponenta ima svoje ulaze i izlaze koje treba 
 * spajat sa ostalima.
 *  
 * @author Tommy
 *
 */
public class ConnectionPointManager {
	private LinkedList<ConnectionPoint> points=new LinkedList<ConnectionPoint>();
	private SchemaDrawingAdapter adapter=null;
	
	public ConnectionPointManager(SchemaDrawingAdapter adapter){
		this.adapter=adapter;
	}
	
	public void addConnectionPoint(ConnectionPoint cp){
		points.add(cp);
	}
	
	public void updateDrawings(){
		//adapter.
	}
}
