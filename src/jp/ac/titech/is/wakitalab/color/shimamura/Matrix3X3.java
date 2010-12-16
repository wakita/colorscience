/* * 作成日: 2003/11/16 * */package jp.ac.titech.is.wakitalab.color.shimamura;/** * @author shimaken * 行列変換を扱う */public class Matrix3X3 {		/*	 * 定数	 */			/**	 * 3X3の各成分	 */	private double value11;	private double value12;	private double value13;	private double value21;	private double value22;	private double value23;	private double value31;	private double value32;	private double value33;			/*	 * コンストラクター	 */				/**	 * double 9つのコンストラクター	 */	public Matrix3X3 (double v11, double v12, double v13,	                   double v21, double v22, double v23,	                   double v31, double v32, double v33) {		value11 = v11;		value12 = v12;		value13 = v13;		value21 = v21;		value22 = v22;		value23 = v23;		value31 = v31;		value32 = v32;		value33 = v33;	}		/**	 * double[] のコンストラクター	 */	public Matrix3X3 (double[] values) {		value11 = values[0];		value12 = values[1];		value13 = values[2];		value21 = values[3];		value22 = values[4];		value23 = values[5];		value31 = values[6];		value32 = values[7];		value33 = values[8];	}		/**	 * 引数なしのコンストラクター	 */	public Matrix3X3 () {		value11 = 0.0;		value12 = 0.0;		value13 = 0.0;		value21 = 0.0;		value22 = 0.0;		value23 = 0.0;		value31 = 0.0;		value32 = 0.0;		value33 = 0.0;	}			/*	 * メソッド	 */			/**	 * 計算に使うdouble値 value1, value2, value3	 */	double value1,value2,value3;		/**	 * メソッドの中で一時的に使うVector3D new_v	 */	Vector3D new_v;		/**	 * Matrixを使ってvを変換する。	 */	Vector3D convertVector3D(Vector3D v) {// TODO インターフェースでVector3Dを定義して、返り値をvに入れられるようにする。		value1 = v.getValue1();		value2 = v.getValue2();		value3 = v.getValue3();		new_v = new Vector3D(			value11*value1+value12*value2+value13*value3,			value21*value1+value22*value2+value23*value3,			value31*value1+value32*value2+value33*value3);		return new_v;	}		/**	 * Vector3Dを使わずに行列でdoubles[3]を変換する	 * @param values	 */	void convertDoubles(double[] values) {		value1 = value11*values[0]+value12*values[1]+value13*values[2];		value2 = value21*values[0]+value22*values[1]+value23*values[2];		value3 = value31*values[0]+value32*values[1]+value33*values[2];		values[0] = value1;		values[1] = value2;		values[2] = value3;	}		/**	 * Matrix を使って fromV を変換して toV に出力する。	 * @param fromV	 * @param toV	 */	void convertVector3D(Vector3D fromV, Vector3D toV) {		value1 = fromV.getValue1();		value2 = fromV.getValue2();		value3 = fromV.getValue3();		toV.setValue(				value11*value1+value12*value2+value13*value3,				value21*value1+value22*value2+value23*value3,				value31*value1+value32*value2+value33*value3);	}		/**	 * 逆行列を計算する。	 * @param dest 逆行列の格納先	 */	Matrix3X3 inverse(){			double det = determinant();			return new Matrix3X3(			(value22 * value33 - value23 * value32) / det,			(value32 * value13 - value33 * value12) / det,			(value12 * value23 - value13 * value22) / det,			(value23 * value31 - value21 * value33) / det,			(value33 * value11 - value31 * value13) / det,			(value13 * value21 - value23 * value11) / det,			(value21 * value32 - value22 * value31) / det,			(value31 * value12 - value32 * value11) / det,			(value11 * value22 - value12 * value21) / det);		}		/**	 * 行列式を取得する。	 * @return 行列式	 */	double determinant(){		return (			value11 * value22 * value33			+ value12 * value23 * value31			+ value13 * value21 * value32			- value11 * value23 * value32			- value12 * value21 * value33			- value13 * value22 * value31);	}	/**	 * 行列の積を取る。	 * @param matrix 積を取る行列	 * @param dest 積を格納する行列	 */	void multi(Matrix3X3 matrix, Matrix3X3 dest){				dest.value11 = value11 * matrix.value11 +			value12 * matrix.value21 + value13 * matrix.value31;		dest.value12 = value11 * matrix.value12 +			value12 * matrix.value22 + value13 * matrix.value32;		dest.value13 = value11 * matrix.value13 +			value12 * matrix.value23 + value13 * matrix.value33;		dest.value21 = value21 * matrix.value11 +			value22 * matrix.value21 + value23 * matrix.value31;		dest.value22 = value21 * matrix.value12 +			value22 * matrix.value22 + value23 * matrix.value32;		dest.value23 = value21 * matrix.value13 +			value22 * matrix.value23 + value23 * matrix.value33;		dest.value31 = value31 * matrix.value11 +			value32 * matrix.value21 + value33 * matrix.value31;		dest.value32 = value31 * matrix.value12 +			value32 * matrix.value22 + value33 * matrix.value32;		dest.value33 = value31 * matrix.value13 +			value32 * matrix.value23 + value33 * matrix.value33;			}		double getValue11() {		return value11;	}	double getValue12() {		return value12;	}	double getValue13() {		return value13;	}	double getValue21() {		return value21;	}	double getValue22() {		return value22;	}	double getValue23() {		return value23;	}	double getValue31() {		return value31;	}	double getValue32() {		return value32;	}	double getValue33() {		return value33;	}	/**	 * 行列の文字列表現を取得する。	 * @return 行列の文字列表現	 */	public String toString(){		return (			"|" + value11 + "|" + value12 + "|" + value13 + "|\n"			+ "|" + value21 + "|" + value22 + "|" + value23 + "|\n"			+ "|" + value31 + "|" + value32 + "|" + value33 + "|\n");	}	}