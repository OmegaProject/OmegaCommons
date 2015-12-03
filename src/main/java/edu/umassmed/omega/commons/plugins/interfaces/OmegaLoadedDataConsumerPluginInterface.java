package main.java.edu.umassmed.omega.commons.plugins.interfaces;

import main.java.edu.umassmed.omega.commons.data.OmegaLoadedData;

public interface OmegaLoadedDataConsumerPluginInterface {
	public void setLoadedData(final OmegaLoadedData loadedData);

	public OmegaLoadedData getLoadedData();
}
