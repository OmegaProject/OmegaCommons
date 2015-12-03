package main.java.edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;

import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import main.java.edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventTrajectories extends OmegaPluginEvent {

	private final List<OmegaTrajectory> trajectories;

	private final boolean selection;

	public OmegaPluginEventTrajectories(final List<OmegaTrajectory> trajs,
	        final boolean selection) {
		this(null, trajs, selection);
	}

	public OmegaPluginEventTrajectories(final OmegaPlugin source,
	        final List<OmegaTrajectory> trajs, final boolean selection) {
		super(source);
		this.trajectories = trajs;
		this.selection = selection;
	}

	public List<OmegaTrajectory> getTrajectories() {
		return this.trajectories;
	}

	public boolean isSelectionEvent() {
		return this.selection;
	}
}
