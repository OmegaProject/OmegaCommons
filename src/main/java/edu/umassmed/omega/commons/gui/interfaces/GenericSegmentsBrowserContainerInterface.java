package main.java.edu.umassmed.omega.commons.gui.interfaces;

import java.util.List;
import java.util.Map;

import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public interface GenericSegmentsBrowserContainerInterface {

	public void sendEventTrajectories(
			List<OmegaTrajectory> selectedTrajectories, boolean selected);

	public void sendEventSegments(
	        Map<OmegaTrajectory, List<OmegaSegment>> selectedSegments,
	        boolean selected);

	public void handleTrajectoryNameChanged();

	public void updateStatus(String message);
}
