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
	private final Map<OmegaSegment, Double[]> centroidSignalsMap;
	
	private final OmegaSNRRun snrRun;
	// SNR related START
	private final Map<OmegaSegment, Double[]> noisesMap;
	private final Map<OmegaSegment, Double[]> snrsMap;
	private final Map<OmegaSegment, Double[]> areasMap;
	private final Map<OmegaSegment, Double[]> meanSignalsMap;

	// SNR related END

	public OmegaTrackingMeasuresIntensityRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, Double[]> peakSignalsMap,
			final Map<OmegaSegment, Double[]> centroidSignalsMap,
			final Map<OmegaSegment, Double[]> noisesMap,
			final Map<OmegaSegment, Double[]> snrsMap,
			final Map<OmegaSegment, Double[]> areasMap,
			final Map<OmegaSegment, Double[]> meanSignalsMap,
			final OmegaSNRRun snrRun) {
		super(owner, algorithmSpec,
				AnalysisRunType.OmegaTrackingMeasuresIntensityRun, segments,
				TrackingMeasuresType.Intensity);
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

	public OmegaTrackingMeasuresIntensityRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec, final String name,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, Double[]> peakSignalsMap,
			final Map<OmegaSegment, Double[]> centroidSignalsMap,
			final Map<OmegaSegment, Double[]> noisesMap,
			final Map<OmegaSegment, Double[]> snrsMap,
			final Map<OmegaSegment, Double[]> areasMap,
			final Map<OmegaSegment, Double[]> meanSignalsMap,
	        final OmegaSNRRun snrRun) {
		super(owner, algorithmSpec,
				AnalysisRunType.OmegaTrackingMeasuresIntensityRun, name,
				segments, TrackingMeasuresType.Intensity);
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

	public OmegaTrackingMeasuresIntensityRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec, final Date timeStamps,
			final String name,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, Double[]> peakSignalsMap,
			final Map<OmegaSegment, Double[]> centroidSignalsMap,
			final Map<OmegaSegment, Double[]> noisesMap,
			final Map<OmegaSegment, Double[]> snrsMap,
			final Map<OmegaSegment, Double[]> areasMap,
			final Map<OmegaSegment, Double[]> meanSignalsMap,
	        final OmegaSNRRun snrRun) {
		super(owner, algorithmSpec,
				AnalysisRunType.OmegaTrackingMeasuresIntensityRun, timeStamps,
				name, segments, TrackingMeasuresType.Intensity);
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
