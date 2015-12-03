package edu.umassmed.omega.commons.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.umassmed.omega.commons.eventSystem.OmegaTrajectoryIOEventListener;
import edu.umassmed.omega.commons.eventSystem.events.OmegaTrajectoryIOEvent;

public class OmegaTrajectoryIOUtility {
	private final List<OmegaTrajectoryIOEventListener> listeners = new ArrayList<OmegaTrajectoryIOEventListener>();

	public synchronized void addOmegaImporterListener(
	        final OmegaTrajectoryIOEventListener listener) {
		this.listeners.add(listener);
	}

	public synchronized void removeOmegaImporterEventListener(
	        final OmegaTrajectoryIOEventListener listener) {
		this.listeners.remove(listener);
	}

	public synchronized void fireEvent(final OmegaTrajectoryIOEvent event) {
		final Iterator<OmegaTrajectoryIOEventListener> i = this.listeners
				.iterator();
		while (i.hasNext()) {
			i.next().handleIOEvent(event);
		}
	}
}
