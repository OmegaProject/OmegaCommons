package edu.umassmed.omega.commons.gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.RootPaneContainer;

import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.eventSystem.events.OmegaMessageEvent;
import edu.umassmed.omega.commons.gui.interfaces.OmegaMessageDisplayerPanelInterface;

public abstract class GenericBrowserPanel extends GenericPanel implements
		OmegaMessageDisplayerPanelInterface {

	private static final long serialVersionUID = -6838845023522379475L;

	protected static final int SPOT_SIZE_DEFAULT = 30;
	protected static final int SPOT_SPACE_DEFAULT = 50;
	protected static final int TRAJECTORY_NAME_SPACE_MODIFIER = 4;
	protected static final int TRAJECTORY_SQUARE_BORDER = 3;
	protected static final int TRAJECTORY_TRIANGLE_SIZE = 10;
	protected static final int TRAJECTORY_TRIANGLE_POINTS = 3;

	private final List<OmegaTrajectory> trajectories, shownTrajectories,
			selectedTrajectories;

	private OmegaTrajectory selectedTraj;
	private OmegaROI selectedParticle;

	private int radius, numOfTraj, maxTrajLength, sizeT;
	private boolean isSpotsEnabled, isSpotsAutomaticallyDisabled;
	private final boolean isShowEnabled, isSelectionEnabled;

	public GenericBrowserPanel(final RootPaneContainer parent,
			final boolean showEnabled, final boolean selectionEnabled) {
		super(parent);

		this.trajectories = new ArrayList<OmegaTrajectory>();
		this.shownTrajectories = new ArrayList<OmegaTrajectory>();
		this.selectedTrajectories = new ArrayList<OmegaTrajectory>();

		this.selectedTraj = null;
		this.selectedParticle = null;

		this.isSpotsEnabled = true;
		this.isSpotsAutomaticallyDisabled = false;
		this.isShowEnabled = showEnabled;
		this.isSelectionEnabled = selectionEnabled;

		this.radius = 4;
		this.numOfTraj = 0;
		this.maxTrajLength = 0;
		this.sizeT = 0;
	}

	protected int getSizeT() {
		return this.sizeT;
	}

	protected void setSizeT(final int sizeT) {
		this.sizeT = sizeT;
	}

	protected int getMaxTrajectoryLength() {
		return this.maxTrajLength;
	}

	protected void setMaxTrajectoryLength(final int maxTrackLength) {
		this.maxTrajLength = maxTrackLength;
	}

	protected int getNumberOfTrajectories() {
		return this.numOfTraj;
	}

	protected void setNumberOfTrajectories(final int numOfTracks) {
		this.numOfTraj = numOfTracks;
	}

	protected List<OmegaTrajectory> getTrajectories() {
		return this.trajectories;
	}

	protected List<OmegaTrajectory> getShownTrajectories() {
		return this.shownTrajectories;
	}

	protected boolean isShowParticles() {
		return this.isSpotsEnabled;
	}
	
	protected boolean isShowParticlesAutomaticallyDisabled() {
		return this.isSpotsAutomaticallyDisabled;
	}
	
	public void setShowParticlesAutomaticallyDisabled(
			final boolean isSpotsAutomaticallyDisabled) {
		this.isSpotsAutomaticallyDisabled = isSpotsAutomaticallyDisabled;
	}

	public void setShowParticles(final boolean showParticles) {
		this.isSpotsEnabled = showParticles;
	}

	protected OmegaTrajectory getSelectedTrajectory() {
		return this.selectedTraj;
	}

	protected void setSelectedTrajectory(final OmegaTrajectory selectedTraj) {
		this.selectedTraj = selectedTraj;
	}

	protected OmegaROI getSelectedParticle() {
		return this.selectedParticle;
	}

	protected void setSelectedParticle(final OmegaROI selectedParticle) {
		this.selectedParticle = selectedParticle;
	}

	public List<OmegaTrajectory> getSelectedTrajectories() {
		return this.selectedTrajectories;
	}

	protected boolean isShowEnabled() {
		return this.isShowEnabled;
	}

	protected boolean isSelectionEnabled() {
		return this.isSelectionEnabled;
	}

	public void setRadius(final int radius) {
		this.radius = radius;
	}

	protected int getRadius() {
		return this.radius;
	}

	@Override
	public abstract void updateMessageStatus(final OmegaMessageEvent evt);

	public abstract Dimension getTrajectoriesPanelSize();

	public abstract void addToPaint(Graphics2D g2d);
}
