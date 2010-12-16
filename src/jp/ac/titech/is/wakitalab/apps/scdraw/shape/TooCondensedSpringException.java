package jp.ac.titech.is.wakitalab.apps.scdraw.shape;

@SuppressWarnings("serial")
public class TooCondensedSpringException extends IllegalArgumentException {
	   int height, thickness, lines;

	   public TooCondensedSpringException(int height, int thickness, int lines) {
	       this.height = height;
	       this.thickness = thickness;
	       this.lines = lines;
	   }
	   
	   public String toString() {
	       return String.format("TooCondensedSpringException(height = %d, thickness = %d, #lines = %d)", height, thickness, lines);
	   }
	}
