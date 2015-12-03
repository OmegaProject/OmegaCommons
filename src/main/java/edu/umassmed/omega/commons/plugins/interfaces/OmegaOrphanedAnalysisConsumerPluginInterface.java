package main.java.edu.umassmed.omega.commons.plugins.interfaces;

import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OrphanedAnalysisContainer;

public interface OmegaOrphanedAnalysisConsumerPluginInterface {
	public void setOrphanedAnalysis(
			final OrphanedAnalysisContainer orphanedAnalysis);

	public OrphanedAnalysisContainer getOrphanedAnalysis();
}
