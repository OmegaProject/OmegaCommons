package main.java.edu.umassmed.omega.commons.runnable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import main.java.edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import main.java.edu.umassmed.omega.commons.gui.interfaces.OmegaMessageDisplayerPanelInterface;
import main.java.edu.umassmed.omega.commons.libraries.OmegaMobilityLibrary;

public class OmegaMobilityAnalyzer implements Runnable {

	private OmegaMessageDisplayerPanelInterface displayerPanel;

	private final int tMax;
	private final Map<OmegaTrajectory, List<OmegaSegment>> segments;
	private final Map<OmegaSegment, List<Double>> distancesMap;
	private final Map<OmegaSegment, List<Double>> displacementsMap;
	private final Map<OmegaSegment, Double> maxDisplacementesMap;
	private final Map<OmegaSegment, Integer> totalTimeTraveledMap;
	private final Map<OmegaSegment, List<Double>> confinementRatioMap;
	private final Map<OmegaSegment, List<Double[]>> anglesAndDirectionalChangesMap;

	public OmegaMobilityAnalyzer(final int tMax,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		this(null, tMax, segments);
	}

	public OmegaMobilityAnalyzer(
			final OmegaMessageDisplayerPanelInterface displayerPanel,
			final int tMax,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments) {

		this.displayerPanel = displayerPanel;
		this.tMax = tMax;
		this.segments = new LinkedHashMap<OmegaTrajectory, List<OmegaSegment>>(
				segments);
		this.distancesMap = new LinkedHashMap<>();
		this.displacementsMap = new LinkedHashMap<>();
		this.maxDisplacementesMap = new LinkedHashMap<>();
		this.totalTimeTraveledMap = new LinkedHashMap<>();
		this.confinementRatioMap = new LinkedHashMap<>();
		this.anglesAndDirectionalChangesMap = new LinkedHashMap<>();
		this.displayerPanel = null;
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
				int totalTimeTraveled = 0;
				final List<Double> distances = new ArrayList<>();
				final List<Double> displacements = new ArrayList<>();
				final List<Double> confinementRatios = new ArrayList<>();
				final List<Double[]> anglesAndDirectionalChanges = new ArrayList<>();
				boolean counting = false;
				Double prevAngle = null;
				final int startFrameIndex = segment.getStartingROI()
				        .getFrameIndex();
				final int endFrameIndex = segment.getEndingROI()
				        .getFrameIndex();
				final int startROI = rois.indexOf(segment.getStartingROI());
				final int endROI = rois.indexOf(segment.getEndingROI());
				final List<OmegaROI> segmentROIs = rois.subList(startROI,
				        endROI + 1);
				for (int t = startFrameIndex; t < endFrameIndex; t++) {
					final Double distance = OmegaMobilityLibrary
							.computeTotalDistanceTraveled(segmentROIs, t);
					final Double displacement = OmegaMobilityLibrary
							.computeTotalNetDisplacement(segmentROIs, t);

					final Double[] angleAndDirectionalChange = OmegaMobilityLibrary
							.computeDirectionalChange(segmentROIs, prevAngle, t);
					prevAngle = angleAndDirectionalChange[0];

					Double confinementRatio = null;
					if (displacement != null) {
						confinementRatio = displacement / distance;
						if (!counting) {
							counting = true;
						}
						if (maxDisp < displacement) {
							maxDisp = displacement;
						}
					}
					if (counting && (displacement == null)) {
						final int maxT = segmentROIs
						        .get(segmentROIs.size() - 1).getFrameIndex();
						if (t > maxT) {
							counting = false;
						}
					}
					if (counting) {
						totalTimeTraveled++;
					}
					distances.add(distance);
					displacements.add(displacement);
					confinementRatios.add(confinementRatio);
					anglesAndDirectionalChanges.add(angleAndDirectionalChange);
				}
				this.distancesMap.put(segment, distances);
				this.displacementsMap.put(segment, displacements);
				this.maxDisplacementesMap.put(segment, maxDisp);
				this.totalTimeTraveledMap.put(segment, totalTimeTraveled);
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

	public Map<OmegaTrajectory, List<OmegaSegment>> getSegments() {
		return this.segments;
	}

	public Map<OmegaSegment, List<Double>> getDistancesResults() {
		return this.distancesMap;
	}

	public Map<OmegaSegment, List<Double>> getDisplacementsResults() {
		return this.displacementsMap;
	}

	public Map<OmegaSegment, Double> getMaxDisplacementsResults() {
		return this.maxDisplacementesMap;
	}

	public Map<OmegaSegment, Integer> getTotalTimeTraveledResults() {
		return this.totalTimeTraveledMap;
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
