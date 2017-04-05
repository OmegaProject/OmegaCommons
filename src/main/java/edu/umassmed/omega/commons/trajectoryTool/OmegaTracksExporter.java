package edu.umassmed.omega.commons.trajectoryTool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.swing.RootPaneContainer;

import edu.umassmed.omega.commons.constants.OmegaConstants;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAlgorithmInformation;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParameter;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParticleDetectionRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParticleLinkingRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaRunDefinition;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaSNRRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresDiffusivityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresMobilityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresVelocityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrajectoriesRelinkingRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrajectoriesSegmentationRun;
import edu.umassmed.omega.commons.data.coreElements.OmegaPlane;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaParticle;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegmentationTypes;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.trajectoryTool.gui.OmegaTracksToolDialog;
import edu.umassmed.omega.commons.utilities.OmegaIOUtility;
import edu.umassmed.omega.commons.utilities.OmegaStringUtilities;

public class OmegaTracksExporter extends OmegaIOUtility {

	private OmegaParticleDetectionRun detRun;
	private OmegaParticleLinkingRun linkRun;
	private OmegaTrajectoriesRelinkingRun relinkRun;
	private OmegaTrajectoriesSegmentationRun segmRun;
	private OmegaTrackingMeasuresRun measuresRun;
	private final OmegaSNRRun snrRun;

	// private final Map<OmegaPlane, List<OmegaROI>> particles;
	// private final Map<OmegaROI, Map<String, Object>> particlesValues;
	// private final List<OmegaTrajectory> tracks;

	private OmegaTracksToolDialog dialog;

	public OmegaTracksExporter(final RootPaneContainer parent) {
		// this.particles = new LinkedHashMap<OmegaPlane, List<OmegaROI>>();
		// this.particlesValues = new LinkedHashMap<OmegaROI, Map<String,
		// Object>>();
		// this.tracks = new ArrayList<OmegaTrajectory>();
		this.detRun = null;
		this.linkRun = null;
		this.relinkRun = null;
		this.segmRun = null;
		this.measuresRun = null;
		this.snrRun = null;
		this.dialog = new OmegaTracksToolDialog(parent, true, false, this);
	}

	public OmegaTracksExporter() {
		// this.particles = new LinkedHashMap<OmegaPlane, List<OmegaROI>>();
		// this.particlesValues = new LinkedHashMap<OmegaROI, Map<String,
		// Object>>();
		// this.tracks = new ArrayList<OmegaTrajectory>();
		this.detRun = null;
		this.linkRun = null;
		this.relinkRun = null;
		this.segmRun = null;
		this.measuresRun = null;
		this.snrRun = null;
		this.dialog = null;
	}

	public void showDialog(final RootPaneContainer parent) {
		if (this.dialog == null) {
			this.dialog = new OmegaTracksToolDialog(parent, true, false, this);
		}
		if (this.measuresRun != null) {
			this.dialog.setFileName(this.measuresRun.getName());
		} else if (this.segmRun != null) {
			this.dialog.setFileName(this.segmRun.getName());
		} else if (this.relinkRun != null) {
			this.dialog.setFileName(this.relinkRun.getName());
		} else if (this.linkRun != null) {
			this.dialog.setFileName(this.linkRun.getName());
		} else if (this.snrRun != null) {
			this.dialog.setFileName(this.snrRun.getName());
		} else if (this.detRun != null) {
			this.dialog.setFileName(this.detRun.getName());
		}
		this.dialog.updateParentContainer(parent);
		this.dialog.setVisible(true);
	}

	public void export(final boolean multifile,
			final String fileNameIdentifier, final String extension,
			final String targetIdentifier, final String particleIdentifier,
			final boolean startAtOne, final String nonParticleIdentifier,
			final String particleSeparator,
			final List<String> particleDataOrder, final File sourceFolder)
			throws IOException {
		if (this.measuresRun != null) {
			this.exportAnalysisMetadata(this.measuresRun, multifile,
					fileNameIdentifier, particleSeparator,
					nonParticleIdentifier, sourceFolder, extension);
			if (this.measuresRun instanceof OmegaTrackingMeasuresMobilityRun) {
				this.exportTrackingMeasuresMobility(multifile,
						fileNameIdentifier, extension, targetIdentifier,
						particleIdentifier, startAtOne, nonParticleIdentifier,
						particleSeparator, particleDataOrder, sourceFolder);
			} else if (this.measuresRun instanceof OmegaTrackingMeasuresVelocityRun) {
				this.exportTrackingMeasuresVelocity(multifile,
						fileNameIdentifier, extension, targetIdentifier,
						particleIdentifier, startAtOne, nonParticleIdentifier,
						particleSeparator, particleDataOrder, sourceFolder);
			} else if (this.measuresRun instanceof OmegaTrackingMeasuresDiffusivityRun) {
				this.exportTrackingMeasuresDiffusivity(multifile,
						fileNameIdentifier, extension, targetIdentifier,
						particleIdentifier, startAtOne, nonParticleIdentifier,
						particleSeparator, particleDataOrder, sourceFolder);
			}
		} else if (this.segmRun != null) {
			this.exportAnalysisMetadata(this.segmRun, multifile,
					fileNameIdentifier, particleSeparator,
					nonParticleIdentifier, sourceFolder, extension);
			this.exportSegments(multifile, fileNameIdentifier, extension,
					targetIdentifier, particleIdentifier, startAtOne,
					nonParticleIdentifier, particleSeparator,
					particleDataOrder, sourceFolder);
		} else if (this.relinkRun != null) {
			this.exportAnalysisMetadata(this.relinkRun, multifile,
					fileNameIdentifier, particleSeparator,
					nonParticleIdentifier, sourceFolder, extension);
			this.exportTrajectories(multifile, fileNameIdentifier, extension,
					targetIdentifier, particleIdentifier, startAtOne,
					nonParticleIdentifier, particleSeparator,
					particleDataOrder, sourceFolder);
		} else if (this.linkRun != null) {
			this.exportAnalysisMetadata(this.linkRun, multifile,
					fileNameIdentifier, particleSeparator,
					nonParticleIdentifier, sourceFolder, extension);
			this.exportTrajectories(multifile, fileNameIdentifier, extension,
					targetIdentifier, particleIdentifier, startAtOne,
					nonParticleIdentifier, particleSeparator,
					particleDataOrder, sourceFolder);
		} else if (this.snrRun != null) {
			this.exportAnalysisMetadata(this.snrRun, multifile,
					fileNameIdentifier, particleSeparator,
					nonParticleIdentifier, sourceFolder, extension);
			this.exportSNR(multifile, fileNameIdentifier, extension,
					targetIdentifier, particleIdentifier, startAtOne,
					nonParticleIdentifier, particleSeparator,
					particleDataOrder, sourceFolder);
		} else if (this.detRun != null) {
			this.exportAnalysisMetadata(this.detRun, multifile,
					fileNameIdentifier, particleSeparator,
					nonParticleIdentifier, sourceFolder, extension);
			this.exportParticles(multifile, fileNameIdentifier, extension,
					targetIdentifier, particleIdentifier, startAtOne,
					nonParticleIdentifier, particleSeparator,
					particleDataOrder, sourceFolder);
		}
	}

	private void exportAnalysisMetadata(final OmegaAnalysisRun analysisRun,
			final boolean multifile, final String fileNameIdentifier,
			final String particleSeparator, final String nonParticleIdentifier,
			final File sourceFolder, final String extension) throws IOException {
		if (!sourceFolder.isDirectory())
			throw new IllegalArgumentException("The source folder "
					+ sourceFolder + " has to be a valid directory");
		if ((sourceFolder.listFiles().length != 0) && multifile)
			throw new IllegalArgumentException("The source folder "
					+ sourceFolder + " has to be empty");
		File f1 = null, f2 = null, f3 = null;
		if (multifile) {
			f1 = new File(sourceFolder.getPath() + File.separatorChar
					+ fileNameIdentifier + "_" + analysisRun.getName() + "."
					+ extension);
		} else {
			if ((this.measuresRun != null) || (this.snrRun != null)) {
				if ((this.measuresRun != null)
						&& (this.measuresRun instanceof OmegaTrackingMeasuresDiffusivityRun)) {
					f1 = new File(sourceFolder.getPath() + File.separatorChar
							+ fileNameIdentifier + "_local_intervals."
							+ extension);
					f2 = new File(sourceFolder.getPath() + File.separatorChar
							+ fileNameIdentifier + "_local." + extension);
					f3 = new File(sourceFolder.getPath() + File.separatorChar
							+ fileNameIdentifier + "_global." + extension);
				} else {
					f1 = new File(sourceFolder.getPath() + File.separatorChar
							+ fileNameIdentifier + "_local." + extension);
					f2 = new File(sourceFolder.getPath() + File.separatorChar
							+ fileNameIdentifier + "_global." + extension);
				}
			} else {
				f1 = new File(sourceFolder.getPath() + File.separatorChar
						+ fileNameIdentifier + "." + extension);
			}
		}
		if (f1.exists())
			throw new IllegalArgumentException("The file " + f1.getName()
					+ " already exists in " + sourceFolder);
		if ((f2 != null) && f2.exists())
			throw new IllegalArgumentException("The file " + f2.getName()
					+ " already exists in " + sourceFolder);
		if ((f3 != null) && f3.exists())
			throw new IllegalArgumentException("The file " + f3.getName()
					+ " already exists in " + sourceFolder);
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

		String newPartSep = "";
		if (particleSeparator != null) {
			newPartSep = particleSeparator;
			newPartSep = newPartSep.replace("TAB", "\t");
		}

		buf.append(nonParticleIdentifier + " "
				+ OmegaDataToolConstants.RUN_NAME + newPartSep
				+ analysisRun.getName() + "\n");
		final SimpleDateFormat format = new SimpleDateFormat(
				OmegaConstants.OMEGA_DATE_FORMAT);
		final String date = format.format(analysisRun.getTimeStamps());
		buf.append(nonParticleIdentifier + " " + OmegaDataToolConstants.RUN_ON
				+ newPartSep + date + "\n");
		buf.append(nonParticleIdentifier + " " + OmegaDataToolConstants.RUN_BY
				+ newPartSep + analysisRun.getExperimenter().getFirstName()
				+ newPartSep + analysisRun.getExperimenter().getLastName()
				+ "\n");
		final OmegaRunDefinition runDef = analysisRun.getAlgorithmSpec();
		final OmegaAlgorithmInformation algoInfo = runDef.getAlgorithmInfo();
		buf.append(nonParticleIdentifier + " "
				+ OmegaDataToolConstants.ALGORITHM + newPartSep
				+ algoInfo.getName() + "\n");
		buf.append(nonParticleIdentifier + " " + OmegaDataToolConstants.VERSION
				+ newPartSep + algoInfo.getVersion() + "\n");
		buf.append(nonParticleIdentifier + " " + OmegaDataToolConstants.AUTHOR
				+ newPartSep + algoInfo.getAuthor().getFirstName() + newPartSep
				+ algoInfo.getAuthor().getLastName() + "\n");
		final String date2 = format.format(algoInfo.getPublicationData());
		buf.append(nonParticleIdentifier + " "
				+ OmegaDataToolConstants.PUBLISHED + newPartSep + date2 + "\n");
		buf.append(nonParticleIdentifier + " "
				+ OmegaDataToolConstants.REFERENCE + newPartSep
				+ algoInfo.getReference() + "\n");
		buf.append(nonParticleIdentifier + " "
				+ OmegaDataToolConstants.DESCRIPTION + newPartSep
				+ algoInfo.getDescription() + "\n");
		buf.append(nonParticleIdentifier + " "
				+ OmegaDataToolConstants.PARAMETERS + "\n");
		for (final OmegaParameter param : runDef.getParameters()) {
			buf.append(nonParticleIdentifier + " "
					+ OmegaDataToolConstants.PARAM + newPartSep
					+ param.getName() + newPartSep + param.getValue() + "\n");
		}
		buf.append(nonParticleIdentifier + "\n");
		buf.append("\n");

		final FileWriter fw1 = new FileWriter(f1, false);
		final BufferedWriter bw1 = new BufferedWriter(fw1);
		bw1.write(buf.toString());
		bw1.close();
		fw1.close();
		if (f2 == null)
			return;
		final FileWriter fw2 = new FileWriter(f2, false);
		final BufferedWriter bw2 = new BufferedWriter(fw2);
		bw2.write(buf.toString());
		bw2.close();
		fw2.close();
		if (f3 == null)
			return;
		final FileWriter fw3 = new FileWriter(f3, false);
		final BufferedWriter bw3 = new BufferedWriter(fw3);
		bw3.write(buf.toString());
		bw3.close();
		fw3.close();
	}

	private void exportSNR(final boolean multifile,
			final String fileNameIdentifier, final String extension,
			final String planeIdentifier, final String particleIdentifier,
			final boolean startAtOne, final String nonParticleIdentifier,
			final String particleSeparator,
			final List<String> particleDataOrder, final File sourceFolder)
			throws IOException, IllegalArgumentException {
		if (!sourceFolder.isDirectory())
			throw new IllegalArgumentException("The source folder "
					+ sourceFolder + " has to be a valid directory");
		if ((sourceFolder.listFiles().length != 0) && multifile)
			throw new IllegalArgumentException("The source folder "
					+ sourceFolder + " has to be empty");
		final Map<OmegaPlane, List<OmegaROI>> particlesMap = this.detRun
				.getResultingParticles();
		final Map<OmegaPlane, Double> imageBGR = this.snrRun
				.getResultingImageBGR();
		final Map<OmegaPlane, Double> imageNoise = this.snrRun
				.getResultingImageNoise();
		final Map<OmegaPlane, Double> imageAvgSNR = this.snrRun
				.getResultingImageAverageSNR();
		final Map<OmegaPlane, Double> imageMinSNR = this.snrRun
				.getResultingImageMinimumSNR();
		final Map<OmegaPlane, Double> imageMaxSNR = this.snrRun
				.getResultingImageMaximumSNR();
		final Map<OmegaPlane, Double> imageAvgErrorIndexSNR = this.snrRun
				.getResultingImageAverageErrorIndexSNR();
		final Map<OmegaPlane, Double> imageMinErrorIndexSNR = this.snrRun
				.getResultingImageMinimumErrorIndexSNR();
		final Map<OmegaPlane, Double> imageMaxErrorIndexSNR = this.snrRun
				.getResultingImageMaximumErrorIndexSNR();
		final Map<OmegaROI, Integer> localCenterSignal = this.snrRun
				.getResultingLocalCenterSignals();
		final Map<OmegaROI, Double> localMeanSignal = this.snrRun
				.getResultingLocalMeanSignals();
		final Map<OmegaROI, Integer> localParticleArea = this.snrRun
				.getResultingLocalParticleArea();
		final Map<OmegaROI, Integer> localPeakSignal = this.snrRun
				.getResultingLocalPeakSignals();
		final Map<OmegaROI, Double> localNoise = this.snrRun
				.getResultingLocalNoises();
		final Map<OmegaROI, Double> localSNR = this.snrRun
				.getResultingLocalSNRs();
		final Map<OmegaROI, Double> localErrorIndexSNR = this.snrRun
				.getResultingLocalErrorIndexSNRs();
		String newPlaneIdent = "";
		if (newPlaneIdent != null) {
			newPlaneIdent = planeIdentifier;
			newPlaneIdent = newPlaneIdent.replace("TAB", "\t");
		}
		String newPartIdent = "";
		if (newPartIdent != null) {
			newPartIdent = particleIdentifier;
			newPartIdent = newPartIdent.replace("TAB", "\t");
		}
		String newPartSep = "\t";
		if (newPartSep != null) {
			newPartSep = particleSeparator;
			newPartSep = newPartSep.replace("TAB", "\t");
		}
		String nonPartIdent = "";
		if (nonPartIdent != null) {
			nonPartIdent = nonParticleIdentifier;
			nonPartIdent = nonPartIdent.replace("TAB", "\t");
		}
		boolean append = false;
		if (newPlaneIdent != null) {
			append = true;
		}

		int counter = 1;
		final int max = particlesMap.keySet().size();
		final int maxDigits = String.valueOf(max).length();
		for (final OmegaPlane plane : particlesMap.keySet()) {
			final File globalFile = new File(sourceFolder.getPath()
					+ File.separatorChar + fileNameIdentifier + "_global."
					+ extension);
			final String multiDigitCounter = OmegaStringUtilities
					.getMultiDigitsCounter(counter, maxDigits);
			File localFile;
			if (multifile) {
				localFile = new File(sourceFolder.getPath()
						+ File.separatorChar + fileNameIdentifier + "_"
						+ multiDigitCounter + "_Plane_" + plane.getIndex()
						+ "_local." + extension);
				if (localFile.exists())
					throw new IllegalArgumentException("The file "
							+ localFile.getName() + " already exists in "
							+ sourceFolder);
			} else {
				localFile = new File(sourceFolder.getPath()
						+ File.separatorChar + fileNameIdentifier + "_local."
						+ extension);
			}
			if (!localFile.exists()) {
				localFile.createNewFile();
			}
			if (!globalFile.exists()) {
				globalFile.createNewFile();
			}
			if (counter >= (max - 1)) {
			}

			final FileWriter fw = new FileWriter(localFile, append);
			final BufferedWriter bw = new BufferedWriter(fw);
			final FileWriter fw2 = new FileWriter(globalFile, append);
			final BufferedWriter bw2 = new BufferedWriter(fw2);
			final String planeIdent;
			if (planeIdentifier == null) {
				planeIdent = counter + "\n";
			} else {
				planeIdent = nonPartIdent + newPlaneIdent + newPartSep
						+ counter;
			}
			bw.write(planeIdent + "\n");
			bw2.write(planeIdent);
			bw2.write(newPartSep);
			bw2.write(String.valueOf(imageBGR.get(plane)));
			bw2.write(newPartSep);
			bw2.write(String.valueOf(imageNoise.get(plane)));
			bw2.write(newPartSep);
			bw2.write(String.valueOf(imageNoise.get(plane)));
			bw2.write(newPartSep);
			bw2.write(String.valueOf(imageMinSNR.get(plane)));
			bw2.write(newPartSep);
			bw2.write(String.valueOf(imageAvgSNR.get(plane)));
			bw2.write(newPartSep);
			bw2.write(String.valueOf(imageMaxSNR.get(plane)));
			bw2.write(newPartSep);
			bw2.write(String.valueOf(imageMinErrorIndexSNR.get(plane)));
			bw2.write(newPartSep);
			bw2.write(String.valueOf(imageAvgErrorIndexSNR.get(plane)));
			bw2.write(newPartSep);
			bw2.write(String.valueOf(imageMaxErrorIndexSNR.get(plane)));
			bw2.write("\n");
			for (final OmegaROI roi : particlesMap.get(plane)) {
				this.exportParticle(bw, startAtOne, newPartIdent, newPartSep,
						particleDataOrder, roi);
				bw.write(String.valueOf(localParticleArea));
				bw.write(newPartSep);
				bw.write(String.valueOf(localCenterSignal));
				bw.write(newPartSep);
				bw.write(String.valueOf(localPeakSignal));
				bw.write(newPartSep);
				bw.write(String.valueOf(localMeanSignal));
				bw.write(newPartSep);
				bw.write(String.valueOf(localNoise));
				bw.write(newPartSep);
				bw.write(String.valueOf(localSNR));
				bw.write(newPartSep);
				bw.write(String.valueOf(localErrorIndexSNR));
				bw.write(newPartSep);
				bw.write("\n");
			}

			bw.close();
			fw.close();
			bw2.close();
			fw2.close();
			counter++;
		}
		this.reset();
	}
	
	private void exportTrackingMeasuresDiffusivity(final boolean multifile,
			final String fileNameIdentifier, final String extension,
			final String trajectoryIdentifier, final String particleIdentifier,
			final boolean startAtOne, final String nonParticleIdentifier,
			final String particleSeparator,
			final List<String> particleDataOrder, final File sourceFolder)
			throws IOException, IllegalArgumentException {
		if (!sourceFolder.isDirectory())
			throw new IllegalArgumentException("The source folder "
					+ sourceFolder + " has to be a valid directory");
		if ((sourceFolder.listFiles().length != 0) && multifile)
			throw new IllegalArgumentException("The source folder "
					+ sourceFolder + " has to be empty");
		final Map<OmegaTrajectory, List<OmegaSegment>> segments = this.segmRun
				.getResultingSegments();
		final OmegaSegmentationTypes types = this.segmRun
				.getSegmentationTypes();
		final OmegaTrackingMeasuresDiffusivityRun diffRun = (OmegaTrackingMeasuresDiffusivityRun) this.measuresRun;
		final Map<OmegaSegment, Double[]> nu = diffRun.getNyResults();
		final Map<OmegaSegment, Double[][]> deltaT = diffRun.getDeltaTResults();
		final Map<OmegaSegment, Double[][]> mu = diffRun.getMuResults();
		final Map<OmegaSegment, Double[][]> logDeltaT = diffRun
				.getLogDeltaTResults();
		final Map<OmegaSegment, Double[][]> logMu = diffRun.getLogMuResults();
		diffRun.getGammaDResults();
		final Map<OmegaSegment, Double[][]> gamma = diffRun.getGammaDResults();
		final Map<OmegaSegment, Double[][]> logGamma = diffRun
				.getGammaDFromLogResults();
		diffRun.getGammaFromLogResults();
		final Map<OmegaSegment, Double[]> smss = diffRun
				.getSmssFromLogResults();
		final Map<OmegaSegment, Double[]> errors = diffRun
				.getErrosFromLogResults();
		String newTrajIdent = "";
		if (newTrajIdent != null) {
			newTrajIdent = trajectoryIdentifier;
			newTrajIdent = newTrajIdent.replace("TAB", "\t");
		}
		String newPartIdent = "";
		if (newPartIdent != null) {
			newPartIdent = particleIdentifier;
			newPartIdent = newPartIdent.replace("TAB", "\t");
		}
		String newPartSep = "\t";
		if (newPartSep != null) {
			newPartSep = particleSeparator;
			newPartSep = newPartSep.replace("TAB", "\t");
		}
		String nonPartIdent = "";
		if (nonPartIdent != null) {
			nonPartIdent = nonParticleIdentifier;
			nonPartIdent = nonPartIdent.replace("TAB", "\t");
		}
		final String begin = nonPartIdent + " ";
		boolean append = false;
		if (newTrajIdent != null) {
			append = true;
		}
		int counter = 1;
		final int max = segments.keySet().size();
		final int maxDigits = String.valueOf(max).length();
		for (final OmegaTrajectory track : segments.keySet()) {
			final File localIntervalFile, localFile, globalFile;
			final String multiDigitCounter = OmegaStringUtilities
					.getMultiDigitsCounter(counter, maxDigits);
			if (multifile) {
				localIntervalFile = new File(sourceFolder.getPath()
						+ File.separatorChar + fileNameIdentifier + "_"
						+ multiDigitCounter + "_" + track.getName()
						+ "_local_intervals." + extension);
				if (localIntervalFile.exists())
					throw new IllegalArgumentException("The file "
							+ localIntervalFile.getName()
							+ " already exists in " + sourceFolder);
				localFile = new File(sourceFolder.getPath()
						+ File.separatorChar + fileNameIdentifier + "_"
						+ multiDigitCounter + "_" + track.getName() + "_local."
						+ extension);
				if (localFile.exists())
					throw new IllegalArgumentException("The file "
							+ localFile.getName() + " already exists in "
							+ sourceFolder);
			} else {
				localIntervalFile = new File(sourceFolder.getPath()
						+ File.separatorChar + fileNameIdentifier
						+ "_local_intervals." + extension);
				localFile = new File(sourceFolder.getPath()
						+ File.separatorChar + fileNameIdentifier + "_local."
						+ extension);
			}
			globalFile = new File(sourceFolder.getPath() + File.separatorChar
					+ fileNameIdentifier + "_global." + extension);
			if (!localFile.exists()) {
				localFile.createNewFile();
			}
			boolean isLast = false;
			if (counter >= (max - 1)) {
				isLast = true;
			}

			final FileWriter fw = new FileWriter(localIntervalFile, append);
			final BufferedWriter bw = new BufferedWriter(fw);
			final FileWriter fw2 = new FileWriter(localFile, append);
			final BufferedWriter bw2 = new BufferedWriter(fw2);
			final FileWriter fw3 = new FileWriter(globalFile, true);
			final BufferedWriter bw3 = new BufferedWriter(fw3);

			final String trackIdent;
			if (trajectoryIdentifier == null) {
				trackIdent = counter + "\n";
			} else {
				trackIdent = nonPartIdent + newTrajIdent + newPartSep + counter
						+ "\n";
			}

			bw.write(trackIdent);
			bw2.write(trackIdent);
			bw3.write(trackIdent);

			for (final OmegaSegment segment : segments.get(track)) {
				final OmegaROI start = segment.getStartingROI();
				final OmegaROI end = segment.getEndingROI();
				final String segmentString = begin
						+ "Segment from "
						+ start.getFrameIndex()
						+ " to "
						+ end.getFrameIndex()
						+ newPartSep
						+ "type"
						+ newPartSep
						+ types.getSegmentationName(segment
								.getSegmentationType());
				bw.write(segmentString + "\n");
				bw2.write(segmentString + "\n");
				bw3.write(segmentString);
				bw3.write(newPartSep + logGamma.get(segment)[2][3] + newPartSep);
				if ((errors != null) && !errors.isEmpty()) {
					bw3.write(String.valueOf(errors.get(segment)[0]));
				} else {
					bw3.write(newPartSep);
				}
				bw3.write(newPartSep);
				bw3.write(String.valueOf(smss.get(segment)[0]));
				bw3.write(newPartSep);
				if ((errors != null) && !errors.isEmpty()) {
					bw3.write(String.valueOf(errors.get(segment)[1]));
				} else {
					bw3.write(newPartSep);
				}
				bw3.write("\n");

				for (final Double n : nu.get(segment)) {
					final Integer ny = new BigDecimal(n).intValue();
					final Double[] logMusNu = logMu.get(segment)[ny];
					final Double[] musNu = mu.get(segment)[ny];
					final Double[] logDeltaTNu = logDeltaT.get(segment)[ny];
					final Double[] deltaTNu = deltaT.get(segment)[ny];
					final Double[] gammaValues = gamma.get(segment)[ny];
					bw2.write(newPartIdent);
					bw2.write(newPartSep);
					bw2.write(String.valueOf(ny));
					bw2.write(newPartSep);
					for (final Double gammaVal : gammaValues) {
						bw2.write(String.valueOf(gammaVal));
						bw2.write(newPartSep);
					}
					final Double[] gammaLogValues = logGamma.get(segment)[ny];
					for (final Double gammaLogVal : gammaLogValues) {
						bw2.write(String.valueOf(gammaLogVal));
						bw2.write(newPartSep);
					}
					bw2.write("\n");
					for (int i = 0; i < logMusNu.length; i++) {
						final Double logMuLocal = logMusNu[i];
						final Double muLocal = musNu[i];
						final Double logDeltaTLocal = logDeltaTNu[i];
						final Double deltaTLocal = deltaTNu[i];
						bw.write(newPartIdent);
						bw.write(newPartSep);
						bw.write(String.valueOf(ny));
						bw.write(newPartSep);
						bw.write(String.valueOf(i + 1));
						bw.write(newPartSep);
						bw.write(String.valueOf(muLocal));
						bw.write(newPartSep);
						bw.write(String.valueOf(deltaTLocal));
						bw.write(newPartSep);
						bw.write(String.valueOf(logMuLocal));
						bw.write(newPartSep);
						bw.write(String.valueOf(logDeltaTLocal));
						bw.write(newPartSep);
						bw.write("\n");
					}
				}
			}
			if (!isLast && !multifile) {
				bw.write("\n");
			}
			bw.close();
			fw.close();
			bw2.close();
			fw2.close();
			bw3.close();
			fw3.close();
			counter++;
		}
		this.reset();
	}

	private void exportTrackingMeasuresVelocity(final boolean multifile,
			final String fileNameIdentifier, final String extension,
			final String trajectoryIdentifier, final String particleIdentifier,
			final boolean startAtOne, final String nonParticleIdentifier,
			final String particleSeparator,
			final List<String> particleDataOrder, final File sourceFolder)
			throws IOException, IllegalArgumentException {
		if (!sourceFolder.isDirectory())
			throw new IllegalArgumentException("The source folder "
					+ sourceFolder + " has to be a valid directory");
		if ((sourceFolder.listFiles().length != 0) && multifile)
			throw new IllegalArgumentException("The source folder "
					+ sourceFolder + " has to be empty");
		final Map<OmegaTrajectory, List<OmegaSegment>> segments = this.segmRun
				.getResultingSegments();
		final OmegaSegmentationTypes types = this.segmRun
				.getSegmentationTypes();
		final OmegaTrackingMeasuresVelocityRun veloRun = (OmegaTrackingMeasuresVelocityRun) this.measuresRun;
		final Map<OmegaSegment, List<Double>> localSpeeds = veloRun
				.getLocalSpeedFromOriginResults();
		final Map<OmegaSegment, List<Double>> localVelocities = veloRun
				.getLocalVelocityFromOriginResults();
		final Map<OmegaSegment, Double> globalSpeeds = veloRun
				.getAverageCurvilinearSpeedMapResults();
		final Map<OmegaSegment, Double> globalVelocities = veloRun
				.getAverageStraightLineVelocityMapResults();
		String newTrajIdent = "";
		if (newTrajIdent != null) {
			newTrajIdent = trajectoryIdentifier;
			newTrajIdent = newTrajIdent.replace("TAB", "\t");
		}
		String newPartIdent = "";
		if (newPartIdent != null) {
			newPartIdent = particleIdentifier;
			newPartIdent = newPartIdent.replace("TAB", "\t");
		}
		String newPartSep = "\t";
		if (newPartSep != null) {
			newPartSep = particleSeparator;
			newPartSep = newPartSep.replace("TAB", "\t");
		}
		String nonPartIdent = "";
		if (nonPartIdent != null) {
			nonPartIdent = nonParticleIdentifier;
			nonPartIdent = nonPartIdent.replace("TAB", "\t");
		}
		final String begin = nonPartIdent + " ";
		boolean append = false;
		if (newTrajIdent != null) {
			append = true;
		}
		if (newTrajIdent != null) {
			append = true;
		}
		int counter = 1;
		final int max = segments.keySet().size();
		final int maxDigits = String.valueOf(max).length();
		for (final OmegaTrajectory track : segments.keySet()) {
			final File localFile, globalFile;
			final String multiDigitCounter = OmegaStringUtilities
					.getMultiDigitsCounter(counter, maxDigits);
			if (multifile) {
				localFile = new File(sourceFolder.getPath()
						+ File.separatorChar + fileNameIdentifier + "_"
						+ multiDigitCounter + "_" + track.getName() + "_local."
						+ extension);
				if (localFile.exists())
					throw new IllegalArgumentException("The file "
							+ localFile.getName() + " already exists in "
							+ sourceFolder);
			} else {
				localFile = new File(sourceFolder.getPath()
						+ File.separatorChar + fileNameIdentifier + "_local."
						+ extension);
			}
			globalFile = new File(sourceFolder.getPath() + File.separatorChar
					+ fileNameIdentifier + "_global." + extension);
			if (!localFile.exists()) {
				localFile.createNewFile();
			}
			boolean isLast = false;
			if (counter >= (max - 1)) {
				isLast = true;
			}
			final FileWriter fw = new FileWriter(localFile, append);
			final BufferedWriter bw = new BufferedWriter(fw);
			final FileWriter fw2 = new FileWriter(globalFile, true);
			final BufferedWriter bw2 = new BufferedWriter(fw2);

			final String trackIdent;
			if (trajectoryIdentifier == null) {
				trackIdent = counter + "\n";
			} else {
				trackIdent = nonPartIdent + newTrajIdent + newPartSep + counter
						+ "\n";
			}
			bw.write(trackIdent);
			bw2.write(trackIdent);

			for (final OmegaSegment segment : segments.get(track)) {
				boolean isSegment = false;
				final OmegaROI start = segment.getStartingROI();
				final OmegaROI end = segment.getEndingROI();

				final String segmentString = begin
						+ "Segment from "
						+ start.getFrameIndex()
						+ " to "
						+ end.getFrameIndex()
						+ newPartSep
						+ "type"
						+ newPartSep
						+ types.getSegmentationName(segment
								.getSegmentationType());
				bw.write(segmentString + "\n");
				bw2.write(segmentString);
				bw2.write(newPartSep + globalSpeeds.get(segment) + newPartSep
						+ globalVelocities.get(segment) + "\n");

				int segmCounter = 0;
				for (final OmegaROI roi : track.getROIs()) {
					if (roi == start) {
						isSegment = true;
					} else if (roi == end) {
						isSegment = false;
						this.exportParticle(bw, startAtOne, newPartIdent,
								newPartSep, particleDataOrder, roi);
						bw.write(String.valueOf(localSpeeds.get(segment).get(
								segmCounter)));
						bw.write(newPartSep);
						bw.write(String.valueOf(localVelocities.get(segment)
								.get(segmCounter)));
						bw.write(newPartSep);
						bw.write("\n");
						segmCounter++;
						break;
					}
					if (isSegment) {
						this.exportParticle(bw, startAtOne, newPartIdent,
								newPartSep, particleDataOrder, roi);
						bw.write(String.valueOf(localSpeeds.get(segment).get(
								segmCounter)));
						bw.write(newPartSep);
						bw.write(String.valueOf(localVelocities.get(segment)
								.get(segmCounter)));
						bw.write(newPartSep);
						bw.write("\n");
						segmCounter++;
					}
				}
			}
			if (!isLast && !multifile) {
				bw.write("\n");
			}
			bw.close();
			fw.close();
			bw2.close();
			fw2.close();
			counter++;
		}
		this.reset();
	}

	private void exportTrackingMeasuresMobility(final boolean multifile,
			final String fileNameIdentifier, final String extension,
			final String trajectoryIdentifier, final String particleIdentifier,
			final boolean startAtOne, final String nonParticleIdentifier,
			final String particleSeparator,
			final List<String> particleDataOrder, final File sourceFolder)
			throws IOException, IllegalArgumentException {
		if (!sourceFolder.isDirectory())
			throw new IllegalArgumentException("The source folder "
					+ sourceFolder + " has to be a valid directory");
		if ((sourceFolder.listFiles().length != 0) && multifile)
			throw new IllegalArgumentException("The source folder "
					+ sourceFolder + " has to be empty");
		final Map<OmegaTrajectory, List<OmegaSegment>> segments = this.segmRun
				.getResultingSegments();
		final OmegaSegmentationTypes types = this.segmRun
				.getSegmentationTypes();
		final OmegaTrackingMeasuresMobilityRun mobiRun = (OmegaTrackingMeasuresMobilityRun) this.measuresRun;
		final Map<OmegaSegment, List<Double>> distances = mobiRun
				.getDistancesFromOriginResults();
		final Map<OmegaSegment, List<Double>> displacements = mobiRun
				.getDisplacementsFromOriginResults();
		final Map<OmegaSegment, List<Double>> confinements = mobiRun
				.getConfinementRatioResults();
		final Map<OmegaSegment, List<Double[]>> angle = mobiRun
				.getAnglesAndDirectionalChangesResults();
		final Map<OmegaSegment, Double> maxDisplacement = mobiRun
				.getMaxDisplacementsFromOriginResults();
		final Map<OmegaSegment, List<Double>> totalTimeTraveled = mobiRun
				.getTimeTraveledResults();
		int counter = 1;
		final int max = segments.keySet().size();
		final int maxDigits = String.valueOf(max).length();
		String newTrajIdent = "";
		if (newTrajIdent != null) {
			newTrajIdent = trajectoryIdentifier;
			newTrajIdent = newTrajIdent.replace("TAB", "\t");
		}
		String newPartIdent = "";
		if (newPartIdent != null) {
			newPartIdent = particleIdentifier;
			newPartIdent = newPartIdent.replace("TAB", "\t");
		}
		String newPartSep = "\t";
		if (newPartSep != null) {
			newPartSep = particleSeparator;
			newPartSep = newPartSep.replace("TAB", "\t");
		}
		String nonPartIdent = "";
		if (nonPartIdent != null) {
			nonPartIdent = nonParticleIdentifier;
			nonPartIdent = nonPartIdent.replace("TAB", "\t");
		}
		final String begin = nonPartIdent + " ";
		boolean append = false;
		if (newTrajIdent != null) {
			append = true;
		}
		for (final OmegaTrajectory track : segments.keySet()) {
			final File localFile, globalFile;
			final String multiDigitCounter = OmegaStringUtilities
					.getMultiDigitsCounter(counter, maxDigits);
			if (multifile) {
				localFile = new File(sourceFolder.getPath()
						+ File.separatorChar + fileNameIdentifier + "_"
						+ multiDigitCounter + "_" + track.getName() + "_local."
						+ extension);
				if (localFile.exists())
					throw new IllegalArgumentException("The file "
							+ localFile.getName() + " already exists in "
							+ sourceFolder);
			} else {
				localFile = new File(sourceFolder.getPath()
						+ File.separatorChar + fileNameIdentifier + "_local."
						+ extension);
			}
			globalFile = new File(sourceFolder.getPath() + File.separatorChar
					+ fileNameIdentifier + "_global." + extension);
			if (!localFile.exists()) {
				localFile.createNewFile();
			}
			boolean isLast = false;
			if (counter >= (max - 1)) {
				isLast = true;
			}

			final FileWriter fw = new FileWriter(localFile, append);
			final BufferedWriter bw = new BufferedWriter(fw);
			final FileWriter fw2 = new FileWriter(globalFile, true);
			final BufferedWriter bw2 = new BufferedWriter(fw2);

			final String trackIdent;
			if (trajectoryIdentifier == null) {
				trackIdent = counter + "\n";
			} else {
				trackIdent = nonPartIdent + newTrajIdent + newPartSep + counter
						+ "\n";
			}
			bw.write(trackIdent);
			bw2.write(trackIdent);

			for (final OmegaSegment segment : segments.get(track)) {
				boolean isSegment = false;
				final OmegaROI start = segment.getStartingROI();
				final OmegaROI end = segment.getEndingROI();

				final String segmentString = begin
						+ "Segment from "
						+ start.getFrameIndex()
						+ " to "
						+ end.getFrameIndex()
						+ newPartSep
						+ "type"
						+ newPartSep
						+ types.getSegmentationName(segment
								.getSegmentationType());
				bw.write(segmentString + "\n");
				bw2.write(segmentString);
				bw2.write(newPartSep
						+ String.valueOf(maxDisplacement.get(segment)) + "\n");

				int segmCounter = 0;
				for (final OmegaROI roi : track.getROIs()) {
					if (roi == start) {
						isSegment = true;
					} else if (roi == end) {
						isSegment = false;
						this.exportParticle(bw, startAtOne, newPartIdent,
								newPartSep, particleDataOrder, roi);
						bw.write(String.valueOf(totalTimeTraveled.get(segment)
								.get(segmCounter)));
						bw.write(newPartSep);
						bw.write(String.valueOf(distances.get(segment).get(
								segmCounter)));
						bw.write(newPartSep);
						bw.write(String.valueOf(displacements.get(segment).get(
								segmCounter)));
						bw.write(newPartSep);
						bw.write(String.valueOf(confinements.get(segment).get(
								segmCounter)));
						bw.write(newPartSep);
						bw.write(String.valueOf(angle.get(segment).get(
								segmCounter)[0]));
						bw.write(newPartSep);
						bw.write(String.valueOf(angle.get(segment).get(
								segmCounter)[1]));
						bw.write(newPartSep);
						bw.write("\n");
						segmCounter++;
						break;
					}
					if (isSegment) {
						this.exportParticle(bw, startAtOne, newPartIdent,
								newPartSep, particleDataOrder, roi);
						bw.write(String.valueOf(distances.get(segment).get(
								segmCounter)));
						bw.write(newPartSep);
						bw.write(String.valueOf(displacements.get(segment).get(
								segmCounter)));
						bw.write(newPartSep);
						bw.write(String.valueOf(confinements.get(segment).get(
								segmCounter)));
						bw.write(newPartSep);
						bw.write(String.valueOf(angle.get(segment).get(
								segmCounter)[0]));
						bw.write(newPartSep);
						bw.write(String.valueOf(angle.get(segment).get(
								segmCounter)[1]));
						bw.write(newPartSep);
						bw.write("\n");
						segmCounter++;
					}
				}
			}
			if (!isLast && !multifile) {
				bw.write("\n");
			}
			bw.close();
			fw.close();
			bw2.close();
			fw2.close();
			counter++;
		}
		this.reset();
	}

	private void exportSegments(final boolean multifile,
			final String fileNameIdentifier, final String extension,
			final String trajectoryIdentifier, final String particleIdentifier,
			final boolean startAtOne, final String nonParticleIdentifier,
			final String particleSeparator,
			final List<String> particleDataOrder, final File sourceFolder)
			throws IOException, IllegalArgumentException {
		if (!sourceFolder.isDirectory())
			throw new IllegalArgumentException("The source folder "
					+ sourceFolder + " has to be a valid directory");
		if ((sourceFolder.listFiles().length != 0) && multifile)
			throw new IllegalArgumentException("The source folder "
					+ sourceFolder + " has to be empty");
		final Map<OmegaTrajectory, List<OmegaSegment>> segments = this.segmRun
				.getResultingSegments();
		final OmegaSegmentationTypes types = this.segmRun
				.getSegmentationTypes();
		int counter = 1;
		final int max = segments.keySet().size();
		final int maxDigits = String.valueOf(max).length();
		String newTrajIdent = "";
		if (newTrajIdent != null) {
			newTrajIdent = trajectoryIdentifier;
			newTrajIdent = newTrajIdent.replace("TAB", "\t");
		}
		String newPartIdent = "";
		if (newPartIdent != null) {
			newPartIdent = particleIdentifier;
			newPartIdent = newPartIdent.replace("TAB", "\t");
		}
		String newPartSep = "\t";
		if (newPartSep != null) {
			newPartSep = particleSeparator;
			newPartSep = newPartSep.replace("TAB", "\t");
		}
		String nonPartIdent = "";
		if (nonPartIdent != null) {
			nonPartIdent = nonParticleIdentifier;
			nonPartIdent = nonPartIdent.replace("TAB", "\t");
		}
		final String begin = nonPartIdent + " ";
		boolean append = false;
		if (newTrajIdent != null) {
			append = true;
		}
		for (final OmegaTrajectory track : segments.keySet()) {
			final File f;
			final String multiDigitCounter = OmegaStringUtilities
					.getMultiDigitsCounter(counter, maxDigits);
			if (multifile) {
				f = new File(sourceFolder.getPath() + File.separatorChar
						+ fileNameIdentifier + "_" + multiDigitCounter + "_"
						+ track.getName() + "." + extension);
				if (f.exists())
					throw new IllegalArgumentException("The file "
							+ f.getName() + " already exists in "
							+ sourceFolder);
			} else {
				f = new File(sourceFolder.getPath() + File.separatorChar
						+ fileNameIdentifier + "." + extension);
			}
			if (!f.exists()) {
				f.createNewFile();
			}
			boolean isLast = false;
			if (counter >= (max - 1)) {
				isLast = true;
			}

			final FileWriter fw = new FileWriter(f, append);
			final BufferedWriter bw = new BufferedWriter(fw);

			final String trackIdent;
			if (trajectoryIdentifier == null) {
				trackIdent = counter + "\n";
			} else {
				trackIdent = nonPartIdent + newTrajIdent + newPartSep + counter
						+ "\n";
			}
			bw.write(trackIdent);

			for (final OmegaSegment segment : segments.get(track)) {
				boolean isSegment = false;
				final OmegaROI start = segment.getStartingROI();
				final OmegaROI end = segment.getEndingROI();

				bw.write(begin
						+ "Segment from "
						+ start.getFrameIndex()
						+ " to "
						+ end.getFrameIndex()
						+ newPartSep
						+ "type"
						+ newPartSep
						+ types.getSegmentationName(segment
								.getSegmentationType()) + "\n");

				for (final OmegaROI roi : track.getROIs()) {
					if (roi == start) {
						isSegment = true;
					} else if (roi == end) {
						isSegment = false;
						this.exportParticle(bw, startAtOne, newPartIdent,
								newPartSep, particleDataOrder, roi);
						bw.write("\n");

					}
					if (isSegment) {
						this.exportParticle(bw, startAtOne, newPartIdent,
								newPartSep, particleDataOrder, roi);
						bw.write("\n");
					}
				}
			}
			if (!isLast && !multifile) {
				bw.write("\n");
			}
			bw.close();
			fw.close();
			counter++;

		}
		this.reset();
	}

	// TODO change IllegalArgumentException with a custom exception
	private void exportTrajectories(final boolean multifile,
			final String fileNameIdentifier, final String extension,
			final String trajectoryIdentifier, final String particleIdentifier,
			final boolean startAtOne, final String nonParticleIdentifier,
			final String particleSeparator,
			final List<String> particleDataOrder, final File sourceFolder)
			throws IOException, IllegalArgumentException {
		if (!sourceFolder.isDirectory())
			throw new IllegalArgumentException("The source folder "
					+ sourceFolder + " has to be a valid directory");
		if ((sourceFolder.listFiles().length != 0) && multifile)
			throw new IllegalArgumentException("The source folder "
					+ sourceFolder + " has to be empty");
		String newTrajIdent = "";
		if (newTrajIdent != null) {
			newTrajIdent = trajectoryIdentifier;
			newTrajIdent = newTrajIdent.replace("TAB", "\t");
		}
		String newPartIdent = "";
		if (newPartIdent != null) {
			newPartIdent = particleIdentifier;
			newPartIdent = newPartIdent.replace("TAB", "\t");
		}
		String newPartSep = "\t";
		if (newPartSep != null) {
			newPartSep = particleSeparator;
			newPartSep = newPartSep.replace("TAB", "\t");
		}
		String nonPartIdent = "";
		if (nonPartIdent != null) {
			nonPartIdent = nonParticleIdentifier;
			nonPartIdent = nonPartIdent.replace("TAB", "\t");
		}
		boolean append = false;
		if (newTrajIdent != null) {
			append = true;
		}
		final List<OmegaTrajectory> tracks;

		if (this.relinkRun == null) {
			tracks = this.linkRun.getResultingTrajectories();
		} else {
			tracks = this.relinkRun.getResultingTrajectories();
		}
		int counter = 1;
		final int max = tracks.size();
		final int maxDigits = String.valueOf(max).length();

		for (final OmegaTrajectory track : tracks) {
			final File f;
			final String multiDigitCounter = OmegaStringUtilities
					.getMultiDigitsCounter(counter, maxDigits);
			if (multifile) {
				f = new File(sourceFolder.getPath() + File.separatorChar
						+ fileNameIdentifier + "_" + multiDigitCounter + "_"
						+ track.getName() + "." + extension);
				if (f.exists())
					throw new IllegalArgumentException("The file "
							+ f.getName() + " already exists in "
							+ sourceFolder);
			} else {
				f = new File(sourceFolder.getPath() + File.separatorChar
						+ fileNameIdentifier + "." + extension);
			}
			if (!f.exists()) {
				f.createNewFile();
			}
			boolean isLast = false;
			if (tracks.indexOf(track) == (tracks.size() - 1)) {
				isLast = true;
			}

			final FileWriter fw = new FileWriter(f, append);
			final BufferedWriter bw = new BufferedWriter(fw);

			final String trackIdent;
			if (trajectoryIdentifier == null) {
				trackIdent = counter + "\n";
			} else {
				trackIdent = nonPartIdent + newTrajIdent + newPartSep + counter
						+ "\n";
			}
			bw.write(trackIdent);

			for (final OmegaROI roi : track.getROIs()) {
				this.exportParticle(bw, startAtOne, newPartIdent, newPartSep,
						particleDataOrder, roi);
				bw.write("\n");
			}

			if (!isLast && !multifile) {
				bw.write("\n");
			}

			bw.close();
			fw.close();
			counter++;
		}

		this.reset();
	}

	private void exportParticles(final boolean multifile,
			final String fileNameIdentifier, final String extension,
			final String planeIdentifier, final String particleIdentifier,
			final boolean startAtOne, final String nonParticleIdentifier,
			final String particleSeparator,
			final List<String> particleDataOrder, final File sourceFolder)
			throws IOException {
		if (!sourceFolder.isDirectory())
			throw new IllegalArgumentException("The source folder "
					+ sourceFolder + " has to be a valid directory");
		// if ((sourceFolder.listFiles().length != 0) && multifile)
		// throw new IllegalArgumentException("The source folder "
		// + sourceFolder + " has to be empty");
		final Map<OmegaPlane, List<OmegaROI>> particles = this.detRun
				.getResultingParticles();
		int counter = 1;
		final int max = particles.size();
		final int maxDigits = String.valueOf(max).length();
		String newPlaneIdent = "";
		if (newPlaneIdent != null) {
			newPlaneIdent = planeIdentifier;
			newPlaneIdent = newPlaneIdent.replace("TAB", "\t");
		}
		String newPartIdent = "";
		if (newPartIdent != null) {
			newPartIdent = particleIdentifier;
			newPartIdent = newPartIdent.replace("TAB", "\t");
		}
		String newPartSep = "\t";
		if (newPartSep != null) {
			newPartSep = particleSeparator;
			newPartSep = newPartSep.replace("TAB", "\t");
		}
		String nonPartIdent = "";
		if (nonPartIdent != null) {
			nonPartIdent = nonParticleIdentifier;
			nonPartIdent = nonPartIdent.replace("TAB", "\t");
		}
		boolean append = false;
		if (newPlaneIdent != null) {
			append = true;
		}
		while (counter <= max) {
			for (final OmegaPlane plane : particles.keySet()) {
				if (plane.getIndex() != (counter - 1)) {
					continue;
				}
				final File f;
				final String multiDigitCounter = OmegaStringUtilities
						.getMultiDigitsCounter(counter, maxDigits);
				if (multifile) {
					f = new File(sourceFolder.getPath() + File.separatorChar
							+ fileNameIdentifier + "_" + multiDigitCounter
							+ "_" + planeIdentifier + "_" + counter + "."
							+ extension);
					if (f.exists())
						throw new IllegalArgumentException("The file "
								+ f.getName() + " already exists in "
								+ sourceFolder);
				} else {
					f = new File(sourceFolder.getPath() + File.separatorChar
							+ fileNameIdentifier + "." + extension);
				}
				if (!f.exists()) {
					f.createNewFile();
				}
				boolean isLast = false;
				if (counter == max) {
					isLast = true;
				}

				final FileWriter fw = new FileWriter(f, append);
				final BufferedWriter bw = new BufferedWriter(fw);

				final String planeIdent;
				if (planeIdentifier == null) {
					planeIdent = counter + "\n";
				} else {
					planeIdent = nonPartIdent + newPlaneIdent + newPartSep
							+ counter + "\n";
				}
				bw.write(planeIdent);

				for (final OmegaROI roi : particles.get(plane)) {
					this.exportParticle(bw, startAtOne, newPartIdent,
							newPartSep, particleDataOrder, roi);
					bw.write("\n");
				}

				if (!isLast && !multifile) {
					bw.write("\n");
				}

				bw.close();
				fw.close();
				counter++;
				break;
			}
		}
		this.reset();
	}

	private void exportParticle(final BufferedWriter bw,
			final boolean startAtOne, final String newPartIdent,
			final String newPartSep, final List<String> particleDataOrder,
			final OmegaROI roi) throws IOException {
		final StringBuffer buf = new StringBuffer();
		buf.append(newPartIdent);
		Integer frameIndex = roi.getFrameIndex();
		if (startAtOne) {
			frameIndex += 1;
		}
		final Map<OmegaROI, Map<String, Object>> particlesValues = this.detRun
				.getResultingParticlesValues();
		final Map<String, Object> values = particlesValues.get(roi);
		for (final String order : particleDataOrder) {
			if (order.equals(OmegaDataToolConstants.PARTICLE_SEPARATOR)) {
				buf.append(newPartSep);
			} else if (order.equals(OmegaDataToolConstants.PARTICLE_FRAMEINDEX)) {
				buf.append(frameIndex);
			} else if (order.equals(OmegaDataToolConstants.PARTICLE_XCOORD)) {
				buf.append(roi.getX());
			} else if (order.equals(OmegaDataToolConstants.PARTICLE_YCOORD)) {
				buf.append(roi.getY());
			} else if (order
					.equals(OmegaDataToolConstants.PARTICLE_CENT_INTENSITY)) {
				if (!(roi instanceof OmegaParticle)) {
					// TODO problem
				}
				buf.append(((OmegaParticle) roi).getCentroidIntensity());
			} else if (order
					.equals(OmegaDataToolConstants.PARTICLE_PEAK_INTENSITY)) {
				if (!(roi instanceof OmegaParticle)) {
					// TODO problem
				}
				buf.append(((OmegaParticle) roi).getPeakIntensity());
			} else if (values.containsKey(order)) {
				buf.append(values.get(order));
			} else {
				// TODO error?
			}
			buf.append(newPartSep);
		}
		bw.write(buf.toString());
	}

	public void setParticleDetectionRun(final OmegaParticleDetectionRun detRun) {
		this.detRun = detRun;
	}

	public void setParticleLinkingRun(final OmegaParticleLinkingRun linkRun) {
		this.linkRun = linkRun;
	}

	public void setTrackRelinkingRun(
			final OmegaTrajectoriesRelinkingRun relinkRun) {
		this.relinkRun = relinkRun;
	}

	public void setTrackSegmentationRun(
			final OmegaTrajectoriesSegmentationRun segmRun) {
		this.segmRun = segmRun;
	}

	public void setTrackingMeasuresRun(
			final OmegaTrackingMeasuresRun measuresRun) {
		this.measuresRun = measuresRun;
	}

	public void reset() {
		this.detRun = null;
		this.linkRun = null;
		this.relinkRun = null;
		this.segmRun = null;
		this.measuresRun = null;
	}
}
