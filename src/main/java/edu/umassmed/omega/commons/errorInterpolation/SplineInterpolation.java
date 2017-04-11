package edu.umassmed.omega.commons.errorInterpolation;

import flanagan.interpolation.CubicSpline;

/**
 * @author galliva
 * @version 1.0
 * @since 2012-03-21
 */
public class SplineInterpolation {
	private static double[] SNR = { 1.291059, 1.990510, 2.846111, 3.494379,
			4.556798, 6.516668, 8.832892, 11.632132, 15.067460, 19.326731,
			24.642859, 31.306549 };
	
	private static double[] Bias = { 0.6135, 0.3918, 0.2131, 0.1435, 0.0833,
			0.0417, 0.0267, 0.0182, 0.0130, 0.0100, 0.0084, 0.0071 };
	private static double[] Sigma = { 0.4275, 0.3484, 0.2449, 0.2043, 0.1677,
			0.1121, 0.0887, 0.0681, 0.0545, 0.0458, 0.0383, 0.0345 };
	
	public static enum Size {
		BIAS, SIGMA
	};
	
	public static double getMaxSNR() {
		return SplineInterpolation.SNR[11];
	}
	
	public static double getMinSNR() {
		return SplineInterpolation.SNR[0];
	}
	
	/**
	 * Using a cubic spline, extrapolates the value of BIAS or SIGMA at the
	 * specified SNR.
	 *
	 * @param SNRValue
	 * @param sizeToInterpolate
	 *            Specify the {@link Size} to interpolate.
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static double interpolate(final double SNRValue,
			final Size sizeToInterpolate) throws IllegalArgumentException {
		final Double val = SNRValue;
		
		CubicSpline cs = null;
		
		switch (sizeToInterpolate) {
			case BIAS:
				cs = new CubicSpline(SplineInterpolation.SNR,
						SplineInterpolation.Bias);
				break;
			case SIGMA:
				cs = new CubicSpline(SplineInterpolation.SNR,
						SplineInterpolation.Sigma);
				break;
		}
		
		final double result = cs.interpolate(val);
		
		return result;
	}
}
