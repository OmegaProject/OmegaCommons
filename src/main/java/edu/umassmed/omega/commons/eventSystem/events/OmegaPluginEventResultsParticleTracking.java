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

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParameter;
import edu.umassmed.omega.commons.data.coreElements.OmegaElement;
import edu.umassmed.omega.commons.data.coreElements.OmegaPlane;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.pluginArchetypes.OmegaPluginArchetype;

public class OmegaPluginEventResultsParticleTracking extends
        OmegaPluginEventAlgorithm {
	
	private final Map<OmegaPlane, List<OmegaROI>> resultingParticles;
	private final Map<OmegaROI, Map<String, Object>> resultingParticlesValues;
	private final List<OmegaTrajectory> resultingTrajectories;
	
	public OmegaPluginEventResultsParticleTracking(
	        final List<OmegaElement> selections, final OmegaElement element,
	        final List<OmegaParameter> params,
	        final Map<OmegaPlane, List<OmegaROI>> resultingParticles,
	        final List<OmegaTrajectory> resultingTrajectories,
			final Map<OmegaROI, Map<String, Object>> resultingParticlesValues) {
		this(null, selections, element, params, resultingParticles,
		        resultingTrajectories, resultingParticlesValues);
	}
	
	public OmegaPluginEventResultsParticleTracking(final OmegaPluginArchetype source,
	        final List<OmegaElement> selections, final OmegaElement element,
	        final List<OmegaParameter> params,
	        final Map<OmegaPlane, List<OmegaROI>> resultingParticles,
	        final List<OmegaTrajectory> resultingTrajectories,
			final Map<OmegaROI, Map<String, Object>> resultingParticlesValues) {
		super(source, selections, element, params);
		
		this.resultingParticles = resultingParticles;
		this.resultingParticlesValues = resultingParticlesValues;
		this.resultingTrajectories = resultingTrajectories;
	}
	
	public Map<OmegaPlane, List<OmegaROI>> getResultingParticles() {
		return this.resultingParticles;
	}
	
	public Map<OmegaROI, Map<String, Object>> getResultingParticlesValues() {
		return this.resultingParticlesValues;
	}
	
	public List<OmegaTrajectory> getResultingTrajectories() {
		return this.resultingTrajectories;
	}
}
