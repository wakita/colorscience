package jp.ac.titech.is.wakitalab.color.masuda;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;



public class DSTest extends JFrame implements ActionListener {
	DichromatSimulator simulator = new DichromatSimulator();
	ColorPanel colorPanel;
	
	public static void main(String[] args) {
		new DSTest("DichromatSimulator ds").test();
	}
	
	DSTest(String title) {
		super(title);
		initialize();
	}
	
	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 500);
	}
	
	Color[] originalChart, protanopeChart, deuteranopeChart, tritanopeChart;
	int num_of_color;
	
	void test() {
		
		int step = 32;
		int num = 256/step;
		num_of_color = (int)Math.pow(num, 3);
		originalChart = new Color[num_of_color];
		protanopeChart = new Color[num_of_color];
		deuteranopeChart = new Color[num_of_color];
		tritanopeChart = new Color[num_of_color];
		int n = 0;
		for (int r = 0; r < num; r++) {
			for (int g = 0; g < num; g++) {
				for (int b = 0; b < num; b++) {
					originalChart[n] = new Color(r*step, g*step, b*step);
					n++;
				}
			}
		}
		originalChart[0] = Color.white;
		
		
		
		colorPanel = new ColorPanel(originalChart);
		
		JScrollPane scrollPane = new JScrollPane(colorPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(scrollPane);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		
		natureItem.addActionListener(this);
		editMenu.add(natureItem);
		
		protanItem.addActionListener(this);
		editMenu.add(protanItem);
		
		deutanItem.addActionListener(this);
		editMenu.add(deutanItem);
		
		tritanItem.addActionListener(this);
		editMenu.add(tritanItem);
		
		
		
//		pack();
		setVisible(true);
		
		long before = System.currentTimeMillis();
		for (int i = 0; i < num_of_color; i++) {
			protanopeChart[i] = simulator.p_L(new SRGBColor(originalChart[i]).getRGB()).getColor();
			deuteranopeChart[i] = simulator.p_M(new SRGBColor(originalChart[i]).getRGB()).getColor();
			tritanopeChart[i] = simulator.p_S(new SRGBColor(originalChart[i]).getRGB()).getColor();
		}
		long after = System.currentTimeMillis();
		System.out.println("Time = " + (after - before)/1000.0);
		colorPanel.setProtanope(protanopeChart);
		colorPanel.setDeutanope(deuteranopeChart);
		colorPanel.setTritanope(tritanopeChart);
	}
	
	JMenuItem natureItem = new JMenuItem("nature");
	JMenuItem protanItem = new JMenuItem("protanope");
	JMenuItem deutanItem = new JMenuItem("deuteranope");
	JMenuItem tritanItem = new JMenuItem("tritanope");
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(natureItem)) {
			colorPanel.setType(Constants.NATURE);
			colorPanel.repaint();
		} else if (e.getSource().equals(protanItem)) {
			colorPanel.setType(Constants.PROTANOPE);
			colorPanel.repaint();
		} else if (e.getSource().equals(deutanItem)) {
			colorPanel.setType(Constants.DEUTERANOPE);
			colorPanel.repaint();
		} else if (e.getSource().equals(tritanItem)) {
			colorPanel.setType(Constants.TRITANOPE);
			colorPanel.repaint();
		}
	}
}
