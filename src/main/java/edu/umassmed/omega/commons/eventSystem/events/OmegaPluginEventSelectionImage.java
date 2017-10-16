package edu.umassmed.omega.commons.eventSystem.events;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainerInterface;
import edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventSelectionImage extends OmegaPluginEvent {

	private final OmegaAnalysisRunContainerInterface img;

	public OmegaPluginEventSelectionImage(final OmegaAnalysisRunContainerInterface img) {
		this(null, img);
	}

	public OmegaPluginEventSelectionImage(final OmegaPlugin source,
			final OmegaAnalysisRunContainerInterface img) {
		super(source);
		this.img = img;
	}

	public OmegaAnalysisRunContainerInterface getImage() {
		return this.img;
	}
}
