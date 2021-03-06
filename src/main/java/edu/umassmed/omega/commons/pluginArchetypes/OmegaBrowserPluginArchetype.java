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
package edu.umassmed.omega.commons.pluginArchetypes;

import java.util.List;

import edu.umassmed.omega.commons.data.OmegaData;
import edu.umassmed.omega.commons.data.OmegaLoadedData;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAnalysisRun;
import edu.umassmed.omega.commons.pluginArchetypes.interfaces.OmegaLoadedAnalysisConsumerPluginInterface;
import edu.umassmed.omega.commons.pluginArchetypes.interfaces.OmegaLoadedDataConsumerPluginInterface;
import edu.umassmed.omega.commons.pluginArchetypes.interfaces.OmegaMainDataConsumerPluginInterface;
import edu.umassmed.omega.commons.pluginArchetypes.interfaces.OmegaTracksExporterConsumerPluginInterface;
import edu.umassmed.omega.commons.pluginArchetypes.interfaces.OmegaTracksImporterConsumerPluginInterface;
import edu.umassmed.omega.commons.trajectoryTool.OmegaTracksExporter;
import edu.umassmed.omega.commons.trajectoryTool.OmegaTracksImporter;

public abstract class OmegaBrowserPluginArchetype extends OmegaPluginArchetype implements
		OmegaMainDataConsumerPluginInterface,
		OmegaLoadedDataConsumerPluginInterface,
		OmegaLoadedAnalysisConsumerPluginInterface,
		OmegaTracksImporterConsumerPluginInterface,
		OmegaTracksExporterConsumerPluginInterface {
	
	private OmegaData omegaData;
	private OmegaLoadedData loadedData;
	private List<OmegaAnalysisRun> loadedAnalysisRuns;
	
	private OmegaTracksImporter tracksImporter;
	private OmegaTracksExporter tracksExporter;
	
	public OmegaBrowserPluginArchetype() {
		this(1);
	}
	
	public OmegaBrowserPluginArchetype(final int maxNumOfPanels) {
		super(maxNumOfPanels);
		
		this.omegaData = null;
		this.loadedData = null;
		this.loadedAnalysisRuns = null;
	}
	
	@Override
	public void setLoadedData(final OmegaLoadedData loadedData) {
		this.loadedData = loadedData;
	}
	
	@Override
	public OmegaLoadedData getLoadedData() {
		return this.loadedData;
	}
	
	@Override
	public void setLoadedAnalysisRun(
			final List<OmegaAnalysisRun> loadedAnalysisRuns) {
		this.loadedAnalysisRuns = loadedAnalysisRuns;
	}
	
	@Override
	public List<OmegaAnalysisRun> getLoadedAnalysisRuns() {
		return this.loadedAnalysisRuns;
	}
	
	@Override
	public void setMainData(final OmegaData omegaData) {
		this.omegaData = omegaData;
	}
	
	@Override
	public OmegaData getMainData() {
		return this.omegaData;
	}
	
	@Override
	public void setTracksImporter(final OmegaTracksImporter tracksImporter) {
		this.tracksImporter = tracksImporter;
	}
	
	@Override
	public OmegaTracksImporter getTracksImporter() {
		return this.tracksImporter;
	}
	
	@Override
	public void setTracksExporter(final OmegaTracksExporter tracksExporter) {
		this.tracksExporter = tracksExporter;
	}
	
	@Override
	public OmegaTracksExporter getTracksExporter() {
		return this.tracksExporter;
	}
}
