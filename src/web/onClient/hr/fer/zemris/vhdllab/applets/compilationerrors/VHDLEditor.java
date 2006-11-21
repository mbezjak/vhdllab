package hr.fer.zemris.vhdllab.applets.compilationerrors;

/**
 * Sucelje koje treba implementirati VHDL editor.  Metoda se pozicionira 
 * na liniju odredenu argumentom i highlighta tu liniju
 * 
 * @author Boris Ozegovic
 */
public interface VHDLEditor 
{
	public void highlightLine(int line);
}
