package main.java.edu.umassmed.omega.commons.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaParticleDetectionRun;
import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaParticleLinkingRun;
import main.java.edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrajectoriesSegmentationRun;
import main.java.edu.umassmed.omega.commons.data.coreElements.OmegaFrame;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegmentationTypes;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import main.java.edu.umassmed.omega.commons.eventSystem.OmegaFilterEventListener;
import main.java.edu.umassmed.omega.commons.eventSystem.events.OmegaFilterEvent;

public class GenericTrackingResultsPanel extends GenericScrollPane implements
OmegaFilterEventListener {

	private static final long serialVersionUID = 1114253444374606565L;

	private OmegaAnalysisRun analysisRun;

	private GenericAnalysisInformationPanel infoPanel;
	private GenericFilterPanel filterPanel;

	private JTable table;
	private TableRowSorter<DefaultTableModel> rowSorter;
	private final List<Integer> ints, doubles;

	// private JFXPanel fxPanel;
	// private GridPane gp;

	public GenericTrackingResultsPanel(final RootPaneContainer parent) {
		super(parent);
		this.ints = new ArrayList<Integer>();
		this.doubles = new ArrayList<Integer>();

		this.createAndAddWidgets();

		this.addListeners();
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

	private void addParticleColumns() {
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		dtm.addColumn("ID");
		this.ints.add(0);
		dtm.addColumn("Frame");
		this.ints.add(1);
		dtm.addColumn("X");
		this.doubles.add(2);
		dtm.addColumn("Y");
		this.doubles.add(3);
	}

	private void addTrajectoryColumns() {
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		dtm.addColumn("Track");
		final DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		this.table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		dtm.addColumn("Index");
		this.ints.add(5);
	}

	private void addSegmentColumns() {
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		dtm.addColumn("Segment");
		final DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		this.table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
	}

	private void addParticleValuesColumns() {
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		final OmegaParticleDetectionRun detectionRun = (OmegaParticleDetectionRun) this.analysisRun;
		final Map<OmegaROI, Map<String, Object>> particlesValues = detectionRun
		        .getResultingParticlesValues();
		if (particlesValues.isEmpty())
			return;
		final OmegaROI roi = (OmegaROI) particlesValues.keySet().toArray()[0];
		final Map<String, Object> particleValues = particlesValues.get(roi);
		int counter = dtm.getColumnCount();
		for (final String s : particleValues.keySet()) {
			final Object val = particleValues.get(s);
			if ((val instanceof Integer) || (val instanceof Long)) {
				this.ints.add(counter);
			} else if ((val instanceof Float) || (val instanceof Double)) {
				this.doubles.add(counter);
			}
			counter++;
			dtm.addColumn(s);
		}
	}

	private void populateResultsPanel() {
		if (this.analysisRun instanceof OmegaParticleDetectionRun) {
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
		} else {
			// ERROR
		}
		// this.table.setRowSorter(this.rowSorter);
	}

	public void populateParticlesResults(
	        final Map<OmegaFrame, List<OmegaROI>> particles,
	        final Map<OmegaROI, Map<String, Object>> particlesValues) {
		this.resetResultsPanel();
		this.addParticleColumns();
		this.addParticleValuesColumns();
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		for (final OmegaFrame frame : particles.keySet()) {
			final List<OmegaROI> rois = particles.get(frame);
			for (final OmegaROI roi : rois) {
				final Map<String, Object> particleValues = particlesValues
				        .get(roi);
				final List<Object> row = new ArrayList<Object>();
				row.add(roi.getElementID());
				row.add(frame.getIndex());
				row.add(roi.getX());
				row.add(roi.getY());
				for (final String valName : particleValues.keySet()) {
					row.add(particleValues.get(valName));
				}
				dtm.addRow(row.toArray());
			}
		}
	}

	public void populateTrajectoriesResults(final List<OmegaTrajectory> tracks) {
		this.resetResultsPanel();
		this.addParticleColumns();
		this.addTrajectoryColumns();
		this.updateFilterPanel();
		final DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		for (final OmegaTrajectory track : tracks) {
			final String trackName = track.getName();
			final List<OmegaROI> rois = track.getROIs();
			for (final OmegaROI roi : rois) {
				final Object[] row = { roi.getElementID(), roi.getFrameIndex(),
				        roi.getX(), roi.getY(), trackName, rois.indexOf(roi) };
				dtm.addRow(row);
			}
		}
	}

	public void populateSegmentsResults(
			final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		this.resetResultsPanel();
		this.addParticleColumns();
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
			final List<OmegaROI> rois = track.getROIs();
			for (final OmegaROI roi : rois) {
				final int index = roi.getFrameIndex();
				OmegaSegment selectedSegment = null;
				for (final OmegaSegment segment : segmentList) {
					final int start = segment.getStartingROI().getFrameIndex();
					final int end = segment.getEndingROI().getFrameIndex();
					if ((index >= start) && (index <= end)) {
						selectedSegment = segment;
						break;
					}
				}
				final String segmName = types
				        .getSegmentationName(selectedSegment
				                .getSegmentationType());
				final Object[] row = { roi.getElementID(), roi.getFrameIndex(),
				        roi.getX(), roi.getY(), trackName, rois.indexOf(roi),
				        segmName };
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

	public void setAnalysisRun(final OmegaAnalysisRun analysisRun) {
		this.analysisRun = analysisRun;
		this.infoPanel.update(analysisRun);
		if (this.analysisRun != null) {
			this.populateResultsPanel();
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
		System.out.println(key + " " + val + " " + isExact);
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
}
