package edu.umassmed.omega.commons.gui.interfaces;

import java.util.List;

import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public interface GenericTrajectoriesBrowserContainerInterface {

	public void sendEventTrajectories(
	        List<OmegaTrajectory> selectedTrajectories, boolean selected);

	public void handleTrajectoryNameChanged();

	public void updateStatus(String message);
}
