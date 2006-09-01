package hr.fer.zemris.vhdllab.vhdl.tb;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

/**
 * Thrown to indicate that <code>CircuitInterface</code>
 * and <code>Generator</code> are not compatible.
 *
 * @author  Miro Bezjak
 * @see CircuitInterface#isCompatible(Generator)
 * @see Generator#isCompatible(CircuitInterface)
 */
public class IncompatibleDataException extends Exception {

	private static final long serialVersionUID = -7658286609142394319L;

	/**
     * Constructs an <code>IncompatibleDataException</code> with no 
     * detail message. 
     */
	public IncompatibleDataException() {
		super();
	}

	/**
     * Constructs an <code>IncompatibleDataException</code> with the 
     * specified detail message. 
     *
     * @param s the detail message.
     */
	public IncompatibleDataException(String s) {
		super(s);
	}
}
