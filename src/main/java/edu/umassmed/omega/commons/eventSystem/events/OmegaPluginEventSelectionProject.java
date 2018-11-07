package edu.umassmed.omega.commons.eventSystem.events;

import edu.umassmed.omega.commons.data.coreElements.OmegaProject;
import edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventSelectionProject extends OmegaPluginEvent {

	private final OmegaProject project;

	public OmegaPluginEventSelectionProject(final OmegaProject project) {
		this(null, project);
	}

	public OmegaPluginEventSelectionProject(final OmegaPlugin source,
			final OmegaProject project) {
		super(source);
		this.project = project;
	}

	public OmegaProject getProject() {
		return this.project;
	}
}
