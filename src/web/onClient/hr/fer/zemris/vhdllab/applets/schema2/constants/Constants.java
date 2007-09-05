package hr.fer.zemris.vhdllab.applets.schema2.constants;

import java.awt.Color;
import java.awt.Font;

public class Constants {

	public static final String PREDEFINED_FILENAME = "predefined.xml";
	public static final String USER_CATEGORY_NAME = "UserComponents";
	public static final String PREFERRED_NAME_SUFIX = "_instance";
	public static final String ALLOWED_SET_DIVIDER = "#";

	public static final int GRID_SIZE = 10;
	public static final int DEFAULT_SCHEMA_WIDTH = 500;
	public static final int TEXT_NORMAL_FONT_SIZE = 12;
	public static final int TEXT_SMALL_FONT_SIZE = 10;
	public static final String TEXT_FONT_NORMAL = "Serif";
	public static final String TEXT_FONT_CANVAS = "Arial";
	public static final Font FONT_CANVAS = new Font(TEXT_FONT_CANVAS, Font.PLAIN, 
			TEXT_NORMAL_FONT_SIZE);
	public static final Font FONT_CANVAS_SMALL = new Font(TEXT_FONT_CANVAS, Font.PLAIN,
			TEXT_SMALL_FONT_SIZE);
	
	public static final Color COLOR_TEXT_NORMAL = new Color(0, 0, 0);
	public static final Color COLOR_TEXT_FADED = new Color(180, 180, 180);

	public static class Categories {
		public static final String INOUT = "INOUT";
		public static final String OTHER = "OTHER";
	}

}
