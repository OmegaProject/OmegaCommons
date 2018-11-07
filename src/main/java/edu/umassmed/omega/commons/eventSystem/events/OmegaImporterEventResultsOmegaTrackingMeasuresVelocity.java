package edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainerInterface;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.utilities.OmegaIOUtility;

public class OmegaImporterEventResultsOmegaTrackingMeasuresVelocity extends
		OmegaImporterEventResultsOmegaTrackingMeasures {

	// Velocity
	private final Map<OmegaSegment, List<Double>> localSpeedMap;
	private final Map<OmegaSegment, List<Double>> localSpeedFromOriginMap;
	private final Map<OmegaSegment, List<Double>> localVelocityFromOriginMap;
	private final Map<OmegaSegment, Double> averageCurvilinearSpeedMap;
	private final Map<OmegaSegment, Double> averageStraightLineVelocityMap;
	private final Map<OmegaSegment, Double> forwardProgressionLinearityMap;

	public OmegaImporterEventResultsOmegaTrackingMeasuresVelocity(
			final OmegaIOUtility source,
			final OmegaAnalysisRunContainerInterface container,
			final Map<Integer, String> parents,
			final Map<String, String> analysisData,
			final Map<String, String> paramData,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, List<Double>> localSpeedMap,
			final Map<OmegaSegment, List<Double>> localSpeedFromOriginMap,
			final Map<OmegaSegment, List<Double>> localVelocityFromOriginMap,
			final Map<OmegaSegment, Double> averageCurvilinearSpeedMap,
			final Map<OmegaSegment, Double> averageStraightLineVelocityMap,
			final Map<OmegaSegment, Double> forwardProgressionLinearityMap,
			final boolean completeChainAfterImport) {
		super(source, container, parents, analysisData, paramData, segments,
				completeChainAfterImport);
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
