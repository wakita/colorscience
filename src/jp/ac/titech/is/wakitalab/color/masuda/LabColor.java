package jp.ac.titech.is.wakitalab.color.masuda;



public class LabColor extends MyColor {
	private double L, a, b;
	public LabColor(double L, double a, double b) {
		this.L = L; this.a = a; this.b = b;
	}
	
	public double L() {
		return L;
	}
	public double a() {
		return a;
	}
	public double b() {
		return b;
	}
	@Override
	public String toString() {
		return "(" + L() + "," + a() + "," + b() + ")";
	}
	@Override
	public XYZColor getXYZ() {
		throw new UnsupportedOperationException();
	}
}
