package jp.ac.titech.is.wakitalab.apps.scdraw;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import jp.ac.titech.is.wakitalab.apps.scdraw.shape.SDEllipse;
import jp.ac.titech.is.wakitalab.apps.scdraw.shape.SDRectangle;


public class SDEditorControl extends MouseInputAdapter {
	SDEditorModel model;
	
	public SDEditorControl(SDEditorModel model) {
		this.model = model;
	}
	
	private Point p0;
	
	public void mousePressed(MouseEvent e) {
		if (model.mode == null) return;
		
		p0 = e.getPoint();
		Grid.adjustToGrid(p0);
		
		switch (model.mode) {
		case SDRectangle:
			model.setCurrent(new SDRectangle(p0));
			break;
		case SDEllipse:
			model.setCurrent(new SDEllipse(p0));
			break;
		}
		
		model.update();
	}
	
	public void mouseDragged(MouseEvent e) {
		if (model.getCurrent() == null) return;

		Point p = e.getPoint();
		Grid.adjustToGrid(p);
		model.getCurrent().reshape(p0, p, e.isShiftDown());
		model.update();
	}
	
	public void mouseReleased(MouseEvent e) {
		if (model.getCurrent() == null) return;
		
		Point p = e.getPoint();		
		if (p0.x != p.x && p0.y != p.y) {
			mouseDragged(e);
			model.addShape();
		}
		
		model.setCurrent(null);
		model.update();
	}
}
