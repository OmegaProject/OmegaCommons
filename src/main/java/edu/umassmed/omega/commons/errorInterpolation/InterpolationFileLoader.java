package main.java.edu.umassmed.omega.commons.errorInterpolation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.java.edu.umassmed.omega.commons.utilities.CSVFileLoader;

public class InterpolationFileLoader extends CSVFileLoader {

	private final List<InterpolationPoint> interpolationPoints;
	private final List<Double> snrValues;
	private final List<Double> lengthValues;
	private final List<Double> smssValues;
	private final List<Double> dValues;
	private Double snrMin, snrMax;
	private Double lengthMin, lengthMax;
	private Double smssMin, smssMax;
	private Double dMin, dMax;

	public InterpolationFileLoader(final String path)
	        throws IllegalArgumentException {
		this(new File(path));
	}

	public InterpolationFileLoader(final File file)
	        throws IllegalArgumentException {
		super(file);
		this.interpolationPoints = new ArrayList<InterpolationPoint>();
		this.snrValues = new ArrayList<Double>();
		this.lengthValues = new ArrayList<Double>();
		this.smssValues = new ArrayList<Double>();
		this.dValues = new ArrayList<Double>();
		this.snrMin = Double.MAX_VALUE;
		this.snrMax = Double.MIN_VALUE;
		this.lengthMin = Double.MAX_VALUE;
		this.lengthMax = Double.MIN_VALUE;
		this.smssMin = Double.MAX_VALUE;
		this.smssMax = Double.MIN_VALUE;
		this.dMin = Double.MAX_VALUE;
		this.dMax = Double.MIN_VALUE;
	}

	@Override
	public void load() throws IOException, FileNotFoundException {
		super.load();
		final Map<Integer, String[]> fileContent = this.getFileContent();
		for (final Integer line : fileContent.keySet()) {
			final String[] content = fileContent.get(line);
			final Double snr = Double.valueOf(content[0]);
			final Double l = Double.valueOf(content[1]);
			final Double smss = Double.valueOf(content[2]);
			final Double d = Double.valueOf(content[3]);
			final Double val = Double.valueOf(content[4]);
			final InterpolationPoint ip = new InterpolationPoint(snr, l, smss,
			        d, val);
			this.adjustSNRMinMax(snr);
			this.adjustLMinMax(l);
			this.adjustSMSSMinMax(smss);
			this.adjustDMinMax(d);
			this.snrValues.add(snr);
			this.lengthValues.add(l);
			this.smssValues.add(smss);
			this.dValues.add(d);
			this.interpolationPoints.add(ip);
		}
	}

	private void adjustSNRMinMax(final Double val) {
		if (this.snrMin > val) {
			this.snrMin = val;
		}
		if (this.snrMax < val) {
			this.snrMax = val;
		}
	}

	private void adjustLMinMax(final Double val) {
		if (this.lengthMin > val) {
			this.lengthMin = val;
		}
		if (this.lengthMax < val) {
			this.lengthMax = val;
		}
	}

	private void adjustSMSSMinMax(final Double val) {
		if (this.smssMin > val) {
			this.smssMin = val;
		}
		if (this.smssMax < val) {
			this.smssMax = val;
		}
	}

	private void adjustDMinMax(final Double val) {
		if (this.dMin > val) {
			this.dMin = val;
		}
		if (this.dMax < val) {
			this.dMax = val;
		}
	}

	public InterpolationPoint getPoint(final double snr, final double length,
			final double smss, final double d) {
		for (final InterpolationPoint ip : this.interpolationPoints) {
			if ((ip.getSNR() == snr) && (ip.getLength() == length)
			        && (ip.getSMSS() == smss) && (ip.getD() == d))
				return ip;
		}
		return null;
	}

	public List<InterpolationPoint> getInterpolationPoints() {
		return this.interpolationPoints;
	}

	public List<Double> getSNRValues() {
		return this.snrValues;
	}

	public Double getSNRMin() {
		return this.snrMin;
	}

	public Double getSNRMax() {
		return this.snrMax;
	}

	public List<Double> getLengthValues() {
		return this.lengthValues;
	}

	public Double getLengthMin() {
		return this.lengthMin;
	}

	public Double getLengthMax() {
		return this.lengthMax;
	}

	public List<Double> getSMSSValues() {
		return this.smssValues;
	}

	public Double getSMSSMin() {
		return this.smssMin;
	}

	public Double getSMSSMax() {
		return this.smssMax;
	}

	public List<Double> getDValues() {
		return this.dValues;
	}

	public Double getDMin() {
		return this.dMin;
	}

	public Double getDMax() {
		return this.dMax;
	}
}
