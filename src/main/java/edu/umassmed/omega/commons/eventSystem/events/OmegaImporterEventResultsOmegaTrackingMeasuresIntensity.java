package edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainerInterface;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.utilities.OmegaIOUtility;

public class OmegaImporterEventResultsOmegaTrackingMeasuresIntensity extends
		OmegaImporterEventResultsOmegaTrackingMeasures {
	// private Map<OmegaROI, List<Double>> particlesMeasures;
	// Intensity
	private final Map<OmegaSegment, Double[]> peakSignalsMap;
	private final Map<OmegaSegment, Double[]> centroidSignalsMap;
	
	private final Map<OmegaROI, Double> peakSignalsLocMap;
	private final Map<OmegaROI, Double> centroidSignalsLocMap;

	// SNR related START
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

	public OmegaImporterEventResultsOmegaTrackingMeasuresIntensity(
			final OmegaIOUtility source,
			final OmegaAnalysisRunContainerInterface container,
			final Map<Integer, String> parents,
			final Map<String, String> analysisData,
			final Map<String, String> paramData,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, Double[]> peakSignalsMap,
			final Map<OmegaSegment, Double[]> centroidSignalsMap,
			final Map<OmegaROI, Double> peakSignalsLocMap,
			final Map<OmegaROI, Double> centroidSignalsLocMap,
			final Map<OmegaSegment, Double[]> meanSignalsMap,
			final Map<OmegaSegment, Double[]> backgroundsMap,
			final Map<OmegaSegment, Double[]> noisesMap,
			final Map<OmegaSegment, Double[]> snrsMap,
			final Map<OmegaSegment, Double[]> areasMap,
			final Map<OmegaROI, Double> meanSignalsLocMap,
			final Map<OmegaROI, Double> backgroundsLocMap,
			final Map<OmegaROI, Double> noisesLocMap,
			final Map<OmegaROI, Double> areasLocMap,
			final Map<OmegaROI, Double> snrsLocMap,
			final boolean completeChainAfterImport) {
		super(source, container, parents, analysisData, paramData, segments,
				completeChainAfterImport);
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
	// SNR related END

}
