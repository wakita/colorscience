package jp.ac.titech.is.wakitalab.color.shimamura;public class TestMinimizeParameter {	/**	 * @param args	 */	public static void main(String[] args) {		Iromie iromie = new Iromie();		iromie.loadState("C:\\Eclipse\\eclipse-SDK-3.0-win32\\eclipse\\workspace\\ColorViewer\\abc.dat");				int expNumMax = iromie.expNumMax;		SRGB[] tempSrgb = new SRGB[iromie.getColorOBJNum()];		for(int i=0;i<iromie.getColorOBJNum();i++) tempSrgb[i] = new SRGB();/*		for(int expID=0;expID<expNumMax;expID++) {//			MinimizeExperiment exp = (MinimizeExperiment)iromie.expVector.get(expID);			int dType = exp.type;			BitmapCanvas optDichroCanvas = iromie.getOptDichroCanvas(expID, tempSrgb, dType);			JFrame optFrame = new JFrame("ロードテスト");			optFrame.getContentPane().add(optDichroCanvas);			optFrame.setSize(optDichroCanvas.getW()+10, optDichroCanvas.getH()+30);			optFrame.setVisible(true);		}*/				if (expNumMax!=1) return;				// MinimizeExperiment exp = iromie.expVector.get(0);	}}