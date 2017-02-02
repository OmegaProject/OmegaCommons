package edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParameter;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaSNRRun;
import edu.umassmed.omega.commons.data.coreElements.OmegaElement;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventResultsTrackingMeasuresIntensity extends
        OmegaPluginEventResultsTrackingMeasures {
	// private Map<OmegaROI, List<Double>> particlesMeasures;
	// Intensity
	private final Map<OmegaSegment, Double[]> peakSignalsMap;
	private final Map<OmegaSegment, Double[]> centroidSignalsMap;
	
	// SNR related START
	private final OmegaSNRRun snrRun;
	private final Map<OmegaSegment, Double[]> noisesMap;
	private final Map<OmegaSegment, Double[]> snrsMap;
	private final Map<OmegaSegment, Double[]> areasMap;
	private final Map<OmegaSegment, Double[]> meanSignalsMap;
	
	// SNR related END
	
	public OmegaPluginEventResultsTrackingMeasuresIntensity(
	        final OmegaPlugin source, final OmegaElement element,
	        final List<OmegaParameter> params,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, Double[]> peakSignalsMap,
	        final Map<OmegaSegment, Double[]> centroidSignalsMap,
			final Map<OmegaSegment, Double[]> noisesMap,
			final Map<OmegaSegment, Double[]> snrsMap,
			final Map<OmegaSegment, Double[]> areasMap,
			final Map<OmegaSegment, Double[]> meanSignalsMap,
			final OmegaSNRRun snrRun) {
		super(source, element, params, segments);
		this.peakSignalsMap = peakSignalsMap;
		this.centroidSignalsMap = centroidSignalsMap;
		
		// SNR related START
		this.noisesMap = noisesMap;
		this.meanSignalsMap = meanSignalsMap;
		this.snrsMap = snrsMap;
		this.areasMap = areasMap;
		
		this.snrRun = snrRun;
		// SNR related END

	}

	public Map<OmegaSegment, Double[]> getResultingPeakSignals() {
		return this.peakSignalsMap;
	}

	public Map<OmegaSegment, Double[]> getResultingCentroidSignals() {
		return this.centroidSignalsMap;
	}
	
	// SNR related START
	public Map<OmegaSegment, Double[]> getMeanSignalsResults() {
		return this.meanSignalsMap;
	}

	public Map<OmegaSegment, Double[]> getAreasResults() {
		return this.areasMap;
	}

	public Map<OmegaSegment, Double[]> getNoisesResults() {
		return this.noisesMap;
	}

	public Map<OmegaSegment, Double[]> getSNRsResults() {
		return this.snrsMap;
	}
	
	public OmegaSNRRun getSNRRun() {
		return this.snrRun;
	}
	// SNR related END
}
