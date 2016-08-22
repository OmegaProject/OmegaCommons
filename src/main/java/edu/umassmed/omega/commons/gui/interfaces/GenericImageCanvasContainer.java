package edu.umassmed.omega.commons.gui.interfaces;

import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegmentationTypes;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public interface GenericImageCanvasContainer {

	void sendCoreEventTrajectories(List<OmegaTrajectory> trajectories,
	        boolean selection);

	void sendCoreEventSegments(
	        Map<OmegaTrajectory, List<OmegaSegment>> segments,
	        OmegaSegmentationTypes segmTypes, boolean selection);
}
