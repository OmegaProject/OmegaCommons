package edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainerInterface;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.utilities.OmegaIOUtility;

public class OmegaImporterEventResultsOmegaTrackingMeasures extends
		OmegaImporterEventResultsOmegaData {
	
	// Diffusivity
	private final Map<OmegaTrajectory, List<OmegaSegment>> segments;
	
	public OmegaImporterEventResultsOmegaTrackingMeasures(
			final OmegaIOUtility source,
			final OmegaAnalysisRunContainerInterface container,
			final Map<Integer, String> parents,
			final Map<String, String> analysisData,
			final Map<String, String> paramData,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final boolean completeChainAfterImport) {
		super(source, container, parents, analysisData, paramData,
				completeChainAfterImport);
		this.segments = segments;
	}
	
	public Map<OmegaTrajectory, List<OmegaSegment>> getResultingSegments() {
		return this.segments;
	}
}
