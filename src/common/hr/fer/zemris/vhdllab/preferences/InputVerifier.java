package hr.fer.zemris.vhdllab.preferences;

public interface InputVerifier {

	boolean isCorrectInput(String input);
	String getErrorMessage();

}
