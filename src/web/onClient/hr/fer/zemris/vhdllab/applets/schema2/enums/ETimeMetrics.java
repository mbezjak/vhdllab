package hr.fer.zemris.vhdllab.applets.schema2.enums;


/**
 * Metrika vremena.
 * 
 * @author Axel
 *
 */
public enum ETimeMetrics {
	// bitan redoslijed, ne mijenjati, inace ce bit svasta!!!
	femto, pico, nano, micro, mili, sec;
	
	/**
	 * Vraca omjer ove metrike i metrike
	 * koja je parametar.
	 * @param second
	 * @return
	 * Omjer metrika, npr.:
	 * ETimeMetrics.mili.getRatio(ETimeMetrics.micro) == 1000
	 * je izraz koji se evaluira u true.
	 */
	public double getRatio(ETimeMetrics rhs) {
		if (rhs == this) return 1.f;
		
		int state = 0;
		double factor = 1.f;
		
		for (ETimeMetrics metric : ETimeMetrics.values()) {
			switch (state) {
			case 0:
				if (this == metric) state = 1;
				else if (rhs == metric) state = 2;
				break;
			case 1:
				factor /= 1000;
				if (rhs == metric) return factor;
				break;
			case 2:
				factor *= 1000;
				if (this == metric) return factor;
				break;
			}
		}
		return factor;
	}
	
	@Override
	public String toString() {
		switch (this) {
		case femto:
			return "fs";
		case pico:
			return "ps";
		case nano:
			return "ns";
		case micro:
			return "mics";
		case mili: 
			return "ms";
		case sec:
			return "s";
		default:
			return "undefined time type";
		}
	}
}
