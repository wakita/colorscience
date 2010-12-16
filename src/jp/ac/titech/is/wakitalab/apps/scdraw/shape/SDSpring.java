package jp.ac.titech.is.wakitalab.apps.scdraw.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import jp.ac.titech.is.wakitalab.apps.scdraw.util.ColorUtil;
import jp.ac.titech.is.wakitalab.apps.scdraw.util.SDStroke;


public class SDSpring extends BufferedImage {
	   public static final int GRAY_DIFF = 32;

	   int width;
	   int height;
	   Color color;
	   int thickness;
	   int lines;
	   
	   public SDSpring(int width, int height, int imageType) {
	       super(width, height, imageType);
	       this.width = width;
	       this.height = height;
	   }
	   
	   public SDSpring(int width, int height, Color color, int thickness, int lines) {
		   super(width,height, BufferedImage.TYPE_4BYTE_ABGR );
	       this.width = width;
	       this.height = height;
	       this.color = color;
	       this.thickness = thickness;
	       this.lines = lines;
	   }
	   
	   Color transparent = new Color(0, 0, 0, 0);
	   
	   public void draw(Graphics2D g) {
		   int step = (height - thickness) / (lines - 1);
		   
	       g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

	       g.setColor(transparent);
	       g.fillRect(0, 0, width, height);
	       
	       Stroke thick = SDStroke.stroke(thickness), thin = SDStroke.stroke(2);
	       Color darkColor = ColorUtil.darkColor(color), lightColor = ColorUtil.lightColor(color);
	       
	       g.setStroke(thick);
	       g.setColor(darkColor);
	       for (int y = thickness / 2; y + step <= height; y += step) {
	           g.drawLine(0, y, width, y + step);
	       }

	       g.setColor(color);
	       for (int y = thickness / 2 + step; y <= height - step; y += step) {
	           g.drawLine(0, y, width, y);
	       }

	       g.setColor(lightColor);
	       g.setStroke(thin);
	       for (int y = step; y <= height - step; y += step) {
	           g.drawLine(0, y, width, y);
	       }

	       int y = thickness / 2;
	       g.setStroke(thick);
	       g.setColor(color);
	       g.drawLine(0, y, width / 2 + 1 - thickness / 2, y);
	       y += step * (lines - 1);
	       g.drawLine(width / 2 + thickness / 2, y, width, y);
	   }

	   /*
	    * z: [-90, 90]
	    * 
	    * asin(x / width): [0, pi]
	    * asin(x / width) / pi * pitch: [0, pitch]
	    */
	   
	   public void draw2(Graphics2D g) {
	       g.setColor(transparent);
	       g.fillRect(0, 0, width, height);
	       
	       Stroke thick = SDStroke.stroke(thickness);
	       
	       g.setStroke(thick);
	       g.setColor(color);

	       int npoints = lines * 8;
	       int[] xpoints = new int[npoints + 1], ypoints = new int[npoints + 1];

	       for (int n = 0; n <= npoints; n++) {
	    	   ypoints[n] = height * n / npoints;
	    	   double theta = 2.0 * Math.PI * lines * n / npoints;
	    	   xpoints[n] = (int)(width / 2.0 * (1 - Math.sin(theta)));
	       }
	       g.drawPolyline(xpoints, ypoints, npoints + 1);
	   }	

	   public static SDSpring newSpring(int width, int height, Color color, int thickness, int lines) {
	       int step = (height - thickness) / lines;
	       int gap = step - thickness;
	       if (gap < thickness / 5) {
	           throw new TooCondensedSpringException(height, thickness, lines);
	       }

	       height = thickness + step * (lines - 1);
	       
	       SDSpring spring = new SDSpring(width, height, color, thickness, lines);
	       Graphics2D g = spring.createGraphics();
	       spring.draw(g);
	       return spring;
	   }

	   public static SDSpring newSpring2(int width, int height, Color color, int thickness, int lines) {
	       int step = (height - thickness) / lines;
	       int gap = step - thickness;
	       if (gap < thickness / 5) {
	           throw new TooCondensedSpringException(height, thickness, lines);
	       }

	       height = thickness + step * (lines - 1);
	       
	       SDSpring spring = new SDSpring(width, height, color, thickness, lines);
	       Graphics2D g = spring.createGraphics();
	       spring.draw2(g);
	       return spring;
	   }
}
