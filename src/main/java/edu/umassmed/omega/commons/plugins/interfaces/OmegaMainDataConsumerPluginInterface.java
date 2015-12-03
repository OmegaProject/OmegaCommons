package main.java.edu.umassmed.omega.commons.plugins.interfaces;

import main.java.edu.umassmed.omega.commons.data.OmegaData;

public interface OmegaMainDataConsumerPluginInterface {
	public void setMainData(final OmegaData omegaData);

	public OmegaData getMainData();
}
