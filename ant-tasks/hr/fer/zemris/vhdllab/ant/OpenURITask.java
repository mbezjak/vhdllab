package hr.fer.zemris.vhdllab.ant;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Openes a default browser to view specified <code>uri</code>.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 1/10/2007
 */
public class OpenURITask extends Task {

	private String uri;

	/**
	 * Constructor.
	 */
	public OpenURITask() {
	}

	/**
	 * Returns an URI that should be opened.
	 * 
	 * @return an URI that should be opened
	 */
	public String getURI() {
		return uri;
	}

	/**
	 * Sets an URI that should be opened.
	 * 
	 * @param uri
	 *            an URI that should be opened
	 */
	public void setURI(String uri) {
		this.uri = uri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
	public void execute() throws BuildException {
		/*
		 * This task uses Desktop class to open an URI
		 */
		super.execute();
		if (uri == null) {
			throw new BuildException("URL not set");
		}
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.browse(new URL(uri).toURI());
		} catch (MalformedURLException e) {
			throw new BuildException(e);
		} catch (IOException e) {
			throw new BuildException(e);
		} catch (URISyntaxException e) {
			throw new BuildException(e);
		}
	}

}
