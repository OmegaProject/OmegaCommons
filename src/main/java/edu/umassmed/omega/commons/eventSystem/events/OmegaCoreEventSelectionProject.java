package edu.umassmed.omega.commons.eventSystem.events;

import edu.umassmed.omega.commons.data.coreElements.OmegaProject;

public class OmegaCoreEventSelectionProject extends OmegaCoreEvent {
	private final OmegaProject project;

	public OmegaCoreEventSelectionProject(final OmegaProject project) {
		this(-1, project);
	}

	public OmegaCoreEventSelectionProject(final int source,
			final OmegaProject project) {
		super(source);
		this.project = project;
	}

	public OmegaProject getProject() {
		return this.project;
	}
}
