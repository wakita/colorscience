package jp.ac.titech.is.wakitalab.apps.scdraw;

import java.awt.Point;

public class Grid {
	private static boolean gridOn = true;
	private static int gridSize = 5;
	
	public static void adjustToGrid(Point p) {
		if (gridOn)
			p.move(p.x / gridSize * gridSize, p.y / gridSize * gridSize);
	}
	
	public static void gridOn() {
		gridOn = true;
	}
	
	public static void gridOff() {
		gridOn = false;
	}
	
	public static void setGridSize(int g) {
		gridSize = g;
		gridOn();
	}
	
	public static int getGridSize() {
		return gridSize;
	}
}
