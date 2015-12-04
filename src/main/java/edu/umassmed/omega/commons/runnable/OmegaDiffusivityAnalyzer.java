package main.java.edu.umassmed.omega.commons.runnable;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import main.java.edu.umassmed.omega.commons.constants.OmegaConstants;
import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaParameter;
import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaSNRRun;
import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresDiffusivityRun;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import main.java.edu.umassmed.omega.commons.errorInterpolation.ErrorInterpolation;
import main.java.edu.umassmed.omega.commons.gui.interfaces.OmegaMessageDisplayerPanelInterface;
import main.java.edu.umassmed.omega.commons.libraries.OmegaDiffusivityLibrary;

public class OmegaDiffusivityAnalyzer implements Runnable {

	private static ErrorInterpolation dInterpolation;
	private static ErrorInterpolation smssInterpolation;

	private final OmegaMessageDisplayerPanelInterface displayerPanel;

	private final Map<OmegaTrajectory, List<OmegaSegment>> segments;
	private final Map<OmegaSegment, Double[]> nyMap;
	private final Map<OmegaSegment, Double[]> gammaFromLogMap;
	private final Map<OmegaSegment, Double[]> gammaMap;
	private final Map<OmegaSegment, Double[][]> gammaDFromLogMap;
	private final Map<OmegaSegment, Double[][]> gammaDMap;
	private final Map<OmegaSegment, Double[][]> logMuMap;
	private final Map<OmegaSegment, Double[][]> muMap;
	private final Map<OmegaSegment, Double[][]> logDeltaTMap;
	private final Map<OmegaSegment, Double[][]> deltaTMap;
	private final Map<OmegaSegment, Double[]> smssFromLogMap;
	private final Map<OmegaSegment, Double[]> smssMap;
	private final Map<OmegaROI, Double> snrValues;

	// 0: errorD, 1: errorSMSS
	private final Map<OmegaSegment, Double[]> errors, errorsFromLog;

	private final List<OmegaParameter> params;

	private int windowDivisor;
	private String option, computeError;
	private final OmegaSNRRun snrRun;
	private final OmegaTrackingMeasuresDiffusivityRun diffRun;

	public OmegaDiffusivityAnalyzer(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final List<OmegaParameter> params) {
		this(null, segments, params, null);
	}

	public OmegaDiffusivityAnalyzer(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final List<OmegaParameter> params, final OmegaSNRRun snrRun) {
		this(null, segments, params, snrRun, null);
	}

	public OmegaDiffusivityAnalyzer(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final List<OmegaParameter> params, final OmegaSNRRun snrRun,
	        final OmegaTrackingMeasuresDiffusivityRun diffRun) {
		this(null, segments, params, snrRun, diffRun);
	}

	public OmegaDiffusivityAnalyzer(
	        final OmegaMessageDisplayerPanelInterface displayerPanel,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final List<OmegaParameter> params) {
		this(displayerPanel, segments, params, null);
	}

	public OmegaDiffusivityAnalyzer(
	        final OmegaMessageDisplayerPanelInterface displayerPanel,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final List<OmegaParameter> params, final OmegaSNRRun snrRun) {
		this(displayerPanel, segments, params, snrRun, null);
	}

	public OmegaDiffusivityAnalyzer(
			final OmegaMessageDisplayerPanelInterface displayerPanel,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final List<OmegaParameter> params, final OmegaSNRRun snrRun,
	        final OmegaTrackingMeasuresDiffusivityRun diffRun) {
		this.displayerPanel = displayerPanel;

		this.diffRun = diffRun;
		this.snrRun = snrRun;
		this.segments = new LinkedHashMap<OmegaTrajectory, List<OmegaSegment>>(
				segments);

		if (snrRun != null) {
			this.snrValues = snrRun.getResultingLocalSNRs();
			// this.snrValues =
					// snrRun.getResultingLocalBhattacharyyaPoissonSNRs();
		} else {
			this.snrValues = null;
		}

		this.params = params;
		this.initializeParameters();

		if (!this.computeError
		        .equals(OmegaConstants.PARAMETER_ERROR_OPTION_ONLY)) {
			this.nyMap = new LinkedHashMap<>();
			if (this.option
					.equals(OmegaConstants.PARAMETER_DIFFUSIVITY_LOG_OPTION_LOG_ONLY)) {
				this.gammaMap = null;
				this.gammaDMap = null;
				this.muMap = null;
				this.deltaTMap = null;
				this.smssMap = null;
			} else {
				this.gammaMap = new LinkedHashMap<>();
				this.gammaDMap = new LinkedHashMap<>();
				this.muMap = new LinkedHashMap<>();
				this.deltaTMap = new LinkedHashMap<>();
				this.smssMap = new LinkedHashMap<>();
			}

			if (this.option
					.equals(OmegaConstants.PARAMETER_DIFFUSIVITY_LOG_OPTION_LINEAR_ONLY)) {
				this.gammaFromLogMap = null;
				this.gammaDFromLogMap = null;
				this.logMuMap = null;
				this.logDeltaTMap = null;
				this.smssFromLogMap = null;
			} else {
				this.gammaFromLogMap = new LinkedHashMap<>();
				this.gammaDFromLogMap = new LinkedHashMap<>();
				this.logMuMap = new LinkedHashMap<>();
				this.logDeltaTMap = new LinkedHashMap<>();
				this.smssFromLogMap = new LinkedHashMap<>();
			}
		} else {
			this.nyMap = diffRun.getNyResults();
			this.gammaMap = diffRun.getGammaResults();
			this.gammaDMap = diffRun.getGammaDResults();
			this.muMap = diffRun.getMuResults();
			this.deltaTMap = diffRun.getDeltaTResults();
			this.smssMap = diffRun.getSmssResults();
			this.gammaFromLogMap = diffRun.getGammaFromLogResults();
			this.gammaDFromLogMap = diffRun.getGammaDFromLogResults();
			this.logMuMap = diffRun.getLogMuResults();
			this.logDeltaTMap = diffRun.getLogDeltaTResults();
			this.smssFromLogMap = diffRun.getSmssFromLogResults();
		}

		if (!this.computeError
				.equals(OmegaConstants.PARAMETER_ERROR_OPTION_DISABLED)) {
			this.errors = new LinkedHashMap<OmegaSegment, Double[]>();
			this.errorsFromLog = new LinkedHashMap<OmegaSegment, Double[]>();
			this.initializeInterpolators();
		} else {
			this.errors = null;
			this.errorsFromLog = null;
		}

	}

	private void initializeParameters() {
		for (final OmegaParameter param : this.params) {
			if (param.getName().equals(
					OmegaConstants.PARAMETER_DIFFUSIVITY_WINDOW)) {
				if (param.getStringValue().equals(
						OmegaConstants.PARAMETER_DIFFUSIVITY_WINDOW_10)) {
					this.windowDivisor = 10;
				} else if (param.getStringValue().equals(
						OmegaConstants.PARAMETER_DIFFUSIVITY_WINDOW_5)) {
					this.windowDivisor = 5;
				} else {
					this.windowDivisor = 3;
				}
			} else if (param.getName().equals(
					OmegaConstants.PARAMETER_DIFFUSIVITY_LOG_OPTION)) {
				this.option = param.getStringValue();
			} else if (param.getName().equals(
					OmegaConstants.PARAMETER_ERROR_OPTION)) {
				this.computeError = param.getStringValue();
			}
		}
	}

	private void initializeInterpolators() {
		if (OmegaDiffusivityAnalyzer.dInterpolation == null) {
			OmegaDiffusivityAnalyzer.dInterpolation = new ErrorInterpolation(
			        ErrorInterpolation.DEFAULT_D_FILE);
			if (this.displayerPanel != null) {
				this.displayerPanel.updateMessageStatus(new AnalyzerEvent(
				        "Initializing D interpolation", false, false));
			}
		}
		if (OmegaDiffusivityAnalyzer.smssInterpolation == null) {
			OmegaDiffusivityAnalyzer.smssInterpolation = new ErrorInterpolation(
			        ErrorInterpolation.DEFAULT_SMSS_FILE);
			if (this.displayerPanel != null) {
				this.displayerPanel.updateMessageStatus(new AnalyzerEvent(
				        "Initializing SMSS interpolation", false, false));
			}
		}
	}

	@Override
	public void run() {
		if (!this.computeError
		        .equals(OmegaConstants.PARAMETER_ERROR_OPTION_ONLY)) {
			this.runDiffusivityAnalysis();
		}
		if (!this.computeError
		        .equals(OmegaConstants.PARAMETER_ERROR_OPTION_DISABLED)) {
			this.runErrorAnalysis();
		}
	}

	private void runErrorAnalysis() {
		int counter = 1;
		final int max = this.segments.keySet().size();
		for (final OmegaTrajectory track : this.segments.keySet()) {
			final List<OmegaROI> rois = track.getROIs();
			if (this.displayerPanel != null) {
				this.updateStatusAsync("Processing error analysis, trajectory "
				        + track.getName() + " " + counter + "/" + max, false,
				        false);
			}
			for (final OmegaSegment segment : this.segments.get(track)) {
				final int roiStart = rois.indexOf(segment.getStartingROI());
				final int roiEnd = rois.indexOf(segment.getEndingROI());
				final List<OmegaROI> segmentROIs = rois.subList(roiStart,
				        roiEnd + 1);
				Double segmentSNR = 0.0;
				for (int i = 0; i < segmentROIs.size(); i++) {
					final OmegaROI roi = segmentROIs.get(i);
					segmentSNR += this.snrValues.get(roi);
				}
				segmentSNR /= segmentROIs.size();

				if (!this.option
				        .equals(OmegaConstants.PARAMETER_DIFFUSIVITY_LOG_OPTION_LOG_ONLY)) {
					final double dVal = this.gammaDMap.get(segment)[2][3];
					final double smssVal = this.smssMap.get(segment)[0];
					final double lVal = segmentROIs.size();
					boolean canComputeError = true;
					final int valD = OmegaDiffusivityAnalyzer.dInterpolation
					        .validateD(dVal);
					canComputeError = this.validateErrorParam(valD, dVal, "D",
					        false, track, segment) && canComputeError;
					final int valSMSS = OmegaDiffusivityAnalyzer.dInterpolation
					        .validateSMSS(smssVal);
					canComputeError = this.validateErrorParam(valSMSS, smssVal,
					        "SMSS", false, track, segment) && canComputeError;
					final int valL = OmegaDiffusivityAnalyzer.dInterpolation
					        .validateLength(lVal);
					canComputeError = this.validateErrorParam(valL, lVal, "L",
					        false, track, segment) && canComputeError;
					final int valSNR = OmegaDiffusivityAnalyzer.dInterpolation
					        .validateSNR(segmentSNR);
					canComputeError = this.validateErrorParam(valSNR,
					        segmentSNR, "SNR", false, track, segment)
							&& canComputeError;
					if (canComputeError) {
						final Double dError = OmegaDiffusivityAnalyzer.dInterpolation
						        .interpolate(segmentSNR, lVal, smssVal, dVal);
						final Double smssError = OmegaDiffusivityAnalyzer.smssInterpolation
						        .interpolate(segmentSNR, lVal, smssVal, dVal);
						final Double[] segmentErrors = { dError, smssError };
						this.errors.put(segment, segmentErrors);
					}
				}
				if (!this.option
				        .equals(OmegaConstants.PARAMETER_DIFFUSIVITY_LOG_OPTION_LINEAR_ONLY)) {
					final double dVal = this.gammaDFromLogMap.get(segment)[2][3];
					final double smssVal = this.smssFromLogMap.get(segment)[0];
					final double lVal = segmentROIs.size();
					boolean canComputeError = true;
					final int valD = OmegaDiffusivityAnalyzer.dInterpolation
					        .validateD(dVal);
					canComputeError = this.validateErrorParam(valD, dVal, "D",
							true, track, segment) && canComputeError;
					final int valSMSS = OmegaDiffusivityAnalyzer.dInterpolation
					        .validateSMSS(smssVal);
					canComputeError = this.validateErrorParam(valSMSS, smssVal,
					        "SMSS", true, track, segment) && canComputeError;
					final int valL = OmegaDiffusivityAnalyzer.dInterpolation
					        .validateLength(lVal);
					canComputeError = this.validateErrorParam(valL, lVal, "L",
					        true, track, segment) && canComputeError;
					final int valSNR = OmegaDiffusivityAnalyzer.dInterpolation
					        .validateSNR(segmentSNR);
					canComputeError = this.validateErrorParam(valSNR,
					        segmentSNR, "SNR", true, track, segment)
					        && canComputeError;
					if (canComputeError) {
						final Double dError = OmegaDiffusivityAnalyzer.dInterpolation
						        .interpolate(segmentSNR, lVal, smssVal, dVal);
						final Double smssError = OmegaDiffusivityAnalyzer.smssInterpolation
						        .interpolate(segmentSNR, lVal, smssVal, dVal);
						final Double[] segmentErrors = { dError, smssError };
						this.errorsFromLog.put(segment, segmentErrors);
					}
				}
				counter++;
			}
		}
		if (this.displayerPanel != null) {
			this.updateStatusAsync("Error analysis ended", true, false);
		}
	}

	private void runDiffusivityAnalysis() {
		int counter = 1;
		final int max = this.segments.keySet().size();
		for (final OmegaTrajectory track : this.segments.keySet()) {
			final List<OmegaROI> rois = track.getROIs();
			if (this.displayerPanel != null) {
				this.updateStatusAsync(
						"Processing diffusivity analysis, trajectory "
				                + track.getName() + " " + counter + "/" + max,
				        false, false);
			}
			for (final OmegaSegment segment : this.segments.get(track)) {
				final int roiStart = rois.indexOf(segment.getStartingROI());
				final int roiEnd = rois.indexOf(segment.getEndingROI());
				final List<OmegaROI> segmentROIs = rois.subList(roiStart,
						roiEnd + 1);
				final Double[] x = new Double[segmentROIs.size()];
				final Double[] y = new Double[segmentROIs.size()];
				for (int i = 0; i < segmentROIs.size(); i++) {
					final OmegaROI roi = segmentROIs.get(i);
					x[i] = roi.getX();
					y[i] = roi.getY();
				}
				if ((x.length / this.windowDivisor) < 2) {
					System.out.println(track.getName()
							+ " skipped because length=" + x.length
							+ " divided by wd=" + this.windowDivisor
							+ " less than 2");
					continue;
				}
				final int Delta_t = 1; // Time between frames?
				Double[] ny = null;
				if (!this.option
						.equals(OmegaConstants.PARAMETER_DIFFUSIVITY_LOG_OPTION_LOG_ONLY)) {
					final Double[][][] nu_DeltaNDeltaT_DeltaNMu_GammaD_SMSS = OmegaDiffusivityLibrary
							.computeNu_DeltaNDeltaT_DeltaNMu_GammaD_SMSS(x, y,
									this.windowDivisor, Delta_t);
					ny = nu_DeltaNDeltaT_DeltaNMu_GammaD_SMSS[0][0];
					final Double[][] deltaT = nu_DeltaNDeltaT_DeltaNMu_GammaD_SMSS[1];
					final Double[][] mu = nu_DeltaNDeltaT_DeltaNMu_GammaD_SMSS[2];
					final Double[][] gammaD = nu_DeltaNDeltaT_DeltaNMu_GammaD_SMSS[3];
					final Double[] gamma = new Double[ny.length];
					for (int i = 0; i < gammaD.length; i++) {
						gamma[i] = gammaD[i][0];
					}
					final Double[] smss = nu_DeltaNDeltaT_DeltaNMu_GammaD_SMSS[4][0];
					this.nyMap.put(segment, ny);
					this.gammaMap.put(segment, gamma);
					this.deltaTMap.put(segment, deltaT);
					this.muMap.put(segment, mu);
					this.gammaDMap.put(segment, gammaD);
					this.smssMap.put(segment, smss);
				}

				if (!this.option
						.equals(OmegaConstants.PARAMETER_DIFFUSIVITY_LOG_OPTION_LINEAR_ONLY)) {
					final Double[][][] nu_DeltaNLogDeltaT_DeltaNLogMu_GammaDFromLog_SMSSFromLog = OmegaDiffusivityLibrary
							.computeNu_DeltaNLogDeltaT_DeltaNLogMu_GammaDFromLog_SMSSFromLog(
									x, y, this.windowDivisor, Delta_t);
					if (ny == null) {
						ny = nu_DeltaNLogDeltaT_DeltaNLogMu_GammaDFromLog_SMSSFromLog[0][0];
					}
					final Double[][] logDeltaT = nu_DeltaNLogDeltaT_DeltaNLogMu_GammaDFromLog_SMSSFromLog[1];
					final Double[][] logMu = nu_DeltaNLogDeltaT_DeltaNLogMu_GammaDFromLog_SMSSFromLog[2];
					final Double[][] gammaDFromLog = nu_DeltaNLogDeltaT_DeltaNLogMu_GammaDFromLog_SMSSFromLog[3];
					System.out.println(gammaDFromLog[2][3]);
					final Double[] gammaFromLog = new Double[ny.length];
					for (int i = 0; i < gammaDFromLog.length; i++) {
						gammaFromLog[i] = gammaDFromLog[i][0];
					}
					final Double[] smssFromLog = nu_DeltaNLogDeltaT_DeltaNLogMu_GammaDFromLog_SMSSFromLog[4][0];
					this.gammaFromLogMap.put(segment, gammaFromLog);
					this.logDeltaTMap.put(segment, logDeltaT);
					this.logMuMap.put(segment, logMu);
					this.gammaDFromLogMap.put(segment, gammaDFromLog);
					this.smssFromLogMap.put(segment, smssFromLog);
				}
				this.nyMap.put(segment, ny);
				counter++;
			}
		}
		if (this.displayerPanel != null) {
			this.updateStatusAsync("Diffusivity analysis ended", true, false);
		}
	}

	public Map<OmegaTrajectory, List<OmegaSegment>> getSegments() {
		return this.segments;
	}

	public Map<OmegaSegment, Double[]> getNyResults() {
		return this.nyMap;
	}

	public Map<OmegaSegment, Double[]> getGammaFromLogResults() {
		return this.gammaFromLogMap;
	}

	public Map<OmegaSegment, Double[]> getGammaResults() {
		return this.gammaMap;
	}

	public Map<OmegaSegment, Double[][]> getGammaDFromLogResults() {
		return this.gammaDFromLogMap;
	}

	public Map<OmegaSegment, Double[][]> getGammaDResults() {
		return this.gammaDMap;
	}

	public Map<OmegaSegment, Double[][]> getLogMuResults() {
		return this.logMuMap;
	}

	public Map<OmegaSegment, Double[][]> getMuResults() {
		return this.muMap;
	}

	public Map<OmegaSegment, Double[][]> getLogDeltaTResults() {
		return this.logDeltaTMap;
	}

	public Map<OmegaSegment, Double[][]> getDeltaTResults() {
		return this.deltaTMap;
	}

	public Map<OmegaSegment, Double[]> getSmssFromLogResults() {
		return this.smssFromLogMap;
	}

	public Map<OmegaSegment, Double[]> getSmssResults() {
		return this.smssMap;
	}

	public Map<OmegaSegment, Double[]> getErrors() {
		return this.errors;
	}

	public Map<OmegaSegment, Double[]> getErrorsFromLog() {
		return this.errorsFromLog;
	}

	public List<OmegaParameter> getParameters() {
		return this.params;
	}

	private boolean validateErrorParam(final int validation, final double val,
			final String valName, final boolean isLog,
	        final OmegaTrajectory track, final OmegaSegment segm) {
		if (validation != 0) {
			String error = " is ";
			if (validation == 1) {
				error += " bigger ";
			} else {
				error += " smaller ";
			}
			String string = null;
			if (isLog) {
				string = "Log " + valName;
			} else {
				string = "Linear " + valName;
			}
			this.updateStatusAsync(
			        "<html>" + string + " value " + val + error
			                + " than the possible value to compute error for "
			                + track.getName() + " from "
			                + segm.getStartingROI().getFrameIndex() + " to "
			                + segm.getEndingROI().getFrameIndex() + "</html>",
			        false, true);
			return false;
		}
		return true;
	}

	private void updateStatusSync(final String msg, final boolean ended,
	        final boolean dialog) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					OmegaDiffusivityAnalyzer.this.displayerPanel
					.updateMessageStatus(new AnalyzerEvent(msg, ended,
					                dialog));
				}
			});
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void updateStatusAsync(final String msg, final boolean ended,
	        final boolean dialog) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				OmegaDiffusivityAnalyzer.this.displayerPanel
				.updateMessageStatus(new AnalyzerEvent(msg, ended,
				                dialog));
			}
		});
	}

	public OmegaSNRRun getSNRRun() {
		return this.snrRun;
	}

	public OmegaTrackingMeasuresDiffusivityRun getTrackiMeasuresDiffusivityRun() {
		return this.diffRun;
	}
}