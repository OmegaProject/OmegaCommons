package edu.umassmed.omega.commons.eventSystem.events;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainer;

public class OmegaCoreEventSelectionImage extends OmegaCoreEvent {
	private final OmegaAnalysisRunContainer image;
	
	public OmegaCoreEventSelectionImage(final OmegaAnalysisRunContainer img) {
		this(-1, img);
	}
	
	public OmegaCoreEventSelectionImage(final int source,
			final OmegaAnalysisRunContainer img) {
		super(source);
		this.image = img;
	}
	
	public OmegaAnalysisRunContainer getImage() {
		return this.image;
	}
}
