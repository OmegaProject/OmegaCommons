package main.java.edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaParameter;
import main.java.edu.umassmed.omega.commons.data.coreElements.OmegaElement;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import main.java.edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventResultsTrackingMeasuresIntensity extends
        OmegaPluginEventResultsTrackingMeasures {
	// private Map<OmegaROI, List<Double>> particlesMeasures;
	// Intensity
	private final Map<OmegaSegment, Double[]> peakSignalsMap;
	private final Map<OmegaSegment, Double[]> meanSignalsMap;
	private final Map<OmegaSegment, Double[]> localBackgroundsMap;
	private final Map<OmegaSegment, Double[]> localSNRsMap;

	public OmegaPluginEventResultsTrackingMeasuresIntensity(
	        final OmegaPlugin source, final OmegaElement element,
	        final List<OmegaParameter> params,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, Double[]> peakSignalsMap,
			final Map<OmegaSegment, Double[]> meanSignalsMap,
			final Map<OmegaSegment, Double[]> localBackgroundsMap,
			final Map<OmegaSegment, Double[]> localSNRsMap) {
		super(source, element, params, segments);
		this.peakSignalsMap = peakSignalsMap;
		this.meanSignalsMap = meanSignalsMap;
		this.localBackgroundsMap = localBackgroundsMap;
		this.localSNRsMap = localSNRsMap;
	}

	public Map<OmegaSegment, Double[]> getResultingPeakSignals() {
		return this.peakSignalsMap;
	}

	public Map<OmegaSegment, Double[]> getResultingMeanSignals() {
		return this.meanSignalsMap;
	}

	public Map<OmegaSegment, Double[]> getResultingLocalBackgrounds() {
		return this.localBackgroundsMap;
	}

	public Map<OmegaSegment, Double[]> getResultingLocalSNRs() {
		return this.localSNRsMap;
	}
}
