package edu.umassmed.omega.commons.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import javax.swing.Icon;
import javax.swing.RootPaneContainer;

import edu.umassmed.omega.commons.constants.OmegaConstants;
import edu.umassmed.omega.commons.constants.OmegaGUIConstants;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class GenericSegmentsBrowserNamesPanel extends
        GenericTrajectoriesBrowserNamesPanel {

	private static final long serialVersionUID = 313531450859107197L;

	public GenericSegmentsBrowserNamesPanel(final RootPaneContainer parent,
			final GenericSegmentsBrowserPanel sbPanel,
			final boolean isSelectionEnabled, final boolean isShowEnabled) {
		super(parent, sbPanel, isSelectionEnabled, isShowEnabled);
	}

	@Override
	protected void drawCheckboxes(final Graphics2D g2D) {
		final int space = GenericBrowserPanel.SPOT_SPACE_DEFAULT;
		this.getCheckboxes().clear();
		final GenericSegmentsBrowserPanel sbPanel = ((GenericSegmentsBrowserPanel) this
				.getPanel());
		for (int y = 0; y < sbPanel.getNumberOfSegments(); y++) {
			final int yPos = (space * y) + 5 + (space / 2);
			final OmegaTrajectory track = this.findTrack(y);
			Icon icon;
			if (track.isVisible()) {
				icon = this.getCheckbox().getSelectedIcon();
			} else {
				icon = this.getCheckbox().getIcon();
				// actualIcon = UIManager.getLookAndFeel()
				// .getDisabledSelectedIcon(cb, checkedIcon);
			}
			final int adjY = yPos - (icon.getIconHeight() / 2) - 5;
			icon.paintIcon(this.getCheckbox(), g2D, 5, adjY);
			this.getCheckboxes().add(new Point(5, adjY));
		}
	}

	@Override
	protected void drawIDsAndNames(final Graphics2D g2D) {
		final int space = GenericBrowserPanel.SPOT_SPACE_DEFAULT;
		final GenericSegmentsBrowserPanel sbPanel = ((GenericSegmentsBrowserPanel) this
				.getPanel());
		for (int y = 0; y < sbPanel.getNumberOfSegments(); y++) {
			final int xPos = space;
			final int yPos = (space * y) + 5 + (space / 2);
			final OmegaTrajectory track = this.findTrack(y);
			// OmegaSegment segment = sbPanel.getSegmentAtIndex(y);
			final long id = track.getElementID();
			String idS = String.valueOf(id);
			if (id == -1) {
				idS = OmegaGUIConstants.NOT_ASSIGNED;
			}
			g2D.drawString(idS, xPos, yPos);
			g2D.drawString(track.getName(), xPos * 2, yPos);
		}
	}

	@Override
	protected void drawSelectedTrajectoryBackground(final Graphics2D g2D) {
		final GenericSegmentsBrowserPanel sbPanel = ((GenericSegmentsBrowserPanel) this
				.getPanel());
		for (int y = 0; y < sbPanel.getNumberOfSegments(); y++) {
			final OmegaTrajectory track = this.findTrack(y);
			final OmegaSegment segment = this.findSegment(y);
			// OmegaSegment segment = sbPanel.getSegmentAtIndex(y);
			final int yPos = GenericBrowserPanel.SPOT_SPACE_DEFAULT * y;
			final int adjY = yPos;
			if (sbPanel.getSelectedSegments().containsKey(track)) {
				if (sbPanel.getSelectedSegments().get(track).contains(segment)) {
					g2D.setBackground(OmegaConstants
							.getDefaultSelectionBackgroundColor());
					g2D.clearRect(0, adjY, this.getWidth(),
							GenericBrowserPanel.SPOT_SPACE_DEFAULT);
					g2D.setBackground(Color.white);
				}
			}
		}
	}

	@Override
	protected void setPanelSize() {
		final GenericSegmentsBrowserPanel sbPanel = ((GenericSegmentsBrowserPanel) this
				.getPanel());
		final int numOfSegments = sbPanel.getNumberOfSegments();
		int height = this.getParent().getHeight();
		if (numOfSegments > 0) {
			int heightTmp = numOfSegments
					* GenericBrowserPanel.SPOT_SPACE_DEFAULT;
			heightTmp += 40;
			if (height < heightTmp) {
				height = heightTmp;
			}
		}
		final int width = (GenericBrowserPanel.TRAJECTORY_NAME_SPACE_MODIFIER + 1)
				* GenericBrowserPanel.SPOT_SPACE_DEFAULT;
		final Dimension dim = new Dimension(width, height);
		this.setPreferredSize(dim);
		this.setSize(dim);
	}

	private OmegaTrajectory findTrack(int index) {
		final GenericSegmentsBrowserPanel sbPanel = ((GenericSegmentsBrowserPanel) this
				.getPanel());
		for (final OmegaTrajectory track : sbPanel.getShownSegments().keySet()) {
			final List<OmegaSegment> segments = sbPanel.getShownSegments().get(
					track);
			if (index < segments.size())
				return track;
			index -= segments.size();
		}
		return null;
	}

	private OmegaSegment findSegment(int index) {
		final GenericSegmentsBrowserPanel sbPanel = ((GenericSegmentsBrowserPanel) this
				.getPanel());
		for (final OmegaTrajectory track : sbPanel.getShownSegments().keySet()) {
			final List<OmegaSegment> segments = sbPanel.getShownSegments().get(
					track);
			if (index < segments.size())
				return segments.get(index);
			index -= segments.size();
		}
		return null;
	}
}
