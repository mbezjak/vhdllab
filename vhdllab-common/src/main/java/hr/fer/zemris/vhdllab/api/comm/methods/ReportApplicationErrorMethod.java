package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;
import hr.fer.zemris.vhdllab.api.comm.results.Void;

/**
 * @author Miro Bezjak
 *
 */
public final class ReportApplicationErrorMethod extends AbstractMethod<Void> {

	private static final long serialVersionUID = 1L;

	public ReportApplicationErrorMethod(String content, String userId) {
		super("report.application.error", userId);
		setParameter(PROP_FILE_CONTENT, content);
	}

}
