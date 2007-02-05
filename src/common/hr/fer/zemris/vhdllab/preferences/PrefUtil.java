package hr.fer.zemris.vhdllab.preferences;

import java.awt.Color;

public class PrefUtil {

	private static final String ARRAY_SEPARATOR = ",";
	
	public static String serializeColor(Color c) {
		StringBuilder sb = new StringBuilder(12);
		sb.append(c.getRed()).append(ARRAY_SEPARATOR)
			.append(c.getGreen()).append(ARRAY_SEPARATOR)
			.append(c.getBlue());
		return sb.toString();
	}
	
	public static Color deserializeColor(String data) {
		String[] rgb = data.split(ARRAY_SEPARATOR);
		int r = Integer.parseInt(rgb[0]);
		int g = Integer.parseInt(rgb[1]);
		int b = Integer.parseInt(rgb[2]);
		return new Color(r, g, b);
	}
	
}
