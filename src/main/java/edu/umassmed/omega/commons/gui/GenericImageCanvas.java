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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import edu.umassmed.omega.commons.OmegaLogFileManager;
import edu.umassmed.omega.commons.constants.OmegaGUIConstants;
import edu.umassmed.omega.commons.constants.OmegaGenericConstants;
import edu.umassmed.omega.commons.constants.OmegaMathSymbolConstants;
import edu.umassmed.omega.commons.data.coreElements.OmegaImagePixels;
import edu.umassmed.omega.commons.data.imageDBConnectionElements.OmegaGateway;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegmentationTypes;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.gui.dialogs.GenericConfirmationDialog;
import edu.umassmed.omega.commons.gui.interfaces.GenericImageCanvasContainer;
import edu.umassmed.omega.commons.utilities.OmegaColorManagerUtilities;
import edu.umassmed.omega.commons.utilities.OmegaImageRenderingUtilities;
import edu.umassmed.omega.commons.utilities.OmegaImageUtilities;
import edu.umassmed.omega.commons.utilities.OmegaStringUtilities;

/**
 * Paints the image.
 */
public class GenericImageCanvas extends GenericScrollPane {
	private static final long serialVersionUID = -2321440745146284043L;

	private static int AXISSPACE = 40;
	/** The parent JPanel. **/
	// private JPanelViewer jPanelViewer = null;
	private final GenericImageCanvasContainer canvasContainer;

	private final GenericCanvas canvasPanel;

	private OmegaImagePixels pixels;
	private OmegaGateway gateway;

	/** The image to be displayed. **/
	private BufferedImage image;
	/** The image to be displayed (scaled). **/
	private final BufferedImage scaledImage;
	/** Image zoom factor. **/
	private double scale;

	/** Trajectories to be drawed (data exploration). **/
	private final List<OmegaROI> particles;
	/** Trajectories to be drawed, scaled. **/
	private final List<OmegaTrajectory> selectedTrajectories, trajectories;
	private final Map<OmegaTrajectory, List<OmegaSegment>> selectedSegments,
			segments;
	private OmegaSegmentationTypes segmTypes;

	private int selectedTrajectoryIndex, selectedSegmentIndex;

	/** Graphics2D stroke used. **/
	private int currentStroke;
	/** Current frame. **/
	private int currentT, currentZ;
	/** The radius of the pixel **/
	private int radius;

	private boolean isCompressed, showTrajectoriesOnlyUpToT,
			showTrajectoriesOnlyActive, showTrajectoriesOnlyStartingAtT;

	private JPopupMenu canvasMenu;
	private JMenuItem canvasZoomIn, canvasZoomOut;
	private JMenuItem generateRandomColors, chooseColor;

	private Point mousePosition;

	// private final Integer drawLineWidth;
	
	// final JPanelViewer jPanelViewer
	public GenericImageCanvas(final RootPaneContainer parent,
			final GenericImageCanvasContainer canvasContainer,
			final Integer drawLineWidth) {
		super(parent);

		this.canvasContainer = canvasContainer;
		
		this.pixels = null;
		this.gateway = null;

		this.image = null;
		this.scaledImage = null;

		this.radius = OmegaGUIConstants.DRAWING_POINTSIZE;
		this.currentT = 0;
		this.scale = 1.0;
		this.currentStroke = drawLineWidth;

		// this.jPanelViewer = jPanelViewer;

		this.particles = new ArrayList<OmegaROI>();
		this.selectedTrajectories = new ArrayList<OmegaTrajectory>();
		this.trajectories = new ArrayList<OmegaTrajectory>();
		this.selectedSegments = new LinkedHashMap<OmegaTrajectory, List<OmegaSegment>>();
		this.segments = new LinkedHashMap<OmegaTrajectory, List<OmegaSegment>>();
		this.selectedTrajectoryIndex = -1;

		this.mousePosition = null;

		this.canvasPanel = new GenericCanvas(this.getParentContainer(), this);

		this.setViewportView(this.canvasPanel);

		this.createPopupMenu();

		this.addListeners();
	}

	private void createPopupMenu() {
		this.canvasMenu = new JPopupMenu();

		this.canvasZoomIn = new JMenuItem(OmegaGUIConstants.ZOOM_IN);
		this.canvasMenu.add(this.canvasZoomIn);
		this.canvasZoomOut = new JMenuItem(OmegaGUIConstants.ZOOM_OUT);
		this.canvasMenu.add(this.canvasZoomOut);
		this.canvasMenu.add(new JSeparator());

		this.generateRandomColors = new JMenuItem(
				OmegaGUIConstants.RANDOM_COLORS);
		this.chooseColor = new JMenuItem(OmegaGUIConstants.CHOSE_COLOR);
		this.canvasMenu.add(this.generateRandomColors);
		// this.canvasMenu.add(new JSeparator());
	}

	private void addListeners() {
		this.canvasPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent evt) {
				final Point clickP = evt.getPoint();
				GenericImageCanvas.this.handleMouseClick(
						clickP,
						SwingUtilities.isRightMouseButton(evt)
								|| evt.isControlDown(), evt.isShiftDown());
			}

			@Override
			public void mousePressed(final MouseEvent evt) {
				GenericImageCanvas.this.handleMousePressed(evt.getPoint());
			}

			@Override
			public void mouseReleased(final MouseEvent evt) {
				GenericImageCanvas.this.handleMouseReleased(
						evt.getPoint(),
						SwingUtilities.isRightMouseButton(evt)
								|| evt.isControlDown());
			}
		});
		this.canvasPanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(final MouseEvent evt) {
				GenericImageCanvas.this.handleMouseDragged(
						evt.getPoint(),
						SwingUtilities.isRightMouseButton(evt)
								|| evt.isControlDown());
			}
		});
		this.canvasZoomOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				GenericImageCanvas.this.handleZoom(0.5);
			}
		});
		this.canvasZoomIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				GenericImageCanvas.this.handleZoom(2);
			}
		});
		this.generateRandomColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				GenericImageCanvas.this.handleGenerateRandomColors();
			}
		});
		this.chooseColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				GenericImageCanvas.this.handlePickSingleColor();
			}
		});
	}

	private void handleMousePressed(final Point pos) {
		this.mousePosition = pos;
	}

	private void handleMouseDragged(final Point pos, final boolean isRightButton) {
		if (isRightButton)
			return;
		this.computeScrollBarPositionOnDrag(this.mousePosition, pos);
	}

	private void handleMouseReleased(final Point pos,
			final boolean isRightButton) {
		if (isRightButton)
			return;
		this.computeScrollBarPositionOnDrag(this.mousePosition, pos);
	}

	private void computeScrollBarPositionOnDrag(final Point startP,
			final Point endP) {
		final int diffX = startP.x - endP.x;
		final int diffY = startP.y - endP.y;
		final int hVal = this.getHorizontalScrollBar().getValue() + diffX;
		final int vVal = this.getVerticalScrollBar().getValue() + diffY;
		this.getHorizontalScrollBar().setValue(hVal);
		// System.out.println(this.getVerticalScrollBar().getMaximum() + " VS "
		// + vVal);
		this.getVerticalScrollBar().setValue(vVal);
	}

	private void handleMouseClick(final Point clickP,
			final boolean isRightButton, final boolean isControlDown) {
		this.findTrajectoryIndex(clickP);
		this.canvasMenu.remove(this.chooseColor);
		final int index = this.selectedTrajectoryIndex;
		final int segmIndex = this.selectedSegmentIndex;
		OmegaTrajectory traj = null;
		if (index > -1) {
			traj = this.trajectories.get(index);
		}
		OmegaSegment segm = null;
		if (!this.segments.isEmpty() && (traj != null) && (segmIndex != -1)) {
			segm = this.segments.get(traj).get(segmIndex);
		}
		if (isRightButton) {
			this.selectedTrajectories.clear();
			this.selectedSegments.clear();
			if (traj != null) {
				this.canvasMenu.add(this.chooseColor,
						this.canvasMenu.getComponentCount());
			}
			this.canvasMenu.show(this.canvasPanel, clickP.x, clickP.y);
		} else {
			if (!isControlDown) {
				this.selectedTrajectories.clear();
				this.selectedSegments.clear();
				this.sendCoreEventTrajectories(this.selectedTrajectories, true);
				this.sendCoreEventSegments(this.selectedSegments,
						this.segmTypes, true);
				this.repaint();
			}
			this.canvasMenu.setVisible(false);
		}
		if (traj != null) {
			if (segm != null) {
				List<OmegaSegment> segmentList = null;
				if (this.selectedSegments.containsKey(traj)) {
					segmentList = this.selectedSegments.get(traj);
				} else {
					segmentList = new ArrayList<OmegaSegment>();
				}
				segmentList.add(segm);
				this.selectedTrajectories.add(traj);
				this.sendCoreEventTrajectories(this.selectedTrajectories, true);
				this.selectedSegments.put(traj, segmentList);
				this.sendCoreEventSegments(this.selectedSegments,
						this.segmTypes, true);
			} else {
				this.selectedTrajectories.add(traj);
				this.sendCoreEventTrajectories(this.selectedTrajectories, true);
			}
			this.repaint();
		}
	}

	private void handleZoom(final double modifier) {
		this.scale *= modifier;
		this.rescale();
		int newPosX = (int) (this.mousePosition.x * modifier);
		int newPosY = (int) (this.mousePosition.y * modifier);
		final int width = this.getWidth() / 2;
		final int height = this.getHeight() / 2;
		newPosX -= width;
		newPosY -= height;
		this.getHorizontalScrollBar().setValue(newPosX);
		this.getVerticalScrollBar().setValue(newPosY);
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
				.generateRandomColors(this.trajectories.size());
		for (int i = 0; i < this.trajectories.size(); i++) {
			final OmegaTrajectory traj = this.trajectories.get(i);
			final Color c = colors.get(i);
			traj.setColor(c);
			traj.setColorChanged(true);
		}
		this.repaint();
		this.sendCoreEventTrajectories(this.trajectories, false);
	}

	private void handlePickSingleColor() {
		final OmegaTrajectory traj = this.trajectories
				.get(this.selectedTrajectoryIndex);
		final StringBuffer buf1 = new StringBuffer();
		buf1.append(OmegaGUIConstants.TRACK_CHOSE_COLOR_DIALOG_MSG);
		buf1.append(traj.getName());

		final Color c = OmegaColorManagerUtilities.openPaletteColor(this,
				buf1.toString(), traj.getColor());

		final StringBuffer buf2 = new StringBuffer();
		buf2.append(OmegaGUIConstants.TRACK_CHOSE_COLOR_CONFIRM_MSG);
		buf2.append(traj.getName());
		buf2.append("?");

		final GenericConfirmationDialog dialog = new GenericConfirmationDialog(
				this.getParentContainer(),
				OmegaGUIConstants.TRACK_CHOSE_COLOR_CONFIRM, buf2.toString(),
				true);
		dialog.setVisible(true);
		if (!dialog.getConfirmation())
			return;

		traj.setColor(c);
		traj.setColorChanged(true);
		this.repaint();
		final List<OmegaTrajectory> trajectories = new ArrayList<OmegaTrajectory>();
		trajectories.addAll(this.trajectories);
		this.sendCoreEventTrajectories(trajectories, false);
	}

	private void sendCoreEventTrajectories(
			final List<OmegaTrajectory> trajectories, final boolean selection) {
		this.canvasContainer.sendCoreEventTrajectories(trajectories, selection);
	}

	private void sendCoreEventSegments(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final OmegaSegmentationTypes segmTypes, final boolean selection) {
		this.canvasContainer.sendCoreEventSegments(segments, segmTypes,
				selection);
	}

	private void findTrajectoryIndex(final Point clickP) {
		final int x = (int) (clickP.x / this.scale);
		final int y = (int) (clickP.y / this.scale);
		int trajIndex = -1;
		int segmIndex = -1;
		for (final OmegaTrajectory traj : GenericImageCanvas.this.trajectories) {
			final List<OmegaROI> rois = traj.getROIs();
			for (final OmegaROI roi : rois) {
				final int roiX = (int) roi.getX();
				final int roiY = (int) roi.getY();
				if ((x < (roiX + 2)) && (x > (roiX - 2)) && (y < (roiY + 2))
						&& (y > (roiY - 2))) {
					trajIndex = GenericImageCanvas.this.trajectories
							.indexOf(traj);
					final int roiIndex = roi.getFrameIndex();
					if ((this.segments == null) || this.segments.isEmpty()) {
						continue;
					}
					for (final OmegaSegment segm : this.segments.get(traj)) {
						final int startIndex = segm.getStartingROI()
								.getFrameIndex();
						final int endIndex = segm.getEndingROI()
								.getFrameIndex();
						if ((roiIndex >= startIndex) && (roiIndex <= endIndex)) {
							segmIndex = this.segments.get(traj).indexOf(segm);
						}
						if (segmIndex != -1) {
							break;
						}
					}
					break;
				}
			}
			if (trajIndex != -1) {
				break;
			}
		}

		this.selectedTrajectoryIndex = trajIndex;
		this.selectedSegmentIndex = segmIndex;
	}

	/**
	 * Set the bufferedImage to render
	 *
	 * @param image
	 */
	public void setImage(final BufferedImage image) {
		this.image = image;
		Dimension dim;
		if (this.pixels == null) {
			dim = new Dimension(this.image.getWidth(), this.image.getHeight());
		} else {
			final int scaledWidth = (int) (image.getWidth() * this.scale);
			final int scaledHeight = (int) (image.getHeight() * this.scale);
			dim = new Dimension(scaledWidth/*
											 * + GenericImageCanvas.AXISSPACE +
											 * 5
											 */, scaledHeight /*
															 * +
															 * GenericImageCanvas
															 * .AXISSPACE + 5
															 */);
		}
		this.canvasPanel.setPreferredSize(dim);
		this.canvasPanel.setSize(dim);
		this.revalidate();
		this.repaint();
	}

	/**
	 * Set the size of the this JPanel accordingly to the image size and
	 * revalidates the panel.
	 */
	public void rescale() {
		final int width = (int) (this.image.getWidth() * this.scale);
		final int height = (int) (this.image.getHeight() * this.scale);
		if (this.pixels != null) {
			// width += GenericImageCanvas.AXISSPACE + 5;
			// height += GenericImageCanvas.AXISSPACE + 5;
		}
		final Dimension newDimension = new Dimension(width, height);
		this.canvasPanel.setPreferredSize(newDimension);
		this.canvasPanel.setSize(newDimension);
		this.revalidate();
		this.repaint();
	}

	private BufferedImage imageZoom(final BufferedImage image) {
		BufferedImage scaledImage = null;
		try {
			final AffineTransform tx = new AffineTransform();
			tx.scale(this.scale, this.scale);
			final AffineTransformOp op = new AffineTransformOp(tx,
					AffineTransformOp.TYPE_BILINEAR);
			scaledImage = op.filter(image, null);
		} catch (final OutOfMemoryError ex) {
			OmegaLogFileManager.handleCoreException(ex, true);
		}
		return scaledImage;
	}

	public double getScale() {
		return this.scale;
	}

	public void setScale(final double scale) {
		this.scale = scale;
	}

	public void setSegments(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		this.segments.clear();
		if (segments != null) {
			this.setTrajectories(new ArrayList<OmegaTrajectory>(segments
					.keySet()));
			this.segments.putAll(segments);
		} else {
			this.setTrajectories(null);
		}

		// this.actualModifiedTrajectories.clear();
		// if (segmentsMap != null) {
		// this.actualModifiedTrajectories.addAll(trajectories);
		// }
		this.revalidate();
		this.repaint();
	}

	public void setTrajectories(final List<OmegaTrajectory> trajectories) {
		this.trajectories.clear();
		if (trajectories != null) {
			this.trajectories.addAll(trajectories);
		}
		this.revalidate();
		this.repaint();
	}

	public void setParticles(final List<OmegaROI> particles) {
		this.particles.clear();
		if (particles != null) {
			this.particles.addAll(particles);
		}
		this.revalidate();
		this.repaint();
	}

	public void setRadius(final int radius) {
		this.radius = radius;
		this.revalidate();
		this.repaint();
	}

	public void clearSelections() {
		this.selectedTrajectories.clear();
		this.selectedSegments.clear();
	}

	public void updateTrajectories(final List<OmegaTrajectory> trajectories,
			final boolean selection) {
		// TODO check if this can be done smarter, maybe just repainting
		// trajectories instead of everything
		if (selection) {
			this.selectedTrajectories.clear();
			this.selectedSegments.clear();
			if (trajectories != null) {
				this.selectedTrajectories.addAll(trajectories);
				if (!trajectories.isEmpty()) {
					this.centerOnTrajectory(trajectories.get(0));
				}
			}
		} else {
			this.setTrajectories(trajectories);
		}
		this.revalidate();
		this.repaint();
	}

	public void updateSegments(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final OmegaSegmentationTypes segmTypes, final boolean selection) {
		// TODO check if this can be done smarter, maybe just repainting
		// trajectories instead of everything
		this.segmTypes = segmTypes;
		if (selection) {
			this.selectedTrajectories.clear();
			this.selectedSegments.clear();
			if ((segments != null) && !segments.isEmpty()) {
				this.selectedTrajectories.addAll(segments.keySet());
				this.selectedSegments.putAll(segments);
				final OmegaTrajectory track = this.selectedTrajectories.get(0);
				this.centerOnSegment(segments.get(track).get(0));
			}
		} else {
			this.setSegments(segments);
		}
		this.revalidate();
		this.repaint();
	}

	private void centerOnTrajectory(final OmegaTrajectory traj) {
		final double xD = traj.getROIs().get(0).getX() * this.scale;
		final double yD = traj.getROIs().get(0).getY() * this.scale;
		final int x = new BigDecimal(String.valueOf(xD)).setScale(2,
				RoundingMode.HALF_UP).intValue();
		final int y = new BigDecimal(String.valueOf(yD)).setScale(2,
				RoundingMode.HALF_UP).intValue();
		final int xPos = x - (this.getWidth() / 2);
		final int yPos = y - (this.getHeight() / 2);
		this.getVerticalScrollBar().setValue(yPos);
		this.getHorizontalScrollBar().setValue(xPos);
	}

	private void centerOnSegment(final OmegaSegment segment) {
		final double xD = segment.getStartingROI().getX() * this.scale;
		final double yD = segment.getEndingROI().getY() * this.scale;
		final int x = new BigDecimal(String.valueOf(xD)).setScale(2,
				RoundingMode.HALF_UP).intValue();
		final int y = new BigDecimal(String.valueOf(yD)).setScale(2,
				RoundingMode.HALF_UP).intValue();
		final int xPos = x - (this.getWidth() / 2);
		final int yPos = y - (this.getHeight() / 2);
		this.getVerticalScrollBar().setValue(yPos);
		this.getHorizontalScrollBar().setValue(xPos);
	}

	public void resetOverlays() {
		this.particles.clear();
		this.selectedTrajectories.clear();
		this.trajectories.clear();
		this.selectedSegments.clear();
		this.segments.clear();
		this.revalidate();
		this.repaint();
	}

	public void setPixels(final OmegaImagePixels pixels) {
		this.pixels = pixels;
	}

	public void render() {
		if (this.pixels == null) {
			this.renderNoImage();
		} else {
			this.renderImage();
		}
	}

	private void renderNoImage() {
		final BufferedImage img = OmegaImageUtilities.getNoImagePlaceholder();
		this.setImage(img);
	}

	/** Renders a plane. */
	private void renderImage() {
		try {
			final Long id = this.pixels.getOmeroId();
			// now render the pixels, possible to render it compressed or not
			// compressed
			BufferedImage img = null;
			final int sizeX = this.pixels.getSizeX();
			final int sizeY = this.pixels.getSizeY();
			if (this.isCompressed) {
				final int[] buf = this.gateway.renderAsPackedInt(id,
						this.currentT, this.currentZ);
				img = OmegaImageRenderingUtilities.createImage(buf, 32, sizeX,
						sizeY);
			} else {
				final byte[] values = this.gateway.renderCompressed(id,
						this.currentT, this.currentZ);
				final ByteArrayInputStream stream = new ByteArrayInputStream(
						values);
				img = ImageIO.read(stream);
				img.setAccelerationPriority(1f);
			}
			this.setImage(img);
		} catch (final Exception ex) {
			this.renderNoImage();
			OmegaLogFileManager.handleCoreException(ex, false);
		}
	}

	public void setGateway(final OmegaGateway gateway) {
		this.gateway = gateway;
	}

	class GenericCanvas extends GenericPanel {
		private static final long serialVersionUID = -511232055410051604L;
		private final GenericImageCanvas imagePanel;

		public GenericCanvas(final RootPaneContainer parent,
				final GenericImageCanvas imagePanel) {
			super(parent);
			this.imagePanel = imagePanel;
			this.setDoubleBuffered(true);
		}

		/**
		 * Overridden to paint the image.
		 */
		@Override
		public void paint(final Graphics g) {
			// this.paintOld(g);
			this.paintNew(g);
		}

		public void paintNew(final Graphics g) {
			if (this.imagePanel.image == null)
				return;
			BufferedImage scaledImage = null;
			if (this.imagePanel.scale != 1.0) {
				scaledImage = this.imagePanel.imageZoom(this.imagePanel.image);
				if (scaledImage == null) {
					// TODO WARNING ERROR!
					this.imagePanel.scale *= 0.5;
					scaledImage = this.imagePanel
							.imageZoom(this.imagePanel.image);
				}
			} else {
				scaledImage = this.imagePanel.image;
			}

			final Graphics2D g2D = (Graphics2D) g;
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2D.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			final int width = scaledImage.getWidth();
			final int height = scaledImage.getHeight();
			final int scaledWidth = width;// (int) (width * this.scale);
			final int scaledHeight = height;// (int) (height * this.scale);
			g2D.clearRect(0, 0, this.getWidth(), this.getHeight());
			int startX = 0;
			int startY = 0;
			if (this.imagePanel.pixels == null) {
				startX = (this.getWidth() - width) / 2;
				startY = (this.getHeight() - height) / 2;
			}
			g2D.drawImage(scaledImage, null, startX, startY);
			if (this.imagePanel.pixels == null)
				return;
			// this.drawInformations(g2D, scaledWidth, scaledHeight);
			if (!this.imagePanel.particles.isEmpty()) {
				this.drawParticles(g2D);
			}
			if (!this.imagePanel.segments.isEmpty()) {
				this.drawSegmentsNew(g2D, scaledWidth, scaledHeight);
			} else if (!this.imagePanel.trajectories.isEmpty()) {
				this.drawTrajectories(g2D, scaledWidth, scaledHeight);
			}

		}
		
		private void drawSegmentsNew(final Graphics2D g2D, final int width,
				final int height) {
			final List<OmegaTrajectory> selectedTrajectories = this.imagePanel.selectedTrajectories;
			final Map<OmegaTrajectory, List<OmegaSegment>> allSegments = this.imagePanel.segments;
			final Map<OmegaTrajectory, List<OmegaSegment>> selectedSegments = this.imagePanel.selectedSegments;
			final MathContext mc = new MathContext(4, RoundingMode.HALF_UP);
			final double adjusterD = this.imagePanel.scale / 2;
			final int adjuster = new BigDecimal(adjusterD, mc).intValue();
			// set the stroke
			g2D.setStroke(new BasicStroke(this.imagePanel.currentStroke));
			for (final OmegaTrajectory trajectory : allSegments.keySet()) {
				if (!trajectory.isVisible()) {
					continue;
				}
				final List<OmegaROI> rois = trajectory.getROIs();
				final List<OmegaSegment> segments = this.imagePanel.segments
						.get(trajectory);
				final OmegaROI firstROI = rois.get(0);
				final OmegaROI lastROI = rois.get(rois.size() - 1);
				if (GenericImageCanvas.this.showTrajectoriesOnlyStartingAtT
						&& (firstROI.getFrameIndex() != GenericImageCanvas.this.currentT)) {
					continue;
				}
				if (GenericImageCanvas.this.showTrajectoriesOnlyActive) {
					if ((lastROI.getFrameIndex() < GenericImageCanvas.this.currentT)
							|| (firstROI.getFrameIndex() > GenericImageCanvas.this.currentT)) {
						continue;
					}
				}
				final Map<OmegaSegment, Integer> segmMinX = new LinkedHashMap<OmegaSegment, Integer>();
				final Map<OmegaSegment, Integer> segmMaxX = new LinkedHashMap<OmegaSegment, Integer>();
				final Map<OmegaSegment, Integer> segmMinY = new LinkedHashMap<OmegaSegment, Integer>();
				final Map<OmegaSegment, Integer> segmMaxY = new LinkedHashMap<OmegaSegment, Integer>();
				for (final OmegaSegment segment : segments) {
					segmMinX.put(segment, width);
					segmMaxX.put(segment, 0);
					segmMinY.put(segment, height);
					segmMaxY.put(segment, 0);
				}

				for (final OmegaROI roi : rois) {
					final int i = rois.indexOf(roi);
					if (i == (rois.size() - 1)) {
						continue;
					}
					final OmegaROI roi2 = rois.get(i + 1);
					if (GenericImageCanvas.this.showTrajectoriesOnlyUpToT) {
						if (GenericImageCanvas.this.currentT < roi2
								.getFrameIndex()) {
							break;
						}
					}
					Color col = trajectory.getColor();
					OmegaSegment segm = null;
					for (final OmegaSegment segment : segments) {
						if ((roi.getFrameIndex() >= segment.getStartingROI()
								.getFrameIndex())
								&& (roi.getFrameIndex() <= segment
										.getEndingROI().getFrameIndex())) {
							if (segment.isVisible()) {
								final Color tmpCol = GenericImageCanvas.this.segmTypes
										.getSegmentationColor(segment
												.getSegmentationType());
								segm = segment;
								if (tmpCol != OmegaSegmentationTypes.NOT_ASSIGNED_COL) {
									col = tmpCol;
								}
							}
						}
					}
					if (segm == null) {
						continue;
					}

					final double x1D = roi.getX() * this.imagePanel.scale;
					final double y1D = roi.getY() * this.imagePanel.scale;
					final double x2D = roi2.getX() * this.imagePanel.scale;
					final double y2D = roi2.getY() * this.imagePanel.scale;
					final int x1 = new BigDecimal(x1D, mc).intValue()
							+ adjuster;
					final int y1 = new BigDecimal(y1D, mc).intValue()
							+ adjuster;
					final int x2 = new BigDecimal(x2D, mc).intValue()
							+ adjuster;
					final int y2 = new BigDecimal(y2D, mc).intValue()
							+ adjuster;
					if (segm != null) {
						if (segmMinX.get(segm) > x1) {
							segmMinX.put(segm, x1);
						}
						if (segmMaxX.get(segm) < x1) {
							segmMaxX.put(segm, x1);
						}
						if (segmMinY.get(segm) > y1) {
							segmMinY.put(segm, y1);
						}
						if (segmMaxY.get(segm) < y1) {
							segmMaxY.put(segm, y1);
						}
					}

					if (col != trajectory.getColor()) {
						g2D.setStroke(new BasicStroke(
								this.imagePanel.currentStroke));
					} else {
						g2D.setStroke(new BasicStroke(
								this.imagePanel.currentStroke,
								BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
								10.0f, new float[] { 5f }, 10.0f));
					}

					g2D.setColor(col);
					g2D.drawLine(x1, y1, x2, y2);
				}
				for (final OmegaSegment segment : segments) {
					if (selectedSegments.containsKey(trajectory)
							|| selectedTrajectories.contains(trajectory)) {
						final List<OmegaSegment> tmpSegments;
						if (selectedSegments.containsKey(trajectory)) {
							tmpSegments = selectedSegments.get(trajectory);
						} else {
							tmpSegments = allSegments.get(trajectory);
						}
						if (tmpSegments.contains(segment)) {
							g2D.setColor(OmegaGenericConstants
									.getDefaultSelectionBackgroundColor());
							g2D.setStroke(new BasicStroke(
									this.imagePanel.currentStroke));
							final int minX = segmMinX.get(segment) - 3;
							final int maxX = segmMaxX.get(segment) + 3;
							final int minY = segmMinY.get(segment) - 3;
							final int maxY = segmMaxY.get(segment) + 3;
							final int rectWidth = (maxX - minX);
							final int rectHeight = (maxY - minY);
							g2D.drawRect(minX, minY, rectWidth, rectHeight);
						}
					}
				}
			}
		}

		private void drawTrajectories(final Graphics2D g2D, final int width,
				final int height) {
			final int currentT = GenericImageCanvas.this.currentT + 1;
			final MathContext mc = new MathContext(4, RoundingMode.HALF_UP);
			final double adjusterD = this.imagePanel.scale / 2;
			final int adjuster = new BigDecimal(adjusterD, mc).intValue();
			// set the stroke
			g2D.setStroke(new BasicStroke(this.imagePanel.currentStroke));
			// set the trajectory color (if we have one), or set random ones
			// draw trajectories
			// System.out.println("DRAW TRAJ");
			for (final OmegaTrajectory trajectory : this.imagePanel.trajectories) {
				if (!trajectory.isVisible()) {
					continue;
				}
				if (GenericImageCanvas.this.showTrajectoriesOnlyStartingAtT
						&& (trajectory.getROIs().get(0).getFrameIndex() != currentT)) {
					continue;
				}
				final List<OmegaROI> rois = trajectory.getROIs();
				final OmegaROI firstROI = rois.get(0);
				final OmegaROI lastROI = rois.get(rois.size() - 1);
				if (GenericImageCanvas.this.showTrajectoriesOnlyActive) {
					if ((lastROI.getFrameIndex() < currentT)
							|| (firstROI.getFrameIndex() > currentT)) {
						continue;
					}
				}
				int minX = width;
				int maxX = 0;
				int minY = height;
				int maxY = 0;
				for (int i = 0; i < (rois.size() - 1); i++) {
					final OmegaROI one = rois.get(i);
					final OmegaROI two = rois.get(i + 1);
					// System.out.println("T: " +
					// currentT
					// + " VS " + two.getFrameIndex());
					final double x1D = one.getX() * this.imagePanel.scale;
					final double y1D = one.getY() * this.imagePanel.scale;
					final double x2D = two.getX() * this.imagePanel.scale;
					final double y2D = two.getY() * this.imagePanel.scale;
					final int x1 = new BigDecimal(x1D, mc).intValue()
							+ adjuster;
					final int y1 = new BigDecimal(y1D, mc).intValue()
							+ adjuster;
					final int x2 = new BigDecimal(x2D, mc).intValue()
							+ adjuster;
					final int y2 = new BigDecimal(y2D, mc).intValue()
							+ adjuster;
					if (minX > x1) {
						minX = x1;
					}
					if (maxX < x1) {
						maxX = x1;
					}
					if (minY > y1) {
						minY = y1;
					}
					if (maxY < y1) {
						maxY = y1;
					}

					if (GenericImageCanvas.this.showTrajectoriesOnlyUpToT) {
						if (currentT < two.getFrameIndex()) {
							break;
						}
					}
					g2D.setColor(trajectory.getColor());
					g2D.drawLine(x1, y1, x2, y2);
				}
				if (this.imagePanel.selectedTrajectories.contains(trajectory)) {
					g2D.setColor(OmegaGenericConstants
							.getDefaultSelectionBackgroundColor());
					minX -= 3;
					maxX += 3;
					minY -= 3;
					maxY += 3;
					final int rectWidth = (maxX - minX);
					final int rectHeight = (maxY - minY);
					g2D.drawRect(minX, minY, rectWidth, rectHeight);
				}
			}
		}

		private void drawParticles(final Graphics2D g2D) {
			final MathContext mc = new MathContext(4, RoundingMode.HALF_UP);
			final double adjusterD = this.imagePanel.scale / 2;
			final int adjuster = new BigDecimal(adjusterD, mc).intValue();
			final double pointSizeD = this.imagePanel.radius
					* this.imagePanel.scale;
			final int pointSize = new BigDecimal(pointSizeD, mc).intValue();
			final double offsetD = pointSizeD / 2;
			final int offset = new BigDecimal(offsetD, mc).intValue();
			// set the stroke
			g2D.setStroke(new BasicStroke(this.imagePanel.currentStroke));
			// System.out.println("DRAW PARTICLES");
			for (final OmegaROI roi : this.imagePanel.particles) {
				// System.out.println("T: " + GenericImageCanvas.this.currentT
				// + " VS " + roi.getFrameIndex());
				g2D.setColor(Color.RED);
				final double xD = roi.getX() * this.imagePanel.scale;
				final double yD = roi.getY() * this.imagePanel.scale;
				final int roiX = new BigDecimal(xD, mc).intValue();
				final int roiY = new BigDecimal(yD, mc).intValue();
				final int x = (roiX - offset) + adjuster;
				final int y = (roiY - offset) + adjuster;
				g2D.drawOval(x, y, pointSize, pointSize);
			}
		}

		private void drawInformations(final Graphics2D g2D, final int width,
				final int height) {
			final int halfSpace = GenericImageCanvas.AXISSPACE / 2;
			final Double physicalSizeX = GenericImageCanvas.this.pixels
					.getPhysicalSizeX();
			final Double physicalSizeY = GenericImageCanvas.this.pixels
					.getPhysicalSizeY();
			final int imgWidth = this.imagePanel.image.getWidth();
			final int imgHeight = this.imagePanel.image.getHeight();
			g2D.setColor(Color.BLACK);
			// TODO to modify to use dynamic coord instead of fixed number
			// everywhere

			// X
			this.drawArrow(g2D, 0, height + halfSpace, width, height
					+ halfSpace);
			// Y
			g2D.drawLine(width + halfSpace, 0, width + halfSpace, 10);
			this.drawArrow(g2D, width + halfSpace, 30, width + halfSpace,
					height);

			// information on X pixels
			if ((physicalSizeX != null) && (physicalSizeX > 0.0)) {
				final String micron = OmegaStringUtilities.doubleToString(
						physicalSizeX * imgWidth, 2);
				g2D.drawString(micron, 0, height + 15);
				g2D.drawString(OmegaMathSymbolConstants.MU + "m", 0,
						height + 30);
			} else {
				g2D.drawString(String.valueOf(imgWidth), 0, height + 15);
				g2D.drawString("px", 0, height + 30);
			}

			// information on Y pixels
			if ((physicalSizeY != null) && (physicalSizeY > 0.0)) {
				final String micron = OmegaStringUtilities.doubleToString(
						physicalSizeY * imgHeight, 2);
				g2D.drawString(micron, width, 25);
				g2D.drawString(OmegaMathSymbolConstants.MU + "m", width, 35);
			} else {
				g2D.drawString(String.valueOf(imgHeight), width, 25);
				g2D.drawString("px", width, 35);
			}
		}

		private void drawArrow(final Graphics2D g2D, final int x1,
				final int y1, final int x2, final int y2) {
			final float arrowWidth = 6.0f;
			final float theta = 0.423f;
			final int[] xPoints = new int[3];
			final int[] yPoints = new int[3];
			final float[] vecLine = new float[2];
			final float[] vecLeft = new float[2];
			float fLength;
			float th;
			float ta;
			float baseX, baseY;

			xPoints[0] = x2;
			yPoints[0] = y2;

			// build the line vector
			vecLine[0] = (float) xPoints[0] - x1;
			vecLine[1] = (float) yPoints[0] - y1;

			// build the arrow base vector - normal to the line
			vecLeft[0] = -vecLine[1];
			vecLeft[1] = vecLine[0];

			// setup length parameters
			fLength = (float) Math.sqrt((vecLine[0] * vecLine[0])
					+ (vecLine[1] * vecLine[1]));
			th = arrowWidth / (2.0f * fLength);
			ta = arrowWidth
					/ (2.0f * ((float) Math.tan(theta) / 2.0f) * fLength);

			// find the base of the arrow
			baseX = (xPoints[0] - (ta * vecLine[0]));
			baseY = (yPoints[0] - (ta * vecLine[1]));

			// build the points on the sides of the arrow
			xPoints[1] = (int) (baseX + (th * vecLeft[0]));
			yPoints[1] = (int) (baseY + (th * vecLeft[1]));
			xPoints[2] = (int) (baseX - (th * vecLeft[0]));
			yPoints[2] = (int) (baseY - (th * vecLeft[1]));

			g2D.drawLine(x1, y1, (int) baseX, (int) baseY);
			g2D.fillPolygon(xPoints, yPoints, 3);
		}
	}

	public void setCompressed(final boolean compressed) {
		this.isCompressed = compressed;
		this.renderImage();
	}

	public void setActiveChannel(final int channel, final boolean isActive) {
		try {
			this.gateway.setActiveChannel(this.pixels.getOmeroId(), channel,
					isActive);
		} catch (final Exception ex) {
			OmegaLogFileManager.handleCoreException(ex, true);
		}
		this.pixels.setSelectedC(channel, isActive);
	}

	public void setTValues(final int t, final boolean isDefault) {
		if (isDefault) {
			try {
				this.gateway.setDefaultT(this.pixels.getOmeroId(), t);
			} catch (final Exception ex) {
				OmegaLogFileManager.handleCoreException(ex, true);
			}
		}
		this.currentT = t;
		this.pixels.setSelectedT(t);
	}

	public int getCurrentT() {
		return this.currentT;
	}

	public void setZValues(final int z, final boolean isDefault) {
		if (isDefault) {
			try {
				this.gateway.setDefaultZ(this.pixels.getOmeroId(), z);
			} catch (final Exception ex) {
				OmegaLogFileManager.handleCoreException(ex, true);
			}
		}
		this.currentZ = z;
		this.pixels.setSelectedZ(z);
	}

	public OmegaGateway getGateway() {
		return this.gateway;
	}

	public OmegaImagePixels getImagePixels() {
		return this.pixels;
	}

	public void computeAndSetScaleToFit() {
		Double scale = 1.0;
		if (this.pixels == null) {
			this.scale = scale;
			return;
		}
		final Dimension dim = this.getSize();
		double width = dim.width;
		double height = dim.height;
		width -= 8;
		height -= 8;
		// width -= GenericImageCanvas.AXISSPACE + 8;
		// height -= GenericImageCanvas.AXISSPACE + 8;

		final double imgWidth = this.pixels.getSizeX();
		final double imgHeight = this.pixels.getSizeY();

		final double scaleX = width / imgWidth;
		final double scaleY = height / imgHeight;

		if (scaleX <= scaleY) {
			scale = scaleX;
		} else {
			scale = scaleY;
		}
		this.scale = scale;
	}

	public void setShowTrajectoriesOnlyStartingAtT(final boolean enabled) {
		this.showTrajectoriesOnlyStartingAtT = enabled;
		this.revalidate();
		this.repaint();
	}

	public void setShowTrajectoriesOnlyUpToT(final boolean enabled) {
		this.showTrajectoriesOnlyUpToT = enabled;
		this.revalidate();
		this.repaint();
	}

	public void setShowTrajectoriesOnlyActive(final boolean enabled) {
		this.showTrajectoriesOnlyActive = enabled;
		this.revalidate();
		this.repaint();
	}
	
	public void setTrackLineSize(final int lineSize) {
		this.currentStroke = lineSize;
		this.repaint();
	}
}
