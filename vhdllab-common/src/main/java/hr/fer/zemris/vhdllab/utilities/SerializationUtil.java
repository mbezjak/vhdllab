/**
 * 
 */
package hr.fer.zemris.vhdllab.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Miro Bezjak
 *
 */
public class SerializationUtil {

	public static byte[] writeObject(Serializable s) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(100);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(s);
			byte[] array = bos.toByteArray();
			oos.close();
			return array;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object readObject(byte[] array) {
		ByteArrayInputStream bis = new ByteArrayInputStream(array);
		try {
			ObjectInputStream ois = new ObjectInputStream(bis);
			Object object = ois.readObject();
			ois.close();
			return object;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object serializeThenDeserializeObject(Serializable s) {
		return readObject(writeObject(s));
	}
	
}
