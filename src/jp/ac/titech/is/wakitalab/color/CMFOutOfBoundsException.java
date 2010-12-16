package jp.ac.titech.is.wakitalab.color;

/**
 * $Id: CMFOutOfBoundsException.java,v 1.3 2003/11/26 13:00:39 wakita Exp $
 * @author Ken Wakita
 * @version Experimental, Jan 19, 2003
 */

public class CMFOutOfBoundsException extends ArrayIndexOutOfBoundsException {
	private static final long serialVersionUID = 7508957572778030693L;

	CMFOutOfBoundsException() { super(); }

    CMFOutOfBoundsException(int l) { super(l); }

    CMFOutOfBoundsException(String s) { super(s); }
}
