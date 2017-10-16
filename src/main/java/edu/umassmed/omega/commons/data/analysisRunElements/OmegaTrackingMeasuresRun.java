package edu.umassmed.omega.commons.data.analysisRunElements;

import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.coreElements.OmegaExperimenter;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class OmegaTrackingMeasuresRun extends OmegaAnalysisRun {
	
	private static String DISPLAY_NAME = "Tracking Measures Run";

	private final Map<OmegaTrajectory, List<OmegaSegment>> segments;

	private final TrackingMeasuresType measuresType;

	public OmegaTrackingMeasuresRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec, final AnalysisRunType type,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final TrackingMeasuresType measuresType) {
		super(owner, algorithmSpec, type);
		this.segments = segments;
		this.measuresType = measuresType;
	}

	public OmegaTrackingMeasuresRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec, final AnalysisRunType type,
			final String name,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final TrackingMeasuresType measuresType) {
		super(owner, algorithmSpec, type, name);
		this.segments = segments;
		this.measuresType = measuresType;
	}

	public OmegaTrackingMeasuresRun(final OmegaExperimenter owner,
			final OmegaRunDefinition algorithmSpec, final AnalysisRunType type,
			final Date timeStamps, final String name,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final TrackingMeasuresType measuresType) {
		super(owner, algorithmSpec, type, timeStamps, name);
		this.segments = segments;
		this.measuresType = measuresType;
	}

	public Map<OmegaTrajectory, List<OmegaSegment>> getSegments() {
		return this.segments;
	}

	public TrackingMeasuresType getMeasureType() {
		return this.measuresType;
	}
	
	public static String getStaticDisplayName() {
		return OmegaTrackingMeasuresRun.DISPLAY_NAME;
	}

	@Override
	public String getDynamicDisplayName() {
		return OmegaTrackingMeasuresRun.getStaticDisplayName();
	}
}
