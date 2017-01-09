package edu.umassmed.omega.commons.data.analysisRunElements;

import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.coreElements.OmegaExperimenter;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class OmegaTrackingMeasuresIntensityRun extends OmegaTrackingMeasuresRun {
	
	// private Map<OmegaROI, List<Double>> particlesMeasures;
	// Intensity
	private final Map<OmegaSegment, Double[]> peakSignalsMap;
	private final Map<OmegaSegment, Double[]> meanSignalsMap;
	private final Map<OmegaSegment, Double[]> centroidSignalsMap;
	
	public OmegaTrackingMeasuresIntensityRun(final OmegaExperimenter owner,
	        final OmegaRunDefinition algorithmSpec,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments,
	        final Map<OmegaSegment, Double[]> peakSignalsMap,
	        final Map<OmegaSegment, Double[]> meanSignalsMap,
	        final Map<OmegaSegment, Double[]> centroidSignalsMap) {
		super(owner, algorithmSpec,
		        AnalysisRunType.OmegaTrackingMeasuresIntensityRun, segments,
		        TrackingMeasuresType.Intensity);
		this.peakSignalsMap = peakSignalsMap;
		this.meanSignalsMap = meanSignalsMap;
		this.centroidSignalsMap = centroidSignalsMap;
	}
	
	public OmegaTrackingMeasuresIntensityRun(final OmegaExperimenter owner,
	        final OmegaRunDefinition algorithmSpec, final String name,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments,
	        final Map<OmegaSegment, Double[]> peakSignalsMap,
	        final Map<OmegaSegment, Double[]> meanSignalsMap,
	        final Map<OmegaSegment, Double[]> centroidSignalsMap) {
		super(owner, algorithmSpec,
		        AnalysisRunType.OmegaTrackingMeasuresIntensityRun, name,
		        segments, TrackingMeasuresType.Intensity);
		this.peakSignalsMap = peakSignalsMap;
		this.meanSignalsMap = meanSignalsMap;
		this.centroidSignalsMap = centroidSignalsMap;
	}
	
	public OmegaTrackingMeasuresIntensityRun(final OmegaExperimenter owner,
	        final OmegaRunDefinition algorithmSpec, final Date timeStamps,
	        final String name,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments,
	        final Map<OmegaSegment, Double[]> peakSignalsMap,
	        final Map<OmegaSegment, Double[]> meanSignalsMap,
	        final Map<OmegaSegment, Double[]> centroidSignalsMap) {
		super(owner, algorithmSpec,
		        AnalysisRunType.OmegaTrackingMeasuresIntensityRun, timeStamps,
		        name, segments, TrackingMeasuresType.Intensity);
		this.peakSignalsMap = peakSignalsMap;
		this.meanSignalsMap = meanSignalsMap;
		this.centroidSignalsMap = centroidSignalsMap;
	}
	
	public Map<OmegaSegment, Double[]> getPeakSignalsResults() {
		return this.peakSignalsMap;
	}
	
	public Map<OmegaSegment, Double[]> getMeanSignalsResults() {
		return this.meanSignalsMap;
	}

	public Map<OmegaSegment, Double[]> getCentroidSignalsResults() {
		return this.centroidSignalsMap;
	}
}
