package jp.ac.titech.is.wakitalab.apps.scdraw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import jp.ac.titech.is.wakitalab.apps.scdraw.shape.SDShape;
import jp.ac.titech.is.wakitalab.apps.scdraw.shape.SDShapeType;


public class SDEditorModel {
	public List<SDShape> shapes = new LinkedList<SDShape>();
	public List<SDShape> selections = new LinkedList<SDShape>();
	private SDShape current;

	public void setCurrent(SDShape current) {
		this.current = current;
	}

	public SDShape getCurrent() {
		return current;
	}

	SDShapeType mode = null;
	
	private JPanel view = null;
	
	public void addShape() {
		shapes.add(current);
		update();
	}
	
	public void deleteShape() {
		shapes.remove(current);
		update();
	}
	
	public void paintComponent(Graphics2D g) {
		BufferedImage img = new BufferedImage(101, 101, BufferedImage.TYPE_INT_RGB);
		Graphics2D g_img = img.createGraphics();
		g_img.setColor(Color.WHITE);
		g_img.fillRect(0, 0, 100, 100);
		g_img.setColor(Color.GRAY);
		for (int y = 0; y <= 100; y += Grid.getGridSize()) {
			g_img.drawLine(0, y, 100, y);
		}
		for (int x = 0; x <= 100; x += Grid.getGridSize()) {
			g_img.setStroke(new BasicStroke(1));
			g_img.drawLine(x, 0, x, 100);
		}
		AffineTransform atrans = new AffineTransform();
		// atrans.setToRotation(5/*, 200, 100*/);
		// g.drawImage(img, atrans, null);
		// atrans.setToTranslation(200, 200);
		// g.drawImage(img, atrans, null);
		atrans.translate(200, 200);
		for (int theta = 1; theta < 15; theta += 3) {
			atrans.rotate(2 * Math.PI * 3 / 360, 50, 50);
			g.drawImage(img, atrans, null);
		}
		
		for (SDShape shape: shapes) {
			shape.fill(g);
			// shape.draw(g);
		}
		
		for (SDShape shape: selections) {
			shape.paintHandle(g);
		}
		
		if (getCurrent() != null) getCurrent().drawTemporary(g);
	}
	
	protected void setView(JPanel view) {
		this.view = view;
		update();
	}
	
	public void update() {
		assert view != null;
		view.repaint();
	}
}
