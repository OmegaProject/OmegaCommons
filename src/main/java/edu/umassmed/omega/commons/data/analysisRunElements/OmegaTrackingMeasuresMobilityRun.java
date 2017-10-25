package edu.umassmed.omega.commons.data.analysisRunElements;

import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.coreElements.OmegaExperimenter;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class OmegaTrackingMeasuresMobilityRun extends OmegaTrackingMeasuresRun {

	private static String DISPLAY_NAME = "Mobility Tracking Measures Run";

	// Mobility
	private final Map<OmegaSegment, List<Double>> distancesMap;
	private final Map<OmegaSegment, List<Double>> distancesFromOriginMap;
	private final Map<OmegaSegment, List<Double>> displacementsFromOriginMap;
	private final Map<OmegaSegment, Double> maxDisplacementesFromOriginMap;
	private final Map<OmegaSegment, List<Double>> timeTraveledMap;
	private final Map<OmegaSegment, List<Double>> confinementRatioMap;
	private final Map<OmegaSegment, List<Double[]>> anglesAndDirectionalChangesMap;

	public OmegaTrackingMeasuresMobilityRun(
			final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, List<Double>> distancesMap,
			final Map<OmegaSegment, List<Double>> distancesFromOriginMap,
			final Map<OmegaSegment, List<Double>> displacementsFromOriginMap,
			final Map<OmegaSegment, Double> maxDisplacementesFromOriginMap,
			final Map<OmegaSegment, List<Double>> timeTraveledMap,
			final Map<OmegaSegment, List<Double>> confinementRatioMap,
			final Map<OmegaSegment, List<Double[]>> anglesAndDirectionalChangesMap) {
		super(owner, algorithmSpec,
				AnalysisRunType.OmegaTrackingMeasuresMobilityRun, segments,
				TrackingMeasuresType.Mobility);
		this.distancesMap = distancesMap;
		this.distancesFromOriginMap = distancesFromOriginMap;
		this.displacementsFromOriginMap = displacementsFromOriginMap;
		this.maxDisplacementesFromOriginMap = maxDisplacementesFromOriginMap;
		this.timeTraveledMap = timeTraveledMap;
		this.confinementRatioMap = confinementRatioMap;
		this.anglesAndDirectionalChangesMap = anglesAndDirectionalChangesMap;
	}

	public OmegaTrackingMeasuresMobilityRun(
			final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec,
			final String name,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, List<Double>> distancesMap,
			final Map<OmegaSegment, List<Double>> distancesFromOriginMap,
			final Map<OmegaSegment, List<Double>> displacementsFromOriginMap,
			final Map<OmegaSegment, Double> maxDisplacementesFromOriginMap,
			final Map<OmegaSegment, List<Double>> timeTraveledMap,
			final Map<OmegaSegment, List<Double>> confinementRatioMap,
			final Map<OmegaSegment, List<Double[]>> anglesAndDirectionalChangesMap) {
		super(owner, algorithmSpec,
				AnalysisRunType.OmegaTrackingMeasuresMobilityRun, name,
				segments, TrackingMeasuresType.Mobility);
		this.distancesMap = distancesMap;
		this.distancesFromOriginMap = distancesFromOriginMap;
		this.displacementsFromOriginMap = displacementsFromOriginMap;
		this.maxDisplacementesFromOriginMap = maxDisplacementesFromOriginMap;
		this.timeTraveledMap = timeTraveledMap;
		this.confinementRatioMap = confinementRatioMap;
		this.anglesAndDirectionalChangesMap = anglesAndDirectionalChangesMap;
	}

	public OmegaTrackingMeasuresMobilityRun(
			final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec,
			final Date timeStamps,
			final String name,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, List<Double>> distancesMap,
			final Map<OmegaSegment, List<Double>> distancesFromOriginMap,
			final Map<OmegaSegment, List<Double>> displacementsFromOriginMap,
			final Map<OmegaSegment, Double> maxDisplacementesFromOriginMap,
			final Map<OmegaSegment, List<Double>> timeTraveledMap,
			final Map<OmegaSegment, List<Double>> confinementRatioMap,
			final Map<OmegaSegment, List<Double[]>> anglesAndDirectionalChangesMap) {
		super(owner, algorithmSpec,
				AnalysisRunType.OmegaTrackingMeasuresMobilityRun, timeStamps,
				name, segments, TrackingMeasuresType.Mobility);
		this.distancesMap = distancesMap;
		this.distancesFromOriginMap = distancesFromOriginMap;
		this.displacementsFromOriginMap = displacementsFromOriginMap;
		this.maxDisplacementesFromOriginMap = maxDisplacementesFromOriginMap;
		this.timeTraveledMap = timeTraveledMap;
		this.confinementRatioMap = confinementRatioMap;
		this.anglesAndDirectionalChangesMap = anglesAndDirectionalChangesMap;
	}
	
	public Map<OmegaSegment, List<Double>> getDistancesResults() {
		return this.distancesMap;
	}

	public Map<OmegaSegment, List<Double>> getDistancesFromOriginResults() {
		return this.distancesFromOriginMap;
	}

	public Map<OmegaSegment, List<Double>> getDisplacementsFromOriginResults() {
		return this.displacementsFromOriginMap;
	}

	public Map<OmegaSegment, Double> getMaxDisplacementsFromOriginResults() {
		return this.maxDisplacementesFromOriginMap;
	}

	public Map<OmegaSegment, List<Double>> getTimeTraveledResults() {
		return this.timeTraveledMap;
	}

	public Map<OmegaSegment, List<Double>> getConfinementRatioResults() {
		return this.confinementRatioMap;
	}

	public Map<OmegaSegment, List<Double[]>> getAnglesAndDirectionalChangesResults() {
		return this.anglesAndDirectionalChangesMap;
	}
	
	public static String getStaticDisplayName() {
		return OmegaTrackingMeasuresMobilityRun.DISPLAY_NAME;
	}
	
	@Override
	public String getDynamicDisplayName() {
		return OmegaTrackingMeasuresMobilityRun.getStaticDisplayName();
	}
}
