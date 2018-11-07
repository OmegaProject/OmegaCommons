package edu.umassmed.omega.commons.eventSystem.events;

import edu.umassmed.omega.commons.data.coreElements.OmegaImage;

public class OmegaCoreEventSelectionImage extends OmegaCoreEvent {
	private final OmegaImage image;

	public OmegaCoreEventSelectionImage(final OmegaImage image) {
		this(-1, image);
	}

	public OmegaCoreEventSelectionImage(final int source, final OmegaImage image) {
		super(source);
		this.image = image;
	}

	public OmegaImage getImage() {
		return this.image;
	}
}
