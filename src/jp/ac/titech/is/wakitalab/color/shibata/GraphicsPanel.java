package jp.ac.titech.is.wakitalab.color.shibata;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/* **************************** 描画キャンバス ***************************** */

public class GraphicsPanel extends JPanel {
	private int objectNumber = 0; /* 次に作成するオブジェクトナンバ */
	private int characterNumber = 0;	/* 次に作成する文字ナンバ */
	
	private ACShape drawing = null;			/* 描きかけの図形 */
	private Vector<ACShape> shapes = new Vector<ACShape>();		/* canvasに描かれている図形の集合 */
	private Vector<ACShape> selections = new Vector<ACShape>();	/* 選択されている図形の集合 */
	private Vector<ACCharacter> chSelections = new Vector<ACCharacter>();	/* 選択されている文字の集合 */
	private Vector<ACCharacter> characters = new Vector<ACCharacter>();	/* 文字列の集合 */
	private Vector<Spring> springs = new Vector<Spring>();		/* ばねオブジェクトの集合 */
	private Vector<ACShape> claspShapes = new Vector<ACShape>();	/* ばねの張られている図形の集合 */
	private Vector<ACShape> isSettingShapes = new Vector<ACShape>();	/* 色差設置中の図形 */
	private Vector<ColorLock> colorLocks = new Vector<ColorLock>();  /* 鍵オブジェクトの集合 */
	private Vector<ACShape> isLockedShapes = new Vector<ACShape>(); /* 選択された鍵のロックしている図形の集合. showHandleに使用 */
	//private ACEllipse backgroundPalette;	/* 背景パレット. 消去不可の特別モン */
	
	private MouseInputAdapter currentListener = null;
	private Rectangle selecting = null;
	
	private Vector<ACShape> cutBuffer = new Vector<ACShape>();	/* shapeのカットバッファ */
	private Vector<ACCharacter> chCutBuffer = new Vector<ACCharacter>();	/* 文字のカットバッファ */
	
	private static final int INC_X = 20;
	private static final int INC_Y = 20;
	
	private static final int PNG = 0;
	private static final int BMP = 1;
	private static final int JPG = 2;
	
	protected boolean setting_colorDifference = false;	/* 色差・願望度設定モード．trueの時のみばね・端点を表示 */
	private boolean firstShape_of_spring = true;
	boolean isDrawCanvas;
	
	GraphicsPanel() {
		initCanvas(800, 600);
		
		isDrawCanvas = true;
	}
	GraphicsPanel(int width, int height) {
		initCanvas(width, height);
		
		isDrawCanvas = true;
	}
	
	void initCanvas(int width, int height) {
		setBackground(Color.white);
		setPreferredSize(new Dimension(width, height));
		
		ACEllipse ellipse = new ACEllipse(new Point(0, 0));
		ellipse.setBounds(0, 0, 40, 30);
		ellipse.setFillColor(getBackground());
		ellipse.save();
		ellipse.setIsPalette(true);
		ellipse.setIsBackgroundPalette(true);
		addShape(ellipse);
	}
	private void initObjectNumber(int number) { objectNumber = number; }	/* load時に使用 */
	private void initCharacterNumber(int number) {characterNumber = number; }	/* load時に使用 */
	void setFirstShape_of_spring(boolean isFirst) {firstShape_of_spring = isFirst;}
	boolean getFirstShape_of_spring() { return firstShape_of_spring; }
	
	void renewIndexOfArray() {
		for (int i = 0; i < shapes.size(); i++) {
			ACShape shape = (ACShape)shapes.get(i);
			shape.setIndexOfArray(i);
		}
	}
	
	void setListener(MouseInputAdapter listener) {
		unselectAll();
		//chUnselectAll();
		repaint();
		
		if (currentListener != null) {
			removeMouseListener(currentListener);
			removeMouseMotionListener(currentListener);
		}
		addMouseListener(listener);
		addMouseMotionListener(listener);
		
		currentListener = listener;
	}
	
	void setDrawing(ACShape shape) { drawing = shape; }
	
	void addShape() {
		shapes.add(drawing);
		drawing.setObjectNumber(objectNumber++); /* 識別番号の登録 */
		drawing.setIndexOfArray(shapes.size()-1);
		drawing = null;
	}
	void addShape(ACShape palette) {
		shapes.add(palette);
		palette.setObjectNumber(objectNumber++);
		palette.setIndexOfArray(shapes.size()-1);
	}
	void addCharacters(ACCharacter ch) {
		characters.add(ch);
		ch.setCharacterNumber(characterNumber++);
	}
	
	//void setBackgroundPalette(ACEllipse backgroundPalette) { this.backgroundPalette = backgroundPalette; }
	void setShapes(Vector<ACShape> shapes) { this.shapes = shapes; }
	void setSelecting(Rectangle range) { selecting = range; }
	void setCharacters(Vector<ACCharacter> characters) { this.characters = characters; }
	void setSpring(Spring spring) { springs.add(spring); }
	void setSprings(Vector<Spring> springs) { this.springs = springs; }
	void setClaspShape(ACShape shape) { claspShapes.add(shape); }
	void setClaspShapes(Vector<ACShape> claspShapes) { this.claspShapes = claspShapes; }
	void clearClaspShapes() { claspShapes.clear(); }
	void setIsSettingShape(ACShape shape) { isSettingShapes.add(shape); }
	void clearIsSettingShape() { isSettingShapes.clear(); }
	void setColorLock(ColorLock lock) { colorLocks.add(lock); }
	void setColorLocks(Vector<ColorLock> colorLocks) { this.colorLocks = colorLocks; }
	void setIsLockedShape(ACShape shape) { isLockedShapes.add(shape); }
	void clearIsLockedShape() { isLockedShapes.clear(); }
	void dataAllClean() {
		/* loadXML時に使用 */
		objectNumber = 0; /* 次に作成するオブジェクトナンバ */
		characterNumber = 0;	/* 次に作成する文字ナンバ */
		drawing = null;			/* 描きかけの図形 */
		shapes.clear();		/* canvasに描かれている図形の集合 */
		unselectAll();	/* selections.clear()はこれに内包される */
		//selections.clear();	/* 選択されている図形の集合 */
		//chSelections.clear();	/* 選択されている文字の集合 */
		characters.clear();	/* 文字列の集合 */
		springs.clear();		/* ばねオブジェクトの集合 */
		claspShapes.clear();	/* ばねの張られている図形の集合 */
		isSettingShapes.clear();	/* 色差設置中の図形 */
		colorLocks.clear();  /* 鍵オブジェクトの集合 */
		isLockedShapes.clear(); /* 選択された鍵のロックしている図形の集合. showHandleに使用 */
		selecting = null;
		
		//これはしないほうがよい？cutBuffer.clear();	/* shapeのカットバッファ */
		//これもしないほうがよい？chCutBuffer.clear();	/* 文字のカットバッファ */
		
		//setting_colorDifference = false;	/* 色差・願望度設定モード．trueの時のみばね・端点を表示 */
		firstShape_of_spring = true;
		
		//initCanvas();
		setBackground(Color.white);
	}
	
	//ACShape getBackgroundPalette() { return backgroundPalette; }
	Vector<ACShape> getShapes() { return shapes; }
	Vector getSelections() { return selections; }
	Vector <ACCharacter>getCharacters() { return characters; }
	Vector getChSelections() { return chSelections; }
	Vector getSprings() { return springs; }
	Vector<ACShape> getClaspShapes() { return claspShapes; }
	Vector<ColorLock> getColorLocks() { return colorLocks; }
	
	/* Color配列を受け取ってそれをshapesの各図形に設定 */
	void setColors(Color[] colors) {
		for (int i = 0; i < colors.length; i++) {
			ACShape shape = (ACShape)shapes.get(i);
			shape.setFillColor(colors[i]);
		}
	}
	
	void color_difference_settingMode_ON() {
		setting_colorDifference = true;
	}
	void color_difference_settingMode_OFF() {
		setting_colorDifference = false;
	}
	
	void select(ACShape shape, boolean multi) {
		if (shape.isSelected()) {
			//debug System.out.println("return");
			return;
		}
		
		if (!multi) {
			unselectAll();
			//chUnselectAll();
		}
		selections.add(shape);
		shape.select();
		repaint();
	}
	void unselect(ACShape shape) {
		selections.remove(shape);
		shape.unselect();
		repaint();
	}
	void unselectAll() {
		for (int i = 0; i < selections.size(); i++) {
			ACShape shape = (ACShape)selections.get(i);
			shape.unselect();
		}
		selections.clear();
		chUnselectAll();
		repaint();
	}
	
	void chSelect(ACCharacter ch, boolean multi) {
		if (ch.isSelected())
			return;
		
		if (!multi) {
			unselectAll();
			//chUnselectAll();
		}
		chSelections.add(ch);
		ch.select();
		repaint();
	}
	void chUnselect(ACCharacter ch) {
		chSelections.remove(ch);
		ch.unselect();
		repaint();
	}
	void chUnselectAll() {
		for (int i = 0; i < chSelections.size(); i++) {
			ACCharacter ch = (ACCharacter)chSelections.get(i);
			ch.unselect();
		}
		chSelections.clear();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		/* 図形を描画 */
		for (int i = 0; i < shapes.size(); i++) {
			ACShape shape = (ACShape)shapes.get(i);
			if (shape.isBackgroundPalette() && !isDrawCanvas) continue;
			if (shape.isPalette() && !isDrawCanvas) continue;
			shape.draw(g2, false);
		}
		/* 文字の描画 */
		// debug System.out.println("characters.size = "+characters.size());
		for (int i = 0; i < characters.size(); i++) {
			ACCharacter ch = (ACCharacter)characters.get(i);
			ch.draw(g2);
			
			//System.out.println(ch.text);	// debug
		}
		/* 選択された図形のハンドルを表示 */
		for (int i = 0; i < selections.size(); i++) {
			ACShape shape = (ACShape)selections.get(i);
			shape.showHandle(g2);
		}
		/* 選択された文字のハンドルを表示 */
		for (int i = 0; i < chSelections.size(); i++) {
			ACCharacter ch = (ACCharacter)chSelections.get(i);
			ch.showHandle(g2);
		}
		if (setting_colorDifference) {
			/* ばねの端点を描画 */
			for (int i = 0; i < claspShapes.size(); i++) {
				ACShape shape = (ACShape)claspShapes.get(i);
				shape.showClasp(g2);
			}
			
			/* ばねを描画 */
			for (int i = 0; i < springs.size(); i++) {
				Spring temp = (Spring)springs.get(i);
				temp.draw(g2);
			}
			
			for (int i = 0; i < isSettingShapes.size(); i++) {
				ACShape shape = (ACShape)isSettingShapes.get(i);
				shape.showHandle(g2);
			}
			
			for (int i = 0; i < colorLocks.size(); i++) {
				ColorLock lock = (ColorLock)colorLocks.get(i);
				lock.draw(g2);
			}
			
			for (int i = 0; i < isLockedShapes.size(); i++) {
				ACShape shape = (ACShape)isLockedShapes.get(i);
				shape.showHandle(g2);
			}
		}
		
		if (drawing != null)
			drawing.draw(g2, true);
		
		if (selecting != null) {
			g2.setStroke(new BasicStroke(1));
			g2.setColor(Color.black);
			g2.draw(selecting);
		}
		
		//if (isDrawCanvas) backgroundPalette.draw(g2, false);
	}
	
	private void reSettingClaspPoint() {
		clearClaspShapes();
		// debug System.out.println("claspShapes.size = "+claspShapes.size());
		for (int i = 0; i < springs.size(); i++) {
			Spring spring = (Spring)springs.get(i);
			setClaspShape(spring.myShape);
			setClaspShape(spring.yourShape);
		}
	}
	/* 引数のshapeにつながっているばねのshapeの消去に伴う消去 */
	private void deleteSpring(ACShape shape) {
		if (springs.size() == 0) {
			/* ばねがまだ張られていない場合、shapeのclaspShape登録解除のみ行う */
			claspShapes.remove(shape);
			if (!firstShape_of_spring) {
				setFirstShape_of_spring(true);
			}
		}
		
		for (int j = 0; j < springs.size(); j++) {
			Spring spring = (Spring)springs.get(j);
			int oneId = spring.myShape.getObjectNumber();
			int otherId = spring.yourShape.getObjectNumber();
			if (shape.getObjectNumber()==oneId || shape.getObjectNumber()==otherId) {
				/* 移動した図形に張ってあったばねならば */
				springs.remove(spring);	/* ばねの集合から削除 */
				j--;
				// claspShapeも削除
				reSettingClaspPoint();
				//claspShapes.remove(spring.myShape);
				//claspShapes.remove(spring.yourShape);
				
				clearIsSettingShape();	/* ハンドル消去 */
			}
		}
		
		setFirstShape_of_spring(true);
		
		repaint();
	}
	/* 引数のshapeをロックしている鍵のshapeの消去に伴う消去 */
	private void deleteLock(ACShape shape) {
		for (int i = 0; i < colorLocks.size(); i++) {
			ColorLock lock = (ColorLock)colorLocks.get(i);
			int id = lock.lockedShape.getObjectNumber();
			if (shape.getObjectNumber() == id) {
				/* 移動した図形に張ってあった鍵ならば */
				lock.lockedShape.releasenaturalColor();
				colorLocks.remove(lock);	/* 鍵の集合から削除 */
				i--;
				clearIsLockedShape();
			}
		}
		repaint();
	}
	
	void cut() {
		Iterator iter = shapes.iterator();
		cutBuffer.clear();
		
		/* 重なっている順番にカットバッファに入れる */
		while (iter.hasNext()) {
			ACShape shape = (ACShape)iter.next();
			if (shape.isBackgroundPalette()) continue;
			if (shape.isSelected()) {
				// shapeにつながれているばね・鍵の消去
				// ばねの消去に伴って…DeleteSpringListener参照
				deleteSpring(shape);
				// 鍵の消去に伴って…DeleteLockListener参照
				deleteLock(shape);
				
				Rectangle rc = shape.getBounds();
				/* カットの場合は最初はずらさずにペーストするため */
				shape.setBounds(rc.x - INC_X, rc.y - INC_Y, rc.width, rc.height);
				cutBuffer.add(shape);
				
				iter.remove();
			}
		}
		renewIndexOfArray();
		
		Iterator chIter = characters.iterator();
		chCutBuffer.clear();
		
		/* 選択されたACCharacterを文字のカットバッファに入れる */
		while (chIter.hasNext()) {
			ACCharacter ch = (ACCharacter)chIter.next();
			if (ch.isSelected()) {
				Rectangle2D r2d = ch.bounds;
				// boundingboxをずらし、文字の開始位置をずらす
				int afterX = (int)(r2d.getX()-INC_X);
				int afterY = (int)(r2d.getY()-INC_Y);
				r2d.setRect(afterX, afterY, r2d.getWidth(), r2d.getHeight());
				ch.chStartX = afterX;
				ch.chStartY = afterY;
				chCutBuffer.add(ch);
				
				characterNumber = ch.getCharacterNumber();
				
				chIter.remove();
			}
		}
		
		setFirstShape_of_spring(true);
		
		unselectAll();
		//chUnselectAll();
		
		repaint();
	}
	
	void copy() {
		Iterator iter = shapes.iterator();
		
		cutBuffer.clear();
		
		/* 重なっている順番にカットバッファに入れる */
		while(iter.hasNext()) {
			ACShape shape = (ACShape)iter.next();
			if (shape.isBackgroundPalette()) continue;
			if (shape.isSelected()) {
				/* オブジェクトのコピーを作る */
				ACShape copiedShape = (ACShape)shape.clone();
				copiedShape.unselect();
				cutBuffer.add(copiedShape);
			}
		}
		
		Iterator chIter = characters.iterator();
		chCutBuffer.clear();
		while (chIter.hasNext()) {
			ACCharacter ch = (ACCharacter)chIter.next();
			if (ch.isSelected()) {
				/* 文字のコピーを作る */
				ACCharacter copiedChar = (ACCharacter)ch.clone();
				copiedChar.unselect();
				chCutBuffer.add(copiedChar);
			}
		}
	}
	
	void paste() {
		if (cutBuffer.isEmpty() && chCutBuffer.isEmpty())
			return;
		
		unselectAll();
		//chUnselectAll();
		
		for (int i = 0; i < cutBuffer.size(); i++) {
			ACShape shape = (ACShape)cutBuffer.get(i);
			
			/* 少し位置をずらす */
			Rectangle rc = shape.getBounds();
			Point p = new Point(rc.x + INC_X, rc.y + INC_Y);
			MouseListener.adjustToGrid(p);
			shape.setBounds((int)p.getX(), (int)p.getY(), rc.width, rc.height);
			
			/* オブジェクトのコピーを作る */
			ACShape newShape = (ACShape)shape.clone();
			newShape.setObjectNumber(objectNumber++); /* 識別番号の登録 */
			newShape.releasenaturalColor();	/* naturalColorを未設定に */
			shapes.add(newShape);
			select(newShape, true);
		}
		renewIndexOfArray();
		
		for (int i = 0; i < chCutBuffer.size(); i++) {
			ACCharacter ch = (ACCharacter)chCutBuffer.get(i);
			
			/* 文字のboundの位置をずらし、開始位置をずらす. */
			Rectangle2D r2d = ch.bounds;
			double afterX = r2d.getX() + INC_X;
			double afterY = r2d.getY() + INC_Y;
			r2d.setRect(afterX, afterY, r2d.getWidth(), r2d.getHeight());
			ch.chStartX = (int)afterX;
			ch.chStartY = (int)(afterY + r2d.getHeight());
			
			/* 文字のコピーを作る */
			ACCharacter newChar = (ACCharacter)ch.clone();
			newChar.setCharacterNumber(characterNumber++);
			characters.add(newChar);
			chSelect(newChar, true);
		}
		
		repaint();
	}
	
	void duplicate() {
		copy(); paste();
	}
	
	void delete() {
		ListIterator iter = selections.listIterator();
		while (iter.hasNext()) {
			ACShape shape = (ACShape)iter.next();
			if (shape.isBackgroundPalette()) continue;
			
			shapes.remove(shape);
			// shapeにつながれているばね・鍵の消去
			// ばねの消去に伴って…DeleteSpringListener参照
			deleteSpring(shape);
			// 鍵の消去に伴って…DeleteLockListener参照
			deleteLock(shape);
			iter.remove();
		}
		renewIndexOfArray();
		
		ListIterator chIter = chSelections.listIterator();
		while (chIter.hasNext()) {
			ACCharacter ch = (ACCharacter)chIter.next();
			characterNumber = ch.getCharacterNumber();
			characters.remove(ch);
			chIter.remove();
		}
		
		setFirstShape_of_spring(true);
		
		repaint();
	}
	
	void clear() {
		unselectAll();
		shapes.clear();
		characters.clear();
		springs.clear();
		claspShapes.clear();
		isSettingShapes.clear();
		colorLocks.clear();
		isLockedShapes.clear();
		setFirstShape_of_spring(true);
		
		initCanvas(800, 600);
		
		repaint();
	}
	
	void saveAsJPG() {
		// キャンバスをjpg画像として保存
		int fileType = -1;
		
		FileFilterEx filter[] = {
				new FileFilterEx(".png", "PNG ファイル (*.png)"),
				new FileFilterEx(".bmp", "BMP ファイル (*.bmp)"),
				new FileFilterEx(".jpg", "JPEG ファイル (*.jpg)"),
		};
		JFileChooser file = new JFileChooser("./automaticColoring/ac_image");
		for (int i = 0; i < filter.length; i++) {
			file.addChoosableFileFilter(filter[i]);
		}
		if (file.showSaveDialog(this) == JFileChooser.CANCEL_OPTION)
			return;
		
		BufferedImage image = new BufferedImage(800, 680, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		g2.fillRect(0, 0, image.getWidth(), image.getHeight());
		
		/* 図形を描く */
		for (int i = 0; i < shapes.size(); i++) {
			ACShape shape = (ACShape)shapes.get(i);
			if (shape.isPalette() || shape.isBackgroundPalette()) continue;
			shape.draw(g2, false);
		}
		/* 文字を描く */
		for (int i = 0; i < characters.size(); i++) {
			ACCharacter ch = (ACCharacter)characters.get(i);
			ch.draw(g2);
			//System.out.println(ch.text);
		}
		
		try {
			File writeFile = file.getSelectedFile();
			String fileName = writeFile.getName();
			//debug System.out.println(fileName);
			//なぜエラーが出るのか不明 : FileFilter currentFilter = file.getFileFilter();
			if (fileName.length() > 4) {
				String type = fileName.substring(fileName.length()-4, fileName.length());
				//debug : System.out.println(type);
				if (type.equals(".png")) fileType = PNG;
				else if (type.equals("bmp")) fileType = BMP;
				else /* if (type.equals(".jpg")) */ fileType = JPG;	/* デフォルトでjpg */
			} else /* if (fileName.length() <= 3) */ {
				fileType = JPG;
			}
			switch (fileType) {
			case PNG: ImageIO.write(image, "png", writeFile);
						break;
			case BMP: ImageIO.write(image, "bmp", writeFile);
						break;
			case JPG: ImageIO.write(image, "jpeg", writeFile);
						break;
			}
			ImageIO.write(image, "jpeg", writeFile);
		} catch (IOException e) {
			System.out.println("ファイル書き出しに失敗しました");
		}
	}
	
	// 描画した図形の情報をXMLファイルに保存 
	public void saveXML(File saveFile) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation domImpl = builder.getDOMImplementation();
		
		// ルート要素を作成
		Document document = domImpl.createDocument("", "automaticColoring", null);
		Element automaticColoring = document.getDocumentElement();
		
		// 全ての図形の情報をXMLに追加
		for (int i = 0; i < shapes.size(); i++) {
			// 1つの図形を追加
			ACShape one = (ACShape)shapes.get(i);
			Element shape = document.createElement("shape");
			shape.setAttribute("type", one.getTypeName());
			automaticColoring.appendChild(shape);
			
			// その図形の属性を追加
			//System.out.println(one.getType());
			/* 図形固有のデータ */
			if (one.getType() == ACShape.LINE) {
				Line2D.Double line = (Line2D.Double)one.shape;
				appendElement(shape, "x1", line.x1+"", document);
				appendElement(shape, "y1", line.y1+"", document);
				appendElement(shape, "x2", line.x2+"", document);
				appendElement(shape, "y2", line.y2+"", document);
				
				// claspPointを保存
				
				// connectPointを保存
				
			} else if (one.getType() == ACShape.RECTANGLE) {
				Rectangle rect = (Rectangle)one.shape;
				appendElement(shape, "x", rect.x+"", document);
				appendElement(shape, "y", rect.y+"", document);
				appendElement(shape, "width", rect.width+"", document);
				appendElement(shape, "height", rect.height+"", document);
				
				// claspPointを保存
				
				// connectPointを保存
				
			} else if (one.getType() == ACShape.ROUNDRECT){
				RoundRectangle2D.Double rrect = (RoundRectangle2D.Double)one.shape;
				appendElement(shape, "x", rrect.x+"", document);
				appendElement(shape, "y", rrect.y+"", document);
				appendElement(shape, "width", rrect.width+"", document);
				appendElement(shape, "height", rrect.height+"", document);
				appendElement(shape, "arcwidth", rrect.arcwidth+"", document);
				appendElement(shape, "archeight", rrect.archeight+"", document);
				
				// claspPointを保存
				
				// connectPointを保存
				
			} else if (one.getType() == ACShape.ELLIPSE) {
				Ellipse2D.Double ellipse = (Ellipse2D.Double)one.shape;
				appendElement(shape, "x", ellipse.x+"", document);
				appendElement(shape, "y", ellipse.y+"", document);
				appendElement(shape, "width", ellipse.width+"", document);
				appendElement(shape, "height", ellipse.height+"", document);
				
				// claspPointを保存
				
				// connectPointを保存
				
			} else if (one.getType() == ACShape.POLYGON) {
				Polygon polygon = (Polygon)one.shape;
				/* 頂点数を保存 */
				appendElement(shape, "npoints", polygon.npoints+"", document);
				for (int j = 0; j < polygon.npoints; j++) {
					/* 各頂点情報を保存 */
					appendElement(shape, "x"+j, polygon.xpoints[j]+"", document);
					appendElement(shape, "y"+j, polygon.ypoints[j]+"", document);
				}
				
				// claspPointを保存
				
				// connectPointを保存
			}
			/* 図形共通のデータ */
			appendElement(shape, "linecolor", one.lineColor.getRGB()+"", document);
			appendElement(shape, "fillcolor", one.fillColor.getRGB()+"", document);
			//appendElement(shape, "realcolor", one.realColor.getRGB()+"", document);
			appendElement(shape, "linewidth", one.lineWidth+"", document);
			appendElement(shape, "objectNumber", one.getObjectNumber()+"", document);
			appendElement(shape, "isPalette", one.isPalette()+"", document);
			appendElement(shape, "isBackgroundPalette", one.isBackgroundPalette()+"", document);
		}
		/* 文字の情報をXMLファイルに保存 */
		for (int i = 0; i < characters.size(); i++) {
			/* 
			 * 保存するのは…
			 * 
			 * String text … 表す文字
			 * int chStartX, chStartY … 文字の書き出し位置
			 * int characterNumber … 文字の順番
			 * int fontSize … フォントの大きさ
			 * Color fontColor … 文字の色. 図形と同じ要領で.
			 * bounds.x, bounds.y, bounds.width, bounds.height
			 * 
			 * の9つ.
			 * boundsはloadする際のACCharacterをnewするときにできるが、
			 * その際に出来るboundsはフォントサイズに合っていないので
			 * 必ず
			 * bounds.setRect(bounds.x, bounds.y, bounds.width, bounds.height)
			 * を行うこと.上記のbounds.x ... bounds.heightの保存はこれの為のものである.
			 */
			
			// 1つの文字を追加
			ACCharacter one = (ACCharacter)characters.get(i);
			Element character = document.createElement("character");
			character.setAttribute("type", one.getTypeName());
			automaticColoring.appendChild(character);
			
			appendElement(character, "text", one.text, document);
			appendElement(character, "chStartX", one.chStartX+"", document);
			appendElement(character, "chStartY", one.chStartY+"", document);
			appendElement(character, "characterNumber", one.getCharacterNumber()+"", document);
			appendElement(character, "fontSize", one.getFontSize()+"", document);
			appendElement(character, "fontColor",one.getFontColor().getRGB()+"", document);
			appendElement(character, "boundX", one.bounds.getX()+"", document);
			appendElement(character, "boundY", one.bounds.getY()+"", document);
			appendElement(character, "boundWidth", one.bounds.getWidth()+"", document);
			appendElement(character, "boundHeight", one.bounds.getHeight()+"", document);
			
			// その文字の情報を保存していく.(文字固有のデータ)
		}
		/* ばねの情報をXMLファイルに保存 */
		for (int i = 0; i < springs.size(); i++) {
			/*
			 * 保存するのは…
			 * ACShape firstShape, secondShape ←オブジェクトなので、IDで.
			 * int color_difference
			 * int desirability
			 * Point firstClaspPoint, secondClaspPoint ←claspX1, claspY1, claspX2, claspY2で
			 * 		※claspPointを図形で保持する場合はfirstClasp...は不要.
			 * 
			 * 再現するには、これらの情報を元にばねをnewする
			 * Spring spring = new Spring(firstShape, secondShape, firstClasp, secondClasp, new SpringImg(gpanel));
			 * spring.setPopupMenu(createPopupMenu(spring));
			 * gpanel.setSpring(spring);
			 * ※※ firstShape, secondShapeはACShape.      ※※
			 * ※※ まず、上のメソッドで図形をロードしたら ※※
			 * ※※ 保存した図形のIDで図形を探し出す。     ※※
			 * ※※ ばねでclaspPointを保存しなかった場合は ※※
			 * ※※ 図形からclaspPointを計算するところから ※※
			 * ※※ はじめて、しかるのちばねをnewする      ※※
			 */
			
			// 1つのばねを追加
			Spring one = (Spring)springs.get(i);
			Element spring = document.createElement("spring");
			spring.setAttribute("type", "spring");
			automaticColoring.appendChild(spring);
			
			// そのばねの情報を保存していく
			appendElement(spring, "firstShapeNumber", one.myShape.getObjectNumber()+"", document);
			appendElement(spring, "secondShapeNumber", one.yourShape.getObjectNumber()+"", document);
			appendElement(spring, "color_difference", one.color_difference+"", document);
			appendElement(spring, "desirability", one.desirability+"", document);
			appendElement(spring, "complaint", one.getComplaint()+"", document);
			appendElement(spring, "realContrast", one.getRealContrast()+"", document);
			appendElement(spring, "firstClaspX", one.getFirstClaspPoint().x+"", document);
			appendElement(spring, "firstClaspY", one.getFirstClaspPoint().y+"", document);
			appendElement(spring, "secondClaspX", one.getSecondClaspPoint().x+"", document);
			appendElement(spring, "secondClaspY", one.getSecondClaspPoint().y+"", document);
		}
		/* 鍵の情報をXMLファイルに保存 */
		for (int i = 0; i < colorLocks.size(); i++) {
			/*
			 * 保存するのは…
			 * ACShape lockedShape ←この鍵がロックしている図形. IDで保存
			 * int desirability 
			 * Point connectPoint ←connectX, connectY で.
			 * 
			 * 再現するには、これらの情報を元に鍵をnewする
			 * ColorLock lock = new ColorLock(shape, shape.getConnectPoint(), new LockImg(gpanel));
			 * lock.setPopupMenu(createPopupMenu(lock));
			 * lock.setDesirability(desirability); // ここでlock.setLineWidth()もされる
			 * gpanel.setColorLock(lock);
			 * shape.setNaturalColor();	//naturalColorはここで再現.
			 */
			
			// 1つの鍵を追加
			ColorLock one = (ColorLock)colorLocks.get(i);
			Element colorLock = document.createElement("colorLock");
			colorLock.setAttribute("type", "colorLock");
			automaticColoring.appendChild(colorLock);
			
			// 鍵の情報を保存していく
			appendElement(colorLock, "lockedShapeNumber", one.lockedShape.getObjectNumber()+"", document);
			appendElement(colorLock, "desirability", one.desirability+"", document);
			appendElement(colorLock, "connectX", one.getConnectPoint().x+"", document);
			appendElement(colorLock, "connectY", one.getConnectPoint().y+"", document);
			appendElement(colorLock, "complaint", one.getComplaint()+"", document);
			appendElement(colorLock, "origColor", one.origColor.getRGB()+"", document);
		}
		
		// 生成したXMLをファイルに保存
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();
		
		DOMSource source = new DOMSource(document);
		FileOutputStream out = new FileOutputStream(saveFile);
		StreamResult result = new StreamResult(out);
		transformer.transform(source, result);
	}
	
	// 図形の属性を要素として追加
	public void appendElement(Element parent, String name, String value, Document document) {
		Element element = document.createElement(name);
		element.appendChild(document.createTextNode(value));
		parent.appendChild(element);
	}
	
	// XMLファイルの読み込み
	public void loadXML(File loadFile) throws Exception {
		dataAllClean();	/* すべての情報をクリア */
		
		//Vector loadShapes = new Vector();	/* loadした図形を入れる箱 */
		//Vector loadCharacters = new Vector();	/* loadした文字を入れる箱 */
		//Vector loadSprings = new Vector();	/* loadしたばねを入れる箱 */
		//Vector loadClaspShapes = new Vector();	/* loadした、ばねを張っている図形を入れる箱 */
		//Vector loadLocks = new Vector();	/* loadしたかぎを入れる箱 */
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		// ファイルからXMLを読み込む
		Document document = builder.parse(loadFile);
		
		// ルート要素を取得し、automaticColoringのセーブファイルかどうかチェック
		Element root = document.getDocumentElement();
		if (!root.getTagName().equals("automaticColoring"))
			System.exit(1);
		
		// shape要素のリストを取得
		NodeList list = root.getElementsByTagName("shape");
		for (int i = 0; i < list.getLength(); i++) {
			Element shape = (Element)list.item(i);
			
			// type属性の値を取得
			String type = shape.getAttribute("type");
			
			// 直線の場合
			if (type.equals("line")) {
				String x1 = getNodeValue(shape, "x1");
				String y1 = getNodeValue(shape, "y1");
				String x2 = getNodeValue(shape, "x2");
				String y2 = getNodeValue(shape, "y2");
				String linecolor = getNodeValue(shape, "linecolor");
				String fillcolor = getNodeValue(shape, "fillcolor");
				//String realcolor = getNodeValue(shape, "realcolor");
				String linewidth = getNodeValue(shape, "linewidth");
				String objectNumber = getNodeValue(shape, "objectNumber");
				String string_isPalette = getNodeValue(shape, "isPalette");
				String string_isBackgroundPalette = getNodeValue(shape, "isBackgroundPalette");
				
				// 読み込んだ情報を元にlineオブジェクトを作る
				int x = (int)Double.parseDouble(x1);
				int y = (int)Double.parseDouble(y1);
				int width = (int)Double.parseDouble(x2)-x;
				int height = (int)Double.parseDouble(y2)-y;
				boolean isPalette = false, isBackgroundPalette = false;
				if (string_isPalette.equals("true")) { isPalette = true; }
				if (string_isBackgroundPalette.equals("true")) { isBackgroundPalette = true; }
				
				ACLine line = new ACLine(new Point(0, 0));
				line.setBounds(x, y, width, height);
				line.setLineColor(new Color(Integer.parseInt(linecolor)));
				line.setFillColor(new Color(Integer.parseInt(fillcolor)));
				//line.setRealColor(new Color(Integer.parseInt(realcolor)));
				line.setLineWidth(Integer.parseInt(linewidth));
				line.setObjectNumber(Integer.parseInt(objectNumber));
				line.setIsPalette(isPalette);
				line.setIsBackgroundPalette(isBackgroundPalette);
				
				if (line.isBackgroundPalette()) setBackground(line.fillColor);
				
				line.save();
				line.setClaspPoint();
				line.setConnectPoint();
				
				// Vector shapesに追加
				//loadShapes.add(line);
				shapes.add(line);
				
			} else if (type.equals("rectangle") || type.equals("roundrectangle") || type.equals("ellipse")) {
				// roundrectangle, ellipseはrectangleを継承しているため、ほぼ同じように扱える
				String x = getNodeValue(shape, "x");
				String y = getNodeValue(shape, "y");
				String width = getNodeValue(shape, "width");
				String height = getNodeValue(shape, "height");
				String linecolor = getNodeValue(shape, "linecolor");
				String fillcolor = getNodeValue(shape, "fillcolor");
				//String realcolor = getNodeValue(shape, "realcolor");
				String linewidth = getNodeValue(shape, "linewidth");
				String objectNumber = getNodeValue(shape, "objectNumber");
				String string_isPalette = getNodeValue(shape, "isPalette");
				String string_isBackgroundPalette = getNodeValue(shape, "isBackgroundPalette");
				
				boolean isPalette = false, isBackgroundPalette = false;
				if (string_isPalette.equals("true")) { isPalette = true; }
				if (string_isBackgroundPalette.equals("true")) { isBackgroundPalette = true; }
				
				// 読み込んだ情報を元にRectangleオブジェクトを作る
				ACShape shapeOne;
				
				if (type.equals("rectangle")) {
					shapeOne = new ACRectangle(new Point(0, 0));
				} else if (type.equals("roundrectangle")) {
					// 角丸四角形の場合は更に角の丸み具合を取得、設定する
					String arcwidth = getNodeValue(shape, "arcwidth");
					String archeight = getNodeValue(shape, "archeight");
					shapeOne = new ACRoundRectangle(new Point(0, 0));
					((ACRoundRectangle)shapeOne).setArcWidth((int)Double.parseDouble(arcwidth));
					((ACRoundRectangle)shapeOne).setArcHeight((int)Double.parseDouble(archeight));
				} else {
					// type.equals("ellipse");
					shapeOne = new ACEllipse(new Point(0, 0));
				}
				
				shapeOne.setBounds((int)Double.parseDouble(x), (int)Double.parseDouble(y),
									(int)Double.parseDouble(width), (int)Double.parseDouble(height));
				shapeOne.setLineColor(new Color(Integer.parseInt(linecolor)));
				shapeOne.setFillColor(new Color(Integer.parseInt(fillcolor)));
				//shapeOne.setRealColor(new Color(Integer.parseInt(realcolor)));
				shapeOne.setLineWidth(Integer.parseInt(linewidth));
				shapeOne.setObjectNumber(Integer.parseInt(objectNumber));
				shapeOne.setIsPalette(isPalette);
				shapeOne.setIsBackgroundPalette(isBackgroundPalette);
				
				if (shapeOne.isBackgroundPalette()) setBackground(shapeOne.fillColor);
				
				shapeOne.save();
				shapeOne.setClaspPoint();
				shapeOne.setConnectPoint();
				
				// Vector shapesに追加
				//loadShapes.add(shapeOne);
				shapes.add(shapeOne);
				
			} else if (type.equals("polygon")) {
				String objectNumber = getNodeValue(shape, "objectNumber");
				String npoints = getNodeValue(shape, "npoints");
				String string_isPalette = getNodeValue(shape, "isPalette");
				String string_isBackgroundPalette = getNodeValue(shape, "isBackgroundPalette");
				
				int npoints_int = Integer.parseInt(npoints);
				int[] xpoints = new int[npoints_int];
				int[] ypoints = new int[npoints_int];
				for (int j = 0; j < npoints_int; j++) {
					/* 頂点情報をXMLファイルから取得し、順次ACPolygonに登録していく */
					String x_of_point = getNodeValue(shape, "x"+j);
					String y_of_point = getNodeValue(shape, "y"+j);
					xpoints[j] = Integer.parseInt(x_of_point);
					ypoints[j] = Integer.parseInt(y_of_point);
				}
				boolean isPalette = false, isBackgroundPalette = false;
				if (string_isPalette.equals("true")) { isPalette = true; }
				if (string_isBackgroundPalette.equals("true")) { isBackgroundPalette = true; }
				
				String linecolor = getNodeValue(shape, "linecolor");
				String fillcolor = getNodeValue(shape, "fillcolor");
				//String realcolor = getNodeValue(shape, "realcolor");
				String linewidth = getNodeValue(shape, "linewidth");
				
				ACPolygon polygon = new ACPolygon(xpoints, ypoints, npoints_int);
				for (int j = 0; j < npoints_int; j++) {
					polygon.getDrawingPoints().add(new Point(xpoints[j], ypoints[j]));
				}
				polygon.setLineColor(new Color(Integer.parseInt(linecolor)));
				polygon.setFillColor(new Color(Integer.parseInt(fillcolor)));
				//polygon.setRealColor(new Color(Integer.parseInt(realcolor)));
				polygon.setLineWidth(Integer.parseInt(linewidth));
				polygon.setObjectNumber(Integer.parseInt(objectNumber));
				polygon.setIsPalette(isPalette);
				polygon.setIsBackgroundPalette(isBackgroundPalette);
				
				if (polygon.isBackgroundPalette()) setBackground(polygon.fillColor);
				
				polygon.save();
				polygon.setClaspPoint();
				polygon.setConnectPoint();
				
				// Vector shapesに追加
				//loadShapes.add(polygon);
				shapes.add(polygon);
			}
		}
		//shapes = loadShapes;
		renewIndexOfArray();
		initObjectNumber(shapes.size());
		
		// character要素のリストを取得
		NodeList characterList = root.getElementsByTagName("character");
		for (int i = 0; i < characterList.getLength(); i++) {
			// 文字の情報をロードし、new ACCharacterしてloadCharactersに追加する
			/* 
			 * loadするのは…
			 * 
			 * String text … 表す文字
			 * int chStartX, chStartY … 文字の書き出し位置
			 * int characterNumber … 文字の順番
			 * int fontSize … フォントの大きさ
			 * Color fontColor … 文字の色. 図形と同じ要領で.
			 * bounds.x, bounds.y, bounds.width, bounds.height
			 * 
			 * の9つ.
			 * boundsはloadする際のACCharacterをnewするときにできるが、
			 * その際に出来るboundsはフォントサイズに合っていないので
			 * 必ず
			 * bounds.setRect(bounds.x, bounds.y, bounds.width, bounds.height)
			 * を行うこと.上記のbounds.x ... bounds.heightの保存はこれの為のものである.
			 */
			Element character = (Element)characterList.item(i);
			
			String text = getNodeValue(character, "text");
			String stringChStartX = getNodeValue(character, "chStartX");
			String stringChStartY = getNodeValue(character, "chStartY");
			String stringCharacterNumber = getNodeValue(character, "characterNumber");
			String stringFontSize = getNodeValue(character, "fontSize");
			String stringFontColor = getNodeValue(character, "fontColor");
			String stringBoundX = getNodeValue(character, "boundX");
			String stringBoundY = getNodeValue(character, "boundY");
			String stringBoundWidth = getNodeValue(character, "boundWidth");
			String stringBoundHeight = getNodeValue(character, "boundHeight");
			
			// 読み込んだ情報を元にACCharacterオブジェクトを作る
			int chStartX = Integer.parseInt(stringChStartX);
			int chStartY = Integer.parseInt(stringChStartY);
			int characterNumber = Integer.parseInt(stringCharacterNumber);
			int fontSize = Integer.parseInt(stringFontSize);
			Color fontColor = new Color(Integer.parseInt(stringFontColor));
			double boundX = Double.parseDouble(stringBoundX);
			double boundY = Double.parseDouble(stringBoundY);
			double boundWidth = Double.parseDouble(stringBoundWidth);
			double boundHeight = Double.parseDouble(stringBoundHeight);
			
			ACCharacter ch = new ACCharacter(new Point(chStartX, chStartY), text);
			ch.bounds.setRect(boundX, boundY, boundWidth, boundHeight);
			ch.setCharacterNumber(characterNumber);
			ch.setFontSize(fontSize);
			ch.setFontColor(fontColor);
			
			ch.save();
			
			// Vector shapesに追加
			//loadCharacters.add(ch);
			characters.add(ch);
		}
		//characters = loadCharacters;
		initCharacterNumber(characters.size());
		
		// spring要素のリストを取得
		NodeList springList = root.getElementsByTagName("spring");
		for (int i = 0; i < springList.getLength(); i++) {
			// ばね情報をロードし、new SpringしてloadSpringsに追加する
			/*
			 * 保存しているのは…
			 * ACShape firstShape, secondShape ←オブジェクトなので、IDで.
			 * int color_difference
			 * int desirability
			 * Point firstClaspPoint, secondClaspPoint ←claspX1, claspY1, claspX2, claspY2で
			 * 
			 * 再現するには、これらの情報を元にばねをnewする
			 * Spring spring = new Spring(firstShape, secondShape, firstClasp, secondClasp, new SpringImg(gpanel));
			 * spring.setPopupMenu(createPopupMenu(spring));
			 * gpanel.setSpring(spring);
			 * ※※ firstShape, secondShapeはACShape.      ※※
			 * ※※ まず、上のメソッドで図形をロードしたら ※※
			 * ※※ 保存した図形のIDで図形を探し出す。     ※※
			 */
			Element spring =(Element)springList.item(i);
			
			String string_firstShapeNumber = getNodeValue(spring, "firstShapeNumber");
			String string_secondShapeNumber = getNodeValue(spring, "secondShapeNumber");
			String string_color_difference = getNodeValue(spring, "color_difference");
			String string_desirability = getNodeValue(spring, "desirability");
			String string_complaint = getNodeValue(spring, "complaint");
			String string_realContrast = getNodeValue(spring, "realContrast");
			String string_firstClaspX = getNodeValue(spring, "firstClaspX");
			String string_firstClaspY = getNodeValue(spring, "firstClaspY");
			String string_secondClaspX = getNodeValue(spring, "secondClaspX");
			String string_secondClaspY = getNodeValue(spring, "secondClaspY");
			
			int firstShapeNumber = Integer.parseInt(string_firstShapeNumber);
			int secondShapeNumber = Integer.parseInt(string_secondShapeNumber);
			int color_difference = Integer.parseInt(string_color_difference);
			int desirability = Integer.parseInt(string_desirability);
			double complaint = Double.parseDouble(string_complaint);
			double realContrast = Double.parseDouble(string_realContrast);
			int firstClaspX = Integer.parseInt(string_firstClaspX);
			int firstClaspY = Integer.parseInt(string_firstClaspY);
			int secondClaspX = Integer.parseInt(string_secondClaspX);
			int secondClaspY = Integer.parseInt(string_secondClaspY);
			
			ACShape myShape = new ACRectangle(), yourShape = new ACRectangle();
			for (int j = 0; j < shapes.size(); j++) {
				ACShape shape = (ACShape)shapes.get(j);
				if (shape.getObjectNumber() == firstShapeNumber) myShape = shape;
				if (shape.getObjectNumber() == secondShapeNumber) yourShape = shape;
			}
			//loadClaspShapes.add(myShape);
			//loadClaspShapes.add(yourShape);
			claspShapes.add(myShape);
			claspShapes.add(yourShape);
			
			Point firstClaspPoint = new Point(firstClaspX, firstClaspY);
			Point secondClaspPoint = new Point(secondClaspX, secondClaspY);
			
			Spring spring1 = 
				new Spring(myShape, yourShape, firstClaspPoint, secondClaspPoint, new SpringCore());
			SpringListener temp = new SpringListener(this);
			spring1.setPopupMenu(temp.createPopupMenu(spring1));
			spring1.setColorDifference(color_difference);
			spring1.setDesirability(desirability);
			spring1.setComplaint(complaint);
			spring1.setRealContrast(realContrast);
			
			//loadSprings.add(spring1);
			springs.add(spring1);
		}
		//springs = loadSprings;
		//claspShapes = loadClaspShapes;
		
		// colorLock要素のリストを取得
		NodeList lockList = root.getElementsByTagName("colorLock");
		for (int i = 0; i < lockList.getLength(); i++) {
			// 鍵の情報をロードし、new ColorLockしてloadLocksに追加する
			/*
			 * 保存しているのは…
			 * ACShape lockedShape ←この鍵がロックしている図形. IDで保存
			 * int desirability 
			 * Point connectPoint ←connectX, connectY で.
			 * 
			 * これらの情報を元に鍵をnewする
			 * ColorLock lock = new ColorLock(shape, shape.getConnectPoint(), new LockImg(gpanel));
			 * lock.setPopupMenu(createPopupMenu(lock));
			 * lock.setDesirability(desirability); // ここでlock.setLineWidth()もされる
			 * gpanel.setColorLock(lock);
			 * shape.setNaturalColor();	//naturalColorはここで再現.
			 */
			Element lock = (Element)lockList.item(i);
			
			String string_lockedShapeNumber = getNodeValue(lock, "lockedShapeNumber");
			String string_desirability = getNodeValue(lock, "desirability");
			String string_connectX = getNodeValue(lock, "connectX");
			String string_connectY = getNodeValue(lock, "connectY");
			String string_complaint = getNodeValue(lock, "complaint");
			String string_origColor = getNodeValue(lock, "origColor");
			
			int lockedShapeNumber = Integer.parseInt(string_lockedShapeNumber);
			int desirability = Integer.parseInt(string_desirability);
			int connectX = Integer.parseInt(string_connectX);
			int connectY = Integer.parseInt(string_connectY);
			double complaint = Double.parseDouble(string_complaint);
			Color origColor = new Color(Integer.parseInt(string_origColor));
			
			ACShape lockedShape = new ACRectangle();
			for (int j = 0; j < shapes.size(); j++) {
				ACShape shape = (ACShape)shapes.get(j);
				if (shape.getObjectNumber() == lockedShapeNumber) lockedShape = shape;
			}
			Point connectPoint = new Point(connectX, connectY);
			
			ColorLock colorLock = new ColorLock(this, lockedShape, connectPoint, new LockLine());
			colorLock.setDesirability(desirability);
			lockedShape.setNaturalColor();	/* naturalColorとして設定 */
			colorLock.setComplaint(complaint);
			colorLock.origColor = origColor;
			
			//loadLocks.add(colorLock);
			setColorLock(colorLock);	/* 鍵オブジェクトの集合に追加 */
		}
		//colorLocks = loadLocks;
		
		repaint();
	}
	
	// 図形の属性の値を取得
	public String getNodeValue(Element parent, String name) {
		NodeList list = parent.getElementsByTagName(name);
		Element element = (Element)list.item(0);
		return element.getFirstChild().getNodeValue();
	}
	
	void debug() {
		System.out.println("########## DATA OF SHAPES START ##########");
		System.out.println("shapes' size = "+shapes.size());
		for (int i = 0; i < shapes.size(); i++) {
			System.out.println("=== shape"+i+" ===");
			ACShape shape = (ACShape)shapes.get(i);
			System.out.println("ID = "+shape.getObjectNumber());
			System.out.println("IndexOfArray = "+shape.getIndexOfArray());
			System.out.println("Shape type = "+shape.getTypeName());
			System.out.println("fill Color = "+ shape.fillColor);
			System.out.println("Natural_Color = "+shape.getNaturalColor());
			System.out.println("Locked? = "+shape.isLocked);
			System.out.println("IsBackgroundPalette? = " + shape.isBackgroundPalette());
			System.out.println();
		}
		System.out.println("##########  DATA OF SHAPES END  ##########");
		System.out.println();
		System.out.println();
		System.out.println("########## DATA OF CHARACTERS START ##########");
		System.out.println("characters' size = "+characters.size());
		for (int i = 0; i < characters.size(); i++) {
			System.out.println("=== character"+i+" ===");
			ACCharacter ch = (ACCharacter)characters.get(i);
			System.out.println("chID = "+ch.getCharacterNumber());
			System.out.println("test = "+ch.text);
			System.out.println("isSelected? = "+ch.isSelected);
			System.out.println();
		}
		System.out.println("##########  DATA OF CHARACTERS END  ##########");
		System.out.println();
		System.out.println();
		System.out.println("########## DATA OF SPRINGS START ##########");
		System.out.println("springs' size = "+springs.size());
		for (int i = 0; i < springs.size(); i++) {
			System.out.println("=== spring"+i+" ===");
			Spring spring = (Spring)springs.get(i);
			System.out.println("oneShape'ID = "+spring.myShape.getObjectNumber());
			System.out.println("otherShape'ID = "+spring.yourShape.getObjectNumber());
			System.out.println("color_distance = "+spring.color_difference);
			System.out.println("desirability = "+spring.desirability);
			System.out.println();
		}
		System.out.println("##########  DATA OF SPRINGS END  ##########");
		System.out.println();
		System.out.println();
		System.out.println("########## DATA OF LOCKS START ##########");
		System.out.println("colorLocks' size = "+colorLocks.size());
		for (int i = 0; i < colorLocks.size(); i++) {
			System.out.println("=== lock"+i+" ===");
			ColorLock lock = (ColorLock)colorLocks.get(i);
			System.out.println("lock shape"+lock.lockedShape.getObjectNumber());
			System.out.println("desirability = "+lock.desirability);
			System.out.println();
		}
		System.out.println("##########  DATA OF LOCKS END  ##########");
		for (int i = 0; i < 5; i++)
			System.out.println();
	}
}
/* **************************************************************** */








