package edu.umassmed.omega.commons.plugins.interfaces;

import edu.umassmed.omega.commons.trajectoryTool.OmegaTracksImporter;

public interface OmegaTracksImporterConsumerPluginInterface {
	public void setTracksImporter(OmegaTracksImporter tracksImporter);

	public OmegaTracksImporter getTracksImporter();
}
