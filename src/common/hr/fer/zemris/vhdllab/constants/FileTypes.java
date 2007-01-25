package hr.fer.zemris.vhdllab.constants;

import java.util.ArrayList;
import java.util.List;

public class FileTypes {

	public static final String FT_VHDL_SOURCE = "vhdl_source";
	public static final String FT_VHDL_AUTOMAT = "vhdl_automat";
	public static final String FT_VHDL_STRUCT_SCHEMA = "vhdl_struct_schema";
	public static final String FT_VHDL_TB = "vhdl_tb";
	public static final String FT_VHDL_SIMULATION = "vhdl_simulation";

	public static final String FT_THEME = "theme";
	public static final String FT_APPLET = "applet";
	public static final String FT_COMMON = "common";

	public static boolean isSimulation(String type) {
		return type.equals(FileTypes.FT_VHDL_SIMULATION);
	}

	public static boolean isTestbench(String type) {
		return type.equals(FileTypes.FT_VHDL_TB);
	}

	public static boolean isCircuit(String type) {
		return type.equals(FileTypes.FT_VHDL_SOURCE) ||
				type.equals(FileTypes.FT_VHDL_STRUCT_SCHEMA) ||
				type.equals(FileTypes.FT_VHDL_AUTOMAT);
	}

	public static boolean isNotVHDL(String type) {
		return !(isSimulation(type) || isTestbench(type) || isCircuit(type));
	}

	public static List<String> values() {
		List<String> values = new ArrayList<String>();
		values.add(FileTypes.FT_VHDL_SOURCE);
		values.add(FileTypes.FT_VHDL_AUTOMAT);
		values.add(FileTypes.FT_VHDL_STRUCT_SCHEMA);
		values.add(FileTypes.FT_VHDL_TB);
		values.add(FileTypes.FT_VHDL_SIMULATION);
		values.add(FileTypes.FT_THEME);
		values.add(FileTypes.FT_APPLET);
		values.add(FileTypes.FT_COMMON);
		return values;
	}
}
