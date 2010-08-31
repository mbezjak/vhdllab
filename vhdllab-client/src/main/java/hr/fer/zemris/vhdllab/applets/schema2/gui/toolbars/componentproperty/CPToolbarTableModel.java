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
package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty;

import javax.swing.table.DefaultTableModel;

public class CPToolbarTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 7420025038262028216L;

	/**
	 * Kolekcija parametara prikladnih za TableModel
	 */
	private CPToolbarParameterEnvelopeCollection pec = null;

	public CPToolbarTableModel(CPToolbarParameterEnvelopeCollection pec) {
		if (pec == null)
			throw new IllegalArgumentException(
					"CPToolbarParameterEnvelopeCollection nemoze biti null!");
		this.pec = pec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		if (pec != null) {
			return pec.getNumberOfColumns();
		}

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		if (pec != null) {
			return pec.getNumberOfRows();
		}

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int row, int column) {
		if (pec != null) {
			return pec.getValueAt(row, column);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 0) {
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#setValueAt(java.lang.Object,
	 *      int, int)
	 */
	@Override
	public void setValueAt(Object value, int row, int column) {
		if (pec != null) {
			pec.setValueAt(row, column, value);
		} else {
			if (CPToolbar.DEBUG_MODE) {
				System.err
						.println("CPToolbarTableModel: trying to set while CPToolbarParameterEnvelopeCollection is null");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		if (column < 2) {
			return CPToolbar.TOOLBAR_TABLE_HEADER[column];
		}

		return super.getColumnName(column);
	}

}
