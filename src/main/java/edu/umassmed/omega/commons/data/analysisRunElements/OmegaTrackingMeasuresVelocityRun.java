package edu.umassmed.omega.commons.data.analysisRunElements;

import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.coreElements.OmegaExperimenter;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class OmegaTrackingMeasuresVelocityRun extends OmegaTrackingMeasuresRun {

	// Velocity
	private final Map<OmegaSegment, List<Double>> localSpeedMap;
	private final Map<OmegaSegment, List<Double>> localVelocityMap;
	private final Map<OmegaSegment, Double> averageCurvilinearSpeedMap;
	private final Map<OmegaSegment, Double> averageStraightLineVelocityMap;
	private final Map<OmegaSegment, Double> forwardProgressionLinearityMap;

	public OmegaTrackingMeasuresVelocityRun(final OmegaExperimenter owner,
	        final OmegaRunDefinition algorithmSpec,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments,
	        final Map<OmegaSegment, List<Double>> localSpeedMap,
	        final Map<OmegaSegment, List<Double>> localVelocityMap,
	        final Map<OmegaSegment, Double> averageCurvilinearSpeedMap,
	        final Map<OmegaSegment, Double> averageStraightLineVelocityMap,
	        final Map<OmegaSegment, Double> forwardProgressionLinearityMap) {
		super(owner, algorithmSpec,
		        AnalysisRunType.OmegaTrackingMeasuresVelocityRun, segments,
				TrackingMeasuresType.Velocity);
		this.localSpeedMap = localSpeedMap;
		this.localVelocityMap = localVelocityMap;
		this.averageCurvilinearSpeedMap = averageCurvilinearSpeedMap;
		this.averageStraightLineVelocityMap = averageStraightLineVelocityMap;
		this.forwardProgressionLinearityMap = forwardProgressionLinearityMap;
	}

	public OmegaTrackingMeasuresVelocityRun(final OmegaExperimenter owner,
	        final OmegaRunDefinition algorithmSpec, final String name,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments,
	        final Map<OmegaSegment, List<Double>> localSpeedMap,
	        final Map<OmegaSegment, List<Double>> localVelocityMap,
	        final Map<OmegaSegment, Double> averageCurvilinearSpeedMap,
	        final Map<OmegaSegment, Double> averageStraightLineVelocityMapMap,
	        final Map<OmegaSegment, Double> forwardProgressionLinearityMap) {
		super(owner, algorithmSpec,
		        AnalysisRunType.OmegaTrackingMeasuresVelocityRun, name,
		        segments, TrackingMeasuresType.Velocity);
		this.localSpeedMap = localSpeedMap;
		this.localVelocityMap = localVelocityMap;
		this.averageCurvilinearSpeedMap = averageCurvilinearSpeedMap;
		this.averageStraightLineVelocityMap = averageStraightLineVelocityMapMap;
		this.forwardProgressionLinearityMap = forwardProgressionLinearityMap;
	}

	public OmegaTrackingMeasuresVelocityRun(final OmegaExperimenter owner,
	        final OmegaRunDefinition algorithmSpec, final Date timeStamps,
			final String name,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments,
	        final Map<OmegaSegment, List<Double>> localSpeedMap,
	        final Map<OmegaSegment, List<Double>> localVelocityMap,
	        final Map<OmegaSegment, Double> averageCurvilinearSpeedMap,
	        final Map<OmegaSegment, Double> averageStraightLineVelocityMap,
	        final Map<OmegaSegment, Double> forwardProgressionLinearityMap) {
		super(owner, algorithmSpec,
		        AnalysisRunType.OmegaTrackingMeasuresVelocityRun, timeStamps,
		        name, segments, TrackingMeasuresType.Velocity);
		this.localSpeedMap = localSpeedMap;
		this.localVelocityMap = localVelocityMap;
		this.averageCurvilinearSpeedMap = averageCurvilinearSpeedMap;
		this.averageStraightLineVelocityMap = averageStraightLineVelocityMap;
		this.forwardProgressionLinearityMap = forwardProgressionLinearityMap;
	}

	public Map<OmegaSegment, List<Double>> getLocalSpeedResults() {
		return this.localSpeedMap;
	}

	public Map<OmegaSegment, List<Double>> getLocalVelocityResults() {
		return this.localVelocityMap;
	}

	public Map<OmegaSegment, Double> getAverageCurvilinearSpeedMapResults() {
		return this.averageCurvilinearSpeedMap;
	}

	public Map<OmegaSegment, Double> getAverageStraightLineVelocityMapResults() {
		return this.averageStraightLineVelocityMap;
	}

	public Map<OmegaSegment, Double> getForwardProgressionLinearityMapResults() {
		return this.forwardProgressionLinearityMap;
	}
}
