/* * 作成日: 2004/06/04 * * この生成されたコメントの挿入されるテンプレートを変更するため * ウィンドウ > 設定 > Java > コード生成 > コードとコメント */package jp.ac.titech.is.wakitalab.color.shimamura;import java.util.*;/** * @author shinamu1 * * この生成されたコメントの挿入されるテンプレートを変更するため * ウィンドウ > 設定 > Java > コード生成 > コードとコメント */public class ClusterReprPoint {	Luv1976 centerLuv;	private Vector<ClusterPoint> pointV;	int centerGravity;	private int id;		ClusterReprPoint(int id, Luv1976 centerLuv) {		this.id = id;		this.centerLuv = centerLuv;		pointV = new Vector<ClusterPoint>();	}		/**	 *  ClusterPoint を一つ追加する。	 * @param p	 */	void addPoint(ClusterPoint p) {		pointV.addElement(p);	}		void clearPoints() {		pointV.removeAllElements();	}		/**	 * 中心 Luv1976 を新しくする。	 * @return	 */	void changeCenterGravity() {		Iterator iterator = pointV.iterator();		ClusterPoint c;		double[] luvG = new double[3];		int count = 0;		while(iterator.hasNext()) {			c = (ClusterPoint)(iterator.next());			luvG[0] += c.getLuv().getValue1();			luvG[1] += c.getLuv().getValue2();			luvG[2] += c.getLuv().getValue3();			count++;		}		luvG[0] = luvG[0] / (double)count;		luvG[1] = luvG[1] / (double)count;		luvG[2] = luvG[2] / (double)count;		centerLuv.setValue(luvG[0], luvG[1], luvG[2]);	}		int getId() { return id; }	}