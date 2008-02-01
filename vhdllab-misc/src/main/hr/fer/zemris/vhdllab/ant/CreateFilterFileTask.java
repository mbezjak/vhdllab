package hr.fer.zemris.vhdllab.ant;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Hashtable;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Creates a file that is used a filter file to <code>replace</code> task. A
 * filter file will contain all properties accessed through
 * <code>getProject().getProperties()</code>. A prefix and sufix are used to
 * generate keys.
 * <p>
 * For example: if a property 'key = value' exists and both prefix and sufix are
 * '@@' then created filter file will contain a property '@@key@@ = value'.
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 1/10/2007
 */
public class CreateFilterFileTask extends Task {

	private String filterFile;
	private String prefix;
	private String sufix;

	/**
	 * Constructor.
	 */
	public CreateFilterFileTask() {
	}

	/**
	 * Returns a path to a filter file.
	 * 
	 * @return a path to a filter file.
	 */
	public String getFilterFile() {
		return filterFile;
	}

	/**
	 * Sets a path to a filter file.
	 * 
	 * @param filterFile
	 *            a path to a filter file
	 */
	public void setFilterFile(String filterFile) {
		this.filterFile = filterFile;
	}

	/**
	 * Returns a prefix that will be used on all properties.
	 * 
	 * @return a prefix that will be used on all properties
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Sets a prefix that will be used on all properties.
	 * 
	 * @param prefix
	 *            a prefix that will be used on all properties
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Returns a sufix that will be used on all properties.
	 * 
	 * @return a sufix that will be used on all properties
	 */
	public String getSufix() {
		return sufix;
	}

	/**
	 * Sets a sufix that will be used on all properties.
	 * 
	 * @param sufix
	 *            a sufix that will be used on all properties
	 */
	public void setSufix(String sufix) {
		this.sufix = sufix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
	public void execute() throws BuildException {
		super.execute();
		if (filterFile == null) {
			throw new BuildException("Filter file not set");
		}
		if (sufix == null && prefix == null) {
			throw new BuildException(
					"Either prefix or suffix (or both) has to be set");
		}
		writeFilterFile();
	}

	/**
	 * Creates and writes a filter file.
	 */
	@SuppressWarnings("unchecked")
	private void writeFilterFile() {
		File file = getProject().resolveFile(filterFile);
		// create a file if it doesn't exist
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new BuildException(e);
			}
		}
		// create a writer for filter file
		Writer writer;
		try {
			writer = new BufferedWriter(new FileWriter(file, false));
		} catch (Exception e) {
			throw new BuildException(e);
		}

		Hashtable<String, String> h = getProject().getProperties();
		for (Object o : h.keySet()) {
			String key = (String) o;
			if (prefix != null) {
				key = prefix + key;
			}
			if (sufix != null) {
				key = key + sufix;
			}
			String value = h.get(o);
			if (value.contains("\n")) {
				value = value.replace("\n", "\\n");
			}
			String property = key + " = " + value + "\n";
			try {
				writer.write(property);
			} catch (IOException e) {
				throw new BuildException(e);
			}
		}
		try {
			writer.close();
		} catch (IOException ignored) {
		}
	}

}
