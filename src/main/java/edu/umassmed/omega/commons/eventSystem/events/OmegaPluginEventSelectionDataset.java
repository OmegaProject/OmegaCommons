package edu.umassmed.omega.commons.eventSystem.events;

import edu.umassmed.omega.commons.data.coreElements.OmegaDataset;
import edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventSelectionDataset extends OmegaPluginEvent {

	private final OmegaDataset dataset;

	public OmegaPluginEventSelectionDataset(final OmegaDataset dataset) {
		this(null, dataset);
	}

	public OmegaPluginEventSelectionDataset(final OmegaPlugin source,
			final OmegaDataset dataset) {
		super(source);
		this.dataset = dataset;
	}

	public OmegaDataset getDataset() {
		return this.dataset;
	}
}
