package edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.pluginArchetypes.OmegaPluginArchetype;

public class OmegaPluginEventSelectionTrajectoriesSegmentationRun extends
		OmegaPluginEventSelectionAnalysisRun {
	
	private final boolean isCurrent;
	private final Map<OmegaTrajectory, List<OmegaSegment>> segmentsMap;
	
	public OmegaPluginEventSelectionTrajectoriesSegmentationRun(
			final OmegaAnalysisRun analysisRun,
			final Map<OmegaTrajectory, List<OmegaSegment>> segmentsMap,
			final boolean isCurrent) {
		this(null, analysisRun, segmentsMap, isCurrent);
	}
	
	public OmegaPluginEventSelectionTrajectoriesSegmentationRun(
			final OmegaPluginArchetype source, final OmegaAnalysisRun analysisRun,
			final Map<OmegaTrajectory, List<OmegaSegment>> segmentsMap,
			final boolean isCurrent) {
		super(source, null);
		this.segmentsMap = segmentsMap;
		this.isCurrent = isCurrent;
	}
	
	public Map<OmegaTrajectory, List<OmegaSegment>> getSegmentsMap() {
		return this.segmentsMap;
	}

	public boolean isCurrent() {
		return this.isCurrent;
	}
}
