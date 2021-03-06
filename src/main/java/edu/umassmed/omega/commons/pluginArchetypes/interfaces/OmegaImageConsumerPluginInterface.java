package edu.umassmed.omega.commons.pluginArchetypes.interfaces;

import java.util.List;

import edu.umassmed.omega.commons.data.coreElements.OmegaImage;

public interface OmegaImageConsumerPluginInterface {
	public List<OmegaImage> getLoadedImages();

	public void setLoadedImages(final List<OmegaImage> images);
}
