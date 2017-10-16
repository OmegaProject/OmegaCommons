package edu.umassmed.omega.commons.utilities;

import java.util.List;

import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class OmegaTrajectoryUtilities {
	
	public static int computeSegmentLength(final OmegaTrajectory track,
			final OmegaSegment segment) {
		final List<OmegaROI> rois = track.getROIs();
		final OmegaROI startROI = segment.getStartingROI();
		final OmegaROI endROI = segment.getEndingROI();
		
		final int start = rois.indexOf(startROI);
		final int end = rois.indexOf(endROI);
		
		return (end - start) + 1;
	}
}
