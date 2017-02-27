package edu.umassmed.omega.commons.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.RootPaneContainer;

import edu.umassmed.omega.commons.constants.OmegaConstants;
import edu.umassmed.omega.commons.constants.StatsConstants;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParticleDetectionRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParticleLinkingRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaSNRRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresDiffusivityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresIntensityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresMobilityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresVelocityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrajectoriesRelinkingRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrajectoriesSegmentationRun;
import edu.umassmed.omega.commons.gui.GenericTrackingResultsPanel;

public class GenericResultsDialog extends GenericDialog {

	private static final String TAB_SNR_ROI = "ROI results";
	private static final String TAB_SNR_PLANE = "Plane results";
	private static final String TAB_SNR_IMAGE = "Image results";
	private static final String TAB_SE = "Segmentation results";
	private static final String TAB_RL = "Relinking results";
	private static final String TAB_PL = "Linking results";
	private static final String TAB_PD = "Linking results";
	
	private JTabbedPane pane;
	private GenericTrackingResultsPanel resPanel1, resPanel2, resPanel3;
	private JButton ok_btt;

	public GenericResultsDialog(final RootPaneContainer parentContainer,
	        final String title, final boolean modal) {
		super(parentContainer, title, modal);

		final Dimension dim = new Dimension(600, 600);
		this.setSize(dim);
		this.setPreferredSize(dim);
		this.revalidate();
		this.repaint();
		this.pack();
	}
	
	@Override
	protected void createAndAddWidgets() {
		this.pane = new JTabbedPane();
		this.resPanel1 = new GenericTrackingResultsPanel(
		        this.getParentContainer());
		this.resPanel2 = new GenericTrackingResultsPanel(
				this.getParentContainer());
		this.resPanel3 = new GenericTrackingResultsPanel(
				this.getParentContainer());
		this.add(this.pane, BorderLayout.CENTER);

		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		this.ok_btt = new JButton("Close");
		this.ok_btt.setPreferredSize(OmegaConstants.BUTTON_SIZE);
		this.ok_btt.setSize(OmegaConstants.BUTTON_SIZE);
		buttonPanel.add(this.ok_btt);

		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	@Override
	protected void addListeners() {
		this.ok_btt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				GenericResultsDialog.this.setVisible(false);
			}
		});
	}
	
	public void setAnalysis(final OmegaAnalysisRun analysisRun,
	        final OmegaAnalysisRun parentAnalysisRun) {
		this.resPanel1.setAnalysisRun(null);
		this.resPanel2.setAnalysisRun(null);
		this.resPanel3.setAnalysisRun(null);
		this.pane.remove(this.resPanel1);
		this.pane.remove(this.resPanel2);
		this.pane.remove(this.resPanel3);
		if (analysisRun == null)
			return;
		if (analysisRun instanceof OmegaTrackingMeasuresDiffusivityRun) {
			this.pane.add(StatsConstants.TAB_RESULTS_LOCAL, this.resPanel1);
			this.pane.add(StatsConstants.TAB_RESULTS_GLOBAL + " Gamma",
					this.resPanel2);
			this.pane.add(StatsConstants.TAB_RESULTS_GLOBAL + " D & SMSS",
					this.resPanel3);
			this.resPanel1.setAnalysisRun(analysisRun, parentAnalysisRun, true);
			this.resPanel2.setAnalysisRun(analysisRun, parentAnalysisRun,
			        false, false);
			this.resPanel3.setAnalysisRun(analysisRun, parentAnalysisRun,
			        false, true);
		} else if (analysisRun instanceof OmegaTrackingMeasuresMobilityRun) {
			this.pane.add(StatsConstants.TAB_RESULTS_LOCAL, this.resPanel1);
			this.pane.add(StatsConstants.TAB_RESULTS_GLOBAL, this.resPanel2);
			this.resPanel1.setAnalysisRun(analysisRun, parentAnalysisRun, true);
			this.resPanel2
			.setAnalysisRun(analysisRun, parentAnalysisRun, false);
		} else if (analysisRun instanceof OmegaTrackingMeasuresVelocityRun) {
			this.pane.add(StatsConstants.TAB_RESULTS_LOCAL, this.resPanel1);
			this.pane.add(StatsConstants.TAB_RESULTS_GLOBAL, this.resPanel2);
			this.resPanel1.setAnalysisRun(analysisRun, parentAnalysisRun, true);
			this.resPanel2
			.setAnalysisRun(analysisRun, parentAnalysisRun, false);
		} else if (analysisRun instanceof OmegaTrackingMeasuresIntensityRun) {
			this.pane.add(StatsConstants.TAB_RESULTS_GLOBAL, this.resPanel1);
			this.resPanel1.setAnalysisRun(analysisRun, parentAnalysisRun, true);
		} else if (analysisRun instanceof OmegaSNRRun) {
			this.pane.add(GenericResultsDialog.TAB_SNR_ROI, this.resPanel1);
			this.pane.add(GenericResultsDialog.TAB_SNR_PLANE, this.resPanel2);
			this.pane.add(GenericResultsDialog.TAB_SNR_IMAGE, this.resPanel3);
			this.resPanel1.setAnalysisRun(analysisRun, true);
			this.resPanel2.setAnalysisRun(analysisRun, false, true);
			this.resPanel3.setAnalysisRun(analysisRun, parentAnalysisRun,
			        false, false);
		} else if (analysisRun instanceof OmegaTrajectoriesSegmentationRun) {
			this.pane.add(GenericResultsDialog.TAB_SE, this.resPanel1);
			this.resPanel1.setAnalysisRun(analysisRun);
		} else if (analysisRun instanceof OmegaTrajectoriesRelinkingRun) {
			this.pane.add(GenericResultsDialog.TAB_RL, this.resPanel1);
			this.resPanel1.setAnalysisRun(analysisRun);
		} else if (analysisRun instanceof OmegaParticleLinkingRun) {
			this.pane.add(GenericResultsDialog.TAB_PL, this.resPanel1);
			this.resPanel1.setAnalysisRun(analysisRun);
		} else if (analysisRun instanceof OmegaParticleDetectionRun) {
			this.pane.add(GenericResultsDialog.TAB_PD, this.resPanel1);
			this.resPanel1.setAnalysisRun(analysisRun);
		}
	}
}
