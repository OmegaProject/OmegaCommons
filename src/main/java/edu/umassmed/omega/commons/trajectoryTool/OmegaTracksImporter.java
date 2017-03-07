package edu.umassmed.omega.commons.trajectoryTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.RootPaneContainer;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainer;
import edu.umassmed.omega.commons.data.coreElements.OmegaPlane;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaParticle;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.eventSystem.events.OmegaImporterEventResultsParticleDetection;
import edu.umassmed.omega.commons.eventSystem.events.OmegaImporterEventResultsParticleTracking;
import edu.umassmed.omega.commons.trajectoryTool.gui.OmegaTracksToolDialog;
import edu.umassmed.omega.commons.utilities.OmegaIOUtility;
import edu.umassmed.omega.commons.utilities.OmegaStringUtilities;

// TODO just copied from the importer, needs to be review to export a list of
// tracks
public class OmegaTracksImporter extends OmegaIOUtility {
	
	private final Map<Integer, OmegaPlane> frames;
	private final Map<OmegaPlane, List<OmegaROI>> particles;
	private final Map<OmegaROI, Map<String, Object>> particlesValues;
	private final List<OmegaTrajectory> tracks;

	private final Map<String, String> analysisDataMap, paramMap;
	private boolean isReadingParams;
	
	private OmegaTracksToolDialog dialog;
	
	private OmegaAnalysisRunContainer container;

	public static int IMPORTER_MODE_NOT_SET = -1;
	public static int IMPORTER_MODE_PARTICLES = 0;
	public static int IMPORTER_MODE_TRACKS = 1;
	private int mode;
	
	public OmegaTracksImporter(final RootPaneContainer parent) {
		this.frames = new LinkedHashMap<Integer, OmegaPlane>();
		this.particles = new LinkedHashMap<OmegaPlane, List<OmegaROI>>();
		this.particlesValues = new LinkedHashMap<OmegaROI, Map<String, Object>>();
		this.tracks = new ArrayList<OmegaTrajectory>();
		
		this.dialog = new OmegaTracksToolDialog(parent, true, true, this);
		
		this.container = null;
		
		this.mode = OmegaTracksImporter.IMPORTER_MODE_NOT_SET;
		
		this.analysisDataMap = new LinkedHashMap<String, String>();
		this.paramMap = new LinkedHashMap<String, String>();
		this.isReadingParams = false;
	}
	
	public OmegaTracksImporter() {
		this.frames = new LinkedHashMap<Integer, OmegaPlane>();
		this.particles = new LinkedHashMap<OmegaPlane, List<OmegaROI>>();
		this.particlesValues = new LinkedHashMap<OmegaROI, Map<String, Object>>();
		this.tracks = new ArrayList<OmegaTrajectory>();
		
		this.dialog = null;
		
		this.container = null;
		
		this.mode = OmegaTracksImporter.IMPORTER_MODE_NOT_SET;
		
		this.analysisDataMap = new LinkedHashMap<String, String>();
		this.paramMap = new LinkedHashMap<String, String>();
		this.isReadingParams = false;
	}
	
	public void showDialog(final RootPaneContainer parent) {
		if (this.dialog == null) {
			this.dialog = new OmegaTracksToolDialog(parent, true, true, this);
		}
		this.dialog.updateParentContainer(parent);
		this.dialog.setVisible(true);
	}

	public void setMode(final int mode) {
		if ((mode != OmegaTracksImporter.IMPORTER_MODE_PARTICLES)
				&& (mode != OmegaTracksImporter.IMPORTER_MODE_TRACKS))
			throw new IllegalArgumentException("Mode: " + mode
					+ " not a possible choice for "
					+ OmegaTracksImporter.class.getName());
		this.mode = mode;
	}

	public void importData(final boolean multifile,
	        final String fileNameIdentifier, final String dataIdentifier,
	        final String particleIdentifier, final boolean startAtOne,
	        final String nonParticleIdentifier, final String particleSeparator,
			final List<String> particleDataOrder, final File sourceFolder)
					throws IllegalArgumentException, IOException {
		if (this.mode == OmegaTracksImporter.IMPORTER_MODE_NOT_SET)
			throw new IllegalArgumentException(
			        OmegaTracksImporter.class.getName()
			                + " mode not set, cannot import data");
		if (this.mode == OmegaTracksImporter.IMPORTER_MODE_PARTICLES) {
			this.importParticles(fileNameIdentifier, dataIdentifier,
					particleIdentifier, startAtOne, nonParticleIdentifier,
					particleSeparator, particleDataOrder, sourceFolder);
		} else if (this.mode == OmegaTracksImporter.IMPORTER_MODE_TRACKS) {
			this.importTrajectories(multifile, fileNameIdentifier,
			        dataIdentifier, particleIdentifier, startAtOne,
			        nonParticleIdentifier, particleSeparator,
			        particleDataOrder, sourceFolder);
		}
	}
	
	private void importParticles(final String fileNameIdentifier,
			final String planeIdentifier, final String particleIdentifier,
			final boolean startAtOne, final String nonParticleIdentifier,
			final String particleSeparator,
			final List<String> particleDataOrder, final File sourceFolder)
					throws IllegalArgumentException, IOException {
		if (!sourceFolder.isDirectory())
			throw new IllegalArgumentException("The destination folder: "
					+ sourceFolder + " has to be a valid directory");
		if (sourceFolder.listFiles().length == 0)
			throw new IllegalArgumentException("The destination folder: "
					+ sourceFolder + " has to be not empty");
		boolean isValid = false;
		
		for (final File f : sourceFolder.listFiles()) {
			final String fName = f.getName();
			if (!fName.matches(fileNameIdentifier)
					&& !fName.startsWith(fileNameIdentifier)) {
				continue;
			}
			isValid = true;
			final FileReader fr = new FileReader(f);
			final BufferedReader br = new BufferedReader(fr);
			this.importParticles(f.getName(), planeIdentifier,
					particleIdentifier, startAtOne, nonParticleIdentifier,
					particleSeparator, particleDataOrder, br);
			br.close();
			fr.close();
		}
		if (!isValid)
			throw new IllegalArgumentException(
					"The source folder: "
							+ sourceFolder
							+ " has to contain at least 1 file containing the given file name identifier");
		
		final LinkedHashMap<String, String> analysisData = new LinkedHashMap<String, String>(
				this.analysisDataMap);
		final LinkedHashMap<String, String> paramData = new LinkedHashMap<String, String>(
		        this.paramMap);
		final Map<OmegaPlane, List<OmegaROI>> resultingParticles = new LinkedHashMap<OmegaPlane, List<OmegaROI>>(
				this.particles);
		final Map<OmegaROI, Map<String, Object>> resultingParticlesValues = new LinkedHashMap<OmegaROI, Map<String, Object>>(
				this.particlesValues);
		final OmegaImporterEventResultsParticleDetection evt = new OmegaImporterEventResultsParticleDetection(
				this, this.container, analysisData, paramData,
				resultingParticles, resultingParticlesValues);
		this.fireEvent(evt);
	}
	
	// TODO change IllegalArgumentException with a custom exception
	private void importTrajectories(final boolean multifile,
	        final String fileNameIdentifier, final String trajectoryIdentifier,
	        final String particleIdentifier, final boolean startAtOne,
	        final String nonParticleIdentifier, final String particleSeparator,
			final List<String> particleDataOrder, final File sourceFolder)
					throws IOException, IllegalArgumentException {
		if (!sourceFolder.isDirectory())
			throw new IllegalArgumentException("The destination folder: "
					+ sourceFolder + " has to be a valid directory");
		if (sourceFolder.listFiles().length == 0)
			throw new IllegalArgumentException("The destination folder: "
					+ sourceFolder + " has to be not empty");
		boolean isValid = false;
		
		for (final File f : sourceFolder.listFiles()) {
			final String fName = f.getName();
			if (!fName.matches(fileNameIdentifier)
					&& !fName.startsWith(fileNameIdentifier)) {
				continue;
			}
			isValid = true;
			final FileReader fr = new FileReader(f);
			final BufferedReader br = new BufferedReader(fr);
			this.importTrajectories(multifile, f.getName(),
			        trajectoryIdentifier, particleIdentifier, startAtOne,
			        nonParticleIdentifier, particleSeparator,
			        particleDataOrder, br);
			br.close();
			fr.close();
		}
		if (!isValid)
			throw new IllegalArgumentException(
					"The source folder: "
							+ sourceFolder
							+ " has to contain at least 1 file containing the given file name identifier");
		
		final List<OmegaTrajectory> toRemoveTracks = new ArrayList<OmegaTrajectory>();
		for (final OmegaTrajectory t : this.tracks) {
			t.recalculateLength();
			if (t.getLength() < 2) {
				toRemoveTracks.add(t);
			}
		}
		this.tracks.removeAll(toRemoveTracks);
		if (this.tracks.isEmpty())
			throw new IllegalArgumentException(
					"Something wrong with parameters: no tracks have been found in "
			                + sourceFolder);
		
		final LinkedHashMap<String, String> analysisData = new LinkedHashMap<String, String>(
				this.analysisDataMap);
		final LinkedHashMap<String, String> paramData = new LinkedHashMap<String, String>(
		        this.paramMap);
		final Map<OmegaPlane, List<OmegaROI>> resultingParticles = new LinkedHashMap<OmegaPlane, List<OmegaROI>>(
				this.particles);
		final Map<OmegaROI, Map<String, Object>> resultingParticlesValues = new LinkedHashMap<OmegaROI, Map<String, Object>>(
				this.particlesValues);
		final List<OmegaTrajectory> resultingTrajectories = new ArrayList<OmegaTrajectory>(
				this.tracks);
		final OmegaImporterEventResultsParticleTracking evt = new OmegaImporterEventResultsParticleTracking(
				this, this.container, analysisData, paramData,
				resultingParticles, resultingTrajectories,
				resultingParticlesValues);
		this.fireEvent(evt);
	}
	
	private void importParticles(final String fileName,
			final String planeIdentifier, final String particleIdentifier,
			final boolean startAtOne, final String nonParticleIdentifier,
			final String particleSeparator,
			final List<String> particleDataOrder, final BufferedReader br)
					throws IOException, IllegalArgumentException {
		fileName.substring(0, fileName.lastIndexOf("."));
		String line = br.readLine();
		while (line != null) {
			if (line.isEmpty()) {
				line = br.readLine();
				continue;
			}
			
			if ((nonParticleIdentifier != null)
					&& line.startsWith(nonParticleIdentifier)) {
				line = br.readLine();
				this.importAnalysisData(nonParticleIdentifier,
				        particleSeparator, line);
				continue;
			}
			
			if ((particleIdentifier != null)
					&& line.startsWith(particleIdentifier)) {
				line = line.replaceFirst(particleIdentifier, "");
				this.importParticle(startAtOne, particleSeparator,
						particleDataOrder, line);
			} else {
				this.importParticle(startAtOne, particleSeparator,
						particleDataOrder, line);
			}
			line = br.readLine();
		}
	}

	private void importAnalysisData(final String nonParticleIdentifier,
	        final String particleSeparator, final String line) {
		String newLine = line.replaceFirst(nonParticleIdentifier, "");
		newLine = newLine.replaceFirst(" ", "");
		final String[] tokens = newLine.split(particleSeparator);
		if (tokens[0].equals(OmegaDataToolConstants.RUN_NAME)) {
			this.analysisDataMap.put(tokens[0], tokens[1]);
		} else if (tokens[0].equals(OmegaDataToolConstants.RUN_ON)) {
			this.analysisDataMap.put(tokens[0], tokens[1]);
		} else if (tokens[0].equals(OmegaDataToolConstants.RUN_BY)) {
			this.analysisDataMap.put(tokens[0], tokens[1] + " " + tokens[2]);
		} else if (tokens[0].equals(OmegaDataToolConstants.ALGORITHM)) {
			this.analysisDataMap.put(tokens[0], tokens[1]);
		} else if (tokens[0].equals(OmegaDataToolConstants.VERSION)) {
			this.analysisDataMap.put(tokens[0], tokens[1]);
		} else if (tokens[0].equals(OmegaDataToolConstants.PUBLISHED)) {
			this.analysisDataMap.put(tokens[0], tokens[1]);
		} else if (tokens[0].equals(OmegaDataToolConstants.AUTHOR)) {
			this.analysisDataMap.put(tokens[0], tokens[1] + " " + tokens[2]);
		} else if (tokens[0].equals(OmegaDataToolConstants.REFERENCE)) {
			this.analysisDataMap.put(tokens[0], tokens[1]);
		} else if (tokens[0].equals(OmegaDataToolConstants.DESCRIPTION)) {
			this.analysisDataMap.put(tokens[0], tokens[1]);
		} else if (tokens[0].equals(OmegaDataToolConstants.PARAMETERS)) {
			this.isReadingParams = true;
		} else if (tokens[0].equals(OmegaDataToolConstants.PARAM)) {
			this.paramMap.put(tokens[1], tokens[2]);
		} else if (tokens[0].equals(nonParticleIdentifier)
		        && this.isReadingParams) {
			this.isReadingParams = false;
		}
		
	}
	
	private void importTrajectories(final boolean multifile,
	        final String fileName, final String trajectoryIdentifier,
	        final String particleIdentifier, final boolean startAtOne,
	        final String nonParticleIdentifier, final String particleSeparator,
			final List<String> particleDataOrder, final BufferedReader br)
					throws IOException, IllegalArgumentException {
		final String name1 = fileName.substring(0, fileName.lastIndexOf("."));
		OmegaTrajectory trajectory = null;
		if (multifile) {
			trajectory = new OmegaTrajectory(-1, name1);
			// trajectory.setName(name1);
			this.tracks.add(trajectory);
		}
		String line = br.readLine();
		while (line != null) {
			if (line.isEmpty()) {
				line = br.readLine();
				continue;
			}
			if (!multifile && line.startsWith(trajectoryIdentifier)) {
				final String name = OmegaStringUtilities.removeSymbols(line);
				String name2 = OmegaStringUtilities.replaceWhitespaces(name,
						"_");
				if (name2.startsWith("_")) {
					name2 = name2.replaceFirst("_", "");
				}
				trajectory = new OmegaTrajectory(-1, name2);
				// trajectory.setName(name1 + "_" + name2);
				this.tracks.add(trajectory);
			}
			
			if ((nonParticleIdentifier != null)
					&& line.startsWith(nonParticleIdentifier)) {
				line = br.readLine();
				continue;
			}
			
			if ((particleIdentifier != null)
					&& line.startsWith(particleIdentifier)
					&& (trajectory != null)) {
				line = line.replaceFirst(particleIdentifier, "");
				final OmegaParticle p = this.importParticle(startAtOne,
						particleSeparator, particleDataOrder, line);
				trajectory.addROI(p);
			} else {
				final OmegaParticle p = this.importParticle(startAtOne,
						particleSeparator, particleDataOrder, line);
				trajectory.addROI(p);
			}
			line = br.readLine();
		}
	}
	
	private OmegaParticle importParticle(final boolean startAtOne,
			final String particleSeparator,
			final List<String> particleDataOrder, final String particleToImport)
					throws IllegalArgumentException {
		final String[] particleData = particleToImport.split(particleSeparator);
		Integer frameIndex = null;
		Double x = null, y = null, peakIntensity = null, centroidIntensity = null;
		final Map<String, Object> particleValues = new LinkedHashMap<String, Object>();
		if (particleData.length < particleDataOrder.size())
			throw new IllegalArgumentException("The line: " + particleToImport
			        + " doesn't contain the required information");
		for (int i = 0; i < particleDataOrder.size(); i++) {
			final String order = particleDataOrder.get(i);
			final String data = particleData[i];
			if (order.equals(OmegaDataToolConstants.PARTICLE_SEPARATOR)) {
				continue;
			} else if (order.equals(OmegaDataToolConstants.PARTICLE_FRAMEINDEX)) {
				frameIndex = Integer.valueOf(data);
			} else if (order.equals(OmegaDataToolConstants.PARTICLE_XCOORD)) {
				x = Double.valueOf(data);
			} else if (order.equals(OmegaDataToolConstants.PARTICLE_YCOORD)) {
				y = Double.valueOf(data);
			} else if (order
			        .equals(OmegaDataToolConstants.PARTICLE_PEAK_INTENSITY)) {
				peakIntensity = Double.valueOf(data);
			} else if (order
			        .equals(OmegaDataToolConstants.PARTICLE_CENT_INTENSITY)) {
				centroidIntensity = Double.valueOf(data);
			} else {
				// TODO ADD CHOICE OF TYPE OF CONTENT IN GUI AND HERE
				particleValues.put(order, Double.valueOf(data));
			}
		}
		if ((frameIndex == null) || (x == null) || (y == null))
			throw new IllegalArgumentException(
					"The line: "
							+ particleToImport
							+ " doesn't contain enough information for identifying an ROI");
		OmegaParticle p = null;
		if (!startAtOne) {
			frameIndex++;
		}
		if ((peakIntensity == null) && (centroidIntensity == null)) {
			p = new OmegaParticle(frameIndex, x, y);
		} else {
			if ((peakIntensity != null) && (centroidIntensity != null)) {
				p = new OmegaParticle(frameIndex, x, y, peakIntensity,
				        centroidIntensity);
			} else {
				if (peakIntensity == null) {
					p = new OmegaParticle(frameIndex, x, y, centroidIntensity);
				} else {
					p = new OmegaParticle(frameIndex, x, y, peakIntensity);
				}
			}
		}
		
		OmegaPlane frame;
		if (this.frames.containsKey(frameIndex)) {
			frame = this.frames.get(frameIndex);
		} else {
			frame = new OmegaPlane(frameIndex);
			this.frames.put(frameIndex, frame);
		}
		
		List<OmegaROI> rois;
		if (this.particles.containsKey(frame)) {
			rois = this.particles.get(frame);
		} else {
			rois = new ArrayList<OmegaROI>();
		}
		rois.add(p);
		this.particles.put(frame, rois);
		this.particlesValues.put(p, particleValues);
		
		return p;
	}
	
	public Map<String, String> getAnalysisData() {
		return this.analysisDataMap;
	}
	
	public Map<String, String> getParametersData() {
		return this.paramMap;
	}
	
	public Map<Integer, OmegaPlane> getFrames() {
		return this.frames;
	}
	
	public Map<OmegaPlane, List<OmegaROI>> getParticles() {
		return this.particles;
	}
	
	public Map<OmegaROI, Map<String, Object>> getParticlesValues() {
		return this.particlesValues;
	}
	
	public List<OmegaTrajectory> getTracks() {
		return this.tracks;
	}
	
	public void reset() {
		this.frames.clear();
		this.particles.clear();
		this.particlesValues.clear();
		this.tracks.clear();
	}
	
	public void setContainer(final OmegaAnalysisRunContainer container) {
		this.container = container;
	}
}
