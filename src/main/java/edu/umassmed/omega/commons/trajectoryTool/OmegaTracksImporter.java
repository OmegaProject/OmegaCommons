package edu.umassmed.omega.commons.trajectoryTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.RootPaneContainer;

import edu.umassmed.omega.commons.constants.OmegaAlgorithmParameterConstants;
import edu.umassmed.omega.commons.constants.OmegaGUIConstants;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainerInterface;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParticleDetectionRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParticleLinkingRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaSNRRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresDiffusivityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresIntensityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresMobilityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresVelocityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrajectoriesRelinkingRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrajectoriesSegmentationRun;
import edu.umassmed.omega.commons.data.coreElements.OmegaPlane;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaParticle;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegmentationTypes;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.eventSystem.events.OmegaImporterEventResultsGeneralParticleDetection;
import edu.umassmed.omega.commons.eventSystem.events.OmegaImporterEventResultsGeneralParticleTracking;
import edu.umassmed.omega.commons.eventSystem.events.OmegaImporterEventResultsOmegaData;
import edu.umassmed.omega.commons.eventSystem.events.OmegaImporterEventResultsOmegaParticleDetection;
import edu.umassmed.omega.commons.eventSystem.events.OmegaImporterEventResultsOmegaParticleLinking;
import edu.umassmed.omega.commons.eventSystem.events.OmegaImporterEventResultsOmegaSNR;
import edu.umassmed.omega.commons.eventSystem.events.OmegaImporterEventResultsOmegaTrackingMeasuresDiffusivity;
import edu.umassmed.omega.commons.eventSystem.events.OmegaImporterEventResultsOmegaTrackingMeasuresIntensity;
import edu.umassmed.omega.commons.eventSystem.events.OmegaImporterEventResultsOmegaTrackingMeasuresMobility;
import edu.umassmed.omega.commons.eventSystem.events.OmegaImporterEventResultsOmegaTrackingMeasuresVelocity;
import edu.umassmed.omega.commons.eventSystem.events.OmegaImporterEventResultsOmegaTrajectoriesRelinking;
import edu.umassmed.omega.commons.eventSystem.events.OmegaImporterEventResultsOmegaTrajectoriesSegmentation;
import edu.umassmed.omega.commons.trajectoryTool.gui.OmegaTracksToolDialog;
import edu.umassmed.omega.commons.utilities.OmegaIOUtility;
import edu.umassmed.omega.commons.utilities.OmegaStringUtilities;

// TODO just copied from the importer, needs to be review to export a list of
// tracks
public class OmegaTracksImporter extends OmegaIOUtility {
	
	private final Map<Integer, OmegaPlane> frames;
	private final Map<OmegaPlane, List<OmegaROI>> particles;
	private final Map<OmegaROI, Map<String, Object>> particlesValues;
	private final List<OmegaTrajectory> tracks;
	
	private final Map<Integer, String> parents;
	private final Map<String, String> analysisDataMap, paramMap;
	private boolean isReadingParams, isReadingParents;
	
	private OmegaTracksToolDialog dialog;
	
	private OmegaAnalysisRunContainerInterface container;
	
	private OmegaSegmentationTypes segmTypes;
	
	public static int IMPORTER_MODE_NOT_SET = -1;
	public static int IMPORTER_MODE_PARTICLES = 0;
	public static int IMPORTER_MODE_TRACKS = 1;
	public static int IMPORTER_MODE_OMEGA = 2;
	private int mode;
	
	private static List<String> dataNames = new ArrayList<String>();
	static {
		OmegaTracksImporter.dataNames.add(OmegaParticleDetectionRun
				.getStaticDisplayName());
		OmegaTracksImporter.dataNames.add(OmegaParticleLinkingRun
				.getStaticDisplayName());
		OmegaTracksImporter.dataNames.add(OmegaTrajectoriesRelinkingRun
				.getStaticDisplayName());
		OmegaTracksImporter.dataNames.add(OmegaTrajectoriesSegmentationRun
				.getStaticDisplayName());
		OmegaTracksImporter.dataNames.add(OmegaTrackingMeasuresIntensityRun
				.getStaticDisplayName());
		OmegaTracksImporter.dataNames.add(OmegaTrackingMeasuresVelocityRun
				.getStaticDisplayName());
		OmegaTracksImporter.dataNames.add(OmegaTrackingMeasuresMobilityRun
				.getStaticDisplayName());
		OmegaTracksImporter.dataNames.add(OmegaTrackingMeasuresDiffusivityRun
				.getStaticDisplayName());
		OmegaTracksImporter.dataNames.add(OmegaSNRRun.getStaticDisplayName());
	}
	
	public OmegaTracksImporter(final RootPaneContainer parent) {
		this.frames = new LinkedHashMap<Integer, OmegaPlane>();
		this.particles = new LinkedHashMap<OmegaPlane, List<OmegaROI>>();
		this.particlesValues = new LinkedHashMap<OmegaROI, Map<String, Object>>();
		this.tracks = new ArrayList<OmegaTrajectory>();
		
		this.dialog = new OmegaTracksToolDialog(parent, true, true, this);
		
		this.container = null;
		
		this.mode = OmegaTracksImporter.IMPORTER_MODE_NOT_SET;
		
		this.parents = new LinkedHashMap<Integer, String>();
		this.analysisDataMap = new LinkedHashMap<String, String>();
		this.paramMap = new LinkedHashMap<String, String>();
		this.isReadingParams = false;
		this.isReadingParents = false;
		
		this.segmTypes = null;
	}
	
	public OmegaTracksImporter() {
		this.frames = new LinkedHashMap<Integer, OmegaPlane>();
		this.particles = new LinkedHashMap<OmegaPlane, List<OmegaROI>>();
		this.particlesValues = new LinkedHashMap<OmegaROI, Map<String, Object>>();
		this.tracks = new ArrayList<OmegaTrajectory>();
		
		this.dialog = null;
		
		this.container = null;
		
		this.mode = OmegaTracksImporter.IMPORTER_MODE_NOT_SET;
		
		this.parents = new LinkedHashMap<Integer, String>();
		this.analysisDataMap = new LinkedHashMap<String, String>();
		this.paramMap = new LinkedHashMap<String, String>();
		this.isReadingParams = false;
		this.isReadingParents = false;
		
		this.segmTypes = null;
	}
	
	public void showDialog(final RootPaneContainer parent) {
		if (this.dialog == null) {
			this.dialog = new OmegaTracksToolDialog(parent, true, true, this);
		} else {
			this.dialog.resetDialog();
		}
		this.dialog.updateParentContainer(parent);
		this.dialog.setVisible(true);
	}
	
	public void setMode(final int mode) {
		if ((mode != OmegaTracksImporter.IMPORTER_MODE_PARTICLES)
				&& (mode != OmegaTracksImporter.IMPORTER_MODE_TRACKS)
				&& (mode != OmegaTracksImporter.IMPORTER_MODE_OMEGA))
			throw new IllegalArgumentException("Mode: " + mode
					+ " not a possible choice for "
					+ OmegaTracksImporter.class.getName());
		this.mode = mode;
	}
	
	public void importData(final boolean multifile, final String fileNameIdent,
			final boolean hasSubFolders, final String dataIdentifier,
			final String particleIdent, final boolean startAtOne,
			final String commentIdent, final String lineSep,
			final List<String> particleDataOrder, final File sourceFolder)
			throws IllegalArgumentException, IOException, ParseException {
		
		this.particles.clear();
		this.particlesValues.clear();
		this.tracks.clear();
		this.parents.clear();
		this.analysisDataMap.clear();
		this.paramMap.clear();
		this.isReadingParams = false;
		this.isReadingParents = false;
		
		String newTargetIdent = "";
		if (dataIdentifier != null) {
			newTargetIdent = dataIdentifier;
			newTargetIdent = newTargetIdent.replace("TAB", "\t");
		}
		String newPartIdent = "";
		if (particleIdent != null) {
			newPartIdent = particleIdent;
			newPartIdent = newPartIdent.replace("TAB", "\t");
		}
		String newLineSep = "\t";
		if (lineSep != null) {
			newLineSep = lineSep;
			newLineSep = newLineSep.replace("TAB", "\t");
		}
		String newCommentIdent = "";
		if (commentIdent != null) {
			newCommentIdent = commentIdent;
			newCommentIdent = newCommentIdent.replace("TAB", "\t");
		}
		
		if (this.mode == OmegaTracksImporter.IMPORTER_MODE_NOT_SET)
			throw new IllegalArgumentException(
					OmegaTracksImporter.class.getName()
							+ " mode not set, cannot import data");
		if (this.mode == OmegaTracksImporter.IMPORTER_MODE_PARTICLES) {
			this.importParticles(fileNameIdent, newTargetIdent, newPartIdent,
					startAtOne, newCommentIdent, newLineSep, particleDataOrder,
					sourceFolder);
		} else if (this.mode == OmegaTracksImporter.IMPORTER_MODE_TRACKS) {
			this.importTrajectories(multifile, fileNameIdent, newTargetIdent,
					newPartIdent, startAtOne, newCommentIdent, newLineSep,
					particleDataOrder, sourceFolder);
		} else {
			this.importData(multifile, fileNameIdent, hasSubFolders,
					newTargetIdent, newPartIdent, startAtOne, newCommentIdent,
					newLineSep, sourceFolder, this.segmTypes);
		}
	}
	
	private Map<String, List<File>> listAllFilesByType(final File sourceFolder,
			final String newCommentIdent, final String newLineSep)
			throws IOException {
		final Map<String, List<File>> files = new LinkedHashMap<String, List<File>>();
		for (final File f : sourceFolder.listFiles()) {
			if (!f.isFile()) {
				continue;
			}
			final String dynamicDisplayName = this.getDataType(f,
					newCommentIdent, newLineSep);
			List<File> filesList;
			if (files.containsKey(dynamicDisplayName)) {
				filesList = files.get(dynamicDisplayName);
			} else {
				filesList = new ArrayList<File>();
			}
			filesList.add(f);
			files.put(dynamicDisplayName, filesList);
		}
		for (final File f : sourceFolder.listFiles()) {
			if (f.isFile()) {
				continue;
			}
			files.putAll(this
					.listAllFilesByType(f, newCommentIdent, newLineSep));
		}
		return files;
	}
	
	private Map<File, String> listAllFiles(final File sourceFolder) {
		final Map<File, String> files = new LinkedHashMap<File, String>();
		for (final File f : sourceFolder.listFiles()) {
			if (!f.isFile()) {
				continue;
			}
			final String name = f.getName().substring(0,
					f.getName().lastIndexOf("."));
			files.put(f, name);
			
		}
		for (final File f : sourceFolder.listFiles()) {
			if (f.isFile()) {
				continue;
			}
			files.putAll(this.listAllFiles(f));
		}
		return files;
	}
	
	private void importData(final boolean multifile,
			final String fileNameIdent, final boolean hasSubFolders,
			final String newTargetIdent, final String newDataIdent,
			final boolean startAtOne, final String newCommentIdent,
			final String newLineSep, final File sourceFolder,
			final OmegaSegmentationTypes segmTypes) throws IOException,
			ParseException {
		if (!sourceFolder.isDirectory())
			throw new IllegalArgumentException("The destination folder: "
					+ sourceFolder + " has to be a valid directory");
		if (sourceFolder.listFiles().length == 0)
			throw new IllegalArgumentException("The destination folder: "
					+ sourceFolder + " has to be not empty");
		final Map<File, String> fileNames = this.listAllFiles(sourceFolder);
		final Map<String, List<File>> filesByType = this.listAllFilesByType(
				sourceFolder, newCommentIdent, newLineSep);
		final Map<String, OmegaImporterEventResultsOmegaData> events = new LinkedHashMap<String, OmegaImporterEventResultsOmegaData>();
		
		// TODO need to check which files are necessary
		for (final File f : filesByType.get(OmegaParticleDetectionRun
				.getStaticDisplayName())) {
			final List<File> files = new ArrayList<File>();
			files.add(f);
			final OmegaImporterEventResultsOmegaData evt = this.importAnalysis(
					files, OmegaParticleDetectionRun.getStaticDisplayName(),
					newDataIdent, newCommentIdent, newLineSep, events,
					segmTypes, startAtOne);
			events.put(fileNames.get(f), evt);
		}
		final Map<String, List<File>> snrFiles = this.getFilesGroup(
				filesByType.get(OmegaSNRRun.getStaticDisplayName()), fileNames);
		for (final String fileName : snrFiles.keySet()) {
			final List<File> files = snrFiles.get(fileName);
			final OmegaImporterEventResultsOmegaData evt = this.importAnalysis(
					files, OmegaSNRRun.getStaticDisplayName(), newDataIdent,
					newCommentIdent, newLineSep, events, segmTypes, startAtOne);
			events.put(fileName, evt);
		}
		for (final File f : filesByType.get(OmegaParticleLinkingRun
				.getStaticDisplayName())) {
			final List<File> files = new ArrayList<File>();
			files.add(f);
			final OmegaImporterEventResultsOmegaData evt = this.importAnalysis(
					files, OmegaParticleLinkingRun.getStaticDisplayName(),
					newDataIdent, newCommentIdent, newLineSep, events,
					segmTypes, startAtOne);
			events.put(fileNames.get(f), evt);
		}
		for (final File f : filesByType.get(OmegaTrajectoriesRelinkingRun
				.getStaticDisplayName())) {
			final List<File> files = new ArrayList<File>();
			files.add(f);
			final OmegaImporterEventResultsOmegaData evt = this.importAnalysis(
					files,
					OmegaTrajectoriesRelinkingRun.getStaticDisplayName(),
					newDataIdent, newCommentIdent, newLineSep, events,
					segmTypes, startAtOne);
			events.put(fileNames.get(f), evt);
		}
		for (final File f : filesByType.get(OmegaTrajectoriesSegmentationRun
				.getStaticDisplayName())) {
			final List<File> files = new ArrayList<File>();
			files.add(f);
			final OmegaImporterEventResultsOmegaData evt = this.importAnalysis(
					files,
					OmegaTrajectoriesSegmentationRun.getStaticDisplayName(),
					newDataIdent, newCommentIdent, newLineSep, events,
					segmTypes, startAtOne);
			events.put(fileNames.get(f), evt);
		}
		final Map<String, List<File>> inteFiles = this.getFilesGroup(
				filesByType.get(OmegaTrackingMeasuresIntensityRun
						.getStaticDisplayName()), fileNames);
		for (final String fileName : inteFiles.keySet()) {
			final List<File> files = inteFiles.get(fileName);
			final OmegaImporterEventResultsOmegaData evt = this.importAnalysis(
					files,
					OmegaTrackingMeasuresIntensityRun.getStaticDisplayName(),
					newDataIdent, newCommentIdent, newLineSep, events,
					segmTypes, startAtOne);
			events.put(fileName, evt);
		}
		final Map<String, List<File>> veloFiles = this.getFilesGroup(
				filesByType.get(OmegaTrackingMeasuresVelocityRun
						.getStaticDisplayName()), fileNames);
		for (final String fileName : veloFiles.keySet()) {
			final List<File> files = veloFiles.get(fileName);
			final OmegaImporterEventResultsOmegaData evt = this.importAnalysis(
					files,
					OmegaTrackingMeasuresVelocityRun.getStaticDisplayName(),
					newDataIdent, newCommentIdent, newLineSep, events,
					segmTypes, startAtOne);
			events.put(fileNames.get(fileName), evt);
		}
		final Map<String, List<File>> mobiFiles = this.getFilesGroup(
				filesByType.get(OmegaTrackingMeasuresMobilityRun
						.getStaticDisplayName()), fileNames);
		for (final String fileName : mobiFiles.keySet()) {
			final List<File> files = mobiFiles.get(fileName);
			final OmegaImporterEventResultsOmegaData evt = this.importAnalysis(
					files,
					OmegaTrackingMeasuresMobilityRun.getStaticDisplayName(),
					newDataIdent, newCommentIdent, newLineSep, events,
					segmTypes, startAtOne);
			events.put(fileName, evt);
		}
		final Map<String, List<File>> diffFiles = this.getFilesGroup(
				filesByType.get(OmegaTrackingMeasuresDiffusivityRun
						.getStaticDisplayName()), fileNames);
		for (final String fileName : diffFiles.keySet()) {
			final List<File> files = diffFiles.get(fileName);
			final OmegaImporterEventResultsOmegaData evt = this.importAnalysis(
					files,
					OmegaTrackingMeasuresDiffusivityRun.getStaticDisplayName(),
					newDataIdent, newCommentIdent, newLineSep, events,
					segmTypes, startAtOne);
			events.put(fileName, evt);
		}

		System.out.println(events);
		
		for (final String evtName : events.keySet()) {
			final OmegaImporterEventResultsOmegaData evt = events.get(evtName);
			this.fireEvent(evt);
		}

		// for (final File f1 : sourceFolder.listFiles()) {
		// isValid = true;
		// }
		// if (!isValid)
		// throw new IllegalArgumentException("The source folder: " +
		// sourceFolder +
		// " has to contain at least 1 file containing the given file name identifier");
	}
	
	private Map<String, List<File>> getFilesGroup(final List<File> fileList,
			final Map<File, String> fileNames) {
		final Map<String, List<File>> groupedFiles = new LinkedHashMap<String, List<File>>();
		for (final File f : fileList) {
			final String name = fileNames.get(f);
			final int index = name.lastIndexOf("_");
			final String originalName = name.substring(0, index);
			List<File> files;
			if (groupedFiles.containsKey(originalName)) {
				files = groupedFiles.get(originalName);
			} else {
				files = new ArrayList<File>();
			}
			files.add(f);
			groupedFiles.put(originalName, files);
		}
		return groupedFiles;
	}
	
	private String getDataType(final File f, final String newCommentIdent,
			final String newLineSep) throws IOException {
		final FileReader fr = new FileReader(f);
		final BufferedReader br = new BufferedReader(fr);
		boolean idFound = false;
		String dynamicDisplayName = null;
		String line = br.readLine();
		while (line != null) {
			if (line.isEmpty() || !line.startsWith(newCommentIdent)) {
				line = br.readLine();
				continue;
			}
			if (!idFound && line.contains(OmegaGUIConstants.INFO_ID)) {
				idFound = true;
				final int index = line.indexOf(OmegaGUIConstants.INFO_ID);
				dynamicDisplayName = line.substring(0, index - 1);
				dynamicDisplayName = dynamicDisplayName.replace(
						newCommentIdent, "");
				break;
			}
		}
		br.close();
		fr.close();
		return dynamicDisplayName;
	}

	private String getFileType(final File f, final String newCommentIdent,
			final String newLineSep) throws IOException {
		final FileReader fr = new FileReader(f);
		final BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		while (line != null) {
			if (line.isEmpty() || !line.startsWith(newCommentIdent)) {
				line = br.readLine();
				continue;
			}
			if (line.contains(OmegaGUIConstants.INFO_FILE_TYPE)) {
				String s = line.replace(newCommentIdent, "");
				s = s.replace(OmegaGUIConstants.INFO_FILE_TYPE, "");
				s = s.replace(newLineSep, "");
				br.close();
				fr.close();
				return s;
			}
			line = br.readLine();
		}
		br.close();
		fr.close();
		return null;
	}
	
	private OmegaImporterEventResultsOmegaData importAnalysis(
			final List<File> files, final String dynamicDisplayName,
			final String newDataIdent, final String newCommentIdent,
			final String newLineSep,
			final Map<String, OmegaImporterEventResultsOmegaData> events,
			final OmegaSegmentationTypes segmTypes, final boolean startAtOne)
			throws IOException, ParseException {
		
		final Map<String, String> analysisDataMap = new LinkedHashMap<String, String>();
		final Map<String, String> paramDataMap = new LinkedHashMap<String, String>();
		final Map<Integer, String> parents = new LinkedHashMap<Integer, String>();
		
		final List<Map<Integer, List<String[]>>> data = new ArrayList<Map<Integer, List<String[]>>>();
		final List<String> lineHeaders = new ArrayList<String>();
		for (final File f : files) {
			
			final String fileType = this.getFileType(f, newCommentIdent,
					newLineSep);
			final String typeIdentifier = this.findTypeIdentifier(fileType,
					dynamicDisplayName);
			
			final FileReader fr = new FileReader(f);
			final BufferedReader br = new BufferedReader(fr);
			boolean isDataLineNext = false;
			String lineHeader = null;
			String line = br.readLine();
			int counter = 0;
			final Map<Integer, List<String[]>> localData = new LinkedHashMap<Integer, List<String[]>>();
			while (line != null) {
				if (line.isEmpty()) {
					this.isReadingParams = false;
					this.isReadingParents = false;
					if (isDataLineNext) {
						counter++;
						isDataLineNext = false;
					}
					line = br.readLine();
					continue;
				}
				if (!isDataLineNext && line.startsWith(newCommentIdent)) {
					final String identifier1 = newCommentIdent + typeIdentifier
							+ newLineSep;
					final String identifier2 = newCommentIdent + typeIdentifier
							+ "\n";
					final String identifier3 = newCommentIdent + typeIdentifier;
					if (line.startsWith(identifier1)
							|| line.equals(identifier2)
							|| line.equals(identifier3)) {
						isDataLineNext = true;
					}
					if (!isDataLineNext) {
						this.importAnalysisData(analysisDataMap, paramDataMap,
								parents, newCommentIdent, newLineSep, line);
					}
				} else if (isDataLineNext) {
					if (line.contains(newCommentIdent)) {
						if (lineHeader == null) {
							lineHeader = line.replaceFirst(newCommentIdent, "");
						}
						line = br.readLine();
						continue;
					}
					if (!newDataIdent.isEmpty()
							&& !line.startsWith(newDataIdent)) {
						continue;
					}
					final String[] tokens = line.split(newLineSep);
					List<String[]> dataList;
					if (localData.containsKey(counter)) {
						dataList = localData.get(counter);
					} else {
						dataList = new ArrayList<String[]>();
					}
					dataList.add(tokens);
					localData.put(counter, dataList);
				}
				line = br.readLine();
			}
			br.close();
			fr.close();
			data.add(localData);
			lineHeaders.add(lineHeader);
		}
		
		if (dynamicDisplayName.equals(OmegaTrackingMeasuresIntensityRun
				.getStaticDisplayName())) {
			final String detectionName = parents
					.get(OmegaDataToolConstants.PARENT_DETECTION);
			final OmegaImporterEventResultsOmegaParticleDetection particleDetEvt = (OmegaImporterEventResultsOmegaParticleDetection) events
					.get(detectionName);
			final String segmName = parents
					.get(OmegaDataToolConstants.PARENT_SEGMENTATION);
			final OmegaImporterEventResultsOmegaTrajectoriesSegmentation segmEvt = (OmegaImporterEventResultsOmegaTrajectoriesSegmentation) events
					.get(segmName);
			return this.importTrackingMeasuresIntensityAnalysis(lineHeaders,
					data, newLineSep, analysisDataMap, paramDataMap, parents,
					particleDetEvt.getResultingParticles(),
					segmEvt.getResultingSegments(), startAtOne);
		} else if (dynamicDisplayName.equals(OmegaTrackingMeasuresVelocityRun
				.getStaticDisplayName())) {
			final String segmName = parents
					.get(OmegaDataToolConstants.PARENT_SEGMENTATION);
			final OmegaImporterEventResultsOmegaTrajectoriesSegmentation segmEvt = (OmegaImporterEventResultsOmegaTrajectoriesSegmentation) events
					.get(segmName);
			return this.importTrackingMeasuresVelocityAnalysis(lineHeaders,
					data, newLineSep, analysisDataMap, paramDataMap, parents,
					segmEvt.getResultingSegments());
		} else if (dynamicDisplayName.equals(OmegaTrackingMeasuresMobilityRun
				.getStaticDisplayName())) {
			final String segmName = parents
					.get(OmegaDataToolConstants.PARENT_SEGMENTATION);
			final OmegaImporterEventResultsOmegaTrajectoriesSegmentation segmEvt = (OmegaImporterEventResultsOmegaTrajectoriesSegmentation) events
					.get(segmName);
			return this.importTrackingMeasuresMobilityAnalysis(lineHeaders,
					data, newLineSep, analysisDataMap, paramDataMap, parents,
					segmEvt.getResultingSegments());
		} else if (dynamicDisplayName
				.equals(OmegaTrackingMeasuresDiffusivityRun
						.getStaticDisplayName())) {
			final String segmName = parents
					.get(OmegaDataToolConstants.PARENT_SEGMENTATION);
			final OmegaImporterEventResultsOmegaTrajectoriesSegmentation segmEvt = (OmegaImporterEventResultsOmegaTrajectoriesSegmentation) events
					.get(segmName);
			return this.importTrackingMeasuresDiffusivityAnalysis(lineHeaders,
					data, newLineSep, analysisDataMap, paramDataMap, parents,
					segmEvt.getResultingSegments());
		} else if (dynamicDisplayName.equals(OmegaTrajectoriesSegmentationRun
				.getStaticDisplayName())) {
			final String relinkingName = parents
					.get(OmegaDataToolConstants.PARENT_RELINKING);
			final OmegaImporterEventResultsOmegaTrajectoriesRelinking relinkingEvt = (OmegaImporterEventResultsOmegaTrajectoriesRelinking) events
					.get(relinkingName);
			return this.importTrajectoriesSegmentationAnalysis(
					lineHeaders.get(0), data.get(0), newLineSep,
					analysisDataMap, paramDataMap, parents,
					relinkingEvt.getResultingTrajectories(), segmTypes,
					startAtOne);
		} else if (dynamicDisplayName.equals(OmegaTrajectoriesRelinkingRun
				.getStaticDisplayName())) {
			final String detectionName = parents
					.get(OmegaDataToolConstants.PARENT_DETECTION);
			final OmegaImporterEventResultsOmegaParticleDetection particleDetEvt = (OmegaImporterEventResultsOmegaParticleDetection) events
					.get(detectionName);
			return this
					.importTrajectoriesRelinkingAnalysis(lineHeaders.get(0),
							data.get(0), newLineSep, analysisDataMap,
							paramDataMap, parents,
							particleDetEvt.getResultingParticles(), startAtOne);
		} else if (dynamicDisplayName.equals(OmegaParticleLinkingRun
				.getStaticDisplayName())) {
			final String detectionName = parents
					.get(OmegaDataToolConstants.PARENT_DETECTION);
			final OmegaImporterEventResultsOmegaParticleDetection particleDetEvt = (OmegaImporterEventResultsOmegaParticleDetection) events
					.get(detectionName);
			return this
					.importParticleLinkingAnalysis(lineHeaders.get(0),
							data.get(0), newLineSep, analysisDataMap,
							paramDataMap, parents,
							particleDetEvt.getResultingParticles(), startAtOne);
		} else if (dynamicDisplayName
				.equals(OmegaSNRRun.getStaticDisplayName())) {
			final String detectionName = parents
					.get(OmegaDataToolConstants.PARENT_DETECTION);
			final OmegaImporterEventResultsOmegaParticleDetection particleDetEvt = (OmegaImporterEventResultsOmegaParticleDetection) events
					.get(detectionName);
			return this.importSNRAnalysis(lineHeaders, data, newLineSep,
					analysisDataMap, paramDataMap, parents,
					particleDetEvt.getResultingParticles(), startAtOne);
		} else if (dynamicDisplayName.equals(OmegaParticleDetectionRun
				.getStaticDisplayName()))
			return this.importParticleDetectionAnalysis(lineHeaders.get(0),
					data.get(0), newLineSep, analysisDataMap, paramDataMap,
					parents, startAtOne);
		return null;
	}

	private OmegaImporterEventResultsOmegaTrackingMeasuresDiffusivity importTrackingMeasuresDiffusivityAnalysis(
			final List<String> lineHeaders,
			final List<Map<Integer, List<String[]>>> dataList,
			final String newLineSep, final Map<String, String> analysisDataMap,
			final Map<String, String> paramDataMap,
			final Map<Integer, String> parents,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		
		Double minDetectableODC = null;
		if (paramDataMap
				.containsKey(OmegaGUIConstants.RESULTS_DIFFISIVITY_MIN_DET_ODC)) {
			final String minDetODCS = paramDataMap
					.get(OmegaGUIConstants.RESULTS_DIFFISIVITY_MIN_DET_ODC);
			minDetectableODC = Double.valueOf(minDetODCS);
		}

		final Map<OmegaSegment, Double[]> gammaLog = new LinkedHashMap<OmegaSegment, Double[]>();
		final Map<OmegaSegment, Double[]> smssLog = new LinkedHashMap<OmegaSegment, Double[]>();
		final Map<OmegaSegment, Double[]> errorsLog = new LinkedHashMap<OmegaSegment, Double[]>();

		final Map<OmegaSegment, List<Double>> nyList = new LinkedHashMap<OmegaSegment, List<Double>>();
		final Map<OmegaSegment, Map<Double, List<Double>>> muList = new LinkedHashMap<OmegaSegment, Map<Double, List<Double>>>();
		final Map<OmegaSegment, Map<Double, List<Double>>> logMuList = new LinkedHashMap<OmegaSegment, Map<Double, List<Double>>>();
		final Map<OmegaSegment, Map<Double, List<Double>>> deltaTList = new LinkedHashMap<OmegaSegment, Map<Double, List<Double>>>();
		final Map<OmegaSegment, Map<Double, List<Double>>> logDeltaTList = new LinkedHashMap<OmegaSegment, Map<Double, List<Double>>>();
		final Map<OmegaSegment, Map<Double, List<Double>>> gammaDList = new LinkedHashMap<OmegaSegment, Map<Double, List<Double>>>();
		final Map<OmegaSegment, Map<Double, List<Double>>> gammaDLogList = new LinkedHashMap<OmegaSegment, Map<Double, List<Double>>>();

		for (int k = 0; k < lineHeaders.size(); k++) {
			final String[] headers = lineHeaders.get(k).split(newLineSep);
			final Map<Integer, List<String[]>> data = dataList.get(k);
			for (final int counter : data.keySet()) {
				OmegaTrajectory track = null;
				OmegaSegment segm = null;
				String trackName = null, segmName = null;
				for (final String[] entry : data.get(counter)) {
					Long trackID = null, segmID = null;
					// final Double[][] gammaFromLog = new
					// Double[OmegaDiffusivityLibrary.MAX_NU + 1][4];
					final Double[] smssFromLog = new Double[1];
					final Double[] errorFromLog = new Double[2];
					Double nu = null;
					Double muLoc = null, logMuLoc = null, deltaTLoc = null, logDeltaTLoc = null;
					Double gammaLoc1 = null, gammaLoc2 = null, gammaLoc3 = null, gammaLoc4 = null;
					Double gammaLogLoc1 = null, gammaLogLoc2 = null, gammaLogLoc3 = null, gammaLogLoc4 = null;
					for (int i = 0; i < headers.length; i++) {
						final String header = headers[i];
						final String val = entry[i];
						if (header.equals(OmegaGUIConstants.RESULTS_TRACK_ID)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								trackID = Long.valueOf(val);
							} else {
								trackID = -1L;
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SEGM_ID)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								segmID = Long.valueOf(val);
							} else {
								segmID = -1L;
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_TRACK_NAME)) {
							trackName = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SEGM_NAME)) {
							segmName = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SEGM_TYPE)) {
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_GAMMA)) {
							gammaLoc1 = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_Y0)) {
							gammaLoc2 = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_FIT)) {
							gammaLoc3 = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_MSD)) {
							// if (val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
							// gammaFromLog[2][0] = null;
							// } else {
							// gammaFromLog[2][0] = Double.valueOf(val);
							// }
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_Y02)) {
							// if (val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
							// gammaFromLog[2][1] = null;
							// } else {
							// gammaFromLog[2][1] = Double.valueOf(val);
							// }
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_ODC)) {
							// if (val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
							// gammaFromLog[2][3] = null;
							// } else {
							// gammaFromLog[2][3] = Double.valueOf(val);
							// }
							gammaLoc4 = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_GAMMA_LOG)) {
							gammaLogLoc1 = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_Y0_LOG)) {
							gammaLogLoc2 = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_FIT_LOG)) {
							gammaLogLoc3 = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_ODC_LOG)) {
							gammaLogLoc4 = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_SMSS)) {
							if (val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								smssFromLog[0] = null;
							} else {
								smssFromLog[0] = Double.valueOf(val);
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_ODC_ERR)) {
							if (val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								errorFromLog[0] = null;
							} else {
								errorFromLog[0] = Double.valueOf(val);
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_SMSS_ERR)) {
							if (val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								errorFromLog[1] = null;
							} else {
								errorFromLog[1] = Double.valueOf(val);
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_MOMENT_ORDER)) {
							nu = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_DELTA)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								deltaTLoc = Double.valueOf(val);
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_MU)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								muLoc = Double.valueOf(val);
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_DELTA_LOG)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								logDeltaTLoc = Double.valueOf(val);
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_DIFFUSIVITY_MU_LOG)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								logMuLoc = Double.valueOf(val);
							}
						}
					}
					if ((track == null) && (trackID != null)) {
						for (final OmegaTrajectory traj : segments.keySet()) {
							if (trackID != -1L) {
								if (trackID == traj.getElementID()) {
									track = traj;
									break;
								}
							} else {
								if (trackName.equals(traj.getName())) {
									track = traj;
									break;
								}
							}
						}
					}
					if (trackID != null) {
						final List<OmegaSegment> segmentList = segments
								.get(track);
						for (final OmegaSegment segment : segmentList) {
							if (segmID != -1L) {
								if (segmID == segment.getElementID()) {
									segm = segment;
									break;
								}
							} else {
								if (segmName.equals(segment.getName())) {
									segm = segment;
									break;
								}
							}
						}
					}
					// TODo check for ny and add to mu / deltaT map based on
					// that
					if (smssFromLog != null) {
						smssLog.put(segm, smssFromLog);
					}
					if (errorFromLog != null) {
						errorsLog.put(segm, errorFromLog);
					}
					// if (gammaFromLog != null) {
					// gammaDLog.put(segm, gammaFromLog);
					// }
					if (nu != null) {
						List<Double> nyListLocal;
						if (nyList.containsKey(segm)) {
							nyListLocal = nyList.get(segm);
						} else {
							nyListLocal = new ArrayList<Double>();
						}
						if (!nyListLocal.contains(nu)) {
							nyListLocal.add(nu);
						}
						nyList.put(segm, nyListLocal);
						
						if ((gammaLoc1 != null) && (gammaLoc2 != null)
								&& (gammaLoc3 != null) && (gammaLoc4 != null)) {
							final List<Double> gammaD = new ArrayList<Double>();
							gammaD.add(gammaLoc1);
							gammaD.add(gammaLoc2);
							gammaD.add(gammaLoc3);
							gammaD.add(gammaLoc4);
							Map<Double, List<Double>> gammaListLocal;
							if (gammaDList.containsKey(segm)) {
								gammaListLocal = gammaDList.get(segm);
							} else {
								gammaListLocal = new LinkedHashMap<Double, List<Double>>();
							}
							gammaListLocal.put(nu, gammaD);
							gammaDList.put(segm, gammaListLocal);
						}
						
						if ((gammaLogLoc1 != null) && (gammaLogLoc2 != null)
								&& (gammaLogLoc3 != null)
								&& (gammaLogLoc4 != null)) {
							final List<Double> gammaDLog = new ArrayList<Double>();
							gammaDLog.add(gammaLogLoc1);
							gammaDLog.add(gammaLogLoc2);
							gammaDLog.add(gammaLogLoc3);
							gammaDLog.add(gammaLogLoc4);
							Map<Double, List<Double>> gammaLogListLocal;
							if (gammaDLogList.containsKey(segm)) {
								gammaLogListLocal = gammaDLogList.get(segm);
							} else {
								gammaLogListLocal = new LinkedHashMap<Double, List<Double>>();
							}
							gammaLogListLocal.put(nu, gammaDLog);
							gammaDLogList.put(segm, gammaLogListLocal);
						}
						
						if (muLoc != null) {
							Map<Double, List<Double>> muListLocal;
							if (muList.containsKey(segm)) {
								muListLocal = muList.get(segm);
							} else {
								muListLocal = new LinkedHashMap<Double, List<Double>>();
							}
							List<Double> muListLoc;
							if (muListLocal.containsKey(nu)) {
								muListLoc = muListLocal.get(nu);
							} else {
								muListLoc = new ArrayList<Double>();
							}
							muListLoc.add(muLoc);
							muListLocal.put(nu, muListLoc);
							muList.put(segm, muListLocal);
						}
						if (logMuLoc != null) {
							Map<Double, List<Double>> logMuListLocal;
							if (logMuList.containsKey(segm)) {
								logMuListLocal = logMuList.get(segm);
							} else {
								logMuListLocal = new LinkedHashMap<Double, List<Double>>();
							}
							List<Double> logMuListLoc;
							if (logMuListLocal.containsKey(nu)) {
								logMuListLoc = logMuListLocal.get(nu);
							} else {
								logMuListLoc = new ArrayList<Double>();
							}
							logMuListLoc.add(logMuLoc);
							logMuListLocal.put(nu, logMuListLoc);
							logMuList.put(segm, logMuListLocal);
						}
						if (deltaTLoc != null) {
							Map<Double, List<Double>> deltaTListLocal;
							if (deltaTList.containsKey(segm)) {
								deltaTListLocal = deltaTList.get(segm);
							} else {
								deltaTListLocal = new LinkedHashMap<Double, List<Double>>();
							}
							List<Double> deltaTListLoc;
							if (deltaTListLocal.containsKey(nu)) {
								deltaTListLoc = deltaTListLocal.get(nu);
							} else {
								deltaTListLoc = new ArrayList<Double>();
							}
							deltaTListLoc.add(deltaTLoc);
							deltaTListLocal.put(nu, deltaTListLoc);
							deltaTList.put(segm, deltaTListLocal);
						}
						if (logDeltaTLoc != null) {
							Map<Double, List<Double>> logDeltaTListLocal;
							if (logDeltaTList.containsKey(segm)) {
								logDeltaTListLocal = logDeltaTList.get(segm);
							} else {
								logDeltaTListLocal = new LinkedHashMap<Double, List<Double>>();
							}
							List<Double> logDeltaTListLoc;
							if (logDeltaTListLocal.containsKey(nu)) {
								logDeltaTListLoc = logDeltaTListLocal.get(nu);
							} else {
								logDeltaTListLoc = new ArrayList<Double>();
							}
							logDeltaTListLoc.add(logDeltaTLoc);
							logDeltaTListLocal.put(nu, logDeltaTListLoc);
							logDeltaTList.put(segm, logDeltaTListLocal);
						}
					}
				}
			}
		}
		
		final Map<OmegaSegment, Double[]> ny = this.transformMap(nyList);
		Map<OmegaSegment, Double[][]> mu = null;
		if (!muList.isEmpty()) {
			mu = this.transformDoubleMap(muList, true);
		}
		Map<OmegaSegment, Double[][]> logMu = null;
		if (!logMuList.isEmpty()) {
			logMu = this.transformDoubleMap(logMuList, true);
		}
		Map<OmegaSegment, Double[][]> deltaT = null;
		if (!deltaTList.isEmpty()) {
			deltaT = this.transformDoubleMap(deltaTList, true);
		}
		Map<OmegaSegment, Double[][]> logDeltaT = null;
		if (!logDeltaTList.isEmpty()) {
			logDeltaT = this.transformDoubleMap(logDeltaTList, true);
		}
		Map<OmegaSegment, Double[][]> gammaD = null;
		if (!gammaDList.isEmpty()) {
			gammaD = this.transformDoubleMap(gammaDList, true);
		}
		Map<OmegaSegment, Double[][]> gammaDLog = null;
		if (!gammaDLogList.isEmpty()) {
			gammaDLog = this.transformDoubleMap(gammaDLogList, true);
		}

		final OmegaImporterEventResultsOmegaTrackingMeasuresDiffusivity evt = new OmegaImporterEventResultsOmegaTrackingMeasuresDiffusivity(
				this, this.container, parents, analysisDataMap, paramDataMap,
				segments, ny, mu, logMu, deltaT, logDeltaT, gammaD, gammaDLog,
				gammaLog, smssLog, errorsLog, minDetectableODC,
				OmegaDataToolConstants.COMPLETE_CHAIN_AFTER_IMPORT);
		return evt;
	}
	
	private OmegaImporterEventResultsOmegaTrackingMeasuresMobility importTrackingMeasuresMobilityAnalysis(
			final List<String> lineHeaders,
			final List<Map<Integer, List<String[]>>> dataList,
			final String newLineSep, final Map<String, String> analysisDataMap,
			final Map<String, String> paramDataMap,
			final Map<Integer, String> parents,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		
		final Map<OmegaSegment, List<Double>> localDistancesFromOrigin = new LinkedHashMap<OmegaSegment, List<Double>>();
		final Map<OmegaSegment, List<Double>> localDisplacementsFromOrigin = new LinkedHashMap<OmegaSegment, List<Double>>();
		final Map<OmegaSegment, List<Double>> localConfinementRatios = new LinkedHashMap<OmegaSegment, List<Double>>();
		final Map<OmegaSegment, List<Double[]>> localAnglesAndDirectionalChanges = new LinkedHashMap<OmegaSegment, List<Double[]>>();
		final Map<OmegaSegment, List<Double>> timeTraveled = new LinkedHashMap<OmegaSegment, List<Double>>();
		final Map<OmegaSegment, Double> maxDisplacementes = new LinkedHashMap<OmegaSegment, Double>();
		final Map<OmegaSegment, List<Double>> localDistances = new LinkedHashMap<OmegaSegment, List<Double>>();
		
		for (int k = 0; k < lineHeaders.size(); k++) {
			final String[] headers = lineHeaders.get(k).split(newLineSep);
			final Map<Integer, List<String[]>> data = dataList.get(k);
			for (final int counter : data.keySet()) {
				OmegaTrajectory track = null;
				OmegaSegment segm = null;
				String trackName = null, segmName = null;
				for (final String[] entry : data.get(counter)) {
					String maxDisp = null, localDistanceFromOrigin = null, localDisplacementFromOrigin = null, localConfinementRatio = null, timeTraveledLoc = null, localDistance = null;
					final String[] angleChange = new String[] { null, null };
					Long trackID = null, segmID = null;
					for (int i = 0; i < headers.length; i++) {
						final String header = headers[i];
						final String val = entry[i];
						if (header.equals(OmegaGUIConstants.RESULTS_TRACK_ID)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								trackID = Long.valueOf(val);
							} else {
								trackID = -1L;
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SEGM_ID)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								segmID = Long.valueOf(val);
							} else {
								segmID = -1L;
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_TRACK_NAME)) {
							trackName = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SEGM_NAME)) {
							segmName = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SEGM_TYPE)) {
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_MOBILITY_TOT_CURV_DIST)) {
							// localDisplacementFromOrigin.add(Double.valueOf(val));
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_MOBILITY_MAX_STR_DIST)) {
							maxDisp = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_MOBILITY_TOT_STR_DIST)) {
							// localDistanceFromOrigin.add(Double.valueOf(val));
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_MOBILITY_CONF_RATIO)) {
							// localConfinementRatio.add(Double.valueOf(val));
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_MOBILITY_TOT_TIME)) {
							// timeTraveledLoc.add(Double.valueOf(val));
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_MOBILITY_DIST)) {
							localDistance = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_MOBILITY_CUM_CURV_DIST)) {
							localDistanceFromOrigin = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_MOBILITY_CUM_STR_DIST)) {
							localDisplacementFromOrigin = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_MOBILITY_CUM_CONF_RATIO)) {
							localConfinementRatio = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_MOBILITY_CUM_INSTA_ANGLE)) {
							angleChange[0] = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_MOBILITY_DIR_CHANGE)) {
							angleChange[1] = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_MOBILITY_CUM_TIME)) {
							timeTraveledLoc = val;
						}
					}
					if ((track == null) && (trackID != null)) {
						for (final OmegaTrajectory traj : segments.keySet()) {
							if (trackID != -1L) {
								if (trackID == traj.getElementID()) {
									track = traj;
									break;
								}
							} else {
								if (trackName.equals(traj.getName())) {
									track = traj;
									break;
								}
							}
						}
					}
					if (trackID != null) {
						final List<OmegaSegment> segmentList = segments
								.get(track);
						for (final OmegaSegment segment : segmentList) {
							if (segmID != -1L) {
								if (segmID == segment.getElementID()) {
									segm = segment;
									break;
								}
							} else {
								if (segmName.equals(segment.getName())) {
									segm = segment;
									break;
								}
							}
						}
					}
					if (segmID != null) {
						Double val = null;
						if (maxDisp != null) {
							if (!maxDisp.equals(OmegaGUIConstants.NOT_ASSIGNED)
									&& !maxDisp.equals(OmegaGUIConstants.NULL)) {
								val = Double.valueOf(maxDisp);
							}
							maxDisplacementes.put(segm, val);
						}
						if (localDistance != null) {
							val = null;
							if (!localDistance
									.equals(OmegaGUIConstants.NOT_ASSIGNED)
									&& !localDistance
											.equals(OmegaGUIConstants.NULL)) {
								val = Double.valueOf(localDistance);
							}
							this.addValueInMap(localDistances, segm, val);
						}
						if (localDistanceFromOrigin != null) {
							val = null;
							if (!localDistanceFromOrigin
									.equals(OmegaGUIConstants.NOT_ASSIGNED)
									&& !localDistanceFromOrigin
											.equals(OmegaGUIConstants.NULL)) {
								val = Double.valueOf(localDistanceFromOrigin);
							}
							this.addValueInMap(localDistancesFromOrigin, segm,
									val);
						}
						if (localDisplacementFromOrigin != null) {
							val = null;
							if (!localDisplacementFromOrigin
									.equals(OmegaGUIConstants.NOT_ASSIGNED)
									&& !localDisplacementFromOrigin
											.equals(OmegaGUIConstants.NULL)) {
								val = Double
										.valueOf(localDisplacementFromOrigin);
							}
							this.addValueInMap(localDisplacementsFromOrigin,
									segm, val);
						}
						if (localConfinementRatio != null) {
							val = null;
							if (!localConfinementRatio
									.equals(OmegaGUIConstants.NOT_ASSIGNED)
									&& !localConfinementRatio
											.equals(OmegaGUIConstants.NULL)) {
								val = Double.valueOf(localConfinementRatio);
							}
							this.addValueInMap(localConfinementRatios, segm,
									val);
						}
						if ((angleChange != null) && (angleChange[0] != null)
								&& (angleChange[1] != null)) {
							Double val1 = null, val2 = null;
							if (!angleChange[0]
									.equals(OmegaGUIConstants.NOT_ASSIGNED)
									&& !angleChange[0]
											.equals(OmegaGUIConstants.NULL)) {
								val1 = Double.valueOf(angleChange[0]);
							}
							if (!angleChange[1]
									.equals(OmegaGUIConstants.NOT_ASSIGNED)
									&& !angleChange[1]
											.equals(OmegaGUIConstants.NULL)) {
								val2 = Double.valueOf(angleChange[1]);
							}
							this.addValueInMap(
									localAnglesAndDirectionalChanges, segm,
									new Double[] { val1, val2 });
						}
						if (timeTraveledLoc != null) {
							val = null;
							if (!timeTraveledLoc
									.equals(OmegaGUIConstants.NOT_ASSIGNED)
									&& !timeTraveledLoc
											.equals(OmegaGUIConstants.NULL)) {
								val = Double.valueOf(timeTraveledLoc);
							}
							this.addValueInMap(timeTraveled, segm, val);
						}
					}
				}
			}
		}
		
		final OmegaImporterEventResultsOmegaTrackingMeasuresMobility evt = new OmegaImporterEventResultsOmegaTrackingMeasuresMobility(
				this, this.container, parents, analysisDataMap, paramDataMap,
				segments, localDistances, localDistancesFromOrigin,
				localDisplacementsFromOrigin, maxDisplacementes, timeTraveled,
				localConfinementRatios, localAnglesAndDirectionalChanges,
				OmegaDataToolConstants.COMPLETE_CHAIN_AFTER_IMPORT);
		return evt;
	}

	private OmegaImporterEventResultsOmegaTrackingMeasuresVelocity importTrackingMeasuresVelocityAnalysis(
			final List<String> lineHeaders,
			final List<Map<Integer, List<String[]>>> dataList,
			final String newLineSep, final Map<String, String> analysisDataMap,
			final Map<String, String> paramDataMap,
			final Map<Integer, String> parents,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		
		final Map<OmegaSegment, List<Double>> localSpeedMap = new LinkedHashMap<OmegaSegment, List<Double>>();
		final Map<OmegaSegment, List<Double>> localSpeedFromOriginMap = new LinkedHashMap<OmegaSegment, List<Double>>();
		final Map<OmegaSegment, List<Double>> localVelocityFromOriginMap = new LinkedHashMap<OmegaSegment, List<Double>>();
		final Map<OmegaSegment, Double> averageCurvilinearSpeedMap = new LinkedHashMap<OmegaSegment, Double>();
		final Map<OmegaSegment, Double> averageStraightLineVelocityMap = new LinkedHashMap<OmegaSegment, Double>();
		final Map<OmegaSegment, Double> forwardProgressionLinearityMap = new LinkedHashMap<OmegaSegment, Double>();
		
		for (int k = 0; k < lineHeaders.size(); k++) {
			final String[] headers = lineHeaders.get(k).split(newLineSep);
			final Map<Integer, List<String[]>> data = dataList.get(k);
			for (final int counter : data.keySet()) {
				OmegaTrajectory track = null;
				OmegaSegment segm = null;
				String trackName = null, segmName = null;
				for (final String[] entry : data.get(counter)) {
					Long trackID = null, segmID = null;
					String speedLoc = null, speedFromOrig = null, veloFromOrig = null, speed = null, velo = null, forward = null;
					for (int i = 0; i < headers.length; i++) {
						final String header = headers[i];
						final String val = entry[i];
						if (header.equals(OmegaGUIConstants.RESULTS_TRACK_ID)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								trackID = Long.valueOf(val);
							} else {
								trackID = -1L;
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SEGM_ID)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								segmID = Long.valueOf(val);
							} else {
								segmID = -1L;
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_TRACK_NAME)) {
							trackName = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SEGM_NAME)) {
							segmName = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SEGM_TYPE)) {
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_VELOCITY_AVG_SPEED)) {
							speed = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_VELOCITY_AVG_VELO)) {
							velo = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_VELOCITY_PROG)) {
							forward = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_VELOCITY_SPEED)) {
							speedLoc = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_VELOCITY_CUM_SPEED)) {
							speedFromOrig = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_VELOCITY_CUM_VELO)) {
							veloFromOrig = val;
						}
					}
					if ((track == null) && (trackID != null)) {
						for (final OmegaTrajectory traj : segments.keySet()) {
							if (trackID != -1L) {
								if (trackID == traj.getElementID()) {
									track = traj;
									break;
								}
							} else {
								if (trackName.equals(traj.getName())) {
									track = traj;
									break;
								}
							}
						}
					}
					if (trackID != null) {
						final List<OmegaSegment> segmentList = segments
								.get(track);
						for (final OmegaSegment segment : segmentList) {
							if (segmID != -1L) {
								if (segmID == segment.getElementID()) {
									segm = segment;
									break;
								}
							} else {
								if (segmName.equals(segment.getName())) {
									segm = segment;
									break;
								}
							}
						}
					}
					if (segmID != null) {
						Double val = null;
						if (speed != null) {
							if (!speed.equals(OmegaGUIConstants.NOT_ASSIGNED)
									&& !speed.equals(OmegaGUIConstants.NULL)) {
								val = Double.valueOf(speed);
							}
							averageStraightLineVelocityMap.put(segm, val);
						}
						if (velo != null) {
							val = null;
							if (!velo.equals(OmegaGUIConstants.NOT_ASSIGNED)
									&& !velo.equals(OmegaGUIConstants.NULL)) {
								val = Double.valueOf(velo);
							}
							averageCurvilinearSpeedMap.put(segm, val);
						}
						if (forward != null) {
							val = null;
							if (!forward.equals(OmegaGUIConstants.NOT_ASSIGNED)
									&& !forward.equals(OmegaGUIConstants.NULL)) {
								val = Double.valueOf(forward);
							}
							forwardProgressionLinearityMap.put(segm, val);
						}
						if (speedLoc != null) {
							val = null;
							if (!speedLoc
									.equals(OmegaGUIConstants.NOT_ASSIGNED)
									&& !speedLoc.equals(OmegaGUIConstants.NULL)) {
								val = Double.valueOf(speedLoc);
							}
							this.addValueInMap(localSpeedMap, segm, val);
						}
						if (speedFromOrig != null) {
							val = null;
							if (!speedFromOrig
									.equals(OmegaGUIConstants.NOT_ASSIGNED)
									&& !speedFromOrig
											.equals(OmegaGUIConstants.NULL)) {
								val = Double.valueOf(speedFromOrig);
							}
							this.addValueInMap(localSpeedFromOriginMap, segm,
									val);
						}
						if (veloFromOrig != null) {
							val = null;
							if (!veloFromOrig
									.equals(OmegaGUIConstants.NOT_ASSIGNED)
									&& !veloFromOrig
											.equals(OmegaGUIConstants.NULL)) {
								val = Double.valueOf(veloFromOrig);
							}
							this.addValueInMap(localVelocityFromOriginMap,
									segm, val);
						}
					}
				}
			}
		}
		final OmegaImporterEventResultsOmegaTrackingMeasuresVelocity evt = new OmegaImporterEventResultsOmegaTrackingMeasuresVelocity(
				this, this.container, parents, analysisDataMap, paramDataMap,
				segments, localSpeedMap, localSpeedFromOriginMap,
				localVelocityFromOriginMap, averageCurvilinearSpeedMap,
				averageStraightLineVelocityMap, forwardProgressionLinearityMap,
				OmegaDataToolConstants.COMPLETE_CHAIN_AFTER_IMPORT);
		return evt;
	}
	
	private OmegaImporterEventResultsOmegaTrackingMeasuresIntensity importTrackingMeasuresIntensityAnalysis(
			final List<String> lineHeaders,
			final List<Map<Integer, List<String[]>>> dataList,
			final String newLineSep, final Map<String, String> analysisDataMap,
			final Map<String, String> paramDataMap,
			final Map<Integer, String> parents,
			final Map<OmegaPlane, List<OmegaROI>> particles,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final boolean startAtOne) {
		
		final Map<OmegaSegment, Double[]> peakSignalsMap = new LinkedHashMap<OmegaSegment, Double[]>();
		final Map<OmegaSegment, Double[]> centroidSignalsMap = new LinkedHashMap<OmegaSegment, Double[]>();
		final Map<OmegaROI, Double> peakSignalsLocMap = new LinkedHashMap<OmegaROI, Double>();
		final Map<OmegaROI, Double> centroidSignalsLocMap = new LinkedHashMap<OmegaROI, Double>();
		
		final Map<OmegaSegment, Double[]> meanSignalsMap = new LinkedHashMap<OmegaSegment, Double[]>();
		final Map<OmegaSegment, Double[]> backgroundsMap = new LinkedHashMap<OmegaSegment, Double[]>();
		final Map<OmegaSegment, Double[]> noisesMap = new LinkedHashMap<OmegaSegment, Double[]>();
		final Map<OmegaSegment, Double[]> snrsMap = new LinkedHashMap<OmegaSegment, Double[]>();
		final Map<OmegaSegment, Double[]> areasMap = new LinkedHashMap<OmegaSegment, Double[]>();
		final Map<OmegaROI, Double> meanSignalsLocMap = new LinkedHashMap<OmegaROI, Double>();
		final Map<OmegaROI, Double> backgroundsLocMap = new LinkedHashMap<OmegaROI, Double>();
		final Map<OmegaROI, Double> noisesLocMap = new LinkedHashMap<OmegaROI, Double>();
		final Map<OmegaROI, Double> areasLocMap = new LinkedHashMap<OmegaROI, Double>();
		final Map<OmegaROI, Double> snrsLocMap = new LinkedHashMap<OmegaROI, Double>();
		
		for (int k = 0; k < lineHeaders.size(); k++) {
			final String[] headers = lineHeaders.get(k).split(newLineSep);
			final Map<Integer, List<String[]>> data = dataList.get(k);
			for (final int counter : data.keySet()) {
				OmegaTrajectory track = null;
				OmegaSegment segm = null;
				String trackName = null, segmName = null;
				OmegaROI roiMatch = null;
				for (final String[] entry : data.get(counter)) {
					Long particleID = null, trackID = null, segmID = null;
					Integer frameIndex = null;
					Double x = null, y = null;
					Double centroid = null, centroidAvg = null, centroidMax = null, centroidMin = null;
					Double peak = null, peakAvg = null, peakMax = null, peakMin = null;
					Double mean = null, meanAvg = null, meanMax = null, meanMin = null;
					Double bg = null, bgAvg = null, bgMax = null, bgMin = null;
					Double noise = null, noiseAvg = null, noiseMax = null, noiseMin = null;
					Double snr = null, snrAvg = null, snrMax = null, snrMin = null;
					for (int i = 0; i < headers.length; i++) {
						final String header = headers[i];
						final String val = entry[i];
						if (header
								.equals(OmegaGUIConstants.RESULTS_PARTICLE_ID)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								particleID = Long.valueOf(val);
							} else {
								particleID = -1L;
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_TRACK_ID)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								trackID = Long.valueOf(val);
							} else {
								trackID = -1L;
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SEGM_ID)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								segmID = Long.valueOf(val);
							} else {
								segmID = -1L;
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_FRAME)) {
							frameIndex = Integer.valueOf(val);
						} else if (header.equals(OmegaGUIConstants.RESULTS_X)) {
							x = Double.valueOf(val);
						} else if (header.equals(OmegaGUIConstants.RESULTS_Y)) {
							y = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_TRACK_NAME)) {
							trackName = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SEGM_NAME)) {
							segmName = val;
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SEGM_TYPE)) {
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID)) {
							centroid = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_PEAK)) {
							peak = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_MEAN)) {
							mean = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND)) {
							bg = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_NOISE)) {
							noise = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_SNR)) {
							snr = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID_AVG)) {
							centroidAvg = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID_MIN)) {
							centroidMin = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID_MAX)) {
							centroidMax = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_PEAK_AVG)) {
							peakAvg = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_PEAK_MIN)) {
							peakMin = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_PEAK_MAX)) {
							peakMax = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_MEAN_AVG)) {
							meanAvg = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_MEAN_MIN)) {
							meanMin = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_MEAN_MAX)) {
							meanMax = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND_AVG)) {
							bgAvg = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND_MIN)) {
							bgMin = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND_MAX)) {
							bgMax = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_NOISE_AVG)) {
							noiseAvg = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_NOISE_MIN)) {
							noiseMin = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_NOISE_MAX)) {
							noiseMax = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_SNR_AVG)) {
							snrAvg = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_SNR_MIN)) {
							snrMin = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_SNR_MAX)) {
							snrMax = Double.valueOf(val);
						}
					}
					if ((frameIndex != null) && !startAtOne) {
						frameIndex++;
					}
					if ((track == null) && (trackID != null)) {
						for (final OmegaTrajectory traj : segments.keySet()) {
							if (trackID != -1L) {
								if (trackID == traj.getElementID()) {
									track = traj;
									break;
								}
							} else {
								if (trackName.equals(traj.getName())) {
									track = traj;
									break;
								}
							}
						}
					}
					if (trackID != null) {
						final List<OmegaSegment> segmentList = segments
								.get(track);
						for (final OmegaSegment segment : segmentList) {
							if (segmID != -1L) {
								if (segmID == segment.getElementID()) {
									segm = segment;
									break;
								}
							} else {
								if (segmName.equals(segment.getName())) {
									segm = segment;
									break;
								}
							}
						}
						if (particleID != null) {
							final List<OmegaROI> rois = track.getROIs();
							for (final OmegaROI roi : rois) {
								if (particleID != -1L) {
									if (particleID == roi.getElementID()) {
										roiMatch = roi;
										break;
									}
								} else {
									if ((frameIndex == roi.getFrameIndex())
											&& (roi.getX() == x)
											&& (roi.getY() == y)) {
										roiMatch = roi;
										break;
									}
								}
							}
						}
					}
					if ((segmID != null) && (particleID == null)) {
						if ((peakAvg != null) && (peakMin != null)
								&& (peakMax != null)) {
							peakSignalsMap.put(segm, new Double[] { peakAvg,
									peakMin, peakMax });
						}
						if ((centroidAvg != null) && (centroidMin != null)
								&& (centroidMax != null)) {
							centroidSignalsMap.put(segm, new Double[] {
									centroidAvg, centroidMin, centroidMax });
						}
						if ((meanAvg != null) && (meanMin != null)
								&& (meanMax != null)) {
							meanSignalsMap.put(segm, new Double[] { meanAvg,
									meanMin, meanMax });
						}
						if ((bgAvg != null) && (bgMin != null)
								&& (bgMax != null)) {
							backgroundsMap.put(segm, new Double[] { bgAvg,
									bgMin, bgMax });
						}
						if ((noiseAvg != null) && (noiseMin != null)
								&& (noiseMax != null)) {
							noisesMap.put(segm, new Double[] { noiseAvg,
									noiseMin, noiseMax });
						}
						if ((snrAvg != null) && (snrMin != null)
								&& (snrMax != null)) {
							snrsMap.put(segm, new Double[] { snrAvg, snrMin,
									snrMax });
						}
					}
					
					if (particleID != null) {
						if (peak != null) {
							peakSignalsLocMap.put(roiMatch, peak);
						}
						if (centroid != null) {
							centroidSignalsLocMap.put(roiMatch, centroid);
						}
						if (mean != null) {
							meanSignalsLocMap.put(roiMatch, mean);
						}
						if (bg != null) {
							backgroundsLocMap.put(roiMatch, bg);
						}
						if (noise != null) {
							noisesLocMap.put(roiMatch, noise);
						}
						if (snr != null) {
							snrsLocMap.put(roiMatch, snr);
						}
					}
				}
			}
		}
		final OmegaImporterEventResultsOmegaTrackingMeasuresIntensity evt = new OmegaImporterEventResultsOmegaTrackingMeasuresIntensity(
				this, this.container, parents, analysisDataMap, paramDataMap,
				segments, peakSignalsMap, centroidSignalsMap,
				peakSignalsLocMap, centroidSignalsLocMap, meanSignalsMap,
				backgroundsMap, noisesMap, snrsMap, areasMap,
				meanSignalsLocMap, backgroundsLocMap, noisesLocMap,
				areasLocMap, snrsLocMap,
				OmegaDataToolConstants.COMPLETE_CHAIN_AFTER_IMPORT);
		return evt;
	}
	
	private OmegaImporterEventResultsOmegaTrajectoriesSegmentation importTrajectoriesSegmentationAnalysis(
			final String lineHeader, final Map<Integer, List<String[]>> data,
			final String newLineSep, final Map<String, String> analysisDataMap,
			final Map<String, String> paramDataMap,
			final Map<Integer, String> parents,
			final List<OmegaTrajectory> tracks,
			final OmegaSegmentationTypes segmTypes, final boolean startAtOne) {
		final Map<OmegaTrajectory, List<OmegaSegment>> segments = new LinkedHashMap<OmegaTrajectory, List<OmegaSegment>>();
		final String[] headers = lineHeader.split(newLineSep);
		for (final int counter : data.keySet()) {
			OmegaTrajectory track = null;
			OmegaSegment segm = null;
			String trackName = null, segmName = null, segmTypeName = null;
			OmegaROI startROI = null, endROI = null;
			Long trackID = -1L, segmID = -1L;
			for (final String[] entry : data.get(counter)) {
				Long particleID = -1L;
				Integer frameIndex = null;
				Double x = null, y = null;
				for (int i = 0; i < headers.length; i++) {
					final String header = headers[i];
					final String val = entry[i];
					if (header.equals(OmegaGUIConstants.RESULTS_PARTICLE_ID)) {
						if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
							particleID = Long.valueOf(val);
						}
					} else if (header
							.equals(OmegaGUIConstants.RESULTS_TRACK_ID)) {
						if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
							trackID = Long.valueOf(val);
						}
					} else if (header.equals(OmegaGUIConstants.RESULTS_SEGM_ID)) {
						if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
							segmID = Long.valueOf(val);
						}
					} else if (header.equals(OmegaGUIConstants.RESULTS_FRAME)) {
						frameIndex = Integer.valueOf(val);
					} else if (header.equals(OmegaGUIConstants.RESULTS_X)) {
						x = Double.valueOf(val);
					} else if (header.equals(OmegaGUIConstants.RESULTS_Y)) {
						y = Double.valueOf(val);
					} else if (header
							.equals(OmegaGUIConstants.RESULTS_TRACK_NAME)) {
						trackName = val;
					} else if (header
							.equals(OmegaGUIConstants.RESULTS_SEGM_NAME)) {
						segmName = val;
					} else if (header
							.equals(OmegaGUIConstants.RESULTS_SEGM_TYPE)) {
						segmTypeName = val;
					}
				}
				if ((frameIndex != null) && !startAtOne) {
					frameIndex++;
				}
				if (track == null) {
					for (final OmegaTrajectory traj : tracks) {
						if (trackID != -1L) {
							if (trackID == traj.getElementID()) {
								track = traj;
								break;
							}
						} else {
							if (trackName.equals(traj.getName())) {
								track = traj;
								break;
							}
						}
					}
				}
				OmegaROI roiMatch = null;
				final List<OmegaROI> rois = track.getROIs();
				for (final OmegaROI roi : rois) {
					if (particleID != -1L) {
						if (particleID == roi.getElementID()) {
							roiMatch = roi;
							break;
						}
					} else {
						if ((frameIndex == roi.getFrameIndex())
								&& (roi.getX() == x) && (roi.getY() == y)) {
							roiMatch = roi;
							break;
						}
					}
				}
				if (roiMatch == null) {
					// TODO error
				}
				if (startROI == null) {
					startROI = roiMatch;
				} else {
					endROI = roiMatch;
				}
			}
			if (segm == null) {
				segm = new OmegaSegment(startROI, endROI, segmName);
				segm.setElementID(segmID);
				final int segmType = segmTypes
						.getSegmentationValue(segmTypeName);
				segm.setSegmentationType(segmType);
				List<OmegaSegment> segmentList;
				if (segments.containsKey(track)) {
					segmentList = segments.get(track);
				} else {
					segmentList = new ArrayList<OmegaSegment>();
				}
				segmentList.add(segm);
				segments.put(track, segmentList);
			}
		}
		final OmegaImporterEventResultsOmegaTrajectoriesSegmentation evt = new OmegaImporterEventResultsOmegaTrajectoriesSegmentation(
				this, this.container, parents, analysisDataMap, paramDataMap,
				segments, segmTypes,
				OmegaDataToolConstants.COMPLETE_CHAIN_AFTER_IMPORT);
		return evt;
	}
	
	private OmegaImporterEventResultsOmegaTrajectoriesRelinking importTrajectoriesRelinkingAnalysis(
			final String lineHeader, final Map<Integer, List<String[]>> data,
			final String newLineSep, final Map<String, String> analysisDataMap,
			final Map<String, String> paramDataMap,
			final Map<Integer, String> parents,
			final Map<OmegaPlane, List<OmegaROI>> particles,
			final boolean startAtOne) {
		final OmegaImporterEventResultsOmegaParticleLinking linkingEvt = this
				.importParticleLinkingAnalysis(lineHeader, data, newLineSep,
						analysisDataMap, paramDataMap, parents, particles,
						startAtOne);
		final OmegaImporterEventResultsOmegaTrajectoriesRelinking relinkingEvt = new OmegaImporterEventResultsOmegaTrajectoriesRelinking(
				this, this.container, parents, linkingEvt.getAnalysisData(),
				linkingEvt.getParamData(),
				linkingEvt.getResultingTrajectories(),
				OmegaDataToolConstants.COMPLETE_CHAIN_AFTER_IMPORT);
		return relinkingEvt;
	}
	
	private OmegaImporterEventResultsOmegaParticleLinking importParticleLinkingAnalysis(
			final String lineHeader, final Map<Integer, List<String[]>> data,
			final String newLineSep, final Map<String, String> analysisDataMap,
			final Map<String, String> paramDataMap,
			final Map<Integer, String> parents,
			final Map<OmegaPlane, List<OmegaROI>> particles,
			final boolean startAtOne) {
		final Map<Integer, OmegaPlane> planes = new LinkedHashMap<Integer, OmegaPlane>();
		for (final OmegaPlane plane : particles.keySet()) {
			planes.put(plane.getIndex(), plane);
		}
		final List<OmegaTrajectory> tracks = new ArrayList<OmegaTrajectory>();
		final String[] headers = lineHeader.split(newLineSep);
		for (final int counter : data.keySet()) {
			OmegaTrajectory track = null;
			final List<OmegaROI> trackROIs = new ArrayList<OmegaROI>();
			for (final String[] entry : data.get(counter)) {
				String trackName = null;
				Long particleID = -1L, trackID = -1L;
				Integer frameIndex = null, trackLength = null;
				Double x = null, y = null;
				for (int i = 0; i < headers.length; i++) {
					final String header = headers[i];
					final String val = entry[i];
					if (header.equals(OmegaGUIConstants.RESULTS_PARTICLE_ID)) {
						if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
							particleID = Long.valueOf(val);
						}
					} else if (header
							.equals(OmegaGUIConstants.RESULTS_TRACK_ID)) {
						if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
							trackID = Long.valueOf(val);
						}
					} else if (header.equals(OmegaGUIConstants.RESULTS_FRAME)) {
						frameIndex = Integer.valueOf(val);
					} else if (header.equals(OmegaGUIConstants.RESULTS_X)) {
						x = Double.valueOf(val);
					} else if (header.equals(OmegaGUIConstants.RESULTS_Y)) {
						y = Double.valueOf(val);
					} else if (header.equals(OmegaGUIConstants.RESULTS_C)) {
						Integer.valueOf(val);
					} else if (header.equals(OmegaGUIConstants.RESULTS_Z)) {
						Integer.valueOf(val);
					} else if (header
							.equals(OmegaGUIConstants.RESULTS_TRACK_NAME)) {
						trackName = val;
					} else if (header
							.equals(OmegaGUIConstants.RESULTS_TRACK_LENGTH)) {
						trackLength = Integer.valueOf(val);
					}
				}
				if ((frameIndex != null) && !startAtOne) {
					frameIndex++;
				}
				if (track == null) {
					final Double index = Double
							.valueOf(String.valueOf(counter));
					track = new OmegaTrajectory(trackLength, trackName, index);
					track.setElementID(trackID);
					tracks.add(track);
				}
				OmegaParticle p = null;
				final OmegaPlane plane = planes.get(frameIndex);
				final List<OmegaROI> rois = particles.get(plane);
				for (final OmegaROI roi : rois) {
					if (particleID != -1L) {
						if (roi.getElementID() == particleID) {
							p = (OmegaParticle) roi;
							break;
						}
					} else {
						if ((roi.getFrameIndex() == frameIndex)
								&& (roi.getX() == x) && (roi.getY() == y)) {
							p = (OmegaParticle) roi;
							break;
						}
					}
					if (p != null) {
						break;
					}
				}
				if (p == null) {
					// TODO error
				}
				trackROIs.add(p);
			}
			track.addROIs(trackROIs);
		}
		final OmegaImporterEventResultsOmegaParticleLinking evt = new OmegaImporterEventResultsOmegaParticleLinking(
				this, this.container, parents, analysisDataMap, paramDataMap,
				tracks, OmegaDataToolConstants.COMPLETE_CHAIN_AFTER_IMPORT);
		return evt;
	}
	
	private OmegaImporterEventResultsOmegaSNR importSNRAnalysis(
			final List<String> lineHeaders,
			final List<Map<Integer, List<String[]>>> dataList,
			final String newLineSep, final Map<String, String> analysisDataMap,
			final Map<String, String> paramDataMap,
			final Map<Integer, String> parents,
			final Map<OmegaPlane, List<OmegaROI>> particles,
			final boolean startAtOne) {
		final Map<Integer, OmegaPlane> planes = new LinkedHashMap<Integer, OmegaPlane>();
		for (final OmegaPlane plane : particles.keySet()) {
			planes.put(plane.getIndex(), plane);
		}
		final Map<OmegaPlane, Double> resultingImageAvgCenterSignal = new LinkedHashMap<OmegaPlane, Double>();
		final Map<OmegaPlane, Double> resultingImageAvgPeakSignal = new LinkedHashMap<OmegaPlane, Double>();
		final Map<OmegaPlane, Double> resultingImageAvgMeanSignal = new LinkedHashMap<OmegaPlane, Double>();
		final Map<OmegaPlane, Double> resultingImageBGR = new LinkedHashMap<OmegaPlane, Double>();
		final Map<OmegaPlane, Double> resultingImageNoise = new LinkedHashMap<OmegaPlane, Double>();
		final Map<OmegaPlane, Double> resultingImageAverageSNR = new LinkedHashMap<OmegaPlane, Double>();
		final Map<OmegaPlane, Double> resultingImageMinimumSNR = new LinkedHashMap<OmegaPlane, Double>();
		final Map<OmegaPlane, Double> resultingImageMaximumSNR = new LinkedHashMap<OmegaPlane, Double>();
		final Map<OmegaPlane, Double> resultingImageAverageErrorIndexSNR = new LinkedHashMap<OmegaPlane, Double>();
		final Map<OmegaPlane, Double> resultingImageMinimumErrorIndexSNR = new LinkedHashMap<OmegaPlane, Double>();
		final Map<OmegaPlane, Double> resultingImageMaximumErrorIndexSNR = new LinkedHashMap<OmegaPlane, Double>();
		final Map<OmegaROI, Integer> resultingLocalCenterSignal = new LinkedHashMap<OmegaROI, Integer>();
		final Map<OmegaROI, Double> resultingLocalMeanSignals = new LinkedHashMap<OmegaROI, Double>();
		final Map<OmegaROI, Integer> resultingLocalSignalSizes = new LinkedHashMap<OmegaROI, Integer>();
		final Map<OmegaROI, Integer> resultingLocalPeakSignals = new LinkedHashMap<OmegaROI, Integer>();
		final Map<OmegaROI, Double> resultingLocalBackgrounds = new LinkedHashMap<OmegaROI, Double>();
		final Map<OmegaROI, Double> resultingLocalNoises = new LinkedHashMap<OmegaROI, Double>();
		final Map<OmegaROI, Double> resultingLocalSNRs = new LinkedHashMap<OmegaROI, Double>();
		final Map<OmegaROI, Double> resultingLocalErrorIndexSNRs = new LinkedHashMap<OmegaROI, Double>();
		Double resultingAvgCenterSignal = null;
		Double resultingAvgPeakSignal = null;
		Double resultingAvgMeanSignal = null;
		Double resultingBGR = null;
		Double resultingNoise = null;
		Double resultingAvgSNR = null;
		Double resultingMinSNR = null;
		Double resultingMaxSNR = null;
		Double resultingAvgErrorIndexSNR = null;
		Double resultingMinErrorIndexSNR = null;
		Double resultingMaxErrorIndexSNR = null;

		for (int k = 0; k < lineHeaders.size(); k++) {
			final String[] headers = lineHeaders.get(k).split(newLineSep);
			final Map<Integer, List<String[]>> data = dataList.get(k);
			for (final int counter : data.keySet()) {
				for (final String[] entry : data.get(counter)) {
					Long particleID = null, planeID = null, imageID = null;
					Integer frameIndex = null;
					Double x = null, y = null;
					Double mean = null, bg = null, noise = null, snr = null;
					Integer centroid = null, peak = null, area = null;
					Double centroidAvg = null, peakAvg = null, meanAvg = null, bgAvg = null, noiseAvg = null, snrAvg = null, snrMin = null, snrMax = null;
					Double errorIndexSNR = null, errorIndexSNRAvg = null, errorIndexSNRMin = null, errorIndexSNRMax = null;
					for (int i = 0; i < headers.length; i++) {
						final String header = headers[i];
						final String val = entry[i];
						if (header
								.equals(OmegaGUIConstants.RESULTS_PARTICLE_ID)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								particleID = Long.valueOf(val);
							} else {
								particleID = -1L;
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_PLANE_ID)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								planeID = Long.valueOf(val);
							} else {
								planeID = -1L;
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_IMAGE_ID)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								imageID = Long.valueOf(val);
							} else {
								imageID = -1L;
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_IMAGE_ID)) {
							if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
								imageID = Long.valueOf(val);
							}
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_FRAME)
								|| header
										.equals(OmegaGUIConstants.RESULTS_PLANE_INDEX)) {
							frameIndex = Integer.valueOf(val);
						} else if (header.equals(OmegaGUIConstants.RESULTS_X)) {
							x = Double.valueOf(val);
						} else if (header.equals(OmegaGUIConstants.RESULTS_Y)) {
							y = Double.valueOf(val);
						} else if (header.equals(OmegaGUIConstants.RESULTS_C)) {
							Integer.valueOf(val);
						} else if (header.equals(OmegaGUIConstants.RESULTS_Z)) {
							Integer.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID)) {
							centroid = Integer.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_PEAK)) {
							peak = Integer.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_MEAN)) {
							mean = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND)) {
							bg = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_NOISE)) {
							noise = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_SNR)) {
							snr = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SNR_SNR_INDEX)) {
							errorIndexSNR = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SNR_AREA)) {
							area = Integer.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID_AVG)) {
							centroidAvg = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_PEAK_AVG)) {
							peakAvg = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_INTENSITY_MEAN_AVG)) {
							meanAvg = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SNR_BACKGROUND)) {
							bgAvg = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SNR_NOISE)) {
							noiseAvg = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SNR_SNR_AVG)) {
							snrAvg = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SNR_SNR_MIN)) {
							snrMin = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SNR_SNR_MAX)) {
							snrMax = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SNR_SNR_INDEX_AVG)) {
							errorIndexSNRAvg = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SNR_SNR_INDEX_MIN)) {
							errorIndexSNRMin = Double.valueOf(val);
						} else if (header
								.equals(OmegaGUIConstants.RESULTS_SNR_SNR_INDEX_MAX)) {
							errorIndexSNRMax = Double.valueOf(val);
						}
					}
					if ((frameIndex != null) && !startAtOne) {
						frameIndex++;
					}
					if (planeID != null) {
						OmegaParticle p = null;
						final OmegaPlane plane = planes.get(frameIndex);
						if (particleID != null) {
							final List<OmegaROI> rois = particles.get(plane);
							for (final OmegaROI roi : rois) {
								if (particleID != -1L) {
									if (roi.getElementID() == particleID) {
										p = (OmegaParticle) roi;
										break;
									}
								} else {
									if ((roi.getFrameIndex() == frameIndex)
											&& (roi.getX() == x)
											&& (roi.getY() == y)) {
										p = (OmegaParticle) roi;
										break;
									}
								}
								if (p != null) {
									break;
								}
							}
							if (p == null) {
								// TODO error
							}
							if (particleID != null) {
								resultingLocalCenterSignal.put(p, centroid);
								resultingLocalMeanSignals.put(p, mean);
								resultingLocalSignalSizes.put(p, area);
								resultingLocalPeakSignals.put(p, peak);
								resultingLocalBackgrounds.put(p, bg);
								resultingLocalNoises.put(p, noise);
								resultingLocalSNRs.put(p, snr);
								resultingLocalErrorIndexSNRs.put(p,
										errorIndexSNR);
							}
						}
						if (planeID != null) {
							resultingImageAvgCenterSignal.put(plane,
									centroidAvg);
							resultingImageAvgPeakSignal.put(plane, peakAvg);
							resultingImageAvgMeanSignal.put(plane, meanAvg);
							resultingImageBGR.put(plane, bgAvg);
							resultingImageNoise.put(plane, noise);
							resultingImageAverageSNR.put(plane, snrAvg);
							resultingImageMinimumSNR.put(plane, snrMin);
							resultingImageMaximumSNR.put(plane, snrMax);
							resultingImageAverageErrorIndexSNR.put(plane,
									errorIndexSNRAvg);
							resultingImageMinimumErrorIndexSNR.put(plane,
									errorIndexSNRMin);
							resultingImageMaximumErrorIndexSNR.put(plane,
									errorIndexSNRMax);
						}
					}
					
					if (imageID != null) {
						resultingAvgCenterSignal = centroidAvg;
						resultingAvgPeakSignal = peakAvg;
						resultingAvgMeanSignal = meanAvg;
						resultingBGR = bgAvg;
						resultingNoise = noiseAvg;
						resultingAvgSNR = snrAvg;
						resultingMinSNR = snrMin;
						resultingMaxSNR = snrMax;
						resultingAvgErrorIndexSNR = errorIndexSNRAvg;
						resultingMinErrorIndexSNR = errorIndexSNRMin;
						resultingMaxErrorIndexSNR = errorIndexSNRMax;
					}
				}
			}
		}
		
		final OmegaImporterEventResultsOmegaSNR evt = new OmegaImporterEventResultsOmegaSNR(
				this, this.container, parents, analysisDataMap, paramDataMap,
				resultingImageAvgCenterSignal, resultingImageAvgPeakSignal,
				resultingImageAvgMeanSignal, resultingImageBGR,
				resultingImageNoise, resultingImageAverageSNR,
				resultingImageMinimumSNR, resultingImageMaximumSNR,
				resultingImageAverageErrorIndexSNR,
				resultingImageMinimumErrorIndexSNR,
				resultingImageMaximumErrorIndexSNR, resultingLocalCenterSignal,
				resultingLocalMeanSignals, resultingLocalSignalSizes,
				resultingLocalPeakSignals, resultingLocalBackgrounds,
				resultingLocalNoises, resultingLocalSNRs,
				resultingLocalErrorIndexSNRs, resultingAvgCenterSignal,
				resultingAvgPeakSignal, resultingAvgMeanSignal, resultingBGR,
				resultingNoise, resultingAvgSNR, resultingMinSNR,
				resultingMaxSNR, resultingAvgErrorIndexSNR,
				resultingMinErrorIndexSNR, resultingMaxErrorIndexSNR,
				OmegaDataToolConstants.COMPLETE_CHAIN_AFTER_IMPORT);
		return evt;
	}
	
	private OmegaImporterEventResultsOmegaParticleDetection importParticleDetectionAnalysis(
			final String lineHeader, final Map<Integer, List<String[]>> data,
			final String newLineSep, final Map<String, String> analysisDataMap,
			final Map<String, String> paramDataMap,
			final Map<Integer, String> parents, final boolean startAtOne) {
		final LinkedHashMap<OmegaPlane, List<OmegaROI>> particlesMap = new LinkedHashMap<OmegaPlane, List<OmegaROI>>();
		final LinkedHashMap<OmegaROI, Map<String, Object>> particlesValuesMap = new LinkedHashMap<OmegaROI, Map<String, Object>>();
		final String[] headers = lineHeader.split(newLineSep);
		for (final int counter : data.keySet()) {
			OmegaPlane plane = null;
			final List<OmegaROI> rois = new ArrayList<OmegaROI>();
			for (final String[] entry : data.get(counter)) {
				Long particleID = -1L;
				Integer frameIndex = null, c = null, z = null;
				Double x = null, y = null;
				final Map<String, Object> values = new LinkedHashMap<String, Object>();
				for (int i = 0; i < headers.length; i++) {
					final String header = headers[i];
					final String val = entry[i];
					if (header.equals(OmegaGUIConstants.RESULTS_PARTICLE_ID)) {
						if (!val.equals(OmegaGUIConstants.NOT_ASSIGNED)) {
							particleID = Long.valueOf(val);
						}
					} else if (header.equals(OmegaGUIConstants.RESULTS_FRAME)) {
						frameIndex = Integer.valueOf(val);
					} else if (header.equals(OmegaGUIConstants.RESULTS_X)) {
						x = Double.valueOf(val);
					} else if (header.equals(OmegaGUIConstants.RESULTS_Y)) {
						y = Double.valueOf(val);
					} else if (header.equals(OmegaGUIConstants.RESULTS_C)) {
						c = Integer.valueOf(val);
					} else if (header.equals(OmegaGUIConstants.RESULTS_Z)) {
						z = Integer.valueOf(val);
					} else {
						values.put(header, val);
					}
				}
				if ((frameIndex != null) && !startAtOne) {
					frameIndex++;
				}
				if (plane == null) {
					plane = new OmegaPlane(frameIndex, c, z);
					System.out.println("Frame " + frameIndex);
				}
				// this.frame.setParentPixels(this.pixels);
				// for (final SDWorker2 worker : completedWorkers) {
				// image.getDefaultPixels().addFrame(worker.getC(),
				// worker.getZ(), worker.getFrame());
				// }
				final OmegaParticle p = new OmegaParticle(frameIndex, x, y);
				p.setElementID(particleID);
				rois.add(p);
				particlesValuesMap.put(p, values);
			}
			particlesMap.put(plane, rois);
		}
		final OmegaImporterEventResultsOmegaParticleDetection evt = new OmegaImporterEventResultsOmegaParticleDetection(
				this, this.container, parents, analysisDataMap, paramDataMap,
				particlesMap, particlesValuesMap,
				OmegaDataToolConstants.COMPLETE_CHAIN_AFTER_IMPORT);
		return evt;
	}
	
	private Map<OmegaSegment, Double[][]> transformDoubleMap(
			final Map<OmegaSegment, Map<Double, List<Double>>> map,
			final boolean keyIsDouble) {
		final Map<OmegaSegment, Double[][]> newMap = new LinkedHashMap<OmegaSegment, Double[][]>();
		for (final OmegaSegment segm : map.keySet()) {
			final Map<Double, List<Double>> valuesMap = map.get(segm);
			final Double[][] valuesArray = new Double[valuesMap.size()][];
			for (int i = 0; i < valuesMap.size(); i++) {
				final List<Double> valuesList;
				if (keyIsDouble) {
					valuesList = valuesMap.get(Double.valueOf(i));
				} else {
					valuesList = valuesMap.get(i);
				}
				if (valuesList == null) {
					valuesArray[i] = null;
					continue;
				}
				valuesArray[i] = new Double[valuesList.size()];
				for (int y = 0; y < valuesList.size(); y++) {
					final Double value = valuesList.get(y);
					valuesArray[i][y] = value;
				}
			}
			newMap.put(segm, valuesArray);
		}
		return newMap;
	}
	
	private Map<OmegaSegment, Double[]> transformMap(
			final Map<OmegaSegment, List<Double>> map) {
		final Map<OmegaSegment, Double[]> newMap = new LinkedHashMap<OmegaSegment, Double[]>();
		for (final OmegaSegment segm : map.keySet()) {
			final List<Double> values = map.get(segm);
			final Double[] valuesArray = new Double[values.size()];
			for (int i = 0; i < values.size(); i++) {
				valuesArray[i] = values.get(i);
			}
			newMap.put(segm, valuesArray);
		}
		return newMap;
	}
	
	private void addValueInMap(final Map<OmegaSegment, List<Double[]>> map,
			final OmegaSegment segm, final Double[] value) {
		List<Double[]> locValues;
		if (map.containsKey(segm)) {
			locValues = map.get(segm);
		} else {
			locValues = new ArrayList<Double[]>();
		}
		locValues.add(value);
		map.put(segm, locValues);
	}
	
	private void addValueInMap(final Map<OmegaSegment, List<Double>> map,
			final OmegaSegment segm, final Double value) {
		List<Double> locValues;
		if (map.containsKey(segm)) {
			locValues = map.get(segm);
		} else {
			locValues = new ArrayList<Double>();
		}
		locValues.add(value);
		map.put(segm, locValues);
	}
	
	private String findTypeIdentifier(final String fileType,
			final String dynamicDisplayName) {
		for (final String dynName : OmegaTracksImporter.dataNames) {
			if (!dynName.equals(dynamicDisplayName)) {
				continue;
			}
			if (dynName
					.equals(OmegaParticleDetectionRun.getStaticDisplayName()))
				return "Frame";
			else if (dynName.equals(OmegaTrajectoriesRelinkingRun
					.getStaticDisplayName())
					|| dynName.equals(OmegaParticleLinkingRun
							.getStaticDisplayName()))
				return "Trajectory";
			else if (dynName.equals(OmegaTrajectoriesSegmentationRun
					.getStaticDisplayName()))
				return "Segment";
			else if (dynName.equals(OmegaTrackingMeasuresIntensityRun
					.getStaticDisplayName())
					|| dynName.equals(OmegaTrackingMeasuresVelocityRun
							.getStaticDisplayName())
					|| dynName.equals(OmegaTrackingMeasuresMobilityRun
							.getStaticDisplayName()))
				return "Segment";
			else if (dynName.equals(OmegaTrackingMeasuresDiffusivityRun
					.getStaticDisplayName()))
				if (fileType.equals(OmegaGUIConstants.INFO_FILE_TYPE_DM_PS))
					return "Trajectory";
				else
					return "Segment";

			else if (dynName.equals(OmegaSNRRun.getStaticDisplayName()))
				if (fileType.equals(OmegaGUIConstants.INFO_FILE_TYPE_SNR_IMAGE))
					return "Image";
				else
					return "Frame";
		}
		return null;
	}
	
	private void importParticles(final String fileNameIdent,
			final String planeIdentifier, final String particleIdent,
			final boolean startAtOne, final String commentIdent,
			final String lineSep, final List<String> particleDataOrder,
			final File sourceFolder) throws IllegalArgumentException,
			IOException {
		if (!sourceFolder.isDirectory())
			throw new IllegalArgumentException("The destination folder: "
					+ sourceFolder + " has to be a valid directory");
		if (sourceFolder.listFiles().length == 0)
			throw new IllegalArgumentException("The destination folder: "
					+ sourceFolder + " has to be not empty");
		boolean isValid = false;
		
		for (final File f : sourceFolder.listFiles()) {
			final String fName = f.getName();
			if (!fName.matches(fileNameIdent)
					&& !fName.startsWith(fileNameIdent)) {
				continue;
			}
			isValid = true;
			final FileReader fr = new FileReader(f);
			final BufferedReader br = new BufferedReader(fr);
			this.importParticles(f.getName(), planeIdentifier, particleIdent,
					startAtOne, commentIdent, lineSep, particleDataOrder, br);
			br.close();
			fr.close();
		}
		if (!isValid)
			throw new IllegalArgumentException(
					"The source folder: "
							+ sourceFolder
							+ " has to contain at least 1 file containing the given file name identifier");
		
		final LinkedHashMap<String, String> analysisData = new LinkedHashMap<String, String>(
				this.analysisDataMap);
		final LinkedHashMap<String, String> paramData = new LinkedHashMap<String, String>(
				this.paramMap);
		final Map<OmegaPlane, List<OmegaROI>> resultingParticles = new LinkedHashMap<OmegaPlane, List<OmegaROI>>(
				this.particles);
		final Map<OmegaROI, Map<String, Object>> resultingParticlesValues = new LinkedHashMap<OmegaROI, Map<String, Object>>(
				this.particlesValues);
		// Send the right event
		final OmegaImporterEventResultsGeneralParticleDetection evt = new OmegaImporterEventResultsGeneralParticleDetection(
				this, this.container, analysisData, paramData,
				resultingParticles, resultingParticlesValues,
				OmegaDataToolConstants.COMPLETE_CHAIN_AFTER_IMPORT);
		this.fireEvent(evt);
		
		// this.fireEvent(evt);
	}
	
	// TODO change IllegalArgumentException with a custom exception
	private void importTrajectories(final boolean multifile,
			final String fileNameIdent, final String dataIdent,
			final String particleIdent, final boolean startAtOne,
			final String commentIdent, final String lineSep,
			final List<String> particleDataOrder, final File sourceFolder)
			throws IOException, IllegalArgumentException {
		if (!sourceFolder.isDirectory())
			throw new IllegalArgumentException("The destination folder: "
					+ sourceFolder + " has to be a valid directory");
		if (sourceFolder.listFiles().length == 0)
			throw new IllegalArgumentException("The destination folder: "
					+ sourceFolder + " has to be not empty");
		boolean isValid = false;
		
		Double fileCounter = 0.0;
		for (final File f : sourceFolder.listFiles()) {
			final String fName = f.getName();
			if (!fName.matches(fileNameIdent)
					&& !fName.startsWith(fileNameIdent)) {
				continue;
			}
			isValid = true;
			final FileReader fr = new FileReader(f);
			final BufferedReader br = new BufferedReader(fr);
			fileCounter++;
			this.importTrajectories(multifile, f.getName(), fileCounter,
					dataIdent, particleIdent, startAtOne, commentIdent,
					lineSep, particleDataOrder, br);
			br.close();
			fr.close();
		}
		if (!isValid)
			throw new IllegalArgumentException(
					"The source folder: "
							+ sourceFolder
							+ " has to contain at least 1 file containing the given file name identifier");
		
		final List<OmegaTrajectory> toRemoveTracks = new ArrayList<OmegaTrajectory>();
		for (final OmegaTrajectory t : this.tracks) {
			t.recalculateLength();
			if (t.getLength() < 2) {
				toRemoveTracks.add(t);
			}
		}
		this.tracks.removeAll(toRemoveTracks);
		if (this.tracks.isEmpty())
			throw new IllegalArgumentException(
					"Something wrong with parameters: no tracks have been found in "
							+ sourceFolder);
		
		final LinkedHashMap<String, String> analysisData = new LinkedHashMap<String, String>(
				this.analysisDataMap);
		final LinkedHashMap<String, String> paramData = new LinkedHashMap<String, String>(
				this.paramMap);
		final Map<OmegaPlane, List<OmegaROI>> resultingParticles = new LinkedHashMap<OmegaPlane, List<OmegaROI>>(
				this.particles);
		final Map<OmegaROI, Map<String, Object>> resultingParticlesValues = new LinkedHashMap<OmegaROI, Map<String, Object>>(
				this.particlesValues);
		final List<OmegaTrajectory> resultingTrajectories = new ArrayList<OmegaTrajectory>(
				this.tracks);
		final OmegaImporterEventResultsGeneralParticleTracking evt = new OmegaImporterEventResultsGeneralParticleTracking(
				this, this.container, analysisData, paramData,
				resultingParticles, resultingTrajectories,
				resultingParticlesValues,
				OmegaDataToolConstants.COMPLETE_CHAIN_AFTER_IMPORT);
		this.fireEvent(evt);
	}
	
	private void importParticles(final String fileName,
			final String planeIdentifier, final String particleIdent,
			final boolean startAtOne, final String commentIdent,
			final String lineSep, final List<String> particleDataOrder,
			final BufferedReader br) throws IOException,
			IllegalArgumentException {
		fileName.substring(0, fileName.lastIndexOf("."));
		String line = br.readLine();
		while (line != null) {
			if (line.isEmpty()) {
				this.isReadingParams = false;
				this.isReadingParents = false;
				line = br.readLine();
				continue;
			}
			
			if (((commentIdent != null) && !commentIdent.isEmpty())
					&& line.startsWith(commentIdent)) {
				this.importAnalysisData(this.analysisDataMap, this.paramMap,
						this.parents, commentIdent, lineSep, line);
				line = br.readLine();
				continue;
			}
			
			if (((particleIdent != null) && !particleIdent.isEmpty())
					&& line.startsWith(particleIdent)) {
				line = line.replaceFirst(particleIdent, "");
				this.importParticle(startAtOne, lineSep, particleDataOrder,
						line);
			} else {
				this.importParticle(startAtOne, lineSep, particleDataOrder,
						line);
			}
			line = br.readLine();
		}
	}
	
	private void importAnalysisData(final Map<String, String> analysisDataMap,
			final Map<String, String> paramMap,
			final Map<Integer, String> parents, final String commentIdent,
			final String lineSep, final String line) {
		String newLine;
		if ((commentIdent != null) && !commentIdent.isEmpty()) {
			newLine = line.replaceFirst(commentIdent, "");
		} else {
			newLine = line;
		}
		// newLine = newLine.replaceFirst(" ", "");
		final String[] tokens = newLine.split(lineSep);
		if (tokens[0].contains(OmegaGUIConstants.INFO_ID)) {
			analysisDataMap.put(OmegaGUIConstants.INFO_ID, tokens[1]);
		} else if (tokens[0].equals(OmegaGUIConstants.INFO_NAME)) {
			analysisDataMap.put(tokens[0], tokens[1]);
		} else if (tokens[0].equals(OmegaGUIConstants.INFO_EXECUTED)) {
			analysisDataMap.put(tokens[0], tokens[1]);
		} else if (tokens[0].equals(OmegaGUIConstants.INFO_OWNER)) {
			analysisDataMap.put(tokens[0], tokens[1]);
		} else if (tokens[0].equals(OmegaGUIConstants.INFO_ALGO)) {
			if (tokens[1].contains("[")) {
				final String[] names = newLine.split("\t");
				analysisDataMap.put(tokens[0], names[0]);
				String shortname = names[1];
				shortname = shortname.replace("[", "");
				shortname = shortname.replace("]", "");
				analysisDataMap.put(OmegaGUIConstants.INFO_SALGO, names[1]);
			} else {
				analysisDataMap.put(tokens[0], tokens[1]);
				if (tokens[2].contains("[")) {
					String shortname = tokens[2];
					shortname = shortname.replace("[", "");
					shortname = shortname.replace("]", "");
					analysisDataMap
							.put(OmegaGUIConstants.INFO_SALGO, shortname);
				}
			}
		} else if (tokens[0].equals(OmegaGUIConstants.INFO_ALGO_VERSION)) {
			analysisDataMap.put(tokens[0], tokens[1]);
		} else if (tokens[0].equals(OmegaGUIConstants.INFO_ALGO_RELEASED)) {
			analysisDataMap.put(tokens[0], tokens[1]);
		} else if (tokens[0].equals(OmegaGUIConstants.INFO_ALGO_AUTHOR)) {
			analysisDataMap.put(tokens[0], tokens[1]);
		} else if (tokens[0].equals(OmegaGUIConstants.INFO_ALGO_REF)) {
			if (tokens.length == 2) {
				analysisDataMap.put(tokens[0], tokens[1]);
			} else {
				analysisDataMap.put(tokens[0], "");
			}
		} else if (tokens[0].equals(OmegaGUIConstants.INFO_ALGO_DESC)) {
			if (tokens.length == 2) {
				analysisDataMap.put(tokens[0], tokens[1]);
			} else {
				analysisDataMap.put(tokens[0], "");
			}
		} else if (tokens[0].equals(OmegaGUIConstants.INFO_PARENTS)) {
			this.isReadingParents = true;
		} else if (tokens[0].equals(OmegaGUIConstants.INFO_PARAMS)) {
			this.isReadingParams = true;
		} else if (this.isReadingParents) {
			String parent = tokens[0];
			if (parent.contains("-")) {
				parent = parent.replaceFirst("-", "");
			}
			final String[] pars = parent.split(":");
			final String pars0 = pars[0];
			final String pars1 = pars[1];
			final Integer pars0I = Integer.valueOf(pars0);
			parents.put(pars0I, pars1);
		} else if (this.isReadingParams) {
			String param = tokens[0];
			if (param.contains("-")) {
				param = param.replaceFirst("-", "");
			}
			if (param
					.contains(OmegaAlgorithmParameterConstants.PARAM_ERROR_SNR)) {
				parents.put(OmegaDataToolConstants.PARENT_SNR, tokens[1]);
			}
			if (param.contains(OmegaAlgorithmParameterConstants.PARAM_ZSECTION)) {
				paramMap.put(OmegaAlgorithmParameterConstants.PARAM_ZSECTION,
						tokens[1]);
			} else if (param
					.contains(OmegaAlgorithmParameterConstants.PARAM_CHANNEL)) {
				paramMap.put(OmegaAlgorithmParameterConstants.PARAM_CHANNEL,
						tokens[1]);
			} else {
				paramMap.put(param, tokens[1]);
			}
		}
	}
	
	private void importTrajectories(final boolean multifile,
			final String fileName, final Double fileCounter,
			final String dataIdent, final String particleIdent,
			final boolean startAtOne, final String commentIdent,
			final String lineSep, final List<String> particleDataOrder,
			final BufferedReader br) throws IOException,
			IllegalArgumentException {
		final String name1 = fileName.substring(0, fileName.lastIndexOf("."));
		OmegaTrajectory trajectory = null;
		if (multifile) {
			trajectory = new OmegaTrajectory(-1, name1, fileCounter);
			// trajectory.setName(name1);
			this.tracks.add(trajectory);
		}
		String line = br.readLine();
		Double counter = 0.0;
		Integer trackIndex = -1;
		while (line != null) {
			if (line.isEmpty()) {
				if (trajectory != null) {
					trajectory = null;
				}
				this.isReadingParams = false;
				this.isReadingParents = false;
				line = br.readLine();
				continue;
			}
			
			if (((commentIdent != null) && !commentIdent.isEmpty())
					&& line.startsWith(commentIdent)
					&& !line.startsWith(commentIdent + dataIdent)) {
				this.importAnalysisData(this.analysisDataMap, this.paramMap,
						this.parents, commentIdent, lineSep, line);
				line = br.readLine();
				continue;
			}
			
			if (!multifile && ((dataIdent != null) && !dataIdent.isEmpty())
					&& line.startsWith(dataIdent)) {
				final String name = OmegaStringUtilities.removeSymbols(line);
				String name2 = OmegaStringUtilities.replaceWhitespaces(name,
						"_");
				if (name2.startsWith("_")) {
					name2 = name2.replaceFirst("_", "");
				}
				trajectory = new OmegaTrajectory(-1, name2, counter);
				counter++;
				// trajectory.setName(name1 + "_" + name2);
				this.tracks.add(trajectory);
			}
			
			Integer tmpTrackIndex = -1;
			if (particleDataOrder
					.contains(OmegaDataToolConstants.PARTICLE_TRACKINDEX)) {
				final int ind = particleDataOrder
						.indexOf(OmegaDataToolConstants.PARTICLE_TRACKINDEX);
				final String s = line.split(lineSep)[ind];
				tmpTrackIndex = Integer.valueOf(s);
			}
			
			if (((particleIdent != null) && !particleIdent.isEmpty())
					&& line.startsWith(particleIdent) && (trajectory != null)) {
				line = line.replaceFirst(particleIdent, "");
				final OmegaParticle p = this.importParticle(startAtOne,
						lineSep, particleDataOrder, line);
				trajectory.addROI(p);
			} else if (((dataIdent != null) && !dataIdent.isEmpty())
					&& line.startsWith(commentIdent + dataIdent)) {
				String name = line;
				name = name.replace(dataIdent, "");
				name = name.replaceFirst("\t", "");
				if (name.contains("\t")) {
					final String[] tokens = name.split("\t");
					name = tokens[1];
				}
				trajectory = new OmegaTrajectory(-1, name, counter);
				counter++;
				this.tracks.add(trajectory);
			} else if (((trajectory == null) && (trackIndex == -1))
					|| (trackIndex != tmpTrackIndex)) {
				trajectory = new OmegaTrajectory(-1, "Track" + counter, counter);
				counter++;
				this.tracks.add(trajectory);
				final OmegaParticle p = this.importParticle(startAtOne,
						lineSep, particleDataOrder, line);
				trajectory.addROI(p);
				trackIndex = tmpTrackIndex;
			} else {
				final OmegaParticle p = this.importParticle(startAtOne,
						lineSep, particleDataOrder, line);
				trajectory.addROI(p);
			}
			line = br.readLine();
		}
	}
	
	private OmegaParticle importParticle(final boolean startAtOne,
			final String lineSep, final List<String> particleDataOrder,
			final String particleToImport) throws IllegalArgumentException {
		final String[] particleData = particleToImport.split(lineSep);
		Integer frameIndex = null;
		Double x = null, y = null, peakIntensity = null, centroidIntensity = null;
		final Map<String, Object> particleValues = new LinkedHashMap<String, Object>();
		if (particleData.length < particleDataOrder.size())
			throw new IllegalArgumentException("The line: " + particleToImport
					+ " doesn't contain the required information");
		for (int i = 0; i < particleDataOrder.size(); i++) {
			final String order = particleDataOrder.get(i);
			final String data = particleData[i];
			if (order.equals(OmegaDataToolConstants.PARTICLE_SEPARATOR)) {
				continue;
			} else if (order.equals(OmegaDataToolConstants.PARTICLE_TRACKINDEX)) {
				continue;
			} else if (order.equals(OmegaDataToolConstants.PARTICLE_FRAMEINDEX)) {
				frameIndex = Integer.valueOf(data);
			} else if (order.equals(OmegaDataToolConstants.PARTICLE_XCOORD)) {
				x = Double.valueOf(data);
			} else if (order.equals(OmegaDataToolConstants.PARTICLE_YCOORD)) {
				y = Double.valueOf(data);
			} else if (order
					.equals(OmegaDataToolConstants.PARTICLE_PEAK_INTENSITY)) {
				peakIntensity = Double.valueOf(data);
			} else if (order
					.equals(OmegaDataToolConstants.PARTICLE_CENT_INTENSITY)) {
				centroidIntensity = Double.valueOf(data);
			} else if (order.equals(OmegaDataToolConstants.PARTICLE_ID)) {
				// FIXME DO NOTHING HERE ATM
				// IT SHOULD BE ABLE TO SET THE PARTICLE ID
			} else if (order.equals(OmegaDataToolConstants.PARTICLE_C)) {
				// FIXME DO NOTHING HERE ATM
				// IT SHOULD BE ABLE TO SET THE PARTICLE C
			} else if (order.equals(OmegaDataToolConstants.PARTICLE_Z)) {
				// FIXME DO NOTHING HERE ATM
				// IT SHOULD BE ABLE TO SET THE PARTICLE Z
			} else {
				// TODO ADD CHOICE OF TYPE OF CONTENT IN GUI AND HERE
				particleValues.put(order, Double.valueOf(data));
			}
		}
		if ((frameIndex == null) || (x == null) || (y == null))
			throw new IllegalArgumentException(
					"The line: "
							+ particleToImport
							+ " doesn't contain enough information for identifying an ROI");
		OmegaParticle p = null;
		if (!startAtOne) {
			frameIndex++;
		}
		if ((peakIntensity == null) && (centroidIntensity == null)) {
			p = new OmegaParticle(frameIndex, x, y, x, y);
		} else {
			if ((peakIntensity != null) && (centroidIntensity != null)) {
				p = new OmegaParticle(frameIndex, x, y, peakIntensity,
						centroidIntensity);
			} else {
				if (peakIntensity == null) {
					p = new OmegaParticle(frameIndex, x, y, x, y,
							centroidIntensity);
				} else {
					p = new OmegaParticle(frameIndex, x, y, x, y, peakIntensity);
				}
			}
		}
		
		OmegaPlane frame;
		if (this.frames.containsKey(frameIndex)) {
			frame = this.frames.get(frameIndex);
		} else {
			frame = new OmegaPlane(frameIndex);
			this.frames.put(frameIndex, frame);
		}
		
		List<OmegaROI> rois;
		if (this.particles.containsKey(frame)) {
			rois = this.particles.get(frame);
		} else {
			rois = new ArrayList<OmegaROI>();
		}
		rois.add(p);
		this.particles.put(frame, rois);
		this.particlesValues.put(p, particleValues);
		
		return p;
	}
	
	public Map<Integer, String> getParents() {
		return this.parents;
	}
	
	public Map<String, String> getAnalysisData() {
		return this.analysisDataMap;
	}
	
	public Map<String, String> getParametersData() {
		return this.paramMap;
	}
	
	public Map<Integer, OmegaPlane> getFrames() {
		return this.frames;
	}
	
	public Map<OmegaPlane, List<OmegaROI>> getParticles() {
		return this.particles;
	}
	
	public Map<OmegaROI, Map<String, Object>> getParticlesValues() {
		return this.particlesValues;
	}
	
	public List<OmegaTrajectory> getTracks() {
		return this.tracks;
	}
	
	public void reset() {
		this.frames.clear();
		this.particles.clear();
		this.particlesValues.clear();
		this.tracks.clear();
	}
	
	public void setContainer(final OmegaAnalysisRunContainerInterface container) {
		this.container = container;
	}
	
	public void setSegmentationTypes(final OmegaSegmentationTypes segmTypes) {
		this.segmTypes = segmTypes;
	}
}
