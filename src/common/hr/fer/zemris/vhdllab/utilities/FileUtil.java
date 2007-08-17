package hr.fer.zemris.vhdllab.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

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

	/**
	 * Returns a properties object out of content from input stream
	 * <code>is</code>. This method will also close <code>is</code> when
	 * properties is created. If {@link IOException} occurs then this method
	 * will return <code>null</code>.
	 * 
	 * @param is
	 *            an input stream for properties file
	 * @return a properties object
	 * @throws NullPointerException
	 *             if <code>is</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the input stream contains a malformed Unicode escape
	 *             sequence.
	 */
	public static Properties getProperties(InputStream is) {
		if (is == null) {
			throw new NullPointerException("Input stream can not be null.");
		}
		Properties p = new Properties();
		try {
			p.load(is);
		} catch (IOException e) {
			return null;
		} finally {
			try {
				is.close();
			} catch (Throwable ignore) {
			}
		}
		return p;
	}

	/**
	 * Removes all <code>../</code> and <code>./</code> characters so that
	 * <code>path</code> could not point to higher hierarchical directory nor
	 * point to current directory. This method will also fix path so that it may
	 * be used as a relative path (never as absolute).
	 * <p>
	 * The result is a string:
	 * <ul>
	 * <li>without <code>../</code> characters in it.</li>
	 * <li>without <code>./</code> character in it.</li>
	 * <li>without <code>:</code> character in it.</li>
	 * <li>not starting with <code>/</code> character</li>
	 * </ul>
	 * 
	 * @param path
	 *            a path to a file
	 * @return a filtered path
	 */
	public static String filterMaliciousPath(String path) {
		path = path.replaceAll("\\.+/|:", "");
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		path = path.replaceAll("//", "/");
		return path;
	}

	/**
	 * Merges two paths into one. <code>endPath</code> is considered to be
	 * relative to <code>beginPath</code>. <code>endPath</code> will also
	 * be scanned and filtered for <code>..</code> special characters (among
	 * others) as described in {@link #filterMaliciousPath(String)} method.
	 * There is no need for <code>beginPath</code> to be absolute path and in
	 * that case merged path will be relative aswell.
	 * 
	 * @param beginPath
	 *            an path before <code>endPath</code>
	 * @param endPath
	 *            a path relative to <code>beginPath</code>
	 * @return merged paths
	 */
	public static String mergePaths(String beginPath, String endPath) {
		endPath = FileUtil.filterMaliciousPath(endPath);
		if (!beginPath.startsWith("/")) {
			beginPath += "/";
		}
		return beginPath + endPath;
	}
}