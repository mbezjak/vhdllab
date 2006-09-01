package hr.fer.zemris.ajax.shared;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * This class contains various methods needed for the conversion of a Properties
 * object to String and vice versa.
 * 
 * @author marcupic
 *
 */
public class XMLUtil {
	
	/**
	 * Conversion of Properties object into String. In case of exception
	 * during conversion empty string is returned.
	 * 
	 * @param p properties to serialize into String
	 * @return String representation of properties object
	 */
	public static String serializeProperties(Properties p) {
		if(p==null) throw new NullPointerException("Properties must not be null!");
		StringBuilder sb = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			p.storeToXML(bos,null,"UTF-8");
			bos.flush();
			byte[] data = bos.toByteArray();
			// Simpler way for the monster code below:
			// return new String(data,"UTF-8");
			// However, since I already wrote it, I'll leave it here...
			BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data),"UTF-8"));
			sb = new StringBuilder(data.length);
			char[] buf = new char[1024];
			while(true) {
				int rd = br.read(buf);
				if(rd<1) break;
				sb.append(buf,0,rd);
			}
		} catch(Exception ex) {
			sb.setLength(0);
			ex.printStackTrace();
		}
		return sb==null ? "" : sb.toString();
	}

	/**
	 * Convert string representation of Properties object into the object itself.
	 * In case of exception, null is returned.
	 * @param data String representation of properties
	 * @return Properties object recovered from string
	 */
	public static Properties deserializeProperties(String data) {
		Properties p = new Properties();
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data.getBytes("UTF-8"));
			p.loadFromXML(bis);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return p;
	}

	/**
	 * Helper mehod that reads whole InputStream and converts its content into
	 * String. For byte interpretation default encoding is used. 
	 * @param is input stream
	 * @return string obtained from input stream
	 */
	public static String inputStreamAsText(InputStream is) {
		StringBuilder sb = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			sb = new StringBuilder(4096);
			char[] buf = new char[1024];
			while(true) {
				int rd = br.read(buf);
				if(rd<1) break;
				sb.append(buf,0,rd);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return sb==null ? "" : sb.toString();
	}

	/**
	 * Helper mehod that reads whole InputStream and converts its content into
	 * String. For byte interpretation specified encoding is used (e.g. "UTF-8"). 
	 * @param is input stream
	 * @param encoding which encoding to use for byte interpretation
	 * @return string obtained from input stream
	 */
	public static String inputStreamAsText(InputStream is, String encoding) {
		StringBuilder sb = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is,encoding));
			sb = new StringBuilder(4096);
			char[] buf = new char[1024];
			while(true) {
				int rd = br.read(buf);
				if(rd<1) break;
				sb.append(buf,0,rd);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return sb==null ? "" : sb.toString();
	}
}