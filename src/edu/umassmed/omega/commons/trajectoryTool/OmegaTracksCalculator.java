package edu.umassmed.omega.commons.trajectoryTool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.constants.OmegaConstants;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParameter;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.eventSystem.events.OmegaMessageEvent;
import edu.umassmed.omega.commons.gui.interfaces.OmegaMessageDisplayerPanelInterface;
import edu.umassmed.omega.commons.runnable.OmegaDiffusivityAnalyzer;
import edu.umassmed.omega.commons.utilities.OmegaAlgorithmsUtilities;
import edu.umassmed.omega.commons.utilities.OmegaTrajectoryIOUtility;

public class OmegaTracksCalculator extends OmegaTrajectoryIOUtility implements
OmegaMessageDisplayerPanelInterface {

	public static void main(final String[] args) {
		final OmegaTracksCalculator otc = new OmegaTracksCalculator();
		otc.computeAndWriteValues();
	}

	final Map<Double, Map<Integer, Map<Double, Map<Double, List<Double>>>>> smssOutput;
	final Map<Double, Map<Integer, Map<Double, Map<Double, List<Double>>>>> dOutput;

	private final String readDirName, writeDirName, subDirName1, subDirName2,
	fileName, trajIdent, particleIdent, nonParticleIdent, particleSep;
	private final File dir, omegaSMSSDir, omegaDDir;
	private final List<String> dataOrder;

	private final String windowDivisor, logOption, computeError;
	private final OmegaTracksImporter oti;

	public OmegaTracksCalculator() {
		this.windowDivisor = OmegaConstants.PARAMETER_DIFFUSIVITY_WINDOW_3;
		this.logOption = OmegaConstants.PARAMETER_DIFFUSIVITY_LOG_OPTION_LOG_ONLY;
		this.computeError = OmegaConstants.PARAMETER_ERROR_OPTION_DISABLED;
		this.readDirName = "E:\\2014-10-06_TrajectoryGeneratorValidation_NoNoise";
		this.writeDirName = "E:\\2015-10-16_OmegaSMSSAndDValidation_NoNoise_WD3";
		this.dir = new File(this.readDirName);
		final String omegaSMSSDirName = this.writeDirName + "\\omegaSMSS";
		final String omegaDDirName = this.writeDirName + "\\omegaD";
		this.omegaSMSSDir = new File(omegaSMSSDirName);
		this.omegaSMSSDir.mkdir();
		this.omegaDDir = new File(omegaDDirName);
		this.omegaDDir.mkdir();
		this.subDirName1 = "tracks_[\\d-]+";
		this.subDirName2 = "L_20_SMSS_[\\d-]+_D_[\\d-]+";
		// this.subDirName2 = "L_[\\d]+_SMSS_[\\d-]+_D_[\\d-]+";
		this.dataOrder = new ArrayList<String>();
		this.dataOrder.add(OmegaTracksImporter.PARTICLE_FRAMEINDEX);
		this.dataOrder.add(OmegaTracksImporter.PARTICLE_XCOORD);
		this.dataOrder.add(OmegaTracksImporter.PARTICLE_YCOORD);
		this.fileName = "track_[\\d]+.out";
		this.trajIdent = null;
		this.particleIdent = null;
		this.nonParticleIdent = null;
		this.particleSep = "\t";
		this.oti = new OmegaTracksImporter();
		this.smssOutput = new LinkedHashMap<>();
		this.dOutput = new LinkedHashMap<>();
	}

	public void computeAndWriteValues() {
		this.computeValues();
		this.writeValues();
	}

	private void computeValues() {
		try {
			for (final File f1 : this.dir.listFiles()) {
				if (!f1.isDirectory()) {
					continue;
				}
				final String fName1 = f1.getName();
				if (!fName1.matches(this.subDirName1)) {
					continue;
				}
				final String[] vals1 = fName1.split("_");
				final Double snr = Double.valueOf(vals1[1].replace("-", "."));
				for (final File f2 : f1.listFiles()) {
					if (!f2.isDirectory()) {
						continue;
					}
					final String fName2 = f2.getName();
					if (!fName2.matches(this.subDirName2)) {
						continue;
					}
					final String[] vals2 = fName2.split("_");
					final Integer L = Integer.valueOf(vals2[1]);
					final Double SMSS = Double.valueOf(vals2[3].replace("-",
							"."));
					final Double D = Double.valueOf(vals2[5].replace("-", "."));
					System.out.println(Calendar.getInstance().getTime()
							+ " import SNR " + snr + " L " + L + " SMSS "
							+ SMSS + " D " + D);
					this.oti.reset();
					this.oti.importTrajectories(this.fileName, this.trajIdent,
							this.particleIdent, false, this.nonParticleIdent,
							this.particleSep, this.dataOrder, f2);
					final List<OmegaTrajectory> tracks = this.oti.getTracks();
					System.out.println(Calendar.getInstance().getTime() + " "
					        + tracks.size() + " trajectory imported...");

					Map<Integer, Map<Double, Map<Double, List<Double>>>> lSMSSMap;
					if (this.smssOutput.containsKey(snr)) {
						lSMSSMap = this.smssOutput.get(snr);
					} else {
						lSMSSMap = new LinkedHashMap<>();
					}
					Map<Double, Map<Double, List<Double>>> smssSMSSMap;
					if (lSMSSMap.containsKey(L)) {
						smssSMSSMap = lSMSSMap.get(L);
					} else {
						smssSMSSMap = new LinkedHashMap<>();
					}
					Map<Double, List<Double>> dSMSSMap;
					if (smssSMSSMap.containsKey(SMSS)) {
						dSMSSMap = smssSMSSMap.get(SMSS);
					} else {
						dSMSSMap = new LinkedHashMap<>();
					}
					List<Double> outputSMSS;
					if (dSMSSMap.containsKey(D)) {
						outputSMSS = dSMSSMap.get(D);
					} else {
						outputSMSS = new ArrayList<>();
					}

					Map<Integer, Map<Double, Map<Double, List<Double>>>> lDMap;
					if (this.dOutput.containsKey(snr)) {
						lDMap = this.dOutput.get(snr);
					} else {
						lDMap = new LinkedHashMap<>();
					}
					Map<Double, Map<Double, List<Double>>> smssDMap;
					if (lDMap.containsKey(L)) {
						smssDMap = lDMap.get(L);
					} else {
						smssDMap = new LinkedHashMap<>();
					}
					Map<Double, List<Double>> dDMap;
					if (smssDMap.containsKey(SMSS)) {
						dDMap = smssDMap.get(SMSS);
					} else {
						dDMap = new LinkedHashMap<>();
					}
					List<Double> outputD;
					if (dDMap.containsKey(D)) {
						outputD = dDMap.get(D);
					} else {
						outputD = new ArrayList<>();
					}

					final Map<OmegaTrajectory, List<OmegaSegment>> segments = OmegaAlgorithmsUtilities
							.createDefaultSegmentation(tracks);
					if (segments.keySet().size() > 1000) {
						System.out.println("ERROR");
					}
					System.out
					        .println(Calendar.getInstance().getTime() + " "
					                + segments.keySet().size()
					                + " segments created...");
					final List<OmegaParameter> params = new ArrayList<OmegaParameter>();
					params.add(new OmegaParameter(
					        OmegaConstants.PARAMETER_DIFFUSIVITY_WINDOW,
					        this.windowDivisor));
					params.add(new OmegaParameter(
					        OmegaConstants.PARAMETER_DIFFUSIVITY_LOG_OPTION,
							this.logOption));
					params.add(new OmegaParameter(
					        OmegaConstants.PARAMETER_ERROR_OPTION,
							this.computeError));
					final OmegaDiffusivityAnalyzer analyzer = new OmegaDiffusivityAnalyzer(
							this, segments, params);
					final Thread t = new Thread(analyzer);
					t.start();
					t.setName("analyzer_SNR_" + snr + "_L_" + L + "_SMSS_"
							+ SMSS + "_D_" + D);
					System.out.println(Calendar.getInstance().getTime()
					        + " analyzer started...");

					int counter = 0;
					while (t.isAlive()) {
						try {
							Thread.sleep(60000);
						} catch (final InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						counter++;
					}
					try {
						t.join();
					} catch (final InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println();
					System.out
					.println(Calendar.getInstance().getTime()
							+ " analyzer finished, lasted " + counter
							+ " mins");

					final Map<OmegaSegment, Double[][]> segmentD = analyzer
							.getGammaDFromLogResults();
					final Map<OmegaSegment, Double[]> segmentSMSS = analyzer
							.getSmssFromLogResults();

					for (final OmegaTrajectory traj : tracks) {
						final List<OmegaSegment> trajSegments = segments
								.get(traj);
						for (final OmegaSegment segment : trajSegments) {
							final Double omegaD = segmentD.get(segment)[2][3];
							final Double omegaSMSS = segmentSMSS.get(segment)[0];
							outputSMSS.add(omegaSMSS);
							outputD.add(omegaD);
						}
					}

					dDMap.put(D, outputD);
					dSMSSMap.put(D, outputSMSS);
					smssDMap.put(SMSS, dDMap);
					smssSMSSMap.put(SMSS, dSMSSMap);
					lDMap.put(L, smssDMap);
					lSMSSMap.put(L, smssSMSSMap);
					this.dOutput.put(snr, lDMap);
					this.smssOutput.put(snr, lSMSSMap);
				}
			}
		} catch (final IllegalArgumentException | IOException ex) {
			// TODO should I do something here?
		}
	}

	private void writeValues() {
		int snrCounter = 0, lCounter = 0;
		String snrString = "", lString = "";
		for (final Double snr : this.smssOutput.keySet()) {
			snrCounter++;
			if (snrCounter < 10) {
				snrString = "0" + String.valueOf(snrCounter);
			} else {
				snrString = String.valueOf(snrCounter);
			}
			final Map<Integer, Map<Double, Map<Double, List<Double>>>> lSMSSMap = this.smssOutput
					.get(snr);
			final Map<Integer, Map<Double, Map<Double, List<Double>>>> lDMap = this.dOutput
					.get(snr);
			for (final Integer l : lSMSSMap.keySet()) {
				lCounter++;
				if (lCounter < 10) {
					lString = "0" + String.valueOf(lCounter);
				} else {
					lString = String.valueOf(lCounter);
				}
				int rowCounter = 0;
				final Map<Double, Map<Double, List<Double>>> smssSMSSMap = lSMSSMap
						.get(l);
				final Map<Double, Map<Double, List<Double>>> smssDMap = lDMap
						.get(l);
				for (final Double smss : smssSMSSMap.keySet()) {
					final Map<Double, List<Double>> dSMSSMap = smssSMSSMap
							.get(smss);
					final Map<Double, List<Double>> dDMap = smssDMap.get(smss);
					for (final Double d : dSMSSMap.keySet()) {
						rowCounter++;
						System.out.println("Computing SNR " + snr + " L " + l
								+ " SMSS " + smss + " D " + d);
						final List<Double> smssValues = dSMSSMap.get(d);
						final List<Double> dValues = dDMap.get(d);
						final String smssFileName = this.omegaSMSSDir
								+ "\\SMSS_values_SNR_" + snrString + "_L_"
								+ lString + ".csv";
						final String dFileName = this.omegaDDir
								+ "\\D_values_SNR_" + snrString + "_L_"
								+ lString + ".csv";
						final StringBuffer row = new StringBuffer();
						row.append(String.valueOf(rowCounter));
						row.append(" ");
						row.append(String.valueOf(snr));
						row.append(" ");
						row.append(String.valueOf(l));
						row.append(" ");
						row.append(String.valueOf(smss));
						row.append(" ");
						row.append(String.valueOf(d));
						row.append(";");
						row.append("***");
						row.append(";");
						final StringBuffer smssRow = new StringBuffer(
								row.toString());
						final StringBuffer dRow = new StringBuffer(
								row.toString());

						for (final Double val : smssValues) {
							smssRow.append(String.valueOf(val));
							smssRow.append(";");
						}
						smssRow.append("\n");
						for (final Double val : dValues) {
							dRow.append(String.valueOf(val));
							dRow.append(";");
						}
						dRow.append("\n");
						try {
							final File smssFile = new File(smssFileName);
							FileWriter fw = new FileWriter(smssFile, true);
							BufferedWriter bw = new BufferedWriter(fw);
							bw.write(smssRow.toString());
							bw.close();
							fw.close();

							final File dFile = new File(dFileName);
							fw = new FileWriter(dFile, true);
							bw = new BufferedWriter(fw);
							bw.write(dRow.toString());
							bw.close();
							fw.close();
						} catch (final IOException e) {
							// TODO should I do something here?
						}
					}
				}
			}
		}
	}

	@Override
	public void updateMessageStatus(final OmegaMessageEvent evt) {
		System.out.println(evt.getMessage());
	}
}
