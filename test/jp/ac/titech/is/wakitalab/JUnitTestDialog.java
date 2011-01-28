package jp.ac.titech.is.wakitalab;

import javax.swing.JOptionPane;

import junit.framework.Assert;

public class JUnitTestDialog {
    
    public static void interact(String message) {
        Assert.assertEquals(JOptionPane.showConfirmDialog(null, message, "JUnit Test Dialog", JOptionPane.YES_NO_OPTION), 0);
    }
}
