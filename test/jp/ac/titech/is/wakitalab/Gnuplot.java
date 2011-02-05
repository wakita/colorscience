/*
 * $Id: Gnuplot.java,v 1.2 2005/11/18 09:03:36 wakita Exp $
 */
package jp.ac.titech.is.wakitalab;

import static org.junit.Assert.*;

import java.io.*;

import javax.swing.*;

public class Gnuplot {
    public java.lang.Process process;
    
    public String imagePath;
    public String title = "なにかのテスト";

    private java.io.PrintStream out;
    
    public void prnt(Object ...args) { out.print(args); }
    public void println(Object ... args) { out.println(args); }
    public void printf(String format, Object ... args) { out.printf(format, args); }

    private static final String executable = "/opt/local/bin/gnuplot";

    private static final String defaultTerminalType = "aqua"; // "aqua" or "x11"
    
    private static final boolean DEBUG = false;

    private void start() {
        try {
            process = java.lang.Runtime.getRuntime().exec(executable);
            out = DEBUG ? System.out : new java.io.PrintStream(process.getOutputStream());
        } catch (java.io.IOException e) {
            throw new Error("Failed to start gnuplot");
        }
    }
    
    public void flush() {
        out.flush();
    }

    public void stop() {
        out.close();
        try { process.waitFor(); } catch (InterruptedException e) {}
    }
    
    public Gnuplot() {
        this(defaultTerminalType, null);
    }

    public Gnuplot(String filename) {
        this("png", filename);
    }
    
    public Gnuplot(String terminalType, String filename) {
        terminalType = terminalType == null ? Gnuplot.defaultTerminalType : terminalType;
        start();
        out.printf("set terminal %s\n", terminalType);
        if (filename != null) {
            imagePath = filename;
            out.printf("set output '%s'\n", filename);
        } else imagePath = null;
    }
    
    public static Gnuplot startGnuplotWithPNG() throws IOException {
        File tmp = File.createTempFile("gnuplotTest-", ".png", new File("/tmp"));
        Gnuplot gnuplot = new Gnuplot("png font \"/Library/Fonts/Arial Unicode.ttf\" 14", tmp.getAbsolutePath());
        return gnuplot;
    }

    public void title(String title) {
        this.title = title;
        out.printf("set title \"%s\"\n", title);
    }

    public void xlabel(String label) {
        out.printf("set xlabel \"%s\"\n", label);
    }

    public void ylabel(String label) {
        out.printf("set ylabel \"%s\"\n", label);
    }
    
    public void labels(String xlabel, String ylabel) {
        xlabel(xlabel); ylabel(ylabel);
    }
    
    public void xrange(String range) {
        out.printf("set xrange %s\n", range);
    }
    
    public void yrange(String range) {
        out.printf("set yrange %s\n", range);
    }
    
    public void ranges(String xrange, String yrange) {
        xrange(xrange);
        yrange(yrange);
    }

    public void beginPlot() {
        out.println("set grid");
        out.println("plot '-'");
    }

    public void beginPlot(String options) {
        out.printf("plot '-' %s\n", options);
    }

    public void plot(double x, double y) {
        out.printf("%f, %f\n", x, y);
    }

    public void endPlot() {
        out.println("e");
    }

    public void test(String message) {
        if (DEBUG) return;
        
        if (imagePath == null) throw new IllegalStateException("画像ファイルを生成していません。");
        
        stop();
        
        assertEquals(JOptionPane.showConfirmDialog(null, message + "\n結果は満足のいくものですか？", title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(imagePath)),
                JOptionPane.YES_OPTION);
        
        if (imagePath != null) new File(imagePath).delete();
    }
    
    public void test() {
        test("");
    }
}
