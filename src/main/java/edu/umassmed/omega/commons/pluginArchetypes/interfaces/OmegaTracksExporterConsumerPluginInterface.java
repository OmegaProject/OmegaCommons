package edu.umassmed.omega.commons.pluginArchetypes.interfaces;

import edu.umassmed.omega.commons.trajectoryTool.OmegaTracksExporter;

public interface OmegaTracksExporterConsumerPluginInterface {
	public void setTracksExporter(OmegaTracksExporter tracksExporter);

	public OmegaTracksExporter getTracksExporter();
}
