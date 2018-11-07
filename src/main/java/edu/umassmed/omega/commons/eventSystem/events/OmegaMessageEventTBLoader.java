package edu.umassmed.omega.commons.eventSystem.events;

import java.awt.image.BufferedImage;
import java.util.List;

public class OmegaMessageEventTBLoader extends OmegaMessageEvent {
	
	private final boolean repaint;
	
	private final Long imageID;
	private final Integer channel, zPlane;
	private final List<BufferedImage> images;
	
	public OmegaMessageEventTBLoader(final String msg, final boolean repaint) {
		this(msg, repaint, null, null, null, null);
	}
	
	public OmegaMessageEventTBLoader(final String msg, final boolean repaint,
			final Long imageID, final Integer channel, final Integer zPlane,
			final List<BufferedImage> images) {
		super(msg);
		this.repaint = repaint;

		this.imageID = imageID;
		this.channel = channel;
		this.zPlane = zPlane;
		this.images = images;
	}
	
	public boolean isRepaint() {
		return this.repaint;
	}
	
	public Long getImageID() {
		return this.imageID;
	}
	
	public Integer getChannel() {
		return this.channel;
	}
	
	public Integer getZPlane() {
		return this.zPlane;
	}
	
	public List<BufferedImage> getImages() {
		return this.images;
	}
}
