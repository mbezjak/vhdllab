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

	public ReportApplicationErrorMethod(String userId, String content) {
		super("report.application.error");
		setParameter(PROP_USER_ID, userId);
		setParameter(PROP_FILE_CONTENT, content);
	}

}
