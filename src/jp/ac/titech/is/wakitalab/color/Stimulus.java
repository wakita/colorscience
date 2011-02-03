package jp.ac.titech.is.wakitalab.color;

/**
 * Abstract representation of photopic stimulus
 *
 * @version Experimental, Jan 19, 2003
 * $Id: Stimulus.java,v 1.3 2003/11/26 13:00:39 wakita Exp $
 * @author Ken Wakita
 */

public abstract class Stimulus {
	
	public static double lumensOfWatts(double w) {
		return 683 * w;
	}
	
	public static double wattsOfLumens(double l) {
		return l / 638;
	}

	interface Iterator {

		/**
		 * Returns <code>true</code> if the iteration has more
		 * elements.  In other words, returns <code>true</code> if
		 * <code>next</code> would return an element rather than
		 * throwing an exception.
		 */

		public boolean hasNext();

		/**
		 * Returns the next elements in the iteration.
		 * @return The next element in the iteration.
		 */

		public double next();
	}

	public abstract double radiance(int l);
}
