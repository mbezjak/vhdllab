package hr.fer.zemris.vhdllab.applets.schema;

import java.awt.Color;

public class SchemaColorProvider {
	
	//Grid component
	public Color GRID_BG;
	public Color GRID_LINES;
	public Color LINE;
	
	//Menu component
	public Color MENU_BG;
	public Color MENU_TEXT;
	public Color MENU_OVER;
	
	public SchemaColorProvider(){
		//Grid component
		GRID_BG = Color.white;
		GRID_LINES = Color.gray;
		LINE = Color.black;
		
		//Menu component
		MENU_BG=Color.black;
		MENU_TEXT=Color.CYAN;
		
	}
}
