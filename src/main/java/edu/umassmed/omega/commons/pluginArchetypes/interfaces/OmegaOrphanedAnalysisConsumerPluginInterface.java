package edu.umassmed.omega.commons.pluginArchetypes.interfaces;

import edu.umassmed.omega.commons.data.analysisRunElements.OrphanedAnalysisContainer;

public interface OmegaOrphanedAnalysisConsumerPluginInterface {
	public void setOrphanedAnalysis(
			final OrphanedAnalysisContainer orphanedAnalysis);

	public OrphanedAnalysisContainer getOrphanedAnalysis();
}
