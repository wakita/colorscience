package jp.ac.titech.is.wakitalab.color.smartcolor;

/**
 * Gray class: L* component of CIELAB representation for a monochromatic color.
 * 
 * $Id: GrayCoordination.java,v 1.5 2003/11/26 15:16:23 wakita Exp $
 * Created on 2003/09/22
 * @author Ken Wakita
 */

public class GrayCoordination {
	
	// private java.util.Random random = new java.util.Random();
		
	private int size;
	private Gray gray[];
		
	GrayCoordination(int n) {
		size = n;
		gray = new Gray[n];
		for (int i = 0; i < n; i++)
			gray[i] = new Gray();
	}
	
	public int size() {
		return size;
	}

	public Gray get(int i) {
		assert i >= 0 && i < size;
		return gray[i];
	}
	
	public double lightness(int i) {
		assert i >= 0 && i < size;
		return gray[i].L;
	}
	
	public void set(int i, double g) {
		assert i >= 0 && i < size;
		gray[i].set(g);
	}
	
	public void set(int i, Gray g) {
		assert i >= 0 && i < size;
 		gray[i] = g;
	}
	
	public void setLightness(int i, double g) {
		assert i >= 0 && i < size;
		gray[i].set(g);
	}
	
	public double distance(int i, int j) {
		return get(i).distance(get(j));
	}
/*
	public void add(int i, Gray g) {
		assert i >= 0 && i < size;
		Gray[i] += v;
	}
*/
}
