package edu.umassmed.omega.commons.exceptions;

import edu.umassmed.omega.commons.pluginArchetypes.OmegaPluginArchetype;

public class OmegaCoreExceptionPlugin extends OmegaCoreException {
	private static final long serialVersionUID = 4298107617349997503L;

	private final OmegaPluginArchetype plugin;

	public OmegaCoreExceptionPlugin(final OmegaPluginArchetype plugin,
	        final String message) {
		super(plugin.getName() + " - " + message);
		this.plugin = plugin;
	}

	public OmegaPluginArchetype getSource() {
		return this.plugin;
	}
}
