package hr.fer.zemris.vhdllab.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility class for loading properties, filtering malicious path etc.
 * 
 * @author Miro Bezjak
 */
public final class FileUtil {

	/**
	 * Dont let anyone instantiate this class!
	 */
	private FileUtil() {
	}

	/**
	 * Reads complete file content in <code>UTF-8</code> encoding and returns
	 * it as a string. If any exception occurs then this method will return
	 * <code>null</code>.
	 * 
	 * @param fileName
	 *            a path to a file name
	 * @return content of a file
	 * @see #readFile(String, String)
	 */
	public static String readFile(String fileName) {
		return FileUtil.readFile(fileName, "UTF-8");
	}

	/**
	 * Reads complete file content in specified encoding and returns it as a
	 * string. If any exception occurs then this method will return
	 * <code>null</code>.
	 * 
	 * @param fileName
	 *            a path to a file name
	 * @param encoding
	 *            a file encoding
	 * @return content of a file
	 * @see #readFile(String)
	 */
	public static String readFile(String fileName, String encoding) {
		DataInputStream data = null;
		String content = null;
		try {
			data = new DataInputStream(new BufferedInputStream(
					new FileInputStream(fileName)));
			java.io.File file = new java.io.File(fileName);
			byte[] bytes = new byte[(int) file.length()];
			data.readFully(bytes);
			content = new String(bytes, encoding);
		} catch (Exception e) {
			return null;
		} finally {
			if (data != null) {
				try {
					data.close();
				} catch (Throwable ignore) {
				}
			}
		}
		return content;
	}

	/**
	 * Reads complete file content in UTF-8 encoding and returns it as a string.
	 * If any exception occurs then this method will return <code>null</code>.
	 * 
	 * @param is
	 *            an input stream of a file
	 * @return content of a file
	 * @see #readFile(String)
	 */
	public static String readFile(InputStream is) {
		return readFile(is, "UTF-8");
	}

	/**
	 * Reads complete file content in specified encoding and returns it as a
	 * string. If any exception occurs then this method will return
	 * <code>null</code>.
	 * 
	 * @param is
	 *            an input stream of a file
	 * @param encoding
	 *            a file encoding
	 * @return content of a file
	 * @see #readFile(String)
	 */
	public static String readFile(InputStream is, String encoding) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, encoding));
			String s;
			while ((s = reader.readLine()) != null) {
				sb.append(s);
				sb.append('\n');
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}