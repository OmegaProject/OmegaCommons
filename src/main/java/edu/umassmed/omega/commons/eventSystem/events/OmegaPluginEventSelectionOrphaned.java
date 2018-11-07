package edu.umassmed.omega.commons.eventSystem.events;

import edu.umassmed.omega.commons.data.analysisRunElements.OrphanedAnalysisContainer;
import edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventSelectionOrphaned extends OmegaPluginEvent {

	private final OrphanedAnalysisContainer container;

	public OmegaPluginEventSelectionOrphaned(
			final OrphanedAnalysisContainer container) {
		this(null, container);
	}

	public OmegaPluginEventSelectionOrphaned(final OmegaPlugin source,
			final OrphanedAnalysisContainer container) {
		super(source);
		this.container = container;
	}

	public OrphanedAnalysisContainer getContainer() {
		return this.container;
	}
}
