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
package edu.umassmed.omega.commons.eventSystem.events;

import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRunContainerInterface;
import edu.umassmed.omega.commons.data.coreElements.OmegaPlane;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.utilities.OmegaIOUtility;

public class OmegaImporterEventResultsGeneralParticleDetection extends
		OmegaTrajectoryIOEvent {
	
	private final OmegaAnalysisRunContainerInterface container;

	private final Map<String, String> analysisData, paramData;

	private final Map<OmegaPlane, List<OmegaROI>> resultingParticles;
	private final Map<OmegaROI, Map<String, Object>> resultingParticlesValues;
	
	public OmegaImporterEventResultsGeneralParticleDetection(
			final OmegaIOUtility source,
			final OmegaAnalysisRunContainerInterface container,
			final Map<String, String> analysisData,
			final Map<String, String> paramData,
			final Map<OmegaPlane, List<OmegaROI>> resultingParticles,
			final Map<OmegaROI, Map<String, Object>> resultingParticlesValues,
			final boolean completeChainAfterImport) {
		super(source, OmegaTrajectoryIOEvent.INPUT, completeChainAfterImport);
		this.container = container;
		this.analysisData = analysisData;
		this.paramData = paramData;
		this.resultingParticles = resultingParticles;
		this.resultingParticlesValues = resultingParticlesValues;
	}
	
	public OmegaAnalysisRunContainerInterface getContainer() {
		return this.container;
	}
	
	public Map<String, String> getAnalysisData() {
		return this.analysisData;
	}
	
	public Map<String, String> getParamData() {
		return this.paramData;
	}
	
	public Map<OmegaPlane, List<OmegaROI>> getResultingParticles() {
		return this.resultingParticles;
	}
	
	public Map<OmegaROI, Map<String, Object>> getResultingParticlesValues() {
		return this.resultingParticlesValues;
	}
}
