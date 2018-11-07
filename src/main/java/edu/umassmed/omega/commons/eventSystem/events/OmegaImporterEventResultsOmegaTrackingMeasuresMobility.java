package edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainerInterface;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.utilities.OmegaIOUtility;

public class OmegaImporterEventResultsOmegaTrackingMeasuresMobility extends
		OmegaImporterEventResultsOmegaTrackingMeasures {

	// Mobility
	private final Map<OmegaSegment, List<Double>> distancesMap;
	private final Map<OmegaSegment, List<Double>> distancesFromOriginMap;
	private final Map<OmegaSegment, List<Double>> displacementsFromOriginMap;
	private final Map<OmegaSegment, Double> maxDisplacementesFromOriginMap;
	private final Map<OmegaSegment, List<Double>> timeTraveledMap;
	private final Map<OmegaSegment, List<Double>> confinementRatioMap;
	private final Map<OmegaSegment, List<Double[]>> anglesAndDirectionalChangesMap;

	public OmegaImporterEventResultsOmegaTrackingMeasuresMobility(
			final OmegaIOUtility source,
			final OmegaAnalysisRunContainerInterface container,
			final Map<Integer, String> parents,
			final Map<String, String> analysisData,
			final Map<String, String> paramData,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, List<Double>> distancesMap,
			final Map<OmegaSegment, List<Double>> distancesFromOriginMap,
			final Map<OmegaSegment, List<Double>> displacementsFromOriginMap,
			final Map<OmegaSegment, Double> maxDisplacementesFromOriginMap,
			final Map<OmegaSegment, List<Double>> timeTraveledMap,
			final Map<OmegaSegment, List<Double>> confinementRatioMap,
			final Map<OmegaSegment, List<Double[]>> anglesAndDirectionalChangesMap,
			final boolean completeChainAfterImport) {
		super(source, container, parents, analysisData, paramData, segments,
				completeChainAfterImport);
		this.distancesMap = distancesMap;
		this.distancesFromOriginMap = distancesFromOriginMap;
		this.displacementsFromOriginMap = displacementsFromOriginMap;
		this.maxDisplacementesFromOriginMap = maxDisplacementesFromOriginMap;
		this.timeTraveledMap = timeTraveledMap;
		this.confinementRatioMap = confinementRatioMap;
		this.anglesAndDirectionalChangesMap = anglesAndDirectionalChangesMap;
	}
	
	public Map<OmegaSegment, List<Double>> getResultingDistances() {
		return this.distancesMap;
	}

	public Map<OmegaSegment, List<Double>> getResultingDistancesFromOrigin() {
		return this.distancesFromOriginMap;
	}

	public Map<OmegaSegment, List<Double>> getResultingDisplacementsFromOrigin() {
		return this.displacementsFromOriginMap;
	}

	public Map<OmegaSegment, Double> getResultingMaxDisplacementsFromOrigin() {
		return this.maxDisplacementesFromOriginMap;
	}

	public Map<OmegaSegment, List<Double>> getResultingTimeTraveled() {
		return this.timeTraveledMap;
	}

	public Map<OmegaSegment, List<Double>> getResultingConfinementRatio() {
		return this.confinementRatioMap;
	}

	public Map<OmegaSegment, List<Double[]>> getResultingAnglesAndDirectionalChanges() {
		return this.anglesAndDirectionalChangesMap;
	}
}
