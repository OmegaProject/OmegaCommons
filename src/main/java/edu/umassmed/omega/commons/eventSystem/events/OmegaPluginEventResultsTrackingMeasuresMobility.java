package main.java.edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaParameter;
import main.java.edu.umassmed.omega.commons.data.coreElements.OmegaElement;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import main.java.edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventResultsTrackingMeasuresMobility extends
        OmegaPluginEventResultsTrackingMeasures {

	// Mobility
	private final Map<OmegaSegment, List<Double>> distancesMap;
	private final Map<OmegaSegment, List<Double>> displacementsMap;
	private final Map<OmegaSegment, Double> maxDisplacementesMap;
	private final Map<OmegaSegment, Integer> totalTimeTraveledMap;
	private final Map<OmegaSegment, List<Double>> confinementRatioMap;
	private final Map<OmegaSegment, List<Double[]>> anglesAndDirectionalChangesMap;

	public OmegaPluginEventResultsTrackingMeasuresMobility(
	        final OmegaPlugin source,
			final OmegaElement element,
	        final List<OmegaParameter> params,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
	        final Map<OmegaSegment, List<Double>> distancesMap,
	        final Map<OmegaSegment, List<Double>> displacementsMap,
	        final Map<OmegaSegment, Double> maxDisplacementesMap,
	        final Map<OmegaSegment, Integer> totalTimeTraveledMap,
	        final Map<OmegaSegment, List<Double>> confinementRatioMap,
	        final Map<OmegaSegment, List<Double[]>> anglesAndDirectionalChangesMap) {
		super(source, element, params, segments);
		this.distancesMap = distancesMap;
		this.displacementsMap = displacementsMap;
		this.maxDisplacementesMap = maxDisplacementesMap;
		this.totalTimeTraveledMap = totalTimeTraveledMap;
		this.confinementRatioMap = confinementRatioMap;
		this.anglesAndDirectionalChangesMap = anglesAndDirectionalChangesMap;
	}

	public Map<OmegaSegment, List<Double>> getResultingDistances() {
		return this.distancesMap;
	}

	public Map<OmegaSegment, List<Double>> getResultingDisplacements() {
		return this.displacementsMap;
	}

	public Map<OmegaSegment, Double> getResultingMaxDisplacements() {
		return this.maxDisplacementesMap;
	}

	public Map<OmegaSegment, Integer> getResultingTotalTimeTraveled() {
		return this.totalTimeTraveledMap;
	}

	public Map<OmegaSegment, List<Double>> getResultingConfinementRatio() {
		return this.confinementRatioMap;
	}

	public Map<OmegaSegment, List<Double[]>> getResultingAnglesAndDirectionalChanges() {
		return this.anglesAndDirectionalChangesMap;
	}
}
