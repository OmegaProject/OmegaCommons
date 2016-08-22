package edu.umassmed.omega.commons.plugins.interfaces;

import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegmentationTypes;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public interface OmegaSelectSegmentsInterface extends
OmegaSelectTrajectoriesInterface {
	public void updateSegments(
	        Map<OmegaTrajectory, List<OmegaSegment>> segments,
	        OmegaSegmentationTypes segmTypes, boolean isSelection);
}
