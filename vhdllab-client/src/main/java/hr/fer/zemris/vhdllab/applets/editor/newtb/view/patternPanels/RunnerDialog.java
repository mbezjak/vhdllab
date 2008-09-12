package hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels;

import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.SignalChange;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.PatternDialog;

public class RunnerDialog {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//JOptionPane.showMessageDialog(null, EvaluationMethod.ParseInt);
		//JOptionPane.showMessageDialog(null, PatternDialog.getResultVector(4, 400).toString(), "OK!", JOptionPane.INFORMATION_MESSAGE);
		//JOptionPane.showMessageDialog(null, PatternDialog.getResultScalar(200).toString(), "OK!", JOptionPane.INFORMATION_MESSAGE);
		
		try {
			for(SignalChange sc : PatternDialog.getResultVector(4, 100).getChanges(10000)) {
				System.out.println(sc.toString());
			}
		} catch (UniformSignalChangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UniformPatternException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
