package edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegmentationTypes;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class OmegaCoreEventSegments extends OmegaCoreEvent {
	private final Map<OmegaTrajectory, List<OmegaSegment>> segments;
	private final OmegaSegmentationTypes segmTypes;

	private final boolean selection;

	public OmegaCoreEventSegments(
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments,
	        final OmegaSegmentationTypes segmTypes, final boolean selection) {
		this(-1, segments, segmTypes, selection);
	}

	public OmegaCoreEventSegments(final int source,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments,
	        final OmegaSegmentationTypes segmTypes, final boolean selection) {
		super(source);
		this.segments = segments;
		this.segmTypes = segmTypes;
		this.selection = selection;
	}

	public Map<OmegaTrajectory, List<OmegaSegment>> getSegments() {
		return this.segments;
	}

	public OmegaSegmentationTypes getSegmentationTypes() {
		return this.segmTypes;
	}

	public boolean isSelectionEvent() {
		return this.selection;
	}
}
