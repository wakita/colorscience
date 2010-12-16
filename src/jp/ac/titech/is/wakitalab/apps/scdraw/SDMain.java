package jp.ac.titech.is.wakitalab.apps.scdraw;

import javax.swing.JFrame;

public class SDMain extends JFrame {
	
	private SDMain() {
		super("SmartColor Draw");
		setLocation(20, 40);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		new SDView(getContentPane());
		
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		new SDMain();
	}
}
