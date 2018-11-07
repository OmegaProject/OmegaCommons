package edu.umassmed.omega.commons.eventSystem.events;

import edu.umassmed.omega.commons.data.coreElements.OmegaImage;
import edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventSelectionImage extends OmegaPluginEvent {
	
	private final OmegaImage image;
	
	public OmegaPluginEventSelectionImage(final OmegaImage image) {
		this(null, image);
	}
	
	public OmegaPluginEventSelectionImage(final OmegaPlugin source,
			final OmegaImage img) {
		super(source);
		this.image = img;
	}
	
	public OmegaImage getImage() {
		return this.image;
	}
}
