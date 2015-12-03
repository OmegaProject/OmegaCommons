package main.java.edu.umassmed.omega.commons.plugins;

import java.util.List;

import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OrphanedAnalysisContainer;
import main.java.edu.umassmed.omega.commons.data.coreElements.OmegaImage;
import main.java.edu.umassmed.omega.commons.data.imageDBConnectionElements.OmegaGateway;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegmentationTypes;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaImageConsumerPluginInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaLoadedAnalysisConsumerPluginInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaLoaderPluginInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaOrphanedAnalysisConsumerPluginInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectImagePluginInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectParticleDetectionRunPluginInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectParticleLinkingRunPluginInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectTrajectoriesInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectTrajectoriesRelinkingRunPluginInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectTrajectoriesSegmentationRunPluginInterface;

public abstract class OmegaTrajectoriesSegmentationPlugin extends
        OmegaAlgorithmPlugin implements OmegaSelectImagePluginInterface,
        OmegaSelectParticleDetectionRunPluginInterface,
        OmegaSelectParticleLinkingRunPluginInterface,
        OmegaSelectTrajectoriesRelinkingRunPluginInterface,
        OmegaSelectTrajectoriesSegmentationRunPluginInterface,
        OmegaLoadedAnalysisConsumerPluginInterface,
        OmegaImageConsumerPluginInterface,
        OmegaOrphanedAnalysisConsumerPluginInterface,
        OmegaLoaderPluginInterface, OmegaSelectTrajectoriesInterface {

	private List<OmegaAnalysisRun> loadedAnalysisRuns;
	private OrphanedAnalysisContainer orphanedAnalysis;
	private List<OmegaImage> loadedImages;
	private OmegaGateway gateway;

	private List<OmegaSegmentationTypes> segmTypesList;

	public OmegaTrajectoriesSegmentationPlugin() {
		this(1);
	}

	public OmegaTrajectoriesSegmentationPlugin(final int numOfPanels) {
		super(numOfPanels);

		this.loadedAnalysisRuns = null;
		this.orphanedAnalysis = null;
		this.loadedImages = null;
		this.gateway = null;

		this.segmTypesList = null;
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

	public List<OmegaSegmentationTypes> getSegmentationTypesList() {
		return this.segmTypesList;
	}

	public void setSegmentationTypesList(
	        final List<OmegaSegmentationTypes> segmTypesList) {
		this.segmTypesList = segmTypesList;
	}

	public abstract void updateSegmentationTypesList(
	        List<OmegaSegmentationTypes> segmTypesList);

	public abstract void selectCurrentTrajectoriesSegmentationRun(
	        OmegaAnalysisRun analysisRun);
}
