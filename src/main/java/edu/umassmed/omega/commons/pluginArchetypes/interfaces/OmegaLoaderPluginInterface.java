package edu.umassmed.omega.commons.pluginArchetypes.interfaces;

import edu.umassmed.omega.commons.data.imageDBConnectionElements.OmegaGateway;

public interface OmegaLoaderPluginInterface {
	public OmegaGateway getGateway();

	public void setGateway(OmegaGateway gateway);
}
