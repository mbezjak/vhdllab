/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.schema2.enums;

import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.TimeFormatException;

/**
 * Metrika vremena.
 * 
 * @author Axel
 * 
 */
public enum ETimeMetrics {
	// bitan redoslijed, ne mijenjati, inace ce bit svasta!!!
	femto() {
		@Override
		public String toString() {
			return "fs";
		}
	},
	pico() {
		@Override
		public String toString() {
			return "ps";
		}
	},
	nano() {
		@Override
		public String toString() {
			return "ns";
		}
	},
	micro() {
		@Override
		public String toString() {
			return "us";
		}
	},
	mili() {
		@Override
		public String toString() {
			return "ms";
		}
	},
	sec() {
		@Override
		public String toString() {
			return "s";
		}
	};

	private static String[] metrics = new String[] { "s", "ms", "us", "ns",
			"ps", "fs" };

	/**
	 * Vraca omjer ove metrike i metrike koja je parametar.
	 * 
	 * @return Omjer metrika, npr.:
	 *         ETimeMetrics.mili.getRatio(ETimeMetrics.micro) == 1000 je izraz
	 *         koji se evaluira u true.
	 */
	public double getRatio(ETimeMetrics rhs) {
		if (rhs == this)
			return 1.f;

		int state = 0;
		double factor = 1.f;

		for (ETimeMetrics metric : ETimeMetrics.values()) {
			switch (state) {
			case 0:
				if (this == metric)
					state = 1;
				else if (rhs == metric)
					state = 2;
				break;
			case 1:
				factor /= 1000;
				if (rhs == metric)
					return factor;
				break;
			case 2:
				factor *= 1000;
				if (this == metric)
					return factor;
				break;
			}
		}
		return factor;
	}

	public static ETimeMetrics parseMetric(String metric) {
		if (metric.equals("fs"))
			return ETimeMetrics.femto;
		else if (metric.equals("ps"))
			return ETimeMetrics.pico;
		else if (metric.equals("ns"))
			return ETimeMetrics.nano;
		else if (metric.equals("us"))
			return ETimeMetrics.micro;
		else if (metric.equals("ms"))
			return ETimeMetrics.mili;
		else if (metric.equals("s"))
			return ETimeMetrics.sec;
		else
			throw new TimeFormatException("Metric '" + metric
					+ "' is unimplemented.");
	}

	/**
	 * Dohvaca array mogucih vremenskih metrika
	 * 
	 * @return vremenske metrike
	 */
	public static String[] getAllTimes() {
		return metrics;
	}

}
