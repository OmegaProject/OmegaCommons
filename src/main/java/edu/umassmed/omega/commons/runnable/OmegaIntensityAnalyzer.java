package edu.umassmed.omega.commons.runnable;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaSNRRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrajectoriesSegmentationRun;
import edu.umassmed.omega.commons.data.coreElements.OmegaElement;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaParticle;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.gui.interfaces.OmegaMessageDisplayerPanelInterface;

public class OmegaIntensityAnalyzer implements Runnable {
	
	private final OmegaMessageDisplayerPanelInterface displayerPanel;
	
	private final Map<OmegaTrajectory, List<OmegaSegment>> segments;
	private final Map<OmegaSegment, Double[]> peakSignalsMap;
	private final Map<OmegaSegment, Double[]> centroidSignalsMap;

	private final Map<OmegaROI, Double> peakSignalsLocMap;
	private final Map<OmegaROI, Double> centroidSignalsLocMap;
	
	// SNR related START
	private final Map<OmegaSegment, Double[]> backgroundsMap;
	private final Map<OmegaSegment, Double[]> noisesMap;
	private final Map<OmegaSegment, Double[]> snrsMap;
	private final Map<OmegaSegment, Double[]> areasMap;
	private final Map<OmegaSegment, Double[]> meanSignalsMap;

	private final Map<OmegaROI, Double> meanSignalsLocMap;
	private final Map<OmegaROI, Double> backgroundsLocMap;
	private final Map<OmegaROI, Double> noisesLocMap;
	private final Map<OmegaROI, Double> areasLocMap;
	private final Map<OmegaROI, Double> snrsLocMap;
	// SNR related END

	private OmegaSNRRun snrRun;
	private final OmegaTrajectoriesSegmentationRun segmRun;

	private final List<OmegaElement> selections;
	
	public OmegaIntensityAnalyzer(
			final OmegaTrajectoriesSegmentationRun segmRun,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments) {
		this(null, segmRun, segments, null);
	}
	
	public OmegaIntensityAnalyzer(
			final OmegaMessageDisplayerPanelInterface displayerPanel,
			final OmegaTrajectoriesSegmentationRun segmRun,
			final Map<OmegaTrajectory, List<OmegaSegment>> segments,
			final List<OmegaElement> selections) {
		this.displayerPanel = displayerPanel;

		this.segmRun = segmRun;
		this.selections = selections;
		
		this.segments = new LinkedHashMap<OmegaTrajectory, List<OmegaSegment>>(
				segments);
		this.peakSignalsMap = new LinkedHashMap<OmegaSegment, Double[]>();
		this.centroidSignalsMap = new LinkedHashMap<OmegaSegment, Double[]>();

		this.peakSignalsLocMap = new LinkedHashMap<OmegaROI, Double>();
		this.centroidSignalsLocMap = new LinkedHashMap<OmegaROI, Double>();

		// SNR related START
		this.backgroundsMap = new LinkedHashMap<OmegaSegment, Double[]>();
		this.noisesMap = new LinkedHashMap<OmegaSegment, Double[]>();
		this.meanSignalsMap = new LinkedHashMap<OmegaSegment, Double[]>();
		this.snrsMap = new LinkedHashMap<OmegaSegment, Double[]>();
		this.areasMap = new LinkedHashMap<OmegaSegment, Double[]>();

		this.backgroundsLocMap = new LinkedHashMap<OmegaROI, Double>();
		this.noisesLocMap = new LinkedHashMap<OmegaROI, Double>();
		this.meanSignalsLocMap = new LinkedHashMap<OmegaROI, Double>();
		this.snrsLocMap = new LinkedHashMap<OmegaROI, Double>();
		this.areasLocMap = new LinkedHashMap<OmegaROI, Double>();
		// SNR related END
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
			final Double[] backgrounds = new Double[3];
			final Double[] noises = new Double[3];
			final Double[] means = new Double[3];
			final Double[] areas = new Double[3];
			final Double[] snrs = new Double[3];
			// SNR related END
			for (final OmegaSegment segment : this.segments.get(track)) {
				this.resetArray(peaks);
				this.resetArray(centroids);
				// SNR related START
				this.resetArray(backgrounds);
				this.resetArray(noises);
				this.resetArray(means);
				this.resetArray(areas);
				this.resetArray(snrs);
				// SNR related END
				final int roiStart = rois.indexOf(segment.getStartingROI());
				final int roiEnd = rois.indexOf(segment.getEndingROI());
				final List<OmegaROI> segmentROIs = rois.subList(roiStart,
						roiEnd + 1);
				boolean foundAtLeastOnePeak = false;
				boolean foundAtLeastOneCentroid = false;
				for (final OmegaROI roi : segmentROIs) {
					final OmegaParticle particle = (OmegaParticle) roi;
					
					final Double centroidSignal = particle
							.getCentroidIntensity();
					this.centroidSignalsLocMap.put(roi, centroidSignal);
					final Double peakSignal = particle.getPeakIntensity();
					this.peakSignalsLocMap.put(roi, peakSignal);

					if (peakSignal != null) {
						peaks[1] += peakSignal;
						this.findMinMax(peaks, peakSignal);
						foundAtLeastOnePeak = true;
					}
					if (centroidSignal != null) {
						centroids[1] += centroidSignal;
						this.findMinMax(centroids, centroidSignal);
						foundAtLeastOneCentroid = true;
					}

					// SNR related START
					if (this.snrRun != null) {
						// Integer centerSignal =
						// snrRun.getResultingLocalCenterSignals().get(roi);
						final Double meanSignal = this.snrRun
								.getResultingLocalMeanSignals().get(roi);
						this.meanSignalsLocMap.put(roi, meanSignal);
						final Double background = this.snrRun
								.getResultingLocalBackgrounds().get(roi);
						this.backgroundsLocMap.put(roi, background);
						final Double noise = this.snrRun
								.getResultingLocalNoises().get(roi);
						this.noisesLocMap.put(roi, noise);
						final Integer area = this.snrRun
								.getResultingLocalParticleArea().get(roi);
						this.areasLocMap.put(roi, (double) area);
						// Double peakSignal =
						// snrRun.getResultingLocalPeakSignals().get(roi);
						final Double snr = this.snrRun.getResultingLocalSNRs()
								.get(roi);
						this.snrsLocMap.put(roi, snr);
						
						means[1] += meanSignal;
						backgrounds[1] += background;
						noises[1] += noise;
						areas[1] += area;
						snrs[1] += snr;
						
						this.findMinMax(means, meanSignal);
						this.findMinMax(backgrounds, background);
						this.findMinMax(noises, noise);
						this.findMinMax(areas, Double.valueOf(area));
						this.findMinMax(snrs, snr);
					}
					// SNR related END
				}
				if (!foundAtLeastOnePeak) {
					peaks[0] = null;
					peaks[1] = null;
					peaks[2] = null;
				} else if (peaks[1] != 0) {
					peaks[1] /= segmentROIs.size();
				}
				if (!foundAtLeastOneCentroid) {
					centroids[0] = null;
					centroids[1] = null;
					centroids[2] = null;
				} else if (centroids[1] != 0) {
					centroids[1] /= segmentROIs.size();
				}
				this.peakSignalsMap.put(segment, peaks);
				this.centroidSignalsMap.put(segment, centroids);
				if (this.snrRun != null) {
					means[1] /= segmentROIs.size();
					areas[1] /= segmentROIs.size();
					snrs[1] /= segmentROIs.size();
					noises[1] /= segmentROIs.size();
				} else {
					means[0] = null;
					means[1] = null;
					means[2] = null;
					areas[0] = null;
					areas[1] = null;
					areas[2] = null;
					snrs[0] = null;
					snrs[1] = null;
					snrs[2] = null;
					noises[0] = null;
					noises[1] = null;
					noises[2] = null;
					backgrounds[0] = null;
					backgrounds[1] = null;
					backgrounds[2] = null;
				}
				this.meanSignalsMap.put(segment, means);
				this.areasMap.put(segment, areas);
				this.snrsMap.put(segment, snrs);
				this.noisesMap.put(segment, noises);
				this.backgroundsMap.put(segment, backgrounds);
				counter++;
			}
		}
		if (this.displayerPanel != null) {
			this.updateStatusAsync("Processing intensity analysis ended", true,
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
	
	public Map<OmegaSegment, Double[]> getPeakSignalsResults() {
		return this.peakSignalsMap;
	}
	
	public Map<OmegaSegment, Double[]> getCentroidSignalsResults() {
		return this.centroidSignalsMap;
	}

	public Map<OmegaROI, Double> getPeakSignalsLocalResults() {
		return this.peakSignalsLocMap;
	}
	
	public Map<OmegaROI, Double> getCentroidSignalsLocalResults() {
		return this.centroidSignalsLocMap;
	}

	// SNR related START
	public Map<OmegaSegment, Double[]> getMeanSignalsResults() {
		return this.meanSignalsMap;
	}

	public Map<OmegaSegment, Double[]> getAreasResults() {
		return this.areasMap;
	}
	
	public Map<OmegaSegment, Double[]> getBackgroundsResults() {
		return this.backgroundsMap;
	}

	public Map<OmegaSegment, Double[]> getNoisesResults() {
		return this.noisesMap;
	}

	public Map<OmegaSegment, Double[]> getSNRsResults() {
		return this.snrsMap;
	}

	public Map<OmegaROI, Double> getMeanSignalsLocalResults() {
		return this.meanSignalsLocMap;
	}

	public Map<OmegaROI, Double> getAreasLocalResults() {
		return this.areasLocMap;
	}
	
	public Map<OmegaROI, Double> getBackgroundsLocalResults() {
		return this.backgroundsLocMap;
	}

	public Map<OmegaROI, Double> getNoisesLocalResults() {
		return this.noisesLocMap;
	}

	public Map<OmegaROI, Double> getSNRsLocalResults() {
		return this.snrsLocMap;
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
