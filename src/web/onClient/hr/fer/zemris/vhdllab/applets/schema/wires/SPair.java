package hr.fer.zemris.vhdllab.applets.schema.wires;

public class SPair<T> {
	public T first;
	public T second;
	
	SPair() {
	}

	public SPair(T first, T second) {
		super();
		this.first = first;
		this.second = second;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof SPair)) return false;
		SPair<T> rhs = (SPair<T>)arg0;
		return this.first.equals(rhs) && this.second.equals(rhs);
	}

	@Override
	public int hashCode() {
		return first.hashCode() + second.hashCode();
	}
	
	
}
