/*
 * Created on 2003/12/03
 * $Id: IllFormedCSVException.java,v 1.2 2003/12/08 00:18:37 wakita Exp $
 */
package jp.ac.titech.is.wakitalab.pervasives;

/**
 * @author Ken Wakita
 * @version Experimental, 2003/12/03
 */
public class IllFormedCSVException extends IllegalArgumentException {
	private static final long serialVersionUID = -8255180548989899130L;
	public IllFormedCSVException() { super(); }
    public IllFormedCSVException(String msg) { super(msg); }
}
