package hr.fer.zemris.vhdllab.api;

import static org.junit.Assert.fail;

import hr.fer.zemris.vhdllab.api.StatusCodes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * A test case for {@link StatusCodes} class.
 * 
 * @author Miro Bezjak
 */
public class StatusCodesTest {

	/**
	 * Check that every field in {@link StatusCodes} is 'public static final
	 * short' and that no two fields contains the same value.
	 */
	@Test
	public void valuesAndTypes() throws Exception {
		Class<StatusCodes> clazz = StatusCodes.class;
		Set<Short> values = new HashSet<Short>();
		for (Field f : clazz.getDeclaredFields()) {
			int modifiers = f.getModifiers();
			if (!Modifier.isPublic(modifiers)) {
				fail("Field " + f.getName() + " is not public");
			}
			if (!Modifier.isStatic(modifiers)) {
				fail("Field " + f.getName() + " is not static");
			}
			if (!Modifier.isFinal(modifiers)) {
				fail("Field " + f.getName() + " is not final");
			}
			if (!f.getType().getCanonicalName().equals("short")) {
				fail("Field " + f.getName() + " is not of type short");
			}
			Short value = (Short) f.get(null);
			if (values.contains(value)) {
				fail("Duplicate value " + value + " for field " + f.getName());
			}
			values.add(value);
		}
	}

}
