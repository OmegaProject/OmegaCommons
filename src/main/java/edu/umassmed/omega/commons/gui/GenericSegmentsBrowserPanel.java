package edu.umassmed.omega.commons.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.RootPaneContainer;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import edu.umassmed.omega.commons.constants.OmegaGUIConstants;
import edu.umassmed.omega.commons.data.coreElements.OmegaImage;
import edu.umassmed.omega.commons.data.coreElements.OmegaImagePixels;
import edu.umassmed.omega.commons.data.imageDBConnectionElements.OmegaGateway;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegmentationTypes;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.eventSystem.OmegaFilterEventListener;
import edu.umassmed.omega.commons.eventSystem.events.OmegaFilterEvent;
import edu.umassmed.omega.commons.eventSystem.events.OmegaMessageEvent;
import edu.umassmed.omega.commons.eventSystem.events.OmegaMessageEventTBLoader;
import edu.umassmed.omega.commons.gui.dialogs.GenericConfirmationDialog;
import edu.umassmed.omega.commons.gui.interfaces.GenericSegmentsBrowserContainerInterface;
import edu.umassmed.omega.commons.gui.interfaces.OmegaMessageDisplayerPanelInterface;
import edu.umassmed.omega.commons.utilities.OmegaColorManagerUtilities;

public class GenericSegmentsBrowserPanel extends GenericBrowserPanel implements
OmegaMessageDisplayerPanelInterface, OmegaFilterEventListener {
	private static final long serialVersionUID = -4914198379223290679L;

	protected static final int SPOT_SIZE_DEFAULT = 30;
	protected static final int SPOT_SPACE_DEFAULT = 50;
	protected static final int TRAJECTORY_NAME_SPACE_MODIFIER = 4;
	protected static final int TRAJECTORY_SQUARE_BORDER = 3;

	private final GenericSegmentsBrowserContainerInterface tbContainer;

	private GenericFilterPanel filterPanel;

	private GenericTrajectoriesBrowserHeaderPanel tbHeaderPanel;
	private JScrollPane tbHeaderScrollPane;
	private GenericSegmentsBrowserTrajectoriesPanel sbTrajectoriesPanel;
	private JScrollPane tbTrajectoriesScrollPane;
	private GenericSegmentsBrowserNamesPanel tbNamesPanel;
	private JScrollPane tbNamesScrollPane;
	private GenericTrajectoriesBrowserLabelsPanel tbLabelsPanel;

	private JPopupMenu tbMenu;
	private JMenuItem showParticles_itm, generateRandomColors_itm,
	chooseColor_itm;

	private OmegaGateway gateway;
	private OmegaImage img;

	private final Map<OmegaTrajectory, List<OmegaSegment>> segments,
	shownSegments, selectedSegments;

	private OmegaSegment selectedSegment;

	private int numOfSegms;

	private OmegaSegmentationTypes segmTypes;

	public GenericSegmentsBrowserPanel(final RootPaneContainer parent,
			final GenericSegmentsBrowserContainerInterface tbContainer,
			final OmegaGateway gateway, final boolean showEnabled,
			final boolean selectionEnabled) {
		super(parent, showEnabled, selectionEnabled);

		this.tbContainer = tbContainer;

		this.gateway = gateway;
		this.img = null;

		this.segments = new LinkedHashMap<OmegaTrajectory, List<OmegaSegment>>();
		this.shownSegments = new LinkedHashMap<OmegaTrajectory, List<OmegaSegment>>();
		this.selectedSegments = new LinkedHashMap<OmegaTrajectory, List<OmegaSegment>>();

		this.selectedSegment = null;

		this.numOfSegms = 0;

		this.segmTypes = null;

		this.createPopupMenu();

		this.setLayout(new BorderLayout());

		this.createAndAddWidgets();

		this.addListeners();
	}

	private void createAndAddWidgets() {
		final JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());

		final List<String> columNames = new ArrayList<String>();
		columNames.add("ID");
		columNames.add("Name");
		columNames.add("Length");
		this.filterPanel = new GenericFilterPanel(this.getParentContainer());
		this.filterPanel.updateCombo(columNames);
		this.filterPanel.addOmegaFilterListener(this);
		topPanel.add(this.filterPanel, BorderLayout.NORTH);

		this.tbLabelsPanel = new GenericTrajectoriesBrowserLabelsPanel(
				this.getParentContainer(), this.isShowEnabled());
		topPanel.add(this.tbLabelsPanel, BorderLayout.WEST);

		this.tbHeaderPanel = new GenericTrajectoriesBrowserHeaderPanel(
				this.getParentContainer(), this);
		this.tbHeaderScrollPane = new JScrollPane(this.tbHeaderPanel);
		this.tbHeaderScrollPane
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		this.tbHeaderScrollPane
		.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		topPanel.add(this.tbHeaderScrollPane, BorderLayout.CENTER);

		this.add(topPanel, BorderLayout.NORTH);

		this.tbNamesPanel = new GenericSegmentsBrowserNamesPanel(
				this.getParentContainer(), this, this.isSelectionEnabled(),
				this.isShowEnabled());
		this.tbNamesScrollPane = new JScrollPane(this.tbNamesPanel);
		this.tbNamesScrollPane
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		this.tbNamesScrollPane
		.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(this.tbNamesScrollPane, BorderLayout.WEST);

		this.sbTrajectoriesPanel = new GenericSegmentsBrowserTrajectoriesPanel(
				this.getParentContainer(), this, this.gateway,
				this.isSelectionEnabled());
		this.tbTrajectoriesScrollPane = new JScrollPane(
				this.sbTrajectoriesPanel);
		this.add(this.tbTrajectoriesScrollPane, BorderLayout.CENTER);
	}

	private void createPopupMenu() {
		this.tbMenu = new JPopupMenu();
		this.showParticles_itm = new JMenuItem(
				OmegaGUIConstants.TRACK_BROWSER_HIDE_SPOT_THUMB);
		this.generateRandomColors_itm = new JMenuItem(
				OmegaGUIConstants.RANDOM_COLORS);
		this.chooseColor_itm = new JMenuItem(OmegaGUIConstants.CHOSE_COLOR);
	}

	private void addListeners() {
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent evt) {
				GenericSegmentsBrowserPanel.this.handleResize();
			}
		});
		this.sbTrajectoriesPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(final MouseEvent evt) {
				GenericSegmentsBrowserPanel.this.handleMouseIn(evt.getPoint());
			}

			@Override
			public void mouseExited(final MouseEvent evt) {
				GenericSegmentsBrowserPanel.this.handleMouseOut();
			}
		});
		this.sbTrajectoriesPanel
		.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(final MouseEvent evt) {
				GenericSegmentsBrowserPanel.this
				.handleMouseMovement(evt.getPoint());
			}
		});
		this.tbTrajectoriesScrollPane.getHorizontalScrollBar()
		.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(final AdjustmentEvent evt) {
				GenericSegmentsBrowserPanel.this
				.handleHorizontalScrollBarChanged();
			}
		});
		this.tbTrajectoriesScrollPane.getVerticalScrollBar()
		.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(final AdjustmentEvent evt) {
				GenericSegmentsBrowserPanel.this
				.handleVerticalScrollBarChanged();
			}
		});
		this.sbTrajectoriesPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent evt) {
				GenericSegmentsBrowserPanel.this.handleMouseClick(
						evt.getPoint(), SwingUtilities.isRightMouseButton(evt)
						|| evt.isControlDown(), evt.isShiftDown(), true);
			}
		});
		this.tbNamesPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent evt) {
				GenericSegmentsBrowserPanel.this.handleMouseClick(
						evt.getPoint(), SwingUtilities.isRightMouseButton(evt)
						|| evt.isControlDown(), evt.isShiftDown(),
						false);
			}
		});
		this.showParticles_itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				GenericSegmentsBrowserPanel.this.handleShowSpotsThumbnail();
			}
		});
		this.generateRandomColors_itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				GenericSegmentsBrowserPanel.this.handleGenerateRandomColors();
			}
		});
		this.chooseColor_itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				GenericSegmentsBrowserPanel.this.handlePickSingleColor();
			}
		});
	}

	private void handleResize() {
		this.sbTrajectoriesPanel.setPanelSize();
		this.tbNamesPanel.setPanelSize();
		this.tbHeaderPanel.setPanelSize();
	}

	protected void handleMouseOut() {

	}

	protected void handleMouseIn(final Point pos) {

	}

	protected void handleMouseMovement(final Point pos) {

	}

	private void handleHorizontalScrollBarChanged() {
		final int value = this.tbTrajectoriesScrollPane
				.getHorizontalScrollBar().getValue();
		this.tbHeaderScrollPane.getHorizontalScrollBar().setValue(value);
	}

	private void handleVerticalScrollBarChanged() {
		final int value = this.tbTrajectoriesScrollPane.getVerticalScrollBar()
				.getValue();
		this.tbNamesScrollPane.getVerticalScrollBar().setValue(value);
	}

	protected void handleMouseClick(final Point clickP,
			final boolean isRightButton, final boolean isShiftDown,
			final boolean isTrackPanel) {
		final OmegaSegment oldSegm = this.getSelectedSegment();
		this.resetClickReferences();
		this.findSelectedTrajectory(clickP);
		this.findSelectedSegment(clickP);
		if (isRightButton) {
			if (isTrackPanel && (this.getSelectedSegment() != null)) {
				this.findSelectedParticle(clickP);
			}
			this.createTrajectoryMenu();
			this.showTrajectoryMenu(clickP, isTrackPanel);
			this.getSelectedTrajectories().clear();
			this.selectedSegments.clear();
		} else {
			if (this.getSelectedSegment() != null) {
				this.checkIfCheckboxAndSelect(clickP);
			}
			if (!isShiftDown) {
				this.getSelectedTrajectories().clear();
				this.selectedSegments.clear();
			}
		}

		// FIXME there is a bug when right click on a track already selected, it
		// becomes unselected e when picked a item in the menu the methods does
		// not find selectedtraj reference!
		if ((this.getSelectedSegment() != null)) {
			if (isRightButton || (this.getSelectedSegment() != oldSegm)) {
				this.getSelectedTrajectories()
				.add(this.getSelectedTrajectory());
				if (this.selectedSegments.containsKey(this
						.getSelectedTrajectory())) {
					this.selectedSegments.get(this.getSelectedTrajectory())
					.add(this.selectedSegment);
				} else {
					final List<OmegaSegment> segments = new ArrayList<OmegaSegment>();
					segments.add(this.selectedSegment);
					this.selectedSegments.put(this.getSelectedTrajectory(),
							segments);
				}
			}
		} else {
			if (!isRightButton) {
				this.setSelectedTrajectory(null);
				this.getSelectedTrajectories().clear();
				this.selectedSegment = null;
				this.selectedSegments.clear();
			}
			// this.selectedTraj = null;
		}
		if (this.selectedSegment == null) {
			this.sendEventTrajectories(this.getSelectedTrajectories(), true);
		} else {
			this.sendEventSegments(this.getSelectedSegments(), true);
		}
		this.repaint();
	}

	protected void resetClickReferences() {
		this.selectedSegment = null;
		this.setSelectedTrajectory(null);
		this.setSelectedParticle(null);
	}

	protected void findSelectedTrajectory(final Point clickP) {
		this.sbTrajectoriesPanel.findSelectedTrajectory(clickP);
	}

	protected void findSelectedSegment(final Point clickP) {
		this.sbTrajectoriesPanel.findSelectedSegment(clickP);
	}

	protected void checkIfCheckboxAndSelect(final Point clickP) {
		this.tbNamesPanel.checkIfCheckboxAndSelect(clickP);
	}

	protected void findSelectedParticle(final Point clickP) {
		this.sbTrajectoriesPanel.findSelectedParticle(clickP);
	}

	protected void createTrajectoryMenu() {
		this.tbMenu.removeAll();

		final StringBuffer buf = new StringBuffer();
		int frameIndex = -1;
		if (this.getSelectedTrajectory() != null) {
			buf.append("Segment ");
			buf.append(this.getSelectedTrajectory().getName());
			if (this.getSelectedParticle() != null) {
				frameIndex = this.getSelectedParticle().getFrameIndex() + 1;
				buf.append(" - Frame ");
				buf.append(frameIndex);
			}
			this.tbMenu.add(new JLabel(buf.toString()));
		}
		if (this.tbMenu.getComponentCount() > 0) {
			this.tbMenu.add(new JSeparator());
		}
		this.tbMenu.add(this.showParticles_itm);
		this.tbMenu.add(new JSeparator());
		this.tbMenu.add(this.generateRandomColors_itm);
		if (this.getSelectedTrajectory() != null) {
			this.tbMenu.add(this.chooseColor_itm);
		}
	}

	protected void showTrajectoryMenu(final Point clickP,
	        final boolean isTrackPanel) {
		if (isTrackPanel) {
			this.tbMenu.show(this.sbTrajectoriesPanel, clickP.x, clickP.y);
		} else {
			this.tbMenu.show(this.tbNamesPanel, clickP.x, clickP.y);
		}
	}

	protected void disableShowSportsThumbnail() {
		this.showParticles_itm
		        .setText(OmegaGUIConstants.TRACK_BROWSER_SHOW_SPOT_THUMB);
		this.setShowParticles(!this.isShowParticles());
	}

	private void handleShowSpotsThumbnail() {
		if (this.isShowParticles()) {
			this.showParticles_itm
			.setText(OmegaGUIConstants.TRACK_BROWSER_SHOW_SPOT_THUMB);
			this.setShowParticles(!this.isShowParticles());
		} else {
			this.showParticles_itm
			.setText(OmegaGUIConstants.TRACK_BROWSER_HIDE_SPOT_THUMB);
			this.setShowParticles(!this.isShowParticles());
		}
		this.repaint();
	}

	private void handleGenerateRandomColors() {
		final GenericConfirmationDialog dialog = new GenericConfirmationDialog(
				this.getParentContainer(),
				OmegaGUIConstants.TRACK_RANDOM_COLOR_CONFIRM,
				OmegaGUIConstants.TRACK_RANDOM_COLOR_CONFIRM_MSG, true);
		dialog.setVisible(true);
		if (!dialog.getConfirmation())
			return;
		final List<Color> colors = OmegaColorManagerUtilities
				.generateRandomColors(this.getTrajectories().size());
		for (int i = 0; i < this.getTrajectories().size(); i++) {
			final OmegaTrajectory traj = this.getTrajectories().get(i);
			final Color c = colors.get(i);
			traj.setColor(c);
			traj.setColorChanged(true);
		}
		this.repaint();
		this.sendEventTrajectories(this.getTrajectories(), false);
	}

	private void handlePickSingleColor() {
		final StringBuffer buf1 = new StringBuffer();
		buf1.append(OmegaGUIConstants.TRACK_CHOSE_COLOR_DIALOG_MSG);
		buf1.append(this.getSelectedTrajectory().getName());

		final Color c = OmegaColorManagerUtilities.openPaletteColor(this,
				buf1.toString(), this.getSelectedTrajectory().getColor());

		final StringBuffer buf2 = new StringBuffer();
		buf2.append(OmegaGUIConstants.TRACK_CHOSE_COLOR_CONFIRM_MSG);
		buf2.append(this.getSelectedTrajectory().getName());
		buf2.append("?");

		final GenericConfirmationDialog dialog = new GenericConfirmationDialog(
				this.getParentContainer(),
				OmegaGUIConstants.TRACK_CHOSE_COLOR_CONFIRM, buf2.toString(),
				true);
		dialog.setVisible(true);
		if (!dialog.getConfirmation())
			return;

		this.getSelectedTrajectory().setColor(c);
		this.getSelectedTrajectory().setColorChanged(true);
		this.repaint();
		final List<OmegaTrajectory> trajectories = new ArrayList<OmegaTrajectory>();
		trajectories.addAll(this.getTrajectories());
		this.sendEventTrajectories(trajectories, false);
	}

	@Override
	public void addToPaint(final Graphics2D g2D) {

	}

	public void updateSegments(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final OmegaSegmentationTypes segmTypes, final boolean selection) {
		this.segmTypes = segmTypes;
		if ((this.img == null) && (segments != null) && !segments.isEmpty()) {
			int maxT = -1;
			for (final OmegaTrajectory track : segments.keySet()) {
				if (maxT < track.getLength()) {
					maxT = track.getLength();
				}
			}
			this.setSizeT(maxT);
		}
		if (selection) {
			this.resetClickReferences();
			this.selectedSegments.clear();
			this.getSelectedTrajectories().clear();
			if (segments != null) {
				this.selectedSegments.putAll(segments);
				this.getSelectedTrajectories().addAll(segments.keySet());
				if (!segments.isEmpty()) {
					this.centerOnTrajectory(this.getSelectedTrajectories().get(
							0));
				}
			}
		} else {
			this.getTrajectories().clear();
			this.segments.clear();
			if (segments != null) {
				this.segments.putAll(segments);
				// segments.putAll(segments);
				this.getTrajectories().addAll(segments.keySet());
				this.sbTrajectoriesPanel.setPanelSize();
			}
			this.getShownTrajectories().clear();
			this.shownSegments.clear();
			if (segments != null) {
				this.getShownTrajectories().addAll(this.getTrajectories());
				
				this.shownSegments.putAll(segments);
			}
			this.setTrajectoriesValues();
		}
		// TODO try to simplify the paint method dividing it for single
		// trajectory or something similar its useless to repaint everything
		// each time
		this.handleResize();
		this.repaint();
	}

	// TODO to fix update trajectory must update segments too
	// public void updateTrajectories(final List<OmegaTrajectory> trajectories,
	// final boolean selection) {
	// if ((this.img == null) && (trajectories != null)
	// && !trajectories.isEmpty()) {
	// int maxT = -1;
	// for (final OmegaTrajectory traj : trajectories) {
	// if (maxT < traj.getLength()) {
	// maxT = traj.getLength();
	// }
	// }
	// this.setSizeT(maxT);
	// }
	// if (selection) {
	// this.resetClickReferences();
	// this.getSelectedTrajectories().clear();
	// if (trajectories != null) {
	// this.getSelectedTrajectories().addAll(trajectories);
	// if (!trajectories.isEmpty()) {
	// this.centerOnTrajectory(trajectories.get(0));
	// }
	// }
	// } else {
	// this.getTrajectories().clear();
	// if (trajectories != null) {
	// this.getTrajectories().addAll(trajectories);
	// this.sbTrajectoriesPanel.setPanelSize();
	// }
	// this.getShownTrajectories().clear();
	// this.shownSegments.clear();
	// if (trajectories != null) {
	// this.getShownTrajectories().addAll(trajectories);
	// }
	// this.setTrajectoriesValues();
	// }
	// // TODO try to simplify the paint method dividing it for single
	// // trajectory or something similar its useless to repaint everything
	// // each time
	// this.handleResize();
	// this.repaint();
	// }

	// TODO Change to work on segments
	private void centerOnTrajectory(final OmegaTrajectory traj) {
		final Point p = this.sbTrajectoriesPanel.findTrajectoryLocation(traj);
		if (p != null) {
			final int xPos = p.x
					- (this.tbTrajectoriesScrollPane.getWidth() / 2);
			final int yPos = p.y
					- (this.tbTrajectoriesScrollPane.getHeight() / 2);
			this.tbTrajectoriesScrollPane.getVerticalScrollBar().setValue(yPos);
			this.tbTrajectoriesScrollPane.getHorizontalScrollBar().setValue(
					xPos);
		}
	}

	public void addTrajectoriesToSelection(
			final List<OmegaTrajectory> trajectories, final int index) {
		this.getSelectedTrajectories().addAll(index, trajectories);
		for (final OmegaTrajectory track : trajectories) {
			this.selectedSegments.put(track, this.segments.get(track));
		}
	}

	public int removeTrajectoriesFromSelection(
			final List<OmegaTrajectory> trajectories) {
		if (trajectories.isEmpty())
			return this.getSelectedTrajectories().size();
		int index = this.getSelectedTrajectories().indexOf(trajectories.get(0));
		for (final OmegaTrajectory track : trajectories) {
			this.shownSegments.remove(track);
		}
		this.getSelectedTrajectories().removeAll(trajectories);
		if (index < 0) {
			index = 0;
		}
		return index;
	}

	private void setTrajectoriesValues() {
		// TODO can be delegate to the superclass
		int numOfSegments = 0;
		for (final OmegaTrajectory track : this.shownSegments.keySet()) {
			numOfSegments += this.shownSegments.get(track).size();
		}
		this.setNumberOfSegments(numOfSegments);
		this.setNumberOfTrajectories(this.getShownTrajectories().size());
		if (this.getShownTrajectories().isEmpty()) {
			this.setMaxTrajectoryLength(0);
			return;
		}
		for (final OmegaTrajectory trajectory : this.getShownTrajectories()) {
			if (this.getMaxTrajectoryLength() < trajectory.getLength()) {
				this.setMaxTrajectoryLength(trajectory.getLength());
			}
		}
	}

	public void setGateway(final OmegaGateway gateway) {
		this.gateway = gateway;
		this.sbTrajectoriesPanel.setGateway(gateway);
		if (this.img == null)
			return;
		final OmegaImagePixels pixels = this.img.getDefaultPixels();
		final Double physicalSizeT = gateway.computeSizeT(pixels.getOmeroId(),
				pixels.getSizeT(), pixels.getSizeT());
		this.tbHeaderPanel.setPhysicalSizeT(physicalSizeT);
		this.tbLabelsPanel.setHasPhysicalSizeT(physicalSizeT != null);
	}

	public void setImage(final OmegaImage image) {
		if (image == null) {
			this.img = null;
			this.setSizeT(-1);
			this.tbHeaderPanel.setPhysicalSizeT(null);
		} else {
			this.img = image;
			final OmegaImagePixels pixels = this.img.getDefaultPixels();
			this.setSizeT(pixels.getSizeT());
			final Double physicalSizeT = this.gateway.computeSizeT(
					pixels.getElementID(), this.getSizeT(), this.getSizeT());
			this.tbHeaderPanel.setPhysicalSizeT(physicalSizeT);
			this.tbLabelsPanel.setHasPhysicalSizeT(physicalSizeT != null);
		}
		this.sbTrajectoriesPanel.setImage(image);
	}

	@Override
	public void updateMessageStatus(final OmegaMessageEvent evt) {
		final OmegaMessageEventTBLoader specificEvt = (OmegaMessageEventTBLoader) evt;
		this.updateStatus(specificEvt.getMessage());
		if (specificEvt.isRepaint()) {
			this.sbTrajectoriesPanel.loadBufferedImages();
		}
	}

	protected Point getClickPosition() {
		return this.sbTrajectoriesPanel.getClickPosition();
	}

	protected JPopupMenu getMenu() {
		return this.tbMenu;
	}

	protected void sendEventTrajectories(
			final List<OmegaTrajectory> selectedTrajectories,
			final boolean selected) {
		this.tbContainer.sendEventTrajectories(selectedTrajectories, selected);
	}

	protected void sendEventSegments(
			final Map<OmegaTrajectory, List<OmegaSegment>> selectedSegments,
			final boolean selected) {
		this.tbContainer.sendEventSegments(selectedSegments, selected);
	}

	protected void updateStatus(final String message) {
		this.tbContainer.updateStatus(message);
	}

	@Override
	public Dimension getTrajectoriesPanelSize() {
		final Dimension size = this.getSize();
		final Dimension headerSize = this.tbHeaderPanel.getSize();
		return new Dimension(size.width - headerSize.width, size.height
				- headerSize.height - 25);
	}

	@Override
	public void handleFilterEvent(final OmegaFilterEvent event) {
		final String key = event.getKey();
		final String val = event.getValue();
		final boolean isExact = event.isExact();
		// System.out.println(key + " " + val + " " + isExact);

		this.getShownTrajectories().clear();
		this.shownSegments.clear();
		if ((val == null) || val.equals("")) {
			this.getShownTrajectories().addAll(this.getTrajectories());
			this.shownSegments.putAll(this.segments);
			return;
		}

		if (key.equals("Length")) {
			this.handleLengthFilter(val);
		} else {
			this.handleNormalFilter(key, val, isExact);
		}
		this.setTrajectoriesValues();
		this.handleResize();
		this.repaint();
		// USE COMPARATOR ON THE TRACKS LIST TO REORDER IT
	}

	private void handleLengthFilter(final String value) {
		final boolean equal = value.contains("=");
		final boolean less = value.contains("<");
		final boolean more = value.contains(">");
		if (less && more)
			// TODO THROW ERROR
			return;
		if (!less && !more && !equal)
			// TODO THROW ERROR
			return;
		String number = value.replace("=", "");
		number = number.replace("<", "");
		number = number.replace(">", "");
		Integer val = -1;
		try {
			val = Integer.valueOf(number);
		} catch (final NumberFormatException ex) {
			ex.printStackTrace();
			return;
		}
		// TODO modify it for the length of the segments?! or add an option in
		// the combo for one or the other
		for (final OmegaTrajectory track : this.getTrajectories()) {
			if (less) {
				if (track.getLength() < val) {
					this.getShownTrajectories().add(track);
					// shownSegments.put(track, segments.get(track));
					continue;
				}
			} else if (more) {
				if (track.getLength() > val) {
					this.getShownTrajectories().add(track);
					// shownSegments.put(track, segments.get(track));
					continue;
				}
			}
			if (equal)
				if (track.getLength() == val) {
					this.getShownTrajectories().add(track);
					// shownSegments.put(track, segments.get(track));
				}
		}
	}

	private void handleNormalFilter(final String key, final String value,
			final boolean isExact) {
		String regex = value;
		if (isExact) {
			regex = "^" + value + "$";
		} else {
			regex = ".*" + value + ".*";
		}

		for (final OmegaTrajectory track : this.getTrajectories())
			if (key.equals("ID")) {
				if (track.getElementID().toString().matches(regex)) {
					this.getShownTrajectories().add(track);
					this.shownSegments.put(track, this.segments.get(track));
				}
			} else if (key.equals("Name")) {
				if (track.getName().matches(regex)) {
					this.getShownTrajectories().add(track);
					this.shownSegments.put(track, this.segments.get(track));
				}
			}
	}

	protected OmegaSegment getSelectedSegment() {
		return this.selectedSegment;
	}

	protected void setSelectedSegment(final OmegaSegment selectedSegment) {
		this.selectedSegment = selectedSegment;
	}

	protected int getNumberOfSegments() {
		return this.numOfSegms;
	}

	protected void setNumberOfSegments(final int numOfSegms) {
		this.numOfSegms = numOfSegms;
	}

	public Map<OmegaTrajectory, List<OmegaSegment>> getSegments() {
		return this.segments;
	}

	public Map<OmegaTrajectory, List<OmegaSegment>> getShownSegments() {
		return this.shownSegments;
	}

	public Map<OmegaTrajectory, List<OmegaSegment>> getSelectedSegments() {
		return this.selectedSegments;
	}

	public Color getSegmentColor(final int segmType) {
		return this.segmTypes.getSegmentationColor(segmType);
	}
}
