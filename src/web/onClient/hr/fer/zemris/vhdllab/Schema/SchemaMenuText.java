package hr.fer.zemris.vhdllab.Schema;

import java.util.ResourceBundle;

public class SchemaMenuText {
	public String FILE = "";
	public String FILE_NEW = "";
	public String FILE_SAVE = "";
	public String FILE_EXIT = "";
	public String FILE_DESC = "";
	public String FILE_NEW_DESC = "";
	public String FILE_SAVE_DESC = "";
	public String FILE_EXIT_DESC = "";
	
	
	public String EDIT = "";
	public String EDIT_CUT = "";
	public String EDIT_COPY = "";
	public String EDIT_PASTE = "";

	public String OPTION = "";
	public String HELP = "";
	
	

	
	public SchemaMenuText( ResourceBundle rb ){
		FILE=rb.getString("menu.file");
		FILE_DESC=rb.getString("menu.file.desc");
		FILE_NEW=rb.getString("menu.file.new");
		FILE_NEW_DESC=rb.getString("menu.file.new.desc");
		FILE_SAVE=rb.getString("menu.file.save");
		FILE_EXIT=rb.getString("menu.file.exit");

		EDIT=rb.getString("menu.edit");
		EDIT_CUT=rb.getString("menu.edit.cut");
		EDIT_COPY=rb.getString("menu.edit.copy");
		EDIT_PASTE=rb.getString("menu.edit.paste");

		OPTION=rb.getString("menu.option");
		HELP=rb.getString("menu.help");
		
		
	}
}
