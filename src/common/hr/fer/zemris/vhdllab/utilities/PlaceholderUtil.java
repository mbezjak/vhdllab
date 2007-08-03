package hr.fer.zemris.vhdllab.utilities;

/**
 * Helper class for dealing with placeholders. Look at
 * <code>Client_Main_ApplicationResources_en.properties</code> file in
 * src/i18n/client source folder to learn what placeholders are.
 * 
 * @author Miro Bezjak
 */
public class PlaceholderUtil {

	/**
	 * Replace placeholders in <code>message</code> with
	 * <code>replacements</code> string. Look at
	 * <code>Client_Main_ApplicationResources_en.properties</code> file in
	 * src/i18n/client source folder to learn what placeholders are.
	 * 
	 * @param message
	 *            a message from where to replace placeholders
	 * @param replacements
	 *            an array of string to replace placeholders
	 * @return modified message
	 */
	public static String replacePlaceholders(String message,
			String[] replacements) {
		if (replacements == null)
			return message;
		String replaced = message;
		int i = 0; // placeholders starts with 0
		for (String s : replacements) {
			replaced = replaced.replace("{" + i + "}", s);
			i++;
		}
		return replaced;
	}

}
