package edu.umassmed.omega.commons.trajectoryTool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.RootPaneContainer;

import edu.umassmed.omega.commons.data.coreElements.OmegaFrame;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.trajectoryTool.gui.OmegaTracksToolDialog;
import edu.umassmed.omega.commons.utilities.OmegaStringUtilities;
import edu.umassmed.omega.commons.utilities.OmegaTrajectoryIOUtility;

public class OmegaTracksExporter extends OmegaTrajectoryIOUtility {
	public static final String PARTICLE_FRAMEINDEX = "identifier";
	public static final String PARTICLE_XCOORD = "x";
	public static final String PARTICLE_YCOORD = "y";
	public static final String PARTICLE_INTENSITY = "intensity";
	public static final String PARTICLE_PROBABILITY = "probability";
	public static final String PARTICLE_SEPARATOR = "separator";

	private final Map<OmegaFrame, List<OmegaROI>> particles;
	private final Map<OmegaROI, Map<String, Object>> particlesValues;
	private final List<OmegaTrajectory> tracks;

	private OmegaTracksToolDialog dialog;

	public OmegaTracksExporter(final RootPaneContainer parent) {
		this.particles = new LinkedHashMap<OmegaFrame, List<OmegaROI>>();
		this.particlesValues = new LinkedHashMap<OmegaROI, Map<String, Object>>();
		this.tracks = new ArrayList<OmegaTrajectory>();

		this.dialog = new OmegaTracksToolDialog(parent, true, false, this);
	}

	public OmegaTracksExporter() {
		this.particles = new LinkedHashMap<OmegaFrame, List<OmegaROI>>();
		this.particlesValues = new LinkedHashMap<OmegaROI, Map<String, Object>>();
		this.tracks = new ArrayList<OmegaTrajectory>();

		this.dialog = null;
	}

	public void showDialog(final RootPaneContainer parent) {
		if (this.dialog == null) {
			this.dialog = new OmegaTracksToolDialog(parent, true, false, this);
		}
		this.dialog.updateParentContainer(parent);
		this.dialog.setVisible(true);
	}

	// TODO change IllegalArgumentException with a custom exception
	public void exportTrajectories(final String fileNameIdentifier,
	        final String extension, final String trajectoryIdentifier,
	        final String particleIdentifier, final boolean startAtOne,
	        final String nonParticleIdentifier, final String particleSeparator,
	        final List<String> particleDataOrder, final File sourceFolder)
	        throws IOException, IllegalArgumentException {
		if (!sourceFolder.isDirectory())
			throw new IllegalArgumentException("The source folder: "
			        + sourceFolder + " has to be a valid directory");
		if (sourceFolder.listFiles().length != 0)
			throw new IllegalArgumentException("The source folder: "
			        + sourceFolder + " has to be empty");

		int counter = 1;
		final int max = this.tracks.size();
		final int maxDigits = String.valueOf(max).length();
		for (final OmegaTrajectory track : this.tracks) {
			final File f;
			final String multiDigitCounter = OmegaStringUtilities
			        .getMultiDigitsCounter(counter, maxDigits);
			if (trajectoryIdentifier == null) {
				f = new File(sourceFolder.getPath() + File.separatorChar
				        + fileNameIdentifier + multiDigitCounter + extension);
			} else {
				f = new File(sourceFolder.getPath() + File.separatorChar
				        + fileNameIdentifier + extension);
			}
			boolean isLast = false;
			if (this.tracks.indexOf(track) == (this.tracks.size() - 1)) {
				isLast = true;
			}
			final String newTrajIdent = trajectoryIdentifier != null ? trajectoryIdentifier
			        .replace("\\t", "\t") : null;
			final String newPartIdent = particleIdentifier != null ? particleIdentifier
			        .replace("\\t", "\t") : null;
			final String newNonPartIdent = nonParticleIdentifier != null ? nonParticleIdentifier
			        .replace("\\t", "\t") : null;
			final String newPartSep = particleSeparator != null ? particleSeparator
			        .replace("\\t", "\t") : null;
			this.exportTrajectories(f, newTrajIdent, newPartIdent, startAtOne,
					newNonPartIdent, newPartSep, particleDataOrder, counter,
					track, isLast);
			counter++;
		}

		this.particles.clear();
		this.particlesValues.clear();
		this.tracks.clear();
		// final OmegaImporterEventResultsParticleTracking evt = new
		// OmegaImporterEventResultsParticleTracking(
		// this, resultingParticles, resultingTrajectories,
		// resultingParticlesValues);
		// this.fireEvent(evt);
	}

	private void exportTrajectories(final File f,
			final String trajectoryIdentifier, final String particleIdentifier,
	        final boolean startAtOne, final String nonParticleIdentifier,
	        final String particleSeparator,
	        final List<String> particleDataOrder, final int counter,
	        final OmegaTrajectory track, final boolean isLast)
					throws IOException, IllegalArgumentException {
		if (!f.exists()) {
			f.createNewFile();
		}
		boolean append = false;
		if (trajectoryIdentifier != null) {
			append = true;
		}

		final FileWriter fw = new FileWriter(f, append);
		final BufferedWriter bw = new BufferedWriter(fw);

		if (trajectoryIdentifier != null) {
			bw.write(trajectoryIdentifier + " " + counter + "\n");
		}

		for (final OmegaROI roi : track.getROIs()) {
			this.exportParticle(bw, startAtOne, particleIdentifier,
			        particleSeparator, particleDataOrder, roi);
		}

		if ((trajectoryIdentifier != null) && !isLast) {
			bw.write("\n");
		}

		bw.close();
		fw.close();
	}

	private void exportParticle(final BufferedWriter bw,
	        final boolean startAtOne, final String particleIdentifier,
	        final String particleSeparator,
			final List<String> particleDataOrder, final OmegaROI roi)
					throws IOException {
		final StringBuffer buf = new StringBuffer();
		buf.append(particleIdentifier);
		Integer frameIndex = roi.getFrameIndex();
		if (startAtOne) {
			frameIndex += 1;
		}
		final Map<String, Object> values = this.particlesValues.get(roi);
		for (final String order : particleDataOrder) {
			if (order.equals(OmegaTracksExporter.PARTICLE_SEPARATOR)) {
				buf.append(particleSeparator);
			} else if (order.equals(OmegaTracksExporter.PARTICLE_FRAMEINDEX)) {
				buf.append(frameIndex);
			} else if (order.equals(OmegaTracksExporter.PARTICLE_XCOORD)) {
				buf.append(roi.getX());
			} else if (order.equals(OmegaTracksExporter.PARTICLE_YCOORD)) {
				buf.append(roi.getY());
			} else if (values.containsKey(order)) {
				buf.append(values.get(order));
			} else {
				// TODO error?
			}
			buf.append(particleSeparator);
		}
		buf.append("\n");
		bw.write(buf.toString());
	}

	public void setParticles(final Map<OmegaFrame, List<OmegaROI>> particles) {
		this.particles.putAll(particles);
	}

	public void setParticlesValues(
	        final Map<OmegaROI, Map<String, Object>> particlesValues) {
		this.particlesValues.putAll(particlesValues);
	}

	public void setTracks(final List<OmegaTrajectory> tracks) {
		this.tracks.addAll(tracks);
	}

	public void reset() {
		this.particles.clear();
		this.particlesValues.clear();
		this.tracks.clear();
	}
}
