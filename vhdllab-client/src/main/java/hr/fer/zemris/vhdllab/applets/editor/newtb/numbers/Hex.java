package hr.fer.zemris.vhdllab.applets.editor.newtb.numbers;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.Messages;

public class Hex {
	/**
	 * Same as parseBinary method, just for hex. Accepts both lowercase and uppercase
	 * @param g
	 * @throws NumberFormatException
	 */
	public static int parseHex(String g) throws NumberFormatException {
		if(g == null || g.length() == 0)
			throw new NumberFormatException(Messages.noNumber);
		g = g.toUpperCase();
		try {
			int ret = 0, mult = 1;
			for (int i = g.length() - 1; i >= 0; i--, mult *= 16)
			{
				char t = g.charAt(i);
				if(t > 'F' || (t < 'A' && !Character.isDigit(t)))
						throw new Exception();

				ret += mult
					* (t >= 'A' ? t - 55
							: t - 48);
			}
			return ret;
		} catch (Exception e) {
			throw new NumberFormatException(Messages.wrongHexFormat);
		}
	}

}
