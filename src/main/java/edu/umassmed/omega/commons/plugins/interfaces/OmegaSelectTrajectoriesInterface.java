package main.java.edu.umassmed.omega.commons.plugins.interfaces;

import java.util.List;

import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public interface OmegaSelectTrajectoriesInterface {
	public void updateTrajectories(List<OmegaTrajectory> tracks,
			boolean isSelection);
}
