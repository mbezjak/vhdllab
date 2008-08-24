package hr.fer.zemris.vhdllab.applets.editor.schema2.misc;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.ETimeMetrics;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.TimeFormatException;

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
	 * Defaultna vrijednost je 0, a metrika - sekunde.
	 * 
	 */
	public Time() {
		timeInterval = 0;
		timeMetric = ETimeMetrics.sec;
	}

	/**
	 * Time(5, ETimeMetrics.pico) konstruira vremenski odsjecak od 5 ps.
	 * 
	 * @param timeVal
	 * @param metric
	 *            Odreduje metriku (ps, ns, mics...)
	 */
	public Time(double timeVal, ETimeMetrics metric) {
		timeInterval = timeVal;
		timeMetric = metric;
	}

	public Time(Time other) {
		this.timeInterval = other.timeInterval;
		this.timeMetric = other.timeMetric;
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
		String[] sf = code.split(" ");
		Double val;
		ETimeMetrics metric;

		if (sf.length != 2)
			throw new TimeFormatException();

		try {
			val = Double.parseDouble(sf[0]);
		} catch (NumberFormatException nfe) {
			throw new TimeFormatException(nfe);
		}

		metric = ETimeMetrics.parseMetric(sf[1]);

		return new Time(val, metric);
	}

	/**
	 * Dohvaca brojcanu vrijednost vremena
	 * 
	 * @return broj
	 */
	public double getTimeInterval() {
		return timeInterval;
	}

	/**
	 * Dohvaca metricku vrijednost vremena
	 * 
	 * @return metrika
	 */
	public ETimeMetrics getTimeMetric() {
		return timeMetric;
	}

	/**
	 * Testira jednakost time objekta i nekog drugog objekta. Ako je drugi
	 * objekt time, ali nije u istoj metrici, konverzija ce se automatski
	 * napraviti.
	 * 
	 */
	@Override
	public final boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Time))
			return false;
		Time t_obj = (Time) obj;
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
	 * NE ovisi o metrici, vec samo o timeIntervalu.
	 * 
	 */
	@Override
	public final int hashCode() {
		double factor = this.timeMetric.getRatio(ETimeMetrics.femto);
		long time = (long) (timeInterval * factor);

		return (int) (time % Integer.MAX_VALUE);
	}

	@Override
	public final String toString() {
		return (double) this.timeInterval + " " + this.timeMetric.toString();
	}

}
