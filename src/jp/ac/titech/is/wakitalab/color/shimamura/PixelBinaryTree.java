/* * 作成日: 2004/06/09 * * この生成されたコメントの挿入されるテンプレートを変更するため * ウィンドウ > 設定 > Java > コード生成 > コードとコメント */package jp.ac.titech.is.wakitalab.color.shimamura;/** * @author shinamu1 * * この生成されたコメントの挿入されるテンプレートを変更するため * ウィンドウ > 設定 > Java > コード生成 > コードとコメント */public class PixelBinaryTree {	private PixelBinaryTree left;	private PixelBinaryTree right;	private int key;		PixelBinaryTree(int key) {		this.key = key;	}		boolean add (int key) {		if (key < this.key) {			if (left != null) {				return left.add(key);			} else {				left = new PixelBinaryTree(key);				return true;			}		} else if (key > this.key) {			if (right != null) {				return right.add(key);			} else {				right = new PixelBinaryTree(key);				return true;			}		} else {			return false;		}	}		int sort (int[] array, int tail) {		if (left != null) {			tail = left.sort(array, tail);		}		array[tail] = key;		tail++;		if (right != null) {			tail = right.sort(array, tail);		}		return tail;	}		int seek (int key) {		if (key < this.key) {			if (left != null) {				return left.seek(key);			} else {				return -1;			}		} else if (key > this.key) {			if (right != null) {				return right.seek(key);			} else {				return -1;			}		} else {			return key;		}	}}