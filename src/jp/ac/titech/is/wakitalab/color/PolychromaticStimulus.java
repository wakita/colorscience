package jp.ac.titech.is.wakitalab.color;

/**
 * Representation of a polychromatic stimulus
 *
 * $Id: PolychromaticStimulus.java,v 1.4 2004/05/17 00:00:31 wakita Exp $
 * @author Ken Wakita
 * @version Experimental, Jan 19, 2003
 */

public class PolychromaticStimulus extends Stimulus {

	/** The lowest wavelength in the stimulus. */
	public final int low;

	/** The highest wavelength in the stimulus. */
	public final int high;

	/** Step of wave length that the stimulus is measured. */
	public final int step;

	/** Radiance of the stimulus for various wave length */
	private final double radiance[];

	/** An iterator over a collection of observed wave length. */

	public class Iterator implements Stimulus.Iterator {
		int l = low;
		Iterator() {
		}

		/**
		 * Returns <code>true</code> if the iteration has more
		 * elements.  In other words, returns <code>true</code> if
		 * <code>next</code> would return an element rather than
		 * throwing an exception.
		 *
		 * @return <code>true</code> if the iterator has more
		 * elements.
		 */

		public boolean hasNext() {
			return l <= high;
		}

		/**
		 * Returns the next elements in the iteration.
		 * @return The next element in the iteration.
		 * @throws NoSuchElementException Iteration has no more
		 * elements.
		 */

		public double next() {
			assert l >= low;
			assert l <= high;
			assert radiance.length == (high - low) / step + 1;
			if (l > high) throw new java.util.NoSuchElementException();
			double r = radiance[(l - low) / step];
			l += step;
			return r;
		}
	}

	/**
	 * Construct a polychromatic stimulus
	 *
	 * @param low The lowest wavelength in the stimulus.
	 * @param high The highest wavelength in the stimulus.
	 */

	public PolychromaticStimulus(int low, int high, int step, double radiance[]) {
        assert high > low;
        assert step > 0;
        assert(high - low) % step == 0;
        assert radiance.length == (high - low) / step + 1;
        this.low = low;
        this.high = high;
        this.step = step;
        this.radiance = radiance;
	}

	/** Radiance of the stimulus at the wave length of
	 * <code>l</code>.
	 */

	public double radiance(int l) {
		assert l >= low && l <= high;
		int i = (l - low) / step, mod = (l - low) % step;

		if (mod == 0)
			return radiance[i];

		return ((step - mod) * radiance[i] + mod * radiance[i + 1]) / step;
	}

	/** Returns an iterator over the stimulus from the lowest wave
	 * length to the highest.
	 */

	public Stimulus.Iterator iterator() {
		return new Iterator();
	}
}
