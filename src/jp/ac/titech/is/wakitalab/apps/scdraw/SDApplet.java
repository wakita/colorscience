package jp.ac.titech.is.wakitalab.apps.scdraw;

import javax.swing.JApplet;

public class SDApplet extends JApplet {
	
	public void init() {
	    try {
	        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
	            public void run() {
	        		new SDView(getContentPane());
	            }
	        });
	    } catch (Exception e) {
	        System.err.println("GUI failed to build successfully.");
	    }
	}
}
