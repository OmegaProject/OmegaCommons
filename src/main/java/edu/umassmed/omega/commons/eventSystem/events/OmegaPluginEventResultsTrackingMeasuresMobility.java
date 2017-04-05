package edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParameter;
import edu.umassmed.omega.commons.data.coreElements.OmegaElement;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventResultsTrackingMeasuresMobility extends
OmegaPluginEventResultsTrackingMeasures {
	
	// Mobility
	private final Map<OmegaSegment, List<Double>> distancesMap;
	private final Map<OmegaSegment, List<Double>> distancesFromOriginMap;
	private final Map<OmegaSegment, List<Double>> displacementsFromOriginMap;
	private final Map<OmegaSegment, Double> maxDisplacementesFromOriginMap;
	private final Map<OmegaSegment, List<Double>> timeTraveledMap;
	private final Map<OmegaSegment, List<Double>> confinementRatioMap;
	private final Map<OmegaSegment, List<Double[]>> anglesAndDirectionalChangesMap;
	
	public OmegaPluginEventResultsTrackingMeasuresMobility(
			final OmegaPlugin source,
			final List<OmegaElement> selections,
			final OmegaElement element,
			final List<OmegaParameter> params,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, List<Double>> distancesMap,
			final Map<OmegaSegment, List<Double>> distancesFromOriginMap,
			final Map<OmegaSegment, List<Double>> displacementsFromOriginMap,
			final Map<OmegaSegment, Double> maxDisplacementesFromOriginMap,
			final Map<OmegaSegment, List<Double>> timeTraveledMap,
			final Map<OmegaSegment, List<Double>> confinementRatioMap,
			final Map<OmegaSegment, List<Double[]>> anglesAndDirectionalChangesMap) {
		super(source, selections, element, params, segments);
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
