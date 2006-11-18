package hr.fer.zemris.vhdllab.applets.schema.components;

public class Ptr<T extends Object> {
	public T val;
	
	public Ptr() {
		val = null;
	}
	
	public Ptr(T v) {
		val = v;		
	}
}
