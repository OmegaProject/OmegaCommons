package main.java.edu.umassmed.omega.commons.libraries;

import java.util.List;

import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class OmegaVelocityLibrary {

	public static Double computeLocalSpeed(final OmegaTrajectory track,
			final int endT) {
		final int startT = track.getROIs().get(0).getFrameIndex();
		final Double dist = OmegaMobilityLibrary.computeTotalDistanceTraveled(
				track, endT);
		if (dist == null)
			return null;
		return OmegaVelocityLibrary.computeMovementOverTime(dist, startT, endT);
	}

	public static Double computeLocalSpeed(final List<OmegaROI> rois,
			final int endT) {
		final int startT = rois.get(0).getFrameIndex();
		final Double dist = OmegaMobilityLibrary.computeTotalDistanceTraveled(
		        rois, endT);
		if (dist == null)
			return null;
		return OmegaVelocityLibrary.computeMovementOverTime(dist, startT, endT);
	}

	public static Double computeLocalVelocity(final OmegaTrajectory track,
			final int endT) {
		final int startT = track.getROIs().get(0).getFrameIndex();
		final Double displ = OmegaMobilityLibrary.computeTotalNetDisplacement(
				track, endT);
		if (displ == null)
			return null;
		return OmegaVelocityLibrary
				.computeMovementOverTime(displ, startT, endT);
	}

	public static Double computeLocalVelocity(final List<OmegaROI> rois,
			final int endT) {
		final int startT = rois.get(0).getFrameIndex();
		final Double displ = OmegaMobilityLibrary.computeTotalNetDisplacement(
		        rois, endT);
		if (displ == null)
			return null;
		return OmegaVelocityLibrary
				.computeMovementOverTime(displ, startT, endT);
	}

	public static Double computeMeanSpeed(final OmegaTrajectory track) {
		final List<OmegaROI> rois = track.getROIs();
		final int startT = rois.get(0).getFrameIndex();
		final int endT = rois.get(rois.size() - 1).getFrameIndex();
		final Double dist = OmegaMobilityLibrary.computeTotalDistanceTraveled(
				track, endT);
		if (dist == null)
			return null;
		return OmegaVelocityLibrary.computeMovementOverTime(dist, startT, endT);
	}

	public static Double computeMeanSpeed(final List<OmegaROI> rois) {
		final int startT = rois.get(0).getFrameIndex();
		final int endT = rois.get(rois.size() - 1).getFrameIndex();
		final Double dist = OmegaMobilityLibrary.computeTotalDistanceTraveled(
				rois, endT);
		if (dist == null)
			return null;
		return OmegaVelocityLibrary.computeMovementOverTime(dist, startT, endT);
	}

	public static Double computeMeanVelocity(final OmegaTrajectory track) {
		final List<OmegaROI> rois = track.getROIs();
		final int startT = rois.get(0).getFrameIndex();
		final int endT = rois.get(rois.size() - 1).getFrameIndex();
		final Double displ = OmegaMobilityLibrary.computeTotalNetDisplacement(
				track, endT);
		if (displ == null)
			return null;
		return OmegaVelocityLibrary
				.computeMovementOverTime(displ, startT, endT);
	}

	public static Double computeMeanVelocity(final List<OmegaROI> rois) {
		final int startT = rois.get(0).getFrameIndex();
		final int endT = rois.get(rois.size() - 1).getFrameIndex();
		final Double displ = OmegaMobilityLibrary.computeTotalNetDisplacement(
		        rois, endT);
		if (displ == null)
			return null;
		return OmegaVelocityLibrary
				.computeMovementOverTime(displ, startT, endT);
	}

	public static Double computeMovementOverTime(final Double mov,
			final int startT, final int endT) {
		return mov / endT;
	}
}
