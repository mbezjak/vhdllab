/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.schema2.misc;



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














