package hr.fer.zemris.vhdllab.preferences.verifier;

import hr.fer.zemris.vhdllab.preferences.InputVerifier;

public class AlwaysTrueVerifier implements InputVerifier {

	public String getErrorMessage() {
		return null;
	}

	public boolean isCorrectInput(String input) {
		return true;
	}

}
