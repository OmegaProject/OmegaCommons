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
package edu.umassmed.omega.commons.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.RootPaneContainer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import edu.umassmed.omega.commons.OmegaLogFileManager;
import edu.umassmed.omega.commons.constants.OmegaConstants;
import edu.umassmed.omega.commons.constants.OmegaConstantsAlgorithmParameters;
import edu.umassmed.omega.commons.constants.OmegaGUIConstants;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAlgorithmInformation;
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
import edu.umassmed.omega.commons.data.coreElements.OmegaPlane;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.utilities.OmegaAnalysisRunContainerUtilities;
import edu.umassmed.omega.commons.utilities.OmegaMathsUtilities;
import edu.umassmed.omega.commons.utilities.OmegaStringUtilities;

public class GenericAnalysisInformationPanel extends GenericScrollPane {

	private static final long serialVersionUID = -8599077833612345455L;

	private JTextPane info_txt;

	private final SimpleAttributeSet normal, bold;

	private JButton algoDetails_btt;

	private final GenericAlgorithmDetailsDialog algoInfoDialog;
	private OmegaAlgorithmInformation algoInfo;

	public GenericAnalysisInformationPanel(final RootPaneContainer parent) {
		super(parent);

		this.normal = new SimpleAttributeSet();
		this.bold = new SimpleAttributeSet();
		StyleConstants.setBold(this.bold, true);

		// this.setBorder(new TitledBorder("Information"));

		this.algoInfoDialog = new GenericAlgorithmDetailsDialog(parent);

		this.createAndAddWidgets();

		this.addListeners();
	}

	private void createAndAddWidgets() {
		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		this.info_txt = new JTextPane();
		this.info_txt.setEditable(false);
		this.info_txt.setEditorKit(new GenericWrapEditorKit());
		this.info_txt.setBackground(this.getBackground());
		try {
			this.appendString(OmegaGUIConstants.SIDEPANEL_NO_DETAILS, this.bold);
		} catch (final BadLocationException ex) {
			OmegaLogFileManager.handleCoreException(ex, true);
		}
		mainPanel.add(this.info_txt, BorderLayout.CENTER);

		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		this.algoDetails_btt = new JButton(
				OmegaGUIConstants.ALGORITHM_INFORMATION);
		this.algoDetails_btt.setPreferredSize(OmegaConstants.BUTTON_SIZE_LARGE);
		this.algoDetails_btt.setSize(OmegaConstants.BUTTON_SIZE_LARGE);
		this.algoDetails_btt.setEnabled(false);
		buttonPanel.add(this.algoDetails_btt);

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setViewportView(mainPanel);
	}

	private void addListeners() {
		this.algoDetails_btt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				GenericAnalysisInformationPanel.this.handleShowAlgoDetails();
			}
		});
	}

	private void handleShowAlgoDetails() {
		this.algoInfoDialog.setVisible(true);
	}

	public void resizePanel(final int width, final int height) {
		final int lines = OmegaStringUtilities.countLines(this.info_txt,
				this.info_txt.getDocument().getLength());
		int neededHeight = lines * 18;
		final int neededWidth = width - 20;
		final int adjHeight = height - 60;
		// System.out.println(height + " VS " + neededHeight);
		if (adjHeight > neededHeight) {
			neededHeight = adjHeight;
		} else {
			// neededWidth -= 20;
			// neededHeight += 17;
		}

		final Dimension panelDim = new Dimension(width, height);
		this.setPreferredSize(panelDim);
		this.setSize(panelDim);
		final Dimension textDim = new Dimension(neededWidth, neededHeight);
		this.info_txt.setPreferredSize(textDim);
		this.info_txt.setSize(textDim);
	}

	private void appendString(final String s, final AttributeSet style)
			throws BadLocationException {
		final Document doc = this.info_txt.getDocument();
		final int length = doc.getLength();
		doc.insertString(length, s, style);
	}

	private void appendNewline() throws BadLocationException {
		final Document doc = this.info_txt.getDocument();
		final int length = doc.getLength();
		doc.insertString(length, "\n", this.normal);
	}

	private void reset() throws BadLocationException {
		final Document doc = this.info_txt.getDocument();
		final int length = doc.getLength();
		doc.remove(0, length);
	}

	public void updateContent(final OmegaAnalysisRun analysisRun) {
		this.algoInfoDialog.updateAlgorithmInformation(null);
		this.algoDetails_btt.setEnabled(false);
		// updateDialog(null)
		try {
			this.reset();
			if (analysisRun != null) {
				this.algoDetails_btt.setEnabled(true);
				this.algoInfoDialog.updateAlgorithmInformation(analysisRun
						.getAlgorithmSpec().getAlgorithmInfo());
				// updateDialog(algo)
				this.getGenericAnalysisInformation(analysisRun);
				this.appendNewline();
				this.getSpecificElementInformation(analysisRun);
			} else {
				this.appendString(OmegaGUIConstants.SIDEPANEL_NO_DETAILS,
						this.bold);
			}
		} catch (final BadLocationException ex) {
			OmegaLogFileManager.handleCoreException(ex, true);
		}

		// this.resizePanel(this.getWidth(), this.getHeight());
		this.info_txt.revalidate();
		this.info_txt.repaint();
	}

	private void getGenericAnalysisInformation(
			final OmegaAnalysisRun analysisRun) throws BadLocationException {
		final SimpleDateFormat format = new SimpleDateFormat(
				OmegaConstants.OMEGA_DATE_FORMAT);
		final long id = analysisRun.getElementID();
		final String clazz = analysisRun.getDynamicDisplayName();
		this.appendString(clazz, this.bold);
		this.appendString(OmegaGUIConstants.SIDEPANEL_INFO_ID, this.bold);
		if (id == -1) {
			this.appendString(OmegaGUIConstants.NOT_ASSIGNED, this.normal);
		} else {
			this.appendString(String.valueOf(id), this.normal);
		}
		this.appendNewline();
		this.appendString(OmegaGUIConstants.SIDEPANEL_INFO_OWNER, this.bold);
		final String name = analysisRun.getExperimenter().getFirstName() + " "
				+ analysisRun.getExperimenter().getLastName();
		this.appendString(name, this.normal);
		this.appendNewline();
		// this.appendString(OmegaGUIConstants.SIDEPANEL_INFO_NAME, this.bold);
		// this.appendString(OmegaGUIConstants.SIDEPANEL_INFO_NOT_NAMED,
		// this.normal);
		// this.appendNewline();
		this.appendString("Algorithm: ", this.bold);
		final OmegaRunDefinition algoSpec = analysisRun.getAlgorithmSpec();
		final String algoName = algoSpec.getAlgorithmInfo().getName();
		this.appendString(algoName, this.normal);
		this.appendNewline();
		if (algoSpec.getParameters().size() > 0) {
			this.appendString("Parameters: ", this.bold);
			for (final OmegaParameter param : algoSpec.getParameters()) {
				this.appendNewline();
				this.appendString("-", this.normal);
				String paramName = param.getName();
				if (paramName
						.equals(OmegaConstantsAlgorithmParameters.PARAM_ZSECTION)
						|| paramName
								.equals(OmegaConstantsAlgorithmParameters.PARAM_CHANNEL)) {
					paramName = "Analyzed " + paramName;
				}
				this.appendString(paramName, this.normal);
				this.appendString(": ", this.normal);
				this.appendString(param.getStringValue(), this.normal);
			}
			this.appendNewline();
		}
		this.appendString(OmegaGUIConstants.INFO_EXECUTED, this.bold);
		final String acquiredDate = format.format(analysisRun.getTimeStamps());
		this.appendString(acquiredDate.replace("_", " "), this.normal);
		this.appendNewline();
		this.appendString(OmegaGUIConstants.SIDEPANEL_INFO_NUM_ANALYSIS,
				this.bold);
		this.appendString(String.valueOf(OmegaAnalysisRunContainerUtilities
				.getAnalysisCount(analysisRun)), this.normal);
	}

	private void getSpecificElementInformation(
			final OmegaAnalysisRun analysisRun) throws BadLocationException {
		if (analysisRun instanceof OmegaSNRRun) {
			this.appendAdditionaSNRInformation((OmegaSNRRun) analysisRun);
		} else if (analysisRun instanceof OmegaTrackingMeasuresDiffusivityRun) {
			this.appendAdditionalTMDInformation((OmegaTrackingMeasuresDiffusivityRun) analysisRun);
		} else if (analysisRun instanceof OmegaTrackingMeasuresIntensityRun) {
			this.appendAdditionalTMIInformation((OmegaTrackingMeasuresIntensityRun) analysisRun);
		} else if (analysisRun instanceof OmegaTrackingMeasuresMobilityRun) {
			this.appendAdditionalTMMInformation((OmegaTrackingMeasuresMobilityRun) analysisRun);
		} else if (analysisRun instanceof OmegaTrackingMeasuresVelocityRun) {
			this.appendAdditionalTMVInformation((OmegaTrackingMeasuresVelocityRun) analysisRun);
		} else if (analysisRun instanceof OmegaTrajectoriesSegmentationRun) {
			this.appendAdditionalTSInformation((OmegaTrajectoriesSegmentationRun) analysisRun);
		} else if (analysisRun instanceof OmegaTrajectoriesRelinkingRun) {
			this.appendAdditionalTEInformation((OmegaTrajectoriesRelinkingRun) analysisRun);
		} else if (analysisRun instanceof OmegaParticleLinkingRun) {
			this.appendAdditionalPLInformation((OmegaParticleLinkingRun) analysisRun);
		} else if (analysisRun instanceof OmegaParticleDetectionRun) {
			this.appendAdditionalPDInformation((OmegaParticleDetectionRun) analysisRun);
		}
	}

	private void appendAdditionaSNRInformation(final OmegaSNRRun analysisRun)
			throws BadLocationException {
		this.appendString("Image mean SNR: ", this.bold);
		final Double snr = analysisRun.getResultingAvgSNR();
		this.appendString(String.valueOf(snr), this.normal);
	}

	private void appendAdditionalTMVInformation(
			final OmegaTrackingMeasuresVelocityRun analysisRun) {

	}

	private void appendAdditionalTMMInformation(
			final OmegaTrackingMeasuresMobilityRun analysisRun) {

	}

	private void appendAdditionalTMIInformation(
			final OmegaTrackingMeasuresIntensityRun analysisRun) {

	}

	private void appendAdditionalTMDInformation(
			final OmegaTrackingMeasuresDiffusivityRun analysisRun) {

	}

	private void appendAdditionalTSInformation(
			final OmegaTrajectoriesSegmentationRun analysisRun)
			throws BadLocationException {
		final Map<OmegaTrajectory, List<OmegaSegment>> segments = analysisRun
				.getResultingSegments();
		this.appendString("Total number of trajectories: ", this.bold);
		final int tracksC = segments.keySet().size();
		final String tracks = String.valueOf(tracksC);
		this.appendString(tracks, this.normal);
		this.appendNewline();
		this.appendString("Total number of segments: ", this.bold);
		final Double segmentsN[] = new Double[tracksC];
		int i = 0;
		int segmentsT = 0;
		for (final OmegaTrajectory track : segments.keySet()) {
			final int segmN = segments.get(track).size();
			segmentsN[i] = (double) segmN;
			segmentsT += segmN;
			i++;
		}
		this.appendString(String.valueOf(segmentsT), this.normal);
		this.appendNewline();
		this.appendString("Average number of segments per trajectory: ",
				this.bold);
		final Double segmMean = OmegaMathsUtilities.mean(segmentsN);
		this.appendString(String.valueOf(segmMean), this.normal);

	}

	private void appendAdditionalTEInformation(
			final OmegaTrajectoriesRelinkingRun analysisRun)
			throws BadLocationException {
		this.appendAdditionalPLInformation(analysisRun);
	}

	private void appendAdditionalPLInformation(
			final OmegaParticleLinkingRun analysisRun)
			throws BadLocationException {
		final List<OmegaTrajectory> tracks = analysisRun
				.getResultingTrajectories();
		this.appendString("Total number of trajectories: ", this.bold);
		final String tracksV = String.valueOf(tracks.size());
		this.appendString(tracksV, this.normal);
		this.appendNewline();

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

		this.appendString("Average trajectory length: ", this.bold);
		final String averagetl = String.valueOf(meanTracksLength);
		this.appendString(averagetl, this.normal);
		this.appendNewline();
		this.appendString("Max trajectory length: ", this.bold);
		final String maxtl = String.valueOf(maxTracksLength);
		this.appendString(maxtl, this.normal);
		this.appendNewline();
		this.appendString("Min trajectory length: ", this.bold);
		final String mintl = String.valueOf(minTracksLength);
		this.appendString(mintl, this.normal);
	}

	private void appendAdditionalPDInformation(
			final OmegaParticleDetectionRun analysisRun)
			throws BadLocationException {
		double numP = 0;
		int maxNumP = 0;
		int minNumP = 0;
		int f = 0;
		for (final OmegaPlane frame : analysisRun.getResultingParticles()
				.keySet()) {
			final int localNumP = analysisRun.getResultingParticles()
					.get(frame).size();
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
		this.appendString("Average number of spots found per time point: ",
				this.bold);
		final String mean = String.valueOf(numP);
		this.appendString(mean, this.normal);
		this.appendNewline();
		this.appendString("Max  number of spots found per time point: ",
				this.bold);
		final String max = String.valueOf(maxNumP);
		this.appendString(max, this.normal);
		this.appendNewline();
		this.appendString("Min  number of spots found per time point: ",
				this.bold);
		final String min = String.valueOf(minNumP);
		this.appendString(min, this.normal);
	}
	
	@Override
	public void updateParentContainer(final RootPaneContainer parent) {
		super.updateParentContainer(parent);
		this.algoInfoDialog.updateParentContainer(parent);
	}
}
