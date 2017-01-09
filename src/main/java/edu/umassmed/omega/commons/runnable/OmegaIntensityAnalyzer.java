package edu.umassmed.omega.commons.runnable;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import edu.umassmed.omega.commons.data.trajectoryElements.OmegaParticle;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.gui.interfaces.OmegaMessageDisplayerPanelInterface;

public class OmegaIntensityAnalyzer implements Runnable {
	
	private OmegaMessageDisplayerPanelInterface displayerPanel;
	
	private final Map<OmegaTrajectory, List<OmegaSegment>> segments;
	private final Map<OmegaSegment, Double[]> peakSignalsMap;
	private final Map<OmegaSegment, Double[]> meanSignalsMap;
	private final Map<OmegaSegment, Double[]> centroidSignalsMap;
	
	//
	// private OmegaSNRRun snrRun;
	
	public OmegaIntensityAnalyzer(
	        final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		this(null, segments);
	}
	
	public OmegaIntensityAnalyzer(
			final OmegaMessageDisplayerPanelInterface displayerPanel,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		this.displayerPanel = displayerPanel;
		
		this.segments = new LinkedHashMap<OmegaTrajectory, List<OmegaSegment>>(
				segments);
		this.peakSignalsMap = new LinkedHashMap<OmegaSegment, Double[]>();
		this.meanSignalsMap = new LinkedHashMap<OmegaSegment, Double[]>();
		this.centroidSignalsMap = new LinkedHashMap<OmegaSegment, Double[]>();

		this.displayerPanel = null;
	}

	// public void setSNRRun(final OmegaSNRRun snrRun) {
	// this.snrRun = snrRun;
	// }
	
	private void resetArray(final Double[] array) {
		array[0] = Double.MAX_VALUE;
		array[1] = 0.0;
		array[2] = Double.MIN_VALUE;
	}
	
	@Override
	public void run() {
		int counter = 1;
		final int max = this.segments.keySet().size();
		for (final OmegaTrajectory track : this.segments.keySet()) {
			final List<OmegaROI> rois = track.getROIs();
			if (this.displayerPanel != null) {
				this.updateStatusAsync(
				        "Processing intensity analysis, trajectory "
				                + track.getName() + " " + counter + "/" + max,
				        false, false);
			}
			for (final OmegaSegment segment : this.segments.get(track)) {
				final Double[] peaks = new Double[3];
				final Double[] means = new Double[3];
				final Double[] centroids = new Double[3];
				// final Double[] bgs = new Double[3];
				// final Double[] snrs = new Double[3];
				this.resetArray(peaks);
				this.resetArray(means);
				this.resetArray(centroids);
				final int roiStart = rois.indexOf(segment.getStartingROI());
				final int roiEnd = rois.indexOf(segment.getEndingROI());
				final List<OmegaROI> segmentROIs = rois.subList(roiStart,
				        roiEnd + 1);
				for (final OmegaROI roi : segmentROIs) {
					// for (final OmegaROI roi : track.getROIs()) {
					final OmegaParticle particle = (OmegaParticle) roi;
					
					final Double centroidSignal = particle
							.getCentroidIntensity();
					final Double peakSignal = particle.getPeakIntensity();
					
					final Double meanSignal = (peakSignal + centroidSignal) / 2;

					peaks[1] += peakSignal;
					means[1] += meanSignal;
					centroids[1] += centroidSignal;
					if (peaks[0] > peakSignal) {
						peaks[0] = peakSignal;
					}
					if (peaks[2] < peakSignal) {
						peaks[2] = peakSignal;
					}
					if (means[0] > meanSignal) {
						means[0] = meanSignal;
					}
					if (means[2] < meanSignal) {
						means[2] = meanSignal;
					}
					if (centroids[0] > centroidSignal) {
						centroids[0] = centroidSignal;
					}
					if (centroids[2] < centroidSignal) {
						centroids[2] = centroidSignal;
					}
				}
				peaks[1] /= track.getLength();
				means[1] /= track.getLength();
				centroids[1] /= track.getLength();
				this.peakSignalsMap.put(segment, peaks);
				this.meanSignalsMap.put(segment, means);
				this.centroidSignalsMap.put(segment, centroids);
				counter++;
			}
		}
		if (this.displayerPanel != null) {
			this.updateStatusAsync("Processing intensity analysis ended", true,
					false);
		}
	}
	
	public Map<OmegaTrajectory, List<OmegaSegment>> getSegments() {
		return this.segments;
	}
	
	public Map<OmegaSegment, Double[]> getPeakSignalsResults() {
		return this.peakSignalsMap;
	}
	
	public Map<OmegaSegment, Double[]> getMeanSignalsResults() {
		return this.meanSignalsMap;
	}
	
	public Map<OmegaSegment, Double[]> getCentroidSignalsResults() {
		return this.centroidSignalsMap;
	}
	
	private void updateStatusSync(final String msg, final boolean ended,
	        final boolean dialog) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					OmegaIntensityAnalyzer.this.displayerPanel
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
				OmegaIntensityAnalyzer.this.displayerPanel
				.updateMessageStatus(new AnalyzerEvent(msg, ended,
				                dialog));
			}
		});
	}
}
