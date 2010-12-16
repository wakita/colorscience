package jp.ac.titech.is.wakitalab.color.masuda;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;


public class ColorPanel extends JPanel {
	private final int WIDTH, HEIGHT;
	private static final int SIZE = 30;
	private static final int EMPTY_SPACE = 2;
	private static final int TITLE_SPACE = 30;
	private static final int num_of_column = 20;
	private final Color[] originalChart;
	private Color[] protanopeChart, deutanopeChart, tritanopeChart;
	private int type = Constants.NATURE;
	
	public ColorPanel(Color[] originalChart) {
		this.originalChart = originalChart;
		WIDTH = num_of_column*(SIZE+EMPTY_SPACE);
		HEIGHT = TITLE_SPACE + (originalChart.length/num_of_column + 2)*(SIZE+EMPTY_SPACE);
		super.setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}
	
	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, WIDTH, HEIGHT);
		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		
		
		if (type == Constants.NATURE) {
			Color c;
			int x, y;
			for (int i = 0; i < originalChart.length/num_of_column; i++) {
				for (int j = 0; j < num_of_column; j++) {
					c = originalChart[i*num_of_column+j];
					x = j*(SIZE+EMPTY_SPACE);
					y = i*(SIZE+EMPTY_SPACE)+ TITLE_SPACE;
					g.setColor(c);
					g.fillRect(x, y, SIZE, SIZE);
//					g.setColor(Color.black);
//					g.drawString(Integer.toString(c.getRed()), x, y + (int)(SIZE/3.0));
//					g.drawString(Integer.toString(c.getGreen()), x, y + (int)(SIZE*2/3.0));
//					g.drawString(Integer.toString(c.getBlue()), x, y + SIZE);
				}
			}
		} else if (type == Constants.PROTANOPE) {
			for (int i = 0; i < protanopeChart.length/num_of_column; i++) {
				for (int j = 0; j < num_of_column; j++) {
					g.setColor(protanopeChart[i*num_of_column+j]);
					g.fillRect(j*(SIZE+EMPTY_SPACE), i*(SIZE+EMPTY_SPACE)+ TITLE_SPACE, SIZE, SIZE);
					
				}
			}
		} else if (type == Constants.DEUTERANOPE) {
			for (int i = 0; i < deutanopeChart.length/num_of_column; i++) {
				for (int j = 0; j < num_of_column; j++) {
					g.setColor(deutanopeChart[i*num_of_column+j]);
					g.fillRect(j*(SIZE+EMPTY_SPACE), i*(SIZE+EMPTY_SPACE)+ TITLE_SPACE, SIZE, SIZE);
					
				}
			}
		} else if (type == Constants.TRITANOPE) {
			for (int i = 0; i < tritanopeChart.length/num_of_column; i++) {
				for (int j = 0; j < num_of_column; j++) {
					g.setColor(tritanopeChart[i*num_of_column+j]);
					g.fillRect(j*(SIZE+EMPTY_SPACE), i*(SIZE+EMPTY_SPACE)+ TITLE_SPACE, SIZE, SIZE);
					
				}
			}
		}
	}
	
	public void setProtanope(Color[] protanopeChart) {
		this.protanopeChart = protanopeChart;
	}
	public void setDeutanope(Color[] deutanopeChart) {
		this.deutanopeChart = deutanopeChart;
	}
	public void setTritanope(Color[] tritanopeChart) {
		this.tritanopeChart = tritanopeChart;
	}
	public void setType(int type) {
		this.type = type;
	}
}
