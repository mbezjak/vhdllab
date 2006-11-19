/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;

import java.util.LinkedList;

/**
 * Ovo je razred koji ce se bavit svim "connection" tockama, tj. 
 * svaka fizicka komponenta ima svoje ulaze i izlaze koje treba 
 * spajat sa ostalima.
 *  
 * @author Tommy
 *
 */
public class SchemaConnectionPointManager {
	private LinkedList<SchemaConnectionPoint> points=new LinkedList<SchemaConnectionPoint>();
	private SchemaDrawingAdapter adapter=null;
	
	public SchemaConnectionPointManager(SchemaDrawingAdapter adapter){
		this.adapter=adapter;
	}
	
	public void addConnectionPoint(SchemaConnectionPoint cp){
		points.add(cp);
	}
	
	public void updateDrawings(){
		//adapter.
	}
}
