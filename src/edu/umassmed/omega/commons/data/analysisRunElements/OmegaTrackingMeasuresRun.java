package edu.umassmed.omega.commons.data.analysisRunElements;

import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.coreElements.OmegaExperimenter;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class OmegaTrackingMeasuresRun extends OmegaAnalysisRun {

	private final Map<OmegaTrajectory, List<OmegaSegment>> segments;

	public OmegaTrackingMeasuresRun(final OmegaExperimenter owner,
	        final OmegaAlgorithmSpecification algorithmSpec,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		super(owner, algorithmSpec);
		this.segments = segments;
	}

	public OmegaTrackingMeasuresRun(final OmegaExperimenter owner,
	        final OmegaAlgorithmSpecification algorithmSpec, final String name,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		super(owner, algorithmSpec, name);
		this.segments = segments;
	}

	public OmegaTrackingMeasuresRun(final OmegaExperimenter owner,
	        final OmegaAlgorithmSpecification algorithmSpec,
	        final Date timeStamps, final String name,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		super(owner, algorithmSpec, timeStamps, name);
		this.segments = segments;
	}

	public Map<OmegaTrajectory, List<OmegaSegment>> getSegments() {
		return this.segments;
	}
}
