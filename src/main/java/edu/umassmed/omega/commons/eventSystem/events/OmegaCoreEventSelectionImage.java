package edu.umassmed.omega.commons.eventSystem.events;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainerInterface;

public class OmegaCoreEventSelectionImage extends OmegaCoreEvent {
	private final OmegaAnalysisRunContainerInterface image;
	
	public OmegaCoreEventSelectionImage(final OmegaAnalysisRunContainerInterface img) {
		this(-1, img);
	}
	
	public OmegaCoreEventSelectionImage(final int source,
			final OmegaAnalysisRunContainerInterface img) {
		super(source);
		this.image = img;
	}
	
	public OmegaAnalysisRunContainerInterface getImage() {
		return this.image;
	}
}
