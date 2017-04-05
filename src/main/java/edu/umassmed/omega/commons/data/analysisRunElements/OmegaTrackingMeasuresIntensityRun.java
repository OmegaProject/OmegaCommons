package edu.umassmed.omega.commons.data.analysisRunElements;

import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.coreElements.OmegaExperimenter;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class OmegaTrackingMeasuresIntensityRun extends OmegaTrackingMeasuresRun {
	
	// private Map<OmegaROI, List<Double>> particlesMeasures;
	// Intensity
	private final Map<OmegaSegment, Double[]> peakSignalsMap;
	private final Map<OmegaSegment, Double[]> centroidSignalsMap;
	
	private final Map<OmegaROI, Double> peakSignalsLocMap;
	private final Map<OmegaROI, Double> centroidSignalsLocMap;

	private final OmegaSNRRun snrRun;
	// SNR related START
	private final Map<OmegaSegment, Double[]> backgroundsMap;
	private final Map<OmegaSegment, Double[]> noisesMap;
	private final Map<OmegaSegment, Double[]> snrsMap;
	private final Map<OmegaSegment, Double[]> areasMap;
	private final Map<OmegaSegment, Double[]> meanSignalsMap;
	
	private final Map<OmegaROI, Double> backgroundsLocMap;
	private final Map<OmegaROI, Double> noisesLocMap;
	private final Map<OmegaROI, Double> snrsLocMap;
	private final Map<OmegaROI, Double> areasLocMap;
	private final Map<OmegaROI, Double> meanSignalsLocMap;
	
	// SNR related END
	
	public OmegaTrackingMeasuresIntensityRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec,
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
			final Map<OmegaROI, Double> snrsLocMap,
			final Map<OmegaROI, Double> areasLocMap,
			final Map<OmegaROI, Double> meanSignalsLocMap,
			final OmegaSNRRun snrRun) {
		super(owner, algorithmSpec,
				AnalysisRunType.OmegaTrackingMeasuresIntensityRun, segments,
				TrackingMeasuresType.Intensity);
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

		this.backgroundsLocMap = backgroundsLocMap;
		this.noisesLocMap = noisesLocMap;
		this.meanSignalsLocMap = meanSignalsLocMap;
		this.snrsLocMap = snrsLocMap;
		this.areasLocMap = areasLocMap;

		this.snrRun = snrRun;
		// SNR related END
	}
	
	public OmegaTrackingMeasuresIntensityRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec, final String name,
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
			final Map<OmegaROI, Double> snrsLocMap,
			final Map<OmegaROI, Double> areasLocMap,
			final Map<OmegaROI, Double> meanSignalsLocMap,
			final OmegaSNRRun snrRun) {
		super(owner, algorithmSpec,
				AnalysisRunType.OmegaTrackingMeasuresIntensityRun, name,
				segments, TrackingMeasuresType.Intensity);
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

		this.backgroundsLocMap = backgroundsLocMap;
		this.noisesLocMap = noisesLocMap;
		this.meanSignalsLocMap = meanSignalsLocMap;
		this.snrsLocMap = snrsLocMap;
		this.areasLocMap = areasLocMap;
		
		this.snrRun = snrRun;
		// SNR related END
	}
	
	public OmegaTrackingMeasuresIntensityRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec, final Date timeStamps,
			final String name,
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
			final Map<OmegaROI, Double> snrsLocMap,
			final Map<OmegaROI, Double> areasLocMap,
			final Map<OmegaROI, Double> meanSignalsLocMap,
			final OmegaSNRRun snrRun) {
		super(owner, algorithmSpec,
				AnalysisRunType.OmegaTrackingMeasuresIntensityRun, timeStamps,
				name, segments, TrackingMeasuresType.Intensity);
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

		this.backgroundsLocMap = backgroundsLocMap;
		this.noisesLocMap = noisesLocMap;
		this.meanSignalsLocMap = meanSignalsLocMap;
		this.snrsLocMap = snrsLocMap;
		this.areasLocMap = areasLocMap;
		
		this.snrRun = snrRun;
		// SNR related END
	}
	
	public Map<OmegaSegment, Double[]> getPeakSignalsResults() {
		return this.peakSignalsMap;
	}

	public Map<OmegaSegment, Double[]> getCentroidSignalsResults() {
		return this.centroidSignalsMap;
	}
	
	// SNR related START
	public Map<OmegaSegment, Double[]> getMeanSignalsResults() {
		return this.meanSignalsMap;
	}
	
	public Map<OmegaSegment, Double[]> getAreasResults() {
		return this.areasMap;
	}

	public Map<OmegaSegment, Double[]> getBackgroundsResults() {
		return this.backgroundsMap;
	}
	
	public Map<OmegaSegment, Double[]> getNoisesResults() {
		return this.noisesMap;
	}
	
	public Map<OmegaSegment, Double[]> getSNRsResults() {
		return this.snrsMap;
	}

	public Map<OmegaROI, Double> getPeakSignalsLocalResults() {
		return this.peakSignalsLocMap;
	}

	public Map<OmegaROI, Double> getCentroidSignalsLocalResults() {
		return this.centroidSignalsLocMap;
	}
	
	// SNR related START
	public Map<OmegaROI, Double> getMeanSignalsLocalResults() {
		return this.meanSignalsLocMap;
	}
	
	public Map<OmegaROI, Double> getAreasLocalResults() {
		return this.areasLocMap;
	}

	public Map<OmegaROI, Double> getBackgroundsLocalResults() {
		return this.backgroundsLocMap;
	}
	
	public Map<OmegaROI, Double> getNoisesLocalResults() {
		return this.noisesLocMap;
	}
	
	public Map<OmegaROI, Double> getSNRsLocalResults() {
		return this.snrsLocMap;
	}
	
	public OmegaSNRRun getSNRRun() {
		return this.snrRun;
	}
	// SNR related END
}
