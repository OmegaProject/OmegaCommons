package edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParameter;
import edu.umassmed.omega.commons.data.coreElements.OmegaElement;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.plugins.OmegaPlugin;

public class OmegaPluginEventResultsTrackingMeasures extends
        OmegaPluginEventAlgorithm {
	
	// Diffusivity
	private final Map<OmegaTrajectory, List<OmegaSegment>> segments;
	
	public OmegaPluginEventResultsTrackingMeasures(final OmegaPlugin source,
	        final List<OmegaElement> selections, final OmegaElement element,
	        final List<OmegaParameter> params,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		super(source, selections, element, params);
		this.segments = segments;
	}
	
	public Map<OmegaTrajectory, List<OmegaSegment>> getResultingSegments() {
		return this.segments;
	}
}
