package edu.umassmed.omega.commons.eventSystem.events;

import edu.umassmed.omega.commons.data.coreElements.OmegaProject;
import edu.umassmed.omega.commons.pluginArchetypes.OmegaPluginArchetype;

public class OmegaPluginEventSelectionProject extends OmegaPluginEvent {

	private final OmegaProject project;

	public OmegaPluginEventSelectionProject(final OmegaProject project) {
		this(null, project);
	}

	public OmegaPluginEventSelectionProject(final OmegaPluginArchetype source,
			final OmegaProject project) {
		super(source);
		this.project = project;
	}

	public OmegaProject getProject() {
		return this.project;
	}
}
