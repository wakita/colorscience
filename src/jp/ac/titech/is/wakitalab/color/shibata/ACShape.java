package jp.ac.titech.is.wakitalab.color.shibata;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.*;

import javax.swing.*;

import java.util.*;

/* 図形全般のクラス */

/* *********************** 図形のテンプレート *********************** */
abstract class ACShape implements Cloneable {
	protected Shape shape;			/* 図形の実体 */
	protected boolean isSelected;	/* 選択されているかどうか */
	protected Point claspPoint;	/* ばねの線とつなぐ点 */
	protected Point connectPoint;	/* 鍵の線とつなぐ点 */
	protected ColorLock key;		/* この図形をロックする鍵 */
	protected boolean isLocked;	/* ロックされているかどうか. デバッグ用 */
	
	/* 図形のタイプを表す定数 */
	static final int NOSHAPE = -1;
	static final int LINE = 0;
	static final int RECTANGLE = 1;
	static final int ROUNDRECT = 2;
	static final int POLYGON = 3;
	static final int ELLIPSE = 4;
	static final int CHARACTER = 5;
	
	int type = NOSHAPE;
	String type_name;
	
	/*
	 * オブジェクトナンバ. 自動配色の際に使用する.
	 * i番目とj番目のオブジェクトのコントラスト、など
	 */
	private int objectNumber;
	private int indexOfArray;	/* 自動配色する際に使用する配列用インデックス */
	
	/**
	 * fillColorとrealColorの違い
	 * smartColorで再配色された色は，まずrealColorに入れられ，
	 * 色盲変換を施したものをさらにfillColorに入れる．
	 * キャンバスに塗られるのはfillColorであり，
	 * 色盲シミュレーションにおいてはrealColorをシミュレートしたものが載せられる．
	 */
	protected Color lineColor;
	protected Color fillColor;	/* キャンバスに着色される色 */
	//protected Color realColor;	/* 色盲変換されていない，実際の色 */
	protected int lineWidth;
	private Color naturalColor = null;
	private boolean isBackgroundPalette = false;
	private boolean isPalette = false;
	
	protected static Color defaultLineColor = Color.BLACK;
	protected static Color defaultFillColor = Color.WHITE;
	protected static int defaultLineWidth = 2;
	
	protected static final int HANDLE_SIZE = 3;
	
	/* ハンドルの位置 */
	public static final int NONE = -1; 	/* どのハンドルでもない状態 */
	
	ACShape() {
		lineColor = defaultLineColor;
		fillColor = defaultFillColor;
		//realColor = defaultFillColor;
		lineWidth = defaultLineWidth;
	}
	
	abstract int getType();
	abstract String getTypeName();
	
	void setLock(ColorLock key) { this.key = key; }
	
	void setObjectNumber(int objectNumber) { this.objectNumber = objectNumber; }
	void setIndexOfArray(int indexOfArray) { this.indexOfArray = indexOfArray; }
	int getObjectNumber() { return objectNumber; }
	int getIndexOfArray() { return indexOfArray; }
	
	void setIsBackgroundPalette(boolean isBackgroundPalette) {
		this.isBackgroundPalette = isBackgroundPalette;
	}
	boolean isBackgroundPalette() { return isBackgroundPalette; }
	void setIsPalette(boolean isPalette) { this.isPalette = isPalette; }
	boolean isPalette() { return isPalette; }
	
	static void setDefaultLineColor(Color c) {
		defaultLineColor = c;
	}
	
	static void setDefaultFillColor(Color c) {
		defaultFillColor = c;
	}
	
	static void setDefaultLineWidth(int lineWidth) {
		defaultLineWidth = lineWidth;
	}
	static int getDefaultLineWidth() {
		return defaultLineWidth;
	}
	
	void setLineColor(Color c) 		 { lineColor = c; }
	void setFillColor(Color c) 		 { fillColor = c; }
	//void setRealColor(Color c)		 { realColor = c; }
	void setNaturalColor() 			 { naturalColor = fillColor; isLocked = true; }
		/* 図形にnaturalColorが指定されたら現在のfillColorで固定する */
	Color getNaturalColor() 		 { return naturalColor; }
	void releasenaturalColor() 		 { naturalColor = null; isLocked = false; }
	void setLineWidth(int width)	 { lineWidth = width; }
	int getLineWidth()				 { return lineWidth; }
	
	void draw(Graphics2D g, boolean drawing) {
		int widthTemp = lineWidth;
		Color colorTemp = g.getColor();
		
		g.setStroke(new BasicStroke(lineWidth));
		
		if (!drawing) {
			g.setColor(fillColor);
			g.fill(shape);
		}
		
		g.setColor(lineColor);
		g.draw(shape);
		
		g.setStroke(new BasicStroke(widthTemp));
		g.setColor(colorTemp);
	}
	
	boolean contains(Point p) {
		return shape.contains(p);
	}
	
	boolean intersects(Rectangle range) {
		return shape.intersects(range);
	}
	
	Rectangle getBounds() {
		if (shape == null) {
			System.out.println("shape is null");
			System.exit(0);
		}
		if (shape.getBounds() == null) {
			System.out.println("bounds is null");
			System.exit(0);
		}
		return shape.getBounds();
	}
	
	abstract void setBounds(int x, int y, int width, int height);
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}
	
	boolean isSelected() { return isSelected; }
	void select() { isSelected = true; }
	void unselect() { isSelected = false; }
	
	/* 各図形が呼び出すshowHandleメソッドはこちら.
	 * その中で下のshowHandleメソッドを呼んでいる. */
	abstract void showHandle(Graphics2D g);
	
	/* 渡されたPointにハンドルを表示する.引数のPointは各図形が個別に定める */
	protected void showHandle(Graphics2D g, Point[] points) {
		for (int i = 0; i < points.length; i++)
			g.fillRect(points[i].x - HANDLE_SIZE, points[i].y - HANDLE_SIZE, HANDLE_SIZE * 2, HANDLE_SIZE * 2);
	}
	
	protected boolean isHandle(Point p, Point pHandle) {
		return (p.x >= pHandle.x - HANDLE_SIZE &&
				 p.x <= pHandle.x + HANDLE_SIZE &&
				 p.y >= pHandle.y - HANDLE_SIZE &&
				 p.y <= pHandle.y + HANDLE_SIZE);
	}
	
	abstract int getHandlePosition(Point p);
	
	protected int getHandlePosition(Point p, Point[] points) {
		for (int i = 0; i < points.length; i++) {
			if (isHandle(p, points[i]))
				return i;
		}
		
		return NONE;
	}
	
	/* ばねの留め具の表示 */
	abstract void showClasp(Graphics2D g);
	protected void showClasp(Graphics2D g, Point point) {
		int width = 10;
		int height = 10;
		g.fillOval(point.x-width/2, point.y-height/2, width, height);
	}
	abstract void setClaspPoint();
	abstract Point getClaspPoint();
	
	abstract void setConnectPoint();
	abstract Point getConnectPoint();
	
	abstract void save();
	abstract void move(Point pStart, Point pEnd);
	abstract void reshape(Point pStart, Point pEnd, boolean isShift);
	abstract void reshape(Point pStart, Point pEnd, int pos, boolean isShift);
}
/* **************************************************************** */



/* ************************* Javaの線 ***************************** */
class ExLine extends Line2D.Double {
	ExLine(double x1, double y1, double x2, double y2) {
		super(x1, y1, x2, y2);
	}
	
	public Object clone() {
		return super.clone();
	}
}
/* **************************************************************** */


/* ******************* AutomaticColoringの線 ********************** */
class ACLine extends ACShape {
	static final int TYPE = LINE;
	static final String TYPE_NAME = "line";
	
	private Point p1Orig;
	private Point p2Orig;
	
	private static final int POINT1 = 0;
	private static final int POINT2 = 1;
	
	ACLine(Point p) {
		shape = new ExLine(p.x, p.y, p.x, p.y);
		fillColor = lineColor;
	}
	
	int getType() { return TYPE; }
	String getTypeName() { return TYPE_NAME; }
	
	private Point getP1() {
		Line2D.Double line = (Line2D.Double)shape;
		return new Point((int)line.getX1(), (int)line.getY1());
	}
	
	private Point getP2() {
		Line2D.Double line = (Line2D.Double)shape;
		return new Point((int)line.getX2(), (int)line.getY2());
	}
	
	void setBounds(int x, int y, int width, int height) {
		Line2D.Double line = (Line2D.Double)shape;
		line.setLine(x, y, x + width, y + height);
		setClaspPoint();
		setConnectPoint();
	}
	
	boolean contains(Point p) {
		Line2D.Double line = (Line2D.Double)shape;
		
		return (line.ptSegDist(p) <= HANDLE_SIZE);
	}
	
	public Object clone() {
		ACLine line;
		
		line = (ACLine)super.clone();
		line.p1Orig = (Point)p1Orig.clone();
		line.p2Orig = (Point)p2Orig.clone();
		line.shape = (Shape)((ExLine)shape).clone();
		
		return line;
	}
	
	void showClasp(Graphics2D g) {
		Point p1 = getP1();
		Point p2 = getP2();
		Point point = new Point((p1.x + p2.x)/2, (p1.y + p2.y)/2);
		showClasp(g, point);
	}
	
	void setClaspPoint() {
		Point p1 = getP1();
		Point p2 = getP2();
		Point point = new Point((p1.x + p2.x)/2, (p1.y + p2.y)/2);
		claspPoint = point;
	}
	Point getClaspPoint() {
		return claspPoint;
	}
	
	void setConnectPoint() {
		Point p1 = getP1();
		Point p2 = getP2();
		Point point = new Point((p1.x + 2*p2.x)/3, (p1.y + 2*p2.y)/3);
		connectPoint = point;
	}
	Point getConnectPoint() {
		return connectPoint;
	}
	
	void showHandle(Graphics2D g) {
		Point p1 = getP1();
		Point p2 = getP2();
		Point[] points = {
				new Point(p1.x, p1.y),
				new Point(p2.x, p2.y),
		};
		
		showHandle(g, points);
	}
	
	int getHandlePosition(Point p) {
		Point p1 = getP1();
		Point p2 = getP2();
		Point[] points = {
				new Point(p1.x, p1.y),
				new Point(p2.x, p2.y),
		};
		
		return getHandlePosition(p, points);
	}
	
	/* 端点を保存 */
	void save() {
		p1Orig = getP1();
		p2Orig = getP2();
	}
	
	void move(Point pStart, Point pEnd) {
		Line2D.Double line = (Line2D.Double)shape;
		int incX = pEnd.x - pStart.x;
		int incY = pEnd.y - pStart.y;
		
		line.setLine(p1Orig.x + incX, p1Orig.y + incY, p2Orig.x + incX, p2Orig.y + incY);
		
		setClaspPoint();
		setConnectPoint();
	}
	
	void reshape(Point pStart, Point pEnd, boolean isShift) {
		reshape(pStart, pEnd, POINT2, isShift);
		
		setClaspPoint();
		setConnectPoint();
	}
	
	/* つかんだハンドルに対応する端点を動かす */
	void reshape(Point pStart, Point pEnd, int pos, boolean isShift) {
		Line2D.Double line = (Line2D.Double)shape;
		int incX = pEnd.x - pStart.x;
		int incY = pEnd.y - pStart.y;
		
		switch(pos) {
		case POINT1:
			line.setLine(p1Orig.x + incX, p1Orig.y + incY, p2Orig.x, p2Orig.y);
			break;
		
		case POINT2:
			line.setLine(p1Orig.x, p1Orig.y, p2Orig.x + incX, p2Orig.y + incY);
			break;
		}
	}
}
/* **************************************************************** */


/* **************************************************************** */
/* Rectangleクラスのclone()をACRectangleクラスから呼べるようにするためのクラス */
class ExRectangle extends Rectangle {
	ExRectangle(Point p) {
		super(p);
	}
	
	public Object clone() {
		return super.clone();
	}
}
/* **************************************************************** */



/* *************** AutomaticColoring用のRectangle ***************** */
class ACRectangle extends ACShape {
	static final int TYPE = RECTANGLE;
	static final String TYPE_NAME = "rectangle";
	
	private Rectangle rcOrig;
	
	/* ハンドルの位置 */
	private static final int TOP_LEFT = 0;
	private static final int TOP_RIGHT = 1;
	private static final int BOTTOM_LEFT = 2;
	private static final int BOTTOM_RIGHT = 3;
	private static final int TOP = 4;
	private static final int BOTTOM = 5;
	private static final int LEFT = 6;
	private static final int RIGHT = 7;
	
	ACRectangle() {
	}
	
	ACRectangle(Point p) {
		shape = new ExRectangle(p);
	}
	
	int getType() { return TYPE; }
	String getTypeName() { return TYPE_NAME; }
	
	void setBounds(int x, int y, int width, int height) {
		Rectangle rect = (Rectangle)shape;
		rect.setBounds(x, y, width, height);
		
		setClaspPoint();
		setConnectPoint();
	}
	
	void setLocation(int x, int y) {
		Rectangle rect = (Rectangle)shape;
		rect.setLocation(x, y);
		
		setClaspPoint();
		setConnectPoint();
	}
	
	Shape cloneShape() {
		return (Shape)((ExRectangle)shape).clone();
	}
	
	public Object clone() {
		ACRectangle rect;
		
		rect = (ACRectangle)super.clone();	/* 雛形をクローン */
		rect.shape = cloneShape();	/* 図形の実体をクローン */
		rect.rcOrig = (Rectangle)rcOrig.clone();	/* バックアップ図形をクローン */
		
		return rect;
	}
	
	void showClasp(Graphics2D g) {
		Rectangle rc = getBounds();
		Point point = new Point(rc.x + rc.width/2, rc.y + rc.height/2);
		showClasp(g, point);
	}
	
	void setClaspPoint() {
		Rectangle rc = getBounds();
		Point point = new Point(rc.x + rc.width/2, rc.y + rc.height/2);
		claspPoint = point;
	}
	Point getClaspPoint() { return claspPoint; }
	
	void setConnectPoint() {
		Rectangle rc = getBounds();
		Point point = new Point(rc.x + 3*rc.width/4, rc.y + 3*rc.height/4);
		connectPoint = point;
	}
	Point getConnectPoint() { return connectPoint; }
	
	void showHandle(Graphics2D g) {
		Rectangle rc = getBounds();
		Point[] points = {
				new Point(rc.x, rc.y),
				new Point(rc.x + rc.width, rc.y),
				new Point(rc.x, rc.y + rc.height),
				new Point(rc.x + rc.width, rc.y + rc.height),
				new Point(rc.x + rc.width / 2, rc.y),
				new Point(rc.x + rc.width / 2, rc.y + rc.height),
				new Point(rc.x, rc.y + rc.height / 2),
				new Point(rc.x + rc.width, rc.y + rc.height / 2),
		};
		
		showHandle(g, points);
	}
	
	/* 移動・変形前の図形を保存 */
	void save() { rcOrig = getBounds(); }
	
	Rectangle getOrigBounds() { return rcOrig; }
	
	/* 図形を移動 */
	void move(Point pStart, Point pEnd) {
		int incX = pEnd.x - pStart.x;
		int incY = pEnd.y - pStart.y;
		
		setLocation(rcOrig.x + incX, rcOrig.y + incY);
		
		setClaspPoint();
		setConnectPoint();
	}
	
	/* 右下をドラッグしているとき(作成時)の変形 */
	void reshape(Point pStart, Point pEnd, boolean isShift) {
		reshape(pStart, pEnd, BOTTOM_RIGHT, isShift);
		
		setClaspPoint();
		setConnectPoint();
	}
	
	/* 位置がposのハンドルをドラッグしているときの変形 */
	void reshape(Point pStart, Point pEnd, int pos, boolean isShift) {
		int x = rcOrig.x;
		int y = rcOrig.y;
		int width = rcOrig.width;
		int height = rcOrig.height;
		int incX = pEnd.x - pStart.x;
		int incY = pEnd.y - pStart.y;
		
		switch (pos) {
			case LEFT:
			case TOP_LEFT:
			case BOTTOM_LEFT:
				x += incX;
				width -= incX;
				break;
			
			case RIGHT:
			case TOP_RIGHT:
			case BOTTOM_RIGHT:
				width += incX;
				break;
		}
		
		switch (pos) {
			case TOP:
			case TOP_LEFT:
			case TOP_RIGHT:
				y += incY;
				height -= incY;
				break;
			
			case BOTTOM:
			case BOTTOM_LEFT:
			case BOTTOM_RIGHT:
				height += incY;
				break;
		}
		
		if (isShift) {
			double ratio = (double)rcOrig.width / rcOrig.height;
			int newWidth, newHeight;
			
			switch(pos) {
			case TOP_LEFT:
			case TOP_RIGHT:
			case BOTTOM_LEFT:
			case BOTTOM_RIGHT:
				if ((double)width / ratio > (double)height) {
					newWidth = (int)(Math.abs(height) * ratio) * ((width < 0) ? -1 : 1);
					if (pos == TOP_LEFT || pos == BOTTOM_LEFT)
						x -= newWidth - width;
					width = newWidth;
				} else {
					newHeight = (int)(Math.abs(width) / ratio) * ((height < 0) ? -1 : 1);
					if (pos == TOP_LEFT || pos == TOP_RIGHT)
						y -= newHeight - height;
					height = newHeight;
				}
			}
		}
		
		if (width < 0) {
			width = -width;
			x -= width;
		}
		if (height < 0) {
			height = -height;
			y -= height;
		}
		
		setBounds(x, y, width, height);
	}
	
	int getHandlePosition(Point p) {
		Rectangle rc = getBounds();
		Point[] points = {
				new Point(rc.x, rc.y),
				new Point(rc.x + rc.width, rc.y),
				new Point(rc.x, rc.y + rc.height),
				new Point(rc.x + rc.width, rc.y + rc.height),
				new Point(rc.x + rc.width / 2, rc.y),
				new Point(rc.x + rc.width / 2, rc.y + rc.height),
				new Point(rc.x, rc.y + rc.height / 2),
				new Point(rc.x + rc.width, rc.y + rc.height / 2)
		};
		
		return getHandlePosition(p, points);
	}
}
/* **************************************************************** */




/* ******************** 角丸四角形clone()用 *********************** */
class ExRoundRectangle extends RoundRectangle2D.Double {
	ExRoundRectangle(double x, double y, double w, double h, double arcw, double arch) {
		super(x, y, w, h, arcw, arch);
	}
	
	public Object clone() {
		return super.clone();
	}
}
/* **************************************************************** */




/* **************** AutomaticColoring用角丸四角形 ***************** */
class ACRoundRectangle extends ACRectangle {
	static final int TYPE = ROUNDRECT;
	static final String TYPE_NAME = "roundrectangle";
	
	private int arc_width = 20;
	private int arc_height = 20;
	
	ACRoundRectangle(Point p) {
		shape = new ExRoundRectangle(p.x, p.y, 0, 0, arc_width, arc_height);
	}
	
	int getType() { return TYPE; }
	String getTypeName() { return TYPE_NAME; }
	
	void setBounds(int x, int y, int width, int height) {
		RoundRectangle2D.Double rr = (RoundRectangle2D.Double)shape;
		rr.setRoundRect(x, y, width, height, arc_width, arc_height);
		
		setClaspPoint();
		setConnectPoint();
	}
	
	void setLocation(int x, int y) {
		RoundRectangle2D.Double rr = (RoundRectangle2D.Double)shape;
		Rectangle rc = shape.getBounds();
		rr.setRoundRect(x, y, rc.width, rc.height, arc_width, arc_height);
		
		setClaspPoint();
		setConnectPoint();
	}
	
	void setArcWidth(int arc_width) {
		this.arc_width = arc_width;
	}
	void setArcHeight(int arc_height) {
		this.arc_height = arc_height;
	}
	
	public Shape cloneShape() {
		return (Shape)((ExRoundRectangle)shape).clone();
	}
}
/* **************************************************************** */



/* ********************** 多角形clone()用 ************************* */
class ExPolygon extends Polygon implements Cloneable {
	ExPolygon() {
		super();
	}
	ExPolygon(int[] xpoints, int[] ypoints, int npoints) {
		super(xpoints, ypoints, npoints);
	}
	
	public Object clone() {
		Polygon polygon;
		
		try {
			polygon = (Polygon)super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("clone not supported");
			return null;
		}
		
		/* Polygonは専用のclone()を持たないため配列オブジェクトは複製されない
		 * そのため、自分で複製する必要がある
		 */
		polygon.xpoints = new int[npoints];
		polygon.ypoints = new int[npoints];
		for (int i = 0; i < npoints; i++) {
			polygon.xpoints[i] = xpoints[i];
			polygon.ypoints[i] = ypoints[i];
		}
		
		return polygon;
	}
}
/* **************************************************************** */



/* ****************** AutomaticColoring用多角形 ******************* */
class ACPolygon extends ACShape {
	static final int TYPE = POLYGON;
	static final String TYPE_NAME = "polygon";
	
	private Vector<Point> drawingPoints = new Vector<Point>();	
	/* 頂点(Point())情報を積んでいる. 
	 * drawingPoints[0]に1つ目の頂点が、drawingPoints[1]に2つ目の頂点が、…
	 */
	
	private Point[] pOrig;
	
	ACPolygon() {
		shape = new ExPolygon();
	}
	ACPolygon(int[] xpoints, int[] ypoints, int npoints) {
		shape = new ExPolygon(xpoints, ypoints, npoints);
	}
	
	int getType() { return TYPE; }
	String getTypeName() { return TYPE_NAME; }
	
	Vector<Point> getDrawingPoints() { return drawingPoints; }
	
	/* 作成中の多角形の頂点を追加 */
	void addPoint(Point p) {
		drawingPoints.add(p);
	}
	
	/* 最後に追加した多角形の頂点を変更 */
	void changePoint(Point p) {
		drawingPoints.remove(drawingPoints.size() - 1);
		drawingPoints.add(p);
	}
	
	/* 多角形を決定して閉じる */
	void close() {
		drawingPoints.remove(drawingPoints.size() - 1);
		
		Polygon polygon = (Polygon)shape;
		for (int i = 0; i < drawingPoints.size(); i++) {
			Point p = (Point)drawingPoints.get(i);
			polygon.addPoint(p.x, p.y);
		}
		
		setClaspPoint();
		setConnectPoint();
	}
	
	/* polygonを囲うrectangleの左上頂点が(x, y)になるように移動 */
	void setBounds(int x, int y, int width, int height) {
		Rectangle rc = getBounds();	/* polygonをすっぽり覆うrectangleを返す */
		Polygon polygon = (Polygon)shape;
		for (int i = 0; i < polygon.npoints; i++) {
			polygon.xpoints[i] += x - rc.x;	/* 引数として与えられた数値と今までの数値の差分 */
			polygon.ypoints[i] += y - rc.y;
		}
		
		setClaspPoint();
		setConnectPoint();
	}
	
	public Object clone() {
		ACPolygon polygon;
		
		polygon = (ACPolygon)super.clone();
		polygon.shape = (Shape)((ExPolygon)shape).clone();
		
		return polygon;
	}
	
	/* ACShapeとは異なる独自のdrawメソッド */
	void draw(Graphics2D g, boolean drawing) {
		if (drawing) {
			g.setStroke(new BasicStroke(getLineWidth()));
			
			/* Polygonとして描画すると最初と最後の頂点が結ばれてしまうため
			 * 作成中は線分の集合として描画する
			 */
			for (int i = 0; i < drawingPoints.size() - 1; i++) {
				Point p1 = (Point)drawingPoints.get(i);
				Point p2 = (Point)drawingPoints.get(i+1);
				Line2D.Double line = new Line2D.Double(p1, p2);
				g.draw(line);
			}
		} else
			super.draw(g, false);
	}
	
	void showClasp(Graphics2D g) {
		/**
		 * 不要なコード？
		Polygon polygon = (Polygon)shape;
		Point point;
		
		/* shapeが直線・点・三角形のとき */
		/**
		if (polygon.npoints == 1) {
			point = new Point(polygon.xpoints[0], polygon.ypoints[0]);
			showClasp(g, point);
			return;
		}
		if (polygon.npoints == 2) {
			int x = (polygon.xpoints[0]+polygon.xpoints[1])/2;
			int y = (polygon.ypoints[0]+polygon.ypoints[1])/2;
			point = new Point(x, y);
			showClasp(g, point);
			return;
		}
		if (polygon.npoints == 3) {
			int x = (polygon.xpoints[0]+polygon.xpoints[1])/2;
			int y = (polygon.ypoints[0]+polygon.ypoints[1])/2;
			x = (x + polygon.xpoints[2])/2;
			y = (y + polygon.ypoints[2])/2;
			point = new Point(x, y);
			showClasp(g, point);
			return;
		}
		
		int index = polygon.npoints/2;
		int sumX = 0;
		int sumY = 0;
		for (int i = 0; i < polygon.npoints; i++) {
			sumX += polygon.xpoints[i];
			sumY += polygon.ypoints[i];
		}
		int claspX = sumX / polygon.npoints;
		int claspY = sumY / polygon.npoints;
		point = new Point(claspX, claspY);
		if (!polygon.contains(claspPoint)) {
			for (int i = 0; i < index; i++) {
				int x = (polygon.xpoints[0]+polygon.xpoints[index+i])/2;
				int y = (polygon.ypoints[0]+polygon.ypoints[index+i])/2;
				point = new Point(x, y);
				if (polygon.contains(point)) {
					showClasp(g, point);
					return;
				}
			
				x = (polygon.xpoints[0]+polygon.xpoints[index-i])/2;
				y = (polygon.ypoints[0]+polygon.ypoints[index-i])/2;
				point = new Point(x, y);
				if (polygon.contains(point)) {
					showClasp(g, point);
					return;
				}
			}
		}
		*/
		showClasp(g, claspPoint);
	}
	
	void setClaspPoint() {
		Polygon polygon = (Polygon)shape;
		Point point;
		
		/* shapeが直線・点・三角形のとき */
		/**
		 * おそらく不要
		if (polygon.npoints == 1) {
			point = new Point(polygon.xpoints[0], polygon.ypoints[0]);
			claspPoint = point;
			return;
		}
		if (polygon.npoints == 2) {
			int x = (polygon.xpoints[0]+polygon.xpoints[1])/2;
			int y = (polygon.ypoints[0]+polygon.ypoints[1])/2;
			point = new Point(x, y);
			claspPoint = point;
			return;
		}
		if (polygon.npoints == 3) {
			int x = (polygon.xpoints[0]+polygon.xpoints[1])/2;
			int y = (polygon.ypoints[0]+polygon.ypoints[1])/2;
			x = (x + polygon.xpoints[2])/2;
			y = (y + polygon.ypoints[2])/2;
			point = new Point(x, y);
			claspPoint = point;
			return;
		}
		*/
		
		int index = polygon.npoints/2;
		/* polygon内部の点を探す */
		int sumX = 0;
		int sumY = 0;
		for (int i = 0; i < polygon.npoints; i++) {
			sumX += polygon.xpoints[i];
			sumY += polygon.ypoints[i];
		}
		int claspX = sumX / polygon.npoints;
		int claspY = sumY / polygon.npoints;
		claspPoint = new Point(claspX, claspY);
		//debug System.out.println(polygon.contains(claspPoint));
		if (!polygon.contains(claspPoint)) {
			//debug System.out.println("if内実行中");
			for (int i = 0; i < index; i++) {
				//debug System.out.println("for内実行中");
				int x = (polygon.xpoints[0]+polygon.xpoints[index+i])/2;
				int y = (polygon.ypoints[0]+polygon.ypoints[index+i])/2;
				point = new Point(x, y);
				if (polygon.contains(point)) {
					claspPoint = point;
					break;
				}
			
				x = (polygon.xpoints[0]+polygon.xpoints[index-i])/2;
				y = (polygon.ypoints[0]+polygon.ypoints[index-i])/2;
				point = new Point(x, y);
				if (polygon.contains(point)) {
					claspPoint = point;
					break;
				}
			}
		}
		/* polygon内部の点探し終了 */
	}
	Point getClaspPoint() {
		return claspPoint;
	}
	
	void setConnectPoint() {
		Polygon polygon = (Polygon)shape;
		Point point;
		
		/* shapeが直線・点・三角形のとき */
		if (polygon.npoints == 1) {
			point = new Point(polygon.xpoints[0], polygon.ypoints[0]);
			connectPoint = point;
			return;
		}
		if (polygon.npoints == 2) {
			int x = (polygon.xpoints[0]+2*polygon.xpoints[1])/3;
			int y = (polygon.ypoints[0]+2*polygon.ypoints[1])/3;
			point = new Point(x, y);
			connectPoint = point;
			return;
		}
		if (polygon.npoints == 3) {
			int x = (polygon.xpoints[0]+polygon.xpoints[1])/2;
			int y = (polygon.ypoints[0]+polygon.ypoints[1])/2;
			x = (x + 2*polygon.xpoints[2])/3;
			y = (y + 2*polygon.ypoints[2])/3;
			point = new Point(x, y);
			connectPoint = point;
			return;
		}
		
		/* polygon内部の点を探す */
		for (int i = 0; i < polygon.npoints; i++) {
			int x = (polygon.xpoints[0]+polygon.xpoints[i+2])/2;
			int y = (polygon.ypoints[0]+polygon.ypoints[i+2])/2;
			point = new Point(x, y);
			if (polygon.contains(point)) {
				connectPoint = point;
				break;
			}
		}
	}
	Point getConnectPoint() { return connectPoint; }
	
	void showHandle(Graphics2D g) {
		Polygon polygon = (Polygon)shape;
		Point[] points = new Point[polygon.npoints];
		for (int i = 0; i < polygon.npoints; i++)
			points[i] = new Point(polygon.xpoints[i], polygon.ypoints[i]);
		
		showHandle(g, points);
	}
	
	int getHandlePosition(Point p) {
		Polygon polygon = (Polygon)shape;
		Point[] points = new Point[polygon.npoints];
		for (int i = 0; i < polygon.npoints; i++)
			points[i] = new Point(polygon.xpoints[i], polygon.ypoints[i]);
		
		return getHandlePosition(p, points);
	}
	
	/* 多角形の頂点を保存 */
	void save() {
		Polygon polygon = (Polygon)shape;
		pOrig = new Point[polygon.npoints];
		for (int i = 0; i < polygon.npoints; i++)
			pOrig[i] = new Point(polygon.xpoints[i], polygon.ypoints[i]);
	}
	
	void move(Point pStart, Point pEnd) {
		int incX = pEnd.x - pStart.x;
		int incY = pEnd.y - pStart.y;
		
		Polygon polygon = (Polygon)shape;
		for (int i = 0; i < polygon.npoints; i++) {
			polygon.xpoints[i] = pOrig[i].x + incX;
			polygon.ypoints[i] = pOrig[i].y + incY;
		}
		
		setClaspPoint();
		setConnectPoint();
	}
	
	/* オーバーライド */
	void reshape(Point pStart, Point pEnd, boolean isShift) {
	}
	
	/* つかんだハンドルに対応する頂点を動かす */
	void reshape(Point pStart, Point pEnd, int pos, boolean isShift) {
		Polygon polygon = (Polygon)shape;
		int incX = pEnd.x - pStart.x;
		int incY = pEnd.y - pStart.y;
		
		if (pos >= 0 && pos < polygon.npoints) {
			polygon.xpoints[pos] = pOrig[pos].x + incX;
			polygon.ypoints[pos] = pOrig[pos].y + incY;
		}
		
		setClaspPoint();
		setConnectPoint();
	}
}
/* **************************************************************** */



/* ************************ 楕円clone()用 ************************* */
class ExEllipse extends Ellipse2D.Double {
	ExEllipse(double x, double y, double w, double h) {
		super(x, y, w, h);
	}
	
	public Object clone() {
		return super.clone();
	}
}
/* **************************************************************** */



/* ******************** AutomaticColoring用楕円 ******************* */
class ACEllipse extends ACRectangle {
	static final int TYPE = ELLIPSE;
	static final String TYPE_NAME = "ellipse";
	
	ACEllipse(Point p) {
		shape = new ExEllipse(p.x, p.y, 0, 0);
	}
	
	int getType() { return TYPE; }
	String getTypeName() { return TYPE_NAME; }
	
	void setBounds(int x, int y, int width, int height) {
		Ellipse2D.Double el = (Ellipse2D.Double)shape;
		el.setFrame(x, y, width, height);
		
		setClaspPoint();
		setConnectPoint();
	}
	
	void setLocation(int x, int y) {
		Ellipse2D.Double el = (Ellipse2D.Double)shape;
		Rectangle rc = shape.getBounds();
		el.setFrame(x, y, rc.width, rc.height);
		
		setClaspPoint();
		setConnectPoint();
	}
	
	public Shape cloneShape() {
		return (Shape)((ExEllipse)shape).clone();
	}
}
/* **************************************************************** */



/* ***************** AutomaticColoring用文字列 ******************** */
class ACCharacter implements Cloneable {
	Rectangle2D bounds;	/* 文字列のcontains判定用 */
	String text;	/* 描く文字列 */
	int chStartX, chStartY;	/* 文字の書き始めの位置 */
	private float revisionX = 3;
	// private float ascent;	/* 位置補正. ベースライン上部領域 */
	// private float descent;	/* 位置補正. ベースライン下部領域 */
	private int characterNumber;
	
	protected boolean isSelected = false;	/* この文字が選択されているかどうか */
	
	static final int CHARACTER = 5;
	
	static final int TYPE = CHARACTER;
	static final String TYPE_NAME = "character";
	private int fontSize = 16;
	private Color fontColor = Color.BLACK;
	
	protected static final int HANDLE_SIZE = 3;
	
	private Rectangle2D bdOrig;	/* bounding boxのバックアップ */
	
	/* ハンドルの位置 */
	private static final int TOP_LEFT = 0;
	private static final int TOP_RIGHT = 1;
	private static final int BOTTOM_LEFT = 2;
	private static final int BOTTOM_RIGHT = 3;
	public static final int NONE = -1; 	/* どのハンドルでもない状態 */
	
	ACCharacter(Point clickedPoint, String text) {
		this.text = text;
		chStartX = clickedPoint.x;
		chStartY = clickedPoint.y;
		
		TextLayout tl = new TextLayout(text,
				new Font("Serif", Font.PLAIN, fontSize),
				new FontRenderContext(null, false, false));
		
		// descent = tl.getDescent();
		
		bounds = tl.getBounds();
		//double boundWidth = bounds.getWidth() - revisionX;
		bounds.setRect(afterX(chStartX), afterY(chStartY), bounds.getWidth(), bounds.getHeight());
		//bounds.setRect(chStartX, chStartY, bounds.getWidth(), bounds.getHeight());
	}
	
	int afterX(int chStartX) { return chStartX; }
	int afterY(int chStartY) { return (int)(chStartY - bounds.getHeight()); }
	
	int getType() { return TYPE; }
	String getTypeName() { return TYPE_NAME; }
	
	boolean isSelected() { return isSelected; }
	void select() { isSelected = true; }
	void unselect() { isSelected = false; }
	
	void setCharacterNumber(int characterNumber) {
		this.characterNumber = characterNumber;
	}
	
	int getCharacterNumber() {
		return characterNumber;
	}
	
	void setFontSize(int fontSize) { this.fontSize = fontSize; }
	int getFontSize() { return fontSize; }
	void setFontColor(Color fontColor) { this.fontColor = fontColor; }
	Color getFontColor() { return fontColor; }
	
	void setBounds(int x, int y, int width, int height) {
		bounds.setRect(x, y, width, height);
	}
	
	void setLocation(int x, int y) {
		bounds.setRect(x, y, bounds.getWidth(), bounds.getHeight());
	}
	
	void draw(Graphics2D g) {
		Font tempF = g.getFont();
		Color tempColor = g.getColor();
		
		g.setFont(new Font("Serif", Font.PLAIN, fontSize));
		g.setColor(fontColor);
		
		g.drawString(text, (float)chStartX, (float)chStartY);
		//System.out.println("draw");
		g.setFont(tempF);	/* もとのフォント設定に戻す */
		g.setColor(tempColor);
		//g.draw(bounds); // debug
	}
	
	Rectangle2D cloneBounds() {
		return (Rectangle2D)(bounds.clone());
	}
	
	public Object clone() {
		ACCharacter ch;
		try {
			ch = (ACCharacter)super.clone();	/* 雛形をクローン */
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
		ch.bounds = cloneBounds();
		ch.bdOrig = (Rectangle2D)bdOrig.clone();	/* バックアップ図形をクローン */
		
		return ch;
	}
	
	/* 渡されたPointにハンドルを表示する.引数のPointは各図形が個別に定める */
	protected void showHandle(Graphics2D g, Point[] points) {
		for (int i = 0; i < points.length; i++)
			g.fillRect(points[i].x - HANDLE_SIZE, points[i].y - HANDLE_SIZE, HANDLE_SIZE * 2, HANDLE_SIZE * 2);
	}
	void showHandle(Graphics2D g) {
		int x = (int)(bounds.getX());
		int y = (int)(bounds.getY());
		int width = (int)(bounds.getWidth());
		int height = (int)(bounds.getHeight());
		Point[] points = {
				new Point(x, y),	/* 左上 */
				new Point(x + width, y),	/* 右上 */
				new Point(x, y + height),	/* 左下 */
				new Point(x + width, y + height),	/* 右下 */
		};
		
		showHandle(g, points);
	}
	
	/* 移動・変形前のbounding boxを保存 */
	void save() { 
		bdOrig = (Rectangle2D)bounds.clone();
	}
	
	Rectangle2D getOrigBounds() { return bdOrig; }
	
	/* 図形を移動 */
	void move(Point pStart, Point pEnd) {
		int incX = pEnd.x - pStart.x;
		int incY = pEnd.y - pStart.y;
		
		int afterX = (int)(bdOrig.getX()+incX);
		int afterY = (int)(bdOrig.getY()+incY);
		
		// debug System.out.println("("+afterX+", "+afterY+")");
		
		setLocation(afterX, afterY);	/* bounding boxの移動 */
		
		// 文字の移動
		chStartX = afterX;
		chStartY = (int)(afterY + bounds.getHeight());
	}
	
	/* 位置がposのハンドルをドラッグしているときの変形 */
	/**
	 * フォントサイズを変更する場合にはこのメソッドを書き直す.
	 * (このメソッドはACRectangleを書き直したもの.そのため無駄が多い？)
	 * 
	 * 伸ばした高さによってフォントサイズを定め、新たなテキストレイアウトを作成し、
	 * バウンディングボックスを再設定する
	 */
	void reshape(Point pStart, Point pEnd, int pos) {
		double x = bdOrig.getX();
		double y = bdOrig.getY();
		double width = bdOrig.getWidth();
		double height = bdOrig.getHeight();
		int incX = pEnd.x - pStart.x;
		int incY = pEnd.y - pStart.y;
		
		switch (pos) {
			case TOP_LEFT:
			case BOTTOM_LEFT:
				x += incX;
				width -= incX;
				break;
			
			case TOP_RIGHT:
			case BOTTOM_RIGHT:
				width += incX;
				break;
		}
		
		switch (pos) {
			case TOP_LEFT:
			case TOP_RIGHT:
				y += incY;
				height -= incY;
				break;
			
			case BOTTOM_LEFT:
			case BOTTOM_RIGHT:
				height += incY;
				break;
		}
		
		double ratio = bdOrig.getWidth() / bdOrig.getHeight();
		int newWidth, newHeight;
			
		switch(pos) {
		case TOP_LEFT:
		case TOP_RIGHT:
		case BOTTOM_LEFT:
		case BOTTOM_RIGHT:
			if ((double)width / ratio > (double)height) {
				newWidth = (int)(Math.abs(height) * ratio) * ((width < 0) ? -1 : 1);
				if (pos == TOP_LEFT || pos == BOTTOM_LEFT)
					x -= newWidth - width;
				width = newWidth;
			} else {
				newHeight = (int)(Math.abs(width) / ratio) * ((height < 0) ? -1 : 1);
				if (pos == TOP_LEFT || pos == TOP_RIGHT)
					y -= newHeight - height;
				height = newHeight;
			}
		}
		
		if (width < 0) {
			width = -width;
			x -= width;
		}
		if (height < 0) {
			height = -height;
			y -= height;
		}
		
		// 旧コード(ぜんぜんダメな？コード)
		//fontSize = (int)height;
		//TextLayout tl = new TextLayout(text,
		//		new Font("Serif", Font.PLAIN, fontSize),
		//		new FontRenderContext(null, false, false));
		//descent = tl.getDescent();
		//bounds = tl.getBounds();
		
		setBounds((int)x, (int)y, (int)width, (int)height);
		chStartX = (int)(x + revisionX);
		chStartY = (int)(y + bounds.getHeight());
		
		//bounds.setRect(chStartX, chStartY, bounds.getWidth(), bounds.getHeight());
		fontSize = (int)height;
	}
	/** */
	
	int getHandlePosition(Point p) {
		int x = (int)(bounds.getX());
		int y = (int)(bounds.getY());
		int width = (int)(bounds.getWidth());
		int height = (int)(bounds.getHeight());
		Point[] points = {
				new Point(x, y),
				new Point(x + width, y),
				new Point(x, y + height),
				new Point(x + width, y + height)
		};
		
		return getHandlePosition(p, points);
	}
	
	protected boolean isHandle(Point p, Point pHandle) {
		/* pHandleはハンドルの中心点 */
		return (p.x >= pHandle.x - HANDLE_SIZE &&
				 p.x <= pHandle.x + HANDLE_SIZE &&
				 p.y >= pHandle.y - HANDLE_SIZE &&
				 p.y <= pHandle.y + HANDLE_SIZE);
	}
	
	protected int getHandlePosition(Point p, Point[] points) {
		for (int i = 0; i < points.length; i++) {
			if (isHandle(p, points[i]))
				return i;
		}
		
		return NONE;
	}
}

/* **************************************************************** */



/* **************************** ばね ****************************** */
class Spring implements Cloneable {
	ACShape myShape, yourShape;	/* ばねの結ぶ2図形 */
	int color_difference;		/* 2図形間の色差 */
	int desirability;			/* 色差の願望度 */
	private double complaint;	/* 不満度. 配色後に設定される */
	private double realContrast;	/* 配色後のコントラスト */
	
	private JPopupMenu popup;
	
	private Point firstClaspPoint, secondClaspPoint;	/* pStart:firstShapeのclaspPoint */
	private Point2D.Double meanPoint1, meanPoint2;	/* 線とばねの中継点 */
	private Point2D.Double springAxisPoint;
	//旧コード SpringImg springImg;			/* ばねの中央部分 */
	SpringCore springCore;		/* ばねの中央部分 */
	//旧コード Line2D.Double line;			/* 図形と図形を結ぶ線 */
	ExpressionLine line1, line2;	/* 図形とばねを結ぶ線 */
	
	/* レベルに応じた実際の色差(color_differenceの数値として実際に使用) */
	static final int DLEVEL0 = 0;
	static final int DLEVEL1 = 18;
	static final int DLEVEL2 = 36;
	static final int DLEVEL3 = 54;
	static final int DLEVEL4 = 72;
	static final int DLEVEL5 = 90;
	static final int DLEVEL6 = 108;
	static final int DLEVEL7 = 126;
	static final int DLEVEL8 = 144;
	static final int DLEVEL9 = 162;
	
	static final int SPRINGDESIRELEVEL1 = 1;
	static final int SPRINGDESIRELEVEL2 = 5;
	static final int SPRINGDESIRELEVEL3 = 10;
	static final int SPRINGDESIRELEVEL4 = 15;
	static final int SPRINGDESIRELEVEL5 = 20;
	static final int SPRINGDESIRELEVEL6 = 25;
	static final int SPRINGDESIRELEVEL7 = 30;
	static final int SPRINGDESIRELEVEL8 = 35;
	static final int SPRINGDESIRELEVEL9 = 40;
	static final int SPRINGDESIRELEVEL10 = 45;
	
	/** 色差による色の濃さのレベル
	 *  verticalLinesとslantLinesでGrayLevelをずらす.
	 *  vertical:GRAYLEVEL3 -> slant:GRAYLEVEL4
	 *  といった具合.
	 */
	static final Color GRAYLEVEL1 = new Color(220, 220, 220);
	static final Color GRAYLEVEL2 = new Color(205, 205, 205);
	static final Color GRAYLEVEL3 = new Color(190, 190, 190);
	static final Color GRAYLEVEL4 = new Color(175, 175, 175);
	static final Color GRAYLEVEL5 = new Color(160, 160, 160);
	static final Color GRAYLEVEL6 = new Color(145, 145, 145);
	static final Color GRAYLEVEL7 = new Color(130, 130, 130);
	static final Color GRAYLEVEL8 = new Color(115, 115, 115);
	static final Color GRAYLEVEL9 = new Color(100, 100, 100);
	static final Color GRAYLEVEL10 = new Color(85, 85, 85);
	static final Color GRAYLEVELEX = new Color(70, 70, 70);
	static final Color GRAYLEVELEX2 = new Color(55, 55, 55);
	static final Color GRAYLEVELEX3 = new Color(40, 40, 40);
	
	float lineWidth;
	Color lineColor = GRAYLEVEL5, slantColor = GRAYLEVEL8;
	
	/*
	 *  pStart  line   springAxisPoint(※)     line    pEnd
	 *   ↓      ↓     ↓                      ↓      ↓
	 *  ○---------------/|/|/|/|/|/|/|/----------------○
	 *  ↑                  ↑                          ↑
	 *  図形1             springImg                    図形2
	 *   (myNumber)                                      (yourNumber)
	 *   
	 * ※springAxisPointはばね画像の左上頂点.その点を軸として画像を回転させる.
	 *   実際の回転および画像の表示はSpringImgクラスの仕事である.
	 *   本クラスでは、AxisPointを指定して、SpringImgクラスのメソッドで回転させる.
	 */
	
	/* 始点と終点を定めると自動でばねが作成される */
	Spring(ACShape myShape, ACShape yourShape, Point firstClaspPoint, Point secondClaspPoint, SpringCore springCore) {
		this.myShape = myShape;
		this.yourShape = yourShape;
		this.firstClaspPoint = firstClaspPoint;
		this.secondClaspPoint = secondClaspPoint;
		this.springCore = springCore;
		
		color_difference = DLEVEL5;	/* デフォルトで中間レベル */
		desirability = SPRINGDESIRELEVEL5;	/* デフォルトで中間レベル */
		setLineWidth();
		
		/* ばねの画像部分を設置(springの設定) */
		if (setSpringPosition(firstClaspPoint, secondClaspPoint)) {
			// springImgオブジェクトのspringAxisPointへの平行移動と回転
			/* setSpringPositionの中でspringAxisPointを計算 */
			springCore.setLocation(springAxisPoint.x, springAxisPoint.y);
			double sin = (secondClaspPoint.y-firstClaspPoint.y)/distance(firstClaspPoint, secondClaspPoint);
			double cos = (secondClaspPoint.x-firstClaspPoint.x)/distance(firstClaspPoint, secondClaspPoint);
			springCore.setRotateAngle(sin, cos);
		}
		
		/* 図形1とばねをつなぐ線の作成 */
		line1 = new ExpressionLine();
		line1.setLine(firstClaspPoint, meanPoint1);
		line1.setLineWidth(lineWidth);
		line1.setLineColor(lineColor);
		/* ばねと図形2をつなぐ線の作成 */
		line2 = new ExpressionLine();
		line2.setLine(meanPoint2, secondClaspPoint);
		line2.setLineWidth(lineWidth);
		line2.setLineColor(lineColor);
	}
	
	public Object clone() {
		Spring spring;
		try {
			spring = (Spring)super.clone();	/* 雛形をクローン */
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
		
		return spring;
	}
	
	void setComplaint(double complaint) { this.complaint = complaint; }
	void setRealContrast(double realContrast) { this.realContrast = realContrast; }
	double getComplaint() { return complaint; }
	double getRealContrast() { return realContrast; }
	Point getFirstClaspPoint() { return firstClaspPoint; }
	Point getSecondClaspPoint() { return secondClaspPoint; }
	void setFirstClaspPoint(Point firstClaspPoint) { this.firstClaspPoint = firstClaspPoint; }
	void setSecondClaspPoint(Point secondClaspPoint) { this.secondClaspPoint = secondClaspPoint; }
	Point2D.Double getSpringAxisPoint() { return springAxisPoint; }
	
	void setPopupMenu(JPopupMenu popup) { this.popup = popup; }
	JPopupMenu getPopupMenu() { return popup; }
	
	double distance(Point pStart, Point pEnd) {
		int x = pEnd.x - pStart.x;
		int y = pEnd.y - pStart.y;
		return Math.sqrt(x*x + y*y);
	}
	
	void setColorDifference(int degree) {
		color_difference = degree;
		setLineColor();
	}
	void setDesirability(int desirability) {
		this.desirability = desirability;
		setLineWidth();
	}
	void reSetFirstClaspPoint() { this.firstClaspPoint = myShape.claspPoint; }
	void reSetSecondClaspPoint() { this.secondClaspPoint = yourShape.claspPoint; }
	void reSetLine() { 
		line1.setLine(firstClaspPoint, meanPoint1);
		line2.setLine(meanPoint2, secondClaspPoint);
	}
	
	boolean setSpringPosition(Point firstClaspPoint, Point secondClaspPoint) {
		/* setpStart, setpEndを実行後には必ずsetSpringPositionを実行すること */
		/* springAxisPointの設定 */
		/**
		if (distance(pStart, pEnd) < springLength()) {
			System.out.println("2点間の距離が小さすぎます");
			return false;
		}
		*/
		
		springAxisPoint = new Point2D.Double();
		meanPoint1 = new Point2D.Double();
		meanPoint2 = new Point2D.Double();
		
		/* pStartからpEndへ向かうベクトル */
		double vecX = secondClaspPoint.x - firstClaspPoint.x;
		double vecY = secondClaspPoint.y - firstClaspPoint.y;
		
		/* 上記ベクトルを正規化 */
		double normalizedX = vecX / distance(firstClaspPoint, secondClaspPoint);
		double normalizedY = vecY / distance(firstClaspPoint, secondClaspPoint);
		/* 上記ベクトルの正規法線ベクトル */
		double verticalX = normalizedY;
		double verticalY = -normalizedX;
		
		/* ClaspPointからばねの接続点までの距離 */
		double dis_of_ClaspToMean = (distance(firstClaspPoint, secondClaspPoint)-springCore.getCoreWidth())/2;
		double adjust = 3.0;
		meanPoint1.x = normalizedX * dis_of_ClaspToMean + firstClaspPoint.x;
		meanPoint1.y = normalizedY * dis_of_ClaspToMean + firstClaspPoint.y;
		meanPoint2.x = normalizedX * (dis_of_ClaspToMean + springCore.getCoreWidth()-adjust) + firstClaspPoint.x;
		meanPoint2.y = normalizedY * (dis_of_ClaspToMean + springCore.getCoreWidth()-adjust) + firstClaspPoint.y;
		
		springAxisPoint.x = normalizedX * dis_of_ClaspToMean
							+ verticalX * springCore.getCoreHeight()/2 + firstClaspPoint.x;
		springAxisPoint.y = normalizedY * dis_of_ClaspToMean
							+ verticalY * springCore.getCoreHeight()/2 + firstClaspPoint.y;
			
		return true;
	}
	
	void setLineColor() {
		switch (color_difference) {
		case DLEVEL0:
			lineColor = Spring.GRAYLEVEL1;
			slantColor = Spring.GRAYLEVEL4;
			break;
		case DLEVEL1:
			lineColor = Spring.GRAYLEVEL2;
			slantColor = Spring.GRAYLEVEL5;
			break;
		case DLEVEL2:
			lineColor = Spring.GRAYLEVEL3;
			slantColor = Spring.GRAYLEVEL6;
			break;
		case DLEVEL3:
			lineColor = Spring.GRAYLEVEL4;
			slantColor = Spring.GRAYLEVEL7;
			break;
		case DLEVEL4:
			lineColor = Spring.GRAYLEVEL5;
			slantColor = Spring.GRAYLEVEL8;
			break;
		case DLEVEL5:
			lineColor = Spring.GRAYLEVEL6;
			slantColor = Spring.GRAYLEVEL9;
			break;
		case DLEVEL6:
			lineColor = Spring.GRAYLEVEL7;
			slantColor = Spring.GRAYLEVEL10;
			break;
		case DLEVEL7:
			lineColor = Spring.GRAYLEVEL8;
			slantColor = Spring.GRAYLEVELEX;
			break;
		case DLEVEL8:
			lineColor = Spring.GRAYLEVEL9;
			slantColor = Spring.GRAYLEVELEX2;
			break;
		case DLEVEL9:
			lineColor = Spring.GRAYLEVEL10;
			slantColor = Spring.GRAYLEVELEX3;
			break;
		}
	}
	void setLineWidth() {
		switch (desirability) {
		case SPRINGDESIRELEVEL1: lineWidth = 2.0f; break;
		case SPRINGDESIRELEVEL2: lineWidth = 2.3f; break;
		case SPRINGDESIRELEVEL3: lineWidth = 2.6f; break;
		case SPRINGDESIRELEVEL4: lineWidth = 2.9f; break;
		case SPRINGDESIRELEVEL5: lineWidth = 3.2f; break;
		case SPRINGDESIRELEVEL6: lineWidth = 3.5f; break;
		case SPRINGDESIRELEVEL7: lineWidth = 3.8f; break;
		case SPRINGDESIRELEVEL8: lineWidth = 4.1f; break;
		case SPRINGDESIRELEVEL9: lineWidth = 4.4f; break;
		case SPRINGDESIRELEVEL10: lineWidth = 4.7f; break;
		}
	}
	
	/**
	 * 引数colorからある程度色差のあるシャドウの色を返す
	 * @param color
	 * @return 影の色
	 */
	Color dropColor(Color color) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		
		int dropRed, dropGreen, dropBlue;
		if (r < 120) {
			dropRed = r + 80;
			dropGreen = g + 80;
			dropBlue = b + 80;
		} else {
			dropRed = r - 80;
			dropGreen = g - 80;
			dropBlue = b - 80;
		}
		
		return new Color(dropRed, dropGreen, dropBlue);
	}
	
	float dropDistance(float lineWidth) {
		return lineWidth / 2.0f;
	}
	
	void draw(Graphics2D g) {
		// 旧コード(どうしようもない時に使用しよう！)
		//reSetFirstClaspPoint();
		//reSetSecondClaspPoint();
		//setSpringPosition(firstClaspPoint, secondClaspPoint);
		//springImg.setLocation(springAxisPoint.x, springAxisPoint.y);
		//double sin = (secondClaspPoint.y-firstClaspPoint.y)/distance(firstClaspPoint, secondClaspPoint);
		//double cos = (secondClaspPoint.x-firstClaspPoint.x)/distance(firstClaspPoint, secondClaspPoint);
		//springImg.setRotateAngle(sin, cos);
		//reSetLine();
		
		//g.draw(line);
		Color tempColor = g.getColor();
		Stroke tempStroke = g.getStroke();
		
		// ドロップシャドウ
		/**
		float dropDist = dropDistance(lineWidth);	// 影との距離
		g.setColor(dropColor(lineColor));	// シャドウ用の色
		g.setStroke(new BasicStroke(lineWidth));	// 実物とシャドウは同じ線の太さで.
		ExpressionLine shadowLine1 = (ExpressionLine)line1.clone();
		ExpressionLine shadowLine2 = (ExpressionLine)line2.clone();
		shadowLine1.setLine(line1.x1, line1.y1+dropDist, line1.x2, line1.y2+dropDist);
		shadowLine2.setLine(line2.x1, line2.y1+dropDist, line2.x2, line2.y2+dropDist);
		shadowLine1.draw(g);
		shadowLine2.draw(g);
		*/
		
		// 輪郭
		g.setColor(dropColor(lineColor));
		g.setStroke(new BasicStroke(lineWidth+0.7f));
		ExpressionLine shadowLine1 = (ExpressionLine)line1.clone();
		ExpressionLine shadowLine2 = (ExpressionLine)line2.clone();
		shadowLine1.draw(g);
		shadowLine2.draw(g);
		
		g.setColor(lineColor);
		g.setStroke(new BasicStroke(lineWidth));
		line1.draw(g);
		line2.draw(g);
		
		//System.out.println("draw line");	// debug
		springCore.draw(g, lineWidth, lineColor, slantColor);
		
		g.setColor(tempColor);
		g.setStroke(tempStroke);
	}
}
/* **************************************************************** */




/* ****************** ばねの画像を管理するクラス ****************** */
class SpringImg {
	private Image springImg;	/* ばねの画像 */
	private Shape bound;	/* contains判定用 */
	private GraphicsPanel gpanel;
	private double x , y;	/* 回転のアンカーポイント. springAxisPoint */
	private int width, height;	/* 図形の寸法 */
	private double sin, cos;	/* 回転角度 */
	private AffineTransform rotate;	/* 画像の回転、平行移動の総合情報 */
	
	private Toolkit tk;
	
	SpringImg(GraphicsPanel gpanel) {
		/* gpanelはイメージオブザーバー */
		/* canvasの左上頂点に長方形を設定、回転なし */
		this.gpanel = gpanel;
		tk = gpanel.getToolkit();
		springImg = tk.getImage("icon/spring_strong.gif");
		x = 0.0;
		y = 0.0;
		sin = 0.0;
		cos = 1.0;	/* theta = 0 の場合 */
		width = springImg.getWidth(gpanel);
		height = springImg.getHeight(gpanel);
		bound = new Rectangle2D.Double(0, 0, width, height);
	}
	SpringImg(GraphicsPanel gpanel, double x, double y, double sin, double cos) {
		/* gpanelはイメージオブザーバー
		 * Imageを(x, y)を左上頂点として配置 */
		/* 指定された位置に指定された角度だけ回転して配置 */
		this.gpanel = gpanel;
		tk = gpanel.getToolkit();
		springImg = tk.getImage("icon/spring_strong.gif");
		this.x = x;
		this.y = y;
		this.sin = sin;
		this.cos = cos;
		
		rotate = AffineTransform.getTranslateInstance(x, y);	/* 指定地に移動 */
		AffineTransform temp = new AffineTransform(cos, sin, -sin, cos, 0, 0);	/* 指定角度回転 */
		rotate.concatenate(temp);	/* 平行移動と回転をひとまとめ */
		
		/* 画像サイズを取得 */
		width = springImg.getWidth(gpanel);
		height = springImg.getHeight(gpanel);
		
		/* bounding box の設定 */
		bound = new Rectangle2D.Double(x, y, width, height);	/* 指定した位置に画像サイズの大きさの図形を */
		bound = temp.createTransformedShape(bound);	/* 指定した位置に置いたので後は回転だけ */
	}
	
	/* ばね画像のサイズを取得 */
	int getImgWidth() { return springImg.getWidth(gpanel); }
	int getImgHeight() { return springImg.getHeight(gpanel); }
	Shape getBoundingBox() { return bound; }
	
	/* 画像の左上頂点の移動 */
	void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
		rotate = AffineTransform.getTranslateInstance(x, y);
		AffineTransform temp = new AffineTransform(cos, sin, -sin, cos, 0, 0);
		rotate.concatenate(temp);
		bound = new Rectangle2D.Double(0, 0, width, height);	/* 初期化 */
		bound = rotate.createTransformedShape(bound);	/* 改めて平行移動・回転 */
	}
	
	/* 画像を画像の左上頂点を中心として引数の角度だけ回転 */
	/* sin(theta)とcos(theta)が与えられた場合 */
	void setRotateAngle(double sin, double cos) {
		rotate = AffineTransform.getTranslateInstance(x, y);
		AffineTransform temp = new AffineTransform(cos, sin, -sin, cos, 0, 0);
		rotate.concatenate(temp);
		bound = new Rectangle2D.Double(0, 0, width, height);	/* 初期化 */
		bound = rotate.createTransformedShape(bound);	/* 改めて平行移動・回転 */
	}
	
	void draw(Graphics2D g, AffineTransform at) {
		/* AffineTransformを作用させたあとのImage springImgを表示
		 * Rectangleは表示しなくてもcontains()は正常に動作する */
		bound = at.createTransformedShape(bound);
		rotate = at;
		g.drawImage(springImg, rotate, gpanel);
		
		//g.draw(bound);	/* debug */
	}
	
	void draw(Graphics2D g) {
		g.drawImage(springImg, rotate, gpanel);
		//Rectangle rect = bound.getBounds();
		
		//g.draw(bound);	/* debug */
	}
}
/* **************************************************************** */




/* ****************** ばねの画像部分を線で表現 ******************** */
class SpringCore {
	private Line2D.Double[] verticalLines;	/* 垂直部分を描画するための線の集合 */
	private Line2D.Double[] slantLines;	/* 斜め部分を描画するための線の集合 */
	private int verticalNumber = 9;	/* 垂直な線の本数. 斜めの線の本数はverNum-1 */
	private Shape bound;
	//private GraphicsPanel gpanel;
	private double x, y;	/* 回転のアンカーポイント. springAxisPoint.x, y */
	//private int width, height;	/* ばねの寸法 */
	private double sin, cos;	/* 回転角度 */
	private AffineTransform rotate;	/* ばねの回転、平行移動の総合情報 */
	
	static final int COREWIDTH = 60;
	static final int COREHEIGHT = 30;	/* ばねの寸法 */
	
	/**
	 * draw()するときにAffineTransformで線を移動・回転させて描画している.
	 * だから、ばねの移動などはすべてAffineTransformだけを処理しておけばいい.
	 * コンストラクターで実行しているsetLine()は線のテンプレートを作成している.
	 */
	
	SpringCore() {
		/* canvasの左上頂点に長方形を設定、回転なし */
		x = 0.0;
		y = 0.0;
		sin = 0.0;
		cos = 1.0;	/* theta = 0 の場合 */
		bound = new Rectangle2D.Double(0, 0, COREWIDTH, COREHEIGHT);
		setLine(x, y);
	}
	SpringCore(double x, double y, double sin, double cos) {
		/* 指定された位置に指定された角度だけ回転して配置 */
		this.x = x;
		this.y = y;
		this.sin = sin;
		this.cos = cos;
		
		rotate = AffineTransform.getTranslateInstance(x, y);
		AffineTransform temp = new AffineTransform(cos, sin, -sin, cos, 0, 0);	/* 指定角度回転 */
		rotate.concatenate(temp);	/* 平行移動と回転をひとまとめ */
		
		bound = new Rectangle2D.Double(x, y, COREWIDTH, COREHEIGHT);
		bound = temp.createTransformedShape(bound);	/* このboxは指定した位置に設置されて入るがまだ回転していない */
		setLine(x, y);
	}
	
	int getCoreWidth() { return COREWIDTH; }
	int getCoreHeight() { return COREHEIGHT; }
	Shape getBoundingBox() { return bound; }
	
	/* verticalLines, slantLinesの設置. x, yは左上の点 */
	private void setLine(double x, double y) {
		double interval = COREWIDTH / (verticalNumber-1);
		
		verticalLines = new Line2D.Double[verticalNumber];
		for (int i = 0; i < verticalNumber; i++) {
			//Point2D.Double p_start;
			//if (i == verticalNumber-1) {
			//	p_start = new Point2D.Double(x + interval*i, y + COREHEIGHT/2);
			//} else {
				Point2D.Double p_start = new Point2D.Double(x + interval*i, y);
			//}
			//Point2D.Double p_end;
			//if (i == 0) {
			//	p_end = new Point2D.Double(x + interval*i, y + COREHEIGHT/2);
			//} else {
				Point2D.Double p_end = new Point2D.Double(x + interval*i, y + COREHEIGHT);
			//}
			
			verticalLines[i] = new Line2D.Double(p_start, p_end);
		}
		
		slantLines = new Line2D.Double[verticalNumber-1];
		for (int i = 0; i < slantLines.length; i++) {
			Point2D.Double p_start = (Point2D.Double)verticalLines[i].getP1();
			Point2D.Double p_end = (Point2D.Double)verticalLines[i+1].getP2();
			
			slantLines[i] = new Line2D.Double(p_start, p_end);
		}
	}
	
	/* bounding box の移動 */
	void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
		rotate = AffineTransform.getTranslateInstance(x, y);
		AffineTransform temp = new AffineTransform(cos, sin, -sin, cos, 0, 0);
		rotate.concatenate(temp);
		bound = new Rectangle2D.Double(0, 0, COREWIDTH, COREHEIGHT);	/* 初期化 */
		bound = rotate.createTransformedShape(bound);	/* 改めて平行移動・回転 */
	}
	/**
	 * ばねをばねの左上頂点を中心として引数の角度だけ回転
	 * sin(theta)とcos(theta)が与えられた場合
	 */
	void setRotateAngle(double sin, double cos) {
		rotate = AffineTransform.getTranslateInstance(x, y);
		AffineTransform temp = new AffineTransform(cos, sin, -sin, cos, 0, 0);
		rotate.concatenate(temp);
		bound = new Rectangle2D.Double(0, 0, COREWIDTH, COREHEIGHT);	/* 初期化 */
		bound = rotate.createTransformedShape(bound);	/* 改めて平行移動・回転 */
	}
	
	/**
	 * 引数colorからある程度色差のあるシャドウの色を返す
	 * @param color
	 * @return 影の色
	 */
	Color dropColor(Color color) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		
		int dropRed, dropGreen, dropBlue;
		if (r < 120) {
			dropRed = r + 80;
			dropGreen = g + 80;
			dropBlue = b + 80;
		} else {
			dropRed = r - 80;
			dropGreen = g - 80;
			dropBlue = b - 80;
		}
		
		return new Color(dropRed, dropGreen, dropBlue);
	}
	
	void draw(Graphics2D g, AffineTransform at) {
		/**
		 * AffineTransformを作用させた後のばねの表示
		 */
		bound = at.createTransformedShape(bound);
		rotate = at;
		
		/**
		 * 画像部分をShapeに置き換えられれば○.
		 * そうすれば、あとはAffineTransformで回転等は容易に行える
		 * 
		 * verticalLines, slantLinesをAffineTransformで移動させつつ一本ずつ描画する
		 */
		Color tempColor = g.getColor();
		for (int i = 0; i < slantLines.length; i++) {
			Shape temp = at.createTransformedShape(slantLines[i]);
			g.draw(temp);
		}
		g.setColor(tempColor);
		for (int i = 0; i < verticalLines.length; i++) {
			Shape temp = at.createTransformedShape(verticalLines[i]);
			g.draw(temp);
		}
		
		//g.draw(bound);	/* debug */
	}
	
	void draw(Graphics2D g, float lineWidth, Color lineColor, Color slantColor) {
		//color_difference = 1;	// debug
		//desirability = 6;	// debug
		// int dropDist = 1;
		
		Color tempColor = g.getColor();
		Stroke tempStroke = g.getStroke();	/* buckup */
		
		// 輪郭
		g.setColor(dropColor(slantColor));
		g.setStroke(new BasicStroke(lineWidth+0.7f));
		for (int i = 0; i < slantLines.length; i++) {
			Line2D.Double cloneLine = (Line2D.Double)slantLines[i].clone();
			Shape shadowShape = rotate.createTransformedShape(cloneLine);
			g.draw(shadowShape);
		}
		g.setColor(dropColor(lineColor));
		for (int i = 0; i < verticalLines.length; i++) {
			Line2D.Double cloneLine = (Line2D.Double)verticalLines[i].clone();
			Shape shadowShape = rotate.createTransformedShape(cloneLine);
			g.draw(shadowShape);
		}
		
		g.setStroke(new BasicStroke(lineWidth));
		g.setColor(slantColor);
		for (int i = 0; i < slantLines.length; i++) {
			Shape temp = rotate.createTransformedShape(slantLines[i]);
			g.draw(temp);
		}
		g.setColor(lineColor);
		for (int i = 0; i < verticalLines.length; i++) {
			Shape temp = rotate.createTransformedShape(verticalLines[i]);
			g.draw(temp);
		}
		
		g.setColor(tempColor);
		g.setStroke(tempStroke);
		
		//g.draw(bound);	/* debug */
	}
}
/* **************************************************************** */




/* ********************** 線付鍵オブジェクト ********************** */
class ColorLock implements Cloneable {
	int desirability;
	private double complaint;
	float lineWidth;
	Color origColor;
	
	private GraphicsPanel gpanel;
	private JPopupMenu popup;
	private Point2D.Double location;	/* 鍵の左上頂点座標 */
	private Point connectPoint;
	// 旧コードprivate Color naturalColor;	/* この鍵がロックしている図形のnaturalColor */
	// 旧コード LockImg lockImg;
	LockLine lockLine;
	//旧コード Line2D.Double line;	/* 鍵と図形を結ぶ線 */
	ExpressionLine line;
	static final int KEYLINE_LENGTH = 25;	/* 鍵と図形を結ぶ線の長さ */
	ACShape lockedShape;	/* この鍵がロックしている図形 */
	
	static final int DLEVEL1 = 1;
	static final int DLEVEL2 = 5;
	static final int DLEVEL3 = 10;
	static final int DLEVEL4 = 15;
	static final int DLEVEL5 = 20;
	static final int DLEVEL6 = 25;
	static final int DLEVEL7 = 30;
	static final int DLEVEL8 = 35;
	static final int DLEVEL9 = 40;
	static final int DLEVEL10 = 45;
	
	/* 図形と線を結ぶ点と鍵画像を受け取ると自動設置 */
	/* connectPointは各図形ごとに定まっているので、各図形から受け取る */
	ColorLock(GraphicsPanel gpanel, ACShape lockedShape, Point connectPoint, LockLine lockLine) {
		this.gpanel = gpanel;
		this.lockedShape = lockedShape;
		this.connectPoint = connectPoint;
		this.lockLine = lockLine;
		desirability = DLEVEL5;	// default setting
		setLineWidth();
		origColor = lockedShape.fillColor;
		// 旧コード this.naturalColor = naturalColor;
		
		lockedShape.setLock(this);
		
		setColorLockPosition();
		
		line = new ExpressionLine();
		double lockX = location.x+lockLine.getWidth()/2;	/* 鍵と線をつなぐ点 */
		double lockY = location.y;
		line.setLine(connectPoint.x, connectPoint.y, lockX, lockY);
		
		popup = createPopupMenu(this);
	}
	
	public Object clone() {
		ColorLock lock;
		try {
			lock = (ColorLock)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
		
		return lock;
	}
	
	JPopupMenu createPopupMenu(ColorLock lock) {
		JPopupMenu popup = new JPopupMenu();
		JMenu desirability_menu = new JMenu("願望の大きさレベル");
		popup.insert(desirability_menu, 0);
		
		JMenuItem nlevel10 = new JMenuItem("Level 10     大");
		nlevel10.addActionListener(new NaturalColorDesirabilityListener(lock, DLEVEL10, gpanel));
		desirability_menu.add(nlevel10);
		for (int i = 9; i > 0; i--) {
			if (i == 5) {
				JMenuItem nLevelItem = new JMenuItem("Level   "+i+"     中");
				nLevelItem.addActionListener(new NaturalColorDesirabilityListener(lock, DLEVEL5, gpanel));
				desirability_menu.add(nLevelItem);
				continue;
			}
			if (i == 1) {
				JMenuItem nlevelItem = new JMenuItem("Level   "+i+"     小");
				nlevelItem.addActionListener(new NaturalColorDesirabilityListener(lock, DLEVEL1, gpanel));
				desirability_menu.add(nlevelItem);
				continue;
			}
			JMenuItem nlevelItem = new JMenuItem("Level  "+i+"");
			int desire = i;
			switch(i) {
			case 2:desire = DLEVEL2; break;
			case 3:desire = DLEVEL3; break;
			case 4:desire = DLEVEL4; break;
			case 6:desire = DLEVEL6; break;
			case 7:desire = DLEVEL7; break;
			case 8:desire = DLEVEL8; break;
			case 9:desire = DLEVEL9; break;
			}
			nlevelItem.addActionListener(new NaturalColorDesirabilityListener(lock, desire, gpanel));
			desirability_menu.add(nlevelItem);
		}
		
		popup.add(new JPopupMenu.Separator());
		
		JMenuItem deleteLockItem = new JMenuItem("ロック解除");
		deleteLockItem.addActionListener(new DeleteLockListener(gpanel, lock));
		popup.add(deleteLockItem);
		
		return popup;
	}
	
	void setPopupMenu(JPopupMenu popup) { this.popup = popup; }
	JPopupMenu getPopupMenu() { return popup; }
	
	void setComplaint(double complaint) { this.complaint = complaint; }
	double getComplaint() { return complaint; }
	
	void setLineWidth() {
		switch (desirability) {
		case DLEVEL1: lineWidth = 1.0f; break;
		case DLEVEL2: lineWidth = 1.5f; break;
		case DLEVEL3: lineWidth = 2.0f; break;
		case DLEVEL4: lineWidth = 2.5f; break;
		case DLEVEL5: lineWidth = 3.0f; break;
		case DLEVEL6: lineWidth = 3.5f; break;
		case DLEVEL7: lineWidth = 4.0f; break;
		case DLEVEL8: lineWidth = 4.5f; break;
		case DLEVEL9: lineWidth = 5.0f; break;
		case DLEVEL10: lineWidth = 5.5f; break;
		}
	}
	
	void setDesirability(int desirability) {
		this.desirability = desirability;
		setLineWidth();
	}
	
	void reSetLine() {
		double lockX = location.x + lockLine.getWidth()/2;
		double lockY = location.y;
		line.setLine(connectPoint.x, connectPoint.y, lockX, lockY);
	}
	
	Point getConnectPoint() { return connectPoint; }
	
	void reSetConnectPoint() { connectPoint = lockedShape.connectPoint; }
	
	void setColorLockPosition() {
		// connectPointからlocation(鍵画像の左上頂点)を計算し、画像を移動
		location = new Point2D.Double();
		location.x = connectPoint.x - lockLine.getWidth()/2;
		location.y = connectPoint.y + KEYLINE_LENGTH;
		
		lockLine.setLocation(location.x, location.y);
	}
	
	/**
	 * connectPointを定めて、ColorLockPositionを定めて、図を描く
	 */
	
	void draw(Graphics2D g) {
		g.fillOval(connectPoint.x-5, connectPoint.y-5, 10, 10);
		Stroke tempStroke = g.getStroke();
		Color tempColor = g.getColor();
		
		g.setStroke(new BasicStroke(lineWidth+0.7f));
		g.setColor(new Color(20, 20, 20));
		ExpressionLine cloneLine = (ExpressionLine)line.clone();
		cloneLine.draw(g);
		
		g.setStroke(new BasicStroke(lineWidth));
		g.setColor(new Color(100, 100, 100));
		line.draw(g);
		lockLine.draw(g);
		
		g.setStroke(tempStroke);
		g.setColor(tempColor);
	}
}
/* **************************************************************** */




/* ****************** 鍵の画像を管理するクラス ******************** */
class LockImg {
	private Image lockImg;
	private GraphicsPanel gpanel;
	private Shape bound;	/* contains判定用 */
	private Toolkit tk;
	private int width, height;	/* 鍵の寸法 */
	// private double x, y;	/* 鍵の位置 */
	private AffineTransform translate;
	
	LockImg(GraphicsPanel gpanel) {
		/* gpanelはイメージオブザーバー */
		this.gpanel = gpanel;
		tk = gpanel.getToolkit();
		lockImg = tk.getImage("icon/naturalColor.gif");
		width = lockImg.getWidth(gpanel);
		height = lockImg.getHeight(gpanel);
		bound = new Rectangle2D.Double(0, 0, width, height);
	}
	
	int getImgWidth() { return lockImg.getWidth(gpanel); }
	int getImgHeight() { return lockImg.getHeight(gpanel); }
	Shape getBoundingBox() { return bound; }
	
	/* 画像の左上頂点の移動 */
	void setLocation(double x, double y) {
		// this.x = x;
		// this.y = y;
		translate = AffineTransform.getTranslateInstance(x, y);
		bound = new Rectangle2D.Double(0, 0, width, height);	/* 初期化 */
		bound = translate.createTransformedShape(bound);	/* 改めて平行移動 */
	}
	
	void draw(Graphics2D g) {
		g.drawImage(lockImg, translate, gpanel);
		
		// Rectangle rect = bound.getBounds();
		g.draw(bound);	/* debug */
	}
}
/* **************************************************************** */




/* ******************** 鍵の画像部分を線で表現 ******************** */
class LockLine {
	private Rectangle2D.Double body;	/* 鍵の胴体部分. 中は金？灰？で塗りつぶす */
	private Ellipse2D.Double chain;	/* 鍵の開閉部分. 線は太く、色は銀色？ */
	private Ellipse2D.Double design1;	/* 鍵の胴体部分に描く模様その1 */
	private Line2D.Double design2;		/* 鍵の胴体部分に描く模様その2 */
	private Shape bound;	/* contains判定用 */
	// private double x, y;	/* 鍵の位置 */
	private AffineTransform translate;
	private static final int BODYLINEWIDTH = 1;
	private static final int CHAINLINEWIDTH = 7;
	private static final int DESIGNLINEWIDTH = 4;
	private static final Color BODYCOLOR = new Color(90, 90, 90);
	private static final Color CHAINLINECOLOR = new Color(160, 160, 160);
	private static final Color DESIGNCOLOR = new Color(200, 200, 200);
	private static final int BODYWIDTH = 27;
	private static final int CHAINWIDTH = 16;	/* 鍵の寸法 */
	private static final int DESIGNWIDTH = BODYWIDTH/3;	
	private static final int BODYHEIGHT = 24;
	private static final int CHAINHEIGHT = 20;
	private static final int DESIGNHEIGHT = BODYHEIGHT/3;
	
	/**
	 * 願望の大きさを鍵の大きさで現すならば…
	 * bodywidth, bodyheight, designwidth, chainwidth, chainheight, designheight
	 * を，願望が変わるたび変更し，setLine()し，setLocation()を実行する.
	 */
	
	LockLine() {
		bound = new Rectangle2D.Double(0, 0, BODYWIDTH, BODYHEIGHT+CHAINHEIGHT);
		setLine();
	}
	
	int getWidth() { return BODYWIDTH; }
	int getHeight() { return BODYHEIGHT+CHAINHEIGHT; }
	Shape getBoundingBox() { return bound; }
	
	private void setLine() {
		double x = 0;
		double y = CHAINHEIGHT/2;
		body = new Rectangle2D.Double(x, y, BODYWIDTH, BODYHEIGHT);
		
		x = (BODYWIDTH-CHAINWIDTH)/2;
		y = 0;
		chain = new Ellipse2D.Double(x, y, CHAINWIDTH, CHAINHEIGHT);
		
		double x1 = BODYWIDTH/2;
		double y1 = CHAINHEIGHT/2 + BODYHEIGHT/4;
		double x2 = BODYWIDTH/2;
		double y2 = CHAINHEIGHT/2 + BODYHEIGHT/2;
		design2 = new Line2D.Double(x1, y1, x2, y2);
		
		x = BODYWIDTH/3;
		y = CHAINHEIGHT/2 + 2*BODYHEIGHT/5;
		design1 = new Ellipse2D.Double(x, y, DESIGNWIDTH, DESIGNHEIGHT);
	}
	
	void setLocation(double x, double y) {
		// this.x = x;
		// this.y = y;
		translate = AffineTransform.getTranslateInstance(x, y);
		bound = new Rectangle2D.Double(0, 0, BODYWIDTH, BODYHEIGHT+CHAINHEIGHT);
		bound = translate.createTransformedShape(bound);
	}
	
	/**
	 * 引数colorからある程度色差のあるシャドウの色を返す
	 * @param color
	 * @return 影の色
	 */
	Color dropColor(Color color) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		
		int dropRed, dropGreen, dropBlue;
		if (r < 120) {
			dropRed = r + 80;
			dropGreen = g + 80;
			dropBlue = b + 80;
		} else {
			dropRed = r - 80;
			dropGreen = g - 80;
			dropBlue = b - 80;
		}
		
		return new Color(dropRed, dropGreen, dropBlue);
	}
	
	void draw(Graphics2D g) {
		Color tempColor = g.getColor();
		Stroke tempStroke = g.getStroke();
		
		g.setColor(dropColor(CHAINLINECOLOR));
		g.setStroke(new BasicStroke(CHAINLINEWIDTH+0.7f));
		Ellipse2D.Double cloneChain = (Ellipse2D.Double)chain.clone();
		Shape cloneDrawChain = translate.createTransformedShape(cloneChain);
		g.draw(cloneDrawChain);
		
		g.setColor(dropColor(BODYCOLOR));
		g.setStroke(new BasicStroke(BODYLINEWIDTH+0.7f));
		Rectangle2D.Double cloneBody = (Rectangle2D.Double)body.clone();
		Shape cloneDrawBody = translate.createTransformedShape(cloneBody);
		g.draw(cloneDrawBody);
		
		g.setColor(CHAINLINECOLOR);
		g.setStroke(new BasicStroke(CHAINLINEWIDTH));
		Shape drawChain = translate.createTransformedShape(chain);
		g.draw(drawChain);
		
		g.setColor(BODYCOLOR);
		g.setStroke(new BasicStroke(BODYLINEWIDTH));
		Shape drawBody = translate.createTransformedShape(body);
		g.draw(drawBody);
		g.fill(drawBody);
		
		g.setColor(DESIGNCOLOR);
		g.setStroke(new BasicStroke(DESIGNLINEWIDTH));
		Shape drawDesign2 = translate.createTransformedShape(design2);
		g.draw(drawDesign2);
		Shape drawDesign1 = translate.createTransformedShape(design1);
		g.draw(drawDesign1);
		g.fill(drawDesign1);
		
		g.setColor(tempColor);
		g.setStroke(tempStroke);
	}
}
/* **************************************************************** */




/* *********************** ばね、鍵用の線 ************************* */
class ExpressionLine extends Line2D.Double {
	// private Color lineColor = Color.GRAY;
	// private float lineWidth;
	static float defaultLineWidth = 2.4f;
	ExpressionLine() {
		// lineWidth = defaultLineWidth;
	}
	
	void draw(Graphics2D g) {
		//System.out.println("springline draw");	/* debug */
		//debug System.out.println(lineWidth);
		//g.setStroke(new BasicStroke(lineWidth));
		//g.setColor(lineColor);
		g.draw(this);
	}
	
	void setLineColor(Color color) { /* lineColor = color; */ }
	void setLineWidth(float width) { /* lineWidth = width; */ }
}
/* **************************************************************** */





