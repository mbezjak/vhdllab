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
package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.SwingComponent;

import java.util.HashMap;
import java.util.Map;

import javax.swing.table.TableCellEditor;



/**
 * Model za "napredni" JTable, omogucuje dodavanje razlicitih
 * <code>TableCellEditor</code> za isti stupac u JTable
 * 
 */
public class RowEditorModel {
	private Map<Integer, TableCellEditor> data;

	public RowEditorModel() {
		data = new HashMap<Integer, TableCellEditor>();
	}

	public void addEditorForRow(int row, TableCellEditor e) {
		data.put(Integer.valueOf(row), e);
	}

	public void removeEditorForRow(int row) {
		data.remove(Integer.valueOf(row));
	}

	public TableCellEditor getEditor(int row) {
		return data.get(Integer.valueOf(row));
	}
}
