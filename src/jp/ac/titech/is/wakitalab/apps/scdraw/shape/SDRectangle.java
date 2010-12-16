package jp.ac.titech.is.wakitalab.apps.scdraw.shape;

import java.awt.Point;
import java.awt.Rectangle;

public class SDRectangle extends SDShape {
	static private final SDShapeType type = SDShapeType.SDRectangle;

	public SDShapeType getType() {
		return type;
	}
	
	Rectangle nshape;  // Native representation of the rectangle
	
	public SDRectangle(Point p) {
		nshape = new Rectangle();
		shape = nshape;
		setLocation(p);
	}
	
	public void setBounds(int x, int y, int width, int height) {
		nshape.setBounds(x, y, width, height);
	}
	
	/*
	public void setBounds(Rectangle rect) {
		nshape.setBounds(rect);
	}
	*/
	
	void setLocation(int x, int y) {
		nshape.setLocation(x, y);
	}
	
	void setLocation(Point p) {
		nshape.setLocation(p);
	}
}
