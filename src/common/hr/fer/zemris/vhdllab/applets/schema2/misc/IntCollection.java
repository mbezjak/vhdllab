package hr.fer.zemris.vhdllab.applets.schema2.misc;

import java.util.Iterator;



public interface IntCollection {

	void clear();

	void reset();
	
	int size();

	int capacity();

	boolean empty();
	
	Iterator iterator();

}