package edu.umassmed.omega.commons.runnable;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaSNRRun;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaParticle;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.gui.interfaces.OmegaMessageDisplayerPanelInterface;

public class OmegaIntensityAnalyzer implements Runnable {
	
	private OmegaMessageDisplayerPanelInterface displayerPanel;
	
	private final Map<OmegaTrajectory, List<OmegaSegment>> segments;
	private final Map<OmegaSegment, Double[]> peakSignalsMap;
	private final Map<OmegaSegment, Double[]> centroidSignalsMap;
	
	// SNR related START
	private final Map<OmegaSegment, Double[]> noisesMap;
	private final Map<OmegaSegment, Double[]> snrsMap;
	private final Map<OmegaSegment, Double[]> areasMap;
	private final Map<OmegaSegment, Double[]> meanSignalsMap;
	// SNR related END

	private OmegaSNRRun snrRun;
	
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
		this.centroidSignalsMap = new LinkedHashMap<OmegaSegment, Double[]>();

		// SNR related START
		this.noisesMap = new LinkedHashMap<OmegaSegment, Double[]>();
		this.meanSignalsMap = new LinkedHashMap<OmegaSegment, Double[]>();
		this.snrsMap = new LinkedHashMap<OmegaSegment, Double[]>();
		this.areasMap = new LinkedHashMap<OmegaSegment, Double[]>();
		// SNR related END

		this.displayerPanel = null;
	}
	
	public void setSNRRun(final OmegaSNRRun snrRun) {
		this.snrRun = snrRun;
	}

	public OmegaSNRRun getSNRRun() {
		return this.snrRun;
	}
	
	private void resetArray(final Double[] array) {
		array[0] = Double.MAX_VALUE;
		array[1] = 0.0;
		array[2] = Double.MIN_VALUE;
	}
	
	private void findMinMax(final Double[] array, final Double val) {
		if (array[0] > val) {
			array[0] = val;
		}
		if (array[2] < val) {
			array[2] = val;
		}
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
			final Double[] peaks = new Double[3];
			final Double[] centroids = new Double[3];
			// SNR related START
			final Double[] noises = new Double[3];
			final Double[] means = new Double[3];
			final Double[] areas = new Double[3];
			final Double[] snrs = new Double[3];
			// SNR related END
			for (final OmegaSegment segment : this.segments.get(track)) {
				this.resetArray(peaks);
				this.resetArray(centroids);
				// SNR related START
				this.resetArray(noises);
				this.resetArray(means);
				this.resetArray(areas);
				this.resetArray(snrs);
				// SNR related END
				final int roiStart = rois.indexOf(segment.getStartingROI());
				final int roiEnd = rois.indexOf(segment.getEndingROI());
				final List<OmegaROI> segmentROIs = rois.subList(roiStart,
				        roiEnd + 1);
				for (final OmegaROI roi : segmentROIs) {
					final OmegaParticle particle = (OmegaParticle) roi;
					
					final Double centroidSignal = particle
							.getCentroidIntensity();
					final Double peakSignal = particle.getPeakIntensity();

					peaks[1] += peakSignal;
					centroids[1] += centroidSignal;
					
					this.findMinMax(peaks, peakSignal);
					this.findMinMax(centroids, centroidSignal);

					// SNR related START
					if (this.snrRun != null) {
						// Integer centerSignal =
						// snrRun.getResultingLocalCenterSignals().get(roi);
						final Double meanSignal = this.snrRun
						        .getResultingLocalMeanSignals().get(roi);
						final Double noise = this.snrRun
						        .getResultingLocalNoises().get(roi);
						final Integer area = this.snrRun
						        .getResultingLocalParticleArea().get(roi);
						// Double peakSignal =
						// snrRun.getResultingLocalPeakSignals().get(roi);
						final Double snr = this.snrRun.getResultingLocalSNRs()
						        .get(roi);
						
						means[1] += meanSignal;
						noises[1] += noise;
						areas[1] += area;
						snrs[1] += snr;
						
						this.findMinMax(means, meanSignal);
						this.findMinMax(noises, noise);
						this.findMinMax(areas, Double.valueOf(area));
						this.findMinMax(snrs, snr);
					}
					// SNR related END
				}
				peaks[1] /= segmentROIs.size();
				centroids[1] /= segmentROIs.size();
				this.peakSignalsMap.put(segment, peaks);
				this.centroidSignalsMap.put(segment, centroids);
				if (this.snrRun != null) {
					means[1] /= segmentROIs.size();
					areas[1] /= segmentROIs.size();
					snrs[1] /= segmentROIs.size();
					noises[1] /= segmentROIs.size();
					this.meanSignalsMap.put(segment, means);
					this.areasMap.put(segment, areas);
					this.snrsMap.put(segment, snrs);
					this.noisesMap.put(segment, noises);
				}
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
	
	public Map<OmegaSegment, Double[]> getCentroidSignalsResults() {
		return this.centroidSignalsMap;
	}

	// SNR related START
	public Map<OmegaSegment, Double[]> getMeanSignalsResults() {
		return this.meanSignalsMap;
	}

	public Map<OmegaSegment, Double[]> getAreasResults() {
		return this.areasMap;
	}

	public Map<OmegaSegment, Double[]> getNoisesResults() {
		return this.noisesMap;
	}

	public Map<OmegaSegment, Double[]> getSNRsResults() {
		return this.snrsMap;
	}

	// SNR related END
	
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
