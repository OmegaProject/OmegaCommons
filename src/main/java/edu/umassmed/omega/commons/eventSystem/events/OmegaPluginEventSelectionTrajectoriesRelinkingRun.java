package edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventSelectionTrajectoriesRelinkingRun extends
		OmegaPluginEventSelectionAnalysisRun {
	
	private final boolean isCurrent;
	private final List<OmegaTrajectory> trajectories;
	
	public OmegaPluginEventSelectionTrajectoriesRelinkingRun(
			final OmegaAnalysisRun analysisRun,
			final List<OmegaTrajectory> trajectories, final boolean isCurrent) {
		this(null, analysisRun, trajectories, isCurrent);
	}
	
	public OmegaPluginEventSelectionTrajectoriesRelinkingRun(
			final OmegaPlugin source, final OmegaAnalysisRun analysisRun,
			final List<OmegaTrajectory> trajectories, final boolean isCurrent) {
		super(source, analysisRun);
		this.trajectories = trajectories;
		this.isCurrent = isCurrent;
	}
	
	public List<OmegaTrajectory> getTrajectories() {
		return this.trajectories;
	}

	public boolean isCurrent() {
		return this.isCurrent;
	}
}
