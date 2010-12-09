package jp.ac.titech.is.wakitalab.apps.scdraw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class GPanel extends JPanel {
	
	SDEditorModel model;

	public GPanel(SDEditorModel model, int width, int height) {
		this.model = model;
		setBackground(Color.white);
		setPreferredSize(new Dimension(width, height));

	}
	
	public GPanel(SDEditorModel model) {
		this(model, 800, 600);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		model.paintComponent(g2d);
	}
}
