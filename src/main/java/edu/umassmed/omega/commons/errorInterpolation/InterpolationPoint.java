package edu.umassmed.omega.commons.errorInterpolation;

public class InterpolationPoint {
	private final Double snr;
	private final Double l;
	private final Double smss;
	private final Double d;
	private final Double val;

	public InterpolationPoint(final Double snr, final Double l,
	        final Double smss, final Double d, final Double value) {
		this.snr = snr;
		this.l = l;
		this.smss = smss;
		this.d = d;
		this.val = value;
	}

	public Double getSNR() {
		return this.snr;
	}

	public Double getLength() {
		return this.l;
	}

	public Double getSMSS() {
		return this.smss;
	}

	public Double getD() {
		return this.d;
	}

	public Double getValue() {
		return this.val;
	}
}
