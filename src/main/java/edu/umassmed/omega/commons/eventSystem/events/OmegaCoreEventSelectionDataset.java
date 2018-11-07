package edu.umassmed.omega.commons.eventSystem.events;

import edu.umassmed.omega.commons.data.coreElements.OmegaDataset;

public class OmegaCoreEventSelectionDataset extends OmegaCoreEvent {
	private final OmegaDataset dataset;

	public OmegaCoreEventSelectionDataset(final OmegaDataset dataset) {
		this(-1, dataset);
	}

	public OmegaCoreEventSelectionDataset(final int source,
			final OmegaDataset img) {
		super(source);
		this.dataset = img;
	}

	public OmegaDataset getDataset() {
		return this.dataset;
	}
}
