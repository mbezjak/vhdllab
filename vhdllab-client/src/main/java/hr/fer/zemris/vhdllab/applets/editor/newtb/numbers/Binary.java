package hr.fer.zemris.vhdllab.applets.editor.newtb.numbers;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.Messages;

public class Binary {
	/**
	 * Returns integer which is decimal representation of given binary represented number
	 * @param b String which is binary number, example: "1101"
	 * @return For given example, it would return 13
	 */
	public static int parseBinary(String b)
	{
		if(b == null || b.length() == 0)
			throw new NumberFormatException(Messages.noNumber);
		b = b.toUpperCase();
		try {
			int ret = 0;
			for(int i = 0; i < b.length(); i++, ret *= 2)
				if(b.charAt(i) != '1' && b.charAt(i)!='0')
					throw new Exception();
				else
					ret += b.charAt(i) - '0';
			return ret / 2;
		} catch (Exception e) {
			throw new NumberFormatException(Messages.wrongBinaryFormat);
		}
	}

}
