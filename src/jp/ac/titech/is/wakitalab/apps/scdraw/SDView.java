package jp.ac.titech.is.wakitalab.apps.scdraw;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class SDView {
	public static void addToolButton(JToolBar toolbar, ButtonGroup bg, ActionListener listener, String label, String tooltip) {
		AbstractButton b = new JToggleButton(label);
		b.addActionListener(listener);
		b.setToolTipText(tooltip);
		bg.add(b);
		toolbar.add(b);
	}
	
	private SDEditorModel model = new SDEditorModel();
	
	SDView(Container topPane) {
		JTabbedPane tabbedPane = new JTabbedPane();
		topPane.add(tabbedPane);
		
		{
			JPanel panel = new JPanel(false);
			panel.setLayout(new BorderLayout());
			
			JPanel toolbar = new JPanel();
			toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
			panel.add(toolbar, BorderLayout.NORTH);

			SDToolButtonFactory toolButtonFactory = new SDToolButtonFactory(model);
			toolbar.add(toolButtonFactory.createDrawTools());
			toolbar.add(toolButtonFactory.createSCTools());

			GPanel gpanel = new GPanel(model);
			model.setView(panel);
			SDEditorControl control = new SDEditorControl(model);
			gpanel.addMouseListener(control);
			gpanel.addMouseMotionListener(control);
			panel.add(gpanel);

			tabbedPane.addTab("Drawing", panel);
		}
		
		JPanel colorPanel = new JPanel(false);
		tabbedPane.addTab("Color", colorPanel);
		
		JPanel analyzePanel = new JPanel(false);
		tabbedPane.addTab("Analyze", analyzePanel);
	}
}
