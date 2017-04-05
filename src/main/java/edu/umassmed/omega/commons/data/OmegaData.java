/*******************************************************************************
 * Copyright (C) 2014 University of Massachusetts Medical School Alessandro
 * Rigano (Program in Molecular Medicine) Caterina Strambio De Castillia
 * (Program in Molecular Medicine)
 *
 * Created by the Open Microscopy Environment inteGrated Analysis (OMEGA) team:
 * Alex Rigano, Caterina Strambio De Castillia, Jasmine Clark, Vanni Galli,
 * Raffaello Giulietti, Loris Grossi, Eric Hunter, Tiziano Leidi, Jeremy Luban,
 * Ivo Sbalzarini and Mario Valle.
 *
 * Key contacts: Caterina Strambio De Castillia: caterina.strambio@umassmed.edu
 * Alex Rigano: alex.rigano@umassmed.edu
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package edu.umassmed.omega.commons.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.OmegaLogFileManager;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAlgorithmInformation;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainer;
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
import edu.umassmed.omega.commons.data.analysisRunElements.OrphanedAnalysisContainer;
import edu.umassmed.omega.commons.data.coreElements.OmegaDataset;
import edu.umassmed.omega.commons.data.coreElements.OmegaExperimenter;
import edu.umassmed.omega.commons.data.coreElements.OmegaExperimenterGroup;
import edu.umassmed.omega.commons.data.coreElements.OmegaImage;
import edu.umassmed.omega.commons.data.coreElements.OmegaImagePixels;
import edu.umassmed.omega.commons.data.coreElements.OmegaPerson;
import edu.umassmed.omega.commons.data.coreElements.OmegaPlane;
import edu.umassmed.omega.commons.data.coreElements.OmegaProject;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegmentationTypes;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class OmegaData {

	private final List<OmegaProject> projects;
	private final List<OmegaExperimenter> experimenters;
	private final List<OmegaExperimenterGroup> groups;
	private final List<OmegaSegmentationTypes> segmTypesList;
	private final OrphanedAnalysisContainer orphanedAnalysis;

	private final List<OmegaPerson> persons;

	public OmegaData() {
		this.projects = new ArrayList<OmegaProject>();
		this.experimenters = new ArrayList<OmegaExperimenter>();
		this.groups = new ArrayList<OmegaExperimenterGroup>();
		this.segmTypesList = new ArrayList<OmegaSegmentationTypes>();
		this.segmTypesList.add(OmegaSegmentationTypes
				.getDefaultSegmentationTypes());
		this.orphanedAnalysis = new OrphanedAnalysisContainer();

		this.persons = new ArrayList<OmegaPerson>();
	}

	public StringBuffer printOmegaData() {
		StringBuffer buf = new StringBuffer();

		for (final OmegaProject project : this.projects) {
			buf.append("Project " + project.getName() + "\n");
			buf.append("\tId: " + project.getElementID() + "\n");
			buf.append("\tOId: " + project.getOmeroId() + "\n");
			buf.append("\tNum Dataset: " + project.getDatasets().size() + "\n");
			buf.append("\tNum Analysis: " + project.getAnalysisRuns().size()
					+ "\n");
			buf.append("\tContains:" + "\n");
			for (final OmegaDataset dataset : project.getDatasets()) {
				buf.append("-----------------------------------------------------\n");
				buf.append("Dataset " + dataset.getName() + "\n");
				buf.append("\tId: " + dataset.getElementID() + "\n");
				buf.append("\tOId: " + dataset.getOmeroId() + "\n");
				buf.append("\tNum Images: " + dataset.getImages().size() + "\n");
				buf.append("\tNum Analysis: "
						+ dataset.getAnalysisRuns().size() + "\n");
				buf.append("\tContains:" + "\n");
				for (final OmegaImage image : dataset.getImages()) {
					final OmegaImagePixels defaultPixels = image
							.getDefaultPixels();
					final OmegaExperimenter exp = image.getExperimenter();
					buf.append("*************************************************\n");
					buf.append("Image " + image.getName() + "\n");
					buf.append("\tId: " + image.getElementID() + "\n");
					buf.append("\tOId: " + image.getOmeroId() + "\n");
					buf.append("\tNum Analysis: "
							+ image.getAnalysisRuns().size() + "\n");
					buf.append("\tOwner:" + exp.getFirstName() + " "
							+ exp.getLastName() + "\n");
					buf.append("\t\tOwner Id:" + exp.getElementID() + "\n");
					buf.append("\t\tOwner OId:" + exp.getOmeroId() + "\n");
					buf.append("\tPixel: " + "\n");
					buf.append("\t\tPixel Id: " + defaultPixels.getElementID()
							+ "\n");
					buf.append("\t\tPixel OId: " + defaultPixels.getOmeroId()
							+ "\n");
					buf.append("\t\tPixel values X: "
							+ defaultPixels.getPhysicalSizeX() + " Y: "
							+ defaultPixels.getPhysicalSizeY() + " Z: "
							+ defaultPixels.getPhysicalSizeZ() + " T: "
							+ defaultPixels.getPhysicalSizeT() + "\n");
					buf.append("\t\tNum Frames: " + "\n");
					for (final int c : defaultPixels.getAllFrames().keySet()) {
						final Map<Integer, List<OmegaPlane>> zMap = defaultPixels
								.getAllFrames().get(c);
						for (final int z : zMap.keySet()) {
							final List<OmegaPlane> planes = zMap.get(z);
							buf.append("\t\t\t C: " + c + " Z: " + z
									+ " Planes: " + planes.size() + "\n");
						}
					}
					buf = this.printAnalysis(image, 0, buf);
				}
			}
			buf.append("#####################################################\n");
		}

		buf.append("Orphaned analysis" + "\n");
		buf.append("\tNum Analysis: "
				+ this.orphanedAnalysis.getAnalysisRuns().size() + "\n");
		buf = this.printAnalysis(this.orphanedAnalysis, 0, buf);
		return buf;
	}

	public StringBuffer printAnalysis(
			final OmegaAnalysisRunContainer container, final int lvl,
			final StringBuffer buf) {
		String space = "";
		for (int i = 0; i <= lvl; i++) {
			space += ("\t");
		}
		final String space1 = space + "\t";
		final String space2 = space1 + "\t";
		final String space3 = space2 + "\t";
		buf.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
		for (final OmegaAnalysisRun analysisRun : container.getAnalysisRuns()) {
			final OmegaExperimenter exp = analysisRun.getExperimenter();
			final OmegaRunDefinition algoSpec = analysisRun.getAlgorithmSpec();
			final OmegaAlgorithmInformation algoInfo = algoSpec
					.getAlgorithmInfo();
			buf.append(space + "Analysis: " + analysisRun.getName() + "\n");
			buf.append(space + "Type: " + analysisRun.getType().name() + "\n");
			buf.append(space + "Owner:" + exp.getFirstName() + " "
					+ exp.getLastName() + "\n");
			buf.append(space1 + "Owner Id:" + exp.getElementID() + "\n");
			buf.append(space1 + "Owner OId:" + exp.getOmeroId() + "\n");
			buf.append(space + "Algo: " + algoInfo.getName());
			buf.append(space1 + "Id: " + algoInfo.getElementID());
			buf.append(space1 + "Version: " + algoInfo.getVersion());
			buf.append(space1 + "Author: ["
					+ algoInfo.getAuthor().getElementID() + "] "
					+ algoInfo.getAuthor().getFirstName() + " "
					+ algoInfo.getAuthor().getLastName());
			buf.append(space1 + "RdId: " + algoSpec.getElementID());
			buf.append(space1 + "Num Params: "
					+ algoSpec.getParameters().size() + "\n");
			for (final OmegaParameter param : algoSpec.getParameters()) {
				buf.append(space2 + "Param: [" + param.getElementID() + "] "
						+ param.getName() + " - " + param.getValue());
				if (param.getUnit() != null) {
					buf.append(" " + param.getUnit());
				}
				buf.append("\n");
			}
			if (analysisRun instanceof OmegaSNRRun) {
				final OmegaSNRRun snrRun = (OmegaSNRRun) analysisRun;
				Long minID = Long.MAX_VALUE;
				Long maxID = Long.MIN_VALUE;
				for (final OmegaPlane p : snrRun.getResultingImageBGR()
						.keySet()) {
					if (p.getElementID() < minID) {
						minID = p.getElementID();
					}
					if (p.getElementID() > maxID) {
						maxID = p.getElementID();
					}
				}
				for (long i = minID; i <= maxID; i++) {
					for (final OmegaPlane p : snrRun.getResultingImageBGR()
							.keySet()) {
						// System.out.println(p.getElementID() + " VS " + i);
						if (p.getElementID() != i) {
							continue;
						}
						buf.append(space
								+ "Plane ID: "
								+ p.getElementID()
								+ "\tBGR: "
								+ snrRun.getResultingImageBGR().get(p)
								+ "\tNOI: "
								+ snrRun.getResultingImageNoise().get(p)
								+ "\tminSNR: "
								+ snrRun.getResultingImageMinimumSNR().get(p)
								+ "\tavgSNR: "
								+ snrRun.getResultingImageAverageSNR().get(p)
								+ "\tmaxSNR: "
								+ snrRun.getResultingImageMaximumSNR().get(p)
								+ "\tminISNR: "
								+ snrRun.getResultingImageMinimumErrorIndexSNR()
										.get(p)
								+ "\tavgISNR: "
								+ snrRun.getResultingImageAverageErrorIndexSNR()
										.get(p)
								+ "\tmaxISNR: "
								+ snrRun.getResultingImageMaximumErrorIndexSNR()
										.get(p) + "\n");
					}
				}
				minID = Long.MAX_VALUE;
				maxID = Long.MIN_VALUE;
				for (final OmegaROI p : snrRun.getResultingLocalCenterSignals()
						.keySet()) {
					if (p.getElementID() < minID) {
						minID = p.getElementID();
					}
					if (p.getElementID() > maxID) {
						maxID = p.getElementID();
					}
				}
				for (long i = minID; i <= maxID; i++) {
					for (final OmegaROI p : snrRun
							.getResultingLocalCenterSignals().keySet()) {
						if (p.getElementID() != i) {
							continue;
						}
						buf.append(space
								+ "ROI ID: "
								+ p.getElementID()
								+ "\tCS: "
								+ snrRun.getResultingLocalCenterSignals()
										.get(p)
								+ "\tPS: "
								+ snrRun.getResultingLocalPeakSignals().get(p)
								+ "\tMS: "
								+ snrRun.getResultingLocalMeanSignals().get(p)
								+ "\tPA: "
								+ snrRun.getResultingLocalParticleArea().get(p)
								+ "\tNOI: "
								+ snrRun.getResultingLocalNoises().get(p)
								+ "\tavgISNR: "
								+ snrRun.getResultingLocalSNRs().get(p)
								+ "\tISNR: "
								+ snrRun.getResultingLocalErrorIndexSNRs().get(
										p) + "\n");
					}
				}
			} else if (analysisRun instanceof OmegaTrackingMeasuresDiffusivityRun) {
				final OmegaTrackingMeasuresDiffusivityRun diffRun = (OmegaTrackingMeasuresDiffusivityRun) analysisRun;
				for (final OmegaTrajectory t : diffRun.getSegments().keySet()) {
					for (final OmegaSegment s : diffRun.getSegments().get(t)) {
						buf.append(space + "Segment: " + s.getElementID()
								+ "\n");
						buf.append(space1 + "Ny:");
						for (int i = 0; i < diffRun.getNyResults().get(s).length; i++) {
							buf.append("\t");
							buf.append(diffRun.getNyResults().get(s)[i]);
						}
						buf.append("\n");
						buf.append(space1 + "Mu:");
						for (int i = 0; i < diffRun.getMuResults().get(s).length; i++) {
							buf.append("\n");
							buf.append(space2 + "I: " + i);
							for (int k = 0; k < diffRun.getMuResults().get(s)[i].length; k++) {
								buf.append("\t");
								buf.append(diffRun.getMuResults().get(s)[i][k]);
							}
						}
						buf.append("\n");
						buf.append(space1 + "LogMu:");
						for (int i = 0; i < diffRun.getLogMuResults().get(s).length; i++) {
							buf.append("\n");
							buf.append(space2 + "I: " + i);
							for (int k = 0; k < diffRun.getLogMuResults()
									.get(s)[i].length; k++) {
								buf.append("\t");
								buf.append(diffRun.getLogMuResults().get(s)[i][k]);
							}
						}
						buf.append("\n");
						buf.append(space1 + "DeltaT:");
						for (int i = 0; i < diffRun.getDeltaTResults().get(s).length; i++) {
							buf.append("\n");
							buf.append(space2 + "I: " + i);
							for (int k = 0; k < diffRun.getDeltaTResults().get(
									s)[i].length; k++) {
								buf.append("\t");
								buf.append(diffRun.getDeltaTResults().get(s)[i][k]);
							}
						}
						buf.append("\n");
						buf.append(space1 + "LogDeltaT:");
						for (int i = 0; i < diffRun.getLogDeltaTResults()
								.get(s).length; i++) {
							buf.append("\n");
							buf.append(space2 + "I: " + i);
							for (int k = 0; k < diffRun.getLogDeltaTResults()
									.get(s)[i].length; k++) {
								buf.append("\t");
								buf.append(diffRun.getLogDeltaTResults().get(s)[i][k]);
							}
						}
						buf.append("\n");
						buf.append(space1 + "GammaD:");
						for (int i = 0; i < diffRun.getGammaDResults().get(s).length; i++) {
							buf.append("\n");
							buf.append(space2 + "I: " + i);
							for (int k = 0; k < diffRun.getGammaDResults().get(
									s)[i].length; k++) {
								buf.append("\t");
								buf.append(diffRun.getGammaDResults().get(s)[i][k]);
							}
						}
						buf.append("\n");
						buf.append(space1 + "GammaDLog:");
						for (int i = 0; i < diffRun.getGammaDFromLogResults()
								.get(s).length; i++) {
							buf.append("\n");
							buf.append(space2 + "I: " + i + "\t");
							for (int k = 0; k < diffRun
									.getGammaDFromLogResults().get(s)[i].length; k++) {
								buf.append("\t");
								buf.append(diffRun.getGammaDFromLogResults()
										.get(s)[i][k]);
							}
						}
						buf.append(space1 + "GammaLog:");
						for (int i = 0; i < diffRun.getGammaFromLogResults()
								.get(s).length; i++) {
							buf.append("\t");
							buf.append(diffRun.getGammaFromLogResults().get(s)[i]);
						}
						buf.append("\n");
						buf.append(space1 + "SMSS:");
						for (int i = 0; i < diffRun.getSmssFromLogResults()
								.get(s).length; i++) {
							buf.append("\t");
							buf.append(diffRun.getSmssFromLogResults().get(s)[i]);
						}
						buf.append("\n");
						if ((diffRun.getErrosFromLogResults() != null)
								&& !diffRun.getErrosFromLogResults().isEmpty()) {
							buf.append(space1 + "Errors:");
							for (int i = 0; i < diffRun
									.getErrosFromLogResults().get(s).length; i++) {
								buf.append("\t");
								buf.append(diffRun.getErrosFromLogResults()
										.get(s)[i]);
							}
							buf.append("\n");
						}
					}
				}
			} else if (analysisRun instanceof OmegaTrackingMeasuresMobilityRun) {
				final OmegaTrackingMeasuresMobilityRun mobiRun = (OmegaTrackingMeasuresMobilityRun) analysisRun;
				for (final OmegaTrajectory t : mobiRun.getSegments().keySet()) {
					for (final OmegaSegment s : mobiRun.getSegments().get(t)) {
						buf.append(space + "Segment: " + s.getElementID()
								+ "\n");
						buf.append(space1 + "Distances:");
						for (int i = 0; i < mobiRun
								.getDistancesFromOriginResults().get(s).size(); i++) {
							buf.append("\t");
							buf.append(mobiRun.getDistancesFromOriginResults()
									.get(s).get(i));
						}
						buf.append("\n");
						buf.append(space1 + "Displacements:");
						for (int i = 0; i < mobiRun
								.getDisplacementsFromOriginResults().get(s)
								.size(); i++) {
							buf.append("\t");
							buf.append(mobiRun
									.getDisplacementsFromOriginResults().get(s)
									.get(i));
						}
						buf.append("\n");
						buf.append(space1 + "TotalTime:\t"
								+ mobiRun.getTimeTraveledResults().get(s)
								+ "\n");
						buf.append(space1
								+ "MaxDisplacements:\t"
								+ mobiRun
										.getMaxDisplacementsFromOriginResults()
										.get(s) + "\n");
						buf.append(space1 + "ConfinementRatio:");
						for (int i = 0; i < mobiRun
								.getConfinementRatioResults().get(s).size(); i++) {
							buf.append("\t");
							buf.append(mobiRun.getConfinementRatioResults()
									.get(s).get(i));
						}
						buf.append("\n");
						buf.append(space1 + "Angles:");
						for (int i = 0; i < mobiRun
								.getAnglesAndDirectionalChangesResults().get(s)
								.size(); i++) {
							buf.append("\t");
							buf.append(mobiRun
									.getAnglesAndDirectionalChangesResults()
									.get(s).get(i)[0]
									+ " - "
									+ mobiRun
											.getAnglesAndDirectionalChangesResults()
											.get(s).get(i)[1]);
						}
					}
				}
			} else if (analysisRun instanceof OmegaTrackingMeasuresVelocityRun) {
				final OmegaTrackingMeasuresVelocityRun velRun = (OmegaTrackingMeasuresVelocityRun) analysisRun;
				for (final OmegaTrajectory t : velRun.getSegments().keySet()) {
					for (final OmegaSegment s : velRun.getSegments().get(t)) {
						buf.append(space + "Segment: " + s.getElementID()
								+ "\n");
						buf.append(space1 + "Speed:");
						for (int i = 0; i < velRun
								.getLocalSpeedFromOriginResults().get(s).size(); i++) {
							buf.append("\t");
							buf.append(velRun.getLocalSpeedFromOriginResults()
									.get(s).get(i));
						}
						buf.append("\n");
						buf.append(space1 + "Velocity:");
						for (int i = 0; i < velRun
								.getLocalVelocityFromOriginResults().get(s)
								.size(); i++) {
							buf.append("\t");
							buf.append(velRun
									.getLocalVelocityFromOriginResults().get(s)
									.get(i));
						}
						buf.append("\n");
						buf.append(space1
								+ "Average Speed:\t"
								+ velRun.getAverageCurvilinearSpeedMapResults()
										.get(s) + "\n");
						buf.append(space1
								+ "Average Velocity:\t"
								+ velRun.getAverageStraightLineVelocityMapResults()
										.get(s) + "\n");
						buf.append(space1
								+ "Forward Progression:\t"
								+ velRun.getForwardProgressionLinearityMapResults()
										.get(s) + "\n");
					}
				}
			} else if (analysisRun instanceof OmegaTrackingMeasuresIntensityRun) {
				final OmegaTrackingMeasuresIntensityRun intRun = (OmegaTrackingMeasuresIntensityRun) analysisRun;
				for (final OmegaTrajectory t : intRun.getSegments().keySet()) {
					for (final OmegaSegment s : intRun.getSegments().get(t)) {
						buf.append(space1 + "PS:");
						for (int i = 0; i < intRun.getPeakSignalsResults().get(
								s).length; i++) {
							buf.append("\t");
							buf.append(intRun.getPeakSignalsResults().get(s)[i]);
						}
						buf.append("\n");
						buf.append(space1 + "CS:");
						for (int i = 0; i < intRun.getCentroidSignalsResults()
								.get(s).length; i++) {
							buf.append("\t");
							buf.append(intRun.getCentroidSignalsResults()
									.get(s)[i]);
						}
						buf.append("\n");
					}
				}
			} else if (analysisRun instanceof OmegaTrajectoriesSegmentationRun) {
				final OmegaTrajectoriesSegmentationRun segmRun = (OmegaTrajectoriesSegmentationRun) analysisRun;
				for (final OmegaTrajectory t : segmRun.getResultingSegments()
						.keySet()) {
					for (final OmegaSegment s : segmRun.getResultingSegments()
							.get(t)) {
						buf.append(space1 + "Segment: " + s.getElementID()
								+ "\tType: " + s.getSegmentationType() + "\n");
						buf.append(space2 + "Start: "
								+ s.getStartingROI().getElementID() + "\tEnd: "
								+ s.getEndingROI().getElementID() + "\n");
					}
				}
			} else if ((analysisRun instanceof OmegaTrajectoriesRelinkingRun)
					|| (analysisRun instanceof OmegaParticleLinkingRun)) {
				final OmegaParticleLinkingRun linRun = (OmegaParticleLinkingRun) analysisRun;
				for (final OmegaTrajectory t : linRun
						.getResultingTrajectories()) {
					buf.append(space1 + "Track: " + t.getName() + "\tId: "
							+ t.getElementID() + "\tL: " + t.getLength() + "\n");
				}
			} else if (analysisRun instanceof OmegaParticleDetectionRun) {
				final OmegaParticleDetectionRun detRun = (OmegaParticleDetectionRun) analysisRun;
				for (final OmegaPlane p : detRun.getResultingParticles()
						.keySet()) {
					for (final OmegaROI roi : detRun.getResultingParticles()
							.get(p)) {
						buf.append(space2 + "P: " + p.getIndex() + "\tId: "
								+ roi.getElementID() + "\tX: " + roi.getX()
								+ "\tY: " + roi.getY() + "\n");
						final Map<String, Object> values = detRun
								.getResultingParticlesValues().get(roi);
						buf.append(space3 + "V: ");
						for (final String s : values.keySet()) {
							buf.append(s + "-" + values.get(s) + "\t");
						}
						buf.append("\n");
					}
				}
			} else {

			}
			this.printAnalysis(analysisRun, lvl + 1, buf);
		}
		return buf;
	}

	public OrphanedAnalysisContainer getOrphanedContainer() {
		return this.orphanedAnalysis;
	}

	public List<OmegaAnalysisRun> getOrphanedAnalyses() {
		return this.orphanedAnalysis.getAnalysisRuns();
	}

	public void addOrphanedAnalysis(final OmegaAnalysisRun analysisRun) {
		this.orphanedAnalysis.addAnalysisRun(analysisRun);
	}

	public void consolidateData() {
		if (OmegaLogFileManager.isDebug()) {
			OmegaLogFileManager.appendToCoreLog("Consolidation started");
		}
		for (final OmegaProject project : this.projects) {
			this.consolidateData(project);
			for (final OmegaDataset dataset : project.getDatasets()) {
				this.consolidateData(dataset);
				for (final OmegaImage image : dataset.getImages()) {
					this.consolidateData(image);
					final OmegaExperimenter exp = image.getExperimenter();
					final OmegaExperimenter sameExperimenter = this
							.findSameExperiementer(exp);
					if (sameExperimenter != null) {
						if (OmegaLogFileManager.isDebug()) {
							OmegaLogFileManager
									.appendToCoreLog("Experimenter: "
											+ exp.printName()
											+ " already found, replacing.");
						}
						image.changeExperimenter(sameExperimenter);
					} else {
						final OmegaPerson samePerson = this.findSamePerson(exp);
						if (samePerson != null) {
							if (OmegaLogFileManager.isDebug()) {
								OmegaLogFileManager
										.appendToCoreLog("Experimenter: "
												+ exp.printName()
												+ " found as person, replacing person with exp.");
							}
							this.changePersonWithExperimenter(samePerson, exp);
						}
						if (OmegaLogFileManager.isDebug()) {
							OmegaLogFileManager
									.appendToCoreLog("Experimenter: "
											+ exp.printName()
											+ " not found, adding.");
						}
						this.addExperimenter(exp);
					}
					for (final OmegaImagePixels pixels : image.getPixels()) {
						this.consolidateData(pixels);
						for (int c = 0; c < pixels.getSizeC(); c++) {
							for (int z = 0; z < pixels.getSizeZ(); z++) {
								for (final OmegaPlane frame : pixels.getFrames(
										c, z)) {
									this.consolidateData(frame);
								}
							}
						}

					}
				}
			}
		}
		if (OmegaLogFileManager.isDebug()) {
			OmegaLogFileManager.appendToCoreLog("Consolidation ended");
		}
	}

	private void consolidateData(final OmegaAnalysisRunContainer container) {
		for (final OmegaAnalysisRun analysisRun : container.getAnalysisRuns()) {
			final OmegaExperimenter exp = analysisRun.getExperimenter();
			final OmegaExperimenter sameExperimenter = this
					.findSameExperiementer(exp);
			if (sameExperimenter != null) {
				if (OmegaLogFileManager.isDebug()) {
					OmegaLogFileManager.appendToCoreLog("Experimenter: "
							+ exp.printName() + " already found, replacing.");
				}
				analysisRun.changeExperimenter(sameExperimenter);
			} else {
				final OmegaPerson samePerson = this.findSamePerson(exp);
				if (samePerson != null) {
					if (OmegaLogFileManager.isDebug()) {
						OmegaLogFileManager
								.appendToCoreLog("Experimenter: "
										+ exp.printName()
										+ " found as person, replacing person with exp.");
					}
					this.changePersonWithExperimenter(samePerson, exp);
				}
				if (OmegaLogFileManager.isDebug()) {
					OmegaLogFileManager.appendToCoreLog("Experimenter: "
							+ exp.printName() + " not found, adding.");
				}
				this.addExperimenter(exp);
			}

			final OmegaPerson author = analysisRun.getAlgorithmSpec()
					.getAlgorithmInfo().getAuthor();
			final OmegaPerson samePerson = this.findSamePerson(author);
			if (samePerson != null) {
				if (OmegaLogFileManager.isDebug()) {
					OmegaLogFileManager
							.appendToCoreLog("Person: " + author.printName()
									+ " already found, replacing.");
					analysisRun.getAlgorithmSpec().getAlgorithmInfo()
							.changeAuthor(samePerson);
				}
			} else {
				if (OmegaLogFileManager.isDebug()) {
					OmegaLogFileManager.appendToCoreLog("Person: "
							+ author.printName() + " not found, adding.");
					this.addPerson(author);
				}
			}
		}
	}

	public void changePersonWithExperimenter(final OmegaPerson person,
			final OmegaExperimenter exp) {
		this.persons.remove(person);
		for (final OmegaProject project : this.projects) {
			this.changePersonWithExperimenter(project, person, exp);
			for (final OmegaDataset dataset : project.getDatasets()) {
				this.changePersonWithExperimenter(dataset, person, exp);
				for (final OmegaImage image : dataset.getImages()) {
					this.changePersonWithExperimenter(image, person, exp);
					for (final OmegaImagePixels pixels : image.getPixels()) {
						this.changePersonWithExperimenter(pixels, person, exp);
						for (int c = 0; c < pixels.getSizeC(); c++) {
							for (int z = 0; z < pixels.getSizeZ(); z++) {
								for (final OmegaPlane frame : pixels.getFrames(
										c, z)) {
									this.changePersonWithExperimenter(frame,
											person, exp);
								}
							}
						}

					}
				}
			}
		}
	}

	private void changePersonWithExperimenter(
			final OmegaAnalysisRunContainer container,
			final OmegaPerson person, final OmegaExperimenter exp) {
		for (final OmegaAnalysisRun analysisRun : container.getAnalysisRuns()) {
			final OmegaPerson author = analysisRun.getAlgorithmSpec()
					.getAlgorithmInfo().getAuthor();
			if (author == person) {
				analysisRun.getAlgorithmSpec().getAlgorithmInfo()
						.changeAuthor(exp);
			}
			this.changePersonWithExperimenter(analysisRun, person, exp);
		}
	}

	// Element id or omero id ?
	private void checkSegmentationTypesListConsistency() {
		final List<OmegaSegmentationTypes> toRemove = new ArrayList<OmegaSegmentationTypes>();
		for (final OmegaSegmentationTypes segmTypes : this.segmTypesList) {
			if (toRemove.contains(segmTypes)) {
				continue;
			}
			for (final OmegaSegmentationTypes segmTypes2 : this.segmTypesList) {
				if (toRemove.contains(segmTypes2)) {
					continue;
				}
				if (segmTypes.getName().equals(segmTypes2.getName()))
					if (segmTypes.equals(segmTypes2)) {
						continue;
					}
				if (segmTypes.getElementID() == -1) {
					toRemove.add(segmTypes);
				} else if (segmTypes2.getElementID() == -1) {
					toRemove.add(segmTypes2);
				} else {
					// TODO create exception here
					OmegaLogFileManager
							.appendToCoreLog("Omega Data segm types error");
				}
			}
		}
		this.segmTypesList.removeAll(toRemove);
		OmegaSegmentationTypes defaultSegmTypes = null;
		for (final OmegaSegmentationTypes segmTypes : this.segmTypesList) {
			if (segmTypes.getName().equals(OmegaSegmentationTypes.DEFAULT_NAME)) {
				defaultSegmTypes = segmTypes;
			}
		}
		this.segmTypesList.remove(defaultSegmTypes);
		this.segmTypesList.add(0, defaultSegmTypes);
	}

	public void updateSegmentationTypes() {
		for (final OmegaProject proj : this.projects) {
			for (final OmegaDataset dataset : proj.getDatasets()) {
				for (final OmegaImage img : dataset.getImages()) {
					for (final OmegaAnalysisRun analysisRun : img
							.getAnalysisRuns()) {
						this.checkAnalysisRunForSegmentationTypes(analysisRun);
					}
				}
			}
		}
		this.checkSegmentationTypesListConsistency();
	}

	private void checkAnalysisRunForSegmentationTypes(
			final OmegaAnalysisRun analysisRun) {
		if (!(analysisRun instanceof OmegaTrajectoriesSegmentationRun))
			return;
		final OmegaTrajectoriesSegmentationRun tmRun = (OmegaTrajectoriesSegmentationRun) analysisRun;
		this.segmTypesList.add(tmRun.getSegmentationTypes());
		for (final OmegaAnalysisRun innerAnalysisRun : analysisRun
				.getAnalysisRuns()) {
			this.checkAnalysisRunForSegmentationTypes(innerAnalysisRun);
		}
	}

	public List<OmegaSegmentationTypes> getSegmentationTypesList() {
		return this.segmTypesList;
	}

	public List<OmegaProject> getProjects() {
		return this.projects;
	}

	public void addProject(final OmegaProject project) {
		if (this.projects.contains(project))
			return;
		this.projects.add(project);
	}

	public void addExperimenter(final OmegaExperimenter experimenter) {
		// TODO Consolidation here
		this.addPerson(experimenter);
		if (this.experimenters.contains(experimenter))
			return;
		this.experimenters.add(experimenter);
	}

	public OmegaExperimenter findSameExperiementer(
			final OmegaExperimenter experimenter) {
		for (final OmegaExperimenter exp : this.experimenters) {
			if (exp.isSamePersonAs(experimenter))
				return exp;
		}
		return null;
	}

	public OmegaPerson findSamePerson(final OmegaPerson person) {
		for (final OmegaPerson p : this.persons) {
			if (p.isSamePersonAs(person))
				return p;
		}
		return null;
	}

	public void addPerson(final OmegaPerson person) {
		// TODO Consolidation here
		if (this.persons.contains(person))
			return;
		this.persons.add(person);
	}

	public void addExperimenterGroup(final OmegaExperimenterGroup group) {
		this.groups.add(group);
	}

	public OmegaExperimenterGroup getExperimenterGroup(final long id,
			final boolean gatewayId) {
		for (final OmegaExperimenterGroup group : this.groups) {
			if (!gatewayId) {
				if (group.getElementID() == id)
					return group;
			} else {
				if (group.getOmeroId() == id)
					return group;
			}
		}
		return null;
	}

	public OmegaExperimenter getExperimenter(final long id,
			final boolean gatewayId) {
		for (final OmegaExperimenter experimenter : this.experimenters) {
			if (!gatewayId) {
				if (experimenter.getElementID() == id)
					return experimenter;
			} else {
				if (experimenter.getOmeroId() == id)
					return experimenter;
			}
		}
		return null;
	}

	public OmegaPerson getPerson(final long id) {
		for (final OmegaPerson person : this.persons) {
			if (person.getElementID() == id)
				return person;
		}
		return null;
	}

	public OmegaProject getProject(final long id, final boolean gatewayId) {
		for (final OmegaProject project : this.projects) {
			if (!gatewayId) {
				if (project.getElementID() == id)
					return project;
			} else {
				if (project.getOmeroId() == id)
					return project;
			}
		}
		return null;
	}

	public OmegaDataset getDataset(final long id, final boolean gatewayId) {
		for (final OmegaProject project : this.projects) {
			for (final OmegaDataset dataset : project.getDatasets())
				if (!gatewayId) {
					if (dataset.getElementID() == id)
						return dataset;
				} else {
					if (dataset.getOmeroId() == id)
						return dataset;
				}
		}
		return null;
	}

	public OmegaImage getImage(final long id, final boolean gatewayId) {
		for (final OmegaProject project : this.projects) {
			for (final OmegaDataset dataset : project.getDatasets()) {
				for (final OmegaImage image : dataset.getImages()) {
					if (!gatewayId) {
						if (image.getElementID() == id)
							return image;
					} else {
						if (image.getOmeroId() == id)
							return image;
					}
				}
			}
		}
		return null;
	}

	// public boolean containsExperimenterGroup(final long id,
	// final boolean gatewayId) {
	// for (final OmegaExperimenterGroup group : this.groups) {
	// if (!gatewayId) {
	// if (group.getElementID() == id)
	// return true;
	// } else {
	// if (group.getOmeroId() == id)
	// return true;
	// }
	// }
	// return false;
	// }

	// public boolean containsExperimenter(final long id, final boolean
	// gatewayId) {
	// for (final OmegaExperimenter experimenter : this.experimenters) {
	// if (!gatewayId) {
	// if (experimenter.getElementID() == id)
	// return true;
	// } else {
	// if (experimenter.getOmeroId() == id)
	// return true;
	// }
	// }
	// return false;
	// }

	// public boolean containsProject(final long id, final boolean gatewayId) {
	// for (final OmegaProject project : this.projects) {
	// if (!gatewayId) {
	// if (project.getElementID() == id)
	// return true;
	// } else {
	// if (project.getOmeroId() == id)
	// return true;
	// }
	// }
	// return false;
	// }

	// public boolean containsDataset(final long id, final boolean gatewayId) {
	// for (final OmegaProject project : this.projects) {
	// for (final OmegaDataset dataset : project.getDatasets())
	// if (!gatewayId) {
	// if (dataset.getElementID() == id)
	// return true;
	// } else {
	// if (dataset.getOmeroId() == id)
	// return true;
	// }
	// }
	// return false;
	// }

	// public boolean containsImage(final long id, final boolean gatewayId) {
	// for (final OmegaProject project : this.projects) {
	// for (final OmegaDataset dataset : project.getDatasets()) {
	// for (final OmegaImage image : dataset.getImages()) {
	// if (!gatewayId) {
	// if (image.getElementID() == id)
	// return true;
	// } else {
	// if (image.getOmeroId() == id)
	// return true;
	// }
	// }
	// }
	// }
	// return false;
	// }
}
