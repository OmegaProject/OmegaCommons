package edu.umassmed.omega.commons.eventSystem.events;

import edu.umassmed.omega.commons.data.coreElements.OmegaDataset;
import edu.umassmed.omega.commons.pluginArchetypes.OmegaPluginArchetype;

public class OmegaPluginEventSelectionDataset extends OmegaPluginEvent {

	private final OmegaDataset dataset;

	public OmegaPluginEventSelectionDataset(final OmegaDataset dataset) {
		this(null, dataset);
	}

	public OmegaPluginEventSelectionDataset(final OmegaPluginArchetype source,
			final OmegaDataset dataset) {
		super(source);
		this.dataset = dataset;
	}

	public OmegaDataset getDataset() {
		return this.dataset;
	}
}
