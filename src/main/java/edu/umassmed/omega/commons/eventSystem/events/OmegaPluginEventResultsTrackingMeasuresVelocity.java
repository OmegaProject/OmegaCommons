package main.java.edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaParameter;
import main.java.edu.umassmed.omega.commons.data.coreElements.OmegaElement;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import main.java.edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventResultsTrackingMeasuresVelocity extends
OmegaPluginEventResultsTrackingMeasures {

	// Velocity
	private final Map<OmegaSegment, List<Double>> localSpeedMap;
	private final Map<OmegaSegment, List<Double>> localVelocityMap;
	private final Map<OmegaSegment, Double> meanSpeedMap;
	private final Map<OmegaSegment, Double> meanVelocityMap;

	public OmegaPluginEventResultsTrackingMeasuresVelocity(
	        final OmegaPlugin source, final OmegaElement element,
	        final List<OmegaParameter> params,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments,
	        final Map<OmegaSegment, List<Double>> localSpeedMap,
	        final Map<OmegaSegment, List<Double>> localVelocityMap,
	        final Map<OmegaSegment, Double> meanSpeedMap,
	        final Map<OmegaSegment, Double> meanVelocityMap) {
		super(source, element, params, segments);
		this.localSpeedMap = localSpeedMap;
		this.localVelocityMap = localVelocityMap;
		this.meanSpeedMap = meanSpeedMap;
		this.meanVelocityMap = meanVelocityMap;
	}

	public Map<OmegaSegment, List<Double>> getResultingLocalSpeed() {
		return this.localSpeedMap;
	}

	public Map<OmegaSegment, List<Double>> getResultingLocalVelocity() {
		return this.localVelocityMap;
	}

	public Map<OmegaSegment, Double> getResultingMeanSpeed() {
		return this.meanSpeedMap;
	}

	public Map<OmegaSegment, Double> getResultingMeanVelocity() {
		return this.meanVelocityMap;
	}
}
