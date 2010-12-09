/* * 作成日: 2004/06/04 * * この生成されたコメントの挿入されるテンプレートを変更するため * ウィンドウ > 設定 > Java > コード生成 > コードとコメント */package jp.ac.titech.is.wakitalab.color.shimamura;/** * @author shinamu1 * * この生成されたコメントの挿入されるテンプレートを変更するため * ウィンドウ > 設定 > Java > コード生成 > コードとコメント */public class Luv1976 extends Vector3D {		final static double un = (4.0 * XYZ.Xn) / (XYZ.Xn + 15 * XYZ.Yn + 3 * XYZ.Zn);	final static double vn = (9.0 * XYZ.Xn) / (XYZ.Xn + 15 * XYZ.Yn + 3 * XYZ.Zn);		Luv1976 () {		super();	}		Luv1976 (double value1, double value2, double value3) {		super(value1, value2, value3);	}		XYZ getXYZ () {		double l = getValue1();		double u = getValue2();		double v = getValue3();		double yyn = getXYZyyn(l);		double y = yyn * XYZ.Yn;		double u_ = (u / (13.0 * l)) + un;		double v_ = (v / (13.0 * l)) + vn;		double x = 2.25 * y * u_ / v_;		double z = (3.0 * y) / v_ - x / 3.0 - (5.0 * y);		return new XYZ(x, y, z);			}		void getXYZ(XYZ xyz) {		double l = getValue1();		double u = getValue2();		double v = getValue3();		double yyn = getXYZyyn(l);		double y = yyn * XYZ.Yn;		double u_ = (u / (13.0 * l)) + un;		double v_ = (v / (13.0 * l)) + vn;		double x = 2.25 * y * u_ / v_;		double z = (3.0 * y) / v_ - x / 3.0 - (5.0 * y);		xyz.setValue(x, y, z);	}		double getXYZyyn (double l) {		if (l > 7.9996) {			return Math.pow((l + 16.0) / 116.0, 3.0);		} else {			return l / 903.29;		}	}		SRGB getSRGB() {		return getXYZ().getSRGB();	}		void getSRGB(SRGB srgb) {		getXYZ(XYZ.tempXYZ);		XYZ.tempXYZ.getSRGB(srgb);	}}