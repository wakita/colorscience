/* * 作成日: 2004/01/11 * */package jp.ac.titech.is.wakitalab.color.shimamura;import java.awt.Canvas;import java.awt.Graphics;import java.awt.Image;import java.awt.image.MemoryImageSource;import java.awt.MediaTracker;/** * @author shimaken * */public class BitmapCanvas extends Canvas {		private int w;	private int h;	private int[] pix;		public void paint(Graphics g){		if (pix != null) {			MemoryImageSource mimg = new MemoryImageSource(w,h,pix,0,w);			Image img = createImage(mimg);	//イメージを生成			MediaTracker mt = new MediaTracker(this);			mt.addImage(img,0);			try{				mt.waitForAll();			} catch(InterruptedException e){ }			g.drawImage(img,0,0,this);	//イメージを表示		}	}		public void setImage(int w, int h, int[] pix){		this.w = w;		this.h = h;		this.pix = pix;	}	public int getW() {return w;}	public int getH() {return h;}}