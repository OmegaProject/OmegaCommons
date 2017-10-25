package edu.umassmed.omega.commons.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RootPaneContainer;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import edu.umassmed.omega.commons.constants.OmegaConstants;
import edu.umassmed.omega.commons.constants.OmegaGUIConstants;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParameter;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParticleDetectionRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParticleLinkingRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaSNRRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresDiffusivityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresIntensityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresMobilityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrackingMeasuresVelocityRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrajectoriesSegmentationRun;
import edu.umassmed.omega.commons.data.coreElements.OmegaImage;
import edu.umassmed.omega.commons.data.coreElements.OmegaPlane;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegmentationTypes;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.eventSystem.OmegaFilterEventListener;
import edu.umassmed.omega.commons.eventSystem.events.OmegaFilterEvent;
import edu.umassmed.omega.commons.utilities.OmegaTrajectoryUtilities;

public class GenericTrackingResultsPanel extends GenericScrollPane implements
		OmegaFilterEventListener {
	
	private static final long serialVersionUID = 1114253444374606565L;
	
	private OmegaAnalysisRun parentAnalysisRun;
	private OmegaAnalysisRun analysisRun;
	
	private GenericAnalysisInformationPanel infoPanel;
	private GenericFilterPanel filterPanel;
	
	private JTable table;
	private TableRowSorter<DefaultTableModel> rowSorter;
	
	private boolean isLocal, isSpecific;

	private final List<Integer> ints, doubles;

	private String z, c;

	private MyTableHeader tableHeader;
	
	// private JFXPanel fxPanel;
	// private GridPane gp;
	
	public GenericTrackingResultsPanel(final RootPaneContainer parent) {
		super(parent);

		this.createAndAddWidgets();

		this.addListeners();

		this.isLocal = true;
		this.isSpecific = false;
		this.parentAnalysisRun = null;

		this.ints = new ArrayList<Integer>();
		this.doubles = new ArrayList<Integer>();

		this.z = "NA";
		this.c = "NA";
	}
	
	private void createAndAddWidgets() {
		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		final JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());

		this.filterPanel = new GenericFilterPanel(this.getParentContainer());
		this.filterPanel.addOmegaFilterListener(this);
		centerPanel.add(this.filterPanel, BorderLayout.NORTH);

		// JPanel optionsPanel = new JPanel();
		// centerPanel.add(optionsPanel, BorderLayout.NORTH);

		// this.gp = new GridPane();
		// this.gp = new GridPane();
		// this.gp.setAlignment(Pos.TOP_LEFT);
		// this.gp.setPadding(new Insets(OmegaGUIConstants.PADDING));
		// this.gp.setHgap(OmegaGUIConstants.GAP);
		// this.gp.setVgap(OmegaGUIConstants.GAP);
		// Platform.runLater(new Runnable() {
		// @Override
		// public void run() {
		// GenericTrackingResultsPanel.this.initFX();
		// }
		// });
		final TableModel tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 4879341195916488038L;

			@Override
			public Class<?> getColumnClass(final int columnIndex) {
				return GenericTrackingResultsPanel.this
						.getColumnClass(columnIndex);
			}
		};
		this.table = new JTable(tableModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			};
		};
		this.tableHeader = new MyTableHeader(this.table.getColumnModel());
		this.table.setTableHeader(this.tableHeader);

		this.rowSorter = new TableRowSorter<DefaultTableModel>(
				(DefaultTableModel) this.table.getModel());
		this.table.setRowSorter(this.rowSorter);
		final JScrollPane sp = new JScrollPane(this.table);
		centerPanel.add(sp, BorderLayout.CENTER);

		mainPanel.add(centerPanel, BorderLayout.CENTER);

		this.infoPanel = new GenericAnalysisInformationPanel(
				this.getParentContainer());
		mainPanel.add(this.infoPanel, BorderLayout.WEST);

		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setViewportView(mainPanel);
	}

	private class MyTableHeader extends JTableHeader {
		
		private final List<String> tooltips;
		
		MyTableHeader(final TableColumnModel columnModel) {
			super(columnModel);// do everything a normal JTableHeader does
			this.tooltips = new ArrayList<String>();// plus extra data
		}
		
		public void addTooltip(final String s) {
			this.tooltips.add(s);
		}
		
		@Override
		public String getToolTipText(final MouseEvent e) {
			final java.awt.Point p = e.getPoint();
			final int index = this.columnModel.getColumnIndexAtX(p.x);
			final int realIndex = this.columnModel.getColumn(index)
					.getModelIndex();
			return this.tooltips.get(realIndex);
		}
		
		public void clear() {
			this.tooltips.clear();
		}
	}
	
	// private void initFX() {
	// this.fxPanel.setScene(new Scene(this.gp));
	// }
	
	private void addListeners() {
		
	}

	private Class<?> getColumnClass(final int columnIndex) {
		if (this.ints.contains(columnIndex))
			return Integer.class;
		else if (this.doubles.contains(columnIndex))
			return Double.class;
		else
			return String.class;
	}
	
	private void addParticleColumns(final boolean needIndex) {
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_PARTICLE_ID);
		this.addIntValueColumn(OmegaGUIConstants.RESULTS_FRAME);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_X);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_Y);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_Z);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_C);
		if (needIndex) {
			this.addIntValueColumn(OmegaGUIConstants.RESULTS_INDEX);
		}
	}
	
	private void addTrajectoryColumns() {
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_TRACK_ID);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_TRACK_NAME);
		this.addIntValueColumn(OmegaGUIConstants.RESULTS_TRACK_LENGTH);
	}

	private void addSegmentColumns() {
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_SEGM_ID);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_SEGM_NAME);
		this.addIntValueColumn(OmegaGUIConstants.RESULTS_SEGM_TYPE);
		this.addIntValueColumn(OmegaGUIConstants.RESULTS_SEGM_LENGTH);
	}
	
	private void addIntValueColumn(final String name) {
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		dtm.addColumn(name);
		final DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		final int index = this.table.getColumnModel().getColumnIndex(name);
		this.table.getColumnModel().getColumn(index)
				.setCellRenderer(rightRenderer);
		this.ints.add(index);
		this.tableHeader.addTooltip(name);
	}
	
	private void addDoubleValueColumn(final String name) {
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		dtm.addColumn(name);
		final DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		final int index = this.table.getColumnModel().getColumnIndex(name);
		this.table.getColumnModel().getColumn(index)
				.setCellRenderer(rightRenderer);
		this.doubles.add(index);
		this.tableHeader.addTooltip(name);
	}
	
	private void addStringValueColumn(final String name) {
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		dtm.addColumn(name);
		final DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		final int index = this.table.getColumnModel().getColumnIndex(name);
		this.table.getColumnModel().getColumn(index)
				.setCellRenderer(rightRenderer);
		this.tableHeader.addTooltip(name);
	}
	
	private void addParticleValuesColumns() {
		this.table.getModel();
		final OmegaParticleDetectionRun detectionRun = (OmegaParticleDetectionRun) this.analysisRun;
		final Map<OmegaROI, Map<String, Object>> particlesValues = detectionRun
				.getResultingParticlesValues();
		if (particlesValues.isEmpty())
			return;
		final OmegaROI roi = (OmegaROI) particlesValues.keySet().toArray()[0];
		final Map<String, Object> particleValues = particlesValues.get(roi);
		for (final String s : particleValues.keySet()) {
			// dtm.addColumn(s);
			// final int index = this.table.getColumnModel().getColumnIndex(s);
			final Object val = particleValues.get(s);
			if ((val instanceof Integer) || (val instanceof Long)) {
				// this.ints.add(index);
				this.addIntValueColumn(s);
			} else if ((val instanceof Float) || (val instanceof Double)) {
				// this.doubles.add(index);
				this.addDoubleValueColumn(s);
			}
		}
	}
	
	private void populateResultsPanel(final String c, final String z) {
		if (c != null) {
			this.c = c;
		}
		if (z != null) {
			this.z = z;
		}
		this.tableHeader.clear();
		if (this.analysisRun instanceof OmegaSNRRun) {
			final OmegaSNRRun snrRun = (OmegaSNRRun) this.analysisRun;
			if (this.isLocal) {
				this.populateLocalSNRResults(
						snrRun.getResultingLocalParticleArea(),
						snrRun.getResultingLocalCenterSignals(),
						snrRun.getResultingLocalPeakSignals(),
						snrRun.getResultingLocalMeanSignals(),
						snrRun.getResultingLocalNoises(),
						snrRun.getResultingLocalBackgrounds(),
						snrRun.getResultingLocalSNRs(),
						snrRun.getResultingLocalErrorIndexSNRs());
				this.rowSorter.toggleSortOrder(1);
			} else {
				if (this.isSpecific) {
					this.populateGlobalSpecificSNRResults(
							snrRun.getResultingImageAverageCenterSignal(),
							snrRun.getResultingImageAveragePeakSignal(),
							snrRun.getResultingImageAverageMeanSignal(),
							snrRun.getResultingImageBGR(),
							snrRun.getResultingImageNoise(),
							snrRun.getResultingImageMinimumSNR(),
							snrRun.getResultingImageAverageSNR(),
							snrRun.getResultingImageMaximumSNR(),
							snrRun.getResultingImageMinimumErrorIndexSNR(),
							snrRun.getResultingImageAverageErrorIndexSNR(),
							snrRun.getResultingImageMaximumErrorIndexSNR());
					this.rowSorter.toggleSortOrder(1);
				} else {
					this.populateGlobalGenericSNRResults(
							snrRun.getResultingAverageCenterSignal(),
							snrRun.getResultingAveragePeakSignal(),
							snrRun.getResultingAverageMeanSignal(),
							snrRun.getResultingBackground(),
							snrRun.getResultingNoise(),
							snrRun.getResultingMinSNR(),
							snrRun.getResultingAvgSNR(),
							snrRun.getResultingMaxSNR(),
							snrRun.getResultingMinErrorIndexSNR(),
							snrRun.getResultingAvgErrorIndexSNR(),
							snrRun.getResultingMaxErrorIndexSNR());
					this.rowSorter.toggleSortOrder(1);
				}
			}
		} else if (this.analysisRun instanceof OmegaParticleDetectionRun) {
			final OmegaParticleDetectionRun detRun = (OmegaParticleDetectionRun) this.analysisRun;
			this.populateParticlesResults(detRun.getResultingParticles(),
					detRun.getResultingParticlesValues());
			this.rowSorter.toggleSortOrder(1);
		} else if (this.analysisRun instanceof OmegaParticleLinkingRun) {
			final OmegaParticleLinkingRun linkRun = (OmegaParticleLinkingRun) this.analysisRun;
			this.populateTrajectoriesResults(linkRun.getResultingTrajectories());
			this.rowSorter.toggleSortOrder(4);
		} else if (this.analysisRun instanceof OmegaTrajectoriesSegmentationRun) {
			final OmegaTrajectoriesSegmentationRun segmRun = (OmegaTrajectoriesSegmentationRun) this.analysisRun;
			this.populateSegmentsResults(segmRun.getResultingSegments());
			this.rowSorter.toggleSortOrder(4);
		} else if (this.analysisRun instanceof OmegaTrackingMeasuresIntensityRun) {
			final OmegaTrackingMeasuresIntensityRun intRun = (OmegaTrackingMeasuresIntensityRun) this.analysisRun;
			if (this.isLocal) {
				this.populateLocalIntensitySegmentsResults(
						intRun.getSegments(),
						intRun.getPeakSignalsLocalResults(),
						intRun.getCentroidSignalsLocalResults(),
						intRun.getSNRRun() != null,
						intRun.getMeanSignalsLocalResults(),
						intRun.getBackgroundsLocalResults(),
						intRun.getNoisesLocalResults(),
						intRun.getSNRsLocalResults());
				this.rowSorter.toggleSortOrder(4);
			} else {
				this.populateGlobalIntensitySegmentsResults(
						intRun.getSegments(), intRun.getPeakSignalsResults(),
						intRun.getCentroidSignalsResults(),
						intRun.getSNRRun() != null,
						intRun.getMeanSignalsResults(),
						intRun.getBackgroundsResults(),
						intRun.getNoisesResults(), intRun.getSNRsResults());
				this.rowSorter.toggleSortOrder(1);
			}
		} else if (this.analysisRun instanceof OmegaTrackingMeasuresVelocityRun) {
			final OmegaTrackingMeasuresVelocityRun velRun = (OmegaTrackingMeasuresVelocityRun) this.analysisRun;
			if (this.isLocal) {
				this.populateLocalVelocitySegmentsResults(velRun.getSegments(),
						velRun.getLocalSpeedResults(),
						velRun.getLocalSpeedFromOriginResults(),
						velRun.getLocalVelocityFromOriginResults());
				this.rowSorter.toggleSortOrder(4);
			} else {
				this.populateGlobalVelocitySegmentsResults(
						velRun.getSegments(),
						velRun.getAverageCurvilinearSpeedMapResults(),
						velRun.getAverageStraightLineVelocityMapResults(),
						velRun.getForwardProgressionLinearityMapResults());
				this.rowSorter.toggleSortOrder(1);
			}
		} else if (this.analysisRun instanceof OmegaTrackingMeasuresMobilityRun) {
			final OmegaTrackingMeasuresMobilityRun mobRun = (OmegaTrackingMeasuresMobilityRun) this.analysisRun;
			if (this.isLocal) {
				this.populateLocalMobilitySegmentsResults(mobRun.getSegments(),
						mobRun.getDistancesResults(),
						mobRun.getDistancesFromOriginResults(),
						mobRun.getDisplacementsFromOriginResults(),
						mobRun.getConfinementRatioResults(),
						mobRun.getAnglesAndDirectionalChangesResults(),
						mobRun.getTimeTraveledResults());
				this.rowSorter.toggleSortOrder(4);
			} else {
				this.populateGlobalMobilitySegmentsResults(
						mobRun.getSegments(),
						mobRun.getMaxDisplacementsFromOriginResults(),
						mobRun.getDistancesFromOriginResults(),
						mobRun.getDisplacementsFromOriginResults(),
						mobRun.getConfinementRatioResults(),
						mobRun.getTimeTraveledResults());
				this.rowSorter.toggleSortOrder(1);
			}
		} else if (this.analysisRun instanceof OmegaTrackingMeasuresDiffusivityRun) {
			final OmegaTrackingMeasuresDiffusivityRun difRun = (OmegaTrackingMeasuresDiffusivityRun) this.analysisRun;
			final OmegaParameter param = difRun.getAlgorithmSpec()
					.getParameter(OmegaConstants.PARAMETER_DIFFUSIVITY_WINDOW);
			String LD = OmegaGUIConstants.NOT_ASSIGNED;
			if (param != null) {
				LD = param.getStringValue();
			}
			if (this.isLocal) {
				this.populateLocalDiffusivitySegmentsResults(
						difRun.getSegments(), LD, difRun.getNyResults(),
						difRun.getLogMuResults(), difRun.getMuResults(),
						difRun.getLogDeltaTResults(), difRun.getDeltaTResults());
			} else {
				if (this.isSpecific) {
					this.populateGlobalSpecificDiffusivitySegmentsResults(
							difRun.getSegments(), LD,
							difRun.getGammaDFromLogResults(),
							difRun.getSmssFromLogResults(),
							difRun.getErrosFromLogResults());
				} else {
					this.populateGlobalGenericDiffusivitySegmentsResults(
							difRun.getSegments(), LD, difRun.getNyResults(),
							difRun.getGammaDFromLogResults(),
							difRun.getGammaDResults());
				}
			}
		} else {
			// ERROR
		}
		// this.table.setRowSorter(this.rowSorter);
	}

	private void populateParticlesResults(
			final Map<OmegaPlane, List<OmegaROI>> particles,
			final Map<OmegaROI, Map<String, Object>> particlesValues) {
		this.addParticleColumns(false);
		this.addParticleValuesColumns();
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		for (final OmegaPlane frame : particles.keySet()) {
			final List<OmegaROI> rois = particles.get(frame);
			for (final OmegaROI roi : rois) {
				final Map<String, Object> particleValues = particlesValues
						.get(roi);
				final List<Object> row = new ArrayList<Object>();
				String id;
				if (roi.getElementID() == -1) {
					id = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					id = String.valueOf(roi.getElementID());
				}
				row.add(id);
				row.add(frame.getIndex());
				row.add(roi.getX());
				row.add(roi.getY());
				row.add(this.z);
				row.add(this.c);
				for (final String valName : particleValues.keySet()) {
					row.add(particleValues.get(valName));
				}
				dtm.addRow(row.toArray());
			}
		}
	}
	
	public void populateTrajectoriesResults(final List<OmegaTrajectory> tracks) {
		this.resetResultsPanel();
		this.addParticleColumns(false);
		this.addTrajectoryColumns();
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		for (final OmegaTrajectory track : tracks) {
			final String trackName = track.getName();
			final int trackLen = track.getLength();
			final List<OmegaROI> rois = track.getROIs();
			for (final OmegaROI roi : rois) {
				String roiID;
				if (roi.getElementID() == -1) {
					roiID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					roiID = String.valueOf(roi.getElementID());
				}
				String trackID;
				if (track.getElementID() == -1) {
					trackID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					trackID = String.valueOf(track.getElementID());
				}
				final Object[] row = { roiID, roi.getFrameIndex(), roi.getX(),
						roi.getY(), this.z, this.c, trackID, trackName,
						trackLen };
				dtm.addRow(row);
			}
		}
	}
	
	public void populateSegmentsResults(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		this.resetResultsPanel();
		this.addParticleColumns(false);
		this.addTrajectoryColumns();
		this.addSegmentColumns();
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		OmegaSegmentationTypes types = null;
		if (this.analysisRun != null) {
			types = ((OmegaTrajectoriesSegmentationRun) this.analysisRun)
					.getSegmentationTypes();
		}
		for (final OmegaTrajectory track : segments.keySet()) {
			final List<OmegaSegment> segmentList = segments.get(track);
			final String trackName = track.getName();
			final int trackLen = track.getLength();
			final List<OmegaROI> rois = track.getROIs();
			String trackID;
			if (track.getElementID() == -1) {
				trackID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				trackID = String.valueOf(track.getElementID());
			}
			for (final OmegaSegment segment : segmentList) {
				String segmID;
				if (segment.getElementID() == -1) {
					segmID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					segmID = String.valueOf(segment.getElementID());
				}
				final int start = segment.getStartingROI().getFrameIndex();
				final int end = segment.getEndingROI().getFrameIndex();
				for (final OmegaROI roi : rois) {
					String roiID;
					if (roi.getElementID() == -1) {
						roiID = OmegaGUIConstants.NOT_ASSIGNED;
					} else {
						roiID = String.valueOf(roi.getElementID());
					}
					final int index = roi.getFrameIndex();
					if ((index >= start) && (index <= end)) {
						final String segmName = segment.getName();
						final String segmType = types
								.getSegmentationName(segment
										.getSegmentationType());
						final int segmLen = OmegaTrajectoryUtilities
								.computeSegmentLength(track, segment);
						final Object[] row = { roiID, roi.getFrameIndex(),
								roi.getX(), roi.getY(), this.z, this.c,
								trackID, trackName, trackLen, segmID, segmName,
								segmType, segmLen };
						dtm.addRow(row);
					}
				}
			}
		}
	}

	private void populateLocalSNRResults(
			final Map<OmegaROI, Integer> particleArea,
			final Map<OmegaROI, Integer> centerSignal,
			final Map<OmegaROI, Integer> peakSignal,
			final Map<OmegaROI, Double> meanSignal,
			final Map<OmegaROI, Double> noise,
			final Map<OmegaROI, Double> background,
			final Map<OmegaROI, Double> snr,
			final Map<OmegaROI, Double> indexSNR) {
		this.addParticleColumns(false);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_PEAK);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_MEAN);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_NOISE);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_SNR);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_SNR_AREA);
		// this.addDoubleValueColumn("Index SNR");
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		for (final OmegaROI roi : particleArea.keySet()) {
			String roiID;
			if (roi.getElementID() == -1) {
				roiID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				roiID = String.valueOf(roi.getElementID());
			}
			final int index = roi.getFrameIndex();
			final Integer area = particleArea.get(roi);
			final Integer center = centerSignal.get(roi);
			final Integer peak = peakSignal.get(roi);
			final Double mean = meanSignal.get(roi);
			final Double localNoise = noise.get(roi);
			final Double localBackground = background.get(roi);
			final Double localSNR = snr.get(roi);
			// final Double localIndexSNR = indexSNR.get(roi);
			final Object[] row = { roiID, index, roi.getX(), roi.getY(),
					this.z, this.c, center, peak, mean, localBackground,
					localNoise, localSNR, area };
			// localIndexSNR
			dtm.addRow(row);
		}
	}
	
	private void populateGlobalSpecificSNRResults(
			final Map<OmegaPlane, Double> avgCenterSignal,
			final Map<OmegaPlane, Double> avgPeakSignal,
			final Map<OmegaPlane, Double> avgMeanSignal,
			final Map<OmegaPlane, Double> bgr,
			final Map<OmegaPlane, Double> noise,
			final Map<OmegaPlane, Double> minSNR,
			final Map<OmegaPlane, Double> avgSNR,
			final Map<OmegaPlane, Double> maxSNR,
			final Map<OmegaPlane, Double> minIndexSNR,
			final Map<OmegaPlane, Double> avgIndexSNR,
			final Map<OmegaPlane, Double> maxIndexSNR) {
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_PLANE_ID);
		this.addIntValueColumn(OmegaGUIConstants.RESULTS_PLANE_INDEX);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID_AVG);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_PEAK_AVG);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_MEAN_AVG);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_SNR_BACKGROUND);
		this.addIntValueColumn(OmegaGUIConstants.RESULTS_SNR_NOISE);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_SNR_SNR_AVG);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_SNR_SNR_MIN);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_SNR_SNR_MAX);
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		for (final OmegaPlane plane : bgr.keySet()) {
			String planeID;
			if (plane.getElementID() == -1) {
				planeID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				planeID = String.valueOf(plane.getElementID());
			}
			final Double localAvgCenterSignal = avgCenterSignal.get(plane);
			final Double localAvgPeakSignal = avgPeakSignal.get(plane);
			final Double localAvgMeanSignal = avgMeanSignal.get(plane);
			final Double localBGR = bgr.get(plane);
			final Double localNoise = noise.get(plane);
			final Double localMinSNR = minSNR.get(plane);
			final Double localAvgSNR = avgSNR.get(plane);
			final Double localMaxSNR = maxSNR.get(plane);
			final Object[] row = { planeID, plane.getIndex() + 1,
					localAvgCenterSignal, localAvgPeakSignal,
					localAvgMeanSignal, localBGR, localNoise, localAvgSNR,
					localMinSNR, localMaxSNR };
			dtm.addRow(row);
		}
	}
	
	private void populateGlobalGenericSNRResults(final Double avgCenterSignal,
			final Double avgPeakSignal, final Double avgMeanSignal,
			final Double bgr, final Double noise, final Double minSNR,
			final Double avgSNR, final Double maxSNR, final Double minIndexSNR,
			final Double avgIndexSNR, final Double maxIndexSNR) {
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_IMAGE_ID);
		// this.addDoubleValueColumn("T");
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_Z);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_C);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID_AVG);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_PEAK_AVG);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_MEAN_AVG);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_SNR_BACKGROUND);
		this.addIntValueColumn(OmegaGUIConstants.RESULTS_SNR_NOISE);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_SNR_SNR_AVG);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_SNR_SNR_MIN);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_SNR_SNR_MAX);
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		final OmegaParticleDetectionRun detRun = (OmegaParticleDetectionRun) this.parentAnalysisRun;
		OmegaImage img = null;
		for (final OmegaPlane p : detRun.getResultingParticles().keySet()) {
			img = p.getParentPixels().getParentImage();
			break;
		}
		if (img == null)
			// TODO error
			return;
		String imageID;
		if (img.getElementID() == -1) {
			imageID = OmegaGUIConstants.NOT_ASSIGNED;
		} else {
			imageID = String.valueOf(img.getElementID());
		}
		final Object[] row = { imageID, this.z, this.c, avgCenterSignal,
				avgPeakSignal, avgMeanSignal, bgr, noise, avgSNR, minSNR,
				maxSNR };
		dtm.addRow(row);
	}
	
	private void populateLocalIntensitySegmentsResults(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaROI, Double> peakSignals,
			final Map<OmegaROI, Double> centroidSignals, final boolean hasSNR,
			final Map<OmegaROI, Double> meanSignals,
			final Map<OmegaROI, Double> backgrounds,
			final Map<OmegaROI, Double> noises, final Map<OmegaROI, Double> snrs) {
		this.addTrajectoryColumns();
		this.addSegmentColumns();
		this.addIntValueColumn("Centroid Intensity");
		this.addIntValueColumn("Peak Intensity");
		if (hasSNR) {
			this.addDoubleValueColumn("Mean Intensity");
			this.addDoubleValueColumn("Local Background");
			this.addDoubleValueColumn("Local Noise");
			this.addDoubleValueColumn("Local SNR");
		}
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		OmegaSegmentationTypes types = null;
		if (this.analysisRun != null) {
			types = ((OmegaTrajectoriesSegmentationRun) this.parentAnalysisRun)
					.getSegmentationTypes();
		}
		for (final OmegaTrajectory track : segments.keySet()) {
			String trackID;
			if (track.getElementID() == -1) {
				trackID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				trackID = String.valueOf(track.getElementID());
			}
			final List<OmegaSegment> segmentList = segments.get(track);
			final String trackName = track.getName();
			final int trackLen = track.getLength();
			final List<OmegaROI> rois = track.getROIs();
			for (final OmegaSegment segment : segmentList) {
				String segmID;
				if (segment.getElementID() == -1) {
					segmID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					segmID = String.valueOf(segment.getElementID());
				}
				final int start = segment.getStartingROI().getFrameIndex();
				final int end = segment.getEndingROI().getFrameIndex();
				final String segmName = segment.getName();
				final String segmType = types.getSegmentationName(segment
						.getSegmentationType());
				final int segmLen = OmegaTrajectoryUtilities
						.computeSegmentLength(track, segment);
				for (final OmegaROI roi : rois) {
					if (roi.getElementID() == -1) {
					} else {
						String.valueOf(roi.getElementID());
					}
					final int index = roi.getFrameIndex();
					if ((index < start) || (index >= end)) {
						continue;
					}
					final Double centroid = centroidSignals.get(roi);
					final Double peak = peakSignals.get(roi);
					
					if (hasSNR) {
						final Double mean = meanSignals.get(roi);
						final Double background = backgrounds.get(roi);
						final Double noise = noises.get(roi);
						final Double snr = snrs.get(roi);
						final Object[] row = { trackID, trackName, trackLen,
								segmID, segmName, segmType, segmLen, centroid,
								peak, mean, background, noise, snr };
						dtm.addRow(row);
					} else {
						final Object[] row = { trackID, trackName, trackLen,
								segmID, segmName, segmType, segmLen, centroid,
								peak };
						dtm.addRow(row);
					}
				}
			}
		}
	}

	private void populateGlobalIntensitySegmentsResults(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, Double[]> peakSignals,
			final Map<OmegaSegment, Double[]> centroidSignals,
			final boolean hasSNR,
			final Map<OmegaSegment, Double[]> meanSignals,
			final Map<OmegaSegment, Double[]> backgrounds,
			final Map<OmegaSegment, Double[]> noises,
			final Map<OmegaSegment, Double[]> snrs) {
		this.addTrajectoryColumns();
		this.addSegmentColumns();
		this.addIntValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID_AVG);
		this.addIntValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID_MIN);
		this.addIntValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_CENTROID_MAX);
		this.addIntValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_PEAK_AVG);
		this.addIntValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_PEAK_MIN);
		this.addIntValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_PEAK_MAX);
		if (hasSNR) {
			this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_MEAN_AVG);
			this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_MEAN_MIN);
			this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_MEAN_MAX);
			this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND_AVG);
			this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND_MIN);
			this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND_MAX);
			this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_NOISE_AVG);
			this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_NOISE_MIN);
			this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_NOISE_MAX);
			this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_SNR_AVG);
			this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_SNR_MIN);
			this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_INTENSITY_SNR_MAX);
		}
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		OmegaSegmentationTypes types = null;
		if (this.analysisRun != null) {
			types = ((OmegaTrajectoriesSegmentationRun) this.parentAnalysisRun)
					.getSegmentationTypes();
		}
		for (final OmegaTrajectory track : segments.keySet()) {
			String trackID;
			if (track.getElementID() == -1) {
				trackID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				trackID = String.valueOf(track.getElementID());
			}
			final List<OmegaSegment> segmentList = segments.get(track);
			final String trackName = track.getName();
			final int trackLen = track.getLength();
			for (final OmegaSegment segment : segmentList) {
				String segmID;
				if (segment.getElementID() == -1) {
					segmID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					segmID = String.valueOf(segment.getElementID());
				}
				final Double[] peaks = peakSignals.get(segment);
				final Double[] centroids = centroidSignals.get(segment);
				final String segmName = segment.getName();
				final String segmType = types.getSegmentationName(segment
						.getSegmentationType());
				final int segmLen = OmegaTrajectoryUtilities
						.computeSegmentLength(track, segment);
				if (hasSNR) {
					final Double[] means = meanSignals.get(segment);
					final Double[] bgr = backgrounds.get(segment);
					final Double[] noise = noises.get(segment);
					final Double[] snr = snrs.get(segment);
					final Object[] row = { trackID, trackName, trackLen,
							segmID, segmName, segmType, segmLen, centroids[1],
							centroids[0], centroids[2], peaks[1], peaks[0],
							peaks[2], means[1], means[0], means[2], bgr[1],
							bgr[0], bgr[2], noise[1], noise[0], noise[2],
							snr[1], snr[0], snr[2] };
					dtm.addRow(row);
				} else {
					final Object[] row = { trackID, trackName, trackLen,
							segmID, segmName, segmType, segmLen, centroids[1],
							centroids[0], centroids[2], peaks[1], peaks[0],
							peaks[2] };
					dtm.addRow(row);
				}
			}
		}
	}
	
	private void populateLocalVelocitySegmentsResults(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, List<Double>> localSpeeds,
			final Map<OmegaSegment, List<Double>> localSpeedsFromOrigin,
			final Map<OmegaSegment, List<Double>> localVelocitiesFromOrigin) {
		this.addParticleColumns(false);
		this.addTrajectoryColumns();
		this.addSegmentColumns();
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_VELOCITY_SPEED);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_VELOCITY_CUM_SPEED);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_VELOCITY_CUM_VELO);
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		OmegaSegmentationTypes types = null;
		if (this.analysisRun != null) {
			types = ((OmegaTrajectoriesSegmentationRun) this.parentAnalysisRun)
					.getSegmentationTypes();
		}
		for (final OmegaTrajectory track : segments.keySet()) {
			String trackID;
			if (track.getElementID() == -1) {
				trackID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				trackID = String.valueOf(track.getElementID());
			}
			final List<OmegaSegment> segmentList = segments.get(track);
			final String trackName = track.getName();
			final int trackLen = track.getLength();
			final List<OmegaROI> rois = track.getROIs();
			for (final OmegaSegment segment : segmentList) {
				String segmID;
				if (segment.getElementID() == -1) {
					segmID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					segmID = String.valueOf(segment.getElementID());
				}
				final List<Double> speeds = localSpeeds.get(segment);
				final List<Double> speedsFromOrigin = localSpeedsFromOrigin
						.get(segment);
				final List<Double> velocitiesFromOrigin = localVelocitiesFromOrigin
						.get(segment);
				final int start = segment.getStartingROI().getFrameIndex();
				final int end = segment.getEndingROI().getFrameIndex();
				int i = 0;
				for (final OmegaROI roi : rois) {
					String roiID;
					if (roi.getElementID() == -1) {
						roiID = OmegaGUIConstants.NOT_ASSIGNED;
					} else {
						roiID = String.valueOf(roi.getElementID());
					}
					final int index = roi.getFrameIndex();
					i++;
					if ((index < start) || (index >= end)) {
						continue;
					}
					final Double speed = speeds.get(i);
					final Double speedFromOrigin = speedsFromOrigin.get(i);
					final Double velocityFromOrigin = velocitiesFromOrigin
							.get(i);

					final String segmName = segment.getName();
					final String segmType = types.getSegmentationName(segment
							.getSegmentationType());
					final int segmLen = OmegaTrajectoryUtilities
							.computeSegmentLength(track, segment);
					final Object[] row = { roiID, roi.getFrameIndex(),
							roi.getX(), roi.getY(), this.z, this.c, trackID,
							trackName, trackLen, segmID, segmName, segmType,
							segmLen, speed, speedFromOrigin, velocityFromOrigin };
					dtm.addRow(row);
				}
			}
		}
	}
	
	private void populateGlobalVelocitySegmentsResults(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, Double> averageSpeeds,
			final Map<OmegaSegment, Double> averageVelocities,
			final Map<OmegaSegment, Double> forwardProgressions) {
		this.addTrajectoryColumns();
		this.addSegmentColumns();
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_VELOCITY_AVG_SPEED);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_VELOCITY_AVG_VELO);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_VELOCITY_PROG);
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		OmegaSegmentationTypes types = null;
		if (this.analysisRun != null) {
			types = ((OmegaTrajectoriesSegmentationRun) this.parentAnalysisRun)
					.getSegmentationTypes();
		}
		for (final OmegaTrajectory track : segments.keySet()) {
			String trackID;
			if (track.getElementID() == -1) {
				trackID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				trackID = String.valueOf(track.getElementID());
			}
			final List<OmegaSegment> segmentList = segments.get(track);
			final String trackName = track.getName();
			final int trackLen = track.getLength();
			for (final OmegaSegment segment : segmentList) {
				String segmID;
				if (segment.getElementID() == -1) {
					segmID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					segmID = String.valueOf(segment.getElementID());
				}
				final Double speed = averageSpeeds.get(segment);
				final Double velocity = averageVelocities.get(segment);
				final Double forwardProgression = forwardProgressions
						.get(segment);
				final String segmName = segment.getName();
				final String segmType = types.getSegmentationName(segment
						.getSegmentationType());
				final int segmLen = OmegaTrajectoryUtilities
						.computeSegmentLength(track, segment);
				final Object[] row = { trackID, trackName, trackLen, segmID,
						segmName, segmType, segmLen, speed, velocity,
						forwardProgression };
				dtm.addRow(row);
			}
		}
	}
	
	private void populateLocalMobilitySegmentsResults(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, List<Double>> localDistances,
			final Map<OmegaSegment, List<Double>> localDistancesFromOrigin,
			final Map<OmegaSegment, List<Double>> localDisplacementsFromOrigin,
			final Map<OmegaSegment, List<Double>> localConfinementRatios,
			final Map<OmegaSegment, List<Double[]>> localAnglesAndDirectionalChanges,
			final Map<OmegaSegment, List<Double>> timeTraveled) {
		this.addParticleColumns(false);
		this.addTrajectoryColumns();
		this.addSegmentColumns();
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_MOBILITY_DIST);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_MOBILITY_CUM_CURV_DIST);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_MOBILITY_CUM_STR_DIST);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_MOBILITY_CUM_CONF_RATIO);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_MOBILITY_CUM_INSTA_ANGLE);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_MOBILITY_DIR_CHANGE);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_MOBILITY_CUM_TIME);
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		OmegaSegmentationTypes types = null;
		if (this.analysisRun != null) {
			types = ((OmegaTrajectoriesSegmentationRun) this.parentAnalysisRun)
					.getSegmentationTypes();
		}
		for (final OmegaTrajectory track : segments.keySet()) {
			String trackID;
			if (track.getElementID() == -1) {
				trackID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				trackID = String.valueOf(track.getElementID());
			}
			final List<OmegaSegment> segmentList = segments.get(track);
			final String trackName = track.getName();
			final int trackLen = track.getLength();
			final List<OmegaROI> rois = track.getROIs();
			for (final OmegaSegment segment : segmentList) {
				String segmID;
				if (segment.getElementID() == -1) {
					segmID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					segmID = String.valueOf(segment.getElementID());
				}

				final String segmName = segment.getName();
				final String segmType = types.getSegmentationName(segment
						.getSegmentationType());
				final int segmLen = OmegaTrajectoryUtilities
						.computeSegmentLength(track, segment);
				final List<Double> times = timeTraveled.get(segment);
				final List<Double> instDist = localDistances.get(segment);
				final List<Double> distances = localDistancesFromOrigin
						.get(segment);
				final List<Double> displacements = localDisplacementsFromOrigin
						.get(segment);
				final List<Double> confiments = localConfinementRatios
						.get(segment);
				final List<Double[]> anglesAndDirectionalChanges = localAnglesAndDirectionalChanges
						.get(segment);
				final int start = segment.getStartingROI().getFrameIndex();
				final int end = segment.getEndingROI().getFrameIndex();
				int i = 0;
				for (final OmegaROI roi : rois) {
					String roiID;
					if (roi.getElementID() == -1) {
						roiID = OmegaGUIConstants.NOT_ASSIGNED;
					} else {
						roiID = String.valueOf(roi.getElementID());
					}
					final int index = roi.getFrameIndex();
					if ((index < start) || (index >= end)) {
						continue;
					}
					final Double time = times.get(i);
					final Double ldist = instDist.get(i);
					final Double dist = distances.get(i);
					final Double displ = displacements.get(i);
					final Double conf = confiments.get(i);
					final Double[] angles = anglesAndDirectionalChanges.get(i);
					i++;
					
					final Object[] row = { roiID, roi.getFrameIndex(),
							roi.getX(), roi.getY(), this.z, this.c, trackID,
							trackName, trackLen, segmID, segmName, segmType,
							segmLen, ldist, dist, displ, conf, angles[0],
							angles[1], time };
					dtm.addRow(row);
				}
			}
		}
	}
	
	private void populateGlobalMobilitySegmentsResults(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final Map<OmegaSegment, Double> maxDisplacementes,
			final Map<OmegaSegment, List<Double>> localDistancesFromOrigin,
			final Map<OmegaSegment, List<Double>> localDisplacementsFromOrigin,
			final Map<OmegaSegment, List<Double>> confinementRatios,
			final Map<OmegaSegment, List<Double>> timeTraveled) {
		this.addTrajectoryColumns();
		this.addSegmentColumns();
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_MOBILITY_TOT_CURV_DIST);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_MOBILITY_MAX_STR_DIST);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_MOBILITY_TOT_STR_DIST);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_MOBILITY_CONF_RATIO);
		this.addDoubleValueColumn(OmegaGUIConstants.RESULTS_MOBILITY_TOT_TIME);
		// this.addDoubleValueColumn("Total time");
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		OmegaSegmentationTypes types = null;
		if (this.analysisRun != null) {
			types = ((OmegaTrajectoriesSegmentationRun) this.parentAnalysisRun)
					.getSegmentationTypes();
		}
		for (final OmegaTrajectory track : segments.keySet()) {
			String trackID;
			if (track.getElementID() == -1) {
				trackID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				trackID = String.valueOf(track.getElementID());
			}
			final List<OmegaSegment> segmentList = segments.get(track);
			final String trackName = track.getName();
			final int trackLen = track.getLength();
			final List<OmegaROI> rois = track.getROIs();
			final int lastIndex = rois.get(rois.size() - 1).getFrameIndex() - 1;
			for (final OmegaSegment segment : segmentList) {
				String segmID;
				if (segment.getElementID() == -1) {
					segmID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					segmID = String.valueOf(segment.getElementID());
				}
				final String segmName = segment.getName();
				final String segmType = types.getSegmentationName(segment
						.getSegmentationType());
				final int segmLen = OmegaTrajectoryUtilities
						.computeSegmentLength(track, segment);
				final Double time = timeTraveled.get(segment).get(lastIndex);
				final Double confRatio = confinementRatios.get(segment).get(
						lastIndex);
				final Double dist = localDistancesFromOrigin.get(segment).get(
						lastIndex);
				final Double disp = localDisplacementsFromOrigin.get(segment)
						.get(lastIndex);
				final Double maxDisp = maxDisplacementes.get(segment);
				// final Double disp = maxDisplacementes.get(segment);
				final Object[] row = { trackID, trackName, trackLen, segmID,
						segmName, segmType, segmLen, dist, maxDisp, disp,
						confRatio, time };
				dtm.addRow(row);
			}
		}
	}
	
	private void populateLocalDiffusivitySegmentsResults(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final String windowDivisor,
			final Map<OmegaSegment, Double[]> nyMap,
			final Map<OmegaSegment, Double[][]> logMuMap,
			final Map<OmegaSegment, Double[][]> muMap,
			final Map<OmegaSegment, Double[][]> logDeltaTMap,
			final Map<OmegaSegment, Double[][]> deltaTMap) {
		this.addTrajectoryColumns();
		this.addSegmentColumns();
		// this.addStringValueColumn("Interval");
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_WIN_SIZE);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_MOMENT_ORDER);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_DELTA);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_MU);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_DELTA_LOG);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_MU_LOG);
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		OmegaSegmentationTypes types = null;
		if (this.analysisRun != null) {
			types = ((OmegaTrajectoriesSegmentationRun) this.parentAnalysisRun)
					.getSegmentationTypes();
		}
		for (final OmegaTrajectory track : segments.keySet()) {
			String trackID;
			if (track.getElementID() == -1) {
				trackID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				trackID = String.valueOf(track.getElementID());
			}
			final List<OmegaSegment> segmentList = segments.get(track);
			final String trackName = track.getName();
			final int trackLen = track.getLength();
			for (final OmegaSegment segment : segmentList) {
				String segmID;
				if (segment.getElementID() == -1) {
					segmID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					segmID = String.valueOf(segment.getElementID());
				}
				Double[] nus = null;
				if ((nyMap != null) && nyMap.containsKey(segment)) {
					nus = nyMap.get(segment);
				}
				Double[][] logMus = null;
				if ((logMuMap != null) && logMuMap.containsKey(segment)) {
					logMus = logMuMap.get(segment);
				}
				Double[][] mus = null;
				if ((muMap != null) && muMap.containsKey(segment)) {
					mus = muMap.get(segment);
				}
				Double[][] logDeltaTs = null;
				if ((logDeltaTMap != null) && logDeltaTMap.containsKey(segment)) {
					logDeltaTs = logDeltaTMap.get(segment);
				}
				Double[][] deltaTs = null;
				if ((deltaTMap != null) && deltaTMap.containsKey(segment)) {
					deltaTs = deltaTMap.get(segment);
				}
				final String segmName = segment.getName();
				final String segmType = types.getSegmentationName(segment
						.getSegmentationType());
				final int segmLen = OmegaTrajectoryUtilities
						.computeSegmentLength(track, segment);
				if (nus == null) {
					continue;
				}
				for (final Double nu : nus) {
					final Integer ny = new BigDecimal(nu).intValue();
					Double[] logMusNu = null;
					if (logMus != null) {
						logMusNu = logMus[ny];
					}
					Double[] musNu = null;
					if (mus != null) {
						musNu = mus[ny];
					}
					Double[] logDeltaTNu = null;
					if (logDeltaTs != null) {
						logDeltaTNu = logDeltaTs[ny];
					}
					Double[] deltaTNu = null;
					if (deltaTs != null) {
						deltaTNu = deltaTs[ny];
					}
					int l = 0;
					if (logMusNu != null) {
						l = logMusNu.length;
					} else if (musNu != null) {
						l = musNu.length;
					} else if (logDeltaTNu != null) {
						l = logDeltaTNu.length;
					} else if (deltaTNu != null) {
						l = deltaTNu.length;
					}
					for (int i = 0; i < l; i++) {
						String logMu = OmegaGUIConstants.NOT_ASSIGNED;
						if ((logMusNu != null) && (logMusNu[i] != null)) {
							logMu = String.valueOf(logMusNu[i]);
						}
						String mu = OmegaGUIConstants.NOT_ASSIGNED;
						if ((musNu != null) && (musNu[i] != null)) {
							mu = String.valueOf(musNu[i]);
						}
						String logDeltaT = OmegaGUIConstants.NOT_ASSIGNED;
						if ((logDeltaTNu != null) && (logDeltaTNu[i] != null)) {
							logDeltaT = String.valueOf(logDeltaTNu[i]);
						}
						String deltaT = OmegaGUIConstants.NOT_ASSIGNED;
						if ((deltaTNu != null) && (deltaTNu[i] != null)) {
							deltaT = String.valueOf(deltaTNu[i]);
						}
						final String nyVal = String.valueOf(ny);
						final Object[] row = { trackID, trackName, trackLen,
								segmID, segmName, segmType, segmLen,
								windowDivisor, nyVal, deltaT, mu, logDeltaT,
								logMu };
						dtm.addRow(row);
					}
					
				}
				
			}
		}
	}
	
	private void populateGlobalGenericDiffusivitySegmentsResults(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final String windowDivisor,
			final Map<OmegaSegment, Double[]> nyMap,
			final Map<OmegaSegment, Double[][]> gammaDFromLogResultsMap,
			final Map<OmegaSegment, Double[][]> gammaDResultsMap) {
		this.addTrajectoryColumns();
		this.addSegmentColumns();
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_WIN_SIZE);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_MOMENT_ORDER);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_GAMMA);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_Y0);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_FIT);
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		OmegaSegmentationTypes types = null;
		if (this.analysisRun != null) {
			types = ((OmegaTrajectoriesSegmentationRun) this.parentAnalysisRun)
					.getSegmentationTypes();
		}
		// final OmegaTrajectory track1 = segments.keySet().iterator().next();
		// final OmegaSegment segment1 = segments.get(track1).get(0);
		// final Double nu1 = nyMap.get(segment1)[0];
		// new BigDecimal(nu1).intValue();
		for (final OmegaTrajectory track : segments.keySet()) {
			String trackID;
			if (track.getElementID() == -1) {
				trackID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				trackID = String.valueOf(track.getElementID());
			}
			final List<OmegaSegment> segmentList = segments.get(track);
			final String trackName = track.getName();
			final int trackLen = track.getLength();
			for (final OmegaSegment segment : segmentList) {
				String segmID;
				if (segment.getElementID() == -1) {
					segmID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					segmID = String.valueOf(segment.getElementID());
				}
				Double[] nus = null;
				if ((nyMap != null) && nyMap.containsKey(segment)) {
					nus = nyMap.get(segment);
				}
				Double[][] gammaDsFromLog = null;
				if ((gammaDFromLogResultsMap != null)
						&& gammaDFromLogResultsMap.containsKey(segment)) {
					gammaDsFromLog = gammaDFromLogResultsMap.get(segment);
				}
				Double[][] gammaDs = null;
				if ((gammaDResultsMap != null)
						&& gammaDResultsMap.containsKey(segment)) {
					gammaDs = gammaDResultsMap.get(segment);
				}
				final String segmName = segment.getName();
				final String segmType = types.getSegmentationName(segment
						.getSegmentationType());
				final int segmLen = OmegaTrajectoryUtilities
						.computeSegmentLength(track, segment);
				if (nus == null) {
					continue;
				}
				for (final Double nu : nus) {
					final Integer ny = new BigDecimal(nu).intValue();
					Double[] gammaDFromLog = null;
					int gammaLogLength = 0;
					if (gammaDsFromLog != null) {
						gammaDFromLog = gammaDsFromLog[ny];
						gammaLogLength = gammaDFromLog.length - 1;
					}
					Double[] gammaD = null;
					int gammaLength = 0;
					if (gammaDs != null) {
						gammaD = gammaDs[ny];
						gammaLength = gammaD.length - 1;
					}
					final int size = 8 + gammaLogLength + gammaLength;
					final Object[] row = new Object[size];
					row[0] = trackID;
					row[1] = trackName;
					row[2] = trackLen;
					row[3] = segmID;
					row[4] = segmName;
					row[5] = segmType;
					row[6] = segmLen;
					row[7] = windowDivisor;
					row[8] = String.valueOf(ny);
					int index = 9;
					for (int i = 0; i < (gammaLength - 1); i++) {
						String gammaVal = OmegaGUIConstants.NOT_ASSIGNED;
						if (gammaD != null) {
							final Double gamma = gammaD[i];
							if (Double.isNaN(gamma)) {
								gammaVal = String.valueOf(gamma);
								row[index] = gammaVal;
							} else {
								gammaVal = String.valueOf(gamma);
								row[index] = gammaVal;
							}
						}
						index++;
					}
					for (int i = 0; i < (gammaLogLength - 1); i++) {
						String gammaVal = OmegaGUIConstants.NOT_ASSIGNED;
						if (gammaDFromLog != null) {
							final Double gamma = gammaDFromLog[i];
							if (Double.isNaN(gamma)) {
								gammaVal = String.valueOf(gamma);
								row[index] = gammaVal;
							} else {
								gammaVal = String.valueOf(gamma);
								row[index] = gammaVal;
							}
						}
						index++;
					}
					dtm.addRow(row);
				}
			}
		}
	}
	
	private void populateGlobalSpecificDiffusivitySegmentsResults(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final String windowDivisor,
			final Map<OmegaSegment, Double[][]> gammaDFromLogResultsMap,
			final Map<OmegaSegment, Double[]> smssFromLogResultsMap,
			final Map<OmegaSegment, Double[]> errosFromLogResultsMap) {
		this.addTrajectoryColumns();
		this.addSegmentColumns();
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_WIN_SIZE);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_MSD);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_Y02);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_ODC);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_SMSS);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_ODC_ERR);
		this.addStringValueColumn(OmegaGUIConstants.RESULTS_DIFFUSIVITY_SMSS_ERR);
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		OmegaSegmentationTypes types = null;
		if (this.analysisRun != null) {
			types = ((OmegaTrajectoriesSegmentationRun) this.parentAnalysisRun)
					.getSegmentationTypes();
		}
		for (final OmegaTrajectory track : segments.keySet()) {
			String trackID;
			if (track.getElementID() == -1) {
				trackID = OmegaGUIConstants.NOT_ASSIGNED;
			} else {
				trackID = String.valueOf(track.getElementID());
			}
			final List<OmegaSegment> segmentList = segments.get(track);
			final String trackName = track.getName();
			final int trackLen = track.getLength();
			for (final OmegaSegment segment : segmentList) {
				String segmID;
				if (segment.getElementID() == -1) {
					segmID = OmegaGUIConstants.NOT_ASSIGNED;
				} else {
					segmID = String.valueOf(segment.getElementID());
				}
				Double[][] gammaFromLog = null;
				if ((gammaDFromLogResultsMap != null)
						&& gammaDFromLogResultsMap.containsKey(segment)) {
					gammaFromLog = gammaDFromLogResultsMap.get(segment);
				}
				Double[] smssFromLog = null;
				if ((smssFromLogResultsMap != null)
						&& smssFromLogResultsMap.containsKey(segment)) {
					smssFromLog = smssFromLogResultsMap.get(segment);
				}
				Double[] errorFromLog = null;
				if ((errosFromLogResultsMap != null)
						&& errosFromLogResultsMap.containsKey(segment)) {
					errorFromLog = errosFromLogResultsMap.get(segment);
				}
				final String segmName = segment.getName();
				final String segmType = types.getSegmentationName(segment
						.getSegmentationType());
				final int segmLen = OmegaTrajectoryUtilities
						.computeSegmentLength(track, segment);
				String dVal = OmegaGUIConstants.NOT_ASSIGNED;
				String smsdVal = OmegaGUIConstants.NOT_ASSIGNED;
				String intercept = OmegaGUIConstants.NOT_ASSIGNED;
				if ((gammaFromLog != null) && (gammaFromLog[2] != null)) {
					dVal = String.valueOf(gammaFromLog[2][3]);
					smsdVal = String.valueOf(gammaFromLog[2][0]);
					intercept = String.valueOf(gammaFromLog[2][1]);
				}
				String dErr = OmegaGUIConstants.NOT_ASSIGNED;
				if (errorFromLog != null) {
					dErr = String.valueOf(errorFromLog[0]);
				}
				String smssVal = OmegaGUIConstants.NOT_ASSIGNED;
				if (smssFromLog != null) {
					smssVal = String.valueOf(smssFromLog[0]);
				}
				String smssErr = OmegaGUIConstants.NOT_ASSIGNED;
				if (errorFromLog != null) {
					smssErr = String.valueOf(errorFromLog[1]);
				}

				final Object[] row = { trackID, trackName, trackLen, segmID,
						segmName, segmType, segmLen, windowDivisor, smsdVal,
						intercept, dVal, smssVal, dErr, smssErr };
				dtm.addRow(row);
			}
		}
	}
	
	private void updateFilterPanel() {
		final List<String> columNames = new ArrayList<String>();
		for (int i = 0; i < this.table.getColumnCount(); i++) {
			columNames.add(this.table.getColumnName(i));
		}
		this.filterPanel.updateCombo(columNames);
	}
	
	private void resetResultsPanel() {
		// this.table.setRowSorter(null)
		this.ints.clear();
		this.doubles.clear();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		for (int i = 0; i < dtm.getColumnCount(); i++) {
			this.rowSorter.setComparator(i, null);
		}
		dtm.setColumnCount(0);
		dtm.setRowCount(0);
		this.table.revalidate();
		this.table.repaint();
	}
	
	public void setAnalysisRun(final OmegaAnalysisRun analysisRun,
			final String c, final String z) {
		this.setAnalysisRun(analysisRun, true, c, z);
	}
	
	public void setAnalysisRun(final OmegaAnalysisRun analysisRun,
			final boolean isLocal, final String c, final String z) {
		this.setAnalysisRun(analysisRun, null, isLocal, c, z);
	}

	public void setAnalysisRun(final OmegaAnalysisRun analysisRun,
			final boolean isLocal, final boolean isSpecific, final String c,
			final String z) {
		this.setAnalysisRun(analysisRun, null, isLocal, isSpecific, c, z);
	}

	public void setAnalysisRun(final OmegaAnalysisRun analysisRun,
			final OmegaAnalysisRun parentAnalysisRun, final boolean isLocal,
			final String c, final String z) {
		this.setAnalysisRun(analysisRun, parentAnalysisRun, isLocal, true, c, z);
	}
	
	public void setAnalysisRun(final OmegaAnalysisRun analysisRun,
			final OmegaAnalysisRun parentAnalysisRun, final boolean isLocal,
			final boolean isSpecific, final String c, final String z) {
		this.isSpecific = isSpecific;
		this.isLocal = isLocal;
		this.parentAnalysisRun = parentAnalysisRun;
		this.analysisRun = analysisRun;
		this.infoPanel.updateContent(analysisRun);
		this.resetResultsPanel();
		if (this.analysisRun != null) {
			this.populateResultsPanel(c, z);
		}
	}
	
	public OmegaAnalysisRun getAnalysisRun() {
		return this.analysisRun;
	}
	
	@Override
	public void handleFilterEvent(final OmegaFilterEvent event) {
		final String key = event.getKey();
		String val = event.getValue();
		final boolean isExact = event.isExact();
		// System.out.println(key + " " + val + " " + isExact);
		if (isExact) {
			val = "^" + val + "$";
		}
		RowFilter<DefaultTableModel, Object> rf = null;
		// If current expression doesn't parse, don't update.
		if (val.isEmpty()) {
			this.rowSorter.setRowFilter(null);
			return;
		}
		int columnIndex = -1;
		for (int i = 0; i < this.table.getColumnCount(); i++) {
			final String columnName = this.table.getColumnName(i);
			if (key.equals(columnName)) {
				columnIndex = i;
				break;
			}
		}
		try {
			if (columnIndex == -1) {
				rf = RowFilter.regexFilter(val);
			} else {
				rf = RowFilter.regexFilter(val, columnIndex);
			}
		} catch (final PatternSyntaxException ex) {
			return;
		}
		this.rowSorter.setRowFilter(rf);
	}

	@Override
	public void updateParentContainer(final RootPaneContainer parent) {
		super.updateParentContainer(parent);
		this.infoPanel.updateParentContainer(parent);
	}
}
