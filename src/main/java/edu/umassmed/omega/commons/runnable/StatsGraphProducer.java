package edu.umassmed.omega.commons.runnable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.DefaultCategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import edu.umassmed.omega.commons.constants.StatsConstants;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaParticle;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegmentationTypes;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public abstract class StatsGraphProducer implements Runnable {

	public final static int LINE_GRAPH = 0;
	public final static int BAR_GRAPH = 1;
	public final static int HISTOGRAM_GRAPH = 2;

	private CategoryItemRenderer categoryItemRenderer;
	private CategoryItemRenderer xyLineAndShapeRenderer;

	private volatile boolean isTerminated;
	private double completed;

	private final int graphType;

	private final Map<OmegaTrajectory, List<OmegaSegment>> segmentsMap;
	private final OmegaSegmentationTypes segmTypes;

	public StatsGraphProducer(final int graphType,
			final Map<OmegaTrajectory, List<OmegaSegment>> segmentsMap,
			final OmegaSegmentationTypes segmTypes) {
		this.segmentsMap = segmentsMap;
		this.segmTypes = segmTypes;

		this.isTerminated = false;
		this.completed = 0.0;

		this.graphType = graphType;

		this.categoryItemRenderer = null;
		this.xyLineAndShapeRenderer = null;
	}

	/**
	 * Category Item renderer that is drawing item based on track color
	 *
	 * @return
	 */
	public CategoryItemRenderer getTracksRenderer() {
		if (this.categoryItemRenderer == null) {
			switch (this.graphType) {
			case BAR_GRAPH:
				this.createTracksBarRenderer();
				break;
			case HISTOGRAM_GRAPH:
				this.createTracksHistogramBarRenderer();
				break;
			default:
				this.createTracksLineRenderer();
			}

		}
		return this.categoryItemRenderer;
	}

	private void createTracksBarRenderer() {
		this.categoryItemRenderer = new BarRenderer() {
			private static final long serialVersionUID = 3343456141507762482L;

			@Override
			public Paint getItemPaint(final int row, final int column) {
				final String name = (String) this.getPlot().getDataset()
						.getColumnKey(column);
				for (final OmegaTrajectory track : StatsGraphProducer.this.segmentsMap
						.keySet()) {
					if (track.getName().equals(name))
						return track.getColor();
				}
				return Color.black;
			}
		};
	}

	private void createTracksHistogramBarRenderer() {
		this.categoryItemRenderer = new BarRenderer() {
			private static final long serialVersionUID = -1976632258511076322L;

			// @Override
			// public Paint getItemPaint(final int row, final int column) {
			// final String name = (String) this.getPlot().getDataset()
			// .getColumnKey(column);
			// for (final OmegaTrajectory track :
			// StatsGraphProducer.this.segmentsMap
			// .keySet()) {
			// if (track.getName().equals(name))
			// return track.getColor();
			// }
			// return Color.black;
			// }

		};
	}

	private void createTracksLineRenderer() {
		this.categoryItemRenderer = new DefaultCategoryItemRenderer() {
			private static final long serialVersionUID = 3343456141507762482L;

			// @Override
			// public Paint getItemPaint(final int row, final int column) {
			// final String name = (String) this.getPlot().getDataset()
			// .getColumnKey(column);
			// for (final OmegaTrajectory track :
			// StatsGraphProducer.this.segmentsMap
			// .keySet()) {
			// if (track.getName().equals(name))
			// return track.getColor();
			// }
			// return Color.black;
			// }
		};
	}

	/**
	 * Line and Shape renderer that is drawing dashed line between between
	 * missing timepoints and solid line in other cases
	 *
	 * @param renderingMap
	 *
	 * @return
	 */
	public CategoryItemRenderer getTimepointsRenderer(
			final Map<String, Map<Integer, Boolean>> renderingMap) {
		if (this.xyLineAndShapeRenderer == null) {
			switch (this.graphType) {
			case BAR_GRAPH:
				this.createTimepointsBarRenderer(renderingMap);
				break;
			case HISTOGRAM_GRAPH:
				this.createTimepointsHistogramBarRenderer();
				break;
			default:
				this.createTimepointsLineRenderer(renderingMap);
			}

		}
		return this.xyLineAndShapeRenderer;
	}

	private void createTimepointsBarRenderer(
			final Map<String, Map<Integer, Boolean>> renderingMap) {
		this.xyLineAndShapeRenderer = new BarRenderer() {
			private static final long serialVersionUID = -4868788326156259962L;

			@Override
			public boolean getItemVisible(final int series, final int item) {
				final DefaultCategoryDataset dataset = (DefaultCategoryDataset) this
						.getPlot().getDataset();
				final String name = (String) dataset.getRowKey(series);
				final Map<Integer, Boolean> renderingList = renderingMap
						.get(name);
				if (!renderingList.containsKey(item))
					return false;
				final boolean bool = renderingList.get(item);
				return bool;
			}

			@Override
			public Paint getSeriesPaint(final int series) {
				final String trackName = (String) this.getPlot().getDataset()
				        .getRowKey(series);
				for (final OmegaTrajectory track : StatsGraphProducer.this.segmentsMap
				        .keySet()) {
					for (final OmegaSegment segment : StatsGraphProducer.this.segmentsMap
					        .get(track)) {
						final String name = track.getName() + " "
						        + segment.getStartingROI().getFrameIndex()
								+ "-" + segment.getEndingROI().getFrameIndex();
						if (name.equals(trackName))
							return StatsGraphProducer.this.segmTypes
							        .getSegmentationColor(segment
							                .getSegmentationType());
					}
					if (track.getName().equals(trackName))
						return track.getColor();
				}
				return Color.black;
			}
		};
	}

	private void createTimepointsHistogramBarRenderer() {
		this.xyLineAndShapeRenderer = new BarRenderer() {
			private static final long serialVersionUID = -4868788326156259962L;

		};
	}

	private void createTimepointsLineRenderer(
	        final Map<String, Map<Integer, Boolean>> renderingMap) {
		this.xyLineAndShapeRenderer = new DefaultCategoryItemRenderer() {
			private static final long serialVersionUID = 1071820316920620277L;

			// @Override
			// public boolean getItemLineVisible(final int series, final int
			// item) {
			// System.out.println("IL " + series + " - " + item);
			// final DefaultCategoryDataset dataset = (DefaultCategoryDataset)
			// this
			// .getPlot().getDataset();
			// final String name = (String) dataset.getRowKey(series);
			// final Map<Integer, Boolean> renderingList = renderingMap
			// .get(name);
			// if (!renderingList.containsKey(item))
			// return false;
			// return true;
			// }
			//
			// @Override
			// public boolean getItemVisible(final int series, final int item) {
			// System.out.println("I " + series + " - " + item);
			// final DefaultCategoryDataset dataset = (DefaultCategoryDataset)
			// this
			// .getPlot().getDataset();
			// final String name = (String) dataset.getRowKey(series);
			// final Map<Integer, Boolean> renderingList = renderingMap
			// .get(name);
			// if (!renderingList.containsKey(item))
			// return false;
			// return renderingList.get(item);
			// }

			@Override
			public boolean getItemShapeVisible(final int series, final int item) {
				final DefaultCategoryDataset dataset = (DefaultCategoryDataset) this
				        .getPlot().getDataset();
				final String name = (String) dataset.getRowKey(series);
				final Map<Integer, Boolean> renderingList = renderingMap
				        .get(name);
				if (!renderingList.containsKey(item))
					return false;
				final boolean bool = renderingList.get(item);
				return bool;
			}

			@Override
			public Stroke getItemStroke(final int row, final int column) {
				if ((column - 1) < 0)
					return new BasicStroke(1.0f);

				final DefaultCategoryDataset dataset = (DefaultCategoryDataset) this
				        .getPlot().getDataset();
				final String name = (String) dataset.getRowKey(row);
				final Map<Integer, Boolean> renderingList = renderingMap
				        .get(name);

				if (!renderingList.containsKey(column)
				        || !renderingList.containsKey(column - 1))
					return new BasicStroke(1.0f);

				final Boolean bool1 = renderingList.get(column);
				final Boolean bool2 = renderingList.get(column - 1);

				if (!bool1 || !bool2)
					return new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
					        BasicStroke.JOIN_MITER, 1.0f, new float[] { 5.0f,
					                5.0f }, 0.0f);
				else
					return new BasicStroke(1.0f);
			}

			@Override
			public Paint getSeriesPaint(final int series) {
				final String trackName = (String) this.getPlot().getDataset()
				        .getRowKey(series);
				for (final OmegaTrajectory track : StatsGraphProducer.this.segmentsMap
				        .keySet()) {
					for (final OmegaSegment segment : StatsGraphProducer.this.segmentsMap
					        .get(track)) {
						final String name = track.getName() + " "
						        + segment.getStartingROI().getFrameIndex()
								+ "-" + segment.getEndingROI().getFrameIndex();
						if (name.equals(trackName))
							return StatsGraphProducer.this.segmTypes
							        .getSegmentationColor(segment
							                .getSegmentationType());
					}
					if (track.getName().equals(trackName))
						return track.getColor();
				}
				return Color.black;
			}
		};
	}

	@Override
	public void run() {
		this.isTerminated = false;
		this.completed = 0.0;
	}

	protected abstract Double[] getValue(OmegaSegment segment, OmegaROI roi);

	protected Map<String, Map<Integer, Boolean>> createRenderingMap(
			final DefaultCategoryDataset catDataset) {
		final Map<String, Map<Integer, Boolean>> renderingMap = new LinkedHashMap<>();
		for (final OmegaTrajectory track : this.getSegmentsMap().keySet()) {
			for (final OmegaSegment segment : this.getSegmentsMap().get(track)) {
				final String name = track.getName() + " "
						+ segment.getStartingROI().getFrameIndex() + "-"
						+ segment.getEndingROI().getFrameIndex();
				final Map<Integer, Boolean> renderingList = new LinkedHashMap<>();
				Integer oldIndex = null;
				Double oldVal = null;
				final List<OmegaROI> rois = track.getROIs();
				final int roiStart = rois.indexOf(segment.getStartingROI());
				final int roiEnd = rois.indexOf(segment.getEndingROI());
				final List<OmegaROI> segmentROIs = rois.subList(roiStart,
				        roiEnd + 1);
				for (final OmegaROI roi : segmentROIs) {
					final OmegaParticle particle = (OmegaParticle) roi;
					final int newIndex = particle.getFrameIndex();
					final Double newVal = this.getValue(segment, particle)[0];
					if (newVal == null) {
						continue;
					}

					if ((oldIndex != null) && (oldVal != null)) {
						final int delta = newIndex - oldIndex;
						final Double deltaVal = (newVal - oldVal) / delta;
						for (int i = 1; i < delta; i++) {
							final Integer t = oldIndex + i;
							final Double val = oldVal + (deltaVal * i);
							catDataset.setValue(val, name, t);
							renderingList.put(t, false);
						}
					}
					renderingList.put(newIndex, true);
					oldIndex = newIndex;
					oldVal = newVal;
				}
				renderingMap.put(name, renderingList);
			}
		}
		return renderingMap;
	}

	public abstract String getTitle();

	public abstract String getYAxisTitle();

	public abstract void updateStatus(boolean tof);

	protected JPanel prepareTracksGraph(final boolean isROI) {
		final String title = this.getTitle();
		Dataset dataset = null;
		if (this.getGraphType() == StatsGraphProducer.HISTOGRAM_GRAPH) {
			dataset = new HistogramDataset();
			((HistogramDataset) dataset).setType(HistogramType.FREQUENCY);
		} else {
			dataset = new DefaultCategoryDataset();
		}
		final double partial = 100.0 / this.getSegmentsMap().keySet().size();
		final double increase = new BigDecimal(partial).setScale(2,
				RoundingMode.HALF_UP).doubleValue();
		final CategoryItemRenderer renderer = this.getTracksRenderer();
		final List<Double> histValues = new ArrayList<>();
		for (final OmegaTrajectory track : this.getSegmentsMap().keySet()) {
			final List<OmegaROI> rois = track.getROIs();
			for (final OmegaSegment segment : this.getSegmentsMap().get(track)) {
				if (this.isTerminated())
					return null;
				final String name = track.getName() + " "
				        + segment.getStartingROI().getFrameIndex() + "-"
				        + segment.getEndingROI().getFrameIndex();
				final Double value;
				if (isROI) {
					final int roiStart = rois.indexOf(segment.getStartingROI());
					final int roiEnd = rois.indexOf(segment.getEndingROI());
					final List<OmegaROI> segmentROIs = rois.subList(roiStart,
							roiEnd + 1);
					final OmegaROI roi = segmentROIs
							.get(segmentROIs.size() - 1);
					value = this.getValue(segment, roi)[0];
				} else {
					value = this.getValue(segment, null)[0];
				}
				if (value == null) {
					continue;
				}
				if (dataset instanceof HistogramDataset) {
					histValues.add(value);
				} else {
					final DefaultCategoryDataset catDataset = (DefaultCategoryDataset) dataset;
					catDataset.addValue(value, title, name);
				}
				this.updateStatus(false);
				this.increaseCompletion(increase);
			}
		}

		if (dataset instanceof HistogramDataset) {
			final double[] data = new double[histValues.size()];
			for (int i = 0; i < histValues.size(); i++) {
				data[i] = histValues.get(i);
			}
			((HistogramDataset) dataset).addSeries(title, data, data.length);
		}

		final CategoryAxis xAxis = new CategoryAxis(
				StatsConstants.GRAPH_LAB_X_TRACK);
		final NumberAxis yAxis = new NumberAxis(this.getYAxisTitle());

		// renderer.setSeriesFillPaint(0, Color.black);
		Plot plot = null;
		JFreeChart chart = null;
		if (dataset instanceof HistogramDataset) {
			final HistogramDataset histDataset = (HistogramDataset) dataset;
			chart = ChartFactory.createHistogram(title, this.getYAxisTitle(),
					StatsConstants.GRAPH_LAB_Y_FREQ, histDataset,
					PlotOrientation.VERTICAL, true, true, true);
			plot = chart.getPlot();
		} else {
			final DefaultCategoryDataset catDataset = (DefaultCategoryDataset) dataset;
			plot = new CategoryPlot(catDataset, xAxis, yAxis, renderer);
			chart = new JFreeChart(title, plot);
		}
		plot.setBackgroundPaint(Color.WHITE);

		final JPanel panel = new ChartPanel(chart);

		// this.graphPanel = new GenericGraphPanel(dataset,
		// GenericGraphPanel.GRAPH_KIND_LINE, title, "Tracks",
		// this.getYAxisTitle(), GenericGraphPanel.PLOT_VERTICAL, true,
		// true, false);

		return panel;
	}

	protected JPanel prepareTimepointsGraph(final int maxT) {
		final String title = this.getTitle();
		Dataset dataset = null;
		if (this.getGraphType() == StatsGraphProducer.HISTOGRAM_GRAPH) {
			dataset = new HistogramDataset();
			((HistogramDataset) dataset).setType(HistogramType.FREQUENCY);
		} else {
			dataset = new DefaultCategoryDataset();
		}
		final double partial = 100.0 / (maxT * this.getSegmentsMap().keySet()
				.size());
		final double increase = new BigDecimal(partial).setScale(2,
				RoundingMode.HALF_UP).doubleValue();
		for (Integer t = 0; t < maxT; t++) {
			final List<Double> histValues = new ArrayList<>();
			for (final OmegaTrajectory track : this.getSegmentsMap().keySet()) {
				final List<OmegaROI> rois = track.getROIs();
				for (final OmegaSegment segment : this.getSegmentsMap().get(
				        track)) {
					final String name = track.getName() + " "
					        + segment.getStartingROI().getFrameIndex() + "-"
					        + segment.getEndingROI().getFrameIndex();
					boolean found = false;
					final int roiStart = rois.indexOf(segment.getStartingROI());
					final int roiEnd = rois.indexOf(segment.getEndingROI());
					final List<OmegaROI> segmentROIs = rois.subList(roiStart,
							roiEnd + 1);
					for (final OmegaROI roi : segmentROIs) {
						if (this.isTerminated())
							return null;
						// TODO FIND A SMARTER WAY TO DO IT HERE
						final Integer timepoint = roi.getFrameIndex();
						if (!timepoint.equals(t)) {
							continue;
						}
						final Double value = this.getValue(segment, roi)[0];
						if (value == null) {
							continue;
						}
						if (dataset instanceof HistogramDataset) {
							histValues.add(value);
						} else {
							final DefaultCategoryDataset catDataset = (DefaultCategoryDataset) dataset;
							catDataset.addValue(value, name, timepoint);
							found = true;
						}
					}
					if (!found) {
						if (dataset instanceof CategoryDataset) {
							final DefaultCategoryDataset catDataset = (DefaultCategoryDataset) dataset;
							catDataset.addValue(null, name, t);
						}
					}
				}
			}

			if (dataset instanceof HistogramDataset) {
				final double[] data = new double[histValues.size()];
				for (int i = 0; i < histValues.size(); i++) {
					data[i] = histValues.get(i) == null ? 0 : histValues.get(i);
				}
				((HistogramDataset) dataset).addSeries(t, data, data.length);
			}

			this.updateStatus(false);
			this.increaseCompletion(increase);
		}

		Map<String, Map<Integer, Boolean>> renderingMap = null;
		if ((dataset instanceof CategoryDataset)
				&& (this.getGraphType() == StatsGraphProducer.LINE_GRAPH)) {
			renderingMap = this
					.createRenderingMap((DefaultCategoryDataset) dataset);
		}
		final CategoryItemRenderer renderer = this
				.getTimepointsRenderer(renderingMap);

		final CategoryAxis xAxis = new CategoryAxis(
				StatsConstants.GRAPH_LAB_X_TIME);
		// xAxis.setTickUnit(new NumberTickUnit(1.0));
		final NumberAxis yAxis = new NumberAxis(this.getYAxisTitle());

		Plot plot = null;
		JFreeChart chart = null;
		if (dataset instanceof HistogramDataset) {
			final HistogramDataset histDataset = (HistogramDataset) dataset;
			chart = ChartFactory.createHistogram(title, this.getYAxisTitle(),
					StatsConstants.GRAPH_LAB_Y_FREQ, histDataset,
					PlotOrientation.VERTICAL, true, true, true);
			plot = chart.getPlot();
		} else {
			final DefaultCategoryDataset catDataset = (DefaultCategoryDataset) dataset;
			plot = new CategoryPlot(catDataset, xAxis, yAxis, renderer);
			chart = new JFreeChart(title, plot);
		}
		plot.setBackgroundPaint(Color.WHITE);

		final JPanel panel = new ChartPanel(chart);

		// this.graphPanel = new GenericGraphPanel(xySeriesCollection,
		// GenericGraphPanel.GRAPH_KIND_LINE, title, "Timepoints",
		// this.getYAxisTitle(), GenericGraphPanel.PLOT_VERTICAL, true,
		// true, false);
		// this.graphPanel.changeColors(drawTracks);

		return panel;
	}

	public JPanel prepareMSDGraph() {
		final String title = StatsConstants.GRAPH_MTC_NAME_MSD;
		final double partial = 100.0 / this.getSegmentsMap().keySet().size();
		final double increase = new BigDecimal(partial).setScale(2,
		        RoundingMode.HALF_UP).doubleValue();
		int counter = 0;
		final Map<OmegaSegment, Integer> segmentSeriesMap = new LinkedHashMap<OmegaSegment, Integer>();
		final XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		for (final OmegaTrajectory track : this.getSegmentsMap().keySet()) {
			for (final OmegaSegment segment : this.getSegmentsMap().get(track)) {
				if (this.isTerminated)
					return null;
				final String serieName = track.getName() + " "
				        + segment.getStartingROI().getFrameIndex() + "-"
				        + segment.getEndingROI().getFrameIndex();
				final XYSeries serie = new XYSeries(serieName, false);
				final Double[] msd = this.getValue(segment, null);
				final Double[] deltaT = this.getValue(segment, null);
				for (int i = 0; i < msd.length; i++) {
					final Double msdVal = msd[i];
					final Double deltaTVal = deltaT[i];
					if ((msdVal != null) && (deltaTVal != null)) {
						serie.add(deltaTVal, msdVal);
					}
				}
				this.updateStatus(false);
				this.completed += increase;
				if (this.completed > 100.0) {
					this.completed = 100.0;
				}
				segmentSeriesMap.put(segment, counter);
				counter++;
				xySeriesCollection.addSeries(serie);
			}
		}

		final NumberAxis xAxis = new NumberAxis(
		        StatsConstants.GRAPH_MTC_LAB_MSD_X);
		final NumberAxis yAxis = new NumberAxis(
		        StatsConstants.GRAPH_MTC_LAB_MSD_Y);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		final XYPlot plot = new XYPlot(xySeriesCollection, xAxis, yAxis,
		        renderer);
		plot.setBackgroundPaint(Color.WHITE);

		for (final OmegaSegment segment : segmentSeriesMap.keySet()) {
			final int index = segmentSeriesMap.get(segment);
			final Color c = this.segmTypes.getSegmentationColor(segment
			        .getSegmentationType());
			renderer.setSeriesPaint(index, c);
		}
		plot.setRenderer(renderer);

		final JFreeChart chart = new JFreeChart(title, plot);

		final JPanel panel = new ChartPanel(chart);

		return panel;
		// this.chartPanels[1] = new GenericGraphPanel(dataset,
		// GenericGraphPanel.GRAPH_KIND_LINE, title, "Log delta T",
		// "Log linear MSD", GenericGraphPanel.PLOT_VERTICAL, true, true,
		// false);
	}

	public JPanel prepareMSSGraph() {
		final String title = StatsConstants.GRAPH_MTC_NAME_MSS;
		final double partial = 100.0 / this.getSegmentsMap().keySet().size();
		final double increase = new BigDecimal(partial).setScale(2,
		        RoundingMode.HALF_UP).doubleValue();
		int counter = 0;
		final Map<OmegaSegment, Integer> segmentSeriesMap = new LinkedHashMap<OmegaSegment, Integer>();
		final XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		final XYSeriesCollection boundsCollection = new XYSeriesCollection();
		final XYSeries upperbound = new XYSeries("Upperbound");
		final XYSeries lowerbound = new XYSeries("Lowerbound");
		for (final OmegaTrajectory track : this.getSegmentsMap().keySet()) {
			for (final OmegaSegment segment : this.getSegmentsMap().get(track)) {
				if (this.isTerminated)
					return null;
				final String serieName = title + " "
						+ segment.getStartingROI().getFrameIndex() + "-"
						+ segment.getEndingROI().getFrameIndex();
				final XYSeries serie = new XYSeries(serieName, false);
				final Double[] gamma = this.getValue(segment, null);
				if (gamma == null) {
					continue;
				}
				final Double[] ny = this.getValue(segment, null);
				if (ny == null) {
					continue;
				}
				for (int i = 0; i < gamma.length; i++) {
					if (this.isTerminated)
						return null;
					final Double gammaVal = gamma[i];
					final Double nyVal = ny[i];
					serie.add(nyVal, gammaVal);
				}

				if (!upperbound.isEmpty() || !lowerbound.isEmpty()) {
					continue;
				}
				for (int i = 0; i < gamma.length; i++) {
					final Double nyVal = ny[i];
					final Double ub = 1 * nyVal;
					final Double lb = 0.5 * nyVal;
					upperbound.add(nyVal, ub);
					lowerbound.add(nyVal, lb);
				}
				this.updateStatus(false);
				this.completed += increase;
				if (this.completed > 100.0) {
					this.completed = 100.0;
				}
				segmentSeriesMap.put(segment, counter);
				counter++;
				xySeriesCollection.addSeries(serie);
			}
		}
		boundsCollection.addSeries(upperbound);
		boundsCollection.addSeries(lowerbound);

		final NumberAxis xAxis = new NumberAxis(
		        StatsConstants.GRAPH_MTC_LAB_MSS_X);
		final NumberAxis yAxis = new NumberAxis(
		        StatsConstants.GRAPH_MTC_LAB_MSS_Y);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		final XYPlot plot = new XYPlot(xySeriesCollection, xAxis, yAxis,
		        renderer);
		plot.setBackgroundPaint(Color.WHITE);

		for (final OmegaSegment segment : segmentSeriesMap.keySet()) {
			final int index = segmentSeriesMap.get(segment);
			final Color c = this.segmTypes.getSegmentationColor(segment
					.getSegmentationType());
			renderer.setSeriesPaint(index, c);
		}
		plot.setRenderer(renderer);

		final XYDifferenceRenderer diffRenderer = new XYDifferenceRenderer(
		        Color.lightGray, Color.white, false);

		diffRenderer.setSeriesStroke(0, new BasicStroke(1.0f,
		        BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f,
		        new float[] { 5.0f, 5.0f }, 0.0f));
		diffRenderer.setSeriesStroke(1, new BasicStroke(1.0f,
		        BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f,
		        new float[] { 5.0f, 5.0f }, 0.0f));

		plot.setDataset(1, boundsCollection);
		plot.setRenderer(1, diffRenderer);

		final JFreeChart chart = new JFreeChart(title, plot);

		final JPanel panel = new ChartPanel(chart);

		// this.chartPanels[2] = new GenericGraphPanel(dataset,
		// GenericGraphPanel.GRAPH_KIND_LINE, title, "Order moment",
		// "Gamma", GenericGraphPanel.PLOT_VERTICAL, true, true, false);
		return panel;
	}

	public JPanel prepareSMSSvsDGraph() {
		final String title = StatsConstants.GRAPH_MTC_NAME_SMSS_D;
		// final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		final double partial = 100.0 / this.getSegmentsMap().keySet().size();
		final double increase = new BigDecimal(partial).setScale(2,
		        RoundingMode.HALF_UP).doubleValue();
		int counter = 0;
		final Map<OmegaSegment, Integer> segmentSeriesMap = new LinkedHashMap<OmegaSegment, Integer>();
		final XYIntervalSeriesCollection xySeriesCollection = new XYIntervalSeriesCollection();
		for (final OmegaTrajectory track : this.getSegmentsMap().keySet()) {
			for (final OmegaSegment segment : this.getSegmentsMap().get(track)) {
				if (this.isTerminated)
					return null;
				final String serieName = track.getName() + " "
				        + segment.getStartingROI().getFrameIndex() + "-"
				        + segment.getEndingROI().getFrameIndex();
				final XYIntervalSeries serie = new XYIntervalSeries(serieName);
				final Double[] values = this.getValue(segment, null);
				if (values == null) {
					continue;
				}
				final Double d = values[0];
				final Double smss = values[1];
				final double errorD = values[2];
				final double errorSMSS = values[3];
				System.out.println(track.getName() + " - " + errorD + " - "
				        + errorSMSS);
				final double dMinus = d - errorD;
				final double dPlus = d + errorD;
				final double smssMinus = smss - errorSMSS;
				final double smssPlus = smss + errorSMSS;
				serie.add(d, dMinus, dPlus, smss, smssMinus, smssPlus);
				this.updateStatus(false);
				this.completed += increase;
				if (this.completed > 100.0) {
					this.completed = 100.0;
				}
				segmentSeriesMap.put(segment, counter);
				counter++;
				xySeriesCollection.addSeries(serie);
			}
		}

		// dataset2.addSeries(series);
		// dataset.addValue(dVal, title, smssVal);

		// X axis NumberAxis or LogarithmicAxis
		final NumberAxis numberaxisX = new NumberAxis(
		        StatsConstants.GRAPH_MTC_LAB_SMSS_D_X);
		// numberaxisX.setTickUnit(new NumberTickUnit(0.1));
		final NumberAxis numberaxisY = new NumberAxis(
		        StatsConstants.GRAPH_MTC_LAB_SMSS_D_Y);
		numberaxisY.setRange(0.0, 1.0);
		// numberaxisY.setTickUnit(new NumberTickUnit(0.1));

		// error bars customization
		final XYErrorRenderer renderer = new XYErrorRenderer();
		// renderer.setErrorPaint(Color.black);
		renderer.setErrorStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
		        BasicStroke.JOIN_MITER, 1.0f, new float[] { 5.0f, 5.0f }, 0.0f));
		// }
		final XYPlot plot = new XYPlot(xySeriesCollection, numberaxisX,
		        numberaxisY, renderer);
		plot.setBackgroundPaint(Color.WHITE);

		// series
		final Shape cross = ShapeUtilities.createDiagonalCross(1, 1);

		// series shape
		// for (int i = 0; i < series.le; i++) {
		renderer.setSeriesLinesVisible(0, false);
		renderer.setSeriesShapesVisible(0, true);
		renderer.setSeriesShape(0, cross);
		// }

		// for (int i = 0; i < this.series.length; i++) {
		for (final OmegaSegment segment : segmentSeriesMap.keySet()) {
			final int index = segmentSeriesMap.get(segment);
			final Color c = this.segmTypes.getSegmentationColor(segment
			        .getSegmentationType());
			renderer.setSeriesPaint(index, c);
		}
		plot.setRenderer(renderer);

		final Marker marker = new ValueMarker(0.5);
		marker.setPaint(Color.BLACK);
		marker.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
		        BasicStroke.JOIN_MITER, 1.0f, new float[] { 5.0f, 5.0f }, 0.0f));
		plot.addRangeMarker(marker);

		final JFreeChart chart = new JFreeChart(title, plot);

		final JPanel panel = new ChartPanel(chart);

		return panel;
		// this.chartPanels[3] = new GenericGraphPanel(xySeriesCollection,
		// GenericGraphPanel.GRAPH_KIND_SCATTER, title, "D", "SMSS",
		// GenericGraphPanel.PLOT_VERTICAL, true, true, false);
	}

	public JPanel prepareTrackGraph() {
		final String title = StatsConstants.GRAPH_MTC_NAME_TRACK;
		final double partial = 100.0 / this.getSegmentsMap().keySet().size();
		final double increase = new BigDecimal(partial).setScale(2,
		        RoundingMode.HALF_UP).doubleValue();
		Double maxX = 0.0, maxY = 0.0, minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
		int counter = 0;
		final Map<OmegaSegment, Integer> segmentSeriesMap = new LinkedHashMap<OmegaSegment, Integer>();
		final XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		for (final OmegaTrajectory track : this.getSegmentsMap().keySet()) {
			final List<OmegaROI> rois = track.getROIs();
			Double prevX = null, prevY = null;
			Double x = 0.0, y = 0.0;
			for (final OmegaSegment segment : this.getSegmentsMap().get(track)) {
				if (this.isTerminated)
					return null;
				final String serieName = track.getName() + " "
						+ segment.getStartingROI().getFrameIndex() + "-"
						+ segment.getEndingROI().getFrameIndex();
				final XYSeries serie = new XYSeries(serieName, false);
				final int roiStart = rois.indexOf(segment.getStartingROI());
				final int roiEnd = rois.indexOf(segment.getEndingROI());
				final List<OmegaROI> segmentROIs = rois.subList(roiStart,
						roiEnd + 1);
				for (final OmegaROI roi : segmentROIs) {
					if (this.isTerminated)
						return null;
					final Double roiX = roi.getX();
					final Double roiY = roi.getY();
					if (prevX != null) {
						x += roiX - prevX;
					}
					if (prevY != null) {
						y -= roiY - prevY;
					}
					if (maxX < x) {
						maxX = x;
					}
					if (maxY < y) {
						maxY = y;
					}
					if (minX > x) {
						minX = x;
					}
					if (minY > y) {
						minY = y;
					}
					prevX = roiX;
					prevY = roiY;
					serie.add(x, y);
				}
				this.updateStatus(false);
				this.completed += increase;
				if (this.completed > 100.0) {
					this.completed = 100.0;
				}
				segmentSeriesMap.put(segment, counter);
				counter++;
				xySeriesCollection.addSeries(serie);
			}
		}

		Double max = 0.0, min = 0.0;
		maxX = StrictMath.abs(maxX);
		maxY = StrictMath.abs(maxY);
		minX = StrictMath.abs(minX);
		minY = StrictMath.abs(minY);
		if (maxX >= maxY) {
			max = maxX;
		} else {
			max = maxY;
		}
		if (minX <= minY) {
			min = minX;
		} else {
			min = minY;
		}
		min -= 2.5;
		max += 2.5;

		final NumberAxis xAxis = new NumberAxis(
		        StatsConstants.GRAPH_MTC_LAB_TRACK_X);
		xAxis.setRange(min, max);
		final NumberAxis yAxis = new NumberAxis(
		        StatsConstants.GRAPH_MTC_LAB_TRACK_Y);
		yAxis.setRange(min, max);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		final XYPlot plot = new XYPlot(xySeriesCollection, xAxis, yAxis,
		        renderer);
		plot.setBackgroundPaint(Color.WHITE);

		for (final OmegaSegment segment : segmentSeriesMap.keySet()) {
			final int index = segmentSeriesMap.get(segment);
			final Color c = this.segmTypes.getSegmentationColor(segment
					.getSegmentationType());
			renderer.setSeriesPaint(index, c);
		}
		plot.setRenderer(renderer);

		final JFreeChart chart = new JFreeChart(title, plot);

		final JPanel panel = new ChartPanel(chart);

		return panel;
	}

	public void increaseCompletion(final double increase) {
		this.completed += increase;
		if (this.completed > 100.0) {
			this.completed = 100.0;
		}
	}

	public Map<OmegaTrajectory, List<OmegaSegment>> getSegmentsMap() {
		return this.segmentsMap;
	}

	protected void setCompleted(final double completed) {
		this.completed = completed;
	}

	public double getCompleted() {
		return this.completed;
	}

	public boolean isTerminated() {
		return this.isTerminated;
	}

	public void terminate() {
		this.isTerminated = true;
	}

	public int getGraphType() {
		return this.graphType;
	}
}
