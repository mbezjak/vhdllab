package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.server.api.StatusCodes;

import java.lang.reflect.Method;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;

import org.apache.log4j.Logger;

/**
 * A utility class for dao tier. Contains common methods used in dao tier.
 * 
 * @author Miro Bezjak
 * @since 6/2/2008
 * @version 1.0
 */
public class DAOUtil {

	/**
	 * A log instance for this class.
	 */
	private static final Logger log = Logger.getLogger(DAOUtil.class);

	/**
	 * Extracts a database column length specified via ejb annotations. If
	 * column lenght wasn't specified for <code>property</code> then this
	 * method will throw {@link IllegalArgumentException}.
	 * 
	 * @param clazz
	 *            a class of an entity to retrieve column length for
	 * @param property
	 *            a name of a property (field) for whom to retrieve column
	 *            length for
	 * @return a column length for specified <code>clazz</code> and
	 *         <code>property</code>
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>property</code> is an empty string or length
	 *             information can't be retrieved for specified
	 *             <code>property</code>
	 * @throws DAOException
	 *             if any error occurs during retrieving column length
	 */
	public static int columnLengthFor(Class<?> clazz, String property)
			throws DAOException {
		if (clazz == null) {
			throw new NullPointerException("Class cant be null");
		}
		if (property == null) {
			throw new NullPointerException("Property cant be null");
		}
		if (property.equals("")) {
			throw new IllegalArgumentException("Property cant be empty string");
		}
		// first try AttributeOverrides annotation
		AttributeOverrides overrides = clazz
				.getAnnotation(AttributeOverrides.class);
		if (overrides != null) {
			for (AttributeOverride o : overrides.value()) {
				if (o.name().equals(property)) {
					return o.column().length();
				}
			}
		}
		// else search through AttributeOverride annotation
		AttributeOverride override = clazz
				.getAnnotation(AttributeOverride.class);
		if (override != null && override.name().equals(property)) {
			return override.column().length();
		}
		// else see Colum annotation in getter for a property
		String getter = "get"
				+ String.valueOf(property.charAt(0)).toUpperCase()
				+ property.substring(1);
		Method method;
		try {
			method = clazz.getMethod(getter);
		} catch (Exception e) {
			log.fatal("Can't retrieve column length for class "
						+ clazz.getCanonicalName() + " and property "
						+ property, e);
			throw new DAOException(StatusCodes.SERVER_ERROR, e);
		}
		Column column = method.getAnnotation(Column.class);
		if (column != null) {
			return column.length();
		}
		// else throw exception
		throw new IllegalArgumentException("No length defined for property: "
				+ property);
	}

}
