package main.java.edu.umassmed.omega.commons.eventSystem.events;

import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import main.java.edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventSelectionAnalysisRun extends OmegaPluginEvent {

	private final OmegaAnalysisRun analysisRun;

	public OmegaPluginEventSelectionAnalysisRun(
	        final OmegaAnalysisRun analysisRun) {
		this(null, analysisRun);
	}

	public OmegaPluginEventSelectionAnalysisRun(final OmegaPlugin source,
	        final OmegaAnalysisRun analysisRun) {
		super(source);
		this.analysisRun = analysisRun;
	}

	public OmegaAnalysisRun getAnalysisRun() {
		return this.analysisRun;
	}
}
