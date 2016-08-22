package edu.umassmed.omega.commons.libraries;

import java.util.ArrayList;
import java.util.List;

import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.libraries.OmegaMobilityLibrary;

public class OmegaMobilityLibraryTest {
	public static void main(final String[] args) {
		final OmegaROI roi1 = new OmegaROI(0, 10.5, 10.5);
		final OmegaROI roi2 = new OmegaROI(1, 12.5, 10.5);
		final OmegaROI roi3 = new OmegaROI(2, 8.5, 10.5);
		final List<OmegaROI> rois = new ArrayList<OmegaROI>();
		rois.add(roi1);
		rois.add(roi2);
		rois.add(roi3);

		final double netDisp1 = OmegaMobilityLibrary
				.computeTotalNetDisplacement(rois, 1);
		final double totDist1 = OmegaMobilityLibrary
				.computeTotalDistanceTraveled(rois, 1);

		System.out.println("Displ: " + netDisp1 + " VS Dist:" + totDist1);

		final double netDisp2 = OmegaMobilityLibrary
				.computeTotalNetDisplacement(rois, 2);
		final double totDist2 = OmegaMobilityLibrary
				.computeTotalDistanceTraveled(rois, 2);

		System.out.println("Displ: " + netDisp2 + " VS Dist:" + totDist2);
	}
}
