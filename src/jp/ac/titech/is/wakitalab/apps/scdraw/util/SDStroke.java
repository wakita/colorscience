package jp.ac.titech.is.wakitalab.apps.scdraw.util;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.Hashtable;

public class SDStroke {
	private static Hashtable<Integer, Stroke> strokes = new Hashtable<Integer, Stroke>();
	static public Stroke stroke(int thickness) {
		Stroke stroke = strokes.get(thickness);
		if (stroke != null) return stroke;
		else {
			stroke = new BasicStroke(thickness);
			strokes.put(thickness, stroke);
			return stroke;
		}
	}
}
