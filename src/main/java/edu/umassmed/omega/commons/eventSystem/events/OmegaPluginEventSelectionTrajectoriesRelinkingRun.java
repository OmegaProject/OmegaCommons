package main.java.edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;

import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import main.java.edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventSelectionTrajectoriesRelinkingRun extends
        OmegaPluginEventSelectionAnalysisRun {

	private final List<OmegaTrajectory> trajectories;

	public OmegaPluginEventSelectionTrajectoriesRelinkingRun(
	        final OmegaAnalysisRun analysisRun,
	        final List<OmegaTrajectory> trajectories) {
		this(null, analysisRun, trajectories);
	}

	public OmegaPluginEventSelectionTrajectoriesRelinkingRun(
	        final OmegaPlugin source, final OmegaAnalysisRun analysisRun,
	        final List<OmegaTrajectory> trajectories) {
		super(source, analysisRun);
		this.trajectories = trajectories;
	}

	public List<OmegaTrajectory> getTrajectories() {
		return this.trajectories;
	}
}
