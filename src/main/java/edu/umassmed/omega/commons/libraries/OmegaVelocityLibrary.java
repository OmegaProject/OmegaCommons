package edu.umassmed.omega.commons.libraries;

import java.util.List;

import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class OmegaVelocityLibrary {
	
	public static Double computeLocalSpeed(final double physicalT,
			final OmegaTrajectory track, final int endT) {
		final int startT = track.getROIs().get(0).getFrameIndex();
		final Double dist = OmegaMobilityLibrary.computeTotalDistanceTraveled(
				track, endT);
		if (dist == null)
			return null;
		return OmegaVelocityLibrary.computeMovementOverTime(dist, physicalT,
				startT, endT);
	}

	public static Double computeLocalSpeed(final double physicalT,
			final List<OmegaROI> rois, final int t) {
		final Double dist = OmegaMobilityLibrary.computeDistanceTraveled(rois,
				t);
		if (dist == null)
			return null;
		return OmegaVelocityLibrary.computeMovementOverTime(dist, physicalT,
				t - 1, t);
	}
	
	public static Double computeLocalSpeedFromOrigin(final double physicalT,
			final List<OmegaROI> rois, final int endT) {
		final int startT = rois.get(0).getFrameIndex();
		final Double dist = OmegaMobilityLibrary.computeTotalDistanceTraveled(
				rois, endT);
		if (dist == null)
			return null;
		return OmegaVelocityLibrary.computeMovementOverTime(dist, physicalT,
				startT, endT);
	}
	
	public static Double computeLocalVelocityFromOrigin(final double physicalT,
			final OmegaTrajectory track, final int endT) {
		final int startT = track.getROIs().get(0).getFrameIndex();
		final Double displ = OmegaMobilityLibrary.computeTotalNetDisplacement(
				track, endT);
		if (displ == null)
			return null;
		return OmegaVelocityLibrary.computeMovementOverTime(displ, physicalT,
				startT, endT);
	}
	
	public static Double computeLocalVelocity(final double physicalT,
			final List<OmegaROI> rois, final int endT) {
		final int startT = rois.get(0).getFrameIndex();
		final Double displ = OmegaMobilityLibrary.computeTotalNetDisplacement(
				rois, endT);
		if (displ == null)
			return null;
		return OmegaVelocityLibrary.computeMovementOverTime(displ, physicalT,
				startT, endT);
	}
	
	public static Double computeMeanSpeed(final double physicalT,
			final OmegaTrajectory track) {
		final List<OmegaROI> rois = track.getROIs();
		final int startT = rois.get(0).getFrameIndex();
		final int endT = rois.get(rois.size() - 1).getFrameIndex();
		final Double dist = OmegaMobilityLibrary.computeTotalDistanceTraveled(
				track, endT);
		if (dist == null)
			return null;
		return OmegaVelocityLibrary.computeMovementOverTime(dist, physicalT,
				startT, endT);
	}
	
	public static Double computeAveragerCurvilinearSpeed(
			final double physicalT, final List<OmegaROI> rois) {
		final int startT = rois.get(0).getFrameIndex();
		final int endT = rois.get(rois.size() - 1).getFrameIndex();
		final Double dist = OmegaMobilityLibrary.computeTotalDistanceTraveled(
				rois, endT);
		if (dist == null)
			return null;
		return OmegaVelocityLibrary.computeMovementOverTime(dist, physicalT,
				startT, endT);
	}
	
	public static Double computeMeanVelocity(final double physicalT,
			final OmegaTrajectory track) {
		final List<OmegaROI> rois = track.getROIs();
		final int startT = rois.get(0).getFrameIndex();
		final int endT = rois.get(rois.size() - 1).getFrameIndex();
		final Double displ = OmegaMobilityLibrary.computeTotalNetDisplacement(
				track, endT);
		if (displ == null)
			return null;
		return OmegaVelocityLibrary.computeMovementOverTime(displ, physicalT,
				startT, endT);
	}
	
	public static Double computeAverageStraightLineVelocity(
			final double physicalT, final List<OmegaROI> rois) {
		final int startT = rois.get(0).getFrameIndex();
		final int endT = rois.get(rois.size() - 1).getFrameIndex();
		final Double displ = OmegaMobilityLibrary.computeTotalNetDisplacement(
				rois, endT);
		if (displ == null)
			return null;
		return OmegaVelocityLibrary.computeMovementOverTime(displ, physicalT,
				startT, endT);
	}
	
	public static Double computeMovementOverTime(final Double mov,
			final double physicalT, final int startT, final int endT) {
		final double t = endT - startT;
		final Double realT = t * physicalT;
		return mov / realT;
	}
}
