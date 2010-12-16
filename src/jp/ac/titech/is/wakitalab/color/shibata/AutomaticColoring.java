package jp.ac.titech.is.wakitalab.color.shibata;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import jp.ac.titech.is.wakitalab.color.shimamura.Dichromat;
import jp.ac.titech.is.wakitalab.color.shimamura.LMS;
import jp.ac.titech.is.wakitalab.color.shimamura.SRGB;

public class AutomaticColoring extends JFrame implements ActionListener {
	static public final boolean DEMO = true;
	private final String demoData1 = "/Users/wakita/tmp/SCDrawDemo1.xml";

	private GraphicsPanel gpanel;
	private JMenuItem itemPaste, itemDuplicate;
	static boolean CONTRAST_SETTINGMODE_ON = true;
	static boolean CONTRAST_SETTINGMODE_OFF = false;

	JFileChooser fcsr = new JFileChooser(new File("./save"));
	FileFilterEx filter = new FileFilterEx(".xml", "xml (*.xml)");

	AutomaticColoring() {
		super("AutomaticColoring DrawTool");
		setLocation(20, 40);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());

		gpanel = new GraphicsPanel();

		pane.add(gpanel);

		JToolBar tools = new JToolBar("tool box");

		ButtonGroup group = new ButtonGroup();

		ToolButton selectButton = new ToolButton(gpanel, new ImageIcon("icon/select.gif"), CONTRAST_SETTINGMODE_OFF);
		selectButton.setMouseListener(new PointerListener(gpanel));
		selectButton.setToolTipText("select");
		tools.add(selectButton);
		group.add(selectButton);

		ToolButton rectButton = new ToolButton(gpanel, new ImageIcon("icon/rectangle.gif"), CONTRAST_SETTINGMODE_OFF);
		rectButton.setMouseListener(new RectangleListener(gpanel));
		rectButton.setToolTipText("rectangle");
		tools.add(rectButton);
		group.add(rectButton);

		ToolButton rrButton = new ToolButton(gpanel, new ImageIcon("icon/roundrect.gif"), CONTRAST_SETTINGMODE_OFF);
		rrButton.setMouseListener(new RoundRectangleListener(gpanel));
		rrButton.setToolTipText("roundrectangle");
		tools.add(rrButton);
		group.add(rrButton);

		ToolButton elButton = new ToolButton(gpanel, new ImageIcon("icon/ellipse.gif"), CONTRAST_SETTINGMODE_OFF);
		elButton.setMouseListener(new EllipseListener(gpanel));
		elButton.setToolTipText("ellipse");
		tools.add(elButton);
		group.add(elButton);

		ToolButton lineButton = new ToolButton(gpanel, new ImageIcon("icon/line.gif"), CONTRAST_SETTINGMODE_OFF);
		lineButton.setMouseListener(new LineListener(gpanel));
		lineButton.setToolTipText("line");
		tools.add(lineButton);
		group.add(lineButton);

		ToolButton polyButton = new ToolButton(gpanel, new ImageIcon("icon/polygon.gif"), CONTRAST_SETTINGMODE_OFF);
		polyButton.setMouseListener(new PolygonListener(gpanel));
		polyButton.setToolTipText("polygon");
		tools.add(polyButton);
		group.add(polyButton);

		ToolButton charButton = new ToolButton(gpanel, new ImageIcon("icon/character.gif"), CONTRAST_SETTINGMODE_OFF);
		charButton.setMouseListener(new CharacterListener(gpanel));
		charButton.setToolTipText("character");
		tools.add(charButton);
		group.add(charButton);

		JButton paletteButton = new JButton(new ImageIcon("icon/palette.gif"));
		paletteButton.addActionListener(new PaletteListener(gpanel));
		paletteButton.setToolTipText("create palette");
		tools.add(paletteButton);
		group.add(paletteButton);

		JButton lineColorButton = new JButton(new ImageIcon("icon/linecolor.gif"));
		lineColorButton.addActionListener(new ColorListener(gpanel, ColorListener.LINE));
		lineColorButton.setToolTipText("set line color");
		tools.add(lineColorButton);
		group.add(lineColorButton);

		JButton fillColorButton = new JButton(new ImageIcon("icon/fillcolor.gif"));
		fillColorButton.addActionListener(new ColorListener(gpanel, ColorListener.FILL));
		fillColorButton.setToolTipText("set fill color");
		tools.add(fillColorButton);
		group.add(fillColorButton);

		JButton thickButton = new JButton(new ImageIcon("icon/thick.gif"));
		thickButton.addActionListener(new LineWidthListener(gpanel, 1));
		thickButton.setToolTipText("thick");
		tools.add(thickButton);
		group.add(thickButton);

		JButton thinButton = new JButton(new ImageIcon("icon/thin.gif"));
		thinButton.addActionListener(new LineWidthListener(gpanel, -1));
		thinButton.setToolTipText("thin");
		tools.add(thinButton);
		group.add(thinButton);

		/*
		// cut
		JButton cutButton = new JButton(new ImageIcon("icon/cut.gif"));
		cutButton.addActionListener(this);
		cutButton.setActionCommand("Cut");
		cutButton.setToolTipText("cut");
		tools.add(cutButton);
		group.add(cutButton);

		// copy
		JButton copyButton = new JButton(new ImageIcon("icon/copy.gif"));
		copyButton.addActionListener(this);
		copyButton.setActionCommand("Copy");
		copyButton.setToolTipText("copy");
		tools.add(copyButton);
		group.add(copyButton);

		// paste
		JButton pasteButton = new JButton(new ImageIcon("icon/paste.gif"));
		pasteButton.addActionListener(this);
		pasteButton.setActionCommand("Paste");
		pasteButton.setToolTipText("paste");
		tools.add(pasteButton);
		group.add(pasteButton);

		// delete
		JButton deleteButton = new JButton(new ImageIcon("icon/trash.gif"));
		deleteButton.addActionListener(this);
		deleteButton.setActionCommand("Delete");
		deleteButton.setToolTipText("delete");
		tools.add(deleteButton);
		group.add(deleteButton);

		// clear
		JButton clearButton = new JButton(new ImageIcon("icon/clear.gif"));
		clearButton.addActionListener(this);
		clearButton.setActionCommand("Clear");
		clearButton.setToolTipText("clear");
		tools.add(clearButton);
		group.add(clearButton);
		*/

		ToolButton springButton = new ToolButton(gpanel, new ImageIcon("icon/spring.gif"), CONTRAST_SETTINGMODE_ON);
		springButton.setMouseListener(new SpringListener(gpanel));
		springButton.setToolTipText("setting color_difference, it's desirability");
		tools.add(springButton);
		group.add(springButton);

		ToolButton lockButton = new ToolButton(gpanel, new ImageIcon("icon/lock.gif"), CONTRAST_SETTINGMODE_ON);
		lockButton.setMouseListener(new LockListener(gpanel));
		lockButton.setToolTipText("setting natural_color");
		tools.add(lockButton);
		group.add(lockButton);

		JButton setContrastButton = new JButton(new ImageIcon("icon/coloration.gif"));
		setContrastButton.addActionListener(new SetContrastListener(gpanel));
		setContrastButton.setToolTipText("Let's coloring!");
		tools.add(setContrastButton);
		group.add(setContrastButton);

		pane.add(tools, BorderLayout.NORTH);

		createMenu();

		pack();
		setVisible(true);
	}

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu gameMenu = new JMenu("Game");
		JMenu visualMenu = new JMenu("Visual");
		menuBar.add(fileMenu);
		menuBar.add(gameMenu);
		menuBar.add(visualMenu);

		JMenuItem openItem = new JMenuItem("XML Open");
		fileMenu.add(openItem);
		openItem.addActionListener(this);
		openItem.setActionCommand("Open");

		JMenuItem saveItem = new JMenuItem("XML Save");
		fileMenu.add(saveItem);
		saveItem.addActionListener(this);
		saveItem.setActionCommand("Save");

		JMenuItem jpgItem = new JMenuItem("JPEG");
		fileMenu.add(jpgItem);
		jpgItem.addActionListener(this);
		jpgItem.setActionCommand("SaveAsJPG");

		fileMenu.addSeparator();

		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenu.add(exitItem);
		exitItem.addActionListener(this);
		exitItem.setActionCommand("Exit");


		JMenuItem itemCut = new JMenuItem("Cut");
		itemCut.addActionListener(this);
		itemCut.setActionCommand("Cut");
		gameMenu.add(itemCut);

		JMenuItem itemCopy = new JMenuItem("Copy");
		itemCopy.addActionListener(this);
		itemCopy.setActionCommand("Copy");
		gameMenu.add(itemCopy);

		itemPaste = new JMenuItem("Paste");
		itemPaste.addActionListener(this);
		itemPaste.setActionCommand("Paste");
		itemPaste.setEnabled(false);
		gameMenu.add(itemPaste);
		
		itemDuplicate = new JMenuItem("Duplicate");
		itemDuplicate.addActionListener(this);
		itemDuplicate.setActionCommand("Duplicate");
		itemDuplicate.setEnabled(true);
		gameMenu.add(itemDuplicate);

		JMenuItem itemDelete = new JMenuItem("Delete");
		itemDelete.addActionListener(this);
		itemDelete.setActionCommand("Delete");
		gameMenu.add(itemDelete);

		JMenuItem itemClear = new JMenuItem("Clear");
		itemClear.addActionListener(this);
		itemClear.setActionCommand("Clear");
		gameMenu.add(itemClear);


		JMenuItem coloring_result = new JMenuItem("Coloring Result");
		coloring_result.addActionListener(this);
		coloring_result.setActionCommand("coloring_result");
		visualMenu.add(coloring_result);

		JMenuItem success_rate = new JMenuItem("Success RAte");
		success_rate.addActionListener(this);
		success_rate.setActionCommand("success_rate");
		visualMenu.add(success_rate);

		setJMenuBar(menuBar);
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals("Cut")) {
			gpanel.cut();
			itemPaste.setEnabled(true);
		} else if (action.equals("Copy")) {
			gpanel.copy();
			itemPaste.setEnabled(true);
		} else if (action.equals("Paste"))
			gpanel.paste();
		else if (action.equals("Duplicate")) {
			gpanel.duplicate();
			itemPaste.setEnabled(true);
		} else if (action.equals("Delete"))
			gpanel.delete();
		else if (action.equals("Clear"))
			gpanel.clear();
		else if (action.equals("coloring_result")) {
			new ShowResult(gpanel);
		}
		else if (action.equals("success_rate")) {
			new ShowComplaint(gpanel);
		}
		else if (action.equals("Open")) {
			File loadFile = null;
			
			try {
				if (DEMO) {
					gpanel.loadXML(new File(demoData1));
				} else {
					int fd = fcsr.showOpenDialog(this);
					loadFile = fcsr.getSelectedFile();
					if (fd == JFileChooser.APPROVE_OPTION) {
						gpanel.loadXML(loadFile);
					}
				}
			} catch (Exception ex) {
				System.out.println("Exception:" + ex);
			}
		} else if (action.equals("Save")) {
			try {
				if (DEMO) {
					gpanel.saveXML(new File(demoData1));
				} else {
					int fd = fcsr.showSaveDialog(this);
					File saveFile = fcsr.getSelectedFile();
					if (fd == JFileChooser.APPROVE_OPTION) {
						gpanel.saveXML(saveFile);
					}
				}
			} catch (Exception ex) {
				System.out.println("Exception: " + ex);
			}

		} else if (action.equals("SaveAsJPG")) {
			gpanel.saveAsJPG();
		} else if (action.equals("Exit")) {
			int value = JOptionPane.showConfirmDialog(
					this, "???", 
					"AutomaticColoring DrawTool", JOptionPane.YES_NO_OPTION
			);
			if (value == JOptionPane.YES_OPTION)
				System.exit(0);
		} else if (action.equals("debug")) {
			gpanel.debug();
		}
	}

	public static void main(String[] args) {
		new AutomaticColoring();
	}

}

class FileFilterEx extends FileFilter {
	private String extension, msg;

	public FileFilterEx(String extension, String msg) {
		this.extension = extension;
		this.msg = msg;
	}

	public boolean accept(File f) {
		return f.getName().endsWith(extension);
	}
	public String getDescription() { return msg; }
}
/* **************************************************************** */




class ShowResult extends JFrame {
	private GraphicsPanel gpanel;
	private Color[] colors;
	private JPanel trichromatPanel = new JPanel();
	private JPanel protanopePanel = new JPanel();
	private JPanel deuteranopePanel = new JPanel();
	private JPanel tritanopePanel = new JPanel();
	private JLabel trichromatTitle = new JLabel("Trichromat");
	private JLabel protanopeTitle = new JLabel("Protanope");
	private JLabel deuteranopeTitle = new JLabel("Deuteranope");
	private JLabel tritanopeTitle = new JLabel("Tritanope");

	private static final int TRICHROMAT = 0;
	private static final int PROTANOPE = 1;
	private static final int DEUTERANOPE = 2;
	private static final int TRITANOPE = 3;

	private static final double SCALE = 2.0/5.0;

	ShowResult(GraphicsPanel gpanel) {
		super("SCDraw");
		this.gpanel = gpanel;

		getTrichromatColors();

		setSize(680, 620);
		setLocation(40, 60);
		this.getContentPane().setLayout(new GridLayout(2, 2));

		trichromatPanel.add(trichromatTitle);
		trichromatPanel.add(getTypePanel(TRICHROMAT));
		this.getContentPane().add(trichromatPanel);
		protanopePanel.add(protanopeTitle);
		protanopePanel.add(getTypePanel(PROTANOPE));
		this.getContentPane().add(protanopePanel);
		deuteranopePanel.add(deuteranopeTitle);
		deuteranopePanel.add(getTypePanel(DEUTERANOPE));
		this.getContentPane().add(deuteranopePanel);
		tritanopePanel.add(tritanopeTitle);
		tritanopePanel.add(getTypePanel(TRITANOPE));
		this.getContentPane().add(tritanopePanel);

		setVisible(true);
	}

	private void getTrichromatColors() {
		Vector shapes = gpanel.getShapes();
		colors = new Color[shapes.size()];
		for (int i = 0; i < shapes.size(); i++) {
			//colors[i] = ((ACShape)shapes.get(i)).realColor;
			colors[i] = ((ACShape)shapes.get(i)).fillColor;
		}
	}

	private Color[] change_to_protanope() {
		Color[] answer = new Color[colors.length];
		for (int i = 0; i < colors.length; i++) {
			SRGB srgb = new SRGB(colors[i]);
			LMS lms = srgb.getLMS();
			LMS protanope_lms = Dichromat.convert(lms, Dichromat.PROTANOPE);
			SRGB protanope_srgb = protanope_lms.getSRGB();
			float red = (float)protanope_srgb.getValue1();
			float green = (float)protanope_srgb.getValue2();
			float blue = (float)protanope_srgb.getValue3();
			if (red < 0) {red = 0.0f;}
			if (green < 0) {green = 0.0f;}
			if (blue < 0) {blue = 0.0f;}
			if (red > 1.0f) {red = 1.0f;}
			if (green > 1.0f) {green = 1.0f;}
			if (blue > 1.0f) {blue = 1.0f;}
			Color rgbColor = new Color(red, green, blue);
			answer[i] = rgbColor;
                    //answer[i] = colors[i];
		}

		return answer;
	}
	private Color[] change_to_deuteranope() {
		Color[] answer = new Color[colors.length];
		for (int i = 0; i < colors.length; i++) {
			SRGB srgb = new SRGB(colors[i]);
			LMS lms = srgb.getLMS();
			LMS deuteranope_lms = Dichromat.convert(lms, Dichromat.DEUTERANOPE);
			SRGB deuteranope_srgb = deuteranope_lms.getSRGB();
			float red = (float)deuteranope_srgb.getValue1();
			float green = (float)deuteranope_srgb.getValue2();
			float blue = (float)deuteranope_srgb.getValue3();
			if (red < 0) {red = 0.0f;}
			if (green < 0) {green = 0.0f;}
			if (blue < 0) {blue = 0.0f;}
			if (red > 1.0f) {red = 1.0f;}
			if (green > 1.0f) {green = 1.0f;}
			if (blue > 1.0f) {blue = 1.0f;}
			Color rgbColor = new Color(red, green, blue);
			answer[i] = rgbColor;
		}

		return answer;
	}
	private Color[] change_to_tritanope() {
		Color[] answer = new Color[colors.length];
		for (int i = 0; i < colors.length; i++) {
			SRGB srgb = new SRGB(colors[i]);
			LMS lms = srgb.getLMS();
			LMS tritanope_lms = Dichromat.convert(lms, Dichromat.TRITANOPE);
			SRGB tritanope_srgb = tritanope_lms.getSRGB();
			float red = (float)tritanope_srgb.getValue1();
			float green = (float)tritanope_srgb.getValue2();
			float blue = (float)tritanope_srgb.getValue3();
			if (red < 0) {red = 0.0f;}
			if (green < 0) {green = 0.0f;}
			if (blue < 0) {blue = 0.0f;}
			if (red > 1.0f) {red = 1.0f;}
			if (green > 1.0f) {green = 1.0f;}
			if (blue > 1.0f) {blue = 1.0f;}
			Color rgbColor = new Color(red, green, blue);
			answer[i] = rgbColor;
		}

		return answer;
	}

	private GraphicsPanel getTypePanel(int dType) {
		Vector shapes = gpanel.getShapes();
		Vector characters = gpanel.getCharacters();
		Vector<ACShape> reSizeShapes = new Vector<ACShape>();
		Vector<ACCharacter> reSizeCharacters = new Vector<ACCharacter>();
		GraphicsPanel canvas = new GraphicsPanel(320, 240);
		canvas.isDrawCanvas = false;

		canvas.setBackground(gpanel.getBackground());

		for (int i = 0; i < shapes.size(); i++) {
			reSizeShapes.add(reSize((ACShape)shapes.get(i), SCALE));
		}

		if (dType == PROTANOPE) {
			Color[] protanopeColor = change_to_protanope();
			for (int i = 0; i < reSizeShapes.size(); i++) {
				((ACShape)reSizeShapes.get(i)).fillColor = protanopeColor[i];
			}
		} else if (dType == DEUTERANOPE) {
			Color[] deuteranopeColor = change_to_deuteranope();
			for (int i = 0; i < reSizeShapes.size(); i++) {
				((ACShape)reSizeShapes.get(i)).fillColor = deuteranopeColor[i];
			}
		} else if (dType == TRITANOPE) {
			Color[] tritanopeColor = change_to_tritanope();
			for (int i = 0; i < reSizeShapes.size(); i++) {
				((ACShape)reSizeShapes.get(i)).fillColor = tritanopeColor[i];
			}
		} 
		/**
		 else if (dType == TRICHROMAT) {
			for (int i = 0; i < reSizeShapes.size(); i++) {
				((ACShape)reSizeShapes.get(i)).fillColor = ((ACShape)reSizeShapes.get(i)).realColor;
			}
		}
		 */

		for (int i = 0; i < characters.size(); i++) {
			reSizeCharacters.add(reSize((ACCharacter)characters.get(i), SCALE));
		}

		canvas.setShapes(reSizeShapes);

		canvas.setCharacters(reSizeCharacters);

		canvas.repaint();

		return canvas;
	}

	ACShape reSize(ACShape shape, double scale) {
		int type = shape.getType();
		int lineWidth = 1;

		if (type == ACShape.LINE) {
			ACLine resizeLine = (ACLine)shape.clone();
			resizeLine.setLineWidth(lineWidth);

			Line2D.Double line = (Line2D.Double)(resizeLine.shape);
			line.x1 = line.x1*scale;
			line.x2 = line.x2*scale;
			line.y1 = line.y1*scale;
			line.y2 = line.y2*scale;

			return resizeLine;
		} else if (type == ACShape.RECTANGLE) {
			ACRectangle resizeRectangle = (ACRectangle)shape.clone();
			resizeRectangle.setLineWidth(lineWidth);

			Rectangle rect = (Rectangle)(resizeRectangle.shape);
			rect.x = (int)(rect.x*scale);
			rect.y = (int)(rect.y*scale);
			rect.width = (int)(rect.width*scale);
			rect.height = (int)(rect.height*scale);

			return resizeRectangle;
		} else if (type == ACShape.ROUNDRECT) {
			ACRoundRectangle resizeRoundRect = (ACRoundRectangle)shape.clone();
			resizeRoundRect.setLineWidth(lineWidth);

			RoundRectangle2D.Double roundRect = (RoundRectangle2D.Double)(resizeRoundRect.shape);
			roundRect.x = roundRect.x*scale;
			roundRect.y = roundRect.y*scale;
			roundRect.width = roundRect.width*scale;
			roundRect.height = roundRect.height*scale;
			roundRect.archeight = roundRect.archeight*scale;
			roundRect.arcwidth = roundRect.arcwidth*scale;

			return resizeRoundRect;
		} else if (type == ACShape.ELLIPSE) {
			ACEllipse resizeEllipse = (ACEllipse)shape.clone();
			resizeEllipse.setLineWidth(lineWidth);

			Ellipse2D.Double ellipse = (Ellipse2D.Double)(resizeEllipse.shape);
			ellipse.x = ellipse.x*scale;
			ellipse.y = ellipse.y*scale;
			ellipse.width = ellipse.width*scale;
			ellipse.height = ellipse.height*scale;

			return resizeEllipse;
		} else {
			/* if (type == ACShape.POLYGON) */
			ACPolygon resizePolygon = (ACPolygon)shape.clone();
			resizePolygon.setLineWidth(lineWidth);

			Polygon polygon = (Polygon)(resizePolygon.shape);
			for (int i = 0; i < polygon.npoints; i++) {
				polygon.xpoints[i] = (int)(polygon.xpoints[i]*scale);
				polygon.ypoints[i] = (int)(polygon.ypoints[i]*scale);
			}

			return resizePolygon;
		}
	}

	ACCharacter reSize(ACCharacter ch, double scale) {
		ACCharacter resizeCharacter = (ACCharacter)ch.clone();
		int scaleFontSize = (int)(resizeCharacter.getFontSize()*scale);
		if (scaleFontSize < 1) scaleFontSize = 1;
		resizeCharacter.setFontSize(scaleFontSize);
		resizeCharacter.chStartX = (int)(resizeCharacter.chStartX*scale);
		resizeCharacter.chStartY = (int)(resizeCharacter.chStartY*scale);
		double resizeWidth = resizeCharacter.bounds.getWidth() * SCALE;
		double resizeHeight = resizeCharacter.bounds.getHeight() * SCALE;
		resizeCharacter.bounds.setRect(
				resizeCharacter.chStartX, resizeCharacter.chStartY, resizeWidth, resizeHeight);

		return resizeCharacter;
	}
}
/* **************************************************************** */




class ShowComplaint extends JFrame {
	private GraphicsPanel gpanel;
	private Color[] colors;
	private Vector<ACShape> copyShapes;
	private Vector<ACCharacter> copyCharacters;
	private Vector<Spring> copySprings;
	private Vector<ACShape> copyClaspShapes;
	private Vector<ColorLock> copyLocks;

	ShowComplaint(GraphicsPanel gpanel) {
		super("Complaint");
		this.gpanel = gpanel;

		getTrichromatColors();

		setSize(800, 640);
		setLocation(40, 60);

		this.getContentPane().add(getCompPanel());

		setVisible(true);
	}

	private void getTrichromatColors() {
		Vector shapes = gpanel.getShapes();
		colors = new Color[shapes.size()];
		for (int i = 0; i < shapes.size(); i++) {
			colors[i] = ((ACShape)shapes.get(i)).fillColor;
		}
	}

	private GraphicsPanel getCompPanel() {
		Vector<ACShape> shapes = gpanel.getShapes();
		Vector<ACCharacter> characters = gpanel.getCharacters();
		Vector springs = gpanel.getSprings();
		Vector colorLocks = gpanel.getColorLocks();
		Vector<ACShape> claspShapes = gpanel.getClaspShapes();
		copyShapes = new Vector<ACShape>();
		copyCharacters = new Vector<ACCharacter>();
		copySprings = new Vector<Spring>();
		copyClaspShapes = new Vector<ACShape>();
		copyLocks = new Vector<ColorLock>();

		GraphicsPanel canvas = new GraphicsPanel(800, 600);
		//canvas.isDrawCanvas = false;

		ComplaintSpringListener csListener = new ComplaintSpringListener(canvas);
		ComplaintLockListener clListener = new ComplaintLockListener(canvas);

		canvas.setBackground(gpanel.getBackground());

		for (int i = 0; i < shapes.size(); i++) {
			copyShapes.add((ACShape)(shapes.get(i).clone()));
		}
		for (int i = 0; i < characters.size(); i++) {
			copyCharacters.add((ACCharacter)(characters.get(i).clone()));
		}
		for (int i = 0; i < springs.size(); i++) {
			Spring temp = (Spring)((Spring)springs.get(i)).clone();
			temp.setPopupMenu(csListener.createPopupMenu(temp));
			temp.lineColor = new Color(145, 145, 145);
			temp.slantColor = new Color(100, 100, 100);
			temp.lineWidth = getLineWidth(temp.getComplaint());
			copySprings.add(temp);
		}
		for (int i = 0; i < claspShapes.size(); i++) {
			copyClaspShapes.add((ACShape)(claspShapes.get(i).clone()));
		}
		for (int i = 0; i < colorLocks.size(); i++) {
			ColorLock temp = (ColorLock)((ColorLock)colorLocks.get(i)).clone();
			temp.setPopupMenu(clListener.createPopupMenu(temp));
			temp.lineWidth = getLineWidth(temp.getComplaint());
			copyLocks.add(temp);
		}

		canvas.setShapes(copyShapes);
		canvas.setCharacters(copyCharacters);
		canvas.setSprings(copySprings);
		canvas.setClaspShapes(copyClaspShapes);
		canvas.setColorLocks(copyLocks);

		canvas.setting_colorDifference = true;

		canvas.addMouseListener(csListener);
		canvas.addMouseListener(clListener);

		canvas.repaint();

		return canvas;
	}

	/**
	 * @param complaint
	 * @return
	 */
	private float getLineWidth(double complaint) {
		if (complaint < 100) return 1.0f;
		else if (complaint < 500) return 1.3f;
		else if (complaint < 1000) return 1.6f;
		else if (complaint < 1500) return 1.9f;
		else if (complaint < 2000) return 2.2f;
		else if (complaint < 3000) return 2.5f;
		else if (complaint < 5000) return 2.8f;
		else if (complaint < 7000) return 3.1f;
		else if (complaint < 10000) return 3.4f;
		else return 3.7f;
	}
}
/* **************************************************************** */




class OrderOfPriorityWindow extends JFrame implements TreeCellRenderer {
	private JLabel title = new JLabel("        The order of priority        ");
	private JScrollPane displayPanel;
	private GraphicsPanel gpanel;
	private JTree tree;
	JPopupMenu popup = new JPopupMenu();

	OrderOfPriorityWindow(GraphicsPanel gpanel) {
		super("The order of priority");
		this.gpanel = gpanel;

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		add(title, BorderLayout.NORTH);
		initTree();
		displayPanel = new JScrollPane(tree);
		add(displayPanel, BorderLayout.CENTER);

		setLocation(827, 40);

		popupInit();

		pack();
		setVisible(true);
	}

	public void popupInit() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		tree.setEditable(true);
		tree.addMouseListener(new MouseClickHandler());
		for (int i = 1; i <= 10; i++) {
			JMenuItem editMenu = new JMenuItem("move to level"+i);
			editMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					tree.startEditingAtPath(tree.getSelectionPath());
				}
			});
			popup.add(editMenu);
		}
	}
	private class MouseClickHandler extends MouseAdapter {
		public void mouseReleased(MouseEvent event) {
			TreePath path = tree.getPathForLocation(event.getX(), event.getY());
			if (path != null) {
				tree.setSelectionPath(path);
				String pathString = path.toString();
				if (event.isPopupTrigger() && isSpringLeaf(pathString)) {
					popup.show(tree, event.getX(), event.getY());
				}
			}
		}
	}
	private boolean isSpringLeaf(String pathString) {
		String constStr = "[The order of priority, order_of_";
		for (int i = 1; i <= gpanel.getSprings().size(); i++) {
			if (pathString.equals(constStr+"spring, level "+i+", spring"+i+"]")){
				return true;
			}
		}
		for (int i = 1; i <= gpanel.getColorLocks().size(); i++) {
			if (pathString.equals(constStr+"key, level "+i+", key"+i+"]")) {
				return true;
			}
		}
		return false;
	}

	public Component getTreeCellRendererComponent(
			JTree tree, Object value, boolean selected,
			boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		JLabel label = new JLabel(value.toString());
		if (leaf) {
			label.setIcon(new ImageIcon("icon/leaf_spring.gif"));
			if (selected) label.setForeground(Color.RED);
		}
		return label;
	}

	void initTree() {
		DefaultMutableTreeNode springRoot = new DefaultMutableTreeNode("order_of_spring");
		DefaultMutableTreeNode keyRoot = new DefaultMutableTreeNode("order_of_key");
		DefaultMutableTreeNode[] spring_node = new DefaultMutableTreeNode[10];
		DefaultMutableTreeNode[] key_node = new DefaultMutableTreeNode[10];
		for (int i = 0; i < 10; i++) {
			spring_node[i] = new DefaultMutableTreeNode("level "+(i+1));
			key_node[i] = new DefaultMutableTreeNode("level "+(i+1));
			springRoot.add(spring_node[i]);
			keyRoot.add(key_node[i]);
		}

		MutableTreeNode testNode = new DefaultMutableTreeNode("spring1", false);
		spring_node[0].add(testNode);

		MutableTreeNode seed_node[] = {
				springRoot, keyRoot
		};

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("The order of priority");
		for (int i = 0; i < seed_node.length; i++) root.add(seed_node[i]);

		tree = new JTree(root);
		//tree.setCellRenderer(this);
	}
}
