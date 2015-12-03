package main.java.edu.umassmed.omega.commons.runnable;

import main.java.edu.umassmed.omega.commons.eventSystem.events.OmegaMessageEvent;

public class AnalyzerEvent extends OmegaMessageEvent {

	private final boolean isEnded, needDialog;

	public AnalyzerEvent(final String msg, final boolean isEnded,
			final boolean needDialog) {
		super(msg);
		this.isEnded = isEnded;
		this.needDialog = needDialog;
	}

	public boolean isEnded() {
		return this.isEnded;
	}

	public boolean needDialog() {
		return this.needDialog;
	}
}
