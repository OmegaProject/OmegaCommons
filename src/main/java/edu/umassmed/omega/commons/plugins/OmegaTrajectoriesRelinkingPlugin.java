package main.java.edu.umassmed.omega.commons.plugins;

import java.util.List;

import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OrphanedAnalysisContainer;
import main.java.edu.umassmed.omega.commons.data.coreElements.OmegaImage;
import main.java.edu.umassmed.omega.commons.data.imageDBConnectionElements.OmegaGateway;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaImageConsumerPluginInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaLoadedAnalysisConsumerPluginInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaLoaderPluginInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaOrphanedAnalysisConsumerPluginInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectImagePluginInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectParticleDetectionRunPluginInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectParticleLinkingRunPluginInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectTrajectoriesInterface;
import main.java.edu.umassmed.omega.commons.plugins.interfaces.OmegaSelectTrajectoriesRelinkingRunPluginInterface;

public abstract class OmegaTrajectoriesRelinkingPlugin extends
        OmegaAlgorithmPlugin implements OmegaSelectImagePluginInterface,
        OmegaSelectParticleDetectionRunPluginInterface,
        OmegaSelectParticleLinkingRunPluginInterface,
        OmegaSelectTrajectoriesRelinkingRunPluginInterface,
        OmegaLoadedAnalysisConsumerPluginInterface,
        OmegaImageConsumerPluginInterface,
        OmegaOrphanedAnalysisConsumerPluginInterface,
        OmegaLoaderPluginInterface, OmegaSelectTrajectoriesInterface {

	private List<OmegaAnalysisRun> loadedAnalysisRuns;
	private OrphanedAnalysisContainer orphanedAnalysis;
	private List<OmegaImage> loadedImages;
	private OmegaGateway gateway;

	public OmegaTrajectoriesRelinkingPlugin() {
		this(1);
	}

	public OmegaTrajectoriesRelinkingPlugin(final int numOfPanels) {
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

	public abstract void selectCurrentTrajectoriesRelinkingRun(
	        OmegaAnalysisRun analysisRun);
}
