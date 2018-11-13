package edu.umassmed.omega.commons.pluginArchetypes;

import java.util.List;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OrphanedAnalysisContainer;
import edu.umassmed.omega.commons.data.coreElements.OmegaImage;
import edu.umassmed.omega.commons.data.imageDBConnectionElements.OmegaGateway;
import edu.umassmed.omega.commons.pluginArchetypes.interfaces.OmegaImageConsumerPluginInterface;
import edu.umassmed.omega.commons.pluginArchetypes.interfaces.OmegaLoadedAnalysisConsumerPluginInterface;
import edu.umassmed.omega.commons.pluginArchetypes.interfaces.OmegaLoaderPluginInterface;
import edu.umassmed.omega.commons.pluginArchetypes.interfaces.OmegaOrphanedAnalysisConsumerPluginInterface;
import edu.umassmed.omega.commons.pluginArchetypes.interfaces.OmegaSelectImagePluginInterface;
import edu.umassmed.omega.commons.pluginArchetypes.interfaces.OmegaSelectParticleDetectionRunPluginInterface;
import edu.umassmed.omega.commons.pluginArchetypes.interfaces.OmegaSelectParticleLinkingRunPluginInterface;
import edu.umassmed.omega.commons.pluginArchetypes.interfaces.OmegaSelectSegmentsInterface;
import edu.umassmed.omega.commons.pluginArchetypes.interfaces.OmegaSelectTrackingMeasuresRunPluginInterface;
import edu.umassmed.omega.commons.pluginArchetypes.interfaces.OmegaSelectTrajectoriesRelinkingRunPluginInterface;
import edu.umassmed.omega.commons.pluginArchetypes.interfaces.OmegaSelectTrajectoriesSegmentationRunPluginInterface;

public abstract class OmegaTrackingMeasuresPluginArchetype extends OmegaAlgorithmPluginArchetype implements
		OmegaSelectImagePluginInterface,
		OmegaSelectParticleDetectionRunPluginInterface,
		OmegaSelectParticleLinkingRunPluginInterface,
		OmegaSelectTrajectoriesRelinkingRunPluginInterface,
		OmegaSelectTrajectoriesSegmentationRunPluginInterface,
		OmegaSelectTrackingMeasuresRunPluginInterface,
		OmegaLoadedAnalysisConsumerPluginInterface,
		OmegaImageConsumerPluginInterface,
		OmegaOrphanedAnalysisConsumerPluginInterface,
		OmegaLoaderPluginInterface, OmegaSelectSegmentsInterface {

	private List<OmegaAnalysisRun> loadedAnalysisRuns;
	private OrphanedAnalysisContainer orphanedAnalysis;
	private List<OmegaImage> loadedImages;
	private OmegaGateway gateway;

	public OmegaTrackingMeasuresPluginArchetype() {
		this(1);
	}

	public OmegaTrackingMeasuresPluginArchetype(final int numOfPanels) {
		super(numOfPanels);

		this.loadedAnalysisRuns = null;
		this.orphanedAnalysis = null;
		this.loadedImages = null;
		this.gateway = null;
	}

	@Override
	public List<OmegaImage> getLoadedImages() {
		return this.loadedImages;
	}

	@Override
	public void setLoadedImages(final List<OmegaImage> images) {
		this.loadedImages = images;
	}

	@Override
	public void setOrphanedAnalysis(
			final OrphanedAnalysisContainer orphanedAnalysis) {
		this.orphanedAnalysis = orphanedAnalysis;
	}

	@Override
	public OrphanedAnalysisContainer getOrphanedAnalysis() {
		return this.orphanedAnalysis;
	}

	@Override
	public void setLoadedAnalysisRun(
			final List<OmegaAnalysisRun> loadedAnalysisRuns) {
		this.loadedAnalysisRuns = loadedAnalysisRuns;
	}

	@Override
	public List<OmegaAnalysisRun> getLoadedAnalysisRuns() {
		return this.loadedAnalysisRuns;
	}

	@Override
	public void setGateway(final OmegaGateway gateway) {
		this.gateway = gateway;
	}

	@Override
	public OmegaGateway getGateway() {
		return this.gateway;
	}

	public abstract void setGraphLineSize(int lineSize);

	public abstract void setGraphShapeSize(int shapeSize);
}
