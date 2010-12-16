/* * 作成日: 2004/06/04 * * この生成されたコメントの挿入されるテンプレートを変更するため * ウィンドウ > 設定 > Java > コード生成 > コードとコメント */package jp.ac.titech.is.wakitalab.color.shimamura;/** * @author shinamu1 * * この生成されたコメントの挿入されるテンプレートを変更するため * ウィンドウ > 設定 > Java > コード生成 > コードとコメント */public class ClusterPoint {	private int srgbInt;	private int weight;	private int reprInt;	private Luv1976 luv;	private ClusterReprPoint repr;	private ClusterReprPoint oldRepr;		ClusterPoint(int srgbInt) {		this.srgbInt = srgbInt;		SRGB srgb = new SRGB(srgbInt);		luv = srgb.getLuv1976();	}		void setWeight(int weight) {		this.weight = weight;	}		void setReprInt(int reprInt) {		this.reprInt = reprInt;	}	int getSRGBInt() {		return srgbInt;	}		int getWeight() {		return weight;	}		Luv1976 getLuv() {		return luv;	}		int getReprInt() {		return reprInt;	}		void setRepr(ClusterReprPoint newRepr) {		oldRepr = repr;		repr = newRepr;	}		ClusterReprPoint getRepr() {		return repr;	}		ClusterReprPoint getOldRepr() {		return oldRepr;	}		double getDistance(ClusterReprPoint targetRepr) {		double distance1 = luv.getValue1() - targetRepr.centerLuv.getValue1();		double distance2 = luv.getValue2() - targetRepr.centerLuv.getValue2();		double distance3 = luv.getValue3() - targetRepr.centerLuv.getValue3();		return 		Math.pow(		(distance1 * distance1 + distance2 * distance2 + distance3 * distance3 ),		0.5);	}	}