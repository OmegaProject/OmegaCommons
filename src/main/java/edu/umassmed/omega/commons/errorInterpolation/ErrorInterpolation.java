package edu.umassmed.omega.commons.errorInterpolation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import edu.umassmed.omega.commons.constants.OmegaConstants;
import edu.umassmed.omega.commons.utilities.OmegaFileUtilities;

public class ErrorInterpolation {

	public static final InputStream DEFAULT_D_FILE = OmegaFileUtilities
	        .getErrorInterpolationFilename(OmegaConstants.OMEGA_ERROR_INTERPOLATION_D_FILE);
	public static final InputStream DEFAULT_SMSS_FILE = OmegaFileUtilities
			.getErrorInterpolationFilename(OmegaConstants.OMEGA_ERROR_INTERPOLATION_SMSS_FILE);
	private final InterpolationFileLoader ifl;

	public ErrorInterpolation(final String path)
	        throws IllegalArgumentException, FileNotFoundException {
		this(new FileInputStream(path));
	}

	public ErrorInterpolation(final InputStream is)
			throws IllegalArgumentException {
		this.ifl = new InterpolationFileLoader(is);
		try {
			this.ifl.load();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int validateD(final double d) {
		if (d < this.ifl.getDMin())
			return -1;
		if (d > this.ifl.getDMax())
			return 1;
		return 0;
	}

	public int validateSMSS(final double smss) {
		if (smss < this.ifl.getSMSSMin())
			return -1;
		if (smss > this.ifl.getSMSSMax())
			return 1;
		return 0;
	}

	public int validateLength(final double length) {
		if (length < this.ifl.getLengthMin())
			return -1;
		if (length > this.ifl.getLengthMax())
			return 1;
		return 0;
	}

	public int validateSNR(final double snr) {
		if (snr < this.ifl.getSNRMin())
			return -1;
		if (snr > this.ifl.getSNRMax())
			return 1;
		return 0;
	}

	public Double interpolate(final double snr, final double length,
	        final double smss, final double d) throws IllegalArgumentException {
		if ((snr < this.ifl.getSNRMin()) || (snr > this.ifl.getSNRMax()))
			throw new IllegalArgumentException("SNR out of range");
		if ((length < this.ifl.getLengthMin())
		        || (length > this.ifl.getLengthMax()))
			throw new IllegalArgumentException("L out of range");
		if ((smss < this.ifl.getSMSSMin()) || (smss > this.ifl.getSMSSMax()))
			throw new IllegalArgumentException("SMSS out of range");
		if ((d < this.ifl.getDMin()) || (d > this.ifl.getDMax()))
			throw new IllegalArgumentException("D out of range");

		final Double[] SNRs = this
		        .getClosestPoint(this.ifl.getSNRValues(), snr);
		final Double[] Ls = this.getClosestPoint(this.ifl.getLengthValues(),
		        length);
		final Double[] SMSSs = this.getClosestPoint(this.ifl.getSMSSValues(),
		        smss);
		final Double[] Ds = this.getClosestPoint(this.ifl.getDValues(), d);

		final Double X_SNR = this.normalizeValue(snr, SNRs);
		final Double Y_L = this.normalizeValue(length, Ls);
		final Double Z_SMSS = this.normalizeValue(smss, SMSSs);
		final Double W_D = this.normalizeValue(d, Ds);

		Double result = 0.0;

		for (int iX = 0; iX < 2; iX++) {
			for (int iY = 0; iY < 2; iY++) {
				for (int iZ = 0; iZ < 2; iZ++) {
					for (int iW = 0; iW < 2; iW++) {
						final double XM = (iX == 0) ? (1 - X_SNR) : X_SNR;
						final double YM = (iY == 0) ? (1 - Y_L) : Y_L;
						final double ZM = (iZ == 0) ? (1 - Z_SMSS) : Z_SMSS;
						final double WM = (iW == 0) ? (1 - W_D) : W_D;
						final InterpolationPoint ip = this.ifl.getPoint(
						        SNRs[iX], Ls[iY], SMSSs[iZ], Ds[iW]);
						result = result + (ip.getValue() * XM * YM * ZM * WM);
					}
				}
			}
		}
		return result;
	}

	public Double[] getClosestPoint(final List<Double> values,
	        final double value) {
		double minDist1 = Double.MAX_VALUE;
		double minDist2 = Double.MAX_VALUE;
		Double point1 = null;
		Double point2 = null;
		for (final Double val : values) {
			final double currentDistance = StrictMath.abs(val - value);
			if ((val <= value) && (currentDistance < minDist1)) {
				point1 = val;
				minDist1 = currentDistance;
			}
			if ((val > value) && (currentDistance < minDist2)) {
				point2 = val;
				minDist2 = currentDistance;
			}
		}
		if ((point1 == null) || (point2 == null))
			return null;
		final Double result[] = { point1, point2 };
		return result;
	}

	public Double normalizeValue(final double value, final Double[] values) {
		if ((values[1] - values[0]) == 0.0)
			throw new IllegalArgumentException();
		return (value - values[0]) / (values[1] - values[0]);
	}

	public static void main(final String[] args) {
		ErrorInterpolation ei;
		try {
			ei = new ErrorInterpolation(
			        ".//resources//D_interpolation_data_old.csv");
		} catch (final IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		ei.interpolate(3.0, 100.0, 0.0, 0.005);
	}
}
