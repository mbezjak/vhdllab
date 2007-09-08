/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;
import hr.fer.zemris.vhdllab.communicaton.results.Void;

/**
 * @author Miro Bezjak
 *
 */
public final class ReportApplicationErrorMethod extends AbstractMethod<Void> {

	private static final long serialVersionUID = 1L;

	public ReportApplicationErrorMethod(String content) {
		super("report.application.error");
		setParameter(PROP_FILE_CONTENT, content);
	}

}
