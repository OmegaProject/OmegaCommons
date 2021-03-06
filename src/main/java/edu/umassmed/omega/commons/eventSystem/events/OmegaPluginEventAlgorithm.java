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

import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParameter;
import edu.umassmed.omega.commons.data.coreElements.OmegaElement;
import edu.umassmed.omega.commons.pluginArchetypes.OmegaPluginArchetype;

public class OmegaPluginEventAlgorithm extends OmegaPluginEvent {

	private final List<OmegaElement> selections;
	private final OmegaElement element;
	private final List<OmegaParameter> params;

	public OmegaPluginEventAlgorithm(final List<OmegaElement> selections,
			final OmegaElement element, final List<OmegaParameter> params) {
		this(null, selections, element, params);
	}

	public OmegaPluginEventAlgorithm(final OmegaPluginArchetype source,
	        final List<OmegaElement> selections, final OmegaElement element,
			final List<OmegaParameter> params) {
		super(source);

		this.selections = selections;
		
		this.element = element;

		this.params = params;
	}
	
	public List<OmegaElement> getSelections() {
		return this.selections;
	}

	public OmegaElement getElement() {
		return this.element;
	}

	public List<OmegaParameter> getParameters() {
		return this.params;
	}
}
