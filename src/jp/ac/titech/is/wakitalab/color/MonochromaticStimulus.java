package jp.ac.titech.is.wakitalab.color;

/**
 * Representation of a monochromatic stimulus.
 *
 * $Id: MonochromaticStimulus.java,v 1.3 2003/11/26 13:00:39 wakita Exp $
 * @author Ken Wakita
 * @version Experimental, Jan 19, 2003
 */

public class MonochromaticStimulus extends Stimulus {
    protected int waveLength;
    protected double radiance;

    public class Iterator implements Stimulus.Iterator {
	boolean hasNext = true;
	Iterator() { }
	public boolean hasNext() { return hasNext; }
	public double next() { hasNext = false; return radiance; }
    }

    /**
     * Construct a monochromatic stimulus of a given wavelength and
     * radiance.
     *
     * @param waveLength The wave length of the monochromatic
     * stimulus.
     *
     * @param radiance The radiance of the monochromatic stimulus.
     */

    public MonochromaticStimulus(int waveLength, double radiance) {
	this.waveLength = waveLength; this.radiance = radiance;
    }

    /**
     * Radiance of stimulus at the given wave length.  The radiance is
     * zero for all the wave length's other than for the wave length
     * of this (monochromatic) stimulus.
     *
     * @param l Wave length (nano meter)
     * @return Relative radiance of the stimulus at wave length l.
     */

    public double radiance(int l) { return waveLength == l ? radiance : 0; }

    public Stimulus.Iterator iterator() { return new Iterator(); }
}
