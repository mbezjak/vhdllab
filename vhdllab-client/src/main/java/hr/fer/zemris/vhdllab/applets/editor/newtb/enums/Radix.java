package hr.fer.zemris.vhdllab.applets.editor.newtb.enums;

public enum Radix {
	Binary, Decimal, Hexadecimal;
	
	public  static int toInt(Radix radix) {
		switch(radix){
			case Binary:
				return 2;
			case Decimal:
				return 10;
			case Hexadecimal:
				return 16;
			default:
				return 0;
		}
	}
}
