package edu.umassmed.omega.commons.pluginArchetypes.interfaces;

import java.util.List;

import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public interface OmegaSelectTrajectoriesInterface {
	public void updateTrajectories(List<OmegaTrajectory> tracks,
			boolean isSelection);
	
	public void clearTrajectoriesSelection();
}
