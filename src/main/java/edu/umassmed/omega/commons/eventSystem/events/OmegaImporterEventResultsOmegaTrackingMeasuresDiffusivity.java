package edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainerInterface;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.utilities.OmegaIOUtility;

public class OmegaImporterEventResultsOmegaTrackingMeasuresDiffusivity extends
		OmegaImporterEventResultsOmegaTrackingMeasures {
	
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
	// private final Map<OmegaSegment, Double[]> errors;
	private final Map<OmegaSegment, Double[]> errorsFromLog;
	
	private final Double minDetectableODC;
	
	public OmegaImporterEventResultsOmegaTrackingMeasuresDiffusivity(
			final OmegaIOUtility source,
			final OmegaAnalysisRunContainerInterface container,
			final Map<Integer, String> parents,
			final Map<String, String> analysisData,
			final Map<String, String> paramData,
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
			final Double minDetectableODC,
			final boolean completeChainAfterImport) {
		super(source, container, parents, analysisData, paramData, segments,
				completeChainAfterImport);
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
		// this.errors = errors;
		this.errorsFromLog = errorsLog;
		
		this.minDetectableODC = minDetectableODC;
	}
	
	public Map<OmegaSegment, Double[]> getResultingNy() {
		return this.nyMap;
	}
	
	public Map<OmegaSegment, Double[]> getResultingGammaFromLog() {
		return this.gammaFromLogMap;
	}
	
	// public Map<OmegaSegment, Double[]> getResultingGamma() {
	// return this.gammaMap;
	// }
	
	public Map<OmegaSegment, Double[][]> getResultingGammaDFromLog() {
		return this.gammaDFromLogMap;
	}
	
	public Map<OmegaSegment, Double[][]> getResultingGammaD() {
		return this.gammaDMap;
	}
	
	public Map<OmegaSegment, Double[][]> getResultingLogMu() {
		return this.logMuMap;
	}
	
	public Map<OmegaSegment, Double[][]> getResultingMu() {
		return this.muMap;
	}
	
	public Map<OmegaSegment, Double[][]> getResultingLogDeltaT() {
		return this.logDeltaTMap;
	}
	
	public Map<OmegaSegment, Double[][]> getResultingDeltaT() {
		return this.deltaTMap;
	}
	
	public Map<OmegaSegment, Double[]> getResultingSmssFromLog() {
		return this.smssFromLogMap;
	}
	
	// public Map<OmegaSegment, Double[]> getResultingSmss() {
	// return this.smssMap;
	// }
	
	// public Map<OmegaSegment, Double[]> getErrors() {
	// return this.errors;
	// }
	
	public Map<OmegaSegment, Double[]> getResultingErrorsFromLog() {
		return this.errorsFromLog;
	}
	
	public Double getResultingMinimumDetectableODC() {
		return this.minDetectableODC;
	}
}
