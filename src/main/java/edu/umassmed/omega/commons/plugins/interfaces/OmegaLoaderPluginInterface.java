package main.java.edu.umassmed.omega.commons.plugins.interfaces;

import main.java.edu.umassmed.omega.commons.data.imageDBConnectionElements.OmegaGateway;

public interface OmegaLoaderPluginInterface {
	public OmegaGateway getGateway();

	public void setGateway(OmegaGateway gateway);
}
