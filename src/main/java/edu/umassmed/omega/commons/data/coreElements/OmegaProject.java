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

public class OmegaProject extends OmegaNamedElement implements OmeroElement,
OmegaAnalysisRunContainer {

	private final List<OmegaDataset> datasets;

	private final List<OmegaAnalysisRun> analysisRuns;

	private Long omeroId;

	public OmegaProject(final String name) {
		super(-1L, name);
		this.omeroId = -1L;
		this.datasets = new ArrayList<OmegaDataset>();
		this.analysisRuns = new ArrayList<OmegaAnalysisRun>();
	}

	public OmegaProject(final String name, final List<OmegaDataset> datasets) {
		super(-1L, name);
		this.omeroId = -1L;
		this.datasets = datasets;
		this.analysisRuns = new ArrayList<OmegaAnalysisRun>();
	}

	public List<OmegaDataset> getDatasets() {
		return this.datasets;
	}

	public boolean containsDataset(final long id, final boolean gatewayId) {
		for (final OmegaDataset dataset : this.datasets) {
			if (gatewayId) {
				if (dataset.getOmeroId() == id)
					return true;
			} else {
				if (dataset.getElementID() == id)
					return true;
			}
		}
		return false;
	}

	public void addDataset(final OmegaDataset dataset) {
		this.datasets.add(dataset);
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
