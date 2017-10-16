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
import edu.umassmed.omega.commons.libraries.OmegaVelocityLibrary;

public class OmegaVelocityAnalyzer implements Runnable {
	
	private final OmegaMessageDisplayerPanelInterface displayerPanel;
	
	private final int tMax;
	private final Double physicalT;
	private final Map<OmegaTrajectory, List<OmegaSegment>> segments;
	private final Map<OmegaSegment, List<Double>> localSpeedMap;
	private final Map<OmegaSegment, List<Double>> localSpeedFromOriginMap;
	private final Map<OmegaSegment, List<Double>> localVelocityFromOriginMap;
	private final Map<OmegaSegment, Double> averagerCurvilinearSpeedMap;
	private final Map<OmegaSegment, Double> averageStraightLineVelocityMap;
	private final Map<OmegaSegment, Double> forwardProgressionLinearityMap;

	private final OmegaTrajectoriesSegmentationRun segmRun;
	private final List<OmegaElement> selections;
	
	public OmegaVelocityAnalyzer(final double physicalT, final int tMax,
			final OmegaTrajectoriesSegmentationRun segmRun,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		this(null, physicalT, tMax, segmRun, segments, null);
	}
	
	public OmegaVelocityAnalyzer(
			final OmegaMessageDisplayerPanelInterface displayerPanel,
			final double physicalT, final int tMax,
			final OmegaTrajectoriesSegmentationRun segmRun,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final List<OmegaElement> selections) {
		this.displayerPanel = displayerPanel;
		
		this.segmRun = segmRun;
		this.selections = selections;
		
		this.tMax = tMax;
		this.physicalT = physicalT;
		this.segments = new LinkedHashMap<OmegaTrajectory, List<OmegaSegment>>(
				segments);
		this.localSpeedMap = new LinkedHashMap<OmegaSegment, List<Double>>();
		this.localSpeedFromOriginMap = new LinkedHashMap<OmegaSegment, List<Double>>();
		this.localVelocityFromOriginMap = new LinkedHashMap<OmegaSegment, List<Double>>();
		this.averagerCurvilinearSpeedMap = new LinkedHashMap<OmegaSegment, Double>();
		this.averageStraightLineVelocityMap = new LinkedHashMap<OmegaSegment, Double>();
		this.forwardProgressionLinearityMap = new LinkedHashMap<OmegaSegment, Double>();
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
				final List<Double> localSpeedsFromOrigin = new ArrayList<>();
				final List<Double> localVelocitiesFromOrigin = new ArrayList<>();
				for (int t = 1; t <= this.tMax; t++) {
					final Double localSpeed = OmegaVelocityLibrary
							.computeLocalSpeed(this.physicalT, segmentROIs, t);
					final Double localSpeedFromOrigin = OmegaVelocityLibrary
							.computeLocalSpeedFromOrigin(this.physicalT,
									segmentROIs, t);
					final Double localVelocityFromOrigin = OmegaVelocityLibrary
							.computeLocalVelocity(this.physicalT, segmentROIs,
									t);
					localSpeeds.add(localSpeed);
					localSpeedsFromOrigin.add(localSpeedFromOrigin);
					localVelocitiesFromOrigin.add(localVelocityFromOrigin);
				}
				this.localSpeedMap.put(segment, localSpeeds);
				this.localSpeedFromOriginMap
						.put(segment, localSpeedsFromOrigin);
				this.localVelocityFromOriginMap.put(segment,
						localVelocitiesFromOrigin);
				
				final Double averagerCurvilinearSpeed = OmegaVelocityLibrary
						.computeAveragerCurvilinearSpeed(this.physicalT,
								segmentROIs);
				final Double averageStraightLineVelocity = OmegaVelocityLibrary
						.computeAverageStraightLineVelocity(this.physicalT,
								segmentROIs);
				final Double forwardProgressionLinearity = averageStraightLineVelocity
						/ averagerCurvilinearSpeed;
				this.averagerCurvilinearSpeedMap.put(segment,
						averagerCurvilinearSpeed);
				this.averageStraightLineVelocityMap.put(segment,
						averageStraightLineVelocity);
				this.forwardProgressionLinearityMap.put(segment,
						forwardProgressionLinearity);
				counter++;
			}
		}
		if (this.displayerPanel != null) {
			this.updateStatusAsync("Processing velocity analysis ended", true,
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
	
	public Map<OmegaSegment, List<Double>> getLocalSpeedResults() {
		return this.localSpeedMap;
	}
	
	public Map<OmegaSegment, List<Double>> getLocalSpeedFromOriginResults() {
		return this.localSpeedFromOriginMap;
	}
	
	public Map<OmegaSegment, List<Double>> getLocalVelocityFromOriginResults() {
		return this.localVelocityFromOriginMap;
	}
	
	public Map<OmegaSegment, Double> getAverageCurvilinearSpeedResults() {
		return this.averagerCurvilinearSpeedMap;
	}
	
	public Map<OmegaSegment, Double> getAverageStraightLineVelocityResults() {
		return this.averageStraightLineVelocityMap;
	}
	
	public Map<OmegaSegment, Double> getForwardProgressionLinearityResults() {
		return this.forwardProgressionLinearityMap;
	}
	
	private void updateStatusSync(final String msg, final boolean ended,
			final boolean dialog) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					if (OmegaVelocityAnalyzer.this.displayerPanel == null)
						return;
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
				if (OmegaVelocityAnalyzer.this.displayerPanel == null)
					return;
				OmegaVelocityAnalyzer.this.displayerPanel
						.updateMessageStatus(new AnalyzerEvent(msg, ended,
								dialog));
			}
		});
	}
}
