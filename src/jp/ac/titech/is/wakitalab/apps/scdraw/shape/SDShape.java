package jp.ac.titech.is.wakitalab.apps.scdraw.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class SDShape implements Shape, Cloneable {
	Shape shape;
	abstract public SDShapeType getType();

	public boolean contains(Point2D p) {
		return shape.contains(p);
	}

	public boolean contains(Rectangle2D rect) {
		return shape.contains(rect);
	}

	public boolean contains(double x, double y) {
		return shape.contains(x, y);
	}

	public boolean contains(double x, double y, double w, double h) {
		return shape.contains(x, y, w, h);
	}

	public Rectangle getBounds() {
		return shape.getBounds();
	}

	public Rectangle2D getBounds2D() {
		return shape.getBounds2D();
	}

	public PathIterator getPathIterator(AffineTransform t) {
		return shape.getPathIterator(t);
	}

	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return shape.getPathIterator(at, flatness);
	}

	public boolean intersects(Rectangle2D rect) {
		return shape.intersects(rect);
	}

	public boolean intersects(double x, double y, double w, double h) {
		return shape.intersects(x, y, w, h);
	}
	
	public abstract void setBounds(int x, int y, int w, int h);
	
	public void reshape(Point p1, Point p2, boolean square) {
		int x = Math.min(p1.x, p2.x), y = Math.min(p1.y, p2.y);
		int w = Math.abs(p2.x - p1.x), h = Math.abs(p2.y - p1.y);
		if (square) {
			h = w = Math.max(w, h);
			x = p1.x < p2.x ? p1.x : p1.x - w;
			y = p1.y < p2.y ? p1.y : p1.y - w;
		}
		setBounds(x, y, w, h);
	}
	
	static private int newID = 0;
	protected int id = newID++;
	
	private Color lineColor, fillColor, designColor;

	protected static Color
		defaultLineColor = Color.BLACK,
		defaultFillColor = Color.ORANGE,
		defaultDesignColor = Color.BLUE;
	
	protected int lineWidth;
	protected static int defaultLineWidth = 2;
	
	SDShape() {
		lineColor = defaultLineColor;
		fillColor = defaultFillColor;
		designColor = defaultDesignColor;
		//realColor = defaultFillColor;
		lineWidth = defaultLineWidth;
	}
	
	public void draw(Graphics2D g) {
		g.setColor(lineColor);
		g.draw(shape);
	}
	
	public void fill(Graphics2D g) {
		g.setColor(fillColor);
		g.fill(shape);
	}
	
	public void drawTemporary(Graphics2D g) {
		g.setColor(designColor);
		g.draw(shape);
	}
	
	protected static final int HANDLE_SIZE = 3;
	
	Rectangle[] handles = new Rectangle[8];
	private void setHandle(int i, int x, int y) {
		Rectangle r = handles[i];
		r.setBounds(x - HANDLE_SIZE + 1, y - HANDLE_SIZE + 1, HANDLE_SIZE * 2, HANDLE_SIZE * 2);
	}
	
	public void paintHandle(Graphics2D g) {
		Rectangle b = getBounds();
		int w = b.width, h = b.height;
		setHandle(0, b.x, b.y);
		setHandle(0, b.x + w/2, b.y);
		setHandle(0, b.x + w, b.y);
		setHandle(0, b.x + w, b.y + h/2);
		setHandle(0, b.x + w, b.y + h);
		setHandle(0, b.x + w/2, b.y + h);
		setHandle(0, b.x, b.y + h);
		setHandle(0, b.x, b.y + h/2);
		
		for (Rectangle handle: handles) {
			g.fill(handle);
		}
	}
}
