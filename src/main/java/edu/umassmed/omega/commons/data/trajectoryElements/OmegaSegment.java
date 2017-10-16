package edu.umassmed.omega.commons.data.trajectoryElements;

import edu.umassmed.omega.commons.data.coreElements.OmegaNamedElement;

public class OmegaSegment extends OmegaNamedElement implements
		Comparable<OmegaSegment> {
	
	private static String DISPLAY_NAME = "Segment";
	
	public static final String DEFAULT_SEGM_NAME = "Segm";
	
	private final OmegaROI from, to;
	private int segmentationType;

	private boolean isVisible;
	
	public OmegaSegment(final OmegaROI startingROI, final OmegaROI endingROI,
			final String name) {
		super(-1L, name);
		this.from = startingROI;
		this.to = endingROI;

		this.isVisible = true;
	}
	
	public OmegaROI getStartingROI() {
		return this.from;
	}
	
	public OmegaROI getEndingROI() {
		return this.to;
	}
	
	public int getSegmentationType() {
		return this.segmentationType;
	}
	
	public void setSegmentationType(final int segmentationType) {
		this.segmentationType = segmentationType;
	}
	
	public boolean isEqual(final OmegaSegment segment) {
		if ((this.from.getFrameIndex() == segment.from.getFrameIndex())
				&& (this.to.getFrameIndex() == segment.to.getFrameIndex())
				&& (this.segmentationType == segment.segmentationType))
			return true;
		return false;
	}
	
	@Override
	public int compareTo(final OmegaSegment segm) {
		return (this.getName().compareTo(segm.getName()));
	}

	public boolean isVisible() {
		return this.isVisible;
	}
	
	public void setVisible(final boolean isVisible) {
		this.isVisible = isVisible;
	}

	public static String getStaticDisplayName() {
		return OmegaSegment.DISPLAY_NAME;
	}

	@Override
	public String getDynamicDisplayName() {
		return OmegaSegment.getStaticDisplayName();
	}
}
