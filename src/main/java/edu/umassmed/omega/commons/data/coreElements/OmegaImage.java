/*******************************************************************************
 * Copyright (C) 2014 University of Massachusetts Medical School Alessandro
 * Rigano (Program in Molecular Medicine) Caterina Strambio De Castillia
 * (Program in Molecular Medicine)
 *
 * Created by the Open Microscopy Environment inteGrated Analysis (OMEGA) team:
 * Alex Rigano, Caterina Strambio De Castillia, Jasmine Clark, Vanni Galli,
 * Raffaello Giulietti, Loris Grossi, Eric Hunter, Tiziano Leidi, Jeremy Luban,
 * Ivo Sbalzarini and Mario Valle.
 *
 * Key contacts: Caterina Strambio De Castillia: caterina.strambio@umassmed.edu
 * Alex Rigano: alex.rigano@umassmed.edu
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package edu.umassmed.omega.commons.data.coreElements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainerInterface;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParticleDetectionRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParticleLinkingRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaSNRRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrajectoriesRelinkingRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaTrajectoriesSegmentationRun;
import edu.umassmed.omega.commons.trajectoryTool.OmegaDataToolConstants;

public class OmegaImage extends OmegaNamedElement implements OmeroElement,
		OmegaAnalysisRunContainerInterface {
	
	private static String DISPLAY_NAME = "Image";

	private final List<OmegaDataset> datasets;

	private final List<OmegaImagePixels> pixelsList;

	private OmegaExperimenter experimenter;

	private final List<OmegaAnalysisRun> analysisRuns;

	private final Date acquisitionDate, importedDate;

	private Long omeroId;

	// where
	// sizeX and sizeY = micron per pixel on axis
	// sizeZ = depth
	// sizeC = channels
	// sizeT = seconds per frames

	public OmegaImage(final String name, final OmegaExperimenter experimenter,
			final Date acquisitionDate, final Date importedDate) {
		super(-1L, name);
		this.omeroId = -1L;
		this.acquisitionDate = acquisitionDate;
		this.importedDate = importedDate;

		this.datasets = new ArrayList<OmegaDataset>();

		this.experimenter = experimenter;

		this.pixelsList = new ArrayList<OmegaImagePixels>();

		this.analysisRuns = new ArrayList<OmegaAnalysisRun>();
	}

	public OmegaImage(final String name, final OmegaExperimenter experimenter,
			final Date acquisitionDate, final Date importedDate,
			final List<OmegaImagePixels> pixelsList) {
		super(-1L, name);
		this.omeroId = -1L;
		this.acquisitionDate = acquisitionDate;
		this.importedDate = importedDate;

		this.datasets = new ArrayList<OmegaDataset>();

		this.experimenter = experimenter;

		this.pixelsList = pixelsList;

		this.analysisRuns = new ArrayList<OmegaAnalysisRun>();
	}

	public Date getAcquisitionDate() {
		return this.acquisitionDate;
	}

	public Date getImportedDate() {
		return this.importedDate;
	}

	public void addParentDataset(final OmegaDataset dataset) {
		this.datasets.add(dataset);
	}

	public List<OmegaDataset> getParentDatasets() {
		return this.datasets;
	}

	public OmegaExperimenter getExperimenter() {
		return this.experimenter;
	}

	public List<OmegaImagePixels> getPixels() {
		return this.pixelsList;
	}

	public OmegaImagePixels getDefaultPixels() {
		final OmegaImagePixels defaultPixels = this.getPixels(0);
		if (defaultPixels == null) {
			// TODO throw error
		}
		return this.getPixels(0);
	}

	public OmegaImagePixels getPixels(final int index) {
		return this.pixelsList.get(index);
	}

	public boolean containsPixels(final Long id, final boolean gatewayId) {
		for (final OmegaImagePixels pixels : this.pixelsList) {
			if (!gatewayId) {
				if (pixels.getElementID() == id)
					return true;
			} else {
				if (pixels.getOmeroId() == id)
					return true;
			}
		}
		return false;
	}

	public void addPixels(final OmegaImagePixels pixels) {
		this.pixelsList.add(pixels);
	}

	@Override
	public List<OmegaAnalysisRun> getAnalysisRuns() {
		return this.analysisRuns;
	}

	@Override
	public void addAnalysisRun(final OmegaAnalysisRun analysisRun) {
		this.analysisRuns.add(analysisRun);
	}

	@Override
	public void removeAnalysisRun(final OmegaAnalysisRun analysisRun) {
		this.analysisRuns.remove(analysisRun);
	}

	@Override
	public boolean containsAnalysisRun(final long id) {
		for (final OmegaAnalysisRun analysisRun : this.analysisRuns) {
			if (analysisRun.getElementID() == id)
				return true;
		}
		return false;
	}

	@Override
	public void setOmeroId(final Long omeroId) {
		this.omeroId = omeroId;
	}

	@Override
	public Long getOmeroId() {
		return this.omeroId;
	}

	public void changeExperimenter(final OmegaExperimenter experimenter) {
		this.experimenter = experimenter;
	}

	public static String getStaticDisplayName() {
		return OmegaImage.DISPLAY_NAME;
	}
	
	@Override
	public String getDynamicDisplayName() {
		return OmegaImage.getStaticDisplayName();
	}
	
	@Override
	public OmegaAnalysisRunContainerInterface findSpecificAnalysis(
			final String name, final int parentType) {
		OmegaAnalysisRunContainerInterface parent = null;
		Class<?> clazz;
		switch (parentType) {
			case OmegaDataToolConstants.PARENT_DETECTION:
				clazz = OmegaParticleDetectionRun.class;
				break;
			case OmegaDataToolConstants.PARENT_LINKING:
				clazz = OmegaParticleLinkingRun.class;
				break;
			case OmegaDataToolConstants.PARENT_RELINKING:
				clazz = OmegaTrajectoriesRelinkingRun.class;
				break;
			case OmegaDataToolConstants.PARENT_SEGMENTATION:
				clazz = OmegaTrajectoriesSegmentationRun.class;
				break;
			case OmegaDataToolConstants.PARENT_SNR:
				clazz = OmegaSNRRun.class;
				break;
			default:
				clazz = null;
		}
		if (clazz == null)
			return parent;
		for (final OmegaAnalysisRun analysis : this.analysisRuns) {
			if (analysis.getName().equals(name)) {
				parent = analysis;
			} else {
				parent = analysis.findSpecificAnalysis(name, parentType);
			}
			if (parent != null) {
				break;
			}
		}
		return parent;
	}
}
