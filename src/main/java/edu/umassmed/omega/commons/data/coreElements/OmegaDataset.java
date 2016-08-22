/*******************************************************************************
 * Copyright (C) 2014 University of Massachusetts Medical School
 * Alessandro Rigano (Program in Molecular Medicine)
 * Caterina Strambio De Castillia (Program in Molecular Medicine)
 *
 * Created by the Open Microscopy Environment inteGrated Analysis (OMEGA) team:
 * Alex Rigano, Caterina Strambio De Castillia, Jasmine Clark, Vanni Galli,
 * Raffaello Giulietti, Loris Grossi, Eric Hunter, Tiziano Leidi, Jeremy Luban,
 * Ivo Sbalzarini and Mario Valle.
 *
 * Key contacts:
 * Caterina Strambio De Castillia: caterina.strambio@umassmed.edu
 * Alex Rigano: alex.rigano@umassmed.edu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package edu.umassmed.omega.commons.data.coreElements;

import java.util.ArrayList;
import java.util.List;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainer;

public class OmegaDataset extends OmegaNamedElement implements OmeroElement,
        OmegaAnalysisRunContainer {

	private OmegaProject project;

	private final List<OmegaImage> images;

	private final List<OmegaAnalysisRun> analysisRuns;

	private Long omeroId;

	public OmegaDataset(final String name) {
		super(-1L, name);
		this.omeroId = -1L;
		this.project = null;

		this.images = new ArrayList<OmegaImage>();
		this.analysisRuns = new ArrayList<OmegaAnalysisRun>();
	}

	public OmegaDataset(final String name, final List<OmegaImage> images) {
		super(-1L, name);
		this.omeroId = -1L;
		this.project = null;

		this.images = images;
		this.analysisRuns = new ArrayList<OmegaAnalysisRun>();
	}

	public void setParentProject(final OmegaProject project) {
		this.project = project;
	}

	public OmegaProject getParentProject() {
		return this.project;
	}

	public List<OmegaImage> getImages() {
		return this.images;
	}

	public boolean containsImage(final Long id, final boolean gatewayId) {
		for (final OmegaImage image : this.images) {
			if (!gatewayId) {
				if (image.getElementID() == id)
					return true;
			} else {
				if (image.getOmeroId() == id)
					return true;
			}
		}
		return false;
	}

	public void addImage(final OmegaImage image) {
		this.images.add(image);
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
}
