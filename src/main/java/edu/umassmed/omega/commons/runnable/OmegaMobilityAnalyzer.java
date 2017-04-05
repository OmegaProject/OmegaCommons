package edu.umassmed.omega.commons.runnable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrajectoriesSegmentationRun;
import edu.umassmed.omega.commons.data.coreElements.OmegaElement;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.gui.interfaces.OmegaMessageDisplayerPanelInterface;
import edu.umassmed.omega.commons.libraries.OmegaMobilityLibrary;

public class OmegaMobilityAnalyzer implements Runnable {

	private final OmegaMessageDisplayerPanelInterface displayerPanel;

	private final int tMax;
	private final Double physicalT;
	private final Map<OmegaTrajectory, List<OmegaSegment>> segments;
	private final Map<OmegaSegment, List<Double>> distancesMap;
	private final Map<OmegaSegment, List<Double>> distancesFromOriginMap;
	private final Map<OmegaSegment, List<Double>> displacementsFromOriginMap;
	private final Map<OmegaSegment, Double> maxDisplacementesFromOriginMap;
	private final Map<OmegaSegment, List<Double>> timeTraveledMap;
	private final Map<OmegaSegment, List<Double>> confinementRatioMap;
	private final Map<OmegaSegment, List<Double[]>> anglesAndDirectionalChangesMap;
	
	private final OmegaTrajectoriesSegmentationRun segmRun;
	private final List<OmegaElement> selections;

	public OmegaMobilityAnalyzer(final double physicalT, final int tMax,
			final OmegaTrajectoriesSegmentationRun segmRun,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		this(null, physicalT, tMax, segmRun, segments, null);
	}

	public OmegaMobilityAnalyzer(
			final OmegaMessageDisplayerPanelInterface displayerPanel,
			final double physicalT, final int tMax,
			final OmegaTrajectoriesSegmentationRun segmRun,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final List<OmegaElement> selections) {
		
		this.displayerPanel = displayerPanel;
		this.physicalT = physicalT;
		this.tMax = tMax;
		
		this.segmRun = segmRun;
		this.selections = selections;

		this.segments = new LinkedHashMap<OmegaTrajectory, List<OmegaSegment>>(
				segments);
		this.distancesMap = new LinkedHashMap<OmegaSegment, List<Double>>();
		this.distancesFromOriginMap = new LinkedHashMap<OmegaSegment, List<Double>>();
		this.displacementsFromOriginMap = new LinkedHashMap<OmegaSegment, List<Double>>();
		this.maxDisplacementesFromOriginMap = new LinkedHashMap<OmegaSegment, Double>();
		this.timeTraveledMap = new LinkedHashMap<OmegaSegment, List<Double>>();
		this.confinementRatioMap = new LinkedHashMap<OmegaSegment, List<Double>>();
		this.anglesAndDirectionalChangesMap = new LinkedHashMap<OmegaSegment, List<Double[]>>();
	}

	@Override
	public void run() {
		int counter = 1;
		final int max = this.segments.keySet().size();
		for (final OmegaTrajectory track : this.segments.keySet()) {
			final List<OmegaROI> rois = track.getROIs();
			if (this.displayerPanel != null) {
				this.updateStatusAsync(
						"Processing mobility analysis, trajectory "
								+ track.getName() + " " + counter + "/" + max,
						false, false);
			}
			for (final OmegaSegment segment : this.segments.get(track)) {
				double maxDisp = 0.0;
				int timeTrav = 0;
				final List<Double> distances = new ArrayList<Double>();
				final List<Double> distancesFromOrigin = new ArrayList<Double>();
				final List<Double> displacementsFromOrigin = new ArrayList<Double>();
				final List<Double> confinementRatios = new ArrayList<Double>();
				final List<Double[]> anglesAndDirectionalChanges = new ArrayList<Double[]>();
				final List<Double> timeTraveled = new ArrayList<Double>();
				boolean counting = false;
				Double prevAngle = null;
				// final int startFrameIndex = segment.getStartingROI()
				// .getFrameIndex();
				// final int endFrameIndex = segment.getEndingROI()
				// .getFrameIndex();
				final int startROI = rois.indexOf(segment.getStartingROI());
				final int endROI = rois.indexOf(segment.getEndingROI());
				final List<OmegaROI> segmentROIs = rois.subList(startROI,
						endROI + 1);
				for (int t = 1; t <= this.tMax; t++) {
					final Double distance = OmegaMobilityLibrary
							.computeDistanceTraveled(segmentROIs, t);
					final Double distanceFromOrigin = OmegaMobilityLibrary
							.computeTotalDistanceTraveled(segmentROIs, t);
					final Double displacementFromOrigin = OmegaMobilityLibrary
							.computeTotalNetDisplacement(segmentROIs, t);

					final Double[] angleAndDirectionalChange = OmegaMobilityLibrary
							.computeDirectionalChange(segmentROIs, prevAngle, t);
					prevAngle = angleAndDirectionalChange[0];

					Double confinementRatio = null;
					if (displacementFromOrigin != null) {
						confinementRatio = displacementFromOrigin
								/ distanceFromOrigin;
						if (!counting) {
							counting = true;
						}
						if (maxDisp < displacementFromOrigin) {
							maxDisp = displacementFromOrigin;
						}
					}
					if (counting && (displacementFromOrigin == null)) {
						final int maxT = segmentROIs
								.get(segmentROIs.size() - 1).getFrameIndex();
						if (t > maxT) {
							counting = false;
						}
					}
					if (counting) {
						timeTrav++;
					}
					final Double localTimeTraveled = timeTrav * this.physicalT;
					distances.add(distance);
					distancesFromOrigin.add(distanceFromOrigin);
					displacementsFromOrigin.add(displacementFromOrigin);
					confinementRatios.add(confinementRatio);
					anglesAndDirectionalChanges.add(angleAndDirectionalChange);
					timeTraveled.add(localTimeTraveled);
				}
				this.distancesMap.put(segment, distances);
				this.distancesFromOriginMap.put(segment, distancesFromOrigin);
				this.displacementsFromOriginMap.put(segment,
						displacementsFromOrigin);
				this.maxDisplacementesFromOriginMap.put(segment, maxDisp);
				this.timeTraveledMap.put(segment, timeTraveled);
				this.confinementRatioMap.put(segment, confinementRatios);
				this.anglesAndDirectionalChangesMap.put(segment,
						anglesAndDirectionalChanges);
				counter++;
			}
		}
		if (this.displayerPanel != null) {
			this.updateStatusAsync("Processing mobility analysis ended", true,
					false);
		}
	}

	public List<OmegaElement> getSelections() {
		return this.selections;
	}
	
	public OmegaTrajectoriesSegmentationRun getTrajectorySegmentationRun() {
		return this.segmRun;
	}

	public Map<OmegaTrajectory, List<OmegaSegment>> getSegments() {
		return this.segments;
	}

	public Map<OmegaSegment, List<Double>> getDistancesResults() {
		return this.distancesMap;
	}

	public Map<OmegaSegment, List<Double>> getDistancesFromOriginResults() {
		return this.distancesFromOriginMap;
	}

	public Map<OmegaSegment, List<Double>> getDisplacementsFromOriginResults() {
		return this.displacementsFromOriginMap;
	}

	public Map<OmegaSegment, Double> getMaxDisplacementsFromOriginResults() {
		return this.maxDisplacementesFromOriginMap;
	}

	public Map<OmegaSegment, List<Double>> getTotalTimeTraveledResults() {
		return this.timeTraveledMap;
	}

	public Map<OmegaSegment, List<Double>> getConfinementRatioResults() {
		return this.confinementRatioMap;
	}

	public Map<OmegaSegment, List<Double[]>> getAnglesAndDirectionalChangesResults() {
		return this.anglesAndDirectionalChangesMap;
	}

	private void updateStatusSync(final String msg, final boolean ended,
			final boolean dialog) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					OmegaMobilityAnalyzer.this.displayerPanel
							.updateMessageStatus(new AnalyzerEvent(msg, ended,
									dialog));
				}
			});
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void updateStatusAsync(final String msg, final boolean ended,
			final boolean dialog) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				OmegaMobilityAnalyzer.this.displayerPanel
				.updateMessageStatus(new AnalyzerEvent(msg, ended,
						dialog));
			}
		});
	}
}
