package edu.umassmed.omega.commons.trajectoryTool.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;

import edu.umassmed.omega.commons.constants.OmegaConstants;
import edu.umassmed.omega.commons.constants.OmegaConstantsAlgorithmParameters;
import edu.umassmed.omega.commons.constants.OmegaGUIConstants;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainerInterface;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParticleDetectionRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParticleLinkingRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OrphanedAnalysisContainer;
import edu.umassmed.omega.commons.data.coreElements.OmegaElement;
import edu.umassmed.omega.commons.data.coreElements.OmegaImage;
import edu.umassmed.omega.commons.gui.GenericAnalysisInformationPanel;
import edu.umassmed.omega.commons.gui.GenericElementInformationPanel;
import edu.umassmed.omega.commons.gui.dialogs.GenericDialog;
import edu.umassmed.omega.commons.gui.interfaces.GenericElementInformationContainerInterface;

public class OmegaTracksToolTargetSelectorDialog extends GenericDialog
implements GenericElementInformationContainerInterface {

	private static final long serialVersionUID = 2617772428822487840L;

	private JComboBox<String> images_cmb, particles_cmb, trajectories_cmb;
	private boolean popImages, popParticles, popTrajectories, next;

	private List<OmegaImage> images;
	private OrphanedAnalysisContainer orphanedAnalysis;
	private OmegaAnalysisRunContainerInterface selectedImage;
	private List<OmegaAnalysisRun> loadedAnalysisRuns;

	private final List<OmegaParticleDetectionRun> particleDetectionRuns;
	private OmegaParticleDetectionRun selectedParticleDetectionRun;
	private final List<OmegaParticleLinkingRun> particleLinkingRuns;
	private OmegaParticleLinkingRun selectedParticleLinkingRun;

	private GenericElementInformationPanel geip;
	private GenericAnalysisInformationPanel gaip1, gaip2;

	private boolean isImporter;

	private JButton action_btt, close_btt;

	public OmegaTracksToolTargetSelectorDialog(final RootPaneContainer parent,
			final List<OmegaImage> images,
	        final OrphanedAnalysisContainer orphanedAnalysis,
			final List<OmegaAnalysisRun> analysisRuns) {
		super(parent, "Omega Tracks Target Selector", true);
		this.selectedImage = null;
		this.particleDetectionRuns = new ArrayList<>();
		this.selectedParticleDetectionRun = null;
		this.particleLinkingRuns = new ArrayList<>();
		this.selectedParticleLinkingRun = null;

		this.images = images;
		this.orphanedAnalysis = orphanedAnalysis;
		this.loadedAnalysisRuns = analysisRuns;

		this.isImporter = false;

		this.popImages = false;
		this.popParticles = false;
		this.popTrajectories = false;

		this.next = false;

		this.populateImagesCombo();

		// this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setSize(new Dimension(600, 400));
	}

	@Override
	protected void createAndAddWidgets() {
		final JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(3, 1));

		final JPanel p1 = new JPanel();
		p1.setLayout(new BorderLayout());
		final JLabel lbl1 = new JLabel(OmegaGUIConstants.SELECT_IMAGE);
		lbl1.setPreferredSize(OmegaConstants.TEXT_SIZE);
		p1.add(lbl1, BorderLayout.WEST);
		this.images_cmb = new JComboBox<String>();
		this.images_cmb.setMaximumRowCount(OmegaConstants.COMBOBOX_MAX_OPTIONS);
		this.images_cmb.setEnabled(false);
		p1.add(this.images_cmb, BorderLayout.CENTER);
		topPanel.add(p1);

		final JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		final JLabel lbl2 = new JLabel(OmegaGUIConstants.SELECT_TRACKS_SPOT);
		lbl2.setPreferredSize(OmegaConstants.TEXT_SIZE);
		p2.add(lbl2, BorderLayout.WEST);
		this.particles_cmb = new JComboBox<String>();
		this.particles_cmb
		.setMaximumRowCount(OmegaConstants.COMBOBOX_MAX_OPTIONS);
		this.particles_cmb.setEnabled(false);
		p2.add(this.particles_cmb, BorderLayout.CENTER);
		topPanel.add(p2);

		final JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout());
		final JLabel lbl3 = new JLabel(OmegaGUIConstants.SELECT_TRACKS_LINKING);
		lbl3.setPreferredSize(OmegaConstants.TEXT_SIZE);
		p3.add(lbl3, BorderLayout.WEST);
		this.trajectories_cmb = new JComboBox<String>();
		this.trajectories_cmb
		.setMaximumRowCount(OmegaConstants.COMBOBOX_MAX_OPTIONS);
		this.trajectories_cmb.setEnabled(false);
		p3.add(this.trajectories_cmb, BorderLayout.CENTER);
		topPanel.add(p3);

		this.add(topPanel, BorderLayout.NORTH);

		final JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(1, 3));

		this.geip = new GenericElementInformationPanel(
		        this.getParentContainer(), this);
		centerPanel.add(this.geip);
		this.gaip1 = new GenericAnalysisInformationPanel(
				this.getParentContainer());
		centerPanel.add(this.gaip1);
		this.gaip2 = new GenericAnalysisInformationPanel(
				this.getParentContainer());
		centerPanel.add(this.gaip2);
		this.add(centerPanel, BorderLayout.CENTER);
		final JPanel buttPanel = new JPanel();
		buttPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		this.action_btt = new JButton("Next");
		this.action_btt.setPreferredSize(OmegaConstants.BUTTON_SIZE);
		buttPanel.add(this.action_btt);

		this.close_btt = new JButton(OmegaGUIConstants.MENU_FILE_CLOSE);
		this.close_btt.setPreferredSize(OmegaConstants.BUTTON_SIZE);
		buttPanel.add(this.close_btt);

		this.add(buttPanel, BorderLayout.SOUTH);
	}

	@Override
	protected void addListeners() {
		this.images_cmb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolTargetSelectorDialog.this.selectImage();
			}
		});
		this.particles_cmb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				OmegaTracksToolTargetSelectorDialog.this
				        .selectParticleDetectionRun();
			}
		});
		this.trajectories_cmb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				OmegaTracksToolTargetSelectorDialog.this
				        .selectParticleLinkingRun();
			}
		});
		this.action_btt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolTargetSelectorDialog.this.handleAction();
			}
		});
		this.close_btt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolTargetSelectorDialog.this.handleClose();
			};
		});
	}

	@Override
	public void setVisible(final boolean isVisible) {
		if (isVisible) {
			this.next = false;
		}
		super.setVisible(isVisible);
	}

	public boolean isNext() {
		return this.next;
	}

	private void handleAction() {
		this.next = true;
		this.setVisible(false);
	}

	private void handleClose() {
		this.setVisible(false);
	}

	private void selectImage() {
		if (this.popImages)
			return;
		final int index = this.images_cmb.getSelectedIndex();
		this.selectedImage = null;
		if (index == -1) {
			this.populateParticlesCombo();
			// this.resetTrajectories();
			return;
		}
		if ((this.images == null) || (index >= this.images.size())) {
			this.selectedImage = this.orphanedAnalysis;
			// this.sbPanel.setImage(null);
		} else {
			this.selectedImage = this.images.get(index);
		}
		// if (!this.isHandlingEvent) {
		// this.fireEventSelectionPluginImage();
		// }
		this.geip.updateContent((OmegaElement) this.selectedImage);
		this.populateParticlesCombo();
		// this.populateTrajectoriesCombo();
	}

	private void selectParticleDetectionRun() {
		if (this.popParticles)
			return;
		final int index = this.particles_cmb.getSelectedIndex();
		this.selectedParticleDetectionRun = null;
		this.gaip1.updateContent(this.selectedParticleDetectionRun);
		if ((index == -1) || this.isImporter) {
			this.populateTrajectoriesCombo();
			// this.runPanel.populateSNRCombo();
			// this.resetTrajectories();
			return;
		}
		this.selectedParticleDetectionRun = this.particleDetectionRuns
		        .get(index);
		// if (!this.isHandlingEvent) {
		// this.fireEventSelectionPluginParticleDetectionRun();
		// }
		this.gaip1.updateContent(this.selectedParticleDetectionRun);
		this.populateTrajectoriesCombo();
		// this.runPanel.populateSNRCombo();
	}

	private void selectParticleLinkingRun() {
		if (this.popTrajectories)
			return;
		final int index = this.trajectories_cmb.getSelectedIndex();
		this.selectedParticleLinkingRun = null;
		this.gaip2.updateContent(this.selectedParticleLinkingRun);
		if ((index == -1) || this.isImporter)
			// this.populateTrajectoriesRelinkingCombo();
			// this.populateTrackingMeasuresCombo();
			// this.resetTrajectories();
			return;
		this.selectedParticleLinkingRun = this.particleLinkingRuns.get(index);
		// if (!this.isHandlingEvent) {
		// this.fireEventSelectionParticleLinkingRun();
		// }
		this.selectedParticleLinkingRun.getAlgorithmSpec().getParameter(
		        OmegaConstantsAlgorithmParameters.PARAM_RADIUS);
		// if ((radius != null)
		// && radius.getClazz().equals(Integer.class.getName())) {
		// this.setRadius((int) radius.getValue());
		// }
		// this.populateTrajectoriesRelinkingCombo();
		// this.populateTrackingMeasuresCombo();
		// this.tbPanel.updateTrajectories(
		// this.selectedParticleLinkingRun.getResultingTrajectories(),
		// false);
		this.gaip2.updateContent(this.selectedParticleLinkingRun);
	}

	private void populateImagesCombo() {
		this.popImages = true;
		this.images_cmb.removeAllItems();
		this.selectedImage = null;
		this.images_cmb.setSelectedIndex(-1);
		this.images_cmb.setEnabled(true);

		if (this.images != null) {
			for (final OmegaImage image : this.images) {
				this.images_cmb.addItem(image.getName());
			}
		}
		this.images_cmb.addItem(OmegaGUIConstants.PLUGIN_ORPHANED_ANALYSES);
		this.popImages = false;

		if (this.images_cmb.getItemCount() > 0) {
			this.images_cmb.setSelectedIndex(0);
		} else {
			this.images_cmb.setSelectedIndex(-1);
		}
	}

	private void populateParticlesCombo() {
		this.popParticles = true;
		this.particles_cmb.removeAllItems();
		this.particleDetectionRuns.clear();
		this.particles_cmb.setSelectedIndex(-1);
		this.selectedParticleDetectionRun = null;
		this.gaip1.updateContent(this.selectedParticleDetectionRun);
		if ((this.selectedImage == null) || this.isImporter) {
			this.particles_cmb.setEnabled(false);
			this.populateTrajectoriesCombo();
			// this.runPanel.populateSNRCombo();
			this.popParticles = false;
			return;
		}

		for (final OmegaAnalysisRun analysisRun : this.loadedAnalysisRuns) {
			if (this.selectedImage.getAnalysisRuns().contains(analysisRun)
			        && (analysisRun instanceof OmegaParticleDetectionRun)) {
				this.particleDetectionRuns
				        .add((OmegaParticleDetectionRun) analysisRun);
				this.particles_cmb.addItem(analysisRun.getName());
			}
		}

		if (this.particleDetectionRuns.isEmpty()) {
			this.particles_cmb.setEnabled(false);
			this.populateTrajectoriesCombo();
			// this.runPanel.populateSNRCombo();
			this.popParticles = false;
			return;
		}

		this.popParticles = false;
		if (this.particles_cmb.getItemCount() > 0) {
			this.particles_cmb.setEnabled(true);
			this.particles_cmb.setSelectedIndex(0);
		} else {
			this.particles_cmb.setSelectedIndex(-1);
		}
	}

	private void populateTrajectoriesCombo() {
		this.popTrajectories = true;
		this.trajectories_cmb.removeAllItems();
		this.particleLinkingRuns.clear();
		this.trajectories_cmb.setSelectedIndex(-1);
		this.selectedParticleLinkingRun = null;
		this.gaip2.updateContent(this.selectedParticleLinkingRun);
		if ((this.selectedParticleDetectionRun == null) || this.isImporter) {
			this.trajectories_cmb.setEnabled(false);
			// this.populateTrajectoriesRelinkingCombo();
			// this.populateTrackingMeasuresCombo();
			// this.resetTrajectories();
			return;
		}

		for (final OmegaAnalysisRun analysisRun : this.loadedAnalysisRuns) {
			if ((analysisRun instanceof OmegaParticleLinkingRun)
					&& this.selectedParticleDetectionRun.getAnalysisRuns()
					.contains(analysisRun)) {
				this.particleLinkingRuns
				        .add((OmegaParticleLinkingRun) analysisRun);
				this.trajectories_cmb.addItem(analysisRun.getName());
			}
		}
		if (this.particleLinkingRuns.isEmpty()) {
			this.trajectories_cmb.setEnabled(false);
			// this.populateTrajectoriesRelinkingCombo();
			// this.populateTrackingMeasuresCombo();
			this.popTrajectories = false;
			return;
		}

		this.popTrajectories = false;
		if (this.trajectories_cmb.getItemCount() > 0) {
			this.trajectories_cmb.setEnabled(true);
			this.trajectories_cmb.setSelectedIndex(0);
		} else {
			this.trajectories_cmb.setSelectedIndex(-1);
		}
	}

	public void updateCombos(final List<OmegaImage> images,
			final OrphanedAnalysisContainer orphanedAnalysis,
			final List<OmegaAnalysisRun> analysisRuns) {
		// this.isHandlingEvent = true;
		this.images = images;
		this.orphanedAnalysis = orphanedAnalysis;
		this.loadedAnalysisRuns = analysisRuns;
		// this.runPanel.updateCombos(analysisRuns);

		this.populateImagesCombo();
		// this.isHandlingEvent = false;
	}

	public void setImporter(final boolean isImporter) {
		this.isImporter = isImporter;
		this.particles_cmb.setEnabled(!isImporter);
		this.trajectories_cmb.setEnabled(!isImporter);
		if (isImporter) {
			this.particles_cmb.setSelectedIndex(-1);
			this.trajectories_cmb.setSelectedIndex(-1);
		} else {
			this.particles_cmb.setSelectedIndex(0);
			this.trajectories_cmb.setSelectedIndex(0);
		}
	}

	public OmegaAnalysisRunContainerInterface getSelectedImage() {
		return this.selectedImage;
	}

	public OmegaParticleDetectionRun getSelectedParticleDetectionRun() {
		return this.selectedParticleDetectionRun;
	}

	public OmegaParticleLinkingRun getSelectedParticleLinkingRun() {
		return this.selectedParticleLinkingRun;
	}

	@Override
	public void fireElementChanged() {
		// this.geip.update((OmegaElement) this.selectedImage);
	}

	@Override
	public void updateParentContainer(final RootPaneContainer parentContainer) {
		super.updateParentContainer(parentContainer);
		this.geip.updateParentContainer(parentContainer);
		this.gaip1.updateParentContainer(parentContainer);
		this.gaip2.updateParentContainer(parentContainer);
	}
}
