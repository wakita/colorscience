package jp.ac.titech.is.wakitalab.apps.scdraw.shape;

import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class SDEllipse extends SDShape {
	static private final SDShapeType type = SDShapeType.SDRectangle;

	public SDShapeType getType() {
		return type;
	}
	
	Ellipse2D nshape;  // Native representation of the rectangle
	
	public SDEllipse(Point p) {
		nshape = new Ellipse2D.Double();
		nshape.setFrame(p.x, p.y, 0, 0);
		shape = nshape;
		setLocation(p);
	}
	
	public void setBounds(int x, int y, int width, int height) {
		nshape.setFrame(x, y, width, height);
	}
	
	/*
	public void setBounds(Rectangle rect) {
		nshape.setFrame(rect);
	}
	
	public void setBounds(Point p1, Point p2) {
		nshape.setFrame(new Rectangle(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y));
	}
	*/
	
	public void setLocation(int x, int y) {
		nshape.setFrame(x, y, nshape.getWidth(), nshape.getHeight());
	}
	
	public void setLocation(Point p) {
		nshape.setFrame(p.x, p.y, nshape.getWidth(), nshape.getHeight());
	}
}
