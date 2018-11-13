package edu.umassmed.omega.commons.pluginArchetypes.interfaces;

import edu.umassmed.omega.commons.data.OmegaLoadedData;

public interface OmegaLoadedDataConsumerPluginInterface {
	public void setLoadedData(final OmegaLoadedData loadedData);

	public OmegaLoadedData getLoadedData();
}
