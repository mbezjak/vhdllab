package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EParamTypes;

import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 * Factory class for Table row editors
 * 
 * @author Garfield
 * 
 */
public class DefaultTableRowEditorFactory {

	/**
	 * Cached editors for types
	 */
	private static Map<EParamTypes, DefaultCellEditor> cachedEditors = new HashMap<EParamTypes, DefaultCellEditor>(
			5);

	/**
	 * 
	 * Provides editors for param type
	 * 
	 * @param type
	 *            Type of component parameter
	 * @see hr.fer.zemris.vhdllab.applets.schema2.enums.EParamTypes
	 * @return instace of editor for type, null if no special editor is needed
	 */
	public static DefaultCellEditor getRowEditorForType(EParamTypes type) {
		DefaultCellEditor editor = null;

		if (!cachedEditors.containsKey(type)) {
			editor = createEditorForType(type);
			if (editor != null) {
				cachedEditors.put(type, editor);
			}
		} else {
			editor = cachedEditors.get(type);
		}

		return editor;
	}

	/**
	 * Dispatcher for new editor types
	 * 
	 * @param type
	 *            type
	 * @return new instance of editor
	 */
	private static DefaultCellEditor createEditorForType(EParamTypes type) {

		if (type == EParamTypes.BOOLEAN) {
			return createEditorForBooleanType();
		}

		return null;
	}

	/**
	 * Creates new instance of editor for boolean type
	 * 
	 * @return
	 */
	private static DefaultCellEditor createEditorForBooleanType() {
		JComboBox cb = new JComboBox();
		cb.addItem("true");
		cb.addItem("false");

		return new DefaultCellEditor(cb);
	}
}
