package edu.umassmed.omega.commons.libraries;

import java.util.ArrayList;
import java.util.List;

import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;

public class OmegaVelocityLibraryTest {
	public static void main(final String[] args) {
		final OmegaROI roi1 = new OmegaROI(0, 10.5, 10.5, 10.5, 10.5);
		final OmegaROI roi2 = new OmegaROI(1, 12.5, 10.5, 12.5, 10.5);
		final OmegaROI roi3 = new OmegaROI(2, 8.5, 10.5, 8.5, 10.5);
		final List<OmegaROI> rois = new ArrayList<OmegaROI>();
		rois.add(roi1);
		rois.add(roi2);
		rois.add(roi3);

		final double localSpeed1 = OmegaVelocityLibrary
				.computeLocalSpeedFromOrigin(1, rois, 1);
		final double localVelocity1 = OmegaVelocityLibrary
				.computeLocalVelocity(1, rois, 1);

		System.out.println("Speed: " + localSpeed1 + " VS Velocity:"
				+ localVelocity1);

		final double localSpeed2 = OmegaVelocityLibrary
				.computeLocalSpeedFromOrigin(1, rois, 2);
		final double localVelocity2 = OmegaVelocityLibrary
				.computeLocalVelocity(1, rois, 2);

		System.out.println("Speed: " + localSpeed2 + " VS Velocity:"
				+ localVelocity2);
	}
}
