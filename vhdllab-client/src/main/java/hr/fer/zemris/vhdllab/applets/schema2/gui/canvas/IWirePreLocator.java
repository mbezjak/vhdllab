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
package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaController;

import java.awt.Graphics2D;

public interface IWirePreLocator {

	public int getOrientation();

	public void setOrientation(int orientation);

	public int getX1();

	public void setX1(int x1);

	public int getX2();

	public void setX2(int x2);

	public int getY1();

	public void setY1(int y1);

	public int getY2();

	public void setY2(int y2);

	public void instantiateWire(ISchemaController controller,
			CriticalPoint wireBeginning, CriticalPoint wireEnding);

	public void draw(Graphics2D g);

	public boolean isWireInstance();

	public boolean isWireInstantiable();

	public void setWireInstantiable(boolean wireInstantiable);

	public void setWireInstantiable(CriticalPoint wireBeginning,
			CriticalPoint wireEnding);

	public void revalidateWire();

}
