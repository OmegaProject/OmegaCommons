package edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegmentationTypes;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventSegments extends OmegaPluginEvent {

	private final Map<OmegaTrajectory, List<OmegaSegment>> segments;
	private final OmegaSegmentationTypes segmTypes;

	private final boolean selection;

	public OmegaPluginEventSegments(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final OmegaSegmentationTypes segmTypes, final boolean selection) {
		this(null, segments, segmTypes, selection);
	}

	public OmegaPluginEventSegments(final OmegaPlugin source,
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
