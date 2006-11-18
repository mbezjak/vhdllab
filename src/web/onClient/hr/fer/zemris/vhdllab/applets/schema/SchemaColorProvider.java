package hr.fer.zemris.vhdllab.applets.schema;

import java.awt.Color;
/**
 * Razred koji sadrzi sve konstante boja koje se dinamicki
 * loadaju iz repozitorija "Themes" 
 * 
 * @author Tommy
 *
 */
public class SchemaColorProvider {
	
	//Grid component
	public Color GRID_BG;
	public Color GRID_LINES;
	public Color LINE;
	
	//Menu component
	public Color MENU_BG;
	public Color MENU_TEXT;
	public Color MENU_OVER;
	
	//SchemeAdapter
	public Color ADAPTER_CONNECTION_POINT;
	public Color ADAPTER_LINE;
	
	public SchemaColorProvider(){
		//Grid component
		GRID_BG = Color.WHITE;
		GRID_LINES = Color.GRAY;
		LINE = Color.BLACK;
		
		//Menu component
		MENU_BG=Color.BLACK;
		MENU_TEXT=Color.CYAN;
		
		//SchemeAdapter
		ADAPTER_CONNECTION_POINT=Color.BLACK;
		ADAPTER_CONNECTION_POINT=Color.YELLOW;
	}
}
