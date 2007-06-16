package hr.fer.zemris.vhdllab.applets.schema2.misc;

import hr.fer.zemris.vhdllab.applets.schema2.enums.ETimeMetrics;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;


/**
 * Wrapper za vrijeme (time) parametar.
 * 
 * @author Axel
 *
 */
public final class Time {
	private double timeInterval;
	private ETimeMetrics timeMetric;
	
	/**
	 * Defaultna vrijednost je
	 * 0, a metrika - sekunde.
	 *
	 */
	public Time() {
		timeInterval = 0;
		timeMetric = ETimeMetrics.sec;
	}
	
	/**
	 * Time(5, ETimeMetrics.pico)
	 * konstruira vremenski odsjecak
	 * od 5 ps.
	 * 
	 * @param timeVal
	 * @param metric
	 * Odreduje metriku (ps, ns, mics...)
	 */
	public Time(double timeVal, ETimeMetrics metric) {
		timeInterval = timeVal;
		timeMetric = metric;
	}
	
	/**
	 * Pretvara iz jedne metrike u drugu.
	 * 
	 * @param metric
	 */
	public final void convertMetric(ETimeMetrics metric) {
		if (metric != timeMetric) {
			double factor = this.timeMetric.getRatio(metric);
			this.timeInterval *= factor;
			this.timeMetric = metric;
		}
	}
	
	
	public static final Time parseTime(String code) {
		throw new NotImplementedException(); // TODO
	}

	/**
	 * Testira jednakost time objekta
	 * i nekog drugog objekta.
	 * Ako je drugi objekt time, ali nije 
	 * u istoj metrici, konverzija ce se automatski
	 * napraviti.
	 * 
	 */
	@Override
	public final boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Time)) return false;
		Time t_obj = (Time)obj;
		ETimeMetrics met = t_obj.timeMetric;
		t_obj.convertMetric(this.timeMetric);
		if (this.timeInterval == t_obj.timeInterval) {
			t_obj.convertMetric(met);
			return true;
		}
		t_obj.convertMetric(met);
		return false;
	}

	/**
	 * NE ovisi o metrici, vec samo
	 * o timeIntervalu.
	 * 
	 */
	@Override
	public final int hashCode() {
		double factor = this.timeMetric.getRatio(ETimeMetrics.femto);
		long time = (long)(timeInterval * factor);
		
		return (int)(time % Integer.MAX_VALUE);
	}

	@Override
	public final String toString() {
		return (long)this.timeInterval + " " + this.timeMetric.toString();  
	}
	
	
}
