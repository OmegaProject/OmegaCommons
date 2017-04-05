package edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParameter;
import edu.umassmed.omega.commons.data.coreElements.OmegaElement;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventResultsTrackingMeasuresVelocity extends
		OmegaPluginEventResultsTrackingMeasures {

	// Velocity
	private final Map<OmegaSegment, List<Double>> localSpeedMap;
	private final Map<OmegaSegment, List<Double>> localSpeedFromOriginMap;
	private final Map<OmegaSegment, List<Double>> localVelocityFromOriginMap;
	private final Map<OmegaSegment, Double> averageCurvilinearSpeedMap;
	private final Map<OmegaSegment, Double> averageStraightLineVelocityMap;
	private final Map<OmegaSegment, Double> forwardProgressionLinearityMap;

	public OmegaPluginEventResultsTrackingMeasuresVelocity(
			final OmegaPlugin source, final List<OmegaElement> selections,
			final OmegaElement element, final List<OmegaParameter> params,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, List<Double>> localSpeedMap,
			final Map<OmegaSegment, List<Double>> localSpeedFromOriginMap,
			final Map<OmegaSegment, List<Double>> localVelocityFromOriginMap,
			final Map<OmegaSegment, Double> averageCurvilinearSpeedMap,
			final Map<OmegaSegment, Double> averageStraightLineVelocityMap,
			final Map<OmegaSegment, Double> forwardProgressionLinearityMap) {
		super(source, selections, element, params, segments);
		this.localSpeedMap = localSpeedMap;
		this.localSpeedFromOriginMap = localSpeedFromOriginMap;
		this.localVelocityFromOriginMap = localVelocityFromOriginMap;
		this.averageCurvilinearSpeedMap = averageCurvilinearSpeedMap;
		this.averageStraightLineVelocityMap = averageStraightLineVelocityMap;
		this.forwardProgressionLinearityMap = forwardProgressionLinearityMap;
	}

	public Map<OmegaSegment, List<Double>> getResultingLocalSpeed() {
		return this.localSpeedMap;
	}

	public Map<OmegaSegment, List<Double>> getResultingLocalSpeedFromOrigin() {
		return this.localSpeedFromOriginMap;
	}

	public Map<OmegaSegment, List<Double>> getResultingLocalVelocityFromOrigin() {
		return this.localVelocityFromOriginMap;
	}

	public Map<OmegaSegment, Double> getResultingAverageCurvilinearSpeed() {
		return this.averageCurvilinearSpeedMap;
	}

	public Map<OmegaSegment, Double> getResultingAverageStraightLineVelocity() {
		return this.averageStraightLineVelocityMap;
	}

	public Map<OmegaSegment, Double> getResultingForwardProgressionLinearity() {
		return this.forwardProgressionLinearityMap;
	}
}
