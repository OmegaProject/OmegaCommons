package edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParameter;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaSNRRun;
import edu.umassmed.omega.commons.data.coreElements.OmegaElement;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventResultsTrackingMeasuresIntensity extends
OmegaPluginEventResultsTrackingMeasures {
	// private Map<OmegaROI, List<Double>> particlesMeasures;
	// Intensity
	private final Map<OmegaSegment, Double[]> peakSignalsMap;
	private final Map<OmegaSegment, Double[]> centroidSignalsMap;
	
	private final Map<OmegaROI, Double> peakSignalsLocMap;
	private final Map<OmegaROI, Double> centroidSignalsLocMap;

	// SNR related START
	private final OmegaSNRRun snrRun;
	private final Map<OmegaSegment, Double[]> backgroundsMap;
	private final Map<OmegaSegment, Double[]> noisesMap;
	private final Map<OmegaSegment, Double[]> snrsMap;
	private final Map<OmegaSegment, Double[]> areasMap;
	private final Map<OmegaSegment, Double[]> meanSignalsMap;

	private final Map<OmegaROI, Double> meanSignalsLocMap;
	private final Map<OmegaROI, Double> backgroundsLocMap;
	private final Map<OmegaROI, Double> noisesLocMap;
	private final Map<OmegaROI, Double> areasLocMap;
	private final Map<OmegaROI, Double> snrsLocMap;

	// SNR related END

	public OmegaPluginEventResultsTrackingMeasuresIntensity(
			final OmegaPlugin source, final List<OmegaElement> selections,
			final OmegaElement element, final List<OmegaParameter> params,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, Double[]> peakSignalsMap,
			final Map<OmegaSegment, Double[]> centroidSignalsMap,
			final Map<OmegaROI, Double> peakSignalsLocMap,
			final Map<OmegaROI, Double> centroidSignalsLocMap,
			final Map<OmegaSegment, Double[]> backgroundsMap,
			final Map<OmegaSegment, Double[]> noisesMap,
			final Map<OmegaSegment, Double[]> snrsMap,
			final Map<OmegaSegment, Double[]> areasMap,
			final Map<OmegaSegment, Double[]> meanSignalsMap,
			final Map<OmegaROI, Double> backgroundsLocMap,
			final Map<OmegaROI, Double> noisesLocMap,
			final Map<OmegaROI, Double> areasLocMap,
			final Map<OmegaROI, Double> snrsLocMap,
			final Map<OmegaROI, Double> meanSignalsLocMap,
			final OmegaSNRRun snrRun) {
		super(source, selections, element, params, segments);
		this.peakSignalsMap = peakSignalsMap;
		this.centroidSignalsMap = centroidSignalsMap;

		this.peakSignalsLocMap = peakSignalsLocMap;
		this.centroidSignalsLocMap = centroidSignalsLocMap;

		// SNR related START
		this.backgroundsMap = backgroundsMap;
		this.noisesMap = noisesMap;
		this.meanSignalsMap = meanSignalsMap;
		this.snrsMap = snrsMap;
		this.areasMap = areasMap;

		this.meanSignalsLocMap = meanSignalsLocMap;
		this.backgroundsLocMap = backgroundsLocMap;
		this.noisesLocMap = noisesLocMap;
		this.areasLocMap = areasLocMap;
		this.snrsLocMap = snrsLocMap;

		this.snrRun = snrRun;
		// SNR related END
		
	}
	
	public Map<OmegaSegment, Double[]> getResultingPeakSignals() {
		return this.peakSignalsMap;
	}
	
	public Map<OmegaSegment, Double[]> getResultingCentroidSignals() {
		return this.centroidSignalsMap;
	}
	
	public Map<OmegaROI, Double> getResultingPeakSignalsLocal() {
		return this.peakSignalsLocMap;
	}
	
	public Map<OmegaROI, Double> getResultingCentroidSignalsLocal() {
		return this.centroidSignalsLocMap;
	}

	// SNR related START
	public Map<OmegaSegment, Double[]> getResultingMeanSignals() {
		return this.meanSignalsMap;
	}
	
	public Map<OmegaSegment, Double[]> getResultingAreas() {
		return this.areasMap;
	}
	
	public Map<OmegaSegment, Double[]> getResultingBackgrounds() {
		return this.backgroundsMap;
	}
	
	public Map<OmegaSegment, Double[]> getResultingNoises() {
		return this.noisesMap;
	}
	
	public Map<OmegaSegment, Double[]> getResultingSNRs() {
		return this.snrsMap;
	}

	public Map<OmegaROI, Double> getResultingMeanSignalsLocal() {
		return this.meanSignalsLocMap;
	}
	
	public Map<OmegaROI, Double> getResultingAreasLocal() {
		return this.areasLocMap;
	}
	
	public Map<OmegaROI, Double> getResultingBackgroundsLocal() {
		return this.backgroundsLocMap;
	}
	
	public Map<OmegaROI, Double> getResultingNoisesLocal() {
		return this.noisesLocMap;
	}
	
	public Map<OmegaROI, Double> getResultingSNRsLocal() {
		return this.snrsLocMap;
	}

	public OmegaSNRRun getSNRRun() {
		return this.snrRun;
	}
	// SNR related END
}
