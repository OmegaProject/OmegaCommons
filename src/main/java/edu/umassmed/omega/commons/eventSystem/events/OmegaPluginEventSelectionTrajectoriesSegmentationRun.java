package main.java.edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import main.java.edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventSelectionTrajectoriesSegmentationRun extends
        OmegaPluginEventSelectionAnalysisRun {

	private final Map<OmegaTrajectory, List<OmegaSegment>> segmentsMap;

	public OmegaPluginEventSelectionTrajectoriesSegmentationRun(
	        final OmegaAnalysisRun analysisRun,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segmentsMap) {
		this(null, analysisRun, segmentsMap);
	}

	public OmegaPluginEventSelectionTrajectoriesSegmentationRun(
	        final OmegaPlugin source, final OmegaAnalysisRun analysisRun,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segmentsMap) {
		super(source, null);
		this.segmentsMap = segmentsMap;
	}

	public Map<OmegaTrajectory, List<OmegaSegment>> getSegmentsMap() {
		return this.segmentsMap;
	}
}
