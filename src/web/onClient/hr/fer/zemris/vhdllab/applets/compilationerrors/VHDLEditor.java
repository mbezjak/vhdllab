package hr.fer.zemris.vhdllab.applets.compilationerrors;

/**
 * Sucelje koje treba implementirati VHDL editor.  Pozivanjem metode highlightLine
 * VHDL editor pozicionira kursor na liniju odredenu argumentom i highlighta tu liniju
 * 
 * @author Boris Ozegovic
 */
public interface VHDLEditor 
{
	public void highlightLine(int line);
}
