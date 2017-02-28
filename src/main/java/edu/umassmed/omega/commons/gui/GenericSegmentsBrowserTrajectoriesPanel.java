package edu.umassmed.omega.commons.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.RootPaneContainer;

import edu.umassmed.omega.commons.OmegaImageManager;
import edu.umassmed.omega.commons.OmegaLogFileManager;
import edu.umassmed.omega.commons.constants.OmegaConstants;
import edu.umassmed.omega.commons.data.coreElements.OmegaImage;
import edu.umassmed.omega.commons.data.imageDBConnectionElements.OmegaGateway;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegmentationTypes;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.eventSystem.events.OmegaMessageEventTBLoader;
import edu.umassmed.omega.commons.runnable.TBROIThumbnailLoader;

public class GenericSegmentsBrowserTrajectoriesPanel extends GenericPanel {
	
	private static final long serialVersionUID = 2744718217539154609L;
	
	private final GenericSegmentsBrowserPanel sbPanel;
	
	private final Map<Point, OmegaROI> particlesMap;
	private final Map<OmegaSegment, OmegaTrajectory> segmentsTracksMap;
	private final Map<OmegaTrajectory, List<OmegaSegment>> tracksSegmentsMap;
	private final Map<Integer, OmegaTrajectory> trajectoriesMap;
	private final Map<Integer, OmegaSegment> segmentsMap;
	
	private OmegaGateway gateway;
	private OmegaImage img;
	// private final List<BufferedImage> buffImages;
	
	private Thread frameLoaderThread;
	private TBROIThumbnailLoader frameLoader;
	
	private Point clickPosition;
	private final boolean invertTrackSegmColor;
	private final boolean isSelectionEnabled, isDoubleColorEnabled;
	
	public GenericSegmentsBrowserTrajectoriesPanel(
	        final RootPaneContainer parent,
	        final GenericSegmentsBrowserPanel sbPanel,
	        final OmegaGateway gateway, final boolean isSelectionEnabled) {
		super(parent);
		this.sbPanel = sbPanel;
		
		this.particlesMap = new LinkedHashMap<Point, OmegaROI>();
		this.trajectoriesMap = new LinkedHashMap<Integer, OmegaTrajectory>();
		
		this.segmentsTracksMap = new LinkedHashMap<OmegaSegment, OmegaTrajectory>();
		this.tracksSegmentsMap = new LinkedHashMap<OmegaTrajectory, List<OmegaSegment>>();
		this.segmentsMap = new LinkedHashMap<Integer, OmegaSegment>();
		
		this.gateway = gateway;
		this.img = null;
		// this.buffImages = new ArrayList<BufferedImage>();
		
		this.frameLoaderThread = null;
		this.frameLoader = null;
		
		this.clickPosition = null;
		
		this.isSelectionEnabled = isSelectionEnabled;
		this.isDoubleColorEnabled = true;
		this.invertTrackSegmColor = false;
	}
	
	protected Point findTrajectoryLocation(final OmegaTrajectory trajectory) {
		final OmegaROI startingROI = trajectory.getROIs().get(0);
		for (final Point p : this.particlesMap.keySet()) {
			final OmegaROI roi = this.particlesMap.get(p);
			if (roi.equals(startingROI))
				return p;
		}
		return null;
	}
	
	// TODO TO FIX this should just find segments
	protected void findSelectedSegment(final Point clickP) {
		final int adj1 = GenericBrowserPanel.SPOT_SPACE_DEFAULT / 4;
		final int adj2 = adj1 * 3;
		for (final Integer y : this.segmentsMap.keySet()) {
			if ((clickP.y > (y - adj1)) && (clickP.y < (y + adj2))) {
				this.sbPanel.setSelectedSegment(this.segmentsMap.get(y));
				// this.sbPanel.setSelectedTrajectory(this.segmentsTracksMap
				// .get(segm));
				// this.sbPanel.setSelectedTrajectory(this.trajectoriesMap.get(y));
				return;
			}
		}
	}
	
	// TODO TO FIX this should just find tracks
	protected void findSelectedTrajectory(final Point clickP) {
		final int adj1 = GenericBrowserPanel.SPOT_SPACE_DEFAULT / 4;
		final int adj2 = adj1 * 3;
		for (final Integer y : this.trajectoriesMap.keySet()) {
			if ((clickP.y > (y - adj1)) && (clickP.y < (y + adj2))) {
				this.sbPanel.setSelectedTrajectory(this.trajectoriesMap.get(y));
				// this.sbPanel.setSelectedTrajectory(this.trajectoriesMap.get(y));
				return;
			}
		}
	}
	
	protected void findSelectedParticle(final Point clickP) {
		final int size = GenericBrowserPanel.SPOT_SIZE_DEFAULT;
		final int border = GenericBrowserPanel.TRAJECTORY_SQUARE_BORDER;
		for (final Point p : this.particlesMap.keySet()) {
			final int px = p.x - border;
			final int py = p.y - border;
			final int pX = p.x + size + border;
			final int pY = p.y + size + border;
			if ((clickP.x > px) && (clickP.x < pX)) {
				if ((clickP.y > py) && (clickP.y < pY)) {
					this.clickPosition = clickP;
					this.sbPanel.setSelectedParticle(this.particlesMap.get(p));
					return;
				}
			}
		}
	}
	
	private void drawAlternateTimeframeBackground(final Graphics2D g2D) {
		final int space = GenericBrowserPanel.SPOT_SPACE_DEFAULT;
		final int border = GenericBrowserPanel.TRAJECTORY_SQUARE_BORDER;
		for (Integer x = 0; x < this.sbPanel.getSizeT(); x++) {
			Color bg;
			if ((x % 2) == 0) {
				bg = Color.gray;
			} else {
				bg = Color.white;
			}
			g2D.setBackground(bg);
			final int xPos = ((space * (x)) - border) + (space / 2);
			final int xAdj = xPos - (space / 2);
			final int yAdj = 0;
			final int width = space;
			final int height = this.getHeight();
			g2D.clearRect(xAdj, yAdj, width, height);
		}
		g2D.setBackground(Color.white);
	}
	
	private void drawSelectedTrajectoryBackground(final Graphics2D g2D) {
		for (int y = 0; y < this.sbPanel.getNumberOfSegments(); y++) {
			final OmegaTrajectory track = this.findTrack(y);
			final OmegaSegment segment = this.findSegment(y);
			final int yPos = GenericBrowserPanel.SPOT_SPACE_DEFAULT * y;
			final int adjY = yPos;
			final Map<OmegaTrajectory, List<OmegaSegment>> segments = this.sbPanel
					.getSelectedSegments();
			if (segments.containsKey(track)) {
				if (segments.get(track).contains(segment)) {
					g2D.setBackground(OmegaConstants
					        .getDefaultSelectionBackgroundColor());
					g2D.clearRect(0, adjY, this.getWidth(),
					        GenericBrowserPanel.SPOT_SPACE_DEFAULT);
					g2D.setBackground(Color.white);
				}
			}
			// if (this.sbPanel.getSelectedTrajectories().contains(traj)) {
			// g2D.setBackground(OmegaConstants
			// .getDefaultSelectionBackgroundColor());
			// g2D.clearRect(0, adjY, this.getWidth(),
			// GenericBrowserPanel.SPOT_SPACE_DEFAULT);
			// g2D.setBackground(Color.white);
			// }
		}
	}
	
	// TODO to be changed to draw segments
	private void drawTrajectories(final Graphics2D g2D) {
		final int space = GenericBrowserPanel.SPOT_SPACE_DEFAULT;
		final int size = GenericBrowserPanel.SPOT_SIZE_DEFAULT;
		final int border = GenericBrowserPanel.TRAJECTORY_SQUARE_BORDER;
		this.trajectoriesMap.clear();
		this.particlesMap.clear();
		this.segmentsMap.clear();
		this.segmentsTracksMap.clear();
		int y = 0;
		for (final OmegaTrajectory track : this.sbPanel.getShownSegments()
		        .keySet()) {
			final Color trackColor = track.getColor();
			for (final OmegaSegment segm : this.sbPanel.getShownSegments().get(
			        track)) {
				final List<OmegaROI> rois = track.getROIs();
				final int yPos = ((space * y) + (space / 4)) - border;
				if (!this.trajectoriesMap.containsKey(yPos)) {
					this.trajectoriesMap.put(yPos, track);
				}
				if (!this.segmentsMap.containsKey(yPos)) {
					this.segmentsMap.put(yPos, segm);
				}
				final int startROI = rois.indexOf(segm.getStartingROI());
				final int endROI = rois.indexOf(segm.getEndingROI());
				for (int x = startROI; x <= endROI; x++) {
					final OmegaROI roi = rois.get(x);
					final int roiIndex = roi.getFrameIndex() - 1;
					BufferedImage bufferedImage = null;
					List<BufferedImage> buffImages = null;
					if (this.img != null) {
						buffImages = OmegaImageManager.getImages(this.img
						        .getOmeroId());
					}
					// if (this.buffImages.size() > roiIndex) {
					// bufferedImage = this.buffImages.get(roiIndex);
					// }
					if ((buffImages != null) && (buffImages.size() > roiIndex)) {
						bufferedImage = buffImages.get(roiIndex);
					}
					final int xPos = ((space * roiIndex) - (size / 2) - border)
					        + (space / 2);
					final Point p = new Point(xPos, yPos);
					this.particlesMap.put(p, roi);
					if (this.sbPanel
					        .getSegmentColor(segm.getSegmentationType()) == null) {
						// TODO generate random colors
					}
					final Color segmColor = this.sbPanel.getSegmentColor(segm
					        .getSegmentationType());
					final int lastROIIndex = segm.getEndingROI()
							.getFrameIndex() - 1;
					if (roiIndex < lastROIIndex) {
						final OmegaROI nextROI = rois.get(x + 1);
						final int nextROIIndex = nextROI.getFrameIndex() - 1;
						final int xNextPos = ((space * nextROIIndex)
						        - (size / 2) - border)
						        + (space / 2);
						final Point p2 = new Point(xNextPos, yPos);
						this.drawConnection(g2D, p, p2, trackColor, segmColor);
					}
					if ((bufferedImage == null)
					        || !this.sbPanel.isShowParticles()) {
						if ((bufferedImage == null)
						        && this.sbPanel.isShowParticles()) {
							this.sbPanel.disableShowSportsThumbnail();
						}
						this.drawParticleSquare(g2D, p, trackColor, segmColor);
					} else {
						this.drawParticleImage(g2D, p, trackColor, segmColor,
								bufferedImage, roi);
					}
				}
				y++;
			}
		}
	}
	
	private void drawConnection(final Graphics2D g2D, final Point p,
	        final Point p2, final Color trackColor, final Color segmColor) {
		final int size = GenericBrowserPanel.SPOT_SIZE_DEFAULT;
		if (this.invertTrackSegmColor) {
			g2D.setColor(segmColor);
		} else {
			g2D.setColor(trackColor);
		}
		final int width = p2.x - p.x;
		final int height = 3;
		g2D.fillRect(p.x + size, (p.y + (size / 2)) - 3, width, height);
		g2D.setColor(Color.white);
	}
	
	private void drawParticleSquare(final Graphics2D g2D, final Point p,
	        final Color trackColor, final Color segmColor) {
		final int border = GenericBrowserPanel.TRAJECTORY_SQUARE_BORDER;
		final int spotSize = GenericBrowserPanel.SPOT_SIZE_DEFAULT
		        + (GenericBrowserPanel.TRAJECTORY_SQUARE_BORDER * 2);
		// if (this.isDoubleColorEnabled
		// && (segmColor != OmegaSegmentationTypes.NOT_ASSIGNED_COL)) {
		//
		// } else {
		// g2D.setColor(segmColor);
		// }
		if (this.invertTrackSegmColor) {
			g2D.setColor(segmColor);
		} else {
			g2D.setColor(trackColor);
		}
		g2D.fillRect(p.x - border, p.y - border, spotSize, spotSize);
		g2D.setColor(Color.black);
		if (this.isDoubleColorEnabled
		        && (segmColor != OmegaSegmentationTypes.NOT_ASSIGNED_COL)) {
			this.drawSegmentColorMarker(g2D, p, spotSize, border, trackColor,
			        segmColor);
		}
		g2D.setColor(Color.black);
	}
	
	private void drawParticleImage(final Graphics2D g2D, final Point p,
	        final Color trackColor, final Color segmColor,
			final BufferedImage bufferedImage, final OmegaROI roi) {
		final int radius = this.sbPanel.getRadius();
		final int border = GenericBrowserPanel.TRAJECTORY_SQUARE_BORDER;
		final int spotSize = GenericBrowserPanel.SPOT_SIZE_DEFAULT
		        + (GenericBrowserPanel.TRAJECTORY_SQUARE_BORDER * 2);
		final double xD = roi.getX();
		final double yD = roi.getY();
		final int x1 = new BigDecimal(String.valueOf(xD)).setScale(0,
		        BigDecimal.ROUND_HALF_UP).intValue();
		final int y1 = new BigDecimal(String.valueOf(yD)).setScale(0,
		        BigDecimal.ROUND_HALF_UP).intValue();
		if (this.invertTrackSegmColor) {
			g2D.setColor(segmColor);
		} else {
			g2D.setColor(trackColor);
		}
		g2D.fillRect(p.x - border, p.y - border, spotSize, spotSize);
		g2D.setColor(Color.black);
		final int xS1 = x1 - radius;
		final int yS1 = y1 - radius;
		final int xS2 = x1 + radius;
		final int yS2 = y1 + radius;
		g2D.drawImage(bufferedImage, p.x, p.y, p.x
		        + (GenericBrowserPanel.SPOT_SIZE_DEFAULT), p.y
		        + (GenericBrowserPanel.SPOT_SIZE_DEFAULT), xS1, yS1, xS2, yS2,
				this);
		if (this.isDoubleColorEnabled
		        && (segmColor != OmegaSegmentationTypes.NOT_ASSIGNED_COL)) {
			this.drawSegmentColorMarker(g2D, p, spotSize, border, trackColor,
			        segmColor);
		}
		g2D.setColor(Color.black);
	}

	private void drawSegmentColorMarker(final Graphics2D g2D, final Point p,
			final int spotSize, final int border, final Color trackColor,
			final Color segmColor) {
		final int x = p.x - border;
		final int y = p.y - border;
		final int newX = x + spotSize;
		final int newY = y + spotSize;
		if (!this.invertTrackSegmColor) {
			g2D.setColor(segmColor);
		} else {
			g2D.setColor(trackColor);
		}
		final int[] p1x = { x, x,
		        x + GenericBrowserPanel.TRAJECTORY_TRIANGLE_SIZE };
		final int[] p1y = { y,
		        y + GenericBrowserPanel.TRAJECTORY_TRIANGLE_SIZE, y };
		final Polygon triangle1 = new Polygon(p1x, p1y,
		        GenericBrowserPanel.TRAJECTORY_TRIANGLE_POINTS);
		g2D.fillPolygon(triangle1);
		final int[] p2x = { newX, newX,
		        newX - GenericBrowserPanel.TRAJECTORY_TRIANGLE_SIZE };
		final int[] p2y = { y,
		        y + GenericBrowserPanel.TRAJECTORY_TRIANGLE_SIZE, y };
		final Polygon triangle2 = new Polygon(p2x, p2y,
		        GenericBrowserPanel.TRAJECTORY_TRIANGLE_POINTS);
		g2D.fillPolygon(triangle2);
		final int[] p3x = { x, x,
		        x + GenericBrowserPanel.TRAJECTORY_TRIANGLE_SIZE };
		final int[] p3y = { newY,
				newY - GenericBrowserPanel.TRAJECTORY_TRIANGLE_SIZE, newY };
		final Polygon triangle3 = new Polygon(p3x, p3y,
		        GenericBrowserPanel.TRAJECTORY_TRIANGLE_POINTS);
		g2D.fillPolygon(triangle3);
		final int[] p4x = { newX, newX,
		        newX - GenericBrowserPanel.TRAJECTORY_TRIANGLE_SIZE };
		final int[] p4y = { newY,
				newY - GenericBrowserPanel.TRAJECTORY_TRIANGLE_SIZE, newY };
		final Polygon triangle4 = new Polygon(p4x, p4y,
		        GenericBrowserPanel.TRAJECTORY_TRIANGLE_POINTS);
		g2D.fillPolygon(triangle4);
	}
	
	@Override
	public void paint(final Graphics g) {
		if (this.sbPanel.getShownSegments() == null)
			return;
		// this.setPanelSize();
		
		final Graphics2D g2D = (Graphics2D) g;
		g2D.setBackground(Color.white);
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_RENDERING,
		        RenderingHints.VALUE_RENDER_QUALITY);
		g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2D.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		this.drawAlternateTimeframeBackground(g2D);
		if (this.isSelectionEnabled) {
			this.drawSelectedTrajectoryBackground(g2D);
		}
		
		this.drawTrajectories(g2D);
		
		this.sbPanel.addToPaint(g2D);
	}
	
	protected void setGateway(final OmegaGateway gateway) {
		this.gateway = gateway;
	}
	
	protected void setImage(final OmegaImage img) {
		if ((this.img == img))
			return;
		this.img = img;
		// this.buffImages.clear();
		if ((this.frameLoaderThread != null)
				&& this.frameLoaderThread.isAlive()) {
			this.frameLoader.kill();
			// try {
			// this.frameLoaderThread.join();
			// } catch (final InterruptedException ex) {
			// OmegaLogFileManager.handleCoreException(ex);
			// }
		}
		if ((img != null)
				&& (OmegaImageManager.getImages(img.getOmeroId()) != null)) {
			this.sbPanel.updateMessageStatus(new OmegaMessageEventTBLoader(
					"All frames loaded", false));
			this.frameLoader = null;
			return;
		}
		this.frameLoader = new TBROIThumbnailLoader(this.sbPanel, this.gateway,
		        this.img);
		this.frameLoaderThread = new Thread(this.frameLoader);
		this.frameLoaderThread.setName(this.frameLoader.getClass()
		        .getSimpleName());
		OmegaLogFileManager
		        .registerAsExceptionHandlerOnThread(this.frameLoaderThread);
		this.frameLoaderThread.start();
	}
	
	public void loadBufferedImages() {
		// TODO find a way to 'cache' images during the thread instead of here
		// this.buffImages.clear();
		if (this.frameLoader != null) {
			final List<BufferedImage> images = this.frameLoader.getImages();
			OmegaImageManager.clearImages();
			OmegaImageManager.saveImages(this.img.getOmeroId(), images);
		}
		// this.buffImages.addAll(this.frameLoader.getImages());
		this.repaint();
	}
	
	public void setPanelSize() {
		final int sizeT = this.sbPanel.getSizeT();
		final int numOfSegms = this.sbPanel.getNumberOfSegments();
		if ((sizeT < 1) || (numOfSegms < 1))
			return;
		int width = sizeT * GenericBrowserPanel.SPOT_SPACE_DEFAULT;
		int height = numOfSegms * GenericBrowserPanel.SPOT_SPACE_DEFAULT;
		final Dimension parentDim = this.sbPanel.getTrajectoriesPanelSize();
		if (parentDim.width > width) {
			width = parentDim.width;
		}
		if (parentDim.height > height) {
			height = parentDim.height;
		}
		final Dimension dim = new Dimension(width, height);
		this.setPreferredSize(dim);
		this.setSize(dim);
	}
	
	protected Point getClickPosition() {
		return this.clickPosition;
	}
	
	private OmegaTrajectory findTrack(int index) {
		for (final OmegaTrajectory track : this.sbPanel.getShownSegments()
		        .keySet()) {
			final List<OmegaSegment> segments = this.sbPanel.getShownSegments()
			        .get(track);
			if (index < segments.size())
				return track;
			index -= segments.size();
		}
		return null;
	}
	
	private OmegaSegment findSegment(int index) {
		for (final OmegaTrajectory track : this.sbPanel.getShownSegments()
		        .keySet()) {
			final List<OmegaSegment> segments = this.sbPanel.getShownSegments()
			        .get(track);
			if (index < segments.size())
				return segments.get(index);
			index -= segments.size();
		}
		return null;
	}
}
