package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars;

import hr.fer.zemris.vhdllab.applets.schema2.dummies.DummyOR;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;

import java.util.Set;

public class TestComponent {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DummyOR dummy_or = new DummyOR("dummy_or");
		IParameterCollection parameters = dummy_or.getParameters();

		Set<String> parametersName = parameters.getParameterNames();

		for (String s : parametersName) {
			System.out.println("Name:" + s);
		}

//		for (IParameter p : parameters) {
//			System.out.println(p.getName() + " -> " + p.getValueAsString());
//		}
	}
}
