package hr.fer.zemris.vhdllab.applets.schema2.misc;



public class Rect2d {
	
	/* static fields */

	
	/* private fields */
	public int left, top, width, height;
	

	/* ctors */

	public Rect2d() {
	}
	
	public Rect2d(int xpos, int ypos, int wdt, int hgt) {
		left = xpos; top = ypos; width = wdt; height = hgt;
	}

	
	
	/* methods */
	
	public boolean in(int x, int y) {
		return x >= left && x < (left + width) && y >= top && y < (top + width);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Rect2d)) return false;
		Rect2d other = (Rect2d)obj;
		return other.left == this.left && other.top == this.top
			&& other.width == this.width && other.height == this.height;
	}
	
	@Override
	public int hashCode() {
		return this.left << 24 + this.top << 16 + this.width << 8 + this.height;
	}
	
	
	
	
}














