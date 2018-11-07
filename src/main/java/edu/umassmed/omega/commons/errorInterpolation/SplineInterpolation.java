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

	private static double[] Bias_NEW = { -0.6783, -0.5161, -0.4988, -0.4992,
			-0.4994, -0.4994, -0.4992, -0.4990, -0.4987, -0.4994, -0.4992,
			-0.4993 };
	private static double[] Sigma_NEW = { 3.9978, 1.2923, 0.4724, 0.4430,
			0.4220, 0.4085, 0.3994, 0.3966, 0.3947, 0.3936, 0.3933, 0.3923 };
	
	// private static double[] Bias_L3 = { 2.5867, 0.7736, 0.5684, 0.5558,
	// 0.5473,
	// 0.5409, 0.5375, 0.5361, 0.5349, 0.5352, 0.5348, 0.5349 };
	// private static double[] Sigma_L3 = { 3.1235, 1.1559, 0.3866, 0.3689,
	// 0.3577, 0.3503, 0.3459, 0.3447, 0.3441, 0.3428, 0.3430, 0.3423 };
	//
	// private static double[] Bias_L5 = { 0.6135, 0.3918, 0.2131, 0.1435,
	// 0.0833,
	// 0.0417, 0.0267, 0.0182, 0.0130, 0.0100, 0.0084, 0.0071 };
	// private static double[] Sigma_L5 = { 0.4275, 0.3484, 0.2449, 0.2043,
	// 0.1677, 0.1121, 0.0887, 0.0681, 0.0545, 0.0458, 0.0383, 0.0345 };
	//
	// private static double[] Bias_L10 = { 0.6135, 0.3918, 0.2131, 0.1435,
	// 0.0833, 0.0417, 0.0267, 0.0182, 0.0130, 0.0100, 0.0084, 0.0071 };
	// private static double[] Sigma_L10 = { 0.4275, 0.3484, 0.2449, 0.2043,
	// 0.1677, 0.1121, 0.0887, 0.0681, 0.0545, 0.0458, 0.0383, 0.0345 };

	public static enum Size {
		BIAS_L3, SIGMA_L3, BIAS_L5, SIGMA_L5, BIAS_L10, SIGMA_L10
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
			case BIAS_L3:
				cs = new CubicSpline(SplineInterpolation.SNR,
						SplineInterpolation.Bias_NEW);
				break;
			case SIGMA_L3:
				cs = new CubicSpline(SplineInterpolation.SNR,
						SplineInterpolation.Sigma_NEW);
				break;
			case BIAS_L5:
				cs = new CubicSpline(SplineInterpolation.SNR,
						SplineInterpolation.Bias_NEW);
				break;
			case SIGMA_L5:
				cs = new CubicSpline(SplineInterpolation.SNR,
						SplineInterpolation.Sigma_NEW);
				break;
			case BIAS_L10:
				cs = new CubicSpline(SplineInterpolation.SNR,
						SplineInterpolation.Bias_NEW);
				break;
			case SIGMA_L10:
				cs = new CubicSpline(SplineInterpolation.SNR,
						SplineInterpolation.Sigma_NEW);
				break;
		}

		final double result = cs.interpolate(val);

		return result;
	}
}
