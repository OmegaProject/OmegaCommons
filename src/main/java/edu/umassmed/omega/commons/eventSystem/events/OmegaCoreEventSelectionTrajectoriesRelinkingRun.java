package main.java.edu.umassmed.omega.commons.eventSystem.events;

import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;

public class OmegaCoreEventSelectionTrajectoriesRelinkingRun extends
        OmegaCoreEventSelectionAnalysisRun {

	public OmegaCoreEventSelectionTrajectoriesRelinkingRun() {
		this(-1, null);
	}

	public OmegaCoreEventSelectionTrajectoriesRelinkingRun(final int source) {
		super(source, null);
	}

	public OmegaCoreEventSelectionTrajectoriesRelinkingRun(
	        final OmegaAnalysisRun analysisRun) {
		this(-1, analysisRun);
	}

	public OmegaCoreEventSelectionTrajectoriesRelinkingRun(final int source,
	        final OmegaAnalysisRun analysisRun) {
		super(source, analysisRun);
	}
}
