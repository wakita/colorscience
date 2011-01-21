package jp.ac.titech.is.wakitalab.math;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class MatrixTest {
	private Random rand = new Random();
	
	private double r() {
		return rand.nextDouble();
	}
	
	@Test public void plus() {
		Matrix m = new Matrix(r(), r(), r(), r(), r(), r(), r(), r(), r());

		assertTrue(m.times(2).equals(m.plus(m)));
		
		Matrix r = m;
		for (int i = 0; i < 10; i++) r = r.plus(r);
		assertTrue(r.equals(m.times(1<<10)));
	}
	
	@Test public void times1() {
		Matrix m = new Matrix(r(), r(), r(), r(), r(), r(), r(), r(), r());
	
		final int N = 11;
		double rad = Math.PI * 2 / N;
		double cos = Math.cos(rad), sin = Math.sin(rad);
		Matrix rot = new Matrix(cos, -sin, 0, sin, cos, 0, 0, 0, 1);
		
		Matrix r = m;
		for (int i = 0; i < N; i++) r = r.times(rot);
		assertTrue(r.equals(m));
	}
	
	@Test public void inverse() {
		Matrix m = new Matrix(r(), r(), r(), r(), r(), r(), r(), r(), r());
		assertTrue(m.times(m.inverse()).equals(Matrix.I));
		assertTrue(m.inverse().times(m).equals(Matrix.I));
	}
}
