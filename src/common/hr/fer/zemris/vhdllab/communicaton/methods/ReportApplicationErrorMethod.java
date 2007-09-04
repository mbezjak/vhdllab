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
public class ReportApplicationErrorMethod extends AbstractMethod<Void> {

	public ReportApplicationErrorMethod(String content) {
		super("report.application.error");
		setParameter(PROP_FILE_CONTENT, content);
	}

}
