package edu.umassmed.omega.commons.runnable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.gui.interfaces.OmegaMessageDisplayerPanelInterface;
import edu.umassmed.omega.commons.libraries.OmegaVelocityLibrary;

public class OmegaVelocityAnalyzer implements Runnable {

	private OmegaMessageDisplayerPanelInterface displayerPanel;

	private final int tMax;
	private final Map<OmegaTrajectory, List<OmegaSegment>> segments;
	private final Map<OmegaSegment, List<Double>> localSpeedMap;
	private final Map<OmegaSegment, List<Double>> localVelocityMap;
	private final Map<OmegaSegment, Double> meanSpeedMap;
	private final Map<OmegaSegment, Double> meanVelocityMap;

	public OmegaVelocityAnalyzer(final int tMax,
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		this(null, tMax, segments);
	}

	public OmegaVelocityAnalyzer(
			final OmegaMessageDisplayerPanelInterface displayerPanel,
	        final int tMax,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		this.displayerPanel = displayerPanel;

		this.tMax = tMax;
		this.segments = new LinkedHashMap<OmegaTrajectory, List<OmegaSegment>>(
		        segments);
		this.localSpeedMap = new LinkedHashMap<>();
		this.localVelocityMap = new LinkedHashMap<>();
		this.meanSpeedMap = new LinkedHashMap<>();
		this.meanVelocityMap = new LinkedHashMap<>();
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
				        "Processing velocity analysis, trajectory "
				                + track.getName() + " " + counter + "/" + max,
				        false, false);
			}
			for (final OmegaSegment segment : this.segments.get(track)) {
				final int roiStart = rois.indexOf(segment.getStartingROI());
				final int roiEnd = rois.indexOf(segment.getEndingROI());
				final List<OmegaROI> segmentROIs = rois.subList(roiStart,
				        roiEnd + 1);
				final List<Double> localSpeeds = new ArrayList<>();
				final List<Double> localVelocities = new ArrayList<>();
				for (int t = 0; t < this.tMax; t++) {
					final Double localSpeed = OmegaVelocityLibrary
					        .computeLocalSpeed(segmentROIs, t);
					final Double localVelocity = OmegaVelocityLibrary
					        .computeLocalVelocity(segmentROIs, t);
					localSpeeds.add(localSpeed);
					localVelocities.add(localVelocity);
				}
				this.localSpeedMap.put(segment, localSpeeds);
				this.localVelocityMap.put(segment, localVelocities);

				final Double meanSpeed = OmegaVelocityLibrary
				        .computeMeanSpeed(segmentROIs);
				final Double meanVelocity = OmegaVelocityLibrary
				        .computeMeanVelocity(segmentROIs);
				this.meanSpeedMap.put(segment, meanSpeed);
				this.meanVelocityMap.put(segment, meanVelocity);
				counter++;
			}
		}
		if (this.displayerPanel != null) {
			this.updateStatusAsync("Processing velocity analysis ended", true,
					false);
		}
	}

	public Map<OmegaTrajectory, List<OmegaSegment>> getSegments() {
		return this.segments;
	}

	public Map<OmegaSegment, List<Double>> getLocalSpeedResults() {
		return this.localSpeedMap;
	}

	public Map<OmegaSegment, List<Double>> getLocalVelocityResults() {
		return this.localVelocityMap;
	}

	public Map<OmegaSegment, Double> getMeanSpeedResults() {
		return this.meanSpeedMap;
	}

	public Map<OmegaSegment, Double> getMeanVelocityResults() {
		return this.meanVelocityMap;
	}

	private void updateStatusSync(final String msg, final boolean ended,
			final boolean dialog) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					OmegaVelocityAnalyzer.this.displayerPanel
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
				OmegaVelocityAnalyzer.this.displayerPanel
				.updateMessageStatus(new AnalyzerEvent(msg, ended,
						dialog));
			}
		});
	}
}
