package main.java.edu.umassmed.omega.commons.data.analysisRunElements;

import java.util.Date;
import java.util.List;
import java.util.Map;

import main.java.edu.umassmed.omega.commons.data.coreElements.OmegaExperimenter;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class OmegaTrackingMeasuresIntensityRun extends OmegaTrackingMeasuresRun {

	// private Map<OmegaROI, List<Double>> particlesMeasures;
	// Intensity
	private final Map<OmegaSegment, Double[]> peakSignalsMap;
	private final Map<OmegaSegment, Double[]> meanSignalsMap;
	private final Map<OmegaSegment, Double[]> localBackgroundsMap;
	private final Map<OmegaSegment, Double[]> localSNRsMap;

	public OmegaTrackingMeasuresIntensityRun(final OmegaExperimenter owner,
	        final OmegaAlgorithmSpecification algorithmSpec,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments,
	        final Map<OmegaSegment, Double[]> peakSignalsMap,
	        final Map<OmegaSegment, Double[]> meanSignalsMap,
	        final Map<OmegaSegment, Double[]> localBackgroundsMap,
	        final Map<OmegaSegment, Double[]> localSNRsMap) {
		super(owner, algorithmSpec, segments);
		this.peakSignalsMap = peakSignalsMap;
		this.meanSignalsMap = meanSignalsMap;
		this.localBackgroundsMap = localBackgroundsMap;
		this.localSNRsMap = localSNRsMap;
	}

	public OmegaTrackingMeasuresIntensityRun(final OmegaExperimenter owner,
	        final OmegaAlgorithmSpecification algorithmSpec, final String name,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments,
	        final Map<OmegaSegment, Double[]> peakSignalsMap,
	        final Map<OmegaSegment, Double[]> meanSignalsMap,
	        final Map<OmegaSegment, Double[]> localBackgroundsMap,
	        final Map<OmegaSegment, Double[]> localSNRsMap) {
		super(owner, algorithmSpec, name, segments);
		this.peakSignalsMap = peakSignalsMap;
		this.meanSignalsMap = meanSignalsMap;
		this.localBackgroundsMap = localBackgroundsMap;
		this.localSNRsMap = localSNRsMap;
	}

	public OmegaTrackingMeasuresIntensityRun(final OmegaExperimenter owner,
	        final OmegaAlgorithmSpecification algorithmSpec,
	        final Date timeStamps, final String name,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments,
	        final Map<OmegaSegment, Double[]> peakSignalsMap,
	        final Map<OmegaSegment, Double[]> meanSignalsMap,
	        final Map<OmegaSegment, Double[]> localBackgroundsMap,
	        final Map<OmegaSegment, Double[]> localSNRsMap) {
		super(owner, algorithmSpec, timeStamps, name, segments);
		this.peakSignalsMap = peakSignalsMap;
		this.meanSignalsMap = meanSignalsMap;
		this.localBackgroundsMap = localBackgroundsMap;
		this.localSNRsMap = localSNRsMap;
	}

	public Map<OmegaSegment, Double[]> getPeakSignalsResults() {
		return this.peakSignalsMap;
	}

	public Map<OmegaSegment, Double[]> getMeanSignalsResults() {
		return this.meanSignalsMap;
	}

	public Map<OmegaSegment, Double[]> getLocalBackgroundsResults() {
		return this.localBackgroundsMap;
	}

	public Map<OmegaSegment, Double[]> getLocalSNRsResults() {
		return this.localSNRsMap;
	}
}
