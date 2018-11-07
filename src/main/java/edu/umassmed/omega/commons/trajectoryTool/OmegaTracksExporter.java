package edu.umassmed.omega.commons.trajectoryTool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.RootPaneContainer;

import edu.umassmed.omega.commons.constants.OmegaAlgorithmParameterConstants;
import edu.umassmed.omega.commons.constants.OmegaGUIConstants;
import edu.umassmed.omega.commons.constants.OmegaGenericConstants;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParameter;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParticleDetectionRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParticleLinkingRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaRunDefinition;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaSNRRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresDiffusivityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresIntensityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresMobilityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresVelocityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrajectoriesRelinkingRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrajectoriesSegmentationRun;
import edu.umassmed.omega.commons.data.coreElements.OmegaImage;
import edu.umassmed.omega.commons.data.coreElements.OmegaPlane;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegmentationTypes;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.trajectoryTool.gui.OmegaTracksToolDialog;
import edu.umassmed.omega.commons.utilities.OmegaAnalysisRunContainerUtilities;
import edu.umassmed.omega.commons.utilities.OmegaIOUtility;
import edu.umassmed.omega.commons.utilities.OmegaMathsUtilities;
import edu.umassmed.omega.commons.utilities.OmegaStringUtilities;
import edu.umassmed.omega.commons.utilities.OmegaTrajectoryUtilities;

public class OmegaTracksExporter extends OmegaIOUtility {
	
	private Map<Integer, OmegaImage> images;
	private Map<Integer, Map<Integer, OmegaParticleDetectionRun>> detRuns;
	private Map<Integer, Map<Integer, OmegaParticleLinkingRun>> linkRuns;
	private Map<Integer, Map<Integer, OmegaTrajectoriesRelinkingRun>> relinkRuns;
	private Map<Integer, Map<Integer, OmegaTrajectoriesSegmentationRun>> segmRuns;
	private Map<Integer, Map<Integer, OmegaTrackingMeasuresIntensityRun>> inteRuns;
	private Map<Integer, Map<Integer, OmegaTrackingMeasuresMobilityRun>> mobiRuns;
	private Map<Integer, Map<Integer, OmegaTrackingMeasuresVelocityRun>> veloRuns;
	private Map<Integer, Map<Integer, OmegaTrackingMeasuresDiffusivityRun>> diffRuns;
	private Map<Integer, Map<Integer, OmegaSNRRun>> snrRuns;

	private Map<OmegaAnalysisRun, String> runPaths;
	
	private boolean exportLastOnly;
	// private final Map<OmegaPlane, List<OmegaROI>> particles;
	// private final Map<OmegaROI, Map<String, Object>> particlesValues;
	// private final List<OmegaTrajectory> tracks;
	
	private OmegaTracksToolDialog dialog;
	
	public OmegaTracksExporter(final RootPaneContainer parent) {
		// this.particles = new LinkedHashMap<OmegaPlane, List<OmegaROI>>();
		// this.particlesValues = new LinkedHashMap<OmegaROI, Map<String,
		// Object>>();
		// this.tracks = new ArrayList<OmegaTrajectory>();
		this.exportLastOnly = false;
		this.detRuns = null;
		this.linkRuns = null;
		this.relinkRuns = null;
		this.segmRuns = null;
		this.inteRuns = null;
		this.mobiRuns = null;
		this.veloRuns = null;
		this.diffRuns = null;
		this.snrRuns = null;
		this.dialog = new OmegaTracksToolDialog(parent, true, false, this);
	}
	
	public OmegaTracksExporter() {
		// this.particles = new LinkedHashMap<OmegaPlane, List<OmegaROI>>();
		// this.particlesValues = new LinkedHashMap<OmegaROI, Map<String,
		// Object>>();
		// this.tracks = new ArrayList<OmegaTrajectory>();
		this.exportLastOnly = false;
		this.images = null;
		this.detRuns = null;
		this.linkRuns = null;
		this.relinkRuns = null;
		this.segmRuns = null;
		this.inteRuns = null;
		this.mobiRuns = null;
		this.veloRuns = null;
		this.diffRuns = null;
		this.snrRuns = null;
		this.dialog = null;
	}
	
	public void showDialog(final RootPaneContainer parent) {
		if (this.dialog == null) {
			this.dialog = new OmegaTracksToolDialog(parent, true, false, this);
		} else {
			this.dialog.resetDialog();
		}
		String prefix = "";
		if ((this.images != null) && (this.images.size() == 1)) {
			prefix = this.images.get(0).getName();
			this.dialog.setPrefix(prefix);
		}

		this.dialog.enableFileName();

		// final String name = null;
		if (!this.exportLastOnly) {
			this.dialog.disableFileName();
		} else if ((this.inteRuns != null) && (this.inteRuns.size() == 1)
				&& (this.inteRuns.get(0).size() == 1)) {
			this.dialog.setFileName(this.inteRuns.get(0).get(0).getName());
		} else if ((this.veloRuns != null) && (this.veloRuns.size() == 1)
				&& (this.veloRuns.get(0).size() == 1)) {
			this.dialog.setFileName(this.veloRuns.get(0).get(0).getName());
		} else if ((this.mobiRuns != null) && (this.mobiRuns.size() == 1)
				&& (this.mobiRuns.get(0).size() == 1)) {
			this.dialog.setFileName(this.mobiRuns.get(0).get(0).getName());
		} else if ((this.diffRuns != null) && (this.diffRuns.size() == 1)
				&& (this.diffRuns.get(0).size() == 1)) {
			this.dialog.setFileName(this.diffRuns.get(0).get(0).getName());
		} else if ((this.segmRuns != null) && (this.segmRuns.size() == 1)
				&& (this.segmRuns.get(0).size() == 1)) {
			this.dialog.setFileName(this.segmRuns.get(0).get(0).getName());
		} else if ((this.relinkRuns != null) && (this.relinkRuns.size() == 1)
				&& (this.relinkRuns.get(0).size() == 1)) {
			this.dialog.setFileName(this.relinkRuns.get(0).get(0).getName());
		} else if ((this.linkRuns != null) && (this.linkRuns.size() == 1)
				&& (this.linkRuns.get(0).size() == 1)) {
			this.dialog.setFileName(this.linkRuns.get(0).get(0).getName());
		} else if ((this.snrRuns != null) && (this.snrRuns.size() == 1)
				&& (this.snrRuns.get(0).size() == 1)) {
			this.dialog.setFileName(this.snrRuns.get(0).get(0).getName());
		} else if ((this.detRuns != null) && (this.detRuns.size() == 1)
				&& (this.detRuns.get(0).size() == 1)) {
			this.dialog.setFileName(this.detRuns.get(0).get(0).getName());
			// this.dialog.setFileName(name);
		} else {
			this.dialog.disableFileName();
		}
		// this.dialog.setFileName(name);

		this.dialog.updateParentContainer(parent);
		this.dialog.setVisible(true);
	}
	
	public void export(final boolean multifile,
			final String fileNameIdentifier, final String extension,
			final boolean addImageName, final boolean createSubFolders,
			final String targetIdentifier, final String dataIdentifier,
			final boolean startAtOne, final String commentIdentifier,
			final String dataSeparator, final File sourceFolder)
			throws IOException {

		if (!sourceFolder.isDirectory())
			throw new IllegalArgumentException("The source folder "
					+ sourceFolder + " has to be a valid directory");

		String newTargetIdent = "";
		if (targetIdentifier != null) {
			newTargetIdent = targetIdentifier;
			newTargetIdent = newTargetIdent.replace("TAB", "\t");
			newTargetIdent = newTargetIdent.replace("SPACE", " ");
		}
		String newDataIdent = "";
		if (dataIdentifier != null) {
			newDataIdent = dataIdentifier;
			newDataIdent = newDataIdent.replace("TAB", "\t");
			newDataIdent = newDataIdent.replace("SPACE", " ");
		}
		
		// TODO if csv this needs to be set to ,
		String newDataSep = "\t";
		if (dataSeparator != null) {
			newDataSep = dataSeparator;
			newDataSep = newDataSep.replace("TAB", "\t");
			newDataSep = newDataSep.replace("SPACE", " ");
		}
		String newCommentIdent = "";
		if (commentIdentifier != null) {
			newCommentIdent = commentIdentifier;
			newCommentIdent = newCommentIdent.replace("TAB", "\t");
			newCommentIdent = newCommentIdent.replace("SPACE", " ");
		}
		if (this.diffRuns != null) {
			for (final Integer imageIndex : this.images.keySet()) {
				final Map<Integer, OmegaTrackingMeasuresDiffusivityRun> runs = this.diffRuns
						.get(imageIndex);
				final Map<Integer, OmegaTrajectoriesSegmentationRun> runs2 = this.segmRuns
						.get(imageIndex);
				for (final Integer index : runs2.keySet()) {
					final OmegaTrajectoriesSegmentationRun segmRun = runs2
							.get(index);
					for (final Integer index2 : runs.keySet()) {
						final OmegaTrackingMeasuresDiffusivityRun diffRun = runs
								.get(index2);
						if (!segmRun.getAnalysisRuns().contains(diffRun)) {
							continue;
						}
						String filename = null;
						final String path = this.runPaths.get(diffRun);
						if (addImageName && !this.exportLastOnly) {
							filename = this.images.get(imageIndex).getName()
									+ "_" + diffRun.getName();
						} else if (!this.exportLastOnly) {
							filename = diffRun.getName();
						}/**
						 * else if (addImageName) { filename =
						 * this.images.get(imageIndex).getName() + "_" +
						 * fileNameIdentifier; }
						 **/
						else {
							filename = fileNameIdentifier;
						}
						this.exportAnalysisMetadata(path, filename, diffRun,
								multifile, createSubFolders, newDataSep,
								newCommentIdent, sourceFolder, extension);
						this.exportTrackingMeasuresDiffusivity(path, filename,
								diffRun, segmRun.getResultingSegments(),
								segmRun.getSegmentationTypes(), multifile,
								createSubFolders, extension, newTargetIdent,
								newDataIdent, startAtOne, newCommentIdent,
								newDataSep, sourceFolder);
					}
				}
			}
			if (this.exportLastOnly)
				return;
		}
		if (this.mobiRuns != null) {
			for (final Integer imageIndex : this.images.keySet()) {
				final Map<Integer, OmegaTrackingMeasuresMobilityRun> runs = this.mobiRuns
						.get(imageIndex);
				final Map<Integer, OmegaParticleDetectionRun> runs2 = this.detRuns
						.get(imageIndex);
				final Map<Integer, OmegaTrajectoriesSegmentationRun> runs3 = this.segmRuns
						.get(imageIndex);
				final Map<Integer, OmegaParticleLinkingRun> runs4 = this.linkRuns
						.get(imageIndex);
				final Map<Integer, OmegaTrajectoriesRelinkingRun> runs5 = this.relinkRuns
						.get(imageIndex);
				for (final Integer index : runs2.keySet()) {
					final OmegaParticleDetectionRun detRun = runs2.get(index);
					for (final Integer index2 : runs4.keySet()) {
						final OmegaParticleLinkingRun linkRun = runs4
								.get(index2);
						if (!detRun.getAnalysisRuns().contains(linkRun)) {
							continue;
						}
						for (final Integer index3 : runs5.keySet()) {
							final OmegaTrajectoriesRelinkingRun relinkRun = runs5
									.get(index3);
							if (!linkRun.getAnalysisRuns().contains(relinkRun)) {
								continue;
							}
							for (final Integer index4 : runs3.keySet()) {
								final OmegaTrajectoriesSegmentationRun segmRun = runs3
										.get(index4);
								if (!relinkRun.getAnalysisRuns().contains(
										segmRun)) {
									continue;
								}
								for (final Integer index5 : runs.keySet()) {
									final OmegaTrackingMeasuresMobilityRun mobiRun = runs
											.get(index5);
									if (!segmRun.getAnalysisRuns().contains(
											mobiRun)) {
										continue;
									}
									final Map<Integer, OmegaPlane> frames = new LinkedHashMap<Integer, OmegaPlane>();
									for (final OmegaPlane plane : detRun
											.getResultingParticles().keySet()) {
										frames.put(plane.getIndex(), plane);
									}
									String filename = null;
									final String path = this.runPaths
											.get(mobiRun);
									if (addImageName && !this.exportLastOnly) {
										filename = this.images.get(imageIndex)
												.getName()
												+ "_"
												+ mobiRun.getName();
									} else if (!this.exportLastOnly) {
										filename = mobiRun.getName();
									}/**
									 * else if (addImageName) { filename =
									 * this.images.get(imageIndex).getName() +
									 * "_" + fileNameIdentifier; }
									 **/
									else {
										filename = fileNameIdentifier;
									}
									this.exportAnalysisMetadata(path, filename,
											mobiRun, multifile,
											createSubFolders, newDataSep,
											newCommentIdent, sourceFolder,
											extension);
									this.exportTrackingMeasuresMobility(path,
											filename, mobiRun,
											segmRun.getResultingSegments(),
											segmRun.getSegmentationTypes(),
											frames, multifile,
											createSubFolders, extension,
											newTargetIdent, newDataIdent,
											startAtOne, newCommentIdent,
											newDataSep, sourceFolder);
								}
							}
						}
					}
				}
			}
			if (this.exportLastOnly)
				return;
		}
		if (this.veloRuns != null) {
			for (final Integer imageIndex : this.images.keySet()) {
				final Map<Integer, OmegaTrackingMeasuresVelocityRun> runs = this.veloRuns
						.get(imageIndex);
				final Map<Integer, OmegaParticleDetectionRun> runs2 = this.detRuns
						.get(imageIndex);
				final Map<Integer, OmegaTrajectoriesSegmentationRun> runs3 = this.segmRuns
						.get(imageIndex);
				final Map<Integer, OmegaParticleLinkingRun> runs4 = this.linkRuns
						.get(imageIndex);
				final Map<Integer, OmegaTrajectoriesRelinkingRun> runs5 = this.relinkRuns
						.get(imageIndex);
				for (final Integer index : runs2.keySet()) {
					final OmegaParticleDetectionRun detRun = runs2.get(index);
					for (final Integer index2 : runs4.keySet()) {
						final OmegaParticleLinkingRun linkRun = runs4
								.get(index2);
						if (!detRun.getAnalysisRuns().contains(linkRun)) {
							continue;
						}
						for (final Integer index3 : runs5.keySet()) {
							final OmegaTrajectoriesRelinkingRun relinkRun = runs5
									.get(index3);
							if (!linkRun.getAnalysisRuns().contains(relinkRun)) {
								continue;
							}
							for (final Integer index4 : runs3.keySet()) {
								final OmegaTrajectoriesSegmentationRun segmRun = runs3
										.get(index4);
								if (!relinkRun.getAnalysisRuns().contains(
										segmRun)) {
									continue;
								}
								for (final Integer index5 : runs.keySet()) {
									final OmegaTrackingMeasuresVelocityRun veloRun = runs
											.get(index5);
									if (!segmRun.getAnalysisRuns().contains(
											veloRun)) {
										continue;
									}
									final Map<Integer, OmegaPlane> frames = new LinkedHashMap<Integer, OmegaPlane>();
									for (final OmegaPlane plane : detRun
											.getResultingParticles().keySet()) {
										frames.put(plane.getIndex(), plane);
									}
									String filename = null;
									final String path = this.runPaths
											.get(veloRun);
									if (addImageName && !this.exportLastOnly) {
										filename = this.images.get(imageIndex)
												.getName()
												+ "_"
												+ veloRun.getName();
									} else if (!this.exportLastOnly) {
										filename = veloRun.getName();
									}/**
									 * else if (addImageName) { filename =
									 * this.images.get(imageIndex).getName() +
									 * "_" + fileNameIdentifier; }
									 **/
									else {
										filename = fileNameIdentifier;
									}
									this.exportAnalysisMetadata(path, filename,
											veloRun, multifile,
											createSubFolders, newDataSep,
											newCommentIdent, sourceFolder,
											extension);
									this.exportTrackingMeasuresVelocity(path,
											filename, veloRun,
											segmRun.getResultingSegments(),
											segmRun.getSegmentationTypes(),
											frames, multifile,
											createSubFolders, extension,
											newTargetIdent, newDataIdent,
											startAtOne, newCommentIdent,
											newDataSep, sourceFolder);
								}
							}
						}
					}
				}
			}
			if (this.exportLastOnly)
				return;
		}
		if (this.inteRuns != null) {
			for (final Integer imageIndex : this.images.keySet()) {
				final Map<Integer, OmegaTrackingMeasuresIntensityRun> runs = this.inteRuns
						.get(imageIndex);
				final Map<Integer, OmegaParticleDetectionRun> runs2 = this.detRuns
						.get(imageIndex);
				final Map<Integer, OmegaTrajectoriesSegmentationRun> runs3 = this.segmRuns
						.get(imageIndex);
				final Map<Integer, OmegaParticleLinkingRun> runs4 = this.linkRuns
						.get(imageIndex);
				final Map<Integer, OmegaTrajectoriesRelinkingRun> runs5 = this.relinkRuns
						.get(imageIndex);
				for (final Integer index : runs2.keySet()) {
					final OmegaParticleDetectionRun detRun = runs2.get(index);
					for (final Integer index2 : runs4.keySet()) {
						final OmegaParticleLinkingRun linkRun = runs4
								.get(index2);
						if (!detRun.getAnalysisRuns().contains(linkRun)) {
							continue;
						}
						for (final Integer index3 : runs5.keySet()) {
							final OmegaTrajectoriesRelinkingRun relinkRun = runs5
									.get(index3);
							if (!linkRun.getAnalysisRuns().contains(relinkRun)) {
								continue;
							}
							for (final Integer index4 : runs3.keySet()) {
								final OmegaTrajectoriesSegmentationRun segmRun = runs3
										.get(index4);
								if (!relinkRun.getAnalysisRuns().contains(
										segmRun)) {
									continue;
								}
								for (final Integer index5 : runs.keySet()) {
									final OmegaTrackingMeasuresIntensityRun inteRun = runs
											.get(index5);
									if (!segmRun.getAnalysisRuns().contains(
											inteRun)) {
										continue;
									}
									final Map<Integer, OmegaPlane> frames = new LinkedHashMap<Integer, OmegaPlane>();
									for (final OmegaPlane plane : detRun
											.getResultingParticles().keySet()) {
										frames.put(plane.getIndex(), plane);
									}
									String filename = null;
									final String path = this.runPaths
											.get(inteRun);
									if (addImageName && !this.exportLastOnly) {
										filename = this.images.get(imageIndex)
												.getName()
												+ "_"
												+ inteRun.getName();
									} else if (!this.exportLastOnly) {
										filename = inteRun.getName();
									}/**
									 * else if (addImageName) { filename =
									 * this.images.get(imageIndex).getName() +
									 * "_" + fileNameIdentifier; }
									 **/
									else {
										filename = fileNameIdentifier;
									}
									this.exportAnalysisMetadata(path, filename,
											inteRun, multifile,
											createSubFolders, newDataSep,
											newCommentIdent, sourceFolder,
											extension);
									this.exportTrackingMeasuresIntensity(path,
											filename, inteRun,
											segmRun.getResultingSegments(),
											segmRun.getSegmentationTypes(),
											frames, multifile,
											createSubFolders, extension,
											newTargetIdent, newDataIdent,
											startAtOne, newCommentIdent,
											newDataSep, sourceFolder);
								}
							}
						}
					}
				}
			}
			if (this.exportLastOnly)
				return;
		}
		if (this.segmRuns != null) {
			for (final Integer imageIndex : this.images.keySet()) {
				final Map<Integer, OmegaTrajectoriesSegmentationRun> runs = this.segmRuns
						.get(imageIndex);
				final Map<Integer, OmegaParticleDetectionRun> runs2 = this.detRuns
						.get(imageIndex);
				final Map<Integer, OmegaParticleLinkingRun> runs4 = this.linkRuns
						.get(imageIndex);
				final Map<Integer, OmegaTrajectoriesRelinkingRun> runs5 = this.relinkRuns
						.get(imageIndex);
				for (final Integer index : runs2.keySet()) {
					final OmegaParticleDetectionRun detRun = runs2.get(index);
					for (final Integer index2 : runs4.keySet()) {
						final OmegaParticleLinkingRun linkRun = runs4
								.get(index2);
						if (!detRun.getAnalysisRuns().contains(linkRun)) {
							continue;
						}
						for (final Integer index3 : runs5.keySet()) {
							final OmegaTrajectoriesRelinkingRun relinkRun = runs5
									.get(index3);
							if (!linkRun.getAnalysisRuns().contains(relinkRun)) {
								continue;
							}
							for (final Integer index4 : runs.keySet()) {
								final OmegaTrajectoriesSegmentationRun segmRun = runs
										.get(index4);
								if (!relinkRun.getAnalysisRuns().contains(
										segmRun)) {
									continue;
								}
								final Map<Integer, OmegaPlane> frames = new LinkedHashMap<Integer, OmegaPlane>();
								for (final OmegaPlane plane : detRun
										.getResultingParticles().keySet()) {
									frames.put(plane.getIndex(), plane);
								}
								String filename = null;
								final String path = this.runPaths.get(segmRun);
								if (addImageName && !this.exportLastOnly) {
									filename = this.images.get(imageIndex)
											.getName()
											+ "_"
											+ segmRun.getName();
								} else if (!this.exportLastOnly) {
									filename = segmRun.getName();
								}/**
								 * else if (addImageName) { filename =
								 * this.images.get(imageIndex).getName() + "_" +
								 * fileNameIdentifier; }
								 **/
								else {
									filename = fileNameIdentifier;
								}
								this.exportAnalysisMetadata(path, filename,
										segmRun, multifile, createSubFolders,
										newDataSep, newCommentIdent,
										sourceFolder, extension);
								this.exportSegments(path, filename,
										segmRun.getResultingSegments(),
										segmRun.getSegmentationTypes(), frames,
										multifile, createSubFolders, extension,
										newTargetIdent, newDataIdent,
										startAtOne, newCommentIdent,
										newDataSep, sourceFolder);
							}
						}
					}
				}
			}
			if (this.exportLastOnly)
				return;
		}
		if (this.relinkRuns != null) {
			for (final Integer imageIndex : this.images.keySet()) {
				final Map<Integer, OmegaTrajectoriesRelinkingRun> runs = this.relinkRuns
						.get(imageIndex);
				final Map<Integer, OmegaParticleDetectionRun> runs2 = this.detRuns
						.get(imageIndex);
				final Map<Integer, OmegaParticleLinkingRun> runs4 = this.linkRuns
						.get(imageIndex);
				for (final Integer index : runs2.keySet()) {
					final OmegaParticleDetectionRun detRun = runs2.get(index);
					for (final Integer index2 : runs4.keySet()) {
						final OmegaParticleLinkingRun linkRun = runs4
								.get(index2);
						if (!detRun.getAnalysisRuns().contains(linkRun)) {
							continue;
						}
						for (final Integer index3 : runs.keySet()) {
							final OmegaTrajectoriesRelinkingRun relinkRun = runs
									.get(index3);
							if (!linkRun.getAnalysisRuns().contains(relinkRun)) {
								continue;
							}
							final Map<Integer, OmegaPlane> frames = new LinkedHashMap<Integer, OmegaPlane>();
							for (final OmegaPlane plane : detRun
									.getResultingParticles().keySet()) {
								frames.put(plane.getIndex(), plane);
							}
							String filename = null;
							final String path = this.runPaths.get(relinkRun);
							if (addImageName && !this.exportLastOnly) {
								filename = this.images.get(imageIndex)
										.getName() + "_" + relinkRun.getName();
							} else if (!this.exportLastOnly) {
								filename = relinkRun.getName();
							}/**
							 * else if (addImageName) { filename =
							 * this.images.get(imageIndex).getName() + "_" +
							 * fileNameIdentifier; }
							 **/
							else {
								filename = fileNameIdentifier;
							}
							this.exportAnalysisMetadata(path, filename,
									relinkRun, multifile, createSubFolders,
									newDataSep, newCommentIdent, sourceFolder,
									extension);
							this.exportTrajectories(path, filename,
									relinkRun.getResultingTrajectories(),
									frames, multifile, createSubFolders,
									extension, newTargetIdent, newDataIdent,
									startAtOne, newCommentIdent, newDataSep,
									sourceFolder);
						}
					}
				}
			}
			if (this.exportLastOnly)
				return;
		}
		if (this.linkRuns != null) {
			for (final Integer imageIndex : this.images.keySet()) {
				final Map<Integer, OmegaParticleLinkingRun> runs = this.linkRuns
						.get(imageIndex);
				final Map<Integer, OmegaParticleDetectionRun> runs2 = this.detRuns
						.get(imageIndex);
				for (final Integer index : runs2.keySet()) {
					final OmegaParticleDetectionRun detRun = runs2.get(index);
					for (final Integer index2 : runs.keySet()) {
						final OmegaParticleLinkingRun linkRun = runs
								.get(index2);
						if (!detRun.getAnalysisRuns().contains(linkRun)) {
							continue;
						}
						final Map<Integer, OmegaPlane> frames = new LinkedHashMap<Integer, OmegaPlane>();
						for (final OmegaPlane plane : detRun
								.getResultingParticles().keySet()) {
							frames.put(plane.getIndex(), plane);
						}
						String filename = null;
						final String path = this.runPaths.get(linkRun);
						if (addImageName && !this.exportLastOnly) {
							filename = this.images.get(imageIndex).getName()
									+ "_" + linkRun.getName();
						} else if (!this.exportLastOnly) {
							filename = linkRun.getName();
						}/**
						 * else if (addImageName) { filename =
						 * this.images.get(imageIndex).getName() + "_" +
						 * fileNameIdentifier; }
						 **/
						else {
							filename = fileNameIdentifier;
						}
						this.exportAnalysisMetadata(path, filename, linkRun,
								multifile, createSubFolders, newDataSep,
								newCommentIdent, sourceFolder, extension);
						this.exportTrajectories(path, filename,
								linkRun.getResultingTrajectories(), frames,
								multifile, createSubFolders, extension,
								newTargetIdent, newDataIdent, startAtOne,
								newCommentIdent, newDataSep, sourceFolder);
					}
				}
			}
			if (this.exportLastOnly)
				return;
		}
		if (this.snrRuns != null) {
			for (final Integer imageIndex : this.images.keySet()) {
				final OmegaImage image = this.images.get(imageIndex);
				final Map<Integer, OmegaSNRRun> runs = this.snrRuns
						.get(imageIndex);
				final Map<Integer, OmegaParticleDetectionRun> runs2 = this.detRuns
						.get(imageIndex);
				for (final Integer index : runs2.keySet()) {
					final OmegaParticleDetectionRun detRun = runs2.get(index);
					for (final Integer index2 : runs.keySet()) {
						final OmegaSNRRun snrRun = runs.get(index2);
						if (!detRun.getAnalysisRuns().contains(snrRun)) {
							continue;
						}
						final Map<Integer, OmegaPlane> frames = new LinkedHashMap<Integer, OmegaPlane>();
						for (final OmegaPlane plane : detRun
								.getResultingParticles().keySet()) {
							frames.put(plane.getIndex(), plane);
						}
						String filename = null;
						final String path = this.runPaths.get(snrRun);
						if (addImageName && !this.exportLastOnly) {
							filename = this.images.get(imageIndex).getName()
									+ "_" + snrRun.getName();
						} else if (!this.exportLastOnly) {
							filename = snrRun.getName();
						}/**
						 * else if (addImageName) { filename =
						 * this.images.get(imageIndex).getName() + "_" +
						 * fileNameIdentifier; }
						 **/
						else {
							filename = fileNameIdentifier;
						}
						this.exportAnalysisMetadata(path, filename, snrRun,
								multifile, createSubFolders, newDataSep,
								newCommentIdent, sourceFolder, extension);
						this.exportSNR(path, filename, image, snrRun,
								detRun.getResultingParticles(), frames,
								multifile, createSubFolders, extension,
								newTargetIdent, newDataIdent, startAtOne,
								newCommentIdent, newDataSep, sourceFolder);
					}
				}
			}
			if (this.exportLastOnly)
				return;
		}
		if (this.detRuns != null) {
			for (final Integer imageIndex : this.images.keySet()) {
				final Map<Integer, OmegaParticleDetectionRun> runs = this.detRuns
						.get(imageIndex);
				for (final Integer index : runs.keySet()) {
					final OmegaParticleDetectionRun detRun = runs.get(index);
					String filename = null;
					final String path = this.runPaths.get(detRun);
					if (addImageName && !this.exportLastOnly) {
						filename = this.images.get(imageIndex).getName() + "_"
								+ detRun.getName();
					} else if (!this.exportLastOnly) {
						filename = detRun.getName();
					}/**
					 * else if (addImageName) { filename =
					 * this.images.get(imageIndex).getName() + "_" +
					 * fileNameIdentifier; }
					 **/
					else {
						filename = fileNameIdentifier;
					}
					this.exportAnalysisMetadata(path, filename, detRun,
							multifile, createSubFolders, newDataSep,
							newCommentIdent, sourceFolder, extension);
					this.exportParticles(path, filename,
							detRun.getResultingParticles(),
							detRun.getResultingParticlesValues(), multifile,
							createSubFolders, extension, newTargetIdent,
							newDataIdent, startAtOne, newCommentIdent,
							newDataSep, sourceFolder);
				}
			}
		}
	}
	
	private void exportAnalysisMetadata(final String path,
			final String filename, final OmegaAnalysisRun analysisRun,
			final boolean multifile, final boolean createSubFolders,
			final String newDataSep, final String newCommentIdent,
			final File sourceFolder, final String extension) throws IOException {
		final SimpleDateFormat format = new SimpleDateFormat(
				OmegaGenericConstants.OMEGA_DATE_FORMAT);
		File f1 = null, f2 = null, f3 = null;
		File targetDir = null;
		if (createSubFolders) {
			targetDir = new File(sourceFolder.getPath() + File.separatorChar
					+ path);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
		} else {
			targetDir = sourceFolder;
		}
		String[] parents = null;
		if ((path != null) && !path.isEmpty()) {
			final String sep = File.separatorChar + "" + File.separatorChar;
			parents = path.split(sep);
		}
		
		if (multifile) {
			f1 = new File(targetDir.getPath() + File.separatorChar + filename
					+ "_metadata." + extension);
		} else {
			if (analysisRun instanceof OmegaTrackingMeasuresDiffusivityRun) {
				f1 = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_DM_DDT + "."
						+ extension);
				f2 = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_DM_MSS + "."
						+ extension);
				f3 = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_DM_PS + "."
						+ extension);
			} else if (analysisRun instanceof OmegaSNRRun) {
				f1 = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_SNR_IMAGE + "."
						+ extension);
				f2 = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_SNR_PLANE + "."
						+ extension);
				f3 = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_SNR_ROI + "."
						+ extension);
			} else if ((analysisRun instanceof OmegaTrackingMeasuresIntensityRun)
					|| (analysisRun instanceof OmegaTrackingMeasuresVelocityRun)
					|| (analysisRun instanceof OmegaTrackingMeasuresMobilityRun)) {
				f1 = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_GLOBAL + "."
						+ extension);
				f2 = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_LOCAL + "."
						+ extension);
			} else {
				f1 = new File(targetDir.getPath() + File.separatorChar
						+ filename + "." + extension);
			}
		}
		if (!f1.exists()) {
			f1.createNewFile();
		}
		if ((f2 != null) && !f2.exists()) {
			f2.createNewFile();
		}
		if ((f3 != null) && !f3.exists()) {
			f3.createNewFile();
		}
		final StringBuffer buf = new StringBuffer();
		final StringBuffer buf2 = new StringBuffer();
		final StringBuffer buf3 = new StringBuffer();

		final StringBuffer tmpBuf = new StringBuffer();

		final String newline = "\n";

		// buf.append(newCommentIdent);
		// buf.append("Summary");
		// buf.append(newline);

		String id;
		if (analysisRun.getElementID() == -1) {
			id = OmegaGUIConstants.NOT_ASSIGNED;
		} else {
			id = String.valueOf(analysisRun.getElementID());
		}

		buf.append(newCommentIdent);
		buf.append(analysisRun.getDynamicDisplayName());
		buf.append(" ");
		buf.append(OmegaGUIConstants.INFO_ID);
		buf.append(newDataSep);
		buf.append(id);
		buf.append(newline);

		buf.append(newCommentIdent);
		buf.append(OmegaGUIConstants.INFO_FILE_TYPE);
		buf.append(newDataSep);

		buf2.append(buf);
		buf3.append(buf);

		if (analysisRun instanceof OmegaTrackingMeasuresDiffusivityRun) {
			buf.append(OmegaGUIConstants.INFO_FILE_TYPE_DM_DDT);
			buf2.append(OmegaGUIConstants.INFO_FILE_TYPE_DM_MSS);
			buf3.append(OmegaGUIConstants.INFO_FILE_TYPE_DM_PS);
		} else if ((analysisRun instanceof OmegaTrackingMeasuresIntensityRun)
				|| (analysisRun instanceof OmegaTrackingMeasuresVelocityRun)
				|| (analysisRun instanceof OmegaTrackingMeasuresMobilityRun)) {
			buf.append(OmegaGUIConstants.INFO_FILE_TYPE_GLOBAL);
			buf2.append(OmegaGUIConstants.INFO_FILE_TYPE_LOCAL);
		} else if (analysisRun instanceof OmegaSNRRun) {
			buf.append(OmegaGUIConstants.INFO_FILE_TYPE_SNR_IMAGE);
			buf2.append(OmegaGUIConstants.INFO_FILE_TYPE_SNR_PLANE);
			buf3.append(OmegaGUIConstants.INFO_FILE_TYPE_SNR_ROI);
		} else {
			buf.append(OmegaGUIConstants.INFO_FILE_TYPE_REGULAR);
		}

		tmpBuf.append(newline);
		tmpBuf.append(newCommentIdent);
		tmpBuf.append(OmegaGUIConstants.INFO_NAME);
		tmpBuf.append(newDataSep);
		tmpBuf.append(analysisRun.getName());
		tmpBuf.append(newline);

		tmpBuf.append(newCommentIdent);
		tmpBuf.append(OmegaGUIConstants.INFO_OWNER);
		tmpBuf.append(newDataSep);
		tmpBuf.append(analysisRun.getExperimenter().getFirstName());
		tmpBuf.append(" ");
		tmpBuf.append(analysisRun.getExperimenter().getLastName());
		tmpBuf.append(newline);

		if (parents != null) {
			tmpBuf.append(newline);
			tmpBuf.append(newCommentIdent);
			tmpBuf.append(OmegaGUIConstants.INFO_PARENTS);
			tmpBuf.append(newline);
			int counter = 0;
			for (final String s : parents) {
				tmpBuf.append(newCommentIdent);
				tmpBuf.append("-");
				tmpBuf.append(String.valueOf(counter));
				tmpBuf.append(":");
				tmpBuf.append(s);
				tmpBuf.append(newline);
				counter++;
			}
		}

		tmpBuf.append(newline);
		final OmegaRunDefinition runDef = analysisRun.getAlgorithmSpec();

		tmpBuf.append(newCommentIdent);
		tmpBuf.append(OmegaGUIConstants.INFO_ALGO);
		tmpBuf.append(newDataSep);
		tmpBuf.append(runDef.getAlgorithmInfo().getName());
		tmpBuf.append(newDataSep);
		tmpBuf.append("[");
		tmpBuf.append(runDef.getAlgorithmInfo().getShortName());
		tmpBuf.append("]");
		tmpBuf.append(newline);

		tmpBuf.append(newCommentIdent);
		tmpBuf.append(OmegaGUIConstants.INFO_ALGO_AUTHOR);
		tmpBuf.append(newDataSep);
		tmpBuf.append(runDef.getAlgorithmInfo().getAuthors());
		tmpBuf.append(newline);

		tmpBuf.append(newCommentIdent);
		tmpBuf.append(OmegaGUIConstants.INFO_ALGO_VERSION);
		tmpBuf.append(newDataSep);
		tmpBuf.append(runDef.getAlgorithmInfo().getVersion());
		tmpBuf.append(newline);

		tmpBuf.append(newCommentIdent);
		tmpBuf.append(OmegaGUIConstants.INFO_ALGO_RELEASED);
		tmpBuf.append(newDataSep);
		String released = format.format(runDef.getAlgorithmInfo()
				.getPublicationData());
		released = released.replace("_", " ");
		tmpBuf.append(released);
		tmpBuf.append(newline);
		
		tmpBuf.append(newCommentIdent);
		tmpBuf.append(OmegaGUIConstants.INFO_ALGO_REF);
		tmpBuf.append(newDataSep);
		tmpBuf.append(runDef.getAlgorithmInfo().getReference());
		tmpBuf.append(newline);
		
		tmpBuf.append(newCommentIdent);
		tmpBuf.append(OmegaGUIConstants.INFO_ALGO_DESC);
		tmpBuf.append(newDataSep);
		tmpBuf.append(runDef.getAlgorithmInfo().getDescription());
		tmpBuf.append(newline);

		tmpBuf.append(newline);

		if (runDef.getParameters().size() > 0) {
			tmpBuf.append(newCommentIdent);
			tmpBuf.append(OmegaGUIConstants.INFO_PARAMS);
			for (final OmegaParameter param : runDef.getParameters()) {
				tmpBuf.append(newline);
				tmpBuf.append(newCommentIdent);
				tmpBuf.append("-");
				String paramName = param.getName();
				if (paramName
						.equals(OmegaAlgorithmParameterConstants.PARAM_ZSECTION)
						|| paramName
								.equals(OmegaAlgorithmParameterConstants.PARAM_CHANNEL)) {
					paramName = "Analyzed " + paramName;
				}
				tmpBuf.append(paramName);
				tmpBuf.append(newDataSep);
				tmpBuf.append(param.getStringValue());
				// tmpBuf.append(newline);
			}
			tmpBuf.append(newline);
			tmpBuf.append(newline);
		}

		String acquiredDate = format.format(analysisRun.getTimeStamps());
		acquiredDate = acquiredDate.replace("_", " ");
		tmpBuf.append(newCommentIdent);
		tmpBuf.append(OmegaGUIConstants.INFO_EXECUTED);
		tmpBuf.append(newDataSep);
		tmpBuf.append(acquiredDate);
		tmpBuf.append(newline);

		final int counter = OmegaAnalysisRunContainerUtilities
				.getAnalysisCount(analysisRun);
		tmpBuf.append(newCommentIdent);
		tmpBuf.append(OmegaGUIConstants.INFO_NUM_ANALYSIS);
		tmpBuf.append(newDataSep);
		tmpBuf.append(counter);
		tmpBuf.append(newline);

		tmpBuf.append(newline);

		this.addSpecificMetadata(analysisRun, tmpBuf, newDataSep,
				newCommentIdent, newline);

		buf.append(tmpBuf);
		buf2.append(tmpBuf);
		buf3.append(tmpBuf);

		final FileWriter fw1 = new FileWriter(f1, false);
		final BufferedWriter bw1 = new BufferedWriter(fw1);
		bw1.write(buf.toString());
		bw1.close();
		fw1.close();
		if (f2 == null)
			return;
		final FileWriter fw2 = new FileWriter(f2, false);
		final BufferedWriter bw2 = new BufferedWriter(fw2);
		bw2.write(buf2.toString());
		bw2.close();
		fw2.close();
		if (f3 == null)
			return;
		final FileWriter fw3 = new FileWriter(f3, false);
		final BufferedWriter bw3 = new BufferedWriter(fw3);
		bw3.write(buf3.toString());
		bw3.close();
		fw3.close();
	}
	
	private void addSpecificMetadata(final OmegaAnalysisRun analysisRun,
			final StringBuffer buf, final String newDataSep,
			final String newCommentIdent, final String newline) {
		if (analysisRun instanceof OmegaSNRRun) {
			
		} else if (analysisRun instanceof OmegaTrackingMeasuresDiffusivityRun) {
			buf.append(newCommentIdent);
			buf.append(OmegaGUIConstants.RESULTS_DIFFISIVITY_MIN_DET_ODC);
			buf.append(newDataSep);
			final Double ODC = ((OmegaTrackingMeasuresDiffusivityRun) analysisRun)
					.getMinimumDetectableODC();
			buf.append(String.valueOf(ODC));
			buf.append(newline);
		} else if (analysisRun instanceof OmegaTrackingMeasuresIntensityRun) {
			
		} else if (analysisRun instanceof OmegaTrackingMeasuresMobilityRun) {
			
		} else if (analysisRun instanceof OmegaTrackingMeasuresVelocityRun) {
			
		} else if (analysisRun instanceof OmegaTrajectoriesSegmentationRun) {
			final Map<OmegaTrajectory, List<OmegaSegment>> segments = ((OmegaTrajectoriesSegmentationRun) analysisRun)
					.getResultingSegments();
			buf.append(newCommentIdent);
			buf.append("Total number of trajectories");
			buf.append(newDataSep);
			final int tracksC = segments.keySet().size();
			final String tracks = String.valueOf(tracksC);
			buf.append(tracks);
			buf.append(newline);
			
			buf.append(newCommentIdent);
			buf.append("Total number of segments");
			buf.append(newDataSep);
			final Double segmentsN[] = new Double[tracksC];
			int i = 0;
			int segmentsT = 0;
			for (final OmegaTrajectory track : segments.keySet()) {
				final int segmN = segments.get(track).size();
				segmentsN[i] = (double) segmN;
				segmentsT += segmN;
				i++;
			}
			buf.append(String.valueOf(segmentsT));
			buf.append(newline);
			
			buf.append(newCommentIdent);
			buf.append("Average number of segments per trajectory");
			buf.append(newDataSep);
			final Double segmMean = OmegaMathsUtilities.mean(segmentsN);
			buf.append(String.valueOf(segmMean));
			buf.append(newline);
		} else if (analysisRun instanceof OmegaTrajectoriesRelinkingRun) {
			this.addTrajectoriesMetadata(analysisRun, buf, newCommentIdent,
					newDataSep, newline);
		} else if (analysisRun instanceof OmegaParticleLinkingRun) {
			this.addTrajectoriesMetadata(analysisRun, buf, newCommentIdent,
					newDataSep, newline);
		} else if (analysisRun instanceof OmegaParticleDetectionRun) {
			double numP = 0;
			int maxNumP = 0;
			int minNumP = 0;
			int f = 0;
			for (final OmegaPlane frame : ((OmegaParticleDetectionRun) analysisRun)
					.getResultingParticles().keySet()) {
				final int localNumP = ((OmegaParticleDetectionRun) analysisRun)
						.getResultingParticles().get(frame).size();
				if (maxNumP < localNumP) {
					maxNumP = localNumP;
				}
				if ((minNumP == 0) || (minNumP > localNumP)) {
					minNumP = localNumP;
				}
				numP += localNumP;
				f++;
			}
			numP /= f;
			
			buf.append(newCommentIdent);
			buf.append("Average number of spots found per time point");
			buf.append(newDataSep);
			final String mean = String.valueOf(numP);
			buf.append(mean);
			buf.append(newline);
			
			buf.append(newCommentIdent);
			buf.append("Max  number of spots found per time point");
			buf.append(newDataSep);
			final String max = String.valueOf(maxNumP);
			buf.append(max);
			buf.append(newline);
			
			buf.append(newCommentIdent);
			buf.append("Min  number of spots found per time point");
			buf.append(newDataSep);
			final String min = String.valueOf(minNumP);
			buf.append(min);
			buf.append(newline);
		}
		buf.append(newline);
		buf.append(newline);
	}
	
	private void addTrajectoriesMetadata(final OmegaAnalysisRun analysisRun,
			final StringBuffer buf, final String newCommentIdent,
			final String newDataSep, final String newline) {
		final List<OmegaTrajectory> tracks = ((OmegaParticleLinkingRun) analysisRun)
				.getResultingTrajectories();
		buf.append(newCommentIdent);
		buf.append("Total number of trajectories");
		buf.append(newDataSep);
		final String tracksV = String.valueOf(tracks.size());
		buf.append(tracksV);
		buf.append(newline);

		int maxTracksLength = 0;
		int minTracksLength = 0;
		double meanTracksLength = 0;
		for (final OmegaTrajectory track : tracks) {
			meanTracksLength += track.getLength();
			if (maxTracksLength < track.getLength()) {
				maxTracksLength = track.getLength();
			}
			if ((minTracksLength == 0) || (minTracksLength > track.getLength())) {
				minTracksLength = track.getLength();
			}
		}
		meanTracksLength /= tracks.size();

		buf.append(newCommentIdent);
		buf.append("Average trajectory length");
		buf.append(newDataSep);
		final String averagetl = String.valueOf(meanTracksLength);
		buf.append(averagetl);
		buf.append(newline);

		buf.append(newCommentIdent);
		buf.append("Max trajectory length");
		buf.append(newDataSep);
		final String maxtl = String.valueOf(maxTracksLength);
		buf.append(maxtl);
		buf.append(newline);

		buf.append(newCommentIdent);
		buf.append("Min trajectory length");
		buf.append(newDataSep);
		final String mintl = String.valueOf(minTracksLength);
		buf.append(mintl);
		buf.append(newline);
	}
	
	private void exportSNR(final String path, final String filename,
			final OmegaImage image, final OmegaSNRRun snrRun,
			final Map<OmegaPlane, List<OmegaROI>> particles,
			final Map<Integer, OmegaPlane> frames, final boolean multifile,
			final boolean createSubFolders, final String extension,
			final String newTargetIdent, final String newDataIdent,
			final boolean startAtOne, final String newCommentIdent,
			final String newDataSep, final File sourceFolder)
			throws IOException, IllegalArgumentException {
		boolean append = false;
		if (newTargetIdent != null) {
			append = true;
		}

		File targetDir = null;
		if (createSubFolders) {
			targetDir = new File(sourceFolder.getPath() + File.separatorChar
					+ path);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
		} else {
			targetDir = sourceFolder;
		}

		final Double imageAvgCenterSignal = snrRun
				.getResultingAverageCenterSignal();
		final Double imageAvgPeakSignal = snrRun
				.getResultingAveragePeakSignal();
		final Double imageAvgMeanSignal = snrRun
				.getResultingAverageMeanSignal();
		final Double imageBGR = snrRun.getResultingBackground();
		final Double imageNoise = snrRun.getResultingNoise();
		final Double imageAvgSNR = snrRun.getResultingAvgSNR();
		final Double imageMinSNR = snrRun.getResultingMinSNR();
		final Double imageMaxSNR = snrRun.getResultingMaxSNR();
		final Double imageAvgIndexSNR = snrRun.getResultingAvgErrorIndexSNR();
		final Double imageMinIndexSNR = snrRun.getResultingMinErrorIndexSNR();
		final Double imageMaxIndexSNR = snrRun.getResultingMaxErrorIndexSNR();

		final Map<OmegaPlane, Double> frameAvgCenterSignal = snrRun
				.getResultingImageAverageCenterSignal();
		final Map<OmegaPlane, Double> frameAvgPeakSignal = snrRun
				.getResultingImageAveragePeakSignal();
		final Map<OmegaPlane, Double> frameAvgMeanSignal = snrRun
				.getResultingImageAverageMeanSignal();
		final Map<OmegaPlane, Double> frameBGR = snrRun.getResultingImageBGR();
		final Map<OmegaPlane, Double> frameNoise = snrRun
				.getResultingImageNoise();
		final Map<OmegaPlane, Double> frameAvgSNR = snrRun
				.getResultingImageAverageSNR();
		final Map<OmegaPlane, Double> frameMinSNR = snrRun
				.getResultingImageMinimumSNR();
		final Map<OmegaPlane, Double> frameMaxSNR = snrRun
				.getResultingImageMaximumSNR();
		final Map<OmegaPlane, Double> frameAvgIndexSNR = snrRun
				.getResultingImageAverageErrorIndexSNR();
		final Map<OmegaPlane, Double> frameMinIndexSNR = snrRun
				.getResultingImageMinimumErrorIndexSNR();
		final Map<OmegaPlane, Double> frameMaxIndexSNR = snrRun
				.getResultingImageMaximumErrorIndexSNR();

		final Map<OmegaROI, Integer> localCenterSignal = snrRun
				.getResultingLocalCenterSignals();
		final Map<OmegaROI, Double> localMeanSignal = snrRun
				.getResultingLocalMeanSignals();
		final Map<OmegaROI, Double> localBackground = snrRun
				.getResultingLocalBackgrounds();
		final Map<OmegaROI, Integer> localPeakSignal = snrRun
				.getResultingLocalPeakSignals();
		final Map<OmegaROI, Double> localNoise = snrRun
				.getResultingLocalNoises();
		final Map<OmegaROI, Double> localSNR = snrRun.getResultingLocalSNRs();
		snrRun.getResultingLocalErrorIndexSNRs();
		final Map<OmegaROI, Integer> localArea = snrRun
				.getResultingLocalParticleArea();

		final File globalFile = new File(targetDir.getPath()
				+ File.separatorChar + filename + "_"
				+ OmegaGUIConstants.INFO_FILE_TYPE_SNR_IMAGE + "." + extension);
		if (!globalFile.exists()) {
			globalFile.createNewFile();
		}
		final FileWriter fw3 = new FileWriter(globalFile, append);
		final BufferedWriter bw3 = new BufferedWriter(fw3);

		final StringBuffer headerBuf = new StringBuffer();
		headerBuf.append(newCommentIdent);
		headerBuf.append(OmegaGUIConstants.RESULTS_PARTICLE_ID);
		headerBuf.append(newDataSep);
		headerBuf.append(OmegaGUIConstants.RESULTS_FRAME);
		headerBuf.append(newDataSep);
		headerBuf.append(OmegaGUIConstants.RESULTS_X);
		headerBuf.append(newDataSep);
		headerBuf.append(OmegaGUIConstants.RESULTS_Y);
		headerBuf.append(newDataSep);
		headerBuf.append(OmegaGUIConstants.RESULTS_Z);
		headerBuf.append(newDataSep);
		headerBuf.append(OmegaGUIConstants.RESULTS_C);
		headerBuf.append(newDataSep);
		headerBuf.append(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID);
		headerBuf.append(newDataSep);
		headerBuf.append(OmegaGUIConstants.RESULTS_INTENSITY_PEAK);
		headerBuf.append(newDataSep);
		headerBuf.append(OmegaGUIConstants.RESULTS_INTENSITY_MEAN);
		headerBuf.append(newDataSep);
		headerBuf.append(OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND);
		headerBuf.append(newDataSep);
		headerBuf.append(OmegaGUIConstants.RESULTS_INTENSITY_NOISE);
		headerBuf.append(newDataSep);
		headerBuf.append(OmegaGUIConstants.RESULTS_INTENSITY_SNR);
		headerBuf.append(newDataSep);
		headerBuf.append(OmegaGUIConstants.RESULTS_SNR_SNR_INDEX);
		headerBuf.append(newDataSep);
		headerBuf.append(OmegaGUIConstants.RESULTS_SNR_AREA);
		
		final StringBuffer headerBuf2 = new StringBuffer();
		headerBuf2.append(newCommentIdent);
		headerBuf2.append(OmegaGUIConstants.RESULTS_PLANE_ID);
		headerBuf2.append(newDataSep);
		headerBuf2.append(OmegaGUIConstants.RESULTS_PLANE_INDEX);
		headerBuf2.append(newDataSep);
		headerBuf2.append(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID_AVG);
		headerBuf2.append(newDataSep);
		headerBuf2.append(OmegaGUIConstants.RESULTS_INTENSITY_PEAK_AVG);
		headerBuf2.append(newDataSep);
		headerBuf2.append(OmegaGUIConstants.RESULTS_INTENSITY_MEAN_AVG);
		headerBuf2.append(newDataSep);
		headerBuf2.append(OmegaGUIConstants.RESULTS_SNR_BACKGROUND);
		headerBuf2.append(newDataSep);
		headerBuf2.append(OmegaGUIConstants.RESULTS_SNR_NOISE);
		headerBuf2.append(newDataSep);
		headerBuf2.append(OmegaGUIConstants.RESULTS_SNR_SNR_AVG);
		headerBuf2.append(newDataSep);
		headerBuf2.append(OmegaGUIConstants.RESULTS_SNR_SNR_MIN);
		headerBuf2.append(newDataSep);
		headerBuf2.append(OmegaGUIConstants.RESULTS_SNR_SNR_MAX);
		headerBuf2.append(newDataSep);
		headerBuf2.append(OmegaGUIConstants.RESULTS_SNR_SNR_INDEX_AVG);
		headerBuf2.append(newDataSep);
		headerBuf2.append(OmegaGUIConstants.RESULTS_SNR_SNR_INDEX_MIN);
		headerBuf2.append(newDataSep);
		headerBuf2.append(OmegaGUIConstants.RESULTS_SNR_SNR_INDEX_MAX);

		final StringBuffer headerBuf3 = new StringBuffer();
		headerBuf3.append(newCommentIdent);
		headerBuf3.append(OmegaGUIConstants.RESULTS_IMAGE_ID);
		headerBuf3.append(newDataSep);
		headerBuf3.append(OmegaGUIConstants.RESULTS_Z);
		headerBuf3.append(newDataSep);
		headerBuf3.append(OmegaGUIConstants.RESULTS_C);
		headerBuf3.append(newDataSep);
		headerBuf3.append(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID_AVG);
		headerBuf3.append(newDataSep);
		headerBuf3.append(OmegaGUIConstants.RESULTS_INTENSITY_PEAK_AVG);
		headerBuf3.append(newDataSep);
		headerBuf3.append(OmegaGUIConstants.RESULTS_INTENSITY_MEAN_AVG);
		headerBuf3.append(newDataSep);
		headerBuf3.append(OmegaGUIConstants.RESULTS_SNR_BACKGROUND);
		headerBuf3.append(newDataSep);
		headerBuf3.append(OmegaGUIConstants.RESULTS_SNR_NOISE);
		headerBuf3.append(newDataSep);
		headerBuf3.append(OmegaGUIConstants.RESULTS_SNR_SNR_AVG);
		headerBuf3.append(newDataSep);
		headerBuf3.append(OmegaGUIConstants.RESULTS_SNR_SNR_MIN);
		headerBuf3.append(newDataSep);
		headerBuf3.append(OmegaGUIConstants.RESULTS_SNR_SNR_MAX);
		headerBuf3.append(newDataSep);
		headerBuf3.append(OmegaGUIConstants.RESULTS_SNR_SNR_INDEX_AVG);
		headerBuf3.append(newDataSep);
		headerBuf3.append(OmegaGUIConstants.RESULTS_SNR_SNR_INDEX_MIN);
		headerBuf3.append(newDataSep);
		headerBuf3.append(OmegaGUIConstants.RESULTS_SNR_SNR_INDEX_MAX);

		headerBuf.append("\n");
		headerBuf2.append("\n");
		headerBuf3.append("\n");

		bw3.write(newCommentIdent);
		bw3.write("Image");
		bw3.write("\n");
		bw3.write(headerBuf3.toString());

		String imageID;
		if (image.getElementID() == -1) {
			imageID = OmegaGUIConstants.NOT_ASSIGNED;
		} else {
			imageID = String.valueOf(image.getElementID());
		}
		final Integer c = (Integer) snrRun.getAlgorithmSpec()
				.getParameter(OmegaAlgorithmParameterConstants.PARAM_CHANNEL)
				.getValue();
		final Integer z = (Integer) snrRun.getAlgorithmSpec()
				.getParameter(OmegaAlgorithmParameterConstants.PARAM_ZSECTION)
				.getValue();

		bw3.write(imageID);
		bw3.write(newDataSep);
		bw3.write(String.valueOf(c));
		bw3.write(newDataSep);
		bw3.write(String.valueOf(z));
		bw3.write(newDataSep);
		bw3.write(String.valueOf(imageAvgCenterSignal));
		bw3.write(newDataSep);
		bw3.write(String.valueOf(imageAvgPeakSignal));
		bw3.write(newDataSep);
		bw3.write(String.valueOf(imageAvgMeanSignal));
		bw3.write(newDataSep);
		bw3.write(String.valueOf(imageBGR));
		bw3.write(newDataSep);
		bw3.write(String.valueOf(imageNoise));
		bw3.write(newDataSep);
		bw3.write(String.valueOf(imageAvgSNR));
		bw3.write(newDataSep);
		bw3.write(String.valueOf(imageMinSNR));
		bw3.write(newDataSep);
		bw3.write(String.valueOf(imageMaxSNR));
		bw3.write(newDataSep);
		bw3.write(String.valueOf(imageAvgIndexSNR));
		bw3.write(newDataSep);
		bw3.write(String.valueOf(imageMinIndexSNR));
		bw3.write(newDataSep);
		bw3.write(String.valueOf(imageMaxIndexSNR));
		bw3.write("\n");

		bw3.close();
		fw3.close();

		int counter = 0;
		if (startAtOne) {
			counter += 1;
		}

		int max = particles.size() - 1;
		if (startAtOne) {
			max += 1;
		}
		while (counter <= max) {
			for (final OmegaPlane plane : particles.keySet()) {
				int index = plane.getIndex();
				if (startAtOne) {
					index++;
				}
				if (index != counter) {
					continue;
				}
				counter++;
				File localFile1, localFile2;
				if (multifile) {
					localFile1 = new File(targetDir.getPath()
							+ File.separatorChar + filename + "_Frame_" + index
							+ "_" + OmegaGUIConstants.INFO_FILE_TYPE_SNR_PLANE
							+ "." + extension);
					if (localFile1.exists())
						throw new IllegalArgumentException("The file "
								+ localFile1.getName() + " already exists in "
								+ targetDir);
					localFile2 = new File(targetDir.getPath()
							+ File.separatorChar + filename + "_Frame_" + index
							+ "_" + OmegaGUIConstants.INFO_FILE_TYPE_SNR_ROI
							+ "." + extension);
					if (localFile2.exists())
						throw new IllegalArgumentException("The file "
								+ localFile2.getName() + " already exists in "
								+ targetDir);
				} else {
					localFile1 = new File(targetDir.getPath()
							+ File.separatorChar + filename + "_"
							+ OmegaGUIConstants.INFO_FILE_TYPE_SNR_PLANE + "."
							+ extension);
					localFile2 = new File(targetDir.getPath()
							+ File.separatorChar + filename + "_"
							+ OmegaGUIConstants.INFO_FILE_TYPE_SNR_ROI + "."
							+ extension);
				}
				if (!localFile1.exists()) {
					localFile1.createNewFile();
				}
				if (!localFile2.exists()) {
					localFile1.createNewFile();
				}

				String planeID;
				if (plane.getElementID() == -1) {
					planeID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					planeID = String.valueOf(plane.getElementID());
				}

				final FileWriter fw = new FileWriter(localFile1, append);
				final BufferedWriter bw = new BufferedWriter(fw);
				final FileWriter fw2 = new FileWriter(localFile2, append);
				final BufferedWriter bw2 = new BufferedWriter(fw2);

				String planeIdent = newCommentIdent;
				if (newTargetIdent.equals("")) {
					planeIdent += "Frame" + newDataSep + "[" + planeID + "]"
							+ newDataSep + index + "\n";
				} else {
					planeIdent += newTargetIdent + newDataSep + "[" + planeID
							+ "]" + newDataSep + index + "\n";
				}
				bw.write(planeIdent);
				bw2.write(planeIdent);

				bw.write(headerBuf.toString());
				bw2.write(headerBuf2.toString());

				final Double fAvgCenterSignal = frameAvgCenterSignal.get(plane);
				final Double fAvgPeakSignal = frameAvgPeakSignal.get(plane);
				final Double fAvgMeanSignal = frameAvgMeanSignal.get(plane);
				final Double fBg = frameBGR.get(plane);
				final Double fNoise = frameNoise.get(plane);
				final Double fAvgSNR = frameAvgSNR.get(plane);
				final Double fMinSNR = frameMinSNR.get(plane);
				final Double fMaxSNR = frameMaxSNR.get(plane);
				final Double fAvgIndexSNR = frameAvgIndexSNR.get(plane);
				final Double fMinIndexSNR = frameMinIndexSNR.get(plane);
				final Double fMaxIndexSNR = frameMaxIndexSNR.get(plane);

				bw2.write(planeID);
				bw2.write(newDataSep);
				bw2.write(String.valueOf(index));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(fAvgCenterSignal));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(fAvgPeakSignal));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(fAvgMeanSignal));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(fBg));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(fNoise));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(fAvgSNR));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(fMinSNR));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(fMaxSNR));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(fAvgIndexSNR));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(fMinIndexSNR));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(fMaxIndexSNR));
				bw2.write("\n");
				bw2.write("\n");
				bw2.close();
				fw2.close();

				final List<OmegaROI> parts = particles.get(plane);
				for (final OmegaROI roi : parts) {
					// System.out.println("PLANE INDEX: " + index + "FI: "
					// + roi.getFrameIndex());
					this.exportParticle(bw, startAtOne, newDataIdent,
							newDataSep, roi, c, z, null);
					final Integer centerSignal = localCenterSignal.get(roi);
					final Integer peakSignal = localPeakSignal.get(roi);
					final Double meanSignal = localMeanSignal.get(roi);
					final Double background = localBackground.get(roi);
					final Double noise = localNoise.get(roi);
					final Double snr = localSNR.get(roi);
					final Double indexSNR = localSNR.get(roi);
					final Integer area = localArea.get(roi);
					bw.write(newDataSep);
					bw.write(String.valueOf(centerSignal));
					bw.write(newDataSep);
					bw.write(String.valueOf(peakSignal));
					bw.write(newDataSep);
					bw.write(String.valueOf(meanSignal));
					bw.write(newDataSep);
					bw.write(String.valueOf(background));
					bw.write(newDataSep);
					bw.write(String.valueOf(noise));
					bw.write(newDataSep);
					bw.write(String.valueOf(snr));
					bw.write(newDataSep);
					bw.write(String.valueOf(indexSNR));
					bw.write(newDataSep);
					bw.write(String.valueOf(area));
					bw.write(newDataSep);
					bw.write("\n");
				}
				bw.write("\n");
				bw.close();
				fw.close();
			}
		}
	}
	
	private void exportTrackingMeasuresDiffusivity(final String path,
			final String filename,
			final OmegaTrackingMeasuresDiffusivityRun diffRun,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final OmegaSegmentationTypes types, final boolean multifile,
			final boolean createSubFolders, final String extension,
			final String newTargetIdent, final String newDataIdent,
			final boolean startAtOne, final String newCommentIdent,
			final String newDataSep, final File sourceFolder)
			throws IOException, IllegalArgumentException {
		final Map<OmegaSegment, Double[]> nyMap = diffRun.getNyResults();
		final Map<OmegaSegment, Double[][]> deltaTMap = diffRun
				.getDeltaTResults();
		final Map<OmegaSegment, Double[][]> muMap = diffRun.getMuResults();
		final Map<OmegaSegment, Double[][]> logDeltaTMap = diffRun
				.getLogDeltaTResults();
		final Map<OmegaSegment, Double[][]> logMuMap = diffRun
				.getLogMuResults();
		diffRun.getGammaDResults();
		final Map<OmegaSegment, Double[][]> gammaDResultsMap = diffRun
				.getGammaDResults();
		final Map<OmegaSegment, Double[][]> gammaDFromLogResultsMap = diffRun
				.getGammaDFromLogResults();
		final Map<OmegaSegment, Double[]> smssFromLogResultsMap = diffRun
				.getSmssFromLogResults();
		final Map<OmegaSegment, Double[]> errosFromLogResultsMap = diffRun
				.getErrosFromLogResults();
		final String windowSize = diffRun
				.getAlgorithmSpec()
				.getParameter(
						OmegaAlgorithmParameterConstants.PARAM_DIFFUSIVITY_WINDOW)
				.getStringValue();
		boolean append = false;
		if (newTargetIdent != null) {
			append = true;
		}
		
		File targetDir = null;
		if (createSubFolders) {
			targetDir = new File(sourceFolder.getPath() + File.separatorChar
					+ path);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
		} else {
			targetDir = sourceFolder;
		}
		
		for (final OmegaTrajectory track : segments.keySet()) {
			final File localIntervalFile, localFile, globalFile;
			if (multifile) {
				localIntervalFile = new File(targetDir.getPath()
						+ File.separatorChar + filename + "_" + track.getName()
						+ "_" + OmegaGUIConstants.INFO_FILE_TYPE_DM_DDT + "."
						+ extension);
				if (localIntervalFile.exists())
					throw new IllegalArgumentException("The file "
							+ localIntervalFile.getName()
							+ " already exists in " + targetDir);
				localFile = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_" + track.getName() + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_DM_MSS + "."
						+ extension);
				if (localFile.exists())
					throw new IllegalArgumentException("The file "
							+ localFile.getName() + " already exists in "
							+ targetDir);
			} else {
				localIntervalFile = new File(targetDir.getPath()
						+ File.separatorChar + filename + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_DM_DDT + "."
						+ extension);
				localFile = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_DM_MSS + "."
						+ extension);
			}
			globalFile = new File(targetDir.getPath() + File.separatorChar
					+ filename + "_" + OmegaGUIConstants.INFO_FILE_TYPE_DM_PS
					+ "." + extension);
			if (!localFile.exists()) {
				localFile.createNewFile();
			}
			
			String trackID;
			if (track.getElementID() == -1) {
				trackID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				trackID = String.valueOf(track.getElementID());
			}
			
			final FileWriter fw = new FileWriter(localIntervalFile, append);
			final BufferedWriter bw = new BufferedWriter(fw);
			final FileWriter fw2 = new FileWriter(localFile, append);
			final BufferedWriter bw2 = new BufferedWriter(fw2);
			final FileWriter fw3 = new FileWriter(globalFile, true);
			final BufferedWriter bw3 = new BufferedWriter(fw3);
			
			String trackIdent = newCommentIdent;
			if (newTargetIdent.equals("")) {
				trackIdent += "Trajectory" + newDataSep + "[" + trackID + "]"
						+ newDataSep + track.getName() + "\n";
			} else {
				trackIdent += newTargetIdent + newDataSep + "[" + trackID + "]"
						+ newDataSep + track.getName() + "\n";
			}
			bw.write(trackIdent);
			bw2.write(trackIdent);
			bw3.write(trackIdent);
			
			final StringBuffer headerBuf = new StringBuffer();
			headerBuf.append(newCommentIdent);
			headerBuf.append(OmegaGUIConstants.RESULTS_TRACK_ID);
			headerBuf.append(newDataSep);
			headerBuf.append(OmegaGUIConstants.RESULTS_TRACK_NAME);
			headerBuf.append(newDataSep);
			headerBuf.append(OmegaGUIConstants.RESULTS_TRACK_LENGTH);
			headerBuf.append(newDataSep);
			headerBuf.append(OmegaGUIConstants.RESULTS_SEGM_ID);
			headerBuf.append(newDataSep);
			headerBuf.append(OmegaGUIConstants.RESULTS_SEGM_NAME);
			headerBuf.append(newDataSep);
			headerBuf.append(OmegaGUIConstants.RESULTS_SEGM_TYPE);
			headerBuf.append(newDataSep);
			headerBuf.append(OmegaGUIConstants.RESULTS_SEGM_LENGTH);
			headerBuf.append(newDataSep);
			headerBuf.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_WIN_SIZE);
			
			final StringBuffer headerBuf2 = new StringBuffer();
			headerBuf2.append(headerBuf);
			headerBuf2.append(newDataSep);
			headerBuf2
					.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_MOMENT_ORDER);
			if (gammaDResultsMap != null) {
				headerBuf2.append(newDataSep);
				headerBuf2.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_GAMMA);
				headerBuf2.append(newDataSep);
				headerBuf2.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_Y0);
				headerBuf2.append(newDataSep);
				headerBuf2.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_FIT);
				headerBuf2.append(newDataSep);
				headerBuf2.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_ODC);
			}
			if (gammaDFromLogResultsMap != null) {
				headerBuf2.append(newDataSep);
				headerBuf2
						.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_GAMMA_LOG);
				headerBuf2.append(newDataSep);
				headerBuf2.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_Y0_LOG);
				headerBuf2.append(newDataSep);
				headerBuf2
						.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_FIT_LOG);
				headerBuf2.append(newDataSep);
				headerBuf2
						.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_ODC_LOG);
			}
			
			final StringBuffer headerBuf3 = new StringBuffer();
			headerBuf3.append(headerBuf);
			headerBuf3.append(newDataSep);
			headerBuf3.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_MSD);
			headerBuf3.append(newDataSep);
			headerBuf3.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_Y02);
			headerBuf3.append(newDataSep);
			headerBuf3.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_ODC);
			headerBuf3.append(newDataSep);
			headerBuf3.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_SMSS);
			headerBuf3.append(newDataSep);
			headerBuf3.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_ODC_ERR);
			headerBuf3.append(newDataSep);
			headerBuf3.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_SMSS_ERR);

			headerBuf.append(newDataSep);
			headerBuf
					.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_MOMENT_ORDER);
			headerBuf.append(newDataSep);
			headerBuf.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_DELTA);
			headerBuf.append(newDataSep);
			headerBuf.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_MU);
			headerBuf.append(newDataSep);
			headerBuf.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_DELTA_LOG);
			headerBuf.append(newDataSep);
			headerBuf.append(OmegaGUIConstants.RESULTS_DIFFUSIVITY_MU_LOG);

			headerBuf.append("\n");
			headerBuf2.append("\n");
			headerBuf3.append("\n");
			
			for (final OmegaSegment segment : segments.get(track)) {
				String segmID;
				if (segment.getElementID() == -1) {
					segmID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					segmID = String.valueOf(segment.getElementID());
				}
				segment.getStartingROI();
				segment.getEndingROI();
				final int segmLength = OmegaTrajectoryUtilities
						.computeSegmentLength(track, segment);
				final String segmType = types.getSegmentationName(segment
						.getSegmentationType());
				
				String segmIdent = newCommentIdent;
				if (newTargetIdent.equals("")) {
					segmIdent += "Segment" + newDataSep + "[" + segmID + "]"
							+ newDataSep + segment.getName() + "\n";
				} else {
					segmIdent += newTargetIdent + newDataSep + "[" + segmID
							+ "]" + newDataSep + segment.getName() + "\n";
				}
				bw.write(segmIdent);
				bw2.write(segmIdent);
				
				bw.write(headerBuf.toString());
				bw2.write(headerBuf2.toString());
				bw3.write(headerBuf3.toString());
				
				final StringBuffer buf = new StringBuffer();
				buf.append(newDataIdent);
				buf.append(trackID);
				buf.append(newDataSep);
				buf.append(track.getName());
				buf.append(newDataSep);
				buf.append(track.getLength());
				buf.append(newDataSep);
				buf.append(segmID);
				buf.append(newDataSep);
				buf.append(segment.getName());
				buf.append(newDataSep);
				buf.append(segmType);
				buf.append(newDataSep);
				buf.append(segmLength);

				Double[][] gammaFromLog = null;
				if ((gammaDFromLogResultsMap != null)
						&& gammaDFromLogResultsMap.containsKey(segment)) {
					gammaFromLog = gammaDFromLogResultsMap.get(segment);
				}
				Double[] smssFromLog = null;
				if ((smssFromLogResultsMap != null)
						&& smssFromLogResultsMap.containsKey(segment)) {
					smssFromLog = smssFromLogResultsMap.get(segment);
				}
				Double[] errorFromLog = null;
				if ((errosFromLogResultsMap != null)
						&& errosFromLogResultsMap.containsKey(segment)) {
					errorFromLog = errosFromLogResultsMap.get(segment);
				}
				String dVal = OmegaGUIConstants.NOT_ASSIGNED;
				String smsdVal = OmegaGUIConstants.NOT_ASSIGNED;
				String intercept = OmegaGUIConstants.NOT_ASSIGNED;
				if ((gammaFromLog != null) && (gammaFromLog[2] != null)) {
					dVal = String.valueOf(gammaFromLog[2][3]);
					smsdVal = String.valueOf(gammaFromLog[2][0]);
					intercept = String.valueOf(gammaFromLog[2][1]);
				}
				String dErr = OmegaGUIConstants.NOT_ASSIGNED;
				if (errorFromLog != null) {
					dErr = String.valueOf(errorFromLog[0]);
				}
				String smssVal = OmegaGUIConstants.NOT_ASSIGNED;
				if (smssFromLog != null) {
					smssVal = String.valueOf(smssFromLog[0]);
				}
				String smssErr = OmegaGUIConstants.NOT_ASSIGNED;
				if (errorFromLog != null) {
					smssErr = String.valueOf(errorFromLog[1]);
				}
				
				bw3.write(buf.toString());
				bw3.write(newDataSep);
				bw3.write(windowSize);
				bw3.write(newDataSep);
				bw3.write(smsdVal);
				bw3.write(newDataSep);
				bw3.write(intercept);
				bw3.write(newDataSep);
				bw3.write(dVal);
				bw3.write(newDataSep);
				bw3.write(smssVal);
				bw3.write(newDataSep);
				bw3.write(dErr);
				bw3.write(newDataSep);
				bw3.write(smssErr);
				bw3.write("\n");
				
				Double[] nus = null;
				if ((nyMap != null) && nyMap.containsKey(segment)) {
					nus = nyMap.get(segment);
				}
				Double[][] gammaDsFromLog = null;
				if ((gammaDFromLogResultsMap != null)
						&& gammaDFromLogResultsMap.containsKey(segment)) {
					gammaDsFromLog = gammaDFromLogResultsMap.get(segment);
				}
				Double[][] gammaDs = null;
				if ((gammaDResultsMap != null)
						&& gammaDResultsMap.containsKey(segment)) {
					gammaDs = gammaDResultsMap.get(segment);
				}
				Double[][] logMus = null;
				if ((logMuMap != null) && logMuMap.containsKey(segment)) {
					logMus = logMuMap.get(segment);
				}
				Double[][] mus = null;
				if ((muMap != null) && muMap.containsKey(segment)) {
					mus = muMap.get(segment);
				}
				Double[][] logDeltaTs = null;
				if ((logDeltaTMap != null) && logDeltaTMap.containsKey(segment)) {
					logDeltaTs = logDeltaTMap.get(segment);
				}
				Double[][] deltaTs = null;
				if ((deltaTMap != null) && deltaTMap.containsKey(segment)) {
					deltaTs = deltaTMap.get(segment);
				}
				if (nus == null) {
					continue;
				}
				
				for (final Double nu : nus) {
					final Integer ny = new BigDecimal(nu).intValue();
					Double[] gammaDFromLog = null;
					int gammaLogLength = 0;
					if (gammaDsFromLog != null) {
						gammaDFromLog = gammaDsFromLog[ny];
						gammaLogLength = gammaDFromLog.length;
					}
					Double[] gammaD = null;
					int gammaLength = 0;
					if (gammaDs != null) {
						gammaD = gammaDs[ny];
						gammaLength = gammaD.length;
					}
					
					bw2.write(buf.toString());
					bw2.write(newDataSep);
					bw2.write(windowSize);
					bw2.write(newDataSep);
					bw2.write(String.valueOf(ny));
					for (int i = 0; i < gammaLength; i++) {
						String gammaVal = OmegaGUIConstants.NOT_ASSIGNED;
						if (gammaD != null) {
							final Double gamma = gammaD[i];
							if (Double.isNaN(gamma)) {
								gammaVal = String.valueOf(gamma);
								bw2.write(newDataSep);
								bw2.write(gammaVal);
							} else {
								gammaVal = String.valueOf(gamma);
								bw2.write(newDataSep);
								bw2.write(gammaVal);
							}
						}
					}
					for (int i = 0; i < gammaLogLength; i++) {
						String gammaVal = OmegaGUIConstants.NOT_ASSIGNED;
						if (gammaDFromLog != null) {
							final Double gamma = gammaDFromLog[i];
							if (Double.isNaN(gamma)) {
								gammaVal = String.valueOf(gamma);
								bw2.write(newDataSep);
								bw2.write(gammaVal);
							} else {
								gammaVal = String.valueOf(gamma);
								bw2.write(newDataSep);
								bw2.write(gammaVal);
							}
						}
					}
					bw2.write("\n");
					Double[] logMusNu = null;
					if (logMus != null) {
						logMusNu = logMus[ny];
					}
					Double[] musNu = null;
					if (mus != null) {
						musNu = mus[ny];
					}
					Double[] logDeltaTNu = null;
					if (logDeltaTs != null) {
						logDeltaTNu = logDeltaTs[ny];
					}
					Double[] deltaTNu = null;
					if (deltaTs != null) {
						deltaTNu = deltaTs[ny];
					}
					int l = 0;
					if (logMusNu != null) {
						l = logMusNu.length;
					} else if (musNu != null) {
						l = musNu.length;
					} else if (logDeltaTNu != null) {
						l = logDeltaTNu.length;
					} else if (deltaTNu != null) {
						l = deltaTNu.length;
					}
					for (int i = 0; i < l; i++) {
						String logMu = OmegaGUIConstants.NOT_ASSIGNED;
						if ((logMusNu != null) && (logMusNu[i] != null)) {
							logMu = String.valueOf(logMusNu[i]);
						}
						String mu = OmegaGUIConstants.NOT_ASSIGNED;
						if ((musNu != null) && (musNu[i] != null)) {
							mu = String.valueOf(musNu[i]);
						}
						String logDeltaT = OmegaGUIConstants.NOT_ASSIGNED;
						if ((logDeltaTNu != null) && (logDeltaTNu[i] != null)) {
							logDeltaT = String.valueOf(logDeltaTNu[i]);
						}
						String deltaT = OmegaGUIConstants.NOT_ASSIGNED;
						if ((deltaTNu != null) && (deltaTNu[i] != null)) {
							deltaT = String.valueOf(deltaTNu[i]);
						}
						bw.write(buf.toString());
						bw.write(newDataSep);
						bw.write(windowSize);
						bw.write(newDataSep);
						bw.write(String.valueOf(ny));
						bw.write(newDataSep);
						bw.write(String.valueOf(deltaT));
						bw.write(newDataSep);
						bw.write(String.valueOf(mu));
						bw.write(newDataSep);
						bw.write(String.valueOf(logDeltaT));
						bw.write(newDataSep);
						bw.write(String.valueOf(logMu));
						bw.write("\n");
					}
				}
			}
			bw.write("\n");
			bw2.write("\n");
			bw3.write("\n");
			bw.close();
			fw.close();
			bw2.close();
			fw2.close();
			bw3.close();
			fw3.close();
		}
	}
	
	private void exportTrackingMeasuresVelocity(final String path,
			final String filename,
			final OmegaTrackingMeasuresVelocityRun veloRun,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final OmegaSegmentationTypes types,
			final Map<Integer, OmegaPlane> frames, final boolean multifile,
			final boolean createSubFolders, final String extension,
			final String newTargetIdent, final String newDataIdent,
			final boolean startAtOne, final String newCommentIdent,
			final String newDataSep, final File sourceFolder)
			throws IOException, IllegalArgumentException {
		final Map<OmegaSegment, List<Double>> localSpeeds = veloRun
				.getLocalSpeedResults();
		final Map<OmegaSegment, List<Double>> localOriginSpeeds = veloRun
				.getLocalSpeedFromOriginResults();
		final Map<OmegaSegment, List<Double>> localOriginVelocities = veloRun
				.getLocalVelocityFromOriginResults();
		final Map<OmegaSegment, Double> globalSpeeds = veloRun
				.getAverageCurvilinearSpeedMapResults();
		final Map<OmegaSegment, Double> globalVelocities = veloRun
				.getAverageStraightLineVelocityMapResults();
		final Map<OmegaSegment, Double> forwardProgressions = veloRun
				.getForwardProgressionLinearityMapResults();
		boolean append = false;
		if (newTargetIdent != null) {
			append = true;
		}
		
		File targetDir = null;
		if (createSubFolders) {
			targetDir = new File(sourceFolder.getPath() + File.separatorChar
					+ path);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
		} else {
			targetDir = sourceFolder;
		}
		
		final int counter = 1;
		final int max = segments.keySet().size();
		final int maxDigits = String.valueOf(max).length();
		for (final OmegaTrajectory track : segments.keySet()) {
			final File localFile, globalFile;
			OmegaStringUtilities.getMultiDigitsCounter(counter, maxDigits);
			if (multifile) {
				localFile = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_" + track.getName() + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_LOCAL + "."
						+ extension);
				if (localFile.exists())
					throw new IllegalArgumentException("The file "
							+ localFile.getName() + " already exists in "
							+ targetDir);
			} else {
				localFile = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_LOCAL + "."
						+ extension);
			}
			globalFile = new File(targetDir.getPath() + File.separatorChar
					+ filename + "_" + OmegaGUIConstants.INFO_FILE_TYPE_GLOBAL
					+ "." + extension);
			if (!localFile.exists()) {
				localFile.createNewFile();
			}
			
			String trackID;
			if (track.getElementID() == -1) {
				trackID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				trackID = String.valueOf(track.getElementID());
			}
			
			final FileWriter fw = new FileWriter(localFile, append);
			final BufferedWriter bw = new BufferedWriter(fw);
			final FileWriter fw2 = new FileWriter(globalFile, true);
			final BufferedWriter bw2 = new BufferedWriter(fw2);
			
			String trackIdent = newCommentIdent;
			if (newTargetIdent.equals("")) {
				trackIdent += "Trajectory" + newDataSep + "[" + trackID + "]"
						+ newDataSep + track.getName() + "\n";
			} else {
				trackIdent += newTargetIdent + newDataSep + "[" + trackID + "]"
						+ newDataSep + track.getName() + "\n";
			}
			bw.write(trackIdent);
			bw2.write(trackIdent);
			
			for (final OmegaSegment segment : segments.get(track)) {
				String segmID;
				if (segment.getElementID() == -1) {
					segmID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					segmID = String.valueOf(segment.getElementID());
				}
				boolean isSegment = false;
				final OmegaROI start = segment.getStartingROI();
				final OmegaROI end = segment.getEndingROI();
				final int segmLength = OmegaTrajectoryUtilities
						.computeSegmentLength(track, segment);
				final String segmType = types.getSegmentationName(segment
						.getSegmentationType());
				
				String segmIdent = newCommentIdent;
				if (newTargetIdent.equals("")) {
					segmIdent += "Segment" + newDataSep + "[" + segmID + "]"
							+ newDataSep + segment.getName() + "\n";
				} else {
					segmIdent += newTargetIdent + newDataSep + "[" + segmID
							+ "]" + newDataSep + segment.getName() + "\n";
				}
				bw.write(segmIdent);
				bw2.write(segmIdent);

				final StringBuffer headerBuf = new StringBuffer();
				headerBuf.append(newCommentIdent);
				headerBuf.append(OmegaGUIConstants.RESULTS_PARTICLE_ID);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_FRAME);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_X);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_Y);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_Z);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_C);
				headerBuf.append(newDataSep);

				final StringBuffer tempBuf = new StringBuffer();
				// headerBuf2.append(newCommentIdent);
				tempBuf.append(OmegaGUIConstants.RESULTS_TRACK_ID);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_TRACK_NAME);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_TRACK_LENGTH);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_SEGM_ID);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_SEGM_NAME);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_SEGM_TYPE);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_SEGM_LENGTH);

				headerBuf.append(tempBuf);

				final StringBuffer headerBuf2 = new StringBuffer();
				headerBuf2.append(newCommentIdent);
				headerBuf2.append(tempBuf);

				headerBuf2.append(newDataSep);
				headerBuf2.append(OmegaGUIConstants.RESULTS_VELOCITY_AVG_SPEED);
				headerBuf2.append(newDataSep);
				headerBuf2.append(OmegaGUIConstants.RESULTS_VELOCITY_AVG_VELO);
				headerBuf2.append(newDataSep);
				headerBuf2.append(OmegaGUIConstants.RESULTS_VELOCITY_PROG);

				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_VELOCITY_SPEED);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_VELOCITY_CUM_SPEED);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_VELOCITY_CUM_VELO);

				headerBuf.append("\n");
				headerBuf2.append("\n");

				bw.write(headerBuf.toString());
				bw2.write(headerBuf2.toString());
				
				bw2.write(newDataIdent);
				
				final StringBuffer buf = new StringBuffer();
				buf.append(trackID);
				buf.append(newDataSep);
				buf.append(track.getName());
				buf.append(newDataSep);
				buf.append(track.getLength());
				buf.append(newDataSep);
				buf.append(segmID);
				buf.append(newDataSep);
				buf.append(segment.getName());
				buf.append(newDataSep);
				buf.append(segmType);
				buf.append(newDataSep);
				buf.append(segmLength);

				final Double gSpeed = globalSpeeds.get(segment);
				final Double gVelo = globalVelocities.get(segment);
				final Double forwardProgression = forwardProgressions
						.get(segment);
				
				bw2.write(buf.toString());
				bw2.write(newDataSep);
				bw2.write(String.valueOf(gSpeed));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(gVelo));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(forwardProgression));
				bw2.write("\n");
				
				final List<Double> speedsO = localOriginSpeeds.get(segment);
				final List<Double> velocitiesO = localOriginVelocities
						.get(segment);
				final List<Double> speeds = localSpeeds.get(segment);
				
				int i = 0;
				for (final OmegaROI roi : track.getROIs()) {
					speeds.get(i);
					final Double speed = speeds.get(i);
					final Double speedO = speedsO.get(i);
					final Double velocity = velocitiesO.get(i);
					i++;
					final OmegaPlane frame = frames
							.get(roi.getFrameIndex() - 1);
					boolean write = false;
					if (roi == start) {
						isSegment = true;
					} else if (roi == end) {
						isSegment = false;
						write = true;
					}
					if (isSegment) {
						write = true;
					}
					if (write) {
						this.exportParticle(bw, startAtOne, newDataIdent,
								newDataSep, roi, frame.getChannel(),
								frame.getZPlane(), null);
						bw.write(newDataSep);
						bw.write(buf.toString());
						bw.write(newDataSep);
						bw.write(String.valueOf(speed));
						bw.write(newDataSep);
						bw.write(String.valueOf(speedO));
						bw.write(newDataSep);
						bw.write(String.valueOf(velocity));
						bw.write("\n");
						if (!isSegment) {
							break;
						}
					}
				}
			}
			bw.write("\n");
			bw2.write("\n");
			bw.close();
			fw.close();
			bw2.close();
			fw2.close();
		}
	}
	
	private void exportTrackingMeasuresMobility(final String path,
			final String filename,
			final OmegaTrackingMeasuresMobilityRun mobiRun,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final OmegaSegmentationTypes types,
			final Map<Integer, OmegaPlane> frames, final boolean multifile,
			final boolean createSubFolders, final String extension,
			final String newTargetIdent, final String newDataIdent,
			final boolean startAtOne, final String newCommentIdent,
			final String newDataSep, final File sourceFolder)
			throws IOException, IllegalArgumentException {
		boolean append = false;
		if (newTargetIdent != null) {
			append = true;
		}
		
		File targetDir = null;
		if (createSubFolders) {
			targetDir = new File(sourceFolder.getPath() + File.separatorChar
					+ path);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
		} else {
			targetDir = sourceFolder;
		}
		
		final Map<OmegaSegment, List<Double>> localDistancesFromOrigin = mobiRun
				.getDistancesFromOriginResults();
		final Map<OmegaSegment, List<Double>> localDisplacementsFromOrigin = mobiRun
				.getDisplacementsFromOriginResults();
		final Map<OmegaSegment, List<Double>> localConfinementRatios = mobiRun
				.getConfinementRatioResults();
		final Map<OmegaSegment, List<Double[]>> localAnglesAndDirectionalChanges = mobiRun
				.getAnglesAndDirectionalChangesResults();
		final Map<OmegaSegment, List<Double>> timeTraveled = mobiRun
				.getTimeTraveledResults();
		final Map<OmegaSegment, Double> maxDisplacementes = mobiRun
				.getMaxDisplacementsFromOriginResults();
		final Map<OmegaSegment, List<Double>> localDistances = mobiRun
				.getDistancesResults();
		
		final int counter = 1;
		final int max = segments.keySet().size();
		final int maxDigits = String.valueOf(max).length();
		
		for (final OmegaTrajectory track : segments.keySet()) {
			final File localFile, globalFile;
			OmegaStringUtilities.getMultiDigitsCounter(counter, maxDigits);
			if (multifile) {
				localFile = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_" + track.getName() + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_LOCAL + "."
						+ extension);
				if (localFile.exists())
					throw new IllegalArgumentException("The file "
							+ localFile.getName() + " already exists in "
							+ targetDir);
			} else {
				localFile = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_LOCAL + "."
						+ extension);
			}
			globalFile = new File(targetDir.getPath() + File.separatorChar
					+ filename + "_" + OmegaGUIConstants.INFO_FILE_TYPE_GLOBAL
					+ "." + extension);
			if (!localFile.exists()) {
				localFile.createNewFile();
			}
			
			String trackID;
			if (track.getElementID() == -1) {
				trackID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				trackID = String.valueOf(track.getElementID());
			}
			
			final FileWriter fw = new FileWriter(localFile, append);
			final BufferedWriter bw = new BufferedWriter(fw);
			final FileWriter fw2 = new FileWriter(globalFile, true);
			final BufferedWriter bw2 = new BufferedWriter(fw2);
			
			String trackIdent = newCommentIdent;
			if (newTargetIdent.equals("")) {
				trackIdent += "Trajectory" + newDataSep + "[" + trackID + "]"
						+ newDataSep + track.getName() + "\n";
			} else {
				trackIdent += newTargetIdent + newDataSep + "[" + trackID + "]"
						+ newDataSep + track.getName() + "\n";
			}
			bw.write(trackIdent);
			bw2.write(trackIdent);
			
			for (final OmegaSegment segment : segments.get(track)) {
				String segmID;
				if (segment.getElementID() == -1) {
					segmID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					segmID = String.valueOf(segment.getElementID());
				}
				boolean isSegment = false;
				final OmegaROI start = segment.getStartingROI();
				final OmegaROI end = segment.getEndingROI();
				final int segmLength = OmegaTrajectoryUtilities
						.computeSegmentLength(track, segment);
				final String segmType = types.getSegmentationName(segment
						.getSegmentationType());

				String segmIdent = newCommentIdent;
				if (newTargetIdent.equals("")) {
					segmIdent += "Segment" + newDataSep + "[" + segmID + "]"
							+ newDataSep + segment.getName() + "\n";
				} else {
					segmIdent += newTargetIdent + newDataSep + "[" + segmID
							+ "]" + newDataSep + segment.getName() + "\n";
				}
				bw.write(segmIdent);
				bw2.write(segmIdent);

				final StringBuffer headerBuf = new StringBuffer();
				headerBuf.append(newCommentIdent);
				headerBuf.append(OmegaGUIConstants.RESULTS_PARTICLE_ID);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_FRAME);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_X);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_Y);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_Z);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_C);
				headerBuf.append(newDataSep);

				final StringBuffer tempBuf = new StringBuffer();
				tempBuf.append(OmegaGUIConstants.RESULTS_TRACK_ID);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_TRACK_NAME);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_TRACK_LENGTH);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_SEGM_ID);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_SEGM_NAME);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_SEGM_TYPE);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_SEGM_LENGTH);

				headerBuf.append(tempBuf);
				
				final StringBuffer headerBuf2 = new StringBuffer();
				headerBuf2.append(newCommentIdent);
				headerBuf2.append(tempBuf);

				headerBuf2.append(newDataSep);
				headerBuf2
						.append(OmegaGUIConstants.RESULTS_MOBILITY_TOT_CURV_DIST);
				headerBuf2.append(newDataSep);
				headerBuf2
						.append(OmegaGUIConstants.RESULTS_MOBILITY_MAX_STR_DIST);
				headerBuf2.append(newDataSep);
				headerBuf2
						.append(OmegaGUIConstants.RESULTS_MOBILITY_TOT_STR_DIST);
				headerBuf2.append(newDataSep);
				headerBuf2
						.append(OmegaGUIConstants.RESULTS_MOBILITY_CONF_RATIO);
				headerBuf2.append(newDataSep);
				headerBuf2.append(OmegaGUIConstants.RESULTS_MOBILITY_TOT_TIME);

				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_MOBILITY_DIST);
				headerBuf.append(newDataSep);
				headerBuf
						.append(OmegaGUIConstants.RESULTS_MOBILITY_CUM_CURV_DIST);
				headerBuf.append(newDataSep);
				headerBuf
						.append(OmegaGUIConstants.RESULTS_MOBILITY_CUM_STR_DIST);
				headerBuf.append(newDataSep);
				headerBuf
						.append(OmegaGUIConstants.RESULTS_MOBILITY_CUM_CONF_RATIO);
				headerBuf.append(newDataSep);
				headerBuf
						.append(OmegaGUIConstants.RESULTS_MOBILITY_CUM_INSTA_ANGLE);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_MOBILITY_DIR_CHANGE);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_MOBILITY_CUM_TIME);

				headerBuf.append("\n");
				headerBuf2.append("\n");

				bw.write(headerBuf.toString());
				bw2.write(headerBuf2.toString());
				
				final StringBuffer buf = new StringBuffer();
				buf.append(trackID);
				buf.append(newDataSep);
				buf.append(track.getName());
				buf.append(newDataSep);
				buf.append(track.getLength());
				buf.append(newDataSep);
				buf.append(segmID);
				buf.append(newDataSep);
				buf.append(segment.getName());
				buf.append(newDataSep);
				buf.append(segmType);
				buf.append(newDataSep);
				buf.append(segmLength);
				
				final List<Double> times = timeTraveled.get(segment);
				final List<Double> instDist = localDistances.get(segment);
				final List<Double> distances = localDistancesFromOrigin
						.get(segment);
				final List<Double> displacements = localDisplacementsFromOrigin
						.get(segment);
				final List<Double> confiments = localConfinementRatios
						.get(segment);
				final List<Double[]> anglesAndDirectionalChanges = localAnglesAndDirectionalChanges
						.get(segment);
				
				final List<OmegaROI> rois = track.getROIs();
				final int lastIndex = rois.get(rois.size() - 1).getFrameIndex() - 1;
				final Double dist = distances.get(lastIndex);
				final Double maxDisp = maxDisplacementes.get(segment);
				final Double disp = displacements.get(lastIndex);
				final Double confRatio = confiments.get(lastIndex);
				final Double time = times.get(lastIndex);
				
				bw2.write(newDataIdent);
				bw2.write(buf.toString());
				bw2.write(newDataSep);
				bw2.write(String.valueOf(dist));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(maxDisp));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(disp));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(confRatio));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(time));
				bw2.write("\n");
				
				int i = 0;
				for (final OmegaROI roi : rois) {
					boolean write = false;
					final OmegaPlane frame = frames
							.get(roi.getFrameIndex() - 1);
					if (roi == start) {
						isSegment = true;
					} else if (roi == end) {
						isSegment = false;
						write = true;
					}
					if (isSegment) {
						write = true;
					}
					final Double timeL = times.get(i);
					final Double idistL = instDist.get(i);
					final Double distL = distances.get(i);
					final Double displL = displacements.get(i);
					final Double confL = confiments.get(i);
					final Double[] anglesL = anglesAndDirectionalChanges.get(i);
					i++;
					if (write) {
						this.exportParticle(bw, startAtOne, newDataIdent,
								newDataSep, roi, frame.getChannel(),
								frame.getZPlane(), null);
						bw.write(newDataSep);
						bw.write(buf.toString());
						bw.write(newDataSep);
						bw.write(String.valueOf(idistL));
						bw.write(newDataSep);
						bw.write(String.valueOf(distL));
						bw.write(newDataSep);
						bw.write(String.valueOf(displL));
						bw.write(newDataSep);
						bw.write(String.valueOf(confL));
						bw.write(newDataSep);
						bw.write(String.valueOf(anglesL[0]));
						bw.write(newDataSep);
						bw.write(String.valueOf(anglesL[1]));
						bw.write(newDataSep);
						bw.write(String.valueOf(timeL));
						bw.write("\n");
						if (!isSegment) {
							break;
						}
					}
				}
			}
			bw.write("\n");
			bw2.write("\n");
			bw.close();
			fw.close();
			bw2.close();
			fw2.close();
		}
	}
	
	private void exportTrackingMeasuresIntensity(final String path,
			final String filename,
			final OmegaTrackingMeasuresIntensityRun inteRun,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final OmegaSegmentationTypes types,
			final Map<Integer, OmegaPlane> frames, final boolean multifile,
			final boolean createSubFolders, final String extension,
			final String newTargetIdent, final String newDataIdent,
			final boolean startAtOne, final String newCommentIdent,
			final String newDataSep, final File sourceFolder)
			throws IOException, IllegalArgumentException {
		boolean append = false;
		if (newTargetIdent != null) {
			append = true;
		}
		
		File targetDir = null;
		if (createSubFolders) {
			targetDir = new File(sourceFolder.getPath() + File.separatorChar
					+ path);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
		} else {
			targetDir = sourceFolder;
		}

		final Map<OmegaROI, Double> centroidLocalResults = inteRun
				.getCentroidSignalsLocalResults();
		final Map<OmegaROI, Double> peakLocalResults = inteRun
				.getPeakSignalsLocalResults();
		final Map<OmegaROI, Double> meanLocalResults = inteRun
				.getMeanSignalsLocalResults();
		final Map<OmegaROI, Double> bgLocalResults = inteRun
				.getBackgroundsLocalResults();
		final Map<OmegaROI, Double> noiseLocalResults = inteRun
				.getNoisesLocalResults();
		final Map<OmegaROI, Double> snrLocalResults = inteRun
				.getSNRsLocalResults();
		// inteRun.getAreasLocalResults();
		final Map<OmegaSegment, Double[]> centroidGlobalResults = inteRun
				.getCentroidSignalsResults();
		final Map<OmegaSegment, Double[]> peakGlobalResults = inteRun
				.getPeakSignalsResults();
		final Map<OmegaSegment, Double[]> meanGlobalResults = inteRun
				.getMeanSignalsResults();
		final Map<OmegaSegment, Double[]> bgGlobalResults = inteRun
				.getBackgroundsResults();
		final Map<OmegaSegment, Double[]> noiseGlobalResults = inteRun
				.getNoisesResults();
		final Map<OmegaSegment, Double[]> snrGlobalResults = inteRun
				.getSNRsResults();
		// inteRun.getAreasResults();

		for (final OmegaTrajectory track : segments.keySet()) {
			final File localFile, globalFile;
			if (multifile) {
				localFile = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_" + track.getName() + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_LOCAL + "."
						+ extension);
				if (localFile.exists())
					throw new IllegalArgumentException("The file "
							+ localFile.getName() + " already exists in "
							+ targetDir);
			} else {
				localFile = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_"
						+ OmegaGUIConstants.INFO_FILE_TYPE_LOCAL + "."
						+ extension);
			}
			globalFile = new File(targetDir.getPath() + File.separatorChar
					+ filename + "_" + OmegaGUIConstants.INFO_FILE_TYPE_GLOBAL
					+ "." + extension);
			if (!localFile.exists()) {
				localFile.createNewFile();
			}

			String trackID;
			if (track.getElementID() == -1) {
				trackID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				trackID = String.valueOf(track.getElementID());
			}

			final FileWriter fw = new FileWriter(localFile, append);
			final BufferedWriter bw = new BufferedWriter(fw);
			final FileWriter fw2 = new FileWriter(globalFile, true);
			final BufferedWriter bw2 = new BufferedWriter(fw2);
			
			String trackIdent = newCommentIdent;
			if (newTargetIdent.equals("")) {
				trackIdent += "Trajectory" + newDataSep + "[" + trackID + "]"
						+ newDataSep + track.getName() + "\n";
			} else {
				trackIdent += newTargetIdent + newDataSep + "[" + trackID + "]"
						+ newDataSep + track.getName() + "\n";
			}
			bw.write(trackIdent);
			bw2.write(trackIdent);

			for (final OmegaSegment segment : segments.get(track)) {
				String segmID;
				if (segment.getElementID() == -1) {
					segmID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					segmID = String.valueOf(segment.getElementID());
				}
				boolean isSegment = false;
				final OmegaROI start = segment.getStartingROI();
				final OmegaROI end = segment.getEndingROI();
				final int segmLength = OmegaTrajectoryUtilities
						.computeSegmentLength(track, segment);
				final String segmType = types.getSegmentationName(segment
						.getSegmentationType());
				String segmIdent = newCommentIdent;
				if (newTargetIdent.equals("")) {
					segmIdent += "Segment" + newDataSep + "[" + segmID + "]"
							+ newDataSep + segment.getName() + "\n";
				} else {
					segmIdent += newTargetIdent + newDataSep + "[" + segmID
							+ "]" + newDataSep + segment.getName() + "\n";
				}
				bw.write(segmIdent);
				bw2.write(segmIdent);
				
				final StringBuffer headerBuf = new StringBuffer();
				headerBuf.append(newCommentIdent);
				headerBuf.append(OmegaGUIConstants.RESULTS_PARTICLE_ID);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_FRAME);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_X);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_Y);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_Z);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_C);
				headerBuf.append(newDataSep);

				final StringBuffer tempBuf = new StringBuffer();
				tempBuf.append(OmegaGUIConstants.RESULTS_TRACK_ID);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_TRACK_NAME);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_TRACK_LENGTH);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_SEGM_ID);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_SEGM_NAME);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_SEGM_TYPE);
				tempBuf.append(newDataSep);
				tempBuf.append(OmegaGUIConstants.RESULTS_SEGM_LENGTH);

				headerBuf.append(tempBuf);

				final StringBuffer headerBuf2 = new StringBuffer();
				headerBuf2.append(newCommentIdent);
				headerBuf2.append(tempBuf);

				headerBuf2.append(newDataSep);
				headerBuf2
						.append(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID_AVG);
				headerBuf2.append(newDataSep);
				headerBuf2
						.append(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID_MIN);
				headerBuf2.append(newDataSep);
				headerBuf2
						.append(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID_MAX);
				headerBuf2.append(newDataSep);
				headerBuf2.append(OmegaGUIConstants.RESULTS_INTENSITY_PEAK_AVG);
				headerBuf2.append(newDataSep);
				headerBuf2.append(OmegaGUIConstants.RESULTS_INTENSITY_PEAK_MIN);
				headerBuf2.append(newDataSep);
				headerBuf2.append(OmegaGUIConstants.RESULTS_INTENSITY_PEAK_MAX);
				if (inteRun.getSNRRun() != null) {
					headerBuf2.append(newDataSep);
					headerBuf2
							.append(OmegaGUIConstants.RESULTS_INTENSITY_MEAN_AVG);
					headerBuf2.append(newDataSep);
					headerBuf2
							.append(OmegaGUIConstants.RESULTS_INTENSITY_MEAN_MIN);
					headerBuf2.append(newDataSep);
					headerBuf2
							.append(OmegaGUIConstants.RESULTS_INTENSITY_MEAN_MAX);
					headerBuf2.append(newDataSep);
					headerBuf2
							.append(OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND_AVG);
					headerBuf2.append(newDataSep);
					headerBuf2
							.append(OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND_MIN);
					headerBuf2.append(newDataSep);
					headerBuf2
							.append(OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND_MAX);
					headerBuf2.append(newDataSep);
					headerBuf2
							.append(OmegaGUIConstants.RESULTS_INTENSITY_NOISE_AVG);
					headerBuf2.append(newDataSep);
					headerBuf2
							.append(OmegaGUIConstants.RESULTS_INTENSITY_NOISE_MIN);
					headerBuf2.append(newDataSep);
					headerBuf2
							.append(OmegaGUIConstants.RESULTS_INTENSITY_NOISE_MAX);
					headerBuf2.append(newDataSep);
					headerBuf2
							.append(OmegaGUIConstants.RESULTS_INTENSITY_SNR_AVG);
					headerBuf2.append(newDataSep);
					headerBuf2
							.append(OmegaGUIConstants.RESULTS_INTENSITY_SNR_MIN);
					headerBuf2.append(newDataSep);
					headerBuf2
							.append(OmegaGUIConstants.RESULTS_INTENSITY_SNR_MAX);
				}

				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID);
				headerBuf.append(newDataSep);
				headerBuf.append(OmegaGUIConstants.RESULTS_INTENSITY_PEAK);
				if (inteRun.getSNRRun() != null) {
					headerBuf.append(newDataSep);
					headerBuf.append(OmegaGUIConstants.RESULTS_INTENSITY_MEAN);
					headerBuf.append(newDataSep);
					headerBuf
							.append(OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND);
					headerBuf.append(newDataSep);
					headerBuf.append(OmegaGUIConstants.RESULTS_INTENSITY_NOISE);
					headerBuf.append(newDataSep);
					headerBuf.append(OmegaGUIConstants.RESULTS_INTENSITY_SNR);
				}

				headerBuf.append("\n");
				headerBuf2.append("\n");

				bw.write(headerBuf.toString());
				bw2.write(headerBuf2.toString());

				final StringBuffer buf = new StringBuffer();
				buf.append(newDataIdent);
				buf.append(trackID);
				buf.append(newDataSep);
				buf.append(track.getName());
				buf.append(newDataSep);
				buf.append(track.getLength());
				buf.append(newDataSep);
				buf.append(segmID);
				buf.append(newDataSep);
				buf.append(segment.getName());
				buf.append(newDataSep);
				buf.append(segmType);
				buf.append(newDataSep);
				buf.append(segmLength);

				final Double[] centroids = centroidGlobalResults.get(segment);
				final Double[] peaks = peakGlobalResults.get(segment);
				
				bw2.write(buf.toString());
				bw2.write(newDataSep);
				bw2.write(String.valueOf(centroids[1]));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(centroids[0]));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(centroids[2]));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(peaks[1]));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(peaks[0]));
				bw2.write(newDataSep);
				bw2.write(String.valueOf(peaks[2]));

				if (inteRun.getSNRRun() != null) {
					final Double[] means = meanGlobalResults.get(segment);
					final Double[] backgrounds = bgGlobalResults.get(segment);
					final Double[] noises = noiseGlobalResults.get(segment);
					final Double[] snr = snrGlobalResults.get(segment);

					bw2.write(newDataSep);
					bw2.write(String.valueOf(means[1]));
					bw2.write(newDataSep);
					bw2.write(String.valueOf(means[0]));
					bw2.write(newDataSep);
					bw2.write(String.valueOf(means[2]));
					bw2.write(newDataSep);
					bw2.write(String.valueOf(backgrounds[1]));
					bw2.write(newDataSep);
					bw2.write(String.valueOf(backgrounds[0]));
					bw2.write(newDataSep);
					bw2.write(String.valueOf(backgrounds[2]));
					bw2.write(newDataSep);
					bw2.write(String.valueOf(noises[1]));
					bw2.write(newDataSep);
					bw2.write(String.valueOf(noises[0]));
					bw2.write(newDataSep);
					bw2.write(String.valueOf(noises[2]));
					bw2.write(newDataSep);
					bw2.write(String.valueOf(snr[1]));
					bw2.write(newDataSep);
					bw2.write(String.valueOf(snr[0]));
					bw2.write(newDataSep);
					bw2.write(String.valueOf(snr[2]));
				}
				bw2.write("\n");
				for (final OmegaROI roi : track.getROIs()) {
					final OmegaPlane frame = frames
							.get(roi.getFrameIndex() - 1);
					boolean write = false;
					if (roi == start) {
						isSegment = true;
						write = true;
					} else if (roi == end) {
						isSegment = false;
						write = true;
					}
					if (isSegment) {
						write = true;
					}
					final Double centroid = centroidLocalResults.get(roi);
					final Double peak = peakLocalResults.get(roi);
					if (write) {
						this.exportParticle(bw, startAtOne, newDataIdent,
								newDataSep, roi, frame.getChannel(),
								frame.getZPlane(), null);
						bw.write(newDataSep);
						bw.write(buf.toString());
						bw.write(newDataSep);
						bw.write(String.valueOf(centroid));
						bw.write(newDataSep);
						bw.write(String.valueOf(peak));
						if (inteRun.getSNRRun() != null) {
							final Double mean = meanLocalResults.get(roi);
							final Double background = bgLocalResults.get(roi);
							final Double noise = noiseLocalResults.get(roi);
							final Double snr = snrLocalResults.get(roi);
							bw.write(newDataSep);
							bw.write(String.valueOf(mean));
							bw.write(newDataSep);
							bw.write(String.valueOf(background));
							bw.write(newDataSep);
							bw.write(String.valueOf(noise));
							bw.write(newDataSep);
							bw.write(String.valueOf(snr));
						}
						bw.write("\n");
						if (!isSegment) {
							break;
						}
					}

				}
			}
			bw.write("\n");
			bw2.write("\n");
			bw.close();
			fw.close();
			bw2.close();
			fw2.close();
		}
	}
	
	private void exportSegments(final String path, final String filename,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final OmegaSegmentationTypes types,
			final Map<Integer, OmegaPlane> frames, final boolean multifile,
			final boolean createSubFolders, final String extension,
			final String newTargetIdent, final String newDataIdent,
			final boolean startAtOne, final String newCommentIdent,
			final String newDataSep, final File sourceFolder)
			throws IOException, IllegalArgumentException {
		boolean append = false;
		if (newTargetIdent != null) {
			append = true;
		}

		File targetDir = null;
		if (createSubFolders) {
			targetDir = new File(sourceFolder.getPath() + File.separatorChar
					+ path);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
		} else {
			targetDir = sourceFolder;
		}

		for (final OmegaTrajectory track : segments.keySet()) {
			String trackID;
			if (track.getElementID() == -1) {
				trackID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				trackID = String.valueOf(track.getElementID());
			}

			final File f;
			if (multifile) {
				f = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_" + track.getName() + "." + extension);
				if (f.exists())
					throw new IllegalArgumentException("The file "
							+ f.getName() + " already exists in " + targetDir);
			} else {
				f = new File(targetDir.getPath() + File.separatorChar
						+ filename + "." + extension);
			}
			if (!f.exists()) {
				f.createNewFile();
			}
			
			final FileWriter fw = new FileWriter(f, append);
			final BufferedWriter bw = new BufferedWriter(fw);
			
			for (final OmegaSegment segment : segments.get(track)) {
				String segmID;
				if (segment.getElementID() == -1) {
					segmID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					segmID = String.valueOf(segment.getElementID());
				}
				boolean isSegment = false;
				final OmegaROI start = segment.getStartingROI();
				final OmegaROI end = segment.getEndingROI();
				final int segmLength = OmegaTrajectoryUtilities
						.computeSegmentLength(track, segment);
				final String segmType = types.getSegmentationName(segment
						.getSegmentationType());
				String segmIdent = newCommentIdent;
				if (newTargetIdent.equals("")) {
					segmIdent += "Segment" + newDataSep + "[" + segmID + "]"
							+ newDataSep + segment.getName() + "\n";
				} else {
					segmIdent += newTargetIdent + newDataSep + "[" + segmID
							+ "]" + newDataSep + segment.getName() + "\n";
				}
				bw.write(segmIdent);

				bw.write(newCommentIdent);
				bw.write(OmegaGUIConstants.RESULTS_PARTICLE_ID);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_FRAME);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_X);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_Y);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_Z);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_C);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_TRACK_ID);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_TRACK_NAME);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_TRACK_LENGTH);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_SEGM_ID);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_SEGM_NAME);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_SEGM_TYPE);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_SEGM_LENGTH);
				bw.write("\n");
				
				for (final OmegaROI roi : track.getROIs()) {
					final OmegaPlane frame = frames
							.get(roi.getFrameIndex() - 1);
					boolean write = false;
					if (roi == start) {
						isSegment = true;
						write = true;
					} else if (roi == end) {
						isSegment = false;
						write = true;
					}
					if (write) {
						this.exportParticle(bw, startAtOne, newDataIdent,
								newDataSep, roi, frame.getChannel(),
								frame.getZPlane(), null);
						final StringBuffer buf = new StringBuffer();
						buf.append(newDataSep);
						buf.append(trackID);
						buf.append(newDataSep);
						buf.append(track.getName());
						buf.append(newDataSep);
						buf.append(track.getLength());
						buf.append(newDataSep);
						buf.append(segmID);
						buf.append(newDataSep);
						buf.append(segment.getName());
						buf.append(newDataSep);
						buf.append(segmType);
						buf.append(newDataSep);
						buf.append(segmLength);
						buf.append("\n");
						bw.write(buf.toString());
						if (!isSegment) {
							break;
						}
					}
				}
			}
			bw.write("\n");
			bw.close();
			fw.close();
		}
	}
	
	private void exportTrajectories(final String path, final String filename,
			final List<OmegaTrajectory> tracks,
			final Map<Integer, OmegaPlane> frames, final boolean multifile,
			final boolean createSubFolders, final String extension,
			final String newTargetIdent, final String newDataIdent,
			final boolean startAtOne, final String newCommentIdent,
			final String newDataSep, final File sourceFolder)
			throws IOException {
		boolean append = false;
		if (newTargetIdent != null) {
			append = true;
		}

		File targetDir = null;
		if (createSubFolders) {
			targetDir = new File(sourceFolder.getPath() + File.separatorChar
					+ path);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
		} else {
			targetDir = sourceFolder;
		}

		for (final OmegaTrajectory track : tracks) {
			final File f;
			if (multifile) {
				f = new File(targetDir.getPath() + File.separatorChar
						+ filename + "_" + track.getName() + "." + extension);
				if (f.exists())
					throw new IllegalArgumentException("The file "
							+ f.getName() + " already exists in " + targetDir);
			} else {
				f = new File(targetDir.getPath() + File.separatorChar
						+ filename + "." + extension);
			}
			if (!f.exists()) {
				f.createNewFile();
			}

			final FileWriter fw = new FileWriter(f, append);
			final BufferedWriter bw = new BufferedWriter(fw);
			String trackID;
			if (track.getElementID() == -1) {
				trackID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				trackID = String.valueOf(track.getElementID());
			}

			String trackIdent = newCommentIdent;
			if (newTargetIdent.equals("")) {
				trackIdent += "Trajectory" + newDataSep + "[" + trackID + "]"
						+ newDataSep + track.getName() + "\n";
			} else {
				trackIdent += newTargetIdent + newDataSep + "[" + trackID + "]"
						+ newDataSep + track.getName() + "\n";
			}
			bw.write(trackIdent);
			
			bw.write(newCommentIdent);
			bw.write(OmegaGUIConstants.RESULTS_PARTICLE_ID);
			bw.write(newDataSep);
			bw.write(OmegaGUIConstants.RESULTS_FRAME);
			bw.write(newDataSep);
			bw.write(OmegaGUIConstants.RESULTS_X);
			bw.write(newDataSep);
			bw.write(OmegaGUIConstants.RESULTS_Y);
			bw.write(newDataSep);
			bw.write(OmegaGUIConstants.RESULTS_Z);
			bw.write(newDataSep);
			bw.write(OmegaGUIConstants.RESULTS_C);
			bw.write(newDataSep);
			bw.write(OmegaGUIConstants.RESULTS_TRACK_ID);
			bw.write(newDataSep);
			bw.write(OmegaGUIConstants.RESULTS_TRACK_NAME);
			bw.write(newDataSep);
			bw.write(OmegaGUIConstants.RESULTS_TRACK_LENGTH);
			bw.write("\n");

			for (final OmegaROI roi : track.getROIs()) {
				final OmegaPlane frame = frames.get(roi.getFrameIndex() - 1);
				this.exportParticle(bw, startAtOne, newDataIdent, newDataSep,
						roi, frame.getChannel(), frame.getZPlane(), null);
				bw.write(newDataSep);
				bw.write(trackID);
				bw.write(newDataSep);
				bw.write(track.getName());
				bw.write(newDataSep);
				bw.write(String.valueOf(track.getLength()));
				bw.write("\n");
			}
			bw.write("\n");
			bw.close();
			fw.close();
		}
	}
	
	private void exportParticles(final String path, final String filename,
			final Map<OmegaPlane, List<OmegaROI>> particles,
			final Map<OmegaROI, Map<String, Object>> particleValues,
			final boolean multifile, final boolean createSubFolders,
			final String extension, final String newTargetIdent,
			final String newDataIdent, final boolean startAtOne,
			final String newCommentIdent, final String newDataSep,
			final File sourceFolder) throws IOException {
		boolean append = false;
		if (newTargetIdent != null) {
			append = true;
		}
		int counter = 0;
		if (startAtOne) {
			counter++;
		}
		int max = particles.size();
		if (startAtOne) {
			max++;
		}

		File targetDir = null;
		if (createSubFolders) {
			targetDir = new File(sourceFolder.getPath() + File.separatorChar
					+ path);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
		} else {
			targetDir = sourceFolder;
		}
		
		while (counter < max) {
			for (final OmegaPlane plane : particles.keySet()) {
				final int c = plane.getChannel();
				final int z = plane.getZPlane();
				int index = plane.getIndex();
				if (startAtOne) {
					index++;
				}
				if (index != counter) {
					continue;
				}
				final File f;
				if (multifile) {
					f = new File(targetDir.getPath() + File.separatorChar
							+ filename + "_Frame_" + index + "." + extension);
					if (f.exists()) {
						append = false;
					}
				} else {
					f = new File(targetDir.getPath() + File.separatorChar
							+ filename + "." + extension);
				}
				if (!f.exists()) {
					f.createNewFile();
				}
				
				final FileWriter fw = new FileWriter(f, append);
				final BufferedWriter bw = new BufferedWriter(fw);
				
				String planeID;
				if (plane.getElementID() == -1) {
					planeID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					planeID = String.valueOf(plane.getElementID());
				}
				
				final List<OmegaROI> rois = particles.get(plane);
				
				String planeIdent = newCommentIdent;
				if (newTargetIdent.equals("")) {
					planeIdent += "Frame" + newDataSep + "[" + planeID + "]"
							+ newDataSep + index + "\n";
				} else {
					planeIdent += newTargetIdent + newDataSep + "[" + planeID
							+ "]" + newDataSep + index + "\n";
				}
				bw.write(planeIdent);
				
				bw.write(newCommentIdent);
				bw.write(OmegaGUIConstants.RESULTS_PARTICLE_ID);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_FRAME);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_X);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_Y);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_Z);
				bw.write(newDataSep);
				bw.write(OmegaGUIConstants.RESULTS_C);
				for (final String key : particleValues.get(rois.get(0))
						.keySet()) {
					bw.write(newDataSep);
					bw.write(key);
				}
				bw.write("\n");
				
				for (final OmegaROI roi : rois) {
					this.exportParticle(bw, startAtOne, newDataIdent,
							newDataSep, roi, c, z, particleValues.get(roi));
					bw.write("\n");
				}
				
				bw.write("\n");
				bw.close();
				fw.close();
				counter++;
				break;
			}
		}
	}
	
	private void exportParticle(final BufferedWriter bw,
			final boolean startAtOne, final String newDataIdent,
			final String newDataSep, final OmegaROI roi, final int c,
			final int z, final Map<String, Object> particleValues)
			throws IOException {
		final StringBuffer buf = new StringBuffer();
		buf.append(newDataIdent);
		Integer frameIndex = roi.getFrameIndex();
		if (!startAtOne) {
			frameIndex--;
		}
		
		String roiID;
		if (roi.getElementID() == -1) {
			roiID = OmegaGUIConstants.NOT_ASSIGNED;
		} else {
			roiID = String.valueOf(roi.getElementID());
		}
		
		buf.append(roiID);
		buf.append(newDataSep);
		buf.append(String.valueOf(frameIndex));
		buf.append(newDataSep);
		buf.append(String.valueOf(roi.getX()));
		buf.append(newDataSep);
		buf.append(String.valueOf(roi.getY()));
		buf.append(newDataSep);
		buf.append(String.valueOf(c));
		buf.append(newDataSep);
		buf.append(String.valueOf(z));
		if (particleValues != null) {
			for (final String key : particleValues.keySet()) {
				buf.append(newDataSep);
				buf.append(String.valueOf(particleValues.get(key)));
			}
		}
		bw.write(buf.toString());
	}
	
	public void setImages(final Map<Integer, OmegaImage> images) {
		this.images = images;
	}
	
	public void setParticleDetectionRun(
			final Map<Integer, Map<Integer, OmegaParticleDetectionRun>> detRuns) {
		this.detRuns = detRuns;
	}
	
	public void setParticleLinkingRun(
			final Map<Integer, Map<Integer, OmegaParticleLinkingRun>> linkRuns) {
		this.linkRuns = linkRuns;
	}
	
	public void setTrackRelinkingRun(
			final Map<Integer, Map<Integer, OmegaTrajectoriesRelinkingRun>> relinkRuns) {
		this.relinkRuns = relinkRuns;
	}
	
	public void setTrackSegmentationRun(
			final Map<Integer, Map<Integer, OmegaTrajectoriesSegmentationRun>> segmRuns) {
		this.segmRuns = segmRuns;
	}
	
	public void setIntensityTrackingMeasuresRun(
			final Map<Integer, Map<Integer, OmegaTrackingMeasuresIntensityRun>> measuresRuns) {
		this.inteRuns = measuresRuns;
	}
	
	public void setVelocityTrackingMeasuresRun(
			final Map<Integer, Map<Integer, OmegaTrackingMeasuresVelocityRun>> measuresRuns) {
		this.veloRuns = measuresRuns;
	}
	
	public void setMobilityTrackingMeasuresRun(
			final Map<Integer, Map<Integer, OmegaTrackingMeasuresMobilityRun>> measuresRuns) {
		this.mobiRuns = measuresRuns;
	}
	
	public void setDiffusivityTrackingMeasuresRun(
			final Map<Integer, Map<Integer, OmegaTrackingMeasuresDiffusivityRun>> measuresRuns) {
		this.diffRuns = measuresRuns;
	}
	
	public void setSNRRun(final Map<Integer, Map<Integer, OmegaSNRRun>> snrRuns) {
		this.snrRuns = snrRuns;
	}
	
	public void resetAll() {
		this.detRuns = null;
		this.linkRuns = null;
		this.relinkRuns = null;
		this.segmRuns = null;
		this.inteRuns = null;
		this.veloRuns = null;
		this.mobiRuns = null;
		this.diffRuns = null;
	}
	
	public void setExportLastOnly() {
		this.exportLastOnly = true;
	}
	
	public void setAnalysisRunPaths(final Map<OmegaAnalysisRun, String> paths) {
		this.runPaths = paths;
	}
}
