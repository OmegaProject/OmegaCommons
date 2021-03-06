package edu.umassmed.omega.commons.pluginArchetypes.interfaces;

import java.util.List;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;

public interface OmegaLoadedAnalysisConsumerPluginInterface {
	public void setLoadedAnalysisRun(
	        final List<OmegaAnalysisRun> loadedAnalysisRuns);

	public List<OmegaAnalysisRun> getLoadedAnalysisRuns();
}
