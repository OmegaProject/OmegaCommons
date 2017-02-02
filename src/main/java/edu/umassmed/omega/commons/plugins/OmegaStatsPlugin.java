package edu.umassmed.omega.commons.plugins;

import java.util.List;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OrphanedAnalysisContainer;
import edu.umassmed.omega.commons.data.coreElements.OmegaImage;
import edu.umassmed.omega.commons.data.imageDBConnectionElements.OmegaGateway;
import edu.umassmed.omega.commons.plugins.interfaces.OmegaImageConsumerPluginInterface;
import edu.umassmed.omega.commons.plugins.interfaces.OmegaLoadedAnalysisConsumerPluginInterface;
import edu.umassmed.omega.commons.plugins.interfaces.OmegaLoaderPluginInterface;
import edu.umassmed.omega.commons.plugins.interfaces.OmegaOrphanedAnalysisConsumerPluginInterface;
import edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectImagePluginInterface;
import edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectParticleDetectionRunPluginInterface;
import edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectParticleLinkingRunPluginInterface;
import edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectSegmentsInterface;
import edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectTrackingMeasuresRunPluginInterface;
import edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectTrajectoriesRelinkingRunPluginInterface;
import edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectTrajectoriesSegmentationRunPluginInterface;

public abstract class OmegaStatsPlugin extends OmegaAlgorithmPlugin implements
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
	
	public OmegaStatsPlugin() {
		this(1);
	}
	
	public OmegaStatsPlugin(final int numOfPanels) {
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
}
