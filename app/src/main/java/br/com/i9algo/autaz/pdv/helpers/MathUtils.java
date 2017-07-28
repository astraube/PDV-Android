package br.com.i9algo.autaz.pdv.helpers;

public class MathUtils {

	public static final double valueToPercent(double total, double value) {
		return (value * 100) / total;
	}
	public static final double percentToValue(double total, double percent) {
		return (percent * total) / 100;
	}
}
