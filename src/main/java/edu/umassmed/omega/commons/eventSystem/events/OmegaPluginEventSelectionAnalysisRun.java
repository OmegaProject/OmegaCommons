package edu.umassmed.omega.commons.eventSystem.events;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import edu.umassmed.omega.commons.pluginArchetypes.OmegaPluginArchetype;

public class OmegaPluginEventSelectionAnalysisRun extends OmegaPluginEvent {

	private final OmegaAnalysisRun analysisRun;

	public OmegaPluginEventSelectionAnalysisRun(
	        final OmegaAnalysisRun analysisRun) {
		this(null, analysisRun);
	}

	public OmegaPluginEventSelectionAnalysisRun(final OmegaPluginArchetype source,
	        final OmegaAnalysisRun analysisRun) {
		super(source);
		this.analysisRun = analysisRun;
	}

	public OmegaAnalysisRun getAnalysisRun() {
		return this.analysisRun;
	}
}
