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
import java.util.Calendar;
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

public class OmegaPlane extends OmegaElement implements
		OmegaAnalysisRunContainerInterface {

	private static String DISPLAY_NAME = "Plane";

	private OmegaImagePixels pixels;

	private final Integer index;

	private final List<OmegaAnalysisRun> analysisRuns;

	private Integer zPlane, channel;
	// TODO needed?
	private final Date timeStamps;

	public OmegaPlane(final Integer index) {
		super(-1L);

		this.pixels = null;

		this.index = index;

		this.zPlane = -1;
		this.channel = -1;

		this.timeStamps = Calendar.getInstance().getTime();

		this.analysisRuns = new ArrayList<OmegaAnalysisRun>();
	}

	public OmegaPlane(final Integer index, final Integer channel,
			final Integer zPlane) {
		super(-1L);

		this.pixels = null;

		this.index = index;

		this.zPlane = zPlane;
		this.channel = channel;

		this.timeStamps = Calendar.getInstance().getTime();

		this.analysisRuns = new ArrayList<OmegaAnalysisRun>();
	}

	public OmegaPlane(final Long elementID, final Integer index,
			final Integer channel, final List<OmegaAnalysisRun> analysisRuns) {
		super(elementID);

		this.pixels = null;

		this.index = index;

		this.channel = channel;

		this.timeStamps = Calendar.getInstance().getTime();

		this.analysisRuns = new ArrayList<OmegaAnalysisRun>();
	}

	public void setParentPixels(final OmegaImagePixels pixels) {
		this.pixels = pixels;
	}

	public OmegaImagePixels getParentPixels() {
		return this.pixels;
	}

	public Integer getIndex() {
		return this.index;
	}

	public Integer getChannel() {
		return this.channel;
	}

	public void setChannel(final Integer channel) {
		this.channel = channel;
	}

	public Integer getZPlane() {
		return this.zPlane;
	}

	public void setZPlane(final Integer zPlane) {
		this.zPlane = zPlane;
	}

	public Date getTimeStamps() {
		return this.timeStamps;
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
	
	public static String getStaticDisplayName() {
		return OmegaPlane.DISPLAY_NAME;
	}
	
	@Override
	public String getDynamicDisplayName() {
		return OmegaPlane.getStaticDisplayName();
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
