package jp.ac.titech.is.wakitalab.color.shibata;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
import javax.swing.event.MouseInputAdapter;

import jp.ac.titech.is.wakitalab.color.shimamura.CompoundDesire;
import jp.ac.titech.is.wakitalab.color.shimamura.DesiredContrast;
import jp.ac.titech.is.wakitalab.color.shimamura.DesiredKeepability;
import jp.ac.titech.is.wakitalab.color.shimamura.Dichromat;
import jp.ac.titech.is.wakitalab.color.shimamura.LMS;
import jp.ac.titech.is.wakitalab.color.shimamura.Lab;
import jp.ac.titech.is.wakitalab.color.shimamura.SRGB;
import jp.ac.titech.is.wakitalab.color.shimamura.SimulatedAnnealing;


abstract class MouseListener extends MouseInputAdapter {
	private GraphicsPanel gpanel;
	private Point pStart;
	private ACShape shape = null;
	
	MouseListener(GraphicsPanel gpanel) {
		this.gpanel = gpanel;
	}
	
	static final int G = 20;
	
	static void adjustToGrid(Point p) {
		p.move((int)p.getX() / G * G, (int)p.getY() / G * G);
	}
	
	abstract ACShape createNewShape(Point p);
	
	public void mousePressed(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1)
			return;
		
		pStart = e.getPoint();
		adjustToGrid(pStart);
		shape = createNewShape(pStart);
		shape.save();
		
		gpanel.setDrawing(shape);
		gpanel.repaint();
	}
	
	public void mouseDragged(MouseEvent e) {
		if (shape == null)
			return;
		
		Point pEnd = e.getPoint();
		adjustToGrid(pEnd);
		shape.reshape(pStart, pEnd, e.isShiftDown());
		gpanel.repaint();
	}
	
	public void mouseReleased(MouseEvent e) {
		if (shape == null) return;
		
		Point pEnd = e.getPoint();
		adjustToGrid(pEnd);
		if (pStart.getX() != pEnd.getX() && pStart.getY() != pEnd.getY()) {
			shape.reshape(pStart, pEnd, e.isShiftDown());
			gpanel.addShape();
		} else {
			gpanel.setDrawing(null);
		}
		shape = null;
		gpanel.repaint();
	}
}
/* **************************************************************** */



class PointerListener extends MouseInputAdapter {
	private GraphicsPanel gpanel;
	private Point pStart;
	private Rectangle range = null;
	private int mode = NONE;
	private boolean dragging = false;
	private int pos;
	
	private static final int NONE = 0;
	private static final int MOVE = 1;
	private static final int RESHAPE = 2;
	
	PointerListener(GraphicsPanel gpanel) {
		this.gpanel = gpanel;
	}
	
	void reshape(MouseEvent e) {
		Point p = e.getPoint();
		MouseListener.adjustToGrid(p);
		int x, y, width, height;
		
		x = Math.min(pStart.x, p.x);
		y = Math.min(pStart.y, p.y);
		width = Math.abs(p.x - pStart.x);
		height = Math.abs(p.y - pStart.y);
		
		range.setBounds(x, y, width, height);
		gpanel.repaint();
	}
	
	void change(MouseEvent e) {
		Vector selections = gpanel.getSelections();
		Vector chSelections = gpanel.getChSelections();
		Point p = e.getPoint();
		MouseListener.adjustToGrid(p);
		
		if (mode == NONE)
			return;
		
		switch(mode) {
		case MOVE:
			for (int i = 0; i < selections.size(); i++) {
				ACShape shape = (ACShape)selections.get(i);
				shape.move(pStart, p);
				
				reMoveSpring(shape);
				
				reMoveLock(shape);
			}
			for (int i = 0; i < chSelections.size(); i++) {
				ACCharacter ch = (ACCharacter)chSelections.get(i);
				ch.move(pStart, p);
			}
			break;
			
		case RESHAPE:
			for (int i = 0; i < selections.size(); i++) {
				ACShape shape = (ACShape)selections.get(i);
				shape.reshape(pStart, p, pos, e.isShiftDown());
				
				reMoveSpring(shape);
				reMoveLock(shape);
			}
			for (int i = 0; i < chSelections.size(); i++) {
				ACCharacter ch = (ACCharacter)chSelections.get(i);
				ch.reshape(pStart, p, pos);
			}
			break;
		}
		
		gpanel.repaint();
	}
	
	private void reMoveSpring(ACShape shape) {
		Vector springs = gpanel.getSprings();
		for (int j = 0; j < springs.size(); j++) {
			Spring spring = (Spring)springs.get(j);
			int oneId = spring.myShape.getObjectNumber();
			int otherId = spring.yourShape.getObjectNumber();
			if (shape.getObjectNumber()==oneId || shape.getObjectNumber()==otherId) {
				spring.reSetFirstClaspPoint();
				spring.reSetSecondClaspPoint();
				Point firstCPoint = spring.getFirstClaspPoint();
				Point secondCPoint = spring.getSecondClaspPoint();
				Point2D.Double axisPoint = spring.getSpringAxisPoint();
				spring.setSpringPosition(firstCPoint, secondCPoint);
				spring.springCore.setLocation(axisPoint.x, axisPoint.y);
				double sin = (secondCPoint.y-firstCPoint.y)/spring.distance(firstCPoint, secondCPoint);
				double cos = (secondCPoint.x-firstCPoint.x)/spring.distance(firstCPoint, secondCPoint);
				spring.springCore.setRotateAngle(sin, cos);
				spring.reSetLine();
			}
		}
	}
	private void reMoveLock(ACShape shape) {
		Vector locks = gpanel.getColorLocks();
		for (int i = 0; i < locks.size(); i++) {
			ColorLock lock = (ColorLock)locks.get(i);
			int id = lock.lockedShape.getObjectNumber();
			if (shape.getObjectNumber() == id) {
				lock.reSetConnectPoint();
				lock.setColorLockPosition();
				lock.reSetLine();
			}
		}
	}
	
	public void mousePressed(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1)
			return;
		
		pStart = e.getPoint();
		// MouseListener.adjustToGrid(pStart);
		
		if (e.isControlDown()) {
			range = new Rectangle(e.getPoint());
			gpanel.setSelecting(range);
			return;
		}
		
		Vector selections = gpanel.getSelections();
		Vector chSelections = gpanel.getChSelections();
		
		for (int i = selections.size()-1; i >= 0; i--) {
			ACShape shape = (ACShape)selections.get(i);
			pos = shape.getHandlePosition(pStart);
			if (pos != ACShape.NONE) {
				mode = RESHAPE;
				gpanel.select(shape, false);
				return;
			}
		}
		for (int i = chSelections.size()-1; i >= 0; i--) {
			ACCharacter ch = (ACCharacter)chSelections.get(i);
			pos = ch.getHandlePosition(pStart);
			if (pos != ACCharacter.NONE) {
				mode = RESHAPE;
				return;
			}
		}
		
		for (int i = selections.size()-1; i >= 0; i--) {
			ACShape shape = (ACShape)selections.get(i);
			if (shape.contains(pStart)) {
				mode = MOVE;
				return;
			}
		}
		for (int i = chSelections.size()-1; i >= 0; i--) {
			ACCharacter ch = (ACCharacter)chSelections.get(i);
			if (ch.bounds.contains(pStart)) {
				mode = MOVE;
				return;
			}
		}

		Vector characters = gpanel.getCharacters();
		for (int i = characters.size()-1; i >= 0; i--) {
			ACCharacter ch = (ACCharacter)characters.get(i);
			if (ch.bounds.contains(pStart)) {
				gpanel.chSelect(ch, e.isShiftDown());
				return;
			}
		}

		Vector shapes = gpanel.getShapes();

		for (int i = shapes.size()-1; i >= 0; i--) {
			ACShape shape = (ACShape)shapes.get(i);
			if (shape.contains(pStart)) {
				gpanel.select(shape, e.isShiftDown());
				return;
			}
		}
		
		gpanel.unselectAll();
	}
	
	public void mouseDragged(MouseEvent e) {
		if (range != null) {
			reshape(e);
			return;
		}
		
		if (!dragging) {
			Vector selections = gpanel.getSelections();
			for (int i = 0; i < selections.size(); i++) {
				ACShape shape = (ACShape)selections.get(i);
				shape.save();
			}
			Vector chSelections = gpanel.getChSelections();
			for (int i = 0; i < chSelections.size(); i++) {
				ACCharacter ch = (ACCharacter)chSelections.get(i);
				ch.save();
			}
			dragging = true;
		}
		
		change(e);
	}
	
	public void mouseReleased(MouseEvent e) {
		if (range != null) {
			selectRange(e);
			return;
		}
		
		if (!dragging)
			return;
		
		change(e);
		dragging = false;
		mode = NONE;
	}
	
	private void selectRange(MouseEvent e) {
		reshape(e);
		
		Vector shapes = gpanel.getShapes();
		
		gpanel.unselectAll();
		
		for (int i = 0; i < shapes.size(); i++) {
			ACShape shape = (ACShape)shapes.get(i);
			if (shape.intersects(range))
				gpanel.select(shape, true);
		}
		
		gpanel.setSelecting(null);
		range = null;
		
		gpanel.repaint();
	}
}

class ToolButton extends JToggleButton implements ActionListener {
	GraphicsPanel gpanel;
	MouseInputAdapter listener = null;
	boolean settingMode = false;
	
	ToolButton(GraphicsPanel gpanel, ImageIcon icon, boolean settingMode) {
		super(icon);
		this.gpanel = gpanel;
		this.settingMode = settingMode;
		
		addActionListener(this);
	}
	
	void setMouseListener(MouseInputAdapter listener) {
		this.listener = listener;
	}
	
	public void actionPerformed(ActionEvent e) {
		gpanel.setListener(listener);
		if (settingMode) {gpanel.color_difference_settingMode_ON(); }
		else { gpanel.color_difference_settingMode_OFF(); }
	}
}
/* **************************************************************** */



class LineListener extends MouseListener {
	LineListener(GraphicsPanel gpanel) {
		super(gpanel);
	}
	
	ACShape createNewShape(Point p) {
		p.move((int)p.getX() / 10 * 10, (int)p.getY() / 10 * 10);
		return new ACLine(p);
	}
}
/* *************************************************************** */



class LineWidthListener implements ActionListener {
	private GraphicsPanel gpanel;
	private int inc;
	
	private static final int MAX_LINEWIDTH = 8;
	
	LineWidthListener(GraphicsPanel gpanel, int inc) {
		this.gpanel = gpanel;
		this.inc = inc;
	}
	
	public void actionPerformed(ActionEvent e) {
		gpanel.color_difference_settingMode_OFF();
		Vector selections = gpanel.getSelections();
		
		if (selections.isEmpty()) {
			int lineWidth = ACShape.getDefaultLineWidth();
			
			lineWidth += inc;
			
			if (lineWidth < 0)
				lineWidth = 0;
			if (lineWidth > MAX_LINEWIDTH)
				lineWidth = MAX_LINEWIDTH;
			
			ACShape.setDefaultLineWidth(lineWidth);
		} else {
			for (int i = 0; i < selections.size(); i++) {
				ACShape shape = (ACShape)selections.get(i);
				
				int lineWidth = shape.getLineWidth();
				
				lineWidth += inc;
				
				if (lineWidth < 0)
					lineWidth = 0;
				if (lineWidth > MAX_LINEWIDTH)
					lineWidth = MAX_LINEWIDTH;
				
				shape.setLineWidth(lineWidth);
			}
			gpanel.repaint();
		}
	}
}
/* **************************************************************** */



class RectangleListener extends MouseListener {
	RectangleListener(GraphicsPanel gpanel) {
		super(gpanel);
	}
	
	ACShape createNewShape(Point p) {
		return new ACRectangle(p);
	}
}
/* **************************************************************** */




class RoundRectangleListener extends MouseListener {
	RoundRectangleListener(GraphicsPanel gpanel) {
		super(gpanel);
	}
	
	ACShape createNewShape(Point p) {
		return new ACRoundRectangle(p);
	}
}
/* **************************************************************** */




class PolygonListener extends MouseInputAdapter {
	private GraphicsPanel gpanel;
	// private Point pPrev;
	private ACPolygon polygon = null;
	
	PolygonListener(GraphicsPanel gpanel) {
		this.gpanel = gpanel;
	}
	
	public void mouseClicked(MouseEvent e) {
		Point p = e.getPoint();
		MouseListener.adjustToGrid(p);
		
		if (e.getClickCount() > 1) {
			if (polygon == null) return;
			polygon.close();
			gpanel.addShape();
			polygon = null;
		} else {
			if (polygon == null) {
				polygon = new ACPolygon();
				polygon.addPoint(p);
				gpanel.setDrawing(polygon);
			} else
				polygon.changePoint(p);
			polygon.addPoint(p);
			gpanel.repaint();
		}
		
		gpanel.repaint();
	}
	
	public void mouseMoved(MouseEvent e) {
		if (polygon == null)
			return;
		
		Point p = e.getPoint();
		MouseListener.adjustToGrid(p);
		polygon.changePoint(p);
		
		gpanel.repaint();
	}
}
/* **************************************************************** */



class CharacterListener extends MouseInputAdapter {
	private GraphicsPanel gpanel;
	// private int height = 16;
	
	CharacterListener(GraphicsPanel gpanel) {
		this.gpanel = gpanel;
	}
	
	public void mouseClicked(MouseEvent e) {
		Point startPoint = e.getPoint();
		String text = JOptionPane.showInputDialog(gpanel, "?????");
		if (text != null && text.length() > 0) {
			ACCharacter ch = new ACCharacter(startPoint, text);
			ch.save();
			gpanel.addCharacters(ch);
			//gpanel.addShape(ch);
		}
		gpanel.repaint();
		// debug System.out.println("repaint");
	}
}
/* **************************************************************** */



class EllipseListener extends MouseListener {
	EllipseListener(GraphicsPanel gpanel) {
		super(gpanel);
	}
	
	ACShape createNewShape(Point p) {
		return new ACEllipse(p);
	}
}
/* **************************************************************** */



class PaletteListener implements ActionListener {
	private GraphicsPanel gpanel;
	private int x = 40, y = 0, width = 40, height = 30;
	
	PaletteListener(GraphicsPanel gpanel) {
		this.gpanel = gpanel;
	}
	
	public void actionPerformed(ActionEvent e) {
		gpanel.color_difference_settingMode_OFF();
		Color c = JColorChooser.showDialog(null, "Choose Color", null);
		
		if (c == null) return;
		
		ACEllipse ellipse = new ACEllipse(new Point(x, y));
		ellipse.setBounds(x, y, width, height);
		ellipse.setFillColor(c);
		//ellipse.setRealColor(c);
		ellipse.save();
		ellipse.setIsPalette(true);
		gpanel.addShape(ellipse);
		gpanel.repaint();
		
		if (x < 720) x += width;
		else { x = 0; y += height; }
	}
}
/* **************************************************************** */



class ColorListener implements ActionListener {
	private GraphicsPanel gpanel;
	private int type;
	
	static int LINE = 0;
	static int FILL = 1;
	
	ColorListener(GraphicsPanel gpanel, int type) {
		this.gpanel = gpanel;
		this.type = type;
	}
	
	public void actionPerformed(ActionEvent e) {
		gpanel.color_difference_settingMode_OFF();
		Vector selections = gpanel.getSelections();
		Vector chSelections = gpanel.getChSelections();
		Color c = JColorChooser.showDialog(null, "Choose Color", null);
		
		if (c == null)
			return;
		
		if (selections.isEmpty() && chSelections.isEmpty()) {
			if (type == LINE)
				ACShape.setDefaultLineColor(c);
			else if (type == FILL) {
				ACShape.setDefaultFillColor(c);
				gpanel.repaint();
			}
		} else {
			for (int i = 0; i < selections.size(); i++) {
				ACShape shape = (ACShape)selections.get(i);
				if (type == LINE) {
					shape.setLineColor(c);
					if (shape.getTypeName().equals(ACLine.TYPE_NAME)) {
						shape.setFillColor(c);
						//shape.setRealColor(c);
						ColorLock lock = new ColorLock(gpanel, shape, shape.getConnectPoint(), new LockLine());
						gpanel.setColorLock(lock);
						shape.setNaturalColor();
					}
				} else if (type == FILL) {
					if (shape.isBackgroundPalette()) gpanel.setBackground(c);
					shape.setFillColor(c);
					
					if (shape.getNaturalColor() == null && !shape.isBackgroundPalette() && !shape.isPalette()) {
						ColorLock lock = new ColorLock(gpanel, shape, shape.getConnectPoint(), new LockLine());
						gpanel.setColorLock(lock);
					}
					shape.setNaturalColor();
					if (shape.getTypeName().equals(ACLine.TYPE_NAME))
						shape.setLineColor(c);
				}
			}
			for (int i = 0; i < chSelections.size(); i++) {
				ACCharacter ch = (ACCharacter)chSelections.get(i);
				ch.setFontColor(c);
			}
			gpanel.repaint();
		}
	}
}
/* **************************************************************** */




class SetContrastListener implements ActionListener {
	private GraphicsPanel gpanel;
	private Vector springs;
	private Vector shapes;
	// private Vector<ColorLock> colorLocks;
	private double[][] color_distance, desirability;
	private Color[] natural_colors;
	private double[] naturalColor_desirability;
	private static String[] TYPES = {
		"�O�F�o��", "����F�o��V", "����F�o��V", "��O��F�o��V"
	};
	
	SetContrastListener(GraphicsPanel gpanel) {
		this.gpanel = gpanel;
		springs = gpanel.getSprings();
		shapes = gpanel.getShapes();
		// colorLocks = gpanel.getColorLocks();
	}
	
	private void matrix_init() {
		int size = gpanel.getShapes().size();
		color_distance = new double[size][size];
		desirability = new double[size][size];
	}
	private void array_init() {
		int size = gpanel.getShapes().size();
		natural_colors = new Color[size];
		naturalColor_desirability = new double[size];
	}
	
	@SuppressWarnings("unused")
	private void displayMatrix(double[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				System.out.print(array[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	/* �s�����O�p�� */
	private double[][] setUpperTriangle(double[][] array) {
		double[][] answer = (double[][])array.clone();
		//displayMatrix(answer);
		for (int i = 0; i < answer.length; i++) {
			for (int j = i+1; j < answer[i].length; j++) {
				answer[i][j] += answer[j][i];
			}
			for (int j = 0; j < i; j++) {
				answer[i][j] = 0.f;
			}
		}
		
		return answer; 
	}
	
	private void setColorDistance_and_desirability() {
		matrix_init();
		//springs = gpanel.getSprings();
		
		for (int i = 0; i < springs.size(); i++) {
			Spring spring = (Spring)springs.get(i);
			int myIndex = spring.myShape.getIndexOfArray();
			int yourIndex = spring.yourShape.getIndexOfArray();
			color_distance[myIndex][yourIndex] = spring.color_difference;
			desirability[myIndex][yourIndex] = spring.desirability;
			//System.out.println("spring.desirability = "+spring.desirability);
		}
	}
	
	private void setNatural_colors() {
		array_init();
		//shapes = gpanel.getShapes();
		/* i��shape��arrayIndexNumber�ɂȂ��Ă��� */
		for (int i = 0; i < shapes.size(); i++) {
			ACShape shape = (ACShape)shapes.get(i);
			if (shape.getNaturalColor() != null) {
				natural_colors[i] = shape.getNaturalColor();
				naturalColor_desirability[i] = shape.key.desirability;
			} else /* if (shape.getNaturalColor() == null) */ {
				/* �Ƃ肠����idealLab��ݒ肵�A�d����0�Ƃ���. */
				natural_colors[i] = shape.fillColor;
				naturalColor_desirability[i] = 0;
			}
		}
	}
	
	/**
	 * ��ɗ^����ꂽColor���1�F�ӎ҂̐F�ɕϊ�����
	 * @param color : �ϊ��������F
	 * @return color�̑�1�F�ӎ҂̌���
	 */
	private Color change_to_protanope(Color color) {
		SRGB srgb = new SRGB(color);	/* PC��̐F */
		LMS lms = srgb.getLMS();	/* �l�Ԃ̐��̔����ɑΉ������F�N���X */
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
		
		return rgbColor;
	}
	
	/**
	 * ��ɗ^����ꂽColor���2�F�ӎ҂̐F�ɕϊ�����
	 * @param color �F�ϊ��������F
	 * @return color�̑�2�F�ӎ҂̌���
	 */
	private Color change_to_deuteranope(Color color) {
		SRGB srgb = new SRGB(color);	/* PC��̐F */
		LMS lms = srgb.getLMS();	/* �l�Ԃ̐��̔����ɑΉ������F�N���X */
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
		
		return rgbColor;
	}
	
	/**
	 * ��ɗ^����ꂽColor���3�F�ӂ̐F�ɕϊ�����
	 * @param color : �ϊ��������F
	 * @return color�̑�3�F�ӎ҂̌���
	 */
	private Color change_to_tritanope(Color color) {
		SRGB srgb = new SRGB(color);	/* PC��̐F */
		LMS lms = srgb.getLMS();	/* �l�Ԃ̐��̔����ɑΉ������F�N���X */
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
		
		return rgbColor;
	}
	
	public void actionPerformed(ActionEvent e) {
		gpanel.color_difference_settingMode_OFF();
		
		/* ���z�I�ȐF(Natural Color)�̕ۑ� */
		for (ColorLock lock : gpanel.getColorLocks()) {
			lock.origColor = lock.lockedShape.fillColor;
			lock.lockedShape.setNaturalColor();	// �����NaturalColoring��.
		}
		
		int type = Dichromat.TRICHROMAT;
		Object selectType = JOptionPane.showInputDialog(
				gpanel, "�F�Ӄ^�C�v��I�����Ă�������", "�I��",
				JOptionPane.QUESTION_MESSAGE, null, TYPES, TYPES[2]);
		
		/* �F�Ӄ^�C�v���I������Ȃ������ꍇ */
		if (selectType == null) { return; }
		
		/* �F�Ӄ^�C�v�ݒ� */
		if (selectType.equals("�O�F�o��")) type = Dichromat.TRICHROMAT;
		else if (selectType.equals("����F�o��V")) type = Dichromat.PROTANOPE;
		else if (selectType.equals("����F�o��V")) type = Dichromat.DEUTERANOPE;
		else if (selectType.equals("��O��F�o��V")) type = Dichromat.TRITANOPE;
	
		/* �΂ˁA���̏���z��ɓ��� */
		setColorDistance_and_desirability();
		setNatural_colors();
		
		/* ��]�̐ݒ� */
		CompoundDesire compoundDesire = new CompoundDesire(shapes.size());
		double[][] contrastIdeal = setUpperTriangle(color_distance);	// �]�݂̐F���̔z��
		double[][] contrastGravity = setUpperTriangle(desirability);	// �F�̑g�ݍ��킹�ɑ΂���d�v�x
		//displayMatrix(contrastIdeal);	// debug
		//System.out.println();
		//displayMatrix(contrastGravity);	// debug
		DesiredContrast cDesire = new DesiredContrast(contrastIdeal, contrastGravity, type);
		compoundDesire.add(cDesire);
		
		Lab[] keepIdealLabs = new Lab[natural_colors.length];	// �ێ��������F�̔z��
		
		/* Color�z��natural_colors��Lab�z��keepIdealLabs�ɕϊ����� */
		for (int i = 0; i < natural_colors.length; i++) {
			//if (natural_colors[i] == null) System.out.println("natural_colors["+i+"]�Ԗڂ�null");
			SRGB srgb = new SRGB(natural_colors[i]);
			keepIdealLabs[i] = srgb.getLab();
		}
		double[] keepGravity = naturalColor_desirability;	// �e�F�ɑ΂���F�����̂܂ܕێ����邱�Ƃ̏d�v�x
		
		DesiredKeepability kDesire = new DesiredKeepability(keepIdealLabs, keepGravity, type);
		compoundDesire.add(kDesire);
		
		/* ACShape�̐F��SRGB�ŏ��� */
		Color[] colors = new Color[shapes.size()];
		for (int i = 0; i < shapes.size(); i++) {
			ACShape shape = (ACShape)shapes.get(i);
			colors[i] = shape.fillColor;
		}
		SRGB[] srgb_colors = new SRGB[colors.length];
		for (int i = 0; i < colors.length; i++) {
			srgb_colors[i] = new SRGB(colors[i]);
		}
		/* SRGB�ŏ���I��� */
		
		Lab[] labs = new Lab[srgb_colors.length];
		for (int i = 0; i < labs.length; i++) {
			labs[i] = srgb_colors[i].getLab();
		}
		
		/**
		 * �s���x�̍����Ƃ��낾�����Ĕz�F�Ƃ��Ɏg���H
		 * �p���b�g�̐F�͌Œ肷�ׂ�����ˁB
		 */
		boolean[] fixColoring = new boolean[shapes.size()];
		for (int i = 0; i < fixColoring.length; i++) {
			// �ƁA�������ƂŁA�p���b�g�̐F���Œ�
			ACShape shape = (ACShape)shapes.get(i);
			if (shape.isBackgroundPalette() || shape.isPalette()) fixColoring[i] = true;
		}
		/**
		for (int i = 0; i < fixColoring.length; i++) {
			System.out.println(fixColoring[i]);
		}
		*/
		
		SimulatedAnnealing annealing = new SimulatedAnnealing(compoundDesire, srgb_colors, fixColoring, type);
		Lab[] newLabs;
		Lab[] afterlabs;
		if (type == Dichromat.TRICHROMAT) {
			newLabs = annealing.solveTrichromat(100000.0, 200, 1.0, 0.95, 10.0, 4, 0.5);	// ����җp
		} else {
			newLabs = annealing.solve(100000.0, 200, 1.0, 0.95, 10.0, 4, 0.5);	// �F�ӎҗp
		}
		afterlabs = newLabs;
		
		SRGB[] newSrgbs = new SRGB[newLabs.length];
		for (int i = 0; i < newSrgbs.length; i++) {
			newSrgbs[i] = newLabs[i].getSRGB();	// Lab�z���SRGB�z��ɕϊ�
		}
		Color[] newColors = new Color[newSrgbs.length];
		for (int i = 0; i < newColors.length; i++) {
			float red = (float)newSrgbs[i].getValue1();
			float green = (float)newSrgbs[i].getValue2();
			float blue = (float)newSrgbs[i].getValue3();
			//System.out.println("(r, g, b) = ("+red+", "+green+", "+blue+")");
			if (red < 0) {red = 0.0f;}
			if (green < 0) {green = 0.0f;}
			if (blue < 0) {blue = 0.0f;}
			if (red > 1.0f) {red = 1.0f;}
			if (green > 1.0f) {green = 1.0f;}
			if (blue > 1.0f) {blue = 1.0f;}
			newColors[i] = new Color(red, green, blue);	// SRGB�z���Color�z��ɕϊ�
		}
		
		/* �z�F���ʂ�}�`�ɐݒ肷�� */
		for (int i = 0; i < shapes.size(); i++) {
			ACShape shape = (ACShape)shapes.get(i);
			//shape.setRealColor(newColors[i]);
			/* newColors[i]���C�F�Ӄ^�C�v�ɂ��ϊ������F��fillColor�ɓ��� */
			if (type == Dichromat.TRICHROMAT) shape.setFillColor(newColors[i]);
			else if (type == Dichromat.PROTANOPE) shape.setFillColor(change_to_protanope(newColors[i]));
			else if (type == Dichromat.DEUTERANOPE) shape.setFillColor(change_to_deuteranope(newColors[i]));
			else if (type == Dichromat.TRITANOPE) shape.setFillColor(change_to_tritanope(newColors[i]));
			
			//shape.setFillColor(newColors[i]);
			if (shape.isBackgroundPalette()) {
				gpanel.setBackground(newColors[i]);
				System.out.println("setBackground = "+newColors[i]);
			}
			
			//System.out.println("color["+i+"] = "+newColors[i]);
		}
		
		/**
		System.out.println();
		for (int i = 0; i < colorLocks.size(); i++) {
			ColorLock lock = (ColorLock)colorLocks.get(i);
			System.out.println("lock("+i+").lockedShape.fillColor = "+lock.lockedShape.fillColor);
		}
		*/
		
		gpanel.repaint();
		
		/*
		 * ���҂ɂƂ��Ă͖����E�s�����͔z�F�Ӑ}��^�����Ƃ���ɂ̂ݐ����邩��A
		 * �΂˂ɖ����x��ێ�����t�B�[���h��p�ӂ��āA����ɓ���
		 * 
		 * �s���x�̎Z�o�́A
		 * compoundDesire�N���X��energy()���\�b�h.
		 * energy(Lab[]) : ���̐F�S�̂̕s���x�̍��v
		 * energy(Lab[], int num) : newColors[num]�Ɋւ���s���x�̍��v
		 * energy(Lab[], int i, int j) : newColors[i], newColors[j]�Ɋւ���s���x�̍��v
		 * 
		 *  ���v�Ƃ́c�R���g���X�g�A�F�̎��R���̓��.
		 *  �R���g���X�g�݂̂̕s���x��baseOfContrast(),
		 *  ���R���̕s���x��baseOfKeepability()
		 *  ��͏��3���.
		 */
		for (int i = 0; i < springs.size(); i++) {
			// �R���g���X�g�݂̂̕s���x�ݒu
			Spring spring = (Spring)springs.get(i);
			int myIndex = spring.myShape.getIndexOfArray();
			int yourIndex = spring.yourShape.getIndexOfArray();
			
			/* baseOfContrast���s��̏�O�p�݂̂ɒl������Ă��邱�Ƃɂ� */
			double complaint;
			
			
			/**
			 * �����I�ɒǉ�����
			 * �����炭����ŕs���x�̕\�����������Ȃ�͂�
			 */
			Color[] afterColors = new Color[shapes.size()];
			for (int j = 0; j < shapes.size(); j++) {
				afterColors[j] = ((ACShape)shapes.get(j)).fillColor;
			}
			SRGB[] aftersrgb_colors = new SRGB[afterColors.length];
			for (int j = 0; j < afterColors.length; j++) {
				aftersrgb_colors[j] = new SRGB(afterColors[j]);
			}
			/* SRGB�ŏ���I��� */
			
			afterlabs = new Lab[aftersrgb_colors.length];
			for (int j = 0; j < afterlabs.length; j++) {
				afterlabs[j] = aftersrgb_colors[j].getLab();
			}
			/* �ǉ������I�� */
			
			
			if (myIndex > yourIndex) complaint = compoundDesire.baseOfContrast(/*newLabs*/afterlabs, yourIndex, myIndex);
			else complaint = compoundDesire.baseOfContrast(/*newLabs*/afterlabs, myIndex, yourIndex);
			spring.setComplaint(complaint);
			
			spring.setRealContrast(/*newLabs*/afterlabs[myIndex].getColorDistance76(/*newLabs*/afterlabs[yourIndex]));
			//System.out.println("("+myIndex+" ,"+yourIndex+") = "+spring.getComplaint());
		}
		
		for (ColorLock lock : gpanel.getColorLocks()) {
			int arrayIndex = lock.lockedShape.getIndexOfArray();
			lock.setComplaint(compoundDesire.baseOfKeep(/*newLabs*/afterlabs, arrayIndex));
		}
	}
}
/* **************************************************************** */






/* ************************* �΂˂̃��X�i ************************* */
class SpringListener extends MouseInputAdapter {
	protected GraphicsPanel gpanel;
	private Vector shapes;
	@SuppressWarnings("unused")
	private Vector palettes;
	private ACShape firstShape;
	private ACShape partnerShape;
	Point clickedPoint;
	Point pStart, pEnd;
	int xStart, yStart, xEnd, yEnd;
	int myNumber, yourNumber;
	// ���R�[�h boolean first = true;
	
	SpringListener(GraphicsPanel gpanel) {
		this.gpanel = gpanel;
		shapes = gpanel.getShapes();
		//palettes = gpanel.getPalettes();
	}
	
	void renewPStart(Point claspPoint) { pStart = claspPoint; }
	
	JPopupMenu createPopupMenu(Spring spring) {
		// ��ɗ^����ꂽ�΂˃I�u�W�F�N�g�p�̃|�b�v�A�b�v���j���[���쐬����
		JPopupMenu popup = new JPopupMenu();
		JMenu color_distance_popupMenu = new JMenu("�R���g���X�g");
		JMenu desirability_popupMenu = new JMenu("�d�v��");
		
		popup.insert(color_distance_popupMenu, 0);
		
		ButtonGroup group = new ButtonGroup();
		JMenuItem clevel9 = new JRadioButtonMenuItem("" + 9);
		group.add(clevel9);
		clevel9.addActionListener(new ColorDistanceListener(spring, Spring.DLEVEL9, gpanel));
		color_distance_popupMenu.add(clevel9);
		for (int i = 8; i >= 0; i--) {
			int dLevel;
			if (i == 8) dLevel = Spring.DLEVEL8;
			else if (i == 7) dLevel = Spring.DLEVEL7;
			else if (i == 6) dLevel = Spring.DLEVEL6;
			else if (i == 5) dLevel = Spring.DLEVEL5;
			else if (i == 4) dLevel = Spring.DLEVEL4;
			else if (i == 3) dLevel = Spring.DLEVEL3;
			else if (i == 2) dLevel = Spring.DLEVEL2;
			else if (i == 1) dLevel = Spring.DLEVEL1;
			else /* if (i == 0) */ {
				JMenuItem clevelItem = new JRadioButtonMenuItem(""+i);
				group.add(clevelItem);
				clevelItem.addActionListener(new ColorDistanceListener(spring, Spring.DLEVEL0, gpanel));
				color_distance_popupMenu.add(clevelItem);
				continue;
			}
			JMenuItem clevelItem = new JRadioButtonMenuItem(""+i);
			group.add(clevelItem);
			clevelItem.addActionListener(new ColorDistanceListener(spring, dLevel, gpanel));
			color_distance_popupMenu.add(clevelItem);
		}
		
		group = new ButtonGroup();
		popup.insert(desirability_popupMenu, 1);
		JMenuItem dLevel9 = new JRadioButtonMenuItem("9");
		group.add(dLevel9);
		dLevel9.addActionListener(new DesirabilityListener(spring, Spring.SPRINGDESIRELEVEL10, gpanel));
		desirability_popupMenu.add(dLevel9);
		
		for (int i = 8; i >= 0; i--) {
			int desire = i;
			switch(i) {
			case 0: desire = Spring.SPRINGDESIRELEVEL1; break;
			case 1: desire = Spring.SPRINGDESIRELEVEL2; break;
			case 2: desire = Spring.SPRINGDESIRELEVEL3; break;
			case 3: desire = Spring.SPRINGDESIRELEVEL4; break;
			case 4: desire = Spring.SPRINGDESIRELEVEL5; break;
			case 5: desire = Spring.SPRINGDESIRELEVEL6; break;
			case 6: desire = Spring.SPRINGDESIRELEVEL7; break;
			case 7: desire = Spring.SPRINGDESIRELEVEL8; break;
			case 8: desire = Spring.SPRINGDESIRELEVEL9; break;
			}
			if (i == 0) {
				JMenuItem dLevelItem = new JRadioButtonMenuItem(""+i);
				group.add(dLevelItem);
				dLevelItem.addActionListener(new DesirabilityListener(spring, desire, gpanel));
				desirability_popupMenu.add(dLevelItem);
				continue;
			}
			JMenuItem dLevelItem = new JRadioButtonMenuItem(""+i);
			group.add(dLevelItem);
			dLevelItem.addActionListener(new DesirabilityListener(spring, desire, gpanel));
			desirability_popupMenu.add(dLevelItem);
		}
		popup.add(new JPopupMenu.Separator());
		
		JMenuItem deleteSpringItem = new JMenuItem("�΂˂̏���");
		deleteSpringItem.addActionListener(new DeleteSpringListener(gpanel, spring));
		popup.add(deleteSpringItem);
		
		return popup;
	}
	
	private boolean isSetting(ACShape firstShape, ACShape secondShape) {
		Vector springs = gpanel.getSprings();
		int firstId = firstShape.getObjectNumber();
		int secondId = secondShape.getObjectNumber();
		for (int i = 0; i < springs.size(); i++) {
			Spring spring = (Spring)springs.get(i);
			int spring_f_id = spring.myShape.getObjectNumber();
			int spring_s_id = spring.yourShape.getObjectNumber();
			
			boolean check1 = (firstId == spring_f_id) && (secondId == spring_s_id);
			boolean check2 = (firstId == spring_s_id) && (secondId == spring_f_id);
			if (check1 || check2) {
				return true;
			}
		}
		
		return false;
	}
	
	public void mousePressed(MouseEvent e) {
		gpanel.clearIsSettingShape();
		gpanel.repaint();
		if (e.isPopupTrigger()) {
			showPopup(e);
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		if (e.isPopupTrigger()) {
			showPopup(e);
		} else {
			shapes = gpanel.getShapes();
			ACShape shape;
			
			clickedPoint = e.getPoint();
			
			for (int i = shapes.size()-1; i >= 0; i--) {
				shape = (ACShape)shapes.get(i);
				if (shape.contains(clickedPoint)) {
					if (gpanel.getFirstShape_of_spring()) {
						gpanel.setClaspShape(shape);	/* �[�_�`��}�`�ɓo�^ */
						firstShape = shape;	/* �}�`�����ۑ� */
						myNumber = shape.getObjectNumber();
						
						/* *** debug *** */
						//if (firstShape.getClaspPoint()== null) {
						//	System.out.println("null");
						//	System.exit(0);
						//}
						/* ************* */
						
						/**
						 * ���R�[�h 
						xStart = firstShape.getClaspPoint().x;
						yStart = firstShape.getClaspPoint().y;
						pStart = new Point(xStart, yStart);
						 */   /* �ړ������Ƃ��ɕς�����K�v������ */
						
						gpanel.setFirstShape_of_spring(false);
					} else {
						if (shape.getObjectNumber() == firstShape.getObjectNumber()) {
							return;
						}
						if (isSetting(firstShape, shape)) {
							/* ��ɐݒu�����΂˂������� */
							gpanel.setFirstShape_of_spring(true);
							break;
						}
						/* �ŏ��̐}�`�ւ̐ڑ��_�𓾂� */
						xStart = firstShape.getClaspPoint().x;
						yStart = firstShape.getClaspPoint().y;
						pStart = new Point(xStart, yStart);
						
						gpanel.setClaspShape(shape);
						partnerShape = shape;
						yourNumber = shape.getObjectNumber();
						xEnd = partnerShape.getClaspPoint().x;
						yEnd = partnerShape.getClaspPoint().y;
						pEnd = new Point(xEnd, yEnd);
						gpanel.setFirstShape_of_spring(true);
						Spring spring = new Spring(firstShape, shape, pStart, pEnd, new SpringCore());
						spring.setPopupMenu(createPopupMenu(spring));
						gpanel.setSpring(spring);
						//System.out.println("set spring");	// debug
					}
					gpanel.repaint();	/* clasp�\�����߂�paintComponent�̒��� */
					return;
				}
			}
			
			// �N���b�N�����_���ǂ̐}�`���܂܂Ȃ��ꍇ
			// �w�i�Ɛ}�`�̂΂ːݒ�
		}
	}
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			showPopup(e);
		}
	}
	
	private void showPopup(MouseEvent e) {
		Point clickedPoint = e.getPoint();
		Vector springs = gpanel.getSprings();
		
		for (int i = springs.size()-1; i >= 0; i--) {
			Spring spring = (Spring)springs.get(i);
			//���R�[�h SpringImg springImg = spring.springImg;
			SpringCore springCore = spring.springCore;
			//���R�[�h Shape boundingBox = springImg.getBoundingBox();
			Shape boundingBox = springCore.getBoundingBox();
			if (boundingBox.contains(clickedPoint)) {
				Component com = (Component)e.getSource();
				if (e.isPopupTrigger()) {
					/* ���ۂ̃|�b�v�A�b�v���j���[�\������ */
					spring.getPopupMenu().show(com, e.getX(), e.getY());
				}
				
				// �I�����ꂽ�΂˂ɐڑ�����Ă���}�`��showHandle()����
				gpanel.setIsSettingShape(spring.myShape);
				gpanel.setIsSettingShape(spring.yourShape);
				
				gpanel.repaint();
				
				break;
			}
		}
	}
}
/* **************************************************************** */




/* ******************** �s���x�ݒ�΂˃��X�i ********************* */
class ComplaintSpringListener extends SpringListener {
	
	ComplaintSpringListener(GraphicsPanel gpanel) {
		super(gpanel);
	}
	
	/* �΂˂��N���b�N���ꂽ�Ƃ��ɕs���x�����|�b�v�A�b�v�\������ */
	public void mousePressed(MouseEvent e) {
		gpanel.clearIsSettingShape();
		showPopup(e); 
	}
	public void mouseEntered(MouseEvent e) {
		mousePressed(e);
	}
	public void mouseClicked(MouseEvent e) { showPopup(e); }
	public void mouseReleased(MouseEvent e) { showPopup(e); }
	
	/* �s���x��(���z�̐F���A�z�F���ʂ̐F���A���̖����x)�����Ƃ��ĕێ� */
	/**
	 * �s���x���x��
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
	 */
	
	/**
	 * 
	 */
	private String complaintLevel(double complaint) {
		String complaintMin = "�_�O";
		String complaintSmall = "��";
		String complaintNormal = "��";
		String complaintLarge = "�ǍD";
		String complaintHuge = "�D�G";
		
		if (complaint < 500) return complaintMin;
		else if (complaint < 1500) return complaintSmall;
		else if (complaint < 3000) return complaintNormal;
		else if (complaint < 7000) return complaintLarge;
		else return complaintHuge;
	}
	
	JPopupMenu createPopupMenu(Spring spring) {
		// ��ɗ^����ꂽ�΂˃I�u�W�F�N�g�p�̃|�b�v�A�b�v���j���[���쐬����
		
		JPopupMenu popup = new JPopupMenu();
		JLabel idealContrast = new JLabel(" ���z�I�Ȏ��ʐ� : "+spring.color_difference);
		JLabel resultContrast = new JLabel(" �ʐF��̎��ʐ� : "+(float)spring.getRealContrast());
		// JLabel complaint = new JLabel(" �s���x : "+complaintLevel(spring.getComplaint()));
		popup.add(idealContrast);
		popup.add(resultContrast);
		// popup.add(complaint);
		
		return popup;
	}
	private void showPopup(MouseEvent e) {
		Point clickedPoint = e.getPoint();
		Vector springs = gpanel.getSprings();
		
		for (int i = springs.size()-1; i >= 0; i--) {
			Spring spring = (Spring)springs.get(i);
			//���R�[�h SpringImg springImg = spring.springImg;
			SpringCore springCore = spring.springCore;
			//���R�[�h Shape boundingBox = springImg.getBoundingBox();
			Shape boundingBox = springCore.getBoundingBox();
			if (boundingBox.contains(clickedPoint)) {
				Component com = (Component)e.getSource();
				if (e.isPopupTrigger()) {
					/* ���ۂ̃|�b�v�A�b�v���j���[�\������ */
					spring.getPopupMenu().show(com, e.getX(), e.getY());
				}
				
				// �I�����ꂽ�΂˂ɐڑ�����Ă���}�`��showHandle()����
				gpanel.setIsSettingShape(spring.myShape);
				gpanel.setIsSettingShape(spring.yourShape);
			}
		}
		gpanel.repaint();
	}
}
/* *************************************************************** */




/* ********************* �s���x�ݒ茮���X�i ********************** */
class ComplaintLockListener extends LockListener {
	
	ComplaintLockListener(GraphicsPanel gpanel) {
		super(gpanel);
	}
	
	/* �����N���b�N���ꂽ�Ƃ��ɕs���x�����|�b�v�A�b�v�\������ */
	public void mousePressed(MouseEvent e) {
		gpanel.clearIsLockedShape();
		showPopup(e);
	}
	public void mouseClicked(MouseEvent e) { showPopup(e); }
	public void mouseReleased(MouseEvent e) { showPopup(e); }
	
	private String complaintLevel(double complaint) {
		String complaintMin = "�ɏ�";
		String complaintSmall = "��";
		String complaintNormal = "��";
		String complaintLarge = "��";
		String complaintHuge = "�ɑ�";
		
		if (complaint < 500) return complaintMin;
		else if (complaint < 1500) return complaintSmall;
		else if (complaint < 3000) return complaintNormal;
		else if (complaint < 7000) return complaintLarge;
		else return complaintHuge;
	}
	
	/**
	 * ��̌������b�N����}�`�̗��z�̐F�A�z�F���ʂ̐F�A
	 * ���̕s���x�����|�b�v�A�b�v���j���[���쐬����.
	 * @param ColorLock lock
	 */
	JPopupMenu createPopupMenu(ColorLock lock) {
		//��ɗ^����ꂽ���I�u�W�F�N�g�p�̃|�b�v�A�b�v���j���[���쐬����
		JPopupMenu popup = new JPopupMenu();
		Color ideal = lock.origColor;
		Color real = lock.lockedShape.fillColor;
		int idealRed = ideal.getRed();
		int idealBlue = ideal.getBlue();
		int idealGreen = ideal.getGreen();
		int realRed = real.getRed();
		int realBlue = real.getBlue();
		int realGreen = real.getGreen();
		JLabel idealColor = 
			new JLabel(" ���z�I�ȐF : (r, g, b) = ("+idealRed+", "+idealGreen+", "+idealBlue+")");
		JLabel resultColor = 
			new JLabel(" �ʐF��̐F : (r, g, b) = ("+realRed+", "+realGreen+", "+realBlue+")");
		JLabel complaint = new JLabel(" �s���x : "+complaintLevel(lock.getComplaint()));
		popup.add(idealColor);
		popup.add(resultColor);
		popup.add(complaint);
		
		return popup;
	}
	/**
	 * ���z�̐F�A�z�F���ʂ̐F�A���̕s���x�����|�b�v�A�b�v���j���[��\������
	 * @param MouseEvent e
	 */
	private void showPopup(MouseEvent e) {
		Point clickedPoint = e.getPoint();
		Vector colorLocks = gpanel.getColorLocks();
		
		for (int i = colorLocks.size()-1; i >= 0; i--) {
			ColorLock lock = (ColorLock)colorLocks.get(i);
			LockLine lockLine = lock.lockLine;
			Shape boundingBox = lockLine.getBoundingBox();
			if (boundingBox.contains(clickedPoint)) {
				Component com = (Component)e.getSource();
				if (e.isPopupTrigger()) {
					lock.getPopupMenu().show(com, e.getX(), e.getY());
				}
				
				// �I�����ꂽ���ɐڑ�����Ă���}�`��showHandle()����
				gpanel.setIsLockedShape(lock.lockedShape);
			}
		}
		gpanel.repaint();
	}
}
/* *************************************************************** */




/* **************** �e�F�����x�������΂��郊�X�i ***************** */
/* ���΂˂��Ƃɐݒu�̂��Ɓ� */
class ColorDistanceListener implements ActionListener {
	private GraphicsPanel gpanel;
	private int level;
	private Spring spring;
	
	ColorDistanceListener(Spring spring, int level, GraphicsPanel gpanel) {
		this.gpanel = gpanel;
		this.spring = spring;
		this.level = level;
	}
	
	/* �w�肳�ꂽ���x����^����ꂽSpring�I�u�W�F�N�g��level�ɐݒ� */
	public void actionPerformed(ActionEvent e) {
		//debug System.out.println("contrast level is " + level);
		spring.setColorDifference(level);
		((JRadioButtonMenuItem)e.getSource()).setSelected(true);
		//spring.color_difference = level;
		gpanel.clearIsSettingShape();
		gpanel.repaint();
	}
}
/* *************************************************************** */




/* ************* ��]�̑��������x�������΂��郊�X�i ************** */
class DesirabilityListener implements ActionListener {
	private GraphicsPanel gpanel;
	private int level;
	private Spring spring;
	
	DesirabilityListener(Spring spring, int level, GraphicsPanel gpanel) {
		this.gpanel = gpanel;
		this.spring = spring;
		this.level = level;
	}
	
	public void actionPerformed(ActionEvent e) {
		// debug System.out.println("desirability level is " + level);
		spring.setDesirability(level);
		((JRadioButtonMenuItem)e.getSource()).setSelected(true);
		gpanel.clearIsSettingShape();
		gpanel.repaint();
	}
}
/* *************************************************************** */




/* ******* NaturalColor�̊�]�̑傫�����x�������΂��郊�X�i ****** */
class NaturalColorDesirabilityListener implements ActionListener {
	private GraphicsPanel gpanel;
	private int level;
	private ColorLock lock;
	
	NaturalColorDesirabilityListener(ColorLock lock, int level, GraphicsPanel gpanel) {
		this.gpanel = gpanel;
		this.lock = lock;
		this.level = level;
	}
	
	public void actionPerformed(ActionEvent e) {
		lock.setDesirability(level);
		gpanel.clearIsLockedShape();
		gpanel.repaint();
	}
}
/* *************************************************************** */




/* ******************* �΂ˏ������Ǘ����郊�X�i ****************** */
class DeleteSpringListener implements ActionListener {
	private GraphicsPanel gpanel;
	private Vector springs;
	private Spring spring;
	
	DeleteSpringListener(GraphicsPanel gpanel, Spring spring) {
		this.gpanel = gpanel;
		springs = gpanel.getSprings();
		this.spring = spring;
	}
	
	public void actionPerformed(ActionEvent e) {
		springs.remove(spring);	/* �΂˂̏W������폜 */
		// claspShape���폜
		Vector claspShapes = gpanel.getClaspShapes();
		claspShapes.remove(spring.myShape);
		claspShapes.remove(spring.yourShape);
		
		gpanel.clearIsSettingShape();	/* �n���h������ */
		gpanel.repaint();
		//System.out.println("delete spring");	// debug
	}
}
/* *************************************************************** */




/* ****************** ���������Ǘ����郊�X�i ********************* */
class DeleteLockListener implements ActionListener {
	private GraphicsPanel gpanel;
	private Vector colorLocks;
	private ColorLock lock;
	
	DeleteLockListener(GraphicsPanel gpanel, ColorLock lock) {
		this.gpanel = gpanel;
		colorLocks = gpanel.getColorLocks();
		this.lock = lock;
	}
	
	public void actionPerformed(ActionEvent e) {
		lock.lockedShape.releasenaturalColor();
		colorLocks.remove(lock);	/* ���̏W������폜 */
		gpanel.clearIsLockedShape();
		gpanel.repaint();
		//System.out.println("delete lock");	// debug
	}
}
/* *************************************************************** */




/* ************* Natural Color���Ǘ����郊�X�i ******************* */
class LockListener extends MouseInputAdapter {
	protected GraphicsPanel gpanel;
	private Vector shapes;
	Point clickedPoint;
	//int id;	/* ���b�N����}�`�̎��ʔԍ�.���̌������Ԗڂ̐}�`�����b�N���Ă��邩���킩�� */
	
	LockListener(GraphicsPanel gpanel) {
		this.gpanel = gpanel;
		shapes = gpanel.getShapes();
	}
	
	/**
	 * �g�p�������܂���.
	 * @param lock
	 * @return
	 */
	JPopupMenu createPopupMenu(ColorLock lock) {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem deleteLockItem = new JMenuItem("���b�N����");
		deleteLockItem.addActionListener(new DeleteLockListener(gpanel, lock));
		popup.add(deleteLockItem);
		
		return popup;
	}
	
	public void mousePressed(MouseEvent e) {
		gpanel.clearIsLockedShape();
		gpanel.repaint();
		if (e.isPopupTrigger()) showPopup(e);
	}
	
	public void mouseClicked(MouseEvent e) {
		if (e.isPopupTrigger()) {
			shapes = gpanel.getShapes();
			ACShape shape;
			
			clickedPoint = e.getPoint();
			
			for (int i = shapes.size()-1; i >= 0; i--) {
				shape = (ACShape)shapes.get(i);
				if (shape.contains(clickedPoint)) {
					if (shape.getNaturalColor() != null) {
						/* ��Ɍ����������Ă����� */
						break;
					} else {
						//gpanel.setIsLockedShape(shape);	/* ���b�N���ꂽ�}�`�Y�ɓo�^ */
						/* �V�������I�u�W�F�N�g����� */
						ColorLock lock = new ColorLock(gpanel, shape, shape.getConnectPoint(), new LockLine());
						//���R�[�h ColorLock�Ɉړ� lock.setPopupMenu(createPopupMenu(lock)); /* �|�b�v�A�b�v��o�^ */
						gpanel.setColorLock(lock);	/* ���I�u�W�F�N�g�̏W���ɒǉ� */
						shape.setNaturalColor();	/* naturalColor�Ƃ��Đݒ� */
						// System.out.println("set lock");	//debug
					}
					gpanel.repaint();	/* clasp�\�����߂�paintComponent�̒��� */
					return;
				}
			}
			// �N���b�N�����_���ǂ̐}�`���܂�ł��Ȃ������ꍇ
			// �w�i�F��naturalColor�̐ݒ�
		} else {
			showPopup(e);
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) showPopup(e);
	}
	
	private void showPopup(MouseEvent e) {
		Point clickedPoint = e.getPoint();
		Vector colorLocks = gpanel.getColorLocks();
		
		for (int i = colorLocks.size()-1; i >= 0; i--) {
			ColorLock lock = (ColorLock)colorLocks.get(i);
			LockLine lockLine = lock.lockLine;
			Shape boundingBox = lockLine.getBoundingBox();
			if (boundingBox.contains(clickedPoint)) {
				Component com = (Component)e.getSource();
				if (e.isPopupTrigger()) {
					lock.getPopupMenu().show(com, e.getX(), e.getY());
				}
				
				// �I�����ꂽ���ɐڑ�����Ă���}�`��showHandle()����
				gpanel.setIsLockedShape(lock.lockedShape);
				
				gpanel.repaint();
				
				break;
			}
		}
	}
}
/* **************************************************************** */




