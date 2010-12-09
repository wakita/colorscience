package jp.ac.titech.is.wakitalab.apps.scdraw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import jp.ac.titech.is.wakitalab.apps.scdraw.shape.SDShapeType;


public class SDToolButtonFactory {
	public static List<AbstractButton> buttons = new Vector<AbstractButton>();
	
	SDEditorModel model;
	
	SDToolButtonFactory(SDEditorModel model) {
		this.model = model;
	}
	
	class ToolButton extends JToggleButton {
		SDShapeType type;
		
		ToolButton(SDShapeType t, JToolBar tools, ButtonGroup bg, ActionListener listener, String text, String tooltip) {
			super(text);
			type = t;
			addActionListener(listener);
			setToolTipText(tooltip);
			bg.add(this);
			tools.add(this);
		}
		
		SDShapeType type() {
			return type;
		}
	}
	
	public JToolBar createDrawTools() {
		JToolBar tools = new JToolBar("Draw tools");
		ButtonGroup bg = new ButtonGroup();
		ActionListener listener = new ActionAdapter() {
			public void actionPerformed(ActionEvent e) {
				model.mode = ((ToolButton)e.getSource()).type();
			}
		};
		
		new ToolButton(SDShapeType.SDPointer, tools, bg, listener, "(S)", "Select an object");
		new ToolButton(SDShapeType.SDRectangle, tools, bg, listener, "(R)", "Draw a rectangle");
		new ToolButton(SDShapeType.SDEllipse, tools, bg, listener, "(E)", "Draw an ellipse");
		new ToolButton(SDShapeType.SDPolygon, tools, bg, listener, "(P)", "Draw a polygon");
		
		return tools;
	}
	
	public JToolBar createSCTools() {
		JToolBar tools = new JToolBar("SmartColor tools");
		ButtonGroup bg = new ButtonGroup();
		ActionListener control = new ActionAdapter() {
			public void actionPerformed(ActionEvent e) {
			}
		};

		new ToolButton(SDShapeType.SDPointer, tools, bg, control, "(C)", "Edit a contrast constaint");
		new ToolButton(SDShapeType.SDPointer, tools, bg, control, "(N)", "Edit a natural coloring constraint");
		new ToolButton(SDShapeType.SDPointer, tools, bg, control, "(S)", "Apply SmartColor technology");

		return tools;
	}
}
