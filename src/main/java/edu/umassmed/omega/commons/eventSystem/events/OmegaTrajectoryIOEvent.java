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

import edu.umassmed.omega.commons.utilities.OmegaIOUtility;

public class OmegaTrajectoryIOEvent {
	public static int INPUT = 0;
	public static int OUTPUT = 1;
	
	private final int eventType;
	
	private final OmegaIOUtility source;

	private final boolean completeChainAfterImport;
	
	public OmegaTrajectoryIOEvent(final int eventType,
			final boolean completeChainAfterImport) {
		this(null, eventType, completeChainAfterImport);
	}
	
	public OmegaTrajectoryIOEvent(final OmegaIOUtility source,
			final int eventType, final boolean completeChainAfterImport) {
		this.source = source;
		this.eventType = eventType;

		this.completeChainAfterImport = completeChainAfterImport;
	}
	
	public OmegaIOUtility getSource() {
		return this.source;
	}
	
	public int getEventType() {
		return this.eventType;
	}

	public boolean hasCompleteChainAfterImport() {
		return this.completeChainAfterImport;
	}
}
