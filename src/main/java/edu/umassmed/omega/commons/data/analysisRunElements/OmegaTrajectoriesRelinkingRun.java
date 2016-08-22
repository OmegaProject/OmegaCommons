package edu.umassmed.omega.commons.data.analysisRunElements;

import java.util.Date;
import java.util.List;

import edu.umassmed.omega.commons.data.coreElements.OmegaExperimenter;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class OmegaTrajectoriesRelinkingRun extends OmegaParticleLinkingRun {

	public OmegaTrajectoriesRelinkingRun(final OmegaExperimenter owner,
	        final OmegaRunDefinition algorithmSpec,
	        final List<OmegaTrajectory> resultingTrajectory) {
		super(owner, algorithmSpec, resultingTrajectory);
	}

	public OmegaTrajectoriesRelinkingRun(final OmegaExperimenter owner,
	        final OmegaRunDefinition algorithmSpec, final String name,
	        final List<OmegaTrajectory> resultingTrajectories) {
		super(owner, algorithmSpec, name, resultingTrajectories);
	}

	public OmegaTrajectoriesRelinkingRun(final OmegaExperimenter owner,
	        final OmegaRunDefinition algorithmSpec,
	        final Date timeStamps, final String name,
	        final List<OmegaTrajectory> resultingTrajectories) {
		super(owner, algorithmSpec, timeStamps, name, resultingTrajectories);
	}
}
