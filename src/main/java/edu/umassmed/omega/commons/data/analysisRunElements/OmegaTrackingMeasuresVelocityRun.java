package main.java.edu.umassmed.omega.commons.data.analysisRunElements;

import java.util.Date;
import java.util.List;
import java.util.Map;

import main.java.edu.umassmed.omega.commons.data.coreElements.OmegaExperimenter;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class OmegaTrackingMeasuresVelocityRun extends OmegaTrackingMeasuresRun {

	// Velocity
	private final Map<OmegaSegment, List<Double>> localSpeedMap;
	private final Map<OmegaSegment, List<Double>> localVelocityMap;
	private final Map<OmegaSegment, Double> meanSpeedMap;
	private final Map<OmegaSegment, Double> meanVelocityMap;

	public OmegaTrackingMeasuresVelocityRun(final OmegaExperimenter owner,
			final OmegaAlgorithmSpecification algorithmSpec,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, List<Double>> localSpeedMap,
			final Map<OmegaSegment, List<Double>> localVelocityMap,
			final Map<OmegaSegment, Double> meanSpeedMap,
			final Map<OmegaSegment, Double> meanVelocityMap) {
		super(owner, algorithmSpec, segments);
		this.localSpeedMap = localSpeedMap;
		this.localVelocityMap = localVelocityMap;
		this.meanSpeedMap = meanSpeedMap;
		this.meanVelocityMap = meanVelocityMap;
	}

	public OmegaTrackingMeasuresVelocityRun(final OmegaExperimenter owner,
			final OmegaAlgorithmSpecification algorithmSpec, final String name,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, List<Double>> localSpeedMap,
			final Map<OmegaSegment, List<Double>> localVelocityMap,
			final Map<OmegaSegment, Double> meanSpeedMap,
			final Map<OmegaSegment, Double> meanVelocityMap) {
		super(owner, algorithmSpec, name, segments);
		this.localSpeedMap = localSpeedMap;
		this.localVelocityMap = localVelocityMap;
		this.meanSpeedMap = meanSpeedMap;
		this.meanVelocityMap = meanVelocityMap;
	}

	public OmegaTrackingMeasuresVelocityRun(final OmegaExperimenter owner,
			final OmegaAlgorithmSpecification algorithmSpec,
			final Date timeStamps, final String name,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, List<Double>> localSpeedMap,
			final Map<OmegaSegment, List<Double>> localVelocityMap,
			final Map<OmegaSegment, Double> meanSpeedMap,
			final Map<OmegaSegment, Double> meanVelocityMap) {
		super(owner, algorithmSpec, timeStamps, name, segments);
		this.localSpeedMap = localSpeedMap;
		this.localVelocityMap = localVelocityMap;
		this.meanSpeedMap = meanSpeedMap;
		this.meanVelocityMap = meanVelocityMap;
	}

	public Map<OmegaSegment, List<Double>> getLocalSpeedResults() {
		return this.localSpeedMap;
	}

	public Map<OmegaSegment, List<Double>> getLocalVelocityResults() {
		return this.localVelocityMap;
	}

	public Map<OmegaSegment, Double> getMeanSpeedResults() {
		return this.meanSpeedMap;
	}

	public Map<OmegaSegment, Double> getMeanVelocityResults() {
		return this.meanVelocityMap;
	}
}
