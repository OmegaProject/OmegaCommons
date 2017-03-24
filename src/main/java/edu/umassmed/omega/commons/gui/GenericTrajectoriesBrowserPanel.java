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
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.RootPaneContainer;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import edu.umassmed.omega.commons.OmegaLogFileManager;
import edu.umassmed.omega.commons.constants.OmegaGUIConstants;
import edu.umassmed.omega.commons.data.coreElements.OmegaImage;
import edu.umassmed.omega.commons.data.coreElements.OmegaImagePixels;
import edu.umassmed.omega.commons.data.imageDBConnectionElements.OmegaGateway;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.eventSystem.OmegaFilterEventListener;
import edu.umassmed.omega.commons.eventSystem.events.OmegaFilterEvent;
import edu.umassmed.omega.commons.eventSystem.events.OmegaMessageEvent;
import edu.umassmed.omega.commons.eventSystem.events.OmegaMessageEventTBLoader;
import edu.umassmed.omega.commons.gui.dialogs.GenericConfirmationDialog;
import edu.umassmed.omega.commons.gui.interfaces.GenericTrajectoriesBrowserContainerInterface;
import edu.umassmed.omega.commons.utilities.OmegaColorManagerUtilities;

public class GenericTrajectoriesBrowserPanel extends GenericBrowserPanel
implements OmegaFilterEventListener {
	private static final long serialVersionUID = -4914198379223290679L;

	private final GenericTrajectoriesBrowserContainerInterface tbContainer;

	private GenericFilterPanel filterPanel;

	private GenericTrajectoriesBrowserHeaderPanel tbHeaderPanel;
	private JScrollPane tbHeaderScrollPane;
	private GenericTrajectoriesBrowserTrajectoriesPanel tbTrajectoriesPanel;
	private JScrollPane tbTrajectoriesScrollPane;
	private GenericTrajectoriesBrowserNamesPanel tbNamesPanel;
	private JScrollPane tbNamesScrollPane;
	private GenericTrajectoriesBrowserLabelsPanel tbLabelsPanel;

	private JPopupMenu tbMenu;
	private JMenuItem showParticles_itm, generateRandomColors_itm,
	chooseColor_itm;

	private OmegaGateway gateway;
	private OmegaImage img;

	public GenericTrajectoriesBrowserPanel(final RootPaneContainer parent,
			final GenericTrajectoriesBrowserContainerInterface tbContainer,
			final OmegaGateway gateway, final boolean showEnabled,
			final boolean selectionEnabled) {
		super(parent, showEnabled, selectionEnabled);

		this.tbContainer = tbContainer;

		this.gateway = gateway;
		this.img = null;

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

		this.tbNamesPanel = new GenericTrajectoriesBrowserNamesPanel(
				this.getParentContainer(), this, this.isSelectionEnabled(),
				this.isShowEnabled());
		this.tbNamesScrollPane = new JScrollPane(this.tbNamesPanel);
		this.tbNamesScrollPane
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		this.tbNamesScrollPane
		.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(this.tbNamesScrollPane, BorderLayout.WEST);

		this.tbTrajectoriesPanel = new GenericTrajectoriesBrowserTrajectoriesPanel(
				this.getParentContainer(), this, this.gateway,
				this.isSelectionEnabled());
		this.tbTrajectoriesScrollPane = new JScrollPane(
				this.tbTrajectoriesPanel);
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
				GenericTrajectoriesBrowserPanel.this.handleResize();
			}
		});
		this.tbTrajectoriesPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(final MouseEvent evt) {
				GenericTrajectoriesBrowserPanel.this.handleMouseIn(evt
						.getPoint());
			}

			@Override
			public void mouseExited(final MouseEvent evt) {
				GenericTrajectoriesBrowserPanel.this.handleMouseOut();
			}
		});
		this.tbTrajectoriesPanel
		.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(final MouseEvent evt) {
				GenericTrajectoriesBrowserPanel.this
				.handleMouseMovement(evt.getPoint());
			}
		});
		this.tbTrajectoriesScrollPane.getHorizontalScrollBar()
		.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(final AdjustmentEvent evt) {
				GenericTrajectoriesBrowserPanel.this
				.handleHorizontalScrollBarChanged();
			}
		});
		this.tbTrajectoriesScrollPane.getVerticalScrollBar()
		.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(final AdjustmentEvent evt) {
				GenericTrajectoriesBrowserPanel.this
				.handleVerticalScrollBarChanged();
			}
		});
		this.tbTrajectoriesPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent evt) {
				GenericTrajectoriesBrowserPanel.this.handleMouseClick(
						evt.getPoint(), SwingUtilities.isRightMouseButton(evt)
						|| evt.isControlDown(), evt.isShiftDown(), true);
			}
		});
		this.tbNamesPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent evt) {
				GenericTrajectoriesBrowserPanel.this.handleMouseClick(
						evt.getPoint(), SwingUtilities.isRightMouseButton(evt)
						|| evt.isControlDown(), evt.isShiftDown(),
						false);
			}
		});
		this.showParticles_itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				GenericTrajectoriesBrowserPanel.this.handleShowSpotsThumbnail();
			}
		});
		this.generateRandomColors_itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				GenericTrajectoriesBrowserPanel.this
				.handleGenerateRandomColors();
			}
		});
		this.chooseColor_itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				GenericTrajectoriesBrowserPanel.this.handlePickSingleColor();
			}
		});
	}

	private void handleResize() {
		this.tbTrajectoriesPanel.setPanelSize();
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
		final OmegaTrajectory oldTraj = this.getSelectedTrajectory();
		this.resetClickReferences();
		this.findSelectedTrajectory(clickP);
		boolean selected = false;
		if (isRightButton) {
			if (isTrackPanel && (this.getSelectedTrajectory() != null)) {
				this.findSelectedParticle(clickP);
			}
			this.createTrajectoryMenu();
			this.showTrajectoryMenu(clickP, isTrackPanel);
			this.getSelectedTrajectories().clear();
		} else {
			if ((this.getSelectedTrajectory() != null) && !isTrackPanel) {
				selected = this.selectIfCheckbox(clickP);
			}
			if (!isShiftDown) {
				this.getSelectedTrajectories().clear();
			}
		}

		if (selected) {
			this.resetClickReferences();
			this.getSelectedTrajectories().clear();
		}

		// FIXME there is a bug when right click on a track already selected, it
		// becomes unselected e when picked a item in the menu the methods does
		// not find selectedtraj reference!
		if ((this.getSelectedTrajectory() != null)) {
			if (isRightButton || (this.getSelectedTrajectory() != oldTraj)) {
				this.getSelectedTrajectories()
				.add(this.getSelectedTrajectory());
			} else {
				this.resetClickReferences();
				this.getSelectedTrajectories().clear();
			}
		} else {
			if (!isRightButton) {
				this.resetClickReferences();
				this.getSelectedTrajectories().clear();
			}
			// this.selectedTraj = null;
		}
		this.sendEventTrajectories(this.getSelectedTrajectories(), true);
		this.repaint();
	}

	protected void resetClickReferences() {
		this.setSelectedTrajectory(null);
		this.setSelectedParticle(null);
	}

	protected void findSelectedTrajectory(final Point clickP) {
		this.tbTrajectoriesPanel.findSelectedTrajectory(clickP);
	}

	protected boolean selectIfCheckbox(final Point clickP) {
		return this.tbNamesPanel.selectIfCheckbox(clickP);
	}

	protected void findSelectedParticle(final Point clickP) {
		this.tbTrajectoriesPanel.findSelectedParticle(clickP);
	}

	protected void createTrajectoryMenu() {
		this.tbMenu.removeAll();

		final StringBuffer buf = new StringBuffer();
		int frameIndex = -1;
		if (this.getSelectedTrajectory() != null) {
			buf.append("Track ");
			buf.append(this.getSelectedTrajectory().getName());
			if (this.getSelectedParticle() != null) {
				frameIndex = this.getSelectedParticle().getFrameIndex();
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
			this.tbMenu.show(this.tbTrajectoriesPanel, clickP.x, clickP.y);
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

	public void updateTrajectories(final List<OmegaTrajectory> trajectories,
			final boolean selection) {
		if ((this.img == null) && (trajectories != null)
				&& !trajectories.isEmpty()) {
			int maxT = -1;
			for (final OmegaTrajectory traj : trajectories) {
				if (maxT < traj.getLength()) {
					maxT = traj.getLength();
				}
			}
			this.setSizeT(maxT);
		}
		if (selection) {
			this.resetClickReferences();
			this.getSelectedTrajectories().clear();
			if (trajectories != null) {
				this.getSelectedTrajectories().addAll(trajectories);
				if (!this.getSelectedTrajectories().isEmpty()) {
					this.centerOnTrajectory(this.getSelectedTrajectories().get(
							0));
				} else if (!trajectories.isEmpty()) {
					this.centerOnTrajectory(trajectories.get(0));
				}
			}
		} else {
			this.getTrajectories().clear();
			final List<OmegaTrajectory> preSelected = new ArrayList<OmegaTrajectory>(
					this.getSelectedTrajectories());
			this.getSelectedTrajectories().clear();
			if (trajectories != null) {
				this.getTrajectories().addAll(trajectories);
				for (final OmegaTrajectory track : preSelected) {
					if (trajectories.contains(track)) {
						this.getSelectedTrajectories().add(track);
					}
				}
				this.tbTrajectoriesPanel.setPanelSize();
			}
			this.getShownTrajectories().clear();
			if (trajectories != null) {
				this.getShownTrajectories().addAll(trajectories);
			}
			this.setTrajectoriesValues();
		}
		// TODO try to simplify the paint method dividing it for single
		// trajectory or something similar its useless to repaint everything
		// each time
		this.handleResize();
		this.repaint();
	}

	private void centerOnTrajectory(final OmegaTrajectory traj) {
		final Point p = this.tbTrajectoriesPanel.findTrajectoryLocation(traj);
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
	}

	public int removeTrajectoriesFromSelection(
			final List<OmegaTrajectory> trajectories) {
		if (trajectories.isEmpty())
			return this.getSelectedTrajectories().size();
		int index = this.getSelectedTrajectories().indexOf(trajectories.get(0));
		this.getSelectedTrajectories().removeAll(trajectories);
		if (index < 0) {
			index = 0;
		}
		return index;
	}

	private void setTrajectoriesValues() {
		// TODO can be delegate to the superclass
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
		this.tbTrajectoriesPanel.setGateway(gateway);
		if (this.img == null)
			return;
		final OmegaImagePixels pixels = this.img.getDefaultPixels();
		Double physicalSizeT;
		try {
			physicalSizeT = gateway.computeSizeT(pixels.getOmeroId(),
					pixels.getSizeT(), pixels.getSizeT());
		} catch (final Exception ex) {
			physicalSizeT = null;
			OmegaLogFileManager.handleCoreException(ex, false);
		}
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
			Double physicalSizeT;
			try {
				physicalSizeT = this.gateway.computeSizeT(pixels.getOmeroId(),
						pixels.getSizeT(), pixels.getSizeT());
			} catch (final Exception ex) {
				physicalSizeT = null;
				OmegaLogFileManager.handleCoreException(ex, false);
			}
			this.tbHeaderPanel.setPhysicalSizeT(physicalSizeT);
			this.tbLabelsPanel.setHasPhysicalSizeT(physicalSizeT != null);
		}
		this.tbTrajectoriesPanel.setImage(image);
	}

	@Override
	public void updateMessageStatus(final OmegaMessageEvent evt) {
		final OmegaMessageEventTBLoader specificEvt = (OmegaMessageEventTBLoader) evt;
		this.updateStatus(specificEvt.getMessage());
		if (specificEvt.isRepaint()) {
			this.tbTrajectoriesPanel.loadBufferedImages();
		}
	}

	protected Point getClickPosition() {
		return this.tbTrajectoriesPanel.getClickPosition();
	}

	protected JPopupMenu getMenu() {
		return this.tbMenu;
	}

	protected void sendEventTrajectories(
			final List<OmegaTrajectory> selectedTrajectories,
			final boolean selected) {
		this.tbContainer.sendEventTrajectories(selectedTrajectories, selected);
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
		if ((val == null) || val.equals("")) {
			this.getShownTrajectories().addAll(this.getTrajectories());
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
		for (final OmegaTrajectory track : this.getTrajectories()) {
			if (less) {
				if (track.getLength() < val) {
					this.getShownTrajectories().add(track);
					continue;
				}
			} else if (more) {
				if (track.getLength() > val) {
					this.getShownTrajectories().add(track);
					continue;
				}
			}
			if (equal)
				if (track.getLength() == val) {
					this.getShownTrajectories().add(track);
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
				}
			} else if (key.equals("Name")) {
				if (track.getName().matches(regex)) {
					this.getShownTrajectories().add(track);
				}
			}
	}
}
