package edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;

import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.pluginArchetypes.OmegaPluginArchetype;

public class OmegaPluginEventTrajectories extends OmegaPluginEvent {

	private final List<OmegaTrajectory> trajectories;

	private final boolean selection;

	public OmegaPluginEventTrajectories(final List<OmegaTrajectory> trajs,
	        final boolean selection) {
		this(null, trajs, selection);
	}

	public OmegaPluginEventTrajectories(final OmegaPluginArchetype source,
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
