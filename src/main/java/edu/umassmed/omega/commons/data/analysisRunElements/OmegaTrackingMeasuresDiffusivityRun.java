package edu.umassmed.omega.commons.data.analysisRunElements;

import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.coreElements.OmegaExperimenter;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class OmegaTrackingMeasuresDiffusivityRun extends
		OmegaTrackingMeasuresRun {
	
	// Diffusivity
	private final Map<OmegaSegment, Double[]> nyMap;
	private final Map<OmegaSegment, Double[][]> logMuMap;
	private final Map<OmegaSegment, Double[][]> muMap;
	private final Map<OmegaSegment, Double[][]> logDeltaTMap;
	private final Map<OmegaSegment, Double[][]> deltaTMap;
	private final Map<OmegaSegment, Double[][]> gammaDFromLogMap;
	private final Map<OmegaSegment, Double[][]> gammaDMap;
	private final Map<OmegaSegment, Double[]> gammaFromLogMap;
	// private final Map<OmegaSegment, Double[]> gammaMap;
	private final Map<OmegaSegment, Double[]> smssFromLogMap;
	// private final Map<OmegaSegment, Double[]> smssMap;
	// private final Map<OmegaSegment, Double[]> errorsMap;
	private final Map<OmegaSegment, Double[]> errorsFromLogMap;
	
	private final OmegaSNRRun snrRun;
	private final OmegaTrackingMeasuresDiffusivityRun diffusivityRun;

	private final Double minDetectableODC;
	
	public OmegaTrackingMeasuresDiffusivityRun(
			final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, Double[]> ny,
			final Map<OmegaSegment, Double[][]> mu,
			final Map<OmegaSegment, Double[][]> logMu,
			final Map<OmegaSegment, Double[][]> deltaT,
			final Map<OmegaSegment, Double[][]> logDeltaT,
			final Map<OmegaSegment, Double[][]> gammaD,
			final Map<OmegaSegment, Double[][]> gammaDLog,
			// final Map<OmegaSegment, Double[]> gamma,
			final Map<OmegaSegment, Double[]> gammaLog,
			// final Map<OmegaSegment, Double[]> smss,
			final Map<OmegaSegment, Double[]> smssLog,
			// final Map<OmegaSegment, Double[]> errors,
			final Map<OmegaSegment, Double[]> errorsLog,
			final Double minDetectableODC, final OmegaSNRRun snrRun,
			final OmegaTrackingMeasuresDiffusivityRun diffusivityRun) {
		super(owner, algorithmSpec,
				AnalysisRunType.OmegaTrackingMeasuresDiffusivityRun, segments,
				TrackingMeasuresType.Diffusivity);
		this.nyMap = ny;
		this.muMap = mu;
		this.logMuMap = logMu;
		this.deltaTMap = deltaT;
		this.logDeltaTMap = logDeltaT;
		this.gammaDMap = gammaD;
		this.gammaDFromLogMap = gammaDLog;
		// this.gammaMap = gamma;
		this.gammaFromLogMap = gammaLog;
		// this.smssMap = smss;
		this.smssFromLogMap = smssLog;
		// this.errorsMap = errors;
		this.errorsFromLogMap = errorsLog;
		this.snrRun = snrRun;
		this.diffusivityRun = diffusivityRun;

		this.minDetectableODC = minDetectableODC;
	}
	
	public OmegaTrackingMeasuresDiffusivityRun(
			final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec,
			final String name,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, Double[]> ny,
			final Map<OmegaSegment, Double[][]> mu,
			final Map<OmegaSegment, Double[][]> logMu,
			final Map<OmegaSegment, Double[][]> deltaT,
			final Map<OmegaSegment, Double[][]> logDeltaT,
			final Map<OmegaSegment, Double[][]> gammaD,
			final Map<OmegaSegment, Double[][]> gammaDLog,
			// final Map<OmegaSegment, Double[]> gamma,
			final Map<OmegaSegment, Double[]> gammaLog,
			// final Map<OmegaSegment, Double[]> smss,
			final Map<OmegaSegment, Double[]> smssLog,
			// final Map<OmegaSegment, Double[]> errors,
			final Map<OmegaSegment, Double[]> errorsLog,
			final Double minDetectableODC, final OmegaSNRRun snrRun,
			final OmegaTrackingMeasuresDiffusivityRun diffusivityRun) {
		super(owner, algorithmSpec,
				AnalysisRunType.OmegaTrackingMeasuresDiffusivityRun, name,
				segments, TrackingMeasuresType.Diffusivity);
		this.nyMap = ny;
		this.muMap = mu;
		this.logMuMap = logMu;
		this.deltaTMap = deltaT;
		this.logDeltaTMap = logDeltaT;
		this.gammaDMap = gammaD;
		this.gammaDFromLogMap = gammaDLog;
		// this.gammaMap = gamma;
		this.gammaFromLogMap = gammaLog;
		// this.smssMap = smss;
		this.smssFromLogMap = smssLog;
		// this.errorsMap = errors;
		this.errorsFromLogMap = errorsLog;
		this.snrRun = snrRun;
		this.diffusivityRun = diffusivityRun;
		this.minDetectableODC = minDetectableODC;
	}
	
	public OmegaTrackingMeasuresDiffusivityRun(
			final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec,
			final Date timeStamps,
			final String name,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, Double[]> ny,
			final Map<OmegaSegment, Double[][]> mu,
			final Map<OmegaSegment, Double[][]> logMu,
			final Map<OmegaSegment, Double[][]> deltaT,
			final Map<OmegaSegment, Double[][]> logDeltaT,
			final Map<OmegaSegment, Double[][]> gammaD,
			final Map<OmegaSegment, Double[][]> gammaDLog,
			// final Map<OmegaSegment, Double[]> gamma,
			final Map<OmegaSegment, Double[]> gammaLog,
			// final Map<OmegaSegment, Double[]> smss,
			final Map<OmegaSegment, Double[]> smssLog,
			// final Map<OmegaSegment, Double[]> errors,
			final Map<OmegaSegment, Double[]> errorsLog,
			final Double minDetectableODC, final OmegaSNRRun snrRun,
			final OmegaTrackingMeasuresDiffusivityRun diffusivityRun) {
		super(owner, algorithmSpec,
				AnalysisRunType.OmegaTrackingMeasuresDiffusivityRun,
				timeStamps, name, segments, TrackingMeasuresType.Diffusivity);
		this.nyMap = ny;
		this.muMap = mu;
		this.logMuMap = logMu;
		this.deltaTMap = deltaT;
		this.logDeltaTMap = logDeltaT;
		this.gammaDMap = gammaD;
		this.gammaDFromLogMap = gammaDLog;
		// this.gammaMap = gamma;
		this.gammaFromLogMap = gammaLog;
		// this.smssMap = smss;
		this.smssFromLogMap = smssLog;
		// this.errorsMap = errors;
		this.errorsFromLogMap = errorsLog;
		this.snrRun = snrRun;
		this.diffusivityRun = diffusivityRun;
		this.minDetectableODC = minDetectableODC;
	}
	
	public Map<OmegaSegment, Double[]> getNyResults() {
		return this.nyMap;
	}
	
	public Map<OmegaSegment, Double[]> getGammaFromLogResults() {
		return this.gammaFromLogMap;
	}
	
	// public Map<OmegaSegment, Double[]> getGammaResults() {
	// return this.gammaMap;
	// }
	
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
	
	// public Map<OmegaSegment, Double[]> getSmssResults() {
	// return this.smssMap;
	// }
	
	// public Map<OmegaSegment, Double[]> getErrorsResults() {
	// return this.errorsMap;
	// }
	
	public Map<OmegaSegment, Double[]> getErrosFromLogResults() {
		return this.errorsFromLogMap;
	}

	public Double getMinimumDetectableODC() {
		return this.minDetectableODC;
	}
	
	public OmegaSNRRun getSNRRun() {
		return this.snrRun;
	}
	
	public OmegaTrackingMeasuresDiffusivityRun getTrackingMeasuresDiffusivityRun() {
		return this.diffusivityRun;
	}
}
