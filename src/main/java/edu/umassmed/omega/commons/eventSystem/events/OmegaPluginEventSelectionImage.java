package main.java.edu.umassmed.omega.commons.eventSystem.events;

import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainer;
import main.java.edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventSelectionImage extends OmegaPluginEvent {

	private final OmegaAnalysisRunContainer img;

	public OmegaPluginEventSelectionImage(final OmegaAnalysisRunContainer img) {
		this(null, img);
	}

	public OmegaPluginEventSelectionImage(final OmegaPlugin source,
			final OmegaAnalysisRunContainer img) {
		super(source);
		this.img = img;
	}

	public OmegaAnalysisRunContainer getImage() {
		return this.img;
	}
}
